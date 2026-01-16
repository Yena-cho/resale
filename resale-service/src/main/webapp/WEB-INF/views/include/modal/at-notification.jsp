<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<s:authentication property="principal.name" var="chaName"/>

<jsp:include page="/WEB-INF/views/include/popHeader.jsp"/>

<script type="text/javascript">
    var atCurrentPage = 1;

    var sendAtHpList = [];
    var telNumIdx = 0;

    var atCusInfoList = [];

    var atHpListTotCnt = 0;             // 총 데이터 수
    var atDataPerPage = 10;             // 한 페이지에 나타낼 데이터 수
    var atPageCount = 5;                // 한 화면에 나타낼 페이지 수
    var atSlectedPage = 1;             // 현재 페이지

    $(document).ready(function () {
        $("#closeAtModal").on("click", function () {
            fn_atInit();
        });

        $("#atRow-th").on("click", function () {
            if ($(this).prop('checked')) {
                $('input:checkbox[name=atCheck]').prop('checked', true);
            } else {
                $('input:checkbox[name=atCheck]').prop('checked', false);
            }
        });
    });

    function fn_atInit() {
        sendAtHpList = [];
        sendAtHpList.list = [];
        telNumIdx = 0;

        $("#tabSendAt a:first").tab("show");
    };

    function fn_atNotification(str) {
        var checkbox = $('input[name=checkList]').is(':checked');
        atCusInfoList = [];

        if (checkbox == false) {
            atCusInfoList = [];
        } else {
            $("input[name=checkList]:checked").each(function () {
                atCusInfoList.push($(this).val());
            });
        }

        var url = "/org/notiMgmt/selAtRecNo";
        var param = {
            strList: atCusInfoList
        };

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (data) {
                sendAtHpList = data;

                if (data.list == null) {
                    sendAtHpList.list = [];
                }

                $('#atChaName').val(data.map.chaName);
                $('#atChaTelNo').val(data.map.chaTelNo);
                fn_atCusHpGrid(data, 'cusReqAtList');
                fn_paging(atHpListTotCnt, atDataPerPage, atPageCount, 1);
                fn_setSendAt('at-claim');
            }
        });

        $('#atStartDt').val(getFormatCurrentDate());
        $('#atEndDt').val(getFormatCurrentDate());

        $('#at-notification').modal({backdrop: 'static', keyboard: false});
    }

    function fn_atCusHpGrid(result, obj) {
        var str = '';
        var gridList = result.list;
        atHpListTotCnt = result.list.length;

        if (atHpListTotCnt > 0) {
            gridList = gridList.slice(((atSlectedPage * atDataPerPage) - 10), ((atSlectedPage * atDataPerPage) - 0));

            $.each(gridList, function (k, v) {
                str += '<tr>';
                str += '<input type="hidden" id="atCusHp" value="' + v.cusHp + '">';
                str += '<td><input class="form-check-input table-check-child" type="checkbox" name="atCheck" id="at-row-' + k + '" value="' + v.notiMasCd + '">';
                str += '<label for="at-row-' + k + '"><span></span></label></td>';
                str += '<td>' + v.cusHp + '</td>';
                str += '<td>' + basicEscape(v.cusName) + '</td>';
                str += '</tr>';
            });
        } else {
            str += '<tr>';
            str += '<td colspan="3">[조회된 내역이 없습니다.]</td>';
            str += '</tr>';
        }

        $('#' + obj).html(str);
        $('#atHpListTotCnt').html('');
        $('#atHpListTotCnt').html(atHpListTotCnt);
    }

    function fn_paging(atHpListTotCnt, atDataPerPage, atPageCount, atCurrentPage) {
        var totalPage = Math.ceil(atHpListTotCnt / atDataPerPage);                        // 총 페이지 수
        var pageGroup = Math.ceil(atCurrentPage / atPageCount);                           // 페이지 그룹
        var last = pageGroup * atPageCount;                                               // 화면에 보여질 마지막 페이지 번호
        if (last > totalPage) last = totalPage;
        var first = 1 + (atPageCount * (pageGroup - 1)) <= 0 ? 1 : 1 + (atPageCount * (pageGroup - 1));            // 화면에 보여질 첫번째 페이지 번호
        var prev = first - 1 < 0 ? 1 : first - 1;
        var next = last + 1;

        var html = "";

        if (prev > 0) {
            html += "<li class='page-item'><a class='page-link' href='#' aria-label='Previous' id ='prev'><span aria-hidden='true'><img src='/assets/imgs/common/icon-pagenation-single-arrow.png'></span><span class='sr-only'>Previous</span></a></li>";
        }

        for (var i = first; i <= last; i++) {
            html += '<li class="page-item"><a class="page-link" href="#" id="' + i + '">' + i + '</a></li>';
        }

        if (last < totalPage) {
            html += '<li class="page-item"><a class="page-link" href="#" aria-label="Next" id="next"><span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-double-arrow.png" class="rotate-180-deg"></span><span class="sr-only">Move to last page</span></a></li>';
        }

        $("#at-paging").html(html);

        $("#at-paging a").click(function () {
            var $item = $(this);
            var $id = $item.attr("id");
            atSlectedPage = $item.text();

            if ($id == "next") {
                atSlectedPage = next;
            } else if ($id == "prev") {
                atSlectedPage = prev
            } else {
                $("#atCurrentPage").val($id);
                atCurrentPage = $("#atCurrentPage").val();
            }

            // 전체 데이터 수, 한 페이지에 나타날 데이터 수, 한 화면에 나타낼 페이지 수, 이동 할 페이지 번호
            fn_atCusHpGrid(sendAtHpList, 'cusReqAtList');
            fn_paging(atHpListTotCnt, atDataPerPage, atPageCount, atSlectedPage);
        });

        $("#at-paging a#" + atCurrentPage).closest('li').addClass('active');
    }

    var claimMsg = [];
    var unpaidMsg = [];
    var depositMsg = [];

    var templateCode = '';
    var msgType = '';

    function fn_setSendAt(str) {
        var url = "/org/notiMgmt/setAtMsg";
        var param = {
        };

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (data) {
                claimMsg = data.claimMsg;
                unpaidMsg = data.unpaidMsg;
                depositMsg = data.depositMsg;

                if (str == 'at-claim') {
                    $("#at-title").html("청구 발송 내용");

                    $("#at-icon-bill").attr("src", "/assets/imgs/collecter/icon-bill.png");
                    $("#at-icon-won-coin").attr("src", "/assets/imgs/collecter/icon-won-mark-in-coin-greyscale.png");
                    $("#at-icon-won-paper").attr("src", "/assets/imgs/collecter/icon-won-mark-paper-greyscale.png");
                    $("#at-icon-document-2").attr("src", "/assets/imgs/collecter/icon-document-2-greyscale.png");
                    $("#at-icon-document-3").attr("src", "/assets/imgs/collecter/icon-document-3-greyscale.png");
                    $("#at-icon-pencil").attr("src", "/assets/imgs/collecter/icon-pencil-on-bottom-greyscale.png");

                    $("#at-content").html(claimMsg.sendMsg);
                    templateCode = claimMsg.templateCode;
                    msgType = claimMsg.msgType;
                } else if (str == 'at-unpaid') {
                    $("#at-title").html("미납 발송 내용");

                    $("#at-icon-bill").attr("src", "/assets/imgs/collecter/icon-bill-greyscale.png");
                    $("#at-icon-won-coin").attr("src", "/assets/imgs/collecter/icon-won-mark-in-coin-greyscale.png");
                    $("#at-icon-won-paper").attr("src", "/assets/imgs/collecter/icon-won-mark-paper.png");
                    $("#at-icon-document-2").attr("src", "/assets/imgs/collecter/icon-document-2-greyscale.png");
                    $("#at-icon-document-3").attr("src", "/assets/imgs/collecter/icon-document-3-greyscale.png");
                    $("#at-icon-pencil").attr("src", "/assets/imgs/collecter/icon-pencil-on-bottom-greyscale.png");

                    $("#at-content").html(unpaidMsg.sendMsg);
                    templateCode = unpaidMsg.templateCode;
                    msgType = unpaidMsg.msgType;
                } else if (str == 'at-acc') {
                    $("#at-title").html("입금 발송 내용");

                    $("#at-icon-bill").attr("src", "/assets/imgs/collecter/icon-bill-greyscale.png");
                    $("#at-icon-won-coin").attr("src", "/assets/imgs/collecter/icon-won-mark-in-coin.png");
                    $("#at-icon-won-paper").attr("src", "/assets/imgs/collecter/icon-won-mark-paper-greyscale.png");
                    $("#at-icon-document-2").attr("src", "/assets/imgs/collecter/icon-document-2-greyscale.png");
                    $("#at-icon-document-3").attr("src", "/assets/imgs/collecter/icon-document-3-greyscale.png");
                    $("#at-icon-pencil").attr("src", "/assets/imgs/collecter/icon-pencil-on-bottom-greyscale.png");

                    $("#at-content").html(depositMsg.sendMsg);
                    templateCode = depositMsg.templateCode;
                    msgType = depositMsg.msgType;
                }
            }, error: function (data) {
                swal({
                    type: 'error',
                    text: "알림톡 문구 호출에 실패하였습니다.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
            }
        });
    }

    function fn_atHpNoDel() {
        var rowCnt = 0;
        var checkbox = $("input[name='atCheck']:checked");
        var delMsgContactList = [];
        checkbox.each(function (i) {
            rowCnt++;
        });

        if (rowCnt == 0) {
            swal({
                type: 'info',
                text: "삭제할 전화번호를 선택해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        swal({
            type: 'question',
            html: "삭제하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                checkbox.each(function (i, v) {
                    delMsgContactList.push(v.value);
                });

                var sendTelCnt = sendAtHpList.list.length;
                var sendTempList = sendAtHpList.list;

                for (var i = 0; i < sendTelCnt; i++) {
                    if (sendAtHpList.list[i]) {
                        var sendTelId = sendAtHpList.list[i].notiMasCd;

                        for (var j = 0; j < delMsgContactList.length; j++) {
                            if (delMsgContactList[j] === sendTelId) {
                                sendTempList.splice(i, 1);
                                i--;
                            }
                        }
                    } else {
                        break;
                    }
                }

                swal({
                    type: 'success',
                    text: "삭제하였습니다.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });

                atSlectedPage = 1;
                fn_atCusHpGrid(sendAtHpList, 'cusReqAtList');
                fn_paging(sendAtHpList.list.length, atDataPerPage, atPageCount, 1);
            }
        });

        $("#atRow-th").prop("checked", false);
    }

    // 전체삭제
    function fn_atHpNoDelAll() {
        if (sendAtHpList == null) {
            swal({
                type: 'info',
                text: "삭제할 수신번호가 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
        } else {
            swal({
                type: 'question',
                html: "전체삭제하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                reverseButtons: true
            }).then(function (result) {
                if (result.value) {
                    sendAtHpList = [];
                    sendAtHpList.list = [];

                    atSlectedPage = 1;
                    fn_atCusHpGrid(sendAtHpList, 'cusReqAtList');
                    fn_paging(sendAtHpList.list.length, atDataPerPage, atPageCount, 1);

                    $('#telNo').val('');

                    swal({
                        type: 'success',
                        text: "삭제하였습니다.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            });

            $("#atRow-th").prop("checked", false);
        }
    }

    function fn_atSend() {
        var gridList = sendAtHpList.list;
        var atCusInfoList = [];
        var notiMasCdList = [];

        if (gridList.length > 0) {
            $.each(gridList, function (k, v) {
                atCusInfoList.push(v.cusHp);
                notiMasCdList.push(v.notiMasCd);
            });

            swal({
                type: 'question',
                html: "알림톡을 전송 하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                reverseButtons: true
            }).then(function (result) {
                if (result.value) {
                    var url = "/org/notiMgmt/atMsgSend";
                    var param = {
                        writer: $('#atChaName').val(),
                        sendPhone: $('#atChaTelNo').val(),
                        contents: $("#at-content").html(),
                        atCusInfoList: atCusInfoList,
                        notiMasCdList: notiMasCdList,
                        templateCode: templateCode,
                        msgType: msgType
                    };

                    $.ajax({
                        type: "post",
                        async: true,
                        url: url,
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify(param),
                        success: function (data) {
                            console.log("data : ", data);

                            if (data.retCode == '0000') {
                                swal({
                                    type: 'success',
                                    text: "알림톡 전송에 성공하였습니다.",
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                });
                            } else {
                                swal({
                                    type: 'error',
                                    text: "알림톡 전송에 실패하였습니다.",
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                });
                            }
                        },
                        error: function (data) {
                            swal({
                                type: 'error',
                                text: "알림톡 전송에 실패하였습니다.",
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        }
                    });
                }
            });
        } else {
            swal({
                type: 'info',
                text: "알림톡을 발송할 전화번호가 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
        }
    }

    // 조회
    var atSendListPage = 1; // 청구목록 현재페이지

    function fn_selAtList(page) {
        if (page == null || page == 'undefined') {
            atSendListPage = '1';
        } else {
            atSendListPage = page;
        }

        var stDt = $('#atStartDt').val().replace(/\./gi, "");
        var edDt = $('#atEndDt').val().replace(/\./gi, "");

        if ((!stDt || !edDt) || (stDt.length < 8 || edDt.length < 8) || (stDt > edDt)) {
            swal({
                type: 'info',
                text: "발송일을 확인해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var url = "/org/notiMgmt/sendAtList";
        var param = {
            curPage: atSendListPage,
            stDt: stDt,
            edDt: edDt,
            selGubn: $('#at-selGubn option:selected').val(),
            keyword: $('#at-keyword').val(),
            search_orderBy: $('#searchOrderByAt option:selected').val(),
            status: $('input[name=atStatus]:checked').val(),
            msgType: $('input[name=atSendSt]:checked').val()
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (data) {
                $('#sendAtMsgTotCnt').html('');
                $('#sendAtMsgTotCnt').html(data.count);
                fn_atSendGrid(data, 'sendAtGrid');
                ajaxModalPaging(data, 'atModalPageArea'); //modal paging
            }
        });
    }

    function fn_atSendGrid(data, obj) {
        var str = '';

        if (data.count > 0) {
            $.each(data.list, function (k, v) {
                var dateFormat = moment(v.sendDate, 'YYYY-MM-DD HH:mm:ss').format('YYYY.MM.DD HH:mm:ss');

                str += '<tr>';
                str += '<td>' + dateFormat + '</td>';
                str += '<td>' + v.msgType + '</td>';
                str += '<td title="' + basicEscape(v.cusName) + '" class="has-ellipsis">' + basicEscape(v.cusName) + '</td>';
                str += '<td>' + v.cusTelNo + '</td>';
                str += '<td title="' + basicEscape(v.sendMsg) + '" class="has-ellipsis">' + basicEscape(v.sendMsg) + '</td>';
                if (v.sendStatusCd == '발송실패') {
                    str += '<td title="' + basicEscape(v.description) + '" class="has-ellipsis">' + v.sendStatusCd + '</td>';
                } else {
                    str += '<td class="has-ellipsis">' + v.sendStatusCd + '</td>';
                }

                str += '</tr>';
            });
        } else {
            str += '<tr>';
            str += '<td colspan="6">[조회된 내역이 없습니다.]</td>';
            str += '</tr>';
        }

        $('#' + obj).html(str);
    }

    <%--function atExeclDownload() {--%>
    <%--    if ($('#sendAtMsgTotCnt').text() == 0) {--%>
    <%--        swal({--%>
    <%--            type: 'info',--%>
    <%--            text: '다운로드할 데이터가 없습니다.',--%>
    <%--            confirmButtonColor: '#3085d6',--%>
    <%--            confirmButtonText: '확인'--%>
    <%--        });--%>
    <%--        return;--%>
    <%--    }--%>

    <%--    swal({--%>
    <%--        type: 'question',--%>
    <%--        html: "다운로드 하시겠습니까?",--%>
    <%--        showCancelButton: true,--%>
    <%--        confirmButtonColor: '#3085d6',--%>
    <%--        cancelButtonColor: '#d33',--%>
    <%--        confirmButtonText: '확인',--%>
    <%--        cancelButtonText: '취소',--%>
    <%--        reverseButtons: true--%>
    <%--    }).then(function (result) {--%>
    <%--        if (result.value) {--%>
    <%--            $('#excelStartDt').val($('#atStartDt').val().replace(/\./gi, ""));--%>
    <%--            $('#excelEndDt').val($('#atEndDt').val().replace(/\./gi, ""));--%>
    <%--            $('#excelGubun').val($('#at-selGubn option:selected').val());--%>
    <%--            $('#excelat-keyword').val($('#at-keyword').val());--%>
    <%--            $('#excelOrderBy').val($('#searchOrderByAt option:selected').val());--%>
    <%--            $('#excelatStatus').val($('input[name=atStatus]:checked').val());--%>

    <%--            // 다운로드--%>
    <%--            document.excelForm.action = "/org/notiMgmt/excelSendSmsListDownload";--%>
    <%--            document.excelForm.submit();--%>
    <%--        }--%>
    <%--    });--%>
    <%--}--%>


    // function atExeclDownload() {
    //     alert("엑셀파일 다운로드");
    // }
</script>

<input type="hidden" id="atCurrentPage" name="atCurrentPage" value="1"/>

<%--<form id="excelForm" name="excelForm" method="post">--%>
<%--    <input type="hidden" id="excelStartDt" name="excelStartDt"/>--%>
<%--    <input type="hidden" id="excelEndDt" name="excelEndDt"/>--%>
<%--    <input type="hidden" id="excelGubun" name="excelGubun"/>--%>
<%--    <input type="hidden" id="excelat-keyword" name="excelat-keyword"/>--%>
<%--    <input type="hidden" id="excelOrderBy" name="excelOrderBy"/>--%>
<%--    <input type="hidden" id="excelatStatus" name="excelatStatus"/>--%>
<%--</form>--%>

<div class="modal fade" id="at-notification" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">알림톡고지</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true" onclick="fn_atInit();">&times;</span></button>
            </div>

            <div class="modal-body">
                <div class="container-fluid mb-4">
                    <div class="row">
                        <div class="col">
                            <div class="tab-selecter type-3">
                                <ul class="nav nav-tabs" role="tablist">
                                    <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#send-at">발송</a></li>
                                    <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#sent-atList" onclick="fn_selAtList();">발송내역</a></li>
                                </ul>
                            </div>

                            <div class="tab-content" class="container-fluid">
                                <div id="send-at" class="tab-pane fade show active">
                                    <div class="row">
                                        <div class="col col-sm-2 col-12">
                                            <ul id="tabSendAt" class="nav nav-tabs vertical-tab" role="tablist">
                                                <li class="nav-item">
                                                    <a class="nav-link active" data-toggle="tab" href="#" onclick="fn_setSendAt('at-claim');">
                                                        <img src="/assets/imgs/collecter/icon-bill.png" id="at-icon-bill" class="icon">청구발송
                                                    </a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" data-toggle="tab" href="#" onclick="fn_setSendAt('at-acc');">
                                                        <img src="/assets/imgs/collecter/icon-won-mark-in-coin-greyscale.png" id="at-icon-won-coin" class="icon">입금발송
                                                    </a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" data-toggle="tab" href="#" onclick="fn_setSendAt('at-unpaid');">
                                                        <img src="/assets/imgs/collecter/icon-won-mark-paper-greyscale.png" id="at-icon-won-paper" class="icon">미납발송
                                                    </a>
                                                </li>
                                            </ul>
                                        </div>

                                        <div class="col col-sm-5 col-12">
                                            <div class="mobile-mockup">
                                                <div class="speaker-camera align-items-center">
                                                    <div class="camera"></div>
                                                    <div class="speaker"></div>
                                                </div>
                                                <div class="mobile-screen mb-3">
                                                    <div id="at-title">청구 발송 내용</div>
                                                    <p id="at-content" class="form-control" style="margin: 1.25rem; width: calc(100% - 2.5rem); height: 530px; background-color: #e9ecef;"></p>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col col-sm-5 col-12 left-border">
                                            <div class="search-box mb-3">
                                                <div class="row">
                                                    <label class="col col-4">발송기관명</label>
                                                    <div class="col col-8">
                                                        <input class="form-control" id="atChaName" disabled="true">
                                                        <input class="form-control" id="atChaTelNo" type="hidden">
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row col mb-1">총 [<strong class="text-primary" id="atHpListTotCnt">0</strong>]명</div>

                                            <div class="table-responsive">
                                                <table class="table table-sm table-hover table-primary">
                                                    <thead>
                                                        <tr>
                                                            <th>
                                                                <input class="form-check-input table-check-parents" type="checkbox" id="atRow-th" name="checkAll">
                                                                <label for="atRow-th"><span></span></label>
                                                            </th>
                                                            <th>연락처(수신번호)</th>
                                                            <th>고객명</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="cusReqAtList">
                                                        <tr>
                                                            <td colspan="3">[조회된 내역이 없습니다.]</td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>

                                            <div class="text-left mt-2">
                                                <button type="button" class="btn btn-sm btn-gray-outlined" onclick="fn_atHpNoDel();">선택삭제</button>
                                                <button type="button" class="btn btn-sm btn-gray-outlined" onclick="fn_atHpNoDelAll();">전체삭제</button>
                                            </div>

                                            <div class="row mt-3">
                                                <div class="col text-center">
                                                    <nav aria-label="Page navigation example">
                                                        <ul class="pagination justify-content-center" id="at-paging" style="margin-bottom: 0 !important;"></ul>
                                                    </nav>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row mt-3 mb-3">
                                        <div class="col-12 ars-content">
                                            <ul>
                                                <li class="text-danger">자동입력값은 청구에 연결된 해당 정보가 자동으로 포함되어 메시지가 발송됩니다.</li>
                                                <li class="text-danger">알림톡 발송은 체크박스 선택유무와 관계없이 전체 고객에게 발송됩니다.</li>
                                                <li class="text-danger">제휴사 정책에 의해 알림톡은 지정된 템플릿 발송만 가능하여 고객구분값은 발송이 불가합니다. 고객구분값을 포함하여 발송하고자 하는 경우에는 문자서비스를 이용하시기 바랍니다.</li>
                                            </ul>
                                        </div>
                                    </div>

                                    <div class="row mt-3">
                                        <div class="col text-center">
                                            <button id="closeAtModal" type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>
                                            <button type="button" class="btn btn-primary" onclick="fn_atSend();">알림톡 발송</button>
                                        </div>
                                    </div>
                                </div>

                                <div id="sent-atList" class="tab-pane fade show">
                                    <div class="row mt-3 mb-1">
                                        <div class="col-12">
                                            <div class="search-box">

                                                <form>
                                                    <div class="form-group row">
                                                        <label class="col col-md-1 col-sm-2 col-2 col-form-label">발송일</label>
                                                        <div class="col col-md-5 col-sm-10 col-10 form-inline">
                                                            <div class="date-input">
                                                                <label class="sr-only">From</label>
                                                                <div class="input-group">
                                                                    <input type="text"
                                                                           class="form-control date-picker-input"
                                                                           placeholder="YYYY.MM.DD" id="atStartDt"
                                                                           aria-label="From"
                                                                           aria-describedby="basic-addon2" maxlength="8"
                                                                           onkeydown="onlyNumber(this)">
                                                                </div>
                                                            </div>
                                                            <span class="range-mark"> ~ </span>
                                                            <div class="date-input">
                                                                <label class="sr-only">To</label>
                                                                <div class="input-group">
                                                                    <input type="text"
                                                                           class="form-control date-picker-input"
                                                                           placeholder="YYYY.MM.DD" id="atEndDt"
                                                                           aria-label="To"
                                                                           aria-describedby="basic-addon2" maxlength="8"
                                                                           onkeydown="onlyNumber(this)">
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <label class="col col-md-1 col-sm-2 col-2 col-form-label">유형</label>
                                                        <div class="col col-md-5 col-sm-10 col-10 form-inline">
                                                            <div class="form-inline">
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="radio" name="atSendSt" id="atSendSt1" value="ALL" checked="checked">
                                                                    <label class="form-check-label" for="atSendSt1"><span class="mr-1"></span>전체</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="radio" name="atSendSt" id="atSendSt2" value="0">
                                                                    <label class="form-check-label" for="atSendSt2"><span class="mr-1"></span>청구</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="radio" name="atSendSt" id="atSendSt3" value="1">
                                                                    <label class="form-check-label" for="atSendSt3"><span class="mr-1"></span>미납</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="radio" name="atSendSt" id="atSendSt4" value="2">
                                                                    <label class="form-check-label" for="atSendSt4"><span class="mr-1"></span>입금</label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-form-label col col-md-1 col-sm-3 col-3">발송결과 </label>
                                                        <div class="col col-md-11 col-sm-9 col-9 form-inline">
                                                            <div class="form-inline">
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="radio" name="atStatus" id="atStatus1" value="ALL" checked="checked">
                                                                    <label class="form-check-label" for="atStatus1"><span class="mr-1"></span>전체</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="radio" name="atStatus" id="atStatus2" value="0">
                                                                    <label class="form-check-label" for="atStatus2"><span class="mr-1"></span>발송대기</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="radio" name="atStatus" id="atStatus4" value="1">
                                                                    <label class="form-check-label" for="atStatus4"><span class="mr-1"></span>발송완료</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="radio" name="atStatus" id="atStatus5" value="2">
                                                                    <label class="form-check-label" for="atStatus5"><span class="mr-1"></span>발송실패</label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-form-label col col-md-1 col-sm-3 col-3">검색구분 </label>
                                                        <div class="col col-md-11 col-sm-9 col-9 form-inline">
                                                            <select class="form-control col-auto" id="at-selGubn">
                                                                <option value="cusName">고객명</option>
                                                                <option value="cusTelNo">수신번호</option>
                                                            </select>
                                                            <input class="form-control col-auto" type="text" id="at-keyword" onkeypress="if(event.keyCode == 13){fn_selAtList();}">
                                                        </div>
                                                    </div>

                                                    <div class="row form-inline mt-3">
                                                        <div class="col-12 text-center">
                                                            <button type="button" class="btn btn-primary btn-wide" onclick="fn_selAtList();">조회</button>
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>

                                            <div class="table-option row mb-2">
                                                <div class="col-md-6 col-sm-12 form-inline">전체건수 : [<strong class="text-primary" id="sendAtMsgTotCnt">0</strong>]건</div>
                                                <div class="col-md-6 col-sm-12 text-right mt-1">
                                                    <select class="form-control" name="searchOrderByAt" id="searchOrderByAt" onchange="fn_selAtList();">
                                                        <option value="sendDate">발송일시순 정렬</option>
                                                        <option value="cusName">고객명순 정렬</option>
                                                        <option value="sendStatusCd">발송결과순 정렬</option>
                                                    </select>

                                                    <!-- TODO 엑셀 파일 저장 -->
<%--                                                    <button class="btn btn-sm btn-d-gray hidden-on-mobile" type="button" onclick="atExeclDownload();">파일저장</button>--%>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col table-responsive pd-n-mg-o">
                                            <table class="table table-primary " style="table-layout: fixed;">
                                                <colgroup>
                                                    <col width="160">
                                                    <col width="60">
                                                    <col width="125">
                                                    <col width="120">
                                                    <col width="255">
                                                    <col width="90">
                                                </colgroup>

                                                <thead>
                                                    <tr>
                                                        <th>발송일시</th>
                                                        <th>유형</th>
                                                        <th>고객명</th>
                                                        <th>수신번호</th>
                                                        <th>내용</th>
                                                        <th>발송결과</th>
                                                    </tr>
                                                </thead>

                                                <tbody id="sendAtGrid">
                                                    <tr>
                                                        <td colspan="6">[조회된 내역이 없습니다.]</td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>

                                    <div class="row mt-3">
                                        <div class="col text-center">
                                            <nav aria-label="Page navigation example">
                                                <ul class="pagination justify-content-center" id="atModalPageArea"></ul>
                                            </nav>
                                        </div>
                                    </div>

                                    <div class="col text-center">
                                        <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

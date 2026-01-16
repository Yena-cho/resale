<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<s:authentication property="principal.name" var="chaName"/>

<jsp:include page="/WEB-INF/views/include/popHeader.jsp"/>

<script type="text/javascript">
    var cuPage2 = 1;
    var strList = [];
    
    var sendMsgInfoList = [];
    var gridListTotCnt = 0;         // 총 데이터 수
    var dataPerPage = 10;           // 한 페이지에 나타낼 데이터 수
    var pageCount = 5;              // 한 화면에 나타낼 페이지 수
    var selectedPage = 1;           // 현재 페이지
    var currentPage = 1;

    function paging(gridListTotCnt, dataPerPage, pageCount, currentPage) {
        var totalPage = Math.ceil(gridListTotCnt / dataPerPage);                        // 총 페이지 수
        var pageGroup = Math.ceil(currentPage / pageCount);                             // 페이지 그룹
        var last = pageGroup * pageCount;                                               // 화면에 보여질 마지막 페이지 번호
        if (last > totalPage) last = totalPage;
        var first = 1 + (pageCount * (pageGroup - 1)) <= 0 ? 1 : 1 + (pageCount * (pageGroup - 1));            // 화면에 보여질 첫번째 페이지 번호
        var prev = first - 1 < 0 ? 1 : first - 1;
        var next = last + 1;

        var html = "";

        if (prev > 0) {
            html += "<li class='page-item'><a class='page-link' href='#' aria-label='Previous' id ='prev'><span aria-hidden='true'><img src='/assets/imgs/common/icon-pagenation-single-arrow.png'></span><span class='sr-only'>Previous</span></a></li>";
        }

        for (var i = first; i <= last; i++) {
            html += '<li class="page-item"><a class="page-link" href="#" id="' + i + '">' +  i + '</a></li>';
        }

        if (last < totalPage) {
            html += '<li class="page-item"><a class="page-link" href="#" aria-label="Next" id="next"><span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-double-arrow.png" class="rotate-180-deg"></span><span class="sr-only">Move to last page</span></a></li>';
        }

        $("#paging").html(html);

        $("#paging a").click(function () {
            var $item = $(this);
            var $id = $item.attr("id");
            selectedPage = $item.text();

            if ($id == "next") {
                selectedPage = next;
            } else if ($id == "prev") {
                selectedPage = prev
            } else {
                $("#currentPage").val($id);
                currentPage = $("#currentPage").val();
            }

            // 전체 데이터 수, 한 페이지에 나타날 데이터 수, 한 화면에 나타낼 페이지 수, 이동 할 페이지 번호
            fn_cusGrid(sendMsgInfoList, 'cusReqList');
            paging(gridListTotCnt, dataPerPage, pageCount, selectedPage);
        });

        $("#paging a#" + currentPage).closest('li').addClass('active');
    }

    function fn_smsNotification(str) {
        var checkbox = $('input[name=checkList]').is(':checked');
        strList = [];

        if (checkbox == false) {
            strList = [];
        } else {
            $("input[name=checkList]:checked").each(function() {
                strList.push($(this).val());
            });
        }

        var url = "/org/notiMgmt/smsRecNo";
        var param = {
            strList: strList,
            curPage : cuPage2
        };

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (data) {
                sendMsgInfoList = data;
                if (data.list == null) {
                    sendMsgInfoList.list = [];
                }
                fn_cusGrid(data, 'cusReqList');
                paging(gridListTotCnt, dataPerPage, pageCount, 1);
                fn_sendNo(sendMsgInfoList, 'sendNo'); 		            // 발신번호
                fn_setSendMsg('claim');			                        // 청구발송내용
            }
        });

        $('#selStartDt').val(getFormatCurrentDate());
        $('#selEndDt').val(getFormatCurrentDate());

        $('#sms-notification').modal({backdrop: 'static', keyboard: false});
    }

    function fn_cusGrid(result, obj) {
        var str = '';
        var gridList = result.list;
        gridListTotCnt = result.list.length;

        if (gridListTotCnt > 0) {
            gridList = gridList.slice(((selectedPage * dataPerPage) - 10), ((selectedPage * dataPerPage) - 0));

            $.each(gridList, function (k, v) {
                str += '<tr>';
                str += '<input type="hidden" id="cusHp" value="' + v.cusHp + '">';
                str += '<td><input class="form-check-input table-check-child" type="checkbox" name="smsCheck" id="srow-' + k + '" value="' + v.notiMasCd + '">';
                str += '<label for="srow-' + k + '"><span></span></label></td>';
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
        $('#gridListTotCnt').html('');
        $('#gridListTotCnt').html(gridListTotCnt);
    }

    // 발신번호
    function fn_sendNo(result, obj) {
        var str = '';
        $.each(result.sendList, function (k, v) {
            str += '<option value="' + v.smsSendTel + '">' + v.smsSendTel + '</option>';
        });
        $('#' + obj).html(str);
    }

    // 발송별 문구
    function fn_setSendMsg(str) {
        var sendGb = "notSmsYn";
        if (str == 'claim') { 			// 청구
            sendGb = "notSmsYn";
            $("#sms-text").html("청구 발송 내용");

            $("#icon-bill").attr("src", "/assets/imgs/collecter/icon-bill.png");
            $("#icon-won-coin").attr("src", "/assets/imgs/collecter/icon-won-mark-in-coin-greyscale.png");
            $("#icon-won-paper").attr("src", "/assets/imgs/collecter/icon-won-mark-paper-greyscale.png");
            $("#icon-document-2").attr("src", "/assets/imgs/collecter/icon-document-2-greyscale.png");
            $("#icon-document-3").attr("src", "/assets/imgs/collecter/icon-document-3-greyscale.png");
            $("#icon-pencil").attr("src", "/assets/imgs/collecter/icon-pencil-on-bottom-greyscale.png");

        } else if (str == 'acc') { 		// 입금
            sendGb = "rcpSmsYn";
            $("#sms-text").html("입금 발송 내용");

            $("#icon-bill").attr("src", "/assets/imgs/collecter/icon-bill-greyscale.png");
            $("#icon-won-coin").attr("src", "/assets/imgs/collecter/icon-won-mark-in-coin.png");
            $("#icon-won-paper").attr("src", "/assets/imgs/collecter/icon-won-mark-paper-greyscale.png");
            $("#icon-document-2").attr("src", "/assets/imgs/collecter/icon-document-2-greyscale.png");
            $("#icon-document-3").attr("src", "/assets/imgs/collecter/icon-document-3-greyscale.png");
            $("#icon-pencil").attr("src", "/assets/imgs/collecter/icon-pencil-on-bottom-greyscale.png");

        } else if (str == 'nonpay') { 	// 미납
            sendGb = "notiSms2";
            $("#sms-text").html("미납 발송 내용");

            $("#icon-bill").attr("src", "/assets/imgs/collecter/icon-bill-greyscale.png");
            $("#icon-won-coin").attr("src", "/assets/imgs/collecter/icon-won-mark-in-coin-greyscale.png");
            $("#icon-won-paper").attr("src", "/assets/imgs/collecter/icon-won-mark-paper.png");
            $("#icon-document-2").attr("src", "/assets/imgs/collecter/icon-document-2-greyscale.png");
            $("#icon-document-3").attr("src", "/assets/imgs/collecter/icon-document-3-greyscale.png");
            $("#icon-pencil").attr("src", "/assets/imgs/collecter/icon-pencil-on-bottom-greyscale.png");

        } else if (str == 'default1') { 	// 기본문구1
            sendGb = "smsMsgDef1";
            $("#sms-text").html("기본문구1 내용");

            $("#icon-bill").attr("src", "/assets/imgs/collecter/icon-bill-greyscale.png");
            $("#icon-won-coin").attr("src", "/assets/imgs/collecter/icon-won-mark-in-coin-greyscale.png");
            $("#icon-won-paper").attr("src", "/assets/imgs/collecter/icon-won-mark-paper-greyscale.png");
            $("#icon-document-2").attr("src", "/assets/imgs/collecter/icon-document-2.png");
            $("#icon-document-3").attr("src", "/assets/imgs/collecter/icon-document-3-greyscale.png");
            $("#icon-pencil").attr("src", "/assets/imgs/collecter/icon-pencil-on-bottom-greyscale.png");

        } else if (str == 'default2') { 	// 기본문구2
            sendGb = "smsMsgDef2";
            $("#sms-text").html("기본문구2 내용");

            $("#icon-bill").attr("src", "/assets/imgs/collecter/icon-bill-greyscale.png");
            $("#icon-won-coin").attr("src", "/assets/imgs/collecter/icon-won-mark-in-coin-greyscale.png");
            $("#icon-won-paper").attr("src", "/assets/imgs/collecter/icon-won-mark-paper-greyscale.png");
            $("#icon-document-2").attr("src", "/assets/imgs/collecter/icon-document-2-greyscale.png");
            $("#icon-document-3").attr("src", "/assets/imgs/collecter/icon-document-3.png");
            $("#icon-pencil").attr("src", "/assets/imgs/collecter/icon-pencil-on-bottom-greyscale.png");

        } else if (str == 'direct') { 	// 직접입력1
            sendGb = "";
            $("#sms-text").html("직접입력 내용");

            $("#icon-bill").attr("src", "/assets/imgs/collecter/icon-bill-greyscale.png");
            $("#icon-won-coin").attr("src", "/assets/imgs/collecter/icon-won-mark-in-coin-greyscale.png");
            $("#icon-won-paper").attr("src", "/assets/imgs/collecter/icon-won-mark-paper-greyscale.png");
            $("#icon-document-2").attr("src", "/assets/imgs/collecter/icon-document-2-greyscale.png");
            $("#icon-document-3").attr("src", "/assets/imgs/collecter/icon-document-3-greyscale.png");
            $("#icon-pencil").attr("src", "/assets/imgs/collecter/icon-pencil-on-bottom.png");

        }

        var url = "/org/notiMgmt/selSmsMsg";
        var param = {};

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (data) {
                $('#smsMsg').attr('disabled', true);

                var smsMsg = '';
                if (str == 'claim') { 			// 청구
                    smsMsg = basicUnEscape(nullValueChange(data.msg.notSms));
                    $('#totbyte').html('<em id="smsbyte">0</em>/80');
                } else if (str == 'acc') { 		// 입금
                    smsMsg = basicUnEscape(nullValueChange(data.msg.rcpSms));
                    $('#totbyte').html('<em id="smsbyte">0</em>/80');
                } else if (str == 'nonpay') {	// 미납
                    smsMsg = basicUnEscape(nullValueChange(data.msg.notiSms2));
                    $('#totbyte').html('<em id="smsbyte">0</em>/80');
                } else if (str == 'default1') {	// 기본문구1
                    smsMsg = basicUnEscape(nullValueChange(data.msg.smsMsgDef1));
                    $('#totbyte').html('<em id="smsbyte">0</em>/80');
                } else if (str == 'default2') {	// 기본문구2
                    smsMsg = basicUnEscape(nullValueChange(data.msg.smsMsgDef2));
                    $('#totbyte').html('<em id="smsbyte">0</em>/80');
                } else if (str == 'direct') {	// 직접입력
                    $('#smsMsg').attr('disabled', false);
                }
                var stringByteLength = 0;
                stringByteLength = (function (s, b, i, c) {
                    for (b = i = 0; c = s.charCodeAt(i++); b += c >> 11 ? 2 : c >> 7 ? 1 : 1) ;
                    return b
                })(smsMsg);
                if (stringByteLength < 81) {
                    maxbyte = 80;
                    $('#smsbyte').html(stringByteLength);
                }
                if (stringByteLength > 80) {
                    maxbyte = 1800;
                    $('#smsbyte').html(stringByteLength);
                }
                $('#smsMsg').val(smsMsg);
            }
        });
    }

    //문자고지
    function tabSetting() {
        $("#tabSendSms a:first").tab("show");
    };

    //청구 발송 내용 byte count
    $(document).ready(function () {
        $("#closeModal").on("click", function () {
            resetData();
            tabSetting();
        });

        $(".smscount").on("keyup", function () {
            var maxbyte = 80;
            var str = $("#smsMsg").val();
            var stringByteLength = 0;
            stringByteLength = (function (s, b, i, c) {
                for (b = i = 0; c = s.charCodeAt(i++); b += c >> 11 ? 2 : c >> 7 ? 1 : 1) ;
                return b
            })(str);
            if (stringByteLength < 81) {
                $("#byteAlert").val("Y");
                maxbyte = 80;
                $('#totbyte').html('<em id="smsbyte">0</em>/' + maxbyte);
            }
            if (stringByteLength > 80) {
                if ($("#byteAlert").val() == "Y") {
                    swal({
                        type: 'info',
                        text: "80byte 초과하여 장문 메시지로 변경됩니다. 장문메시지 발송시 금액이 초과될 수 있습니다.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $("#byteAlert").val("N");
                    })
                    maxbyte = 1800;
                    $('#totbyte').html('<em id="smsbyte">0</em>/' + maxbyte);
                }
            }

            $('#smsbyte').html(stringByteLength);
        });

        $("#smsRow-th").on("click", function () {
            if ($(this).prop('checked')) {
                $('input:checkbox[name=smsCheck]').prop('checked', true);
            } else {
                $('input:checkbox[name=smsCheck]').prop('checked', false);
            }
        });
    });

    var telIdx = 0;

    // 수신번호 추가
    function fn_hpNoAdd() {
        var tel = $('#telNo').val();

        if (sendMsgInfoList == null) {
            sendMsgInfoList.list = [];
        }

        if (tel == '' || tel == null) {
            swal({
                type: 'info',
                text: "연락처를 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (tel.length < 8) {
            swal({
                type: 'info',
                text: "연락처는 최소 8자리 이상 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var addHpNo = {};
        addHpNo.notiMasCd = 'fix_000' + telIdx;
        addHpNo.cusHp = tel;
        addHpNo.cusName = '';

        sendMsgInfoList.list.unshift(addHpNo);

        selectedPage = 1;
        fn_cusGrid(sendMsgInfoList, 'cusReqList');
        paging(sendMsgInfoList.list.length, dataPerPage, pageCount, 1);
        telIdx++;
        $("#telNo").val('');
    }

    // 선택삭제
    function fn_hpNoDel() {
        var rowCnt = 0;
        var checkbox = $("input[name='smsCheck']:checked");
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

                var sendTelCnt = sendMsgInfoList.list.length;
                var sendTempList = sendMsgInfoList.list;

                for (var i = 0; i < sendTelCnt; i++) {
                    if (sendMsgInfoList.list[i]) {
                        var sendTelId = sendMsgInfoList.list[i].notiMasCd;

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

                $('#telNo').val('');

                swal({
                    type: 'success',
                    text: "삭제하였습니다.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });

                selectedPage = 1;
                fn_cusGrid(sendMsgInfoList, 'cusReqList');
                paging(sendMsgInfoList.list.length, dataPerPage, pageCount, 1);
            }
        });

        $("#smsRow-th").prop("checked", false);
    }

    // 전체삭제
    function fn_hpNoDelAll() {
        if (sendMsgInfoList == null) {
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
                    sendMsgInfoList = [];
                    sendMsgInfoList.list = [];

                    selectedPage = 1;
                    fn_cusGrid(sendMsgInfoList, 'cusReqList');
                    paging(sendMsgInfoList.list.length, dataPerPage, pageCount, 1);

                    $('#telNo').val('');

                    swal({
                        type: 'success',
                        text: "삭제하였습니다.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            });

            $("#smsRow-th").prop("checked", false);
        }
    }

    // 메시지 발송
    function fn_msgSend() {
        var gridList = sendMsgInfoList.list;

        var idx = sendMsgInfoList.list.length;
        var strList = [];           // hpNo
        var idxList = [];           // notiMasCd

        $.each(gridList, function (k, v) {
            strList.push(v.cusHp);
            idxList.push(v.notiMasCd);
        });


        if (idx == 0) {
            swal({
                type: 'info',
                text: "수신번호를 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (!$('#smsMsg').val()) {
            swal({
                type: 'info',
                text: "발송할 메시지 내용을 확인해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        swal({
            type: 'question',
            html: "문자메시지를 전송 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {

                var url = "/org/notiMgmt/smsMsgSend";
                var param = {
                    writer: '${chaName}',
                    strList: strList,
                    idxList: idxList,
                    telNo: $('#sendNo').val().replace(/-/gi, ""),
                    msg: basicEscape($('#smsMsg').val())
                };
                $.ajax({
                    type: "post",
                    async: true,
                    url: url,
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (data) {
                        console.log(data);
                        if(data.resultCd == "0001") {
                            swal({
                                type: 'info',
                                text: "정부 불법스팸 규제 적책으로 인한 일일 문자발송제한 정책이 강화되어 일 5,000건 이상 및 23:00~04:00 동안 문자 발송이 불가합니다.",
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        } else if(data.resultCd == '0002') {
                            swal({
                                type: 'info',
                                text: "정부 불법스팸 규제 적책으로 인한 일일 문자발송제한 정책이 강화되어 일 5,000건 이상 및 23:00~04:00 동안 문자 발송이 불가합니다.",
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        } else if(data.resultCd == '0000') {
                            swal({
                                type: 'success',
                                text: "메시지를 전송하였습니다.",
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        } else {
                            swal({
                                type: 'success',
                                text: "메시지를 전송하였습니다.",
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        }
                    },
                    error: function (data) {
                        swal({
                            type: 'error',
                            text: "메시지 전송 실패하였습니다.",
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        });
                    }
                });
            }
        });
    }

    // 조회
    var cPage = 1; // 청구목록 현재페이지
    function fn_selSms(page) {
        if (page == null || page == 'undefined') {
            cPage = '1';
        } else {
            cPage = page;
        }

        var stDt = $('#selStartDt').val().replace(/\./gi, "");
        var edDt = $('#selEndDt').val().replace(/\./gi, "");

        if ((!stDt || !edDt) || (stDt.length < 8 || edDt.length < 8) || (stDt > edDt)) {
            swal({
                type: 'info',
                text: "발송일을 확인해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var url = "/org/notiMgmt/sendSmsList";
        var param = {
            curPage: cPage,
            stDt: stDt,
            edDt: edDt,
            selGubn: $('#selGubun option:selected').val(),
            keyword: $('#keyword').val(),
            search_orderBy: $('#searchOrderBySm option:selected').val(),
            status: $('input[name=smsStatus]:checked').val()
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (data) {
                $('#sendSelGridTotCnt').html('');
                $('#sendSelGridTotCnt').html(data.count);
                fn_sendGrid(data, 'sendSelGrid');
                ajaxModalPaging(data, 'ModalPageArea'); //modal paging
            }
        });
    }

    function fn_sendGrid(data, obj) {
        var str = '';
        if (data.count > 0) {
            $.each(data.list, function (k, v) {
                str += '<tr>';
                str += '<td>' + v.reqDate + '</td>';
                str += '<td>' + v.type + '</td>';
                str += '<td title="' + basicEscape(v.cusName) + '" class="has-ellipsis">' + basicEscape(v.cusName) + '</td>';
                str += '<td>' + v.phone + '</td>';
                str += '<td title="' + basicEscape(v.msg) + '" class="has-ellipsis">' + basicEscape(v.msg) + '</td>';
                str += '<td>' + v.status + '</td>';
                str += '</tr>';
            });
        } else {
            str += '<tr>';
            str += '<td colspan="6">[조회된 내역이 없습니다.]</td>';
            str += '</tr>';
        }
        $('#' + obj).html(str);
    }

    function resetData() {
        sendMsgInfoList = [];
        sendMsgInfoList.list = [];
        telIdx = 0;
    };

    function sendExeclDownload() {
        if ($('#sendSelGridTotCnt').text() == 0) {
            swal({
                type: 'info',
                text: '다운로드할 데이터가 없습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        swal({
            type: 'question',
            html: "다운로드 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                $('#excelStartDt').val($('#selStartDt').val().replace(/\./gi, ""));
                $('#excelEndDt').val($('#selEndDt').val().replace(/\./gi, ""));
                $('#excelGubun').val($('#selGubun option:selected').val());
                $('#excelKeyword').val($('#keyword').val());
                $('#excelOrderBy').val($('#searchOrderBySm option:selected').val());
                $('#excelSmsStatus').val($('input[name=smsStatus]:checked').val());

                // 다운로드
                document.excelForm.action = "/org/notiMgmt/excelSendSmsListDownload";
                document.excelForm.submit();
            }
        });
    }
</script>

<input type="hidden" id="currentPage" name="currentPage" value="1"/>

<form id="excelForm" name="excelForm" method="post">
    <input type="hidden" id="excelStartDt" name="excelStartDt"/>
    <input type="hidden" id="excelEndDt" name="excelEndDt"/>
    <input type="hidden" id="excelGubun" name="excelGubun"/>
    <input type="hidden" id="excelKeyword" name="excelKeyword"/>
    <input type="hidden" id="excelOrderBy" name="excelOrderBy"/>
    <input type="hidden" id="excelSmsStatus" name="excelSmsStatus"/>
</form>

<div class="modal fade" id="sms-notification" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">문자메시지고지</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true" onclick="resetData();">&times;</span></button>
            </div>
            <div class="modal-body">
                <div class="container-fluid mb-4">
                    <div class="row">
                        <div class="col">
                            <div class="tab-selecter type-3">
                                <ul class="nav nav-tabs" role="tablist">
                                    <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#send-sms">발송</a></li>
                                    <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#sent-list" onclick="fn_selSms();">발송내역</a></li>
                                </ul>
                            </div>

                            <div class="tab-content" class="container-fluid">
                                <div id="send-sms" class="tab-pane fade show active">
                                    <div class="row">
                                        <div class="col col-sm-2 col-12">
                                            <ul id="tabSendSms" class="nav nav-tabs vertical-tab" role="tablist">
                                                <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#" onclick="fn_setSendMsg('claim');"><img src="/assets/imgs/collecter/icon-bill.png" id="icon-bill" class="icon">청구발송</a></li>
                                                <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#" onclick="fn_setSendMsg('acc');"><img src="/assets/imgs/collecter/icon-won-mark-in-coin-greyscale.png" id="icon-won-coin" class="icon">입금발송</a></li>
                                                <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#" onclick="fn_setSendMsg('nonpay');"><img src="/assets/imgs/collecter/icon-won-mark-paper-greyscale.png" id="icon-won-paper" class="icon">미납발송</a></li>
                                                <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#" onclick="fn_setSendMsg('default1');"><img src="/assets/imgs/collecter/icon-document-2-greyscale.png" id="icon-document-2" class="icon">기본문구1</a></li>
                                                <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#" onclick="fn_setSendMsg('default2');"><img src="/assets/imgs/collecter/icon-document-3-greyscale.png" id="icon-document-3" class="icon">기본문구2</a></li>
                                                <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#" onclick="fn_setSendMsg('direct');"><img src="/assets/imgs/collecter/icon-pencil-on-bottom-greyscale.png" id="icon-pencil" class="icon">직접입력</a></li>
                                            </ul>
                                        </div>
                                        <div class="col col-sm-5 col-12">
                                            <div class="mobile-mockup">
                                                <div class="speaker-camera align-items-center">
                                                    <div class="camera"></div>
                                                    <div class="speaker"></div>
                                                </div>
                                                <div class="mobile-screen mb-3">
                                                    <div id="sms-text">청구 발송 내용</div>
                                                    <p>
                                                        <textarea id="smsMsg" class="form-control smscount" rows="27" maxlength="912"></textarea>
                                                        <span class="textarea-byte-counter" id="totbyte"><em id="smsbyte">0</em>/80byte</span>
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col col-sm-5 col-12 left-border">
                                            <div class="search-box mb-3">
                                                <div class="row">
                                                    <label class="col col-4">발신번호</label>
                                                    <div class="col col-8">
                                                        <select class="form-control" id="sendNo"></select>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row mb-3">
                                                <div class="col col-9"><input class="form-control col-auto" type="text" id="telNo" value="" onkeyup="onlyNumber(this)" maxlength="12"></div>
                                                <div class="col col-3"><button type="button" class="btn btn-sm btn-d-gray" onclick="fn_hpNoAdd();">추가</button></div>
                                            </div>

                                            <div class="row col mb-1">총 [<strong class="text-primary" id="gridListTotCnt">0</strong>]명</div>
                                            <div class="table-responsive">
                                                <table class="table table-sm table-hover table-primary">
                                                    <thead>
                                                        <tr>
                                                            <th>
                                                                <input class="form-check-input table-check-parents" type="checkbox" id="smsRow-th" name="checkAll">
                                                                <label for="smsRow-th"><span></span></label>
                                                            </th>
                                                            <th>연락처(수신번호)</th>
                                                            <th>고객명</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="cusReqList">
                                                        <tr>
                                                            <td colspan="3">[조회된 내역이 없습니다.]</td>
                                                        </tr>
                                                    </tbody>
                                                </table>

                                                <input type="hidden" value="Y" id="byteAlert"/>
                                            </div>

                                            <div class="text-left mt-2">
                                                <button type="button" class="btn btn-sm btn-gray-outlined" onclick="fn_hpNoDel();">선택삭제</button>
                                                <button type="button" class="btn btn-sm btn-gray-outlined" onclick="fn_hpNoDelAll();">전체삭제</button>
                                            </div>

                                            <div class="row mt-3">
                                                <div class="col text-center">
                                                    <nav aria-label="Page navigation example">
                                                        <ul class="pagination justify-content-center" id="paging" style="margin-bottom: 0 !important;"></ul>
                                                    </nav>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row mt-3 mb-3">
                                        <div class="col-12 ars-content">
                                            <ul>
                                                <li class="text-danger">직접 입력한 휴대폰번호의 경우 연결된 고객 정보 및 청구가 없기 때문에 [고객명], [청구금액]등의 표기는 공란으로 문자가 발송되오니 주의하시기 바랍니다.</li>
                                                <li class="text-danger">자동입력값은 고객 및 청구가 등록되어야 자동으로 해당 정보가 포함되어 메시지가 발송됩니다.</li>
                                                <li class="text-danger">문자메시지 발송은 체크박스 선택유무와 관계없이 전체 고객에게 발송됩니다.</li>
                                            </ul>
                                        </div>
                                    </div>

                                    <div class="row mt-3">
                                        <div class="col text-center">
                                            <button id="closeModal" type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>
                                            <button type="button" class="btn btn-primary" onclick="fn_msgSend();">메시지 발송</button>
                                        </div>
                                    </div>
                                </div>

                                <div id="sent-list" class="tab-pane fade show">
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
                                                                    <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD" id="selStartDt" aria-label="From" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
                                                                </div>
                                                            </div>
                                                            <span class="range-mark"> ~ </span>
                                                            <div class="date-input">
                                                                <label class="sr-only">To</label>
                                                                <div class="input-group">
                                                                    <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD" id="selEndDt" aria-label="To" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <label class="col col-md-1 col-sm-2 col-2 col-form-label">검색구분</label>
                                                        <div class="col col-md-5 col-sm-10 col-10 form-inline">
                                                            <select class="form-control col-auto" id="selGubun">
                                                                <option value="cusName">고객명</option>
                                                                <option value="phone">수신번호</option>
                                                                <option value="msg">내용</option>
                                                            </select>
                                                            <input class="form-control col-auto" type="text" id="keyword" onkeypress="if(event.keyCode == 13){fn_selSms();}">
                                                        </div>
                                                    </div>

                                                    <div class="row">
                                                        <label class="col-form-label col col-md-1 col-sm-3 col-3">발송결과 </label>

                                                        <div class="col col-md-11 col-sm-9 col-9 form-inline">
                                                            <div class="form-inline">
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="radio" name="smsStatus" id="smsStatus1" value="ALL"  checked="checked">
                                                                    <label class="form-check-label" for="smsStatus1"><span class="mr-1"></span>전체</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="radio" name="smsStatus" id="smsStatus2" value="0">
                                                                    <label class="form-check-label" for="smsStatus2"><span class="mr-1"></span>발송대기</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="radio" name="smsStatus" id="smsStatus3" value="1">
                                                                    <label class="form-check-label" for="smsStatus3"><span class="mr-1"></span>발송중</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="radio" name="smsStatus" id="smsStatus4" value="2">
                                                                    <label class="form-check-label" for="smsStatus4"><span class="mr-1"></span>발송완료</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="radio" name="smsStatus" id="smsStatus5" value="3">
                                                                    <label class="form-check-label" for="smsStatus5"><span class="mr-1"></span>발송실패</label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="row form-inline mt-3">
                                                        <div class="col-12 text-center">
                                                            <button type="button" class="btn btn-primary btn-wide" onclick="fn_selSms();">조회</button>
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>

                                            <div class="table-option row mb-2">
                                                <div class="col-md-6 col-sm-12 form-inline">전체건수 : [<strong class="text-primary" id="sendSelGridTotCnt">0</strong>]건</div>
                                                <div class="col-md-6 col-sm-12 text-right mt-1">
                                                    <select class="form-control" name="searchOrderBySm" id="searchOrderBySm" onchange="fn_selSms();">
                                                        <option value="reqDt">발송일시순 정렬</option>
                                                        <option value="cusName">고객명순 정렬</option>
                                                        <option value="status">발송결과순 정렬</option>
                                                    </select>

                                                    <button class="btn btn-sm btn-d-gray hidden-on-mobile" type="button" onclick="sendExeclDownload();">파일저장</button>
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

                                                <tbody id="sendSelGrid">
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
                                                <ul class="pagination justify-content-center" id="ModalPageArea"></ul>
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

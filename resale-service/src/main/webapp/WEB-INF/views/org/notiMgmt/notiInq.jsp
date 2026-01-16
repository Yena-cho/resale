<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<s:authentication property="principal.username" var="userId"/>
<s:authentication property="principal.name" var="userName"/>

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/>

<script type="text/javascript">
    $(document).ready(function () {
        $('.popover-dismiss').popover({
            trigger: 'focus'
        });
        getYearsBox2('yearsBox');
        getMonthBox('monthBox');
        cusGubnBox('cusGubn'); // 고객구분 selectBox
        fnSearch();
    });

    var cuPage = 1;
    var firstDepth = "nav-link-33";
    var secondDepth = "sub-01";
    $(function () {

        //전체 체크, 해제
        $('#payRadio1').click(function () {
            if ($('#payRadio1').prop('checked')) {
                $('input:checkbox[name=payItem]').prop('checked', true);
            } else {
                $('input:checkbox[name=payItem]').prop('checked', false);
            }
        });
        $('input:checkbox[name=payItem]').click(function () {
            if ($('#payRadio1').is(":checked")) {
                $('#payRadio1').prop("checked", false);
            }
            if ($('input:checkbox[name=payItem]').length == $('input:checkbox[name=payItem]:checked').length) {
                $('#payRadio1').prop("checked", true);
            }
        });

        // 검색구분이 계좌번호일때는 숫자만 입력하고 아닐땐 허용
        $('#searchvalue').keyup(function () {
            if ($('#searchGb option:selected').val() == "vano") {
                $('#searchvalue').val($('#searchvalue').val().replace(/[^0-9]/g, ""));
            }
        });

        $('#row-th').click(function () {
            if ($(this).prop('checked')) {
                $('input:checkbox[name=checkList]').prop('checked', true);
            } else {
                $('input:checkbox[name=checkList]').prop('checked', false);
            }
        });
    });

    function cusName(vano) {
        fnDetail(vano);
    }

    function notiItemDetail(idx) {
        var notiMasCd = $('#btnPop-' + idx + '').attr('notiMasCd');
        var cusName = $('#btnPop-' + idx + '').attr('cusName');
        var masMonth = replaceDot($('#btnPop-' + idx + '').attr('masMonth'));
        var vano = $('#btnPop-' + idx + '').attr('vaNo');

        var url = "/org/receiptMgnt/getReceiptDetailAjax";
        var param = {
            notiMasCd: notiMasCd,
            masMonth: masMonth
        };
        $.ajax({
            type: "post",
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    $('#popCusName').text(cusName);
                    $('#popMasMonth').text(formatYearMonth(masMonth));
                    $('#popMasVano').text(vano);
                    var str = '';
                    var popTotalAmt = 0;
                    var popTotalRcpAmt = 0;
                    if (result.count <= 0) {
                        str += '<tr><td colspan="4" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
                    } else {
                        $.each(result.list, function (i, v) {
                            str += '<tr>';
                            str += '<td>' + v.payItemName + '</td>';
                            str += '<td style="text-align: right;">' + numberToCommas(v.payItemAmt) + '</td>';
                            str += '<td style="text-align: right;">' + numberToCommas(v.rcpAmt) + '</td>';
                            str += '<td>' + (v.ptrItemRemark == null ? "" : basicEscape(v.ptrItemRemark)) + '</td>';
                            str += '</tr>';
                        });
                    }
                    $('#popResultBody').html(str);
                    $('#popTotalAmt').text(numberToCommas(result.totalPayAmt));
                    $('#popTotalRcpAmt').text(numberToCommas(result.totalRcpAmt));
                    $("#popup-payment-unit-list").modal({
                        backdrop: 'static',
                        keyboard: false
                    });
                } else {
                    swal({
                        type: 'error',
                        text: result.retMsg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            }
        });

    }

    function onSearchGb() {
        if ($('#searchGb option:selected').val() == "vano") {
            $('#searchvalue').val("");
        }
    }

    function pageChange() {
        fnSearch();
    }

    // 고객구분selectBox
    function cusGubnBox(obj) {
        var str = "<option value='all'>전체</option>";

        if ('${orgSess.cusGubn1}' != null && '${orgSess.cusGubn1}' != '') {
            str += "<option value='cusGubn1'>${orgSess.cusGubn1}</option>";
        }
        if ('${orgSess.cusGubn2}' != null && '${orgSess.cusGubn2}' != '') {
            str += "<option value='cusGubn2'>${orgSess.cusGubn2}</option>";
        }
        if ('${orgSess.cusGubn3}' != null && '${orgSess.cusGubn3}' != '') {
            str += "<option value='cusGubn3'>${orgSess.cusGubn3}</option>";
        }
        if ('${orgSess.cusGubn4}' != null && '${orgSess.cusGubn4}' != '') {
            str += "<option value='cusGubn4'>${orgSess.cusGubn4}</option>";
        }
        $('#' + obj).html(str);
    }

    // 검색
    function fnSearch(page) {
        var checkArr = [];
        if (page == null || page == 'undefined') {
            cuPage = "";
            cuPage = 1;
        } else {
            cuPage = page;
        }

        $('input:checkbox[name=payItem]:checked').each(function (i) {
            checkArr.push($(this).val());
        });

        if ($('#searchGb option:selected').val() == "vano" && $("#searchValue").val()) {
            var str = $("#searchValue").val();
            var s = str.split(",");

            for (var i = 0; i < s.length; i++) {
                if (($.isNumeric(s[i]) && (s[i].length < 2 || s[i].length > 4)) && s[i].length != 14) {
                    swal({
                        type: 'info',
                        html: "가상계좌번호 14자리 모두 입력 또는 <p>가상계좌 끝 4자리 중 최소 2자리 이상, 4자리 이하로 입력해 주세요.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                    return;
                }
                if (!$.isNumeric(s[i])) {
                    swal({
                        type: 'info',
                        text: "가상계좌는 숫자형식으로만 입력가능합니다.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $("#searchValue").val('');
                    });
                    return;
                }
            }
        }

        if (fnValidation()) {
            var url = "/org/notiMgmt/getNotiIngList";
            var param = {
                masMonth: $('#yearsBox option:selected').val() + ""
                    + $('#monthBox option:selected').val(), //청구년도월
                searchGb: $('#searchGb option:selected').val(), //검색구분(cusname:고객명, vano:계좌번호)
                searchValue: $('#searchValue').val(), //검색구분 텍스트값
                rcpDtDupYn: $('#rcpDtDupYN').val(), //값이 P이면 det.notidetst=? else  mas.notiMasSt=?
                //notiMasSt 컬럼으로 조회
                payGb1: $('#payRadio1').is(":checked") ? "ALL" : "NOTALL", //납부구분 (전체)
                payList: checkArr,
                userName: $('#userName').val(),
                chaCd: $('#chaCd').val(),
                cusGubn: $('#cusGubn option:selected').val(), //고객구분
                cusGubnValue: $('#cusGubnValue').val(),
                curPage: cuPage,
                pageScale: $('#pageScale option:selected').val(),
                search_orderBy: $('#searchOrderBy option:selected').val()
                // 고객구분 텍스트값
            };
            $("#row-th").prop("checked", false);
            $.ajax({
                type: "post",
                async: true,
                url: url,
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(param),
                success: function (result) {
                    if (result.retCode == "0000") {
                        fnGrid(result, 'resultBody');		// 현재 데이터로 셋팅
                        ajaxPaging(result, 'PageArea');
                        fn_isNotiYn();
                    } else {
                        swal({
                            type: 'error',
                            text: result.retMsg,
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        })
                    }
                }
            });
        }

    }

    // validation
    function fnValidation() {
        var selectDate = $('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val();
        if ($('#monthBox option:selected').val() == '선택') {
            swal({
                type: 'info',
                text: '정확한 날짜를 선택해주세요',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                var scmove = $('#monthBox').offset().top;
                $('html, body').animate({scrollTop: scmove - 300}, 300);
            });
            return false;
        } else if ($('#yearsBox option:selected').val() == '선택') {
            swal({
                type: 'info',
                text: '정확한 날짜를 선택해주세요',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                var scmove = $('#yearsBox').offset().top;
                $('html, body').animate({scrollTop: scmove - 300}, 300);
            });
            return false;
        }
        return true;
    }

    // 데이터 새로고침
    function fnGrid(result, obj) {
        var str = '';
        $('#totCnt').text(result.count);
        $('#totAmt').text(numberWithCommas(result.totAmt));
        if (result.count <= 0) {
            str += '<tr><td colspan="17" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>';
                str += '<input class="form-check-input table-check-child" name="checkList" type="checkbox" id="row-' + v.rn + '" value="' + v.notiMasCd + '" notiMasCd="' + v.notiMasSt + '" onchange="changeBGColor(this)">';
                str += '<label for="row-' + v.rn + '"><span></span></label>';
                str += '</td>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + nullValueChange(v.masMonth) + '</td>';
                str += '<td><button type="button" class="btn btn-xs btn-link" onclick="cusName(\'' + v.vaNo + '\')" value="' + v.vaNo + '">' + basicEscape(nullValueChange(v.cusName)) + '</button></td>';
                str += '<td>' + v.notiMasStNm + '</td>';
                if ($('#cusGubn1').val() != null && $('#cusGubn1').val() != '') {
                    str += '<td>' + basicEscape(nullValueChange(v.cusGubn1)) + '</td>';
                }
                if ($('#cusGubn2').val() != null && $('#cusGubn2').val() != '') {
                    str += '<td>' + basicEscape(nullValueChange(v.cusGubn2)) + '</td>';
                }
                if ($('#cusGubn3').val() != null && $('#cusGubn3').val() != '') {
                    str += '<td>' + basicEscape(nullValueChange(v.cusGubn3)) + '</td>';
                }
                if ($('#cusGubn4').val() != null && $('#cusGubn4').val() != '') {
                    str += '<td>' + basicEscape(nullValueChange(v.cusGubn4)) + '</td>';
                }
                str += '<td>' + nullValueChange(v.vaNo) + '</td>';
                str += '<td>' + nullValueChange(v.rcpDate) + '</td>';
                str += '<td>' + nullValueChange(v.printDate) + '</td>';
                str += '<td><button type="button" class="btn btn-xs btn-link" id="btnPop-' + v.rn + '" vaNo="' + nullValueChange(v.vaNo) + '" notiMasCd="' + nullValueChange(v.notiMasCd) + '" cusName="' + nullValueChange(v.cusName) + '" masMonth="' + nullValueChange(v.masMonth) + '" onclick="notiItemDetail(' + v.rn + ');">' + v.detCnt + '건</button></td>';
                str += '<td class="text-right">' + numberWithCommas(nullValueChange(v.amt)) + '</td>';
                str += '<td>' + nullValueChange(v.masDay) + '</td>';
                if ($("#tableCheckbox1").prop('checked')) {
                    str += '<td class="isNotiYn hidden hidden-01">' + nullValueChange(v.sendDate) + '</td>';
                    str += '<td class="isNotiYn hidden hidden-01">' + nullValueChange(v.reqDate) + '</td>';
                    str += '<td class="isNotiYn hidden hidden-01">' + nullValueChange(v.reqDt) + '</td>';
                } else {
                    str += '<td class="isNotiYn hidden hidden-01" style="display: none;">' + nullValueChange(v.sendDate) + '</td>';
                    str += '<td class="isNotiYn hidden hidden-01" style="display: none;">' + nullValueChange(v.reqDate) + '</td>';
                    str += '<td class="isNotiYn hidden hidden-01" style="display: none;">' + nullValueChange(v.reqDt) + '</td>';
                }
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    // 페이징 버튼
    function list(page) {
        fnSearch(page);
    }

    // pdf 인쇄
    function fnPrint() {
        var count = $('input:checkbox[name=checkList]:checked').length;
        var checkArr = [];
        if (count == 0) {
            swal({
                type: 'warning',
                text: '선택하신 고지서인쇄건이 없습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return false;
        } else {
            $('input:checkbox[name=checkList]:checked').each(function (i) {
                checkArr.push($(this).val());
            });
            $('#checkListValue').val(checkArr);
            $('#billgubnValue').val($('#billgubn option:selected').val());
            $('#masMonth').val($('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val());
            $('#sFileName').val($('#chaCd').val() + getCurrentDate());
            $('#sSearchValue').val($('#searchValue').val() != "" ? $('#searchValue').val() : null);
            $('#sSearchGb').val($('#searchGb option:selected').val() != "" ? $('#searchGb option:selected').val() : null);
            $('#sCusGubnValue').val($('#cusGubnValue').val() != "" ? $('#cusGubnValue').val() : null);
            $('#sCusGubn').val($('#cusGubn option:selected').val() != "" ? $('#cusGubn option:selected').val() : null);
            $('#sSearchOrderBy').val($('#searchOrderBy option:selected').val());
        }
        var agent = navigator.userAgent.toLowerCase();

        $('#sBrowserType').val('ie');
        // ie용 pdf 출력
        fnSelPrint();

        $('#pdfMakeForm').submit();
    }

    // 선택인쇄 파일설정
    function fnSelPrint() {
        swal({
            type: 'info',
            text: '고지서 파일이 생성되고 있습니다.',
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
        })

        if ($('#billiMgty').val() == "G") {
        } else {
            fnPrintSetting();
        }
    }

    // 일괄인쇄 파일설정
    function fnAllPrint() {
        swal({
            type: 'info',
            text: '인쇄파일 생성까지 약 10분가량 소요됩니다.',
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
        })
        $('#pdfView').removeAttr('onclick');
        $('#pdfView').removeAttr('href');

        if ($('#chaGroupId').val() == "90000025") {
        } else if ($('#billiMgty').val() == "G") {
        } else {
            fnPrintSetting();
        }

    }

    // PA02 미납만 발급 가능함
    function fnPrintSetting() {
        var checkArr = [];
        $('input:checkbox[name=payItem]:checked').each(function (i) {
            checkArr.push($(this).val());
        });

        var url = "/org/notiMgmt/regPdfSetting";
        var param = {
            masMonth: $('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val(), // 청구년도월
            searchGb: $('#searchGb option:selected').val(), // 검색구분(cusname:고객명, vano:계좌번호)
            searchValue: $('#searchValue').val(), // 검색구분 텍스트값
            rcpDtDupYn: $('#rcpDtDupYN').val(), // 값이 P이면 det.notidetst=? else  mas.notiMasSt=?
            payGb1: $('#payRadio1').is(":checked") ? "ALL" : "NOTALL", //납부구분 (전체)
            payList: checkArr,
            userName: $('#userName').val(),
            chaCd: $('#chaCd').val(),
            cusGubn: $('#cusGubn option:selected').val(),	// 고객구분
            cusGubnValue: $('#cusGubnValue').val(),
            curPage: page,
            pageScale: $('#pageScale option:selected').val(),
            chaGroupId: $('#chaGroupId').val(),
            billiMgty: $('#billiMgty').val() == "" ? "0" : $('#billiMgty').val(),
            amtChkTy: $('#amtChkTy').val(),
            chaTrTy: $('#chaTrTy').val(),
            billGubn: $('#billgubn option:selected').val(),	// 인쇄문구설정
            fileName: $('#pdfView').val(),
            search_orderBy: $('#searchOrderBy option:selected').val()
            // notiMascdList : listArr,
            // 고객구분 텍스트값
        };

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    swal({
                        type: 'success',
                        text: 'pdf 파일이 생성되었습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                    $('#pdfView').text('');
                    $('#pdfView').removeAttr('onclick');
                    $('#pdfView').attr('href', '#');
                    $('#pdfView').text(result.fileName);
                    $('#pdfView').attr('onclick', 'fnPdfView("' + result.fileName + '")');
                } else {
                    swal({
                        type: 'error',
                        text: result.retMsg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            }
        });
    }

    // ie용 pdf 출력
    function fnPdfView(file) {
        var url = "/org/notiMgmt/getPdfViewer";
        $('#fileName').val(file);
        document.searchForm.action = url;
        document.searchForm.submit();
    }

    //금액 콤마
    function comma(str) {
        str = String(str);
        return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
    }

    //금액 콤마
    function numberWithCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    // sms 고지
    function fn_smsNoti() {
        fn_reset_scroll();

        var url = "/org/notiMgmt/selSmsUseYn";
        var param = {};

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (data) {
                if (data.map == 'Y') {			// sms 고지
                    $("#smsRow-th").prop("checked", false);
                    fn_smsNotification();
                } else if (data.map == 'N') {	// 문자메시지 서비스 이용등록
                    fn_smsServiceInsPage();
                } else if (data.map == 'W') {	// 문자메시지 서비스 이용등록 신청중
                    swal({
                        type: 'info',
                        text: '문자메시지 서비스 이용등록 신청중입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                } else if (data.map == 'C') {
                    swal({
                        type: 'info',
                        text: '문자메시지 서비스 이용 취소상태입니다. 재사용은 관리자에게 문의바랍니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            }
        });
    }

    // 이메일 고지
    function fn_emailNoti() {
        fn_reset_scroll();
        var url = "/org/notiMgmt/selEmailUseYn";
        var str = $('#searchOrderBy option:selected').val();
        var param = {};
        $('#billGubn').val("01").prop("selected", true);

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (data) {
                if (data.cnt > 0) {
                    fn_emailNotiPage(str);
                } else {
                    swal({
                        type: 'info',
                        text: '고지서 설정 후 사용가능합니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            }
        });
    }

    //modal 페이징 버튼
    function modalList(num, val) {
        if (val == '1') {		        // SMS 고지 modal 페이징
            fn_selSms(num);
        } else if (val == '2') {        // EMAIL 고지 modal 페이징
            fn_selEmail(num);
        } else {                    	// 알림톡 고지 modal 페이징
            fn_selAtList(num);
        }
    }

    function fn_isNotiYn() {
        if ($("#tableCheckbox1").prop('checked')) {
            $(".isNotiYn").show();
        } else {
            $(".isNotiYn").hide();
        }
    }

    function list(page, val) {
        if (val == '55') {
            fn_ListCollector(page); // 기관검색
        } else {
            fnSearch(page);
        }
    }

    function onCusGubn(obj) {
        if (obj.value == 'all') {
            $('#cusGubnValue').attr('placeholder', '콤마(,) 구분자로 다중검색이 가능합니다.');
        } else {
            $('#cusGubnValue').attr('placeholder', '콤마(,) 구분자로 다중검색이 가능합니다.');
        }
    }

    function fn_atNoti() {
        fn_reset_scroll();

        var url = "/org/notiMgmt/selAtUseYn";
        var param = {};

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (data) {
                if (data.map == 'Y') {
                    $("#atRow-th").prop("checked", false);
                    fn_atNotification();
                } else if (data.map == 'N') {
                    fn_atModalOpen();
                } else if (data.map == 'W') {
                    swal({
                        type: 'info',
                        html: '알림톡 서비스 이용등록 신청중입니다.<p>승인 완료 후 담당자 연락처로 연락드리겠습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                } else if (data.map == 'C') {
                    swal({
                        type: 'info',
                        text: '알림톡 서비스 이용 취소상태입니다. 재사용은 관리자에게 문의바랍니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                } else if (data.map == 'D') {
                    fn_atModalOpen();
                }
            }
        });
    }
</script>

<form id="searchForm" name="searchForm" method="post">
    <input type="hidden" name="fileName" id="fileName"/>
</form>

<form id="pdfMakeForm" name="pdfMakeForm" method="post" action="/org/notiMgmt/pdfMakeBill">
    <input type="hidden" id="chaTrTy" name="chaTrTy" value="${orgSess.chaTrTy}"/>
    <input type="hidden" id="billgubnValue" name="billgubnValue" value=""/>
    <input type="hidden" id="checkListValue" name="checkListValue"/>
    <input type="hidden" name="sFileName" id="sFileName"/>
    <input type="hidden" id="masMonth" name="masMonth"/>
    <input type="hidden" id="sSearchValue" name="sSearchValue"/>
    <input type="hidden" id="sSearchGb" name="sSearchGb"/>
    <input type="hidden" id="sCusGubnValue" name="sCusGubnValue"/>
    <input type="hidden" id="sCusGubn" name="sCusGubn"/>
    <input type="hidden" id="sBrowserType" name="sBrowserType"/>
    <input type="hidden" id="sSearchOrderBy" name="sSearchOrderBy"/>
    <input type="hidden" id="pdfStatus" name="pdfStatus" value="pdfOK"/>
</form>

<input type="hidden" id="userName" name="userName" value="${userName}"/>
<input type="hidden" id="chaCd" name="chaCd" value="${userId}"/>
<input type="hidden" id="curPage" name="curPage"/>
<input type="hidden" id="rcpDtDupYN" name="rcpDtDupYn" value="${orgSess.rcpDtDupYn}"/>
<input type="hidden" id="chaGroupId" name="chaGroupId" value="${orgSess.chaGroupId}"/>
<input type="hidden" id="billiMgty" name="billiMgty" value="${orgSess.billiMgty}"/>
<input type="hidden" id="amtChkTy" name="amtChkTy" value="${orgSess.amtChkTy}"/>
<input type="hidden" id="cusGubn1" name="cusGubn1" value="${orgSess.cusGubn1}"/>
<input type="hidden" id="cusGubn2" name="cusGubn2" value="${orgSess.cusGubn2}"/>
<input type="hidden" id="cusGubn3" name="cusGubn3" value="${orgSess.cusGubn3}"/>
<input type="hidden" id="cusGubn4" name="cusGubn4" value="${orgSess.cusGubn4}"/>

<div id="contents">
    <div id="damoa-breadcrumb">
        <nav class="nav container">
            <a class="nav-link active" href="#">고지서 조회/출력</a>
            <a class="nav-link" href="/org/notiMgmt/notiConfig">고지서설정</a>
            <a class="nav-link" href="/org/notiMgmt/notiPrintReq">고지서 출력의뢰</a>
        </nav>
    </div>
    <div class="container">
        <div id="page-title">
            <div id="breadcrumb-in-title-area" class="row align-items-center">
                <div class="col-12 text-right">
                    <img src="/assets/imgs/common/icon-home.png" class="mr-2"> <span>
						> </span> <span class="depth-1">고지관리</span> <span> > </span> <span
                        class="depth-2 active">고지서 조회/출력</span>
                </div>
            </div>
            <div class="row">
                <div class="col-12">
                    <h2>고지서 조회/출력</h2>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div id="page-description">
            <div class="row">
                <div class="col-12">
                    <p>청구 고지서를 조회 및 출력하는 화면입니다.</p>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <form class="search-box">
            <div class="row">
                <label class="col-form-label col col-md-1 col-sm-3 col-3">청구월 </label>
                <div class="col col-md-5 col-sm-9 col-9 form-inline year-month-dropdown">
                    <select class="form-control" id="yearsBox">
                        <option>선택</option>
                    </select>
                    <span class="ml-1 mr-1">년</span>
                    <select class="form-control" id="monthBox">
                        <option>선택</option>
                    </select>
                    <span class="ml-1 mr-auto">월</span>
                </div>

                <label class="col-form-label col col-md-1 col-sm-3 col-3">납부상태 </label>
                <div class="col col-md-5 col-sm-9 col-9 form-inline">
                    <div class="form-inline">
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="payAll" id="payRadio1" value="ALL">
                            <label class="form-check-label" for="payRadio1"><span class="mr-1"></span>전체</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="payItem" id="payRadio2" value="PA02"
                                   checked="checked">
                            <label class="form-check-label" for="payRadio2"><span class="mr-1"></span>미납</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="payItem" id="payRadio3" value="PA04">
                            <label class="form-check-label" for="payRadio3"><span class="mr-1"></span>일부납</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="payItem" id="payRadio4" value="PA05">
                            <label class="form-check-label" for="payRadio4"><span class="mr-1"></span>초과납</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="payItem" id="payRadio5" value="PA03">
                            <label class="form-check-label" for="payRadio5"><span class="mr-1"></span>완납</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="payItem" id="payRadio6" value="PA06">
                            <label class="form-check-label" for="payRadio6"><span class="mr-1"></span>환불</label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">

                <label class="col-form-label col col-md-1 col-sm-3 col-3"> 검색구분 </label>
                <div class="col col-md-5 col-sm-9 col-9 form-inline">
                    <select class="form-control" id="searchGb" onchange="onSearchGb();">
                        <option value="cusname">고객명</option>
                        <option value="vano">가상계좌</option>
                    </select>
                    <input class="form-control" type="text" name="searchValue" id="searchValue"
                           placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch();}"/>
                </div>
                <label class="col-form-label col col-md-1 col-sm-3 col-3">고객구분 </label>
                <div class="col col-md-5 col-sm-9 col-9 form-inline">
                    <select class="form-control" id="cusGubn" onchange="onCusGubn(this)"></select>
                    <div class="input-with-magnet">
                        <input class="form-control" type="text" id="cusGubnValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다."
                               onkeypress="if(event.keyCode == 13){fnSearch();}"/>
                    </div>
                </div>

            </div>

            <div class="row mt-3">
                <div class="col-12 text-center">
                    <input type="button" class="btn btn-primary btn-wide" onclick="fnSearch()" value="조회"/>
                </div>
            </div>
        </form>
    </div>

    <div class="container" id="focus">
        <div id="bill-reference-list" class="list-id">
            <div class="table-option row mb-2">
                <div class="col-md-6 col-sm-12 form-inline">
					<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="totCnt">${map.count}</em>건]
					</span> <span class="amount mr-2">총 청구금액 [총 <em class="font-blue" id="totAmt"><fmt:formatNumber
                        value="${map.totAmt}" pattern="#,###"/></em>원]
					</span>
                    <div class="form-check form-check-inline ml-2">
                        <input class="form-check-input" type="checkbox" id="tableCheckbox1" value="option1" onchange="fn_isNotiYn()">
                        <label for="tableCheckbox1"><span class="mr-2"></span>고지여부 표시</label>
                    </div>
                </div>
                <div class="col-md-6 col-sm-12 text-right mt-1">
                    <select class="form-control" name="searchOrderBy" id="searchOrderBy" onchange="pageChange();">
                        <option value="def">고객명순 정렬</option>
                        <option value="date">납부기한순 정렬</option>
                        <option value="masDate">청구일자순 정렬</option>
                        <option value="vaNo">가상계좌순 정렬</option>
                        <option value="cusGubn">고객구분순 정렬</option>
                    </select>
                    <select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
                        <option value="10" <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회</option>
                        <option value="20" <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회</option>
                        <option value="50" <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회</option>
                        <option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회</option>
                        <option value="200" <c:if test="${map.PAGE_SCALE == '200'}">selected</c:if>>200개씩 조회</option>
                    </select>
                </div>
            </div>
            <div class="row">
                <div class="table-responsive pd-n-mg-o col mb-3">
                    <table class="table table-sm table-hover table-primary">
                        <colgroup>
                            <col width="52">
                            <col width="68">
                            <col width="100">
                            <col width="100">
                            <col width="100">
                            <c:if test="${orgSess.cusGubn1 != '' && orgSess.cusGubn1 != null}">
                                <col width="130">
                            </c:if>
                            <c:if test="${orgSess.cusGubn2 != '' && orgSess.cusGubn2 != null}">
                                <col width="130">
                            </c:if>
                            <c:if test="${orgSess.cusGubn3 != '' && orgSess.cusGubn3 != null}">
                                <col width="130">
                            </c:if>
                            <c:if test="${orgSess.cusGubn4 != '' && orgSess.cusGubn4 != null}">
                                <col width="130">
                            </c:if>
                            <col width="140">
                            <col width="180">
                            <col width="120">
                            <col width="100">
                            <col width="130">
                            <col width="120">
                        </colgroup>

                        <thead>
                        <tr>
                            <th rowspan="2">
                                <input class="form-check-input table-check-parents" type="checkbox" id="row-th"
                                       value="option2">
                                <label for="row-th"><span></span></label>
                            </th>
                            <th rowspan="2">NO</th>
                            <th rowspan="2">청구월</th>
                            <th rowspan="2">고객명</th>
                            <th rowspan="2">납부상태</th>
                            <c:if test="${orgSess.cusGubn1 != '' && orgSess.cusGubn1 != null}">
                                <th rowspan="2">${orgSess.cusGubn1}</th>
                            </c:if>
                            <c:if test="${orgSess.cusGubn2 != '' && orgSess.cusGubn2 != null}">
                                <th rowspan="2">${orgSess.cusGubn2}</th>
                            </c:if>
                            <c:if test="${orgSess.cusGubn3 != '' && orgSess.cusGubn3 != null}">
                                <th rowspan="2">${orgSess.cusGubn3}</th>
                            </c:if>
                            <c:if test="${orgSess.cusGubn4 != '' && orgSess.cusGubn4 != null}">
                                <th rowspan="2">${orgSess.cusGubn4}</th>
                            </c:if>
                            <th rowspan="2">가상계좌</th>
                            <th rowspan="2">납부기한</th>
                            <th rowspan="2">고지서용<br>표시마감일</th>
                            <th rowspan="2">항목건수</th>
                            <th rowspan="2">청구금액(원)</th>
                            <th rowspan="2">청구일자</th>
                            <th colspan="3" width="200" class="isNotiYn hidden hidden-01" style="display: none;">고지여부
                            </th>
                        </tr>
                        <tr class="isNotiYn" style="display: none;">
                            <th width="100" class="hidden hidden-01">알림톡</th>
                            <th width="100" class="hidden hidden-01">문자메시지</th>
                            <th width="100" class="hidden hidden-01">E-MAIL</th>
                        </tr>
                        </thead>
                        <tbody id="resultBody">

                        </tbody>
                    </table>
                </div>
            </div>

            <div class="row mb-4 hidden-on-mobile">
                <div class="col-12 text-right">
                    <button class="btn btn-sm btn-d-gray" id="btn-sms-notification-popup" onclick="fn_smsNoti();">문자메시지고지</button>
<%--                    <button class="btn btn-sm btn-d-gray" id="btn-email-notification-popup" onclick="fn_emailNoti();">E-MAIL고지</button>--%>
<%--                    <button class="btn btn-sm btn-d-gray" id="btn-at-notification-popup" onclick="fn_atNoti();">알림톡고지</button>--%>
                    <button class="btn btn-sm btn-d-gray" onclick="fnPrint()">고지서인쇄</button>
                </div>
            </div>

            <jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false"/>
        </div>
    </div>

    <div class="container mb-3">
        <table class="table table-form mb-2">
            <tbody class="container-fluid">
            <tr class="row no-gutters">
                <th class="col col-lg-2 col-4">
                    일괄고지서 파일생성
                    <a tabindex="0" class="popover-dismiss ml-2" role="button" data-toggle="popover"
                       data-trigger="focus"
                       title="일괄고지서 파일생성이란?"
                       data-content="조회결과 테이블에 조회된 모든 고지서 출력 대상을 하나의 PDF파일로 생성하여 제공 (대량출력을 보다 쉽게 처리 가능)">
                        <img src="/assets/imgs/common/icon-info.png">
                    </a>
                </th>
                <td class="col col-lg-10 col-8"><a href="#" id="pdfView"
                                                   onclick="fnPdfView('${map.pdfFile}')">${map.pdfFile}</a>
                    <button class="btn btn-sm btn-d-gray ml-3" onclick="fnAllPrint()">파일생성</button>
                </td>
            </tr>
            </tbody>
        </table>
        <div class="guide-mention mb-5">
            * 고지서화면이 보이지 않을 경우 PC에 Acrobat Reader를 설치하세요.
            <a href="https://get.adobe.com/reader/?loc=kr" target="_blank" class="btn btn-acrobat-reader-download">
                <img src="/assets/imgs/common/icon-acrobat-reader.png"> ACROBAT READER 설치
            </a>
        </div>
    </div>

    <div class="container">
        <div id="quick-instruction" class="foldable-box">
            <div class="row foldable-box-header">
                <div class="col-8">
                    <img src="/assets/imgs/common/icon-notice.png"> 알려드립니다.
                </div>
                <div class="col-4 text-right">
                    <img src="/assets/imgs/common/btn-arrow-notice.png" class="fold-status">
                </div>
            </div>
            <div class="row foldable-box-body">
                <div class="col">
                    <h6>■ 고지서 인쇄</h6>
                    <ul>
                        <li>조회결과 좌측의 체크박스 선택 후 하단 우측에 '고지서인쇄' 버튼 클릭하여 파일 다운로드</li>
                        <li class="text-danger">고지서인쇄 전에 [고지관리>고지서설정] 메뉴에서 고지서 내용을 입력 필수 (E-MAIL고지서와 동일)</li>
                    </ul>

                    <h6>■ 일괄고지서 파일생성</h6>
                    <ul>
                        <li>고지출력을 원하는 청구 건을 조회 후 하단 '일괄고지서 파일생성' 버튼 클릭</li>
                        <li>생성된 일괄 고지서 파일명을 클릭하여 다운로드 (생성할 고지건이 많을 경우 5분 이상 소요될 수 있음)</li>
                    </ul>

                    <h6>■ 문자메시지고지</h6>
                    <ul>
                        <li>조회결과 좌측의 체크박스 선택 후 하단 우측에 '문자메시지고지' 버튼 클릭</li>
                        <li>발송 내용(청구, 미납 등)을 선택하거나 직접 입력하여 '문자메시지 발송'</li>
                        <li class="text-danger">과학기술정보통신부, 통신3사 스팸 규제 정책에 의해 불법 문자, 스미싱 문자 발송으로</li>
                        <li class="text-danger">인한 일일 문자발송제한 정책이 강화되어 평일 및 공휴일 23:00~04:00 일 5천건이상 발송이 제한됩니다.</li>
                        <li class="text-danger">(청구 및 입금 시 자동 발송은 포함되지 않습니다.)</li>
                    </ul>

                    <h6>■ E-MAIL 고지</h6>
                    <ul>
                        <li>조회결과 좌측의 체크박스 선택 후 하단 우측에 'E-MAIL고지' 버튼 클릭</li>
                        <li>발송 고지서에 추가할 안내문구를 선택</li>
                        <li class="text-danger">E-MAIL고지서 발송 전에 [고지관리>고지서설정] 메뉴에서 고지서 내용을 입력 필수 (출력고지서와 동일)</li>
                    </ul>

                    <h6>■ 알림톡 고지</h6>
                    <ul>
                        <li>조회결과 좌측의 체크박스 선택 후 하단 우측에 '알림톡고지' 버튼 클릭</li>
                        <li>알림톡 고지서 발송 전에 [고지관리>고지상세설정] 메뉴에서 발송 고지서 문구 미리보기 확인 필수</li>
                        <li>발송실패시 SMS, LMS 등 타 발송수단으로 재전송되지 않습니다.</li>
                    </ul>

                    <h6>■ 문자/출력/E-MAIL 고지 문구 설정</h6>
                    <ul>
                        <li>[마이페이지>고지상세설정>문자메시지] 메뉴에서 문자메시지고지 문구를 설정</li>
                        <li>[마이페이지>고지상세설정>출력/E-MAIL] 메뉴에서 출력 및 E-MAIL고지 문구를 설정</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/>
<%--고객구분 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/payer-classification.jsp" flush="false"/>

<%-- 문자메시지 서비스 이용등록 --%>
<jsp:include page="/WEB-INF/views/include/modal/reg-sms-service.jsp" flush="false"/>

<%-- sms 고지 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/sms-notification.jsp" flush="false"/>

<%-- E-mail 고지 등록 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/email-notification.jsp" flush="false"/>

<%-- 고객정보수정 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/detail-payer-information.jsp" flush="false"/>

<%-- 청구상세 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/payment-unit-list.jsp" flush="false"/>

<%-- 개별청구등록 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/modify-charge.jsp" flush="false"/>

<%-- 알림톡 서비스 이용등록 --%>
<jsp:include page="/WEB-INF/views/include/modal/reg-at-service.jsp" flush="false"/>

<%-- 알림톡 고지 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/at-notification.jsp" flush="false"/>
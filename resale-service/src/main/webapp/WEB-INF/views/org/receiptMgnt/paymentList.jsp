<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<s:authentication property="principal.username" var="userId"/>
<s:authentication property="principal.name" var="userName"/>

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
    var cuPage = 1;
    var currPage = 1;
    var toDay = getFormatCurrentDate();
    var firstDepth = "nav-link-34";
    var secondDepth = "sub-02";
    var cusGubn;

    var showType = 'cash';

    $(document).ready(function () {
        getYearsBox('yearsBox');
        getMonthBox('monthBox');
        getYearsBox('fYearsBox');
        getMonthBox('fMonthBox');
        getYearsBox('tYearsBox');
        getMonthBox('tMonthBox');
        getYearsBox2('cardYearsBox');
        getMonthBox('cardMonthBox');
        cusGubnBox('cusGubn');

        $('#showCash').click(function () {
            fnSearch();
            showType = 'cash';
            $('.cashDct').css('display', '');
            $('.refundDct').css('display', 'none');
        });
        $('#showRefund').click(function () {
            cardPaySearch();
            showType = 'refund';
            $('.cashDct').css('display', 'none');
            $('.refundDct').css('display', '');
        });

        $('#fDate').val(getPrevDate(toDay, 1));
        $('#tDate').val(toDay);

        fnSearch();
    });

    function onSearchGb() {
        if ($('#searchGb option:selected').val() == "vano") {
            $('#searchvalue').val("");
        }
    }

    // 검색
    function fnSearch(page) {
        var masmonth = null;
        var startDate = null;
        var endDate = null;
        // 정렬 구분
        var orderBy = $('#orderBy').val();

        if (page == null || page == 'undefined') {
            cuPage = "";
            cuPage = 1;
        } else {
            cuPage = page;
        }

        if($('#searchGb option:selected').val() == "vano" && $("#searchValue").val()) {
            var str = $("#searchValue").val();
            var s = str.split(",");
            for(var i = 0; i < s.length; i++) {
                if(($.isNumeric(s[i]) && (s[i].length < 2 || s[i].length > 4)) && s[i].length != 14) {
                    swal({
                        type: 'info',
                        html: "가상계좌번호 14자리 모두 입력 또는 <p>가상계좌 끝 4자리 중 최소 2자리 이상, 4자리 이하로 입력해 주세요.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                    return;
                }
                if(!$.isNumeric(s[i])) {
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

        if ($('#virtual-account input:radio[name=inlineRadioOptions-acct]:checked').val() == 'month') {
            startDate = null;
            endDate = null;
            masmonth = $('#virtual-account #yearsBox option:selected').val() + "" + $('#virtual-account #monthBox option:selected').val();
        } else {

            startDate = replaceDot($('#virtual-account #fDate').val());
            endDate = replaceDot($('#virtual-account #tDate').val());
            masmonth = null;

            var vdm = dateValidity(startDate, endDate);
            if (vdm != 'ok'){
                swal({
                    type: 'info',
                    text: vdm,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return false;
            }
        }

        var url = '/org/receiptMgnt/paymentListAjax';
        var param = {
            masMonth: masmonth,
            startDate: startDate,
            endDate: endDate,
            searchGb: $('#searchGb option:selected').val(), //검색구분(cusname:고객명, vano:계좌번호)
            searchValue: $('#searchValue').val(),
            cusGubn : $('#cusSearchGubn option:selected').val(), //고객구분
            cusGubnValue : $('#cusSearchGubnValue').val(),
            curPage: cuPage,
            pageScale: $('#pageScale option:selected').val(),
            payItemCd: $('#claimItemCd option:selected').val(),	//청구항목
            orderBy: orderBy
        };

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

    // 데이터 새로고침
    function fnGrid(result, obj) {
        var str = '';
        $('#pageCnt').val(result.PAGE_SCALE);
        $('#totCnt').text(result.count);
        $('#totAmt').text(numberToCommas(result.totAmt));

        if (result.count <= 0) {
            str += '<tr><td colspan="13" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
            $('#' + obj).html(str);
            return;
        }

        $.each(result.list, function (i, v) {
            str += '<tr>';
            str += '<td>' + v.rn + '</td>';
            str += '<td>' + v.masMonth.substring(0, 4) + '.' + v.masMonth.substring(4, 6) + '</td>';
            if(v.payDay != null){
                str += '<td>' + changeDateFormat(v.payDay) + ' ' + v.payTime.substr(0, 2) + ':' + v.payTime.substr(2, 2) + ':' + v.payTime.substr(4, 2) +'</td>';
            }else{
                str += '<td></td>';
            }
            str += '<td><button type="button" class="btn btn-xs btn-link" onclick="showDetails(\''+v.rcpMasCd+'\');">' + basicEscape(v.cusName) + '</button></td>';
            str += '<td>' + v.vaNo + '</td>';
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
            str += '<td class="text-right">' + numberToCommas(v.rcpAmt) + '</td>';
            str += '<td>' + nullValueChange(v.rcpUserName) + '</td>';
            if (v.bnkCd != null && v.bnkCd != ' ') {
                str += '<td>' + nullValueChange(v.bnkCd) + '</td>';
            }else{
                str += '<td>' + nullValueChange(v.ficd) + '</td>';
            }
            str += '</tr>';
        });
        $('#' + obj).html(str);
    }

    function showDetails(rcpMasCd) {
        var url = '/org/receiptMgnt/paymentDetailsAjax';
        var params = {
            rcpMasCd : rcpMasCd
        };

        $.getJSON(url, params, function(data) {
            if(!data.customer && !data.noticeMaster && !data.noticeDetailsList) {
                // FIXME SWAL로 교체
                alert('데이터가 없습니다');
                return;
            }

            fn_reset_scroll();

            var $popup = $('#detail-acct-information');

            // customer
            var customer = data.customer;
            var $customer = $popup.find('.customer');
            if(customer) {
                $customer.show();
                $customer.find('.data-name').text(customer.cusName);
                $customer.find('.data-register-type').text(customer.cusKey);

                if(customer.rcpGubn == "Y") {
                    $customer.find('.data-notice-target').text('대상');
                }  else {
                    $customer.find('.data-notice-target').text('제외');
                }
                $customer.find('.data-vano').text(customer.vano);
                $customer.find('.data-contact-no').text(customer.cusHp);
                $customer.find('.data-email').text(customer.cusMail);
                $customer.find('.data-category-1').text(customer.cusGubn1);
                $customer.find('.data-category-2').text(customer.cusGubn2);
                $customer.find('.data-category-3').text(customer.cusGubn3);
                $customer.find('.data-category-4').text(customer.cusGubn4);
            } else {
                $customer.hide();
            }

            // notice
            var notice = data.noticeMaster;
            var $notice = $popup.find('.notice');
            if(notice) {
                $notice.show();
                $notice.find('.data-month').text(moment(notice.MASMONTH, 'YYYYMM').format('YYYY.MM'));
                $notice.find('.data-date').text(moment(notice.MASDAY, 'YYYYMMDD').format('YYYY.MM.DD'));
                $notice.find('.date-period').text(moment(notice.STARTDATE, 'YYYYMMDD').format('YYYY.MM.DD') + '~' + moment(notice.ENDDATE, 'YYYYMMDD').format('YYYY.MM.DD'));
                notice.PRINTDATE && $notice.find('.data-due-date').text(moment(notice.PRINTDATE, 'YYYYMMDD').format('YYYY.MM.DD'));
            } else {
                $notice.hide();
            }

            // notice details
            var noticeDetails = data.noticeDetailsList;
            var $noticeDetails = $popup.find('.notice-details');
            if(notice) {
                $noticeDetails.show();
                $noticeDetails.find('.item-list').empty();
                noticeDetails.forEach(function(v, i) {
                    var $tr = $('<tr></tr>').addClass('item');
                    $('<td></td>').text(i+1).appendTo($tr);
                    $('<td></td>').addClass('table-title-ellipsis').text(v.PAYITEMNAME).appendTo($tr);
                    $('<td></td>').addClass('text-right').text(numberToCommas(v.PAYAMT)).appendTo($tr);
                    $('<td></td>').addClass('text-right').text(numberToCommas(v.RCPAMT)).appendTo($tr);

                    $noticeDetails.append($tr);
                });

                var totalNoticeAmount = noticeDetails.reduce(function(pv, cv) {
                    pv += cv.PAYAMT || 0;

                    return pv;
                }, 0);
                var totalPaymentAmount = noticeDetails.reduce(function(pv, cv) {
                    pv += cv.RCPAMT || 0;

                    return pv;
                }, 0);

                $noticeDetails.find('.data-total-notice-amount').text(numberToCommas(totalNoticeAmount));
                $noticeDetails.find('.data-total-payment-amount').text(numberToCommas(totalPaymentAmount));
            } else {
                $noticeDetails.hide();
            }

            $popup.modal({
                backdrop : 'static',
                keyboard : false
            });
        });
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

    function fnCusGubn(str) {
        cusGubn = str.value;
    }

    function pageChange() {
        cuPage = 1;
        fnSearch(cuPage);
    }

    function cardPageChange() {
        cardPaySearch(currPage);
    }

    function cardArrayChange() {
        cardPaySearch(currPage);
    }

    function prevDate(num) {
        toDay = $.datepicker.formatDate("yy.mm.dd", new Date() );
        var vdm = dateValidity($('#fDate').val(), toDay);
        $('#tDate').val(toDay);
        $('#fDate').val(monthAgo(toDay, num));
        $('.btn-preset-month').removeClass('active');
        $('#pMonth' + num + '').addClass('active');
    }

    function prevDate2(num) {
        toDay = $.datepicker.formatDate("yy.mm.dd", new Date() );
        var vdm = dateValidity($('#cardfDate').val(), toDay);
        $('#cardtDate').val(toDay);
        $('#cardfDate').val(monthAgo(toDay, num));
        $('.btn-preset-month').removeClass('active');
        $('#cardpMonth' + num + '').addClass('active');
    }

    // 페이징 버튼
    function list(page) {
        $('#pageNo').val(page);
        fnSearch(page);
    }

    // 페이징 버튼
    function list2(page) {
        cardPaySearch(page);
    }

    // 파일저장
    function fn_fileSave() {
        fncClearTime();
        var startDate='';
        var endDate='';
        var masmonth='';
        var alertResult = false;
        if ($('#totCnt').text() <= 0) {
            swal({
                type: 'info',
                text: '다운로드할 데이터가 없습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else {
            swal({
                type: 'question',
                html: "다운로드 하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                onAfterClose: function() {
                    if(alertResult) {
                        vanofile();
                    }
                }
            }).then(function (result) {
                if(result.value) {
                    alertResult = true;
                }
            });
        };
    };

    // 가상계좌 엑셀파일생성
    function vanofile() {
        if ($('#virtual-account input:radio[name=inlineRadioOptions-acct]:checked').val() == 'month') {
            startDate = null;
            endDate = null;
            masmonth = $('#virtual-account #yearsBox option:selected').val() + "" + $('#virtual-account #monthBox option:selected').val();
        } else {
            startDate = replaceDot($('#virtual-account #fDate').val());
            endDate = replaceDot($('#virtual-account #tDate').val());
            masmonth = null;
        }

        $('#fMasMonth').val(masmonth);
        $('#fStartDate').val(startDate);
        $('#fEndDate').val(endDate);
        $('#fSearchGb').val($('#searchGb option:selected').val());
        $('#fSearchValue').val($('#searchValue').val());
        $('#fCusGubn').val($('#cusGubn option:selected').val());
        $('#fCusGubnValue').val($('#cusSearchGubnValue').val());
        $('#fOrderBy').val($('#orderBy option:selected').val());

        // 다운로드
        //$('#fileForm').submit();

        var url = '/org/receiptMgnt/vasHistoryExcelDown?' + $('#fileForm').serialize();
        window.open(url, '_parent');
 	};
    
    // 온라인카드 파일저장
    function cardfileSave() {
        fncClearTime();
        var startDate='';
        var endDate='';
        var masmonth='';
        var alertResult = false;
        if ($('#cardCount').text() <= 0) {
            swal({
                type: 'info',
                text: '다운로드할 데이터가 없습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else {
            swal({
                type: 'question',
                html: "다운로드 하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                onAfterClose: function() {
                    if(alertResult) {
                        cardfile();
                    }
                }
            }).then(function (result) {
                if(result.value) {
                    alertResult = true;
                }
            });
        }
    };
    
 	// 온라인카드 엑셀파일생성
    function cardfile() {
        if ($('#online-credit-card input:radio[name=inlineRadioOptions-card]:checked').val() == 'month') {
            startDate = null;
            endDate = null;
            masmonth = $('#online-credit-card #cardYearsBox option:selected').val() + "" + $('#online-credit-card #cardMonthBox option:selected').val();
        } else {
            cardfDate = replaceDot($('#cardfDate').val());
            cardtDate = replaceDot($('#cardtDate').val());
            masmonth = null;
            $('#fCardfDate').val(cardfDate);
            $('#fCardtDate').val(cardtDate);
        }

		$('#fCardMonth').val(masmonth);
		$('#fCardSearchValue').val($('#cardSearchValue').val());
		$('#fCardOrderBy').val($('#cardOrderBy option:selected').val());
		$('#fStatusCheck').val($("input[name=statusCheck]:checked").val());
		$('#cusGubn option:selected').val();	// 고객구분 selectbox
		$('#fCardCusGubnValue').val($('#cusGubnValue').val());			// 고객구분 text
		// 다운로드
		//('#cardFileForm').submit();

		var url = '/org/receiptMgnt/cardHistoryExcelDown?' + $('#cardFileForm').serialize();
        window.open(url, '_parent');
	};

    //온라인 카드 조회
    function cardPaySearch(page) {
        var masmonth = null;
        var cardfDate = null;
        var cardtDate = null;
        var seachCusGubn = $('#cusGubn option:selected').val();

        if (page == null || page == 'undefined') {
            currPage = "";
            currPage = 1;
        } else {
            currPage = page;
        }

        if ($('#online-credit-card input:radio[name=inlineRadioOptions-card]:checked').val() == 'month') {
            startDate = null;
            endDate = null;
            masmonth = $('#online-credit-card #cardYearsBox option:selected').val() + "" + $('#online-credit-card #cardMonthBox option:selected').val();
        } else {
            cardfDate = replaceDot($('#online-credit-card #cardfDate').val());
            cardtDate = replaceDot($('#online-credit-card #cardtDate').val());
            masmonth = null;

            var vdm = dateValidity(cardfDate, cardtDate);
            if (vdm != 'ok'){
                swal({
                    type: 'info',
                    text: vdm,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return false;
            }
        }

        var url = "/org/receiptMgnt/cardPayListAjax";

        var param = {
            masMonth: masmonth,
            endDate: cardtDate, 	//조회 종료년월
            startDate: cardfDate,	//조회 시작년월
            curPage: currPage,
            pageScale: $('#cardPageScale option:selected').val(),
            orderBy: $('#cardOrderBy option:selected').val(),
            searchValue: $('#cardSearchValue').val(),
            statusCheck: $("input[name=statusCheck]:checked").val(),
            seachCusGubn: seachCusGubn, 				// 고객구분 selectbox
            cusGubnValue: $('#cusGubnValue').val()
        };

        $.ajax({
            type: "post",
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    cardFnGrid(result, 'cardResultBody'); // 현재 데이터로 셋팅
                    ajaxPaging2(result, 'PageArea2');
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

    // 데이터 새로고침
    function cardFnGrid(result, obj) {
        var nowDays = new Date();
        var str = '';
        $('#cardCount').text(result.cardCount);
        $('#cardTotAmt').text(numberToCommas(result.cardTotAmt));

        if (result.cardCount <= 0) {
            str += '<tr><td colspan="11" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.cardPayList, function (i, v) {
                str += '<tr>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + v.masMonth.substring(0, 4) + '.' + v.masMonth.substring(4, 6) + '</td>';
                str += '<td><button type="button" class="btn btn-xs btn-link" onclick="fnCusAcctInfo(\''+v.rcpMasCd+'\');">' + basicEscape(v.cusName) + '</button></td>';
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
                str += '<td class="text-right">' + numberToCommas(v.payItemAmt) + '</td>';
                str += '<td class="text-right">' + numberToCommas(v.rcpAmt) + '</td>';
                if (v.rcpMasSt == 'PA03') {
                    var payDays = new Date(v.payDay.substring(0, 4), v.payDay.substring(4, 6) - 1, v.payDay.substring(6, 8));
                    var gapDays = Math.floor((nowDays.getTime() - payDays.getTime()) / (1000 * 60 * 60 * 24));
                    str += '<td class="text-success">승인';
                    if (gapDays <= 60) {
                        str += '<button type="button" class="btn btn-xs btn-outline-secondary" onclick=fnRefund("' + v.rcpMasCd + '")>결제취소</button>';
                    }
                    str += '</td>';
                } else if (v.rcpMasSt == 'PA09') {
                    str += '<td class="text-danger">취소</td>';
                }
                str += '<td>' + v.payDay2 + '</td>';
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    //카드결제 취소
    function fnRefund(rcpMasCd) {
        swal({
            type: 'question',
            text: '신용카드 결제내역을 취소하시겠습니까?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                var strFeature = "width=700, height=500, scrollbars=no, resizable=yes";
                document.cancelCardForm.target = "formInfo";
                document.cancelCardForm.cancelRcpMasCd.value = rcpMasCd;
                window.open("", "formInfo", strFeature);
                document.cancelCardForm.submit();

            } else {

            }

        });
    }

    /**
     * 2018.8.3 정미래
     * 입금내역 상세보기 모달창
     */
    function fnCusAcctInfo(rcpMasCd) {
        var url = '/org/receiptMgnt/paymentDetailsAjax';
        var params = {
            rcpMasCd : rcpMasCd
        };

        $.getJSON(url, params, function(data) {
            if(!data.customer && !data.noticeMaster && !data.noticeDetailsList) {
                // FIXME SWAL로 교체
                alert('데이터가 없습니다');
                return;
            }

            fn_reset_scroll();

            var $popup = $('#detail-acct-information');

            // customer
            var customer = data.customer;
            var $customer = $popup.find('.customer');
            if(customer) {
                $customer.show();
                $customer.find('.data-name').text(customer.cusName);
                $customer.find('.data-register-type').text(customer.cusKey);
                if(customer.rcpGubn == 'Y') {
                    $customer.find('.data-notice-target').text('대상');
                }  else {
                    $customer.find('.data-notice-target').text('제외');
                }
                $customer.find('.data-vano').text(customer.vano);
                $customer.find('.data-contact-no').text(customer.cusHp);
                $customer.find('.data-email').text(customer.cusMail);
                $customer.find('.data-category-1').text(customer.cusGubn1);
                $customer.find('.data-category-2').text(customer.cusGubn2);
                $customer.find('.data-category-3').text(customer.cusGubn3);
                $customer.find('.data-category-4').text(customer.cusGubn4);
            } else {
                $customer.hide();
            }

            // notice
            var notice = data.noticeMaster;
            var $notice = $popup.find('.notice');
            if(notice) {
                $notice.show();
                $notice.find('.data-month').text(moment(notice.MASMONTH, 'YYYYMM').format('YYYY.MM'));
                $notice.find('.data-date').text(moment(notice.MASDAY, 'YYYYMMDD').format('YYYY.MM.DD'));
                $notice.find('.date-period').text(moment(notice.STARTDATE, 'YYYYMMDD').format('YYYY.MM.DD') + '~' + moment(notice.ENDDATE, 'YYYYMMDD').format('YYYY.MM.DD'));
                notice.PRINTDATE && $notice.find('.data-due-date').text(moment(notice.PRINTDATE, 'YYYYMMDD').format('YYYY.MM.DD'));
            } else {
                $notice.hide();
            }

            // notice details
            var noticeDetails = data.noticeDetailsList;
            var $noticeDetails = $popup.find('.notice-details');
            if(notice) {
                $noticeDetails.show();
                $noticeDetails.find('.item-list').empty();
                noticeDetails.forEach(function(v, i) {
                    var $tr = $('<tr></tr>').addClass('item');
                    $('<td></td>').text(i+1).appendTo($tr);
                    $('<td></td>').addClass('table-title-ellipsis').text(v.PAYITEMNAME).appendTo($tr);
                    $('<td></td>').addClass('text-right').text(numberToCommas(v.PAYAMT)).appendTo($tr);
                    $('<td></td>').addClass('text-right').text(numberToCommas(v.RCPAMT)).appendTo($tr);

                    $noticeDetails.append($tr);
                });

                var totalNoticeAmount = noticeDetails.reduce(function(pv, cv) {
                    pv += cv.PAYAMT || 0;

                    return pv;
                }, 0);
                var totalPaymentAmount = noticeDetails.reduce(function(pv, cv) {
                    pv += cv.RCPAMT || 0;

                    return pv;
                }, 0);

                $noticeDetails.find('.data-total-notice-amount').text(numberToCommas(totalNoticeAmount));
                $noticeDetails.find('.data-total-payment-amount').text(numberToCommas(totalPaymentAmount));
            } else {
                $noticeDetails.hide();
            }

            $popup.modal({
                backdrop : 'static',
                keyboard : false
            });
        });
    }

    function cusName(vano){
        fnDetail(vano);
    }
</script>
<form id="fileForm" name="fileForm" method="post" action="/org/receiptMgnt/payExcelDown">
    <input type="hidden" id="fMasMonth" name="fMasMonth"/>
    <input type="hidden" id="fStartDate" name="fStartDate"/>
    <input type="hidden" id="fEndDate" name="fEndDate"/>
    <input type="hidden" id="fSearchGb" name="fSearchGb"/>
    <input type="hidden" id="fSearchValue" name="fSearchValue"/>
    <input type="hidden" id="fCusGubn" name="fCusGubn"/>
    <input type="hidden" id="fCusGubnValue" name="fCusGubnValue"/>
    <input type="hidden" id="fOrderBy" name="fOrderBy"/>
</form>

<form id="cardFileForm" name="cardFileForm" method="post" action="/org/receiptMgnt/cardExcelDown">
    <input type="hidden" id="fCardMonth" name="fCardMonth"/>
    <input type="hidden" id="fCardfDate" name="fCardfDate"/>
    <input type="hidden" id="fCardtDate" name="fCardtDate"/>
    <input type="hidden" id="fCardSearchValue" name="fCardSearchValue"/>
    <input type="hidden" id="fStatusCheck" name="fStatusCheck"/>
    <input type="hidden" id="fCardOrderBy" name="fCardOrderBy"/>
    <input type="hidden" id="fCardCusGubnValue" name="fCardCusGubnValue"/>
</form>

<form name="cancelCardForm" id="cancelCardForm" method="post" action="/org/receiptMgnt/selectRcpMas">
    <input type="hidden" id="cancelRcpMasCd" name="cancelRcpMasCd"/>
</form>

<input type="hidden" id="cusGubn1" name="cusGubn1" value="${orgSess.cusGubn1}" />
<input type="hidden" id="cusGubn2" name="cusGubn2" value="${orgSess.cusGubn2}" />
<input type="hidden" id="cusGubn3" name="cusGubn3" value="${orgSess.cusGubn3}" />
<input type="hidden" id="cusGubn4" name="cusGubn4" value="${orgSess.cusGubn4}" />

<input type="hidden" id="pageCnt" name="pageCnt" value="${map.PAGE_SCALE}">
<input type="hidden" id="pageNo" name="pageNo" value="1">

<div id="contents">
    <div id="damoa-breadcrumb">
        <nav class="nav container">
            <a class="nav-link" href="/org/receiptMgnt/receiptList">수납내역 조회</a>
            <a class="nav-link active" href="#">입금내역 조회</a>
            <a class="nav-link" href="/org/receiptMgnt/actualReceiptReg">직접수납관리</a>
            <a class="nav-link" href="/org/receiptMgnt/cashReceipt">현금영수증</a>
        </nav>
    </div>
    <div class="container">
        <div id="page-title">
            <div id="breadcrumb-in-title-area" class="row align-items-center">
                <div class="col-12 text-right">
                    <img src="/assets/imgs/common/icon-home.png" class="mr-2">
                    <span> > </span> <span class="depth-1">수납관리</span> <span> >
					</span> <span class="depth-2 active">입금내역 조회</span>
                </div>
            </div>
            <div class="row">
                <div class="col-6">
                    <h2>입금내역 조회</h2>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div id="page-description">
            <div class="row">
                <div class="col-12">
                    <p class="cashDct">가상계좌로 입금된 내역을 확인할 수 있는 화면입니다.</p>
                    <p class="refundDct" style="display: none;">온라인 카드결제 내역을 확인할 수 있는 화면입니다.<br/>온라인 카드결제 서비스는 신청 고객에 한해 제공합니다.</p>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-12">
                <div class="tab-selecter type-3">
                    <ul class="nav nav-tabs" role="tablist">
                        <li class="nav-item"><a id="showCash" class="nav-link active" data-toggle="tab" href="#virtual-account">가상계좌</a></li>
                        <%--<li class="nav-item"><a id="showRefund" class="nav-link" data-toggle="tab" href="#online-credit-card">온라인카드</a></li>--%>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <div class="container tab-content">
        <div id="virtual-account" class="tab-pane fade show active">
            <div class="search-box">
                <form>
                    <div class="row">
                        <label class="col col-md-1 col-sm-2 col-2 col-form-label">입금일자</label>
                        <div class="col col-md-4 col-sm-8 col-8 form-inline">
                            <div class="date-input">
                                <div class="input-group">
                                    <input type="text" id="fDate" class="form-control date-picker-input" placeholder="YYYY.MM.DD" aria-label="From" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
                                </div>
                            </div>
                            <span class="range-mark"> ~ </span>
                            <div class="date-input">
                                <div class="input-group">
                                    <input type="text" id="tDate" class="form-control date-picker-input" placeholder="YYYY.MM.DD" aria-label="To" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
                                </div>
                            </div>
                        </div>

                        <div class="col-md-6 col-sm-10 offset-md-0 offset-sm-2 offset-2">
                            <button id="pMonth1" type="button" class="btn btn-sm btn-preset-month active" onclick="prevDate(1)">1개월</button>
                            <button id="pMonth2" type="button" class="btn btn-sm btn-preset-month" onclick="prevDate(2)">2개월</button>
                            <button id="pMonth3" type="button" class="btn btn-sm btn-preset-month" onclick="prevDate(3)">3개월</button>
                        </div>
                    </div>

                    <div class="row mt-3">
                        <label class="col col-md-1 col-sm-2 col-2 col-form-label">검색구분</label>
                        <div class="col col-md-4 col-sm-10 col-9 form-inline">
                            <select class="form-control" id="searchGb" onchange="onSearchGb();">
                                <option value="cusname">고객명</option>
                                <option value="vano">계좌번호</option>
                            </select>
                            <input class="form-control" type="text" name="searchValue" id="searchValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch();}"/>
                        </div>

                        <label class="col col-md-1 col-sm-2 col-2 col-form-label">고객구분</label>
                        <div class="col col-md-4 col-sm-10 col-9 form-inline">
                            <select class="form-control" id="cusSearchGubn" name="cusSearchGubn" >
                                <option value="all">전체</option>
                                <c:forEach var="cusGubn" items="${map.cusGbList}">
                                    <option value="${cusGubn.code}">${cusGubn.codeName}</option>
                                </c:forEach>
                            </select>
                            <input class="form-control" type="text" id="cusSearchGubnValue"  name="cusSearchGubnValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." maxlength="30" onkeypress="if(event.keyCode == 13){fnSearch();}"/>
                        </div>
                    </div>


                    <div class="row form-inline mt-3">
                        <div class="col-12 text-center">
                            <button type="button" class="btn btn-primary btn-wide" onclick="fnSearch()">조회</button>
                        </div>
                    </div>
                </form>
            </div>

            <div id="income-list" class="list-id">
                <div class="table-option row mb-2">
                    <div class="col-md-6 col-sm-12 form-inline">
                        <span class="amount mr-2">조회결과 [총 <em class="font-blue" id="totCnt">${map.count}</em>건]
                        </span> <span class="amount">입금금액 [총 <em class="font-blue" id="totAmt"><fmt:formatNumber pattern="#,###" value="${map.totAmt}"/></em>원]</span>
                    </div>
                    <div class="col-md-6 col-sm-12 text-right mt-1">
                        <select class="form-control" id="orderBy" onchange="pageChange();">
                            <option value="rcpDate_desc">입금일순 정렬</option>
                            <option value="rcpName_asc">입금자명순 정렬</option>
                            <option value="cusName_asc">고객명순 정렬</option>
                        </select>
                        <select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
                            <option value="10" <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회</option>
                            <option value="20" <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회</option>
                            <option value="50" <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회</option>
                            <option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회</option>
                            <option value="200" <c:if test="${map.PAGE_SCALE == '200'}">selected</c:if>>200개씩 조회</option>
                        </select>
                        <button class="hidden-on-mobile btn btn-sm btn-d-gray" type="button" onclick="fn_fileSave()">파일저장</button>
                    </div>
                </div>
                <div class="row">
                    <div class="table-responsive pd-n-mg-o col mb-3">
                        <table class="table table-sm table-hover table-primary">
                            <thead>
                            <tr>
                                <th>NO</th>
                                <th>청구월</th>
                                <th>입금일시</th>
                                <th>고객명</th>
                                <th>가상계좌</th>
                                <c:if test="${orgSess.cusGubn1 != '' && orgSess.cusGubn1 != null}"><th>${orgSess.cusGubn1}</th></c:if>
                                <c:if test="${orgSess.cusGubn2 != '' && orgSess.cusGubn2 != null}"><th>${orgSess.cusGubn2}</th></c:if>
                                <c:if test="${orgSess.cusGubn3 != '' && orgSess.cusGubn3 != null}"><th>${orgSess.cusGubn3}</th></c:if>
                                <c:if test="${orgSess.cusGubn4 != '' && orgSess.cusGubn4 != null}"><th>${orgSess.cusGubn4}</th></c:if>
                                <th>입금액(원)</th>
                                <th>입금자명</th>
                                <th>입금은행</th>
                            </tr>
                            </thead>

                            <tbody id="resultBody">

                            </tbody>
                        </table>
                    </div>
                </div>

                <jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false"/>
            </div>
        </div>

        <div id="online-credit-card" class="tab-pane fade show">
            <div class="search-box">
                <form>
                    <div id="monthly-or-duration-card" class="row radio-selecter">
                        <legend class="col-form-label col col-md-1 col-sm-2 col-2 pt-0">조회방식</legend>
                        <div class="col col-md-10 col-sm-10 col-9">
                            <div class="form-check form-check-inline">
                                <input class="form-check-input lookup-by-month-card" type="radio"
                                       name="inlineRadioOptions-card" id="inlineRadio3" value="month" checked="checked">
                                <label class="form-check-label" for="inlineRadio3"><span></span>청구월별</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input lookup-by-range-card" type="radio"
                                       name="inlineRadioOptions-card" id="inlineRadio4" value="period">
                                <label class="form-check-label" for="inlineRadio4"><span></span>결제일자별</label>
                            </div>
                        </div>
                    </div>
                    <div id="lookup-by-month-card" class="row">
                        <div class="col-md-11 offset-md-1 offset-sm-2 offset-2 col-10 form-inline year-month-dropdown">
                            <%-- <select class="form-control" id="yearsFullBox">
                            <option selected="selected" value="ALL">전체월</option> --%>
                            <select class="form-control" id="cardYearsBox">
                                <option>선택</option>
                            </select>
                            <span class="ml-1 mr-1">년</span>
                            <select class="form-control" id="cardMonthBox">
                                <option>선택</option>
                            </select>
                            <span class="ml-1">월</span>
                        </div>
                    </div>

                    <div id="lookup-by-range-card" class="row" style="display: none;">
                        <div class="col col-md-5 col-sm-10 offset-md-1 offset-sm-2 offset-2 form-inline">
                            <div class="date-input">
                                <label class="sr-only" for="inlineFormInputGroupUsername">From</label>
                                <div class="input-group">
                                    <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD"
                                           aria-label="From" aria-describedby="basic-addon2" id="cardfDate"
                                           value="${map.cardfDate}" maxlength="8" onkeydown="onlyNumber(this)">
                                </div>
                            </div>
                            <span class="range-mark"> ~ </span>
                            <div class="date-input">
                                <label class="sr-only" for="inlineFormInputGroupUsername">To</label>
                                <div class="input-group">
                                    <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD"
                                           aria-label="To" aria-describedby="basic-addon2" id="cardtDate"
                                           value="${map.cardtDate}" maxlength="8" onkeydown="onlyNumber(this)">
                                </div>
                            </div>
                        </div>

                        <div class="col-md-6 col-sm-10 offset-md-0 offset-sm-2 offset-2">
                            <button id="cardpMonth1" type="button" class="btn btn-sm btn-preset-month active"
                                    onclick="prevDate2(1)">1개월
                            </button>
                            <button id="cardpMonth2" type="button" class="btn btn-sm btn-preset-month"
                                    onclick="prevDate2(2)">2개월
                            </button>
                            <button id="cardpMonth3" type="button" class="btn btn-sm btn-preset-month"
                                    onclick="prevDate2(3)">3개월
                            </button>
                        </div>
                    </div>

                    <div class="row mb-3 mt-3">
                        <div class="col" style="border-top: 1px solid #d6d6d6;"></div>
                    </div>

                    <div class="row">
                        <label class="col col-md-1 col-sm-2 col-2 col-form-label">
                            검색구분
                        </label>
                        <div class="col col-md-4 col-sm-10 col-9 form-inline">
                            <select class="form-control">
                                <option value="">고객명</option>
                            </select>
                            <input class="form-control" type="text" name="cardSearchValue" id="cardSearchValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){cardPaySearch();}"/>
                        </div>
                        <label class="col col-md-1 col-sm-2 col-2 col-form-label">
                            결제상태
                        </label>
                        <div class="col col-md-4 col-sm-10 col-9 form-inline">
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="statusCheck" id="payRadio1"
                                       value="ALL" checked="checked">
                                <label class="form-check-label" for="payRadio1"><span class="mr-1"></span>전체</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="statusCheck" id="payRadio2"
                                       value="PA03">
                                <label class="form-check-label" for="payRadio2"><span class="mr-1"></span>승인</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="statusCheck" id="payRadio3"
                                       value="PA09">
                                <label class="form-check-label" for="payRadio3"><span class="mr-1"></span>취소</label>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <label class="col col-md-1 col-sm-2 col-2 col-form-label">
                            고객구분
                        </label>
                        <div class="col col-md-4 col-sm-10 col-9 form-inline">
                            <select class="form-control" id="cusGubn" name="cusGubn" >
                                <option value="all">전체</option>
                                <c:forEach var="cusGubn" items="${map.cusGbList}">
                                    <option value="${cusGubn.code}">${cusGubn.codeName}</option>
                                </c:forEach>
                            </select>
                            <input class="form-control" type="text" id="cusGubnValue"  name="cusGubnValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." maxlength="30" onkeypress="if(event.keyCode == 13){cardPaySearch();}"/>
                        </div>
                    </div>
                    <div class="row form-inline mt-3">
                        <div class="col-12 text-center">
                            <button type="button" class="btn btn-primary btn-wide" onclick="cardPaySearch();">조회
                            </button>
                        </div>
                    </div>
                </form>
            </div>

            <div id="search-result">
                <div class="table-option row mb-2">
                    <div class="col-md-6 col-sm-12 form-inline">
						<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="cardCount">${map.cardCount}</em>건]
						</span> <span class="amount">결제금액 [총 <em class="font-blue" id="cardTotAmt"><fmt:formatNumber
                            pattern="#,###" value="${map.cardTotAmt}"/></em>원]
						</span>
                    </div>
                    <div class="col-md-6 col-sm-12 text-right mt-1">
                        <select class="form-control" id="cardOrderBy" onchange="cardArrayChange();">
                            <option value="payDt">결제일순 정렬</option>
                            <option value="masMonth">청구월순 정렬</option>
                        </select>
                        <select class="form-control" name="cardPageScale" id="cardPageScale"
                                onchange="cardPageChange();">
                            <option value="10" <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회</option>
                            <option value="20" <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회</option>
                            <option value="50" <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회</option>
                            <option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회
                            </option>
                            <option value="200" <c:if test="${map.PAGE_SCALE == '200'}">selected</c:if>>200개씩 조회</option>
                        </select>
                        <button class="hidden-on-mobile btn btn-sm btn-d-gray" type="button" onclick="cardfileSave()">파일저장</button>
                    </div>
                </div>
                <div class="row">
                    <div class="table-responsive pd-n-mg-o col mb-3">
                        <table class="table table-sm table-hover table-primary">
                            <colgroup>
                                <col width="68">
                                <col width="130">
                                <col width="130">
                                <col width="150">

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

                                <col width="170">
                                <col width="170">
                                <col width="142">
                                <col width="150">
                            </colgroup>

                            <thead>
                            <tr>
                                <th>NO</th>
                                <th>청구월</th>
                                <th>고객명</th>
                                <c:if test="${orgSess.cusGubn1 != '' && orgSess.cusGubn1 != null}">
                                    <th>${orgSess.cusGubn1}</th>
                                </c:if>
                                <c:if test="${orgSess.cusGubn2 != '' && orgSess.cusGubn2 != null}">
                                    <th>${orgSess.cusGubn2}</th>
                                </c:if>
                                <c:if test="${orgSess.cusGubn3 != '' && orgSess.cusGubn3 != null}">
                                    <th>${orgSess.cusGubn3}</th>
                                </c:if>
                                <c:if test="${orgSess.cusGubn4 != '' && orgSess.cusGubn4 != null}">
                                    <th>${orgSess.cusGubn4}</th>
                                </c:if>
                                <th>청구금액(원)</th>
                                <th>결제금액(원)</th>
                                <th class="border-r-e6">결제상태</th>
                                <th class="border-l-n">결제일자</th>
                            </tr>
                            </thead>
                            <tbody id="cardResultBody">

                            </tbody>
                        </table>
                    </div>
                </div>

                <jsp:include page="/WEB-INF/views/include/paging2.jsp" flush="false"/>
            </div>

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
                    <span class="cashDct">
                        <h6>■ 입금내역 조회</h6>
                        <ul>
                            <li>선택 조회 조건 입력 후 조회 버튼 클릭</li>
                            <li>정렬기준 입금일시는 최근순, 고객명은 가나다순</li>
                        </ul>
                    </span>

                    <span class="refundDct" style="display: none;">
                        <h6>■ 신용카드 취소 기능</h6>
                        <ul>
                            <li>온라인에서 결제한 카드 내역일 경우 결제취소 가능</li>
                            <li>신용카드 취소를 원하는 대상 선택 후 '결제취소' 버튼 클릭</li>
                            <li>카드취소 팝업 창에서 가맹점 카드 취소 비밀번호 입력 후 진행</li>
                        </ul>
                    </span>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/>

<%-- 입금내역 상세보기 모달창 --%>
<jsp:include page="/WEB-INF/views/include/modal/detail-acct-information.jsp" flush="false"/>


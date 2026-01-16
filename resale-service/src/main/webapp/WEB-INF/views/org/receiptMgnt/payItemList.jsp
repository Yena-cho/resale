<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:authentication property="principal.username" var="userId" />
<s:authentication property="principal.name" var="userName" />

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false" />

<script type="text/javascript">
    var cuPage = 1;
    var toDay = moment(new Date).format('YYYYMMDD');
    var firstDepth = "nav-link-34";
    var secondDepth = "sub-03";
    var cusGubn;
</script>

<script type="text/javascript">
    $(document).ready(function () {
        getYearsBox2('yearsBox');
        getMonthBox('monthBox');
        cusGubnBox('cusGubn');
        $('#deposRadio1').prop('checked', true);
        $('input:checkbox[name=deposItem]').prop('checked', true);

        $('#fDate').val(moment(toDay).add(-1, 'month').format('YYYYMMDD'));
        $('#tDate').val(toDay);


        $('#deposRadio1').click(function () {
            if ($(this).prop('checked')) {
                $('input:checkbox[name=deposItem]').prop('checked', true);
            } else {
                $('input:checkbox[name=deposItem]').prop('checked', false);
            }
        });

        $('#deposRadio1').click(function(){
            if ($(this).prop('checked')) {
                $('input:checkbox[name=deposItem]').prop('checked', true);
            } else {
                $('input:checkbox[name=deposItem]').prop('checked', false);
            }
        });

        $('input:checkbox[name=deposItem]').click(function () {
            if ($('#deposRadio1').is(":checked")) {
                $('#deposRadio1').prop("checked", false);
            }
            if ($('input:checkbox[name=deposItem]').length == $('input:checkbox[name=deposItem]:checked').length) {
                $('#deposRadio1').prop("checked", true);
            }
        });

        var selGb = "month";
        if ($("#inlineRadio2").is(":checked")) {  //기간별
            selGb = "month";
        } else {
            selGb = "period";
            prevDate(1);
        }
        fnSearch();
    });

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

    function cusName(vano) {
        fnDetail(vano);
    }

    // 검색
    function fnSearch(page) {
        var svecdArr = [];
        var masmonth = null;
        var startDate = null;
        var endDate = null;

        if (page == null || page == 'undefined') {
            cuPage = "";
            cuPage = 1;
        } else {
            cuPage = page;
        }

        $('input:checkbox[name=deposItem]:checked').each(function (i) {
            if ($(this).val() == 'DCS') {
                svecdArr.push($(this).val());
                svecdArr.push('DVA');
            } else {
                svecdArr.push($(this).val());
            }
        });

        if ($('input:radio[name=inlineRadioOptions]:checked').val() == 'month') {
            startDate = null;
            endDate = null;
            masmonth = $('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val();
        } else {
            masmonth = null;
            startDate = replaceDot($('#fDate').val());
            endDate = replaceDot($('#tDate').val());
        }

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
            var url = "/org/receiptMgnt/payItemListAjax";
            var param = {
                masMonth: masmonth,
                startDate: startDate,
                endDate: endDate,
                dateGb: $('input:radio[name=inlineRadioOptions]:checked').val() == 'month' ? 'M' : 'D',
                searchGb: $('#searchGb option:selected').val(),
                searchValue: $('#searchValue').val(),
                svecdList: svecdArr,
                svecdGb1: $('#deposRadio1').is(":checked") ? "ALL" : "NOTALL",
                cusGubn: $('#cusGubn option:selected').val(),
                cusGubnValue: $('#cusGubnValue').val(),
                payItem: $("#payItem option:selected").val() != "" ? $("#payItem option:selected").val() : null,
                adjFiregKey : $("#moneyPassbookName option:selected").val() != "" ? $("#moneyPassbookName option:selected").val() : null,
                chaCd: $('#chaCd').val(),
                curPage: cuPage,
                pageScale: $('#pageScale option:selected').val(),
                sortGb: $('#sortGb option:selected').val()
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
                        ajaxPaging(result, 'PageArea');   // 페이징
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
    }

    function fnGrid(result, obj) {
        var str = '';
        $('#pageCnt').val(result.PAGE_SCALE);
        $('#totCnt').text(numberToCommas(result.totalCnt));
        $('#totAmt').text(numberToCommas(result.totalAmt));

        if (result.totalCnt <= 0) {
            str += '<tr><td colspan="13" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + moment(v.masMonth, 'YYYYMM').format('YYYY.MM')+ '</td>';
                str += '<td>' + moment(v.payDay).format('YYYY.MM.DD') + " " + moment(v.payTime, 'hhmmss').format('HH:mm') + '</td>';
                str += '<td><button type="button" class="btn btn-xs btn-link" onclick="cusName(\'' + v.vaNo + '\')" value="' + v.vaNo + '">' + basicEscape(v.cusName) + '</button></td>';
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
                str += '<td>' + v.payItemName + '</td>';
                str += '<td class="text-right">' + numberToCommas(v.payAmt) + '</td>';
//                str += '<td>' + v.ptrItemRemark + '</td>';
                if ("VAS" == v.sveCd) {
                    str += '<td>가상계좌</td>';
                }
                if ("DCS" == v.sveCd) {
                    str += '<td>현금</td>';
                }
                if ("DCD" == v.sveCd) {
                    str += '<td>오프라인카드</td>';
                }
                if ("DVA" == v.sveCd) {
                    str += '<td>무통장입금</td>';
                }
                /*if ("OCD" == v.sveCd) {
                    str += '<td>온라인카드</td>';
                }*/
                if ($("#adjAccYn").val() == 'Y') {
                    str += '<td>' + nullValueChange(v.grpadjName) + '</td>';
				}
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    // 페이징 버튼
    function list(page) {
        $('#pageNo').val(page);
        fnSearch(page);
    }

    // validation
    function fnValidation() {
        if ($('input:radio[name=inlineRadioOptions]:checked').val() == 'month') {
            return true;
        } else {
            var startDate = replaceDot($('#fDate').val());
            var endDate = replaceDot($('#tDate').val());
            var calcDate = CalcMonth(startDate, endDate);

            if (CalcDay(startDate, endDate) > 365) {
                swal({
                    type: 'info',
                    text: "최대 조회기간은 1년 입니다.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return false;
            }
            var vdm = dateValidity(startDate, endDate);
            if (vdm != 'ok') {
                swal({
                    type: 'info',
                    text: vdm,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return false;
            } else {
                return true;
            }
        }
    }

    function prevDate(num) {
        var toDay = $.datepicker.formatDate("yy.mm.dd", new Date());
        var vdm = dateValidity($('#fDate').val(), toDay);

        $('#tDate').val(toDay);
        $('#fDate').val(monthAgo(toDay, num));
        $('.btn-preset-month').removeClass('active');
        $('#pMonth' + num + '').addClass('active');
    }

    function pageChange() {
        cuPage = 1;
        fnSearch(cuPage);
    }

    function fnCusGubn(str) {
        cusGubn = str.value;
        if (str.value == 'all') {
        } else {
            $('#cusGubnValue').attr('placeholder', '콤마(,) 구분자로 다중검색이 가능합니다.');
        }
    }

    function fn_fileSave() {
        fncClearTime();
        var alertResult = false;
        if($('#totCnt').text() <= 0){
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
                        fnReceipFile();
                    }
                }
            }).then(function(result) {
                if (result.value) {
                    alertResult = true;
                }
            });
        }
    }

    function fnReceipFile() {
        var svecdArr = [];

        $('input:checkbox[name=deposItem]:checked').each(function (i) {
            if ($(this).val() == 'DCS') {
                svecdArr.push($(this).val());
                svecdArr.push('DVA');
            } else {
                svecdArr.push($(this).val());
            }
        });

        if ($('input:radio[name=inlineRadioOptions]:checked').val() == 'month') {
            $('#startDate').val('');
            $('#endDate').val('');
            $('#masMonth').val($('#yearsBox option:selected').val() + "" + $('#monthBox option:selected').val());
        } else {
            $('#masMonth').val('');
            startDate = replaceDot($('#fDate').val());
            endDate = replaceDot($('#tDate').val());
            $('#startDate').val(replaceDot($('#fDate').val()));
            $('#endDate').val(replaceDot($('#tDate').val()));
        }

        $('#fSearchGb').val($('#searchGb option:selected').val());
        $('#fSearchValue').val($('#searchValue').val());
        $('#svecdList').val(svecdArr);
        $('#fCusGubn').val($('#cusGubn option:selected').val());
        $('#fCusGubnValue').val($('#cusGubnValue').val());
        $('#fPayItem').val($("#payItem option:selected").val());
        $('#fAdjFiregKey').val($("#moneyPassbookName option:selected").val());
        $('#fSortGb').val($('#sortGb option:selected').val());

        //$('#fileForm').submit();

        var url = '/org/receiptMgnt/payitemHistoryExcelDown?' + $('#fileForm').serialize();
        window.open(url, '_parent');
    }
</script>

<input type="hidden" id="vano" name="vano"/>
<%--<input type="hidden" id="userName" name="userName" value="${userName}" />--%>
<input type="hidden" id="chaCd" name="chaCd" value="${orgSess.chacd}" />
<input type="hidden" id="cusGubn1" name="cusGubn1" value="${orgSess.cusGubn1}" />
<input type="hidden" id="cusGubn2" name="cusGubn2" value="${orgSess.cusGubn2}" />
<input type="hidden" id="cusGubn3" name="cusGubn3" value="${orgSess.cusGubn3}" />
<input type="hidden" id="cusGubn4" name="cusGubn4" value="${orgSess.cusGubn4}" />
<input type="hidden" id="pageCnt" name="pageCnt" value="${map.PAGE_SCALE}">
<input type="hidden" id="pageNo"  name="pageNo"  value="1">
<input type="hidden" id="adjAccYn" name="adjAccYn" value="${orgSess.adjAccYn}">

<form id="fileForm" name="fileForm" method="post" action="/org/receiptMgnt/payItemExcelDown">
	<input type="hidden" id="masMonth" name="masMonth"/>
	<input type="hidden" id="startDate" name="startDate"/>
	<input type="hidden" id="endDate" name="endDate"/>
	<input type="hidden" id="fSearchGb" name="fSearchGb"/>
	<input type="hidden" id="fSearchValue" name="fSearchValue"/>
	<input type="hidden" id="svecdList" name="svecdList"/>
	<input type="hidden" id="fCusGubn" name="fCusGubn"/>
	<input type="hidden" id="fCusGubnValue" name="fCusGubnValue"/>
	<input type="hidden" id="fPayItem" name="fPayItem"/>
	<input type="hidden" id="payItemCd" name="payItemCd"/>
	<input type="hidden" id="fAdjFiregKey" name="fAdjFiregKey"/>
	<input type="hidden" id="fSortGb" name="fSortGb"/>
</form>

<div id="contents">
	<div id="damoa-breadcrumb">
		<nav class="nav container">
			<a class="nav-link active" href="#">수납내역 조회</a>
			<a class="nav-link" href="/org/receiptMgnt/paymentList">입금내역 조회</a>
			<a class="nav-link" href="/org/receiptMgnt/payItemList">항목별 입금내역</a>
			<a class="nav-link" href="/org/receiptMgnt/actualReceiptReg">직접수납관리</a>
			<a class="nav-link" href="/org/receiptMgnt/cashReceipt">현금영수증</a>
		</nav>
	</div>

	<div class="container">
		<div id="page-title">
			<div id="breadcrumb-in-title-area" class="row align-items-center">
				<div class="col-12 text-right">
					<img src="/assets/imgs/common/icon-home.png" class="mr-2">
					<span> > </span> <span class="depth-1">수납관리</span>
					<span> > </span> <span class="depth-2 active">항목별 입금내역</span>
				</div>
			</div>
			<div class="row">
				<div class="col-12">
					<h2>항목별 입금내역</h2>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<div id="page-description">
			<div class="row">
				<div class="col-12">
					<p>청구항목별 입금 내역을 조회하는 화면입니다.</p>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<div class="search-box">
			<form>
				<div id="monthly-or-duration" class="row radio-seㅇlecter">
					<legend class="col-form-label col col-md-1 col-sm-2 col-2 pt-0">조회방식</legend>
					<div class="col col-md-10 col-sm-10 col-9">
						<div class="form-check form-check-inline">
							<input class="form-check-input lookup-by-month" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="month" checked="checked">
							<label class="form-check-label" for="inlineRadio1"><span></span>청구월별</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input lookup-by-range" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="period">
							<label class="form-check-label" for="inlineRadio2"><span></span>입금일자별</label>
						</div>
					</div>
				</div>

				<div id="lookup-by-month" class="row">
					<div class="col-md-11 offset-md-1 offset-sm-2 offset-2 col-10 form-inline year-month-dropdown">
						<select class="form-control" id="yearsBox">
							<option>선택</option>
						</select>
						<span class="ml-1 mr-1">년</span>
						<select class="form-control" id="monthBox">
							<option>선택</option>
						</select>
						<span class="ml-1">월</span>
					</div>
				</div>

				<div id="lookup-by-range" class="row" style="display: none;">
					<div class="col col-md-5 col-sm-10 offset-md-1 offset-sm-2 offset-2 form-inline">
						<div class="date-input">
                            <label class="sr-only">From</label>
							<div class="input-group">
								<input type="text" id="fDate" class="form-control date-picker-input" aria-label="From" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
							</div>
						</div>
						<span class="range-mark"> ~ </span>
						<div class="date-input">
							<label class="sr-only">To</label>
							<div class="input-group">
								<input type="text" id="tDate" class="form-control date-picker-input" aria-label="To" aria-describedby="basic-addon2" maxlength="8" onkeydown="onlyNumber(this)">
							</div>
                        </div>
					</div>

					<div class="col-md-6 col-sm-10 offset-md-0 offset-sm-2 offset-2">
						<button id="pMonth1" type="button" class="btn btn-sm btn-preset-month active" onclick="prevDate(1)">1개월</button>
						<button id="pMonth2" type="button" class="btn btn-sm btn-preset-month" onclick="prevDate(2)">2개월</button>
						<button id="pMonth3" type="button" class="btn btn-sm btn-preset-month" onclick="prevDate(3)">3개월</button>
					</div>
				</div>

				<div class="row mb-3 mt-3">
					<div class="col" style="border-top: 1px solid #d6d6d6;"></div>
				</div>

				<div class="row">
					<label class="col col-md-1 col-sm-2 col-2 col-form-label">검색구분</label>
					<div class="col col-md-4 col-sm-10 col-9 form-inline">
						<select class="form-control" id="searchGb">
							<option value="cusname">고객명</option>
							<option value="vano">계좌번호</option>
						</select>
						<input class="form-control col-auto" type="text" name="searchValue" id="searchValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch();}"/>
					</div>

					<label class="col col-md-1 col-sm-2 col-2 col-form-label">입금수단</label>
					<div class="col col-md-6 col-sm-11 col-10 form-inline">
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="deposAll" id="deposRadio1" value="ALL">
							<label class="form-check-label" for="deposRadio1"><span class="mr-1"></span>전체</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio2" value="VAS">
							<label class="form-check-label" for="deposRadio2"><span class="mr-1"></span>가상계좌</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio3" value="DCS">
							<label class="form-check-label" for="deposRadio3"><span class="mr-1"></span>현금</label>
						</div>
						<%--<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio4" value="OCD">
							<label class="form-check-label" for="deposRadio4"><span class="mr-1"></span>온라인카드</label>
						</div>--%>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="checkbox" name="deposItem" id="deposRadio5" value="DCD">
							<label class="form-check-label" for="deposRadio5"><span class="mr-1"></span>오프라인카드</label>
						</div>
					</div>
				</div>

				<div class="row">
					<label class="col col-md-1 col-sm-2 col-2 col-form-label">고객구분</label>
					<div class="col col-md-4 col-sm-10 col-9 form-inline">
						<select class="form-control" id="cusGubn" onchange="fnCusGubn(this);">
						</select>
						<div class="input-with-magnet">
							<input class="form-control" type="text" id="cusGubnValue" placeholder="콤마(,) 구분자로 다중검색이 가능합니다." onkeypress="if(event.keyCode == 13){fnSearch();}"/>
						</div>
					</div>

					<label class="col col-md-1 col-sm-2 col-2 col-form-label">청구항목</label>
					<div class="col col-md-4 col-sm-10 col-9 form-inline">
						<select class="form-control col" id="payItem" style="max-width: 100%; margin-right: 0;">
							<option value="">선택</option>
							<c:forEach items="${claimItem}" var="item">
								<option value="${item.code}">${item.codeName} - ${item.code}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<c:choose>
					<c:when test="${orgSess.adjAccYn == 'Y'}">
						<div class="row">
							<label class="col col-md-1 col-sm-2 col-2 col-form-label">입금통장명</label>
							<div class="col col-md-4 col-sm-10 col-9 form-inline">
								<select class="form-control col" id="moneyPassbookName" style="max-width: 100%; margin-right: 0;">
									<option value="">선택</option>
									<c:forEach items="${moneyPassbookName}" var="item">
										<option value="${item.code}">${item.codeName}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</c:when>
				</c:choose>

				<div class="row form-inline mt-3">
					<div class="col-12 text-center">
						<input type="button" class="btn btn-primary btn-wide" onclick="fnSearch()" onkeypress="if(event.keyCode == 13){fnSearch();}" value="조회" />
					</div>
				</div>
			</form>
		</div>
	</div>

	<div class="container">
		<div id="book-keeping-list" class="list-id">
			<div class="table-option row mb-2">
				<div class="col-md-7 col-sm-12 form-inline" id="paAll">
					<span class="amount mr-2">조회결과 [<em class="font-blue" id="totCnt"><fmt:formatNumber pattern="#,###" value="${map.totalCnt}"/></em>건]</span>
					<span class="amount mr-2">입금금액 [<em class="font-blue" id="totAmt"><fmt:formatNumber pattern="#,###" value="${map.totalAmt}"/></em>원]</span>
				</div>
				<div class="col-md-5 col-sm-12 text-right mt-1">
					<select class="form-control" id="sortGb" onchange="pageChange();">
						<option value="payDay">입금일시순 정렬</option>
						<option value="cusName">고객명순 정렬</option>
						<option value="cusGubn">고객구분순 정렬</option>
						<c:if test="${orgSess.adjAccYn == 'Y'}"><option value="grpadjName">입금통장명순 정렬</option></c:if>
					</select>

					<select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
						<option value="10" <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회</option>
						<option value="20" <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회</option>
						<option value="50" <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회</option>
						<option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회</option>
						 <option value="200" <c:if test="${map.PAGE_SCALE == '200'}">selected</c:if>>200개씩 조회</option>
					</select>
					<button class="btn btn-sm btn-d-gray hidden-on-mobile" type="button" onclick="fn_fileSave()">파일저장</button>
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
								<th>가상계좌번호</th>
								<c:if test="${orgSess.cusGubn1 != '' && orgSess.cusGubn1 != null}"><th>${orgSess.cusGubn1}</th></c:if>
								<c:if test="${orgSess.cusGubn2 != '' && orgSess.cusGubn2 != null}"><th>${orgSess.cusGubn2}</th></c:if>
								<c:if test="${orgSess.cusGubn3 != '' && orgSess.cusGubn3 != null}"><th>${orgSess.cusGubn3}</th></c:if>
								<c:if test="${orgSess.cusGubn4 != '' && orgSess.cusGubn4 != null}"><th>${orgSess.cusGubn4}</th></c:if>
								<th>청구항목명</th>
								<th>입금금액(원)</th>
								<%--<th>비고</th>--%>
								<th>입금수단</th>
								<c:if test="${orgSess.adjAccYn == 'Y'}"><th>입금통장명</th></c:if>
							</tr>
						</thead>

						<tbody id="resultBody">

						</tbody>
					</table>
				</div>
			</div>

			<jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false" />
		</div>
	</div>

	<div class="container">
		<div id="quick-instruction" class="foldable-box">
			<div class="row foldable-box-header">
				<div class="col-8">
					<img src="/assets/imgs/common/icon-notice.png">알려드립니다.
				</div>
				<div class="col-4 text-right">
					<img src="/assets/imgs/common/btn-arrow-notice.png" class="fold-status">
				</div>
			</div>
			<div class="row foldable-box-body">
				<div class="col">
					<h6>■ 청구항목</h6>
					<ul>
						<li>특정 청구항목을 선택하지 않은 '선택' 상태일 경우 전체 청구항목 나열</li>
						<li>특정 청구항목을 선택하여 조회 가능</li>
					</ul>

					<h6>■ 정렬순서</h6>
					<ul>
						<li>입금일시순 : 검색결과가 입금일시 최근 순으로 정렬</li>
						<li>고객명순 : 검색결과가 고객명의 가나다순으로 정렬</li>
						<li>고객구분순 : 첫 번째 고객구분명을 가나다순으로 정렬</li>
						<li>입금통장명순 :  두 개 이상의 입금계좌를 이용하는 경우 지정한 통장명의 가나다순으로 정렬</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" />

<%-- 고객정보상세 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/detail-payer-information.jsp" flush="false"/>

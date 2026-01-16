<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false" />

<script type="text/javascript">
	var cuPage = 1;
	// GNB의 Depth 1을 활성화 시켜주는 변수
	var firstDepth = "nav-link-21";
	var secondDepth = "sub-01";

	var notiStatusMap = {
		"PA00" : "임시",
		"PA02" : "미납",
		"PA03" : "완납",
		"PA04" : "일부납",
		"PA05" : "초과납",
		"PA06" : "환불"
	};

    function payToBank(notimasCd) {
        $('[data-dismiss="modal"]').click();
        fn_reset_scroll();

        var notidetCd = [];
        var check = $("input[name='checkList']:checked");
        check.map(function (i) {
            if ($(this).val() != null && $(this).val() != '') {
                notidetCd.push($(this).attr("ndcd"));
            }
        });

        $('#notidetCd').val(notidetCd);

        var url = "/payer/notification/notificationPickRcpYn";
        var param = {
            notidetCd: $('#notidetCd').val(),
            notimasCd: notimasCd
        };
        $.ajax({
            type: "post",
            url: url,
            data: JSON.stringify(param),
            contentType : "application/json; charset=utf-8",
            success: function(data){
                payToBankModal();
            }
        });
    }

	function payToBankModal() {
        $("#popup-pay-to-bank-account").modal({
            backdrop : 'static',
            keyboard : false
        });
	}

	function fnSearch(page) {
		var tmasMonth = $('#tYearsBox').val()+""+$('#tMonthBox').val();
		var fmasMonth = $('#fYearsBox').val()+""+$('#fMonthBox').val();

		if (page == null || page == 'undefined') {
			cuPage = "";
			cuPage = 1;
		} else {
			cuPage = page;
		}

		if (tmasMonth < fmasMonth) {
			swal({
			       type: 'info',
			       text: '조회 시작년월이 더 클 수 없습니다.',
			       confirmButtonColor: '#3085d6',
			       confirmButtonText: '확인'
				});
			return false;
		}

		var url = "/payer/notification/notificationListAjax";

		var param = {
			vaNo : $("#vaNo").val(),
			tmasMonth : tmasMonth, //조회 시작년월
			fmasMonth : fmasMonth, //조회 종료년월
			cusName : $("#cusName").val(),
			chaName : $("#chaName").val(),
			chaCd : $("#chaCd").val(),
			curPage : cuPage,
			pageScale : $('#pageScale option:selected').val(),
			searchOrderBy : $('#searchOrderBy option:selected').val()
		};
		$.ajax({
			type : "post",
			url : url,
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(param),
			success : function(result) {
				if (result.retCode == "0000") {
					fnGrid(result, 'resultBody'); // 현재 데이터로 셋팅
					ajaxPaging(result, 'PageArea');
				} else {
					swal({
					       type: 'error',
					       text: result.retCode,
					       confirmButtonColor: '#3085d6',
					       confirmButtonText: '확인'
						});
				}
			}
		});
	}

	//데이터 새로고침
    function fnGrid(result, obj) {
        $("#listCount").text(result.count);
        var str = '';
        var detCnt = 1;
        var beforeMasCd = '';

        if (result.count <= 0) {
            str += '<tr><td colspan="7" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.notificationList, function (i, v) {
                if (beforeMasCd != v.notimasCd) {
                    str += '<tr>';
                    str += '<td>' + dotDate(v.masMonth) + '</td>';
                    str += '<td>' + dotDate(v.endDate) + '</td>';
                    str += '<td><button type="button" class="btn btn-xs btn-link btn-open-payment-unit-list" onclick="btnDetCnt(\'' + v.notimasCd + '\',\'' + v.masMonth + '\')">' + v.detCnt + '건</td>';
                    str += '<td class="text-right">' + numberToCommas(v.subTot) + '</td>';
                    str += '<td class="text-right">' + numberToCommas(v.unSubTot) + '</td>';
                    str += '<td>' + v.notimasNm + '</td>';
                    str += '<td class="hidden-on-mobile">';
                    if (v.endDate >= v.toDay) {
                        if (v.notimasSt == 'PA02' || v.notimasSt == 'PA04') {
                            str += '<button type="button" class="btn btn-sm btn-gray-outlined btn-pay-to-bank-account hidden-on-mobile" onclick="btnDetCnt(\'' + v.notimasCd + '\',\'' + v.masMonth + '\',\'select\')">납부하기</button>';
                        }
                    }
                    str += '</td></tr>';
                    beforeMasCd = v.notimasCd;
                }
            });
        }

        $('#' + obj).html(str);
    }

	//페이징 조회
	function list(page) {
		fnSearch(page);
	}

	//날짜 포맷추가
	function dotDate(val) {
		if (val.length < 7) {
			val = val.substring(0, 4) + "." + val.substring(4, 6);
		} else {
			val = val.substring(0, 4) + "." + val.substring(4, 6) + "."
					+ val.substring(6, 8);
		}

		return val;
	}

	//화면보여주는 개수 변경
	function pageChange() {
		fnSearch(cuPage);
	}

	//정렬 변경
	function arrayChange() {
		fnSearch(cuPage);
	}

	//금액 콤마구분
	function numberWithCommas(x) {
		return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}

	//카드결제
	function payByCard(notimasCd) {
	    fn_reset_scroll();

		var strFeature = "width=700, height=500, scrollbars=no, resizable=no";

		// 납부선택 처리
		var notidetCds = new Array();
		$(':checkbox[name=checkList]:checked').each(function(i, v){ 
			notidetCds.push( $(this).attr("ndcd") );
		});

		$("#payCardForm").attr("action", "/payer/notification/selectNotiDetails");
		$("#payCardForm").attr("target", "formInfo");
		$("#payCardForm > #notimasCd").val(notimasCd);
		$("#payCardForm > #notidetList").val(notidetCds.join(","));
		window.open("", "formInfo", strFeature);
		$("#payCardForm").submit();
	}

	//항목건수 클릭
	function btnDetCnt(notimasCd, masMonth, select){
		var url = "/payer/notification/noticeDetailList";
		var param = {
			notimasCd : notimasCd,
			masMonth  : masMonth
		};
		$.ajax({
			type : "post",
			url : url,
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(param),
			success : function(result) {
				if (result.retCode == "0000") {
					fnGridModal(result, select); // 항목건수보기
				} else {
					swal({
					       type: 'error',
					       text: result.retCode,
					       confirmButtonColor: '#3085d6',
					       confirmButtonText: '확인'
						});
				}
			}
		});
	}

	/*
	 * 청구항목 상세 팝업 내용 채우기
	 */
	function fnGridModal(result, select) {
		$("#"+obj).find('#popResultBody').empty();
		$("#"+obj).find('#popTotalAmt').empty();
		$("#"+obj).find('#popTotalRcpAmt').empty();

		var obj = (select == "select" ? "popup-payment-unit-catalog": "popup-payment-unit-list");

		$("#"+obj).find("#popCusName").text($("#cusName").val());

		var str = '';
		var popTotalAmt = 0;
		var popTotalRcpAmt = 0;

		var partialPayment = $("#partialPayment").val();
        var amtchkty = $("#amtchkty").val();
        var partialSt = false;

        if (partialPayment == 'Y' && amtchkty == 'Y') {
            partialSt = true;
		} else {
            partialSt = false;
		}

		if (result.count <= 0) {
            if (select == "select") {
                str += '<tr><td colspan="5" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
            } else {
                str += '<tr><td colspan="4" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
			}
		} else {
			$.each(result.list, function(i, v) {
				popTotalAmt += Number(v.payitemamt);
				popTotalRcpAmt += Number(v.rcpamt);

				if (select == "select") {
                    if (v.notidetst == "PA02" || v.notidetst == "PA04") {
                        str += '<tr>';
						str += '<td>';

						if (partialSt == true) {
						    // 부분납 가능 기관
                            if (v.payitemselyn == "N") {
                                // 필수항목
                                str += '	<input class="form-check-input table-check-child checkbox-check-disabled" checked="checked" type="checkbox" name="checkList" id="row-'+i+'" value="'+(v.payitemamt - v.rcpamt == 0 ? v.payitemamt : v.payitemamt - v.rcpamt)+'" num="'+i+'" nmcd="'+v.notimascd+'" ndcd="'+v.notidetcd+'">';
                                str += '	<label for=""><span></span></label>';
                            } else {
                                if (v.pickrcpyn == "N") {
                                    // 일반항목
                                    str += '	<input class="form-check-input table-check-child chkClick" type="checkbox" name="checkList" id="row-'+i+'" onchange="selectedPayItemsTrigger()" value="'+(v.payitemamt - v.rcpamt == 0 ? v.payitemamt : v.payitemamt - v.rcpamt)+'" num="'+i+'" nmcd="'+v.notimascd+'" ndcd="'+v.notidetcd+'">';
                                    str += '	<label for="row-' + i + '"><span></span></label>';
								} else {
                                    // 일반항목
                                    str += '	<input class="form-check-input table-check-child chkClick" type="checkbox" checked="checked" name="checkList" id="row-'+i+'" onchange="selectedPayItemsTrigger()" value="'+(v.payitemamt - v.rcpamt == 0 ? v.payitemamt : v.payitemamt - v.rcpamt)+'" num="'+i+'" nmcd="'+v.notimascd+'" ndcd="'+v.notidetcd+'">';
                                    str += '	<label for="row-' + i + '"><span></span></label>';
								}
                            }
						} else {
                            // 부분납 불가능 기관
							str += '	<input class="form-check-input table-check-child checkbox-check-disabled" checked="checked" type="checkbox" name="checkList" id="row-'+i+'" value="'+(v.payitemamt - v.rcpamt == 0 ? v.payitemamt : v.payitemamt - v.rcpamt)+'" num="'+i+'" nmcd="'+v.notimascd+'" ndcd="'+v.notidetcd+'">';
							str += '	<label for=""><span></span></label>';
						}

						str += '</td>';
                        str += '<td>' + v.payitemname + '</td>';
                        str += '<td>' + numberToCommas(v.payitemamt) + '</td>';
						str += '<td>' + (v.payitemselyn == "N" ? "필수" : "선택") + '</td>';
                        str += '<td>' + defaultString(v.ptritemremark, '') + '</td>';
                        str += '</tr>';
                    }
				} else {
                    str += '<tr>';
                    str += '<td>' + v.payitemname + '</td>';
                    str += '<td>' + numberToCommas(v.payitemamt) + '</td>';
					str += '<td>' + numberToCommas(v.rcpamt) + '</td>';
                    str += '<td>' + defaultString(v.ptritemremark, '') + '</td>';
                    str += '</tr>';
				}
			});
		}
		$("#"+obj).find('#popResultBody').html(str);
		if (select == "select") {
			selectedPayItemsTrigger();

			// 결제처리 버튼생성
			var usePgYn = $('#usePgYn').val();
			var htmlButtons = "";
			htmlButtons += '<button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">닫기</button>';
			htmlButtons += '<button type="button" class="btn btn-primary save-payer-information" onclick="payToBank(\'' + result.notimasCd + '\')">계좌이체</button>';
			if (usePgYn == 'Y') {
                htmlButtons += '<button type="button" class="btn btn-secondary" onclick="payByCard(\'' + result.notimasCd + '\')">신용카드</button>';
			}

			$("#popup-payment-unit-catalog").find('.modal-footer').empty();
			$("#popup-payment-unit-catalog").find('.modal-footer').html(htmlButtons);
		} else {
			$("#"+obj).find('#popTotalAmt').text(numberToCommas(popTotalAmt));
			$("#"+obj).find('#popTotalRcpAmt').text(numberToCommas(popTotalRcpAmt));
			$("#"+obj).find("#popMasMonth").text(formatYearMonth(result.masMonth));
            $("#"+obj).find("#popMasVano").text($('#vaNo').val());
		}

		$("#"+obj).modal('show');
	}

	/* 
	 * 납부선택에 대한 결제금액 처리
	 */
	function selectedPayItemsTrigger() {
		$("#popup-payment-unit-catalog").find('#popTotalAmt').text(0);
		var itemsAmt = 0;
		$(':checkbox[name=checkList]:checked').each(function(i, v){
			itemsAmt += Number($(this).val());
		});
		$("#popup-payment-unit-catalog").find('#popTotalAmt').text(numberToCommas(itemsAmt));
	}
</script>

<input type="hidden" id="tmax" value="${map.tmasMonth}">
<input type="hidden" id="fmax" value="${map.fmasMonth}">

<form name="payCardForm" id="payCardForm" method="post" action="/payer/notification/selectNotiDetails">
	<input type="hidden" id="notimasCd" name="notimasCd"/>
	<input type="hidden" id="accessGubn" name="accessGubn" value="pc"/>
	<input type="hidden" id="notidetList" name="notidetList"/>
</form>

<input type="hidden" id="vaNo" name="vaNo" value="<c:out value="${map.vaNo}" escapeXml="true" />">
<input type="hidden" id="cusName" name="cusName" value="<c:out value="${map.cusName}" escapeXml="true" />" />
<input type="hidden" id="chaName" name="chaName" value="<c:out value="${map.chaName}" escapeXml="true" />" />
<input type="hidden" id="curPage" name="curPage" />
<input type="hidden" id="usePgYn" name="usePgYn" value="${map.usePgYn }" />
<input type="hidden" id="chaCd" name="chaCd" value="<c:out value="${map.chaCd}" escapeXml="true" />">
<input type="hidden" id="partialPayment" name="partialPayment" value="${map.partialPayment}">
<input type="hidden" id="amtchkty" name="amtchkty" value="${map.amtchkty}">

<input type="hidden" id="notidetCd" name="notidetCd" />

<div id="contents">
	<div id="damoa-breadcrumb">
		<nav class="nav container">
			<a class="nav-link active" href="#">고지서내역 조회</a>
		</nav>
	</div>
	<div class="container">
		<div id="page-title">
			<div class="row">
				<div class="col-6">
					<h2>고지내역조회</h2>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
        <div id="page-description">
            <div class="row">
                <div class="col-12">
                    <p>
                        납부구분이 "미납, 일부납"일 경우에만 고지내역에서 확인 가능합니다.<br>
						가상계좌 수납관리 서비스 고객센터 02-786-8201(평일 09:00-12:00, 13:00-17:00)
                    </p>
                </div>
            </div>
        </div>
    </div>

	<div class="container">
		<div id="payable-summary">
			<div class="row">
				<div class="col-12">
					<img src="/assets/imgs/payer/icon-bill-in-letter.png">
					<p>
						<strong>${map.cusName}</strong>님께서 <strong id="thisMonth"></strong>
						납부하실 금액은 <strong><fmt:formatNumber pattern="#,###" value="${map.subTot}" />원</strong> 입니다.
					</p>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<div class="row">
			<div class="col-12">
				<h5>납부자정보</h5>
			</div>
		</div>

		<div class="row">
			<div class="table-responsive pd-n-mg-o col mb-5">
				<table class="table table-sm table-primary">
					<thead>
						<tr>
							<th>납부자명</th>
							<th>이용기관명</th>
							<th>납부계좌</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${map.cusName}</td>
							<td>${map.chaName}</td>
							<td>${map.vaNo}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="container">
		<form class="search-box">
			<div class="row">
				<label class="col-form-label col col-md-1 col-sm-3 col-3">
					고지년월 </label>
				<div class="col col-md-5 col-sm-9 col-9 form-inline">
					<select class="form-control" id="fYearsBox"></select>
					<span class="ml-1 mr-1">년</span>
					<select class="form-control" id="fMonthBox"></select>
					<span class="ml-1 mr-auto">월</span>
					<span class="range-mark"> ~ </span>
					<select class="form-control" id="tYearsBox"></select>
					<span class="ml-1 mr-1">년</span>
					<select class="form-control" id="tMonthBox"></select>
					<span class="ml-1 mr-auto">월</span>
				</div>
			</div>
			<div class="row mt-3">
				<div class="col-12 text-center">
					<input type="button" class="btn btn-primary btn-wide" onclick="fnSearch();" value="조회" />
				</div>
			</div>
		</form>
	</div>

	<div class="container mb-5">
		<div id="bill-reference-list" class="list-id">
			<div class="row">
				<div class="col-12">
					<h5>고지내역</h5>
				</div>
			</div>
			<div class="table-option row mb-2">
				<div class="col-md-6 col-sm-12 form-inline">
					<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="listCount">${map.count}</em>건]</span>
				</div>
				<div class="col-md-6 col-sm-12 text-right mt-1">
					<select class="form-control" name="searchOrderBy" id="searchOrderBy" onchange="arrayChange();">
						<option value="masMonth" <c:if test="${map.search_orderBy == 'masMonth'}">selected</c:if>>고지년월순 정렬</option>
						<option value="endDate" <c:if test="${map.search_orderBy == 'endDate'}">selected</c:if>>납부마감일순 정렬</option>
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
							<col width="120">
							<col width="120">
							<col width="120">
							<col width="120">
							<col width="120">
							<col width="250">
							<col width="250">
						</colgroup>

						<thead>
							<tr>
								<th>고지년월</th>
								<th>납부마감일</th>
								<th>고지건수</th>
								<th>고지금액(원)</th>
								<th>미납금액(원)</th>
								<th>납부구분</th>
								<th class="hidden-on-mobile">납부</th>
							</tr>
						</thead>

						<tbody id="resultBody">
							<tr>
								<td colspan="7" style="text-align: center;">[조회된 내역이 없습니다.]</td>
							</tr>
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
                    <h6>■ 납부자 고지내역조회</h6>
					<ul>
						<li>납부고객의 청구된 고지내역 조회 및 미납금액에 대해 온라인카드 / 계좌이체 결제를 지원하는 화면</li>
					</ul>

                    <h6>■ 납부방법</h6>
                    <ul>
                        <li>고지년월 기간 선택 및 조회 버튼 클릭</li>
                        <li>하단 조회 된 고지월의 결제방법을 선택하여 결제진행</li>
                    </ul>
                </div>
            </div>
        </div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/include/payer/footer.jsp" flush="false" />

<%-- 무통장입금 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/pay-to-bank-account.jsp" flush="false" />

<%-- 청구항목 상세보기 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/payment-unit-list.jsp" flush="false"/>

<%-- 청구항목별 납부선택 상세보기 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/payment-unit-catalog.jsp" flush="false"/>

<script type="text/javascript">
	$(document).ready(function() {
		var tmax = $("#tmax").val();
		var fmax = $("#fmax").val();
	
		$("#thisMonth").html(fmax.substring(4, 6) + "월");

        getYearsBox2('fYearsBox');
		getMonthBox('fMonthBox');
		getYearsBox2('tYearsBox');
		getMonthBox('tMonthBox');

        $("#fYearsBox").val(fmax.substring(0, 4));
        $("#tYearsBox").val(tmax.substring(0, 4));
		$("#fMonthBox").val(fmax.substring(4, 6));
		$("#tMonthBox").val(tmax.substring(4, 6));

		// 목록조회
		fnSearch(1);

        $('#row-th').click(function () {
            if ($(this).prop('checked')) {
                $('.chkClick').prop('checked', true);
            } else {
                $('.chkClick').prop('checked', false);
            }
        });
	});
</script>

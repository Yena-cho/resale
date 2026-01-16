<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/include/payer/header.jsp"
	flush="false" />

<script>
var cuPage = 1;
// GNB의 Depth 1을 활성화 시켜주는 변수
var firstDepth = "nav-link-23";
var secondDepth = "sub-01";

var toDay = getFormatCurrentDate();

$(document).ready(function() {
	var thisMonth = getCurrentMonth();
	thisMonth = thisMonth.substring(4, 6) + "월";
	$("#thisMonth").html(thisMonth);

	$('#fDate').val(getPrevDate(toDay,1));
	$('#tDate').val(toDay);
	
/* 	getYearsBox('fYearsBox');
	getMonthBox('fMonthBox');
	getYearsBox('tYearsBox');
	getMonthBox('tMonthBox'); */
});

function fnSearch(page){
	
	var tmasMonth = replaceDot($('#tDate').val());
	var fmasMonth = replaceDot($('#fDate').val());
	
	var vdm = dateValidity(fmasMonth, tmasMonth);
	if (vdm != 'ok'){
 		swal({
 	           type: 'info',
 	           text: vdm,
 	           confirmButtonColor: '#3085d6',
 	           confirmButtonText: '확인'
 	       });
 		return false;
 	}
		
	if(page == null || page == 'undefined'){
		cuPage = "";
		cuPage = 1;
	}else{
		cuPage = page;
	}
		
	if (tmasMonth < fmasMonth){
		swal({
		       type: 'info',
		       text: "조회시작년월이 더 클 수 없습니다.",
		       confirmButtonColor: '#3085d6',
		       confirmButtonText: '확인'
			});
		return false;
	}
	
	var url = "/payer/cashReceipt/cashReceiptReqListAjax";
	
	var param = {
			vaNo	  : $("#vaNo").val(),
			tmasMonth : tmasMonth, //조회 시작년월 
			fmasMonth : fmasMonth, //조회 종료년월
			chaCd : $("#chaCd").val(),
			curPage : cuPage,
			pageScale : $('#pageScale option:selected').val(),
			searchOrderBy : $('#searchOrderBy option:selected').val(),
			status	: $('input[type=radio][name=issuingStatus]:checked').val()
	};
	$.ajax({
		type : "post",
		url : url,
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(param),
		success : function(result) {
			if (result.retCode == "0000") {
				fnGrid(result, 'resultBody');		// 현재 데이터로 셋팅
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
	
	if(result.count <= 0){
		str+='<tr><td colspan="5" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
	}else{
		$.each(result.list, function(i, v){
			str+='<tr>';
			str+='<td>'+dotDate(v.payDay)+'</td>';
			str+='<td class="text-right">'+numberToCommas(v.cshAmt)+'</td>';
			if("VAS" == v.sveCd){str+='<td>가상계좌</td>';}
			if("DCS" == v.sveCd){str+='<td>현금</td>';}
			if("DCD" == v.sveCd){str+='<td>오프라인카드</td>';}
			if("DVA" == v.sveCd){str+='<td>무통장입금</td>';}
			if("OCD" == v.sveCd){str+='<td>온라인카드</td>';}
			str+='<td>'+dotDate(v.sendDt)+'</td>';
			str+='<td>'+v.cashMasStNm+'</td>';
			str+='</tr>';
		});
	}
	$('#' + obj).html(str);
}

//날짜 포맷추가
function dotDate(val) {
	if(val != null && val != ''){
		if (val.length < 7) {
			val = val.substring(0, 4) + "." + val.substring(4, 6);
		} else {
			val = val.substring(0, 4) + "." + val.substring(4, 6) + "."
					+ val.substring(6, 8);
		}
	}else{
		val = '';
	}
	

	return val;
}

//페이징 조회
function list(page) {
	fnSearch(page);
}

function pageChange() {
	fnSearch(cuPage);
}

function arrayChange() {
	fnSearch(cuPage);
}
function prevDate(num){
	var toDay = $.datepicker.formatDate("yy.mm.dd", new Date() );
	var vdm = dateValidity($('#fDate').val(), toDay);
	/*
 	if (vdm != 'ok'){
 		swal({
 	           type: 'info',
 	           text: vdm,
 	           confirmButtonColor: '#3085d6',
 	           confirmButtonText: '확인'
 	       });
 		$('#tDate').val(toDay);
 		$('#fDate').val(monthAgo(toDay,num));
 		return false;
 	}
	*/
 	$('#tDate').val(toDay);
 	$('#fDate').val(monthAgo(toDay,num));
	$('.btn-preset-month').removeClass('active');
	$('#pMonth'+num+'').addClass('active');
}
</script>
<input type="hidden" id="cusName" name="cusName" value="<c:out value="${map.cusName}" escapeXml="true" />">
<input type="hidden" id="vaNo" name="vaNo" value="<c:out value="${map.vaNo}" escapeXml="true" />">
<input type="hidden" id="chaCd" name="chaCd" value="<c:out value="${map.chaCd}" escapeXml="true" />">
<div id="contents">
	<div id="damoa-breadcrumb">
		<nav class="nav container">
			<a class="nav-link active" href="#">현금영수증조회</a>
		</nav>
	</div>
	<div class="container">
		<div id="page-title">
			<div class="row">
				<div class="col-12">
					<h2>현금영수증조회</h2>
				</div>
			</div>
		</div>
	</div>
	<div class="container mt-3">
		<div class="row">
			<div class="col-12">
				<h5>신청자 정보</h5>
			</div>
		</div>

		<div class="row">
			<div class="table-responsive pd-n-mg-o col mb-5">
				<table class="table table-sm table-primary">
					<thead>
						<tr>
							<th>신청자명</th>
							<th>용도</th>
							<th>발급방법</th>
							<th>발급정보</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><c:out value="${map.cashInfo.cusName}" escapeXml="true"/> </td>
							<td>${map.cashInfo.cusTypeNm}</td>
							<td>${map.cashInfo.confirmNm}</td>
							<td>${map.cashInfo.cusOffNo}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="container">
		<form class="search-box">
			<div class="row">
				<label class="col-form-label col col-md-1 col-sm-3 col-3">납부일자</label>
				<div class="col col-md-4 col-sm-8 col-8 form-inline">
					<div class="date-input">
						<div class="input-group">
							<input type="text" id="fDate" class="form-control date-picker-input" placeholder="YYYY.MM.DD">
						</div>
					</div>
					<span class="range-mark"> ~ </span>
					<div class="date-input">
						<div class="input-group">
							<input type="text" id="tDate" class="form-control date-picker-input" placeholder="YYYY.MM.DD">
						</div>
					</div>
				</div>

				<div class="col col-md-6 col-sm-9 offset-md-0 offset-sm-2 offset-3">
					<button id="pMonth1" type="button" class="btn btn-sm btn-preset-month active" onclick="prevDate(1)">1개월</button>
					<button id="pMonth2" type="button" class="btn btn-sm btn-preset-month" onclick="prevDate(2)">2개월</button>
					<button id="pMonth3" type="button" class="btn btn-sm btn-preset-month" onclick="prevDate(3)">3개월</button>
				</div>
			</div>

			<div class="row mt-1">

				<label class="col-form-label col col-md-1 col-sm-3 col-3">발급현황 </label>
				<div class="col col-md-5 col-sm-9 col-9 form-inline">
					<div class="form-check form-check-inline">
						<input class="form-check-input" type="radio" id="all" name="issuingStatus" value="all" checked="checked"> <label for="all"><span class="mr-2"></span>전체</label>
					</div>
					<div class="form-check form-check-inline">
						<input class="form-check-input" type="radio" id="fin" name="issuingStatus" value="fin"> <label for="fin"><span class="mr-2"></span>완료</label>
					</div>
					<div class="form-check form-check-inline">
						<input class="form-check-input" type="radio" id="req" name="issuingStatus" value="req"> <label for="req"><span class="mr-2"></span>요청</label>
					</div>
					<div class="form-check form-check-inline">
						<input class="form-check-input" type="radio" id="ing" name="issuingStatus" value="ing"> <label for="ing"><span class="mr-2"></span>처리중</label>
					</div>
					<div class="form-check form-check-inline">
						<input class="form-check-input" type="radio" id="not" name="issuingStatus" value="not"> <label for="not"><span class="mr-2"></span>미발행</label>
					</div>
				</div>
			</div>

			<div class="row mt-3">
				<div class="col-12 text-center">
					<button type="button" class="btn btn-primary btn-wide" onclick="fnSearch();">조회</button>
				</div>
			</div>
		</form>
	</div>

	<div class="container mb-5">
		<div id="paid-reference-list" class="list-id">
			<div class="table-option row mb-2">
				<div class="col-md-6 col-sm-12 form-inline">
					<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="listCount">${map.count}</em>건]
					</span>
				</div>
				<div class="col-md-6 col-sm-12 text-right mt-1">
					<select class="form-control" name="searchOrderBy" id="searchOrderBy" onchange="arrayChange();">
						<option value="payDt">납부일자순 정렬</option>
						<option value="reqDt">신청일자순 정렬</option>
						<option value="status">발급현황별 정렬</option>
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
						<thead>
							<tr>
								<th colspan="2">납부정보</th>
								<th rowspan="2">납부방법</th>
								<th colspan="2">현금영수증 발급정보</th>
							</tr>
							<tr>
								<th>납부일자</th>
								<th>납부금액(원)</th>
								<th>신청일자</th>
								<th>발급현황</th>
							</tr>
						</thead>
						<tbody id="resultBody">
							<c:choose>
								<c:when test="${map.count > 0}">
									<c:forEach var="row" items="${map.list}" varStatus="status">
										<tr>
											<td><fmt:parseDate pattern="yyyyMMdd" var="fmtPayDay" value="${row.payDay}"/><fmt:formatDate pattern="yyyy.MM.dd" value="${fmtPayDay}" /></td>
											<td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.cshAmt}"/></td>
											<td><c:choose>
													<c:when test="${row.sveCd eq 'VAS'}">가상계좌</c:when>
													<c:when test="${row.sveCd eq 'DCS'}">현금</c:when>
													<c:when test="${row.sveCd eq 'DCD'}">오프라인카드</c:when>
													<c:when test="${row.sveCd eq 'DVA'}">무통장입금</c:when>
													<c:when test="${row.sveCd eq 'OCD'}">온라인카드</c:when>
												</c:choose></td>
											<td><fmt:parseDate pattern="yyyyMMdd" var="fmtSendDt" value="${row.sendDt}"/><fmt:formatDate pattern="yyyy.MM.dd" value="${fmtSendDt}" /></td>
											<td>${row.cashMasStNm}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="5" style="text-align: center;">[조회된 내역이
											없습니다.]</td>
									</tr>
								</c:otherwise>
							</c:choose>
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
		            <img src="/assets/imgs/common/icon-notice.png">
		            알려드립니다.
		        </div>
		        <div class="col-4 text-right">
		            <img src="/assets/imgs/common/btn-arrow-notice.png" class="fold-status">
		        </div>
		    </div>
		    <div class="row foldable-box-body">
		        <div class="col">
		            <h6>■ 현금영수증 신청조회</h6>
					<ul>
						<li>카드납을 제외한 납부금액에 대해 신청된 현금영수증 신청 현황을 조회하는 화면</li>
					</ul>
		
		            <h6>■ 현금영수증 신청방법</h6>
		            <ul>
		                <li>현금영수증 발급을 원하는 납부년월을 확인</li>
		                <li>이용기관 담당자에게 직접 연락하여 발급 요청</li>
		                <li>이용기관에서 요청 건 발급 처리</li>
		                <li>현금영수증 발급조회 화면에서 확인</li>
		            </ul>
		        </div>
		    </div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/include/payer/footer.jsp" flush="false" />

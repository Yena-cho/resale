<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:authentication property="principal.username" var="userId" />
<s:authentication property="principal.name" var="userName" />

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false" />

<script type="text/javascript">
	var firstDepth = "nav-link-35";
	var secondDepth = "sub-04";
</script>

<div id="contents">
	<div class="container">
		<div id="page-title">
			<div class="row">
				<div class="col-8">
					<h2>이용요금조회</h2>
				</div>
				<div class="col-4 text-right">
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<div id="page-description">
			<div class="row">
				<div class="col-12">
					<p>가상계좌 수납관리 서비스 일별 이용요금을 확인할 수 있는 화면입니다.</p>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<div class="row">
			<div class="col-12">
				<h5>일별 이용내역 조회</h5>
			</div>
		</div>
	</div>

	<div class="container">
		<div class="row">
			<div class="col mb-3">
				<div class="table-responsive">
					<table id="bill-reference-list" class="table table-sm table-hover table-primary">
						<colgroup>
							<col width="100">
							<col width="130">
							<col width="110">
							<col width="110">
							<col width="110">
							<col width="110">
							<col width="110">
							<col width="110">
							<col width="110">
							<col width="110">
							<col width="110">
							<col width="110">
						</colgroup>

						<thead>
						<tr>
							<th rowspan="2">이용월</th>
							<th rowspan="2"><strong>이용요금(원)</strong></th>
							<th colspan="4">수수료</th>
							<th colspan="4">부가서비스</th>
						</tr>
						<tr>
							<th class="border-l">청구건수</th>
							<th>금액(원)</th>
							<th>입금건수</th>
							<th>금액(원)</th>
							<th style="border-left: 2px solid #737373;">SMS건수<br>(20원/건)</th>
							<th>LMS건수<br>(40원/건)</th>
							<th>알림톡<br>(20원/건)</th>
							<th>금액(원)</th>
							<%--<th style="border-left: 2px solid #737373;">출력건수</th>
							<th>금액(원)</th>--%>
						</tr>
						</thead>

						<tbody>
						<c:choose>
							<c:when test="${map.count > 0}">
								<c:forEach var="each" items="${itemList}" varStatus="status">
									<tr>
										<td><fmt:formatDate pattern="yyyy.MM.dd" value="${each.settleDate}"/></td>
										<td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${each.noticeFee + each.vaPaymentFee + each.smsFee+each.lmsFee + each.prnFee}"/></td>
										<td class="text-right"><fmt:formatNumber pattern="#,###" value="${each.noticeCount}" /></td>
										<td class="text-right"><fmt:formatNumber pattern="#,###" value="${each.noticeFee}" /></td>
										<td class="text-right"><fmt:formatNumber pattern="#,###" value="${each.vaPaymentCount}"/></td>
										<td class="text-right"><fmt:formatNumber pattern="#,###" value="${each.vaPaymentFee}"/></td>
										<td class="text-right"><fmt:formatNumber pattern="#,###" value="${each.smsCount}"/></td>
											<%--											<td class="text-right"><fmt:formatNumber pattern="#,###" value="${each.smsFee}"/></td>--%>
										<td class="text-right"><fmt:formatNumber pattern="#,###" value="${each.lmsCount}"/></td>
											<%--											<td class="text-right"><fmt:formatNumber pattern="#,###" value="${each.lmsFee}"/></td>--%>
										<td class="text-right"><fmt:formatNumber pattern="#,###" value="${each.atCount}"/></td>
										<td class="text-right"><fmt:formatNumber pattern="#,###" value="${each.smsFee + each.lmsFee + each.atFee}"/></td>
										<%--<td class="text-right"><fmt:formatNumber pattern="#,###" value="${each.prnCount}"/></td>
										<td class="text-right"><fmt:formatNumber pattern="#,###" value="${each.prnFee}"/></td>--%>
									</tr>
								</c:forEach>
							</c:when>

							<c:otherwise>
								<tr>
									<td colspan="12">[조회된 내역이 없습니다.]</td>
								</tr>
							</c:otherwise>
						</c:choose>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="row list-button-group-bottom">
			<div class="col-12 text-center">
				<a href="/org/myPage/monthSum" class="btn btn-primary btn-outlined btn-wide" >이전화면</a>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" />

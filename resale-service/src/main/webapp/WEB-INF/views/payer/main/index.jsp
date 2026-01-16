<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false"/>

<script>
    var firstDepth = null;
    var secondDepth = null;
</script>

<script type="text/javascript">

// 고지내역
function fn_notiPage() {
	document.payerSess.action = '/payer/notification/notificationList';
	document.payerSess.submit();
}

//납부내역
function fn_payPage() {
	document.payerSess.action = '/payer/payment/payList';
	document.payerSess.submit();
}

//이용신청서 다운로드
function fn_download(){
	fncClearTime();
	$('#fileName').val('payerManual.pdf');

	document.fileForm.action = "/common/library/download";
	document.fileForm.submit();
}

//전화상담예약
function fn_makeReservation() {

	$('#make-reservation').modal({backdrop:'static', keyboard:false});
}
 //자주하는 질문
function moveFaq(no){
	if(no == 1) {
		location.href = "/common/customerAsist/faqList";
	} else {
		location.href = "/common/customerAsist/faqList?no=" + no;
	}
}

function click_menu(val){

	if('pay' == val){
		document.payerSess.action = '/payer/payment/payList';
	}
	document.payerSess.submit();
}
</script>

<form id="fileForm" name="fileForm" method="post">
	<input type="hidden" id="fileName" name="fileName">
	<input type="hidden" id="flag"  name="flag" value="payer">
</form>

            <div id="contents">
                <div class="container">
                    <nav id="damoa-breadcrumb" class="nav">
                        <a class="nav-link" href="#">청구조회</a>
                        <a class="nav-link active" href="#">청구등록</a>
                        <a class="nav-link" href="#">청구대량등록</a>
                        <a class="nav-link" href="#">청구항목관리</a>

                        <div class="dropdown">
                            <button class="btn btn-link dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                2nd depth
                            </button>
                            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                <a class="dropdown-item" href="#">3rd depth</a>
                                <a class="dropdown-item" href="#">3rd depth</a>
                                <a class="dropdown-item" href="#">3rd depth</a>
                            </div>
                        </div>
                    </nav>
                </div>

				<div class="section-container mb-5">
					<div id="payer-dashboard" style="background: #fff; padding-bottom:1.75rem;">
						<div class="dashboard-month-controller">
							<div class="container">
								<div class="row">
									<h3 class="mx-auto title font-weight-400" style="padding: 1rem 0 0 0;">
										<span class="font-blue">
											<fmt:parseDate pattern="yyyyMM" var="maxMonth" value="${map.maxMonth}" /><fmt:formatDate pattern="yyyy년 MM월" value="${maxMonth}" />
										</span> 납부관리 현황
									</h3>
								</div>
							</div>
						</div>
						<div class="container">
							<div class="row summary">
								<div class="col col-md-4 col-sm-6 col-12 text-center">
									<div class="img-wrapper">
										<img src="/assets/imgs/common/icon-won-in-monitor.png" class="icon-won-in-monitor">
									</div>
									<label>고지분</label>
									<div class="amount font-weight-400"><fmt:formatNumber pattern="#,###" value="${map.notification.subTot}"/></div>
									<span>${map.chaInfo.chaName}</span>
								</div>
								<div class="col col-md-4 col-sm-6 col-12 text-center">
									<div class="img-wrapper">
										<img src="/assets/imgs/common/icon-statment-m-balloon.png" class="icon-won-in-monitor">
									</div>
									<label>미납액</label>
									<div class="amount font-weight-400"><fmt:formatNumber pattern="#,###" value="${map.notification.unSubTot}"/></div>
									<span>당월 미납액은 <fmt:formatNumber pattern="#,###" value="${map.notification.unSubTot}"/>원 입니다.</span>
								</div>
								<div class="col col-md-4 col-sm-12 col-12 text-center">
									<div class="img-wrapper">
										<img src="/assets/imgs/common/icon-statment-with-cog.png" class="icon-statment-with-cog">
									</div>
									<label>현금영수증 신청내역</label>
									<div class="amount issue font-weight-400">${map.receipt.totCnt}</div>
									<span>
										<c:choose>
											<c:when test="${map.receipt.sendDt ne '0'}">
												신청일 : ${map.receipt.sendDt}
											</c:when>
										</c:choose>
									</span>
								</div>
							</div>
							<div class="row">
								<button class="btn btn-primary mx-auto uncus" onclick="fn_notiPage();">상세 고지 내역 확인</button>
							</div>
						</div>
					</div>
				</div>
                <div id="payer-short-report" class="container">
                    <div class="row">
                        <div class="col col-md-6 col-sm-12 col-12 mb-4">
                            <div class="row mb-2">
								<div class="col col-12">
									<div class="title-sol-type purple">
										<span>최근 납부된 내역을 안내해드립니다.</span>
										<h4>최근 납부 내역</h4>
									</div>
                                    <div class="see-all text-right">
            							<a href="javascript:click_menu('pay');">> 전체보기</a>
            						</div>
								</div>
							</div>
                            <div class="row">
                                <div class="col col-12 text-center">
                                    <div class="table-responsive">
                                        <table class="table table-sm table-hover table-summary">
                                            <thead>
                                                <tr>
                                                    <th>납부일</th>
                                                    <th>납부자명</th>
                                                    <th>결제수단</th>
                                                    <th>납부금액(원)</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <c:when test="${fn:length(map.payment) != 0}">
                                                    <c:forEach var="row" items="${map.payment}" varStatus="value">
                                                        <tr>
                                                            <td class="text-center"><fmt:parseDate pattern="yyyyMMdd" var="fmtPayDay" value="${row.payDay}" /><fmt:formatDate pattern="yyyy.MM.dd" value="${fmtPayDay}" /></td>
                                                            <td class="text-center">${row.cusName}</td>
                                                            <td class="text-center">${row.sveCd}</td>
                                                            <td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.rcpAmt}"/></td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr>
                                                        <td colspan="4" style="text-align: center;">[조회된 내역이
                                                            없습니다.]</td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="collecter-info" class="col col-md-6 col-sm-12 col-12 mb-4">
                            <div class="row mb-2">
								<div class="col col-12">
									<div class="title-sol-type blue">
										<span>이용하시는 기관 정보입니다</span>
										<h4>이용기관 정보</h4>
									</div>
								</div>
							</div>
                            <div class="row col border-bottom">
                                <label class="col col-md-3 col-sm-4 col-5">
                                    <span class="img-icons"><img src="/assets/imgs/common/icon-building-sm.png"></span>
                                    이용기관명
                                </label>
                                <div class="col col-md-9 col-sm-8 col-7">
									<span>${map.chaInfo.chaName}</span>
                                    <%--<input type="text" class="form-control" value="${map.chaInfo.chaName}" readonly style="height: 30px;">--%>
                                </div>
                            </div>
                            <div class="row col border-bottom">
                                <label class="col col-md-3 col-sm-4 col-5 label">
                                    <span class="img-icons"><img src="/assets/imgs/common/icon-phone.png"></span>
                                    연락처
                                </label>
                                <div class="col col-md-9 col-sm-8 col-7">
									<span>${map.chaInfo.ownerTel}</span>
                                    <%--<input type="text" class="form-control" value="${map.chaInfo.ownerTel}" readonly style="height: 30px;">--%>
                                </div>
                            </div>
							<div class="row col" style="margin-bottom: 0; padding: 0;">
								<label class="col">
									<span class="img-icons"><img src="/assets/imgs/common/icon-location.png"></span>주소
								</label>
							</div>
							<div class="row col border-bottom">
								<div class="col col-md-12 col-sm-12">
									<span style="padding: 0 1rem;">${map.chaInfo.chaAddress1} ${map.chaInfo.chaAddress2}</span>
									<%--<input type="text" class="form-control" style="padding: 0 1rem; height: 30px;" value="${map.chaInfo.chaAddress1} ${map.chaInfo.chaAddress2}" readonly>--%>
								</div>
							</div>
                        </div>
                    </div>
                    <div class="row">
						<div class="col col-md-6 col-sm-12 col-12 mb-4">
							<div class="row">
								<div class="col col-12">
									<div class="title-sol-type orange">
										<span>가상계좌 수납관리 서비스의 최신 소식을 알려드려요!</span>
										<h4>공지사항</h4>
									</div>
                                    <div class="see-all text-right">
            							<a href="/common/customerAsist/noticeList">> 전체보기</a>
            						</div>
								</div>
							</div>
							<div class="row mt-2 mb-5">
								<div class="col col-4">
									<img src="/assets/imgs/common/fin.png" style="width:100%;">
								</div>
								<div class="col col-8">
									<table id="notice" class="table table-sm table-hover table-summary">
										<tbody>
											<c:choose>
												<c:when test="${empty map.nList}">
													<tr>
														<td colspan="2" style="text-align: center;">
														[조회된 내역이 없습니다.]
													</td>
													</tr>
												</c:when>
												<c:otherwise>
													<c:forEach var="list" items="${map.nList}">
													  <tr>
														  <td class="has-ellipsis has-arrow">
															  <a class="text-secondary" href="/common/customerAsist/noticeDetail?no=${list.no}">
																  <c:choose>
																	  <c:when test="${list.title.length() > 15}">
																		  ${list.title.substring(0,15) }...
																	  </c:when>
																	  <c:otherwise>
																		  ${list.title}
																	  </c:otherwise>
																  </c:choose>
																  <c:if test="${list.title.length() > 15  }"></c:if>
															  </a>
															  <c:if test="${list.filesize > 0}">
																<img src="/assets/imgs/common/icon-clip.png" class="icon-clip">
															  </c:if>
														  </td>
													  </tr>
													</c:forEach>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div class="col col-md-6 col-sm-12 col-12 mb-4">
			                <div class="row">
								<div class="col col-12">
									<div class="title-sol-type navy">
										<span>이용에 어려움이 있으신가요?</span>
										<h4>자주하는 질문</h4>
									</div>
								</div>
			                </div>
							<div id="faq" class="row mt-2 mb-5 no-gutters">
								<div class="col col-4">
									<div class="category" onclick="moveFaq(1)">
										전체
									</div>
								</div>
								<div class="col col-4">
									<div class="category" onclick="moveFaq(3)">
										납부
									</div>
								</div>
								<div class="col col-4">
									<div class="category" onclick="moveFaq(4)">
										사이트 이용
									</div>
								</div>
								<div class="col col-4">
									<div class="category" onclick="moveFaq(8)">
										현금영수증
									</div>
								</div>
								<div class="col col-4">
								</div>
                                <div class="col col-4 text-right">
            						<img src="/assets/imgs/common/img-sol-character-01.png" style="max-width:107px;" class="sol-character">
            					</div>
							</div>
			            </div>
                    </div>

					<div class="row hidden-on-mobile">
						<div id="application-download" class="col col-md-6 col-sm-12 col-12 mb-4">
							<div class="row mb-2">
								<div class="col col-12">
									<div class="title-sol-type green">
										<span>가상계좌 수납관리 서비스에 대해 알고 싶으세요?</span>
										<h4>매뉴얼 다운로드</h4>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col">
									<div class="row application-box">
										<div class="col col-6">
					                        <label>가상계좌 수납관리 서비스 납부자 매뉴얼</label>
					                        <button type="button" class="btn btn-block btn-cs-center btn-download-application" onclick="fn_download();">다운로드</button>
										</div>
										<div class="col col-6 text-right">
											<img src="/assets/imgs/common/img-download-certificate.png" style="height:93px; width:92px;">
										</div>
									</div>
								</div>
							</div>
						</div>
						<div id="cs-center" class="col col-md-6 col-sm-12 col-12 mb-4">
							<div class="row mb-2">
								<div class="col col-12">
									<div class="title-sol-type sand">
										<span>전문 상담원이 안내해드립니다.</span>
										<h4>고객센터</h4>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col">
									<div class="row cs-box">
										<div class="col col-12">
											<label>전화상담을 통해 전문 상담원이 안내해드립니다.</label>
										</div>
										<div class="col col-6">
											<a href="tel:02-786-8201" class="btn btn-block btn-cs-center btn-cs-phone-number">02-786-8201</a>
										</div>
										<div class="col col-6">
											<button type="button" class="btn btn-block btn-cs-center btn-book-counselling" data-toggle="modal" onclick="fn_makeReservation();">전화 상담 예약</button>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
                </div>
            </div>

<jsp:include page="/WEB-INF/views/include/payer/footer.jsp" flush="false"/>


 <%-- 전화예약상담 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/counselling-on-call.jsp" flush="false"/>

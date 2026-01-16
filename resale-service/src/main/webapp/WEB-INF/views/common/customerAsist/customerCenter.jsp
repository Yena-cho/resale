<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" 	   uri="http://www.springframework.org/security/tags" %>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_GROUP_USER')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/></s:authorize>

<script>
var firstDepth = "nav-link-08";
var secondDepth = "sub-03";
</script>

            <div id="contents">
                <div id="damoa-breadcrumb">
                    <nav class="nav container">
                        <a class="nav-link" href="#">공지사항</a>
                        <a class="nav-link" href="#">자주하는 질문</a>
                        <a class="nav-link active" href="#">고객센터</a>
                    </nav>
                </div>
                <div class="container">
                    <div id="page-title">
                        <div id="breadcrumb-in-title-area" class="row align-items-center">
                            <div class="col-12 text-right">
                                <img src="/assets/imgs/common/icon-home.png" class="mr-2">
                                <span> > </span>
                                <span class="depth-1">고객지원</span>
                                <span> > </span>
                                <span class="depth-2 active">고객센터</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-6">
                                <h2>고객센터</h2>
                            </div>
                            <div class="col-6 text-right">
                                <%--<button type="button" class="btn btn-img mr-2">--%>
                                    <%--<img src="/assets/imgs/common/btn-typo-control.png">--%>
                                <%--</button>--%>
                                <%--<button type="button" class="btn btn-img">--%>
                                    <%--<img src="/assets/imgs/common/btn-print.png">--%>
                                <%--</button>--%>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div id="page-description">
                        <div id="page-description-box">
                            <div class="row align-items-center">
                                <div class="col col-md-8 col-12">
                                    <p class="mb-3">
                                        <strong>
                                            고객센터 전화상담을 통해 전문상담원이 안내해드립니다.<br>
                                            통화량이 많을 경우 E-MAIL을 통해 문의하시면 보다 신속히 처리해드리겠습니다.
                                        </strong>
                                    </p>
                                    <p class="mb-3">
                                        <span class="font-blue">
                                            - 전화 : <a href="tel:02-786-8201">02-786-8201</a><br/>
                                            - E-MAIL : <a href="mailto:cs@finger.co.kr"><u>cs@finger.co.kr</u></a><br/>
                                            - FAX : 0303-0959-8201
                                        </span>
                                    </p>
                                    <p>
                                        전화응답가능시간: 평일 09:00-12:00, 13:00-17:00<br/>
                                        휴무일: 토요일/일요일 및 공휴일
                                    </p>
                                </div>
                                <div class="col col-md-4 col-12 text-right">
                                    <img src="/assets/imgs/support/img-customer-center.png" class="mt-3">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="map" class="mb-5">
                    <div class="container">
                        <div class="row">
                            <div class="col">
                                <iframe
                                        src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3164.3892233561232!2d126.91792661564708!3d37.52232123441614!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x357c9f186dc4992f%3A0xf8cb3180d6209d09!2z7KCE6rK966Co7ZqM6rSA!5e0!3m2!1sko!2skr!4v1571648622424!5m2!1sko!2skr"
                                        width="600" height="450" frameborder="0" scrollable="false" draggable="false">
                                </iframe>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_GROUP_USER')"><jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/footer.jsp" flush="false"/></s:authorize>

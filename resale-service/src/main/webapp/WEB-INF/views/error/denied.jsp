<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_BANKADMIN')"><jsp:include page="/WEB-INF/views/include/bank/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_SYSADMIN')"><jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/></s:authorize>

<script>
    var firstDepth = null;
    var secondDepth = null;
</script>

<div id="contents">
    <div class="container">
        <div id="page-title">
            <div class="row">
                <div class="col-12">
                    <h2>페이지 접근이 거부되었습니다.</h2>
                </div>
            </div>
        </div>
    </div>

    <div id="page-not-found" class="container">
        <div class="row">
            <div class="col-12 mb-2 text-center">
                <img src="/assets/imgs/common/img-denied.png" style="height:168px;">
            </div>
            <div class="col-12 text-center">
                <span>You don’t have permission to access.</span>
            </div>
        </div>
        <div class="row text-center">
            <div class="col-12">
                <h3 class="display-5">“요청하신 페이지 접근이 거부되었습니다.”</h3>
                <p>
                    방문하시려는 페이지 접근 권한이 없습니다.<br>
                    아래 URL 정보를 이용해 로그인 후 이용해 주시기 바랍니다.<br><br>
                    가상계좌 수납관리 서비스 첫 화면 : <a href="https://www.shinhandamoa.com">https://www.shinhandamoa.com</a><br><br>
                    궁굼한 점이 있으시면, 언제든지 가상계좌 수납관리 서비스 고객센터(TEL. 02-786-8201)로 연락주시기 바랍니다.
                </p>
            </div>
        </div>
        <div class="row text-center">
            <div class="col-12">
                <a href="#" onclick="javascript:history.back();" class="btn btn-wide btn-primary btn-outlined">이전</a>
                <a href="/" class="btn btn-wide btn-primary">가상계좌 수납관리 서비스 첫화면</a>
            </div>
        </div>
    </div>
</div>

<s:authorize access="hasRole('ROLE_ADMIN')">
    <jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/>
</s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')">
    <jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/>
</s:authorize>
<s:authorize access="hasRole('ROLE_USER')">
    <jsp:include page="/WEB-INF/views/include/payer/footer.jsp" flush="false"/>
</s:authorize>
<s:authorize access="hasRole('ROLE_BANKADMIN')">
    <jsp:include page="/WEB-INF/views/include/footer.jsp" flush="false"/>
</s:authorize>
<s:authorize access="hasRole('ROLE_SYSADMIN')">
    <jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>
</s:authorize>
<s:authorize access="isAnonymous()">
    <jsp:include page="/WEB-INF/views/include/footer.jsp" flush="false"/>
</s:authorize>

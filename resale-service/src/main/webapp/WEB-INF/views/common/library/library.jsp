<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" 	   uri="http://www.springframework.org/security/tags" %>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_GROUP_USER')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/></s:authorize>

<script>
    var firstDepth = "nav-link-07";
    var secondDepth = "sub-01";
</script>

<script type="text/javascript">
    // acrobat reader 설치페이지 이동
    function fn_acrobatDownload() {
        window.open("https://get.adobe.com/reader/?loc=kr");
    }

    //파일 다운로드
    function fn_download(str) {
        fncClearTime();

        $('#fileName').val(str);
        $('#flag').val(str);

        document.fileForm.action = "/common/library/download";
        document.fileForm.submit();
    }
</script>

<form id="fileForm" name="fileForm" method="post">
    <input type="hidden" id="fileName" name="fileName">
    <input type="hidden" id="flag" 	   name="flag">
</form>

<div id="contents">
    <div id="damoa-breadcrumb">
        <nav class="nav container">
            <a class="nav-link active" href="#">공지사항</a>
            <a class="nav-link" href="#">자주하는 질문</a>
            <a class="nav-link" href="#">고객센터</a>
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
                    <span class="depth-2 active">공지사항</span>
                </div>
            </div>
            <div class="row">
                <div class="col-12">
                    <h2>자료실</h2>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div id="page-description">
            <div class="row">
                <div class="col-12">
                    <p>
                        서비스 이용에 필요한 서식모음 자료실입니다. 신청서 및 설명서가 보이지 않을 경우 PC에 Acrobat Reader를 설치하세요.
                        <a href="#" class="btn btn-acrobat-reader-download" onclick="fn_acrobatDownload();">
                            <img src="/assets/imgs/common/icon-acrobat-reader.png">
                            ACROBAT READER 설치
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div id="downloads-list">
            <div class="table-option row no-gutters mb-2">
                <div class="col-md-6 col-sm-12 col-12 form-inline">
                    <span class="amount mr-2">[총 <em class="font-blue">4</em>건]</span>
                </div>
                <div class="col-md-6 col-sm-12 col-12 text-right mt-1">
<%--                    <button class="btn btn-sm btn-gray-outlined" onclick="fn_download('all');">전체다운로드</button>--%>
                </div>
            </div>
            <div class="row">
                <div class="table-responsive pd-n-mg-o col mb-3">
                    <table id="notice-list" class="table table-sm table-hover table-primary">
                        <colgroup>
                            <col width="60">
                            <col width="400">
                            <col width="520">
                            <col width="130">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>NO</th>
                            <th>구분</th>
                            <th>제목</th>
                            <th>첨부파일</th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr>
                            <td>1</td>
                            <td class="text-center">(주)핑거 제출서류</td>
                            <td class="text-left">가상계좌 수납관리 서비스 이용 신청서</td>
                            <td class="text-center">
<%--                                <a href="#" class="btn btn-xs btn-gray-outlined" onclick="fn_download('cms');">다운로드</a>--%>
                            </td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td class="text-center">(주)핑거 제출서류</td>
                            <td class="text-left">가상계좌 수납관리 서비스 변경 신청서</td>
                            <td class="text-center">
<%--                                <a href="#" class="btn btn-xs btn-gray-outlined" onclick="fn_download('info');">다운로드</a>--%>
                            </td>
                        </tr>
                        <tr>
                            <td>3</td>
                            <td class="text-center">(주)핑거 제출서류</td>
                            <td class="text-left">가상계좌 수납관리 서비스 해지 신청서</td>
                            <td class="text-center">
<%--                                <a href="#" class="btn btn-xs btn-gray-outlined" onclick="fn_download('withdraw');">다운로드</a>--%>
                            </td>
                        </tr>
                        <tr>
                            <td>4</td>
                            <td class="text-center">(주)핑거 제출서류</td>
                            <td class="text-left">가상계좌 수납관리 서비스 수수료 자동출금 동의서</td>
                            <td class="text-center">
<%--                                <a href="#" class="btn btn-xs btn-gray-outlined" onclick="fn_download('kakaoAt');">다운로드</a>--%>
                            </td>
                        </tr>
                        </tbody>
                    </table>
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

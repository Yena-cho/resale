<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_GROUP_USER')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/></s:authorize>


<script>
var firstDepth = "nav-link-08";
var secondDepth = "sub-04";

$(document).ready(function(){
    if($("#realfilename").text() != ""){
        var x = $("#hfilesize").text();
        var s = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'];
        var e = Math.floor(Math.log(x) / Math.log(1024));

        $("#filesize").text('['+ (x / Math.pow(1024, e)).toFixed(2) + " " + s[e] +']');
    }

});

function fn_qnaeList() {
	location.href = "/common/customerAsist/qnaList";
}

function deleteByNo(no) {
    $('#qnaNo').val(no);
    document.qnaForm.action = "/common/customerAsist/qnaDelete";
    document.qnaForm.submit();
}

function modifyByNo(no) {
    $('#qnaNo').val(no);
    document.qnaForm.action = "/common/customerAsist/qnaUpdate";
    document.qnaForm.submit();
}

function fn_download(file, no){
    fncClearTime();
    var obj = $("#realfilename").text();
    var obj2 = $("#hfilesize").text();
    var obj3 = $("#hfileext").text();
    var url = "/common/customerAsist/fileDownload";

    $('#ffileName').val(obj);
    $('#ffileSize').val(obj2);
    $('#ffileId').val(file);
    $('#ffileExt').val(obj3);
    $('#ffileNo').val(no);

    document.searchForm.action = url;
    document.searchForm.submit();
}
</script>
<form id="searchForm" name="searchForm" method="post">
	<input type="hidden" name="filename" id="ffileName" />
	<input type="hidden" name="filesize" id="ffileSize" />
	<input type="hidden" name="fileid" id="ffileId" />
	<input type="hidden" name="fileext" id="ffileExt" />
	<input type="hidden" name="fileno" id="ffileNo" />
</form>

<form id="qnaForm" name="qnaForm" method="post">
    <input type="hidden" id="qnaNo" name="no">
    <input type="hidden" id="flag" 	name="flag" value="info">
</form>

            <div id="contents">
                <div class="container">
                    <div id="page-title">
                        <div class="row">
                            <div class="col-6">
                                <h2>서비스문의</h2>
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
                        <div class="row">
                            <div class="col-12">
                                <p>서비스 이용 중 발생한 문의사항을 남겨 주시면 신속하게 답변해 드리겠습니다.</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="container mt-4">
                    <div class="row mb-3">
                        <div class="col">
                            <table id="qna-view" class="table table-sm table-form table-primary">
                                <tbody>
                                    <tr class="row no-gutters">
                                        <th class="col-md-1 col-sm-4 col-4 text-left">작성자</th>
                                        <td class="col-md-5 col-sm-8 col-8">${dto.writer }</td>
                                        <th class="col-md-1 col-sm-4 col-4 text-left">E-MAIL</th>
                                        <td class="col-md-5 col-sm-8 col-8">${dto.email }</td>
                                    </tr>
                                    <tr class="row no-gutters">
                                        <th class="col-md-1 col-sm-4 col-4 text-left">분류</th>
                                        <td class="col-md-5 col-sm-8 col-8"> ${dto.data7 }</td>
                                        <th class="col-md-1 col-sm-4 col-4 text-left">파일첨부</th>
                                        <td class="col-md-5 col-sm-8 col-8">
                                        	<a href="#" class="ellipsis mobile-ellipsis"  onclick="fn_download('${dto.fileid}', '${dto.no}')" id="fileid">${dto.filename }</a>
                                        	<div id="realfilename" style="display: none;">${dto.filename }</div>
                                        	<span id="filesize" class="ellipsis-filesize"></span>
                                        	<div id="hfilesize" style="display: none;">${dto.filesize }</div>
                                        	<div id="hfileext" style="display: none;">${dto.fileext }</div>
                                        </td>
                                    </tr>
                                    <tr class="row no-gutters" id="qtitle">
                                        <th class="col-md-1 col-sm-4 col-4 text-left">제목</th>
                                        <td class="col-md-5 col-sm-8 col-8">${dto.title }</td>
                                        <th class="col-md-1 col-sm-4 col-4 text-left">작성일</th>
                                        <td class="col-md-5 col-sm-8 col-8">${dto.day }</td>
                                    </tr>
                                    <tr class="row no-gutters" id="qcontents">
                                        <td colspan="4" class="col content-area align-items-start" style="border-bottom: none !important;">${dto.contents }</td>
                                    </tr>

                                    <c:forEach var="row" items="${map.repleList }">
	                                    <tr class="row no-gutters">
	                                        <td colspan="4" class="col content-area align-items-start" style="flex-direction: column;" >
	                                            <div>
                                                    <h6 class="float-left mr-3"><strong><span>${row.writer }</span></strong></h6>
                                                    <span class="float-left">${row.day }</span>
	                                            </div>
                                                    ${row.contents }
	                                        </td>
	                                    </tr>
                                    </c:forEach>

                                   <c:choose>
                                   	<c:when test="${map.list.size() == 2 and dto.no == map.list[1].no}">
                                   		<tr class="row no-gutters">
                                         <th class="col-md-2 col-sm-4 col-4 text-left">이전글</th>
                                         <td colspan="3" class="col-md-10 col-sm-8 col-8">이전글이 없습니다.</td>
                                     </tr>
                                     <tr class="row no-gutters">
                                         <th class="col-md-2 col-sm-4 col-4 text-left">다음글</th>
                                         <td colspan="3" class="col-md-10 col-sm-8 col-8">
                                         	<a href="/common/customerAsist/qnaDetail?no=${map.list[0].no}">${map.list[0].title}</a>
                                         </td>
                                     </tr>
                                   	</c:when>
                                   	<c:when test="${map.list.size() == 3 and dto.no < map.list[0].no}">
                                   		<tr class="row no-gutters">
                                         <th class="col-md-2 col-sm-4 col-4 text-left">이전글</th>
                                         <td colspan="3" class="col-md-10 col-sm-8 col-8">
                                         	<a href="/common/customerAsist/qnaDetail?no=${map.list[2].no}">${map.list[2].title}</a>
                                         </td>
                                     </tr>
                                     <tr class="row no-gutters">
                                         <th class="col-md-2 col-sm-4 col-4 text-left">다음글</th>
                                         <td colspan="3" class="col-md-10 col-sm-8 col-8">
                                         	<a href="/common/customerAsist/qnaDetail?no=${map.list[0].no}">${map.list[0].title}</a>
                                         </td>
                                     </tr>
                                   	</c:when>
                                   	<c:when test="${map.list.size() == 2 and dto.no == map.list[0].no}">
                                   		<tr class="row no-gutters">
                                         <th class="col-md-2 col-sm-4 col-4 text-left">이전글</th>
                                         <td colspan="3" class="col-md-10 col-sm-8 col-8">
                                         	<a href="/common/customerAsist/qnaDetail?no=${map.list[1].no}">${map.list[1].title}</a>
                                        	</td>
                                     </tr>
                                     <tr class="row no-gutters">
                                         <th class="col-md-2 col-sm-4 col-4 text-left">다음글</th>
                                         <td colspan="3" class="col-md-10 col-sm-8 col-8">다음글이 없습니다.</td>
                                     </tr>
                                   	</c:when>
                                   </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <c:if test="${map.repleList.size() == 0 }">
                    <div class="row list-button-group-bottom">
                        <div class="col-12 text-right">
                            <button onclick="deleteByNo(${dto.no})" class="btn btn-sm btn-d-gray" >삭제</button>
                            <button onclick="modifyByNo(${dto.no})" class="btn btn-sm btn-d-gray" >수정</button>
                        </div>
                    </div>
                    </c:if>
                    <div class="row mb-5">
                        <div class="col text-center">
                            <button class="btn btn-primary btn-wide" onclick="fn_qnaeList();">
                                	목록
                            </button>
                        </div>
                    </div>
                </div>
            </div>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_GROUP_USER')"><jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/footer.jsp" flush="false"/></s:authorize>
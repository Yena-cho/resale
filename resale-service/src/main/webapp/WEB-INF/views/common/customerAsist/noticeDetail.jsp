
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" 	   uri="http://www.springframework.org/security/tags" %>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_GROUP_USER')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/></s:authorize>
<script>

var firstDepth = "nav-link-08";
var secondDepth = "sub-01";
</script>

<script type="text/javascript">

function fn_noticeList() {
	var curPage = ${map.curPage};
	$(":hidden[name=curPage]").val(curPage);
	$("#searchForm").attr("action", "/common/customerAsist/noticeList");
	$("#searchForm").submit();
}

function fn_noticeDetailView(seq) {
	var curPage = ${map.curPage};
	$(":hidden[name=no]").val(seq);
	$(":hidden[name=curPage]").val(curPage);
	$("#noticeForm").submit();
}

//파일 다운로드
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
	$('#ffileExt').val(obj3);
	$('#ffileNo').val(no);
	document.searchForm.action = url;
	document.searchForm.submit();
}

//파일 용량 표시
$(document).ready(function(){
	if($("#realfilename").text() != ""){
		var x = $("#hfilesize").text();
		var s = ['bytes', 'KB', 'MB', 'GB', 'TB', 'PB'];
		var e = Math.floor(Math.log(x) / Math.log(1024));

		$("#filesize").text('['+ (x / Math.pow(1024, e)).toFixed(2) + " " + s[e] +']');
	}
});

</script>
<style>
    .contents img {
        max-width: 100%;
    }

    #notice-view.table-form td.content-area, #qna-view.table-form td.content-area {
        min-height: 18rem;
        padding: 5px !important;
        border-bottom: 1px solid #bfc2d7;
    }
</style>
<form id="searchForm" name="searchForm" method="post">
	<input type="hidden" name="filename" id="ffileName" />
	<input type="hidden" name="filesize" id="ffileSize" />
	<input type="hidden" name="fileid" id="ffileId" />
	<input type="hidden" name="fileext" id="ffileExt" />
	<input type="hidden" name="fileno" id="ffileNo" />
	<input type="hidden" name="curPage" id="curPage" />
</form>

<form id="noticeForm" method="post" action="/common/customerAsist/noticeDetail">
	<input type="hidden" name="no" />
	<input type="hidden" name="curPage" />
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
                       <div class="col-6">
                           <h2>공지사항</h2>
                       </div>
                       <div class="col-6 text-right">
                       </div>
                   </div>
               </div>
           </div>

           <div class="container mt-4">
               <div id="notice">
                   <div class="row mb-3">
                       <div class="col">
                           <table id="notice-view" class="table table-sm table-form table-primary">
                               <thead class="container-fluid">
                                   <tr class="row no-gutters mobile-hiddeon-border">
                                       <th class="col-md-1 col-sm-4 col-4 text-left">제목</th>
                                       <td colspan="3" class="col-md-11 col-sm-8 col-8">
                                           <span class="table-title-ellipsis">${dto.title}</span>
                                       </td>
                                   </tr>
                               </thead>
                               <tbody>
                                   <tr class="row no-gutters">
                                       <th class="col-md-1 col-sm-4 col-4 text-left">등록일</th>
                                       <td class="col-md-5 col-sm-8 col-8">${dto.day}</td>
                                       <th class="col-md-1 col-sm-4 col-4 text-left">첨부파일</th>
                                       <td class="col-md-5 col-sm-8 col-8">
											<a href="#" class="ellipsis mobile-ellipsis" onclick="fn_download('${dto.fileid}', '${dto.no}')">${dto.filename }</a>
											<div id="realfilename" style="display: none;">${dto.filename }</div>
											<span id="filesize" class="ellipsis-filesize"></span>
											<div id="hfilesize" style="display: none;">${dto.filesize }</div>
                                       </td>
                                   </tr>
                                   <tr class="row no-gutters">
                                       <td colspan="4" align="left" class="col content-area align-items-start board-word-break contents">
                                               <div style="width: 100%; position: relative">
                                                   ${dto.contents }
                                               </div>
                                       </td>
                                   </tr>
                                   <c:choose>
                                   	<c:when test="${map.list.size() == 2 and dto.no == map.list[1].no}">
                                   		<tr class="row no-gutters">
                                             <th class="col-md-1 col-sm-4 col-4 text-left">이전글</th>
                                             <td colspan="3" class="col-md-11 col-sm-8 col-8">이전글이 없습니다.</td>
                                         </tr>
                                         <tr class="row no-gutters">
                                             <th class="col-md-1 col-sm-4 col-4 text-left">다음글</th>
                                             <td colspan="3" class="col-md-11 col-sm-8 col-8">
                                                 <a href="#" onclick="fn_noticeDetailView(${map.list[0].no});"><span class="table-title-ellipsis">${map.list[0].title}</span></a>
                                             </td>
                                         </tr>
                                   	</c:when>
                                   	<c:when test="${map.list.size() == 3 and dto.no < map.list[0].no}">
                                   		<tr class="row no-gutters">
                                             <th class="col-md-1 col-sm-4 col-4 text-left">이전글</th>
                                             <td colspan="3" class="col-md-11 col-sm-8 col-8">
                                                 <a href="#" onclick="fn_noticeDetailView(${map.list[2].no});"><span class="table-title-ellipsis">${map.list[2].title}</span></a>
                                             </td>
                                         </tr>
                                         <tr class="row no-gutters">
                                             <th class="col-md-1 col-sm-4 col-4 text-left">다음글</th>
                                             <td colspan="3" class="col-md-11 col-sm-8 col-8">
                                                 <a href="#" onclick="fn_noticeDetailView(${map.list[0].no});"><span class="table-title-ellipsis">${map.list[0].title}</span></a>
                                             </td>
                                         </tr>
                                   	</c:when>
                                   	<c:when test="${map.list.size() == 2 and dto.no == map.list[0].no}">
                                   		<tr class="row no-gutters">
                                            <th class="col-md-1 col-sm-4 col-4 text-left">이전글</th>
                                            <td colspan="3" class="col-md-11 col-sm-8 col-8">
                                                <a href="#" onclick="fn_noticeDetailView(${map.list[1].no});"><span class="table-title-ellipsis">${map.list[1].title}</span></a>
                                            </td>
                                         </tr>
                                         <tr class="row no-gutters">
                                             <th class="col-md-1 col-sm-4 col-4 text-left">다음글</th>
                                             <td colspan="3" class="col-md-11 col-sm-8 col-8">다음글이 없습니다.</td>
                                         </tr>
                                   	</c:when>
                                   </c:choose>
                               </tbody>
                           </table>
                       </div>
                   </div>
                   <div class="row mb-5">
                       <div class="col text-center">
                           <button class="btn btn-primary" onclick="fn_noticeList();">
                              		 목록
                           </button>
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
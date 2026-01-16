<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_GROUP_USER')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/></s:authorize>

<script src="../../../../assets/js/common.js?version=${project.version}"></script>

<script>
var firstDepth = "nav-link-08";
var secondDepth = "sub-04";


$(document).ready(function(){
    list(1);
});

function moveToWrite() {
    document.qnaForm.action = "/common/customerAsist/qnaWrite";
    document.qnaForm.submit();
}

function qnaDetail(no) {
    $('#qnaNo').val(no);
    document.qnaForm.action = "/common/customerAsist/qnaDetail";
    document.qnaForm.submit();

}

//페이지 변경
function list(page) {
	var param ={
			keyWord			: $("#keyword").val(),
			searchOrderBy	: "num",
			pageScale		: "10",
			curPage			: page,
			searchOption	: $("#search_option").val()
	};
	$.ajax({
		type 		: "POST",
		async 		: true,
		url 		: "common/customerAsist/ajaxqnaList",
		contentType : "application/json; charset=utf-8",
		data 		: JSON.stringify(param),
		success		: function(result){
			if(result.retCode == "0000"){
				fnGrid(result, 'reSearchbody');
			}else{
				swal({
			           type: 'success',
			           text: '시스템 오류입니다.',
			           confirmButtonColor: '#3085d6',
			           confirmButtonText: '확인'
			       })
			}

		}
	});
}

function fn_qnaListCall() {
	/* if ($("#keyword").val() == null || $("#keyword").val() == "") {
		swal({
	           type: 'success',
	           text: '검색어를 확인해 주세요.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       }).then(function(){
	    	   location.href = "/common/customerAsist/qnaList";
	       });
	}else { */
		var curpage = ${map.curPage};
		var bbs = 7;
		var param ={
				keyWord			: $("#keyword").val(),
				searchOrderBy	: "num",
				pageScale		: "10",
				curPage			: curpage,
				searchOption	: $("#search_option").val()
		};
		$.ajax({
			type 		: "POST",
			async 		: true,
			url 		: "common/customerAsist/ajaxqnaList",
			contentType : "application/json; charset=utf-8",
			data 		: JSON.stringify(param),
			success		: function(result){
				if(result.retCode == "0000"){
					fnGrid(result, 'reSearchbody');
				}else{
					swal({
				           type: 'success',
				           text: '시스템 오류입니다.',
				           confirmButtonColor: '#3085d6',
				           confirmButtonText: '확인'
				       })
				}
			}
		});
	//}
}


function fnGrid(result, obj) {
	var str = "";

	if(result.count <=0){
		str += '<tr><td colspan="3" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
		$("#totcount").text(result.count);
	}else {
		$.each(result.list, function(i, v) {
			str += "<tr>";
			str +=		"<td>"+ v.rn +"</td>"
			str += 		"<td class='text-left'>";
			str +=			"<a href='#' onclick='qnaDetail("+v.no+");' style='color: black;'>"+basicEscape(v.title)+"</a>&nbsp;";
			str += 			"<span class='font-blue'>["+ v.datcnt +"]</span>&nbsp;";
							if(v.filename != "" && v.filename != null){
								str +="<img src='/assets/imgs/common/icon-clip.png' class='icon-clip'>&nbsp;";
							}
							if(v.recently <= 20){
								str+='<font style="color: red;">new</font>';
							}
			str +=		"</td>";
			str +=		"<td>"+ v.day +"</td>";
			str += "<tr>";
		});
	}


	$("#"+ obj).html(str);
	$("#totcount").text(result.count);
	ajaxPaging(result, 'PageArea');
}

</script>
<form id="qnaForm" name="qnaForm" method="post">
    <input type="hidden" id="qnaNo" name="no">
    <input type="hidden" id="flag" 	name="flag">
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
                <div class="container">
                    <div class="row">
                        <div class="table-responsive col mb-3">
                            <table id="qna-list" class="table table-sm table-hover table-primary table-qna-list">
                                <colgroup>
                                    <col width="70">
                                    <col width="800">
                                    <col width="230">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>제목</th>
                                        <th>작성일</th>
                                    </tr>
                                </thead>

                                <tbody id="reSearchbody">

                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="row list-button-group-bottom">
                        <div class="col-12 text-right">
                            <button class="btn btn-sm btn-d-gray" onclick="moveToWrite();">작성</button>
                        </div>
                    </div>

					<jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false" />

                    <div id="search-box-list" class="container bg-faint-grey mt-4 mb-5 p-3">
                        <div class="row justify-content-center">
                            <div class="col-md-7 col-12 form-inline">
                                <select class="form-control" id="search_option" name="search_option">
                                    <option value="all" <c:if test="${map.search_option == 'all'}">selected</c:if>>전체</option>
									<option value="title" <c:if test="${map.search_option == 'title'}">selected</c:if>>제목</option>
									<option value="contents" <c:if test="${map.search_option == 'content'}">selected</c:if>>내용</option>
                                </select>
                                <input class="form-control mr-1" type="text" id="keyword"  onkeypress="if(event.keyCode==13) {fn_qnaListCall();}">
                                <button class="btn btn-sm btn-primary" onclick="fn_qnaListCall()">검색</button>
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

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_GROUP_USER')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/></s:authorize>

<script>
var firstDepth = "nav-link-08";
var secondDepth = "sub-01";
</script>

<script src="/assets/js/common.js?version=${project.version}"></script>

<script type="text/javascript">

//페이지 변경
function list(page) {
	$("#curPage").val(page);
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
		url 		: "common/customerAsist/ajaxNoticeList",
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



//검색 버튼 눌럿을경우 동작
function fn_noticeListCall() {
	/* if ($("#keyword").val() == null || $("#keyword").val() == "") {
		swal({
	           type: 'success',
	           text: '검색어를 확인해 주세요.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       }).then(function(){
	    	   location.href = "/common/customerAsist/noticeList";
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
			url 		: "common/customerAsist/ajaxNoticeList",
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

function fn_noticeDetailView(seq) {
//	var curPage = $("#curPage").val();
//	location.href = "/common/customerAsist/noticeDetail?no=" + seq+"&curPage="+curPage;
	$(":hidden[name=no]").val(seq);
	$(":hidden[name=curPage]").val($("#curPage").val());
	$("#noticeForm").submit();
}

function fnGrid(result, obj){
	var str = '';

	if(result.count <=0){
		str += '<tr><td colspan="4" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
		$("#totcount").text(result.count);
	}else{
		$('#totcount').text(result.count);
		$.each(result.list, function(i, v){
			str+='<tr>';
			str+='<td>';
			if(v.isfix == 1){
				str+='<span class="label-featured">중요</span>';
			}else{
				str+=v.rn
			}
			str+='</td>';
			str+='<td>';
			//<a href="#" onclick="fn_noticeDetailView('${row.no}');" style="color: black;">${row.title}</a>
			str+='<a href="#" onclick="fn_noticeDetailView('+ v.no + ');" style="color: black;">' + v.title + '</a>';
			if(v.recently <= 20){
				str+='<font style="color: red;">&nbsp;new</font>';
			}
			str+='</td>';
			str+='<td>';
			str+=v.day;
			str+='</td>';
			str+='<td>';
			str+=v.count;
			str+='</td>';
			str+='</tr>';
		});
	}
	$("#"+ obj).html(str);
	$("#totcount").text(result.count);
	ajaxPaging(result, 'PageArea');
}


</script>

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
                        <p>최신소식 및 각종 공지사항을 전해드립니다.</p>
                    </div>
                </div>
            </div>
        </div>
			<input type="hidden" name="curPage" id="curPage" value="${map.curPage}">
			<div class="container">
				<div id="search-result">
					<div class="table-option row no-gutters mb-2">
                            <div class="col-md-6 col-sm-12 col-12 form-inline">
                                <span class="amount mr-2">[총 <em class="font-blue" id="totcount">${map.count}</em>건]</span>
                            </div>
                           <%--  <c:if test="${map.bbs == 1000 }">
                            <div class="col-md-6 col-sm-12 col-12 text-right mt-1">
                                <select class="form-control"  name="search_orderBy" id="search_orderBy">
                                    <option value="day" <c:if test="${map.search_orderBy == 'regDt'}">selected</c:if>>등록일자순 정렬</option>
                                    <option value="state" <c:if test="${map.search_orderBy == 'state'}">selected</c:if>>상태별 정렬</option>
                                </select>
                                <select class="form-control" name="PAGE_SCALE" id="PAGE_SCALE">
                                    <option value="10" <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회</option>
                                    <option value="20" <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회</option>
                                    <option value="50" <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회</option>
                                    <option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회</option>
                                </select>
                            </div>
                            </c:if> --%>
                        </div>


					<div class="row">
						<div class="table-responsive pd-n-mg-o col mb-3">
								<input type="hidden" id="curPage" value="">
							<table id="notice-list" class="table table-sm table-hover table-primary">
								<colgroup>
									<col width="70">
									<col width="750">
									<col width="180">
									<col width="100">
								</colgroup>
								<thead>
									<tr>
										<th>NO</th>
                                        <th>제목</th>
                                        <th>등록일</th>
                                        <th>조회수</th>
									</tr>
								</thead>
								<tbody id="reSearchbody">
								<c:choose>
									<c:when test="${map.list.size() > 0}">
										<c:forEach var="row" items="${map.list}">
											<tr>
												<td>
													<c:choose>
														<c:when test="${row.isfix == 1}">
															<span class="label-featured">중요</span>
														</c:when>
														<c:otherwise>${row.rn}</c:otherwise>
													</c:choose>
												</td>
												<td>
													<a href="#" onclick="fn_noticeDetailView('${row.no}');" style="color: black;">${row.title}</a>
													<c:if test="${row.recently <= 20 }">
														<font style="color: red;">new</font>
													</c:if>
												</td>
												<td>
													${row.day }
												</td>
												<td>
													${row.count}
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="4" style="text-align: center;">
												[조회된 내역이 없습니다.]
											</td>
										</tr>
									</c:otherwise>
								</c:choose>
								</tbody>
							</table>
						</div>
					</div>

					<jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false" />

					<div id="search-box-list" class="container bg-faint-grey mt-4 mb-5 p-3">
						<div class="row justify-content-center">
							<div class="col-md-7 col-12 form-inline">
								<select class="form-control" id="search_option" name="search_option">
									<option value="all" <c:if test="${map.search_option == 'all'}">selected</c:if>>전체</option>
									<option value="title" <c:if test="${map.search_option == 'title'}">selected</c:if>>제목</option>
									<option value="contents" <c:if test="${map.search_option == 'contents'}">selected</c:if>>내용</option>
								</select>
								<input class="form-control mr-1" type="text" id="keyword" name="keyword" value="${map.keyword}" onkeypress="if(event.keyCode==13) {fn_noticeListCall();}">
								<button class="btn btn-sm btn-primary" onclick="fn_noticeListCall();">검색</button>
							</div>
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

<script type="text/javascript">
$("#search_orderBy").change(function(){
	swal({
	       type: 'info',
	       text: "등록일순",
	       confirmButtonColor: '#3085d6',
	       confirmButtonText: '확인'
		});
})

$("#PAGE_SCALE").change(function(){
	swal({
	       type: 'info',
	       text: "등록일순",
	       confirmButtonColor: '#3085d6',
	       confirmButtonText: '확인'
		});
})
</script>

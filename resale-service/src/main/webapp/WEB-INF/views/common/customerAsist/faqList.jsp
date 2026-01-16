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

<script src="/assets/js/common.js?version=${project.version}"></script>
<style>
    .contents img {
        max-width: 100%;
    }

    #faq-list.table-form td.content-area, #qna-view.table-form td.content-area {
        min-height: 18rem;
        padding: 5px !important;
        border-bottom: 1px solid #bfc2d7;
    }
</style>
<script>
var firstDepth = "nav-link-08";
var secondDepth = "sub-02";
</script>

<%
	String no = request.getParameter("no");
%>

<script type="text/javascript">

var gvIcon;
var curpage = 1;

//페이지 변경
function list(page){
	curpage = page;
	var param = {
		keyWord			: $("#keyword").val(),
		icon 			: gvIcon,
		searchOrderBy	: "num",
		pageScale		: "10",
		curPage			: page,
		searchOption	: $("#search_option").val()
	}
	$.ajax({
		type 		: "POST",
		async 		: true,
		url 		: "common/customerAsist/ajaxFaqList",
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
	})
}

//아이콘 클릭
function fn_iconSearch(icon){
	gvIcon = icon;
	$("#keyword").val("");
	var param ={
		keyWord			: $("#keyword").val(),
		icon			: icon,
		searchOrderBy	: "num",
		pageScale		: "10",
		curPage			: "1"
	};
	$.ajax({
		type 		: "POST",
		async 		: true,
		url 		: "common/customerAsist/ajaxFaqList",
		contentType : "application/json; charset=utf-8",
		data 		: JSON.stringify(param),
		success		: function(result){
			if(result.retCode == "0000"){
				$("#all").removeClass("active");
				$("#req").removeClass("active");
				$("#pay").removeClass("active");
				$("#site").removeClass("active");
				$("#noti").removeClass("active");
				$("#receive").removeClass("active");
				$("#additional").removeClass("active");
				$("#cash").removeClass("active");
				$("#"+icon).addClass("active");

				fnGrid(result, 'reSearchbody');
			} else {
				swal({
		            type: 'success',
		            text: '시스템 오류입니다.',
		            confirmButtonColor: '#3085d6',
		            confirmButtonText: '확인'
		        });
			}

		}
	});
}

//검색버튼 클릭
function fn_faqListCall(page) {
	if(page == null || page == 'undefined'){
		cuPage = '1';
	} else {
		curpage = page;
	}
	$("#all").addClass("active");
	$("#req").removeClass("active");
	$("#pay").removeClass("active");
	$("#site").removeClass("active");
	$("#noti").removeClass("active");
	$("#reveive").removeClass("active");
	$("#additional").removeClass("active");
	$("#cash").removeClass("active");

	var param ={
			keyWord			: $("#keyword").val(),
			icon			: "all",
			searchOrderBy	: "num",
			pageScale		: "10",
			curPage			: curpage, //"1"
			searchOption	: $("#search_option").val()
	};
	$.ajax({
		type 		: "POST",
		async 		: true,
		url 		: "common/customerAsist/ajaxFaqList",
		contentType : "application/json; charset=utf-8",
		data 		: JSON.stringify(param),
		success		: function(result){
			if(result.retCode == "0000"){
				fnGrid(result, 'reSearchbody');
			} else {
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

function fnGrid(result, obj){
	var str = '';
	$("#totcount").text(result.count);

	if(result.count <=0){
		str += '<tr><td colspan="3" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
		$("#totcount").text(result.count);
	}else{
		$.each(result.list, function(i, v){
			var gubun;
			if(v.bbs == 51 || v.bbs == 61 || v.bbs == 71){
				gubun = "신청/해지";
			}else if(v.bbs ==52 || v.bbs == 62 || v.bbs == 72){
				gubun = "납부";
			}else if(v.bbs ==53 || v.bbs == 63 || v.bbs == 73){
				gubun = "사이트이용";
			}else if(v.bbs ==54 || v.bbs == 64 || v.bbs == 74){
				gubun = "고지";
			}else if(v.bbs ==55 || v.bbs == 65 || v.bbs == 75){
				gubun = "수납";
			}else if(v.bbs ==56 || v.bbs == 66 || v.bbs == 76){
				gubun = "부가서비스";
			}else if(v.bbs ==57 || v.bbs == 67 || v.bbs == 77){
				gubun = "현금영수증";
			}
            str += '<div style="width: 100%; position: relative">';
			str += '<tr id="'+v.rn+'" onclick="toggleById('+ v.rn +')">';
			str += '<td>'+v.rn+'</td>';
			str += '<td>'+gubun+'</td>';
			str += '<td><span class="font-orange">Q. </span>'+v.title+'</td>';
			str += '</tr>';
			str += '<tr class="'+v.rn+' answer" style="display: none">';
			str += '<td class="font-blue">A.</td>';
			str += '<td colspan="2" class="table-title-ellipsis content-area contents">'+v.contents+'</td>';
			str += '</tr>';
            str += '</div>';

		});
	}
	$("#"+obj).html(str);
	ajaxPaging(result, 'PageArea');
}

//클릭시 태용 미리보기
function toggleById(id) {
	$("."+id).toggle();
}

$(document).ready(function(){
	var icon = "";
	var num = "<c:out value="<%=no%>" escapeXml="true" />";
	if (num=="2") { // 신청/해지
		icon = "req";
	} else if (num=="3") {	// 납부
		icon = "pay";
	} else if (num=="4") { // 사이트이용
		icon = "site";
	} else if (num=="5") { // 고지
		icon = "noti";
	} else if (num=="6") { // 수납
		icon = "receive";
	} else if (num=="7") { // 부가서비스
		icon = "additional";
	} else if (num=="8") { // 현금영수증
		icon = "cash";
	} else {
		icon = "all";
	}
	
	fn_iconSearch(icon);
});

</script>

            <div id="contents">
                <div id="damoa-breadcrumb">
                    <nav class="nav container">
                        <a class="nav-link" href="#">공지사항</a>
                        <a class="nav-link active" href="#">자주하는 질문</a>
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
                                <span class="depth-2 active">자주하는 질문</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-6">
                                <h2>자주하는 질문</h2>
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
                <s:authorize access="isAnonymous()">
                <div class="container" id="beforeLogin">
                    <div id="faq-filter">
                        <div class="row justify-content-center text-center">
                            <div class="col col-md-2 col-sm-3 col-3 active"  onclick="fn_iconSearch('all');" id="all">
                                <img src="/assets/imgs/common/icon-chat-balloon.png"  class="icon-chat-balloon">
                                <h5>전체</h5>
                            </div>
                            <div class="col col-md-2 col-sm-3 col-3" onclick="fn_iconSearch('req');" id="req">
                                <img src="/assets/imgs/common/icon-pencil-on-document-2.png" class="icon-pencil-on-document-2">
                                <h5>신청/해지</h5>
                            </div>
                            <div class="col col-md-2 col-sm-3 col-3" onclick="fn_iconSearch('pay');" id="pay">
                                <img src="/assets/imgs/common/icon-coin-on-hand-2.png"  class="icon-coin-on-hand-2">
                                <h5>납부</h5>
                            </div>
                            <div class="w-100 d-sm-block d-md-none"></div>
                            <div class="col col-md-2 col-sm-3 col-3" onclick="fn_iconSearch('site');" id="site">
                                <img src="/assets/imgs/common/icon-cog-in-monitor.png" class="icon-cog-in-monitor">
                                <h5>사이트 이용</h5>
                            </div>
                            <div class="col col-md-2 col-sm-3 col-3" onclick="fn_iconSearch('noti');" id="noti">
                                <img src="/assets/imgs/common/icon-letter-in-envelop.png" class="icon-letter-in-envelop">
                                <h5>고지</h5>
                            </div>
                        </div>
                    </div>
                </div>
                </s:authorize>
                <s:authorize access="hasRole('ROLE_ADMIN')">
                <div class="container" id="org">
                    <div id="faq-filter">
                        <div class="row justify-content-center text-center">
                            <div class="col col-md-2 col-sm-3 col-4 active"  onclick="fn_iconSearch('all');" id="all">
                                <img src="/assets/imgs/common/icon-chat-balloon.png"  class="icon-chat-balloon">
                                <h5>전체</h5>
                            </div>
                           	<div class="col col-md-2 col-sm-3 col-4" onclick="fn_iconSearch('site');" id="site">
                                <img src="/assets/imgs/common/icon-cog-in-monitor.png" class="icon-cog-in-monitor">
                                <h5>사이트 이용</h5>
                            </div>
                            <div class="col col-md-2 col-sm-3 col-4" onclick="fn_iconSearch('receive');" id="receive">
                                <img src="/assets/imgs/common/icon-won-in-monitor.png" class="icon-cog-in-monitor">
                                <h5>수납</h5>
                            </div>
                            <div class="w-100 d-sm-block d-md-none"></div>
                            <div class="col col-md-2 col-sm-3 col-4" onclick="fn_iconSearch('additional');" id="additional">
                                <img src="/assets/imgs/common/icon-siren-light.png">
                                <h5>부가서비스</h5>
                            </div>
                            <div class="col col-md-2 col-sm-3 col-4" onclick="fn_iconSearch('noti');" id="noti">
                                <img src="/assets/imgs/common/icon-letter-in-envelop.png" class="icon-letter-in-envelop">
                                <h5>고지</h5>
                            </div>
                        </div>
                    </div>
                </div>
                </s:authorize>
                <s:authorize access="hasRole('ROLE_USER')">
                <div class="container" id="submit">
                    <div id="faq-filter">
                        <div class="row justify-content-center text-center">
                            <div class="col col-md-2 col-sm-3 col-3 active"  onclick="fn_iconSearch('all');" id="all">
                                <img src="/assets/imgs/common/icon-chat-balloon.png"  class="icon-chat-balloon">
                                <h5>전체</h5>
                            </div>
                            <div class="col col-md-2 col-sm-3 col-3" onclick="fn_iconSearch('pay');" id="pay">
                                <img src="/assets/imgs/common/icon-coin-on-hand-2.png"  class="icon-coin-on-hand-2">
                                <h5>납부</h5>
                            </div>
                            <div class="w-100 d-sm-block d-md-none"></div>
                            <div class="col col-md-2 col-sm-3 col-3" onclick="fn_iconSearch('site');" id="site">
                                <img src="/assets/imgs/common/icon-cog-in-monitor.png" class="icon-cog-in-monitor">
                                <h5>사이트 이용</h5>
                            </div>
                            <div class="col col-md-2 col-sm-3 col-3" onclick="fn_iconSearch('cash');" id="cash">
                                <img src="/assets/imgs/common/icon-plus-in-shield.png">
                                <h5>현금영수증</h5>
                            </div>
                        </div>
                    </div>
                </div>
                </s:authorize>
                <s:authorize access="hasRole('ROLE_CHACMS')">
                    <div class="container" id="org">
                        <div id="faq-filter">
                            <div class="row justify-content-center text-center">
                                <div class="col col-md-2 col-sm-3 col-4 active"  onclick="fn_iconSearch('all');" id="all">
                                    <img src="/assets/imgs/common/icon-chat-balloon.png"  class="icon-chat-balloon">
                                    <h5>전체</h5>
                                </div>
                                <div class="col col-md-2 col-sm-3 col-4" onclick="fn_iconSearch('site');" id="site">
                                    <img src="/assets/imgs/common/icon-cog-in-monitor.png" class="icon-cog-in-monitor">
                                    <h5>사이트 이용</h5>
                                </div>
                                <div class="col col-md-2 col-sm-3 col-4" onclick="fn_iconSearch('receive');" id="receive">
                                    <img src="/assets/imgs/common/icon-won-in-monitor.png" class="icon-cog-in-monitor">
                                    <h5>수납</h5>
                                </div>
                                <div class="w-100 d-sm-block d-md-none"></div>
                                <div class="col col-md-2 col-sm-3 col-4" onclick="fn_iconSearch('additional');" id="additional">
                                    <img src="/assets/imgs/common/icon-siren-light.png">
                                    <h5>부가서비스</h5>
                                </div>
                                <div class="col col-md-2 col-sm-3 col-4" onclick="fn_iconSearch('noti');" id="noti">
                                    <img src="/assets/imgs/common/icon-letter-in-envelop.png" class="icon-letter-in-envelop">
                                    <h5>고지</h5>
                                </div>
                            </div>
                        </div>
                    </div>
                </s:authorize>
                <div class="container">
                    <div id="search-result">
                        <div id="table-option" class="row no-gutters mb-2">
                            <div class="col-md-6 col-sm-12 col-12 form-inline">
                                <span class="amount mr-2">[총 <em class="font-blue" id="totcount">${map.count}</em>건]</span>
                            </div>
                            <%-- <div class="col-md-6 col-sm-12 col-12 text-right mt-1">
                                <select class="form-control" name="search_orderBy" id="search_orderBy">
                                    <option value="regDt" <c:if test="${map.search_orderBy == 'regDt'}">selected</c:if>>등록일자순 정렬</option>
                                    <option value="state" <c:if test="${map.search_orderBy == 'state'}">selected</c:if>>상태별 정렬</option>
                                </select>
                                <select class="form-control" name="PAGE_SCALE" id="PAGE_SCALE">
                                    <option value="10"  <c:if test="${map.PAGE_SCALE == '10'}">selected</c:if>>10개씩 조회</option>
                                    <option value="20"  <c:if test="${map.PAGE_SCALE == '20'}">selected</c:if>>20개씩 조회</option>
                                    <option value="50"  <c:if test="${map.PAGE_SCALE == '50'}">selected</c:if>>50개씩 조회</option>
                                    <option value="100" <c:if test="${map.PAGE_SCALE == '100'}">selected</c:if>>100개씩 조회</option>
                                </select>
                            </div> --%>
                        </div>
                        <div class="row">
                            <div class="table-responsive pd-n-mg-o col mb-3">
                                <table id="faq-list" class="table table-sm table-hover table-primary">
                                    <colgroup>
                                        <col width="60">
                                        <col width="200">
                                        <col width="840">
                                    </colgroup>

                                    <thead>
                                        <tr>
                                            <th>NO</th>
                                            <th>분류</th>
                                            <th>제목</th>
                                        </tr>
                                    </thead>
                                    <tbody id="reSearchbody">
                                    </tbody>
                                </table>
                            </div>
                        </div>

						<jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false" />

                        <div id="search-box-list" class="container bg-faint-grey mt-4 mb-5 p-3">
                            <div class="row justify-content-center">
                                <div class="col-md-7 col-12 form-inline">
                                    <select class="form-control" id="search_option" name="search_option">
                                        <option value="all"     >전체</option>
                                        <option value="title"   >제목</option>
                                        <option value="contents">내용</option>
                                    </select>
                                    <input class="form-control mr-1" type="text" id="keyword" name="keyword" value="${map.keyword}" onkeypress="if(event.keyCode==13) {fn_faqListCall();}">
                                    <button class="btn btn-sm btn-primary" onclick="fn_faqListCall();">검색</button>
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

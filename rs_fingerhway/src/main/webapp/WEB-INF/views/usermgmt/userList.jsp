<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
  /**
  * @Class Name : userList.jsp
  * @Description : 사용자 현황 화면
  * @Modification Information
  *
  *   수정일		수정자		수정내용
  *  -------	--------	---------------------------
  *  2018.02.09	이동한		최초 생성
  *
  */
%>
<jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/>

<body>

<form:form commandName="paramVO" name="pageNav" id="pageNav" method="post" >
	<!-- 검색조건 유지 -->
	<input type="hidden" id="userId"	 name="userId"	    value="<c:out value='${paramVO.userId}'/>" />
	<input type="hidden" id="searchInd"  name="searchInd"   value="<c:out value='${paramVO.searchInd}'/>" />
	<input type="hidden" id="searchStr"  name="searchStr"   value="<c:out value='${paramVO.searchStr}'/>" />
	<input type="hidden" id="authGrpCd"  name="authGrpCd"   value="<c:out value='${paramVO.authGrpCd}'/>" />
	<input type="hidden" id="useYn"      name="useYn"       value="<c:out value='${paramVO.useYn}'/>" />
	<input type="hidden" id="pageIdx"    name="pageIdx"     value="<c:out value='${paramVO.pageIdx}'/>" />
	<input type="hidden" id="pageSize"    name="pageSize"   value="<c:out value='${paramVO.pageSize}'/>" />
	<input type="hidden" id="recvNum"    name="recvNum"     value="<c:out value='${paramVO.recvNum}'/>" />
	<input type="hidden" id="msgContent" name="msgContent"  value="<c:out value='${paramVO.msgContent}'/>" />
	<input type="hidden" id="sendNum"    name="sendNum"     value="<c:out value='${paramVO.sendNum}'/>" />
	<input type="hidden" id="mainMenuId" name="mainMenuId"  value="<c:out value='${paramVO.mainMenuId}'/>" />
	<input type="hidden" id="subMenuId"  name="subMenuId"   value="<c:out value='${paramVO.subMenuId}'/>" />
</form:form>
<input type="hidden" id="maxPage"  name="maxPage"   value="<c:out value='${resultMap.maxPage}'/>" />
<input type="hidden" id="totCnt"  name="totCnt"   value="<c:out value='${resultMap.totCnt}'/>" />
<div id="body">

	<jsp:include page="/WEB-INF/views/include/topMenu.jsp" flush="false"/>

    <!-- 컨테이너 영역 -->
    <div id="container">
    
	<jsp:include page="/WEB-INF/views/include/lnb.jsp" flush="false"/>
        
        <!-- 오른쪽 영역 -->
        <div id="contents">
        	<!-- 위치 -->
            <nav><ul class="navi_page">
            <li class="home"><a onclick="goMenu('<c:out value='${homeMenuInfoVO.menuUrl}'/>','<c:out value='${homeMenuInfoVO.menuId}'/>','<c:out value='${homeMenuInfoVO.subMenu[0].menuId}'/>');" style="cursor:pointer"><span>home</span></a></li><li>&gt;</li>
            <li><a onclick="goMenu('<c:out value='${menuInfoVO.menuUrl}'/>','<c:out value='${menuInfoVO.menuId}'/>','<c:out value='${menuInfoVO.subMenu[0].menuId}'/>');" style="cursor:pointer">사용자관리</a></li><li>&gt;</li>
            <li>사용자현황</li></ul></nav>
            
            <!-- 상단조회 -->
            <div class="wrap_top">
            	<h3>사용자 현황</h3>
                <h4>경기고속도로 가상계좌 자금관리 서비스 사용자를 등록 및 수정, 삭제할 수 있습니다.</h4> 
                            
                <div class="wrap_search">
                	<div class="wrap_data end wrap_col2">
                        <span class="tit">검색</span>
                        <ul class="search_box">
                        <li class="col1">
                            <div class="select_box">
							<label for="select_layer">
	                        	<c:if test="${paramVO.searchInd==''}">담당자명</c:if>
	                        	<c:if test="${paramVO.searchInd=='N'}">담당자명</c:if>
	                        	<c:if test="${paramVO.searchInd=='I'}">아이디</c:if>
	                        	<c:if test="${paramVO.searchInd=='B'}">소속</c:if>
                        	</label>
                            <select id="searchIndN" class="select_layer" title="">
                                <option value="N" <c:if test="${paramVO.searchInd=='N'||paramVO.searchInd==''}">selected="selected"</c:if>>담당자명</option>
                                <option value="I" <c:if test="${paramVO.searchInd=='I'}">selected="selected"</c:if>>아이디</option>
                                <option value="B" <c:if test="${paramVO.searchInd=='B'}">selected="selected"</c:if>>소속</option>
                            </select>
                            </div>
                        </li>
                        <li class="col2"><input id="searchStrN" type="text" class="input_text" value="<c:out value='${paramVO.searchStr}'/>" /></li>
                        </ul>                      
                   </div>
                   <div class="wrap_data">
                        <span class="tit">권한</span>
                        <div class="select_box">
                        <label for="select_layer">
                        	<c:if test="${paramVO.authGrpCd==''}">전체</c:if>
                        	<c:if test="${paramVO.authGrpCd=='M'}">마스터</c:if>
                        	<c:if test="${paramVO.authGrpCd=='W'}">작업자</c:if>
                        	<c:if test="${paramVO.authGrpCd=='S'}">조회용</c:if>
                        </label>
                        <select id="authGrpCdN" class="select_layer" title="">
                            <option value="" <c:if test="${paramVO.authGrpCd==''}">selected="selected"</c:if>>전체</option>
                            <option value="M" <c:if test="${paramVO.authGrpCd=='M'}">selected="selected"</c:if>>마스터</option>
                            <option value="W" <c:if test="${paramVO.authGrpCd=='W'}">selected="selected"</c:if>>작업자</option>
                            <option value="S" <c:if test="${paramVO.authGrpCd=='S'}">selected="selected"</c:if>>조회용</option>
                        </select>
                        </div>
                    </div>                      
                    <div class="wrap_data">
                    	<span class="tit">상태</span>                   
                        <div class="input_radio">                        
                            <label><input type="radio" name="useYnN" value="A" <c:if test="${paramVO.useYn=='A'||paramVO.useYn==''}">checked</c:if> />전체</label>
                            <label><input type="radio" name="useYnN" value="Y" <c:if test="${paramVO.useYn=='Y'}">checked</c:if> />정상</label>
                            <label><input type="radio" name="useYnN" value="N" <c:if test="${paramVO.useYn=='N'}">checked</c:if> />중지</label>
                        </div>
                    </div> 
                            
                <span class="float_clear"></span>
                </div>
                <div class="btn_wrap">
                    <span class="btn2 ico_search"><a id="searchBtn" style="cursor:pointer">조회</a></span>
                </div>  
            </div>
            <!-- //상단조회 -->
            
            <!-- 하단결과 -->
            <div class="wrap_cont">
            	
                
                <!-- 목록 -->
                <div class="wrap_table">
                <table id="gridResult" style="" class="table1" border="0" cellpadding="0" cellspacing="0" summary="">
                <caption>목록</caption>
                <colgroup>
                <col style="width:8%;" />
                <col style="width:10%;" />                                         
                <col style="width:10%;" />
                <col style="width:10%;" />
                <col style="width:10%;" />
                <col style="width:10%;" />
                <col style="width:;" />             
                </colgroup>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>아이디</th>
                    <th>소속</th>
                    <th>이름</th>
                    <th>권한</th>
                    <th>상태</th>
                    <th>메모</th>
                </tr>
                </thead>
                <tbody> 
                <c:if test="${userInfoList==null}">
                <tr>
                    <td colspan="7"><!-- 합칠 셀갯수 입력 -->
                    <span class="data_none"><img src="/resources/images/ico_alert2.png" />조회 조건을 입력(선택) 하신 후 조회 버튼을 클릭 하세요.</span><!-- 결과없음 메세지 -->
                    </td>
                </tr>
                </c:if>
                <c:if test="${userInfoList.size()==0}">
                <tr>
                    <td colspan="7"><!-- 합칠 셀갯수 입력 -->
                    <span class="data_none"><img src="/resources/images/ico_alert2.png" />조회된 결과가 없습니다.</span><!-- 결과없음 메세지 -->
                    </td>
                </tr>
                </c:if>
                <c:if test="${userInfoList.size()>0}">
                <c:set value="${resultMap.startIdx}" var="startidxno"/>
				<c:forEach var="lineData" items="${userInfoList}" varStatus="status">
				<tr>
        			<td><c:out value="${startidxno+status.count-1}"/></td>
        			<td><a onclick="javascript:userDetailInfo('<c:out value="${lineData.userId}"/>');" class='link_blue' style='cursor:pointer'><c:out value="${lineData.userId}"/></a></td>
        			<td><c:out value="${lineData.belong}"/></td>
        			<td><c:out value="${lineData.userNm}"/></td>
        			<td><c:out value="${lineData.authGrpCdNm}"/></td>
        			<td><c:out value="${lineData.useYnNm}"/></td>
        			<td><c:out value="${lineData.memo}"/></td>
            	</tr>
        		</c:forEach>
                </c:if>
                </tbody>
                </table>                
                </div>
                <!-- //목록 -->
                
                <!-- 페이징 -->
                <div id="pageDiv" class="paging" style="">
					<span class="float_clear">&nbsp;</span>
                </div>
                <!-- //페이징 -->
                
                <div class="btn_wrap">
                    <span class="btn2 rt ico_add"><a id="userRegBtn" style="cursor:pointer">신규등록</a></span>
                    <span class="float_clear"></span>
                </div>       
				
            </div>
            <!-- //하단결과 -->
            
        <span class="float_clear"></span>              
        </div>
        <!-- //오른쪽 영역 -->
        
    <span class="float_clear"></span>    
    </div>
    <!-- //컨테이너 영역 -->  
    <hr />
    <!-- 푸터 영역 -->
    <div id="footer">
        <jsp:include page="../include/footer.jsp" />
    </div>
    <!-- //푸터 영역 -->
    
</div>  
<!-- 탑버튼 --><span class="return_top" ><a href=""><img src="/resources/images/btn_top.png" alt="Top"></a></span>

</body>
<script>
//조건이 변경 되어도, goPage 함수에서는 이전 조건을 사용 하여야 한다.
var searchCondition = {}; //Ajax 사용시

//Ajax로 조회시 
function searchCallBack(data){
	alert(JSON.stringify(data));

	searchCondition = data.searchCondition;
	
	$("#gridResult tbody").html("");
	//$("#gridResult tbody").remove();
	//조회 결과가 없을 경우
	if(data.userInfoList.length==0){
		$("#gridResult tbody").html(
            "<tr>"+
            "<td colspan='7'>"+
            "<span class='data_none'><img src='/resources/images/ico_alert2.png' />조회된 결과가 없습니다.</span>"+
            "</td>"+
            "</tr>");
		
		$("#pageDiv").html("<span class='float_clear'>&nbsp;</span>");
		
		return;
	}
	
	//조회 결과가 있을 경우
	var lineData = data.userInfoList;
	
	var i=0;
	$.each(lineData, function(){
		

	//TR데이터를 그려준다.
	//for(var i=0;i<lineData.length;i++){
		$("#gridResult tbody").append(
			"<tr>"+
        	"<td>"+(i+1)+"</td>"+
        	"<td><a onclick=\"javascript:userDetailInfo('"+lineData[i].userId+"');\" class='link_blue' style='cursor:pointer'>"+lineData[i].userId+"</a></td>"+
        	"<td>"+lineData[i].belong+"</td>"+
        	"<td>"+lineData[i].userNm+"</td>"+
        	"<td>"+lineData[i].authGrpCdNm+"</td>"+
        	"<td>"+lineData[i].useYnNm+"</td>"+
        	"<td>"+lineData[i].memo+"</td>"+
        	"</tr>"
    	);
	//}
		i++;
	});
	
	//페이지 리스트 그리기
	if(data.maxPage<2){
		$("#pageDiv").html("<a class='on'>1</a>");
	} else {
		//현재 페이지 영역을 구한다.
		var pageCnt = 10;	//페이지 리스트당 페이지 수
		var maxPage = data.maxPage;	//최대 페이지 수
		var pageIdx = searchCondition.pageIdx;	//현재 페이지
		var startPageNum = (parseInt(pageIdx/pageCnt)*pageCnt)+1;	//페이지 리스트에 표현될 첫번째 페이지
		if((pageIdx%pageCnt)==0){
			startPageNum = startPageNum - pageCnt;
		}
		var endPageNum = startPageNum+pageCnt;
			
		var pageListHtml = "<a onclick='javascript:goPage("+1+")' style='cursor:pointer' class='first' title='처음'><span>&lt;</span></a>";
		if (pageIdx>1){
			pageListHtml = pageListHtml + "<a onclick='javascript:goPage("+(pageIdx-1)+")' style='cursor:pointer'  class='prev'>이전</a>";
		}
		for(var i=startPageNum;i<endPageNum;i++){
			
			if(i==pageIdx){
				pageListHtml = pageListHtml + "<a class='on'>"+i+"</a>";
			} else {
				pageListHtml = pageListHtml + "<a onclick='javascript:goPage("+i+")' style='cursor:pointer'>"+i+"</a>";
			}
			if(i==maxPage){
				break;
			}
		}
		if(maxPage>pageIdx){
			pageListHtml = pageListHtml + "<a onclick='javascript:goPage("+(pageIdx+1)+")' style='cursor:pointer' class='next'><span>다음</span></a>";
		}
		pageListHtml = pageListHtml + "<a onclick='javascript:goPage("+maxPage+")' style='cursor:pointer' class='end' title='끝' ><span>&gt;</span></a>";

		$("#pageDiv").html(pageListHtml);
	}
}

function userDetailInfo(searchId){
	
	//조회조건 유지를 위하여, form을 채운다.
	$("#userId").val(searchId);
	
	$("#pageNav").prop("action","/userDetail.do");
	$("#pageNav").submit();
	
	//var param = "userId="+searchId+"&searchInd="+searchCondition.searchInd+"&searchStr="+searchCondition.searchStr
	//			+"&authGrpCd="+searchCondition.authGrpCd+"&useYn="+searchCondition.useYn+"&pageSize="+searchCondition.pageSize
	//			+"&pageIdx="+searchCondition.pageIdx
	//location.href = "/userDetail.do?"+param;
}

//페이지 이동 - Ajax 사용시
function goPageAjax(pageIdx){

	var param = {searchInd : searchCondition.searchInd, searchStr : searchCondition.searchStr
			, authGrpCd : searchCondition.authGrpCd, useYn : searchCondition.useYn
			, pageSize : 10, pageIdx : pageIdx};
	
	var _url = "/getUserInfoList.do";
	
	ajaxCall(param, _url, 'searchCallBack' );
}

//페이지 이동
function goPage(pageIdx){

	$("#pageIdx").val(pageIdx);
	
	$("#pageNav").prop("action","/getUserInfoList.do");
	$("#pageNav").submit();
}

//수정 또는 등록 화면에서 오픈 되었을 경우, 이전 조회 조건을 유지하여 자동으로 조회값 세팅 및 조회 하여 준다. 신규등록 일 경우는 첫 페이지로 조회 - Ajax사용시
function autoSearch(){
	//자동으로 조회 값 세팅
	$("#searchIndN").val($("#searchInd").val());
	$("#searchStrN").val($("#searchStr").val());
	$("#authGrpCdN").val($("#authGrpCd").val());
	$('input:radio[name=useYnN]:input[value='+$("#useYn").val()+']').attr("checked", true);
	
	//파라미터 만들기
	var param = {searchInd : $("#searchIndN").val(), searchStr : $("#searchStrN").val()
			, authGrpCd : $("#authGrpCdN").val(), useYn : $(':radio[name="useYnN"]:checked').val()
			, pageSize : 10, pageIdx : $("#pageIdx").val()};
	var _url = "/getUserInfoList.do";
	
	ajaxCall(param, _url, 'searchCallBack' );
}

$(document).ready(function(){
	//초기 값에 pageIdx가 있으면 자동 조회 실행
	//if($("#pageIdx").val()!="0"){
	//	autoSearch();
	//}
	
	$("#searchStr").focus();
	
	//페이징 영역 html
	$("#pageDiv").html(makePageList($("#pageIdx").val(), $("#maxPage").val(), 10));
	
	$("#searchBtn").click(function (e) {
		//파라미터 만들기
		//var param = {searchInd : $("#searchIndN").val(), searchStr : $("#searchStrN").val()
		//		, authGrpCd : $("#authGrpCdN").val(), useYn : $(':radio[name="useYnN"]:checked').val()
		//		, pageSize : 10, pageIdx : 1};
		//var _url = "/getUserInfoList.do";
		
		//ajaxCall(param, _url, 'searchCallBack' );
		
		//Ajax 사용 안함
		
		$("#searchInd").val($("#searchIndN").val());
		$("#searchStr").val($("#searchStrN").val());
		$("#authGrpCd").val($("#authGrpCdN").val());
		$("#useYn").val($(':radio[name="useYnN"]:checked').val());
		$("#pageIdx").val("1");
		$("#pageSize").val("10");
		
		$("#pageNav").prop("action","/getUserInfoList.do");
		$("#pageNav").submit();
	});
	
	$("#userRegBtn").click(function (e) {
		//사용자 등록 화면으로 이동
		
		$("#pageNav").prop("action","/userReg.do");
		$("#pageNav").submit();
	
	});
});
</script>
<jsp:include page="/WEB-INF/views/include/layerPopup.jsp" flush="false"/>
</html>
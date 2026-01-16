<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@	page import="com.ghvirtualaccount.vo.UserInfoVO" %>
<%@	page import="com.ghvirtualaccount.vo.MenuInfoVO" %>
<%@	page import="com.ghvirtualaccount.vo.ParamVO" %>
<%@	page import="java.util.List" %>
<%@	page import="java.util.ArrayList" %>
<%
  /**
  * @Class Name : vAccountUse.jsp
  * @Description : 가상계좌이용현황
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
	<input type="hidden" id="pageIdx"    name="pageIdx"     value="<c:out value='${paramVO.pageIdx}'/>" />
	<input type="hidden" id="pageSize"   name="pageSize"    value="<c:out value='${paramVO.pageSize}'/>" />
	<input type="hidden" id="startMonth" name="startMonth"  value="<c:out value='${paramVO.startMonth}'/>" />
	<input type="hidden" id="endMonth"   name="endMonth"    value="<c:out value='${paramVO.endMonth}'/>" />
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
            <li><a onclick="goMenu('<c:out value='${menuInfoVO.menuUrl}'/>','<c:out value='${menuInfoVO.menuId}'/>','<c:out value='${menuInfoVO.subMenu[0].menuId}'/>');" style="cursor:pointer">분석/통계</a></li><li>&gt;</li>
            <li>가상계좌 이용현황</li></ul></nav>
            
            <!-- 상단조회 -->
            <div class="wrap_top">
            	<h3>가상계좌 이용현황</h3>
                <h4>월별 가상계좌 발급 및 입금 건수와 가상계좌 입금 수수료를 확인 할 수 있는 화면입니다.</h4>
                
                <div class="wrap_search">
                
                     <div class="wrap_data wrap_col1">
                    	<span class="tit">기간설정</span>                    
                        <!-- 달력목록 -->
                        <ul class="list_calendar">
                            <li class="input_wrap"><input id="startMonthN" type="text" class="input_text calendar2" value="<c:out value='${paramVO.startMonth}'/>" readOnly /></li>
                            </li>
                            <li class="txt">~</li>
                            <li class="input_wrap"><input id="endMonthN" type="text" class="input_text calendar2" value="<c:out value='${paramVO.endMonth}'/>" readOnly /></li>
                            </li>
                        </ul>
                        <!-- //달력목록 -->
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
            	
            	<div class="table_top">
                    <span class="txt_right">*부가세 별도</span>
            	<span class="float_clear"></span>
                </div>
                
                <!-- 목록 -->
                <div class="wrap_table">
                <table style="" class="table1" border="0" cellpadding="0" cellspacing="0" summary="">
                <caption>목록</caption>
                <colgroup>
                <col style="width:15%;" />
                <col style="width:21%;" />                                         
                <col style="width:21%;" />
                <col style="width:21%;" />
                <col style="width:21%;" />             
                </colgroup>
                <thead>
                <tr>
                    <th rowspan="2">No.</th>
                    <th rowspan="2">이용년월</th>
                    <th colspan="2" class="th1">가상계좌</th>
                    <th rowspan="2">가상계좌 입금수수료</th>
                </tr>
                <tr>
                    <th class="th1">발급</th>
                    <th class="th1">입금</th>
                </tr>
                </thead>
                <tbody> 
                <c:if test="${resultMap.totCnt==null}">
                <tr>
                    <td colspan="5"><!-- 합칠 셀갯수 입력 -->
                    <span class="data_none"><img src="/resources/images/ico_alert2.png" />조회 조건을 입력(선택) 하신 후 조회 버튼을 클릭 하세요.</span><!-- 결과없음 메세지 -->
                    </td>
                </tr>
                </c:if>
                <c:if test="${resultMap.totCnt==0}">
                <tr>
                    <td colspan="5"><!-- 합칠 셀갯수 입력 -->
                    <span class="data_none"><img src="/resources/images/ico_alert2.png" />조회된 결과가 없습니다.</span><!-- 결과없음 메세지 -->
                    </td>
                </tr>
                </c:if>
                <c:if test="${vAcntAnalysisList.size()>0}">
                <tr class="tr4">
                    <td></td>
                    <td>합계</td>
                    <td><c:out value="${vAcntAnalysisTot.totClaimCnt}"/></td>
                    <td><c:out value="${vAcntAnalysisTot.totReceiptCnt}"/></td>
                    <td><c:out value="${vAcntAnalysisTot.totFeeAmt}"/></td>
				</tr>
                <c:set value="${resultMap.startIdx}" var="startidxno"/>
				<c:forEach var="lineData" items="${vAcntAnalysisList}" varStatus="status">
				<tr>
        			<td><c:out value="${startidxno+status.count-1}"/></td>
        			<td><c:out value="${lineData.regMonth}"/><span class="txt_blue"><c:out value="${lineData.inUseYn}"/></span></td>
        			<td><c:out value="${lineData.totClaimCnt}"/></td>
        			<td><c:out value="${lineData.totReceiptCnt}"/></td>
        			<td><c:out value="${lineData.totFee}"/></td>
            	</tr>
        		</c:forEach>
                </c:if>
                </tbody>
                </table>                
                </div>
                <!-- //목록 -->
                
                <!-- 페이징 -->
                <div id="pageDiv" class="paging">
					<span class="float_clear">&nbsp;</span>
                </div>
                <!-- //페이징 -->
				
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
//페이지 이동
function goPage(pageIdx){

	$("#pageIdx").val(pageIdx);
	
	$("#pageNav").prop("action","/getVAccountUse.do");
	$("#pageNav").submit();
}

$(document).ready(function(){
	
	//조회일자 초기화
	if($("#startMonthN").val()==''){
		$("#startMonthN").val(getPreMonth(6));
	}
	if($("#endMonthN").val()==''){
		$("#endMonthN").val(getCurrentMonth());
	}	
	
	//페이징 영역 html
	$("#pageDiv").html(makePageList($("#pageIdx").val(), $("#maxPage").val(), 10));
	
	$("#searchBtn").click(function (e) {
		
		if($("#startMonthN").val()>$("#endMonthN").val()){
			lAlert("조회 종료월이 조회 시작월보다 빠를 수 없습니다.");
			return;
		}
		
		$("#startMonth").val($("#startMonthN").val());
		$("#endMonth").val($("#endMonthN").val());
		
		$("#pageIdx").val("1");
		$("#pageSize").val("10");
		
		$("#pageNav").prop("action","/getVAccountUse.do");
		$("#pageNav").submit();
	});
	
});
</script>
<jsp:include page="/WEB-INF/views/include/layerPopup.jsp" flush="false"/>
</html>
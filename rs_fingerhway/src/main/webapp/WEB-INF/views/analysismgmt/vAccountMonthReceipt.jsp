<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
  /**
  * @Class Name : vAccountMonthReceipt.jsp
  * @Description : 가상계좌수납통계 - 월별 수납현황
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
	<input type="hidden" id="prePageIdx" name="prePageIdx"  value="<c:out value='${paramVO.prePageIdx}'/>" />
	<input type="hidden" id="pageIdx"    name="pageIdx"     value="<c:out value='${paramVO.pageIdx}'/>" />
	<input type="hidden" id="pageSize"   name="pageSize"    value="<c:out value='${paramVO.pageSize}'/>" />
	<input type="hidden" id="startMonth" name="startMonth"  value="<c:out value='${paramVO.startMonth}'/>" />
	<input type="hidden" id="endMonth"   name="endMonth"    value="<c:out value='${paramVO.endMonth}'/>" />
	<input type="hidden" id="startDate"  name="startDate"   value="<c:out value='${paramVO.startDate}'/>" />
	<input type="hidden" id="endDate"    name="endDate"     value="<c:out value='${paramVO.endDate}'/>" />
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
            <li>가상계좌 수납통계</li></ul></nav>
            
            <!-- 상단조회 -->
            <div class="wrap_top">
            	<h3>가상계좌 수납통계</h3>
                <h4 style="display:none;">가상계좌 수납통계</h4>
                
                <div class="tab tm20">
                <ul>
                <li><a id="goDayTab" style="cursor:pointer">일별 수납현황</a></li>
                <li class="on"><!-- 활성화 --><a>월별 수납현황</a></li>
                </ul>                
                </div>
                
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
            	
            	<div class="table_top" style="display:none;">
                    
                	<div class="wrap_num">
                    	<span>1,388</span>개 중 <em>1 ~50</em><a href="#none"><img src="images/btn_down1.png" alt="▼" /></a><a href="#none" style="display:none;"><img src="images/btn_up1.png" alt="▲" /></a>
                        <!-- 선택레이어 -->
                        <div class="lay_num" style="display:none;">
                        <ul>
                        <li class="on"><a href="#none">1 ~50</a></li>
                        <li><a href="#none">51 ~100</a></li>
                        <li><a href="#none">101 ~150</a></li>
                        </ul>
                        </div>
                        <!-- //선택레이어 -->                		
                    </div>
            	<span class="float_clear"></span>
                </div>
                
                <!-- 목록 -->
                <div class="wrap_table">
                <table style="" class="table1" border="0" cellpadding="0" cellspacing="0" summary="">
                <caption>목록</caption>
                <colgroup>
                <col style="width:11%;" />
                <col style="width:11%;" />
                <col style="width:11%;" />                                         
                <col style="width:11%;" />
                <col style="width:11%;" />
                <col style="width:11%;" />
                <col style="width:11%;" />
                <col style="width:11%;" />
                <col style="width:11%;" />            
                </colgroup>
                <thead>
                <tr>
                    <th rowspan="2">청구월</th>
                    <th colspan="2" class="th1">수납률</th>
                    <th colspan="2" class="th1">청구</th>
                    <th colspan="2" class="th1">입금</th>
                    <th colspan="2" class="th1">미납</th>                    
                </tr>
                <tr>
                    <th class="th1">건수</th>
                    <th class="th1">금액</th>
                    <th class="th1">건수</th>
                    <th class="th1">금액</th>
                    <th class="th1">건수</th>
                    <th class="th1">금액</th>
                    <th class="th1">건수</th>
                    <th class="th1">금액</th>
                </tr>
                </thead>
                <tbody> 
                <c:if test="${resultMap.totCnt==null}">
                <tr>
                    <td colspan="9"><!-- 합칠 셀갯수 입력 -->
                    <span class="data_none"><img src="/resources/images/ico_alert2.png" />조회 조건을 입력(선택) 하신 후 조회 버튼을 클릭 하세요.</span><!-- 결과없음 메세지 -->
                    </td>
                </tr>
                </c:if>
                <c:if test="${resultMap.totCnt==0}">
                <tr>
                    <td colspan="9"><!-- 합칠 셀갯수 입력 -->
                    <span class="data_none"><img src="/resources/images/ico_alert2.png" />조회된 결과가 없습니다.</span><!-- 결과없음 메세지 -->
                    </td>
                </tr>
                </c:if>
                <c:if test="${vAcntAnalysisList.size()>0}">
                <tr class="tr4">
                    <td>총합</td>
                    <td>평균 <c:out value="${vAcntAnalysisTot.totCntAvg}"/></td>
                    <td>평균 <c:out value="${vAcntAnalysisTot.totAmtAvg}"/></td>
                    <td><c:out value="${vAcntAnalysisTot.totClaimCnt}"/></td>
                    <td><c:out value="${vAcntAnalysisTot.totClaimAmt}"/></td>
                    <td><c:out value="${vAcntAnalysisTot.totReceiptCnt}"/></td>
                    <td><c:out value="${vAcntAnalysisTot.totReceiptAmt}"/></td>
                    <td><c:out value="${vAcntAnalysisTot.totDefaultCnt}"/></td>
                    <td><c:out value="${vAcntAnalysisTot.totDefaultAmt}"/></td>
				</tr>
				<c:forEach var="lineData" items="${vAcntAnalysisList}" varStatus="status">
				<tr>
        			<td><c:out value="${lineData.regMonth}"/></td>
        			<td><c:out value="${lineData.totCntAvg}"/></td>
        			<td><c:out value="${lineData.totAmtAvg}"/></td>
        			<td><c:out value="${lineData.totClaimCnt}"/></td>
        			<td><c:out value="${lineData.totClaimAmt}"/></td>
        			<td><c:out value="${lineData.totReceiptCnt}"/></td>
        			<td><c:out value="${lineData.totReceiptAmt}"/></td>
        			<td><c:out value="${lineData.totDefaultCnt}"/></td>
        			<td><c:out value="${lineData.totDefaultAmt}"/></td>
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
                
                <div class="btn_wrap">
                    <span class="btn2 right ico_down"><a id="downloadExcel" style="cursor:pointer">엑셀 다운로드</a></span>
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

//페이지 이동
function goPage(pageIdx){

	$("#pageIdx").val(pageIdx);
	
	$("#pageNav").prop("action","/vAccountMonthReceipt.do");
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
	
	//임시페이지번호
	if($("#prePageIdx").val()==''){
		$("#prePageIdx").val('1');
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
		
		$("#pageNav").prop("action","/vAccountMonthReceipt.do");
		$("#pageNav").submit();
	});
	
	$("#goDayTab").click(function (e) {
		//임시 페이지 번호를 세팅 한다.
		$("#pageIdx").val($("#prePageIdx").val());
		$("#prePageIdx").val($("#pageIdx").val());
		
		$("#pageNav").prop("action","/vAccountDayReceipt.do");
		$("#pageNav").submit();
	});
	
	$("#downloadExcel").click(function (e) {
		
		if($("#totCnt").val()==""||$("#totCnt").val()=="0"){
			lAlert("다운로드 할 정보가 없습니다.\n먼저 조회를 수행 하십시오.")
			return;	
		}
		
		$("#pageNav").prop("action","/vAccountMonthReceiptExcel.do");
		$("#pageNav").submit();
	});
	
});
</script>
<jsp:include page="/WEB-INF/views/include/layerPopup.jsp" flush="false"/>
</html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@	page import="java.util.List" %>
<%@	page import="java.util.Map" %>
<%@	page import="com.ghvirtualaccount.cmmn.StrUtil" %>
<%
  /**
  * @Class Name : DefaultList.jsp
  * @Description : 미납목록
  * @Modification Information
  *
  *   수정일		수정자		수정내용
  *  -------	--------	---------------------------
  *  2018.02.07	이동한		최초 생성
  *
  */
%>
<jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/>
<%
	//조회 월도 만들기
	StrUtil strUtil = new StrUtil();
	List<Map<String, Object>> monthList = strUtil.getMonthList(5);
	
	request.setAttribute("monthList", monthList);
	
%>
<body>

<form:form commandName="paramVO" name="pageNav" id="pageNav" method="post" >
	<!-- 검색조건 유지 -->
	<input type="hidden" id="pageIdx"    name="pageIdx"     value="<c:out value='${paramVO.pageIdx}'/>" />
	<input type="hidden" id="pageSize"   name="pageSize"    value="<c:out value='${paramVO.pageSize}'/>" />
	<input type="hidden" id="startMonth" name="startMonth"  value="<c:out value='${paramVO.startMonth}'/>" />
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
            <li><a onclick="goMenu('<c:out value='${menuInfoVO.menuUrl}'/>','<c:out value='${menuInfoVO.menuId}'/>','<c:out value='${menuInfoVO.subMenu[0].menuId}'/>');" style="cursor:pointer">수납관리</a></li><li>&gt;</li>
            <li>미납 목록</li></ul></nav>
            
            <!-- 상단조회 -->
            <div class="wrap_top">
            	<h3>미납 목록</h3>
                <h4>가상계좌 입금 가능 기한이 지난 미납 목록입니다. </h4>
                <div class="wrap_search">                
                    
                    <div class="wrap_data">
                        <span class="tit">청구월</span>
                        <div class="select_box">
                        <label for="select_layer">
                        	<c:if test="${paramVO.startMonth==''}">전체</c:if>
                        	<c:forEach var="lineData" items="${monthList}" varStatus="status">
                        	<c:if test="${lineData.keyStr == paramVO.startMonth}"><c:out value='${lineData.dpStr}'/></c:if>
                        	</c:forEach>
                        </label>
                        <select id="startMonthN" class="select_layer" title="">
                            <option value="" <c:if test="${paramVO.startMonth==''}">selected="selected"</c:if>>전체</option>
                            <c:forEach var="lineData" items="${monthList}" varStatus="status">
                            <option value="<c:out value='${lineData.keyStr}'/>" <c:if test="${lineData.keyStr == paramVO.startMonth}">selected="selected"</c:if>><c:out value='${lineData.dpStr}'/></option>
                            </c:forEach>
                        </select>
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
            	
            	<div class="table_top">
                	<div class="wrap_num">
						<c:if test="${resultMap.totCnt==null}">
                    	<span>총  0건</span>
                    	</c:if>
                    	<c:if test="${resultMap.totCnt!=null}">
                    	<span>총  <c:out value='${receiptTot.dispTotCnt}'/>건</span>
                    	</c:if>           		
                    </div>
            	<span class="float_clear"></span>
                </div>
                
                <!-- 목록_가로값,셀갯수등 수정 180220 -->
                <div class="wrap_table">
                <table style="" class="table1" border="0" cellpadding="0" cellspacing="0" summary="">
                <caption>목록</caption>
                <colgroup>
                <col style="width:4%;" />
                <col style="width:8%;" />                                         
                <col style="width:7%;" />
                <col style="width:7%;" />
                <col style="width:25%;" />
                <col style="width:7%;" /> 
                <col style="width:20%;" /> 
                <col style="width:10%;" />
                <col style="width:6%;" />
                <col style="width:5%;" />          
                </colgroup>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>차량번호</th>
                    <th>차량주</th>
                    <th>납부기한</th>
                    <th>주소</th>
                    <th>지로번호</th>
                    <th>고객조회번호 </th>
                    <th>총금액</th>
                    <th>고지금&amp;코드</th>
                    <th>지로코드</th>
                </tr>
                </thead>
                <tbody> 
				<c:if test="${resultMap.totCnt==null}">
                <tr>
                    <td colspan="10"><!-- 합칠 셀갯수 입력 -->
                    <span class="data_none"><img src="/resources/images/ico_alert2.png" />조회 조건을 입력(선택) 하신 후 조회 버튼을 클릭 하세요.</span><!-- 결과없음 메세지 -->
                    </td>
                </tr>
                </c:if>
                <c:if test="${resultMap.totCnt==0}">
                <tr>
                    <td colspan="10"><!-- 합칠 셀갯수 입력 -->
                    <span class="data_none"><img src="/resources/images/ico_alert2.png" />조회된 결과가 없습니다.</span><!-- 결과없음 메세지 -->
                    </td>
                </tr>
                </c:if>
                <c:if test="${resultMap.totCnt>0}">
				<tr class="tr4"><!-- 셀병합추가 180220 -->
                    <td></td>                    
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>미납금액 합계</td>
                    <td><c:out value="${receiptTot.totDefaultAmt}"/></td>
                    <td></td>
                    <td></td>
				</tr>
                <c:set value="${resultMap.startIdx}" var="startidxno"/>
				<c:forEach var="lineData" items="${payerInfoList}" varStatus="status">
				<tr>
        			<td><c:out value="${startidxno+status.count-1}"/></td>
                    <td><c:out value="${lineData.carNum}"/></td>
                    <td><c:out value="${lineData.carOwnerNm}"/></td>
                    <td><c:out value="${lineData.payDueDt}"/></td>
                    <td><c:out value="${lineData.carOwnerAddr}"/></td>
                    <td><c:out value="${lineData.giroNum}"/></td>
                    <td><c:out value="${lineData.custInqNum}"/></td>
                    <td><c:out value="${lineData.totAmt}"/></td>
                    <td><c:out value="${lineData.notiAmtCd}"/></td>
                    <td><c:out value="${lineData.giroCd}"/></td>
            	</tr>
        		</c:forEach>
                </c:if>
                </tbody>
                </table>                
                </div>
                <!-- //목록 -->
                
                <span class="mesg_red">※ 미납목록 엑셀다운로드 파일에 상세 청구항목이 포함되어 저장됩니다.</span>
                
                <!-- 페이징 -->
                <div id="pageDiv" class="paging">
					<span class="float_clear">&nbsp;</span>
                </div>
                <!-- //페이징 -->
                
                <div class="btn_wrap">
                    <span class="btn2 right ico_down"><a id="downloadExcel" style="cursor:pointer">미납목록 다운로드</a></span>
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
<!-- 탑버튼 --><span class="return_top" ><a href=""><img src="images/btn_top.png" alt="Top"></a></span>

</body>
<script>

//페이지 이동
function goPage(pageIdx){

	$("#pageIdx").val(pageIdx);
	
	$("#pageNav").prop("action","/getDefaultList.do");
	$("#pageNav").submit();
}

$(document).ready(function(){
	
	//페이징 영역 html
	$("#pageDiv").html(makePageList($("#pageIdx").val(), $("#maxPage").val(), 10));
	
	$("#searchBtn").click(function (e) {
		
		$("#startMonth").val($("#startMonthN").val());
		
		$("#pageIdx").val("1");
		$("#pageSize").val("10");
		
		$("#pageNav").prop("action","/getDefaultList.do");
		$("#pageNav").submit();
	});
	
	$("#downloadExcel").click(function (e) {
		
		if($("#totCnt").val()==""||$("#totCnt").val()=="0"){
			lAlert("다운로드 할 정보가 없습니다.\n먼저 조회를 수행 하십시오.")
			return;	
		}
		
		$("#pageNav").prop("action","/getDefaultExcel.do");
		$("#pageNav").submit();
	});
	
});
</script>
<jsp:include page="/WEB-INF/views/include/layerPopup.jsp" flush="false"/>
</html>
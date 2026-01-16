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
  * @Class Name : receiptPresent.jsp
  * @Description : 수납현황
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
	<input type="hidden" id="searchInd"  name="searchInd"   value="<c:out value='${paramVO.searchInd}'/>" />
	<input type="hidden" id="searchStr"  name="searchStr"   value="<c:out value='${paramVO.searchStr}'/>" />
	<input type="hidden" id="pageIdx"    name="pageIdx"     value="<c:out value='${paramVO.pageIdx}'/>" />
	<input type="hidden" id="pageSize"   name="pageSize"    value="<c:out value='${paramVO.pageSize}'/>" />
	<input type="hidden" id="startMonth" name="startMonth"  value="<c:out value='${paramVO.startMonth}'/>" />
	<input type="hidden" id="successYn"  name="successYn"   value="<c:out value='${paramVO.successYn}'/>" />
	<input type="hidden" id="order"      name="order"       value="<c:out value='${paramVO.order}'/>" />
	<input type="hidden" id="mainMenuId" name="mainMenuId"  value="<c:out value='${paramVO.mainMenuId}'/>" />
	<input type="hidden" id="subMenuId"  name="subMenuId"   value="<c:out value='${paramVO.subMenuId}'/>" />
</form:form>
<input type="hidden" id="maxPage"  name="maxPage"   value="<c:out value='${resultMap.maxPage}'/>" />
<input type="hidden" id="totCnt"  name="totCnt"   value="<c:out value='${resultMap.totCnt}'/>" />
<form:form name="frmClaimPopup" id="frmClaimPopup" method="post" >
	<input type="hidden" id="trno"  name="trno"   value="" />
</form:form>
<form:form name="frmMsgPopup" id="frmMsgPopup" method="post" >
	<input type="hidden" id="carOwnerNm"  name="carOwnerNm"   value="" />
	<input type="hidden" id="occurReason1"  name="occurReason1"   value="" />
	<input type="hidden" id="totAmt"  name="totAmt"   value="" />
	<input type="hidden" id="virtualAcntNum"  name="virtualAcntNum"   value="" />
	<input type="hidden" id="payDueDt"  name="payDueDt"   value="" />
</form:form>
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
            <li>수납 현황</li></ul></nav>
            
            <!-- 상단조회 -->
            <div class="wrap_top">
            	<h3>수납 현황</h3>
                <h4>가상계좌 입금/미납 내역을 조건에 따라 검색합니다.</h4>
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
                    
                    <div class="wrap_data">
                    	<span class="tit">수납상태</span>                   
                        <div class="input_radio">                      
                        	<label><input type="radio" name="successYnN" value="A" <c:if test="${paramVO.successYn=='A'}">checked</c:if> />전체</label>
                        	<label><input type="radio" name="successYnN" value="Y" <c:if test="${paramVO.successYn=='Y'|| paramVO.successYn==''}">checked</c:if> />완납</label>  
                        	<label><input type="radio" name="successYnN" value="N" <c:if test="${paramVO.successYn=='N'}">checked</c:if> />미납</label>
                        </div>
                    </div>
                    
                    <div class="wrap_data">
                    	<span class="tit">정렬순서</span>
                        <div class="select_box">
                    	<label for="select_layer">
                    		<c:if test="${paramVO.order==''||paramVO.order=='PAY_DUE_DT'}">납부기한</c:if>
                        	<c:if test="${paramVO.order=='PAYDAY'}">입금일시</c:if>
                        	<c:if test="${paramVO.order=='CUST_INQ_NUM'}">고객조회번호</c:if>
                        	<c:if test="${paramVO.order=='CAR_OWNER_NM'}">차량주</c:if>
						</label>
                        <select id="orderN" class="select_layer" title="">
                            <option value="PAY_DUE_DT" <c:if test="${paramVO.order==''||paramVO.order=='PAY_DUE_DT'}">selected="selected"</c:if>>납부기한</option>
                            <option value="PAYDAY" <c:if test="${paramVO.order=='PAYDAY'}">selected="selected"</c:if>>입금일시</option>
                            <option value="CUST_INQ_NUM" <c:if test="${paramVO.order=='CUST_INQ_NUM'}">selected="selected"</c:if>>고객조회번호</option>
                            <option value="CAR_OWNER_NM" <c:if test="${paramVO.order=='CAR_OWNER_NM'}">selected="selected"</c:if>>차량주</option>
                        </select>
                    	</div>
                    </div>
                    
					<div class="wrap_data end wrap_col2">
					    <span class="tit">검색</span>
					    <ul class="search_box">
					    <li class="col1">
					        <div class="select_box">
					  			<label for="select_layer">
					  			<c:if test="${paramVO.searchInd==''||paramVO.searchInd=='CAR'}">차량번호</c:if>
								<c:if test="${paramVO.searchInd=='NAME'}">차량주</c:if>
								<c:if test="${paramVO.searchInd=='CUST'}">고객조회번호</c:if>
								<c:if test="${paramVO.searchInd=='ACNT'}">가상계좌</c:if>
								</label>
								<select id="searchIndN" class="select_layer" title="">
									<option value="CAR" <c:if test="${paramVO.searchInd==''||paramVO.searchInd=='CAR'}">selected="selected"</c:if>>차량번호</option>
									<option value="NAME" <c:if test="${paramVO.searchInd=='NAME'}">selected="selected"</c:if>>차량주</option>
									<option value="CUST" <c:if test="${paramVO.searchInd=='CUST'}">selected="selected"</c:if>>고객조회번호</option>
									<option value="ACNT" <c:if test="${paramVO.searchInd=='ACNT'}">selected="selected"</c:if>>가상계좌</option>
								</select>
					      </div>
					  </li>
					  <li class="col2"><input id="searchStrN" type="text" class="input_text" value="<c:out value='${paramVO.searchStr}'/>" /></li>
					     </ul>                      
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
            		<c:if test="${receiptTot!=null}">
                    <div class="result_info">수납 <span><c:out value='${receiptTot.totReceiptCnt}'/>건</span>, 총 금액 <em><c:out value='${receiptTot.totReceiptAmt}'/>원</em>이 입금되었습니다.</div>
                    <div class="result_info">미납 <span class="green"><c:out value='${receiptTot.totDefaultCnt}'/>건</span>, 총 미납금액 <em><c:out value='${receiptTot.totDefaultAmt}'/>원</em> 입니다.</div>
                    </c:if>
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
                
                <!-- 목록 -->
                <div class="wrap_table">
                <table style="" class="table1" border="0" cellpadding="0" cellspacing="0" summary="">
                <caption>목록</caption>
                <colgroup>
                <col style="width:5%;" />
                <col style="width:8%;" />                                         
                <col style="width:8%;" />
                <col style="width:8%;" />
                <col style="width:12%;" />
                <col style="width:8%;" />
                <col style="width:8%;" />
                <col style="width:8%;" />
                <col style="width:8%;" />
                <col style="width:8%;" />
                <col style="width:8%;" />
                <col style="width:10%;" />              
                </colgroup>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>차량번호</th>
                    <th>차량주</th>
                    <th>납부기한</th>
                    <th>고객조회번호</th>
                    <th>총건수</th>
                    <th>총금액</th>
                    <th>가상계좌</th>
                    <th>입금일시</th>
                    <th>입금은행</th>
                    <th>수납현황</th>
                    <th>문자보내기</th>
                </tr>
                </thead>
                <tbody>     
				<c:if test="${resultMap.totCnt==null}">
                <tr>
                    <td colspan="12"><!-- 합칠 셀갯수 입력 -->
                    <span class="data_none"><img src="/resources/images/ico_alert2.png" />조회 조건을 입력(선택) 하신 후 조회 버튼을 클릭 하세요.</span><!-- 결과없음 메세지 -->
                    </td>
                </tr>
                </c:if>
                <c:if test="${resultMap.totCnt==0}">
                <tr>
                    <td colspan="12"><!-- 합칠 셀갯수 입력 -->
                    <span class="data_none"><img src="/resources/images/ico_alert2.png" />조회된 결과가 없습니다.</span><!-- 결과없음 메세지 -->
                    </td>
                </tr>
                </c:if>
                <c:if test="${resultMap.totCnt>0}">
				<c:set value="${resultMap.startIdx}" var="startidxno"/>
				<c:forEach var="lineData" items="${payerInfoList}" varStatus="status">
				<tr>
        			<td><c:out value="${startidxno+status.count-1}"/></td>
                    <td><c:out value="${lineData.carNum}"/></td>
                    <td><c:out value="${lineData.carOwnerNm}"/></td>
                    <td><c:out value="${lineData.payDueDt}"/></td>
                    <td><c:out value="${lineData.custInqNum}"/></td>
                    <td>
                    <a onclick="javascript:claimDetailPop('<c:out value="${lineData.trno}"/>');" class='link_blue' style='cursor:pointer'><c:out value="${lineData.totCnt}"/></a>
                    <td><c:out value="${lineData.totAmt}"/></td>
                    <td><c:out value="${lineData.virtualAcntNum}"/></td>
                    <td><c:out value="${lineData.payday}"/></td>
                    <td><c:out value="${lineData.bankCdNm}"/></td> 
                    <td><c:out value="${lineData.payYnNm}"/></td> 
                    <td>
                    <c:if test="${lineData.msgSendYn=='Y'}">
                    <span class="btn5"><a onclick="javascript:receiptMsgPop('<c:out value="${lineData.carOwnerNm}"/>','<c:out value="${lineData.occurReason1}"/>','<c:out value="${lineData.totAmt}"/>','<c:out value="${lineData.virtualAcntNum}"/>','<c:out value="${lineData.payDueDt}"/>');" style='cursor:pointer'>문자</a></span>
                    </c:if>
                    </td>
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
                    <span class="btn2 right ico_down"><a id="downloadExcel" style="cursor:pointer">수납현황 다운로드</a></span>
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
	
	$("#pageNav").prop("action","/getReceiptStateList.do");
	$("#pageNav").submit();
}

function claimDetailPop(trno){
	
    var _target = "청구항목상세";
    var _url = "/claimDetailPop.do";
    
    $("#trno").val(trno);
    
    var win = window.open("", _target, "width=600px,height=480px, toolbar=no, menubar=no, resizable=1, scrollbars=no");
    
    $("#frmClaimPopup").prop("action",_url);
    $("#frmClaimPopup").attr('target',_target);
    $("#frmClaimPopup").submit();

}

function receiptMsgPop(carOwnerNm,occurReason1,totAmt,virtualAcntNum,payDueDt){
	
    var _target = "문자보내기";
    var _url = "/receiptMsgPop.do";
    
    $("#carOwnerNm").val(carOwnerNm);
    $("#occurReason1").val(occurReason1);
    $("#totAmt").val(totAmt);
    $("#virtualAcntNum").val(virtualAcntNum);
    $("#payDueDt").val(payDueDt);
    
    var win = window.open("", _target, "width=600px,height=500px, toolbar=no, menubar=no, resizable=1, scrollbars=no");
    
    $("#frmMsgPopup").prop("action",_url);
    $("#frmMsgPopup").attr('target',_target);
    $("#frmMsgPopup").submit();
}

$(document).ready(function(){
	
	//페이징 영역 html
	$("#pageDiv").html(makePageList($("#pageIdx").val(), $("#maxPage").val(), 10));
	
	$("#searchBtn").click(function (e) {
		
		$("#startMonth").val($("#startMonthN").val());
		$("#successYn").val($(':radio[name="successYnN"]:checked').val());
		$("#order").val($("#orderN").val());
		$("#searchInd").val($("#searchIndN").val());
		$("#searchStr").val($("#searchStrN").val());
		
		$("#pageIdx").val("1");
		$("#pageSize").val("10");
		
		$("#pageNav").prop("action","/getReceiptStateList.do");
		$("#pageNav").submit();
	});
	
	$("#downloadExcel").click(function (e) {
		
		if($("#totCnt").val()==""||$("#totCnt").val()=="0"){
			lAlert("다운로드 할 정보가 없습니다.\n먼저 조회를 수행 하십시오.")
			return;	
		}
		
		$("#pageNav").prop("action","/getReceiptStateExcel.do");
		$("#pageNav").submit();
	});
	
});
</script>
<jsp:include page="/WEB-INF/views/include/layerPopup.jsp" flush="false"/>
</html>
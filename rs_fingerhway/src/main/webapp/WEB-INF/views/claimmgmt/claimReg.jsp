<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
  /**
  * @Class Name : claimRge.jsp
  * @Description : 청구등록
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
<input type="hidden" id="regUserId"  name="regUserId"   value="<c:out value='${userInfo.userId}'/>" />
<form:form name="excelDownload" id="excelDownload" method="post" >
	<input type="hidden" id="payerInfoId"    name="payerInfoId"     value="" />
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
            <li><a onclick="goMenu('<c:out value='${menuInfoVO.menuUrl}'/>','<c:out value='${menuInfoVO.menuId}'/>','<c:out value='${menuInfoVO.subMenu[0].menuId}'/>');" style="cursor:pointer">청구관리</a></li><li>&gt;</li>
            <li>청구등록</li></ul></nav>
            
            <!-- 상단조회 -->
            <div class="wrap_top">
            	<h3>청구 등록</h3>
                <h4>청구 데이터를 엑셀파일로 업로드 하여 가상계좌번호 발급 및 고지정보를 생성합니다.</h4>
                <div class="wrap_search">
                
                	<span class="btn1 right"><a id="fileUploadBtn" style="cursor:pointer">업로드</a></span>
                    
                    <div class="file_input_div">
                    	<span class="btn1 file_input_img_btn"><em class="search"></em>엑셀파일 찾기</span>
                    	<form:form id="frmUpload" method="post" >
                        <input type="file" id="file_1" name="file_1" class="file_input_hidden" onChange="javascript: document.getElementById('fileName').value = this.value"/>
                        </form:form>
                    </div>
					<input type="text" id="fileName" class="file_input_textbox" readonly >
					<img src="/resources/images/btn_delete1.png" class="file_delete" id="fileDeleteBtn" alt="삭제" />
					                    
                <span class="float_clear"></span>    
                </div>
            </div>
            <!-- //상단조회 -->
            
            <!-- 하단결과 -->
            <div class="wrap_cont">
            	
            	<div class="table_top">
                	<h3>청구 목록</h3>
                	<div class="wrap_num">
                    	<span>총 <c:out value='${resultMap.totCnt}'/>건</span>
                    </div>
            	<span class="float_clear"></span>
                </div>
                
                <!-- 목록 -->
                <div class="wrap_table">
                <table class="table1" style="" border="0" cellpadding="0" cellspacing="0" summary="">
                <caption>목록</caption>
                <colgroup>
                <col style="width:10%;" />
                <col style="width:18%;" />                                         
                <col style="width:18%;" />
                <col style="width:18%;" />
                <col style="" />
                <col style="width:20%;" />              
                </colgroup>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>등록일시</th>
                    <th>등록자</th>
                    <th>청구건수</th>
                    <th>금액</th>
                    <th>고지서 정보 엑셀다운로드</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${resultMap.totCnt==null||resultMap.totCnt==0}">
                    <td colspan="6"><!-- 합칠 셀갯수 입력 -->
                    <span class="data_none"><img src="/resources/images/ico_alert2.png" />조회된 결과가 없습니다.</span><!-- 결과없음 메세지 -->
                    </td>
                </tr>
                </c:if>
                <c:if test="${payerInfoMasterInfoList.size()>0}">
                	<c:set value="${resultMap.startIdx}" var="startidxno"/>
				<c:forEach var="lineData" items="${payerInfoMasterInfoList}" varStatus="status">
				<tr>
        			<td><c:out value="${startidxno+status.count-1}"/></td>
        			<td><c:out value="${lineData.regDttm}"/></span></td>
        			<td><c:out value="${lineData.regUserNm}"/></td>
        			<td><c:out value="${lineData.claimCnt}"/></td>
        			<td><c:out value="${lineData.claimAmtSum}"/></td>
        			<td><span class="btn3 w80"><a href="javascript:downloadExcel('<c:out value="${lineData.payerInfoId}"/>');" style="cursor:pointer">download</a></span></td>
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
                	<span class="sbtn2 rt ico_refresh"><a id="reloadBtn" style="cursor:pointer">새로고침</a></span>
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
	
	$("#pageNav").prop("action","/getPayerInfoMasterList.do");
	$("#pageNav").submit();
}
function fileDelete(){
	$("#file_1").val('');
}
function regClaimCallBack(data){
	//alert(JSON.stringify(data));
	
	lAlert(data.retMsg);
	
	//페이지 재호출
	$("#pageNav").prop("action","/claimMgmt.do");
	$("#pageNav").submit();
}

function downloadExcel(payerInfoId){
	
	var _url = "/downloadPayerInfoList.do";
	
	$("#payerInfoId").val(payerInfoId);
	
	//엑셀 파일 다운로드
	$("#excelDownload").prop("action",_url);
	$("#excelDownload").submit();
}
$(document).ready(function(){
	
	//페이징 영역 html
	$("#pageDiv").html(makePageList($("#pageIdx").val(), $("#maxPage").val(), 10));
	
	$("#fileDeleteBtn").click(function (e){
		$("#file_1").val('');	
		$("#fileName").val('');
	});
	$("#reloadBtn").click(function (e){
		//페이지 재호출
		$("#pageNav").prop("action","/claimMgmt.do");
		$("#pageNav").submit();
	});
	
	$("#fileUploadBtn").click(function (e){
		if($("#file_1").val()==''){
			lAlert("파일을 선택 하셔야 합니다.");
			return;
		}
		//파일 확장자 추출
		var fileExt = $("#file_1").val().substring($("#file_1").val().lastIndexOf('.'),$("#file_1").val().length).toLowerCase();    
		
		if(fileExt!=".xlsx"){
			lAlert("확장자가 xlsx인 파일만 업로드 가능 합니다.");
			return;
		}
		
		//파일 업로드 실행
		//<form id="frmUpload" method="post" enctype="multipart/form-data">
		
		var formData = new FormData();
		formData.append("excelFile",$("#file_1")[0].files[0]);
		formData.append("regUserId",$("#regUserId").val());
		//var formData = new FormData($('#frmUpload')[0]);
		var _url = "/claimReg.do"
		var callBack = "regClaimCallBack";
		
		ajaxFileSend(formData,_url,callBack);
		
	});

});
</script>
<jsp:include page="/WEB-INF/views/include/layerPopup.jsp" flush="false"/>
</html>
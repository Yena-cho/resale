<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
  /**
  * @Class Name : userDetail.jsp
  * @Description : 사용자상세 화면
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

<div id="body">

	<jsp:include page="/WEB-INF/views/include/topMenu.jsp" flush="false"/>

    <!-- 컨테이너 영역 -->
    <div id="container">
    
	<jsp:include page="/WEB-INF/views/include/lnb.jsp" flush="false"/>
        
        <!-- 오른쪽 영역 -->
        <!-- 오른쪽 영역 -->
        <div id="contents">
        	<!-- 위치 -->
            <nav><ul class="navi_page">
            <li class="home"><a onclick="goMenu('<c:out value='${homeMenuInfoVO.menuUrl}'/>','<c:out value='${homeMenuInfoVO.menuId}'/>','<c:out value='${homeMenuInfoVO.subMenu[0].menuId}'/>');" style="cursor:pointer"><span>home</span></a></li><li>&gt;</li>
            <li><a onclick="goMenu('<c:out value='${menuInfoVO.menuUrl}'/>','<c:out value='${menuInfoVO.menuId}'/>','<c:out value='${menuInfoVO.subMenu[0].menuId}'/>');" style="cursor:pointer">사용자관리</a></li><li>&gt;</li>
            <li><a onclick="goMenu('<c:out value='${subMenuInfoVO.menuUrl}'/>','<c:out value='${menuInfoVO.menuId}'/>','<c:out value='${subMenuInfoVO.menuId}'/>');" style="cursor:pointer">사용자현황</a></li><li>&gt;</li>
            <li>사용자 상세</li></ul></nav>

            <!-- 상단조회 -->
            <div class="wrap_top">
            	<h3>사용자 상세</h3>
                <h4 style="display:none;">사용자 상세.</h4>  
            </div>
            <!-- //상단조회 -->
            
            <!-- 하단결과 -->
            <div class="wrap_cont" style="padding-top:30px;">
            	
                <!-- 목록 -->
                <div class="wrap_table">
                <table class="table3 bm30" style="" border="0" cellpadding="0" cellspacing="0" summary="">
                <caption>목록</caption>
                <colgroup>
                <col style="width:15%;" />
                <col style="width:35%;" />                                         
                <col style="width:15%;" />
                <col style="width:35%;" />            
                </colgroup>               
                <tbody>        
                <tr>
                    <th><em class="required">*</em>사용자권한</th>
                    <td class="left"><c:out value='${userInfoVO.authGrpCdNm}'/></td>
                    <th><em class="required">*</em>사용자상태</th>
                    <td class="left"><c:out value='${userInfoVO.useYnNm}'/></td>
				</tr>                
                </tbody>
                </table>
                
                <table class="table3 info_user" style="" border="0" cellpadding="0" cellspacing="0" summary="">
                <caption>목록</caption>
                <colgroup>
                <col style="width:15%;" />
                <col style="width:35%;" />                                         
                <col style="width:15%;" />
                <col style="width:35%;" />            
                </colgroup>               
                <tbody>   
                <tr>
                    <th>등록일</th>
                    <td class="left" colspan="3"><c:out value='${userInfoVO.regDt}'/></td>
				</tr>     
                <tr>
                    <th>아이디</th>
                    <td class="left"><c:out value='${userInfoVO.userId}'/></td>
                    <th><em class="required">*</em>비밀번호</th>
                    <td class="left"><c:out value='${userInfoVO.userPswd}'/></td>
				</tr>
                <tr>
                    <th><em class="required">*</em>이름</th>
                    <td class="left"><c:out value='${userInfoVO.userNm}'/></td>
                    <th>직급</th>
                    <td class="left"><c:out value='${userInfoVO.position}'/></td>
				</tr>
                <tr>
                    <th><em class="required">*</em>소속</th>
                    <td class="left"><c:out value='${userInfoVO.belong}'/></td>
                    <th><em class="required">*</em>부서</th>
                    <td class="left"><c:out value='${userInfoVO.dept}'/></td>
				</tr>
                <tr>
                    <th>전화번호</th>
                    <td class="left"><c:if test="${userInfoVO.phoneNum1!=null}"><c:out value='${userInfoVO.phoneNum1}'/>-<c:out value='${userInfoVO.phoneNum2}'/>-<c:out value='${userInfoVO.phoneNum3}'/></c:if></td>
                    <th><em class="required">*</em>휴대폰</th>
                    <td class="left"><c:if test="${userInfoVO.mobileNum1!=null}"><c:out value='${userInfoVO.mobileNum1}'/>-<c:out value='${userInfoVO.mobileNum2}'/>-<c:out value='${userInfoVO.mobileNum3}'/></c:if></td>
				</tr>
                <tr>
                    <th>이메일</th>
                    <td class="left" colspan="3"><c:out value='${userInfoVO.emailAddr}'/></td>
				</tr>
                <tr>
                    <th>메모</th>
                    <td class="left" colspan="3"><textarea id="memoN" rows="5" class="input_textarea readonly" tabindex="1" readOnly><c:out value='${userInfoVO.memo}'/></textarea></td>
				</tr>      
                </tbody>
                </table>                
                </div>
                <!-- //목록 -->
                
                <div class="btn_wrap">
                    <span class="btn2"><a id="modifyUserInfo" style="cursor:pointer">정보수정</a></span>
                    <span class="btn2"><a id="removeUserInfo" style="cursor:pointer">사용자 삭제</a></span>
                    <span class="btn2"><a id="goUserList" style="cursor:pointer">목록으로</a></span>
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
<script>
function removeCheckCallBack(result){
	if(result==true){
		//삭제 처리
		$("#pageNav").prop("action","/removeUser.do");
		$("#pageNav").submit();
	}
}

$(document).ready(function(){
	$("#modifyUserInfo").click(function (e) {
		//사용자정보 수정화면으로 이동
		$("#pageNav").prop("action","/userModify.do");
		$("#pageNav").submit();
	});

	$("#goUserList").click(function (e) {
		//사용자정보 수정화면으로 이동
		$("#pageNav").prop("action","/getUserInfoList.do");
		$("#pageNav").submit();
	});
	
	$("#removeUserInfo").click(function (e) {
		//삭제 여부 확인
		lConfirm("삭제 후에는 다시 복구 할 수 없습니다.\n사용자 정보를 삭제 하시겠습니까?","removeCheckCallBack");	
	});
});
</script>
</body>
<jsp:include page="/WEB-INF/views/include/layerPopup.jsp" flush="false"/>
</html>
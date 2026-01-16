<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
  /**
  * @Class Name : userModify.jsp
  * @Description : 사용자 수정 화면
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
            <li>사용자 정보 수정</li></ul></nav>
            
            <!-- 상단조회 -->
            <div class="wrap_top">
            	<h3>사용자 정보 수정</h3>
                <h4 style="display:none;">사용자 정보 수정</h4>  
            </div>
            <!-- //상단조회 -->
            
            <!-- 하단결과 -->
            <div class="wrap_cont" style="padding-top:30px;">
            	
                <!-- 목록 -->
                <div class="wrap_table">
                <table class="table3 bm30 info_user" style="" border="0" cellpadding="0" cellspacing="0" summary="">
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
                    <td class="left">
                    	<div class="select_box">
                        <label for="select_layer"><c:if test="${userInfoVO.authGrpCd=='M'}">마스터</c:if><c:if test="${userInfoVO.authGrpCd=='W'}">작업자</c:if><c:if test="${userInfoVO.authGrpCd=='S'}">조회용</c:if></label>
                        <select id="authGrpCdN" class="select_layer" title="">
                            <option value="M" <c:if test="${userInfoVO.authGrpCd=='M'}">selected="selected"</c:if>>마스터</option>
                            <option value="W" <c:if test="${userInfoVO.authGrpCd=='W'}">selected="selected"</c:if>>작업자</option>
                            <option value="S" <c:if test="${userInfoVO.authGrpCd=='S'}">selected="selected"</c:if>>조회용</option>
                        </select>
                        </div>
                    </td>
                    <th><em class="required">*</em>사용자상태</th>
                    <td class="left">
                    	<div class="select_box">
                        <label for="select_layer"><c:if test="${userInfoVO.useYn=='Y'}">정상</c:if><c:if test="${userInfoVO.useYn=='N'}">정지</c:if></label>
                        <select id="useYnN" class="select_layer" title="">
                            <option value="Y" <c:if test="${userInfoVO.useYn=='Y'}">selected="selected"</c:if>>정상</option>
                            <option value="N" <c:if test="${userInfoVO.useYn=='N'}">selected="selected"</c:if>>정지</option>
                        </select>
                        </div>
                    </td>
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
                    <td class="left"><input id="userPswdN" type="text" class="input_text" value="<c:out value='${userInfoVO.userPswd}'/>" /></td>
				</tr>
                <tr>
                    <th><em class="required">*</em>이름</th>
                    <td class="left"><input id="userNmN" type="text" class="input_text" value="<c:out value='${userInfoVO.userNm}'/>" /></td>
                    <th>직급</th>
                    <td class="left"><input id="positionN" type="text" class="input_text" value="<c:out value='${userInfoVO.position}'/>" /></td>
				</tr>
                <tr>
                    <th><em class="required">*</em>소속</th>
                    <td class="left">
                        <div class="select_box">
                        <label for="select_layer"><c:out value='${userInfoVO.belong}'/></label>
                        <select id="belongIdN" class="select_layer" title="">
                            <c:forEach var="lineData" items="${belongInfoList}" varStatus="status">
                            <option value="<c:out value="${lineData.belongId}"/>" <c:if test="${userInfoVO.belongId==lineData.belongId}">selected="selected"</c:if>><c:out value="${lineData.belongNm}"/></option>
                            </c:forEach>
                        </select>
                        </div>
                    </td>
                    <th><em class="required">*</em>부서</th>
                    <td class="left"><input id="deptN" type="text" class="input_text" value="<c:out value='${userInfoVO.dept}'/>" /></td>
				</tr>
                <tr>
                    <th>전화번호</th>
                    <td class="left">
                    	<input id="phoneNum1N" type="text" style="width:50px" class="input_text numberOnly" size="3" value="<c:out value='${userInfoVO.phoneNum1}'/>" maxlength="3" />
                    	-
                    	<input id="phoneNum2N" type="text" style="width:60px"class="input_text numberOnly" size="4" value="<c:out value='${userInfoVO.phoneNum2}'/>" maxlength="4" />
                    	-
                    	<input id="phoneNum3N" type="text" style="width:60px"class="input_text numberOnly" size="4" value="<c:out value='${userInfoVO.phoneNum3}'/>" maxlength="4" />
                    </td>
                    <th><em class="required">*</em>휴대폰</th>
                    <td class="left">
                    	<input id="mobileNum1N" type="text" style="width:50px" class="input_text numberOnly" size="3" value="<c:out value='${userInfoVO.mobileNum1}'/>" maxlength="3" />
                    	-
                    	<input id="mobileNum2N" type="text" style="width:60px" class="input_text numberOnly" size="4" value="<c:out value='${userInfoVO.mobileNum2}'/>" maxlength="4" />
                    	-
                    	<input id="mobileNum3N" type="text" style="width:60px" class="input_text numberOnly" size="4" value="<c:out value='${userInfoVO.mobileNum3}'/>" maxlength="4" />
                    </td>
				</tr>
                <tr>
                    <th>이메일</th>
                    <td class="left" colspan="3">
                    	<ul class="mail_input">
                        <li><input id="emailAddrN" type="text" class="input_text" value="<c:out value='${userInfoVO.emailAddr}'/>" /></li>
                        </ul>                    
                    </td>
				</tr>
                <tr>
                    <th>메모</th>
                    <td class="left" colspan="3"><textarea id="memoN" rows="5" class="input_textarea" tabindex="1"><c:out value='${userInfoVO.memo}'/></textarea></td>
				</tr>      
                </tbody>
                </table>                
                </div>
                <!-- //목록 -->
                
                <div class="btn_wrap">
                    <span class="btn2"><a id="saveUserInfo" style="cursor:pointer">정보수정</a></span>
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
function saveUserInfoCallBack(data){
	
	//lAlert("사용자 정보가 정상적으로 변경 되었습니다.");
	
	$("#pageNav").prop("action","/userDetail.do");
	$("#pageNav").submit();
	
}

function validationData(){

	//비밀번호 필수
	if($("#userPswdN").val()==""){
		lAlert("비밀번호는 필수로 입력 하셔야 합니다.");
		$("#userPswdN").focus();
		return false;
	}
	//비밀번호 네글자 이상
	if($("#userPswdN").val().length<4){
		lAlert("비밀번호는 4글자 이상 입력 하셔야 합니다.");
		$("#userPswdN").focus();
		return false;
	}
	//이름 필수
	if($("#userNmN").val()==""){
		lAlert("이름은 필수로 입력 하셔야 합니다.");
		$("#userNmN").focus();
		return false;
	}
	//소속 필수
	if($("#belongIdN").val()==""){
		lAlert("소속은 필수로 선택 하셔야 합니다.");
		$("#belongIdN").focus();
		return false;
	}
	//부서 필수
	if($("#deptN").val()==""){
		lAlert("부서는 필수로 입력 하셔야 합니다.");
		$("#deptN").focus();
		return false;
	}
	//휴대폰 필수
	if($("#mobileNum1N").val()==""||$("#mobileNum2N").val()==""||$("#mobileNum3N").val()==""){
		lAlert("휴대폰 번호는 필수로 입력 하셔야 합니다.");
		$("#mobileNum1N").focus();
		return false;
	}
	
	return true;
}

$(document).ready(function(){
	$("#saveUserInfo").click(function (e) {
		//사용자정보 저장
		if(validationData()){
			//파라미터 만들기
			var param = {userId : $("#userId").val(), userNm : $("#userNmN").val()
					, userPswd : $("#userPswdN").val(), position : $("#positionN").val()
					, belongId : $("#belongIdN").val(), dept : $("#deptN").val()
					, phoneNum1 : $("#phoneNum1N").val(), phoneNum2 : $("#phoneNum2N").val()
					, phoneNum3 : $("#phoneNum3N").val(), mobileNum1 : $("#mobileNum1N").val()
					, mobileNum2 : $("#mobileNum2N").val(), mobileNum3 : $("#mobileNum3N").val()
					, emailAddr : $("#emailAddrN").val(), memo : $("#memoN").val()
					, authGrpCd : $("#authGrpCdN").val(), useYn : $("#useYnN").val()};

			var _url = "/modifyUserInfo.do";
			
			ajaxCall(param, _url, 'saveUserInfoCallBack' );
		}
	});

	$("#goUserList").click(function (e) {
		//사용자정보 수정화면으로 이동
		$("#pageNav").prop("action","/getUserInfoList.do");
		$("#pageNav").submit();
	});
	
});
</script>
</body>
<jsp:include page="/WEB-INF/views/include/layerPopup.jsp" flush="false"/>
</html>
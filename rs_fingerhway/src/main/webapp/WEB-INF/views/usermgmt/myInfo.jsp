<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
  /**
  * @Class Name : myInfo.jsp
  * @Description : 나의정보 화면
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
	<input type="hidden" id="mainMenuId" name="mainMenuId"  value="<c:out value='${paramVO.mainMenuId}'/>" />
	<input type="hidden" id="subMenuId"  name="subMenuId"   value="<c:out value='${paramVO.subMenuId}'/>" />
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
            <li><a onclick="goMenu('<c:out value='${menuInfoVO.menuUrl}'/>','<c:out value='${menuInfoVO.menuId}'/>','<c:out value='${menuInfoVO.subMenu[0].menuId}'/>');" style="cursor:pointer">사용자관리</a></li><li>&gt;</li>
            <li>나의 정보</li></ul></nav>
            
            <!-- 상단조회 -->
            <div class="wrap_top">
            	<h3>나의 정보</h3>
                <h4>나의 정보를 확인  할 수 있습니다.</h4>  
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
                    <th>아이디</th>
                    <td class="left"><c:out value='${userInfo.userId}'/></td>
                    <th>권한</th>
                    <td class="left"><c:out value='${userInfo.authGrpCdNm}'/></td>
				</tr>                
                </tbody>
                </table>
                
                <table class="table3" style="" border="0" cellpadding="0" cellspacing="0" summary="">
                <caption>목록</caption>
                <colgroup>
                <col style="width:15%;" />
                <col style="width:35%;" />                                         
                <col style="width:15%;" />
                <col style="width:35%;" />            
                </colgroup>               
                <tbody>        
                <tr>
                    <th>담당자</th>
                    <td class="left"><c:out value='${userInfo.userNm}'/></td>
                    <th>직급</th>
                    <td class="left"><c:out value='${userInfo.position}'/></td>
				</tr>
                <tr>
                    <th>소속</th>
                    <td class="left"><c:out value='${userInfo.belong}'/></td>
                    <th>부서</th>
                    <td class="left"><c:out value='${userInfo.dept}'/></td>
				</tr>
                <tr>
                    <th>전화번호</th>
                    <td class="left">
                    <c:if test="${userInfo.phoneNum1!=null}">
                    <c:out value='${userInfo.phoneNum1}'/>-<c:out value='${userInfo.phoneNum2}'/>-<c:out value='${userInfo.phoneNum3}'/>
                    </c:if>
                    </td>
                    <th>연락처</th>
                    <td class="left">
                    <c:if test="${userInfo.mobileNum1!=null}">
                    <c:out value='${userInfo.mobileNum1}'/>-<c:out value='${userInfo.mobileNum2}'/>-<c:out value='${userInfo.mobileNum3}'/>
                    </c:if>
                    </td>
				</tr>
                <tr>
                    <th>이메일</th>
                    <td class="left" colspan="3"><c:out value='${userInfo.emailAddr}'/></td>
				</tr>
                <tr>
                    <th>메모</th>
                    <td class="left" colspan="3"><textarea id="memoN" rows="5" class="input_textarea readonly" tabindex="1" readOnly><c:out value='${userInfo.memo}'/></textarea></td>
				</tr>                
                </tbody>
                </table>                
                </div>
                <!-- //목록 -->
				
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
</script>
<jsp:include page="/WEB-INF/views/include/layerPopup.jsp" flush="false"/>
</html>
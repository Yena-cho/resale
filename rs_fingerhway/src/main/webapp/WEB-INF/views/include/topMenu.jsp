<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%
  /**
  * @Class Name : topMenu.jsp
  * @Description : 상단메뉴
  * @Modification Information
  *
  *   수정일		수정자		수정내용
  *  -------	--------	---------------------------
  *  2018.02.09	이동한		최초 생성
  *
  */
%>

    <!-- 헤더 영역 -->    
    <div id="header"><header>
        <h1><a onclick="goMenu('<c:out value='${homeMenuInfoVO.menuUrl}'/>','<c:out value='${homeMenuInfoVO.menuId}'/>','<c:out value='${homeMenuInfoVO.subMenu[0].menuId}'/>');" style="cursor:pointer"><span>경기고속도로</span></a></h1>
    	<nav><ul class="gnb">
		<c:forEach var="menuList" items="${menuInfo}" varStatus="status">
			<c:if test="${menuList.menuId == paramVO.mainMenuId}">
				<li class="on"><a onclick="goMenu('<c:out value='${menuList.menuUrl}'/>','<c:out value='${menuList.menuId}'/>','<c:out value='${menuList.subMenu[0].menuId}'/>');" style="cursor:pointer"><span><c:out value='${menuList.menuNm}'/></span></a></li>
			</c:if>
			<c:if test="${menuList.menuId != paramVO.mainMenuId}">
				<li><a onclick="goMenu('<c:out value='${menuList.menuUrl}'/>','<c:out value='${menuList.menuId}'/>','<c:out value='${menuList.subMenu[0].menuId}'/>');" style="cursor:pointer"><span><c:out value='${menuList.menuNm}'/></span></a></li>
			</c:if>
  		</c:forEach>
        </ul></nav>
	</header></div>       
    <!-- //헤더 영역 -->
    <hr /> 
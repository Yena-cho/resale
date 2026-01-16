<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%
  /**
  * @Class Name : lnb.jsp
  * @Description : Left Navigation Bar
  * @Modification Information
  *
  *   수정일		수정자		수정내용
  *  -------	--------	---------------------------
  *  2018.02.09	이동한		최초 생성
  *
  */
%>
		<!-- 왼쪽 영역 -->
        <div id="aside"><aside>
            <div class="login_info">
               <p><em><c:out value='${userInfo.getUserNm()}'/> 님</em> 반갑습니다.</p>
               <span class="sbtn1"><a onclick="goMenu('/userInfoMgmt.do','GHVCUM000','GHVCUM001');" style="cursor:pointer">나의정보</a></span><span class="sbtn1"><a onclick="javascirpt:logout();" style="cursor:pointer">LOGOUT</a></span>
            </div>
            <div class="lnb_wrap">
                <h2><c:out value='${menuInfoVO.menuNm}'/></h2>
                <ul class="lnb">
				<c:forEach var="subMenuList" items="${menuInfoVO.subMenu}" varStatus="status">
					<c:if test="${subMenuList.menuId == paramVO.subMenuId}">
						<li class="on"><a onclick="goMenu('<c:out value='${subMenuList.menuUrl}'/>','<c:out value='${menuInfoVO.menuId}'/>','<c:out value='${subMenuList.menuId}'/>');" style="cursor:pointer"><span><c:out value='${subMenuList.menuNm}'/></span></a></li>
					</c:if>
					<c:if test="${subMenuList.menuId != paramVO.subMenuId}">
						<li><a onclick="goMenu('<c:out value='${subMenuList.menuUrl}'/>','<c:out value='${menuInfoVO.menuId}'/>','<c:out value='${subMenuList.menuId}'/>');" style="cursor:pointer"><span><c:out value='${subMenuList.menuNm}'/></span></a></li>
					</c:if>
		  		</c:forEach>
                </ul>
            </div>
            <div class="bnner_wrap">
                <span class="sms"><a id="openMsgPopupBtn" style="cursor:pointer">문자보내기</a></span>
            </div>
        </aside></div>    
        <!-- //왼쪽 영역 -->
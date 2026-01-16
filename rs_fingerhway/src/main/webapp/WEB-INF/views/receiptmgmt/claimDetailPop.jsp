<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
  /**
  * @Class Name : msgContentPop.jsp
  * @Description : 메세지전송 팝업 화면
  * @Modification Information
  *
  *   수정일		수정자		수정내용
  *  -------	--------	---------------------------
  *  2018.02.07	이동한		최초 생성
  *
  */
%>
<jsp:include page="/WEB-INF/views/include/popHeader.jsp" flush="false"/>
<title>청구항목 상세</title>
</head>
<body>

<input type="hidden" id="userId"  name="userId"   value="<c:out value='${userInfo.userId}'/>" />

<!-- 윈도우팝업 -->
<div class="winpop">
<div class="winpop_wrap">
    <div class="winpop_top"><h3>청구항목 상세</h3><a href="#none" onClick="window.close()"><span>닫기</span></a></div>
    <div class="winpop_data">
    	
        <!-- 목록 -->
        <div class="wrap_table">
        <table style="" class="table2" border="0" cellpadding="0" cellspacing="0" summary="">
        <caption>목록</caption>
        <colgroup>
        <col style="width:11%;" />
        <col style="width:24%;" /><!-- 가로값수정 180207 -->                                         
        <col style="width:22%;" />
        <col style="width:16%;" /><!-- 가로값수정 180207 --> 
        <col style="width:26%;" />             
        </colgroup>
        <thead>
        <tr>
            <th>No.</th>
            <th>발생일시</th>
            <th>발생원인</th>
            <th>통행요금</th>
            <th>통행장소</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>1</td>
            <td><c:out value="${claimItemInfo.occurDttm1}"/></td>
            <td><c:out value="${claimItemInfo.occurReason1}"/></td>
            <td><c:out value="${claimItemInfo.passAmt1}"/></td>
            <td><c:out value="${claimItemInfo.passPlace1}"/></td>
        </tr>
        <c:if test="${claimItemInfo.occurDttm2!=''}">
        <tr>
            <td>2</td>
            <td><c:out value="${claimItemInfo.occurDttm2}"/></td>
            <td><c:out value="${claimItemInfo.occurReason2}"/></td>
            <td><c:out value="${claimItemInfo.passAmt2}"/></td>
            <td><c:out value="${claimItemInfo.passPlace2}"/></td>
        </tr>
        </c:if>
        <c:if test="${claimItemInfo.occurDttm3!=''}">
        <tr>
            <td>3</td>
            <td><c:out value="${claimItemInfo.occurDttm3}"/></td>
            <td><c:out value="${claimItemInfo.occurReason3}"/></td>
            <td><c:out value="${claimItemInfo.passAmt3}"/></td>
            <td><c:out value="${claimItemInfo.passPlace3}"/></td>
        </tr>
        </c:if>
        <c:if test="${claimItemInfo.occurDttm4!=''}">
        <tr>
            <td>4</td>
            <td><c:out value="${claimItemInfo.occurDttm4}"/></td>
            <td><c:out value="${claimItemInfo.occurReason4}"/></td>
            <td><c:out value="${claimItemInfo.passAmt4}"/></td>
            <td><c:out value="${claimItemInfo.passPlace4}"/></td>
        </tr>
        </c:if>
        <c:if test="${claimItemInfo.occurDttm5!=''}">
        <tr>
            <td>5</td>
            <td><c:out value="${claimItemInfo.occurDttm5}"/></td>
            <td><c:out value="${claimItemInfo.occurReason5}"/></td>
            <td><c:out value="${claimItemInfo.passAmt5}"/></td>
            <td><c:out value="${claimItemInfo.passPlace5}"/></td>
        </tr>
        </c:if>
        <c:if test="${claimItemInfo.occurDttm6!=''}">
        <tr>
            <td>6</td>
            <td><c:out value="${claimItemInfo.occurDttm6}"/></td>
            <td><c:out value="${claimItemInfo.occurReason6}"/></td>
            <td><c:out value="${claimItemInfo.passAmt6}"/></td>
            <td><c:out value="${claimItemInfo.passPlace6}"/></td>
        </tr>
        </c:if>
        <c:if test="${claimItemInfo.occurDttm7!=''}">
        <tr>
            <td>7</td>
            <td><c:out value="${claimItemInfo.occurDttm7}"/></td>
            <td><c:out value="${claimItemInfo.occurReason7}"/></td>
            <td><c:out value="${claimItemInfo.passAmt7}"/></td>
            <td><c:out value="${claimItemInfo.passPlace7}"/></td>
        </tr>
        </c:if>
        <c:if test="${claimItemInfo.occurDttm8!=''}">
        <tr>
            <td>8</td>
            <td><c:out value="${claimItemInfo.occurDttm8}"/></td>
            <td><c:out value="${claimItemInfo.occurReason8}"/></td>
            <td><c:out value="${claimItemInfo.passAmt8}"/></td>
            <td><c:out value="${claimItemInfo.passPlace8}"/></td>
        </tr>
        </c:if>
        <c:if test="${claimItemInfo.occurDttm9!=''}">
        <tr>
            <td>9</td>
            <td><c:out value="${claimItemInfo.occurDttm9}"/></td>
            <td><c:out value="${claimItemInfo.occurReason9}"/></td>
            <td><c:out value="${claimItemInfo.passAmt9}"/></td>
            <td><c:out value="${claimItemInfo.passPlace9}"/></td>
        </tr>
        </c:if>
        <c:if test="${claimItemInfo.occurDttm10!=''}">
        <tr>
            <td>10</td>
            <td><c:out value="${claimItemInfo.occurDttm10}"/></td>
            <td><c:out value="${claimItemInfo.occurReason10}"/></td>
            <td><c:out value="${claimItemInfo.passAmt10}"/></td>
            <td><c:out value="${claimItemInfo.passPlace10}"/></td>
        </tr>
        </c:if>
        <tr class="tr1">
            <td>총합</td>
            <td></td>
            <td></td>
            <td><c:out value="${claimItemInfo.totAmt}"/></td>
            <td></td>
        </tr>
        </tbody>
        </table>
        </div>
        
    </div>
                    
</div>
</div> 
<!-- //윈도우팝업 -->
</body>
<jsp:include page="/WEB-INF/views/include/layerPopup.jsp" flush="false"/>
</html>
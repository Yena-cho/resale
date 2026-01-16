<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
  /**
  * @Class Name : receiptMsgPop.jsp
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
<title>문자 보내기</title>
</head>
<body>

<input type="hidden" id="userId"  name="userId"   value="<c:out value='${userInfo.userId}'/>" />
<input type="hidden" id="sendNum"  name="sendNum"   value="<c:out value='${userInfo.belongPhoneNum1}'/><c:out value='${userInfo.belongPhoneNum2}'/><c:out value='${userInfo.belongPhoneNum3}'/>" />

<!-- 윈도우팝업 -->
<div class="winpop">
<div class="winpop_wrap">
    <div class="winpop_top"><h3>문자 보내기</h3><a href="#none" onClick="window.close()"><span>닫기</span></a></div>
    <div class="winpop_data">
    
    	<div class="wrap_sms">
            <div class="sms_col1">
                <div class="top_tit"><h4>메시지 작성</h4><p><span id="strLen"> 0/ 2000 Bytes</span></p></div>
                <textarea id="msgContent" rows="20" class="input_textarea" tabindex="1" readOnly>[경기고속도로 통행료 납부안내] 
[<c:out value='${msgParam.carOwnerNm}'/>] 고객님, [<c:out value='${msgParam.occurReason1}'/>] 사유로 
통행료 미납이 발생하였습니다. 
반드시 정확한 금액을 기한 내 
입금해주시기 바랍니다.
▶미납금액 : [<c:out value='${msgParam.totAmt}'/>]
▶은행 : 신한은행 
▶계좌 :[<c:out value='${msgParam.virtualAcntNum}'/>](경기고속도로)
▶입금기한 : [<c:out value='${msgParam.payDueDt}'/>]</textarea>
            	<div class="wrap_sbtn">
                	<span class="sbtn3 left">&nbsp;</span>
                    <span class="sbtn4 right" >&nbsp;</span>
                </div>
            	<div class="wrap_sbtn" style="display:none">
                	<span class="sbtn3 left"><a href="#">다시쓰기</a></span>
                    <span class="sbtn4 right" ><a href="#">처음으로</a></span>
                </div>
            </div>
            <div class="sms_col2">
            	<ul class="top_tit">
                <li><h4>수신번호</h4><input id="recvNum" type="text" value="" tabindex="2" class="numberOnly" /></li>
                <li><h4>발신번호</h4><span><c:if test="${userInfo.belongPhoneNum1!=null}"><c:out value='${userInfo.belongPhoneNum1}'/>-</c:if><c:out value='${userInfo.belongPhoneNum2}'/>-<c:out value='${userInfo.belongPhoneNum3}'/></span></li>
                </ul>
                <ul class="mesg">
                <li>등록하신 발신번호로 지정됩니다.</li>
                <li>[web발신] 표시가 자동으로 메시지 앞부분에 삽입됩니다.</li>
                </ul>
                <span class="btn1 ico_arrow" ><a id="sendMsgBtn" style="cursor:pointer">문자보내기</a></span></div>
      </div>
      
    </div>                    
</div>
</div> 
<!-- //윈도우팝업 -->
<script>
function msgSendCallBack(data){
	alert(JSON.stringify(data));
	
	lAlert(data.retMsg);
	
	//화면 닫는다.
	window.close();
}
function validateValue(){
	if($("#recvNum").val()==""){
		lAlert("수신자 번호를 입력 하셔야 합니다.");
		$("#recvNum").focus();
		return false;
	} else if($("#msgContent").val()==""){
		lAlert("메세지 내용을 입력 하셔야 합니다.");
		$("#msgContent").focus();
		return false;
	}
	return true;
}
$(document).ready(function(){
	
	$("#strLen").text(Length($("#msgContent").val())+' / 2000 Bytes');
	
	$("#sendMsgBtn").click(function (e) {
		if(validateValue()){
			var param = {recvNum : $("#recvNum").val(), msgContent : $("#msgContent").val()
					, sendNum : $("#sendNum").val(), userId : $("#userId").val()};
			var _url = "/sendMsg.do";
			
			ajaxCall(param, _url, 'msgSendCallBack' );
		}
	});
});
</script>
</body>
<jsp:include page="/WEB-INF/views/include/layerPopup.jsp" flush="false"/>
</html>
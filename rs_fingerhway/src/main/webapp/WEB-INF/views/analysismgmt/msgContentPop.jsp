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
<title>문자 보내기</title>
</head>
<body>

<input type="hidden" id="userId"  name="userId"   value="<c:out value='${userInfo.userId}'/>" />
<input type="hidden" id="sendNum"  name="sendNum"   value="<c:out value='${userInfo.belongPhoneNum1}'/><c:out value='${userInfo.belongPhoneNum2}'/><c:out value='${userInfo.belongPhoneNum3}'/>" />

<!-- 윈도우팝업 -->
<div class="winpop" >
<div class="winpop_wrap">
    <div class="winpop_top"><h3>문자 보내기</h3><a href="#none" onClick="window.close()"><span>닫기</span></a></div>
    <div class="winpop_data">

		<div class="wrap_sms2">
    	<div class="top_tit"><span id="strLen">0/ 2000 Bytes</span></div>
    	<div class="sms_input">                
		<textarea rows="20" id="msgContent" class="input_textarea" tabindex="1" readOnly><c:out value='${tbMsgSendHistVO.msgContent}'/></textarea> 
    	</div>     
    	<div class="sms_phone"><span>수신번호</span><input id="recvNum" type="text" value="<c:out value='${tbMsgSendHistVO.recvNum}'/>" tabindex="2" readOnly /></div>            
    	<div class="btn1" ><a id="sendMsgBtn" style="cursor:pointer">재전송</a></div><!-- 팝업닫기링크 수정 180220 -->   
		</div> 
         
    </div>                    
</div>
</div> 

<script>
function msgSendCallBack(data){
	//alert(JSON.stringify(data));
	
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
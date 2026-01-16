<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="laypop_alpha" style="display:none"></div><!-- 반투명 -->  

<!-- 로딩중팝업  추가 180209 -->
<div class="laypop_loading" style="display:none">
<img src="/resources/images/loading.gif" alt="loading"><span>로딩중</span>             
</div> 
<!-- //로딩중팝업 -->
<!-- Alert Layer -->
<div id="divAlert" class="laypop2" style="display:none"><!-- Alert메세지창_화면중앙정렬 -->
<div class="laypop2_wrap">
    <div class="laypop_top"><h3>알림</h3><a onclick="javascript:uAlert();return false;" style="cursor:pointer"><span>닫기</span></a></div>
    <div class="laypop_data">
     	
        <div class="ico_alert">
    		<span class="ico"><img src="/resources/images/ico_alert3.png" alt="" /></span>                       
        	<p id="alertMsg" class="mesg">업로드 형식이 유효하지 않습니다</p>            
        </div>
        
        <!-- 버튼영역-->        
        <div class="wrap_btn">        	  
            <span class="btn1"><a onclick="javascript:uAlert();return false;" style="cursor:pointer">확인</a></span>
        </div>
        <!-- //버튼영역 --> 
               
    </div>                               
</div>
</div> 
<!-- //Alert Layer -->

<div id="divConfirm" class="laypop2" style="display:none"><!-- Confirm메세지창_화면중앙정렬 -->
<div class="laypop2_wrap">
    <div class="laypop_top"><h3>확인</h3><a href="2_1_청구관리.html"><span>닫기</span></a></div>
    <div class="laypop_data">
     	
        <div class="ico_alert">
    		<span class="ico"><img src="/resources/images/ico_alert1.png" alt="" /></span>                       
        	<p id="confirmMsg" class="mesg">엑셀 파일을 교체하시겠습니까?</p>            
        </div>
        
        <!-- 버튼영역-->        
        <div class="wrap_btn">        	  
            <span class="btn1"><a onclick="javascript:uConfirm(true);" style="cursor:pointer">확인</a></span>
            <span class="btn1 gray"><a onclick="javascript:uConfirm(false);" style="cursor:pointer">취소</a></span>
        </div>
        <!-- //버튼영역 --> 
               
    </div>                               
</div>
</div> 
<!-- //레이어팝업 -->
<script>
//처리중 레이어 팝업 열기
function loadProcessPopup(){
	
	$('.laypop_alpha').show();
	$('.laypop_loading').show();
	
	//레이어팝업 뜰때 부모창 스크롤 막기
	$('html, body').css({'overflow': 'hidden', 'height': '100%'});
	$('#element').on('scroll touchmove mousewheel', function(event) {     
	event.preventDefault();
	event.stopPropagation();
	return false; });
}
//처리중 레이어 팝업 닫기
function unloadProcessPopup(){
	$('.laypop_alpha').hide();
	$('.laypop_loading').hide();
	
	//모달창 닫으면 스크롤해제 
	$('html, body').css({'overflow': 'auto', 'height': '100%'});
	//모달창 닫으면 스크롤해제 
	$('#element').off('scroll touchmove mousewheel');
}

//alert 레이어 팝업 열기
function lAlert(msg, excute){
	$('.laypop_alpha').show();
	$('#alertMsg').text(msg);
	$('#divAlert').show();
	
	//레이어팝업 뜰때 부모창 스크롤 막기
	$('html, body').css({'overflow': 'hidden', 'height': '100%'});
	$('#element').on('scroll touchmove mousewheel', function(event) {     
	event.preventDefault();
	event.stopPropagation();
	return false; });
}
//alert 레이어 팝업 열기
function uAlert(){
	$('.laypop_alpha').hide();
	$('#divAlert').hide();
	
	//모달창 닫으면 스크롤해제 
	$('html, body').css({'overflow': 'auto', 'height': '100%'});
	//모달창 닫으면 스크롤해제 
	$('#element').off('scroll touchmove mousewheel');
	
}

//Confirm창 호출시 지정할 콜백함수
var confirmCallBck = "";

//confirm 레이어 팝업 열기
function lConfirm(msg, callBck){
	
	confirmCallBck = callBck;
	
	$('.laypop_alpha').show();
	$('#confirmMsg').text(msg);
	$('#divConfirm').show();
	
	//레이어팝업 뜰때 부모창 스크롤 막기
	$('html, body').css({'overflow': 'hidden', 'height': '100%'});
	$('#element').on('scroll touchmove mousewheel', function(event) {     
	event.preventDefault();
	event.stopPropagation();
	return false; });
}
//confirm 레이어 팝업 열기
function uConfirm(result){
	$('.laypop_alpha').hide();
	$('#divConfirm').hide();
	
	//모달창 닫으면 스크롤해제 
	$('html, body').css({'overflow': 'auto', 'height': '100%'});
	//모달창 닫으면 스크롤해제 
	$('#element').off('scroll touchmove mousewheel');
	
	eval(confirmCallBck+'(result);');
}
</script>
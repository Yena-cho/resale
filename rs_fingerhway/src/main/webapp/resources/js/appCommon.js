//로그아웃 처리 후 콜백 함수
function logoutCallBack(result){
	if(result==true){
		location.href="/logout.do";
	}
}

//로그아웃
function logout(){
	lConfirm("로그아웃 하시겠습니까?","logoutCallBack");
}

//Ajax 호출
function ajaxCall(param, _url, callBack){
	
	//alert(JSON.stringify(param));
	
	//처리중 레이어팝업 실행
	loadProcessPopup();
	$.ajax({
		type: "POST"
		,url: _url
		,cache: false
		,dataType: "json"
		,data: JSON.stringify(param) 
		,contentType: "application/json; charset=utf-8"
		,success : function(data, status){
			//처리중 레이어팝업 닫기
			unloadProcessPopup();

			if(data.retCode=="0000"){
				//콜백 함수 있으면 호출
				if(callBack){ eval(callBack+'(data);');} // callback
			} else {
				//오류 메세지 처리(레이어팝업)
				//alert(data.retMsg);
				lAlert(data.retMsg);
			}
		}
		,error: function(request, status, error){
			//처리중 레이어팝업 닫기
			unloadProcessPopup();
			//오류 메세지 처리(레이어팝업)
		    //alert("loading error:" + request.status);
			lAlert("시스템 오류가 발생 하였습니다.\n계속해서 오류가 발생 할 경우 시스템 담당자에게 문의 하세요.");
		}
	});
}

//Ajax 파일 전송 호출
function ajaxFileSend(formData, _url, callBack){
	
	//처리중 레이어팝업 실행
	loadProcessPopup();
	$.ajax({
		type: "POST"
		,url: _url
		,data:  formData
		,dataType: "JSON"
		,contentType: false 
		,processData : false
		,success : function(data, status){
			//처리중 레이어팝업 닫기
			unloadProcessPopup();

			if(data.retCode=="0000"){
				//콜백 함수 있으면 호출
				if(callBack){ eval(callBack+'(data);');} // callback
			} else {
				//오류 메세지 처리(레이어팝업)
				//alert(data.retMsg);
				lAlert(data.retMsg);
			}
		}
		,error: function(request, status, error){
			//처리중 레이어팝업 닫기
			unloadProcessPopup();
			//오류 메세지 처리(레이어팝업)
		    //alert("loading error:" + request.status);
			lAlert("시스템 오류가 발생 하였습니다.\n계속해서 오류가 발생 할 경우 시스템 담당자에게 문의 하세요.");
		}
	});
}

//공통버튼 처리
$(document).ready(function(){ 
	$("#openMsgPopupBtn").click(function (e) {
		window.open("/sendMsgPopup.do", "문자보내기", "width=600px,height=500px, toolbar=no, menubar=no, resizable=1, scrollbars=no");
	});
	
    //숫자만 체크
    $(".numberOnly").keyup(function(){
        /* 48~57:일반 숫자키 코드, 96~105:숫자키패드 숫자키 코드 , 8 : backspace, 9 : tab ,46 : delete, 37 : 왼쪽방향키 , 39 : 오른쪽방향키 , 36 : home, 35 : End, 144 : NumLock*/
        var keyID = event.which;
    
        if (!((keyID >= 48 && keyID <= 57) || (keyID >= 96 && keyID <= 105) || keyID == 8 || keyID == 9 || keyID == 46 || (keyID >= 35 && keyID <= 39) || keyID == 144)) {
            lAlert("숫자만 입력 가능합니다.");
            this.value = this.value.replace(/[^0-9\.]/g, ''); //숫자를 제외한 문자를 지워준다.
        }
    });

});

//datepicker 처리
$(function() { 
	
	$(".calendar1").datepicker({ 
		inline: true, 
		dateFormat: "yymmdd",    /* 날짜 포맷 */ 
		prevText: 'prev', 
		nextText: 'next', 
		showButtonPanel: false,    /* 버튼 패널 사용 */ 
		changeMonth: false,        /* 월 선택박스 사용 */ 
		changeYear: false,        /* 년 선택박스 사용 */ 
		showOtherMonths: false,    /* 이전/다음 달 일수 보이기 */ 
		selectOtherMonths: false,    /* 이전/다음 달 일 선택하기 */ 
		showOn: "button", 
		buttonImage: "/resources/images/btn_calendar.png", 
		buttonImageOnly: true, 
		minDate: '-30y', 
		closeText: '닫기', 
		currentText: '오늘', 
		showMonthAfterYear: true,        /* 년과 달의 위치 바꾸기 */ 
		/* 한글화 */ 
		monthNames : ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'], 
		monthNamesShort : ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'], 
		dayNames : ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesShort : ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesMin : ['일', '월', '화', '수', '목', '금', '토'],
		showAnim: 'slideDown'
	});
	
	$(".calendar2").datepicker({ 
		inline: true, 
		dateFormat: "yymm",    /* 날짜 포맷 */ 
		prevText: 'prev', 
		nextText: 'next', 
		showButtonPanel: false,    /* 버튼 패널 사용 */ 
		changeMonth: false,        /* 월 선택박스 사용 */ 
		changeYear: false,        /* 년 선택박스 사용 */ 
		showOtherMonths: false,    /* 이전/다음 달 일수 보이기 */ 
		selectOtherMonths: false,    /* 이전/다음 달 일 선택하기 */ 
		showOn: "button", 
		buttonImage: "/resources/images/btn_calendar.png", 
		buttonImageOnly: true, 
		minDate: '-30y', 
		closeText: '닫기', 
		currentText: '오늘', 
		showMonthAfterYear: true,        /* 년과 달의 위치 바꾸기 */ 
		/* 한글화 */ 
		monthNames : ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'], 
		monthNamesShort : ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'], 
		dayNames : ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesShort : ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesMin : ['일', '월', '화', '수', '목', '금', '토'],
		showAnim: 'slideDown'
	});
	
});

//메뉴 이동
function goMenu(menuUrl, mainMenuId, subMenuId){

	//메뉴 이동시 입력폼 초기화
	$('#pageNav').find('input[type="hidden"]').val(''); 
	
	//기본값 세팅
	$("#pageSize").val("10");
	$("#pageIdx").val("1");
	
	//메뉴 ID 세팅
	$("#mainMenuId").val(mainMenuId);
	$("#subMenuId").val(subMenuId);
			
	$("#pageNav").prop("action",menuUrl);
	$("#pageNav").submit();
	
}

//페이징 버튼 html을 만들고 return 한다.
function makePageList(pageIdx, maxPage, pageCnt){
	//페이지 리스트 그리기
	var pageListHtml = "";
	
	if(maxPage==0||maxPage==""){
		pageListHtml = "<span class='float_clear'>&nbsp;</span>";
	} else if(maxPage<2){
		//$("#pageDiv").html("<a class='on'>1</a>");
		pageListHtml = "<a class='on'>1</a>";
	} else {
		//현재 페이지 영역을 구한다.
		var startPageNum = (parseInt(pageIdx/pageCnt)*pageCnt)+1;	//페이지 리스트에 표현될 첫번째 페이지
		if((pageIdx%pageCnt)==0){
			startPageNum = startPageNum - pageCnt;
		}
		var endPageNum = startPageNum+pageCnt;
			
		pageListHtml = "<a onclick='javascript:goPage("+1+")' style='cursor:pointer' class='first' title='처음'><span>&lt;</span></a>";
		if (pageIdx>1){
			pageListHtml = pageListHtml + "<a onclick='javascript:goPage("+(pageIdx-1)+")' style='cursor:pointer'  class='prev'>이전</a>";
		}
		for(var i=startPageNum;i<endPageNum;i++){
			
			if(i==pageIdx){
				pageListHtml = pageListHtml + "<a class='on'>"+i+"</a>";
			} else {
				pageListHtml = pageListHtml + "<a onclick='javascript:goPage("+i+")' style='cursor:pointer'>"+i+"</a>";
			}
			if(i==maxPage){
				break;
			}
		}
		if(parseInt(maxPage)>parseInt(pageIdx)){
			pageListHtml = pageListHtml + "<a onclick='javascript:goPage("+(parseInt(pageIdx)+1)+")' style='cursor:pointer' class='next'><span>다음</span></a>";
		}
		pageListHtml = pageListHtml + "<a onclick='javascript:goPage("+maxPage+")' style='cursor:pointer' class='end' title='끝' ><span>&gt;</span></a>";

		//$("#pageDiv").html(pageListHtml);
	}
	
	return pageListHtml;
}

//현재날짜 리턴
function getCurrentDate(){
	var today = new Date() ; 
	var year = today.getFullYear().toString(); 
	var month = (today.getMonth()+1).toString(); 
	var date = today.getDate().toString();
	
	return year+(month[1] ? month : '0'+month[0])+(date[1] ? date : '0'+date[0]);
}

//현재일자를 기준으로 gap만큼 이전 월의 일자를 리턴
function getPreMonthDate(gap){
	var today = new Date() ; 
	today.setMonth((today.getMonth())-gap);
	var year = today.getFullYear().toString(); 
	var month = (today.getMonth()+1).toString();
	var date = today.getDate().toString();
	
	return year+''+(month[1] ? month : '0'+month[0])+(date[1] ? date : '0'+date[0]);	
}

//현재일을 기준으로 gap만큼 이후 일자를 리턴
function getAddMonthDate(gap){
	var today = new Date() ; 
	today.setMonth((today.getMonth())+gap);
	var year = today.getFullYear().toString(); 
	var month = (today.getMonth()+1).toString();
	var date = today.getDate().toString();
	
	return year+''+(month[1] ? month : '0'+month[0])+(date[1] ? date : '0'+date[0]);
}

//현재월을 리턴
function getCurrentMonth(){
	var today = new Date() ; 
	var year = today.getFullYear().toString(); 
	var month = (today.getMonth()+1).toString(); 
	var date = today.getDate().toString();
	
	return year+''+(month[1] ? month : '0'+month[0]);
}

//현재월을 기준으로 gap만큼 이전 월을 리턴
function getPreMonth(gap){
	var today = new Date() ; 
	today.setMonth((today.getMonth())-gap);
	var year = today.getFullYear().toString(); 
	var month = (today.getMonth()+1).toString();
	var date = today.getDate();
	
	return year+''+(month[1] ? month : '0'+month[0]);	
}

//현재월을 기준으로 gap만큼 이후 월을 리턴
function getAddMonth(gap){
	var today = new Date() ; 
	today.setMonth((today.getMonth())+gap);
	var year = today.getFullYear().toString(); 
	var month = (today.getMonth()+1).toString();
	var date = today.getDate();
	
	return year+''+(month[1] ? month : '0'+month[0]);	
}

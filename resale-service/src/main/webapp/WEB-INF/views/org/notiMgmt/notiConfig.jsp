<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>

<s:authentication property="principal.username" var="userId" />
<s:authentication property="principal.name" var="userName" />

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/>

<script type="text/javascript">
$(document).ready(function(){
	$(".textarea").on("keyup", function() {
		var id = $(this).attr('id');
		var str = $("#"+id+"").val();
		var tbyte = 0;
		var strlength = 0;
		var charStr = '';
		var limit = 1600;
		var line = str.split("\n").length;
		var lines = str.split(/(\r\n|\n|\r)/gm);
		var lineMaxLength = 80;
		var cutstr = '';

		for (var i = 0; i < str.length; i++) {
            //한글체크
            charStr = str.charAt(i);
            if (escape(charStr).length > 4) {
                tbyte += 2;
            } else {
                tbyte++;
            }

            if (tbyte <= limit) {
                strlength = i + 1;
            }
        }

    	for(var i=0; i<lines.length; i++){
			if(lines[i].length > lineMaxLength){
				var text = $(this).val();
				var lines = text.split(/(\r\n|\n|\r)/gm);
				for (var i = 0; i < lines.length; i++) {
					if (lines[i].length > lineMaxLength) {
						lines[i] = lines[i].substring(0, lineMaxLength);
					}
				}
					swal({
						type : 'warning',
						text : '한줄에 '+lineMaxLength+'자 이하 최대 10줄, 최대 800자 까지만 작성할 수 있습니다.',
						confirmButtonColor : '#3085d6',
						confirmButtonText : '확인'
					})
				$(this).val(lines.join(''));
				$(this).blur();
			}
    	}

		if(tbyte > limit){
					cutstr = $("#"+id+"").val().substr(str, strlength);
					$("#"+id+"").val(cutstr);
					$("#billCont1C").html(tbyte);
					limitCharacters($('#billCont1').val(), 1600, 10, 80, 160);
		}else{
			$("#billCont1C").html(tbyte);
		}

		if(event.keyCode == 13){
			limitCharacters($('#billCont1').val(), 1600, 10, 80, 160);
		}

		if(event.keyCode == 17 || event.keyCode == 86){
			limitCharacters($('#billCont1').val(), 1600, 10, 80, 160);
		}
});

countBytes($("#billname").val(), 'billname');
countBytes($("#cusals").val(), 'cusals');
countBytes($("#notichaname").val(), 'notichaname');
countBytes($("#billCont2").val(), 'billCont2');
countBytes($('#billCont1').val(), 'billCont1');
logoView('');
}); // doc ready

function countBytes(s, id){
    var b = 0, i = 0, c
    for(;c=s.charCodeAt(i++);b+=c>>11?2:c>>7?2:1);
    $("#"+id).html(b);
    return b
}

function onlyNumbers(obj) {
    $(obj).keyup(function(){
        $(this).val($(this).val().replace(/[^(0-9)]/g, ""));
        var str = $(this).val();
    }); 
    
}

function validationPhoneNumber(obj) {
	var regExp = new Array();
		regExp[0] = /^\d{3}-\d{3,4}-\d{4}$/; // 핸드폰 정규식
		regExp[1] = /^\d{2,3}-\d{3,4}-\d{4}$/; // 일반전화번호 규식이
		regExp[2] = /^\d{4}-\d{4}$/; // 1588 규식이
	var isValidate = false;

	for (var i=0; i<regExp.length; i++) {
		isValidate = regExp[i].test( $(obj).val() );
		if (isValidate) break;
	}

	if( isValidate == false ){
		swal({
			type : 'warning',
			text : '연락처를 정확하게 입력해 주세요.',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인',
		})
		return;
	}
	
}

var firstDepth = "nav-link-33";
var secondDepth = "sub-02";

//글자수 제한 (총바이트, 총 줄수, 줄당 최대길이, 출당 최대바이트)
function limitCharacters(text, limit, maxLine, lineMaxLength, lineMaxByte){
	var text = text;
	var line = text.split("\n").length;
	var lines = text.split(/(\r\n|\n|\r)/gm);
	var valueCheck = true;
	var _byte = 0;

	//줄 이상 제한
	if(line > maxLine){
		swal({
			type : 'warning',
			text : '안내문구는 '+ maxLine + '줄까지만 입력가능합니다.',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인'
		})
		modifiedText = $('#billCont1').val().split("\n").slice(0, maxLine);
		$('#billCont1').val(modifiedText.join("\n"));
		return false;
		valueCheck = false;
	}
    
    //1열당 글자수 제한
    var k = 0;
    var tooLong = false;
    for(var i=0; i<lines.length; i++){
    	if(lines[i].length > lineMaxLength){
    		tooLong = true;
    		k = i + 1;
    	}
    }

    if(tooLong == true){
    	swal({
    		type : 'warning',
    		text : '한줄에 '+lineMaxLength+'자 이하 최대 10줄, 최대 800자 까지만 작성할 수 있습니다. '+k+' 행을 수정해주세요.',
    		confirmButtonColor : '#3085d6',
    		confirmButtonText : '확인'
    	})
    	
    	valueCheck = false;
    }
    return valueCheck;
}

//조회
function fnSearch() {
		var url = "/org/notiMgmt/getNotiConfigAjax";
		var param = {
				billGubn : $('#selcont option:selected').val(),
				chaCd : $('#chaCd').val()
		};

		$.ajax({
			type : "post",
			async : true,
			url : url,
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(param),
			success : function(result) {
				if (result.retCode == "0000") {
					fnClear();
					if(result.map != null){
						$('#billCont1').val(basicUnEscape(result.map.billCont1)); //고지서내용
						countBytes($('#billCont1').val(), 'billCont1');
						$('#billCont2').val(basicUnEscape(result.map.billCont2)); //안내문구
                        countBytes($('#billCont2').val(), 'billCont2');
                        $('#billname').val(basicUnEscape(result.map.billName)); //고지서명
                        countBytes($('#billname').val(), 'billname');
                        $('#cusals').val(basicUnEscape(result.map.cusAls));//납부자호칭
                        countBytes($('#cusals').val(), 'cusals');
						$('#telno').val(result.map.telNo);
                        countBytes($('#telno').val(), 'telno');
                        $('#notichaname').val(basicUnEscape(result.map.notiChaName));
                        countBytes($('#notichaname').val(), 'notichaname');
                    }
				} else {
					swal({
						type : 'error',
						text : result.retMsg,
						confirmButtonColor : '#3085d6',
						confirmButtonText : '확인'
					})
				}
			}
		});
}

// 초기화
function fnClear(){
	$('#billCont1').val('');
	$('#billname').val('');
	$('#cusals').val('');
}

//저장
function fnSave(){
	if ( limitCharacters($('#billCont1').val(), 1600, 10, 80, 160) == false) return;
	if ( fnValidation() == false ) return;

	var url = "/org/notiMgmt/saveNotiConfig";
	var param = {
			chaCd : $('#chaCd').val(),
			billCont1 : basicEscape($('#billCont1').val()),
			billCont2 : basicEscape($('#billCont2').val()),
			billGubn : $('#selcont option:selected').val(),
			billName : basicEscape($('#billname').val()),
			cusAls : basicEscape($('#cusals').val()),
			notiChaName : basicEscape($('#notichaname').val()),
			telNo : $("#telno").val()
	};

	$.ajax({
		type : "post",
		async : true,
		url : url,
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(param),
		success : function(result) {
			if (result.retCode == "0000") {
				swal({
					type : 'success',
					text : '저장되었습니다.',
					confirmButtonColor : '#3085d6',
					confirmButtonText : '확인'
				});
			} else {
				swal({
					type : 'error',
					text : result.retMsg,
					confirmButtonColor : '#3085d6',
					confirmButtonText : '확인'
				});
			}
		}
	});
}

function fnValidation() {
	// 고지서명
	if($('#billname').val() == ''){
		swal({
			type : 'warning',
			text : '고지서명을 입력하세요',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인'
		})
		return false;
	}
	// 납부자호칭
	if($('#cusals').val() == ''){
		swal({
			type : 'warning',
			text : '납부자호칭을 입력하세요.',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인'
		})
		return false;
	}
	// 안내문구 billCont1
	if($('#billCont1').val() == ''){
		swal({
			type : 'warning',
			text : '안내문구를 입력하세요.',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인'
		});
		return false;
	}
	// 연락처
	if($('#telno').val() == ''){
		swal({
			type : 'warning',
			text : '연락처를 입력하세요.',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인'
		})
		return false;
	}
	// 수납기관명
	if($('#notichaname').val() == ''){
		swal({
			type : 'warning',
			text : '수납기관명을 입력하세요.',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인'
		})
		return false;
	}
	return true;
}

//로고 미리보기
function logoView(logo) {
	$('#logoImageContainer').empty();
    $.ajax({
		dataType: "html",
   	  	url: "/common/base64Images?fileTy=logo&fileName=" + $('#chaCd').val(),
   	  	success: function(data) {
	 		var imageString = "data:image/jpeg;base64,";
			var img = document.createElement('img'); // 이미지 객체 생성
			img.src = imageString + data; 
			if(data == ""){
			}else{
			document.getElementById('logoImageContainer').appendChild(img); // 이미지 동적 추가
			$(img).attr("width", "150");
			$(img).attr("height", "30");
			$(img).css({"margin" : "0 auto", "cursor": "pointer"});
			}
   		}
   	});
}

function byteCheck(size, id) {
	var cutstr ='';
	var limit = size;
	var text = $('#'+id).val();
	var textlength = text.length;
	var _byte = 0;

	for(var idx = 0; idx < text.length; idx++){

	    var oneChar = escape(text.charAt(idx));
		if(oneChar.length == 1){
			_byte ++;
		}else if(oneChar.indexOf("%u") != -1){ // 한글~
			_byte +=2;
		}else if(oneChar.indexOf("%") != -1) {
			_byte ++;
		}
		//길면 자르기
		if (_byte > limit) {
			break;
		}else{
			textlength = idx+1;
		}
	}

    var resultText = text.substring(0, textlength);
    if(resultText !== text) {
        $("#"+id+"").val(resultText);
    }

	if(_byte > limit){
		$("#"+id+"").parent().children().children().html(size);
	}else{
		$("#"+id+"").parent().children().children().html(_byte);
	}
}

function stringByteSize(str) {
   if (str == null || str.length == 0) {
     return 0;
   }

   var size = 0;

   for (var i = 0; i < str.length; i++) {
     size += charByteSize(str.charAt(i));
   }
   return size;
}

</script>
<input type="hidden" id="userName" name="userName" value="${userName}" />
<input type="hidden" id="chaCd" name="chaCd" value="${orgSess.chacd}" />
<input type="hidden" id="logoImg" name="logoImg" value="${logoImg}"/>
<div id="contents">
	<div id="damoa-breadcrumb">
	    <nav class="nav container">
	        <a class="nav-link" href="/org/notiMgmt/notiInq">고지서 조회/출력</a>
	        <a class="nav-link active" href="#">고지서설정</a>
	        <a class="nav-link" href="/org/notiMgmt/notiPrintReq">고지서 출력의뢰</a>
	    </nav>
	</div>
	<div class="container">
	    <div id="page-title">
	        <div id="breadcrumb-in-title-area" class="row align-items-center">
	            <div class="col-12 text-right">
	                <img src="/assets/imgs/common/icon-home.png" class="mr-2">
	                <span> > </span>
	                <span class="depth-1">고지관리</span>
	                <span> > </span>
	                <span class="depth-2 active">고지서설정</span>
	            </div>
	        </div>
	        <div class="row">
	            <div class="col-6">
	                <h2>고지서설정</h2>
	            </div>
	            <div class="col-6 text-right">
	            </div>
	        </div>
	    </div>
	</div>
	<div class="container">
	    <div id="page-description">
	        <div class="row">
	            <div class="col-12">
	                <p>출력고지서 및 E-MAIL고지서의 내용을 설정하는 화면입니다.</p>
	            </div>
	        </div>
	    </div>
	</div>
	<div class="container">
	    <table id="notice-view" class="table table-sm table-form table-primary">
	        <tbody>
	            <tr class="row no-gutters">
	                <th class="col-md-2 col-sm-4 col-4">안내명</th>
	                <td class="col-md-4 col-sm-8 col-8">
	                    <input type="text" id="billname" class="form-control font-size-8rem" value="${map.billName}" onchange="byteCheck(40,'billname');" onkeypress="byteCheck(40,'billname');" onkeyup="byteCheck(40,'billname');">
	                    <span class="textarea-byte-counter"><em id="billnameC">0</em>/40byte</span>
	                </td>
	                <th class="col-md-2 col-sm-4 col-4">로고</th>
	                <td class="col-md-4 col-sm-8 col-8">
	                	<div id="logoImageContainer"></div>
	                </td>
	            </tr>
	            <tr class="row no-gutters">
	                <th class="col-md-2 col-sm-4 col-4">납부자 호칭</th>
	                <td class="col-md-4 col-sm-8 col-8">
	                    <input type="text" id="cusals" class="form-control font-size-8rem" value="${map.cusAls}" onkeydown="byteCheck(40,'cusals');" onkeypress="byteCheck(40,'cusals');" onkeyup="byteCheck(40,'cusals');">
	                    <span class="textarea-byte-counter"><em id="cusAlsC">0</em>/40byte</span>
	                </td>
	                <th class="col-md-2 col-sm-4 col-4 text-left">안내문구선택</th>
	                <td class="col-md-4 col-sm-8 col-8">
	                    <select class="form-control font-size-8rem" id="selcont" onchange="fnSearch();">
	                        <option value="01" <c:if test="${map.billGubn == '01'}">selected</c:if>>안내문구 1</option>
	                        <option value="02" <c:if test="${map.billGubn == '02'}">selected</c:if>>안내문구 2</option>
	                        <option value="03" <c:if test="${map.billGubn == '03'}">selected</c:if>>안내문구 3</option>
	                    </select>
	                </td>
	            </tr>
	            <tr class="row no-gutters">
	                <th class="col-md-2 col-sm-4 col-4" style="flex-direction:column;">
	                    <div>안내문구</div>
	                </th>
	                <td colspan="3" class="col-md-10 col-sm-8 col-8">
	                    <textarea class="form-control textarea font-size-8rem" id="billCont1" rows="7" style="resize:none; border: 1px solid #d3d3d3;" name="billCont1">${map.billCont1}</textarea>
	                    <span class="textarea-byte-counter"><em id="billCont1C">0</em>/1600byte</span>
	                </td>
	            </tr>
				<tr class="row no-gutters">
	                <th class="col-md-2 col-sm-4 col-4" style="flex-direction:column;">
	                    <div>보조안내문구</div>
	                </th>
	                <td colspan="3" class="col-md-10 col-sm-8 col-8">
	                    <input type="text" id="billCont2" class="form-control font-size-8rem" value="${map.billCont2}" onkeydown="byteCheck(120,'billCont2');" onkeypress="byteCheck(120,'billCont2');" onkeyup="byteCheck(120,'billCont2');">
	                    <span class="textarea-byte-counter"><em id="billCont2C">0</em>/120byte</span>
	                </td>
	            </tr>
	            <tr class="row no-gutters">
	                <th class="col-md-2 col-sm-4 col-4">수납기관명</th>
	                <td class="col-md-4 col-sm-8 col-8">
	                    <input type="text" id="notichaname" class="form-control font-size-8rem" value="${map.notiChaName}" onkeydown="byteCheck(50,'notichaname');" onkeypress="byteCheck(50,'notichaname');" onkeyup="byteCheck(50,'notichaname');"/>
	                    <span class="textarea-byte-counter"><em id="notiChaNameC">0</em>/50byte</span>
	                </td>
	                <th class="col-md-2 col-sm-4 col-4">수납기관 연락처</th>
	                <td class="col-md-4 col-sm-8 col-8">
	                    <input type="text" id="telno" class="form-control font-size-8rem" value="${map.telNo}" onblur="validationPhoneNumber(this)"  maxlength="15">
	                </td>
	            </tr>
	            <tr class="row no-gutters">
	                <td class="col-5">
	                </td>
	                <td class="col-7 font-blue">
	                    <span class="ml-auto">가상계좌 수납관리 서비스 고객센터 : 02-786-8201</span>
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <ul class="unordered-list mt-1 mb-4 font-blue">
	        <li>* 로고 이미지 파일의 최소규격은 150px(가로) * 30px(세로)로 파일형식은 jpg 만 지원합니다.</li>
	        <li>* 로고 이미지는 마이페이지 메뉴 내 고지서설정에서 등록이 가능합니다.</li>
	        <li>* 고지서 출력용 샘플 화면으로 실제 출력화면과 다를 수 있습니다.</li>
	    </ul>
	</div>
	<div class="container list-button-group-bottom text-center">
	    <input type="button" class="btn btn-primary btn-wide" onclick="fnSave()" value="저장" />
	</div>

	<div class="container">
		<div id="quick-instruction" class="foldable-box">
			<div class="row foldable-box-header">
				<div class="col-8">
					<img src="/assets/imgs/common/icon-notice.png"> 알려드립니다.
				</div>
				<div class="col-4 text-right">
					<img src="/assets/imgs/common/btn-arrow-notice.png" class="fold-status">
				</div>
			</div>
			<div class="row foldable-box-body">
				<div class="col">
					<h6>■ 로고 및 고지서 안내문구 설정 방법</h6>
					<ul>
						<li>로고 : 최소규격 150px(가로) * 30px(세로)의 JPG 형식 파일만 등록 가능</li>
						<li>납부자 호칭 : 납부자 호칭 앞의 고객명은 자동으로 입력되어 출력</li>
						<li>안내문구 선택 : [마이페이지>고지문구 설정] 메뉴에서 설정한 안내문구 중 선택</li>
						<li>안내문구 :  안내문구 선택을 통해 적용된 문구를 수정하거나 직접 작성 가능</li>
						<li>보조안내문구 : 청구서 상세내역 테이블 우측 상단에 표시</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>

<%-- footer --%>
<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/>
<%-- sms 고지팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/sms-notification.jsp" flush="false"/>
<%-- E-mail 고지 등록 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/email-notification.jsp" flush="false"/>
<%-- 로고 미리보기 --%>
<jsp:include page="/WEB-INF/views/include/modal/logo-view.jsp" flush="false" />


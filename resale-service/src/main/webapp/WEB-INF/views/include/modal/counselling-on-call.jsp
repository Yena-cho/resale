<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" 	   uri="http://www.springframework.org/security/tags" %>

<s:authorize access="isAnonymous()">
	<c:set var="userId" value="guest"/>
</s:authorize>
<s:authorize access="isAuthenticated()">
	<s:authentication property="principal.username" var="userId"/>
</s:authorize>

<script type="text/javascript">

// 구분선택
function fn_callGubun(idx) {
	var gubun;
	if(idx == 1) {	// 기관
		gubun = "가맹점";
		$('#loginid').attr('disabled', false);
	} else {		// 납부자
		gubun = "납부자";
		$('#loginid').attr('disabled', true);
		$("#loginid").val('');
	}
	$('#gubun').val(gubun);
	$('#title').val('[상담예약] ' + gubun);
}

// 전화상담예약
function fn_telBooking() {

	if(!$("#inlineCheckbox1").is(":checked") && !$("#inlineCheckbox2").is(":checked")) {
		swal({
	           type: 'info',
	           text: "구분을 선택 해주세요!",
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
			});
			return;
	}
	
	if($("#inlineCheckbox2").is(":checked") == true && !$("#loginid").val()) {
		swal({
	           type: 'info',
	           text: "기관코드를 입력해주세요!",
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
			}).then(function() {
				$("#orgCd").focus();
	        });
			return;
	}
	if(!$("#regName").val()) {
		swal({
           type: 'info',
           text: "작성자를 입력해주세요!",
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
		}).then(function() {
			$("#regName").focus();
        });
		return;
	}

	var tel = $("#tel1").val() + "-" + $("#tel2").val() + "-" + $("#tel3").val();
//		var tel    = $("#tel1 option:selected").val() + "-" + $("#tel2").val() + "-" + $("#tel3").val();
		var regExp = /(01[0|1|6|9|7])[-](\d{3}|\d{4})[-](\d{4}$)/g;
		var result = regExp.exec(tel);

	if(!result) {
		swal({
           type: 'info',
           text: "핸드폰번호 형식에 맞게 다시 입력해 주세요!",
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
		}).then(function() {
			$("#tel1").focus();
        });
		return;
	}
	
	if($("#agree").is(":checked") == false) {
		swal({
           type: 'info',
           text: "개인정보 수집 및 이용에 대해 동의해 주세요!",
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
		});
		return;
	}
	
	var url = "/telResInsert";
	var param = {
			bbs : '9',
			id : '${userId}',
			writer : $("#regName").val(),
			title : $('#title').val(),
			contents : "[구분] " + $('#gubun').val() + "\r\n" + "\r\n" + "[전화번호] " + tel + "\r\n" + "\r\n",
			code : $("#loginid").val(),
			data1 : tel,
			data2 : $('#reason option:selected').val(),
			data3 : $('#reason option:selected').text(),
			data4 : '1'
	};
	$.ajax({
		type: "post",
		url: url,
		contentType : "application/json; charset=UTF-8",
		data : JSON.stringify(param),
		success: function(){
			swal({
	           type: 'success',
	           text: '전화 상담이 예약되었습니다.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       }).then(function (result){ 
			  if (result.value) {  
				  	$("#make-reservation").fadeOut("fast");
					$(".modal-backdrop").remove();
					location.href = "/";
				  } 
				});
		},
		error:function(data){
			swal({
	           type: 'error',
	           text: '전화 상담이 실패하였습니다.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       })
		}
	});
}

//취소시 값들 초기화
function callCnacell() {
	$("#inlineCheckbox1").prop("checked", false);
	$("#inlineCheckbox2").prop("checked", false);
	$("#loginid").val("");
	$("#regName").val("");
//	$("#tel1").find("option:eq(0)").prop("selected", true);
    $("#tel1").val("");
	$("#tel2").val("");
	$("#tel3").val("");
	//$("#reason").prop("selected",false);
	$("#reason").find("option:eq(0)").prop("selected", true);
	$("#agree").prop("checked", false);
}


//작성자에 숫자입력 불가 처리
function onlyStr() {
	$("#regName").val($("#regName").val().replace(/[0-9]/g,""));
}

//기관코드 숫자만 입력가능 
function removeChar(event) {
    event = event || window.event;
    var keyID = (event.which) ? event.which : event.keyCode;
    if ( keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39 )
        return;
    else
        event.target.value = event.target.value.replace(/[^0-9]/g, "");
}
</script>

<jsp:include page="/WEB-INF/views/include/popHeader.jsp" />

<input type="hidden" id="title"   name="title">
<input type="hidden" id="content" name="content">
<input type="hidden" id="gubun"   name="gubun">

<div class="modal fade" id="make-reservation" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">전화상담예약</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">

                <div id="page-description" class="container-fluid">
                    <div class="row">
                        <div class="col-12">
                            <p>
                                가상계좌 수납관리 서비스는 장시간 대기 없이 상담을 받으실 수 있도록 전화상담예약 서비스를 지원합니다.<br/>
                                고객센터 운영시간 : 평일 09:00-12:00, 13:00-17:00 (토요일 및 공휴일 휴무)<br/>
                                통화량이 많을 경우, 당일 회신이 어려울 수 있습니다.
                            </p>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="search-box">
                        <div class="row">
                            <div class="col col-md-3 col-4">
                                <label class="search-box-label">
                                    구분
                                </label>
                            </div>
                            <div class="col col-md-9 col-8">
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" id="inlineCheckbox1" value="1" name="gubunCd" onclick="fn_callGubun(2);">
                                    <label class="form-check-label" for="inlineCheckbox1"><span></span>신규</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" id="inlineCheckbox2" value="2" name="gubunCd" onclick="fn_callGubun(1);">
                                    <label class="form-check-label" for="inlineCheckbox2"><span></span>이용기관</label>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col col-md-3 col-4">
                                <label class="search-box-label" style="max-width: 100%;">
                                    기관코드
                                </label>
                            </div>
                            <div class="col col-md-9 col-8">
                                <input type="text" class="form-control" id="loginid" name="loginid" placeholder="* 이용기관 구분 선택 시만 입력" maxlength="8" onkeyup="removeChar(event)">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col col-md-3 col-4">
                                <label class="search-box-label">
                                    작성자
                                </label>
                            </div>
                            <div class="col col-md-9 col-8">
                                <input type="text" class="form-control" placeholder="" name="regName" id="regName" onkeyup="onlyStr();">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col col-md-3 col-4">
                                <label class="search-box-label">
                                    연락처
                                </label>
                            </div>
                            <div class="col col-md-9 col-8 form-inline">
								<%--<select class="form-control" id="tel1">--%>
									<%--<option value="010">010</option>--%>
									<%--<option value="011">011</option>--%>
									<%--<option value="017">017</option>--%>
								<%--</select>--%>
                                <input type="text" class="form-control d-flex" name="tel1" id="tel1" maxlength="3" pattern="[0-9]*" onkeyup="removeChar(event)">
                                <span class="ml-1 mr-1"> - </span>
                                <input type="text" class="form-control d-flex" name="tel2" id="tel2" maxlength="4" pattern="[0-9]*" onkeyup="removeChar(event)">
                                <span class="ml-1 mr-1"> - </span>
                                <input type="text" class="form-control d-flex" name="tel3" id="tel3" maxlength="4" pattern="[0-9]*" onkeyup="removeChar(event)">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col col-md-3 col-4">
                                <label class="search-box-label">
                                    상담예약 사유
                                </label>
                            </div>
                            <div class="col col-md-9 col-8 form-inline">
                                <select class="form-control" name="reason" id="reason" style="max-width: 100%; width: 100%; margin-right: 0;">
                                    <option value="01">사이트 이용</option>
                                    <option value="02">증명서 발급</option>
                                    <option value="03">결제</option>
                                    <option value="04">오류 및 불편사항</option>
                                    <option value="05">기타</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="container">
                   <div class="row">
                       <div class="col-12 mb-3">
                           <div class="form-control" style="padding: 10px 15px; height: 150px; border: 1px solid #d3d3d3; overflow: hidden; overflow-y: auto;">
                               <div class="policy" style="margin: 0;">
                                   <h6 style="font-weight: 600;">개인(신용)정보의 수집·이용</h6>
                                   <p>「개인정보보호법」 제 15조 및 제 22조, 「신용정보의 이용 및 보호에 관한 법률」 제 32조 및 제 33조의 규정에 따라 당사가 아래와 같은 내용으로 본인의 개인(신용)정보를 수집,이용하는데 동의합니다.</p>

                                   <h6 style="font-weight: 600;">수집, 이용 목적</h6>
                                   <p>민원처리부서 및 실무부서에 상담 및 민원 신청사항의 확인 및 처리 목적</p>

                                   <h6 style="font-weight: 600;">수집, 이용할 개인(신용)정보의 내용</h6>
                                   <p>로그인ID, 성함, 핸드폰번호</p>

                                   <h6 style="font-weight: 600;">보유, 이용기간</h6>
                                   <p>정보를 제공받은 날로부터 개인(신용)정보의 수집, 이용 목적을 달성할 때 까지</p>
                               </div>
                           </div>
                       </div>
                   </div>
                   <div class="row mb-4">
                       <div class="col-12 text-right">
                           <div class="form-check form-check-inline">
                               <input class="form-check-input" type="checkbox" name="agree" id="agree" >
                               <label class="form-check-label" for="agree"><span></span> 개인정보 수집 및 이용에 대한 동의</label>
                           </div>
                       </div>
                   </div>
               </div>
                <div class="container">
                    <div class="row mb-4">
                        <div class="col-12 text-center">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="callCnacell()">취소</button>
                            <button type="button" class="btn btn-primary" onclick="fn_telBooking();">등록</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

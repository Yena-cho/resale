<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false" />
<script>

// GNB의 Depth 1을 활성화 시켜주는 변수
var firstDepth = "nav-link-35";
var secondDepth = "sub-02";

$(document).ready(function(){

	if('N' == $("#rcpReqYn").val()){
		$(".cashRcpDP").hide();
	}

	if (isMobile()) {
        $("input[type=text]").attr("readonly",true);
        $("#btnCardSave").hide();

        $("input[type=radio]").each(function(){
            $(this).siblings("label").children().hide();

            if($(this).is(":checked") == false){
                $(this).closest('div').css("display","none");
            }
        })
    } else {
        $("input[type=text]").attr("readonly",false);
    }
});


function isMobile() {
    return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}

function cashRcpDP(val){
	if(val == 'Y'){
		$(".cashRcpDP").show();
	}else{
		$(".cashRcpDP").hide();
	}
}

// 고객구분 저장버튼 클릭시
function saveServiceConfig(){

	var _byte1 = byteCheck($('#cusGubn1').val());
	var _byte2 = byteCheck($('#cusGubn2').val());
	var _byte3 = byteCheck($('#cusGubn3').val());
	var _byte4 = byteCheck($('#cusGubn4').val());

	$('#cusGubn1').val(basicEscape($('#cusGubn1').val().trim()));
	$('#cusGubn2').val(basicEscape($('#cusGubn2').val().trim()));
	$('#cusGubn3').val(basicEscape($('#cusGubn3').val().trim()));
	$('#cusGubn4').val(basicEscape($('#cusGubn4').val().trim()));

	if(_byte1 > 20 || _byte2 > 20 || _byte3 > 20 || _byte4 > 20){
		swal({
           type: 'info',
           text: '고객구분값이 너무 깁니다.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
        });
		return;
	}

	var url = "/org/myPage/updateServiceConfig";
	var param = {
			chaCd    	: $('#chaCd').val(),
			cusGubn1    : $('#cusGubn1').val(),
			cusGubn2    : $('#cusGubn2').val(),
			cusGubn3    : $('#cusGubn3').val(),
			cusGubn4    : $('#cusGubn4').val(),
			cusGubnYn1	: $('input[type=radio][name=cusGubnYn1]:checked').val(),
			cusGubnYn2	: $('input[type=radio][name=cusGubnYn2]:checked').val(),
			cusGubnYn3	: $('input[type=radio][name=cusGubnYn3]:checked').val(),
			cusGubnYn4	: $('input[type=radio][name=cusGubnYn4]:checked').val(),
	};
	$.ajax({
		type: "post",
		url: url,
		data: JSON.stringify(param),
		contentType : "application/json; charset=utf-8",
		success: function(data){
			swal({
		           type: 'success',
		           text: '설정 내용이 저장되었습니다.',
		           confirmButtonColor: '#3085d6',
		           confirmButtonText: '확인'
		       }).then(function(result) {
				  if (result.value) {
					  document.location.href="/org/myPage/serviceConfig";
				  }
			   });
		},
		error:function(data){
			swal({
	           type: 'error',
	           text: '설정 내용 저장을 실패하였습니다.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       })
		}
	});

}

//글자수 체크
function byteCheck(str){
	var _byte = 0;
	for(var idx = 0; idx < str.length; idx++){
		var oneChar = escape(str.charAt(idx));
		if(oneChar.length == 1){
			_byte ++;
		}else if(oneChar.indexOf("%u") != -1){ // 한글~
			_byte +=2;
		}else if(oneChar.indexOf("%") != -1) {
			_byte ++;
		}
	}
	return _byte;
}

</script>

<input type="hidden" name="chaCd" id="chaCd" value="${map.chaCd}">
<div id="contents">
    <div id="damoa-breadcrumb">
        <nav class="nav container">
        	<a class="nav-link" href="#">기본정보관리</a>
        	<a class="nav-link active" href="#">서비스설정</a>
        	<a class="nav-link" href="#">고지설정</a>
            <a class="nav-link" href="#">이용료조회</a>
            <a class="nav-link" href="#">수납현황분석</a>
        </nav>
    </div>
    <div class="container">
        <div id="page-title">
            <div id="breadcrumb-in-title-area" class="row align-items-center">
                <div class="col-12 text-right">
                    <img src="/assets/imgs/common/icon-home.png" class="mr-2">
                    <span> > </span>
                    <span class="depth-1">마이페이지</span>
                    <span> > </span>
                    <span class="depth-2 active">서비스설정</span>
                </div>
            </div>
            <div class="row">
                <div class="col-6">
                    <h2>서비스설정</h2>
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
                    <p>고객구분, 가상계좌 수납 및 부가서비스 사용여부를 설정하는 화면입니다.</p>
                </div>
            </div>
        </div>
    </div>

	<div class="container tab-content mt-3">
	    <div id="payer-classification" class="">
			<div class="row">
				<div class="col-6">
					<h5>고객구분</h5>
				</div>
				<div class="col-6"></div>
			</div>

	        <div class="row">
	            <div class="col">
	                <table class="table table-primary table-form">
	                    <tbody class="container-fluid">
	                        <tr class="row no-gutters">
	                            <th class="col-md-2 col-sm-4 col-5">고객구분1</th>
	                            <td class="col-md-4 col-sm-8 col-7">
	                                <input type="text" class="form-control" id="cusGubn1" name="cusGubn1" value="${map.result.cusGubn1}">
	                            </td>
	                            <th class="col-md-2 col-sm-4 col-5">고지서 출력</th>
	                            <td class="col-md-4 col-sm-8 col-7 form-inline">
	                                <div class="form-check form-check-inline">
	                                    <input class="form-check-input" type="radio" id="cusGubnYn1_Y" name="cusGubnYn1" <c:if test="${map.result.cusGubnYn1 eq 'Y'}">checked</c:if> value="Y">
										<label for="cusGubnYn1_Y"><span class="mr-2"></span>표기</label>
	                                </div>
	                                <div class="form-check form-check-inline">
	                                    <input class="form-check-input" type="radio" id="cusGubnYn1_N" name="cusGubnYn1" <c:if test="${map.result.cusGubnYn1 eq 'N'}">checked</c:if> value="N">
										<label for="cusGubnYn1_N"><span class="mr-2"></span>미표기</label>
	                                </div>
	                            </td>
	                        </tr>
	                        <tr class="row no-gutters">
	                            <th class="col-md-2 col-sm-4 col-5">고객구분2</th>
	                            <td class="col-md-4 col-sm-8 col-7">
	                                <input type="text" class="form-control" id="cusGubn2" name="cusGubn2" value="${map.result.cusGubn2}">
	                            </td>
	                            <th class="col-md-2 col-sm-4 col-5">고지서 출력</th>
	                            <td class="col-md-4 col-sm-8 col-7 form-inline">
	                                <div class="form-check form-check-inline">
	                                    <input class="form-check-input" type="radio" id="cusGubnYn2_Y" name="cusGubnYn2" <c:if test="${map.result.cusGubnYn2 eq 'Y'}">checked</c:if> value="Y">
										<label for="cusGubnYn2_Y"><span class="mr-2"></span>표기</label>
	                                </div>
	                                <div class="form-check form-check-inline">
	                                    <input class="form-check-input" type="radio" id="cusGubnYn2_N" name="cusGubnYn2" <c:if test="${map.result.cusGubnYn2 eq 'N'}">checked</c:if> value="N">
										<label for="cusGubnYn2_N"><span class="mr-2"></span>미표기</label>
	                                </div>
	                            </td>
	                        </tr>
	                        <tr class="row no-gutters">
	                            <th class="col-md-2 col-sm-4 col-5">고객구분3</th>
	                            <td class="col-md-4 col-sm-8 col-7">
	                                <input type="text" class="form-control" id="cusGubn3" name="cusGubn3" value="${map.result.cusGubn3}">
	                            </td>
	                            <th class="col-md-2 col-sm-4 col-5">고지서 출력</th>
	                            <td class="col-md-4 col-sm-8 col-7 form-inline">
	                                <div class="form-check form-check-inline">
	                                    <input class="form-check-input" type="radio" id="cusGubnYn3_Y" name="cusGubnYn3" <c:if test="${map.result.cusGubnYn3 eq 'Y'}">checked</c:if> value="Y">
										<label for="cusGubnYn3_Y"><span class="mr-2"></span>표기</label>
	                                </div>
	                                <div class="form-check form-check-inline">
	                                    <input class="form-check-input" type="radio" id="cusGubnYn3_N" name="cusGubnYn3" <c:if test="${map.result.cusGubnYn3 eq 'N'}">checked</c:if> value="N">
										<label for="cusGubnYn3_N"><span class="mr-2"></span>미표기</label>
	                                </div>
	                            </td>
	                        </tr>
	                        <tr class="row no-gutters">
	                            <th class="col-md-2 col-sm-4 col-5">고객구분4</th>
	                            <td class="col-md-4 col-sm-8 col-7">
	                                <input type="text" class="form-control" id="cusGubn4" name="cusGubn4" value="${map.result.cusGubn4}">
	                            </td>
	                            <th class="col-md-2 col-sm-4 col-5">고지서 출력</th>
	                            <td class="col-md-4 col-sm-8 col-7 form-inline">
	                                <div class="form-check form-check-inline">
	                                    <input class="form-check-input" type="radio" id="cusGubnYn4_Y" name="cusGubnYn4" <c:if test="${map.result.cusGubnYn4 eq 'Y'}">checked</c:if> value="Y">
										<label for="cusGubnYn4_Y"><span class="mr-2"></span>표기</label>
	                                </div>
	                                <div class="form-check form-check-inline">
	                                    <input class="form-check-input" type="radio" id="cusGubnYn4_N" name="cusGubnYn4" <c:if test="${map.result.cusGubnYn4 eq 'N'}">checked</c:if> value="N">
										<label for="cusGubnYn4_N"><span class="mr-2"></span>미표기</label>
	                                </div>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	            </div>
	        </div>

	        <div class="row mt-3 mb-5">
	            <div class="col text-center hidden-on-mobile">
	            </div>
	        </div>
	    </div>

	    <div id="virtual-account" class="">
			<div class="row">
				<div class="col-6">
					<h5>가상계좌수납</h5>
				</div>
				<div class="col-6"></div>
			</div>

	        <div class="row">
	            <div class="col">
	                <table class="table table-primary table-form">
	                    <tbody class="container-fluid">
	                        <tr class="row no-gutters">
	                            <th class="col-md-2 col-sm-4 col-6">납부금액 체크</th>
	                            <td class="col-md-10 col-sm-8 col-6 form-inline">
									<c:if test="${map.result.amtChkTy eq 'Y'}">
										<div class="form-check form-check-inline">
											<input class="form-check-input" type="radio" id="amtChkTy_Y" name="amtChkTy" value="Y" checked="checked"><span class="mr-3">사용</span>
										</div>
									</c:if>
									<c:if test="${map.result.amtChkTy eq 'N'}">
										<div class="form-check form-check-inline">
											<input class="form-check-input" type="radio" id="amtChkTy_N" name="amtChkTy" value="N" checked="checked"><span class="mr-3">미사용</span>
										</div>
									</c:if>
	                                <div class="guide-mention hidden-on-mobile">
										* 사용시 금액 청구금액과 일치하는 경우에만 입금 가능 (설정 변경을 원하시는 경우 가상계좌 수납관리 서비스 고객센터로 신청)
	                                </div>
	                            </td>
	                        </tr>
	                        <tr class="row no-gutters">
	                            <th class="col-md-2 col-sm-4 col-6">납부기한 체크</th>
	                            <td class="col-md-10 col-sm-8 col-6 form-inline">
									<c:if test="${map.result.rcpDueChk eq 'Y'}">
										<div class="form-check form-check-inline">
											<input class="form-check-input" type="radio" id="rcpDueChk_Y" name="rcpDueChk" value="Y" checked="checked"><span class="mr-3">사용</span>
										</div>
									</c:if>
									<c:if test="${map.result.rcpDueChk eq 'N'}">
										<div class="form-check form-check-inline">
											<input class="form-check-input" type="radio" id="rcpDueChk_N" name="amtChkTy" value="N" checked="checked"><span class="mr-3">미사용</span>
										</div>
									</c:if>
	                                <div class="guide-mention hidden-on-mobile">
										* 사용시 납부기한 이후에는 해당 청구 건에 대한 입금불가 (납부금액 체크 미사용의 경우, 납부기한 체크 사용여부와 관계없이 입금 가능상태로 변경)
	                                </div>
	                            </td>
	                        </tr>
							<tr class="row no-gutters">
								<th class="col-md-2 col-sm-4 col-6">부분납 사용여부</th>
								<td class="col-md-10 col-sm-8 col-6 form-inline">
									<c:if test="${map.result.partialPayment eq 'Y'}">
										<div class="form-check form-check-inline">
											<span class="mr-3">사용</span>
										</div>
									</c:if>
									<c:if test="${map.result.partialPayment eq 'N'}">
										<div class="form-check form-check-inline">
											<span class="mr-3">미사용</span>
										</div>
									</c:if>
									<div class="guide-mention hidden-on-mobile">
										* 사용시 청구항목별 부분 입금 가능 (사용을 원하실 경우 가상계좌 수납관리 서비스 고객센터로 신청)
									</div>
								</td>
							</tr>
	                    </tbody>
	                </table>
	            </div>
	        </div>
	        <div class="row mt-3 mb-5">
	            <div class="col text-center hidden-on-mobile">
	            </div>
	        </div>
	    </div>

	    <div id="issue-receipt" class="">
			<div class="row">
				<div class="col-6">
					<h5>현금영수증발행</h5>
				</div>
				<div class="col-6"></div>
			</div>

	        <div class="row">
	            <div class="col">
	                <table class="table table-primary table-form">
	                    <tbody class="container-fluid">
	                        <tr class="row no-gutters">
	                            <th class="col-md-2 col-sm-4 col-6">현금영수증발행</th>
	                            <td class="col-md-10 col-sm-8 col-6 form-inline">
									<input type="hidden" value="${map.result.rcpReqYn}" id="rcpReqYn">
										<c:if test="${map.result.rcpReqYn eq 'Y'}">
										<div class="form-check form-check-inline">
											<span class="mr-3">사용</span>
										</div>
										</c:if>
										<c:if test="${map.result.rcpReqYn eq 'N'}">
										<div class="form-check form-check-inline">
											<span class="mr-3">미사용</span>
										</div>
										</c:if>
										<div class="guide-mention hidden-on-mobile">
											* 사용시 현금영수증 발행 가능 (사용을 원하실 경우 가상계좌 수납관리 서비스 고객센터로 신청)
										</div>
	                            </td>
	                        </tr>
							<tr class="row no-gutters">
								<th class="col-md-2 col-sm-4 col-6">의무발행업체 여부</th>
								<td class="col-md-10 col-sm-8 col-6 form-inline">
									<input type="hidden" value="${map.result.mandRcpYn}" id="mandRcpYn">
									<c:if test="${map.result.mandRcpYn eq 'Y'}">
										<div class="form-check form-check-inline">
											<span class="mr-3">사용</span>
										</div>
									</c:if>
									<c:if test="${map.result.mandRcpYn eq 'N'}">
										<div class="form-check form-check-inline">
											<span class="mr-3">미사용</span>
										</div>
									</c:if>
									<div class="guide-mention hidden-on-mobile">
										* 현금영수증 자동발행 사용시에만 발행번호 미입력 고객은 자진발행 지정번호로 자동발행 처리
									</div>
								</td>
							</tr>
	                        <tr class="row no-gutters cashRcpDP">
	                            <th class="col-md-2 col-sm-4 col-6">현금영수증 자동발행</th>
	                            <td class="col-md-10 col-sm-8 col-6 form-inline">
	                                <div class="form-check form-check-inline">
										<c:if test="${map.result.rcpReqTy eq 'A'}">
											<div class="form-check form-check-inline">
												<span class="mr-3">사용</span>
											</div>
										</c:if>
										<c:if test="${map.result.rcpReqTy eq 'M'}">
											<div class="form-check form-check-inline">
												<span class="mr-3">미사용</span>
											</div>
										</c:if>
	                                <div class="guide-mention hidden-on-mobile">
	                                    * 의무발행업종 사업자가 현금영수증 미 발행 시, 과태료 청구가 될 수 있으니 이용 상 유의 바람
	                                </div>
	                            </td>
	                        </tr>
							<tr class="row no-gutters cashRcpDP">
								<th class="col-md-2 col-sm-4 col-6">수기수납 현금영수증<br> 발행여부</th>
								<td class="col-md-10 col-sm-8 col-6 form-inline">
									<c:if test="${map.result.rcpReqSveTy eq '01'}">
										<div class="form-check form-check-inline">
											<span class="mr-3">사용</span>
										</div>
									</c:if>
									<c:if test="${map.result.rcpReqSveTy eq '00'}">
										<div class="form-check form-check-inline">
											<span class="mr-3">미사용</span>
										</div>
									</c:if>
									<div class="guide-mention hidden-on-mobile">
										* 현금영수증 발급 사용 시에만 수기수납 현금영수증 발급에 대한 설정 가능
									</div>
								</td>
							</tr>
							<tr class="row no-gutters cashRcpDP">
								<th class="col-md-2 col-sm-4 col-6">수기수납 현금영수증<br> 자동발행</th>
								<td class="col-md-10 col-sm-8 col-6 form-inline">
									<c:if test="${map.result.mnlRcpReqTy eq 'A'}">
										<div class="form-check form-check-inline">
											<span class="mr-3">사용</span>
										</div>
									</c:if>
									<c:if test="${map.result.mnlRcpReqTy eq 'M'}">
										<div class="form-check form-check-inline">
											<span class="mr-3">미사용</span>
										</div>
									</c:if>
									<div class="guide-mention hidden-on-mobile">
										* 자동 발급 사용 시 수기수납 입금을 등록한 날짜로 현금영수증 발급
									</div>
								</td>
							</tr>
	                        <tr class="row no-gutters cashRcpDP">
	                            <th class="col-md-2 col-sm-4 col-6">과세대상업체 여부</th>
	                            <td class="col-md-10 col-sm-8 col-6 form-inline">
									<c:if test="${map.result.noTaxYn eq 'Y'}">
										<div class="form-check form-check-inline">
											<span class="mr-3">과세</span>
										</div>
									</c:if>
									<c:if test="${map.result.noTaxYn eq 'N'}">
										<div class="form-check form-check-inline">
											<span class="mr-3">면세</span>
										</div>
									</c:if>
	                                <div class="guide-mention hidden-on-mobile">
	                                    * 현금영수증 발행 시 수납액 중 부가가치세를 자동으로 계산 및 분할하여 국세청에 신고 처리
	                                </div>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	            </div>
	        </div>
	        <div class="row mt-3 mb-5">
	            <div class="col text-center hidden-on-mobile">
	            </div>
	        </div>
	    </div>

	    <div id="online-credit-card-payment" class="">
			<%--<div class="row">
				<div class="col-6">
					<h5>온라인 카드결제</h5>
				</div>
				<div class="col-6"></div>
			</div>

	        <div class="row">
	            <div class="col">
	                <table class="table table-primary table-form">
	                    <tbody class="container-fluid">
	                        <tr class="row no-gutters">
	                            <th class="col-md-2 col-sm-4 col-6 mobile-text-left">온라인 카드결제<br>이용여부</th>
	                            <td class="col-md-10 col-sm-8 col-6 form-inline">
									<c:if test="${map.result.usePgYn eq 'Y'}">
										<div class="form-check form-check-inline">
											<input class="form-check-input" type="hidden" id="usePgYn_Y" name="usePgYn"  value="Y" ><span class="mr-3">사용</span>
										</div>
									</c:if>
									<c:if test="${map.result.usePgYn eq 'N'}">
										<div class="form-check form-check-inline">
											<input class="form-check-input" type="hidden" id="usePgYn_N" name="usePgYn"  value="N"><span class="mr-3">미사용</span>
										</div>
									</c:if>
									<span class="guide-mention hidden-on-mobile">* 온라인 신용카드 결제 이용/해제를 원하시는 경우 가상계좌 수납관리 서비스 고객센터로 신청 필요</span>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	            </div>
	        </div>--%>
	        <div class="row mt-3 mb-5">
	            <div class="col text-center hidden-on-mobile">
	                <button class="btn btn-primary btn-wide" id="btnCardSave" onclick="saveServiceConfig();">저장</button>
	            </div>
	        </div>
	    </div>
	</div>

	<div class="container">
	    <div id="quick-instruction" class="foldable-box">
	        <div class="row foldable-box-header">
	            <div class="col-8">
	                <img src="/assets/imgs/common/icon-notice.png">
	                알려드립니다.
	            </div>
	            <div class="col-4 text-right">
	                <img src="/assets/imgs/common/btn-arrow-notice.png" class="fold-status">
	            </div>
	        </div>
	        <div class="row foldable-box-body">
	            <div class="col">
	                <h6>■ 고객구분</h6>
	                <ul>
	                    <li>고객별 그룹을 지정하는 명칭으로 0~4개까지 선택 가능(ex. 반, 학년, 부서, 지역 등)</li>
	                    <li>고객등록 시 지정한 고객구분 명칭으로 표시</li>
						<li>고지서 출력 시 고객구분 명칭의 표기 여부 선택</li>
	                </ul>
					<h6>■ 가상계좌 수납</h6>
					<ul>
						<li>납부금액체크 : 납부금액 체크 기능 미 사용 시, 일부납/부분납 건의 수납이 가능</li>
						<li>납부기한체크 : 납부기한 체크 기능 미 사용 시, 설정된 입금가능 기간과 관계없이 언제든 수납이 가능</li>
						<li class="depth-2 text-danger">※ 납부금액 또는 기한 체크 미사용으로 설정하실 경우, 납부 건의 재수납율 및 환불 건이 증가될 수 있습니다.</li>
					</ul>
					<h6>■ 현금영수증 발행</h6>
					<ul>
						<li>현금영수증 자동 발행 : 가상계좌 수납 및 수기수납 등록 건에 대해 자동으로 현금영수증 발행 처리 (납부자의 현금영수증 발행 정보가 등록되어 있는 건에 한함)</li>
						<li>현금영수증 수동 발행 : 가상계좌 수납 및 수기수납 등록 건에 대해 현금영수증 메뉴에서 수동으로 발행 가능</li>
						<li>과세대상 여부 : 과세 대상 사용 시, 현금영수증 발행 시 부가세 10% 신고 처리</li>
						<li class="depth-2 text-danger">* 수기수납의 경우 선택한 수납일자가 아닌 등록일자로 현금영수증 발행 처리되며, 수기수납 취소 또는 환불작업 시 기발행된 현금영수증은 자동으로 취소되지 않으니, 확인 후 발행된 내역 취소 바랍니다.</li>
					</ul>

					<%--<h6>■ 온라인 카드결제</h6>
	                <ul>
	                    <li>온라인 카드결제 별도 신청 및 승인 절차를 통해 가능하며 고객센터로 문의 필요</li>
						<li>온라인 카드결제 신청한 경우도 이용 여부 '미사용'으로 변경 시, 납부고객은 카드결제 불가 상태로 변경</li>
	                </ul>--%>
	            </div>
	        </div>
	    </div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" />

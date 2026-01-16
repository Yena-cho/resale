<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false" />
<%-- <script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script> --%>

<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>

<script>

// GNB의 Depth 1을 활성화 시켜주는 변수
var firstDepth = "nav-link-35";
var secondDepth = "sub-01";

$(document).ready(function(){
	//한글만 입력 가능하도록 처리 하는 부분
	$("#chrName").keyup(function (event) {
	    var regexp = /[^(가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z)]/gi;

	    v = $(this).val();
	    if (regexp.test(v)) {
	        $(this).val(v.replace(regexp, ''));
	    }
	});
	
    // email 입력 폼 컨트롤
    $(".email-contact .provider-set").on("change",function(){
        var provider = $(this).val();

        if(provider == ""){
            $(".email-provider").attr("disabled", false);
        } else {
            $(".email-provider").attr("disabled", true).val(provider);
        }
    });
        
    if('${map.result.adjAccYn}' == 'N') { // 다계좌 사용여부 = "N"
    	$("#accInfo").hide();
    	$("#accInfoList").hide();
    }
    
    $('.provider-set').find("option").each(function(i) {
    	if(this.value == $('#chrMail2').val()){
    		$('#chrMail2').attr("disabled", true);
    	}
    });

    
});

function openZipSearch(){
    new daum.Postcode({

        oncomplete: function(data) {
			$('[name=chaZipCode]').val(data.zonecode); // 우편번호 (5자리)
			$('[name=chaAddress1]').val(data.address);
			$('[name=chaAddress2]').val(data.buildingName);
        }
    }).open();
}

function saveOrgInfo(){
	var chrHp;
	var chrTelNo;
	var chrMail;
	var chrName = $('#chrName').val();
	var regExpTel = /^\d{2,3}-\d{3,4}-\d{4}$/;
	var regExpHp = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/;
	var chrTel = $("#chrTelNo1").val() + "-" + $("#chrTelNo2").val() + "-" + $("#chrTelNo3").val();
	var chrHp = $("#chrHp1").val() + "-" + $("#chrHp2").val() + "-" + $("#chrHp3").val();
	
	if(!$('#chrName').val()) { // 담당자명
		swal({
           type: 'info',
           text: '담당자명을 입력해주세요.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
        });
		return;
	}
	
	var _byte = byteCheck(chrName);
	
	if(_byte > 15){
		swal({
           type: 'info',
           text: '담당자명 글자수를 초과하였습니다.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
        });
		return;
	}
	
	if(!chrName.match(/^[가-힣]+$/)) {
		swal({
	           type: 'info',
	           text: '담당자명을 확인해주세요.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	        });
			return;	
	}
	
	if(!$('#chrTelNo1').val() || !$('#chrTelNo2').val() || !$('#chrTelNo3').val()) { // 전화번호
		swal({
           type: 'info',
           text: '전화번호를 입력해주세요.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
        });
		return;
	}
	
	if(!regExpTel.test(chrTel)){
		swal({
           type: 'info',
           text: '전화번호를 확인해주세요.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
        });
		return;
	}
	
	if(!$('#chrMail1').val() || !$('#chrMail2').val()) { // 이메일
		swal({
           type: 'info',
           text: 'E-MAIL을 입력해주세요.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
        });
		return;
	}
	
	if(!$('#chrHp1').val() || !$('#chrHp2').val() || !$('#chrHp3').val()) { // 연락처
		swal({
           type: 'info',
           text: '연락처를 입력해주세요.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
        });
		return;
	}
	
	if(!regExpTel.test(chrHp)){
		swal({
           type: 'info',
           text: '연락처를 확인해주세요.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
        });
		return;
	}
	
	if(!$('#chaZipCode').val() || !$('#chaAddress1').val() || !$('#chaAddress2').val()) { // 주소
		swal({
           type: 'info',
           text: '주소를 입력해주세요.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
        });
		return;
	}
	
	var url = "/org/myPage/updateOrgInfo";

	var accList = $('#acclist').val();
	var count = "${map.accList.size()}";
	var jsonRECS  = [];
	var jsonREC   = null;
	
	if(count == 0){
		jsonREC = {};
		
		jsonREC["chaCd"]=$('#chaCd').val();
		jsonREC["chrName"]=$('#chrName').val();
		jsonREC["chrTelNo"]=$("#chrTelNo1").val() + "-" + $("#chrTelNo2").val() + "-" + $("#chrTelNo3").val();
		jsonREC["chrHp"]=$("#chrHp1").val() + "-" + $("#chrHp2").val() + "-" + $("#chrHp3").val();
		jsonREC["chrMail"]=$("#chrMail1").val() + "@" + $("#chrMail2").val();
		jsonREC["chaZipCode"]=$('#chaZipCode').val();
		jsonREC["chaAddress1"]=$('#chaAddress1').val();
		jsonREC["chaAddress2"]=$('#chaAddress2').val();
		
		jsonRECS[0] = jsonREC;
	}else{
	
		for(var i = 0; i<count; i++){
			jsonREC = {};
		
			/* if($('#grpadjname'+i+1).val() == null || $('#grpadjname'+i+1).val() == ''){
				jsonREC["grpAdjName"]='';
				
			}else{
				jsonREC["grpAdjName"]=   $('#grpadjname'+(i+1)+'').val();
				alert (i + " : " + $('#grpadjname'+(i+1)+'').val());
				alert (i + " : " + jsonREC["grpAdjName"]);
			} */
			
			jsonREC["grpAdjName"]=   $('#grpadjname'+(i+1)+'').val();
			jsonREC["adjFiRegkey"]=$('#adjfiregkey'+(i+1)+'').text();
			jsonREC["chaCd"]=$('#chaCd').val();
			jsonREC["chrName"]=$('#chrName').val();
			jsonREC["chrTelNo"]=$("#chrTelNo1").val() + "-" + $("#chrTelNo2").val() + "-" + $("#chrTelNo3").val();
			jsonREC["chrHp"]=$("#chrHp1").val() + "-" + $("#chrHp2").val() + "-" + $("#chrHp3").val();
			jsonREC["chrMail"]=$("#chrMail1").val() + "@" + $("#chrMail2").val();
			jsonREC["chaZipCode"]=$('#chaZipCode').val();
			jsonREC["chaAddress1"]=$('#chaAddress1').val();
			jsonREC["chaAddress2"]=$('#chaAddress2').val();
			
			jsonRECS[i] = jsonREC;
		}
	}
	
	var param = {
			rec: jsonRECS
	};
	$.ajax({
		type: "post",
		url: url,
		data: JSON.stringify(param),
		contentType : "application/json; charset=utf-8",
		success: function(data){
			swal({
		           type: 'success',
		           text: '기본 정보 변경이 완료되었습니다.',
		           confirmButtonColor: '#3085d6',
		           confirmButtonText: '확인'
		       }).then(function(result) { 
				  if (result.value) {
					  document.location.href="/org/myPage/orgInfoMgmt?chaCd="+$("#chaCd").val();
				  } 
			   });
		},
		error:function(data){
			swal({
	           type: 'error',
	           text: '기본 정보 변경을 실패하였습니다.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       })
		}
	});

}

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

function pwdReset(){
	$('#password-reset-popup').modal({
		backdrop : 'static',
		keyboard : false
	});
}
</script>

<input type="hidden" name="chaCd" id="chaCd" value="${map.chaCd}">

<div id="contents">
    <div id="damoa-breadcrumb">
        <nav class="nav container">
        	<a class="nav-link active" href="#">기본정보관리</a>
        	<a class="nav-link" href="#">서비스설정</a>
        	<a class="nav-link" href="#">고지상세설정</a>
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
                    <span class="depth-2 active">기본정보관리</span>
                </div>
            </div>
            <div class="row">
                <div class="col-6">
                    <h2>기본정보관리</h2>
                </div>
                <div class="col-6 text-right">
                    <%--<button type="button" class="btn btn-img">--%>
                        <%--<img src="/assets/imgs/common/btn-typo-control.png">--%>
                    <%--</button>--%>
                    <%--<button type="button" class="btn btn-img">--%>
                        <%--<img src="/assets/imgs/common/btn-print.png">--%>
                    <%--</button>--%>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div id="page-description">
            <div class="row">
                <div class="col-12">
                    <p>기관 담당자 기본 정보를 조회/변경하는 화면입니다.</p>
                </div>
            </div>
        </div>
    </div>


    <div class="container">
        <div class="row">
            <div class="col-4">
                <h5>기본 정보</h5>
            </div>
            <div class="col-8 text-right">
                <span class="guide-mention font-blue">
                    기본 정보 변경 시 다모아 고객센터 문의(02-786-8201)
                </span>
            </div>
        </div>
    </div>
    <div class="container">
        <div class="row">
            <div class="col">
                <table class="table table-form">
                    <tbody class="container-fluid">
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">사업자번호</th>
                            <td colspan="3" class="col-md-10 col-sm-8 col-8">${map.result.chaOffNo}</td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">기관명<br class="show-on-mobile hidden-on-web"/>(업체명)</th>
                            <td class="col-md-4 col-sm-8 col-8">${map.result.chaName}</td>
                            <th class="col-md-2 col-sm-4 col-4">기관코드</th>
                            <td class="col-md-4 col-sm-8 col-8">${map.result.chaCd}</td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">업태</th>
                            <td class="col-md-4 col-sm-8 col-8">${map.result.chaType}</td>
                            <th class="col-md-2 col-sm-4 col-4">업종</th>
                            <td class="col-md-4 col-sm-8 col-8">${map.result.chaStatus}</td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">대표자</th>
                            <td class="col-md-4 col-sm-8 col-8">${map.result.owner}</td>
                            <th class="col-md-2 col-sm-4 col-4">대표전화번호</th>
                            <td class="col-md-4 col-sm-8 col-8">${map.result.ownerTel}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row mt-4">
            <div class="col-6">
                <h5>담당자 정보</h5>
            </div>
        </div>
    </div>
    <div class="container">
        <div class="row">
            <div class="col">
                <table class="table table-form">
                    <tbody class="container-fluid">
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">아이디</th>
                            <td class="col-md-4 col-sm-8 col-8">${map.result.loginId}</td>
                            <th class="col-md-2 col-sm-4 col-4">비밀번호</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <button type="button" class="btn btn-sm btn-d-gray" onclick="pwdReset();">재설정</button>
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">담당자명</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <input type="text" class="form-control" id="chrName" name="chrName" value="${map.result.chrName}">
                            </td>
                            <th class="col-md-2 col-sm-4 col-4">전화번호</th>
                            <c:set var="chrTelNo" value="${fn:split(map.result.chrTelNo, '-')}" />
                            <td class="col-md-4 col-sm-8 col-8 form-inline contact-number">
                            	<input type="hidden" id="chrTelNo" name="chrTelNo">
                                <input type="text" class="form-control" maxlength="3" id="chrTelNo1" name="chrTelNo1" value="${chrTelNo[0]}" onkeyup="onlyNumber(this)">
                                <span class="ml-1 mr-1"> - </span>
                                <input type="text" class="form-control" maxlength="4" id="chrTelNo2" name="chrTelNo2" value="${chrTelNo[1]}" onkeyup="onlyNumber(this)">
                                <span class="ml-1 mr-1"> - </span>
                                <input type="text" class="form-control" maxlength="4" id="chrTelNo3" name="chrTelNo3" value="${chrTelNo[2]}" onkeyup="onlyNumber(this)">
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">E-MAIL</th>
                            <c:set var="chrMail" value="${fn:split(map.result.chrMail, '@')}" />
                            <td class="col-md-4 col-sm-8 col-8 form-inline email-contact">
                            	<input type="hidden" id="chrMail" name="chrMail">
                                <input type="text" class="form-control col" id="chrMail1" name="chrMail1" value="${chrMail[0]}">
                                <span class="ml-1 mr-1">@</span>
                                <input type="text" class="form-control email-provider" id="chrMail2" name="chrMail2" value="${chrMail[1]}">
                                <select class="form-control provider-set mt-2 w-100">
                                    <option value="" <c:if test="${chrMail[1] eq ''}">selected</c:if>>직접입력</option>
                                    <option value="naver.com" <c:if test="${chrMail[1] eq 'naver.com'}">selected</c:if>>naver.com</option>
                                    <option value="nate.com" <c:if test="${chrMail[1] eq 'nate.com'}">selected</c:if>>nate.com</option>
                                    <option value="yahoo.com" <c:if test="${chrMail[1] eq 'yahoo.com'}">selected</c:if>>yahoo.com</option>
                                    <option value="empal.com" <c:if test="${chrMail[1] eq 'empal.com'}">selected</c:if>>empal.com</option>
                                    <option value="gmail.com" <c:if test="${chrMail[1] eq 'gmail.com'}">selected</c:if>>gmail.com</option>
                                    <option value="hanmail.net" <c:if test="${chrMail[1] eq 'hanmail.net'}">selected</c:if>>hanmail.net</option>
                                    <option value="daum.net" <c:if test="${chrMail[1] eq 'daum.net'}">selected</c:if>>daum.net</option>
                                </select>
                            </td>
                            <th class="col-md-2 col-sm-4 col-4">연락처</th>
                            <c:set var="chrHp" value="${fn:replace(map.result.chrHp, '-', '')}" />
                            <td class="col-md-4 col-sm-8 col-8 form-inline contact-number">
                            	<input type="hidden" id="chrHp" name="chrHp">
                                <input type="text" class="form-control" maxlength="3" id="chrHp1" name="chrHp1" value="${fn:substring(chrHp, 0, 3)}" onkeyup="onlyNumber(this)">
                                <span class="ml-1 mr-1"> - </span>
                                <input type="text" class="form-control" maxlength="4" id="chrHp2" name="chrHp2" value="${fn:substring(chrHp, 3, 7)}" onkeyup="onlyNumber(this)">
                                <span class="ml-1 mr-1"> - </span>
                                <input type="text" class="form-control" maxlength="4" id="chrHp3" name="chrHp3" value="${fn:substring(chrHp, 7, 11)}" onkeyup="onlyNumber(this)">
                            </td>
                        </tr>

                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">주소</th>
                            <td class="col-md-10 col-sm-8 col-8 address-form-in-table">
                                <div class="zipcode">
                                    <input type="text" name="chaZipCode" id="chaZipCode" class="form-control zipcode-input mr-1" readonly value="${map.result.chaZipCode }">
                                    <button type="button" class="btn btn-sm btn-d-gray" onclick="openZipSearch();">우편번호검색</button>
                                </div>
                                <div class="address-lines w-100">
                                    <input type="text" class="form-control first-line mt-1" id="chaAddress1" name="chaAddress1" value="${map.result.chaAddress1}">
                                    <input type="text" class="form-control second-line mt-1" id="chaAddress2" name="chaAddress2" value="${map.result.chaAddress2 }">
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="container" id="accInfo">
        <div class="row mt-4">
            <div class="col-12">
                <h5>입금 계좌</h5>
            </div>
        </div>
    </div>

    <div class="container" id="accInfoList">
        <div id="income-bank-account-sorting">
            <div class="row">
                <div class="table-responsive pd-n-mg-o col mb-3">
                <table class="table table-sm table-hover table-primary">
						<colgroup>
							<col width="60">
							<col width="425">
							<col width="425">
							<col width="200">
						</colgroup>
						<thead>
						<tr>
							<th>NO</th>
							<th>입금계좌명</th>
							<th>은행</th>
							<th>계좌정보</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(map.accList) > 0}">
								<c:forEach var="row" items="${map.accList}">
								<tr>
									<td>${row.rn}</td>
									<td><input type="text" class="form-control" value="${row.grpadjName}" id="grpadjname${row.rn}" style="text-align:center" maxlength="15"/></td>
									<td>신한은행</td>
									<td id="adjfiregkey${row.rn}">${row.adjfiRegKey}</td>
								</tr>
								<input type="hidden" value="${map.accList}" id="acclist"/>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="4" style="text-align: center;">
										[조회된 내역이 없습니다.]
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div class="row mt-2 mb-5">
            <div class="col-12 text-center">
                <button type="button" class="btn btn-primary" onclick="saveOrgInfo();">저장</button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" />

<%-- 비밀번호변경 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/password-reset.jsp" flush="false"/>

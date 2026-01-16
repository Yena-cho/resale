<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>

<s:authentication property="principal.username" var="userId" />
<s:authentication property="principal.name" var="userName" />
<s:authentication property="principal.chaOffNo" var="chaOffNo" />

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/>

<script>
var firstDepth = null;
var secondDepth = null;
</script>

<script type="text/javascript">
    // 기관정보조회
    function fn_orgInfoSearch() {
        var url = "/common/login/orgInfoSearch";
        var param = {
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                $("#chaName").text(data.dto.chaName);
                var offNo = data.dto.chaOffNo.substr(0, 3) + "-" + data.dto.chaOffNo.substr(3, 2) + "-" + data.dto.chaOffNo.substr(5);
                $("#chaOffNo").text(offNo);
                $("#owner").text(data.dto.owner);
                $("#ownerTel").text(data.dto.ownerTel);

                var addrZipCd;
                if (data.dto.chaZipCode == null || data.dto.chaZipCode == '') {
                    addrZipCd = "";
                } else {
                    addrZipCd = "(" + data.dto.chaZipCode + ") ";
                }

                var addr1;
                if (data.dto.chaAddress1 == null || data.dto.chaAddress1 == '') {
                    addr1 = "";
                } else {
                    addr1 = data.dto.chaAddress1;
                }

                var addr2;
                if (data.dto.chaAddress2 == null || data.dto.chaAddress2 == '') {
                    addr2 = "";
                } else {
                    addr2 = data.dto.chaAddress2;
                }

                var addr = addrZipCd + addr1 + " " + addr2

                $("#address").text(addr);

                $("#chaStatus").text(data.dto.chaStatus);
                $("#chaType").text(data.dto.chaType);
                $("#chrName").text(data.dto.chrName);
                $("#chrMail").text(data.dto.chrMail);
                $("#chrHp").text(data.dto.chrHp);
                $("#chrTelNo").text(data.dto.chrTelNo);

                // 다계좌정보
                fn_adjGroup(data);
            }
        });
    }

    // 다계좌정보
    function fn_adjGroup(data) {
        var len = 0;
        if (data.list.length > 0) {
            len = data.list.length;
        }
        var row = 1 + len;
        var str = '';

        str += '<tr>';
        str += '<th rowspan="' + row + '" class="border-r">계좌정보</th>';

        if (data.list.length > 0) {
            $.each(data.list, function (k, v) {
                if (k == 0) {
                    str += '<td rowspan="' + data.list.length + '" class="border-r">입금 모계좌</td>';
                    str += '<td class="border-r">' + v.grpAdjName + '</td>';
                    str += '<td>' + v.adjfiregKey + '</td>';
                    str += '</tr>';
                } else if (k > 0) {
                    str += '<tr>';
                    str += '<td class="border-r">' + v.grpAdjName + '</td>';
                    str += '<td>' + v.adjfiregKey + '</td>';
                    str += '</tr>';
                }
            });
        }
        if (row == 1) {
            str += '<td class="border-r border-b">수수료 출금계좌</td>';
            str += '<td class="border-b">' + data.dto.feeAccNo + '</td>';
            str += '</tr>';
        } else {
            str += '<tr>';
            str += '<td colspan="2" class="border-r border-b">수수료 출금계좌</td>';
            str += '<td class="border-b">' + data.dto.feeAccNo + '</td>';
            str += '</tr>';
        }

        $('#adjGroup').html(str);
    }

    // 등록정보취소
    function fn_insInfoCancel() {
        swal({
            type: 'question',
            title: "회원확인 절차를 취소하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                location.href = "/logout";
            }
        });
    }

    // 등록정보확인
    function fn_insInfoConfirm() {
        if (!$('#aggreement-01').is(':checked')) {
            swal({
                type: 'info',
                text: "이용신청서와 동일한 것을 확인 후 체크하셔야 다음 단계로 이동이 가능합니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        $(".step-01").removeClass("on");
        $(".step-02").addClass("on");

        $(".step1").removeClass("display-block");
        $(".step1").addClass("display-none");
        $(".step2").removeClass("display-none");
        $(".step2").addClass("display-block");
    }

    // 이용약관동의
    function fn_AcceptTerms() {
        if (!$('#aggreement-02').is(':checked')) {
            swal({
                type: 'info',
                text: "[신한가상계좌 서비스 이용약관]에 동의해주시기 바랍니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (!$('#aggreement-04').is(':checked')) {
            swal({
                type: 'info',
                text: "[개인정보처리방침]에 동의해주시기 바랍니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        $(".step-02").removeClass("on");
        $(".step-03").addClass("on");

        $(".step2").removeClass("display-block");
        $(".step2").addClass("display-none");
        $(".step3").addClass("display-block");
        $(".step3").removeClass("display-none");
    }

    // 아이디, 비밀번호 변경
    function fn_memberInfoModify() {
        var id = $('#resetId').val();
        var offNo = '${chaOffNo}'.substring('${chaOffNo}'.length - 4, '${chaOffNo}'.length);
        var pw = '${userId}' + '_' + offNo;
        var nowPw = $('#nowPassword').val();
        var newPw = $('#newPassword').val();
        var confPw = $('#confPassword').val();

        if ($('#resetIdCheck').val() == 'C') {
            swal({
                type: 'info',
                text: "아이디 중복검사를 해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function (result) {
                $('#resetId').focus();
            });
            return;
        }
        if ($('#resetIdCheck').val() == 'N') {
            swal({
                type: 'info',
                text: "사용할 수 없는 아이디입니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function (result) {
                $('#resetId').focus();
            });
            return;
        }

        if (!nowPw) {
            swal({
                type: 'info',
                text: "현재 비밀번호를 입력해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function (result) {
                $('#nowPassword').focus();
            });
            return;
        }

        var num = newPw.search(/[0-9]/g);
        var eng = newPw.search(/[a-zA-Z]/gi);
        var spe = newPw.search(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi);

        if (newPw.length < 8 || newPw.length > 20) {
            swal({
                type: 'info',
                text: "비밀번호는 영문, 숫자, 특수문자를 사용하여 8~20자까지, 영문은 대소문자를 구분합니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if (newPw.search(/\s/) != -1) {
            swal({
                type: 'info',
                text: "비밀번호는 공백없이 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });

            return;
        }
        if (newPw.search(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/) != -1) {
            swal({
                type: 'info',
                text: "비밀번호는 영문, 숫자, 특수문자를 사용하여 8~20자까지, 영문은 대소문자를 구분합니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if ((num < 0 && eng < 0) || (eng < 0 && spe < 0) || (spe < 0 && num < 0)) {
            swal({
                type: 'info',
                text: "비밀번호는 영문, 숫자, 특수문자 중 2가지 이상을 혼합하여 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var SamePass_0 = 0; //동일문자 카운트
        var SamePass_1 = 0; //연속성(+) 카운드
        var SamePass_2 = 0; //연속성(-) 카운드

        for (var i = 0; i < newPw.length; i++) {
            var chr_pass_0 = newPw.charAt(i);
            var chr_pass_1 = newPw.charAt(i + 1);

            //동일문자 카운트
            if (chr_pass_0 == chr_pass_1) {
                SamePass_0 = SamePass_0 + 1
            }

            var chr_pass_2 = newPw.charAt(i + 2);

            //연속성(+) 카운드
            if (chr_pass_0.charCodeAt(0) - chr_pass_1.charCodeAt(0) == 1 && chr_pass_1.charCodeAt(0) - chr_pass_2.charCodeAt(0) == 1) {
                SamePass_1 = SamePass_1 + 1
            }

            //연속성(-) 카운드
            if (chr_pass_0.charCodeAt(0) - chr_pass_1.charCodeAt(0) == -1 && chr_pass_1.charCodeAt(0) - chr_pass_2.charCodeAt(0) == -1) {
                SamePass_2 = SamePass_2 + 1
            }
        }

        if (SamePass_0 > 2) {
            swal({
                type: 'info',
                text: "동일문자를 3번 이상 사용할 수 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (SamePass_1 > 2 || SamePass_2 > 2) {
            swal({
                type: 'info',
                text: "연속된 문자열(123 또는 321, abc, cba 등)을\n 3자 이상 사용 할 수 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (newPw != confPw) {
            swal({
                type: 'info',
                text: "신규 비밀번호와 재입력 내용이 다릅니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function (result) {
                $('#confPassword').focus();
            });
            return;
        }

        fn_cusInfoUpdate();
    }

    function idvalidation(id) {
        if (id.length < 8 || id.length > 20) {
            swal({
                type: 'info',
                text: "ID는 8자리~20자리 이내로 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return false;
        }
        if (id.search(/\s/) != -1) {
            swal({
                type: 'info',
                text: "ID는 공백없이 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return false;
        }
        if (id.search(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/) != -1) {
            swal({
                type: 'info',
                text: "ID는 영문,숫자 조합 8자리~20자리 이내로 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return false;
        }
        return true;
    }


    // 아이디 중복체크
    function fn_idOverlap() {

        if (idvalidation($('#resetId').val()) == false) {
            return;
        }
        $('#resetIdCheck').val('C');

        var url = "/common/login/idOverlap";
        var param = {
            loginId: $('#resetId').val()
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                if (data.id == null || data.id == '${userId}') { // 기존 기관코드 사용가능.
                    $('#resetIdSuc').show();
                    $('#resetIdErr').hide();
                    $('#resetIdCheck').val('Y');
                } else {
                    $('#resetIdSuc').hide();
                    $('#resetIdErr').show();
                    $('#resetIdCheck').val('N');
                }
            }
        });
    }

    function fn_idOverlapCk(val) {
        if (val == 'K') {
            $('#resetIdCheck').val('Y');
        } else {
            $('#resetIdCheck').val('C');
        }

        $('#resetIdSuc').hide();
        $('#resetIdErr').hide();
    }

    // 아이디, 비밀번호 변경
    function fn_cusInfoUpdate() {
        var url = "/common/login/customerUpdate";
        var param = {
            loginId: !$('#resetId').val() ? '${userId}' : $('#resetId').val(),
            nowPw: $('#nowPassword').val(),
            loginPw: $('#newPassword').val(),
            userId: '${userId}'
        };

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                if (data.retCode == '0000') {
                    $(".step-03").removeClass("on");
                    $(".step-04").addClass("on");

                    $(".step3").removeClass("display-block");
                    $(".step3").addClass("display-none");
                    $(".step4").addClass("display-block");
                    $(".step4").removeClass("display-none");
                } else if (data.retCode == '0001') {
                    swal({
                        type: 'info',
                        html: '현재 아이디 재설정이 올바르지 않습니다. <p>다시 입력해 주세요.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function (result) {
                        if (result.value) {
                            $('#nowPassword').focus();
                        }
                    });
                } else if (data.retCode == '8888') {
                    swal({
                        type: 'info',
                        html: '현재 비밀번호가 올바르지 않습니다. <p>다시 입력해 주세요.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function (result) {
                        if (result.value) {
                            $('#nowPassword').focus();
                        }
                    });
                } else if (data.retCode == '9999') {
                    swal({
                        type: 'info',
                        html: '현재 비밀번호와 수정한 비밀번호가  같습니다. <p>다시 입력해 주세요.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function (result) {
                        if (result.value) {
                            $('#newPassword').focus();
                        }
                    });
                }
            }
        });
    }

    // 로그인
    function fn_goLogin() {
        if (!$('#orgId').val()) { //아이디를 입력하지 않으면.
            swal({
                type: 'info',
                text: "아이디를 입력해주세요!",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $("#orgId").focus();
            });
            return;
        }

        //아이디 유효성 체크
        if (idvalidation($('#orgId').val()) == false) {
            return;
        }

        if (!$('#orgPw').val()) { //비밀번호를 입력하지 않으면.
            swal({
                type: 'info',
                text: "비밀번호를 입력해주세요!",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $("#orgPw").focus();
            });
            return;
        }

        //비밀번호 유효성 체크
        var num = $("#orgPw").val().search(/[0-9]/g);
        var eng = $("#orgPw").val().search(/[a-zA-Z]/gi);
        var spe = $("#orgPw").val().search(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi);
        if ($("#orgPw").val().length < 8 || $("#orgPw").val().length > 20) {
            swal({
                type: 'info',
                text: "비밀번호는 8자리 ~ 20자리 이내로 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $("#orgPw").focus();
            });
            return;
        }

        if ($("#orgPw").val().search(/\s/) != -1) {
            swal({
                type: 'info',
                text: "비밀번호는 공백없이 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $("#orgPw").focus();
            });
            return;
        }

        if ($("#orgPw").val().search(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/) != -1) {
            swal({
                type: 'info',
                text: "비밀번호는 영문,숫자, 특수문자 중 2가지 이상을 혼합하여 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $("#orgPw").focus();
            });
            return;
        }

        if ((num < 0 && eng < 0) || (eng < 0 && spe < 0) || (spe < 0 && num < 0)) {
            swal({
                type: 'info',
                text: "비밀번호는 영문,숫자, 특수문자 중 2가지 이상을 혼합하여 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $("#orgPw").focus();
            });
            return;
        }

        $('#userId').val("ORG" + ":///:" + $('#orgId').val());
        $('#userPw').val($('#orgPw').val());

        document.loginForm.action = "/login";
        document.loginForm.submit();
}
</script>

<form name="loginForm" id="loginForm" method="post">
	<input type="hidden" name="loginGb" id="loginGb" value="org">
	<input type="hidden" name="info" 	id="info" 	 value="num">
	<input type="hidden" name="userId"  id="userId">
	<input type="hidden" name="userPw"  id="userPw">
</form>

<input type="hidden" id="resetIdCheck" value="C">

<div id="contents">
	<div id="damoa-breadcrumb">
		<nav class="nav container">
			<a class="nav-link" href="#">공지사항</a> <a class="nav-link active" href="#">자주하는 질문</a>
			<a class="nav-link" href="#">고객센터</a>
		</nav>
	</div>
	<div class="container">
		<div id="page-title">
			<div id="breadcrumb-in-title-area" class="row align-items-center">
				<div class="col-12 text-right">
					<img src="/assets/imgs/common/icon-home.png" class="mr-2">
					<span> > </span>
					<span class="depth-1">고객지원</span>
					<span> > </span>
					<span class="depth-2 active">자주하는 질문</span>
				</div>
			</div>
			<div class="row">
				<div class="col-12">
					<h2>회원 확인</h2>
				</div>
			</div>
		</div>
	</div>
	<div class="container confirm-member-step-indicator">
		<div class="row">
			<div class="col col-md-3 col-6 steps step-01 on">
				<div class="row no-gutters align-items-center">
					<div class="col-sm-4 col-6 imgs"></div>
					<div class="col-sm-8 col-6">
						<strong>Step1</strong>
						<p>등록정보확인</p>
					</div>
				</div>
			</div>
			<div class="col col-md-3 col-6 steps step-02">
				<div class="row no-gutters align-items-center">
					<div class="col-sm-4 col-6 imgs"></div>
					<div class="col-sm-8 col-6">
						<strong>Step2</strong>
						<p>약관동의</p>
					</div>
				</div>
			</div>
			<div class="col col-md-3 col-6 steps step-03">
				<div class="row no-gutters align-items-center">
					<div class="col-sm-4 col-6 imgs"></div>
					<div class="col-sm-8 col-6">
						<strong>Step3</strong>
						<p>아이디, 비밀번호 변경</p>
					</div>
				</div>
			</div>
			<div class="col col-md-3 col-6 steps step-04">
				<div class="row no-gutters align-items-center">
					<div class="col-sm-4 col-6 imgs"></div>
					<div class="col-sm-8 col-6">
						<strong>Step4</strong>
						<p>서비스 이용 개시</p>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="container step-box">
		<div class="">
			<div class="">
				<div class="step1 display-block">
					<div class="agree-01 col">
						<div class="row mb-4">
							<div class="col">
								<div class="row">
									<div class="table-responsive col mb-3">
										<table class="table table-form table-primary">
											<tbody class="container-fluid">
												<tr class="row no-gutters">
													<th class="col-md-2 col-sm-4 col-4">기관(업체)명</th>
													<td class="col-md-4 col-sm-8 col-8"><span id="chaName"></span></td>
													<th class="col-md-2 col-sm-4 col-4">사업자등록번호</th>
													<td class="col-md-4 col-sm-8 col-8"><span id="chaOffNo"></span></td>
												</tr>
												<tr class="row no-gutters">
													<th class="col-md-2 col-sm-4 col-4">업태</th>
													<td class="col-md-4 col-sm-8 col-8"><span id="chaType"></span></td>
													<th class="col-md-2 col-sm-4 col-4">업종</th>
													<td class="col-md-4 col-sm-8 col-8"><span id="chaStatus"></span></td>
												</tr>
												<tr class="row no-gutters">
													<th class="col-md-2 col-sm-4 col-4">대표자명</th>
													<td class="col-md-4 col-sm-8 col-8"><span id="owner"></span></td>
													<th class="col-md-2 col-sm-4 col-4">대표 전화번호</th>
													<td class="col-md-4 col-sm-8 col-8"><span id="ownerTel"></span></td>
												</tr>
												<tr class="row no-gutters">
													<th class="col-md-2 col-sm-4 col-4">기관(업체)주소</th>
													<td class="col-md-10 col-sm-8 col-8"><span id="address"></span></td>
												</tr>
												<tr class="row no-gutters">
													<th class="col-md-2 col-sm-4 col-4">담당자명</th>
													<td class="col-md-4 col-sm-8 col-8"><span id="chrName"></span></td>
													<th class="col-md-2 col-sm-4 col-4">담당자 이메일</th>
													<td class="col-md-4 col-sm-8 col-8"><span id="chrMail"></span></td>
												</tr>
												<tr class="row no-gutters">
													<th class="col-md-2 col-sm-4 col-4">담당자 전화번호</th>
													<td class="col-md-4 col-sm-8 col-8"><span id="chrTelNo"></span></td>
													<th class="col-md-2 col-sm-4 col-4">담당자 핸드폰번호</th>
													<td class="col-md-4 col-sm-8 col-8"><span id="chrHp"></span></td>
												</tr>
											</tbody>
										</table>

										<table class="table table-primary mt-sm-4">
											<colgroup>
												<col width="250">
												<col width="200">
												<col width="150">
												<col width="480">
											</colgroup>

											<tbody id="adjGroup">
											</tbody>
										</table>
									</div>
								</div>

								<p class="guide-mention mt-2"></p>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-8 col-12">
								상기 내용은 신한 다모아 이용신청서의 모든 기재사항과 동일하며, 사실과 다름없음을 확인합니다.<br/>
								기재한 내용과 다를 경우 고객센터로 문의하시기 바랍니다.
							</div>
							<div class="col-sm-4 col-12 form-inline">
								<div class="form-check form-check-inline ml-auto">
									<input class="form-check-input" type="checkbox" id="aggreement-01"> <label for="aggreement-01"><span class="mr-2"></span>확인했습니다.</label>
								</div>
							</div>
						</div>
						<div class="row bt-1-e6e6e6 mt-4 pt-4">
							<div class="col text-center">
								<button class="btn btn-primary btn-outlined" onclick="fn_insInfoCancel();">취소</button>
								<button class="btn btn-primary btn-start-auth btn-next-slide" onclick="fn_insInfoConfirm();">다음</button>
							</div>
						</div>
					</div>
				</div>

				<div class="step2 display-none">
					<div class="col">
						<div class="agree-02">
							<div class="row mt-4 mb-4">
								<div class="col">
									<span class="guide-mention">서비스 이용 필수 약관으로 내용을 읽어보시고 동의 여부를 체크하여 주시기 바랍니다.</span>
								</div>
							</div>
							<div
								class="row no-gutters align-items-center title-agreement-area">
								<div class="col">
									<h3>신한 다모아 서비스 이용 약관 동의</h3>
								</div>
								<div class="col form-inline">
									<div class="form-check form-check-inline ml-auto">
										<input class="form-check-input checkbox-parent" type="checkbox" id="aggree-all"> <label for="aggree-all"><span class="mr-2"></span>전체동의</label>
									</div>
								</div>
							</div>
							<div class="row no-gutters align-items-center title-agreement-unit">
								<div class="col">
									<h4>가상계좌 수납관리 서비스 이용약관</h4>
								</div>
								<div class="col text-right form-inline">
									<div class="form-check form-check-inline ml-auto">
										<input class="form-check-input checkbox-child" type="checkbox" id="aggreement-02"> <label for="aggreement-02"><span class="mr-2"></span>동의</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col">
									<div class="form-control" style="padding: 25px; height: 300px; border: 1px solid #d3d3d3; overflow: hidden; overflow-y: auto;">
										<div class="policy" style="margin: 0;">
											<h2>제1장 총칙 </h2>
											<br>

											<h3>제1조(목적) </h3>
											<p>본 약관은 고객이 주식회사 핑거(이하 회사라 함)에서 제공하는 가상계좌 수납관리 서비스(이하”서비스”라 함)를 이용함에 있어 고객과 회사간의 권리, 의무 및 책임사항 등 기타 필요한 사항을 규정함을 목적으로 합니다.</p>

											<h3>제2조(약관의 효력 및 변경)</h3>
											<ul>
												<li> ① 본 약관은 서비스를 이용하고자 하는 모든 고객에 대하여 그 효력을 발생합니다.</li>
												<li> ② 본 약관의 내용은 서비스 화면에 게시하거나 기타의 방법으로 고객에게 공지하고, 이에 동의한 고객이 서비스에 가입함으로써 효력이 발생합니다.</li>
												<li> ③ 회사는 필요하다고 인정되는 경우 본 약관을 변경할 수 있으며, 약관을 변경하고자 하는 경우에는 제2항과 같은 방법으로 1개월 이전에 고지합니다. 다만, 법령의 개정으로 인하여 긴급하게 약관을 변경하는 때에는 이를 즉시 서비스 화면에 게시하고 1개월이상 고객에게 고지합니다.</li>
												<li> ④ 고객은 변경된 약관 사항에 동의하지 않으면 서비스 이용을 중단하고 이용 계약을 해지할 수 있습니다. 다만, 회사가 제3항에 따라 변경된 약관을 고지하였음에도 고객이 변경된 약관의 시행일 전의 영업일까지 이의를 제기하지 아니하는 경우 고객이 변경된 약관에 동의한 것으로 봅니다.</li>
											</ul>

											<h3>제3조(용어의 정의)</h3>
											<ul>
												<li>① 본 약관에서 사용하는 용어의 정의는 다음과 같습니다.</li>
												<li>
													<ol>
														<li>서비스 : 서비스 사이트 및 고객사별 맞춤 개발 관리자 사이트를 통해 제공하는 가상계좌 수납관리 서비스</li>
														<li>고객 : 서비스 사이트에 접속하여 본 약관에 동의하고, 회사로부터 가입을 승인 받아 아이디와 비밀번호를 발급 받고 서비스를 이용하는 회사, 단체</li>
														<li>아이디 : 고객의 식별과 고객의 서비스 이용을 위하여 회사가 자동 채번하여 부여한 숫자 8자리</li>
														<li>비밀번호 : 고객이 부여 받은 아이디와 일치된 고객임을 확인하고, 고객 자신의 정보를 보호하기 위하기 위하여 고객이 정한 8자리 이상의 영대/소문자와 숫자 3가지의 조합</li>
														<li>운영자 : 서비스의 전반적인 관리와 원활한 운영을 위하여 회사가 선정한 자</li>
														<li>서비스 중지 : 정상 이용 중 회사가 정한 일정한 요건에 따라 일정기간 동안 서비스의 제공을 중지하는 것</li>
														<li>수수료 : 회사가 고객에게 서비스를 이용할 수 있도록 일종의 중개용역을 제공하고 그 대가로 징수하는 요금</li>
														<li>제휴은행 : 회사와 가상계좌서비스 업무대행계약을 체결한 은행</li>
														<li>정산 : 가상계좌로 수납된 금액을 약정된 정산 주기에 의거하여 고객의 모계좌로 입금하는 것</li>
														<li>이용자 : 가상계좌 서비스를 이용하는 고객과 직접적으로 거래하는 대상</li>
													</ol>
												</li>

												<li>② 본 약관에서 별도로 정의하지 아니한 용어는 회사의 서비스정책 및 전자금융거래법 등에서 정하는 바에 따른다.</li>
											</ul>

											<h3>제4조(이용 계약의 성립)</h3>
											<ul>
												<li>① 회원가입 시 “이용약관에 동의합니다.”라는 물음에 고객이 동의를 선택한 후 “확인” 버튼을 누르면 약관에 동의하는 것으로 간주됩니다.</li>
												<li>② 서비스 이용계약은 고객이 회사가 정한 가입 절차와 방법에 따라 이용신청을 하고, 회사가 이러한 신청에 대하여 승낙함으로써 체결됩니다. 다만, 필요 시 회사는 고객에게 전문기관을 통한 실명확인 및 본인인증을 요청할 수 있습니다.</li>
												<li>③ 불가피한 사정으로 인해 서비스 사이트를 통한 약관 동의 방식을 활용할 수 없는 경우, 회사가 고객에게 서면으로 약관을 교부하고 고객은 약관에 대한 동의서를 제출함으로써 이용계약이 성립됩니다.</li>
											</ul>

											<h3>제5조(이용신청)</h3>
											<p>이용신청은 서비스 사이트의 고객 등록 화면에서 고객이 온라인신청 및 서면 양식에 다음 사항을 기재하여 제출하는 방식으로 행합니다.</p>
											<ul>
												<li>1. 신청고객 정보 : 고객(회사/단체)명, 사업자등록번호/고유식별번호, 사업장주소, 업태, 업종, 대표자명, 생년월일 , 대표자 전화번호, 회사 전화번호, 고객 계좌정보</li>
												<li>2. 담당자 정보 : 담당자명, 이메일(전자우편) 주소, 담당자 전화번호</li>
												<li>3. 기타 회사가 필요하다고 인정하는 사항</li>
											</ul>

											<h3>제6조(이용신청의 승낙)</h3>
											<ul>
												<li>① 회사는 제5조에서 정한 사항을 정확히 기재하고, 회사가 정한 인증 절차를 완료한 고객에 대하여 서비스 이용 신청을 승낙합니다.</li>
												<li>② 회사는 서비스를 신청한 고객의 업종 및 업태, 휴.폐업 여부, 사용목적 등에 따라 이용신청을 거부할 수 있습니다.</li>
												<li>③ 회사와 고객간 서비스 이용계약은 회사의 승낙이 고객에게 도달한 시점에 성립합니다.</li>
											</ul>


											<h3>제7조(이용신청에 대한 승낙의 제한)</h3>
											<p>회사는 다음 각 호에 해당하는 신청에 대하여는 승낙을 하지 않거나 사후에 이용계약을 해지할 수 있습니다.</p>
											<ul>
												<li>1. 기재내용에 허위, 기재누락, 오기 등이 있는 경우</li>
												<li>2. 실명이 아니거나, 다른 사람의 명의사용 등 고객 등록 시</li>
												<li>3. 고객 등록 사항을 누락하거나 오기하여 신청하는 경우</li>
												<li>4. 과거에 이 약관의 위반 등의 사유로 본 서비스의 이용계약이 해지된 경력이 있는 경우</li>
												<li>5. 기타 이 약관에 위배되거나 위법 또는 부당한 이용신청임이 확인된 경우</li>
												<li>6. 기타 회사가 정한 내부 입점불가 업종인 유해업종에 해당되는 경우</li>
											</ul>

											<h3>제8조(계약 사항의 변경)</h3>
											<p>고객은 이용 신청 시 기재한 사항이 변경되었을 경우 회사가 정한 별도의 이용 방법으로 정해진 양식 및 방법에 의하여 수정하여야 합니다.</p>

											<br>
											<h4>제2장 서비스 이용</h4>
											<br>

											<h3>제9조(서비스의 이용 개시)</h3>
											<ul>
												<li>① 회사는 고객의 서비스 이용 신청을 승낙한 때부터 서비스를 개시합니다. 단, 부가 서비스의 경우에는 신청 일자부터 서비스를 개시합니다.</li>
												<li>② 회사의 업무상 또는 기술상의 장애로 인하여 서비스를 개시하지 못하는 경우에는 사이트에 공지하거나 고객에게 이를 통지합니다.</li>
											</ul>

											<h3>제10조(서비스 내용)</h3>
											<p>회사는 다음 각 호와 같은 서비스를 제공하며, 회사의 사정, 기타 제반 여건에 따라 서비스 내용을 추가하거나 변경할 수 있습니다. 각 서비스의 종류, 기능, 이용조건 등 상세 내용은 서비스 소개 페이지 등에 게시, 안내 합니다.</p>
											<ul>
												<li>1.가상계좌 발급 및 정산</li>
												<li>2.고객관리</li>
												<li>3.청구관리</li>
												<li>4.수납관리</li>
												<li>5.부가서비스</li>
											</ul>

											<h3>제11조(서비스 수수료)</h3>
											<ul>
												<li>① 고객은 서비스 이용 대가로 회사가 정한 기준에 따라 회사에 월 수수료를 납부합니다.</li>
												<li>② 수수료의 납부 방식은 선납과 후납으로 나뉘며, 선납의 경우 본 약관 제 12조에 따라 정산 예정 금액에서 수수료를 공제하며, 후납의 경우 회사가 매월 지정된 일자에 고객의 계좌로부터 자동출금 하거나 고객이 직접 입금합니다. (자동출금일이 공휴일 및 은행 휴무일인 경우 익영업일에 출금)</li>
												<li>③ 회사는 서비스 기본 수수료에 대하여 홈페이지 내 확인할 수 있도록 하며, 서비스와 관련 발생된 수수료에 대하여 사전 등록된 E-mail 주소로 익월 첫 영업일에 전자세금계산서를 발행하여 고지합니다. 추가 고객별 상세 사용현황 및 비용은 고객이 직접 로그인하여 확인할 수 있습니다. 만일 고객이 사용현황 및 비용을 서면으로 확인하고자 하는 경우, 확인을 요청하는 항목, 기간, 기타 사항을 명시하여 이메일 또는 고객센터를 통해 요청할 수 있습니다.</li>
											</ul>

											<h3>제12조(정산)</h3>
											<ul>
												<li>① 회사는 회사가 제공하는 가상계좌 수납관리 서비스를 통해 이루어진 정상적인 거래의 정산 대금을 정산 금액의 일체 또는 제11조에 따른 서비스 수수료를 공제한 후, 본 약관에서 정한 정산 주기에 따라 고객에게 지급합니다.</li>
												<li>② 정산은 일단위로 진행되며 세부 정산 주기는 아래와 같습니다.</li>
												<li>- 정산 대상 기준 : 직전 영업일 00시 ~ 전일 24시 가상계좌 입금건에 대한 총 금액</li>
												<li>- 정산주기 : 익영업일 정산</li>
												<li>③ 제1항의 정산은 회사가 은행으로부터 해당 거래에 대한 대금 수령을 한 이후 정산하는 것을 원칙으로 하며, 은행의 사정으로 인하여 당해 정산 지급이 불가피한 경우 그 사정이 존재하는 기간 동안 회사의 정산지급의무는 면제되나 은행에 원인규명 후 정상적인 정산이 될 수 있도록 노력합니다.</li>
												<li>④ 회사는 정산 지급 시 서비스 수수료를 합산한 금액이 정산금액 보다 큰 경우, 회사는 본 조 2항에 명시된 정산주기에 따라 다음 정산 시 기존에 발생하였거나 발생예정 대금에서 상계처리 할 수 있습니다.</li>
												<li>⑤ 본 조 4항에 따라 고객의 정산금액 부족 등의 사유로 상계처리가 되지 않고 3개월 이상 유지되는 경우, 회사는 고객에게 해당 금액을 별도 청구하며, 고객은 이를 2주 이내에 납부하여야 합니다. 납부기간 내에 납부되지 않는 경우 회사는 서비스를 중지할 수 있습니다.</li>
											</ul>

											<h3>제13조 (지급보류)</h3>
											<ul>
												<li>① 다음 각 호의 경우에 회사는 정산 대금의 일부 또는 전부의 지급을 보류할 수 있습니다.</li>
												<li>1. 이용자가 본인 미거래 등의 이의를 제기한 경우.</li>
												<li>② 제1항의 정산 대금이 이미 고객에게 지급된 경우, 회사는 고객에게 지급할 예정인 차기 정산 대금의 지급을 보류할 수 있습니다.</li>
											</ul>

											<h3>제14조(서비스의 이용시간)</h3>
											<ul>
												<li>① 서비스의 이용은 연중무휴 1일 24시간을 원칙으로 합니다. 다만, 회사는 회사 정책, 고객의 신청에 따라 이용가능시간, 이용가능 횟수 등을 제한할 수 있습니다.</li>
												<li>② 회사는 컴퓨터 등 정보통신설비의 보수점검, 교체 및 고장, 통신두절 등 운영상 상당한 이유가 있는 경우 서비스의 제공을 일시적으로 중단할 수 있습니다. 이러한 경우 회사는 사전 또는 사후에 이를 공지합니다.</li>
											</ul>

											<h3>제15조(서비스의 변경, 종료 및 중지)</h3>
											<ul>
												<li>① 회사는 운영상, 경영상, 기술상의 필요에 따라 서비스의 전부 또는 일부 내용을 사전고지 후 일시적으로 변경할 수 있습니다.</li>
												<li>② 회사는 다음 각 호에 해당하는 경우 서비스의 전부 또는 일부를 제한하거나 중지할 수 있습니다.</li>
												<li>1. 서비스용 설비의 보수, 점검, 교체 및 고장 등에 따른 공사로 인한 부득이한 경우</li>
												<li>2. 고객이 회사의 영업활동을 방해하는 경우</li>
												<li>3. 통신두절, 정전, 제반 설비의 장애 또는 이용량의 폭주 등으로 정상적인 서비스 이용에 지장이 있는 경우</li>
												<li>4. 제휴 은행 및 파트너사와 계약 종료 등과 같은 회사의 제반 사정으로 서비스를 유지할 수 없는 경우</li>
												<li>5. 기타 천재지변, 국가비상사태 등 불가항력적 사유가 있는 경우</li>
												<li>6. 고객이 제 18조에서 정한 고객의 의무를 이행하지 아니한 경우.</li>
												<li>③ 회사는 제2항에 따라 서비스를 중지하고자 할 경우 서비스 중단 7일 전까지 서비스 화면에 해당 사실을 고지 합니다. 다만, 사전에 공지할 수 없는 부득이한 사정이 있는 경우 사전 고지 없이 서비스를 중지할 수 있으며, 이 경우 중지 조치 후에 지체 없이 이를 공지합니다.</li>
												<li>④ 서비스의 일부 또는 전부를 영구적으로 종료하고자 할 경우에는 해당 서비스 종료 30일 전에 서비스 화면에 해당 사실을 공지합니다. 다만, 사전에 공지할 수 없는 부득이한 사정이 있는 경우 사후에 지체 없이 이를 공지합니다.</li>
											</ul>

											<h3>제16조(정보의 제공 및 광고의 게재)</h3>
											<ul>
												<li>① 회사는 서비스를 운영함에 있어 각종 정보를 서비스 화면에 게재하거나 e-mail(전자우편) 및 서신우편 등의 방법으로 고객에게 제공할 수 있습니다.</li>
												<li>② 회사는 서비스의 운영과 관련하여 홈페이지, 서비스 화면, e-mail(전자우편) 등에 광고 등을 게재할 수 있습니다.</li>
												<li>③ 고객이 서비스상에 게재되어 있는 광고를 이용하거나 서비스를 통한 광고주의 판촉활동에 참여하는 등의 방법으로 교신 또는 거래를 하는 것은 전적으로 고객과 광고주 간의 문제입니다. 만약 고객과 광고주간에 문제가 발생할 경우에도 고객과 광고주가 직접 해결하여야 하며, 이와 관련하여 회사는 어떠한 책임도 지지 않습니다.</li>
											</ul>

											<br>
											<h4>제3장 계약당사자의 의무 </h4>
											<br>

											<h3>제17조(회사의 의무) </h3>
											<ul>
												<li>① 회사는 서비스 제공과 관련하여 알고 있는 고객의 신상정보, 계좌, 전자금융거래의 내용과 실적에 관한 정보 또는 자료 등 전자금융거래정보를 본인의 승낙 없이 제3자에게 누설, 배포, 제공하거나 업무상 목적 외에 사용하지 않습니다. 단, 관계법령에 의한 수사상의 목적으로 관계기관으로부터 요구 받은 경우나 정보통신윤리위원회의 요청이 있는 경우 등 법률의 규정에 따른 적법한 절차에 의한 경우에는 그러하지 않습니다.</li>
												<li>② 회사는 서비스와 관련한 고객의 불만사항이 접수되고 고객으로부터 제기된 의견이나 불만이 정당한 것으로 인정되는 경우 이를 신속하게 처리하여야 하며, 신속한 처리가 곤란한 경우 그 사유와 처리 일정을 서비스 화면에 게재하거나 e-mail(전자우편) 등을 통하여 동 고객에게 통지합니다.</li>
												<li>③ 회사가 제공하는 서비스로 인하여 고객([중소기업기본법 제2조 2항] 소기업 제외)에게 손해가 발생했을 때 회사가 사고를 방지하기 위하여 보안절차를 수립하고 이를 철저히 준수하는 등 합리적으로 요구되는 충분한 주의의무를 다한 경우 그 책임의 전부 또는 일부를 고객이 부담하게 할 수 있습니다.</li>
												<li>④ 회사는 정보통신망 이용촉진 및 정보보호에 관한 법률, 통신비밀보호법, 전기통신사업법 등 서비스의 운영, 유지와 관련 있는 법규를 준수합니다.</li>
											</ul>

											<h3>제18조(고객의 의무)</h3>
											<ul>
												<li>① 고객은 서비스를 이용할 때 다음 각 호의 행위를 하여서는 아니 됩니다.</li>
												<ol>
													<li>이용 신청 또는 변경 시 허위 사실을 기재하는 행위</li>
													<li>회사의 서비스 정보를 이용하여 얻은 정보를 회사의 사전 승낙 없이 복제 또는 유통시키거나 상업적으로 이용하는 행위</li>
													<li>타인의 명예를 손상시키거나 불이익을 주는 행위</li>
													<li>게시판 등에 음란물을 게재하거나 음란사이트를 연결(링크)하는 행위</li>
													<li>회사의 저작권, 제3자의 저작권 등 기타 권리를 침해하는 행위</li>
													<li>공공질서 및 미풍양속에 위반되는 내용의 정보, 문장, 도형, 음성 등을 타인에게 유포하는 행위</li>
													<li>서비스와 관련된 설비의 오동작이나 정보 등의 파괴 및 혼란을 유발시키는 컴퓨터 바이러스 감염 자료를 등록 또는 유포하는 행위</li>
													<li>서비스 운영을 고의로 방해하거나 서비스의 안정적 운영을 방해할 수 있는 정보 및 수신자의 명시적인 수신거부의사에 반하여 광고성 정보를 전송하는 행위</li>
													<li>타인으로 가장하는 행위 및 타인과의 관계를 허위로 명시하는 행위</li>
													<li>다른 고객의 개인정보를 수집, 저장, 공개하는 행위</li>
													<li>자신 또는 타인에게 재산상의 이익을 주거나 타인에게 손해를 가할 목적으로 허위의 정보를 유통시키는 행위</li>
													<li>서비스에 게시된 각종 정보를 무단으로 변경하는 행위</li>
													<li>관련 법령에 의하여 그 전송 또는 게시가 금지되는 정보(컴퓨터 프로그램 포함)의 전송 또는 게시 행위</li>
													<li>회사의 직원이나 운영자를 가장하거나 사칭하여 또는 타인의 명의를 도용하여 글을 게시하거나 메일을 발송하는 행위</li>
													<li>컴퓨터 소프트웨어, 하드웨어, 전기통신 장비의 정상적인 가동을 방해, 파괴할 목적으로 고안된 소프트웨어 바이러스, 기타 다른 컴퓨터 코드, 파일, 프로그램을 포함하고 있는 자료를 게시하거나 e-mail(전자우편)으로 발송하는 행위</li>
													<li>기타 불법적이거나 부당한 행위</li>
												</ol>
												<li>② 고객은 관계 법령, 본 약관의 규정, 이용안내 및 서비스상에 공지한 주의사항, 회사가 통지하는 사항 등을 준수하여야 하며, 기타 회사의 업무에 방해되는 행위를 하여서는 아니 됩니다.</li>
												<li>③ 고객은 회사에서 공식적으로 인정한 경우를 제외하고는 서비스를 이용하여 상품을 판매하는 영업 활동을 할 수 없으며 특히 해킹, 광고를 통한 수익, 음란사이트를 통한 상업행위, 상용소프트웨어 불법배포 등을 할 수 없습니다. 이를 위반하여 발생한 영업 활동의 결과 및 손실, 관계기관에 의한 구속 등 법적 조치 등에 관해서는 회사가 책임을 지지 않으며, 고객은 이와 같은 행위와 관련하여 회사에 대하여 손해배상 의무를 집니다.</li>
											</ul>

											<h3>제19조(고객 아이디와 비밀번호 관리에 대한 의무와 책임)</h3>
											<ul>
												<li>① 회사는 서비스 신청 시 수수료를 부과할 수 있으므로, 고객은 고객의 아이디 및 비밀번호 관리를 철저히 하여야 합니다.</li>
												<li>② 고객이 자신의 아이디와 비밀번호에 대하여 관리소홀 또는 부정사용하거나 제3자에게 권한 없이 고객의 아이디나 비밀번호를 이용하여 전자금융거래 할 수 있음을 알거나 쉽게 알 수 있는 경우에도 아이디나 비밀번호를 누설, 노출 또는 이를 방치하는 행위로 인해 사고가 발생한 경우, 그 책임의 전부 또는 일부를 고객의 부담으로 할 수 있습니다. 단, 회사의 시스템 고장 등 회사의 책임 있는 사유로 발생하는 문제에 대해서는 회사가 책임을 집니다.</li>
												<li>③ 고객의 아이디 및 비밀번호를 제3자에게 대여하거나 그 사용을 위임하거나, 양도 또는 담보의 목적으로 제공하거나, 기타 방식으로 이용하게 해서는 안되며, 고객 본인의 아이디 및 비밀번호를 도난 당하거나 제3자가 사용하고 있음을 인지하는 경우에는 바로 회사에 통보하고 회사의 안내가 있는 경우 그에 따라야합니다.</li>
												<li>④ 고객은 회사가 보안강화를 위하여 전자금융거래시 요구하는 추가적인 보안조치를 정당한 사유 없이 거부할 수 없으며, 이를 거부함으로 인하여 발생하는 제반 문제에 대해서는 회사가 책임을 지지 않습니다.</li>
											</ul>

											<h3>제20조(고객의 개인정보보호)</h3>
											<p>회사는 관련법령이 정하는 바에 따라서 고객 등록정보를 포함한 고객의 개인정보를 보호하기 위하여 노력합니다. 고객의 개인정보보호에 관해서는 관련법령 및 회사가 정하는 "개인정보보호정책"에 정한 바에 의합니다. 단, 회사는 고객의 원활한 서비스 제공을 위해 고객의 정보, 고객의 본 약관에 대한 동의 및 제휴은행의 가상계좌 사용용도를 제휴은행에 한해 제공할 수 있습니다.</p>

											<h3>제21조(개인정보의 위탁)</h3>
											<p>회사는 수집된 개인정보의 취급 및 관리 등의 업무(이하 본 조에서 "업무")를 스스로 수행함을 원칙으로 하나, 필요한 경우 법령이 허용하는 범위 내에서 업무의 일부 또는 전부를 회사가 선정한 제3자에 위탁할 수 있습니다.</p>

											<br>
											<h4>제4장 계약해지 및 이용제한</h4>
											<br>

											<h3>제22조(계약해지 및 이용제한)</h3>
											<ul>
												<li>① 고객이 서비스 이용계약을 해지하고자 할 경우에는 본인이 사이트 상에서 또는 회사가 정한 별도의 이용방법으로 회사에 해지신청을 하여야 합니다.</li>
												<li>② 회사는 고객이 본 약관 제11조에 규정한 서비스 수수료 지급 의무 및 제16조에 규정한 고객의 의무를 이행하지 않을 경우 사전 통지 후 이용계약을 해지하거나 또는 서비스 이용을 중지할 수 있습니다.</li>
												<li>③ 본 조 제2항의 회사 조치에 대하여 고객은 회사가 정한 절차에 따라 이의신청을 할 수 있습니다.</li>
												<li>④ 본 조 제3항의 이의가 정당하다고 회사가 인정하는 경우, 회사는 즉시 서비스의 이용을 재개합니다.</li>
											</ul>

											<h3>제23조(양도 등 처분금지)</h3>
											<p>고객은 회사의 서면에 의한 사전 동의 없이는 본 서비스의 이용권한, 기타 이용 계약상 지위를 타인에게 양도, 증여 등 일체의 처분행위를 할 수 없습니다.</p>

											<br>
											<h4>제5장 손해배상 등</h4>
											<br>

											<h3>제24조(준거법 및 분쟁해결)</h3>
											<ul>
												<li>① 본 약관 및 회사와 고객간에 체결된 계약의 해석, 적용 및 이행의 준거법은 대한민국법으로 합니다.</li>
												<li>② 본 약관 및 회사와 고객간에 체결된 계약과 관련한 일체의 분쟁은 민사소송법에서 정한 관할권 있는 법원의 소송으로 해결합니다.</li>
											</ul>

											<h3>제25조 (약관 외 준칙)</h3>
											<p>이 약관에서 정하지 아니한 사항에 대하여는 전자금융거래법, 전자상거래 등에서의 소비자 보호에 관한 법률, 통신판매에 관한 법률, 여신전문금융업법 등 소비자보호 관련 법령에서 정한 바에 따릅니다.</p>

											<p>
												부칙 < 제 1 호, 2021. 04. 29><br>
												본 약관은 2021년 04월 29일부터 시행한다.
											</p>
											<p>
												부칙 < 제 2 호, 2021. 09. 02><br>
												본 약관은 2021년 09월 02일부터 시행합니다.
											</p>
										</div>
									</div>
								</div>
							</div>

							<div class="row no-gutters align-items-center title-agreement-unit">
								<div class="col">
									<h4>개인정보처리방침</h4>
								</div>
								<div class="col text-right form-inline">
									<div class="form-check form-check-inline ml-auto">
										<input class="form-check-input checkbox-child" type="checkbox" id="aggreement-04"> <label for="aggreement-04"><span class="mr-2"></span>동의</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col">
									<div class="form-control" style="padding: 25px; height: 300px; border: 1px solid #d3d3d3; overflow: hidden; overflow-y: auto;">
										<div class="policy" style="margin: 0;">
											<p>
												(주)핑거(이하 ‘회사’라 함)는 정보통신망 이용촉진 및 정보보호에 관한 법률, 통신비밀보호법, 전기통신사업법, 전자거래기본법 등 정보통신서비스제공자가 준수하여야 할 관련 법령상의 개인정보보호 규정을 준수하며, 관련 법령에 의거한 개인정보처리방침을 정하여 이용자 권익 보호에 최선을 다하고 있습니다.<br>
												회사는 개인정보처리방침을 통하여 고객님께서 제공하시는 개인정보가 어떠한 용도와 방식으로 이용되고 있으며, 개인정보보호를 위해 어떠한 조치가 취해지고 있는지 알려드립니다.
											</p>
											<h3>1. 수집하는 개인정보의 항목 및 수집방법 </h3>
											<p>
												가. 회사는 회원가입, 원활한 고객상담, 각종 서비스 제공 등을 위해 최초 회원가입 당시 아래와 같은 개인정보를 수집하고 있습니다.
											</p>
											<ul>
												<li>o 개인 : 이름, 아이디, 생년월일, 비밀번호, 전화번호, 휴대전화번호, 이메일, 주소, 거래대금지급정보(결제은행, 계좌번호, 계좌명)</li>
												<li> o 국가기관, 법인, 개인사업자 : 회사명, 아이디, 비밀번호, 사업자등록번호, 대표자명 및 담당자명, 전화번호, 대표자 휴대폰번호 및 담당자 휴대폰번호, 이메일, 주소, 업종 및 업태, 쇼핑몰URL, 사업장주소, 대표번호, 팩스번호, 거래대금지급정보(결제은행, 계좌번호, 계좌명) 및 상품 또는 용역 거래정보</li>
												<li>o 서비스 이용정보 : 서비스 이용 기록, 접속 로그, 쿠키, 접속 IP 정보, 결제기록</li>
											</ul>
											<p>
												나. 회사는 다음과 같은 방법으로 개인정보를 수집합니다.
											</p>
											<ul>
												<li>o 홈페이지, 서면양식, 팩스, 전화, 상담게시판, 이메일, 이벤트 응모</li>
												<li>o 제휴은행으로부터의 제공</li>
											</ul>
											<p>
												다. 단, 기본적 인권 침해의 우려가 있는 민감한 개인정보(인종 및 민족, 사상 및 신조, 출신지 및 본적지, 정치적 성향 및 범죄기록, 건강상태 및 성생활 등)는 수집하지 않습니다. 회사는 동의가 있거나 관련법령의 규정에 의한 경우를 제외하고는 어떠한 경우에도 『개인정보의 수집 및 이용목적』에서 고지한 범위를 넘어 개인정보를 이용하거나 제3자에게 제공하지 않습니다.
											</p>
											<h3>2. 개인정보의 수집 및 이용목적</h3>
											<p>
												가. 회사는 수집한 개인정보를 다음의 목적을 위해 처리합니다. 처리한 개인정보는 다음의 목적 이외의 용도로는 사용되지 않으며 이용 목적이 변경될 시에는 사전 동의를 구할 예정입니다.
											</p>
											<ul>
												<li>ㅇ 홈페이지 회원가입 및 관리<br>
													회원 가입의사 확인, 회원제 서비스 제공에 따른 본인 식별·인증, 회원자격 유지·관리, 제한적 본인확인제 시행에 따른 본인확인, 서비스 부정이용 방지, 각종 고지·통지, 고충처리, 분쟁 조정을 위한 기록 보존 등을 목적으로 개인정보를 처리합니다.</li>
												<li>ㅇ 민원사무 처리<br>
													민원인의 신원 확인, 민원사항 확인, 사실조사를 위한 연락·통지, 처리결과 통보 등을 목적으로 개인정보를 처리합니다.</li>
												<li>ㅇ 재화 또는 서비스 제공<br>
													서비스 제공에 관한 계약이행 및 서비스 제공에 따른 요금정산 특정 맞춤서비스 제공, 청구서 등 발송, 본인인증, 요금결제 및 요금추심 등을 목적으로 개인정보를 처리합니다.</li>
												<li>ㅇ 마케팅 및 광고에의 활용<br>
													신규 서비스(제품) 개발, 이벤트 및 광고성 정보 제공 및 참여기회 제공, 인구통계학적 특성에 따른 서비스 제공 및 광고 게재, 접속빈도 파악 또는 회원의 서비스 이용에 대한 통계 등을 목적으로 개인정보를 처리합니다.</li>
												<li>ㅇ 회사와 거래를 위한 고객확인(CCD/EDD: 거래목적, 자금의 출처 및 대리인 정보 등) 및 검증<br>
													- 특정 금융거래정보의 보고 및 이용 등에 관한 법률 및 동 법률 시행령에 따른 법령상 의무이행(고객확인, 특정금융거래보고 등)</li>
											</ul>
											<h3>3. 개인정보의 공유 및 제공3. 개인정보의 공유 및 제공</h3>
											<p>
												가. 회사는 이용자들의 개인정보를 “2. 개인정보의 수집 및 이용목적”에서 고지한 범위 내에서 사용하며, 이용자의 사전 동의 없이는 동 범위를 초과하여 이용하거나 원칙적으로 이용자의 개인정보를 외부에 공개하지 않습니다. 다만, 아래의 경우에는 예외로 합니다.
											</p>
											<ul>
												<li>ㅇ 이용자들이 사전에 공개에 동의한 경우</li>
												<li>ㅇ 법령의 규정에 의거하거나, 수사 목적으로 법령에 정해진 절차와 방법에 따라 수사기관의 요구가 있는 경우</li>
											</ul>
											<h3>4. 개인정보의 취급위탁</h3>
											<p>
												회사는 이용자의 동의 없이 개인정보를 외부업체에 위탁하지 않습니다. 향후 그러한 필요가 생길 경우, 위탁 대상자와 위탁업무 내용에 대해 회원에게 통지하고 필요한 경우 사전 동의를 받도록 하겠습니다.
											</p>
											<h3>5. 개인정보의 보유 및 이용기간</h3>
											<p>
												원칙적으로, 개인정보 수집 및 이용목적이 달성된 후에는 해당 정보를 지체 없이 파기합니다. 단, 관계법령의 규정에 의하여 보존할 필요가 있는 경우 회사는 아래와 같이 관계법령에서 정한 일정한 기간 동안 회원정보를 보관합니다.
											</p>
											<ul>
												<li> o 관계 법령에 의한 정보보유 사유<br>
													상법, 전자상거래 등에서의 소비자보호에 관한 법률 등 관계법령의 규정에 의하여 보존할 필요가 있는 경우, 회사는 관계법령에서 정한 일정한 기간 동안 회원정보를 보관합니다. 이 경우 회사는 보관하는 정보를 그 보관의 목적으로만 이용하며 보존기간은 아래와 같습니다.<br>
													- 계약 또는 청약철회 등에 관한 기록 : 5년(전자상거래 등에서의 소비자보호에 관한 법률)<br>
													- 대금결제 및 재화 등의 공급에 관한 기록 : 5년(전자상거래 등에서의 소비자보호에 관한 법률)<br>
													- 소비자의 불만 또는 분쟁처리에 관한 기록 : 3년(전자상거래 등에서의 소비자보호에 관한 법률)<br>
													- 본인확인에 관한 기록 : 6개월(정보통신 이용촉진 및 정보보호 등에 관한 법률)<br>
													- 방문에 관한 기록 : 3개월(통신비밀보호법)
												</li>
											</ul>
											<h3>6. 개인정보의 파기절차 및 방법</h3>
											<p>
												가. 회사는 원칙적으로 개인정보 수집 및 이용목적이 달성된 후에는 해당 정보를 지체 없이 파기합니다.<br>
												나. 파기절차 및 방법은 다음과 같습니다.
											</p>
											<ul>
												<li>o 파기절차<br>
													이용자가 입력한 정보는 목적 달성 후 별도의 DB에 옮겨져(종이의 경우 별도의 서류) 내부 방침 및 기타 관련 법령에 따라 일정기간 저장된 후 혹은 즉시 파기됩니다. 이 때, DB로 옮겨진 개인정보는 법률에 의한 경우가 아니고서는 다른 목적으로 이용되지 않습니다.
												</li>
												<li>ㅇ 파기기한<br>
													이용자의 개인정보는 개인정보의 보유기간이 경과된 경우에는 보유기간의 종료일로부터 5일 이내에, 개인정보의 처리 목적 달성, 해당 서비스의 폐지, 사업의 종료 등 그 개인정보가 불필요하게 되었을 때에는 개인정보의 처리가 불필요한 것으로 인정되는 날로부터 5일 이내에 그 개인정보를 파기합니다.
												</li>
												<li>o 파기방법<br>
													전자적 파일 형태의 정보는 기록을 재생할 수 없는 기술적 방법을 사용합니다.<br>
													종이에 출력된 개인정보는 분쇄기로 분쇄하거나 소각을 통하여 파기합니다.
												</li>
											</ul>
											<h3>7. 정보주체의 권리, 의무 및 그 행사방법</h3>
											<p>
												가. 이용자는 개인정보주체로서 다음과 같은 권리를 행사할 수 있습니다.
											</p>
											<ul>
												<li>o 개인정보 열람요구</li>
												<li>o 오류 등이 있을 경우 정정 요구</li>
												<li>o 삭제요구</li>
												<li>o 처리정지 요구</li>
											</ul>
											<p>
												나. 전항에 따른 정보주체의 권리 행사는 회사에 대해 개인정보 보호법 시행규칙 별지 제8호 서식에 따라 서면, 전자우편, 모사전송(FAX) 등을 통하여 하실 수 있으며 회사는 이에 대해 지체 없이 조치하겠습니다.<br>
												다. 정보주체가 개인정보의 오류 등에 대한 정정 또는 삭제를 요구한 경우에는 회사는 정정 또는 삭제를 완료할 때까지 당해 개인정보를 이용하거나 제공하지 않습니다.<br>
												라. 이용자의 권리 행사는 정보주체의 법정대리인이나 위임을 받은 자 등 대리인을 통하여 하실 수 있습니다. 이 경우 개인정보 보호법 시행규칙 별지 제11호 서식에 따른 위임장을 제출하셔야 합니다.
											</p>
											<h3>8. 개인정보에 관한 기술적/관리적 보호 대책</h3>
											<p>
												가. 회사는 이용자의 개인정보를 취급함에 있어 개인정보가 분실, 도난, 누출, 변조 또는 훼손되지 않도록 안전성 확보를 위하여 다음과 같은 기술적/관리적 대책을 강구하고 있습니다.
											</p>
											<ul>
												<li>
													o 개인정보 암호화<br>
													- 이용자 아이디(ID)의 비밀번호는 암호화되어 저장 및 관리되고 있어 본인만이 알고 있으며, 개인정보의 확인 및 변경도 비밀번호를 알고 있는 본인에 의해서만 가능합니다.<br>
													- 이용자의 개인정보 중 중요한 데이터는 파일 및 전송 데이터를 암호화 하거나 파일 잠금 기능을 사용하는 등의 별도 보안기능을 사용하고 있습니다.
												</li>
												<li>
													o 해킹 등에 대비한 대책<br>
													- 회사는 해킹이나 컴퓨터 바이러스 등에 대해 이용자의 개인정보가 유출되거나 훼손되는 것을 막기 위해 최선을 다하고 있습니다. 개인정보의 훼손에 대비해서 자료를 수시로 백업하고 있고, 최신백신프로그램을 이용하여 이용자들의 개인정보나 자료가 누출되거나 손상되지 않도록 방지하고 있으며, 암호화통신 등을 통하여 네트워크상에서 개인정보를 안전하게 전송할 수 있도록 하고 있습니다.<br>
													- 침입차단시스템을 이용하여 외부로부터의 무단 접근을 통제하고 있으며, 기타 시스템적으로 보안성을 확보하기 위한 가능한 모든 기술적 장치를 갖추려 노력하고 있습니다.
												</li>
												<li>
													o 취급 직원의 최소화 및 교육<br>
													- 회사는 개인정보관련 취급 직원은 담당자에게 한정시키고 있고 이를 위한 별도의 비밀번호를 부여하여 정기적으로 갱신하고 있으며, 담당자에 대한 수시 교육을 통하여 회사 개인정보처리방침의 준수를 항상 강조하고 있습니다.
												</li>
												<li>
													o 개인정보보호전담기구의 운영<br>
													- 사내 개인정보보호전담기구 등을 통하여 회사 개인정보처리방침의 이행사항 및 담당자의 준수여부를 확인하여 문제가 발견될 경우 즉시 수정하고 바로 잡을 수 있도록 노력하고 있습니다.<br>
													- 단, 이용자 본인의 부주의나 인터넷상의 문제로 ID, 비밀번호, 주민등록번호 등 개인정보가 유출되어 발생한 문제에 대해 회사는 일체의 책임을 지지 않습니다.
												</li>
												<li>
													o 정기적인 자체 감사 실시<br>
													개인정보 취급 관련 안정성 확보를 위해 정기적(분기 1회)으로 자체 감사를 실시하고 있습니다.
												</li>
												<li>
													o 내부관리계획의 수립 및 시행<br>
													개인정보의 안전한 처리를 위하여 내부관리계획을 수립하고 시행하고 있습니다.
												</li>
												<li>
													o 접속기록의 보관 및 위변조 방지<br>
													개인정보처리시스템에 접속한 기록을 최소 6개월 이상 보관, 관리하고 있으며, 접속 기록이 위변조 및 도난, 분실되지 않도록 보안기능 사용하고 있습니다.
												</li>
												<li>
													o 개인정보에 대한 접근 제한<br>
													개인정보를 처리하는 데이터베이스시스템에 대한 접근권한의 부여, 변경, 말소를 통하여 개인정보에 대한 접근통제를 위하여 필요한 조치를 하고 있으며 침입차단시스템을 이용하여 외부로부터의 무단 접근을 통제하고 있습니다.
												</li>
												<li>
													o 문서보안을 위한 잠금 장치 사용<br>
													개인정보가 포함된 서류, 보조저장매체 등을 잠금 장치가 있는 안전한 장소에 보관하고 있습니다.
												</li>
												<li>
													o 비인가자에 대한 출입 통제<br>
													개인정보를 보관하고 있는 물리적 보관 장소를 별도로 두고 이에 대해 출입통제 절차를 수립, 운영하고 있습니다.
												</li>
											</ul>
											<h3>9. 개인정보관리책임자 및 담당자의 연락처</h3>
											<p>
												가. 회사는 개인정보 처리에 관한 업무를 총괄해서 책임지고, 개인정보 처리와 관련한 정보주체의 불만처리 및 피해구제 등을 위하여 아래와 같이 개인정보 보호책임자를 지정하고 있습니다.
											</p>
											<table class="table table-primary mb-5">
												<thead>
												<tr>
													<th></th>
													<th>개인정보보호 최고 책임자</th>
													<th>개인정보보호 담당자</th>
												</tr>
												</thead>
												<tbody>
												<tr>
													<th>이름</th>
													<td>장재화</td>
													<td>김진환</td>
												</tr>
												<tr>
													<th>직위</th>
													<td>이사</td>
													<td>선임차장</td>
												</tr>
												<tr>
													<th>메일</th>
													<td>jhchang9072@finger.co.kr</td>
													<td>Jinhwan@finger.co.kr</td>
												</tr>
												</tbody>
											</table>
											<p>나. 아래의 기관은 회사와는 별개의 기관으로서, 회사의 자체적인 개인정보 불만처리, 피해구제 결과에 만족하지 못하시거나 보다 자세한 도움이 필요하시면 문의하여 주시기 바랍니다.</p>
											<ul>
												<li>• 개인정보분쟁조정위원회(www.kopico.go.kr) ☎ 국번없이 1833-6972</li>
												<li>• 한국인터넷진흥원 개인정보침해신고센터(privacy.kisa.or.kr) ☎ 국번없이 118</li>
												<li>• 개인정보보호협회 개인정보보호인증(www.eprivacy.or.kr) ☎02-580-0533~4</li>
												<li>• 대검찰청 사이버수사과(www.spo.go.kr) ☎ 국번없이 1301</li>
												<li>• 경찰청 사이버안전국(cyberbureau.police.go.kr) ☎ 국번없이 182</li>
											</ul>
											<h3>10. 개인정보 처리방침 변경</h3>
											<p>가. 이 개인정보처리방침은 시행일로부터 적용되며, 법령 및 방침에 따른 변경내용의 추가, 삭제 및 정정이 있는 경우에는 변경사항의 시행 7일 전부터 공지사항을 통하여 고지할 것입니다.</p>
											<p>
												부칙<br>
												<br>
												제 1 호, 2018.3.21.<br>
												본 약관은 2018년 03월 28일부터 시행한다.<br>
												<br>
												제 2 호, 2020.4.29.<br>
												본 약관은 2020년 5월 6일부터 시행한다.<br>
												<br>
												제 3 호, 2021.3.22.<br>
												본 약관은 2021년 3월 22일부터 시행한다.
											</p>
										</div>
									</div>
								</div>
							</div>
							<div class="row bt-1-e6e6e6 mt-4 pt-4">
								<div class="col text-center">
									<button class="btn btn-primary btn-outlined" onclick="fn_insInfoCancel();">취소</button>
									<button class="btn btn-primary" onclick="fn_AcceptTerms();">다음</button>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="step3 display-none">
					<div class="col">
						<div class="reset-id-n-pw">
							<div class="row">
								<div class="col">
									<div class="description-box">
										<ul class="unordered-blue-dot-list mt-3 mb-3 ml-3">
											<li>기존 아이디, 비밀번호와 다르게 설정해주시기 바랍니다.</li>
											<li>주민등록번호, 동일숫자, 연속숫자 등 유추하기 쉬운 비밀번호 설정불가</li>
											<li>영&middot;숫자&middot;특수 문자 중 2가지 이상 혼합하여 8자리 이상 20자리 이하 구성</li>
										</ul>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col">
									<table class="table table-primary">
										<tbody>
											<tr>
												<th rowspan="2">아이디 재설정</th>
												<td class="form-inline">
													<div class="form-check form-check-inline">
														<input class="form-check-input" type="radio" id="update-new-id" name="id-modification" onclick="" checked>
														<label for="update-new-id"><span class="mr-2"></span>아이디 변경</label>
													</div>
													<div class="form-check form-check-inline">
														<input class="form-check-input" type="radio" id="use-old-id" name="id-modification" onclick="">
														<label for="use-old-id"><span class="mr-2"></span>기존 아이디 사용</label>
													</div>
												</td>
											</tr>
											<tr>
												<td class="form-inline">
													<input type="text" class="form-control col-6" id="resetId" onkeyup="fn_idOverlapCk('N');">
													<button class="btn btn-sm btn-d-gray ml-1" onclick="fn_idOverlap();">아이디 중복검사</button>
													<div id="resetIdSuc" style="color: blue; display: none;"><font size="2">&nbsp;사용가능</font></div>
                                    				<div id="resetIdErr" style="color: red; display: none;"><font size="2">&nbsp;사용불가</font></div>
												</td>
											</tr>
											<tr>
												<th>현재 비밀번호</th>
												<td class="form-inline"><input type="password" class="form-control  col-6" id="nowPassword" minlength="8" maxlength="20"></td>
											</tr>
											<tr>
												<th>신규 비밀번호</th>
												<td class="form-inline"><input type="password" class="form-control  col-6" id="newPassword" minlength="8" maxlength="20"></td>
											</tr>
											<tr>
												<th>신규 비밀번호 확인</th>
												<td class="form-inline"><input type="password" class="form-control  col-6" id="confPassword" minlength="8" maxlength="20"></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<div class="row mt-4 pt-4">
								<div class="col text-center">
									<button class="btn btn-primary btn-outlined" onclick="fn_insInfoCancel();">취소</button>
									<button class="btn btn-primary" onclick="fn_memberInfoModify();">다음</button>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="step4 display-none">
					<div class="col-lg-7 col-md-9 col-sm-10 col-12 mx-auto">
						<div class="member-auth-completed">
							<div class="row no-gutters mb-4 mt-4 align-items-center">
								<div class="col-4">
									<img src="/assets/imgs/common/img-monitor-pw-tick.png">
								</div>
								<div class="col">
									서비스 개시를 위한 모든 절차를 마치셨습니다.
									<p>이제 변경하신 비밀번호로 바로 로그인 하신 후 신한 다모아 서비스 사이트를 이용하실 수 있습니다.
									</p>
								</div>
							</div>
							<div class="row no-gutters mb-4 bt-1-e6e6e6 login-box">
								<div class="col">
									<div class="row mb-2">
										<div class="col">
											<input type="text" class="form-control" placeholder="아이디" id="orgId" maxlength="20">
										</div>
									</div>
									<div class="row">
										<div class="col">
											<input type="password" class="form-control" placeholder="비밀번호" id="orgPw" maxlength="20" onkeypress="if(event.keyCode == 13){fn_goLogin();}">
										</div>
									</div>
								</div>
								<div class="col-4 ml-2">
									<button type="button" class="btn btn-primary btn-flexible-height btn-lookup-id" onclick="fn_goLogin();">로그인</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="login-instruction">
		<div class="container">
			<div class="row">
				<div class="col col-lg-6 col-md-6 col-12">
					<div class="row align-items-center">
						<div class="col-sm-4 col-12 text-center">
							<img src="/assets/imgs/common/icon-chat-balloon-2.png">
						</div>
						<div class="col-sm-8 col-12">
							<p>상기 내용이 사실과 다르거나 수정할 내용이나 문의사항이 있다면 고객센터로 연락하셔서 수정요청을 해주시기 바랍니다.</p>
						</div>
					</div>
				</div>
				<div class="col col-lg-3 col-md-3 col-6">
					<div class="row align-items-center">
						<div class="col-sm-4 col-12 text-center">
							<img src="/assets/imgs/common/icon-call.png">
						</div>
						<div class="col-sm-8 col-12">
							<strong>고객센터</strong>
							<p>02-786-8201</p>
						</div>
					</div>
				</div>
				<div class="col col-lg-3 col-md-3 col-6">
					<div class="row align-items-center">
						<div class="col-sm-4 col-12 text-center">
							<img src="/assets/imgs/common/icon-clock.png">
						</div>
						<div class="col-sm-8 col-12">
							<strong>운영시간(평일)</strong>
							<p>09:00-12:00, 13:00-17:00</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" />

<script src="/assets/js/swiper.js"></script>

<script>
    $(document).ready(function () {
        // 전체동의 시 checkbox
        $(".checkbox-parent").click(function () {
            if ($(".checkbox-parent").is(":checked")) {
                $(".checkbox-child").prop("checked", true);
            } else if ($(".checkbox-parent").not(":checked")) {
                $(".checkbox-child").prop("checked", false);
            }
        });

        var numCheckbox = $(".checkbox-child").length;

        // Child checkbox 시
        $(".checkbox-child").click(function () {
            if ($(this).is(":checked")) {
                if ($(".checkbox-child:checked").length == numCheckbox) {
                    $(".checkbox-parent").prop("checked", true);
                }
            } else if ($(this).not(":checked")) {
                $(".checkbox-parent").prop("checked", false);
            }
        });

        fn_orgInfoSearch();
    });

    var oldId = '${userId}';
    $(resetId).val(oldId);

    $("label[for=use-old-id]").click(function () {
    	fn_idOverlapCk("K"); // 기존로그인id로 사용가능
        $("#resetId").prop("readonly", true).val(oldId);
    });

    $("label[for=update-new-id]").click(function () {
    	fn_idOverlapCk('N'); // 기존로그인id로 사용가능
        $("#resetId").prop("readonly", false);
    });

    var mySwiper = new Swiper('.swiper-container', {
		// Optional parameters
        direction: 'horizontal',
        autoHeight: true,
        loop: false,
        autoplay: false,
        simulateTouch: false,
    });
</script>

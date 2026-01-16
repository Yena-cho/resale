<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/></s:authorize>

<style>
    .swiper-slide {
        padding: .25rem;
    }

    #checkId, #checkPwd {
        display: block;
    }

    #okId, #noId, #confSms, #newPwd, #okPwd {
        display: none;
    }
</style>

<script>
var firstDepth = null;
var secondDepth = null;
</script>

<script type="text/javascript">
//확인
function fn_idSearch() {
    if (!$('#corNum').val()) {
        $('#message').text("사업자번호/고유단체번호를 입력해주세요!");
        $('#corNum').focus();
        return;
    }
    if (!$('#corAccNum').val()) {
        $('#message').text("수납계좌번호를 입력해주세요!");
        $('#corAccNum').focus();
        return;
    }

    if ($('#corNum').val() && $('#corAccNum').val()) {
        var url = "/common/idSearch";
        var param = {
            chaOffNo: $('#corNum').val(),
            feeAccNo: $('#corAccNum').val()
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                if (data.id != null) {
                    $('#selName').text(data.id.loginName);
                    $('#selId').text(data.id.loginId);

                    $("#checkId").css("display", "none");
                    $("#okId").css("display", "block");

//                    mySwiperId.slideTo(1);
                } else {
                    $("#checkId").css("display", "none");
                    $("#noId").css("display", "block");

//                    mySwiperId.slideTo(2);
                }
            }
        });
    }
}

function fn_textClear(str) {
    if (str == 'PW') {
        $('#messagePw').text('');
    } else {
        $('#message').text('');
    }
}

// 아이디찾기 실패 확인
function fn_reIdSearch() {
    $('#corNum').val('');
    $('#corAccNum').val('');

    $("#checkId").css("display", "block");
    $("#noId").css("display", "none");
}

//비밀번호 찾기
function fn_reset() {
    if (!$('#resetId').val()) {
        $('#messagePw').text("아이디를 입력해주세요!");
        $('#resetId').focus();
        return;
    } else if (!$('#resetOffNo').val()) {
        $('#messagePw').text("사업자번호/고유단체번호를 입력해주세요!");
        $('#resetOffNo').focus();
        return;
    } else if (!$('#resetAccNo').val()) {
        $('#messagePw').text("수납계좌번호를 입력해주세요!");
        $('#resetAccNo').focus();
        return;
    }

    if ($('#resetId').val() && $('#resetOffNo').val() && $('#resetAccNo').val()) {
        var url = "/common/passwordSearch";
        var param = {
            loginId: $('#resetId').val(),
            chaOffNo: $('#resetOffNo').val(),
            feeAccNo: $('#resetAccNo').val()
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                if (data.pw != null) {
                    $('#resetCurHp').val(data.pw.chrHp);

//                    mySwiperPw.slideTo(1);

                    $("#checkPwd").css("display", "none");
                    $("#confSms").css("display", "block");
                } else {
                    var msg = "";
                    if (data.idx == 1) {
                        msg = "아이디를 정확하게 입력해주세요!";
                    } else if (data.idx == 2) {
                        msg = "사업자번호/고유단체번호를 정확하게 입력해주세요!";
                    } else {
                        msg = "수납계좌번호를 정확하게 입력해주세요!";
                    }
                    swal({
                        type: 'error',
                        text: msg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $("#resetId").focus();
                    });
                    return;
                }
            }
        });
    }
}

function fn_loginPageMove() {
    location.href = "/common/login";
}

//인증번호 발송, 재발송
var timer = null;
var cnt = 180;

function fn_confirmSend() {
    $('#certified').attr('disabled', false);
    $('#otpNo').val('');
    $('#countTime').css('display', 'block');

    var url = "/common/login/optNoSend";
    var param = {
        loginId: $('#resetId').val(),
        hpNo: $('#resetCurHp').val(),
        loginYn: 'N'
    };

    $.ajax({
        type: "post",
        async: true,
        url: url,
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(param),
        success: function (data) {
            if (data.retCode != '0001') {
                swal({
                    type: 'info',
                    text: "인증번호가 발송되었습니다. 인증번호를 정확히 입력해 주세요.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });

                cnt = 180;
                getTime(); // 3분 타이머

                $('#otpNoCon').val(data.otpNo);
                $('#optNoSend').css('display', 'none');
                $('#optNoReSend').css('display', 'block');
            } else {
                swal({
                    type: 'info',
                    text: data.retMsg,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
            }
        },
        error: function (data) {
            swal({
                type: 'info',
                text: '메시지 전송요청 중 오류가 발생 하였습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
        }
    });
}

//인증
function fn_otpNoConfirm() {
    if (!$('#otpNo').val()) {
        swal({
            type: 'info',
            text: "인증번호를 입력해주세요.",
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
        });
        return;
    }
    if ($('#otpNo').val() != $('#otpNoCon').val()) {
        swal({
            type: 'info',
            text: "인증번호가 유효하지 않습니다.",
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
        });
        return;
    }
    if (cnt < 1) {
        swal({
            type: 'info',
            text: "유효시간이 경과되었습니다.",
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
        });
        return;
    }
    if ($('#otpNo').val() == $('#otpNoCon').val()) {

        var url = "/common/login/smsSuccess";
        var param = {
            loginId: $('#resetId').val(),
            hpNo: $('#resetCurHp').val(),
            otpNo: $('#otpNoCon').val(),
            loginYn: 'Y'
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                swal({
                    type: 'info',
                    text: "SMS 인증이 완료되었습니다.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                $('#smsOtpY').val('Y');
                stop();
                $('#countTime').css('display', 'none');
                $('#certified').attr('disabled', true);

                fn_confirm();
            },
            error: function (data) {
                swal({
                    type: 'info',
                    text: "SMS 인증도중 오류가 발생하였습니다.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
            }
        });
    }
}

//타이머
function getTime() {

    if (cnt >= 0) {
        var minute = setAddZero(Math.floor(cnt / 60));
        var sec = setAddZero(cnt % 60);

        $('#countTime').text(minute + ":" + sec);
        cnt = cnt - 1;
        timer = setTimeout("getTime()", 1000); //1초마다 getTime함수호출
    } else {
        stop();
    }
}

//타이머종료
function stop() {
    clearTimeout(timer);
    cnt = 5;
    $('#otpNoCon').val('');
}

//인증확인
function fn_confirm() {
    if ($('#smsOtpY').val() != 'Y') {
        swal({
            type: 'info',
            text: "SMS 인증이 완료되지 않았습니다.",
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
        });
        return;
    }

    $("#confSms").css("display", "none");
    $("#newPwd").css("display", "block");

//    mySwiperPw.slideTo(2);
}

//비밀번호 재설정
function fn_passwordChange() {
    var newPw = $('#newPassword').val();
    var confPw = $('#conPassword').val();

    var num = newPw.search(/[0-9]/g);
    var eng = newPw.search(/[a-zA-Z]/gi);
    var spe = newPw.search(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi);

    if (!newPw) {
        $('#messageNew').text("새 비밀번호를 입력해 주세요.");
        $('#newPassword').focus();
        return;
    } else if (newPw != confPw) {
        $('#messageNew').text("신규 비밀번호와 재입력 내용이 다릅니다.");
        $('#conPassword').focus();
        return;
    }

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
        $('#messageNew').text("동일문자를 3번 이상 사용할 수 없습니다.");
    }

    if (SamePass_1 > 2 || SamePass_2 > 2) {
        $('#messageNew').text("연속된 문자열(123 또는 321, abc, cba 등)을\n 3자 이상 사용 할 수 없습니다.");
    }

    fn_passwordUpdate();
}

//비밀번호 재설정
function fn_passwordUpdate() {
    if ($('#newPassword').val() && $('#conPassword').val()) {
        var url = "/common/login/passwordUpdate";
        var param = {
            loginId: $('#resetId').val(),
            loginPw: $('#newPassword').val()
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                swal({
                    type: 'success',
                    text: "정상적으로 변경되었습니다.",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function () {
                    location.href = "/common/login";
                });
            }
        });
    }
}
</script>

<input type="hidden" id="otpNoCon">
<input type="hidden" id="smsOtpY" value="N">
<div id="contents">
    <div id="damoa-breadcrumb">
        <nav class="nav container">
            <a class="nav-link active" href="#">아이디/비밀번호 찾기</a>
        </nav>
    </div>
    <div class="container">
        <div id="page-title">
            <div id="breadcrumb-in-title-area" class="row align-items-center">
                <div class="col-12 text-right">
                    <img src="/assets/imgs/common/icon-home.png" class="mr-2">
                    <span> > </span>
                    <span class="depth-1">아이디/비밀번호 찾기</span>
                    <span> > </span>
                    <span class="depth-2 active">아이디/비밀번호 찾기</span>
                </div>
            </div>
            <div class="row">
                <div class="col-12">
                    <h2>아이디/비밀번호 찾기</h2>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div class="row">
            <div class="col">
                <div class="tab-selecter type-4">
                    <ul class="nav nav-tabs" role="tablist">
                        <li class="nav-item"><a class="nav-link mobile-padding active" data-toggle="tab" href="#lookup-id">아이디 찾기</a></li>
                        <li class="nav-item"><a class="nav-link mobile-padding" data-toggle="tab" href="#reset-pw">비밀번호 찾기(재설정)</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>


    <div class="container tab-content">

        <div id="lookup-id" class="row justify-content-center tab-pane fade show active">
            <div class="col-lg-6 col-md-7 col-sm-9 col-12 mx-auto">
                <div id="" class="swiper-container lookup-id-slider" >
                    <div class="swiper-wrapper">
                        <div class="swiper-slide" id="checkId">
                            <div class="lookup-id login-box">
                                <div class="row no-gutters">
                                    <div class="col">
                                        <div class="row mb-2">
                                            <div class="col">
                                                <input type="tel" class="form-control allow-only-num" placeholder="사업자번호/고유단체번호 ('-'제외하고 입력)" onkeydown="fn_textClear();" id="corNum" maxlength="14" onkeyup="onlyNumber(this)">
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col">
                                                <input type="tel" class="form-control allow-only-num" placeholder="수납계좌번호 ('-'제외하고 입력)" onkeydown="fn_textClear();" id="corAccNum" maxlength="14" onkeyup="onlyNumber(this)">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-4 ml-2">
                                        <button type="button" class="btn btn-primary btn-flexible-height btn-lookup-id" onclick="fn_idSearch();">확인</button>
                                    </div>
                                </div>
                                <div class="row no-gutters">
                                    <span class="error-message font-red hidden" id="message"></span>
                                </div>
                            </div>
                        </div>

                        <div class="swiper-slide" id="okId">
                            <div class="succeed login-box">
                                <div class="row no-gutters mb-4 align-items-center">
                                    <div class="col-md-4 col-sm-6 text-center mb-4">
                                        <img src="/assets/imgs/common/img-monitor-pw-tick.png">
                                    </div>
                                    <div class="col col-md-8 col-sm-6 message type-1 text-center">
                                        <h6>아이디 찾기가 완료되었습니다.</h6>
                                        <h6>[<span id="selName"></span>]님의 아이디는 '<span class="font-blue" id="selId"></span>' 입니다.</h6>
                                    </div>
                                    <div class="row col col-3" style="margin: 0 auto;">
                                        <a href="#" class="btn btn-primary btn-lookup-id-completed btn-wide" onclick="fn_loginPageMove();">확인</a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="swiper-slide" id="noId">
                            <div class="failed login-box">
                                <div class="row no-gutters mb-4 align-items-center">
                                    <div class="col-5 text-center">
                                        <img src="/assets/imgs/common/img-exclamation-mark-in-monitor.png">
                                    </div>
                                    <div class="col message type-1">
                                        <h6>등록된 아이디가 없습니다.</h6>
                                        <h6>다시 확인해주세요.</h6>
                                        <a href="#" class="btn btn-primary" onclick="fn_reIdSearch();">확인</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="reset-pw" class="row justify-content-center tab-pane fade show">
            <div class="col-lg-6 col-md-7 col-sm-9 col-12 mx-auto">
                <div class="swiper-container reset-pw-slider" >
                    <div class="swiper-wrapper">
                        <div class="swiper-slide" id="checkPwd">
                            <div class="password-reset login-box">
                                <div class="row no-gutters">
                                    <div class="col">
                                        <div class="row mb-2">
                                            <div class="col">
                                                <input type="text" class="form-control" placeholder="아이디" maxlength="20" id="resetId" onkeydown="fn_textClear('PW');">
                                            </div>
                                        </div>
                                        <div class="row mb-2">
                                            <div class="col">
                                                <input type="tel" class="form-control" placeholder="사업자번호/고유단체번호 '-'제외하고 입력" onkeyup="onlyNumber(this)" maxlength="30" id="resetOffNo" onkeydown="fn_textClear('PW');">
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col">
                                                <input type="tel" class="form-control" placeholder="수납계좌번호 '-'제외하고 입력" onkeyup="onlyNumber(this)" maxlength="15" id="resetAccNo" onkeydown="fn_textClear('PW');">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-4 ml-2">
                                        <button type="button" class="btn btn-primary btn-flexible-height btn-password-reset-1" onclick="fn_reset();">확인</button>
                                    </div>
                                </div>
                                <div class="row no-gutters">
                                    <span class="error-message font-red hidden" id="messagePw"></span>
                                </div>
                            </div>
                        </div>

                        <div class="swiper-slide" id="confSms">
                            <div class="mobile-auth login-box">
                                <div class="row no-gutters">
                                    <div class="guide-mention mb-2">등록된 기관담당자 휴대폰번호로 SMS인증이 진행됩니다.</div>
                                    <div class="row col-12 mb-2">
                                        <div class="col">
                                            <div class="row">
                                                <div class="col">
                                                    <input type="text" class="form-control allow-only-num auth-num-input" id="resetCurHp" disabled="disabled" value="">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-4 ml-2" style="padding: 0;">
                                            <button class="btn btn-primary btn-flexible-height btn-send-auth-num" id="optNoSend" onclick="fn_confirmSend();">인증번호 발송</button>
                                            <button class="btn btn-primary btn-flexible-height btn-send-auth-num" id="optNoReSend" style="display: none;" onclick="fn_confirmSend();">재발송</button>
                                        </div>
                                    </div>
                                    <div class="row col-12">
                                        <div class="col">
                                            <div class="row mb-2">
                                                <div class="col">
                                                    <input type="text" class="form-control allow-only-num auth-num-input" id="otpNo" placeholder="인증번호 6자리 입력" maxlength="6" >
                                                    <span id="countTime" class="count-down-timer"></span>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-4 ml-2" style="padding: 0;">
                                            <button class="btn btn-primary btn-flexible-height btn-get-auth" id="certified" onclick="fn_otpNoConfirm();">확인</button>
                                        </div>
                                    </div>
                                </div>

                                <div class="guide-mention">
                                    <div>* 3분이내로 인증번호(6자리)를 입력해 주세요.</div>
                                    <div>* 입력시간 초과 시, "재전송" 버튼을 눌러주세요.</div>
                                </div>
                            </div>
                        </div>

                        <div class="swiper-slide" id="newPwd">
                            <div class="new-password login-box">
                                <div class="row no-gutters">
                                    <div class="col">
                                        <div class="row mb-2">
                                            <div class="col">
                                                <input type="password" class="form-control" placeholder="새 비밀번호" minlength="8" maxlength="20" id="newPassword" required>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col">
                                                <input type="password" class="form-control" placeholder="비밀번호 확인" minlength="8" maxlength="20" id="conPassword" required>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-4 ml-2">
                                        <button type="button" class="btn btn-primary btn-flexible-height btn-sub-login btn-reset-password-last-step" onclick="fn_passwordChange();">확인</button>
                                    </div>
                                </div>
                                <div class="row no-gutters">
                                   <span class="error-message font-red hidden" id="messageNew"></span>
                                </div>
                                <div class="row">
                                    <div class="col">
                                        <ul class="unordered-blue-dot-list mt-4">
                                            <li>기존 비밀번호와 다르게 설정해 주시기 바랍니다.</li>
                                            <li>연속숫자 등 유추하기 쉬운 비밀번호 설정불가</li>
                                            <li>영.숫자.특수문자 중 2가지 이상 혼합하여 8자리 이상 20자리 이하 구성</li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="swiper-slide" id="okPwd">
                            <div class="new-password-completed login-box">
                                <div class="row no-gutters mb-4 align-items-center">
                                    <div class="col-4">
                                        <img src="/assets/imgs/common/img-monitor-pw-tick.png">
                                    </div>
                                    <div class="col message type-1">
                                        <p>비밀번호 재설정이 완료되었습니다.</p>
                                        <a href="#" class="btn btn-primary" onclick="fn_loginPageMove();">확인</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="swiper-pagination"></div>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div id="login-instruction">
            <div class="row">

                <div class="col col-lg-4 col-md-4 col-12 has-border">
                    <div class="row align-items-center">
                        <div class="col col-md-12 col-4">
                            <img src="/assets/imgs/common/img-pw-change.png">
                        </div>
                        <div class="col col-md-12 col-8">
                            <h4>비밀번호 변경관리</h4>
                            <p>
                                사용자 비밀번호는 주기적으로 변경관리를 통해 타인에게 노출되지 않도록 주의해주시기 바랍니다.
                            </p>
                        </div>
                    </div>
                </div>
                <div class="col col-lg-4 col-md-4 col-12 has-border">
                    <div class="row align-items-center">
                        <div class="col col-md-12 col-4">
                            <img src="/assets/imgs/common/img-monitor-with-unlock.png">
                        </div>
                        <div class="col col-md-12 col-8">
                            <h4>로그아웃</h4>
                            <p>
                                서비스 이용 중 자리를 비우게 될 경우에는 개인정보보호를 위해 반드시 로그아웃을 하시기 바랍니다.
                            </p>
                        </div>
                    </div>
                </div>
                <div class="col col-lg-4 col-md-4 col-12 has-border">
                    <div class="row align-items-center">
                        <div class="col col-md-12 col-4">
                            <img src="/assets/imgs/common/img-monitor-in-lock.png">
                        </div>
                        <div class="col col-md-12 col-8">
                            <h4>로그인 제한</h4>
                            <p>
                                비밀번호를 연속으로 5회이상 입력 오류 시 로그인이 제한되며, 본인인증 후 비밀번호를 다시 등록해야 합니다.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" /></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/payer/footer.jsp" flush="false" /></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/footer.jsp" flush="false" /></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/footer.jsp" flush="false" /></s:authorize>

<script>
    // 아이디 찾기 인터페이스 슬라이더
    var mySwiperId = new Swiper('.lookup-id-slider', {
// Optional parameters
        direction: 'horizontal',
        autoHeight: true,
        loop: false,
        autoplay: false,
        simulateTouch: false
    });


    // 비밀번호 찾기 인터페이스 슬라이더

    var mySwiperPw = new Swiper('.reset-pw-slider', {
// Optional parameters
        direction: 'horizontal',
        autoHeight: true,
        loop: false,
        autoplay: false,
        simulateTouch: false,
        observer: true,
        observeParents: true
    });
</script>

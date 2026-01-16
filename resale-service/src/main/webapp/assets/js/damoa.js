// damoa.js
// version : 0.1
// create date : 2018.02.23
// authors : Youngjoo Lee
// description : 신한DAMOA 서비스의 기본이 되는 javascript 들

$(document).ready(function () {
    // Page title 불러오기
    pageTitleSetting();

    // Active first Depth
    activeFirstDepth();

    // PC 이외의 기기 인지
    mobileDetect();

    function mobileDetect() {
        mobileAndTabletcheck();

        if (mobileDetectionCheck == true) {
            $(".hidden-on-mobile").hide();
        }
    }

    // 로그인 박스
    $("#payer-login-method").on('change', function () {
        if ($(this).val() == "bank-number") {
            $(".second-row-input").attr({
                "name": "bank-number",
                "type": "tel",
                "placeholder": "납부계좌번호 (-)제외"
            });

            $("#contact").slideUp();
        } else if ($(this).val() == "payer-info") {
            $(".second-row-input").attr({
                "name": "institute",
                "type": "text",
                "placeholder": "이용기관명"
            });

            $("#contact").slideDown();
        }

        $(".second-row-input").val('');
    });

    $("#payer-login-method .form-check-input").on('click', function () {
        if ($("#by-account").is(":checked") == true) {
            $(".variable-row-input").attr({
                "name": "bank-number",
                "type": "tel",
                "placeholder": "납부계좌번호 (-)제외"
            });

            $("#contact").slideUp("fast");

        } else if ($("#by-account").is(":checked") == false) {
            $(".variable-row-input").attr({
                "name": "institute",
                "type": "text",
                "placeholder": "이용기관명"
            });

            $("#contact").slideDown("fast");
        }
    });


    // 2018.04.06 logout 여부를 묻는 alert
    $("#btn-logout").click(function () {
        logout();
    });

    ///////////////////////////////////
    //////////Validations//////////////
    ///////////////////////////////////

    // 숫자만 받기
    $(".allow-only-num").keydown(function (e) {
        allowOnlyNum(e);
    });


    //////////////////////////////
    ///////// Navigation /////////
    //////////////////////////////

    // 1st depth 메뉴에 hover 시 sub menu 열림. 화면 넓이가 992 이하일 시 작동하지 않음

    // 마우스 오버 시 데크스탑 화면일 때
    $(".dropdown").hover(function () {
        if ($("body").width() >= 992) {
            var _this = $(this).find(".dropdown-toggle").attr("id");
            if (firstDepth != _this) {
                $(".for-desktop").stop(true, true).slideUp("fast");
                $(".for-desktop" + "." + firstDepth).stop(true, true).slideUp("fast");
                $(".for-desktop" + "." + _this).stop(true, true).delay(200).slideDown("fast");
            }
        } else {
            return false;
        }
    }, function () {
        if ($("body").width() >= 992) {
            var _this = $(this).find(".dropdown-toggle").attr("id");
            $(".for-desktop" + "." + _this).stop(true, true).slideUp("fast");
            activeFirstDepth();
        } else {
            return false;
        }
    });


    // 클릭시 992px 이하 화면일 때
    $(".dropdown-toggle").on('click tab', function () {
        if ($("body").width() < 992) {
            $("#mobile-sub .for-mobile").slideUp(100);
            var _this = $(this).attr("id");
            $(".for-mobile" + "." + _this).delay(200).slideToggle();
        }
    });


    // 안내 문구 상자 여닫기
    $('.foldable-box .foldable-box-header').click(function () {
        if ($(".foldable-box-body").is(":hidden")) {
            $(".fold-status").removeClass("down");
            $(".foldable-box-header").css('border-bottom', '1px solid #d6d6d6');
        } else {
            $(".fold-status").addClass("down");
            $(".foldable-box-header").css('border-bottom', 'none');
        }
        $('.foldable-box-body').slideToggle();
    });

    $(".btn-open-hidden-cell").click(function () {
        if ($(".hidden-01").is(":visible")) {
            $(".hidden-01").css("display", "none");
            $('.btn-open-hidden-cell').html("+");
        } else {
            $(".hidden-01").css("display", "table-cell");
            $('.btn-open-hidden-cell').html("-");
        }
    });

    $("#tableCheckbox1").click(function () {
        if ($(this).is(":checked")) {
            $(".hidden-01").css("display", "table-cell");
        } else {
            $(".hidden-01").css("display", "none");
        }
    });

    $("#tableCheckbox2").click(function () {
        if ($(this).is(":checked")) {
            $(".hidden-02").css("display", "table-cell");
        } else {
            $(".hidden-02").css("display", "none");
        }
    });

    $("#btn-sitemap").click(function () {
        sitemapNavOpener();
    });


    // th의 checkbox로 td의 전체 tick을 체크
    $(".label-table-check-parents").click(function () {
        var _this = $(this).closest("table").attr("id");

        if ($("#" + _this + " .table-check-parents").is(":checked")) {
            $("#" + _this + " .table-check-child").prop("checked", false);
        } else if ($("#" + _this + " .table-check-parents").not(":checked")) {
            $("#" + _this + " .table-check-child").prop("checked", true);
        }
    });

    // 리스트의 체크박스 갯수
    var numCheckbox;

    // table child checkbox 시
    $(".table-check-child").click(function () {
        var _this = $(this).closest("table").attr("id");
        numCheckbox = $("#" + _this + " .table-check-child").length;

        if ($(this).is(":checked")) {
            if ($("#" + _this + " .table-check-child:checked").length == numCheckbox) {
                $("#" + _this + " .table-check-parents").prop("checked", true);
            }
        } else if ($(this).not(":checked")) {
            $("#" + _this + " .table-check-parents").prop("checked", false);
        }
    });


    // datePicker()
    $(".date-picker-input").datepicker({
        dateFormat: "yy.mm.dd",
        showOn: "button",
        buttonImage: "/assets/imgs/common/btn-cal.png",
        buttonImageOnly: true,
        buttonText: "Select date",
        changeMonth: true,
        changeYear: true,
        beforeShow: function (input) {
            setTimeout(function () {
                $('#ui-datepicker-div').position({
                    my: "left top",
                    at: "left bottom",
                    of: $(input)
                });

            }, 50)
        }
    });


    // email 입력 폼 컨트롤
    $(".email-contact .provider-preset").on("change", function () {
        var provider = $(this).val();

        if (provider == "") {
            $(".email-provider").attr("disabled", false).val("");
        } else {
            $(".email-provider").attr("disabled", true).val(provider);
        }
    });


    $("#monthly-or-duration input").click(function () {
        $(this).attr("checked", "checked")
        if ($(".lookup-by-month").is(":checked")) {
            $("#lookup-by-range").stop().slideUp();
            $("#lookup-by-month").stop().delay(300).slideDown();
        } else {
            $("#lookup-by-month").stop().slideUp();
            $("#lookup-by-range").stop().delay(300).slideDown();
        }
    });

    $("#monthly-or-duration-card input").click(function () {
        $(this).attr("checked", "checked")
        if ($(".lookup-by-month-card").is(":checked")) {
            $("#lookup-by-range-card").stop().slideUp();
            $("#lookup-by-month-card").stop().delay(300).slideDown();
        } else {
            $("#lookup-by-month-card").stop().slideUp();
            $("#lookup-by-range-card").stop().delay(300).slideDown();
        }
    });

    // 조건에 따른 활성화 여부 체크
    $("select.parents").on("change", function () {
        var _target = $(this).val();

        if (_target.length == 0) {
            $(".first-depth-child").attr("disabled", true);
        } else if (_target.length = !0) {
            $(".first-depth-child").attr("disabled", false);
        }
    });

    // input 의 텍스트를 초기화 시킴
    $(".remove-text").click(function () {
        $(this).siblings(".form-control").val("");
    });


    //faq
    $("#faq-list tbody tr").click(function () {
        var _this = $(this).attr("id");

        $("." + _this).slideToggle();
    });


    ////////////////////////////////////
    ///////////// 회원 확인 //////////////
    ////////////////////////////////////

    // // 약관동의 단계로 이동
    // $(".btn-start-auth").click(function(){
    //     slideNext();
    // });
    //
    // // 아이디, 비밀버호 변경
    // $(".btn-member-auth-reset-pw").click(function(){
    //     confirmMemberResetPw();
    // });
    //
    // // 서비스 이용 개시
    // $(".btn-member-auth-completed").click(function(){
    //     confirmMemberCompleted();
    // });
});

// 스크롤 막기
$(document).scroll("touchmove", function (e) {
    if ($(".modal-backdrop").is(":visible")) {
        return false;
    } else {
    }
});

var mobileDetectionCheck = false;

function mobileAndTabletcheck() {
    (function (a) {
        if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))) mobileDetectionCheck = true;
    })(navigator.userAgent || navigator.vendor || window.opera);
    return mobileDetectionCheck;
};

function sitemapNavOpener() {
    // 모바일 기기인지 확인
    mobileAndTabletcheck();

    // 화면 높이 측정
    var winH = $(window).height();
    // 타이틀 및 유저 영역 높이
    var titleAreaH;
    // 펼침 메뉴 영역 높이를 화면에 채우기
    var allLinkFixedHeight;
    // 실제 펼침 메뉴 영역 높이
    var allLinkHeight;

    $("#navbarTogglerSitemap").slideToggle("normal", function () {
        if ($("#navbarTogglerSitemap").is(":visible")) {
            $("#btn-sitemap").addClass("active");
            $("body").addClass("noscroll");

            // 모바일 기기일 때 아래 스크립트 작동
            allLinkHeight = $("#navbarTogglerSitemap .all-links").outerHeight();
            // 타이틀 및 유저 영역 높이
            titleAreaH = $("#navbarTogglerSitemap .title-area").outerHeight();
            // 펼침 메뉴 영역 높이
            allLinkFixedHeight = winH - titleAreaH;
        } else {
            $("body").removeClass("noscroll");
            $("#btn-sitemap").removeClass("active");
        }


        if (mobileDetectionCheck == true && allLinkHeight > allLinkFixedHeight) {
            $("#navbarTogglerSitemap .all-links").css("height", allLinkFixedHeight + "px");

        } else {
            return false;
        }

        // console.log(allLinkHeight);
    });

}


// Page title 불러오기
function pageTitleSetting() {
    var pageTitle = $("#page-title h2").html();

    if (pageTitle == null) {
        $("title").html("가상계좌 수납관리 서비스");
    } else {
        $("title").html("가상계좌 수납관리 서비스 | " + pageTitle);
    }
}

// 로그아웃 2018.04.06
function logout() {
    swal({
        type: 'question',
        // html: delMessage,
        text: '로그아웃 하시겠습니까?',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '확인',
        cancelButtonText: '취소'
    });
}

// 첫번째 네비게이션 활성화
function activeFirstDepth() {
    $("#" + firstDepth).closest(".nav-item").addClass("active");
    if ($("body").width() >= 992) {
        $(".for-desktop" + "." + firstDepth).stop(true).delay(200).slideDown("fast");
        $("." + firstDepth + " " + "." + secondDepth).addClass("active");
    } else if ($("body").width() < 992) {
        $(".for-mobile" + "." + firstDepth).stop(true).delay(200).slideDown("fast");
        $(".for-mobile" + "." + firstDepth + " " + "." + secondDepth).addClass("active");
    }
}

///////////////////////////////////
//////////Validations//////////////
///////////////////////////////////

// 숫자만 받기

function allowOnlyNum(e) {
    // Allow: backspace, delete, tab, escape, enter and .
    if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
        // Allow: Ctrl/cmd+A
        (e.keyCode == 65 && (e.ctrlKey === true || e.metaKey === true)) ||
        // Allow: Ctrl/cmd+C
        (e.keyCode == 67 && (e.ctrlKey === true || e.metaKey === true)) ||
        // Allow: Ctrl/cmd+X
        (e.keyCode == 88 && (e.ctrlKey === true || e.metaKey === true)) ||
        // Allow: home, end, left, right
        (e.keyCode >= 35 && e.keyCode <= 39)) {
        // let it happen, don't do anything
        return;
    }
    // Ensure that it is a number and stop the keypress
    if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
        e.preventDefault();
    }

}


function sendAuthNum() {
    swal({
        type: 'success',
        html: '인증번호가 발송되었습니다.<br> 정확히 입력해 주세요.',
        confirmButtonColor: '#3085d6',
        confirmButtonText: '확인'
    });
    $(".btn-send-auth-num").html("재전송");

    $(".auth-num-input, .btn-get-auth").attr("disabled", false);
    countDownTimerSystem();
}

function reSendAuthNum() {
    swal({
        type: 'success',
        html: '인증번호를 재발송 하시겠습니까?',
        confirmButtonColor: '#3085d6',
        confirmButtonText: '확인'
    });

    countDownTimerSystem();
}

// 인증시간 타이머
var countDownTimer;

function countDownTimerSystem() {
    var duration = "3:00";
    var presentTime = document.getElementById('auth-timer').innerHTML;
    var timeArray = presentTime.split(/[:]+/);
    var m = timeArray[0];
    var s = checkSecond((timeArray[1] - 1));
    if (s == 59) {
        m = m - 1
    }
    if (m < 0) {
        failAuthInTime(duration);
        clearTimeout(countDownTimer);

        return false;
    }

    document.getElementById('auth-timer').innerHTML =
        m + ":" + s;
    countDownTimer = setTimeout(countDownTimerSystem, 1000);
}

// 초 단위가 1자리 일때 0을 붙이는 기능
function checkSecond(sec) {
    if (sec < 10 && sec >= 0) {
        sec = "0" + sec
    }; // add zero in front of numbers < 10
    if (sec < 0) {
        sec = "59"
    };
    return sec;
}

// 시간초과
function failAuthInTime(duration) {
    swal({
        type: 'warning',
        html: '시간이 초과되었습니다.<br>인증번호를 새로 발송해주세요.',
        confirmButtonColor: '#3085d6',
        confirmButtonText: '확인'
    });
    $("#auth-timer").html(duration);
    $(".auth-num-input, .btn-get-auth").attr("disabled", true);
}

// 인증 성공
function getAuthSuccess() {
    swal({
        type: 'success',
        html: '인증에 성공하였습니다.',
        confirmButtonColor: '#3085d6',
        confirmButtonText: '확인'
    });
    $(".btn-reset-password-third-step").attr("disabled", false);
    clearTimeout(countDownTimer);
}

// 인증실패
function getAuthFailed() {
    swal({
        type: 'warning',
        html: '인증에 실패하였습니다.<br>다시 입력해주세요.',
        confirmButtonColor: '#3085d6',
        confirmButtonText: '확인'
    });
    // $(".btn-reset-password-third-step").attr("disabled", false);
}

/**
 * 콤마제거
 * @param num
 * @returns
 */
function removeCommas(num) {
    return num.toString().replace(/[^\d]+/g, '');
}

/**
 * 숫자에 천단위 콤마 추가
 * @param num
 * @returns
 */
function addThousandSeparatorCommas(num) {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

function countBytes(s, id) {
    var b = 0, i = 0, c
    for (; c = s.charCodeAt(i++); b += c >> 11 ? 2 : c >> 7 ? 2 : 1) ;
    $("#" + id + "").parent().children().children().html(b);
    return b
}

////////////////////////////////////
///////////// 회원 확인 //////////////
////////////////////////////////////

// function confirmMemberAuth(speed){
//     $(".agree-01").hide('slide', { direction: "left" }, speed).css("z-index","9");
//     $(".agree-02").show('slide', { direction: "right" }, speed).css("z-index","10");
//
//     var boxHeight = $(".agree-02").outerHeight()
//     $(".step-box").css("height", boxHeight + "px");
//
//     $(".step-01").removeClass("on");
//     $(".step-02").addClass("on");
// }
//
// function confirmMemberResetPw(speed){
//     $(".agree-02").hide('slide', { direction: "left" }, speed).css("z-index","9");
//     $(".reset-id-n-pw").show('slide', { direction: "right" }, speed).css("z-index","10");
//
//     var boxHeight = $(".reset-id-n-pw").outerHeight()
//     $(".step-box").delay(500).css("height", boxHeight + "px");
//
//     $(".step-02").removeClass("on");
//     $(".step-03").addClass("on");
// }
//
// function confirmMemberCompleted(speed){
//     $(".reset-id-n-pw").hide('slide', { direction: "left" }, speed).css("z-index","9");
//     $(".member-auth-completed").show('slide', { direction: "right" }, speed).css("z-index","10");
//
//     var boxHeight = $(".member-auth-completed").outerHeight()
//     $(".step-box").css("height", boxHeight + "px");
//
//     $(".step-03").removeClass("on");
//     $(".step-04").addClass("on");
// }


/****************************************
 정미래
 2018. 07. 27
 ****************************************/

/* 모달창 띄울 때 스크롤 위치 그대로 */
function fn_reset_scroll() {
    var height = $(document).scrollTop();
    $('html, body').animate({scrollTop : height}, 400);
}

/****************************************
 신재현
 2018. 09. 07
 ****************************************/

/* 30분뒤 로그아웃*/

var iMinute;// 시간 지정 분
var iSecond; //초단위로 환산
var timerchecker = null;

function loadJQuery() {
    var oScript = document.createElement("script");
    oScript.type = "text/javascript";
    oScript.charset = "utf-8";
    oScript.src = "/assets/js/jquery.min.js";
    document.getElementsByTagName("head")[0].appendChild(oScript);
}
function initTimer() {
	var discount = 10;
    if (iSecond > 0) {
        iSecond = (iSecond-discount);
        timerchecker = setTimeout("initTimer()", 1000*discount); // 10초 간격으로 체크
    } else {
        clearTimeout(timerchecker);
        window.location.reload();
        // loadJQuery();
        // var url = "/logout";
        // var param = {};
        // $.ajax({
        //     type: "post",
        //     async: true,
        //     url: url,
        //     contentType: "application/json; charset=UTF-8",
        //     data: JSON.stringify(param),
        //     success: function (data) {
        //             location.href = "/main?error=sessionExpired";
        //     }
        // });
    }
}

function fncClearTime() {
    iMinute = 31;
    iSecond = iMinute * 60;
}


/**
 * 체크박스 checked 시 배경색 변경
 */
function changeBGColor(obj) {
	if ($(obj).prop('checked')) {
		$(obj).parent().parent().children().css({"background-color" : "#f1fbe5"});
	} else {
		$(obj).parent().parent().children().css({"background-color" : ""});
	}
}

/**
 * 목록 체크버튼 이벤트처리
 */
function checkButtonTrigger(obj) {
	var query = $(obj).val();
	var checked = $(obj).prop('checked');
	$(':checkbox[name='+query+']').not(".checkbox-none-check-disabled").not(".checkbox-disabled").prop('checked', checked);

	$(':checkbox[name='+query+']').not(".checkbox-none-check-disabled").not(".checkbox-disabled").each(function(i, v){ changeBGColor(v); });
}

/**
 * 개인정보 처리방침
 */
function fn_personalInfoNow(version) {
    var url = "/common/personalInfo/" + version;
    window.open(url, '_blank');
}

/**
 * 서비스 이용약관
 */
function fn_serviceInfoNow(version) {
    var url = "/common/serviceInfo/" + version;
    window.open(url, '_blank');
}

/**
 * 전자금융거래약관
 */
function fn_electronicFinancialInfoNow(version) {
    var url = "/common/electronicFinancialInfo/" + version;
    window.open(url, '_blank');
}

/**
 * 신한은행 인터넷 뱅킹
 */
function fn_shinhanInternet() {
    var url = "https://bank.shinhan.com/index.jsp";
    window.open(url, '_blank');
}

/**
 * 신한은행 지점 찾기
 */
function fn_footerBankSearch() {
    var url = "http://www.shinhan.com/websquare/websquare_contents.jsp?w2xPath=/contents/about_bank/branch/internal01_type01.xml";
    window.open(url, "window", "width=900, height=700");
}

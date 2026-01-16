// admin.js
// version : 0.1
// create date : 2018.05.17
// authors : Youngjoo Lee
// description : 신한DAMOA Admin 서비스의 기본이 되는 javascript 들

$(document).ready(function () {

    //console.log("admins.js");

    //console.log(oneDepth);
    //console.log(twoDepth);

    menuLocation();

    // console.log("admin.js OK!");
});


/**
 * 정미래
 * menu 활성화
 */
function menuLocation() {
    $("#" + oneDepth).addClass("active");
    if (twoDepth != null) {
        $("#" + twoDepth).addClass("active");
    }
}

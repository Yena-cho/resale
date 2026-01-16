$(document).ready(function (e) {
    var windowHeight = $(window).height();
    var contentHeight = $('.swiper-slide').height() + 55;
    var foogerHeight = windowHeight - contentHeight - 30;

    $('img[usemap]').rwdImageMaps();

    if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
        // 모바일 접속 시
        $('#footer').css("height" , foogerHeight);
    } else {
        // PC 접속 시
        window.location.href = "https://www.shinhandamoa.com/";
    }
});
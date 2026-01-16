//셀렉트박스
jQuery(document).ready(function(){    
    var select = $("select.select_layer");    
    select.change(function(){
        var select_name = $(this).children("option:selected").text();
        $(this).siblings("label").text(select_name);
    });
});

//팝업윈도우
function openWindow(pageURL, w, h)
{
	var sizeStyle="width="+w+",height="+h;
	//alert(sizeStyle);
	window.open(pageURL, '', sizeStyle+", toolbar=no, menubar=no, resizable=1, scrollbars=no");
}

//반투명배경 잘림현상 해결
$(window).resize(function(){
	$( '.laypop_alpha' ).css({'width':$(window).width(),'height':$(window).height()});
});
$(window).scroll(function(){
	$( '.laypop_alpha' ).css({'top':$(window).scrollTop(),'left':$(window).scrollLeft()});
});


//탑버튼
$(document).ready(function(){ 
    $(".return_top").hide(); // 탑 버튼 숨김
    $(function () { 
        $(window).scroll(function () {
            if ($(this).scrollTop() > 50) { // 스크롤 내릴 표시
                $('.return_top').fadeIn();
            } else {
                $('.return_top').fadeOut();
            }
        }); 
        $('.return_top').click(function () {
            $('body,html').animate({
                scrollTop: 0
            }, 200);  // 탑 이동 스크롤 속도
            return false;
        });

    }); 

});



<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>가상계좌 수납관리 서비스 : 소모임 및 단체</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">

    <link rel="stylesheet" href="/assets/promotion/css/normalize.css">
    <link rel="stylesheet" href="/assets/promotion/css/common.css">
    <link rel="stylesheet" href="/assets/promotion/css/swiper.css">

    <script src="/assets/promotion/js/jquery-3.3.1.min.js"></script>
    <script src="/assets/promotion/js/jquery.rwdImageMaps.min.js"></script>
    <script src="/assets/promotion/js/common.js"></script>
    <script src="/assets/promotion/js/swiper.js"></script>
</head>

<body>
    <div>
        <div id="header">
            <img src="/assets/promotion/img/logo.png" class="logo-img" alt="로고"/>
        </div>

        <!-- Swiper -->
        <div class="swiper-container">
            <div class="swiper-wrapper">
                <div class="swiper-slide">
                    <img usemap="#m1" src="/assets/promotion/img/m6/m01.jpg" alt="m1"/>
                </div>
                <div class="swiper-slide">
                    <img usemap="#m2" src="/assets/promotion/img/m6/m02.jpg" alt="m2"/>
                </div>
                <div class="swiper-slide">
                    <img usemap="#m3" src="/assets/promotion/img/m6/m03.jpg" alt="m3"/>
                </div>
                <div class="swiper-slide">
                    <img usemap="#m4" src="/assets/promotion/img/m6/m04.jpg" alt="m4"/>
                </div>
            </div>

            <!-- Add Pagination -->
            <!-- <div class="swiper-pagination"></div> -->

            <map name="m1">
                <area shape="rect" alt="ìë´ë¬¸ì" title="ìë´ë¬¸ì" coords="42,674,303,740" href="tel:02-786-8201" target="_self"/>
                <area shape="rect" alt="ííì´ì§" title="ííì´ì§" coords="320,675,579,738" href="https://www.shinhandamoa.com" target="_self"/>
            </map>

            <map name="m2">
                <area shape="rect" alt="ìë´ë¬¸ì" title="ìë´ë¬¸ì" coords="42,674,303,740" href="tel:02-786-8201" target="_self"/>
                <area shape="rect" alt="ííì´ì§" title="ííì´ì§" coords="320,675,579,738" href="https://www.shinhandamoa.com" target="_self"/>
            </map>

            <map name="m3">
                <area shape="rect" alt="ìë´ë¬¸ì" title="ìë´ë¬¸ì" coords="42,674,303,740" href="tel:02-786-8201" target="_self"/>
                <area shape="rect" alt="ííì´ì§" title="ííì´ì§" coords="320,675,579,738" href="https://www.shinhandamoa.com" target="_self"/>
            </map>

            <map name="m4">
                <area shape="rect" alt="ìë´ë¬¸ì" title="ìë´ë¬¸ì" coords="42,674,303,740" href="tel:02-786-8201" target="_self"/>
                <area shape="rect" alt="ííì´ì§" title="ííì´ì§" coords="320,675,579,738" href="https://www.shinhandamoa.com" target="_self"/>
            </map>
        </div>

        <div id="footer">
            <span>고객센터 : <a href="tel:02-786-8201">02-786-8201</a><br/>
                <small>(평일 09:00-12:00, 13:00-17:00, 토요일 및 공휴일 휴무)</small>
            </span><br/>
            <span>팩스 : 0303-0959-8201</span><br/>
            <span>이메일 : <a href="mailto:cs@finger.co.kr">cs@finger.co.kr</a></span>
        </div>
    </div>
</body>

<script>
    var swiper = new Swiper('.swiper-container', {
        slidesPerView: 1,
        spaceBetween: 30,
        loop: true,
        pagination: {
            el: '.swiper-pagination',
        },
    });
</script>

</html>

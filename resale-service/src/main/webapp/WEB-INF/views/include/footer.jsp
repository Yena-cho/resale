<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

            <script type="text/javascript">
                // 글로벌
                function fn_globalMove() {
                    var url = "https://www.shinhanglobal.com/global.shinhan";
                    var url2 = "https://www.shinhanglobal.com/global.shinhan?w2xPath=/kor/network/GP60200100A.xml&br_inf_category=";
                    var category = "";

                    if ($('#global option:selected').val() == 'G') { // 글로벌
                        window.open(url, '_blank');
                    } else if ($('#global option:selected').val() == '0') {
                        category = "10&br_inf_sel_c=0&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8080') {
                        category = "20&br_inf_sel_c=8080&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8005') {
                        category = "20&br_inf_sel_c=8005&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8004') {
                        category = "10&br_inf_sel_c=8004&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8047') {
                        category = "10&br_inf_sel_c=8047&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8033') {
                        category = "10&br_inf_sel_c=8033&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8026') {
                        category = "20&br_inf_sel_c=8026&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8007') {
                        category = "20&br_inf_sel_c=8007&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8061') {
                        category = "10&br_inf_sel_c=8061&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '9999') {
                        category = "10&br_inf_sel_c=9999&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8031') {
                        category = "10&br_inf_sel_c=8031&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8037') {
                        category = "10&br_inf_sel_c=8037&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8055') {
                        category = "40&br_inf_sel_c=8055&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8051') {
                        category = "10&br_inf_sel_c=8051&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8104') {
                        category = "20&br_inf_sel_c=8104&br_inf_idx=1300";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8038') {
                        category = "10&br_inf_sel_c=8038&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8095') {
                        category = "20&br_inf_sel_c=8095&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8096') {
                        category = "20&br_inf_sel_c=8096&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    } else if ($('#global option:selected').val() == '8097') {
                        category = "20&br_inf_sel_c=8097&br_inf_idx=1";
                        window.open(url2 + category, '_blank');
                    }
                    $('#global').val('');
                }

                // 패밀리
                function fn_familyMove() {
                    var url = "";
                    if ($('#family option:selected').val() == '01') {
                        url = "http://www.shinhangroup.co.kr/kr/index.jsp";
                        window.open(url, '_blank');
                    } else if ($('#family option:selected').val() == '02') {
                        url = "https://www.shinhan.com/index.jsp";
                        window.open(url, '_blank');
                    } else if ($('#family option:selected').val() == '03') {
                        url = "https://www.shinhancard.com/conts/person/main.jsp";
                        window.open(url, '_blank');
                    } else if ($('#family option:selected').val() == '04') {
                        url = "https://www.shinhaninvest.com/siw/main/front/view.do";
                        window.open(url, '_blank');
                    } else if ($('#family option:selected').val() == '05') {
                        url = "http://www.shinhanlife.co.kr/bigLife.do";
                        window.open(url, '_blank');
                    } else if ($('#family option:selected').val() == '06') {
                        url = "https://www.shcap.co.kr/main/1101.shc";
                        window.open(url, '_blank');
                    } else if ($('#family option:selected').val() == '07') {
                        url = "https://www.e-jejubank.com/JeJuBankInfo.do";
                        window.open(url, '_blank');
                    } else if ($('#family option:selected').val() == '08') {
                        url = "https://www.shinhansavings.com/main_0000A_00.act";
                        window.open(url, '_blank');
                    } else if ($('#family option:selected').val() == '09') {
                        url = "http://www.shbnppam.com/";
                        window.open(url, '_blank');
                    } else if ($('#family option:selected').val() == '10') {
                        url = "https://www.shinhansys.co.kr/main.do";
                        window.open(url, '_blank');
                    } else if ($('#family option:selected').val() == '11') {
                        url = "http://www.shinhanci.co.kr/";
                        window.open(url, '_blank');
                    } else if ($('#family option:selected').val() == '12') {
                        url = "http://www.beautifulshinhan.co.kr/";
                        window.open(url, '_blank');
                    } else if ($('#family option:selected').val() == '13') {
                        url = "http://www.sbirds.com/";
                        window.open(url, '_blank');
                    } else if ($('#family option:selected').val() == '14') {
                        url = "http://www.shinhanmuseum.co.kr/";
                        window.open(url, '_blank');
                    }
                    $('#family').val('');
                }
            </script>

            <footer>
                <div class="container">
                    <div class="row">
                        <div class="col-md-12" style="line-height: 1.5;">
                            <a href="#" onclick="fn_personalInfoNow('v1');">개인정보처리방침</a><span class="mr-2 ml-2">|</span>
                            <a href="#" onclick="fn_serviceInfoNow('v1');">서비스이용약관</a><span class="mr-2 ml-2 hidden-on-mobile">|</span>
                            <a href="#" onclick="fn_electronicFinancialInfoNow('v1');">전자금융거래약관</a><span class="mr-2 ml-2"></span>
                            <%--<a href="#" onclick="fn_shinhanInternet();" class="hidden-on-mobile">신한은행 인터넷뱅킹</a><span
                                class="mr-2 ml-2 hidden-on-mobile">|</span>
                            <a href="#" onclick="fn_footerBankSearch();" class="hidden-on-mobile">신한은행지점찾기</a>--%>
                            <br/>
                            <span>고객센터 : 02-786-8201<br class="hidden-on-web"/><small>(평일 09:00-12:00, 13:00-17:00, 토요일 및 공휴일 휴무)</small></span><br
                                class="hidden-on-web"/>
                            <span>팩스 : 0303-0959-8201</span><br class="hidden-on-web"/>
                            <span>이메일 : cs@finger.co.kr</span><br/>
                            <span>&#x24B8; FINGER. All rights reserved.</span>
                        </div>
                    </div>
                </div>
            </footer>
            <script>
                $(document).ready(function () {
                    var xhr = new XMLHttpRequest();

                    $(document).ajaxStart(function () {
                        xhr.startTime = new Date().getTime();
                        $(".ajax-loader-area").show();
                    }).ajaxStop(function () {
                        var elapsed = new Date().getTime() - xhr.startTime;
                        if (elapsed < 1000) {
                            $(".ajax-loader-area").delay(200).hide(0);
                        } else {
                            $(".ajax-loader-area").hide();
                        }
                    });
                });
            </script>

            <div class="ajax-loader-area" style="display: none;">
                <div class="ajax-loader"></div>
                <div class="modal-backdrop fade show"></div>
            </div>
        </div>
    </div>
</body>

<script src="/assets/bootstrap/popper.js"></script>
<script src="/assets/bootstrap/bootstrap.min.js"></script>
<script src="/assets/js/swiper.js"></script>
<script src="/assets/js/damoa.js"></script>
<script src="/assets/js/common.js?version=${project.version}"></script>
<script src="/assets/js/sweetalert.js"></script>
<script src="/assets/js/promise.min.js"></script>

<script>
    var mySwiper = new Swiper('.main-slider', {
        direction: 'horizontal',
        loop: true,
        autoplay: true,
        pagination: {
            el: '.swiper-pagination',
        }
    })
</script>
</html>

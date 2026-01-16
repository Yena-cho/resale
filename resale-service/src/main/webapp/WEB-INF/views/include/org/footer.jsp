<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

            <footer>
                <div class="container">
                    <div class="row">
                        <div class="col-md-12" style="line-height: 1.5;">
                            <a href="#" onclick="fn_personalInfoNow('v1');">개인정보처리방침</a><span class="mr-2 ml-2">|</span>
                            <a href="#" onclick="fn_serviceInfoNow('v1');">서비스이용약관</a><span class="mr-2 ml-2 hidden-on-mobile">|</span>
                            <a href="#" onclick="fn_electronicFinancialInfoNow('v1');">전자금융거래약관</a><span class="mr-2 ml-2"></span>
<%--                            <a href="#" onclick="fn_shinhanInternet();" class="hidden-on-mobile">신한은행 인터넷뱅킹</a><span class="mr-2 ml-2 hidden-on-mobile">|</span>--%>
<%--                            <a href="#" onclick="fn_footerBankSearch();" class="hidden-on-mobile">신한은행지점찾기</a>--%>
                            <br/>
                            <span>고객센터 : 02-786-8201<br class="hidden-on-web"/><small>(평일 09:00-12:00, 13:00-17:00, 토요일 및 공휴일 휴무)</small></span><br class="hidden-on-web"/>
                            <span>팩스 : 0303-0959-8201</span><br class="hidden-on-web"/>
                            <span>이메일 : cs@finger.co.kr</span><br/>
                            <span>&#x24B8; FINGER. All rights reserved.</span>
                        </div>
                    </div>
                </div>
            </footer>

            <script>
                $(document).ready(function () {
                    fncClearTime();
                    initTimer();

                    var xhr = new XMLHttpRequest();

                    $(document).ajaxStart(function () {
                        xhr.startTime = new Date().getTime();
                        fncClearTime();
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

    <script src="/assets/bootstrap/popper.js"></script>
    <script src="/assets/bootstrap/bootstrap.min.js"></script>
    <script src="/assets/js/sweetalert.js"></script>
    <script src="/assets/js/damoa.js"></script>
    <script src="/assets/js/collecter.js"></script>
    <script src="/assets/js/moment.js"></script>
</body>

</html>

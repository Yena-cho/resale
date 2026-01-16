<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script type="text/javascript">

    //이체실행 버튼
    function goPaySol(vaNo) {
        /*
            var visitedAt = (new Date()).getTime(); // 방문 시간
            var and_pkg = "com.shinhan.sbanking"; //패키지명
            var and_scheme = "sbankandnor://PR02010100001?입금계좌입력방식=08&입금계좌번호="+vaNo;
            var and_store_url = "market://details?id=com.shinhan.sbanking";
            var ios_scheme = "iphoneSbank://";
            var ios_store_url = "https://itunes.apple.com/kr/app/id357484932?mt=8";
            var openAt = new Date();
            var userAgent = navigator.userAgent.toLocaleLowerCase();
            var chrome25;
            var kitkatWebview;
               if(_checker.android){
                   chrome25 = userAgent.search("chrome") > -1 && navigator.appVersion.match(/Chrome\/\d+.\d+/)[0].split("/")[1] >25;

                   kitkatWebview = userAgent.indexOf("naver") != -1 || userAgent.indexOf("daum") != -1;

                   if(chrome25 && !kitkatWebview){
                       var tempWin = window.open("intent://#Intent;scheme=" + and_scheme + ";package=" + and_pkg + ";end");
                       sleep(500);
                       if(tempWin != null){
                           tempWin.close();
                       }
                   }else{
                       document.location.href="intent://#Intent;scheme=" + and_scheme + ";package=" + and_pkg + ";end";
                   }
               }else if (_checker.iphone || _checker.ipad){
                setTimeout(
                       function() {
                           if ((new Date()).getTime() - visitedAt < 2000) { // 앱이 없을시 실행되는 구문 -- 앱 스토어로 이동
                                location.href = ios_store_url;
                           }
                        }, 500);
                    setTimeout(function() { // 앱이 있으면 앱실행
                         location.href = ios_scheme;
                     }, 0);
               }
        */
        var visitedAt = (new Date()).getTime(); // 방문 시간
        var and_pkg = "com.shinhan.sbanking"; //패키지명
        var and_scheme = "sbankandnor://PR02010100001?입금계좌입력방식=08&입금계좌번호=" + vaNo;
        var and_store_url = "market://details?id=com.shinhan.sbanking";
        //var ios_scheme = "iphoneSbank://";
        var ios_scheme = "iphoneSbank:\/\/";
        var ios_store_url = "https://itunes.apple.com/kr/app/id357484932?mt=8";
        var openAt = new Date();
        var userAgent = navigator.userAgent.toLocaleLowerCase();
        var chrome25;
        var kitkatWebview;
        if (_checker.android) {
            chrome25 = userAgent.search("chrome") > -1 && navigator.appVersion.match(/Chrome\/\d+.\d+/)[0].split("/")[1] > 25;

            kitkatWebview = userAgent.indexOf("naver") != -1 || userAgent.indexOf("daum") != -1;

            if (chrome25 && !kitkatWebview) {
                var tempWin = window.open("intent://#Intent;scheme=" + and_scheme + ";package=" + and_pkg + ";end");

                sleep(500);
                if (tempWin != null) {
                    tempWin.close();
                }

            } else {
                document.location.href = "intent://#Intent;scheme=" + and_scheme + ";package=" + and_pkg + ";end";
            }
        } else if (_checker.iphone || _checker.ipad) {
            var myVar;

            function open_appstore() {
                clearTimeout(myVar);
                window.location = ios_store_url;
            }

            myVar = setTimeout('open_appstore()', 100);

            window.location = ios_scheme;
        }
        /* }else if (_checker.iphone || _checker.ipad){
           setTimeout(
                  function() {
                      if ((new Date()).getTime() - visitedAt < 2000) { // 앱이 없을시 실행되는 구문 -- 앱 스토어로 이동
                           location.href = ios_store_url;
                      }
           }, 500);

           setTimeout(function() { // 앱이 있으면 앱실행
                        location.href = ios_scheme;
           }, 0);
        } */
    }

    function getiOSVersion() {
        var uAgent = navigator.userAgent;
        var startIndex = uAgent.search(/OS\s\d/gm);
        var endIndex = uAgent.indexOf("like Mac OS", startIndex);

        var aVersionInfo = uAgent.substring(startIndex + 3, endIndex - 1).split("_");
        var returnV = parseInt(aVersionInfo[0], 10);

        return returnV;
    }

    var AgentType = navigator.userAgent;

    var _checker = {
        iphone: AgentType.match(/(iPhone|iPod)/),
        ipad: AgentType.match(/iPad/),
        android: AgentType.match(/Android/)
    };

    function sleep(msec) {
        var start = new Date().getTime();
        var cur = start;
        while (cur - start < msec) {
            cur = new Date().getTime();
        }
    }

</script>

<div class="modal fade" id="popup-transfer-by-sol" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">SOL 간편송금</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="modal-body">
                <form>
                    <div class="container table-container mt-3 mb-3">
                        <div id="payment-info" class="list-id">
                            <div class="row">
                                <div class="col">
                                    <table class="table table-sm table-hover table-primary mb-3">
                                        <tbody>
                                            <tr>
                                                <th>납부자명</th>
                                                <td>${map.cusName}</td>
                                            </tr>
                                            <tr>
                                                <th>납부계좌</th>
                                                <td>${map.vaNo}</td>
                                            </tr>
                                            <tr>
                                                <th>수납기관명</th>
                                                <td class="table-title-ellipsis">${map.chaInfo.chaName}</td>
                                            </tr>
                                            <tr>
                                                <th>납부금액</th>
                                                <td id="popUnSubTot"></td>
                                            </tr>
                                            <tr>
                                                <th>납부마감일</th>
                                                <td id="popEndDate"></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="container list-button-group-bottom text-center">
                        <div class="row">
                            <div class="col">
                                <button type="button" class="btn btn-primary" onclick="goPaySol('${map.vaNo}');" id="btnSol">신한 SOL 로 송금하기</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

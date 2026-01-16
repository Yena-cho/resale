<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" 	 uri="http://www.springframework.org/security/tags" %>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_GROUP_USER')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/></s:authorize>

<script>
    var firstDepth = "nav-link-06";
    var secondDepth = "sub-01";
</script>

<script type="text/javascript">

    //파일 다운로드
    function fn_sampleView(str){
        fncClearTime();
        var titleName = "";
        var fileName = "";

        if(str == 'print') {
            titleName = "고지서양식 Sample";
            fileName  = "popup1";
        } else if(str == 'sms') {
            titleName = "문자메시지 발송 안내";
            fileName  = "popup2";
        } else if(str == 'email') {
            titleName = "E-MAIL고지서양식 Sample";
            fileName  = "popup3";
        } else if(str == 'at') {
            titleName = "카카오 알림톡 고지 Sample";
            fileName  = "popup4";
        }

        fn_image(fileName);

        if (fileName == 'popup1') {
            $('#sampleId').text(titleName);
            $('#damoaSample').modal({backdrop:'static', keyboard:false});
        } else if (fileName == 'popup2') {
            $('#sampleId2').text(titleName);
            $('#damoaSample2').modal({backdrop:'static', keyboard:false});
        } else if (fileName == 'popup3') {
            $('#sampleId3').text(titleName);
            $('#damoaSample3').modal({backdrop:'static', keyboard:false});
        } else if (fileName == 'popup4') {
            $('#sampleId4').text(titleName);
            $('#damoaSample4').modal({backdrop:'static', keyboard:false});
        }
    }

</script>
<input type="hidden" id="fileName" name="fileName">
<div id="contents">
    <div class="container">
        <div id="page-title">
            <div class="row">
                <div class="col-6">
                    <h2>가상계좌 수납관리 서비스란?</h2>
                </div>
                <div class="col-6 text-right">
                    <%--<button type="button" class="btn btn-img mr-2">--%>
                    <%--<img src="/assets/imgs/common/btn-typo-control.png">--%>
                    <%--</button>--%>
                    <%--<button type="button" class="btn btn-img">--%>
                    <%--<img src="/assets/imgs/common/btn-print.png">--%>
                    <%--</button>--%>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div id="page-description">
            <div class="row">
                <div class="col-sm-10 col-12">
                    <p>다수의 이용자와 거래를 하는 기업 및 단체 등이 하나의 사이트를 통하여 편리하고 저렴한 수수료로 이용이 가능한 통합 수납관리 서비스입니다.</p>
                </div>
                <%--<div class="col-sm-2 col-12 text-right">
                    <img src="/assets/imgs/common/icon-bill-with-monitor.png" width="130">
                </div>--%>
            </div>
        </div>
    </div>
    <div class="container damoa-summary-0 mb-4">
        <div class="row">
            <div class="col-12">
                <h6 class="mb-3"><strong>가상계좌 수납관리 서비스는</strong></h6>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6 col-sm-12 mb-2">
                <span class="badge round light-blue mr-2">1</span>자동화를 통한 효율적인 수납관리
            </div>

        </div>
        <div class="row">
            <div class="col-md-6 col-sm-12 mb-2">
                <span class="badge round light-blue mr-2">2</span>언제, 어디든, 누구나 가능한 실시간 수납관리
            </div>
        </div>
        <div class="row">
            <div class="col-md-8 col-sm-12 mb-2">
                <span class="badge round light-blue mr-2">3</span>고객별 가상계좌 부여를 통한 간편한 입금자 확인 가능
            </div>
        </div>
        <div class="row">
            <div class="col-md-6 col-sm-12 mb-2">
                <span class="badge round light-blue mr-2">4</span>가상계좌를 통한 수납으로 카드 수수료 대비 비용 절감
            </div>
        </div>
        <div class="row">
            <div class="col-md-6 col-sm-12 mb-2">
                <span class="badge round light-blue mr-2">5</span>다양한 수납방법을 통한 납부고객 편의성 증대
            </div>
        </div>
        <div class="row">
            <div class="col-md-6 col-sm-12 mb-2">
                <span class="badge round light-blue mr-2">6</span>현금영수증 발급 대행 서비스 지원
            </div>
        </div>
        <div class="row">
            <div class="col-md-12 col-sm-12 mb-2">
                <span class="badge round light-blue mr-2">7</span>합리적인 수수료로 이용 가능한 각종 고객 서비스 제공(문자메시지, 알림톡 등)
            </div>
        </div>
    </div>
    <div class="container">
        <div class="damoa-summary-1 mb-5">
            <div class="row align-items-center">
                <div class="col col-md-4 col-12">
                    <div class="row align-items-center">
                        <div class="col col-md-12 col-4 no-bordered">
                            <img src="/assets/imgs/common/icon-user-with-cog.png" width="69">
                        </div>
                        <div class="col col-md-12 col-8">
                            <h5>고객관리</h5>
                            <p>고객 등록 시 자동으로<br/>가상계좌번호 부여</p>
                            <div class="divider"></div>
                            <p>고객 대량등록 기능으로<br/>중복 작업 최소화</p>
                        </div>
                    </div>
                </div>
                <div class="col col-md-4 col-12">
                    <div class="row align-items-center">
                        <div class="col col-md-12 col-4 no-bordered">
                            <img src="/assets/imgs/common/icon-bill-with-cog.png" width="73">
                        </div>
                        <div class="col col-md-12 col-8">
                            <h5>청구관리</h5>
                            <p>부여된 가상계좌로<br/>요금청구 및 고지</p>
                            <div class="divider"></div>
                            <p>다양한 고지 방법을 제공하여<br/>업무효율성 극대화</p>
                        </div>
                    </div>
                </div>
                <div class="col col-md-4 col-12">
                    <div class="row align-items-center">
                        <div class="col col-md-12 col-4 no-bordered">
                            <img src="/assets/imgs/common/icon-won-with-cog.png" width="77">
                        </div>
                        <div class="col col-md-12 col-8">
                            <h5>수납관리</h5>
                            <p>가상계좌로 입금된 내역<br/>실시간 조회 및 미납 관리</p>
                            <div class="divider"></div>
                            <p>별도 수기/환불 등록이 가능하여<br/>수납 관리 업무 간소화</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="damoa-summary-2 mb-5">
            <div class="row">
                <div class="col-12 mb-3">
                    <h6><strong>이용대상</strong></h6>
                    <p>가상계좌 수납관리 서비스는 다수의 고객과 거래하는 누구나 제한 없이 이용이 가능한 서비스입니다.</p>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 col-sm-12 form-inline mb-4">
                    <div class="img-area">
                        <img src="/assets/imgs/common/icon-wallet.png">
                    </div>
                    <div class="text-area">
                        <h6>다수에게 정기적 또는 비정기적인<br/>대금을 수납받는 일반 기업</h6>
                        <p>입금 가능 금액 및 납부기한 설정 가능</p>
                    </div>
                </div>
                <div class="col-md-6 col-sm-12 form-inline mb-4">
                    <div class="img-area">
                        <img src="/assets/imgs/common/icon-school.png">
                    </div>
                    <div class="text-area">
                        <h6>학교, 학원, 어린이집, 유치원 등 다양한 교육기관</h6>
                        <p>수금 목적에 따른 정산계좌 분리 가능<br/>(용도별 계좌 설정에 따른 정산)</p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 col-sm-12 form-inline mb-4">
                    <div class="img-area">
                        <img src="/assets/imgs/common/icon-bag-with-coin.png">
                    </div>
                    <div class="text-area">
                        <h6>종교단체, 소모임, 조합, 동호회 등 소규모 단체 및 기관단체</h6>
                        <p>월회비, 교회헌금, 십일조 등을 편리하게 수납 받고 관리 가능</p>
                    </div>
                </div>
                <div class="col-md-6 col-sm-12 form-inline mb-4">
                    <div class="img-area">
                        <img src="/assets/imgs/common/icon-building.png">
                    </div>
                    <div class="text-area">
                        <h6>부동산임대, 정수기렌탈, 자동차렌탈 등 임대 및 렌탈 업체</h6>
                        <p>실시간 입금 내역 확인 및 판매 현황 체크 가능</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <div class="damoa-summary-3 mb-5">
            <div class="row">
                <div class="col-12">
                    <h6><strong>고지방법</strong></h6>
                </div>
            </div>
            <div class="row">
                <div class="col col-md-3 col-sm-3 col-12 text-center">
                    <div class="wrapper align-items-center">
                        <div class="mx-auto">
                            <div style="height: 120px;">
                                <img src="/assets/imgs/common/img-printer.png" width="71" class="mb-4">
                            </div>
                            <h6>방법1. 출력 서비스</h6>
                            <a class="btn btn-sm btn-gray-outlined" style="margin-top: 30px;" onclick="fn_sampleView('print');">고지서양식 샘플보기</a>
                        </div>
                    </div>
                </div>
                <div class="col col-md-3 col-sm-3 col-12 text-center">
                    <div class="wrapper align-items-center">
                        <div class="mx-auto">
                            <div style="height: 120px;">
                                <img src="/assets/imgs/common/img-mobile-sms.png" width="61" class="mb-4">
                            </div>
                            <h6>방법2. 문자발송 서비스</h6>
                            <p>(SMS/LMS 선택)</p>
                            <a class="btn btn-sm btn-gray-outlined" onclick="fn_sampleView('sms');">문자발송 안내보기</a>
                        </div>
                    </div>
                </div>
                <div class="col col-md-3 col-sm-3 col-12 text-center">
                    <div class="wrapper align-items-center">
                        <div class="mx-auto">
                            <div style="height: 120px;">
                                <img src="/assets/imgs/common/img-email-in-computer.png" width="84" class="mb-4">
                            </div>
                            <h6>방법3. E-MAIL 발송 서비스</h6>
                            <p>&nbsp;</p>
                            <a class="btn btn-sm btn-gray-outlined" onclick="fn_sampleView('email');">E-MAIL 고지서양식 샘플보기</a>
                        </div>
                    </div>
                </div>
                <div class="col col-md-3 col-sm-3 col-12 text-center">
                    <div class="wrapper align-items-center">
                        <div class="mx-auto">
                            <div style="height: 120px;">
                                <img src="/assets/imgs/common/kakao-logo.png" width="84" class="mb-4">
                            </div>
                            <h6>방법4. 카카오 알림톡 발송 서비스</h6>
                            <p>&nbsp;</p>
                            <a class="btn btn-sm btn-gray-outlined" onclick="fn_sampleView('at');">알림톡 샘플보기</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_GROUP_USER')"><jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/footer.jsp" flush="false"/></s:authorize>

<jsp:include page="/WEB-INF/views/include/modal/damoaSample.jsp" flush="false" />

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" 	   uri="http://www.springframework.org/security/tags" %>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_GROUP_USER')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/></s:authorize>

<script>
var firstDepth = "nav-link-06";
var secondDepth = "sub-02";
</script>

<script type="text/javascript">
// 신한은행 지점 검색
function fn_bankSearch() {
    var url = "http://www.shinhan.com/websquare/websquare_contents.jsp?w2xPath=/contents/about_bank/branch/internal01_type01.xml";
    window.open(url, "window", "width=900, height=700");
}
</script>

<div id="contents">
    <div class="container mb-3">
        <div id="page-title">
            <div class="row">
                <div class="col-6">
                    <h2>이용안내</h2>
                </div>
                <div class="col-6 text-right"></div>
            </div>
        </div>
    </div>

    <div class="container mb-4">
        <div class="how-to-use-step">
            <div class="row align-items-center">
                <div class="col col-md-3 col-sm-6 col-6 text-center">
                    <h5>Step1</h5>
                    <p>사전 서류(실명확인용) 준비,<br/>이용 신청서 작성 및 서류 제출</p>
                    <img src="/assets/imgs/common/icon-pencil-on-document.png" width="63">
                </div>
                <div class="col col-md-3 col-sm-6 col-6 text-center">
                    <h5>Step2</h5>
                    <p>추가서류 및 부가서비스 이용신청 서류제출<br/>운영사로 원본 등기발송</p>
                    <img src="/assets/imgs/common/icon-document-in-monitor.png" width="68">
                </div>
                <div class="col col-md-3 col-sm-6 col-6 text-center">
                    <h5>Step3</h5>
                    <p>이용승인<br/>해피콜 및 사용방법 안내</p>
                    <img src="/assets/imgs/common/icon-phone-with-book.png" width="89">
                </div>
                <div class="col col-md-3 col-sm-6 col-6 text-center">
                    <h5>Step4</h5>
                    <p>사이트 이용권한 등록<br/>서비스 정상이용 가능</p>
                    <img src="/assets/imgs/common/icon-coin-on-hand.png" width="62">
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col">
                <div class="tab-selecter type-3">
                    <ul class="nav nav-tabs" role="tablist">
                        <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#submit-application">신청방법</a></li>
                        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#commission">이용수수료</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <div class="container tab-content mt-3">
        <div id="submit-application" class="tab-pane fade show active">
            <div class="row">
                <div class="col col-12">
                    <h5 class="mb-3"><strong>■ 신청 시 준비서류</strong></h5>

                    <div class="row col">
                        <h6><strong>1. 기본 서류</strong></h6>
                    </div>

                    <p>
                        - 가상계좌 수납관리 서비스 이용 신청서<br>
                    </p>
                </div>
            </div>

            <div class="row">
                <div class="col col-12">
                    <strong>2. 실명 확인 서류</strong>
                </div>
            </div>

            <div class="row">
                <div class="table-responsive pd-n-mg-o">
                    <div>
                        <table class="table table-primary table-bordered">
                            <colgroup>
                                <col width="200">
                                <col width="450">
                                <col width="450">
                            </colgroup>

                            <thead>
                            <tr>
                                <th>구분</th>
                                <th>본인 신청시</th>
                                <th>대리인 신청시</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>법인사업자</td>
                                <td>
                                    법인사업자등록증<br/>
                                    법인등기부등본<br/>
                                    법인인감증명서<br/>
                                    대표자 신분증
                                </td>
                                <td>
                                    법인사업자등록증<br/>
                                    법인등기부등본<br/>
                                    법인인감증명서<br/>
                                    대리인 신분증<br/>
                                    법인인감이 날인된 위임장
                                </td>
                            </tr>
                            <tr>
                                <td>개인사업자</td>
                                <td>
                                    개인사업자등록증<br/>
                                    대표자 신분증
                                </td>
                                <td>
                                    대리인 신청 불가
                                </td>
                            </tr>
                            <tr>
                                <td>단체</td>
                                <td>
                                    고유번호증 또는 납세번호증<br/>
                                    대표자 신분증
                                </td>
                                <td>
                                    고유번호증 또는 납세번호증<br/>
                                    대표자 인감증명서<br/>
                                    대리인 신분증<br/>
                                    대표자 인감이 날인된 위임장
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col col-12">
                    <p><span style="color: #676767">* 법인등기부등본, 법인인감증명서, 대표자인감증명서는 최근 6개월 이내분</span></p>
                </div>
            </div>
        </div>


        <div id="commission" class="tab-pane fade show">
            <div class="row">
                <div class="col">
                    <h5 class="mb-3"><strong>■ 가상계좌 수납관리 서비스 월 이용수수료</strong></h5>
                    <p style="text-align: right;">(VAT포함)</p>
                </div>
            </div>

            <div class="row">
                <div class="table-responsive pd-n-mg-o">
                    <div>
                        <table class="table table-primary table-bordered">
                            <colgroup>
                                <col width="150">
                                <col width="200">
                                <col width="200">
                                <col width="200">
                                <col width="200">
                                <col width="200">
                            </colgroup>

                            <tbody>
                            <tr>
                                <th rowspan="2">서비스 이용요금</th>
                                <td style="height: 90px;"><strong>가상계좌 입금건당</br>(계좌입금건)</strong></td>
                                <td colspan="2">300원</br>(단, 100건 미만은 30,000원 정액)</td>
                            </tr>
                            <tr>
                                <td colspan="3" class="text-left" style="height: 120px; padding: 0 15px;">
                                    - <strong>수수료</strong> : 매월 10일에 출금계좌에서 자동출금<br>
                                    (당월사용분은 익월 초, 사용월 말일자로 세금계산서 발행)<br>
                                    <span style="color: #676767">* 공휴일이거나 영업일이 아닐 경우 익영업일에 출금</span>
                                </td>
                            </tr>
                            <tr>
                                <th rowspan="3">기타 부가서비스</th>
                                <td><strong>문자메시지</strong></td>
                                <td colspan="2">SMS 20원/건</br>LMS 40원/건</td>
                            </tr>
                            <tr>
                                <td><strong>알림톡</strong></td>
                                <td colspan="2">20원/건</td>
                            </tr>
                            <tr>
                                <td colspan="3" class="text-left" style="height: 90px; padding: 0 15px;">
                                    문자메시지 서비스, 카카오톡 알림톡 서비스 등 기타 부가서비스의 경우 수수료에 포함하여 청구
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col col-12">
                    <p>
                        <span style="color: #676767; font-size: .85rem;">* 2개월 연속 수수료 미납 시 가상계좌 수납관리 서비스가 중지 되오니 이점 인지하여 주시기 바랍니다. (미납 수수료 완납 시 별도의 가입 절차 없이 서비스 재이용 가능합니다.)</span>
                    </p>
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

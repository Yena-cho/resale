<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
    var MODE = ""; //등록 작업시 "I" , 수정작업시  "M" , 상세조회시 : "Q"
    var SAVE_MSG = "";
    var pgBankRate = 2.6;
    var nowChaSt = "";
</script>

<%-- <script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script> --%>
<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>

<div class="inmodal modal fade" id="popup-new-collecter-info" tabindex="-1" role="dialog" aria-labelledby="regPayer"
     aria-hidden="true">
    <div class="modal-dialog modal-big" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">기관정보상세보기</h5>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h3>기관기본정보</h3>
                        </div>
                    </div>
                </div>

                <form>
                    <div class="container-fluid" id="table_for_U">
                        <div class="row">
                            <div class="col-lg-12">
                                <table class="table table-bordered white-bg">
                                    <tbody>
                                        <tr>
                                            <th width="150">등록일자</th>
                                            <td width="352"><span id="mRegDt"></span></td>
                                            <th width="150">승인일자</th>
                                            <td width="354"><span id="mBnkAcptDt"></span></td>
                                        </tr>
                                        <tr>
                                            <th>기관코드</th>
                                            <td><span id="mDchacd" /></td>
                                            <th>사업자번호</th>
                                            <td><span id="mDchaoffno" /></td>
                                            <th>PG가맹점ID</th>
                                            <td><span id="mchtId" /></td>
                                        </tr>
                                        <tr>
                                            <th>기관명(업체명)</th>
                                            <td><span id="mDchaname" /></td>
                                            <th>다계좌 사용여부</th>
                                            <td class="form-inline"><span id="mDadjaccyn"></span></td>
                                        </tr>
                                        <tr>
                                            <th width="150">기관분류</th>
                                            <td width="352">
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="RdoChatrty1" value="01" name="RdoChatrty">
                                                    <label for="RdoChatrty1">WEB</label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="RdoChatrty2" value="03" name="RdoChatrty">
                                                    <label for="RdoChatrty2">API</label>
                                                </div>
                                            </td>
                                            <th width="150" style="vertical-align: middle;">이용방식</th>
                                            <td width="354" style="vertical-align: middle;">
                                                <div id="enable-validate-amount">
                                                    <div class="radio radio-primary radio-inline">
                                                        <input type="radio" id="enable-validate-amount-true" value="Y" name="enableValidateAmount" />
                                                        <label for="enable-validate-amount-true">승인</label>
                                                    </div>
                                                    <div class="radio radio-primary radio-inline">
                                                        <input type="radio" id="enable-validate-amount-L" value="L" name="enableValidateAmount" />
                                                        <label for="enable-validate-amount-false">한도</label>
                                                    </div>
                                                    <div class="radio radio-primary radio-inline">
                                                        <input type="radio" id="enable-validate-amount-false" value="N" name="enableValidateAmount" />
                                                        <label for="enable-validate-amount-false">통지</label>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>업태</th>
                                            <td><input type="text" class="form-control" id="mDchaType" maxlength="50"></td>
                                            <th>업종</th>
                                            <td><input type="text" class="form-control" id="mDchaStatus" maxlength="50"></td>
                                        </tr>
                                        <tr>
                                            <th>대표자명</th>
                                            <td><input type="text" class="form-control" id="mDowner" maxlength="50"></td>
                                            <th>대표전화번호</th>
                                            <td>
                                                <input type="text" class="form-control tel-input" id="mDownertel1" maxlength="4"><span class="tel-bar">-</span>
                                                <input type="text" class="form-control tel-input" id="mDownertel2" maxlength="4"><span class="tel-bar">-</span>
                                                <input type="text" class="form-control tel-input" id="mDownertel3" maxlength="4">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>주소</th>
                                            <td>
                                                <div class="form-group form-group-sm">
                                                    <input type="text" class="form-control postNo-input" readonly id="mDchazipcode">
                                                    <button type="button" class="btn btn-sm btn-d-gray" onclick="openZipSearch();">우편번호검색</button>
                                                </div>
                                                <div class="form-group form-group-sm">
                                                    <input type="text" class="form-control postTxt-input" id="mDchaaddress1" maxlength="100">
                                                    <input type="text" class="form-control postTxt-input" id="mDchaaddress2" maxlength="100">
                                                </div>
                                            </td>
                                            <th style="vertical-align: middle;">멀티로그인 제한</th>
                                            <td style="vertical-align: middle;">
                                                <div id="enable-multi-login">
                                                    <div class="radio radio-primary radio-inline">
                                                        <input type="radio" id="enable-multi-login-true" value="Y" name="enableMultiLogin" />
                                                        <label for="enable-multi-login-true">설정</label>
                                                    </div>
                                                    <div class="radio radio-primary radio-inline">
                                                        <input type="radio" id="enable-multi-login-false" value="N" name="enableMultiLogin" />
                                                        <label for="enable-multi-login-false">해제</label>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>담당자명</th>
                                            <td><input type="text" class="form-control" id="mDchrname" maxlength="50"></td>
                                            <th>담당자 E-MAIL</th>
                                            <td colspan="3">
                                                <div class="form-group form-group-sm">
                                                    <input type="text" class="form-control email-input" id="chrmail1">
                                                    <span class="ml-1 mr-1 tel-bar">@</span>
                                                    <input type="text" class="form-control email-input" id="chrmail2">
                                                </div>
                                                <div class="form-group form-group-sm">
                                                    <select class="form-control" id="chrmail3" onchange="fn_mailDomainChg(this.value);">
                                                        <option value="">직접입력</option>
                                                        <option value="naver.com">naver.com</option>
                                                        <option value="nate.com">nate.com</option>
                                                        <option value="yahoo.com">yahoo.com</option>
                                                        <option value="empal.com">empal.com</option>
                                                        <option value="gmail.com">gmail.com</option>
                                                        <option value="hanmail.net">hanmail.net</option>
                                                        <option value="daum.net">daum.net</option>
                                                    </select>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>담당자 전화번호</th>
                                            <td>
                                                <input type="text" class="form-control tel-input" id="mDchrtelno1" maxlength="4"><span class="tel-bar">-</span>
                                                <input type="text" class="form-control tel-input" id="mDchrtelno2" maxlength="4"><span class="tel-bar">-</span>
                                                <input type="text" class="form-control tel-input" id="mDchrtelno3" maxlength="4">
                                            </td>
                                            <th>담당자 핸드폰번호</th>
                                            <td>
                                                <input type="text" class="form-control tel-input" id="mDchrhp1" maxlength="4"><span class="tel-bar">-</span>
                                                <input type="text" class="form-control tel-input" id="mDchrhp2" maxlength="4"><span class="tel-bar">-</span>
                                                <input type="text" class="form-control tel-input" id="mDchrhp3" maxlength="4">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th style="vertical-align: middle;">부분납 사용여부</th>
                                            <td style="vertical-align: middle;">
                                                <div id="enable-partial-payment">
                                                    <div class="radio radio-primary radio-inline">
                                                        <input type="radio" id="enable-partial-payment-true" value="Y" name="enablePartialPayment" />
                                                        <label for="enable-partial-payment-true">사용</label>
                                                    </div>
                                                    <div class="radio radio-primary radio-inline">
                                                        <input type="radio" id="enable-partial-payment-false" value="N" name="enablePartialPayment" />
                                                        <label for="enable-partial-payment-false">사용안함</label>
                                                    </div>
                                                </div>
                                            </td>
                                            <th style="vertical-align: middle;">수취인명</th>
                                            <td style="vertical-align: middle;">
                                                <div id="enable-cusNameTy">
                                                    <div class="radio radio-primary radio-inline">
                                                        <input type="radio" id="cusNameTyU" value="U" name="cusNameTy" />
                                                        <label for="cusNameTyU">납부자명</label>
                                                    </div>
                                                    <div class="radio radio-primary radio-inline">
                                                        <input type="radio" id="cusNameTyC" value="C" name="cusNameTy" />
                                                        <label for="cusNameTyC">기관명</label>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-lg-12">
                                <h3>현금영수증 정보</h3>
                            </div>
                        </div>
                    </div>
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-lg-12">
                                <table class="table table-bordered white-bg">
                                <tbody>
                                <tr>
                                    <th style="vertical-align: middle;">현금영수증 발행여부</th>
                                    <td style="vertical-align: middle;">
                                        <div id="enable-rcpReceipt">
                                            <div class="radio radio-primary radio-inline">
                                                <input class="rcpReceipt" type="radio" id="enable-rcpReceipt-true" value="Y" name="rcpReceipt" />
                                                <label for="enable-rcpReceipt-true">사용</label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input class="rcpReceipt" type="radio" id="enable-rcpReceipt-false" value="N" name="rcpReceipt" />
                                                <label for="enable-rcpReceipt-false">사용안함</label>
                                            </div>
                                        </div>
                                    </td>
                                    <th style="vertical-align: middle;">의무발행업체 여부</th>
                                    <td style="vertical-align: middle;">
                                        <div id="mand-rcp">
                                            <div class="radio radio-primary radio-inline">
                                                <input class="mandRcp" type="radio" id="mand-rcp-true" value="Y" name="mandRcp" />
                                                <label for="mand-rcp-true">사용</label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input class="mandRcp" type="radio" id="mand-rcp-false" value="N" name="mandRcp" />
                                                <label for="mand-rcp-false">사용안함</label>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th style="vertical-align: middle;">현금영수증 자동여부</th>
                                    <td style="vertical-align: middle;">
                                        <div id="enable-rcpReceiptTy">
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="enable-rcpReceiptTy-true" value="A" name="rcpReceiptTy" />
                                                <label for="enable-rcpReceiptTy-true">사용</label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="enable-rcpReceiptTy-false" value="M" name="rcpReceiptTy" />
                                                <label for="enable-rcpReceiptTy-false">사용안함</label>
                                            </div>
                                        </div>
                                    </td>
                                    <th style="vertical-align: middle;">과세대상업체 여부</th>
                                    <td style="vertical-align: middle;">
                                        <div id="enable-noTaxYn">
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="enable-noTaxYn-true" value="Y" name="noTaxYn" />
                                                <label for="enable-noTaxYn-true">사용</label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="enable-noTaxYn-false" value="N" name="noTaxYn" />
                                                <label for="enable-noTaxYn-false">사용안함</label>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th style="vertical-align: middle;">수기수납 현금영수증 발행여부</th>
                                    <td style="vertical-align: middle;">
                                        <div id="enable-rcpReqSveTy">
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="enable-rcpReqSveTy-true" value="01" name="rcpReqSveTy" />
                                                <label for="enable-rcpReqSveTy-true">사용</label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="enable-rcpReqSveTy-false" value="00" name="rcpReqSveTy" />
                                                <label for="enable-rcpReqSveTy-false">사용안함</label>
                                            </div>
                                        </div>
                                    </td>
                                    <th style="vertical-align: middle;">수기수납 현금영수증 자동발행</th>
                                    <td style="vertical-align: middle;">
                                        <div id="enable-mnlRcpReqTy">
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="enable-mnlRcpReqTy-true" value="A" name="mnlRcpReqTy" />
                                                <label for="enable-mnlRcpReqTy-true">사용</label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="enable-mnlRcpReqTy-false" value="M" name="mnlRcpReqTy" />
                                                <label for="enable-mnlRcpReqTy-false">사용안함</label>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-lg-12">
                                <h3>다계좌정보</h3>
                            </div>
                        </div>
                    </div>
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-md-6 col-sm-12">
                                <table class="table table-bordered white-bg">
                                    <thead>
                                        <tr>
                                            <th width="50" class="text-center">NO</th>
                                            <th width="100" class="text-center">계좌 순번</th>
                                            <th width="*">계좌명</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <tr>
                                            <td class="text-center">1</td>
                                            <td class="text-center"><span id="tDadjfiregkey1">-</span></td>
                                            <td><span id="tDgrpadjname1">-</span></td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">2</td>
                                            <td class="text-center"><span id="tDadjfiregkey2">-</span></td>
                                            <td><span id="tDgrpadjname2">-</span></td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">3</td>
                                            <td class="text-center"><span id="tDadjfiregkey3">-</span></td>
                                            <td><span id="tDgrpadjname3">-</span></td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">4</td>
                                            <td class="text-center"><span id="tDadjfiregkey4">-</span></td>
                                            <td><span id="tDgrpadjname4">-</span></td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">5</td>
                                            <td class="text-center"><span id="tDadjfiregkey5">-</span></td>
                                            <td><span id="tDgrpadjname5">-</span></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>

                            <div class="col-md-6 col-sm-12">
                                <table class="table table-bordered white-bg">
                                    <thead>
                                        <tr>
                                            <th width="50" class="text-center">NO</th>
                                            <th width="100" class="text-center">계좌 순번</th>
                                            <th width="*">계좌명</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <tr>
                                            <td class="text-center">6</td>
                                            <td><span id="tDadjfiregkey6">-</span></td>
                                            <td><span id="tDgrpadjname6">-</span></td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">7</td>
                                            <td><span id="tDadjfiregkey7">-</span></td>
                                            <td><span id="tDgrpadjname7">-</span></td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">8</td>
                                            <td><span id="tDadjfiregkey8">-</span></td>
                                            <td><span id="tDgrpadjname8">-</span></td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">9</td>
                                            <td><span id="tDadjfiregkey9">-</span></td>
                                            <td><span id="tDgrpadjname9">-</span></td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">10</td>
                                            <td><span id="tDadjfiregkey10">-</span></td>
                                            <td><span id="tDgrpadjname10">-</span></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-lg-12">
                                <h3>수수료정보</h3>
                            </div>
                        </div>
                    </div>
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-lg-12">
                                <table class="table table-bordered white-bg">
                                    <thead>
                                        <tr>
                                            <th colspan="2" width="*" class="text-center">구분</th>
                                            <th width="180" class="text-center">범위</th>
                                            <th width="180" class="text-center">총 금액</th>
                                            <th width="180" class="text-center">은행</th>
                                            <th width="180" class="text-center">핑거</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <tr>
                                            <td rowspan="2" class="text-center">청구</td>
                                            <td class="text-center">정액</td>
                                            <td class="text-right">
                                                <input type="text" class="form-control text-right price-input-w110" id="mDnotminlimit" maxlength="5"><span>건 이하</span>
                                            </td>
                                            <td class="text-right"><span id="tDnotminfee"></span>원</td>
                                            <td class="text-right">0원</td>
                                            <td class="text-right form-inline">
                                                <input type="text" class="form-control text-right price-input-w140" id="mDnotminfingerfee" maxlength="5"><span>원</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">건당</td>
                                            <td class="text-right"><span id="tDnotminlimit1"></span>건 초과</td>
                                            <td class="text-right"><span id="tDnotcntfee"></span>원</td>
                                            <td class="text-right">0원</td>
                                            <td class="text-right form-inline">
                                                <input type="text" class="form-control text-right price-input-w140" id="mDnotcntfingerfee" maxlength="5"><span>원</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">수납</td>
                                            <td class="text-center">건당</td>
                                            <td class="text-right">1건 이상</td>
                                            <td class="text-right form-inline">
                                                <span id="mDrcpcntfee"></span> 원
                                            </td>
                                            <td class="text-right form-inline">
                                                <span id="mDrcpbnkfee"></span> 원
                                            </td>
                                            <td class="text-right form-inline">
                                                <span id="mDrcpfingerfee"></span> 원
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-lg-12">
                                <h3>지점정보</h3>
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-lg-12">
                                <table class="table table-bordered white-bg">
                                    <tbody>
                                        <tr>
                                            <th width="150">지점이름</th>
                                            <td width="352"><span id="tDagtname"></span></td>
                                            <th width="150">지점코드</th>
                                            <td width="354"><span id="tDagtcd"></span></td>
                                        </tr>
                                        <tr>
                                            <th width="150">메모</th>
                                            <td colspan="3">
                                                <input type="text" class="form-control input-sm" value="" maxlength=600 id="mDdaMngMemo">
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary save-cha-info" onclick="saveChaIfoSYS();"><i class="fa fa-fw fa-floppy-o"></i>정보수정</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $(".rcpReceipt").click(function () {
            if ($("#enable-rcpReceipt-true").prop("checked") && $("#mand-rcp-false").prop("checked")) {
                $("input[name=rcpReceiptTy]").prop("disabled", false);
                $("input[name=mnlRcpReqTy]").prop("disabled", false);
                $("input[name=noTaxYn]").prop("disabled", false);
                $("input[name=rcpReqSveTy]").prop("disabled",false);
                $("input[name=mandRcp]").prop("disabled",false);
            } else if ($("#enable-rcpReceipt-true").prop("checked") && $("#mand-rcp-true").prop("checked")) {
                $("input[name=rcpReceiptTy]").prop("disabled", true);
                $("input[name=mnlRcpReqTy]").prop("disabled", true);
                $("input[name=noTaxYn]").prop("disabled", false);
                $("input[name=rcpReqSveTy]").prop("disabled",false);
                $("input[name=mandRcp]").prop("disabled",false);
            } else{
                $("input[name=rcpReceiptTy]").prop("disabled", true);
                $("input[name=mnlRcpReqTy]").prop("disabled", true);
                $("input[name=noTaxYn]").prop("disabled", true);
                $("input[name=rcpReqSveTy]").prop("disabled",true);
                $("input[name=mandRcp]").prop("disabled",true);
                $("#enable-rcpReqSveTy-false").prop("checked", true);
            }
        });

        $(".mandRcp").click(function () {
            if ($("#mand-rcp-true").prop("checked")) {
                $("input[name=rcpReceiptTy]").prop("disabled", true);
                $("#enable-rcpReceiptTy-true").prop("checked", true);
                $("input[name=mnlRcpReqTy]").prop("disabled", true);
                $("#enable-mnlRcpReqTy-true").prop("checked", true);
            }else{
                $("input[name=rcpReceiptTy]").prop("disabled", false);
                $("input[name=mnlRcpReqTy]").prop("disabled", false);
            }
        });

    });

    function collector_info_init(mod, thisId) {
        for (var i = 0; i <= 9; i++) {
            var j = i + 1;
            $("#tDadjfiregkey" + j).text('');
            $("#tDgrpadjname" + j).text('');
        }

            SAVE_MSG = '기관정보수정이 완료되었습니다.';

            $("#table_for_U").show();
            $("#mDrcpcntfee").show();
            $("#mDrcpbnkfee").show();
            $("#mDrcpfingerfee").show();
            $("#mDdaMngMemo").show();
            $("#mDnotminlimit").show();
            $("#mDnotminfingerfee").show();
            $("#mDnotcntfingerfee").show();
            $(".save-cha-info").show();

            $("#modaltitle").text("기관정보상세보기");
            var _chacd = thisId;

            var url = "/sys/chaMgmt/selectChaListInfoAjax";
            var param = {
                chaCd: _chacd
            };

            $.ajax({
                type: "post",
                url: url,
                async: true,
                data: JSON.stringify(param),
                contentType: "application/json; charset=utf-8",
                success: function (data) {
                    var map = data.info;
                    // 기관기본정보

                    $("#mRegDt").text(defaultString(map.bnkRegiDt));
                    $("#mBnkAcptDt").text(defaultString(map.bnkAcptDt));

                    $("#mDchacd").text(map.chaCd);
                    var chaOffNo = "";
                    if(defaultString(map.chaOffNo) != ""){
                        chaOffNo = map.chaOffNo.substr(0,3)+"-"+map.chaOffNo.substr(3,2)+"-"+map.chaOffNo.substr(5,5);
                    }
                    $("#mDchaoffno").text(chaOffNo);
                    $("#mDchaname").text(map.chaName);
                    if (map.adjaccyn == "Y") {
                        $("#mDadjaccyn").text("사용");
                    } else {
                        $("#mDadjaccyn").text("미사용");
                    }

                    (map.chatrty == "01") ? $('#RdoChatrty1').prop('checked', true) : $('#RdoChatrty2').prop('checked', true);
                    (map.amtChkTy == 'Y') ? $('#enable-validate-amount-true').prop('checked', true) : $('#enable-validate-amount-false').prop('checked', true);

                    $("#mDchaType").val(map.chaType);
                    $("#mDchaStatus").val(map.chaStatus);

                    $("#mDowner").val(map.owner);
                    if (map.ownerTel != null && map.ownerTel != '') {
                        var ownertel = map.ownerTel;
                        ownertel = ownertel.split('-');
                        $("#mDownertel1").val(ownertel[0]);
                        $("#mDownertel2").val(ownertel[1]);
                        $("#mDownertel3").val(ownertel[2]);
                    } else {
                        $("#mDownertel1").val("");
                        $("#mDownertel2").val("");
                        $("#mDownertel3").val("");
                    }

                    $("#mDchazipcode").val(map.chaZipCode);
                    $("#mDchaaddress1").val(map.chaAddress1);
                    $("#mDchaaddress2").val(map.chaAddress2);

                    $("#mDchrname").val(map.chrName);
                    if (map.chrMail != null && map.chrMail != '') {
                        var _cusMail = map.chrMail;
                        _cusMail = _cusMail.split('@');
                        $("#chrmail1").val(_cusMail[0]);
                        $("#chrmail2").val(_cusMail[1]);
                        $("#chrmail3").val(_cusMail[1]).prop("selected", true);
                        if ($("#chrmail3").val() == null || $("#chrmail3").val() == '') {
                            $("#chrmail3").val("");
                        }
                        if ($("#chrmail3").val() != '') {
                            $("#chrmail2").prop("disabled", "disabled");
                        }
                    } else {
                        $("#chrmail1").val("");
                        $("#chrmail2").val("");
                        $("#chrmail3").val("").prop("selected", true);
                        $('#chrmail2').attr('disabled', false);
                    }

                    if (map.chrTelNo != null && map.chrTelNo != '') {
                        var chrtelno = map.chrTelNo;

                        chrtelno = chrtelno.split('-');
                        $("#mDchrtelno1").val(chrtelno[0]);
                        $("#mDchrtelno2").val(chrtelno[1]);
                        $("#mDchrtelno3").val(chrtelno[2]);
                    } else {
                        $("#mDchrtelno1").val("");
                        $("#mDchrtelno2").val("");
                        $("#mDchrtelno3").val("");
                    }
                    if (map.chrHp != null && map.chrHp != '') {
                        var chrhp = map.chrHp;
                        if (chrhp.indexOf('-') < 0 && chrhp.length >= 8) {
                            chrhp = getHpNoFormat(Number(chrhp));
                        }
                        chrhp = chrhp.split('-');
                        $("#mDchrhp1").val(chrhp[0]);
                        $("#mDchrhp2").val(chrhp[1]);
                        $("#mDchrhp3").val(chrhp[2]);
                    } else {
                        $("#mDchrhp1").val("");
                        $("#mDchrhp2").val("");
                        $("#mDchrhp3").val("");
                    }



                    if (map.mchtId != null && map.mchtId != '') {
                        var mchtId = map.mchtId;

                        $("#mchtId").val(mchtId);

                    } else {
                        $("#mchtId").val("");

                    }

                    (map.noTaxYn == 'Y') ? $('#enable-noTaxYn-true').prop('checked', true) : $('#enable-noTaxYn-false').prop('checked', true);
                    (map.partialPayment == 'Y') ? $('#enable-partial-payment-true').prop('checked', true) : $('#enable-partial-payment-false').prop('checked', true);

                    (map.rcpReqYn == 'Y') ? $('#enable-rcpReceipt-true').prop('checked', true) : $('#enable-rcpReceipt-false').prop('checked', true);
                    (map.rcpReqTy == 'A') ? $('#enable-rcpReceiptTy-true').prop('checked', true) : $('#enable-rcpReceiptTy-false').prop('checked', true);

                    (map.rcpReqSveTy == '01') ? $('#enable-rcpReqSveTy-true').prop('checked', true) : $('#enable-rcpReqSveTy-false').prop('checked', true);
                    (map.mnlRcpReqTy == 'A') ? $('#enable-mnlRcpReqTy-true').prop('checked', true) : $('#enable-mnlRcpReqTy-false').prop('checked', true);
                    (map.cusNameTy == 'U') ? $('#cusNameTyU').prop('checked', true) : $('#cusNameTyC').prop('checked', true);
                    (map.mandRcpYn == 'Y') ? $('#mand-rcp-true').prop('checked', true) : $('#mand-rcp-false').prop('checked', true);
                    (map.concurrentBlockYn == 'Y') ? $('#enable-multi-login-true').prop('checked', true) : $('#enable-multi-login-false').prop('checked', true);
                    // 다계좌정보
                    var j = 0;
                    $.each(data.agencyList, function (i, v) {
                        j = i + 1;
                        $("#tDadjfiregkey" + j).text(v.adjfiregKey);
                        $("#tDgrpadjname" + j).text(v.grpadjName);
                    });

                    $("#mDnotminlimit").val(numberToCommas(map.notMinLimit));
                    $("#tDnotminlimit1").text(numberToCommas(map.notMinLimit));
                    $("#tDnotminfee").text(numberToCommas(map.notMinFee));
                    $("#tDnotcntfee").text(numberToCommas(map.notCntFee));
                    $("#mDnotminfingerfee").val(numberToCommas(map.notMinFee));
                    $("#mDnotcntfingerfee").val(numberToCommas(map.notCntFee));
                    $("#mDrcpcntfee").text(numberToCommas(map.rcpCntFee));
                    $("#mDrcpbnkfee").text(numberToCommas(map.rcpBnkFee));
                    var rcpfingerfee = map.rcpCntFee - map.rcpBnkFee;
                    $("#mDrcpfingerfee").text(numberToCommas(rcpfingerfee));

                    // 지점정보
                    $("#tDagtname").text(map.agtName);
                    $("#tDagtcd").text(map.agtCd);
                    $("#mDdaMngMemo").val(map.remark);

                    receiptYnCheck();
                }
            });
    }

    function receiptYnCheck(){
        if ($("#enable-rcpReceipt-true").prop("checked") && $("#mand-rcp-false").prop("checked")) {
            $("input[name=rcpReceiptTy]").prop("disabled", false);
            $("input[name=mnlRcpReqTy]").prop("disabled", false);
            $("input[name=noTaxYn]").prop("disabled", false);
            $("input[name=rcpReqSveTy]").prop("disabled",false);
            $("input[name=mandRcp]").prop("disabled",false);
        } else if ($("#enable-rcpReceipt-true").prop("checked") && $("#mand-rcp-true").prop("checked")) {
            $("input[name=rcpReceiptTy]").prop("disabled", true);
            $("input[name=mnlRcpReqTy]").prop("disabled", true);
            $("input[name=noTaxYn]").prop("disabled", false);
            $("input[name=rcpReqSveTy]").prop("disabled",false);
            $("input[name=mandRcp]").prop("disabled",false);
        } else{
            $("input[name=rcpReceiptTy]").prop("disabled", true);
            $("input[name=mnlRcpReqTy]").prop("disabled", true);
            $("input[name=noTaxYn]").prop("disabled", true);
            $("input[name=rcpReqSveTy]").prop("disabled", true);
            $("input[name=mandRcp]").prop("disabled",true);
        }
    }

    //저장(save)
    function saveChaIfoSYS() {
        var url = "";
        var param = {};

            //수정시 필드 검증 함수 추가
            if (!fnValidationMod()) {
                return;
            }

            var ownertel = "";
            if ($("#mDownertel1").val().length > 3 && $("#mDownertel3").val() == "") {
                ownertel = $("#mDownertel1").val() + "-" + $("#mDownertel2").val();
            } else {
                ownertel = $("#mDownertel1").val() + "-" + $("#mDownertel2").val() + "-" + $("#mDownertel3").val();
            }

            url = "/sys/chaMgmt/updateChaInfo";
            param = {
                chatrty: $("input[name=RdoChatrty]:checked").val()
                , amtChkTy: $('#enable-validate-amount').find(':checked').val()
                , chaType: defaultString($("#mDchaType").val())
                , chaStatus: defaultString($("#mDchaStatus").val())
                , owner: $("#mDowner").val()
                , ownerTel: ownertel
                , chaZipCode: $("#mDchazipcode").val()
                , chaAddress1: $("#mDchaaddress1").val()
                , chaAddress2: $("#mDchaaddress2").val()
                , chrName: $("#mDchrname").val()
                , chrMail: $("#chrmail1").val() + "@" + $("#chrmail2").val()
                , chrTelNo: $("#mDchrtelno1").val() + "-" + $("#mDchrtelno2").val() + "-" + $("#mDchrtelno3").val()
                , chrHp: $("#mDchrhp1").val() + "-" + $("#mDchrhp2").val() + "-" + $("#mDchrhp3").val()
                , noTaxYn: $('#enable-noTaxYn').find(':checked').val()
                , partialPayment: $('#enable-partial-payment').find(':checked').val()
                , rcpReqYn: $('#enable-rcpReceipt').find(':checked').val()
                , rcpReqTy: $('#enable-rcpReceiptTy').find(':checked').val()
                , rcpReqSveTy: $('#enable-rcpReqSveTy').find(':checked').val()
                , mnlRcpReqTy: $('#enable-mnlRcpReqTy').find(':checked').val()
                , mandRcpYn: $('#mand-rcp').find(':checked').val()
                , notMinLimit: $("#mDnotminlimit").val().replace(/\,/gi, "")
                , notMinFee: $("#mDnotminfingerfee").val().replace(/\,/gi, "")
                , notCntFee: $("#mDnotcntfingerfee").val().replace(/\,/gi, "")
                , remark: defaultString($("#mDdaMngMemo").val())
                , chaCd: $("#mDchacd").text()
                , jobType: "N"
                , cusNameTy: nullValueChange($("input[name=cusNameTy]:checked").val())
                , concurrentBlockYn: $('#enable-multi-login').find(':checked').val()
            };
        swal({
            type: 'question',
            html: "저장 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (!result.value) {
                return;
            }
            
            $.ajax({
                type: "post",
                url: url,
                async: true,
                data: JSON.stringify(param),
                contentType: "application/json; charset=utf-8",
                success: function (result) {
                    if (result.retCode != "0000") {
                        swal({
                            type: 'error',
                            text: result.retMsg,
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        });
                        return;
                    }
                    
                    swal({
                        type: 'success',
                        text: '기관정보수정이 완료되었습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $("#popup-new-collecter-info").modal("hide");
                        pageChange();
                    });
                }
            });
        });
    }

    function fnValidationMod() {

        if ($('#mDowner').attr('readonly') != 'readonly') {
            if ($('#mDowner').val() == "") {
                swal({
                    type: 'info',
                    text: '대표명을 입력해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function () {
                    $('#mDowner').focus();
                });
                return;
            }
        }

        if ($('#mDownertel1').attr('readonly') != 'readonly') {
            if ($('#mDownertel1').val() == "" || $('#mDownertel2').val() == "" || $('#mDownertel3').val() == "") {
                if ($('#mDownertel1').val().length < 4 && $('#mDownertel3').val() == "") {
                    swal({
                        type: 'info',
                        text: '대표 전화번호를 입력해 주세요.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $('#mDownertel1').focus();
                    });
                    return;
                }
            }
        }

        if ($('#mDchrname').val() == "") {
            swal({
                type: 'info',
                text: '담당자를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#mDchrname').focus();
            });
            return;
        }

        if ($('#mDchrtelno1').val() == "" || $('#mDchrtelno2').val() == "" || $('#mDchrtelno3').val() == "") {
            swal({
                type: 'info',
                text: '담당자 전화번호를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#mDchrtelno1').focus();
            });
            return;
        }

        if ($('#mDchrhp1').val() == "" || $('#mDchrhp2').val() == "" || $('#mDchrhp3').val() == "") {
            swal({
                type: 'info',
                text: '담당자 핸드폰번호를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#mDchrhp1').focus();
            });
            return;
        }

        if ($('#chrmail1').val() == "" || $('#chrmail2').val() == "") {
            swal({
                type: 'info',
                text: '담당자 이메일주소를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#chrmail1').focus();
            });
            return;
        }

        if ($('#mDchazipcode').val() == "" || $('#mDchaaddress1').val() == "" || $('#mDchaaddress2').val() == "") {
            swal({
                type: 'info',
                text: '주소를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if ($("#mDnotminlimit").val() == "") {
            swal({
                type: 'info',
                text: '청구정액기준을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#mDnotminlimit').focus();
            });
            return;
        }

        if ($("#mDnotminfingerfee").val() == "") {
            swal({
                type: 'info',
                text: '청구정액수수료를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#mDnotminfingerfee').focus();
            });
            return;
        }

        if ($("#mDnotcntfingerfee").val() == "") {
            swal({
                type: 'info',
                text: '청구건당수수료를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#mDnotminfingerfee').focus();
            });
            return;
        }

        return true;
    }

    function openZipSearch() {
        new daum.Postcode({

            oncomplete: function (data) {
                $('[id=mDchazipcode]').val(data.zonecode); // 우편번호 (5자리)
                $('[id=mDchaaddress1]').val(data.address);
                $('[id=mDchaaddress2]').val(data.buildingName);
            }
        }).open();
    }

    //도메인 선택시 변경
    function fn_mailDomainChg(val) {
        if (val == '') { // 직접입력
            $('#chrmail2').val(val);
            $('#chrmail2').attr('disabled', false);
        } else {
            $('#chrmail2').val(val);
            $('#chrmail2').attr('disabled', true);
        }
    }

</script>

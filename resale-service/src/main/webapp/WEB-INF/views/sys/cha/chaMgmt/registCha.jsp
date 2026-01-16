<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false" />
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">
<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>

<script src="/assets/js/common.js?version=${project.version}"></script>
<script>
    var oneDepth = "adm-nav-2";
    var twoDepth = "adm-sub-02";
</script>

<style>
    .m0 {margin:0px !important;}
    .wrapper-content {
        max-width: 1200px;
        margin-left: auto;
        margin-right: auto;
    }
</style>

</div>
<form id="frm" name="frm" method="post">
	<input type="hidden" id="loginId" name="loginId">
</form>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>이용기관등록</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>이용기관관리</a>
            </li>
            <li class="active">
                <strong>이용기관등록</strong>
            </li>
        </ol>
        <p class="page-description">기관 정보를 등록하는 화면입니다.</p>
    </div>
    <div class="col-lg-2">

    </div>
</div>

<div class="wrapper-content">
    <div class="animated fadeInRight article">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>이용기관 등록</h5>
                    </div>

                    <div class="ibox-content">
                        <form>
                            <div class="container-fluid">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <h3>기관 기본 정보</h3>
                                    </div>
                                </div>
                            </div>

                            <div class="container-fluid" id="table_for_U">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <table class="table table-bordered white-bg">
                                            <colgroup>
                                                <col width="15%">
                                                <col width="35%">
                                                <col width="15%">
                                                <col width="35%">
                                            </colgroup>
                                            <tbody>
                                            <tr>
                                                <th>은행코드</th>
                                                <td colspan="3">
                                                    <div class="radio radio-primary radio-inline">
                                                        <input type="radio" id="bankA" value="20008155" name="bankId" checked>
                                                        <label for="bankA">일반(20008155)</label>
                                                    </div>
                                                    <div class="radio radio-primary radio-inline">
                                                        <input type="radio" id="bankB" value="20008153" name="bankId">
                                                        <label for="bankB">고속도로(20008153)</label>
                                                    </div>
                                                </td>
<%--                                                <th>기관분류</th>--%>
<%--                                                <td>--%>
<%--                                                    <div class="radio radio-primary radio-inline">--%>
<%--                                                        <input type="radio" id="RdoChatrty1" value="01" name="RdoChatrty" checked>--%>
<%--                                                        <label for="RdoChatrty1">WEB</label>--%>
<%--                                                    </div>--%>
<%--                                                    <div class="radio radio-primary radio-inline">--%>
<%--                                                        <input type="radio" id="RdoChatrty2" value="03" name="RdoChatrty">--%>
<%--                                                        <label for="RdoChatrty2">API</label>--%>
<%--                                                    </div>--%>
<%--                                                </td>--%>
                                            </tr>

                                            <tr>
                                                <th>기관분류 *</th>
                                                <td colspan="3">
                                                    <div class="form-group">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="RdoChatrty1" value="01" name="RdoChatrty">
                                                            <label for="RdoChatrty1">WEB</label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="RdoChatrty2" value="03" name="RdoChatrty">
                                                            <label for="RdoChatrty2">API</label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="RdoChatrty3" value="05" name="RdoChatrty">
                                                            <label for="RdoChatrty3">SET</label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="RdoChatrty4" value="07" name="RdoChatrty">
                                                            <label for="RdoChatrty4">RELAY</label>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <div calss="form-group">
                                                            <div class="col-sm-3 p-0">
                                                                수납통지 수신 URL :
                                                            </div>
                                                            <div class="col-sm-7">
                                                                <input type="text"  class="form-control" id="rcpNoticeUrl" maxlength="200" size=20 readonly placeholder="기관의 수납통지 수신 URL"/>
                                                            </div>
                                                        </div>
                                                        <div calss="form-group">
                                                            <div class="col-sm-3 p-0">
                                                                수취조회 API URL :
                                                            </div>
                                                            <div class="col-sm-7">
                                                                <input type="text"  class="form-control" id="chaApiUrlQuery" maxlength="200" size=20 readonly placeholder="기관의 수취조회 API URL"/>
                                                            </div>
                                                        </div>
                                                        <div calss="form-group">
                                                            <div class="col-sm-3 p-0">
                                                                입금통지 API URL :
                                                            </div>
                                                            <div class="col-sm-7">
                                                                <input type="text" class="form-control" id="chaApiUrlNotice" maxlength="200" size=20 readonly placeholder="기관의 입금통지 API URL"/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>

                                            <tr>
                                                <th>기관명(업체명) *</th>
                                                <td><input type="text" class="form-control" id="chaname" maxlength="50"></td>
                                                <th>사업자번호 *</th>
                                                <td>
                                                    <input type="text" class="form-control" id="chaoffno" maxlength="10">
<%--                                                    <span class="input-group-btn">  // + td에 class="input-group" 추가
                                                        <button class="btn btn-success btn-m" type="button" style="" onclick="lookupCompanyStatus()">휴폐업 조회</button>
                                                    </span>--%>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>대표전화번호 *</th>
                                                <td>
                                                    <input type="text" class="form-control tel-input" id="ownertel1" maxlength="4"><span class="tel-bar">-</span>
                                                    <input type="text" class="form-control tel-input" id="ownertel2" maxlength="4"><span class="tel-bar">-</span>
                                                    <input type="text" class="form-control tel-input" id="ownertel3" maxlength="4">
                                                </td>
                                                <th>대표자명 *</th>
                                                <td><input type="text" class="form-control" id="owner" maxlength="50"></td>
                                            </tr>
                                            <tr>
                                                <th>사용목적</th>
                                                <td><input type="text" class="form-control" id="usePurpose" maxlength="50"></td>
                                                <th>기관상태</th>
                                                <td>
                                                    <div class="radio radio-primary radio-inline">
                                                        <input type="radio" id="RdoChast1" value="ST06" name="RdoChast" checked>
                                                        <label for="RdoChast1">정상</label>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>업종</th>
                                                <td><input type="text" class="form-control" id="chaStatus" maxlength="50"></td>
                                                <th>업태</th>
                                                <td><input type="text" class="form-control" id="chaType" maxlength="50"></td>
                                            </tr>
                                            <tr>
                                                <th>담당자명 *</th>
                                                <td><input type="text" class="form-control" id="chrname" maxlength="50"></td>
                                                <th>담당자 전화번호 *</th>
                                                <td>
                                                    <input type="text" class="form-control tel-input" id="chrtelno1" maxlength="4"><span class="tel-bar">-</span>
                                                    <input type="text" class="form-control tel-input" id="chrtelno2" maxlength="4"><span class="tel-bar">-</span>
                                                    <input type="text" class="form-control tel-input" id="chrtelno3" maxlength="4">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>주소 *</th>
                                                <td colspan="3">
                                                    <div class="form-group form-group-sm m-b-none">
                                                        <input type="text" class="form-control postNo-input" readonly id="chazipcode">
                                                        <button type="button" class="btn btn-sm btn-d-gray" onclick="openZipSearch();">우편번호찾기</button>
                                                    </div>
                                                    <div class="form-group form-group-sm">
                                                        <input type="text" class="form-control postTxt-input" id="chaaddress1" maxlength="50">
                                                        <input type="text" class="form-control postTxt-input" id="chaaddress2" maxlength="50">
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>이메일 *</th>
                                                <td colspan="3">
                                                    <div class="form-inline input-group">
                                                        <div class="form-group"><input type="text" class="form-control email-input" id="chrmail1" maxlength="25"></div>
                                                        <span class="input-group-addon">@</span>
                                                        <div class="form-group"><input type="text" class="form-control email-input" id="chrmail2" maxlength="24"></div>
                                                        <div class="form-group ">
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
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>출금계좌번호</th>

                                                <td class="form-group">
                                                    <div class="col-sm-4 p-0">
                                                        <select class="form-control" id="feebankcode">
                                                        </select>
                                                    </div>
                                                    <div class="col-sm-6 p-x-10">
                                                        <input type="text" class="form-control" id="feeaccno" maxlength="20">
                                                    </div>
                                                    <div class="col-sm-2 p-0">
                                                        <label> <input type="checkbox" id="feepayty"> 선취 </label>
                                                    </div>
                                                </td>
                                                <th>다계좌 사용여부</th>
                                                <td>
                                                    <div id="enable-adjustment-account">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="enable-adjustment-account-true" value="Y" name="enableAdjustmentAccount"/>
                                                            <label for="enable-adjustment-account-true">사용</label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="enable-adjustment-account-false" value="N" name="enableAdjustmentAccount"/>
                                                            <label for="enable-adjustment-account-false">미사용</label>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>납부금액 확인여부</th>
                                                <td>
                                                    <div id="enable-validate-amount">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" class="validateAmountAndPeriod" id="enable-validate-amount-true" value="Y" name="enableValidateAmount" checked/>
                                                            <label for="enable-validate-amount-true">사용</label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" class="validateAmountAndPeriod" id="enable-validate-amount-false" value="N" name="enableValidateAmount" />
                                                            <label for="enable-validate-amount-false">미사용</label>
                                                        </div>
                                                    </div>
                                                </td>
                                                <th>부분납 사용여부</th>
                                                <td>
                                                    <div id="enable-partial-payment">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" class="kAndPeriod" id="enable-partial-payment-true" value="Y" name="enablePartialPayment" checked/>
                                                            <label for="enable-partial-payment-true">사용</label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" class="kAndPeriod" id="enable-partial-payment-false" value="N" name="enablePartialPayment"/>
                                                            <label for="enable-partial-payment-false">미사용</label>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>납부기간 확인여부</th>
                                                <td>
                                                    <div id="enable-validate-period">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" class="validateAmountAndPeriod" id="enable-validate-period-true" value="Y" name="enableValidatePeriod" checked/>
                                                            <label for="enable-validate-period-true">사용</label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" class="validateAmountAndPeriod" id="enable-validate-period-false" value="N" name="enableValidatePeriod" />
                                                            <label for="enable-validate-period-false">미사용</label>
                                                        </div>
                                                    </div>
                                                </td>
                                                <th>수취인명</th>
                                                <td>
                                                    <div id="enable-cusNameTy">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="cusNameTyU" value="U" name="cusNameTy"/>
                                                            <label for="cusNameTyU">고객명</label>
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
                                                <th>현금영수증 발행여부</th>
                                                <td>
                                                    <div id="enable-rcpReceipt">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input class="rcpReceipt" type="radio" id="enable-rcpReceipt-true" value="Y" name="rcpReceipt" />
                                                            <label for="enable-rcpReceipt-true">사용</label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input class="rcpReceipt" type="radio" id="enable-rcpReceipt-false" value="N" name="rcpReceipt"/>
                                                            <label for="enable-rcpReceipt-false">미사용</label>
                                                        </div>
                                                    </div>
                                                </td>
                                                <th>의무발행업체 여부</th>
                                                <td>
                                                    <div id="mand-rcp">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input class="mandRcp" type="radio" id="mand-rcp-true" value="Y" name="mandRcp" />
                                                            <label for="mand-rcp-true">사용</label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input class="mandRcp" type="radio" id="mand-rcp-false" value="N" name="mandRcp" checked/>
                                                            <label for="mand-rcp-false">미사용</label>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>현금영수증 자동 발행</th>
                                                <td>
                                                    <div id="enable-rcpReceiptTy">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="enable-rcpReceiptTy-true" value="A" name="rcpReceiptTy" />
                                                            <label for="enable-rcpReceiptTy-true">사용</label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="enable-rcpReceiptTy-false" value="M" name="rcpReceiptTy" checked/>
                                                            <label for="enable-rcpReceiptTy-false">미사용</label>
                                                        </div>
                                                    </div>
                                                </td>
                                                <th>과세대상업체 여부</th>
                                                <td>
                                                    <div id="enable-noTaxYn">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="enable-noTaxYn-true" value="Y" name="noTaxYn" checked/>
                                                            <label for="enable-noTaxYn-true">면세</label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="enable-noTaxYn-false" value="N" name="noTaxYn" />
                                                            <label for="enable-noTaxYn-false">과세</label>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>수기수납 현금영수증 발행여부</th>
                                                <td>
                                                    <div id="enable-rcpReqSveTy">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="enable-rcpReqSveTy-true" value="01" name="rcpReqSveTy" />
                                                            <label for="enable-rcpReqSveTy-true">사용</label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="enable-rcpReqSveTy-false" value="00" name="rcpReqSveTy" checked/>
                                                            <label for="enable-rcpReqSveTy-false">미사용</label>
                                                        </div>
                                                    </div>
                                                </td>
                                                <th>수기수납 현금영수증 자동발행</th>
                                                <td>
                                                    <div id="enable-mnlRcpReqTy">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="enable-mnlRcpReqTy-true" value="A" name="mnlRcpReqTy" />
                                                            <label for="enable-mnlRcpReqTy-true">사용</label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="enable-mnlRcpReqTy-false" value="M" name="mnlRcpReqTy" checked/>
                                                            <label for="enable-mnlRcpReqTy-false">미사용</label>
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
                                    <div class="col-lg-12">
                                        <table class="table table-bordered white-bg table-align-center" id="adj-table">
                                            <thead>
                                            <tr>
                                                <th width="50" class="text-center">NO</th>
                                                <th width="20%" class="text-center">계좌 순번</th>
                                                <th width="30%">계좌명</th>
                                                <th width=" *">계좌번호</th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <tr id="accInfo1">
                                                <td>1 *</td>
                                                <td><span class="adjfiregkey">00001</span></td>
                                                <td><input type="text" class="form-control grpadjname" id="accName1" maxlength="50"></td>
                                                <td class="form-group">
                                                    <div class="col-sm-3 p-0">
                                                        <select class="form-control bankCd" id="bankCd1">
                                                        </select>
                                                    </div>
                                                    <div class="col-sm-7">
                                                        <input type="text" class="form-control accno" id="accNum1" maxlength="20">
                                                    </div>
<%--                                                    <div class="col-sm-2 p-0 text-right">
                                                        <button class="btn btn-success m0" type="button" onclick="lookupCompanyStatus()">조회</button>
                                                    </div>--%>
                                                </td>
                                            </tr>
                                            <tr class="accInfoTr" id="acc2">
                                                <td>2</td>
                                                <td><span class="adjfiregkey">00002</span></td>
                                                <td><input type="text" class="form-control grpadjname" id="accName2" maxlength="50"></td>
                                                <td class="form-group">
                                                    <div class="col-sm-3 p-0">
                                                        <select class="form-control bankCd" id="bankCd2">
                                                        </select>
                                                    </div>
                                                    <div class="col-sm-7">
                                                        <input type="text" class="form-control accno" id="accNum2" maxlength="20">
                                                    </div>
<%--                                                    <div class="col-sm-2 p-0 text-right">
                                                        <button class="btn btn-success m0" type="button" onclick="lookupCompanyStatus()">조회</button>
                                                    </div>--%>
                                                </td>
                                            </tr>
                                            <tr class="accInfoTr" id="acc3">
                                                <td>3</td>
                                                <td><span class="adjfiregkey">00003</span></td>
                                                <td><input type="text" class="form-control grpadjname" id="accName3" maxlength="50"></td>
                                                <td class="form-group">
                                                    <div class="col-sm-3 p-0">
                                                        <select class="form-control bankCd" id="bankCd3">
                                                        </select>
                                                    </div>
                                                    <div class="col-sm-7">
                                                        <input type="text" class="form-control accno" id="accNum3" maxlength="20">
                                                    </div>
<%--                                                    <div class="col-sm-2 p-0 text-right">
                                                        <button class="btn btn-success m0" type="button" onclick="lookupCompanyStatus()">조회</button>
                                                    </div>--%>
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
                                        <h3>수수료정보</h3>
                                    </div>
                                </div>
                            </div>
                            <div class="container-fluid">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <table class="table table-bordered white-bg table-text-center">
                                            <thead>
                                            <tr>
                                                <th colspan="2" class="text-center" style="min-width: 200px;">구분</th>
                                                <th>범위</th>
                                                <th>총 금액</th>
                                                <th>은행</th>
                                                <th>핑거</th>
                                            </tr>
                                            </thead>

                                            <tbody class="input-text-right">
                                            <tr>
                                                <td rowspan="2">관리 *</td>
                                                <td>정액</td>
                                                <td>
                                                    <div class="input-group"><input type="text" class="form-control" id="notlimit" maxlength="5"><span class="input-group-addon border-n p-x-2">건 이하</span></div>
                                                </td>
                                                <td>
                                                    <div class="input-group"><div class="form-control border-n text-right" id="flatfee"></div><span class="input-group-addon border-n p-x-2">원</span></div>
                                                </td>
                                                <td>
                                                    <div class="input-group"><div class="form-control border-n text-right">0</div><span class="input-group-addon border-n p-x-2">원</span></div>
                                                </td>
                                                <td>
                                                    <div class="input-group"><input type="text" class="form-control" id="flatfingerfee" maxlength="6"><span class="input-group-addon border-n p-x-2">원</span></div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="text-center">건당</td>
                                                <td>
                                                    <div class="input-group"><div class="form-control border-n text-right" id="notlimitCopy">0</div><span class="input-group-addon border-n p-x-2">건 초과</span></div>
                                                </td>
                                                <td>
                                                    <div class="input-group"><div class="form-control border-n text-right" id="notcntfee"></div><span class="input-group-addon border-n p-x-2">원</span></div>
                                                </td>
                                                <td>
                                                    <div class="input-group"><div class="form-control border-n text-right">0</div><span class="input-group-addon border-n p-x-2">원</span></div>
                                                </td>
                                                <td>
                                                    <div class="input-group"><input type="text" class="form-control text-right" id="notfingerfee" maxlength="6"><span class="input-group-addon border-n p-x-2">원</span></div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>수납 *</td>
                                                <td>건당</td>
                                                <td>
                                                    <div class="input-group"><div class="form-control border-n text-right">1</div><span class="input-group-addon border-n p-x-2">건 이상</span></div>
                                                </td>
                                                <td>
                                                    <div class="input-group"><input type="text" class="form-control text-right" id="rcpcntfee" maxlength="6"><span class="input-group-addon border-n p-x-2">원</span></div>
                                                </td>
                                                <td>
                                                    <div class="input-group"><input type="text" class="form-control text-right" id="rcpbnkfee" maxlength="6"><span class="input-group-addon border-n p-x-2">원</span></div>
                                                </td>
                                                <td>
                                                    <div class="input-group"><div class="form-control border-n text-right" id="rcpfingerfee"></div><span class="input-group-addon border-n p-x-2">원</span></div>
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
                                        <table class="table table-bordered white-bg">
                                            <tbody>
                                            <tr>
                                                <th width="150">핑거메모</th>
                                                <td colspan="3">
                                                    <input type="text" class="form-control input-sm" value="" maxlength=500 id="daMngMemo">
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <div class="m-t-md text-center">
                                <a class="btn btn-primary" onclick="registCha()">등록</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 어드민용 스피너 추가 -->
<div class="spinner-area" style="display:none;">
    <div class="sk-spinner sk-spinner-cube-grid">
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
    </div>
    <div class="modal-backdrop-back-spinner"></div>
</div>
<!-- 어드민용 스피너 추가 -->
<!-- ##################################################################### -->


<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false" />

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<script>
    $(document).ready(function() {
        $(".rcpReceipt, .mandRcp").click(function () {
            receiptYnCheck();
        });

        $("input:radio[name='enableAdjustmentAccount']").click(function () {
            adjustmentAccountCheck();
        });

        $("input[name=rcpReqSveTy]").click(function () {
            rcpReqSveTyCheck();
        });

        // 기관분류 선택 시 기관의 API uri 등록 활성화 또는 비활성화
        $("input:radio[name='RdoChatrty']").click(function () {
            rdoChatrtyCheck();
        });

        // 가상계좌 수납방식 설정 - 납부금액 & 납부기간 설정 값 통일
        $("input:radio[class='validateAmountAndPeriod']").click(function () {
            if ($(this).val() === 'Y') {    // 수납방식 : 승인 / 부분납
                $("#enable-validate-amount-true").prop("checked", true);
                $("#enable-validate-period-true").prop("checked", true);
            } else {                        // 수납방식 : 통지
                $("#enable-validate-amount-false").prop("checked", true);
                $("#enable-validate-period-false").prop("checked", true);
                $("#enable-partial-payment-false").prop("checked", true);
            }
        });

        // 관리 정액 범위, 건당 범위
        $("#notlimit").keyup(function() {
            $("#notlimitCopy").text($("#notlimit").val());
        });

        // 관리 정액 수수료
        $("#flatfingerfee").keyup(function() {
            $("#flatfingerfee").val(numberToCommas($("#flatfingerfee").val()));
            $("#flatfee").text($("#flatfingerfee").val());
        });

        // 관리 건당 수수료
        $("#notfingerfee").keyup(function() {
            $("#notfingerfee").val(numberToCommas($("#notfingerfee").val()));
            $("#notcntfee").text($("#notfingerfee").val());
        });

        // 수납 핑거 수수료
        $("#rcpcntfee, #rcpbnkfee").focusout(function() {
            const rcpfingerfee = $("#rcpcntfee").val() - $("#rcpbnkfee").val();
            $("#rcpfingerfee").text(rcpfingerfee);
        });

        // 숫자만 입력
        $("#chaoffno, #ownertel1, #ownertel2, #ownertel3, #chrtelno1, #chrtelno2, #chrtelno3" +
            ", #feeaccno, #accNum1, #accNum2, #accNum3").keydown(function() {
            onlyNumber(this);
        });

        fn_init();
    });

    // 기관분류 선택 시 기관의 API uri 등록 활성화 또는 비활성화
    function rdoChatrtyCheck() {
        if ($("#RdoChatrty3").prop("checked")) {
            $("#rcpNoticeUrl").attr("readonly", false);
            $("#chaApiUrlQuery").attr("readonly", true);
            $("#chaApiUrlNotice").attr("readonly", true);
        } else if ($("#RdoChatrty4").prop("checked")) {
            $("#rcpNoticeUrl").attr("readonly", true);
            $("#chaApiUrlQuery").attr("readonly", false);
            $("#chaApiUrlNotice").attr("readonly", false);
        } else {
            $("#rcpNoticeUrl").attr("readonly", true);
            $("#chaApiUrlQuery").attr("readonly", true);
            $("#chaApiUrlNotice").attr("readonly", true);
        }
    }

    // 현금영수증 발행여부, 의무발행업체여부
    function receiptYnCheck() {
        if ($("#mand-rcp-true").prop("checked")) {
            $("input[name=rcpReceipt]").prop("disabled", true).val(["Y"]);
            $("input[name=rcpReceiptTy]").prop("disabled", true).val(["A"]);
            $("input[name=noTaxYn]").prop("disabled", false);
            $("input[name=rcpReqSveTy]").prop("disabled",true).val(["01"]);
            $("input[name=mnlRcpReqTy]").prop("disabled", true).val(["A"]);
        } else if ($("#mand-rcp-false").prop("checked") && $("#enable-rcpReceipt-false").prop("checked")){
            $("input[name=rcpReceiptTy]").prop("disabled", true).val(["M"]);
            $("input[name=noTaxYn]").prop("disabled", true).val(["Y"]);
            $("input[name=rcpReqSveTy]").prop("disabled",true).val(["00"]);
            $("input[name=mnlRcpReqTy]").prop("disabled", true).val(["M"]);
        } else {
            $("input[name=rcpReceipt]").prop("disabled", false);
            $("input[name=rcpReceiptTy]").prop("disabled", false);
            $("input[name=mnlRcpReqTy]").prop("disabled", false);
            $("input[name=noTaxYn]").prop("disabled", false);
            $("input[name=rcpReqSveTy]").prop("disabled",false);
        }
        rcpReqSveTyCheck();
    }

    // 다계좌 미사용시 다계좌정보 disabled
    function adjustmentAccountCheck() {
        if ($("#enable-adjustment-account-true").prop("checked")) {
            $(".accInfoTr > td").find("*").prop("disabled", false);
            $(".accInfoTr > td").css({"opacity": ""});
        } else {
            $(".accInfoTr > td").find("*").prop("disabled", true);
            $(".accInfoTr > td").css({"opacity": "0.65"});
            $(".accInfoTr").find("input").val("");
            $(".accInfoTr").find("select").val("001");
        }
    }

    // 수기수납 자동발행 disabled
    function rcpReqSveTyCheck() {
        if ($("#enable-rcpReqSveTy-false").prop("checked")) {
            $("input[name=mnlRcpReqTy]").prop("disabled", true).val(["M"]);
        } else {
            $("input[name=mnlRcpReqTy]").prop("disabled", false);
        }
    }

    function fn_init() {
        $("#enable-adjustment-account-false").prop("checked", true);    // 다계좌 사용여부
        $("#cusNameTyU").prop("checked", true);                         // 수취인명 (고객명)
        $("#enable-rcpReceipt-false").prop("checked", true);            // 현금영수증 발행여부
        $("#mand-rcp-false").prop("checked", true);                     // 의무발행업체 여부
        receiptYnCheck();
        adjustmentAccountCheck();

        // 수수료정보
        $("#notlimit").val("100").keyup();
        $("#flatfingerfee").val("15000").keyup();
        $("#notfingerfee").val("150").keyup();
        $("#rcpcntfee").val("150");
        $("#rcpbnkfee").val("120").focusout();

        fn_bankList();
    }

    // 출금은행목록
    function fn_bankList() {
        var url = "/sys/chaMgmt/getBankList";
        var param = {
            showYn : 'Y'
        };

        $.ajax({
            type: "post",
            url: url,
            async: true,
            data: JSON.stringify(param),
            contentType: "application/json; charset=utf-8",
            success: function (result) {
                if(result.retCode === '0000') {
                    var str = '';
                    // str += '<option value="">선택</option>';
                    $.each( result.bankList , function(i, v){
                        str += '<option value="' + v.code + '">' + v.value + '</option>';
                    });
                    $("#feebankcode").html(str);
                    $(".bankCd").html(str);
                }
            }
        })

    }

    // 등록
    function registCha() {
        var url = "/sys/chaMgmt/registChaAjax";
        var param = {};

        //수정시 필드 검증 함수 추가
        if (!fnValidationMod()) {
            return;
        }

        var ownertel = "";
        if ($("#ownertel1").val().length > 3 && $("#ownertel3").val() == "") {
            ownertel = $("#ownertel1").val() + "-" + $("#ownertel2").val();
        } else {
            ownertel = $("#ownertel1").val() + "-" + $("#ownertel2").val() + "-" + $("#ownertel3").val();
        }

        //다계좌정보
        var accountList = [];
        $("#adj-table > tbody").find("tr").each(function () {
            if($(this).find(".grpadjname").val() == "") return true;

            let obj = {adjfiregKey:"", grpAdjName:"", bankCd: "", realAccno:""}
            obj.adjfiregKey = $(this).find(".adjfiregkey").text();
            obj.grpAdjName = $(this).find(".grpadjname").val();
            obj.bankCd = $(this).find(".bankCd").val();
            obj.realAccno = $(this).find(".accno").val();
            accountList.push(obj);

            if($("#enable-adjustment-account-false").is(":checked"))
                return false;
        });

        param = {
            fgCd: $(":radio[name=bankId]:checked").val()
            , chatrty: $(":radio[name=RdoChatrty]:checked").val()
            , chaName: nullValueChange($("#chaname").val())
            , chaOffNo: nullValueChange($("#chaoffno").val())
            , ownerTel: nullValueChange(ownertel)
            , owner: nullValueChange($("#owner").val())
            , usePurpose:nullValueChange($("#usePurpose").val())
            , chast: $(':radio[name="RdoChast"]:checked').val()
            , chaStatus: $("#chaStatus").val() // 업종chaStatus
            , chaType: $("#chaType").val()	  // 업태
            , chrName: nullValueChange($("#chrname").val())
            , chrTelNo: $("#chrtelno1").val() + "-" + $("#chrtelno2").val() + "-" + $("#chrtelno3").val()
            , chaZipCode: $("#chazipcode").val()
            , chaAddress1: $("#chaaddress1").val()
            , chaAddress2: $("#chaaddress2").val()
            , chrMail: $("#chrmail1").val() + "@" + $("#chrmail2").val()
            , fingerFeeAccountNo: nullValueChange($("#feeaccno").val())
            , fingerFeeBankCode: ($("#feeaccno").val() == "") ? "" : $("#feebankcode").val()
            , fingerFeePayty: $("#feepayty").is(":checked") ? "PRE" : "CUR"
            , adjaccyn: $("input[name=enableAdjustmentAccount]:checked").val()
            , amtChkTy: $("input[name=enableValidateAmount]:checked").val()
            , partialPayment: $("input[name=enablePartialPayment]:checked").val()
            , rcpDueChk: $("input[name=enableValidatePeriod]:checked").val()
            , cusNameTy: $("input[name=cusNameTy]:checked").val()
            , rcpReqYn: $("input[name=rcpReceipt]:checked").val()
            , mandRcpYn: $("input[name=mandRcp]:checked").val()
            , rcpReqTy: $("input[name=rcpReceiptTy]:checked").val()
            , noTaxYn: $("input[name=noTaxYn]:checked").val()
            , rcpReqSveTy: $("input[name=rcpReqSveTy]:checked").val()
            , mnlRcpReqTy: $("input[name=mnlRcpReqTy]:checked").val()
            , accountList: accountList
            , notMinLimit: $("#notlimit").val()
            , notMinFee: $("#flatfingerfee").val().replace(/\,/gi, "")
            , notCntFee: $("#notfingerfee").val().replace(/\,/gi, "")
            , rcpCntFee: $("#rcpcntfee").val().replace(/\,/gi, "")
            , rcpBnkFee: $("#rcpbnkfee").val().replace(/\,/gi, "")
            , daMngMemo: $("#daMngMemo").val()
            , rcpNoticeUrl: $("#rcpNoticeUrl").val()
            , chaApiUrlQuery: $("#chaApiUrlQuery").val()
            , chaApiUrlNotice: $("#chaApiUrlNotice").val()
            // , jobType: nullValueChange("U")
        };

        swal({
            type: 'question',
            html: "등록 하시겠습니까?",
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
                        text: '기관정보 등록이 완료되었습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        location.href = "/sys/chaMgmt/chaList";
                    });
                }
            });
        });
    }

    function fnValidationMod() {
        $("input:text").each(function () {
            $(this).val($(this).val().trim());
        });

        if ($('#chaname').val() == "") {
            swal({
                type: 'info',
                text: '기관명을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#chaname').focus();
            });
            return;
        }

        if ($('#chaoffno').val() == "" || $('#chaoffno').val().length < 10) {
            swal({
                type: 'info',
                text: '사업자번호를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#chaoffno').focus();
            });
            return;
        }

        if ($('#ownertel1').attr('readonly') != 'readonly') {
            if ($('#ownertel1').val() == "" || $('#ownertel2').val() == "" || $('#ownertel3').val() == "") {
                if ($('#ownertel1').val().length < 4 && $('#ownertel3').val() == "") {
                    swal({
                        type: 'info',
                        text: '대표 전화번호를 입력해 주세요.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $('#ownertel1').focus();
                    });
                    return;
                }
            }
        }

        if ($('#owner').val() == "") {
            swal({
                type: 'info',
                text: '대표자명을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#owner').focus();
            });
            return;
        }

        if ($('#chrname').val() == "") {
            swal({
                type: 'info',
                text: '담당자명을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#chrname').focus();
            });
            return;
        }

        if ($('#chrtelno1').val() == "" || $('#chrtelno2').val() == "" || $('#chrtelno3').val() == "") {
            swal({
                type: 'info',
                text: '담당자 전화번호를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#chrtelno1').focus();
            });
            return;
        }

        if ($('#chazipcode').val() == "" || $('#chaaddress1').val() == "" || $('#chaaddress2').val() == "") {
            swal({
                type: 'info',
                text: '주소를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if ($('#chrmail1').val() == "" || $('#chrmail2').val() == "") {
            swal({
                type: 'info',
                text: '이메일을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#chrmail1').focus();
            });
            return;
        }

        if ($('#feeaccno').val() == "") {
            swal({
                type: 'info',
                text: '출금 계좌번호를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#feeaccno').focus();
            });
            return;
        }

        if ($('#accName1').val() == "" || $('#accNum1').val() == "") {
            swal({
                type: 'info',
                text: '다계좌 정보를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#accInfo1').focus();
            });
            return;
        }

        if (($('#accName2').val() == "" && $('#accNum2').val() !== "") || ($('#accName2').val() !== "" && $('#accNum2').val() == "")){
            swal({
                type: 'info',
                text: '다계좌 정보2를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#acc2').focus();
            });
            return;
        }

        if (($('#accName3').val() == "" && $('#accNum3').val() !== "") || ($('#accName3').val() !== "" && $('#accNum3').val() == "")){
            swal({
                type: 'info',
                text: '다계좌 정보3를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#acc3').focus();
            });
            return;
        }

        if ($("#notlimit").val() == "") {
            swal({
                type: 'info',
                text: '관리수수료 범위를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#notminlimit').focus();
            });
            return;
        }

        if ($("#flatfingerfee").val() == "" || $("#notfingerfee").val() == "") {
            swal({
                type: 'info',
                text: '관리수수료를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#notminfingerfee').focus();
            });
            return;
        }

        if ($("#rcpcntfee").val() == "" || $("#rcpbnkfee").val() == "") {
            swal({
                type: 'info',
                text: '수납수수료를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $('#notminfingerfee').focus();
            });
            return;
        }

        return true;
    }

    function openZipSearch() {
        new daum.Postcode({

            oncomplete: function (data) {
                $('[id=chazipcode]').val(data.zonecode); // 우편번호 (5자리)
                $('[id=chaaddress1]').val(data.address);
                $('[id=chaaddress2]').val(data.buildingName);
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

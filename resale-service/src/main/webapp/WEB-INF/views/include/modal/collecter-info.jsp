<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript">
    var MODE = ""; //등록 작업시 "I" , 수정작업시  "M" , 상세조회시 : "Q"
    var SAVE_MSG = "";
</script>

<%-- <script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script> --%>
<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>

<div class="modal fade" id="collecter-info" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modaltitle">기관정보상세보기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row no-gutters mt-3">
                        <div class="col-12">
                            <h5>기관기본정보</h5>
                        </div>
                    </div>
                </div>

                <form id="imform">
                    <div class="container-fluid" id="table_for_Q">
                        <table class="table table-form" >
                            <tbody class="container-fluid">
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">기관명(업체명)</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="tDchaname"></span>
                                </td>

                                <th class="col-md-2 col-sm-4 col-4">사업자등록번호</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <span id="tDchaoffno"></span>
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">기관코드</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="tDchacd"></span>
                                </td>

                                <th class="col-md-2 col-sm-4 col-4">기관상태</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="tDchast"></span>
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">업태</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="tDchatype"></span>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">업종</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="tDchastatus"></span>
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">대표자명</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="tDowner"></span>
                                </td>

                                <th class="col-md-2 col-sm-4 col-4">대표 전화번호</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <span id="tDownertel"></span>
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">기관(업체)주소</th>
                                <td class="col-md-10 col-sm-8 col-8">
                                    <span id="tDchazipcode"></span>
                                    <span id="tDchaaddress1"></span>
                                    <span id="tDchaaddress2"></span>
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">담당자명</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="tDchrname"></span>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">담당자 이메일</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="tDchrmail"></span>
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">담당자 전화번호</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <span id="tDchrtelno"></span>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">담당자 핸드폰번호</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <span id="tDchrhp"></span>
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">수수료 계좌번호</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="tDfeeaccno"></span>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">다계좌 사용여부</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <span id="tDadjaccyn"></span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="container-fluid" id="table_for_U">
                        <table class="table table-form" >
                            <tbody class="container-fluid">
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">기관명(업체명)</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control" id="mDchaname" maxlength="50" >
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">사업자등록번호</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <span id="mDchaoffno"></span>
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">기관코드</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="mDchacd"></span>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">기관상태</th>
                                <td class="col-md-4 col-sm-8 col-8" id="tdChaStY">
                                    <span id="tDchastU"></span>
                                </td>
                                <td class="col-md-4 col-sm-8 col-8" id="tdChaStN">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="chaStST05" value="ST05" name="mDchaSt">
                                        <label for="chaStST05"><span class="mr-2"></span>승인대기</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="chaStST03" value="ST03" name="mDchaSt">
                                        <label for="chaStST03"><span class="mr-2"></span>승인거부</label>
                                    </div>
                                </td>
                                <input type="hidden" id="hChaSt" name="hChaSt">
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">업태</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control" id="mDchatype" maxlength="50" >
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">업종</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control" id="mDchastatus" maxlength="50" >
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">대표자명</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control" id="mDowner" maxlength="50" >
                                </td>

                                <th class="col-md-2 col-sm-4 col-4">대표 전화번호</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <div class="contact-number form-inline w-100">
                                        <input type="text" class="form-control" maxlength="3" id="mDownertel1">
                                        <span class="hypen"> - </span>
                                        <input type="text" class="form-control" maxlength="4" id="mDownertel2">
                                        <span class="hypen"> - </span>
                                        <input type="text" class="form-control" maxlength="4" id="mDownertel3">
                                    </div>
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">기관(업체)주소</th>
                                <td class="col-md-10 col-sm-8 col-8 address-form-in-table">
                                    <div class="zipcode">
                                        <input type="text" class="form-control zipcode-input mr-1"  readonly  id="mDchazipcode">
                                        <button type="button"  class="btn btn-sm btn-d-gray" onclick="openZipSearch();">우편번호검색</button>
                                    </div>
                                    <div class="address-lines w-100">
                                        <input type="text" class="form-control first-line mt-1" id="mDchaaddress1"  maxlength="100" >
                                        <input type="text" class="form-control second-line mt-1" id="mDchaaddress2" maxlength="100" >
                                    </div>
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">담당자명</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control" id="mDchrname" maxlength="50" >
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">담당자 이메일</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control email-name col-3" id="mDcusMail1">
                                    <span class="ml-1 mr-1">@</span>
                                    <input type="text" class="form-control mr-1 email-provider col-4" id="mDcusMail2">
                                    <select class="form-control provider-preset col-3" id="mDcusMail3" onclick="fn_mailDomainChg($(this).val());">
                                        <option value="">직접입력</option>
                                        <option value="naver.com">naver.com</option>
                                        <option value="nate.com">nate.com</option>
                                        <option value="yahoo.com">yahoo.com</option>
                                        <option value="empal.com">empal.com</option>
                                        <option value="gmail.com">gmail.com</option>
                                        <option value="hanmail.net">hanmail.net</option>
                                        <option value="daum.net">daum.net</option>
                                    </select>
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">담당자 전화번호</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <div class="contact-number form-inline w-100">
                                        <input type="text" class="form-control" maxlength="3" id="mDchrtelno1">
                                        <span class="hypen"> - </span>
                                        <input type="text" class="form-control" maxlength="4" id="mDchrtelno2">
                                        <span class="hypen"> - </span>
                                        <input type="text" class="form-control" maxlength="4" id="mDchrtelno3">
                                    </div>
                                </td>

                                <th class="col-md-2 col-sm-4 col-4">담당자 핸드폰번호</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <div class="contact-number form-inline w-100">
                                        <input type="text" class="form-control" maxlength="3" id="mDchrHp1">
                                        <span class="hypen"> - </span>
                                        <input type="text" class="form-control" maxlength="4" id="mDchrHp2">
                                        <span class="hypen"> - </span>
                                        <input type="text" class="form-control" maxlength="4" id="mDchrHp3">
                                    </div>
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">수수료 계좌번호</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="mDfeeaccno"></span>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">다계좌 사용여부</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <span id="mDadjaccyn"></span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="container-fluid">
                        <div class="row no-gutters mt-4">
                            <div class="col-12">
                                <h5>다계좌정보</h5>
                            </div>
                        </div>
                    </div>
                    <div class="container-fluid">
                        <div class="row no-gutters">
                            <div class="col col-md-6 col-sm-12">
                                <table class="table table-primary">
                                    <colgroup>
                                        <col width="65">
                                        <col width="120">
                                        <col width="230">
                                    </colgroup>

                                    <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>계좌 순번</th>
                                        <th>계좌명</th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <tr>
                                        <td>1</td>
                                        <td><span id="tDadjfiregkey1">-</span></td>
                                        <td><span id="tDgrpadjname1">-</span></td>
                                    </tr>
                                    <tr>
                                        <td>2</td>
                                        <td><span id="tDadjfiregkey2">-</span></td>
                                        <td><span id="tDgrpadjname2">-</span></td>
                                    </tr>
                                    <tr>
                                        <td>3</td>
                                        <td><span id="tDadjfiregkey3">-</span></td>
                                        <td><span id="tDgrpadjname3">-</span></td>
                                    </tr>
                                    <tr>
                                        <td>4</td>
                                        <td><span id="tDadjfiregkey4">-</span></td>
                                        <td><span id="tDgrpadjname4">-</span></td>
                                    </tr>
                                    <tr>
                                        <td>5</td>
                                        <td><span id="tDadjfiregkey5">-</span></td>
                                        <td><span id="tDgrpadjname5">-</span></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="col col-md-6 col-sm-12">
                                <table class="table table-primary">
                                    <colgroup>
                                        <col width="65">
                                        <col width="120">
                                        <col width="230">
                                    </colgroup>

                                    <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>계좌 순번</th>
                                        <th>계좌명</th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <tr>
                                        <td>6</td>
                                        <td><span id="tDadjfiregkey6">-</span></td>
                                        <td><span id="tDgrpadjname6">-</span></td>
                                    </tr>
                                    <tr>
                                        <td>7</td>
                                        <td><span id="tDadjfiregkey7">-</span></td>
                                        <td><span id="tDgrpadjname7">-</span></td>
                                    </tr>
                                    <tr>
                                        <td>8</td>
                                        <td><span id="tDadjfiregkey8">-</span></td>
                                        <td><span id="tDgrpadjname8">-</span></td>
                                    </tr>
                                    <tr>
                                        <td>9</td>
                                        <td><span id="tDadjfiregkey9">-</span></td>
                                        <td><span id="tDgrpadjname9">-</span></td>
                                    </tr>
                                    <tr>
                                        <td>10</td>
                                        <td><span id="tDadjfiregkey10">-</span></td>
                                        <td><span id="tDgrpadjname10">-</span></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid">
                        <div class="row no-gutters mt-4">
                            <div class="col-12">
                                <h5>이용요금 정보</h5>
                            </div>
                        </div>
                    </div>
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col">
                                <table class="table table-primary">
                                    <colgroup>
                                        <col width="95">
                                        <col width="75">
                                        <col width="160">
                                        <col width="160">
                                        <col width="160">
                                        <col width="160">
                                    </colgroup>

                                    <thead>
                                    <tr>
                                        <th colspan="2">구분</th>
                                        <th>범위</th>
                                        <th>총 금액</th>
                                        <th>은행</th>
                                        <th>핑거</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td rowspan="2">관리수수료</td>
                                        <td>정액</td>
                                        <td class="text-right"><span id="tDnotminlimit"></span>
                                            건 이하</td>
                                        <td class="text-right"> <span id="tDnotminfee"></span>
                                            원</td>
                                        <td class="text-right">0
                                            원</td>
                                        <td class="text-right"><span id="tDnotminfingerfee"></span>
                                            원</td>
                                    </tr>
                                    <tr>
                                        <td>건당</td>
                                        <td class="text-right"><span id="tDnotminlimit1"></span>
                                            건 초과</td>
                                        <td class="text-right"><span id="tDnotcntfee"></span>
                                            원</td>
                                        <td class="text-right">0
                                            원</td>
                                        <td class="text-right"><span id="tDnotcntfingerfee"></span>
                                            원</td>
                                    </tr>
                                    <tr>
                                        <td>입금수수료</td>
                                        <td>건당</td>
                                        <td class="text-right">1건 이상</td>
                                        <td class="form-td-inline text-right"><span id="tDrcpcntfee"></span>
                                            <input type="text" class="form-control text-right" id="mDrcpcntfee" maxlength="10" onkeydown="fn_feeAmtCal();" onkeyup="fn_feeAmtCal();">
                                            <span class="ml-1">원</span>
                                        </td>
                                        <td class="form-td-inline text-right"><span id="tDrcpbnkfee"></span>
                                            <input type="text" class="form-control text-right" id="mDrcpbnkfee" maxlength="10" onkeydown="fn_feeAmtCal();" onkeyup="fn_feeAmtCal();">
                                            <span class="ml-1">원</span>
                                        </td>
                                        <td class="form-td-inline text-right"><span id="tDrcpfingerfee"></span>
                                            <span id="mDrcpfingerfee"></span>
                                            <span class="ml-1">원</span>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid">
                        <div class="row no-gutters mt-4">
                            <div class="col-12">
                                <h5>지점정보</h5>
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid">
                        <table class="table table-form">
                            <tbody class="container-fluid">
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">지점이름</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="tDagtname"></span>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">지점코드</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="tDagtcd"></span>
                                </td>
                            </tr>
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">메모</th>
                                <td class="col-md-10 col-sm-8 col-8" colspan="3" style="display: block;">
                                    <span id="tDremark" class="table-title-ellipsis"></span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary save-cha-info" onclick="saveChaIfo();">저장</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    function fnValidationMod() {

        if (!$('#mDchaname').val()) {
            swal({
                type: 'info',
                text: "기관명을 입력해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function() {
                $('#mDchaname').focus();
            });
            return;
        }

        if (!$('#mDowner').val()) {
            swal({
                type: 'info',
                text: "대표명을 입력해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function() {
                $('#mDowner').focus();
            });
            return;
        }
        if (!$('#mDownertel1').val() || !$('#mDownertel2').val() || !$('#mDownertel3').val()) {
            swal({
                type: 'info',
                text: "대표전화번호를 입력해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if (!$('#mDchrname').val()) {
            swal({
                type: 'info',
                text: "담당자를 입력해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function() {
                $('#mDchrname').focus();
            });
            return;
        }
        if (!$('#mDchrtelno1').val() || !$('#mDchrtelno2').val() || !$('#mDchrtelno3').val()) {
            swal({
                type: 'info',
                text: "담당자 번화번호를 입력해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function() {
                $('#mDchrtelno').focus();
            });
            return;
        }

        if (!$('#mDchazipcode').val() || !$('#mDchaaddress1').val() || !$('#mDchaaddress1').val()) {
            swal({
                type: 'info',
                text: "주소를 입력해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        return true;
    }

    //저장(insert)
    function saveChaIfo() {

        //수정시 필드 검증 함수 추가
        if (!fnValidationMod()) {
            return;
        }

        var url = "/bank/updateChaInfo";
        var param = {
            chaName     : $("#mDchaname").val(),
            owner       : $("#mDowner").val(),
            ownerTel    : $('#mDownertel1').val() + '-' + $('#mDownertel2').val() + '-' + $('#mDownertel3').val(),
            chaCd       : $("#mDchacd").text(),
            chrName     : $("#mDchrname").val(),
            flag		: $("#hChaSt").val(),
            chast		: $("input[name='mDchaSt']:checked").val(),
            chrTelNo    : $('#mDchrtelno1').val() + '-' + $('#mDchrtelno2').val() + '-' + $('#mDchrtelno3').val(),
            chrHp		: $("#mDchrHp1").val() + '-' + $("#mDchrHp2").val() + '-' + $("#mDchrHp3").val(),
            chrMail		: $("#mDcusMail1").val() + "@" + $("#mDcusMail2").val(),
            chaStatus	: $("#mDchastatus").val(),	// 업종
            chaType		: $("#mDchatype").val(),	// 업태
            chaZipCode  : $("#mDchazipcode").val(),
            chaAddress1 : $("#mDchaaddress1").val(),
            chaAddress2 : $("#mDchaaddress2").val(),
            // 수수료정보
            rcpCntFee   : $("#mDrcpcntfee").val(), // 수납건당총금액
            rcpBnkFee   : $("#mDrcpbnkfee").val(), // 은행수납금액
            jobType     :  "UB"
        };
        swal({
            type: 'question',
            html: "기관정보를 수정 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function(result) {
            if (result.value) {
                // 확인
                $.ajax({
                    type: "post",
                    url: url,
                    async : true,
                    data: JSON.stringify(param),
                    contentType : "application/json; charset=utf-8",
                    success: function(result){
                        if (result.retCode == "0000") {
                            swal({
                                type: 'success',
                                text: '기관정보가 수정되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function(result) {
                                if (result.value) {
                                    $("#collecter-info").modal("hide");
                                    $("#row-th").prop("checked",false);
                                    pageChange();
                                }
                            });
                        } else {
                            swal({
                                type: 'warning',
                                text: result.retMsg,
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        }
                    }
                });
            }
        });
    }

    function collector_info_init(mod, thisId) {
        for (var i = 0; i <= 9; i++) {
            var j = i + 1;
            $("#tDadjfiregkey" + j).text('');
            $("#tDgrpadjname" + j).text('');
        }

        MODE = mod;

        if (mod=='Q'){
            SAVE_MSG = "";//'고객정보수정이 완료되었습니다.';

            $("#table_for_Q").show();
            $("#table_for_U").hide();
            $("#tDrcpcntfee").show();
            $("#tDrcpbnkfee").show();
            $("#tDrcpfingerfee").show();
            $("#mDrcpcntfee").hide();
            $("#mDrcpbnkfee").hide();
            $("#mDrcpfingerfee").hide();
            $(".save-cha-info").hide();

            $("#modaltitle").text("기관정보상세보기");

            var _chacd = thisId;
            var url = "/bank/selectChaListInfoAjax";
            var param = {
                chaCd : _chacd
            };
            $.ajax({
                type: "post",
                url: url,
                async : true,
                data: JSON.stringify(param),
                contentType : "application/json; charset=utf-8",
                success: function(data){
                    // 기관기본정보
                    var map = data.info;
                    $("#tDchaname"        ).text(map.chaName);
                    $("#tDowner"          ).text(map.owner);
                    $("#tDownertel"       ).text(map.ownerTel);
                    $("#tDchacd"          ).text(map.chaCd);
                    $("#tDchaoffno"       ).text(map.chaOffNo);
                    $("#tDchrname"        ).text(map.chrName);
                    $("#tDchrtelno"       ).text(map.chrTelNo);
                    $("#tDchast"          ).text(getChastName(map.chast));
                    if(map.chrHp.indexOf("-") > 0) {
                        $("#tDchrhp").text(map.chrHp);
                    } else {
                        var  chrhp = map.chrHp.substr(0, 3) + "-" + map.chrHp.substr(3, 4) + "-" + map.chrHp.substr(7, 4);
                        $("#tDchrhp").text(chrhp);
                    }
                    $("#tDchrmail").text(map.chrMail);
                    $("#tDchastatus").text(map.chaStatus);
                    $("#tDchatype").text(map.chaType);
                    $("#tDchazipcode"     ).text("("+map.chaZipCode+")");
                    $("#tDchaaddress1"    ).text(map.chaAddress1);
                    $("#tDchaaddress2"    ).text(map.chaAddress2);
                    $("#tDfeeaccno"       ).text(map.feeAccNo);
                    $("#tDadjaccyn"       ).text(map.adjaccyn);
                    if(map.adjaccyn == "Y") {
                        $("#tDadjaccyn"       ).text("사용");
                    } else  {
                        $("#tDadjaccyn"       ).text("미사용"       );
                    }
                    // 다계좌정보
                    var j = 0;
                    $.each(data.agencyList, function(i, v){
                        j= i + 1;
                        $("#tDadjfiregkey"+j).text(v.adjfiregKey);
                        $("#tDgrpadjname" +j).text(v.grpadjName);
                    });
                    // 수수료정보
                    $("#tDnotminlimit"    ).text(numberToCommas(map.notMinLimit    ));
                    $("#tDnotminfee"      ).text(numberToCommas(map.notMinFee      ));
                    $("#tDnotminfingerfee").text(numberToCommas(map.notMinFee      ));
                    $("#tDnotminlimit1"   ).text(numberToCommas(map.notMinLimit    ));
                    $("#tDnotcntfee"      ).text(numberToCommas(map.notCntFee      ));
                    $("#tDnotcntfingerfee").text(numberToCommas(map.notCntFee      ));
                    $("#tDrcpcntfee"      ).text(numberToCommas(map.rcpCntFee      ));
                    $("#tDrcpbnkfee"      ).text(numberToCommas(map.rcpBnkFee      ));
                    var rcpfingerfee = map.rcpCntFee - map.rcpBnkFee;
                    $("#tDrcpfingerfee"   ).text(numberToCommas(rcpfingerfee      ));
                    // 지점정보
                    $("#tDagtname"        ).text(map.agtName        );
                    $("#tDagtcd"          ).text(map.agtCd          );
                    $("#tDremark"         ).text(map.remark         );
                }
            });

        } else {  // MODE= "U";
            SAVE_MSG = '고객정보수정이 완료되었습니다.';

            $("#table_for_Q").hide();
            $("#table_for_U").show();

            $("#tDrcpcntfee").hide();
            $("#tDrcpbnkfee").hide();
            $("#tDrcpfingerfee").hide();
            $("#mDrcpcntfee").show();
            $("#mDrcpbnkfee").show();
            $("#mDrcpfingerfee").show();
            $(".save-cha-info").show();

            $("#modaltitle").text("기관정보수정");

            var _chacd = thisId;
            var url = "/bank/selectChaListInfoAjax";
            var param = {
                chaCd : _chacd
            };
            $.ajax({
                type: "post",
                url: url,
                async : true,
                data: JSON.stringify(param),
                contentType : "application/json; charset=utf-8",
                success: function(data){
                    var map = data.info;
                    // 기관기본정보
                    $("#mDchaname"        ).val(map.chaName        );
                    $("#mDowner"          ).val(map.owner          );
                    //대표전화번호
                    if(map.ownerTel  != null && map.ownerTel != '') {
                        var _ownertel = map.ownerTel;
                        _ownertel = _ownertel.split('-');
                        $("#mDownertel1").val(_ownertel[0]);
                        $("#mDownertel2").val(_ownertel[1]);
                        $("#mDownertel3").val(_ownertel[2]);
                    }
                    $("#mDchacd"          ).text(map.chaCd          );
                    $("#mDchaoffno"       ).text(map.chaOffNo       );
                    $("#mDchrname"        ).val(map.chrName        );
                    //대표전화번호
                    if(map.chrTelNo != null && map.chrTelNo != '') {
                        var chrtelno = map.chrTelNo;
                        _chrtelno = chrtelno.split('-');
                        $("#mDchrtelno1").val(_chrtelno[0]);
                        $("#mDchrtelno2").val(_chrtelno[1]);
                        $("#mDchrtelno3").val(_chrtelno[2]);
                    }
                    if(map.chrHp.indexOf('-') > 0) {
                        var chrhp = map.chrHp;
                        _chrhp = chrhp.split('-');
                        $("#mDchrHp1").val(_chrhp[0]);
                        $("#mDchrHp2").val(_chrhp[1]);
                        $("#mDchrHp3").val(_chrhp[2]);
                    } else {
                        $("#mDchrHp1").val(map.chrHp.substr(0, 3));
                        $("#mDchrHp2").val(map.chrHp.substr(3, 4));
                        $("#mDchrHp3").val(map.chrHp.substr(7, 4));
                    }
                    if(map.chrMail  != null && map.chrMail != '')  {
                        var _chrMail = map.chrMail;
                        _chrMail = _chrMail.split('@');
                        $("#mDcusMail1").val(_chrMail[0]);
                        $("#mDcusMail2").val(_chrMail[1]);
                        $("#mDcusMail3").val(_chrMail[1]).prop("selected", true);
                        if($("#mDcusMail3").val() == null || $("#mDcusMail3").val() == '') {
                            $("#mDcusMail3").val("");
                        }
                        if($("#mDcusMail3").val() != '') {
                            $("#mDcusMail2").prop("disabled", "disabled");
                        }
                    }
                    if(map.chast == "ST03") {
                        $("#tdChaStN").show();
                        $("#tdChaStY").hide();
                        $("#chaStST03").prop("checked", true);
                        $("#chaStST05").prop("checked", false);
                    } else {
                        $("#tdChaStN").hide();
                        $("#tdChaStY").show();
                        $("#tDchastU"         ).text(getChastName(map.chast));
                    }
                    $("#hChaSt").val(map.chast);
                    //$("#tDchastU"         ).text(getChastName(map.chast));
                    $("#mDchastatus").val(map.chaStatus);
                    $("#mDchatype").val(map.chaType);
                    $("#mDchazipcode"     ).val(map.chaZipCode     );
                    $("#mDchaaddress1"    ).val(map.chaAddress1    );
                    $("#mDchaaddress2"    ).val(map.chaAddress2    );
                    $("#mDfeeaccno"       ).text(map.feeAccNo       );
                    if(map.adjaccyn == "Y") {
                        $("#mDadjaccyn").text("사용");
                    } else {
                        $("#mDadjaccyn").text("미사용");
                    }
                    // 다계좌정보
                    var agencyList = data.agencyList;
                    var j = 0;
                    $.each(agencyList, function(i, v){
                        j= i + 1;
                        $("#tDadjfiregkey"+j     ).text(v.adjfiregKey    );
                        $("#tDgrpadjname" +j     ).text(v.grpadjName     );
                    });
                    // 수수료정보
                    $("#tDnotminlimit"    ).text(numberToCommas(map.notMinLimit    ));
                    $("#tDnotminlimit1"   ).text(numberToCommas(map.notMinLimit    ));
                    $("#tDnotminfee"      ).text(numberToCommas(map.notMinFee      ));
                    $("#tDnotcntfee"      ).text(numberToCommas(map.notCntFee      ));
                    $("#tDnotminfingerfee"      ).text(numberToCommas(map.notMinFee      ));
                    $("#tDnotcntfingerfee"      ).text(numberToCommas(map.notCntFee      ));

                    $("#mDrcpcntfee"      ).val(numberToCommas(map.rcpCntFee      ));
                    $("#mDrcpbnkfee"      ).val(numberToCommas(map.rcpBnkFee      ));
                    var rcpfingerfee = map.rcpCntFee - map.rcpBnkFee;
                    $("#mDrcpfingerfee"   ).text(numberToCommas(rcpfingerfee      ));
                    // 지점정보
                    $("#tDagtname"        ).text(map.agtName        );
                    $("#tDagtcd"          ).text(map.agtCd          );
                    $("#tDremark"         ).text(map.remark         );
                }
            });
        }  // end of MODE= "U";
    }

    function openZipSearch(){
        new daum.Postcode({
            oncomplete: function(data) {
                $('[id=mDchazipcode]').val(data.zonecode); // 우편번호 (5자리)
                $('[id=mDchaaddress1]').val(data.address);
                $('[id=mDchaaddress2]').val(data.buildingName);
            }
        }).open();
    }

    // 수수료 계산
    function fn_feeAmtCal() {
        $("#mDrcpfingerfee").text($("#mDrcpcntfee").val() - $("#mDrcpbnkfee").val());
    }

    //도메인 선택시 변경
    function fn_mailDomainChg(val) {
        if(val == '') { // 직접입력
            $('#mDcusMail2').val(val);
            $('#mDcusMail2').attr('disabled', false);
        } else {
            $('#mDcusMail2').val(val);
            $('#mDcusMail2').attr('disabled', true);
        }
    }

</script>

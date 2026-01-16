<%--
  Created by IntelliJ IDEA.
  User: jungbna
  Date: 2018-12-18
  Time: 오후 2:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>

<div class="modal fade" id="agencyDetailModify" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modaltitle">기관정보수정</h5>
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
                    <div class="container-fluid">
                        <table class="table table-form" >
                            <tbody class="container-fluid">
                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">등록일자</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="regDtInfo"></span>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">승인상태</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="tDchastU"></span>
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">기관코드</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="mDchacd"></span>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">사업자번호</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <span id="mDchaoffno"></span>
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">기관명(업체명)</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="mDchaname"></span>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">다계좌 여부</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <span id="mDadjaccyn"></span>
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">이용방식</th>
                                <td class="col-md-4 col-sm-8 col-8" id="amtChkTyInfo">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" name="amtChkTyItemInfo" id="amtChkTyItemInfo_IR" value="Y">
                                        <label class="form-check-label" for="amtChkTyItemInfo_IR"><span class="mr-2"></span>승인</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" name="amtChkTyItemInfo" id="amtChkTyItemInfo_SET" value="N">
                                        <label class="form-check-label" for="amtChkTyItemInfo_SET"><span class="mr-2"></span>통지</label>
                                    </div>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">발급요청좌수</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control mr-1" id="accNoCntInfo" maxlength="50"> 좌
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">대표자명</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <input type="text" class="form-control" id="mDowner" maxlength="50" >
                                </td>

                                <th class="col-md-2 col-sm-4 col-4">대표전화번호</th>
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

                            </tbody>
                        </table>
                    </div>

                    <div class="container-fluid adjaccInfo">
                        <div class="row no-gutters mt-4">
                            <div class="col-12">
                                <h5>다계좌정보</h5>
                            </div>
                        </div>
                    </div>
                    <div class="container-fluid adjaccInfo">
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
                                        <th>대리점 코드</th>
                                        <th>대리점명</th>
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
                                        <th>대리점 코드</th>
                                        <th>대리점명</th>
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
                                        <td class="text-right"><span id="tDnotminlimit"></span>건 이하</td>
                                        <td class="text-right"> <span id="tDnotminfee"></span>원</td>
                                        <td class="text-right">0원</td>
                                        <td class="text-right"><span id="tDnotminfingerfee"></span>원</td>
                                    </tr>
                                    <tr>
                                        <td>건당</td>
                                        <td class="text-right"><span id="tDnotminlimit1"></span>건 초과</td>
                                        <td class="text-right"><span id="tDnotcntfee"></span>원</td>
                                        <td class="text-right">0원</td>
                                        <td class="text-right"><span id="tDnotcntfingerfee"></span>원</td>
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
                        <div class="guide-mention w-100 text-danger">
                            기관정보 및 이용요금 수정 시 통합단말의 정보는 자동으로 수정되지 않기 때문에 반드시 통합단말에도 동일한 내용으로 수정 반영 해주시기 바랍니다. <br/>
                            미반영 시, 전문을 통해 자동으로 익일 이전 정보로 돌아갑니다.
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
                                    <input type="text" class="form-control" id="tDremark">
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
        if(!$('#agencyDetailModify #accNoCntInfo').val()){
            swal({
                type: 'info',
                text: '발급요청좌수를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function() {
                $('#agencyDetailModify #accNoCntInfo').focus();
            });
            return;
        }

        if(!$('#agencyDetailModify #mDowner').val()){
            swal({
                type: 'info',
                text: '대표자명을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function() {
                $('#agencyDetailModify #mDowner').focus();
            });
            return;
        }

        if(!$('#agencyDetailModify #mDownertel1').val() || !$('#agencyDetailModify #mDownertel2').val()|| !$('#agencyDetailModify #mDownertel3').val()){
            swal({
                type: 'info',
                text: '대표전화번호를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }

        if(!$('#agencyDetailModify #mDchrtelno1').val() | !$('#agencyDetailModify #mDchrtelno2').val() || !$('#agencyDetailModify #mDchrtelno3').val()){
            swal({
                type: 'info',
                text: '담당자 전화번호를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }

        if(!$('#agencyDetailModify #mDchrHp1').val() || !$('#agencyDetailModify #mDchrHp2').val() || !$('#agencyDetailModify #mDchrHp3').val()){
            swal({
                type: 'info',
                text: '담당자 핸드폰번호를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }

        var mDrcpcntfeeVal = $('#agencyDetailModify #mDrcpcntfee').val();
        var mDrcpbnkfeeVal = $('#agencyDetailModify #mDrcpbnkfee').val();
        if(parseInt(mDrcpcntfeeVal) < parseInt(mDrcpbnkfeeVal)){
            swal({
                type: 'info',
                text: '은행 입금수수료는 총 입금수수료보다 클 수 없습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }

        return true;
    }

    //저장(insert)
    function saveChaIfo() {
        if(!fnValidationMod()) {//저장시 필수 정보 체크
            return;
        }

        var url = "/bank/updateChaListInfoAjax";
        var param = {
            chaCd       : $('#agencyDetailModify #mDchacd').text(), //기관코드
            amtChkTy    : $('input[name=amtChkTyItemInfo]:checked').val(), //이용방식
            accNoCnt    : $('#agencyDetailModify #accNoCntInfo').val(), //발급요청좌수
            owner       : $('#agencyDetailModify #mDowner').val(), //대표자명
            ownerTel    : $('#agencyDetailModify #mDownertel1').val() + '-' + $('#agencyDetailModify #mDownertel2').val() + '-' + $('#agencyDetailModify #mDownertel3').val(), //대표전화번호
            chrTelNo    : $('#agencyDetailModify #mDchrtelno1').val() + '-' + $('#agencyDetailModify #mDchrtelno2').val() + '-' + $('#agencyDetailModify #mDchrtelno3').val(), //담당자 전화번호
            chrHp		: $('#agencyDetailModify #mDchrHp1').val() + '-' + $('#agencyDetailModify #mDchrHp2').val() + '-' + $('#agencyDetailModify #mDchrHp3').val(), //담당자 핸드폰번호
            remark      : $('#agencyDetailModify #tDremark').val(), //메모
            // 이용요금정보
            rcpCntFee   : $('#agencyDetailModify #mDrcpcntfee').val(), // 수납건당총금액
            rcpBnkFee   : $('#agencyDetailModify #mDrcpbnkfee').val(), // 은행수납금액
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
            if(!result.value) {
                return;
            }

            $.ajax({
                type: "post",
                url: url,
                data: JSON.stringify(param),
                contentType : "application/json; charset=utf-8",
                success: function(result){
                    if(result.retCode != "0000"){
                        swal({
                            type: 'warning',
                            text: result.retMsg,
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        });
                        return;
                    }

                    swal({
                        type: 'success',
                        text: '기관정보가 수정되었습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function(result) {
                        if (result.value) {
                            $('#agencyDetailModify').modal("hide");
                            pageChange();
                        }
                    });
                }
            });
        });
    }

    function agencyDetailModify(thisId) {
        for (var i = 0; i <= 9; i++) {
            var j = i + 1;
            $('#agencyDetailModify #tDadjfiregkey' + j).text('');
            $('#agencyDetailModify #tDgrpadjname' + j).text('');
        }

        var url = "/bank/selectChaListInfoAjax";
        var param = {
            chaCd : thisId
        };

        $.ajax({
            type: "post",
            url: url,
            async : true,
            data: JSON.stringify(param),
            contentType : "application/json; charset=utf-8",
            success: function(data){
                var map = data.info;
                var agencyList = data.agencyList;
                var j = 0;

                $('#agencyDetailModify #regDtInfo').text(moment(map.bnkRegiDt,'YYYYMMDD').format('YYYY.MM.DD')); //등록일자

                if(map.chast == "ST02"){ //승인상태
                    $('#agencyDetailModify #tDchastU').text("해지");
                }else if(map.chast == "ST07"){
                    $('#agencyDetailModify #tDchastU').text("승인거부");
                }else if(map.chast == "ST04"){
                    $('#agencyDetailModify #tDchastU').text("정지");
                }else if(map.chast == "ST05"){
                    $('#agencyDetailModify #tDchastU').text("승인대기");
                }

                $('#agencyDetailModify #mDchacd').text(map.chaCd); //기관코드

                if(map.chaOffNo != null && map.chaOffNo != ""){
                    var subChaOffNo = map.chaOffNo.substr(0, 3) + '-' + map.chaOffNo.substr(3, 2) + '-' + map.chaOffNo.substr(5, 5);
                    $('#agencyDetailModify #mDchaoffno').text(subChaOffNo); //사업자번호
                }

                $('#agencyDetailModify #mDchaname').text(map.chaName); //기관명(업체명)
                if(map.adjaccyn == "Y") { //다계좌 여부
                    $('#agencyDetailModify #mDadjaccyn').text("사용");
                    $('#agencyDetailModify .adjaccInfo').show();
                } else {
                    $('#agencyDetailModify #mDadjaccyn').text("미사용");
                    $('#agencyDetailModify .adjaccInfo').hide();
                }

                if(map.amtChkTy == "Y"){ //이용방식
                    $('#agencyDetailModify #amtChkTyItemInfo_IR').attr('checked', true);
                }else{
                    $('#agencyDetailModify #amtChkTyItemInfo_SET').attr('checked', true);
                }

                $("#agencyDetailModify #accNoCntInfo").val(map.accNoCnt); //발급요청좌수
                $("#agencyDetailModify #mDowner").val(map.owner); //대표자명

                if(map.ownerTel  != null && map.ownerTel != '') { //대표전화번호
                    var _ownertel = map.ownerTel;
                    _ownertel = _ownertel.split('-');
                    $("#agencyDetailModify #mDownertel1").val(_ownertel[0]);
                    $("#agencyDetailModify #mDownertel2").val(_ownertel[1]);
                    $("#agencyDetailModify #mDownertel3").val(_ownertel[2]);
                }

                if(map.chrTelNo != null && map.chrTelNo != '') { //담당자 전화번호
                    var chrtelno = map.chrTelNo;
                    _chrtelno = chrtelno.split('-');
                    $("#agencyDetailModify #mDchrtelno1").val(_chrtelno[0]);
                    $("#agencyDetailModify #mDchrtelno2").val(_chrtelno[1]);
                    $("#agencyDetailModify #mDchrtelno3").val(_chrtelno[2]);
                }

                if(map.chrHp != null && map.chrHp != '') { //담당자 핸드폰번호
                    var chrhp = map.chrHp;
                    _chrhp = chrtelno.split('-');
                    $("#agencyDetailModify #mDchrHp1").val(_chrhp[0]);
                    $("#agencyDetailModify #mDchrHp2").val(_chrhp[1]);
                    $("#agencyDetailModify #mDchrHp3").val(_chrhp[2]);
                }

                if(agencyList != null){
                    $.each(agencyList, function(i, v){
                        j = i + 1;
                        $("#agencyDetailModify #tDadjfiregkey"+j).text(v.adjfiregKey);
                        $("#agencyDetailModify #tDgrpadjname" +j).text(v.grpadjName);
                    });

                    // 수수료정보
                    $("#agencyDetailModify #tDnotminlimit").text(numberToCommas(map.notMinLimit));
                    $("#agencyDetailModify #tDnotminlimit1").text(numberToCommas(map.notMinLimit));
                    $("#agencyDetailModify #tDnotminfee").text(numberToCommas(map.notMinFee));
                    $("#agencyDetailModify #tDnotcntfee").text(numberToCommas(map.notCntFee));
                    $("#agencyDetailModify #tDnotminfingerfee").text(numberToCommas(map.notMinFee));
                    $("#agencyDetailModify #tDnotcntfingerfee").text(numberToCommas(map.notCntFee));

                    $("#agencyDetailModify #mDrcpcntfee").val(numberToCommas(map.rcpCntFee));
                    $("#agencyDetailModify #mDrcpbnkfee").val(numberToCommas(map.rcpBnkFee));

                    var rcpfingerfee = map.rcpCntFee - map.rcpBnkFee;
                    $("#agencyDetailModify #mDrcpfingerfee").text(numberToCommas(rcpfingerfee));
                }else {
                }

                // 지점정보
                $("#agencyDetailModify #tDagtname").text(map.agtName);
                $("#agencyDetailModify #tDagtcd").text(map.agtCd);
                $("#agencyDetailModify #tDremark").val(map.remark);
            }
        });
    }

    // 수수료 계산
    function fn_feeAmtCal() {
        $("#agencyDetailModify #mDrcpfingerfee").text($("#agencyDetailModify #mDrcpcntfee").val() - $("#agencyDetailModify #mDrcpbnkfee").val());
    }
</script>

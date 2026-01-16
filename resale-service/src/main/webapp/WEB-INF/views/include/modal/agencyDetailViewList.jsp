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

<div class="modal fade" id="agencyDetailViewList" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
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
                                        <span id="amtChkTyItemInfo"></span>
                                    </div>
                                </td>
                                <th class="col-md-2 col-sm-4 col-4">발급요청좌수</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="accNoCntInfo"></span>좌
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">대표자명</th>
                                <td class="col-md-4 col-sm-8 col-8">
                                    <span id="mDowner"></span>
                                </td>

                                <th class="col-md-2 col-sm-4 col-4">대표전화번호</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <div class="contact-number form-inline w-100">
                                        <span id="mDownertel"></span>
                                    </div>
                                </td>
                            </tr>

                            <tr class="row no-gutters">
                                <th class="col-md-2 col-sm-4 col-4">담당자 전화번호</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <div class="contact-number form-inline w-100">
                                        <span id="mDchrtelno"></span>
                                    </div>
                                </td>

                                <th class="col-md-2 col-sm-4 col-4">담당자 핸드폰번호</th>
                                <td class="col-md-4 col-sm-8 col-8 form-inline">
                                    <div class="contact-number form-inline w-100">
                                        <span id="mDchrHp"></span>
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
                                            <span id="mDrcpcntfee"></span>원
                                        </td>
                                        <td class="form-td-inline text-right"><span id="tDrcpbnkfee"></span>
                                            <span id="mDrcpbnkfee"></span>원
                                        </td>
                                        <td class="form-td-inline text-right"><span id="tDrcpfingerfee"></span>
                                            <span id="mDrcpfingerfee"></span>원
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
                                    <span id="tDremark">
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    function agencyDetailViewList(thisId) {
        for (var i = 0; i <= 9; i++) {
            var j = i + 1;
            $('#agencyDetailViewList #tDadjfiregkey' + j).text('');
            $('#agencyDetailViewList #tDgrpadjname' + j).text('');
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

                $('#agencyDetailViewList #regDtInfo').text(moment(map.bnkRegiDt,'YYYYMMDD').format('YYYY.MM.DD')); //등록일자

                if(map.chast == "ST02"){ //승인상태
                    $('#agencyDetailViewList #tDchastU').text("해지");
                }else if(map.chast == "ST07"){
                    $('#agencyDetailViewList #tDchastU').text("승인거부");
                }else if(map.chast == "ST04"){
                    $('#agencyDetailViewList #tDchastU').text("정지");
                }else if(map.chast == "ST05"){
                    $('#agencyDetailViewList #tDchastU').text("승인대기");
                }else if(map.chast == "ST01"){
                    $('#agencyDetailViewList #tDchastU').text("승인완료");
                }

                $('#agencyDetailViewList #mDchacd').text(map.chaCd); //기관코드

                if(map.chaOffNo != null && map.chaOffNo != ""){
                    var subChaOffNo = map.chaOffNo.substr(0, 3) + '-' + map.chaOffNo.substr(3, 2) + '-' + map.chaOffNo.substr(5, 5);
                    $('#agencyDetailViewList #mDchaoffno').text(subChaOffNo); //사업자번호
                }

                $('#agencyDetailViewList #mDchaname').text(map.chaName); //기관명(업체명)
                if(map.adjaccyn == "Y") { //다계좌 여부
                    $('#agencyDetailViewList #mDadjaccyn').text("사용");
                    $('#agencyDetailViewList .adjaccInfo').show();
                } else {
                    $('#agencyDetailViewList #mDadjaccyn').text("미사용");
                    $('#agencyDetailViewList .adjaccInfo').hide();
                }

                if(map.amtChkTy == "Y"){ //이용방식
                    $('#agencyDetailViewList #amtChkTyInfo').text("승인");
                }else{
                    $('#agencyDetailViewList #amtChkTyInfo').text("통지");
                }

                $("#agencyDetailViewList #accNoCntInfo").text(map.accNoCnt); //발급요청좌수
                $("#agencyDetailViewList #mDowner").text(map.owner); //대표자명

                if(map.ownerTel == "" || map.ownerTel == null) { //대표전화번호
                    $("#agencyDetailViewList #mDownertel").text("");
                }else{
                    $("#agencyDetailViewList #mDownertel").text(map.ownerTel);
                }

                if(map.chrTelNo == "" || map.chrTelNo == null) { //담당자 전화번호
                    $("#agencyDetailViewList #mDchrtelno").text("");
                }else{
                    $("#agencyDetailViewList #mDchrtelno").text(map.chrTelNo);
                }

                if(map.chrHp == "" || map.chrHp == null) { //담당자 핸드폰번호
                    $("#agencyDetailViewList #mDchrHp").text("");
                }else{
                    if(map.chrHp.indexOf('-') != -1){
                        $("#agencyDetailViewList #mDchrHp").text(map.chrHp);
                    }else{
                        var subChrhp = '';
                        if(map.chrHp.length == 10){
                            subChrhp = map.chrHp.substr(0,3) + '-' + map.chrHp.substr(3,3) + '-' + map.chrHp.substr(6,4);
                            $("#agencyDetailViewList #mDchrHp").text(subChrhp);
                        }else{
                            subChrhp = map.chrHp.substr(0,3) + '-' + map.chrHp.substr(3,4) + '-' + map.chrHp.substr(7,4);
                            $("#agencyDetailViewList #mDchrHp").text(subChrhp);
                        }
                    }
                }

                if(agencyList != null){
                    $.each(agencyList, function(i, v){
                        j = i + 1;
                        $("#agencyDetailViewList #tDadjfiregkey"+j).text(v.adjfiregKey);
                        $("#agencyDetailViewList #tDgrpadjname" +j).text(v.grpadjName);
                    });

                    // 수수료정보
                    $("#agencyDetailViewList #tDnotminlimit").text(numberToCommas(map.notMinLimit));
                    $("#agencyDetailViewList #tDnotminlimit1").text(numberToCommas(map.notMinLimit));
                    $("#agencyDetailViewList #tDnotminfee").text(numberToCommas(map.notMinFee));
                    $("#agencyDetailViewList #tDnotcntfee").text(numberToCommas(map.notCntFee));
                    $("#agencyDetailViewList #tDnotminfingerfee").text(numberToCommas(map.notMinFee));
                    $("#agencyDetailViewList #tDnotcntfingerfee").text(numberToCommas(map.notCntFee));

                    $("#agencyDetailViewList #mDrcpcntfee").text(numberToCommas(map.rcpCntFee));
                    $("#agencyDetailViewList #mDrcpbnkfee").text(numberToCommas(map.rcpBnkFee));

                    var rcpfingerfee = map.rcpCntFee - map.rcpBnkFee;
                    $("#agencyDetailViewList #mDrcpfingerfee").text(numberToCommas(rcpfingerfee));
                }else {
                }

                // 지점정보
                $("#agencyDetailViewList #tDagtname").text(map.agtName);
                $("#agencyDetailViewList #tDagtcd").text(map.agtCd);
                $("#agencyDetailViewList #tDremark").text(map.remark);
            }
        });
    }
</script>

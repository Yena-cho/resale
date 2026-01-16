<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
    var MODE = "";
    var SAVE_MSG = "";
</script>

<div class="modal fade" id="detail-cash-history" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><span id="poptitle">현금영수증 발행이력</span></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row no-gutters mt-3">
                        <div class="col-6">
                            <h5>고객정보</h5>
                        </div>
                        <div class="col-6 text-right">
                        </div>
                    </div>
                </div>

                <form>
                    <div class="container-fluid">
                        <table class="table table-form">
                            <tbody class="container-fluid">
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">고객명</th>
                                    <td class="col-md-4 col-sm-8 col-7"><span id="hCusName" class="table-title-ellipsis"></span></td>
                                    <th class="col-md-2 col-sm-4 col-5">고객번호</th>
                                    <td class="col-md-4 col-sm-8 col-7"><span id="hCusKey" class="table-title-ellipsis"></span></td>
                                </tr>
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">납부대상</th>
                                    <td class="col-md-4 col-sm-8 col-7"><span id="hRcpGubn" class="table-title-ellipsis"></span></td>
                                    <th class="col-md-2 col-sm-4 col-5">가상계좌</th>
                                    <td class="col-md-4 col-sm-8 col-7"><div id="hVano" class="table-title-ellipsis"></div></td>
                                </tr>
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">연락처</th>
                                    <td class="col-md-10 col-sm-8 col-7 form-inline" colspan="3">
                                        <div class="contact-number form-inline w-100">
                                            <span id="hCusHp" class="table-title-ellipsis"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="row no-gutters">
                                    <th class="col-md-2 col-sm-4 col-5">메일주소</th>
                                    <td class="col-md-10 col-sm-8 col-7 form-inline" colspan="3">
                                        <div class="form-inline email-contact w-100">
                                            <span id="hCusMail" class="table-title-ellipsis"></span>
                                        </div>
                                    </td>
                                </tr>
                                <c:if test="${!empty orgSess.cusGubn1}">
                                    <tr class="row no-gutters">
                                        <th class="col-md-2 col-sm-4 col-5">${orgSess.cusGubn1}</th>
                                        <td class="col-md-4 col-sm-8 col-7"><span id="hCusGubn1" class="table-title-ellipsis"></span></td>
                                        <th class="col-md-2 col-sm-4 col-5">${orgSess.cusGubn2}</th>
                                        <td class="col-md-4 col-sm-8 col-7"><span id="hCusGubn2" class="table-title-ellipsis"></span></td>
                                    </tr>
                                </c:if>
                                <c:if test="${!empty orgSess.cusGubn3}">
                                    <tr class="row no-gutters">
                                        <th class="col-md-2 col-sm-4 col-5">${orgSess.cusGubn3}</th>
                                        <td class="col-md-4 col-sm-8 col-7"><span id="hCusGubn3" class="table-title-ellipsis"></span></td>
                                        <th class="col-md-2 col-sm-4 col-5">${orgSess.cusGubn4}</th>
                                        <td class="col-md-4 col-sm-8 col-7"><span id="hCusGubn4" class="table-title-ellipsis"></span></td>
                                    </tr>
                                </c:if>
                                <tr class="row no-gutters hidden-on-mobile">
                                    <th class="col-md-2 col-sm-4 col-5">고객상태</th>
                                    <td class="col-md-10 col-sm-8 col-7 form-inline" colspan="3">
                                        <div class="form-inline" id="divcuststat">
                                            <span id="hDisabled" class="table-title-ellipsis"></span>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="container-fluid">
                        <div class="row no-gutters mt-3">
                            <div class="col-6">
                                <h5>현금영수증 발행이력</h5>
                            </div>
                            <div class="col-6 text-right">
                            </div>
                        </div>
                    </div>

                    <div class="container-fluid">
                        <div class="row no-gutters mt-3">
                            <div class="table-responsive">
                                <%--<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="histCnt"></em>건]</span>--%>
                                <table id="search-result-customer" class="table table-striped table-bordered table-hover table-primary mt-3">
                                    <colgroup>
                                        <col width="120">
                                        <col width="120">
                                        <col width="120">
                                        <col width="120">
                                        <col width="120">
                                        <col width="120">
                                        <col width="120">
                                        <col width="140">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th class="border-l">요청일자</th>
                                        <th>발행상태</th>
                                        <th>현금영수증 발행번호</th>
                                        <th>발행/취소금액(원)</th>
                                        <th>승인일자</th>
                                        <th>승인번호</th>
                                        <th>수신일자</th>
                                        <th>실패사유</th>
                                    </tr>
                                    </thead>
                                    <tbody id="cashHistBody">
                                    <tr>
                                        <td colspan="8" class="text-center">
                                            [조회된 내역이 없습니다.]
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <br>
                    <nav aria-label="Page navigation example">
                        <ul class="pagination justify-content-center" id="ModalPageArea">
                        </ul>
                    </nav>

                    <div class="container-fluid">
                        <ul class="no-gutters" style="list-style: none; padding-inline-start: 0px;">
                            <li>* 2018.8.24 이후의 이력만 조회 가능합니다.</li>
                            <li>* 미발행건은 이력 조회가 불가합니다. (단, 발행 취소건은 조회가능.)</li>
                        </ul>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="aCashMasCd" value="">
<input type="hidden" id="aVano" value="">

<script type="text/javascript">
    $(document).ready(function () {
    });
    function modalList(num, val) {
        fnHistDetailList(num);
    }

    function fnHistDetailList(num) {
        if(num == null || num == 'undefined'){
            cuPage = '1';
        }else{
            cuPage = num;
        }
        var url = "/org/receiptMgmt/selectCashHistDetail";
        var param = {
            vano: $("#aVano").val(),
            cashMasCd : $("#aCashMasCd").val(),
            curPage : cuPage
        };

        $.ajax({
            type: "post",
            url: url,
            async: true,
            data: JSON.stringify(param),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                fnCashHistList(data, 'cashHistBody');
                ajaxModalPaging(data, 'ModalPageArea'); //modal paging
            }
        });

        $(".ajax-loader-area").hide();
    }

    function fnHistDetail(cid, vano) {
        $("#aVano").val(vano),
        $("#aCashMasCd").val(cid),

        fn_reset_scroll();
        $("#hCusKey").text("");
        $("#hCusName").text("");
        $("#hDisabled").html("");
        $('#hRcpGubn').text('');
        $("#hCcusHp").text("");
        $("#hVano").text("");
        $("#hCusMail").text('');
        $("#hCusGubn1").text("");
        $("#hCusGubn2").text("");
        $("#hCusGubn3").text("");
        $("#hCusGubn4").text("");

        // 고객상세 + 발행이력
        var url = "/org/receiptMgmt/selectCashHistDetail";
        var param = {
            vano: vano,
            cashMasCd : cid,
            curPage : 1
        };
        $.ajax({
            type: "post",
            url: url,
            async: true,
            data: JSON.stringify(param),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var map = data.info;

                $("#hCusKey").text(map.cusKey);
                $("#hCusName").text(basicUnEscape(map.cusName));
                if (map.disabled == 'N') {
                    $("#hDisabled").html("정상");
                } else if (map.disabled == 'Y') {
                    $("#hDisabled").html("삭제");
                } else {
                    $("#hDisabled").html("임시");
                }
                if (map.rcpGubn == 'Y') { //납부대상
                    $('#hRcpGubn').text('대상');
                } else { //납부제외
                    $('#hRcpGubn').text('제외');
                }

                var cusHp1 = "";
                var cusHp2 = "";
                var cusHp3 = "";
                if (map.cusHp != null && map.cusHp != '') {
                    var _cusHp = getHpNoFormat(map.cusHp);
                    _cusHp = _cusHp.split('-');
                    $("#hCusHp").text(_cusHp[0] + "-" + _cusHp[1] + "-" + _cusHp[2]);
                }

                $("#hVano").text(map.vano);
                if (map.cusMail == null || map.cusMail == '@') {
                    $("#hCusMail").text('');
                } else if (map.cusMail != null && map.cusMail != '') {
                    var _cusMail = map.cusMail;
                    _cusMail = _cusMail.split('@');
                    $("#hCusMail").text(_cusMail[0] + "@" + _cusMail[1]);
                }

                $("#hCusGubn1").text(basicUnEscape(map.cusGubn1));
                $("#hCusGubn2").text(basicUnEscape(map.cusGubn2));
                $("#hCusGubn3").text(basicUnEscape(map.cusGubn3));
                $("#hCusGubn4").text(basicUnEscape(map.cusGubn4));

                fnCashHistList(data, 'cashHistBody');
                ajaxModalPaging(data, 'ModalPageArea'); //modal paging
            }
        });

        fn_reset_scroll();
        $('#detail-cash-history').modal({
            backdrop: 'static',
            keyboard: false
        });
        $(".ajax-loader-area").hide();
    }

    function fnCashHistList(result, obj) {
        var map = result.cashHist;
        var str = '';
        var jobTy = '';
        var succ = '';
        if(result.cashHistCount <= 0){
            str += '<tr><td colspan="8" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        }else{
            $.each(map, function(k, v){
                str += '<tr>';
                str +=  '<td>'+ nullValueChange(v.txDate) +'</td>';
                if(v.job == 'I'){
                    jobTy = '발행';
                }else if(v.job == 'U' && v.txTypeCode == '0') {
                    jobTy = '재발행';
                // }else if(v.job == 'U' && v.txTypeCode == '1') {
                //     jobTy = '취소';
                }else{
                    jobTy = '취소';
                }
                if(v.responseCode == '0000'){
                    succ = '완료';
                }else{
                    succ = '실패';
                }
                str +=  '<td>'+ jobTy + "" + succ +'</td>';
                str +=  '<td>'+ nullValueChange(v.clientIdentityNo) +'</td>';
                str +=  '<td>'+ nullValueChange(v.txAmount) +'</td>';
                if(v.responseCode == '0000'){
                    str +=  '<td>'+ nullValueChange(v.txDate) +'</td>';
                    str +=  '<td>'+ nullValueChange(v.txNo) +'</td>';
                    str +=  '<td></td>';
                    str +=  '<td></td>';
                }else{
                    str +=  '<td></td>';
                    str +=  '<td></td>';
                    str +=  '<td>'+ nullValueChange(v.responseDate) +'</td>';
                    str +=  '<td>'+ nullValueChange(v.responseMessage) +'</td>';
                }
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

</script>

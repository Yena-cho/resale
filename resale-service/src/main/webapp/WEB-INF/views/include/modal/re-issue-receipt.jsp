<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    $(function () {
        $("#cusType").change(function () {
            if ($(this).val() == '1') { //소득공제
                $('#confirmCusA').css('display', 'block');
                $('#confirmCusB').css('display', 'none');
            } else { //지출증빙
                $('#confirmCusA').css('display', 'none');
                $('#confirmCusB').css('display', 'block');
            }
        });
    });

    /**
     * 현금영수증 정보
     */
    function getCashMasCd(cashMasCd, jobType) {
        var url = "/org/receiptMgnt/getCashMasCd";
        var param = {
            cashMasCd: cashMasCd
        };

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode != "0000") {
                    swal({
                        type: 'error',
                        text: result.retMsg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                } else {
                    var vano = result.list[0].vano;
                    var rcpAmt = result.list[0].rcpAmt;
                    var realJob = result.list[0].job;

                    var cusType = result.list[0].cusType;
                    var cusType2 = result.list[0].cusType2;
                    var confirm = result.list[0].confirm;
                    var confirm2 = result.list[0].confirm2;

                    var cusOffNo = result.list[0].cusOffNo;
                    var cusOffNo2 = result.list[0].cusOffNo2;
                    var cashRcpAmt = result.list[0].cashRcpAmt;
                    var cashRcpAmt2 = result.list[0].cashRcpAmt2;

                    var setCusType;
                    var setConfirm;
                    var setCusOffNo;
                    var setCashRcpAmt;

                    if (jobType == 'I') {
                        // 현금영수증 신규 발행
                        $('#taxTitle').text('현금영수증 발행');
                        $('#btnTxt').text('발행');
                    } else if (jobType == 'U') {
                        // 현금영수증 재발행
                        $('#taxTitle').text('현금영수증 재발행');
                        $('#btnTxt').text('재발행');
                    }

                    setCusType = cusType2 == null || cusType2 == '' ? cusType : cusType2;
                    setConfirm = confirm2 == null || confirm2 == '' ? confirm : confirm2;
                    setCusOffNo = cusOffNo2 == null || cusOffNo2 == '' ? cusOffNo : cusOffNo2;
                    setCashRcpAmt = cashRcpAmt2 == null || cashRcpAmt2 == '' ? cashRcpAmt : cashRcpAmt2;

                    if (setCusType == '1') {
                        $('#confirmCusA').css('display', 'block');
                        $('#confirmCusB').css('display', 'none');
                        $('option[name=cusA]').prop('selected', 'selected');

                        if (setConfirm == '11') {
                            $('option[name=typeA]').prop('selected', 'selected');
                        } else if (setConfirm == '12') {
                            $('option[name=typeB]').prop('selected', 'selected');
                        }
                    } else if (setCusType == '2') {
                        $('#confirmCusA').css('display', 'none');
                        $('#confirmCusB').css('display', 'block');
                        $('option[name=cusB]').prop('selected', 'selected');
                    }
                    $('#getVano').val(vano);
                    $('#pCusOffNo').val(setCusOffNo);
                    $('#pRcpAmt').val(setCashRcpAmt);

                    $('#oRcpAmt').val(rcpAmt);
                    $('#pJobType').val(jobType);
                    $('#pCashMasCd').val(cashMasCd);

                    $("#re-issue-receipt").modal({
                        backdrop: 'static',
                        keyboard: false
                    });
                }
            }
        });
    }

    /**
     * 발행, 재발행 요청
     */
    function btnCashSave() {
        if ($('#pCusOffNo').val() == '') {
            swal({
                type: 'info',
                text: "현금영수증 발행번호를 확인해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        // 발행방법 11:휴대폰 /12:현금영수증카드번호/13:주민번호/21:사업자번호
        var confirm = $('select[name=confirm] option:selected').val();

        // 발행용도 1 : 소득공제 / 2: 지출증빙
        if ($('#cusType option:selected').val() == '1') {
            if (confirm == '11') {
                var regExp = /(01[0|1|6|9|7])(\d{3}|\d{4})(\d{4}$)/g;
                var result = regExp.exec($('#pCusOffNo').val());

                if (!result) {
                    swal({
                        type: 'info',
                        text: "전화번호 형식에 맞게 다시 입력해 주세요!",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                    return;
                }
            } else if (confirm == '12') {
                if ($('#pCusOffNo').val().length != 18) {
                    swal({
                        type: 'info',
                        text: "현금영수증카드번호 길이에 맞게 다시 입력해 주세요!",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                    return;
                }
            } else if (confirm == '13') {
                if ($('#pCusOffNo').val().length != 13) {
                    swal({
                        type: 'info',
                        text: "주민번호 길이에 맞게 다시 입력해 주세요!",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                    return;
                }
            }
        } else {
            if ($('#pCusOffNo').val().length != 10) {
                swal({
                    type: 'info',
                    text: "사업자등록번호 길이에 맞게 다시 입력해 주세요!",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }

        if (removeCommas($('#pRcpAmt').val()) == '') {
            swal({
                type: 'info',
                text: '현금영수증 발행금액을 입력해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (removeCommas($('#pRcpAmt').val()) == 0) {
            swal({
                type: 'info',
                text: '현금영수증 발행금액을 입력해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (removeCommas($('#oRcpAmt').val()) * 1 < removeCommas($('#pRcpAmt').val()) * 1) {
            swal({
                type: 'info',
                text: "발행금액이 입금금액보다 클 수 없습니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var jobType = $('#pJobType').val();
        var cusOffNo = basicEscape($('#pCusOffNo').val());
        var rcpAmt = removeCommas($('#pRcpAmt').val());
        var cashMasCd = $('#pCashMasCd').val();
        var cusType = $('#cusType option:selected').val();
        var confirm = $('select[name=confirm] option:selected').val();
        var vano = $('#getVano').val();

        var url;
        if (jobType == 'I') {
            // 신규발행
            url = "/org/receiptMgnt/insertCashMas";
        } else if (jobType == 'U') {
            // 재발행
            url = "/org/receiptMgnt/updateCashMas";
        }

        var param = {
            job: jobType,
            cusOffNo: cusOffNo,
            rcpAmt: rcpAmt,
            cashMasCd: cashMasCd,
            cusType: cusType,
            confirm: confirm
        };

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                var msg;
                if (jobType == 'I') {
                    msg = '현금영수증 발행 요청을 완료하였습니다. 입력하신 정보로 고객의 현금영수증 발행정보를 수정하시겠습니까?';
                } else {
                    msg = '현금영수증 재발행 요청을 완료하였습니다. 입력하신 정보로 고객의 현금영수증 발행정보를 수정하시겠습니까?';
                }

                if (result.retCode == "0000") {
                    swal({
                        type: 'question',
                        text: msg,
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        confirmButtonText: '확인',
                        cancelButtonText: '취소',
                        reverseButtons: true
                    }).then(function (result) {
                        if (result.value) {
                            var chasMasInfoParam = {
                                cusOffNo: cusOffNo,
                                cusType: cusType,
                                confirm: confirm,
                                vano: vano
                            };

                            $.ajax({
                                type: "post",
                                async: true,
                                url: "/org/receiptMgnt/updateCashMasInfo",
                                contentType: "application/json; charset=utf-8",
                                data: JSON.stringify(chasMasInfoParam),
                                success: function () {
                                    swal({
                                        type: 'success',
                                        text: "입력하신 정보로 고객의 현금영수증 발행정보를 수정하였습니다.",
                                        confirmButtonColor: '#3085d6',
                                        confirmButtonText: '확인'
                                    });

                                    $('#re-issue-receipt').modal('hide');
                                    $('#pCusOffNo').val('');
                                    $('#pRcpAmt').val('');

                                    fnSearch();
                                }
                            });
                        } else {
                            $('#re-issue-receipt').modal('hide');
                            $('#pCusOffNo').val('');
                            $('#pRcpAmt').val('');

                            fnSearch();
                        }
                    });
                } else {
                    swal({
                        type: 'error',
                        text: result.retMsg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            }
        });
    }
</script>

<input type="hidden" id="pCashMasCd" name="pCashMasCd"/>
<input type="hidden" id="pJobType" name="pJobType"/>
<input type="hidden" id="oRcpAmt" name="oRcpAmt"/>
<input type="hidden" id="getVano" name="getVano"/>

<div class="modal fade" id="re-issue-receipt" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="taxTitle"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col">
                            <table class="table table-form">
                                <tbody class="container-fluid">
                                    <tr class="row no-gutters">
                                        <th class="col-md-2 col-sm-4 col-4">발행용도</th>
                                        <td class="col-md-4 col-sm-8 col-8">
                                            <select id="cusType" name="cusType" class="form-control receipt-purpose">
                                                <option value="1" name="cusA">소득공제</option>
                                                <option value="2" name="cusB">지출증빙</option>
                                            </select>
                                        </td>
                                        <th class="col-md-2 col-sm-4 col-4">발행방법</th>
                                        <td class="col-md-4 col-sm-8 col-8">
                                            <select id="confirmCusA" name="confirm" class="form-control method-deduction">
                                                <option value="11" name="typeA">휴대폰번호</option>
                                                <option value="12" name="typeB">현금영수증카드번호</option>
                                            </select>

                                            <select id="confirmCusB" name="confirm" class="form-control method-deduction">
                                                <option value="21" name="typeC">사업자등록번호</option>
                                            </select>
                                        </td>
                                    </tr>

                                    <tr class="row no-gutters">
                                        <th class="col-md-2 col-sm-4 col-4">발행번호</th>
                                        <td class="col-md-4 col-sm-8 col-8">
                                            <input type="text" class="form-control" id="pCusOffNo" onkeydown="onlyNumber(this)">&nbsp;'-' 없이 숫자만 입력
                                        </td>
                                        <th class="col-md-2 col-sm-4 col-4">발행금액</th>
                                        <td class="col-md-4 col-sm-8 col-8 form-td-inline">
                                            <input type="text" class="form-control" id="pRcpAmt" onkeyup="inputNumberFormat(this)" onkeydown="inputNumberFormat(this)">
                                            <span class="ml-2">원</span>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="row mt-4">
                        <div class="col">
                            <div class="guide-mention">
                                - 변경할 사항이 있을 경우 수정하여 진행하시길 바랍니다.
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer mb-3">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>
                <button type="button" class="btn btn-primary" onclick="btnCashSave();" id="btnTxt">발행</button>
            </div>
        </div>
    </div>
</div>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    function fn_modifyDayCharge(updateData, curPage) {
        fn_reset_scroll();

        $('#updateNotimasCd').val(updateData);
        $("#curPage2").val(curPage);

        $('#modify-chargeDay-popup').modal({
            backdrop: 'static',
            keyboard: false
        });
    }

    function fn_chargeDayReturn() {
        $('input[name=updateCheck]').prop('checked', false);
        $('#chargeCalYear').val(moment().format('YYYY'));
        $('#chargeCalMonth').val(moment().format('MM'));
        $("#chargeDayStartDate").val(moment().format('YYYY.MM.DD'));
        $("#chargeDayEndDate").val('');
        $("#chargeDayPrintDate").val('');
        $("#chargePrintDateChk").addClass('checkbox-none-check-disabled');
        $('#updateNotimasCd').val('');
    }

    $(document).ready(function () {
        /**
         * 청구월
         */
        getYearsBox2('chargeCalYear');
        getMonthBox('chargeCalMonth');
        $('#chargeCalYear').val(moment().format('YYYY'));
        $('#chargeCalMonth').val(moment().format('MM'));

        /**
         * 납부시작일의 디폴트 값은 오늘로 지정
         */
        $("#chargeDayStartDate").val(moment().format('YYYY.MM.DD'));

        /**
         * 납부기한 체크 선택 시 고지서용 표시마감일 체크박스 선택
         */
        $("#chargePrintDateChk").addClass('checkbox-none-check-disabled');

        $('#chargeDateChk').click(function () {
            if ($('#chargeDateChk').prop('checked')) {
                $("#chargeDayPrintDate").attr('disabled', false);
                $("#chargePrintDateChk").removeClass('checkbox-none-check-disabled');
                $("#chargePrintDateChk").addClass('checkbox-disabled');
            } else {
                $('#chargePrintDateChk').prop('checked', false);
                $("#chargeDayPrintDate").attr('disabled', true);
                $("#chargePrintDateChk").addClass('checkbox-none-check-disabled');
                $("#chargePrintDateChk").removeClass('checkbox-disabled');

            }
        });

        /**
         * 납부마감일 선택 시, 고지서용 표시마감일의 디폴트 값은 납부마감일
         */
        $("#chargeDayEndDate").on("change", function () {
            $("#chargeDayPrintDate").val(this.value);
        });
    });
    
    function fn_chargeDayUpdate() {
        var curPage = $("#curPage2").val();
        var nowDt = moment().format('YYYYMMDD');

        var notiMasCd = $('#updateNotimasCd').val();
        var masMonth = '';
        var stDt = '';
        var edDt = '';
        var prDt = '';

        if ($('#chargeCalChk').is(":checked") == true) {
            masMonth = $('#chargeCalYear').val() + $('#chargeCalMonth').val();
        }

        if ($('#chargeDateChk').is(":checked") == true) {
            stDt = $('#chargeDayStartDate').val() == '' ? '' : moment($('#chargeDayStartDate').val(), 'YYYYMMDD').format('YYYYMMDD');
            edDt = $('#chargeDayEndDate').val() == '' ? '' : moment($('#chargeDayEndDate').val(), 'YYYYMMDD').format('YYYYMMDD');
            prDt = $('#chargeDayPrintDate').val() == '' ? moment($('#chargeDayEndDate').val(), 'YYYYMMDD').format('YYYYMMDD') : moment($('#chargeDayPrintDate').val(), 'YYYYMMDD').format('YYYYMMDD');

            if((!stDt || !edDt) || (stDt.length < 8 || edDt.length < 8)) {
                swal({
                    type: 'info',
                    text: '납부기한을 확인해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

            if (stDt < nowDt) {
                swal({
                    type: 'info',
                    text: '입금가능 시작일은 금일부터 가능합니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

            if (edDt < nowDt) {
                swal({
                    type: 'info',
                    text: '입금가능 마감일은 금일 이후로 선택해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

            if (!prDt || prDt.length < 8) {
                swal({
                    type: 'info',
                    text: '고지서용 표시마감일을 확인해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

            if (stDt > prDt || edDt < prDt) {
                swal({
                    type: 'info',
                    text: '고지서용 표시마감일은 납부기한보다 작거나 클 수 없습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }

        var updateCheckCnt = 0;
        var updateCheck = $('input[name=updateCheck]:checked');
        updateCheck.each(function(i) {
            updateCheckCnt++;
        });

        if (updateCheckCnt < 1) {
            swal({
                type: 'info',
                text: '수정할 항목을 체크해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else {
            swal({
                text: "납부기한을 수정 하시겠습니까?",
                type: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소'
            }).then(function (result) {
                if (result.value) {
                    var url = "/org/claimMgmt/updateClaimDay";
                    var param = {
                        notiMasCd: notiMasCd,
                        masMonth: masMonth,
                        startDate: stDt,
                        endDate: edDt,
                        printDate: prDt
                    };
                    $.ajax({
                        type: "post",
                        url: url,
                        data: JSON.stringify(param),
                        contentType: "application/json; charset=utf-8",
                        success: function (data) {
                            swal({
                                type: 'success',
                                text: '수정하였습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    $('#modify-chargeDay-popup').modal('hide');
                                    fn_chargeDayReturn();
                                    fn_ClaimListCall(curPage);
                                }
                            });
                        },
                        error: function (data) {
                            swal({
                                type: 'error',
                                text: '수정 실패하였습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            })
                        }
                    });
                }
            });
        }
    }
</script>

<jsp:include page="/WEB-INF/views/include/popHeader.jsp"/>

<input type="hidden" id="updateNotimasCd" name="updateNotimasCd"/>
<input type="hidden" id="curPage2" name="curPage2">

<div class="modal fade" id="modify-chargeDay-popup" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title claim_modal_title">납부기한수정</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="fn_chargeDayReturn();"><span aria-hidden="true">&times;</span></button>
            </div>

            <div class="modal-body">
                <div class="search-box" style="margin-bottom: 0;">
                    <div class="row">
                        <div class="col-lg-10 col-md-12">
                            <div class="row">
                                <div class="col col-md-4 col-sm-12 col-12 form-inline mb-2">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="checkbox" name="updateCheck" id="chargeCalChk">
                                        <label class="form-check-label" for="chargeCalChk"><span class="mr-1"></span>청구월</label>
                                    </div>
                                </div>

                                <div class="col col-md-8 col-sm-12 col-12 form-inline year-month-dropdown mb-2">
                                    <select class="form-control" name="chargeCalYear" id="chargeCalYear">
                                        <option value="">선택</option>
                                    </select>
                                    <span class="ml-1 mr-1">년</span>
                                    <select class="form-control"  name="chargeCalMonth" id="chargeCalMonth">
                                        <option value="">선택</option>
                                    </select>
                                    <span class="ml-1">월</span>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col col-md-4 col-sm-12 col-12 form-inline mb-2">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="checkbox" name="updateCheck" id="chargeDateChk">
                                        <label class="form-check-label" for="chargeDateChk"><span class="mr-1"></span>납부기한</label>
                                    </div>
                                </div>

                                <div class="col col-md-7 col-sm-12 col-12 form-inline mb-2">
                                    <div class="date-input">
                                        <label class="sr-only">From</label>
                                        <div class="input-group">
                                            <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD" id="chargeDayStartDate" aria-label="From" aria-describedby="basic-addon2" maxlength="8">
                                        </div>
                                    </div>

                                    <span class="range-mark"> ~ </span>

                                    <div class="date-input">
                                        <label class="sr-only">To</label>
                                        <div class="input-group">
                                            <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD" id="chargeDayEndDate" aria-label="To" aria-describedby="basic-addon2" maxlength="8">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col col-md-4 col-sm-12 col-12 form-inline mb-2">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="checkbox" name="updateCheck" id="chargePrintDateChk" disabled="disabled">
                                        <label class="form-check-label" for="chargePrintDateChk"><span class="mr-1"></span>고지서용 표시마감일</label>
                                    </div>
                                </div>

                                <div class="col col-md-8 col-sm-12 col-12 form-inline mb-2">
                                    <div class="date-input">
                                        <label class="sr-only">From</label>
                                        <div class="input-group"  style="width: 160px;">
                                            <input type="text" class="form-control date-picker-input" placeholder="YYYY.MM.DD" id="chargeDayPrintDate" aria-label="From" aria-describedby="basic-addon2" maxlength="8">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="page-description" style="margin-bottom: 0; padding: .8rem 1.25rem;">
                    <div class="row">
                        <div class="col">
                            <h6 class="text-danger">※ 주 의</h6>
                            <ul style="margin-bottom: .5rem;">
                                <li>수정하시려는 항목을 체크박스에서 선택 하셔야 수정 반영됩니다.</li>
                                <li>납부시작일은 금일날짜 이전으로 설정하실 수 없습니다.</li>
                                <li>단, 고지서용 표시마감일 납부기한 변경 시 수정하실 수 있으며, 고지서용 표시마감일을 미입력 시 자동으로 납부마감일로 설정되니 참고바랍니다.</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal" onclick="fn_chargeDayReturn()">취소</button>
                <button type="button" class="btn btn-primary" onclick="fn_chargeDayUpdate();">수정</button>
            </div>
        </div>
    </div>
</div>

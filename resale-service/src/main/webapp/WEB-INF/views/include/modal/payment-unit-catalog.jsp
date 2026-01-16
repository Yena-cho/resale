<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade" id="popup-payment-unit-catalog" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">고지내역 청구항목 상세</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col">
                            <table class="table table-responsive table-primary">
                                <colgroup>
                                    <col width="52">
                                    <col width="258">
                                    <col width="200">
                                    <col width="100">
                                    <col width="200">
                                </colgroup>

                                <thead>
                                    <tr>
                                    	<th>
											<input class="form-check-input table-check-parents" type="checkbox" id="row-th" value="checkList" onchange="selectedPayItemsTrigger()">
											<label for="row-th"><span></span></label>
										</th>
                                        <th>청구항목</th>
                                        <th>청구금액</th>
                                        <th>필수여부</th>
                                        <th>비고</th>
                                    </tr>
                                </thead>

                                <tbody id="popResultBody"></tbody>

                                <tfoot id="popResultFoot">
                                    <tr>
                                        <td class="summary-bg" colspan="2">결제총액</td>
                                        <td class="summary-bg text-center" id="popTotalAmt"></td>
                                        <td class="summary-bg" colspan="2"></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary save-payer-information"><i class="fa fa-fw fa-floppy-o"></i>계좌이체</button>
                <button type="button" class="btn btn-secondary">신용카드</button>
            </div>
        </div>
    </div>
</div>

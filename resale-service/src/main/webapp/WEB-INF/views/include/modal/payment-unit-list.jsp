<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade" id="popup-payment-unit-list" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">청구항목 상세보기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col">
                            <table class="table table-responsive table-primary mb-3">
                                <colgroup>
                                    <col width="200">
                                    <col width="200">
                                    <col width="409">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>고객명</th>
                                        <th>청구월</th>
                                        <th>가상계좌번호</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <tr>
                                        <td><span id="popCusName"></span></td>
                                        <td><span id="popMasMonth"></span></td>
                                        <td><span id="popMasVano"></span></td>
                                    </tr>
                                </tbody>
                            </table>

                            <table class="table table-responsive table-primary">
                                <colgroup>
                                    <col width="200">
                                    <col width="200">
                                    <col width="200">
                                    <col width="209">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>청구항목</th>
                                        <th>청구금액</th>
                                        <th>수납금액</th>
                                        <th>비고</th>
                                    </tr>
                                </thead>

                                <tbody id="popResultBody">
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                </tbody>

                                <tfoot id="popResultFoot">
                                    <tr>
                                        <td class="summary-bg">총액</td>
                                        <td class="summary-bg text-center" id="popTotalAmt"></td>
                                        <td class="summary-bg text-center" id="popTotalRcpAmt"></td>
                                        <td class="summary-bg"></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer mb-3">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

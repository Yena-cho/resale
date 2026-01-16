<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade" id="issue-receipt" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">현금영수증 발급 정보</h5>
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
                                        <th class="col-md-2 col-sm-4 col-4">발급용도</th>
                                        <td class="col-md-4 col-sm-8 col-8">
                                            <select class="form-control receipt-purpose">
                                                <option value="deduction">소득공제</option>
                                                <option value="expense">지출증빙</option>
                                            </select>
                                        </td>
                                        <th class="col-md-2 col-sm-4 col-4">발급방법</th>
                                        <td class="col-md-4 col-sm-8 col-8">
                                            <select class="form-control method-deduction">
                                                <option>연락처</option>
                                                <option>현금영수증카드번호</option>
                                            </select>
                                            <select class="form-control method-expense" style="display:none;">
                                                <option>사업자등록번호</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr class="row no-gutters">
                                        <th class="col-md-2 col-sm-4 col-4">발급번호</th>
                                        <td class="col-md-4 col-sm-8 col-8">
                                            <input type="text" class="form-control">
                                        </td>
                                        <th class="col-md-2 col-sm-4 col-4">부가가치세</th>
                                        <td class="col-md-4 col-sm-8 col-8 form-inline">
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" id="vatAccepted" name="sorting-order-type">
                                                <label for="vatAccepted"><span class="mr-2"></span>적용</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" id="vatDenied" name="sorting-order-type">
                                                <label for="vatDenied"><span class="mr-2"></span>미적용</label>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class="row no-gutters">
                                        <th class="col-md-2 col-sm-4 col-4">발급번호</th>
                                        <td class="col-md-4 col-sm-8 col-8 form-td-inline">
                                            <input type="text" class="form-control" value="20,000" disabled>
                                            <span class="ml-2">원</span>
                                        </td>
                                        <th class="col-md-2 col-sm-4 col-4">발행금액</th>
                                        <td class="col-md-4 col-sm-8 col-8 form-td-inline">
                                            <input type="text" class="form-control" value="20,000">
                                            <span class="ml-2">원</span>
                                        </td>
                                    </tr>
                                    <tr class="row no-gutters">
                                        <th class="col-md-2 col-sm-4 col-4">봉사료</th>
                                        <td colspan="3" class="col-md-4 col-sm-8 col-8 form-td-inline">
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" id="handling-fee-accteped" name="sorting-order-type">
                                                <label for="handling-fee-accteped"><span class="mr-2"></span>적용</label>
                                            </div>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" id="handling-fee-denied" name="sorting-order-type">
                                                <label for="handling-fee-denied"><span class="mr-2"></span>미적용</label>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="row mt-4">
                        <div class="col">
                            <div class="guide-mention">
                                - 발행 정보 확인 후 진행하시기 바랍니다.<br>
                                - 변경할 사항이 있을 경우 수정하여 진행하시길 바랍니다.
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer mb-3">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>
                <button type="button" class="btn btn-primary">발행</button>
            </div>
        </div>
    </div>
</div>

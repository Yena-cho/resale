<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade" id="payer-classification" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">고객구분 설정</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                <table class="table table-form mt-3">
                    <tbody class="container-fluid">
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">고객구분1</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <input type="text" class="form-control">
                            </td>
                            <th class="col-md-2 col-sm-4 col-4">고지서 출력</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <div class="form-inline">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="visibility-radio-1" name="visibility-in-bill-1">
                                        <label for="visibility-radio-1"><span class="mr-2"></span>출력</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="visibility-radio-2" name="visibility-in-bill-1">
                                        <label for="visibility-radio-2"><span class="mr-2"></span>미출력</label>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">고객구분2</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <input type="text" class="form-control">
                            </td>
                            <th class="col-md-2 col-sm-4 col-4">고지서 출력</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <div class="form-inline">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="visibility-radio-3" name="visibility-in-bill-2">
                                        <label for="visibility-radio-3"><span class="mr-2"></span>출력</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="visibility-radio-4" name="visibility-in-bill-2">
                                        <label for="visibility-radio-4"><span class="mr-2"></span>미출력</label>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">고객구분3</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <input type="text" class="form-control">
                            </td>
                            <th class="col-md-2 col-sm-4 col-4">고지서 출력</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <div class="form-inline">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="visibility-radio-5" name="visibility-in-bill-3">
                                        <label for="visibility-radio-5"><span class="mr-2"></span>출력</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="visibility-radio-6" name="visibility-in-bill-3">
                                        <label for="visibility-radio-6"><span class="mr-2"></span>미출력</label>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">고객구분4</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <input type="text" class="form-control">
                            </td>
                            <th class="col-md-2 col-sm-4 col-4">고지서 출력</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <div class="form-inline">
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="visibility-radio-7" name="visibility-in-bill-4">
                                        <label for="visibility-radio-7"><span class="mr-2"></span>출력</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" id="visibility-radio-8" name="visibility-in-bill-4">
                                        <label for="visibility-radio-8"><span class="mr-2"></span>미출력</label>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-12">
                            <span>고객구분 : 고객별 그룹지정명칭(ex. 반, 부서, 지역, 학번 등)</span>
                        </div>
                        <div class="col-12">
                            <span>고지서출력 : 고지서출력 시 고객구분명칭의 인쇄여부 노출 여부</span>
                        </div>
                    </div>
                </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">취소</button>
                <button type="button" class="btn btn-primary">저장</button>
            </div>
        </div>
    </div>
</div>

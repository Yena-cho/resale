<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="modal fade" id="reg-bulk-form-selecter" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">청구등록 - 엑셀파일 대량등록</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="container">
                        <div id="page-description">
                            <div class="row">
                                <div class="col-12">
                                    <p>2018년 8월 24일부터 청구 대량등록 양식이 변경 되었습니다.
                                        <br/>새로운 양식에서 청구등록 정보를 작성하실 경우 좌측 [청구등록 바로가기] 버튼을 클릭해주세요.
                                        <br/>이전 양식의 정보를 그대로 업로드를 원하실 경우, 우측 [과거양식 변환하기] 버튼을 클릭하여 양식 변환 후, 파일을 등록하실 수 있습니다.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="payer-registration-option">
                    <div class="row">
                        <div class="col col-md-6 col-6 text-center" onclick="fn_regBulkPayer(); overFlowHidden();">
                            <a href="#" data-dismiss="modal" data-toggle="modal" data-target="#reg-bulk-payer">
                                <img src="/assets/imgs/common/icon-bulk-registration.png" class="mb-2">
                                <span>청구등록 바로가기</span>
                            </a>
                        </div>
                        <div class="col col-md-6 col-6 text-center" onclick="fn_regBulkAsisPayer();">
                            <a href="#" data-dismiss="modal" data-toggle="modal" data-target="#reg-bulk-asis-payer">
                                <img src="/assets/imgs/common/icon-bulk-asis-registration.png" class="mb-2">
                                <span>과거양식 변환하기</span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col text-center">
                            <button type="button" class="btn btn-primary btn-outlined close-modal" data-dismiss="modal">닫기</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

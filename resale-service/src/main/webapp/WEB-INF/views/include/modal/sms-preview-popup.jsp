<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade" id="sms-preview" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">미리보기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col">
                            <div class="preview-box mb-4" id="previewContents">
                                [web발신] 000유치원<br>
                                안녕하세요? 홍길동님. 2018년 8월 청구금액은
                                50,000원입니다. 2018년 8월 30일까지 신한은행
                                156-345-235535로 입금해 주시기 바랍니다.<br>
                                청구 상세내역 및 즉시납부 바로가기<br>
                                https://m.shinhandamoa.com/notification
                            </div>

                             <div class="list-button-group-bottom text-center">
                                <button type="button" class="btn btn-primary btn-outlined mb-1" data-dismiss="modal">닫기</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

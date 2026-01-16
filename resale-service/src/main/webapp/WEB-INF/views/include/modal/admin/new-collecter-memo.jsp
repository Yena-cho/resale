<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="modal fade" id="popup-new-collecter-memo" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">가상계좌 수납관리 서비스 관리 메모</h5>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-xs-12">
                            <textarea class="memo-box" rows="8" id="daMngMemo"></textarea>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-md btn-default" data-dismiss="modal">취소</button>
                <button type="button" class="btn btn-md btn-primary" onclick="updateDaMngMemo();"><i class="fa fa-floppy-o"></i>저장</button>
            </div>
        </div>
    </div>
</div>

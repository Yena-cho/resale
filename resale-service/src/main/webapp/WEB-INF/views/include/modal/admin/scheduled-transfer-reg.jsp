<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="modal fade" id="popup-scheduled-transfer-reg" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title">자동이체등록</h5>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="container-fluid">
                        <div class="row form-group">
                            <label class="control-label col-sm-4 col-xs-12">출금은행</label>
                            <div class="col-sm-8 col-xs-12"><input type="text" class="form-control" value=""></div>
                        </div>
                        <div class="row form-group">
                            <label class="control-label col-sm-4 col-xs-12">변경출금계좌</label>
                            <div class="col-sm-8 col-xs-12"><input type="text" class="form-control" value=""></div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary"><i class="fa fa-fw fa-cloud-upload"></i>등록</button>
            </div>
        </div>
    </div>
</div>

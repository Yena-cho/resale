<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="inmodal modal fade" id="popup-new-group" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-big" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">기관그룹 등록</h5>
            </div>

            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="ibox float-e-margins">
                                <div class="ibox-content">
                                    <form method="get">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="form-group form-group-sm">
                                                    <label class="form-label block">그룹ID</label>
                                                    <input type="text" class="form-control ng-untouched ng-pristine ng-valid group-id" style="height: 34px;" disabled="disabled" />
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <label class="form-label block">그룹명</label>
                                                <div class="form-group form-group-sm">
                                                    <div class="input-group col-md-12">
                                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid group-name" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="form-group form-group-sm">
                                                    <label class="form-label block">종류</label>
                                                    <div class="input-group col-md-12">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="group-transaction-type-01" name="group-transaction-type" value="01" />
                                                            <label for="group-transaction-type-01"> WEB </label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" id="group-transaction-type-03" name="group-transaction-type" value="03" />
                                                            <label for="group-transaction-type-03"> API </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="form-group form-group-sm">
                                                    <label class="form-label block">상태</label>
                                                    <div class="input-group col-md-12">
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" name="group-status" id="group-status-st01" value="ST01" />
                                                            <label for="group-status-st01"> 정상 </label>
                                                        </div>
                                                        <div class="radio radio-primary radio-inline">
                                                            <input type="radio" name="group-status" id="group-status-st03" value="ST02" />
                                                            <label for="group-status-st03"> 해지 </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="form-group form-group-sm">
                                                    <label class="form-label block">비밀번호</label>
                                                    <input type="text" class="form-control group-password" value="" disabled="disabled" />
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-lg-12">
                                                <div class="form-group form-group-sm">
                                                    <label class="form-label block">메모</label>
                                                    <textarea name="group-remark" class="form-control group-remark" rows="4" style="height: 150px;"></textarea>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary save-cha-info" onclick="createGroup();"><i class="fa fa-fw fa-floppy-o"></i>저장</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
function createGroup() {
    var url = '/sys/chaMgmt/cha-group';
    
    var data = {};

    var $popup = $('#popup-new-group');
    data.name = $popup.find('input.group-name').val();
    data.status = $popup.find('input[name="group-status"]:checked').val();
    data.transactionType = $popup.find('input[name="group-transaction-type"]:checked').val();
    data.remark = $popup.find('textarea[name="group-remark"]').val();
    data.password = $popup.find('input.group-password').val();
    
    $.ajax(url, {
        type: 'POST',
        data : JSON.stringify(data),
        contentType : 'application/json',
        success: successToCreateGroup,
        failure: failToCreateGroup
    });    
}

function successToCreateGroup(data) {
    alert('등록했습니다');

    $('#popup-new-group').modal('hide');
}

function failToCreateGroup(data) {
    alert('오류가 발생했습니다. 내용을 다시 확인해주세요.');
}
</script>

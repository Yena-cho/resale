<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="modal fade" id="atchaname-update-popup" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">발송기관명 수정</h5>
            </div>

            <input type="hidden" name="popChaCd" id="popChaCd" value="">
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group form-group-sm">
                                <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="popAtchaName" id="popAtchaName" maxlength="20">
                                <input type="hidden" name="popAtHisNo" id="popAtHisNo">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary" onclick="updateAtChaName();">수정</button>
            </div>
        </div>
    </div>
</div>

<script>
    //발송기관명 수정
    function updateAtChaName() {
        swal({
            type: 'question',
            html: '발송기관명을 수정하시겠습니까?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                var param = {
                    chaCd : $("#popChaCd").val(),
                    atChaName : $("#popAtchaName").val(),
                    atHisNo : $("#popAtHisNo").val()
                }
                $.ajax({
                    type: "post",
                    url: "/sys/addServiceMgmtA/updateAtChaName",
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (result) {
                        if (result.retCode == "0000") {
                            $("#atchaname-update-popup").modal('hide');
                            fnSearch($('#pageNo').val());
                            swal({
                                type: 'info',
                                text: '발송기관명을 수정하였습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        } else {
                            swal({
                                type: 'error',
                                text: result.retMsg,
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        }
                    }
                });
            }

        });
    }

</script>
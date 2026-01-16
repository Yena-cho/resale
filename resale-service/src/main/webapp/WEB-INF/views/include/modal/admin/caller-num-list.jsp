<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    //등록 발신번호 수정
    function updateCallerNum() {
        var idx = 0;
        var regExpTel = /^\d{2,3}\d{3,4}\d{4}$/;
        var strList = [];
        var number = $("input[name='fromNumber']");
        number.map(function (i) {
            if ($(this).val() != '') {
                if (!regExpTel.test($(this).val())) {
                    swal({
                        type: 'info',
                        text: '전화번호를 확인해주세요.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                    idx++;
                    return;
                } else {
                    strList.push($(this).val());
                }
            }
        });

        if (idx == 0) {
            var url = "/sys/addServiceMgmt/updateCallerNum";
            var formData = new FormData();
            formData.append("strList", strList);
            formData.append("no", $("#modalNo").val());
            formData.append("chaCd", $("#modalChaCd").val());
            var param = {
                no: $("#modalNo").val(),
                chaCd: $("#modalChaCd").val(),
                strList: strList
            };
            $.ajax({
                type: "post",
                url: url,
                data: formData,
                dataType: "text",
                processData: false,
                contentType: false,
                success: function (data) {
                    swal({
                        type: 'success',
                        text: "발신번호 수정이 완료되었습니다.",
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function (result) {
                        $("#popup-caller-num-list").modal('hide');
                    });
                },
                error: function (data) {
                    swal({
                        type: 'error',
                        text: '발신번호 수정을 실패하였습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function (result) {
                        $("#popup-caller-num-list").modal('hide');
                    });
                }
            });
        }
    }

</script>

<input type="hidden" id="modalNo" name="modalNo">
<input type="hidden" id="modalChaCd" name="modalChaCd">

<div class="modal fade" id="popup-caller-num-list" tabindex="-1" role="dialog" aria-labelledby="regPayer"
     aria-hidden="true">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">등록 발신번호 상세보기</h5>
            </div>

            <div class="modal-body">
                <form>
                    <table class="table table-stripped" id="callerTable">
                        <tr>
                            <td class="text-center">1</td>
                            <td><input type="text" class="form-control" name="fromNumber" onkeydown="onlyNumber(this)" maxlength="12"></td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td><input type="text" class="form-control" name="fromNumber" onkeydown="onlyNumber(this)" maxlength="12"></td>
                        </tr>
                        <tr>
                            <td>3</td>
                            <td><input type="text" class="form-control" name="fromNumber" onkeydown="onlyNumber(this)" maxlength="12"></td>
                        </tr>
                        <tr>
                            <td>4</td>
                            <td><input type="text" class="form-control" name="fromNumber" onkeydown="onlyNumber(this)" maxlength="12"></td>
                        </tr>
                        <tr>
                            <td>5</td>
                            <td><input type="text" class="form-control" name="fromNumber" onkeydown="onlyNumber(this)" maxlength="12"></td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary" onclick="updateCallerNum();"><i class="fa fa-fw fa-edit"></i>수정</button>
            </div>
        </div>
    </div>
</div>
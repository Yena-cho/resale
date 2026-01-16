<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script type="text/javascript">
    function fn_checkType(type) {
        if (type == "U") {
            $(".uploadFee").show();
            $(".resultFee").hide();
        } else if(type == "R") {
            $(".resultFee").show();
            $(".uploadFee").hide();
        }
    }

    function fn_fileChk() {
        var fileNm = $("#uploadfile").val();
        $("#fileNM").val(fn_clearFilePath(fileNm));
    }

    function fn_clearFilePath(val) {
        var tmpStr = val;

        var cnt = 0;
        if(val != null){
            while (true) {
                cnt = tmpStr.indexOf("/");
                if (cnt == -1) break;
                tmpStr = tmpStr.substring(cnt + 1);
            }
            while (true) {
                cnt = tmpStr.indexOf("\\");
                if (cnt == -1) break;
                tmpStr = tmpStr.substring(cnt + 1);
            }
        }

        return tmpStr;
    }

    // 파일업로드
    function fn_fileUpload() {
        var idx = 0;
        var fileNM = $('#fileNM').val();
        if (!fileNM) {
            swal({
                type: 'info',
                text: '파일을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var msg = "";
        var rmsg = "등록하였습니다.";
        if($("#uploadTy").val() == "R") {
            if (fileNM.substring(0, 4) != 'EB22') {
                idx = 1;
                msg = "파일명은 EB22로 시작하여야 합니다.";
            } else {
                var url = "/sys/rest/kftc-withdraw/eb22";
            }
        }else if($("#uploadTy").val() == "U") {
            if (!fn_checkFileType(fileNM)) {
                idx = 1;
                msg = "엑셀 파일만 업로드 가능합니다!";
            } else {
                var url = "/sys/auto/insertAutoTranFeeInfo";
            }
        }

        if (idx > 0) {
            swal({
                type: 'info',
                text: msg,
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if (idx == 0) {
            swal({
                type: 'question',
                html: "등록 하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                reverseButtons: true
            }).then(function (result) {
                if (result.value) {
                    var formData = new FormData();
                    formData.append("file", $('#uploadfile')[0].files[0]);
                    $.ajax({
                        type: "post",
                        url: url,
                        async: true,
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function (data) {
                            if($("#uploadTy").val() == "U"){
                                rmsg=data.rMsg;
                            }
                                swal({
                                    type: 'success',
                                    text: rmsg,
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                }).then(function (result) {
                                    $('#uploadfile').val('');
                                    $('#fileNM').val('');
                                    $("#popup-autoTranFeeUpload").modal("hide");
                                        fn_accSearch();
                                });
                        }, error: function (data) {
                            swal({
                                type: 'warning',
                                text: '등록에 실패하였습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                $('#uploadfile').val('');
                                $('#fileNM').val('');
                                $("#popup-autoTranFeeUpload").modal("hide");
                                    fn_accSearch();
                            });
                        }
                    });
                }
            });
        }

    }

    function fn_checkFileType(filePath) {
        var fileFormat = filePath.split(".");
        if (fileFormat.indexOf("xlsx") > -1) {
            return true;
        } else if (fileFormat.indexOf("xls") > -1) {
            return true;
        } else {
            return false;
        }
    }

    function fn_autoTranFeeExcelDown(){

        document.downForm.action = "/sys/auto/autoTranFeeExcelDown";
        document.downForm.submit();
    }
</script>

<form id="downForm" name="downForm" method="post">
    <input type="hidden" class="form-control" id="uploadTy" value="">
</form>
<div class="modal fade" id="popup-autoTranFeeUpload" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">파일 업로드</h5>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row row-eq-height">
                        <form class="form-horizontal" id="fileForm" name="fileForm" enctype="multipart/form-data" method="post">
                                <div class="col-md-3 col-sm-12 resultFee">출금이체결과(EB22)</div>
                                <div class="col-md-3 col-sm-12 uploadFee">출금업로드</div>
                                <div class="col-md-9 col-sm-12">
                                    <div class="form-inline">
                                    <div class="input-group">
                                        <input type="file" id="uploadfile" name="uploadfile" class="hidden" onchange="fn_fileChk();">
                                        <input type="text" class="form-control" disabled="disabled" id="fileNM">
                                    </div>
                                        <label class="btn btn-sm btn-w-m btn btn-primary" for="uploadfile">파일찾기</label>
                                        <button type="button" class="btn btn-sm btn-w-m btn-primary uploadFee" onclick="fn_autoTranFeeExcelDown();"><i class="fa fa-cloud-download"></i>양식다운</button>
                                    </div>
                                </div>
                        </form>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
                <button type="button" class="btn btn-primary" onclick="fn_fileUpload();"><i class="fa fa-cloud-upload"></i>등록</button>
            </div>
        </div>
    </div>
</div>

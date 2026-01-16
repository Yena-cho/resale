<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script type="text/javascript">
    function fn_fileChk() {
        var fileNm = fn_clearFilePath($("#upload-file").val());
        var maxSize = 10 * 1024 * 1024; //10MB
        var fileSize = 0;
        if ($("#upload-file").val()) {
            fileSize = document.getElementById("upload-file").files[0].size;
        }

        if (fileSize > maxSize) {
            swal({
                type: 'error',
                text: ' 10MB 이하의 파일만 업로드 할 수 있습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (!fn_checkFileType(fileNm)) {
            swal({
                type: "info",
                text: "jpg, tif, mp3 파일만 업로드 가능합니다",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            $('#upload-file').val('');
            $('#fileName').val('');
            return;
        }

        $('#fileName').val(fileNm);
    }

    function fn_clearFilePath(val) {
        var tmpStr = val;
        var cnt = 0;
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

        return tmpStr;
    }

    function fn_checkFileType(filePath) {
        var fileFormat = filePath.substring(filePath.lastIndexOf(".")+1, filePath.length);
        if (fileFormat.toLowerCase() == "jpg" || fileFormat.toLowerCase() == "tif" || fileFormat.toLowerCase() == "mp3") {
            return true;
        } else {
            return false;
        }
    }

    // 변경등록
    function fn_change() {
        var fileName = $('#fileName').val();
        if (!fileName) {
            swal({
                type: 'info',
                text: '파일을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var url = "/sys/auto/cmsChangeFile";

        if (fileName) {
            swal({
                type: 'question',
                html: "등록 하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소'
            }).then(function (result) {
                if (result.value) {
                    var formData = new FormData();
                    formData.append("file", $('#upload-file')[0].files[0]);
                    formData.append("chaCd", $('#fileFormChacd').val());
                    $.ajax({
                        type: "post",
                        url: url,
                        data: formData,
                        dataType: "text",
                        processData: false,
                        contentType: false,
                        success: function (data, status, req) {
                            swal({
                                type: 'success',
                                text: '등록하였습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                $('#upload-file').val('');
                                $('#fileName').val('');
                                $("#popup-cms-upload").modal("hide");
                                fn_search($("#pageNo").val());
                            });
                        },
                        error: function (data) {
                            swal({
                                type: 'error',
                                text: '등록 실패하였습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        }
                    });
                }
            });
        }
    }

</script>

<div class="modal fade" id="popup-cms-upload" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">출금 동의서 업로드</h5>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row row-eq-height">
                        <form class="form-horizontal" id="fileForm" name="fileForm" enctype="multipart/form-data" method="post">
                            <div class="col-md-3 col-sm-12">신청서 등록</div>
                            <div class="col-md-9 col-sm-12">
                                <div class="form-inline">
                                    <div class="input-group">
                                        <input type="file" id="upload-file" name="upload-file" class="hidden" onchange="fn_fileChk();">
                                        <input type="text" class="form-control" disabled="disabled" id="fileName">
                                        <input type="hidden" class="form-control" id="fileFormChacd" value="">
                                    </div>
                                    <label class="btn btn-primary" for="upload-file">파일찾기</label>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="modal-description-list">
                                <ul>
                                    <li>파일용량 10MB 이내</li>
                                    <li>파일형식 : jpg, tif, mp3</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
                <button type="button" class="btn btn-primary" onclick="fn_change();"><i class="fa fa-cloud-upload"></i>등록</button>
            </div>
        </div>
    </div>
</div>

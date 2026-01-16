<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script type="text/javascript">
    function fn_fileChange(chaCd, chaName) {
        $('#mChaCd').val(chaCd);
        $('#mChaName').val(chaName);
        $('#mFeeAccNo').val('');
        $('#popup-withdrawal-agreement-modify').modal({backdrop: 'static', keyboard: false});
    }

    function fn_fileChk() {
        var fileNm = fn_clearFilePath($("#upload-file").val());            // 파일 명

        if (!fn_checkFileType(fileNm)) {
            swal({
                type: "info",
                text: "'jpg' 파일만 업로드 가능합니다",
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
        var fileFormat = filePath.split(".");
        if (fileFormat.indexOf("jpg") > -1) {
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
        if (!$('#mFeeAccNo').val()) {
            swal({
                type: 'info',
                text: '변경 출금계좌를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $("#mFeeAccNo").focus();
            });
            return;
        }

        var url = "/sys/auto/cmsChangeFile";

        if (fileName) {
            swal({
                type: 'question',
                html: "변경등록 하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소'
            }).then(function (result) {
                if (result.value) {
                    var formData = new FormData();
                    formData.append("file", $('#upload-file')[0].files[0]);
                    formData.append("chaCd", $('#mChaCd').val());
                    formData.append("feeAccNo", $('#mFeeAccNo').val());
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
// 							   fn_search();
                                location.href = "/sys/auto/autoTran";
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

<div class="modal fade" id="popup-withdrawal-agreement-modify" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
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
                            <div class="col-md-2 col-sm-12">신청서 등록</div>
                            <div class="col-md-10 col-sm-12">
                                <div class="form-inline">
                                    <div class="input-group">
                                        <input type="file" id="upload-file" name="upload-file" class="hidden" onchange="fn_fileChk();">
                                        <input type="text" class="form-control" disabled="disabled" id="fileName">
                                    </div>
                                    <label class="btn btn-primary" for="upload-file">파일찾기</label>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div class="row row-eq-height m-t-lg">
                        <div class="col-sm-12"><h3>변경정보</h3></div>
                    </div>

                    <div class="row row-eq-height form-group">
                        <div class="col-md-2 col-sm-3">기관코드</div>
                        <div class="col-md-4 col-sm-9"><input type="text" class="form-control" id="mChaCd" disabled="disabled"></div>
                        <div class="col-md-2 col-sm-3">기관명</div>
                        <div class="col-md-4 col-sm-9"><input type="text" class="form-control" id="mChaName" disabled="disabled"></div>
                    </div>
                    <div class="row row-eq-height form-group">
                        <div class="col-md-2 col-sm-3">출금은행</div>
                        <div class="col-md-4 col-sm-9"><input type="text" class="form-control" id="mFeeBank" value="신한은행" disabled="disabled"></div>
                        <div class="col-md-2 col-sm-3">변경 출금계좌</div>
                        <div class="col-md-4 col-sm-9"><input type="text" class="form-control" id="mFeeAccNo" onkeydown="onlyNumber(this)" maxlength="20"></div>
                    </div>

                    <div class="row">
                        <div class="col-sm-12">
                            <div class="modal-description-list">
                                <ul>
                                    <li>파일용량 1MB 이내</li>
                                    <li>파일형식 : jpg</li>
                                    <li>변경을 원하시는 출금동의서 파일을 확인하시고 업로드 해주시기 바랍니다.</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
                <button type="button" class="btn btn-primary" onclick="fn_change();"><i class="fa fa-cloud-upload"></i>변경등록</button>
            </div>
        </div>
    </div>
</div>

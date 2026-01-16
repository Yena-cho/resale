<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="modal fade" id="popup-withdrawal-agreement-upload" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">출금 동의서 업로드</h5>
            </div>

            <form id="fileForm" name="fileForm" enctype="multipart/form-data">
                <input type="hidden" id="fileFormChacd" name="fileFormChacd">
                <input type="file" name="file" style="display: none;" onchange="document.getElementById('filename').value = this.value" accept=".jpg, .gif, .pdf">
            </form>

            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12 form-inline">
                            <input type="text" class="form-control " placeholder="최대 2MB 입력 가능 (jpg 파일 첨부 가능)" id="filename" readonly="readonly">
                            <button type="button" class="btn btn-primary" id="btn-upload" onclick="filesearch();">파일찾기</button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary" onclick="doUploadCmsFile();"><i class="fa fa-floppy-o"></i> 출금동의서업로드</button>
            </div>
        </div>
    </div>
</div>

<script>
    function filesearch() {
        $("input:file").click();
    }
</script>

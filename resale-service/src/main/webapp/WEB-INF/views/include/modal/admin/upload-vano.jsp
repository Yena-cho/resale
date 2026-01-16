<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script type="text/javascript">
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
    function fn_addCustExcel() {
        var file = $("#upload-bulk-payer-by-exel").val();
        if (file == "" || file == null) {
            swal({
                type: 'info',
                text: "파일을 선택해 주세요!",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else if (!fn_checkFileType(file)) {
            swal({
                type: 'info',
                text: "엑셀 파일만 업로드 가능합니다!",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (file != "" || file != null) {
            swal({
                text: "청구대상을 추가 하시겠습니까?",
                type: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                reverseButtons: true
            }).then(function (result) {
                if (result.value) {
                    var url = "/org/claimMgmt/excelUpload";
                    var formData = new FormData();
                    formData.append("file", $('#upload-bulk-payer-by-exel')[0].files[0]);

                    $('.ajax-loader-area').css('display', 'block'); // progress bar start

                    $.ajax({
                        type: "post",
                        url: url,
                        async: true,
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function (data) {

                            $('.ajax-loader-area').css('display', 'none');
                            var msg = "";
                            if (data.failCnt) {
                                msg = "<p>등록 실패된 [" + data.failCnt + "건]을 제외한, 청구 건의 대상 추가를 완료했습니다.";
                            }
                            if (data.resCode == "0000") {
                                swal({
                                    title: "등록에 성공하였습니다.",
                                    html: msg,
                                    type: 'success',
                                    showCancelButton: true,
                                    confirmButtonColor: '#3085d6',
                                    cancelButtonColor: '#d33',
                                    confirmButtonText: '대상목록확인',
                                    cancelButtonText: '실패내역확인',
                                    reverseButtons: true
                                }).then(function (result) {
                                    if (result.value) {
                                        // 확인 - 청구대상목록으로 이동
                                        $('#reg-bulk-by-excel').modal('hide');
                                        overFlowAuto();
                                        fn_ClaimListCall(1);
                                    } else {
                                        $('#fileName').val('');
                                        //취소 - 현재 페이지에서 대상 등록 실패내역을 조회한다.
                                        fn_claimFailList('1');
                                        document.getElementById('fileName').value = '';
                                    }
                                });
                            } else if (data.resCode == "0001") {
                                swal({
                                    type: 'error',
                                    text: '형식에 맞지 않는 양식 파일입니다. 다시 등록해 주세요.',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                });
                            }
                        },
                        error: function (data) {
                            $('.ajax-loader-area').css('display', 'none');
                            swal({
                                type: 'error',
                                text: '등록 실패하였습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function () {
                                fn_claimFailList('1'); // 대량등록 실패내역 조회
                            });
                        }
                    });
                }
            });
        }
    }

</script>

<form id="downForm" name="downForm" method="post">
    <input type="hidden" class="form-control" id="uploadTy" value="">
</form>
<div class="modal fade" id="popup-upload-eb14" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
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
                            <div class="col-md-3 col-sm-12 uploadCMS">결과파일업로드(EB14)</div>
                            <div class="col-md-9 col-sm-12">
                                <div class="form-inline">
                                    <div class="input-group">
                                        <input type="file" id="uploadfile" name="uploadfile" class="hidden" onchange="fn_fileChk();">
                                        <input type="text" class="form-control" disabled="disabled" id="fileNM">
                                    </div>
                                    <label class="btn btn-sm btn-w-m btn btn-primary" for="uploadfile">파일찾기</label>
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

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript">
    function fn_fileChk() {
        var filename = $("#uploadfile").val();
        $("#filename").val(fn_clearFilePath(filename));
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
        var filename = $('#filename').val();
        if (!filename) {
            swal({
                type: 'info',
                text: '파일을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var msg = "";

        // if (!fn_checkFileType(filename)) {
        //     idx = 1;
        //     msg = "엑셀 파일만 업로드 가능합니다!";
        // } else {
        //     var url = "/sys/vanoMgmt/uploadExcelVirtualAccount";
        // }

        // if (idx > 0) {
        //     swal({
        //         type: 'info',
        //         text: msg,
        //         confirmButtonColor: '#3085d6',
        //         confirmButtonText: '확인'
        //     });
        //     return;
        // }

        var url = "/sys/vanoMgmt/csvUpload";


        if (idx == 0) {
            swal({
                type: 'question',
                html: "가상계좌를 등록하시겠습니까?",
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
                    formData.append("fgcd", $("input[name='radio']:checked").val());

                    $.ajax({
                        type: "post",
                        url: url,
                        async: true,
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function (data) {
                            console.log("data.resCode"+data.resCode);

                            if(data.resCode=="0000") {
                                swal({
                                    type: 'success',
                                    text: data.rMsg,
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                }).then(function (data) {
                                    $('#uploadfile').val('');
                                    $('#filename').val('');
                                    fn_csvResult();
                                });
                            }else if(data.resCode=="9998"){

                                swal({
                                    type: 'warning',
                                    text: data.rMsg,
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                })

                            }else {
                                swal({
                                    type: 'warning',
                                    text: '알수없는 오류입니다 관리자에게 문의하세요.',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                })

                            }


                        }, error: function (data) {
                            swal({
                                type: 'warning',
                                text: '등록에 실패하였습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                $('#uploadfile').val('');
                                $('#filename').val('');
                                fnSearch();
                            });
                        }
                    });
                }
            });
        }
    }

    function fn_csvResult() {

        var url = "/sys/vanoMgmt/csvResult";

        var param = {
            searchStartday: "2022011",
        };

        $.ajax({
            type: "POST",
            url: url,
            async: true,
            data: JSON.stringify(param),
            contentType: "application/json; charset=utf-8",
            success: function (result) {
                if (result.retCode == "0000") {
                    $('#vanoId').text(result.AcNum);
                    swal({
                        type: 'success',
                        text: '조회완료되었습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }else {
                    swal({
                        type: 'warning',
                        text: '시스템 오류입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            }
        });

    }


    function fn_csvResultDelete() {

        var url = "/sys/vanoMgmt/csvResultDelete";

        var param = {
            searchStartday: "2022011",
        };

        $.ajax({
            type: "POST",
            url: url,
            async: true,
            data: JSON.stringify(param),
            contentType: "application/json; charset=utf-8",
            success: function (result) {
                if (result.retCode == "0000") {

                    $('#vanoId').text(result.AcNum);
                    swal({
                        type: 'success',
                        text: '삭제완료하였습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })

                }else {
                    swal({
                        type: 'warning',
                        text: '시스템 오류입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            },
        });

    }

    function fn_csvResultInsert() {

        var url = "/sys/vanoMgmt/csvResultInsert";

        var param = {
            searchStartday: "2022011",
        };

        $.ajax({
            type: "POST",
            url: url,
            async: true,
            data: JSON.stringify(param),
            contentType: "application/json; charset=utf-8",
            success: function (result) {
                if (result.retCode == "0000") {
                    $('#vanoId').text(result.AcNum);
                    swal({
                        type: 'success',
                        text: '등록을 완료하였습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }else if(result.retCode == "9998"){

                    swal({
                        type: 'warning',
                        text: '업로드 성공 가상계좌 수와 파일의 가상계좌가 다릅니다. (시스템이나, 파일에 문제가 있을 수 있습니다.) 관리자에게 문의하세요.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })


                }else {
                    swal({
                        type: 'warning',
                        text: '시스템 오류입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            }
        });

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
</script>

<div class="modal fade" id="popup-virtual-account-regist" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title">가상계좌등록</h5>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="container-fluid">
                        <div class="row form-group">
                            <label class="control-label col-sm-4 col-xs-12">은행코드 선택</label>
                            <div class="col-sm-8 col-xs-12">
                                <div class="radio radio-primary radio-inline">
                                    <input type="radio" id="normal" name="radio" value="20008155" checked>
                                    <label for="normal"> 일반(20008155) </label>
                                </div>
                                <div class="radio radio-primary radio-inline">
                                    <input type="radio" id="highway" name="radio" value="20008153">
                                    <label for="highway"> 고속도로(20008153) </label>
                                </div>
                            </div>
                        </div>
                        <div class="row form-group">
                            <label class="control-label col-sm-4 col-xs-12">1. 등록파일 업로드</label>
                            <form class="form-horizontal" id="fileForm" name="fileForm" enctype="multipart/form-data" method="post">
                                <div class="col-sm-8 col-xs-12">
                                    <div class="form-inline">
                                        <div class="input-group">
                                            <input type="file" id="uploadfile" name="uploadfile" class="hidden" onchange="fn_fileChk();">
                                            <input type="text" class="form-control" disabled="disabled" id="filename" value="">
                                        </div>
                                        <label class="btn btn-sm btn-w-m btn btn-primary" for="uploadfile">파일찾기</label>
                                        <button type="button" class="btn btn-primary" onclick="fn_fileUpload();"><i class="fa fa-fw fa-cloud-upload"></i>등록</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="row form-group">
                            <label class="control-label col-sm-4 col-xs-12">2. 업로드 가상계좌 조회</label>
                            <div class="col-sm-8 col-xs-12">
                            <span>업로드 된 가상계좌 수: </span><span id="vanoId" name="vanoId" class="input-sm form-control"></span>
                                <button class="btn btn-primary" type="button" onclick="fn_csvResult();">조회</button>
                                <button class="btn btn-primary" type="button" onclick="fn_csvResultDelete();">업로드한 가상계좌 삭제</button>

                            </div>

                        </div>

                        <div class="row form-group">
                            <label class="control-label col-sm-4 col-xs-12">3. 업로드 가상계좌 적용</label>
                            <button class="btn btn-primary" type="button" onclick="fn_csvResultInsert();">업로드 가상계좌 운영 적용</button>
                        </div>

                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
            </div>
        </div>
    </div>
</div>

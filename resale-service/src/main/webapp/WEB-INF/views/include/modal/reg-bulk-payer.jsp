<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">

    // 대량 등록 실패 내역
    function fn_custFailList(num) {
        moPage = '';
        if (num == null || num == 'undefined') {
            moPage = '1';
        } else {
            moPage = num;
        }

        var url = "/org/custMgmt/selectCustFail";
        var param = {
            curPage: moPage
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                // 대량등록 실패 내역
                fnFailGrid(data, 'failResultBody');
                ajaxModalPaging(data, 'ModalExcelPageArea'); //modal paging
                $('#fileName').val('');
                $('#upload-bulk-payer').val('');
            }
        });
    }

    // 청구항목
    function claimItemCombo(data, obj) {
        var str = '';
        $.each(result.failList, function (k, v) {
            str += '<option value="' + v.code + '" >' + v.codeName + '</option>';
        });
        $('#' + obj).html(str);
    }

    //데이터 새로고침
    function fnFailGrid(result, obj) {
        var str = '';
        if (result.count <= 0) {
            str += '<tr><td colspan="6" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.failList, function (k, v) {
                str += '<tr>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + nullValueChange(v.xRow) + '</td>';
                str += '<td>' + basicEscape(nullValueChange(v.cusName)) + '</td>';
                str += '<td>' + nullValueChange(v.cusKey) + '</td>';
                str += '<td>' + nullValueChange(v.cusHp) + '</td>';
                //str +=  '<td>'+ nullValueChange(v.cusGubn1) +'</td>';
                //str +=  '<td>'+ nullValueChange(v.cusGubn2) +'</td>';
                str += '<td>' + nullValueChange(v.result) + '</td>';
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }


    // 납부고객 대량 등록
    function fn_addCustExcel() {
        var file = $("#upload-bulk-payer").val();
        if (file == "" || file == null) {
            swal({
                type: 'info',
                text: "파일을 선택 해주세요!",
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
                text: "고객등록 대상을 추가 하시겠습니까?",
                type: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소'
            }).then(function (result) {
                if (result.value) {

                    $('.ajax-loader-area').css('display', 'block');

                    var url = "/org/custMgmt/excelUpload";
                    var formData = new FormData();
                    formData.append("file", $('#upload-bulk-payer')[0].files[0]);

                    $.ajax({
                        type: "post",
                        url: url,
                        data: formData,
                        //dataType: "text",
                        processData: false,
                        contentType: false,
                        success: function (data) {
                            $('.ajax-loader-area').css('display', 'none');
                            var msg = "";
//       						if(data.failCnt) {
//       							msg = "<p>등록 실패된 [" + data.failCnt + "건]을 제외한, 고객의 대상 추가를 완료했습니다.";
//       						}
                            if (data.resCode == "0000") {
                                swal({
                                    title: '고객대상 목록에 추가되었습니다.',
                                    html: '목록 확인 후 반드시 하단의 [고객등록] 버튼을 눌러야 완료됩니다.',
                                    type: 'success',
                                    showCancelButton: true,
                                    confirmButtonColor: '#3085d6',
                                    cancelButtonColor: '#d33',
                                    confirmButtonText: '고객목록확인',
                                    cancelButtonText: '실패내역확인'
                                }).then(function (result) {
                                    if (result.value) {
                                        // 확인 - 청구대상목록으로 이동
                                        location.href = "/org/custMgmt/custReg";
                                    } else {
                                        $('#fileName').val('');
                                        $('#upload-bulk-payer').val('');
                                        //취소 - 현재 페이지에서 대상 등록 실패내역을 조회한다.
                                        fn_custFailList('1');
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
                            });
                        }
                    });
                }
            });
        }
    }

    // 대량 등록 실패건 다운로드
    function fn_excelFailDownload() {
    fncClearTime();
        swal({
            text: "등록 실패건을 다운로드 하시겠습니까?",
            type: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                // 다운로드
                document.excelUploadForm.action = "/org/custMgmt/excelFailDownload";
                document.excelUploadForm.submit();
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

    // 엑셀파일양식 다운로드
    function fn_excelDownload() {
        swal({
            text: "엑셀양식을 다운로드 하시겠습니까?",
            type: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                // 다운로드
                document.downForm.action = "/org/custMgmt/excelDownload";
                document.downForm.submit();
            }
        });
    }

    function progress() {
        $('#progressbar').show();
        var val = $('#progressbar').progressbar("value");
        $('#progressbar').progressbar("value", val + 1);
        if (val < 100) {
            setTimeout(progress, 100);
        } else if (val == 100) {
            $('#progressbar').hide();
        }
    }

</script>

<form id="downForm" name="downForm" method="post"></form>

<div class="modal fade" id="reg-bulk-payer" tabindex="-1" role="dialog" aria-labelledby="reg-bulk-payer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">고객등록 - 엑셀파일 대량등록</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="fnSearch();"><span aria-hidden="true">&times;</span></button>
            </div>

            <div class="modal-body">
                <div class="container-fluid">
                    <div id="page-description">
                        <div class="row">
                            <div class="col-12">
                                <p class="mb-1"><strong>등록 안내</strong></p>
                                <p>
                                    ① 우측 [고객등록 엑셀양식]을 다운로드 받아 엑셀파일에 고객 정보 입력
                                    <br/>② [파일찾기]로 고객등록 엑셀파일 업로드
                                    <br/>③ [고객대상 추가] 버튼을 누르면 한 번에 추가
                                </p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="container">
                    <div class="search-box">
                        <div class="row">
                            <div class="col-lg-8 col-md-12">
                                <div class="row">
                                    <div class="col col-md-12 col-sm-12 col-12 form-inline mb-2">
                                        <span>■　　'고객명'은 필수 입력 필요</span>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col col-md-12 col-sm-12 col-12 form-inline mb-2">
                                        <span>■　　현금영수증 자동 발급을 위해 발급정보입력 필요</span>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col col-md-12 col-sm-12 col-12 form-inline mb-2">
                                        <span>■　　문자 발송을 위해서는 휴대폰번호 입력 필수</span>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col col-md-12 col-sm-12 col-12 form-inline mb-2">
                                        <span>■　　이메일 고지발송을 위해 이메일주소 입력 필수</span>
                                    </div>
                                </div>
                            </div>

                            <div class="col-lg-4 col-md-12">
                                <button class="btn btn-excel-form-download-in-modal" onclick="fn_excelDownload();">
                                    <img src="/assets/imgs/common/icon-excel-download.png" class="mb-2">
                                    <span>고객등록<br>엑셀양식</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-12">
                            <h5>고객 대량등록</h5>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <table class="table table-form">
                        <tbody>
                            <tr class="row no-gutters">
                                <th class="col col-2">파일 등록</th>
                                <td class="col col-10 filebox">
                                    <div class="col">
                                        <form id="excelUploadForm" name="excelUploadForm" enctype="multipart/form-data" method="post">
                                            <input class="form-control float-left" style="width: calc(100% - 82px);" value="파일선택" disabled="disabled" id="fileName">
                                            <label class="btn btn-sm btn-d-gray ml-1 mr-1" for="upload-bulk-payer">파일찾기</label>
                                            <input type="file" id="upload-bulk-payer" name="upload-bulk-payer" class="hidden" onchange="document.getElementById('fileName').value = this.value">
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-12 text-center">
                            <button type="button" class="btn btn-primary" onclick="fn_addCustExcel();">고객대상 추가</button>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4 mb-2">
                        <div class="col-6">
                            <h5>고객 대량등록 실패내역</h5>
                        </div>
                        <div class="col-6 text-right">
                            <button class="btn btn-sm btn-d-gray ml-1" onclick="fn_excelFailDownload();">등록 실패건 엑셀다운로드</button>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="table-responsive mb-3">
                        <table class="table table-sm table-hover table-primary">
                            <colgroup>
                                <col width="60">
                                <col width="80">
                                <col width="100">
                                <col width="105">
                                <col width="125">
                                <col width="340">
                            </colgroup>

                            <thead>
                                <tr>
                                    <th>NO</th>
                                    <th>행번호</th>
                                    <th>고객명</th>
                                    <th>고객번호</th>
                                    <th>연락처</th>
                                    <th>실패사유</th>
                                </tr>
                            </thead>

                            <tbody id="failResultBody">
                                <tr>
                                    <td colspan="6" class="text-center">[조회된 내역이 없습니다.]</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <nav aria-label="Page navigation example">
                    <ul class="pagination justify-content-center" id="ModalExcelPageArea">
                    </ul>
                </nav>
            </div>

            <div class="list-button-group-bottom text-center">
                <button type="button" class="btn btn-primary btn-outlined mb-1" data-dismiss="modal" onclick="fnSearch();">닫기</button>
            </div>
        </div>
    </div>
</div>

<div class="ajax-loader-area" style="display: none;">
    <div class="ajax-loader"></div>
    <div class="modal-backdrop fade show"></div>
</div>
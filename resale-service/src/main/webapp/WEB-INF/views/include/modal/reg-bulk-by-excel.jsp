<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    //대량등록
    function fn_regBulkByExcel() {
        fn_reset_scroll();

        $('#upload-bulk-payer-by-exel').val('');
        $('#fileName').val('');

        // 대량등록 실패 내역
        fn_claimFailList(1);

        $('#reg-bulk-by-excel').modal({
            backdrop: 'static',
            keyboard: false
        });

    }

    $(document).ready(function () {
        $("#exStartDate").val(moment(new Date(), 'YYYYMMDD').format("YYYY.MM.DD"));

        // 대량등록 실패 내역
        fn_claimFailList(1);
    });

    // 대량 등록 실패 내역
    function fn_claimFailList(num) {
        moPage = '';
        if (num == null || num == 'undefined') {
            moPage = '1';
        } else {
            moPage = num;
        }

        var url = "/org/claimMgmt/claimRegModalAjax";
        var param = {
            curModalPage: moPage
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                // 대량등록 실패 내역
                fnFailGrid(data, 'resultBodyF');
                ajaxModalPaging(data, 'ModalExcelPageArea'); //modal paging
            }
        });
    }

    //데이터 새로고침
    function fnFailGrid(result, obj) {
        var str = '';
        if (result.failCount <= 0) {
            str += '<tr><td colspan="10" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.failList, function (k, v) {
                str += '<tr>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + nullValueChange(v.masMonth) + '</td>';
                str += '<td>' + basicEscape(nullValueChange(v.cusName)) + '</td>';
                str += '<td>' + nullValueChange(v.cusKey) + '</td>';
                str += '<td>' + nullValueChange(v.payItemName) + '</td>';
                str += '<td style="text-align: right;">' + numberToCommas(nullValueChange(v.xAmt)) + '</td>';
                str += '<td>' + nullValueChange(v.startDate) + '</td>';
                str += '<td>' + nullValueChange(v.endDate) + '</td>';
                str += '<td>' + nullValueChange(v.printDate) + '</td>';
                str += '<td>' + nullValueChange(v.result) + '</td>';
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    // 엑셀파일양식 다운로드
    function fn_excelDownload() {
        fncClearTime();
    	var alertResult = false;
    	
        if ($("#exMonthCheck").is(":checked")) { // 청구월 check 여부
            var year = $("#exClaimYear").val();
            var month = $("#exClaimMonth").val();
            if (!year || !month) {
                swal({
                    type: 'info',
                    text: "청구월을 지정해 주세요!",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }
        if ($("#exInsPeriod").is(":checked")) { // 납부기한적용 check 여부
            var stDt = $("#exStartDate").val().replace(/\./gi, "");
            var edDt = $("#exEndDate").val().replace(/\./gi, "");
            if (!stDt || !edDt || stDt.length < 8 || edDt.length < 8 || stDt > edDt) {
                swal({
                    type: 'info',
                    text: "납부기한적용을 확인해 주세요!",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
            var nowDt = getCurrentDate();
            if (stDt < nowDt) {
                swal({
                    type: 'info',
                    text: '입금가능시작일은 금일부터 가능합니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }
        if ($("#exPrintCheck").is(":checked")) { // 고지서용표시마감일 check 여부
            var prDt = $("#exPrintDate").val().replace(/\./gi, "");
            if (!prDt || prDt.length < 8) {
                swal({
                    type: 'info',
                    text: "고지서용표시마감일을 확인해 주세요!",
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
            if ((stDt > prDt) || (prDt > edDt)) {
                swal({
                    type: 'info',
                    text: '고지서용 표시마감일은 납부기한보다 작거나 클 수 없습니다.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }
        if (!$('#exCus').is(':checked') && !$('#exBeforeCus').is(':checked')) {
            swal({
                type: 'info',
                text: "고객정보 불러오기를 선택해 주세요!",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        swal({
            text: "엑셀양식을 다운로드 하시겠습니까?",
            type: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true,
            onAfterClose: function() {
	            if(alertResult) {
	            	fn_claimRegformFile();
		        } 
	        }
        }).then(function (result) {
            if (result.value) {
            	alertResult = true;
            }
        });
    }
    //청구 대량등록양식 엑셀생성
    function fn_claimRegformFile() {
        $('#monthCheck').val($("#exMonthCheck").is(":checked"));
        $('#excelClaimYear').val($("#exClaimYear").val());
        $('#excelClaimMonth').val($("#exClaimMonth").val());
        $('#insPeriod').val($("#exInsPeriod").is(":checked"));
        $('#excelStartDate').val($("#exStartDate").val().replace(/\./gi, ""));
        $('#excelEndDate').val($("#exEndDate").val().replace(/\./gi, ""));
        $('#printCheck').val($("#exPrintCheck").is(":checked"));
        $('#printDate').val($("#exPrintDate").val().replace(/\./gi, ""));
        if ($('#exCus').is(':checked')) { // 고객정보 불러오기 : 전체고객 또는 전월 청구고객
            $('#selCusCk').val('all');
        } else if ($('#exBeforeCus').is(':checked')) { //전월고객
            $('#selCusCk').val('month');
        }
        // 다운로드
        $.ajax({
            type: "POST",
            url: "/org/claimMgmt/excelDownload",
            data: $("#downForm").serialize(),
            xhrFields: {
                responseType: 'blob' // to avoid binary data being mangled on charset conversion
            },
            success: function(blob, status, xhr) {
                downloadExcelAjax(blob, status, xhr);
            }
        });
    }

    // 청구 대량 등록
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

    //대량 등록 실패건 다운로드
    function fn_excelFailDownload() {
        fncClearTime();
    	var alertResult = false;
        swal({
            text: "등록 실패건을 다운로드 하시겠습니까?",
            type: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true,
            onAfterClose: function() {
	            if(alertResult) {
	            	fnExcelFailFile();
		        } 
	        }
        }).then(function (result) {
            if (result.value) {
            	alertResult = true;
            }
        });
    }
    // 대량등록 실패건 엑셀생성
    function fnExcelFailFile() {
        document.excelUploadForm.action = "/org/claimMgmt/excelFailDownload";
        document.excelUploadForm.submit();
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

<jsp:include page="/WEB-INF/views/include/popHeader.jsp"/>

<form id="downForm" name="downForm" method="post">
    <input type="hidden" id="monthCheck" name="monthCheck">
    <input type="hidden" id="excelClaimYear" name="excelClaimYear">
    <input type="hidden" id="excelClaimMonth" name="excelClaimMonth">
    <input type="hidden" id="insPeriod" name="insPeriod">
    <input type="hidden" id="excelStartDate" name="excelStartDate">
    <input type="hidden" id="excelEndDate" name="excelEndDate">
    <input type="hidden" id="printCheck" name="printCheck">
    <input type="hidden" id="printDate" name="printDate">
    <input type="hidden" id="selCusCk" name="selCusCk">
</form>

<div class="modal fade" id="reg-bulk-by-excel" tabindex="-1" role="dialog" aria-labelledby="regPayer"
     aria-hidden="true">

    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">엑셀파일 대량등록</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"
                        onclick="fn_ClaimListCall(); overFlowAuto();">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div id="page-description">
                        <div class="row">
                            <div class="col-12">
                                <p class="mb-1"><strong>등록 안내</strong></p>
                                <p>
                                    ① 청구등록 엑셀양식 설정 : 청구등록 엑셀양식 설정 후, “청구등록 엑셀양식” 버튼을 클릭하여 다운로드 및 청구내역 입력
                                    <br/>② 청구 파일등록의 [파일찾기] 클릭 하여 파일 업로드
                                    <br/>③ 청구대상 추가 버튼
                                </p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-3">
                        <div class="col-6">
                            <h5>청구등록 엑셀양식 설정</h5>
                        </div>
                        <div class="col-6 text-right">
                            <span class="text-danger">* 필수입력</span>
                        </div>
                    </div>
                </div>

                <div class="container">
                    <div class="search-box">
                        <div class="row">
                            <div class="col-lg-8 col-md-12">
                                <div class="row">
                                    <div class="col col-md-4 col-sm-12 col-12 form-inline mb-2">
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="checkbox" name="exMonthCheck"
                                                   id="exMonthCheck">
                                            <label class="form-check-label" for="exMonthCheck">
                                                <span class="mr-1"></span>청구월</label>
                                        </div>
                                    </div>
                                    <div class="col col-md-8 col-sm-12 col-12 form-inline year-month-dropdown mb-2">
                                        <select class="form-control" name="exClaimYear" id="exClaimYear">
                                            <option value="">선택</option>
                                        </select>
                                        <span class="ml-1 mr-1">년</span>
                                        <select class="form-control" name="exClaimMonth" id="exClaimMonth">
                                            <option value="">선택</option>
                                        </select>
                                        <span class="ml-1 mr-auto">월</span>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col col-md-4 col-sm-12 col-12 form-inline mb-2">
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="checkbox" name="exInsPeriod"
                                                   id="exInsPeriod">
                                            <label class="form-check-label" for="exInsPeriod">
                                                <span class="mr-1"></span>납부기한</label>
                                        </div>
                                    </div>
                                    <div class="col col-md-8 col-sm-12 col-12 form-inline mb-2">
                                        <div class="date-input">
                                            <label class="sr-only" for="inlineFormInputGroupUsername">From</label>
                                            <div class="input-group">
                                                <input type="text" class="form-control date-picker-input"
                                                       aria-label="From" aria-describedby="basic-addon2"
                                                       id="exStartDate" name="exStartDate" maxlength="8" onkeydown="onlyNumber(this)">
                                            </div>
                                        </div>
                                        <span class="range-mark"> ~ </span>
                                        <div class="date-input">
                                            <label class="sr-only" for="inlineFormInputGroupUsername">To</label>
                                            <div class="input-group">
                                                <input type="text" class="form-control date-picker-input"
                                                       placeholder="YYYY.MM.DD" aria-label="To"
                                                       aria-describedby="basic-addon2" id="exEndDate" name="exEndDate"
                                                       maxlength="8" onkeydown="onlyNumber(this)">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col col-md-4 col-sm-12 col-12 form-inline mb-2">
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="checkbox" name="exPrintCheck"
                                                   id="exPrintCheck">
                                            <label class="form-check-label" for="exPrintCheck">
                                                <span class="mr-1"></span>고지서용표시마감일</label>
                                        </div>
                                    </div>
                                    <div class="col col-md-8 col-sm-12 col-12 form-inline mb-2">
                                        <div class="date-input">
                                            <label class="sr-only" for="inlineFormInputGroupUsername">From</label>
                                            <div class="input-group">
                                                <input type="text" class="form-control date-picker-input"
                                                       placeholder="YYYY.MM.DD" aria-label="From"
                                                       aria-describedby="basic-addon2" id="exPrintDate"
                                                       name="exPrintDate" maxlength="8" onkeydown="onlyNumber(this)">
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col col-md-4 col-sm-12 col-12 form-inline mb-2">
                                        <div class="form-check form-check-inline">
                                            <span class="text-danger">*</span> 고객정보 불러오기</label>
                                            <a tabindex="0" class="popover-dismiss" role="button" data-toggle="popover"
                                               data-trigger="focus" style="margin-left: 2px;"
                                               title="고객정보 불러오기란?"
                                               data-content="보다 편리한 대량등록 기능을 제공하기 위해 선택 조건의 고객정보를 사전 입력해 양식 제공">
                                                <img src="/assets/imgs/common/icon-info.png">
                                            </a>
                                        </div>
                                    </div>
                                    <div class="col col-md-4 col-sm-12 col-12 form-inline mb-2">
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="raditOptions" id="exCus"
                                                   value="M" checked>
                                            <label class="form-check-label" for="exCus"><span></span>전체고객</label>
                                        </div>
                                    </div>
                                    <div class="col col-md-4 col-sm-12 col-12 form-inline mb-2">
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="raditOptions"
                                                   id="exBeforeCus" value="D">
                                            <label class="form-check-label" for="exBeforeCus"><span></span>전월
                                                청구고객</label>
                                        </div>
                                    </div>
                                </div>

                            </div>

                            <div class="col-lg-4 col-md-12">
                                <button class="btn btn-excel-form-download-in-modal" onclick="fn_excelDownload();">
                                    <img src="/assets/imgs/common/icon-excel-download.png" class="mb-2">
                                    <span>청구등록<br>엑셀양식</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-4">
                            <h5>청구 파일등록</h5>
                        </div>
                    </div>
                </div>

                <div class="pd-n-mg-o">
                    <form id="excelUploadForm" name="excelUploadForm" enctype="multipart/form-data" method="post">
                        <table class="table table-form">
                            <tbody class="container-fluid">
                            <tr class="row no-gutters">
                                <th class="col col-2">대량 등록</th>
                                <td class="col col-10 filebox">
                                    <input type="text" class="form-control col" disabled="disabled" id="fileName">
                                    <label class="btn btn-sm btn-d-gray ml-1"
                                           for="upload-bulk-payer-by-exel">파일찾기</label>
                                    <input type="file" id="upload-bulk-payer-by-exel" name="upload-bulk-payer-by-exel"
                                           class="hidden"
                                           onchange="document.getElementById('fileName').value = this.value">
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4">
                        <div class="col-12 text-center">
                            <button type="button" class="btn btn-primary" onclick="fn_addCustExcel();">청구대상 추가</button>
                        </div>
                    </div>
                </div>

                <div class="container-fluid">
                    <div class="row no-gutters mt-4 mb-2">
                        <div class="col col-sm-6 col-12">
                            <h5>청구 대량등록 실패내역</h5>
                        </div>
                        <div class="col col-sm-6 col-12 text-right">
                            <button class="btn btn-sm btn-d-gray ml-1" onclick="fn_excelFailDownload();">등록 실패건 엑셀다운로드
                            </button>
                        </div>
                    </div>
                </div>
                <div class="container-fluid">
                    <div class="table-responsive mb-3">
                        <table class="table table-sm table-hover table-primary">
                            <colgroup>
                                <col width="60">
                                <col width="100">
                                <col width="100">
                                <col width="140">
                                <col width="120">
                                <col width="100">
                                <col width="100">
                                <col width="100">
                                <col width="120">
                                <col width="150">
                            </colgroup>

                            <thead>
                            <tr>
                                <th>NO</th>
                                <th>청구년월</th>
                                <th>고객명</th>
                                <th>가상계좌</th>
                                <th>청구항목</th>
                                <th>청구금액</th>
                                <th>납부시작일</th>
                                <th>납부마감일</th>
                                <th>고지서용<br>표시마감일</th>
                                <th>실패사유</th>
                            </tr>
                            </thead>

                            <tbody id="resultBodyF">
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
                <button type="button" class="btn btn-primary btn-outlined mb-1" data-dismiss="modal"
                        onclick="fn_ClaimListCall(); overFlowAuto();">닫기
                </button>
            </div>
        </div>
    </div>
</div>

<div class="ajax-loader-area" style="display: none;">
    <div class="ajax-loader">
    </div>
    <div class="modal-backdrop fade show">
    </div>
</div>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script>
    var oneDepth = "adm-nav-6";
    var twoDepth = "adm-sub-30";
</script>

<script type="text/javascript">
    function fn_fileChk() {
        var fileNm = $("#upload-file").val();
        $("#fileNm").val(fn_clearFilePath(fileNm));
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

    function fn_fileTransClaim() {
        var idx = 0;

        if (!$('#chaCd').val()) {
            swal({
                type: 'info',
                text: '기관코드를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (!$('#masMonth').val()) {
            swal({
                type: 'info',
                text: '청구월을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var fileNM = $('#fileNm').val();
        if (!fileNM) {
            swal({
                type: 'info',
                text: '파일을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var fileFormat = fileNM.split(".");
        if (fileFormat.indexOf("xlsx") > -1 || fileFormat.indexOf("xls") > -1) {
        } else {
            idx=1;
            return;
        }

        if (idx == 1) {
            swal({
                type: 'info',
                text: "엑셀 파일만 업로드 가능합니다",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else {
            var url = "/sys/addServiceMgmt/excelFileTransClaim";
        }

        if (idx == 0) {
            swal({
                type: 'question',
                html: "변환 하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                reverseButtons: true
            }).then(function (result) {
                if (result.value) {
                    $("#fchaCd").val($("#chaCd").val());
                    $("#fmasMonth").val($("#masMonth").val());
                    document.fileForm.action = "/sys/addServiceMgmt/excelFileTransClaim";
                    document.fileForm.submit();
                }
            });
        }
    }

    function fn_fileTransCust() {
        var idx = 0;

        if (!$('#chaCd').val()) {
            swal({
                type: 'info',
                text: '기관코드를 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var fileNM = $('#fileNm').val();
        if (!fileNM) {
            swal({
                type: 'info',
                text: '파일을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var fileFormat = fileNM.split(".");
        if (fileFormat.indexOf("xlsx") > -1 || fileFormat.indexOf("xls") > -1) {
        } else {
            idx=1;
            return;
        }

        if (idx == 1) {
            swal({
                type: 'info',
                text: "엑셀 파일만 업로드 가능합니다",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else {
            var url = "/sys/addServiceMgmt/excelFileTransCust";
        }

        if (idx == 0) {
            swal({
                type: 'question',
                html: "변환 하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                reverseButtons: true
            }).then(function (result) {
                if (result.value) {
                    $("#fchaCd").val($("#chaCd").val());
                    $("#fmasMonth").val($("#masMonth").val());
                    document.fileForm.action = "/sys/addServiceMgmt/excelFileTransCust";
                    document.fileForm.submit();
                }
            });
        }

    }
</script>

</div> 
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>엑셀파일변환</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>부가서비스관리</a>
            </li>
            <li class="active">
                <strong>엑셀파일변환</strong>
            </li>
        </ol>
        <p class="page-description">이용기관의 엑셀파일을 변환해주는 화면입니다.</p>
    </div>
    <div class="col-lg-2"></div>
</div>

<div class="wrapper-content">
    <div class="animated fadeInRight article">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>파일변환</h5>
                    </div>
                    <div class="ibox-content">
                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chaCd" id="chaCd" maxlength="50">
                                            <span class="input-group-btn">
                                                <button class="btn btn-sm btn-w-m btn btn-primary no-margins btn-lookup-collecter" type="button">기관검색</button>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label block">청구월</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group date">
                                            <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                            <input type="text" class="form-control" value="" id="masMonth" name="masMonth" autocomplete="off" />
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">업로드파일</label>
                                    <form class="form-horizontal" id="fileForm" name="fileForm" enctype="multipart/form-data" method="post">
                                        <div class="form-group-sm">
                                                <div class="input-group">
                                                    <input type="file" id="upload-file" name="upload-file" class="hidden" onchange="fn_fileChk();">
                                                    <input type="text" class="form-control" disabled="disabled" id="fileNm">
                                                    <input type="hidden" class="form-control" id="fchaCd" name="chaCd">
                                                    <input type="hidden" class="form-control" id="fmasMonth" name="masMonth">
                                                <div class="input-group-btn">
                                                    <label class="btn btn-sm btn-w-m btn btn-primary no-margins" for="upload-file">파일찾기</label>
                                                </div>
                                                </div>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-md-6">
                                </div>
                            </div>

                            <hr>
                            <div class="col-md-6">
                            <div class="text-center">
                                <button class="btn btn-primary" type="button" onclick="fn_fileTransCust();">고객변환</button>
                            </div>
                            </div>
                            <div class="col-md-6">
                            <div class="text-center">
                                <button class="btn btn-primary" type="button" onclick="fn_fileTransClaim();">청구변환</button>
                            </div>
                            </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="animated fadeInRight" id="focus">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox">
                    <div class="ibox-title">
                        <div class="col-lg-6">

                        </div>
                        <div class="col-lg-6">

                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">

                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>

<script type="text/javascript">
    $(document).ready(function () {
        $(".btn-lookup-collecter").click(function () {
            $("#lookup-collecter-popup").modal({
                backdrop: 'static',
                keyboard: false
            });
            $("#popChacd").val($('#chaCd').val());
            $("#popChaname").val('');
            $('#totCntLookupCollector').text(0);
            $("#ResultBodyCollector").html('');
            $("#ModalPageAreaCollector").html('');
        });

        $('#masMonth').datepicker({
            keyboardNavigation: false,
            format: 'yyyymm',
            forceParse: false,
            autoclose: true,
            minViewMode: 1
        });
    });
</script>

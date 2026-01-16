<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<script>
    var oneDepth = "adm-nav-5";
    var twoDepth = "adm-sub-15";
</script>

<script>
    //숫자만 가능하도록
    function removeChar(event) {
        event = event || window.event;
        var keyID = (event.which) ? event.which : event.keyCode;
        if (keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39)
            return;
        else
            event.target.value = event.target.value.replace(/[^0-9]/g, "");
    }

    //조회
    function search() {
        var usedate = $("#useDate").val().replace(/\./gi, "");
        var issueSt = "";
        if ($("#inlineCheckbox1-1").prop("checked")) {
            issueSt = "all";
        } else {
            issueSt = $("input[name='inlineCheckbox1']:checked").val();
        }
        var pageScale = $("#pageScale").val();
        var searchOrderBy = $("#searchOrderBy").val();

        var param = {
            useDate: usedate,
            issueSt: issueSt,
            comCd: $("#comCd").val(),
            comNm: $("#comNm").val(),
            curPage: "1",
            pageScale: pageScale,
            searchOrderBy: searchOrderBy
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/rcpMgmt/invoiceSearch",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'reSearchbody');
                    sysajaxPaging(result, 'PageArea');
                } else {
                    swal({
                        type: 'success',
                        text: '시스템 오류입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            }
        });
    }

    //페이지 변경
    function list(page) {
        var usedate = $("#useDate").val().replace(/\./gi, "");
        var issueSt = "";
        if ($("#inlineCheckbox1-1").prop("checked")) {
            issueSt = "all";
        } else {
            issueSt = $("input[name='inlineCheckbox1']:checked").val();
        }
        var pageScale = $("#pageScale").val();
        var searchOrderBy = $("#searchOrderBy").val();

        var param = {
            useDate: usedate,
            issueSt: issueSt,
            comCd: $("#comCd").val(),
            comNm: $("#comNm").val(),
            curPage: page,
            pageScale: pageScale,
            searchOrderBy: searchOrderBy
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/rcpMgmt/invoiceSearch",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'reSearchbody');
                    sysajaxPaging(result, 'PageArea');
                } else {
                    swal({
                        type: 'success',
                        text: '시스템 오류입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            }
        });

    }

    // ajax 값 grid로
    function fnGrid(result, obj) {
        var str = '';
        if (result.count <= 0) {
            str += '<tr><td colspan="7" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr id=' + v.rn + '>';
                str += '<td>' + v.useDay + '</td>';
                str += '<td>' + v.comCd + '</td>';
                str += '<td>' + v.comNm + '</td>';
                str += '<td>' + v.price + '</td>';
                str += '<td>' + v.tax + '</td>';
                str += '<td>' + v.email + '</td>';
                if (v.issueSt == '발행') {
                    str += '<td class="text-success">' + v.issueSt + '</td>';
                } else {
                    str += '<td class="text-danger">' + v.issueSt + '</td>';
                }
                str += '<td>' + v.issueNm + '</td>';
                str += '<td>' + v.issueDay + '</td>';
                str += '</tr>';
            });
        }
        $("#" + obj).html(str);
        $("#dcount").text(result.dcount);
        $("#acount").text(result.acount);
    }

    //파일저장
    function fn_fileSave() {
        /* 	if($('#totcount').text() <= 0){
                swal({
                   type: 'info',
                   text: '다운로드할 데이터가 없습니다.',
                   confirmButtonColor: '#3085d6',
                   confirmButtonText: '확인'
                });
                return;
            } */
        swal({
            type: 'question',
            html: "다운로드 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                var useday = $("#useDate").val().replace(/\./gi, "");
                var issueSt = "";
                if ($("#inlineCheckbox1-1").prop("checked")) {
                    issueSt = "all";
                } else {
                    issueSt = $("input[name='inlineCheckbox1']:checked").val();
                }
                $('#fuseDate').val(useday);
                $('#fissueSt').val(issueSt);
                $('#fcomCd').val($("#comCd").val());
                $('#fcomNm').val($("#comNm").val());
                $('#fsearchOrderBy').val($("#searchOrderBy").val());
                // 다운로드
                $('#fileForm').submit();
            }
        });
    }

    //파일업로드
    function fn_fileUpload() {

        var idx = 0;
        var fileName = $('#fileName').val();
        //영어 숫자 특수문자만 가능
        var pattern = /[^(a-zA-Z0-9.`~!@#$%^&*|\\\'\";:\/?\_\-)]/gi;
        if (pattern.test(fileName)) {
            swal({
                type: 'info',
                text: "파일명은 영문,숫자만 허용합니다.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            fileName.value = '';
            return false;
        }
        if (!fileName) {
            swal({
                type: 'info',
                text: '파일을 선택해 주세요.',
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
                cancelButtonText: '취소'
            }).then(function (result) {
                if (result.value) {
                    var formData = new FormData();
                    formData.append("file", $('#uploadFile')[0].files[0]);
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
                                $('#uploadFile').val('');
                                $('#fileName').val('');
                                fn_search();
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

    function fn_fileChk() {
        var fileNm = fn_clearFilePath($("#uploadFile").val());            // 파일 명
        $('#fileName').val(fileNm);
    }
</script>

<form id="fileForm" name="fileForm" method="post" action="/sys/rcpMgmt/invoiceexcelDown">
    <input type="hidden" id="fuseDate" name="useDate" value=""/>
    <input type="hidden" id="fissueSt" name="issueSt" value=""/>
    <input type="hidden" id="fcomCd" name="comCd" value=""/>
    <input type="hidden" id="fcomNm" name="comNm" value=""/>
    <input type="hidden" id="fsearchOrderBy" name="searchOrderBy" value=""/>
</form>
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>세금계산서발행내역</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>정산관리</a>
            </li>
            <li class="active">
                <strong>세금계산서발행내역</strong>
            </li>
        </ol>
        <p class="page-description">수수료 출금을 위한 세금계산서 정보 다운로드 및 발행 결과를 관리하는 화면입니다.</p>
    </div>
</div>

<div class="wrapper-content">
    <div class="animated fadeInRight article">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>결과파일 업로드</h5>
                    </div>

                    <div class="ibox-content">
                        <form>
                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">파일업로드</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" disabled="disabled" id="fileName" style="height: 34px;">
                                            <span class="input-group-btn">
                                                <input type="file" id="uploadFile" name="uploadFile" class="hidden" onchange="fn_fileChk()">
                                                <label class="btn btn-primary btn-lookup-collecter no-margins" for="uploadFile">파일찾기</label>
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6 text-right">
                                    <button type="button" class="btn btn-primary" onclick="fn_fileUpload()"><i class="fa fa-fw fa-cloud-upload"></i> 등록</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="animated fadeInRight article">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>결과파일 업로드</h5>
                    </div>

                    <div class="ibox-content">
                        <form>
                            <div class="row">
                                <div class="col-md-2">
                                    <label class="form-label block">사용월</label>
                                    <div class="input-group date">
                                        <input type="text" class="form-control input-sm" id="useDate" readonly="readonly"><span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    </div>
                                </div>

                                <div class="col-md-4"></div>

                                <div class="col-md-6">
                                    <label class="form-label block">발행상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-1" value="1">
                                            <label for="inlineCheckbox1-1"> 전체 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-2" name="inlineCheckbox1" value="2">
                                            <label for="inlineCheckbox1-2"> 발행 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-3" name="inlineCheckbox1" value="3">
                                            <label for="inlineCheckbox1-3"> 미발행 </label>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">사용월</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control" onkeyup="removeChar(event)" id="comCd">
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">기관명</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control" id="comNm">
                                    </div>
                                </div>
                            </div>

                            <hr>

                            <div class="text-center">
                                <button class="btn btn-primary" onclick="search()">조회</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="animated fadeInRight">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox">
                    <div class="ibox-title">
                        <div class="col-lg-6">
                            <span class="m-r-sm m-l-sm">불능 건수 : <strong class="text-success" id="dcount"></strong> 건</span>
                            <span> | </span>
                            <span class="m-l-sm m-r-sm">전체 건수 : <strong class="text-success" id="acount"></strong> 건</span>
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control">
                                <option>기관명 순</option>
                                <option></option>
                            </select>
                            <select class="form-control" id="pageScale">
                                <option value="10">10개씩 조회</option>
                                <option value="20">20개씩 조회</option>
                                <option value="50">50개씩 조회</option>
                                <option value="100">100개씩 조회</option>
                                <option value="200">200개씩 조회</option>
                            </select>
                            <button class="btn btn-md btn-primary" type="button" onclick="fn_fileSave();">파일저장</button>
                            <button type="button" class="btn btn-primary">거래처등록 다운로드</button>
                            <button type="button" class="btn btn-primary">부가세전표 다운로드</button>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center">
                                <colgroup>
                                    <col width="120">
                                    <col width="120">
                                    <col width="*">
                                    <col width="120">
                                    <col width="150">
                                    <col width="180">
                                    <col width="120">
                                    <col width="150">
                                    <col width="150">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>사용월</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>공급가액</th>
                                        <th>부가세</th>
                                        <th>E-MAIL주소</th>
                                        <th>발행상태</th>
                                        <th>승인번호</th>
                                        <th>발행일자</th>
                                    </tr>
                                </thead>

                                <tbody id="reSearchbody">
                                    <c:choose>
                                        <c:when test="${map.list.size() > 0}">
                                            <c:forEach var="row" items="${map.list}" varStatus="status">
                                                <tr>
                                                    <td>${row.useDay}</td>
                                                    <td>${row.comCd}</td>
                                                    <td>${row.comNm}</td>
                                                    <td>${row.price}</td>
                                                    <td>${row.tax}</td>
                                                    <td>${row.email}</td>
                                                    <c:choose>
                                                        <c:when test="${row.issueSt == '발행'}">
                                                            <td class="text-success">${row.issueSt}</td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td class="text-danger">${row.issueSt}</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td>${row.issueNm}</td>
                                                    <td>${row.issueDay}</td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="7" style="text-align: center;">[조회된 내역이없습니다.]</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>

                        <jsp:include page="/WEB-INF/views/include/sysPaging.jsp" flush="false"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<script>
    $(document).ready(function () {
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1;
        var yyyy = today.getFullYear();
        $("#useDate").val(yyyy + '.' + mm + '.' + dd);
        $("#inlineCheckbox1-1").prop("checked", true);
        search();
    });

    //checkbox 전체 선택 fn
    $(document).on("change", "input[id='inlineCheckbox1-1']", function () {
        if ($("#inlineCheckbox1-1").prop("checked")) {
            $("input[name='inlineCheckbox1']").prop("checked", true);
        } else {
            $("input[name='inlineCheckbox1']").prop("checked", false);
        }
    });
    $(document).on("change", "input[name='inlineCheckbox1']", function () {
        $("#inlineCheckbox1-1").prop("checked", false);
    });

    $('.input-group.date').datepicker({
        todayBtn: "linked",
        keyboardNavigation: false,
        format: 'yyyy.mm.dd',
        maxDate: "+0d",
        forceParse: false,
        calendarWeeks: true,
        autoclose: true
    });

    /* $('.btn-open-scheduled-transfer-modify').click(function(){
        $("#popup-scheduled-transfer-modify").modal({
            backdrop : 'static',
            keyboard : false
        });
    }); */
</script>

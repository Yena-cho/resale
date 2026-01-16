<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- session -->
<s:authentication property="principal.name" var="name"/> <!-- 사용자이름 -->
<s:authentication property="principal.email" var="email"/> <!-- 이메일 -->
<s:authentication property="principal.loginId" var="loginId"/> <!-- 로그인ID -->
<!--/ session -->

<script>
    var oneDepth = "adm-nav-4";
    var twoDepth = "adm-sub-09";
</script>


<script>
    //취소버튼
    function cancellBtn() {
        location.href = "/sys/popupSetting";
    }

    //팝업등록
    function popupReg() {
        swal({
            type: 'question',
            html: '팝업을 등록하시겠습니까?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                var startday = $("#startday").val().replace(/\./gi, "");
                var endday = $("#endday").val().replace(/\./gi, "");

                var now = new Date();
                var today = now.getFullYear() + addZero(now.getMonth() + 1) + addZero(now.getDate());

                var fileName = $('#filename').val();
                var idx = fileName.lastIndexOf('\\') + 1;
                var fileNameCheck = fileName.substring(idx, fileName.length);

                if (fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length) != 'jpg' && fileName != "") {
                    swal({
                        type: 'error',
                        text: '500px * 350px 사이즈 jpg파일만 업로드 할 수 있습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                    return;
                }

                //영어 숫자 특수문자만 가능
                var pattern = /[^(a-zA-Z0-9.`~!@#$%^&*|\\\'\";:\/?\_\-)]/gi;
                if ($("#title").val() == "") {
                    swal({
                        type: 'info',
                        text: '팝업명을 입력해 주세요.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $("#title").focus();
                    });
                    return;
                }
                if (pattern.test(fileNameCheck)) {
                    swal({
                        type: 'info',
                        text: '파일명은 영문,숫자만 허용합니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $("#title").focus();
                    });
                    return;
                }
                if ($("#inlineCheckbox1-1").is(":checked") == false && $("#inlineCheckbox1-2").is(":checked") == false) {
                    swal({
                        type: 'info',
                        text: '노출 기간을 설정해주세요.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                    return;
                }
                if ($("#inlineCheckbox2-1").is(":checked") == false && $("#inlineCheckbox2-2").is(":checked") == false) {
                    swal({
                        type: 'info',
                        text: '노출여부를 설정해주세요.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                    return;
                }
                if ($("#contents").val() == "") {
                    swal({
                        type: 'info',
                        text: '팝업 내용을 입력해 주세요.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function () {
                        $("#contents").focus();
                    });
                    return;
                }
                if ($("#file-upload").val() == "") {
                    swal({
                        type: 'info',
                        text: '팝업 이미지를 등록해 주세요.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                    return;
                }
                if (startday > endday && endday != '' && endday != null) {
                    swal({
                        type: 'info',
                        text: '시작일이 마감일보다 큽니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                    return;
                }
                if (startday < today && endday < today && endday != '' && endday != null) {
                    swal({
                        type: 'info',
                        text: '과거일자를 지정할 수 없습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                    return;
                }
                if ($("#inlineCheckbox1-2").is(":checked")) {
                    if ($("#startday").val() == null || $("#startday").val() == "" || $("#startday").val() == undefined) {
                        swal({
                            type: 'info',
                            text: '노출 시작일을 설정해 주세요.',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        })
                        return;
                    } else if ($("#endday").val() == null || $("#endday").val() == "" || $("#endday").val() == undefined) {
                        swal({
                            type: 'info',
                            text: '노출 마감일을 설정해 주세요.',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        })
                        return;
                    }
                }
                if ($("#inlineCheckbox2-1").is(":checked")) {
                    var param = {
                        no: ""
                    }
                    $.ajax({
                        type: "POST",
                        async: true,
                        url: "/sys/exposynCount",
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify(param),
                        success: function (result) {
                            if (result.retCode = "0000") {
                                if (result.exposynCount >= 1) {
                                    swal({
                                        type: 'warning',
                                        title: '노출 설정된 팝업이 존재합니다.',
                                        text: '기존 노출 팝업을 해제후 진행하시겠습니까?',
                                        showCancelButton: true,
                                        confirmButtonText: '확인',
                                        cancelButtonColor: '#d33',
                                        cancelButtonText: '취소'
                                    }).then(function (result) {
                                        if (result.value) {
                                            updateExposeN();
                                            doinsert();
                                        }
                                    })
                                } else {
                                    doinsert();
                                }
                            } else {
                                swal({
                                    type: 'error',
                                    text: result.retMsg,
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                })
                                return;
                            }
                        }
                    })
                } else {
                    doinsert();
                }
            }
        });
    }

    function doinsert() {
        var formData = new FormData($("#fileForm")[0]);
        $.ajax({
            type: "POST",
            url: "/sys/doPopupWrite",
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                if (result.retCode == "0000") {
                    swal({
                        type: 'success',
                        text: '정상적으로 등록 되었습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function (result) {
                        if (result.value) {
                            location.href = "/sys/popupSetting";
                        }
                    })
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

    function updateExposeN() {
        var param = {
            no: ""
        }
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/updateExposeN",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {

                } else {
                    swal({
                        type: 'error',
                        text: '기존팝업 업데이트 중 오류',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            }
        })
    }


    function whatday(no) {
        var settingDate = new Date();

        var month = settingDate.getMonth() + 1;
        var month_tran = settingDate.getMonth() + 1 + no;
        var day = settingDate.getDate();
        var year = settingDate.getFullYear();

        if (month < 10) {
            month = '0' + month;
        }
        if (month_tran < 10) {
            month_tran = '0' + month_tran;
        }
        if (day < 10) {
            day = '0' + day;
        }
        $("#startday").val(year + '.' + month + '.' + day);
        $("#endday").val(year + '.' + month_tran + '.' + day);
        $("#inlineCheckbox1-1").prop("checked", false);
        $("#inlineCheckbox1-2").prop("checked", true);
    }


    function fn_dxpstype(no) {
        if (no == 1) {
            $("#dxpstype").val('C');
        } else {
            $("#dxpstype").val('P');
        }
    }

    function fn_exposyn(no) {
        if (no == 1) {
            $("#exposyn").val('Y');
        } else {
            $("#exposyn").val('N');
        }
    }
</script>

</div>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>팝업 등록</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>게시판관리</a>
            </li>
            <li class="active">
                <strong>팝업 등록</strong>
            </li>
        </ol>
        <p class="page-description">팝업을 등록하는 화면입니다.</p>
    </div>
    <div class="col-lg-2">

    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox">
                <div class="ibox-content">
                    <form id="fileForm" name="fileForm" enctype="multipart/form-data">
                        <table class="table table-stripped">
                            <tbody>
                                <tr>
                                    <th>팝업명</th>
                                    <td>
                                        <input type="text" class="form-control" name="title" id="title" max="12">
                                        <input type="hidden" name="loginId" value="${loginId }">
                                    </td>
                                </tr>
                                <tr>
                                    <th rowspan="2">노출기간</th>
                                    <td>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="inlineCheckbox1-1" name="duration" onclick="fn_dxpstype(1)">
                                            <label for="inlineCheckbox1-1"> 지속적 노출 </label>
                                            <input type="hidden" name="dxpstype" id="dxpstype">
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="form-inline">
                                            <div class="radio radio-primary radio-inline" style="padding-left:5px;">
                                                <input type="radio" id="inlineCheckbox1-2" name="duration" onclick="fn_dxpstype(0)">
                                                <label for="inlineCheckbox1-2" style="padding-left:5px;"> 기간설정 </label>
                                            </div>

                                            <div class="input-daterange input-group" id="datepicker">
                                                <input type="text" class="input-sm form-control" name="start_dt" id="startday" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" name="end_dt" id="endday" readonly="readonly"/>
                                            </div>

                                            <button class="btn btn-sm btn-primary btn-outline" onclick="whatday(1)" type="button">1개월</button>
                                            <button class="btn btn-sm btn-primary btn-outline" onclick="whatday(2)" type="button">2개월</button>
                                            <button class="btn btn-sm btn-primary btn-outline" onclick="whatday(3)" type="button">3개월</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>노출여부</th>
                                    <td>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="inlineCheckbox2-1" name="show" onclick="fn_exposyn(1)">
                                            <label for="inlineCheckbox2-1"> 노출 </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="inlineCheckbox2-2" name="show" onclick="fn_exposyn(0)">
                                            <label for="inlineCheckbox2-2"> 미노출 </label>
                                        </div>
                                        <input type="hidden" id="exposyn" name="exposyn" value="N">
                                    </td>
                                </tr>
                                <tr>
                                    <th>링크URL</th>
                                    <td><input type="text" class="form-control" id="url" name="url"></td>
                                </tr>
                                <tr>
                                    <th>이미지 업로드</th>
                                    <td class="form-inline">
                                        <input type="text" class="form-control" id="filename" readonly="readonly">
                                        <input type="file" class="form-control" id="file-upload" name="filename" style="display: none;" onchange="document.getElementById('filename').value = this.value">
                                        <label for="file-upload" class="btn btn-primary">파일찾기</label>
                                        <span class="text-info m-l-md">* 이미지는 반드시 가로 350px, 세로 500px 이어야 합니다.</span>
                                    </td>
                                </tr>
                                <tr>
                                    <th>팝업창 내용</th>
                                    <td><textarea class="form-control" rows="16" id="contents" name="contents" maxlength="500"></textarea></td>
                                </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row m-b-lg">
        <div class="col-lg-12 text-right">
            <button class="btn btn-lg btn-w-m btn-default" onclick="cancellBtn()">취소</button>
            <button class="btn btn-lg btn-w-m btn-primary" onclick="popupReg()">등록</button>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<script>
    $('.input-daterange').datepicker({
        keyboardNavigation: false,
        format: 'yyyy.mm.dd',
        maxDate: "+0d",
        forceParse: false,
        autoclose: true
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
</script>

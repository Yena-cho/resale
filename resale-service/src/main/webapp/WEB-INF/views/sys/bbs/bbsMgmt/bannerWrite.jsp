<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<s:authentication property="principal.name" var="name"/>
<s:authentication property="principal.email" var="email"/>
<s:authentication property="principal.loginId" var="loginId"/>

<script>
    var oneDepth = "adm-nav-4";
    var twoDepth = "adm-sub-09-2";


    // ===== XSS 검증 함수 추가 =====
    /**
     * XSS 공격 패턴 검증
     * 2025-10-24 조성운 추가  사유 : 20251024 취약점 지적사항
     * @param text 검증할 텍스트
     * @returns {boolean} 위험한 패턴이 발견되면 true
     */
    function containsXSSPattern(text) {
        if (!text) return false;

        // 위험한 XSS 패턴들
        var xssPatterns = [
            /<script[^>]*>.*?<\/script>/gi,
            /<script[^>]*>/gi,
            /<iframe[^>]*>.*?<\/iframe>/gi,
            /<iframe[^>]*>/gi,
            /javascript:/gi,
            /on\w+\s*=/gi,  // onclick, onerror, onload 등
            /eval\s*\(/gi,
            /expression\s*\(/gi
        ];

        for (var i = 0; i < xssPatterns.length; i++) {
            if (xssPatterns[i].test(text)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 입력값 XSS 검증
     * 2025-10-24 조성운 추가  사유 : 20251024 취약점 지적사항
     * @param name 배너명
     * @param title 제목
     * @param content 내용
     * @returns {boolean} 검증 통과 시 true
     */
    function validateXSS(name, title, content) {
        if (containsXSSPattern(name)) {
            swal({
                type: 'warning',
                text: '배너명에 허용되지 않는 문자가 포함되어 있습니다.\n(<script>, <iframe>, javascript: 등)',
                confirmButtonColor: '#d33',
                confirmButtonText: '확인'
            });
            return false;
        }

        if (containsXSSPattern(title)) {
            swal({
                type: 'warning',
                text: '제목에 허용되지 않는 문자가 포함되어 있습니다.\n(<script>, <iframe>, javascript: 등)',
                confirmButtonColor: '#d33',
                confirmButtonText: '확인'
            });
            return false;
        }

        if (containsXSSPattern(content)) {
            swal({
                type: 'warning',
                text: '내용에 허용되지 않는 문자가 포함되어 있습니다.\n(<script>, <iframe>, javascript: 등)',
                confirmButtonColor: '#d33',
                confirmButtonText: '확인'
            });
            return false;
        }

        return true;
    }

</script>

<script>
    /**
     * 배너 등록
     */
    function uploadBanner() {
        var bannerShowVal = $("input[name=bannerShow]:checked").val();

        if (bannerShowVal == "P") {
            // pc 일 때
            uploadCheckPc();
        } else if (bannerShowVal == "M") {
            // 모바일 일 떄
            uploadCheckMobile();
        }
    }

    /**
     * PC
     */
    function uploadCheckPc() {
        var filePcName = $('#filePcName').val();
        var idx = filePcName.lastIndexOf('\\') + 1;
        var filePcNameCheck = filePcName.substring(idx, filePcName.length);
        var fileType = filePcName.substring(filePcName.lastIndexOf(".") + 1, filePcName.length);

        if ($("#pcName").val() == "") {
            swal({
                type: 'success',
                text: '배너명을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $("#pcName").focus();
            });
            return;
        }
        // 영어 숫자 특수문자만 가능
        var pattern = /[^(a-zA-Z0-9.`~!@#$%^&*|\\\'\";:\/?\_\-)]/gi;
        if (pattern.test(filePcNameCheck)) {
            swal({
                type: 'success',
                text: '파일명은 영문,숫자만 허용합니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $("#pcName").focus();
            });
            return;
        }
        if ($("#showPcY").is(":checked") == false && $("#showPcN").is(":checked") == false) {
            swal({
                type: 'success',
                text: '노출여부를 설정해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }
        if ($("#filePcUpload").val() == "") {
            swal({
                type: 'success',
                text: '배너 이미지를 등록해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }
        if (fileType != 'jpg' && fileType != 'png') {
            swal({
                type: 'error',
                text: '세로 사이즈 400px이며 JPG, PNG 파일만 업로드 할 수 있습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }

        if ($('#orderPcOk span').html() == "사용불가") {
            swal({
                type: 'error',
                text: '사용 불가능한 노출순서입니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }

        // ===== XSS 검증 추가 =====
        // 2025-10-24 조성운 추가  사유 : 20251024 취약점 지적사항
        // 배너명, 제목, 내용에 대한 XSS 패턴 검증
        var pcName = $("#pcName").val();
        var pcTitle = $("#pcTitle").val();
        var pcContent = $("#pcContent").val();

        if (!validateXSS(pcName, pcTitle, pcContent)) {
            return;
        }

        doInsert('P');
    }

    function uploadCheckMobile() {
        var fileMobileName = $('#fileMobileName').val();
        var idx = fileMobileName.lastIndexOf('\\') + 1;
        var fileMobileNameCheck = fileMobileName.substring(idx, fileMobileName.length);
        var fileType = fileMobileName.substring(fileMobileName.lastIndexOf(".") + 1, fileMobileName.length);

        if ($("#mobileName").val() == "") {
            swal({
                type: 'success',
                text: '배너명을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $("#mobileName").focus();
            });
            return;
        }
        // 영어 숫자 특수문자만 가능
        var pattern = /[^(a-zA-Z0-9.`~!@#$%^&*|\\\'\";:\/?\_\-)]/gi;
        if (pattern.test(fileMobileNameCheck)) {
            swal({
                type: 'success',
                text: '파일명은 영문,숫자만 허용합니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function () {
                $("#mobileName").focus();
            });
            return;
        }
        if ($("#showMobileY").is(":checked") == false && $("#showMobileN").is(":checked") == false) {
            swal({
                type: 'success',
                text: '노출여부를 설정해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }
        if ($("#fileMobileUpload").val() == "") {
            swal({
                type: 'success',
                text: '배너 이미지를 등록해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }
        if (fileType != 'jpg' && fileType != 'png') {
            swal({
                type: 'error',
                text: '가로 사이즈 360px이며 JPG, PNG 파일만 업로드 할 수 있습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }
        if ($('#orderMobileOk span').html() == "사용불가") {
            swal({
                type: 'error',
                text: '사용 불가능한 노출순서입니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }

        // ===== XSS 검증 추가 =====
        // 2025-10-24 조성운 추가  사유 : 20251024 취약점 지적사항
        // 배너명, 제목, 내용에 대한 XSS 패턴 검증
        var pcName = $("#mobileName").val();
        var pcTitle = $("#mobileTitle").val();
        var pcContent = $("#mobileContent").val();

        if (!validateXSS(pcName, pcTitle, pcContent)) {
            return;
        }


        doInsert('M');
    }

    function doInsert(val) {
        var orderNo;
        var formData;

        if (val == 'P') {
            formData = new FormData($("#pcFileForm")[0]);
            if ($('#showPcY').prop('checked')) {
                if ($('#pcOrderNo option:selected').val() == 0) {
                    swal({
                        type: 'success',
                        text: '노출순서를 선택해 주세요.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                    return;
                } else {
                    orderNo = $('#pcOrderNo option:selected').val();
                }
            } else {
                orderNo = "0";
            }
        } else {
            formData = new FormData($("#mobileFileForm")[0]);
            if ($('#showMobileY').prop('checked')) {
                if ($('#mobileOrderNo option:selected').val() == 0) {
                    swal({
                        type: 'success',
                        text: '노출순서를 선택해 주세요.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                    return;
                } else {
                    orderNo = $('#mobileOrderNo option:selected').val();
                }
            } else {
                orderNo = "0";
            }
        }

        formData.append("orderNo", orderNo);

        $.ajax({
            type: "POST",
            url: "/sys/doBannerWrite",
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
                            location.href = "/sys/bannerSetting";
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

    /**
     * 등록 취소
     */
    function uploadCancel() {
        location.href = "/sys/bannerSetting";
    }

    function fn_mobileShowYN(no) {
        if (no == 1) {
            $("#showMobileYN").val('Y');
            $("#mobileOrderNo").attr("disabled", false);
        } else {
            $("#showMobileYN").val('N');
            $("#mobileOrderNo").attr("disabled", true);
        }
    }

    function fn_pcShowYN(no) {
        if (no == 1) {
            $("#showPcYN").val('Y');
            $("#pcOrderNo").attr("disabled", false);
        } else {
            $("#showPcYN").val('N');
            $("#pcOrderNo").attr("disabled", true);
        }
    }

    /**
     * 배너 구분
     */
    function bannerShow(val) {
        if (val == 'P') {
            $("#pcRow").css("display", "block");
            $("#mobileRow").css("display", "none");
        } else if (val == 'M') {
            $("#pcRow").css("display", "none");
            $("#mobileRow").css("display", "block");
        }
    }

    /**
     * 노출 순서 확인
     */
    function orderCheck() {
        var viewTypeCd = $('input[name=bannerShow]:checked').val();
        var orderNo;

        if (viewTypeCd == "P") {
            viewTypeCd = "S10001";
            orderNo = $('#pcOrderNo option:selected').val();
        } else if (viewTypeCd == "M") {
            viewTypeCd = "S10002";
            orderNo = $('#mobileOrderNo option:selected').val();
        }

        if (orderNo == "0") {
            swal({
                type: 'success',
                text: '노출순서를 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }

        var param = {
            viewTypeCd : viewTypeCd,
            orderNo : orderNo
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/orderCheck",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    swal({
                        type: 'success',
                        text: '사용가능한 노출순서 입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });

                    if (viewTypeCd == "S10001") {
                        $('#orderPcOk').html('<span style="color: #337ab7;">사용가능</span>');
                    } else {
                        $('#orderMobileOk').html('<span style="color: #337ab7;">사용가능</span>');
                    }
                } else if (result.retCode == "0001") {
                    swal({
                        type: 'error',
                        text: '이미 사용중인 노출순서 입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })

                    if (viewTypeCd == "S10001") {
                        $('#orderPcOk').html('<span class="text-danger">사용불가</span>');
                    } else {
                        $('#orderMobileOk').html('<span class="text-danger">사용불가</span>');
                    }
                } else {
                    swal({
                        type: 'error',
                        text: '시스템 오류입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })
                }
            }
        });
    }
</script>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>배너 등록</h2>
        <ol class="breadcrumb">
            <li><a href="/sys/index">대시보드</a></li>
            <li><a>게시판관리</a></li>
            <li class="active"><strong>배너 등록</strong></li>
        </ol>
        <p class="page-description">배너를 등록하는 화면입니다.</p>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox">
                <div class="ibox-content">
                    <table>
                        <colgroup>
                            <col width="200">
                            <col width="*">
                        </colgroup>

                        <tbody>
                            <tr>
                                <th>배너 구분</th>
                                <td>
                                    <div class="radio radio-primary radio-inline">
                                        <input type="radio" id="pcShow" name="bannerShow" onclick="bannerShow('P');" value="P" checked="checked">
                                        <label for="pcShow"> PC </label>
                                    </div>
                                    <div class="radio radio-primary radio-inline">
                                        <input type="radio" id="mobileShow" name="bannerShow" onclick="bannerShow('M');" value="M">
                                        <label for="mobileShow"> 모바일 </label>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="row" id ="pcRow">
        <div class="col-lg-12">
            <div class="ibox">
                <div class="ibox-content">
                    <form id="pcFileForm" name="pcFileForm" enctype="multipart/form-data">
                        <table class="table table-stripped" style="margin-bottom: 0;">
                            <colgroup>
                                <col width="200">
                                <col width="587">
                                <col width="200">
                                <col width="586">
                            </colgroup>

                            <tbody>
                                <tr>
                                    <th>PC 배너명</th>
                                    <td colspan="3">
                                        <input type="text" class="form-control" name="pcName" id="pcName" max="12">
                                        <input type="hidden" name="loginId" value="${loginId }">
                                        <input type="hidden" id="viewTypeCd" name="viewTypeCd" value="S10001">
                                    </td>
                                </tr>

                                <tr>
                                    <th>PC 노출여부</th>
                                    <td>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="showPcY" name="show" onclick="fn_pcShowYN(1)">
                                            <label for="showPcY"> 노출 </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="showPcN" name="show" checked="checked" onclick="fn_pcShowYN(0)">
                                            <label for="showPcN"> 미노출 </label>
                                        </div>
                                        <input type="hidden" id="showPcYN" name="showPcYN" value="N">
                                    </td>

                                    <th>PC 노출순서</th>
                                    <td>
                                        <select class="form-control" id="pcOrderNo" style="width:150px; float:left;" disabled="disabled" onchange="orderCheck();">
                                            <option value="0">선택</option>
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                        </select>
                                        <span id="orderPcOk" style="float: left; margin: 7px 0px 0px 15px;"></div>
                                    </td>
                                </tr>

                                <tr>
                                    <th>PC 이미지 업로드</th>
                                    <td colspan="3" class="form-inline">
                                        <input type="text" class="form-control" id="filePcName" readonly="readonly" style="width: 600px;">
                                        <input type="file" class="form-control" id="filePcUpload" name="filePcName" style="display: none;" onchange="document.getElementById('filePcName').value = this.value" accept=".jpg, .png">
                                        <label for="filePcUpload" class="btn btn-primary">파일찾기</label>
                                        <span class="text-info m-l-md">* 이미지는 세로 400px 이고, JPG, PNG 파일만 가능합니다.</span>
                                    </td>
                                </tr>

                                <tr>
                                    <th>PC 배너 제목</th>
                                    <td colspan="3">
                                        <textarea class="form-control" rows="2" id="pcTitle" name="pcTitle"></textarea>
                                        <span class="text-info m-l-md">* 한줄에 18글자씩 최대 2줄까지 가능합니다</span>
                                    </td>
                                </tr>

                                <tr>
                                    <th>PC 배너 내용</th>
                                    <td colspan="3">
                                        <textarea class="form-control" rows="5" id="pcContent" name="pcContent"></textarea>
                                        <span class="text-info m-l-md">* 한줄에 40글자씩 최대 10줄까지 가능합니다</span>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row" id ="mobileRow" style="display: none;">
        <div class="col-lg-12">
            <div class="ibox">
                <div class="ibox-content">
                    <form id="mobileFileForm" name="mobileFileForm" enctype="multipart/form-data">
                        <table class="table table-stripped" style="margin-bottom: 0;">
                            <colgroup>
                                <col width="200">
                                <col width="587">
                                <col width="200">
                                <col width="586">
                            </colgroup>

                            <tbody>
                                <tr>
                                    <th>모바일 배너명</th>
                                    <td colspan="3">
                                        <input type="text" class="form-control" name="mobileName" id="mobileName" max="12">
                                        <input type="hidden" name="loginId" value="${loginId }">
                                        <input type="hidden" id="viewTypeCd" name="viewTypeCd" value="S10002">
                                    </td>
                                </tr>

                                <tr>
                                    <th>모바일 노출여부</th>
                                    <td>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="showMobileY" name="show" onclick="fn_mobileShowYN(1)">
                                            <label for="showMobileY"> 노출 </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="showMobileN" name="show" checked="checked" onclick="fn_mobileShowYN(0)">
                                            <label for="showMobileN"> 미노출 </label>
                                        </div>
                                        <input type="hidden" id="showMobileYN" name="showMobileYN" value="N">
                                    </td>

                                    <th>모바일 노출순서</th>
                                    <td>
                                        <select class="form-control" id="mobileOrderNo" style="width:150px; float: left;" disabled="disabled" onchange="orderCheck();">
                                            <option value="0">선택</option>
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                        </select>
                                        <div id="orderMobileOk" style="float: left; margin: 7px 0px 0px 15px;"></div>
                                    </td>
                                </tr>

                                <tr>
                                    <th>모바일 이미지 업로드</th>
                                    <td colspan="3" class="form-inline">
                                        <input type="text" class="form-control" id="fileMobileName" readonly="readonly" style="width: 600px;">
                                        <input type="file" class="form-control" id="fileMobileUpload" name="fileMobileName" style="display: none;" onchange="document.getElementById('fileMobileName').value = this.value" accept=".jpg, .png">
                                        <label for="fileMobileUpload" class="btn btn-primary">파일찾기</label>
                                        <span class="text-info m-l-md">* 이미지는 사이즈는 가로 360px, JPG, PNG 파일만 가능합니다.</span>
                                    </td>
                                </tr>

                                <tr>
                                    <th>모바일 배너 제목</th>
                                    <td colspan="3">
                                        <textarea class="form-control" rows="2" id="mobileTitle" name="mobileTitle"></textarea>
                                        <span class="text-info m-l-md">* 한줄에 6글자씩 최대 2줄까지 가능합니다</span>
                                    </td>
                                </tr>

                                <tr>
                                    <th>모바일 배너 내용</th>
                                    <td colspan="3">
                                        <textarea class="form-control" rows="5" id="mobileContent" name="mobileContent"></textarea>
                                        <span class="text-info m-l-md">* 한줄에 18글자씩 최대 8줄까지 가능합니다</span>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12 text-right">
            <button class="btn btn-lg btn-w-m btn-default" onclick="uploadCancel();">취소</button>
            <button class="btn btn-lg btn-w-m btn-primary" onclick="uploadBanner();">등록</button>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

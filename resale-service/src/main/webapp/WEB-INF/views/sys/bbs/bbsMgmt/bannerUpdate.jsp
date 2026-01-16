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
     * 수정 등록
     */
    function update() {
        if ($("#name").val() == "") {
            swal({
                type: 'success',
                text: '배너명을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if ($("#inlineCheckbox2-1").is(":checked") == false && $("#inlineCheckbox2-2").is(":checked") == false) {
            swal({
                type: 'success',
                text: '노출여부를 설정해주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if ($("#fileName").val() == "") {
            swal({
                type: 'success',
                text: '배너 이미지를 등록해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if ($("#orderOk span").html() == "사용불가") {
            swal({
                type: 'success',
                text: '사용 불가능한 노출순서입니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        // 2025-10-24 조성운 추가  사유 : 20251024 취약점 지적사항
        // ===== XSS 검증 추가 =====
        // 배너명, 제목, 내용에 대한 XSS 패턴 검증
        var name = $("#name").val();
        var title = $("#title").val();
        var content = $("#content").val();

        if (!validateXSS(name, title, content)) {
            return;
        }

        doUpdate();
    }

    function doUpdate() {
        var orderNo = "0";

        if ($('#inlineCheckbox2-1').prop('checked')) {
            if ($('#orderNo option:selected').val() == 0) {
                swal({
                    type: 'success',
                    text: '노출순서를 선택해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                })
                return;
            } else {
                orderNo = $('#orderNo option:selected').val();
            }
        }

        var formData = new FormData($("#fileForm")[0]);
        formData.append("fileName", $('#fileName').val());
        formData.append("orderNo", orderNo);
        formData.append("showYn", $('input[name=show]:checked').val());

        $.ajax({
            type: "POST",
            url: "/sys/doBannerUpdate",
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                if (result.retCode == "0000") {
                    swal({
                        type: 'success',
                        text: '수정완료',
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
     * 수정 취소
     */
    function cancel() {
        location.href = "/sys/bannerSetting";
    }

    function fn_showYN(no) {
        if (no == 1) {
            $("#showY").val('Y');
            $("#orderNo").attr("disabled", false);
        } else {
            $("#showN").val('N');
            $("#orderNo").attr("disabled", true);
        }
    }

    /**
     * 노출 순서 확인
     */
    function orderCheck() {
        var viewTypeCd = $('#viewTypeCd').val();
        var orderNo = orderNo = $('#orderNo option:selected').val();

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

                    $('#orderOk').html('<span style="color: #337ab7;">사용가능</span>');
                } else if (result.retCode == "0001") {
                    swal({
                        type: 'error',
                        text: '이미 사용중인 노출순서 입니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    })

                    $('#orderOk').html('<span class="text-danger">사용불가</span>');
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
        <h2>배너 수정</h2>
        <ol class="breadcrumb">
            <li><a href="/sys/index">대시보드</a></li>
            <li><a>게시판관리</a></li>
            <li class="active"><strong>배너 수정</strong></li>
        </ol>
        <p class="page-description">등록된 배너를 수정하는 화면입니다.</p>
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
                                    <c:if test="${map.list.viewTypeCd == 'S10001'}">PC</c:if>
                                    <c:if test="${map.list.viewTypeCd == 'S10002'}">모바일</c:if>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <div class="ibox">
                <div class="ibox-content">
                    <form id="fileForm" name="fileForm" enctype="multipart/form-data">
                        <table class="table table-stripped">
                            <colgroup>
                                <col width="200">
                                <col width="587">
                                <col width="200">
                                <col width="586">
                            </colgroup>

                            <tbody>
                                <tr>
                                    <th><c:if test="${map.list.viewTypeCd == 'S10001'}">PC</c:if><c:if test="${map.list.viewTypeCd == 'S10002'}">모바일</c:if> 배너명</th>
                                    <td colspan="3">
                                        <input type="text" class="form-control" id="name" name="name" value="${map.list.name}">
                                        <input type="hidden" id="id" name="id" value="${map.list.id}">
                                        <input type="hidden" id="fileId" name="fileId" value="${map.list.fileId}">
                                        <input type="hidden" id="viewTypeCd" name="viewTypeCd" value="${map.list.viewTypeCd}">
                                        <input type="hidden" name="loginId" value="${loginId }">
                                    </td>
                                </tr>

                                <tr>
                                    <th><c:if test="${map.list.viewTypeCd == 'S10001'}">PC</c:if><c:if test="${map.list.viewTypeCd == 'S10002'}">모바일</c:if> 노출여부</th>
                                    <td>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="inlineCheckbox2-1" id="showY" name="show" <c:if test="${map.list.showYn == 'Y'}">checked</c:if> value="Y" onclick="fn_showYN(1)">
                                            <label for="inlineCheckbox2-1"> 노출 </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" id="inlineCheckbox2-2" id="showN" name="show" <c:if test="${map.list.showYn == 'N'}">checked</c:if> value="N" onclick="fn_showYN(0)">
                                            <label for="inlineCheckbox2-2"> 미노출 </label>
                                        </div>

                                        <input type="hidden" id="showYN" name="showYN" value="N">
                                    </td>

                                    <th><c:if test="${map.list.viewTypeCd == 'S10001'}">PC</c:if><c:if test="${map.list.viewTypeCd == 'S10002'}">모바일</c:if> 노출순서</th>
                                    <td>
                                        <select class="form-control" id="orderNo" style="width:150px; float:left;" <c:if test="${map.list.showYn == 'N'}">disabled="disabled"</c:if> onchange="orderCheck();">
                                            <option value="0" <c:if test="${map.list.orderNo == '0'}">selected</c:if>>선택</option>
                                            <option value="1" <c:if test="${map.list.orderNo == '1'}">selected</c:if>>1</option>
                                            <option value="2" <c:if test="${map.list.orderNo == '2'}">selected</c:if>>2</option>
                                            <option value="3" <c:if test="${map.list.orderNo == '3'}">selected</c:if>>3</option>
                                            <option value="4" <c:if test="${map.list.orderNo == '4'}">selected</c:if>>4</option>
                                            <option value="5" <c:if test="${map.list.orderNo == '5'}">selected</c:if>>5</option>
                                        </select>
                                        <div id="orderOk" style="float: left; margin: 7px 0px 0px 15px;"></div>
                                    </td>
                                </tr>

                                <tr>
                                    <th><c:if test="${map.list.viewTypeCd == 'S10001'}">PC</c:if><c:if test="${map.list.viewTypeCd == 'S10002'}">모바일</c:if> 이미지 업로드</th>
                                    <td colspan="3" class="form-inline">
                                        <input type="text" class="form-control" id="fileName" readonly="readonly" value="${map.list.fileName }" style="width: 600px;">
                                        <input type="file" class="form-control" id="file" name="file" style="display: none;" onchange="document.getElementById('fileName').value = this.value" accept=".jpg, .png">
                                        <label for="file" class="btn btn-primary">파일찾기</label>
                                        <span class="text-info m-l-md">
                                            * 이미지는 사이즈는
                                            <c:if test="${map.list.viewTypeCd == 'S10001'}">세로 400px</c:if>
                                            <c:if test="${map.list.viewTypeCd == 'S10002'}">가로 360px</c:if> 이고, JPG, PNG 파일만 가능합니다.
                                        </span>
                                    </td>
                                </tr>

                                <tr>
                                    <th><c:if test="${map.list.viewTypeCd == 'S10001'}">PC</c:if><c:if test="${map.list.viewTypeCd == 'S10002'}">모바일</c:if> 배너 제목</th>
                                    <td colspan="3">
                                        <textarea class="form-control" rows="2" id="title" name="title" style="resize:none;">${map.list.title}</textarea>
                                        <c:if test="${map.list.viewTypeCd == 'S10001'}"><span class="text-info m-l-md">* 한줄에 18글자씩 최대 2줄까지 가능합니다</span></c:if>
                                        <c:if test="${map.list.viewTypeCd == 'S10002'}"><span class="text-info m-l-md">* 한줄에 6글자씩 최대 2줄까지 가능합니다</span></c:if>
                                    </td>
                                </tr>

                                <tr>
                                    <th><c:if test="${map.list.viewTypeCd == 'S10001'}">PC</c:if><c:if test="${map.list.viewTypeCd == 'S10002'}">모바일</c:if> 배너 내용</th>
                                    <td colspan="3">
                                        <textarea class="form-control" rows="5" id="content" name="content" style="resize:none;">${map.list.content}</textarea>
                                        <c:if test="${map.list.viewTypeCd == 'S10001'}"><span class="text-info m-l-md">* 한줄에 40글자씩 최대 10줄까지 가능합니다</span></c:if>
                                        <c:if test="${map.list.viewTypeCd == 'S10002'}"><span class="text-info m-l-md">* 한줄에 18글자씩 최대 8줄까지 가능합니다</span></c:if>
                                    </td>
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
            <button class="btn btn-lg btn-w-m btn-default" onclick="cancel()">취소</button>
            <button class="btn btn-lg btn-w-m btn-primary" onclick="update()">수정</button>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>


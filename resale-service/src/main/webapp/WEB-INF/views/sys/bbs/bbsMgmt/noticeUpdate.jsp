<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
<script src="/assets/smarteditor2/workspace/js/service/HuskyEZCreator.js"></script>
<script>
    var oneDepth = "adm-nav-4";
    var twoDepth = "adm-sub-06";
</script>

<script>
    function cancellBtn() {
        history.back(); // 이전페이지 이동
    }

    function doUpdate_noFile() {
        if ($("#contents").val() == null || $("#contents").val() == '') {
            swal({
                type: 'info',
                text: '내용을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        }
        if ($("#inlineCheckbox2-1").prop("checked") == false && $("#inlineCheckbox2-2").prop("checked") == false) {
            swal({
                type: 'info',
                text: '중요공지 여부를 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else {
            var param = {
                isfix: $("#isfix").val(),
                no: $("#no").val(),
                writer: $("#writer").val().trim(),
                title: $("#title").val().trim(),
                contents: $("#contents").val()
            };

            swal({
                type: 'question',
                html: "수정하시겠습니까?",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                reverseButtons: true
            }).then(function (result) {
                if (result.value) {
                    $.ajax({
                        type: "POST",
                        async: true,
                        url: "/sys/noticeUpdateNoFile",
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify(param),
                        success: function (result) {
                            if (result.retCode == "0000") {
                                swal({
                                    type: 'success',
                                    text: '수정하였습니다.',
                                    confirmButtonColor: '#3085d6',
                                    confirmButtonText: '확인'
                                }).then(function (result) {
                                    if (result.value) {
                                        location.href = "/sys/noticeSetting";
                                    }
                                });
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
            });
        }
    }

    function doUpdate_withFile() {
        var fileName = $("#filename").val();
        var fileext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length);
        // 사이즈체크
        var maxSize = 2 * 1024 * 1024 //2MB
        var fileSize;
        if (fileName != null && fileName != "") {
            fileSize = document.getElementById("rfile").files[0].size;
        }

        if ($("#contents").val() == null || $("#contents").val() == '') {
            swal({
                type: 'info',
                text: '내용을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if ($("#inlineCheckbox2-1").prop("checked") == false && $("#inlineCheckbox2-2").prop("checked") == false) {
            swal({
                type: 'info',
                text: '중요공지 여부를 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if (fileName != ""
            && (fileext != 'jpg' && fileext != 'pdf' && fileext != 'gif'
                && fileext != 'xls' && fileext != 'xlsx')) {
            swal({
                type: 'error',
                text: ' jpg, pdf, gif, 엑셀 파일만 업로드 할 수 있습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else if (fileSize > maxSize) {
            swal({
                type: 'error',
                text: ' 2MB 이하의 파일만 업로드 할 수 있습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        } else if (fileName.search(/\s/) != -1) {
            swal({
                type: 'info',
                text: "파일명은 공백없이 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        swal({
            type: 'question',
            html: "수정하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                var formData = new FormData($("#fileForm")[0]);
                $.ajax({
                    type: "POST",
                    url: "/sys/noticeUpdate",
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (result) {
                        if (result.retCode == "0000") {
                            location.href = "/sys/noticeSetting";
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
        });
    }

    function file_search() {
        $("input:file").click();
    }

    function inlineCheckbox1() {
        $("#inlineCheckbox2-2").prop("checked", false)
        $("#isfix").val(1);
    }

    function inlineCheckbox2() {
        $("#inlineCheckbox2-1").prop("checked", false)
        $("#isfix").val(0);
    }
</script>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>공지사항수정</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>게시판관리</a>
            </li>
            <li class="active">
                <strong>공지사항수정</strong>
            </li>
        </ol>
        <p class="page-description">등록된 공지사항을 수정하는 화면입니다.</p>
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
                                    <th>게시판</th>
                                    <td>공지사항</td>
                                </tr>
                                <tr>
                                    <th>읽기권한</th>
                                    <td>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-2" value="option1" disabled="disabled" <c:if test="${map.dto.bbs == 7}">checked</c:if>>
                                            <label for="inlineCheckbox1-2"> 로그인전 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-3" value="option1" disabled="disabled" <c:if test="${map.dto.bbs == 8}">checked</c:if>>
                                            <label for="inlineCheckbox1-3"> 기관담당자 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-4" value="option1" disabled="disabled" <c:if test="${map.dto.bbs == 6}">checked</c:if>>
                                            <label for="inlineCheckbox1-4"> 납부자 </label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>중요공지</th>
                                    <td>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox2-1" value="1" name="option2" onclick="inlineCheckbox1()" <c:if test="${map.dto.isfix == 1}">checked</c:if>>
                                            <label for="inlineCheckbox2-1"> 중요 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox2-2" value="0" name="option2" onclick="inlineCheckbox2()" <c:if test="${map.dto.isfix == 0}">checked</c:if>>
                                            <label for="inlineCheckbox2-2"> 일반 </label>
                                        </div>
                                        <input type="hidden" id="isfix" name="isfix" value="${map.dto.isfix }">
                                    </td>
                                </tr>
                                <tr>
                                    <td>작성자</td>
                                    <td>${map.dto.writer }<input type="hidden" name="writer" id="writer" value="${map.dto.writer }"></td>
                                </tr>
                                <tr>
                                    <td>제목</td>
                                    <td><input type="text" class="form-control input-sm" value="${map.dto.title }" id="title" name="title" maxlength="200"></td>
                                </tr>
                                <tr>
                                    <td>파일첨부</td>
                                    <td>
                                        <div class="input-group">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" placeholder="최대 2MB 입력 가능 (jpg, gif, pdf 파일 첨부 가능)" id="filename" readonly="readonly" value="${map.dto.filename }">
                                            <input type="file" id="rfile" name="file" style="display: none;" onchange="document.getElementById('filename').value = this.value" accept=".jpg, .gif, .pdf">
                                            <input type="hidden" name="no" value="${map.dto.no }" id="no">
                                            <span class="input-group-btn">
                                                <button class="btn btn-primary no-margins" type="button" id="btn-upload" onclick="file_search()">파일찾기</button>
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>내용</td>
                                    <td><textarea class="form-control" rows="16" id="contents" name="contents" style="resize:none; display: none" maxlength="2000">${map.dto.contents }</textarea></td>
                                </tr>
                            </tbody>
                        </table>
                    </form>

                    <div class="row">
                        <div class="col-lg-12 text-right">
                            <button class="btn btn-lg btn-w-m btn-secondary" onclick="cancellBtn()">취소</button>
                            <button class="btn btn-lg btn-w-m btn-primary" id="noticeRegBtn">수정</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<script>
    $(function() {
        //전역변수선언
        var editor_object = [];
        nhn.husky.EZCreator.createInIFrame({
            oAppRef: editor_object,
            elPlaceHolder: "contents",
            sSkinURI: "/assets/smarteditor2/workspace/SmartEditor2Skin.html",
            htParams: {
                // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
                bUseToolbar: true,
                // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
                bUseVerticalResizer: false,
                // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
                bUseModeChanger: false,
            },
            bucketName : "notice"
        });

        $("#noticeRegBtn").click(function(){
            editor_object.getById["contents"].exec("UPDATE_CONTENTS_FIELD", []);
            $("#contents").val($("#contents").val().replace(/\uFEFF/gi, ""));
            if ($("#rfile").val() == "" || $("#rfile").val() == null) {
                doUpdate_noFile();
            } else {
                doUpdate_withFile();
            }
        });

    });
</script>
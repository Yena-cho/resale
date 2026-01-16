<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
<script src="/assets/smarteditor2/workspace/js/service/HuskyEZCreator.js"></script>
<!-- session -->
<s:authentication property="principal.name" var="name"/> <!-- 사용자이름 -->
<s:authentication property="principal.email" var="email"/> <!-- 이메일 -->
<s:authentication property="principal.loginId" var="loginId"/> <!-- 로그인ID -->
<!--/ session -->

<script>
    var oneDepth = "adm-nav-4";
    var twoDepth = "adm-sub-06";
</script>


<script>

    function filesearch() {
        $("input:file").click();
    }

    function cancellBtn() {
        location.href = "/sys/noticeSetting";
    }

    //버튼들 동작하게 하기
    function inlineCheckbox1() {
        $("#inlineCheckbox2-2").prop("checked", false)
        $("#isfix").val(1);
    }

    function inlineCheckbox2() {
        $("#inlineCheckbox2-1").prop("checked", false)
        $("#isfix").val(0);
    }

    function allClick() {
        $("#inlineCheckbox1-2").prop("checked", false)
        $("#inlineCheckbox1-3").prop("checked", false)
        $("#inlineCheckbox1-4").prop("checked", false)
    }

    function notAll() {
        $("#inlineCheckbox1-1").prop("checked", false)
    }

    function doinsert() {
        var formData = new FormData($("#fileForm")[0]);

        swal({
            type: 'question',
            html: "등록하시겠습니까?",
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
                    url: "/sys/doNoticeWrite",
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
                            }).then(function () {
                                location.href = "/sys/noticeSetting";
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
        });
    }
</script>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>공지사항등록</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>게시판관리</a>
            </li>
            <li class="active">
                <strong>공지사항등록</strong>
            </li>
        </ol>
        <p class="page-description">공지사항을 등록하는 화면입니다.</p>
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
                                            <input type="checkbox" id="inlineCheckbox1-1" name="chkAuth"
                                                   onclick="allClick()">
                                            <label for="inlineCheckbox1-1"> 전체 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-2" name="chkAuth" onclick="notAll()">
                                            <label for="inlineCheckbox1-2"> 로그인전 </label>
                                            <input type="hidden" id="login" name="login">
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-3" name="chkAuth" onclick="notAll()">
                                            <label for="inlineCheckbox1-3"> 기관담당자 </label>
                                            <input type="hidden" id="org" name="org">
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1-4" name="chkAuth" onclick="notAll()">
                                            <label for="inlineCheckbox1-4"> 납부자 </label>
                                            <input type="hidden" id="submit" name="submit">
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>중요공지</th>
                                    <td>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox2-1" onclick="inlineCheckbox1()">
                                            <label for="inlineCheckbox2-1"> 중요 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox2-2" onclick="inlineCheckbox2()">
                                            <label for="inlineCheckbox2-2"> 일반 </label>
                                        </div>
                                        <input type="hidden" id="isfix" name="isfix">
                                    </td>
                                </tr>
                                <tr>
                                    <td>작성자</td>
                                    <td>
                                        ${name }
                                        <input type="hidden" name="writer" value="${name }">
                                        <input type="hidden" name="email" value="${email }">
                                        <input type="hidden" id="loginId" value="${loginId }" name="loginId">
                                        <input type="hidden" id="phoneNumber" value="${map.phoneNumber }"name="phoneNumber">
                                    </td>
                                </tr>
                                <tr>
                                    <td>제목</td>
                                    <td><input type="text" class="form-control input-sm" id="title" name="title" maxlength="200"></td>
                                </tr>
                                <tr>
                                    <td>파일첨부</td>
                                    <td>
                                        <div class="input-group">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" placeholder="최대 2MB 입력 가능 (jpg, gif, pdf 파일 첨부 가능)" id="filename" readonly="readonly" value="${map.dto.filename }">
                                            <input type="file" name="file" style="display: none;" id="rfile" onchange="document.getElementById('filename').value = this.value" accept=".jpg, .gif, .pdf">
                                            <span class="input-group-btn">
                                                <button class="btn btn-primary no-margins" type="button" id="btn-upload" onclick="filesearch()">파일찾기</button>
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>내용</td>
                                    <%--<textarea name="smarteditor" id="smarteditor" rows="10" cols="100" style="width:766px; height:412px;"></textarea>--%>
                                    <td><textarea class="form-control" rows="16" id="contents" maxlength="2000" name="contents"></textarea></td>
                                </tr>
                            </tbody>
                        </table>
                    </form>
                    <div class="row">
                        <div class="col-lg-12 text-right">
                            <button class="btn btn-lg btn-w-m btn-secondary" onclick="cancellBtn()">취소</button>
                            <button class="btn btn-lg btn-w-m btn-primary" id="noticeRegBtn">등록</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(function(){
        //전역변수선언
        var editor_object = [];

        nhn.husky.EZCreator.createInIFrame({
            oAppRef: editor_object,
            elPlaceHolder: "contents",
            sSkinURI: "/assets/smarteditor2/workspace/SmartEditor2Skin.html",
            htParams : {
                // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
                bUseToolbar : true,
                // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
                bUseVerticalResizer : false,
                // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
                bUseModeChanger : false,
            },
            bucketName : "notice"
        });

    $("#noticeRegBtn").click(function(){
        //id가 smarteditor인 textarea에 에디터에서 대입
        editor_object.getById["contents"].exec("UPDATE_CONTENTS_FIELD", []);
        $("#contents").val($("#contents").val().replace(/\uFEFF/gi, ""));
        var fileName = $("#filename").val();
        var fileext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length);

        // 사이즈체크
        var maxSize = 2 * 1024 * 1024; //2MB
        var fileSize = 0;
        if ($("#rfile").val()) {
            fileSize = document.getElementById("rfile").files[0].size;
        }

        //권한설정, 중요공지 선택안했을경우 알럿 띄우는 로직 추가해야함
        if ($("#title").val() == "") {
            swal({
                type: 'success',
                text: '제목을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if ($("#contents").val() == "") {
            swal({
                type: 'success',
                text: '서비스공지 내용을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if ($("#isfix").val() == null || $("#isfix").val() == "" || $("#isfix").val() == undefined) {
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
        }
        if (fileSize > maxSize) {
            swal({
                type: 'error',
                text: ' 2MB 이하의 파일만 업로드 할 수 있습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if (fileName.search(/\s/) != -1) {
            swal({
                type: 'info',
                text: "파일명은 공백없이 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        var chkAuthSel = $("input[name='chkAuth']").is(":checked");

        if (!chkAuthSel) {
            swal({
                type: 'info',
                text: '읽기권한을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
            return;
        } else {
            if ($("#inlineCheckbox1-1").is(":checked")) {
                $("#login").val('true');
                $("#org").val('true');
                $("#submit").val('true');
            }
            if ($("#inlineCheckbox1-2").is(":checked")) {
                $("#login").val('true');
            }
            if ($("#inlineCheckbox1-3").is(":checked")) {
                $("#org").val('true');
            }
            if ($("#inlineCheckbox1-4").is(":checked")) {
                $("#submit").val('true');
            }
        }

        doinsert();
    });

    });
</script>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
<script src="/assets/smarteditor2/workspace/js/service/HuskyEZCreator.js"></script>
<script>
    var oneDepth = "adm-nav-4";
    var twoDepth = "adm-sub-07";
</script>

<!-- session -->
<s:authentication property="principal.name" var="name"/> <!-- 사용자이름 -->
<s:authentication property="principal.email" var="email"/> <!-- 이메일 -->
<s:authentication property="principal.loginId" var="loginId"/> <!-- 로그인ID -->
<!--/ session -->

<script>
    function filesearch() {
        $("input:file").click();
    }

    function cancellBtn() {
        location.href = "/sys/faqSetting";
    }
    function doinsert() {
        var param = {
            id: $("#loginId").val(),
            writer: $("#writer").val().trim(),
            email: $("#email").val(),
            title: $("#title").val(),
            contents: $("#contents").val(),
            option1: $("#login").val(),
            option2: $("#org").val(),
            option3: $("#submit").val(),
            category: $("#category option:selected").val()
        }
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/faqWrite",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    location.href = "/sys/faqSetting";
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

    function role_login() {
        $("#inlineCheckbox1-2").prop("checked", false)
        $("#inlineCheckbox1-3").prop("checked", false)
        var inner_html = '<select id="category"><option value="req">신청/해지</option><option value="site">사이트이용</option><option value="pay">납부</option><option value="noti">고지</option></select>';
        $("#categoryByRole").html(inner_html);
        $("#login").val("true");
        $("#org").val("false");
        $("#submit").val("false");
    }

    function role_org() {
        $("#inlineCheckbox1-1").prop("checked", false)
        $("#inlineCheckbox1-3").prop("checked", false)
        var inner_html = '<select id="category"><option value="site">사이트이용</option><option value="receive">수납</option><option value="additional">부가서비스</option><option value="noti">고지</option></select>';
        $("#categoryByRole").html(inner_html);
        $("#login").val("false");
        $("#org").val("true");
        $("#submit").val("false");
    }

    function role_submit() {
        $("#inlineCheckbox1-1").prop("checked", false)
        $("#inlineCheckbox1-2").prop("checked", false)
        var inner_html = '<select id="category"><option value="pay">납부</option><option value="site">사이트이용</option><option value="cash">현금영수증</option></select>';
        $("#categoryByRole").html(inner_html);
        $("#login").val("false");
        $("#org").val("false");
        $("#submit").val("true");
    }

    function fn_enterkey() {
        $("#contents").focus();
    }
</script>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>자주하는질문 등록</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>게시판관리</a>
            </li>
            <li class="active">
                <strong>자주하는질문 등록</strong>
            </li>
        </ol>
        <p class="page-description">등록된 자주하는질문을 등록하는 화면입니다.</p>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox">
                <div class="ibox-content">
                    <table class="table table-stripped">
                        <tbody>
                        <tr>
                            <th>게시판</th>
                            <td>FAQ</td>
                        </tr>
                        <tr>
                            <th>읽기권한</th>
                            <td>
                                <div class="checkbox checkbox-primary checkbox-inline">
                                    <input type="checkbox" id="inlineCheckbox1-1" onclick="role_login()">
                                    <label for="inlineCheckbox1-1"> 로그인전 </label>
                                    <input type="hidden" id="login" name="login">
                                </div>
                                <div class="checkbox checkbox-primary checkbox-inline">
                                    <input type="checkbox" id="inlineCheckbox1-2" onclick="role_org()">
                                    <label for="inlineCheckbox1-2"> 기관담당자 </label>
                                    <input type="hidden" id="org" name="org">
                                </div>
                                <div class="checkbox checkbox-primary checkbox-inline">
                                    <input type="checkbox" id="inlineCheckbox1-3" onclick="role_submit()">
                                    <label for="inlineCheckbox1-3"> 납부자 </label>
                                    <input type="hidden" id="submit" name="submit">
                                </div>
                                <input type="hidden" id="selectCategory" name="category">
                            </td>
                        </tr>
                        <tr>
                            <th>카테고리</th>
                            <td id="categoryByRole">
                                읽기 권한을 선택해 주세요.
                            </td>
                        </tr>
                        <tr>
                            <td>작성자</td>
                            <td>
                                ${name }
                                <input type="hidden" id="writer" name="writer" value="${name }">
                                <input type="hidden" id="email" name="email" value="${email }">
                                <input type="hidden" id="loginId" value="${loginId }" name="loginId">
                                <input type="hidden" id="phoneNumber" value="${map.phoneNumber }" name="phoneNumber">
                            </td>
                        </tr>
                        <tr>
                            <td>제목</td>
                            <td>
                                <input type="text" class="form-control input-sm" id="title" name="title"
                                       onkeypress="if(event.keyCode==13) {fn_enterkey();}">
                            </td>
                        </tr>
                        <!--  <tr>
                             <td>파일첨부</td>
                             <td>
                                 <input type="text" class="form-control" placeholder="최대 2MB 입력 가능 (jpg, gif, pdf 파일 첨부 가능)" id="filename" readonly="readonly">
                                 <button id="btn-upload" class="" onclick="filesearch()">파일찾기</button>
                                 <input type="file"  name="file" style="display: none;" onchange="document.getElementById('filename').value = this.value" accept=".jpg, .gif, .pdf">
                             </td>
                         </tr> -->
                        <tr>
                            <td>내용</td>
                            <td>
                                <textarea class="form-control" rows="16" id="contents" maxlength="4000"
                                          name="contents"></textarea>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12 text-right">
            <button class="btn btn-lg btn-w-m" onclick="cancellBtn()">취소</button>
            <button class="btn btn-lg btn-w-m btn-primary" id="faqReg">등록</button>
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
            bucketName : "faq"
        });

        $("#faqReg").click(function(){
            editor_object.getById["contents"].exec("UPDATE_CONTENTS_FIELD", []);
            $("#contents").val($("#contents").val().replace(/\uFEFF/gi, ""));
            if ($("#inlineCheckbox1-1").prop("checked") == false && $("#inlineCheckbox1-2").prop("checked") == false && $("#inlineCheckbox1-3").prop("checked") == false) {
                swal({
                    type: 'success',
                    text: '읽기 권한을 선택해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                })
            } else if ($("#title").val() == "") {
                swal({
                    type: 'success',
                    text: '제목을 입력해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                })
            } else if ($("#contents").val() == "") {
                swal({
                    type: 'success',
                    text: '서비스문의 내용을 입력해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                })
            } else {
                if ($("#inlineCheckbox1-1").is(":checked")) {
                    $("#login").val('true');
                }
                if ($("#inlineCheckbox1-2").is(":checked")) {
                    $("#org").val('true');
                }
                if ($("#inlineCheckbox1-3").is(":checked")) {
                    $("#submit").val('true');
                }
                $("#selectCategory").val($("#category option:selected").val());
                doinsert();
            }
        });


    });
</script>
<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

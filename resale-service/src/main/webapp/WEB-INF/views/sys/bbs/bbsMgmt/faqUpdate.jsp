<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
<script src="/assets/smarteditor2/workspace/js/service/HuskyEZCreator.js"></script>
<script>
    var oneDepth = "adm-nav-4";
    var twoDepth = "adm-sub-07";
</script>

<script>
    function cancellBtn() {
        location.href = "/sys/faqSetting";
    }

    function file_search() {
        $("input:file").click();
    }

    function inlineCheckbox1() {
        $("#inlineCheckbox2-2").prop("checked", false)
    }

    function inlineCheckbox2() {
        $("#inlineCheckbox2-1").prop("checked", false)
    }
</script>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>자주하는질문 수정</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>게시판관리</a>
            </li>
            <li class="active">
                <strong>자주하는질문 수정</strong>
            </li>
        </ol>
        <p class="page-description">등록된 자주하는질문을 수정하는 화면입니다.</p>
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
                                        <input type="checkbox" id="inlineCheckbox1-2" value="option1" disabled="disabled" <c:if test="${map.dto.bbs <= 57 && map.dto.bbs >= 51}">checked</c:if>>
                                        <label for="inlineCheckbox1-2"> 로그인전 </label>
                                    </div>
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox1-3" value="option1" disabled="disabled" <c:if test="${map.dto.bbs <= 67 && map.dto.bbs >= 61}">checked</c:if>>
                                        <label for="inlineCheckbox1-3"> 기관담당자 </label>
                                    </div>
                                    <div class="checkbox checkbox-primary checkbox-inline">
                                        <input type="checkbox" id="inlineCheckbox1-4" value="option1" disabled="disabled" <c:if test="${map.dto.bbs <= 77 && map.dto.bbs >= 71}">checked</c:if>>
                                        <label for="inlineCheckbox1-4"> 납부자 </label>
                                    </div>
                                    <input type="hidden" id="no" value="${map.dto.no }">
                                </td>
                            </tr>
                            <tr>
                                <th>카테고리</th>
                                <td>
                                    <c:if test="${map.dto.bbs % 10 == 1}">신청/해지</c:if>
                                    <c:if test="${map.dto.bbs % 10 == 2}">납부</c:if>
                                    <c:if test="${map.dto.bbs % 10 == 3}">사이트 이용</c:if>
                                    <c:if test="${map.dto.bbs % 10 == 4}">고지</c:if>
                                    <c:if test="${map.dto.bbs % 10 == 5}">수납</c:if>
                                    <c:if test="${map.dto.bbs % 10 == 6}">부가서비스</c:if>
                                    <c:if test="${map.dto.bbs % 10 == 7}">현금영수증</c:if>
                                </td>
                            </tr>
                            <tr>
                                <td>작성자</td>
                                <td id="writer">${map.dto.writer }</td>
                            </tr>
                            <tr>
                                <td>제목</td>
                                <td><input type="text" class="form-control input-sm" value="${map.dto.title }" id="title"></td>
                            </tr>
                            <%-- <tr>
                                <td>파일첨부</td>
                                <td>
                                    <input type="text" class="form-control" placeholder="최대 2MB 입력 가능 (jpg, gif, pdf 파일 첨부 가능)" id="filename" readonly="readonly" value="${map.dto.filename }">
                                    <button id="btn-upload" class="btn btn-sm btn-d-gray ml-1" onclick="file_search()">파일찾기</button>
                                    <input type="file"  name="file" style="display: none;" onchange="document.getElementById('filename').value = this.value" accept=".jpg, .gif, .pdf">
                                    <input type="hidden" name="ip" id="getip">
                                </td>
                            </tr> --%>
                            <tr>
                                <td>내용</td>
                                <td><textarea class="form-control" rows="16" id="contents" style="resize:none;">${map.dto.contents}</textarea></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12 text-right">
            <button class="btn btn-lg btn-w-m btn-secondary" onclick="cancellBtn()">취소</button>
            <button class="btn btn-lg btn-w-m btn-primary" id="doUpdate">수정</button>
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

        $("#doUpdate").click(function(){
            editor_object.getById["contents"].exec("UPDATE_CONTENTS_FIELD", []);
            $("#contents").val($("#contents").val().replace(/\uFEFF/gi, ""));
            var param = {
                no: $("#no").val(),
                writer: $("#writer").text().trim(),
                title: $("#title").val(),
                contents: $("#contents").val()
            };
            $.ajax({
                type: "POST",
                async: true,
                url: "/sys/faqUpdate",
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
        });


    });
</script>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

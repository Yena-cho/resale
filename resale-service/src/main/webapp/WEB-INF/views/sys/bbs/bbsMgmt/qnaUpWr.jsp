<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<!-- session -->
<s:authentication property="principal.name" var="name"/> <!-- 사용자이름 -->
<s:authentication property="principal.email" var="email"/> <!-- 이메일 -->
<s:authentication property="principal.loginId" var="loginId"/> <!-- 로그인ID -->
<!--/ session -->

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<script>
    var oneDepth = "adm-nav-4";
    var twoDepth = "adm-sub-08";
</script>

<script>
    function cancellBtn() {
        location.href = "/sys/qnaSetting";
    }

    //수정버튼 클릭
    function updateBtn(no) {
        if ($("#repleno").val() == "" || $("#repleno").val() == null) {
            doInsert()
        } else {
            doUpdate(no)
        }
    }

    function doInsert() {
        if ($("#contents").val() == null || $("#contents").val() == '') {
            swal({
                type: 'info',
                text: '답변을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else {
            var param = {
                fromno: $("#no").val(),
                step: "00",
                id: $("#loginId").val(),
                password: "1",
                writer: $("#writer").val(),
                title: "답변 드립니다.",
                contents: $("#contents").val(),
                code: $("#code").val(),
                email: $("#email").val()
            };

            $.ajax({
                type: "POST",
                async: true,
                url: "/sys/qnaInsert",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(param),
                success: function (result) {
                    if (result.retCode == "0000") {
                        location.href = "/sys/qnaSetting";
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
    }

    function doUpdate(no) {
        if ($("#contents").val() == null || $("#contents").val() == '') {
            swal({
                type: 'info',
                text: '답변을 입력해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            })
        } else {
            var param = {
                no: no,
                contents: $("#contents").val()
            };
            $.ajax({
                type: "POST",
                async: true,
                url: "/sys/qnaUpdate",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(param),
                success: function (result) {
                    if (result.retCode == "0000") {
                        location.href = "/sys/qnaSetting";
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
    }

    function fn_download(file, no) {
        var obj = $("#realfilename").text();
        var obj2 = $("#hfilesize").text();
        var obj3 = $("#hfileext").text();
        var url = "/common/customerAsist/fileDownload";
        $('#ffileName').val(obj);
        $('#ffileSize').val(obj2);
        $('#ffileId').val(file);
        $('#ffileExt').val(obj3);
        $('#ffileNo').val(no);
        document.searchForm.action = url;
        document.searchForm.submit();
    }

    $(document).ready(function () {
        if ($("#realfilename").text() != "") {
            var x = $("#hfilesize").text();
            var s = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'];
            var e = Math.floor(Math.log(x) / Math.log(1024));

            $("#filesize").text('[' + (x / Math.pow(1024, e)).toFixed(2) + " " + s[e] + ']');
        }
    });

</script>

</div>
<form id="searchForm" name="searchForm" method="post">
    <input type="hidden" name="filename" id="ffileName"/>
    <input type="hidden" name="filesize" id="ffileSize"/>
    <input type="hidden" name="fileid" id="ffileId"/>
    <input type="hidden" name="fileext" id="ffileExt"/>
    <input type="hidden" name="fileno" id="ffileNo"/>
</form>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>서비스문의 등록</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>게시판관리</a>
            </li>
            <li class="active">
                <strong>서비스문의 등록</strong>
            </li>
        </ol>
        <p class="page-description">서비스문의에 대한 답변을 등록하는 화면입니다.</p>
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
                                <th>카테고리</th>
                                <td><div id="category">${map.dto.data7 }</div><input type="hidden" id="no" value="${map.dto.no }"></td>
                            </tr>
                            <tr>
                                <th>작성자</th>
                                <td><div>${map.dto.writer }</div></td>
                            </tr>
                            <tr>
                                <th>파일첨부</th>
                                <td>
                                    <a href="#" onclick="fn_download('${map.dto.fileid}', '${map.dto.no}')">${map.dto.filename }</a>
                                    <div id="realfilename" style="display: none;">${map.dto.filename }</div>
                                    <span id="filesize"></span>
                                    <div id="hfilesize" style="display: none;">${map.dto.filesize }</div>
                                </td>
                            </tr>
                            <tr>
                                <th>제목</th>
                                <td><div>${map.dto.title }</div></td>
                            </tr>
                            <tr>
                                <th>내용</th>
                                <td><textarea class="form-control" rows="8" readonly="readonly" style="resize:none;" maxlength="500">${map.dto.contents}</textarea></td>
                            </tr>
                            <tr>
                                <th>답변</th>
                                <td>
                                    <textarea class="form-control" rows="8" id="contents" style="resize:none;" maxlength="500">${map.repledto.contents }</textarea>
                                    <input type="hidden" id="repleno" value="${map.repledto.no }">
                                    <input type="hidden" id="writer" value="${name }">
                                    <input type="hidden" id="eamil" value="${email }">
                                    <input type="hidden" id="loginId" value="${loginId }">
                                    <input type="hidden" id="code" value="${map.dto.code }">
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
            <button class="btn btn-lg btn-w-m btn-secondary" onclick="cancellBtn()">취소</button>
            <button class="btn btn-lg btn-w-m btn-primary" onclick="updateBtn(${map.repledto.no })">수정</button>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

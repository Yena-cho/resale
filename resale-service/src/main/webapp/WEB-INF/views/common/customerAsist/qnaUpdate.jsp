<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<s:authentication property="principal.name" 	var="name"/>
<s:authentication property="principal.email" 	var="email"/>
<s:authentication property="principal.loginId" 	var="loginId"/>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_GROUP_USER')"><jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/header.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/header.jsp" flush="false"/></s:authorize>

<script>

var firstDepth = "nav-link-08";
var secondDepth = "sub-04";
var filepatchCnt = 0;

function clickCancel() {
	location.href = "/common/customerAsist/qnaList";
}
//파일찾기 버튼 클릭
$(function(){
	$('#btn-upload').click(function(e){
		e.preventDefault();
		$("input:file").click();
	});
});

//수정버튼 클릭
function submit(){
	if(valueCheck()){
		var ntitle = basicEscape($("#title").val());
        $("#title").text(ntitle);
		var ncontents = basicEscape($("#qnacontents").val());
        $("#qnacontents").text(ncontents);

		var no = $("#no").val();
		var formData = new FormData($("#fileForm")[0]);
		$.ajax({
			type 		: "POST",
			url 		: "common/customerAsist/qnaUpdateChangeFile",
			data 		: formData,
			processData	: false,
			contentType : false,
			success		: function(result) {
				if(result.retCode == "0000"){
			      	location.href = "/common/customerAsist/qnaDetail?no="+no;
				}else{
					swal({
				           type: 'success',
				           text: result.retCode,
				           confirmButtonColor: '#3085d6',
				           confirmButtonText: '확인'
				       })
				}
			}
		});
	}
}

function valueCheck(){
	var fileName = $("#filename").val();
	var fileext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length).toLowerCase();
	// 사이즈체크
	var maxSize = 2 * 1024 * 1024 //2MB
	var fileSize;
	if(fileName != null && fileName != ""){
		fileSize = document.getElementById("rfile").files[0].size;
	}
	
	if ($("#title").val() == "" ) {
		swal({
	           type: 'success',
	           text: '제목을 입력해 주세요.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       });
	    return false;
	}
	if ($("#qnacontents").val() == ""){
		swal({
	           type: 'success',
	           text: '서비스문의 내용을 입력해 주세요.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       });
	    return false;
	}
	if (fileName != ""
		&& (fileext != 'jpg' && fileext != 'pdf' && fileext != 'gif'
			&& fileext != 'xls' && fileext != 'xlsx')) {
		swal({
			type : 'error',
			text : ' jpg, pdf, gif, 엑셀 파일만 업로드 할 수 있습니다.',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인'
		});
		return false;
	}
	if (fileSize > maxSize) {
		swal({
			type : 'error',
			text : ' 2MB 이하의 파일만 업로드 할 수 있습니다.',
			confirmButtonColor : '#3085d6',
			confirmButtonText : '확인'
		});
		return false;
	}
	 if (fileName.search(/\s/) != -1) {
	    swal({
	        type: 'info',
	        text: "파일명은 공백없이 입력해주세요.",
	        confirmButtonColor: '#3085d6',
	        confirmButtonText: '확인'
	    });
	    return false;
	}
	return true;
}
//엑스버튼 눌러서 해당게시글의 파일 삭제
function deleteFileOnly() {
	var param ={
			no			:	$("#no").val(),
			filename	:	""
	};
	$.ajax({
		type 		: "POST",
		async 		: true,
		url 		: "common/customerAsist/qnaUpdateRemoveFile",
		contentType : "application/json; charset=utf-8",
		data 		: JSON.stringify(param),
		success		: function(result) {
			if(result.retCode == "0000"){
				swal({
			           type: 'success',
			           text: '첨부파일을 삭제하였습니다.',
			           confirmButtonColor: '#3085d6',
			           confirmButtonText: '확인'
			       })
			}else{
				swal({
			           type: 'success',
			           text: '시스템 오류입니다.',
			           confirmButtonColor: '#3085d6',
			           confirmButtonText: '확인'
			       })
			}
		}
	});
	$("#file_id").hide();
}

$(document).ready(function(){
	if($("#oldfilename").text() == "" || $("#oldfilename").text() == null){
		$("#file_id").hide();
	}
});

//수정 버튼 클릭(옛날)
function upBtnClick(){
	var no = $("#no").val();
	if ($("#title").val() == "" ) {
		swal({
	           type: 'success',
	           text: '제목을 입력해 주세요.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       })
	}else if($("#qnacontents").val() == "" ){
		swal({
	           type: 'success',
	           text: '서비스문의 내용을 입력해 주세요.',
	           confirmButtonColor: '#3085d6',
	           confirmButtonText: '확인'
	       })
	}else {
		var param ={
			title	:	$("#title").val(),
			contents:	$("#qnacontents").val(),
			filename:	$("#filename").val(),
			filesize:	$("#filesize").val(),
			no		: 	$("#no").val(),
			data7 	: 	$("#category option:selected").val()
		};
		$.ajax({
			type 		: "POST",
			async 		: true,
			url 		: "common/customerAsist/qnaUpdateExcute",
			contentType : "application/json; charset=utf-8",
			data 		: JSON.stringify(param),
			success		: function(result) {
				if(result.retCode == "0000"){
					location.href = "/common/customerAsist/qnaDetail?no="+no;
				}else{
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


	var writer = $("#writer").text();
	var email = $("#email").text();
	var category = $("#category option:selected").val();
	var title = $("#title").val();
	var contents = $("#qnacontents").val();
}
</script>

            <div id="contents">
                <div class="container">
                    <div id="page-title">
                        <div class="row">
                            <div class="col-6">
                                <h2>서비스문의</h2>
                            </div>
                            <div class="col-6 text-right">
                                <%--<button type="button" class="btn btn-img mr-2">--%>
                                    <%--<img src="/assets/imgs/common/btn-typo-control.png">--%>
                                <%--</button>--%>
                                <%--<button type="button" class="btn btn-img">--%>
                                    <%--<img src="/assets/imgs/common/btn-print.png">--%>
                                <%--</button>--%>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="container mt-4">
                    <div class="row mb-3">
                        <div class="col">
                        	<form id="fileForm" name="fileForm" enctype="multipart/form-data">
                            <table id="qna-view" class="table table-sm table-form table-primary">
                                <tbody>
                                    <tr class="row no-gutters">
                                        <th class="col-md-1 col-sm-4 col-4 text-left">작성자</th>
                                        <td class="col-md-5 col-sm-8 col-8" id="writer">
                                           	${name }
                                        </td>
                                        <th class="col-md-1 col-sm-4 col-4 text-left">E-MAIL</th>
                                        <td class="col-md-5 col-sm-8 col-8" id="email">
                                            ${email }
                                            <input type="hidden" id="phoneNumber" value="${map.phoneNumber }">
                                            <input type="hidden" id="loginId" value="${loginId }">
                                            <input type="hidden" id="no" name="no" value="${map.dto.no }">
                                        </td>
                                    </tr>
                                    <tr class="row no-gutters">
                                        <th class="col-md-1 col-sm-4 col-4 text-left">분류</th>
                                        <td class="col-md-5 col-sm-8 col-8">
                                            <select class="form-control" id="category" name="category">
                                                <option value="고객관리" <c:if test="${map.dto.data7 == '고객관리'}">selected</c:if>>고객관리</option>
                                                <option value="청구" <c:if test="${map.dto.data7 == '청구'}">selected</c:if>>청구</option>
                                                <option value="고지" <c:if test="${map.dto.data7 == '고지'}">selected</c:if>>고지</option>
                                                <option value="수납" <c:if test="${map.dto.data7 == '수납'}">selected</c:if>>수납</option>
                                                <option value="설정" <c:if test="${map.dto.data7 == '설정'}">selected</c:if>>설정</option>
                                                <option value="사이트이용" <c:if test="${map.dto.data7 == '사이트이용'}">selected</c:if>>사이트이용</option>
                                                <option value="신청/해지" <c:if test="${map.dto.data7 == '신청/해지'}">selected</c:if>>신청/해지</option>
                                                <option value="기타" <c:if test="${map.dto.data7 == '기타'}">selected</c:if>>기타</option>
                                            </select>
                                        </td>
                                        <th class="col-md-1 col-sm-4 col-4 text-left">파일첨부</th>
                                        <td class="col-md-5 col-sm-8 col-8 flex-col">
                                            <div class="td-row-in-column">
	                                            <input type="text" class="form-control" placeholder="최대 2MB 입력 가능 (jpg, gif, pdf, xls, xlsx 파일 첨부 가능)" id="filename" readonly="readonly">
	                                            <button id="btn-upload" class="btn btn-sm btn-d-gray ml-1" onfocus="this.blur();" onfocus="this.blur();" type="button">파일찾기</button>
	                                            <input type="file"  name="file" id="rfile" style="display: none;" onchange="document.getElementById('filename').value = this.value" accept=".jpg, .gif, .pdf">
                                            </div>
                                             <div class="td-row-in-column exist-file" id="file_id">
                                                <span id="oldfilename">${map.dto.filename }</span>
                                               	<span class="btn-close font-red ml-2" onclick="deleteFileOnly()" >x</span>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class="row no-gutters">
                                        <th class="col-md-1 col-sm-4 col-4 text-left">제목</th>
                                        <td class="col-md-11 col-sm-8 col-8">
                                            <input type="text" class="form-control" id="title" value="${map.dto.title }" name="title" maxlength="200">
                                        </td>
                                    </tr>
                                    <tr class="row no-gutters">
                                        <td colspan="4" class="col content-area align-items-start">
                                            <textarea class="form-control" rows="15" id="qnacontents" name="contents" maxlength="2000">${map.dto.contents }</textarea>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            </form>
                        </div>
                    </div>
                    <div class="row mb-5">
                        <div class="col text-center">
                            <button class="btn btn-primary btn-outlined btn-wide" onclick="clickCancel()">
                               	 취소
                            </button>
                            <button class="btn btn-primary btn-wide" onclick="submit()">
                               	 수정
                            </button>
                        </div>
                    </div>
                </div>
            </div>

<s:authorize access="hasRole('ROLE_ADMIN')"><jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_CHACMS')"><jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_GROUP_USER')"><jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="hasRole('ROLE_USER')"><jsp:include page="/WEB-INF/views/include/payer/footer.jsp" flush="false"/></s:authorize>
<s:authorize access="isAnonymous()"><jsp:include page="/WEB-INF/views/include/footer.jsp" flush="false"/></s:authorize>
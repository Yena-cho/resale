<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>

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

$(function(){
	$('#btn-upload').click(function(e){
		e.preventDefault();
		$("input:file").click();
	});
});


function clickCancel() {
	location.href = "/common/customerAsist/qnaList";
}
//글쓰기
function regBtClick(){
		var fileName = $("#filename").val();
		var fileext = "";
		// 사이즈체크
		var maxSize = 2 * 1024 * 1024 //2MB
		var fileSize =0;
		if(fileName != null && fileName != ""){
			fileext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length).toLowerCase();
			fileSize = document.getElementById("rfile").files[0].size;
		}

		if ($("#title").val() == "") {
			swal({
				type : 'success',
				text : '제목을 입력해 주세요.',
				confirmButtonColor : '#3085d6',
				confirmButtonText : '확인'
			})
		} else if ($("#qnacontents").val() == "") {
			swal({
				type : 'success',
				text : '서비스문의 내용을 입력해 주세요.',
				confirmButtonColor : '#3085d6',
				confirmButtonText : '확인'
			})
		} else if (fileName != ""
				&& (fileext != 'jpg' && fileext != 'pdf' && fileext != 'gif'
						&& fileext != 'xls' && fileext != 'xlsx')) {
			swal({
				type : 'error',
				text : ' jpg, pdf, gif, 엑셀 파일만 업로드 할 수 있습니다.',
				confirmButtonColor : '#3085d6',
				confirmButtonText : '확인'
			});
		} else if (fileSize > maxSize) {
			swal({
				type : 'error',
				text : ' 2MB 이하의 파일만 업로드 할 수 있습니다.',
				confirmButtonColor : '#3085d6',
				confirmButtonText : '확인'
			});
		} 
// 		else if (fileName.search(/\s/) != -1) {
//             swal({
//                 type: 'info',
//                 text: "파일명은 공백없이 입력해주세요.",
//                 confirmButtonColor: '#3085d6',
//                 confirmButtonText: '확인'
//             });
//         }
		else {
            $("#title").text(basicEscape($("#title").val()));
            $("#qnacontents").text(basicEscape($("#qnacontents").val()));

			var formData = new FormData($("#fileForm")[0]);
			$.ajax({
				type : "POST",
				url : "common/customerAsist/qnaInsert",
				data : formData,
				processData : false,
				contentType : false,
				success : function(result) {
					if (result.retCode == "0000") {
						location.href = "/common/customerAsist/qnaList";
					} else if (result.retCode == "8888") {
                        swal({
                            type : 'error',
                            text : result.retMsg,
                            confirmButtonColor : '#3085d6',
                            confirmButtonText : '확인'
                        })
                    } else {
						swal({
							type : 'error',
							text : result.retMsg,
							confirmButtonColor : '#3085d6',
							confirmButtonText : '확인'
						})
						return;
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
                                           	<input type="hidden" name="writer" value="${name }">
                                        </td>
                                        <th class="col-md-1 col-sm-4 col-4 text-left">E-MAIL</th>
                                        <td class="col-md-5 col-sm-8 col-8" id="email">
                                            ${email }
                                            <input type="hidden" name="email" value="${email }">
                                            <input type="hidden" id="loginId" value="${loginId }" name="loginId">
                                            <input type="hidden" id="phoneNumber" value="${map.phoneNumber }" name="phoneNumber">
                                        </td>
                                    </tr>
                                    <tr class="row no-gutters">
                                        <th class="col-md-1 col-sm-4 col-4 text-left">분류</th>
                                        <td class="col-md-5 col-sm-8 col-8">
                                            <select class="form-control" id="category" name="category">
                                                <option value="고객관리">고객관리</option>
                                                <option value="청구">청구</option>
                                                <option value="고지">고지</option>
                                                <option value="수납">수납</option>
                                                <option value="설정">설정</option>
                                                <option value="사이트이용">사이트이용</option>
                                                <option value="신청/해지">신청/해지</option>
                                                <option value="기타">기타</option>
                                            </select>
                                        </td>
                                        <th class="col-md-1 col-sm-4 col-4 text-left">파일첨부</th>
                                        <td class="col-md-5 col-sm-8 col-8">
                                            <input type="text" class="form-control" placeholder="최대 2MB 입력 가능 (jpg, gif, pdf, xls, xlsx 파일 첨부 가능)" id="filename" readonly="readonly">
                                            <button id="btn-upload" class="btn btn-sm btn-d-gray ml-1" onfocus="this.blur();" type="button">파일찾기</button>
                                            <input type="file"  name="file" id="rfile" style="display: none;" onchange="document.getElementById('filename').value = this.value" accept=".jpg, .gif, .pdf">
                                        </td>
                                    </tr>
                                    <tr class="row no-gutters">
                                        <th class="col-md-1 col-sm-4 col-4 text-left">제목</th>
                                        <td class="col-md-11 col-sm-8 col-8">
                                            <input type="text" class="form-control has-counter" id="title" maxlength="200" name="title">
                                        </td>
                                    </tr>
                                    <tr class="row no-gutters">
                                        <td colspan="4" class="col content-area align-items-start">
                                            <textarea class="form-control has-counter" rows="15" id="qnacontents" maxlength="2000" name="contents" style="border: 1px solid #d3d3d3;"></textarea>
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
                            <button class="btn btn-primary btn-wide" onclick="regBtClick()">
                               	 등록
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
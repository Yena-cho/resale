<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/views/include/group/header.jsp" flush="false"/>

<script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>

<script type="text/javascript">
    var firstDepth = "nav-link-37";
    var secondDepth = false;
</script>

<input type="hidden" id="chaCd"  name="chaCd"  value="${map.groupId}">
<input type="hidden" id="authGb" name="authGb" value="GROUP">

<div id="contents" class="group-contents">
    <div class="container">
        <div id="page-title">
            <div id="breadcrumb-in-title-area" class="row align-items-center">
                <div class="col-12 text-right">
                    <img src="/assets/imgs/common/icon-home.png" class="mr-2">
                    <span> > </span>
                    <span class="depth-1">기관목록</span>
                    <span> > </span>
                    <span class="depth-2 active">기본정보관리</span>
                </div>
            </div>
            <div class="row">
                <div class="col-6">
                    <h2>기본정보관리</h2>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div id="page-description">
            <div class="row">
                <div class="col-12">
                    <p>기관 기본 정보를 조회/변경하는 화면입니다.</p>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-4">
                <h5>기관정보관리</h5>
            </div>
            <div class="col-8 text-right">
                <span class="guide-mention font-blue">
                    기관 기본 정보 변경 시 가상계좌 수납관리 서비스 고객센터 문의(02-786-8201)
                </span>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col">
                <table class="table table-form">
                    <tbody class="container-fluid">
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">그룹아이디</th>
                            <td class="col-md-4 col-sm-8 col-8">${map.loginId}</td>
                            <th class="col-md-2 col-sm-4 col-4">비밀번호</th>
                            <td class="col-md-4 col-sm-8 col-8">
                                <button type="button" class="btn btn-sm btn-d-gray" onclick="pwdReset();">재설정</button>
                            </td>
                        </tr>
                        <tr class="row no-gutters">
                            <th class="col-md-2 col-sm-4 col-4">그룹명</th>
                            <td class="col-md-4 col-sm-8 col-8">${map.groupName}</td>
                            <th class="col-md-2 col-sm-4 col-4">그룹코드</th>
                            <td class="col-md-4 col-sm-8 col-8">${map.groupId}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
    function pwdReset(){
        $('#password-reset-popup').modal({
            backdrop : 'static',
            keyboard : false
        });
    }
</script>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" />

<%-- 비밀번호변경 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/password-reset.jsp" flush="false"/>

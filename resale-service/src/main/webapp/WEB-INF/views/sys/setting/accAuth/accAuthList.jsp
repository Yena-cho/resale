<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<script>
    var oneDepth = "adm-nav-7";
    var twoDepth = "adm-sub-24";
</script>


<script>
    var cuPage = 1;

    //계정추가
    function fn_appAuthAdd(str) {
        $('#insGubn').val('I');
        if (str == 'BANK') { // 은행본부
            $('#admGroup').val('BA');
        } else {			// 운영팀
            $('#admGroup').val('DA');
        }
        // 초기화
        $('#btnCon').show();
        $('#admId').attr('disabled', false);
        $('#admId').val('');
        $('#admName').val('');
        $('#admHp').val('');
        $('#mailId').val('');
        $('#mailDomain1').attr('disabled', false);
        $('#mailDomain1').val('');
        $('#mailDomain2').val('');
        $('#admRank').val('');
        $('#admDept').val('');
        $('#by-st01').attr('checked', true);
        $('.password-change-area').css('display', 'block');
        $('#admPw').val('');
        $('#btn_up').hide();
        $('#resetIdSuc').hide();
        $('#resetIdErr').hide();

        $('#popup-account-add-modify').modal({backdrop: 'static', keyboard: false});
    }

    // tab 선택
    function fn_selTab(str) {
        if (str == 'bank') { // 은행본부
            $('#admGroup').val('BA');
        } else {			// 운영팀
            $('#admGroup').val('DA');
        }
        fn_selAcc();
    }

    // 조회
    function fn_selAcc(page) {
        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }
        var url = "sys/setting/selAccAuth";
        var param = {
            admGroup: $('#admGroup').val(),
            curPage: cuPage
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                if ($('#admGroup').val() == 'BA') {
                    fn_bankGrid(data, 'bankGrid');
                    sysajaxPaging(data, 'ModalPageArea1');
                } else {
                    fn_damoaGrid(data, 'damoaGrid');
                    sysajaxPaging(data, 'ModalPageArea2');
                }
            }
        });
    }

    // 은행본부 목록
    function fn_bankGrid(data, obj) {
        var str = '';
        if (data.count > 0) {
            $.each(data.list, function (k, v) {
                str += '<tr>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + v.admId + '</td>';
                str += '<td>' + v.admName + '</td>';
                str += '<td>' + v.admHp + '</td>';
                str += '<td>' + v.admEmail + '</td>';
                if (v.admStatus == 'ST01') {
                    str += '<td class="text-success">정상</td>';
                } else {
                    str += '<td class="text-danger">정지</td>';
                }
                str += '<td>';
                str += '<button type="button" class="btn btn-xs btn-info btn-open-account-add-modify" onclick="fn_update(\'' + v.admId + '\');">수정</button>';
                str += '<button type="button" class="btn btn-xs btn-danger" onclick=fn_delete(\'' + v.admId + '\');>삭제</button>';
                str += '</td>';
                str += '</tr>';
            });
        } else {
            str += '<tr><td colspan="6" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        }
        $('#' + obj).html(str);
    }

    // 운영팀 목록
    function fn_damoaGrid(data, obj) {
        var str = '';
        if (data.count > 0) {
            $.each(data.list, function (k, v) {
                str += '<tr>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + v.admId + '</td>';
                str += '<td>' + v.admName + '</td>';
                str += '<td>' + v.admHp + '</td>';
                str += '<td>' + v.admEmail + '</td>';
                if (v.admStatus == 'ST01') {
                    str += '<td class="text-success">정상</td>';
                } else {
                    str += '<td class="text-danger">정지</td>';
                }
                str += '<td>';
                str += '<button type="button" class="btn btn-xs btn-info btn-open-account-add-modify" onclick="fn_update(\'' + v.admId + '\');">수정</button>';
                str += '<button type="button" class="btn btn-xs btn-danger" onclick=fn_delete(\'' + v.admId + '\');>삭제</button>';
                str += '</td>';
                str += '</tr>';
            });
        } else {
            str += '<tr><td colspan="6" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        }
        $('#' + obj).html(str);
    }

    function list(page) {
        fn_selAcc(page);
    }

    // 상세보기
    function fn_update(id) {
        $('#insGubn').val('U');
        var url = "sys/setting/accDetail";
        var param = {
            admId: id
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                $('#btnCon').hide();
                $('#admId').attr('disabled', true);
                $('#admId').val(data.map.admId);
                $('#admName').val(data.map.admName);
                $('#admHp').val(data.map.admHp);
                $('#mailId').val(data.map.mailId);
                $('#mailDomain1').attr('disabled', true);
                $('#mailDomain1').val(data.map.mailDomain);
                if ($('#mailDomain2').val() != data.map.mailDomain) {
                    $('#mailDomain2').val('');
                } else {
                    $('#mailDomain2').val(data.map.mailDomain);
                }
                $('#admRank').val(data.map.admRank);
                $('#admDept').val(data.map.admDept);
                if (data.map.admStatus == 'ST01') {
                    $('#by-st01').prop('checked', true);
                    $('#by-st02').prop('checked', false);
                } else {
                    $('#by-st02').prop('checked', true);
                    $('#by-st01').prop('checked', false);
                }
                $('#resetIdSuc').hide();
                $('#resetIdErr').hide();
                $('.password-change-area').css('display', 'none');
                $('#passGubn').val('U');
                $('#admPw').val('');
                $('#btn_up').show();
                $('#btn_up').text('비밀번호 수정');
            }
        });
        $('#popup-account-add-modify').modal({backdrop: 'static', keyboard: false});
    }

    // 삭제
    function fn_delete(id) {
        swal({
            type: 'question',
            html: "삭제하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                var url = "sys/setting/accDelete";
                var param = {
                    admId: id
                };
                $.ajax({
                    type: "post",
                    async: true,
                    url: url,
                    contentType: "application/json; charset=UTF-8",
                    data: JSON.stringify(param),
                    success: function (data) {
                        swal({
                            type: 'success',
                            text: '삭제되었습니다.',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        }).then(function (result) {
                            if (result.value) {
                                fn_selAcc();
                            }
                        });
                    }
                });
            }
        });
    }


</script>

<form id="frm" name="frm" method="post">
    <input type="hidden" id="admGroup" name="admGroup" value="BA">
    <input type="hidden" id="insGubn" name="insGubn" value="U">
</form>

</div>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>계정/권한관리</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>설정관리</a>
            </li>
            <li class="active">
                <strong>계정/권한관리</strong>
            </li>
        </ol>
        <p class="page-description">은행본부 및 운영팀의 계정/권한을 관리하는 화면입니다.</p>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight article">
    <div class="row">
        <div class="col-lg-12">
            <div class="tabs-container">
                <ul class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#bank" onclick="fn_selTab('bank');">은행본부</a></li>
                    <li class=""><a data-toggle="tab" href="#operation" onclick="fn_selTab('damoa');">운영팀</a></li>
                </ul>
                <div class="tab-content">
                    <div id="bank" class="tab-pane active">
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-stripped table-align-center">
                                    <colgroup>
                                        <col width="68">
                                        <col width="440">
                                        <col width="300">
                                        <col width="250">
                                        <col width="250">
                                        <col width="150">
                                        <col width="150">
                                    </colgroup>

                                    <thead>
                                        <tr>
                                            <th>NO</th>
                                            <th>아이디</th>
                                            <th>사용자명</th>
                                            <th>연락처</th>
                                            <th>E-MAIL</th>
                                            <th>계정상태</th>
                                            <th>정보수정</th>
                                        </tr>
                                    </thead>

                                    <tbody id="bankGrid">
                                        <c:choose>
                                            <c:when test="${map.count > 0 }">
                                                <c:forEach var="rows" items="${map.list}">
                                                    <tr>
                                                        <td>${rows.rn}</td>
                                                        <td>${rows.admId}</td>
                                                        <td>${rows.admName}</td>
                                                        <td>${rows.admHp}</td>
                                                        <td>${rows.admEmail}</td>
                                                        <c:if test="${rows.admStatus == 'ST01'}">
                                                            <td class="text-success">정상</td>
                                                        </c:if>
                                                        <c:if test="${rows.admStatus != 'ST01'}">
                                                            <td class="text-danger">정지</td>
                                                        </c:if>
                                                        <td>
                                                            <button type="button"
                                                                    class="btn btn-xs btn-info btn-open-account-add-modify">
                                                                수정
                                                            </button>
                                                            <button type="button" class="btn btn-xs btn-danger">삭제</button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <tr>
                                                    <td colspan="6" style="text-align: center;">[조회된 내역이 없습니다.]</td>
                                                </tr>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>

                            <div class="row m-b-md">
                                <div class="col-lg-12 text-right">
                                    <button type="button" class="btn btn-primary" onclick="fn_appAuthAdd('BANK');"><i class="fa fa-fw fa-plus"></i>계정추가</button>
                                </div>
                            </div>

                            <div class="row m-b-lg">
                                <div class="col-lg-12 text-center">
                                    <!-- paging -->
                                    <div class="row m-b-lg">
                                        <div class="col-lg-12 text-center">
                                            <div class="btn-group" id="ModalPageArea1">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="operation" class="tab-pane">
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-stripped table-align-center">
                                    <colgroup>
                                        <col width="68">
                                        <col width="440">
                                        <col width="300">
                                        <col width="250">
                                        <col width="250">
                                        <col width="150">
                                        <col width="150">
                                    </colgroup>

                                    <thead>
                                        <tr>
                                            <th>NO</th>
                                            <th>아이디</th>
                                            <th>사용자명</th>
                                            <th>연락처</th>
                                            <th>E-MAIL</th>
                                            <th>계정상태</th>
                                            <th>정보수정</th>
                                        </tr>
                                    </thead>

                                    <tbody id="damoaGrid">
                                        <c:choose>
                                            <c:when test="${map.count > 0 }">
                                                <c:forEach var="rows" items="${map.list}">
                                                    <tr>
                                                        <td>${rows.rn}</td>
                                                        <td>${rows.admId}</td>
                                                        <td>${rows.admPw}</td>
                                                        <td>${rows.admName}</td>
                                                        <c:if test="${rows.admStatus == 'ST01'}">
                                                            <td class="text-success">정상</td>
                                                        </c:if>
                                                        <c:if test="${rows.admStatus != 'ST01'}">
                                                            <td class="text-danger">정지</td>
                                                        </c:if>
                                                        <td>
                                                            <button type="button"
                                                                    class="btn btn-xs btn-info btn-open-account-add-modify">
                                                                수정
                                                            </button>
                                                            <button type="button" class="btn btn-xs btn-danger">삭제</button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <tr>
                                                    <td colspan="6" style="text-align: center;">[조회된 내역이 없습니다.]</td>
                                                </tr>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>

                            <div class="row m-b-md">
                                <div class="col-lg-12 text-right">
                                    <button type="button" class="btn btn-primary" onclick="fn_appAuthAdd('DAMOA');"><i class="fa fa-fw fa-plus"></i>계정추가</button>
                                </div>
                            </div>

                            <div class="row m-b-lg">
                                <div class="col-lg-12 text-center">
                                    <!-- paging -->
                                    <div class="row m-b-lg">
                                        <div class="col-lg-12 text-center">
                                            <div class="btn-group" id="ModalPageArea2"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- 계정 추가/수정 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/account-add-modify.jsp" flush="false"/>

<script>
    $(document).ready(function () {
        fn_selAcc(1);
        
        $('#mailDomain2').on('change', function (e) {
    	    var optionSelected = $("option:selected", this);
    	    var valueSelected = this.value;
    	    if(valueSelected =='') {
    	    	$('#mailDomain1').attr('disabled', false);
    		} else 
    			$('#mailDomain1').attr('disabled', false);
    	});
    });
</script>

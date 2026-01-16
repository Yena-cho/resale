<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script>
    var oneDepth = "adm-nav-6";
    var twoDepth = "adm-sub-35-1";
</script>

<script type="text/javascript">
    var cuPage = 1;

    function fnSearch(page) {
        if (page == null || typeof page == 'undefined') {
            cuPage = 1;
        } else {
            cuPage = page;
        }

        var startday = $("#startday").val().replace(/\./gi, "");
        var endday = $("#endday").val().replace(/\./gi, "");

        if (getCurrentDate() < endday || getCurrentDate() < startday) {
            swal({
                type: 'info',
                text: "신청일자를 정확히 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        if (endday < startday) {
            swal({
                type: 'info',
                text: "신청일자를 정확히 입력해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return false;
        }

        var param = {
            fDate: startday,                                           //조회 시작일자
            tDate: endday,                                             //조회 종료일자
            chaCd: $("#chaCd").val(),
            chaName: $("#chaName").val(),
            searchGb: $('#searchGb option:selected').val(),             //검색구분 옵션
            searchValue: $('#searchValue').val(),                       //검색구분 입력값
            statusCheck: $("input[name=statusCheck]:checked").val(),    //신청상태
            searchOrderBy: $('#searchOrderBy option:selected').val(),   //정렬
            curPage: cuPage,
            pageScale: $('#pageScale option:selected').val()
        };
        $.ajax({
            type: "post",
            async: true,
            url: "/sys/addServiceMgmtA/atRegManageAjax",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'resultBody'); // 현재 데이터로 셋팅
                    sysajaxPaging(result, 'PageArea');
                } else {
                    swal({
                        type: 'error',
                        text: result.retMsg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            }
        });
    }

    // 데이터 새로고침
    function fnGrid(result, obj) {
        var str = '';
        $('#waitCount').text(result.waitCount);
        $('#totalCount').text(result.count);

        if (result.count < 1) {
            str += '<tr><td colspan="13" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td><input type="hidden" id="atHisNo" name="atHisNo" value="' + v.no + '"/>' + defaultString(v.rn) + '</td>';
                str += '<td>' + moment(v.atRegDt, 'YYYY-MM-DD HH:mm:ss').format('YYYY.MM.DD') + '</td>';
                str += '<td>' + defaultString(v.chaCd) + '</td>';
                str += '<td>' + defaultString(v.chaName) + '</td>';
                str += '<td>' + defaultString(v.atChaName) + ' <button type="button" class="btn btn-xs btn-primary btn-assign-cha-info" onclick="openUpdateChaName(\'' + v.chaCd +'\', \'' + defaultString(v.atChaName) + '\', \'' + v.no + '\');">수정</button></td>';
                str += '<td>' + defaultString(v.chaStatus) + '</td>';
                str += '<td>' + defaultString(v.loginId) + '</td>';
                str += '<td>' + defaultString(v.owner) + '</td>';
                str += '<td>' + defaultString(v.chaOffNo) + '</td>';
                str += '<td>' + defaultString(v.chrName) + '</td>';
                str += '<td>' + defaultString(v.chrTelNo) + '</td>';

                if (v.atYn === 'W') {
                    str += '<td>승인대기</td>';
                    str += '<td>';
                    str += '<button type="button" class="btn btn-xs btn-primary btn-assign-cha-info" onclick="atAcpt(\'' + v.chaCd +'\', \'' + v.no + '\');">승인</button>';
                    str += '<button type="button" class="btn btn-xs btn-warning btn-assign-cha-info" onclick="atDecl(\'' + v.chaCd +'\', \'' + v.no + '\');">거절</button>';
                    str += '</td>';
                    str += '<td></td>';
                } else if (v.atYn === 'N') {
                    str += '<td>거절</td>';
                    str += '<td>' + moment(v.atAcptDt, 'YYYY-MM-DD HH:mm:ss').format('YYYY.MM.DD') + '</td>';
                    str += '<td></td>';
                } else if (v.atYn === 'Y') {
                    str += '<td>승인완료</td>';
                    str += '<td>' + moment(v.atAcptDt, 'YYYY-MM-DD HH:mm:ss').format('YYYY.MM.DD') + '</td>';
                    str += '<td>';
                    str += '<button type="button" class="btn btn-xs btn-danger btn-assign-cha-info" onclick="atTerminate(\'' + v.chaCd +'\', \'' + v.no + '\');">해지</button>';
                    str += '</td>';
                } else if (v.atYn === 'D') {
                    str += '<td>해지</td>';
                    str += '<td>' + moment(v.atAcptDt, 'YYYY-MM-DD HH:mm:ss').format('YYYY.MM.DD') + '</td>';
                    str += '<td>' + moment(v.atCnclDt, 'YYYY-MM-DD HH:mm:ss').format('YYYY.MM.DD') + '</td>';
                }
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    // 알림톡 승인
    function atAcpt(chaCd, atHisNo) {
        swal({
            type: 'question',
            html: '알림톡 서비스 신청을 승인하시겠습니까?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                updateAtAcptDt(chaCd, 'Y', atHisNo)
            }
        });
    }

    // 알림톡 거절
    function atDecl(chaCd, atHisNo) {
        swal({
            type: 'question',
            html: '알림톡 서비스 신청을 거절하시겠습니까?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                updateAtAcptDt(chaCd, 'N', atHisNo)
            }
        });

    }

    // 알림톡 승인 or 거절 Ajax
    function updateAtAcptDt(chaCd, atYn, atHisNo) {
        var param = {
            chaCd: chaCd,
            statusCheck: atYn,   //Y : 승인, N : 거절
            atHisNo: atHisNo
        };
        $.ajax({
            type: "post",
            url: "/sys/addServiceMgmtA/updateAtAcptDt",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    swal({
                        type: 'info',
                        text: '알림톡 서비스 신청을 ' + result.retMsg + '하였습니다.',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                    fnSearch($('#pageNo').val());
                } else {
                    swal({
                        type: 'error',
                        text: result.retMsg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    });
                }
            }
        });
    }

    // 알림톡 해지
    function atTerminate(chaCd, atHisNo){
        swal({
            type: 'question',
            html: '알림톡 서비스 이용을 해지하시겠습니까?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                var param = {
                    chaCd: chaCd,
                    atHisNo: atHisNo
                };
                $.ajax({
                    type: "post",
                    url: "/sys/addServiceMgmtA/deleteAtYn",
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (result) {
                        if (result.retCode == "0000") {
                            swal({
                                type: 'info',
                                text: '알림톡 서비스 이용을 해지하였습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                            fnSearch($('#pageNo').val());
                        } else {
                            swal({
                                type: 'error',
                                text: result.retMsg,
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        }
                    }
                });
            }
        });
    }

    // 발송기관명 변경 팝업
    function openUpdateChaName(chaCd, atChaName, atHisNo) {
        $('#popAtchaName').val(atChaName);
        $('#popChaCd').val(chaCd);
        $("#popAtHisNo").val(atHisNo);

        $('#atchaname-update-popup').modal({
            backdrop: 'static',
            keyboard: false
        });
    }

    // 페이징 버튼
    function list(page) {
        $('#pageNo').val(page);
        fnSearch(page);
    }

    // 파일 저장
    function fn_fileSave() {
        var rowCnt = $('#totalCount').text();
        if(rowCnt == 0) {
            swal({
                type: 'info',
                text: '다운로드할 데이터가 없습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        swal({
            type: 'question',
            html: "다운로드 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function(result) {
            if (result.value) {
                $('#fDate').val($('#startday').val().replace(/\./g,''));
                $('#tDate').val($('#endday').val().replace(/\./g,''));
                $('#fChaCd').val($('#chaCd').val());
                $('#fChaName').val($('#chaName').val());
                $('#fSearchGb').val($('#searchGb option:selected').val());
                $('#fSearchValue').val($('#searchValue').val());
                $('#fStatusCheck').val($("input[name=statusCheck]:checked").val());
                $('#fSearchOrderBy').val($('#searchOrderBy option:selected').val());

                document.fileForm.action = "/sys/addServiceMgmtA/atRegExcelDown";
                document.fileForm.submit();
            }
        });
    }

</script>

<form id="fileForm" name="fileForm" method="post" action="/sys/addServiceMgmtA/atRegExcelDown">
    <input type="hidden" id="fDate" name="fDate" value=""/>
    <input type="hidden" id="tDate" name="tDate" value=""/>
    <input type="hidden" id="fChaCd" name="fChaCd" value=""/>
    <input type="hidden" id="fChaName" name="fChaName" value=""/>
    <input type="hidden" id="fSearchOrderBy" name="fSearchOrderBy" value=""/>
    <input type="hidden" id="fStatusCheck" name="fStatusCheck" value=""/>
    <input type="hidden" id="fSearchGb" name="fSearchGb" value=""/>
    <input type="hidden" id="fSearchValue" name="fSearchValue" value=""/>
</form>

<input type="hidden" id="pageNo" name="pageNo" value="1">

</div>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>알림톡서비스 신청관리</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>부가서비스관리</a>
            </li>
            <li class="active">
                <strong>알림톡서비스신청관리</strong>
            </li>
        </ol>
        <p class="page-description">이용기관별 알림톡 신청내역을 관리하는 화면입니다.</p>
    </div>
    <div class="col-lg-2"></div>
</div>

<div class="wrapper-content">
    <div class="animated fadeInRight article">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>검색</h5>
                    </div>

                    <div class="ibox-content">
                        <form>
                            <div class="row">
                                <div class="col-md-12">
                                    <label class="form-label block">신청일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" onkeyup="onlyNumber(this)" class="input-sm form-control" name="startday" id="startday" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" onkeyup="onlyNumber(this)" class="input-sm form-control" name="endday" id="endday" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline active" name="btnSetMonth" id="btnSetMonth0" onclick="setMonthTerm(0);">전체</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth" id="btnSetMonth1" onclick="setMonthTerm(1);">1개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth" id="btnSetMonth6" onclick="setMonthTerm(6);">6개월</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chaCd" id="chaCd" maxlength="50">
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">기관명</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chaName" id="chaName" maxlength="50">
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">검색구분</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
											<span class="input-group-select">
												<select class="form-control" id="searchGb">
													<option value="loginId">로그인아이디</option>
													<option value="owner">대표자명</option>
													<option value="chaOffNo">사업자번호</option>
													<option value="chrName">담당자명</option>
													<option value="chrTelNo">담당자번호</option>
												</select>
											</span>
                                            <input type="text" class="form-control" id="searchValue" name="searchValue" maxlength="50">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-inline">
                                                <input type="radio" id="reqstAll" name="statusCheck" value="all" checked>
                                                <label for="reqstAll"> 전체 </label>
                                            </div>
                                            <div class="radio radio-inline">
                                                <input type="radio" id="reqst1" name="statusCheck" value="W">
                                                <label for="reqst1"> 승인대기 </label>
                                            </div>
                                            <div class="radio radio-inline">
                                                <input type="radio" id="reqst2" name="statusCheck" value="Y">
                                                <label for="reqst2"> 승인완료 </label>
                                            </div>
                                            <div class="radio radio-inline">
                                                <input type="radio" id="reqst3" name="statusCheck" value="N">
                                                <label for="reqst3"> 거절 </label>
                                            </div>
                                            <div class="radio radio-inline">
                                                <input type="radio" id="reqst4" name="statusCheck" value="D">
                                                <label for="reqst4"> 해지 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <hr>

                            <div class="text-center">
                                <button class="btn btn-primary" type="button" onclick="fnSearch();">조회</button>
                            </div>
                        </form>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <div class="animated fadeInRight" id="focus">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox">
                    <div class="ibox-title">
                        <div class="col-lg-6">
                            <span class="m-r-sm">대기 건수 : <strong class="text-danger" id="waitCount">0</strong> 건</span>
                            <span> | </span>
                            <span class="m-l-sm">전체 건수 : <strong class="text-success" id="totalCount">0</strong> 건</span>
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="searchOrderBy" id="searchOrderBy" onchange="fnSearch();">
                                <option value="atRegDt">신청일순</option>
                                <option value="chaCd">기관코드순</option>
                            </select>
                            <select class="form-control" name="pageScale" id="pageScale" onchange="fnSearch();">
                                <option value="10">10개씩 조회</option>
                                <option value="20">20개씩 조회</option>
                                <option value="50">50개씩 조회</option>
                                <option value="100">100개씩 조회</option>
                                <option value="200">200개씩 조회</option>
                            </select>
                            <button class="btn btn-md btn-primary" onclick="fn_fileSave()">파일저장</button>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center">
                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>신청일자</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>발송기관명</th>
                                        <th>업종</th>
                                        <th>로그인아이디</th>
                                        <th>대표자명</th>
                                        <th>사업자번호</th>
                                        <th>담당자명</th>
                                        <th>담당자전화번호</th>
                                        <th>상태</th>
                                        <th>승인(거절)일</th>
                                        <th>해지일</th>
                                    </tr>
                                </thead>

                                <tbody id="resultBody">
                                    <tr>
                                        <td colspan="13" style="text-align: center;">
                                            [조회된 내역이 없습니다.]
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <jsp:include page="/WEB-INF/views/include/sysPaging.jsp" flush="false"/>
            </div>
        </div>
    </div>
</div>

<!-- 발송기관명 수정 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/atchaname-update-popup.jsp" flush="false"/>

<!-- FooTer -->
<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        $('.input-daterange').datepicker({
            keyboardNavigation: false,
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
            forceParse: false,
            autoclose: true
        });

        $('.input-group.date').datepicker({
            todayBtn: "linked",
            keyboardNavigation: false,
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
            forceParse: false,
            calendarWeeks: true,
            autoclose: true
        });
        setMonthTerm(1);
        fnSearch();
    });
</script>

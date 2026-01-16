<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<script>
    var oneDepth = "adm-nav-9";
    var twoDepth = "adm-sub-25";
</script>


<script type="text/javascript">
    // 조회
    var cuPage = 1;

    function fn_search(page) {
        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }
        var startDt = $('#startday').val().replace(/\./gi, "");
        var endDt = $('#endday').val().replace(/\./gi, "");

//        if (startDt.length < 8 || endDt.length < 8) {
//            swal({
//                type: 'info',
//                text: '등록기간를 확인해 주세요.',
//                confirmButtonColor: '#3085d6',
//                confirmButtonText: '확인'
//            }).then(function () {
//                if (startDt.length < 8 || !startDt) {
//                    $('#startday').focus();
//                } else if (endDt.length < 8 || !endDt) {
//                    $('#endday').focus();
//                }
//            });
//            return;
//        }

        var stList = [];
        var cmsList = [];
        var check = $("input[name='cmsAppGubn']:checked");
        check.map(function (i) {
            cmsList.push($(this).val());
        });
        var checkbox = $("input[name='cmsReqSt']:checked");
        checkbox.map(function (i) {
            if ($(this).val() != '' && $(this).val() != null) {
                stList.push($(this).val());
            }
        });

        var url = "/sys/auto/autoTranSel";
        var param = {
            startDt: startDt,
            endDt: endDt,
            chaCd: $('#chacd').val(),
            chaName: $('#chaname').val(),
            searchOption: $('#searchOpt').val(),
            keyword: $('#selKeyword').val(),
            stList: stList,
            cmsList: cmsList,
            orderBy: $("#orderBySel option:selected").val(),
            curPage: cuPage,
            pageScale: $("#pageScale option:selected").val()
        };
        $.ajax({
            type: "post",
            url: url,
            data: param,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                fn_grid(data, 'resultBody');
                sysajaxPaging(data, 'PageArea');
            }
        });
    }

    function fn_grid(data, obj) {
        var str = '';
        $('#count').text(data.count);

        if (data.count <= 0) {
            str += '<tr><td colspan="10" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(data.list, function (k, v) {
                str += '<tr>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + v.regDt + '</td>';
                if (v.cmsAppGubn == 'CM01') {
                    str += '<td>신규</td>';
                } else if (v.cmsAppGubn == 'CM02') {
                    str += '<td>변경</td>';
                } else if (v.cmsAppGubn == 'CM03') {
                    str += '<td>해지</td>';
                } else {
                    str += '<td></td>';
                }
                str += '<td>' + v.chaCd + '</td>';
                str += '<td>' + v.chaName + '</td>';
                str += '<td>' + nullValueChange(v.chrName) + '</td>';
                str += '<td>' + nullValueChange(v.feeAccNo) + '</td>';
                // cms 신청상태
                if (v.cmsReqSt == 'CST02') {
                    str += '<td>신청중</td>';
                } else if (v.cmsReqSt == 'CST03') {
                    str += '<td class="text-success">신청완료</td>';
                } else if (v.cmsReqSt == 'CST04') {
                    str += '<td>신청실패</td>';
                } else {
                    str += '<td class="text-danger">미신청</td>';
                }
                str += '<td>' + nullValueChange(v.cmsReqDt) + '</td>';
                str += '<td>';
                if (v.chkCms == 'Y') {
                    str += '<button type="button" class="btn btn-xs btn-info" onclick=fn_fileView("' + v.chaCd + '")>보기</button>';
                } else {
                    str += '';
                }
                if (v.cmsReqSt == 'CST03') { // 승인완료인 경우만..
                    str += '<button type="button" class="btn btn-xs btn-danger" onclick="fn_fileChange(\'' + v.chaCd + '\', \'' + v.chaName + '\');">변경</button>';
                    str += '</td><td>';
                    str += '<button type="button" class="btn btn-xs btn-success" onclick=fn_fileClose("' + v.chaCd + '")>해지</button>';
                } else {
                    str += '<td>';
                    str += '</td><td>';
                    str += '';
                }
                str += '</td>';
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    function list(page, val) {
        if (val == '55') {
            fn_ListCollector(page);
        } else {
            fn_search(page);
        }
    }

    // 기간별 조회 - 기간선택
    function fn_selMonth(val) {

        $('#startday').val(getPrevDate($('#endday').val(), val));

        $("button[name=btnMonth]").removeClass('active');
        $('#pMonth' + val).addClass('active');
    }

    // 기관검색
    function fn_orgSearch() {

        $("#popChacd").val($('#chacd').val());
        $("#popChaname").val($('#chaname').val());
        $("#lookup-collecter-popup").modal({
            backdrop: 'static',
            keyboard: false
        });
        fn_ListCollector();
    }

    // 파일저장
    function fn_fileSave() {
        if ($('#count').text() == 0) {
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
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                $('#startDt').val($('#startday').val().replace(/\./gi, ""));
                $('#endDt').val($('#endday').val().replace(/\./gi, ""));
                $('#chaCd').val($('#chacd').val());
                $('#chaName').val($('#chaname').val());
                $('#searchOption').val($('#searchOpt').val());
                $('#keyword').val($('#selKeyword').val());
                var cmsList = [];
                var check = $("input[name='cmsAppGubn']:checked");
                check.map(function (i) {
                    cmsList.push($(this).val());
                });
                $('#cmsList').val(cmsList);
                var stList = [];
                var checkbox = $("input[name='cmsReqSt']:checked");
                checkbox.map(function (i) {
                    stList.push($(this).val());
                });
                $('#stList').val(stList);
                $('#orderBy').val($('#orderBySel').val());

                // 다운로드
                document.fForm.action = "/sys/auto/autoTranDownload";
                document.fForm.submit();
            }
        });
    }

    // 해지
    function fn_fileClose(idx) {
        swal({
            type: 'question',
            html: "해지 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                var url = "/sys/auto/autoTranCancel";
                var param = {
                    chaCd: idx
                };
                $.ajax({
                    type: "post",
                    url: url,
                    data: param,
                    contentType: "application/json; charset=UTF-8",
                    data: JSON.stringify(param),
                    success: function (data) {
                        swal({
                            type: 'success',
                            text: '해지되었습니다.',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        }).then(function (result) {
                            fn_search();
                        });
                    }
                });
            }
        });
    }
</script>

<form id="fForm" name="fForm" method="post">
    <input type="hidden" id="startDt" name="startDt">
    <input type="hidden" id="endDt" name="endDt">
    <input type="hidden" id="chaCd" name="chaCd">
    <input type="hidden" id="chaName" name="chaName">
    <input type="hidden" id="searchOption" name="searchOption">
    <input type="hidden" id="keyword" name="keyword">
    <input type="hidden" id="cmsList" name="cmsList">
    <input type="hidden" id="stList" name="stList">
    <input type="hidden" id="orderBy" name="orderBy">
</form>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>자동이체신청관리</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>설정관리</a>
            </li>
            <li class="active">
                <strong>자동이체신청관리</strong>
            </li>
        </ol>
        <p class="page-description">제출된 자동이체 신청서를 관리하는 화면입니다.</p>
    </div>
    <div class="col-lg-2">

    </div>
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
                                <div class="col-md-6">
                                    <label class="form-label block">등록기간</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control" id="startday" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="endday" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline active" name="btnSetMonth"  id="btnSetMonth0"  onclick="setMonthTerm(0);">전체</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"        name="btnSetMonth"  id="btnSetMonth1"  onclick="setMonthTerm(1);">1개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"        name="btnSetMonth"  id="btnSetMonth6"  onclick="setMonthTerm(6);">6개월</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6"></div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chacd" id="chacd" maxlength="50">
                                            <span class="input-group-btn">
                                                <button class="btn btn-primary btn-lookup-collecter no-margins" type="button" onclick="fn_orgSearch();">기관검색</button>
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">신청구분</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="cmsAppGubnAll" value="" name="cmsAppGubn">
                                                <label for="cmsAppGubnAll"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CM01" value="CM01" name="cmsAppGubn" onclick="fn_reqChecked();">
                                                <label for="CM01"> 신규 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CM02" value="CM02" name="cmsAppGubn" onclick="fn_reqChecked();">
                                                <label for="CM02"> 변경 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CM03" value="CM03" name="cmsAppGubn" onclick="fn_reqChecked();">
                                                <label for="CM03"> 해지 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">검색구분</label>
                                    <div class="input-group col-md-12">
                                         <span class="input-group-select">
                                            <select class="form-control" id="searchOpt">
                                                <option value="All">전체</option>
                                                <option value="feeAccNo">출금계좌</option>
                                                <option value="chrName">담당자명</option>
                                            </select>
                                        </span>
                                        <input type="text" class="form-control" id="selKeyword" maxlength="100">
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">CMS 신청상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="cmsReqStAll" value="" name="cmsReqSt">
                                                <label for="cmsReqStAll"> 전체</label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CST01" value="CST01" name="cmsReqSt" onclick="fn_stChecked();">
                                                <label for="CST01"> 미신청</label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CST02" value="CST02" name="cmsReqSt" onclick="fn_stChecked();">
                                                <label for="CST02"> 신청중</label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CST03" value="CST03" name="cmsReqSt" onclick="fn_stChecked();">
                                                <label for="CST03"> 신청완료</label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CST04" value="CST04" name="cmsReqSt" onclick="fn_stChecked();">
                                                <label for="CST04"> 신청실패</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <hr>

                            <div class="text-center">
                                <button class="btn btn-primary" type="button" onclick="fn_search();">조회</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="animated fadeInRight">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox">
                    <div class="ibox-title">
                        <div class="col-lg-6">
                            전체 기관 : <strong class="text-success" id="count"></strong> 건
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" id="orderBySel" onchange="fn_search();">
                                <option value="regDt">등록일순</option>
                                <option value="chaCd">기관코드순</option>
                                <option value="makeDt">신청완료일순</option>
                            </select>
                            <select class="form-control" id="pageScale" onchange="fn_search();">
                                <option value="10">10개씩 조회</option>
                                <option value="20">20개씩 조회</option>
                                <option value="50">50개씩 조회</option>
                                <option value="100">100개씩 조회</option>
                                <option value="200">200개씩 조회</option>
                            </select>
                            <button class="btn btn-md btn-primary" onclick="fn_fileSave();">파일저장</button>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center"><!-- 2018.05.11 클래스 수정 -->
                                <colgroup>
                                    <col width="50">
                                    <col width="150">
                                    <col width="120">
                                    <col width="120">
                                    <col width="250">
                                    <col width="160">
                                    <col width="170">
                                    <col width="130">
                                    <col width="130">
                                    <col width="200">
                                    <col width="130">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>등록일자</th>
                                        <th>신청구분</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>담당자명</th>
                                        <th>수수료 출금계좌</th>
                                        <th>CMS신청상태</th>
                                        <th>승인완료일자</th>
                                        <th>제출서류</th>
                                        <th>해지신청</th>
                                    </tr>
                                </thead>

                                <tbody id="resultBody"></tbody>
                            </table>
                        </div>

                        <jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>

<!-- 출금동의서 보기 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/autoTranView.jsp" flush="false"/>

<!-- 출금동의서 변경 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/withdrawal-agreement-modify.jsp" flush="false"/>

<script>
    $(document).ready(function () {
        $("#CM01").prop("checked", true);	// 신청구분
        $("#CST01").prop("checked", true);  // CMS 신청 상태

        setMonthTerm(1);

        fn_search(1);

        $('.input-daterange').datepicker({
            format: "yyyy.mm.dd",
            maxDate: "+0d",
            keyboardNavigation: false,
            forceParse: false,
            autoclose: true
        });

        $('.input-group.date').datepicker({
            format: "yyyy.mm.dd",
            maxDate: "+0d",
            todayBtn: "linked",
            keyboardNavigation: false,
            forceParse: false,
            calendarWeeks: true,
            autoclose: true
        });

        $("#cmsAppGubnAll").click(function () { //만약 전체 선택 체크박스가 체크된상태일경우
            if ($("#cmsAppGubnAll").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
                $("input[name=cmsAppGubn]").prop("checked", true); // 전체선택 체크박스가 해제된 경우
            } else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
                $("input[name=cmsAppGubn]").prop("checked", false);
            }
        });

        $("#cmsReqStAll").click(function () { //만약 전체 선택 체크박스가 체크된상태일경우
            if ($("#cmsReqStAll").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
                $("input[name=cmsReqSt]").prop("checked", true); // 전체선택 체크박스가 해제된 경우
            } else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
                $("input[name=cmsReqSt]").prop("checked", false);
            }
        });
    });

    function fn_reqChecked() {
        if (!$('#CM01').is(':checked') || !$('#CM02').is(':checked') || !$('#CM03').is(':checked')) {
            $('#cmsAppGubnAll').prop('checked', false);
        } else {
            $('#cmsAppGubnAll').prop('checked', true);
        }
    }

    function fn_stChecked() {
        if (!$('#CST01').is(':checked') || !$('#CST02').is(':checked') || !$('#CST03').is(':checked') || !$('#CST04').is(':checked')) {
            $('#cmsReqStAll').prop('checked', false);
        } else {
            $('#cmsReqStAll').prop('checked', true);
        }
    }
</script>

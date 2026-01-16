<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>가상계좌 정산</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>정산관리</a>
            </li>
            <li class="active">
                <strong>가상계좌 정산</strong>
            </li>
        </ol>
    </div>
</div>
<input type="hidden" id="curPage" name="curPage"/>

<form id="fileForm" name="fileForm" method="post">
    <input type="hidden" name="searchStartday" id="searchStartday">
    <input type="hidden" name="searchEndday" id="searchEndday">
    <input type="hidden" name="fchacd" id="fchacd">
    <input type="hidden" name="fchaname" id="fchaname"/>
    <input type="hidden" name="stList" id="stList"/>
    <input type="hidden" name="searchOrderBy" id="searchOrderBy"/>
    <input type="hidden" name="settleseq" id="settleseq"/>
</form>

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
                                    <label class="form-label block">입금일자</label>
                                    <div id="datepicker" class="input-daterange input-group float-left" >
                                        <input id="startday" name="startday" type="text" class="input-sm form-control" readonly="readonly"/>
                                        <span class="input-group-addon">to </span>
                                        <input id="endday" name="endday" type="text" class="input-sm form-control" readonly="readonly"/>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label block">처리상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" name="result" id="resultAll" value="">
                                                <label for="resultAll"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" name="result" id="resultW" value="W" onclick="fn_reqChecked();">
                                                <label for="resultW"> 대기 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" name="result" id="resultS" value="S" onclick="fn_reqChecked();">
                                                <label for="resultS"> 성공 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" name="result" id="resultF" value="F" onclick="fn_reqChecked();">
                                                <label for="resultF"> 실패 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chacd" id="chacd" maxlength="100">
                                            <span class="input-group-btn">
                                                <button class="btn btn-primary btn-lookup-collecter no-margins" type="button">기관검색</button>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label block">기관명</label>
                                    <input type="text" class="form-control" name="chaname" id="chaname" maxlength="100" >
                                </div>
                            </div>
                            <hr>
                            <div class="text-center">
                                <button class="btn btn-primary" type="button" onclick="fnSearch('1');">조회</button>
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
                            <span>조회결과 : <strong class="text-success"><span id="totalCount">0</span></strong> 건</span>
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="vSearchOrderBy" id="vSearchOrderBy" onchange="pageChange();">
                                <option value="rcpdate">입금일자</option>
                                <option value="chaname">기관명순</option>
                                <option value="settlestatus">처리상태순</option>
                            </select>
                            <select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
                                <option value="10">10개씩 조회</option>
                                <option value="20">20개씩 조회</option>
                                <option value="50">50개씩 조회</option>
                                <option value="100">100개씩 조회</option>
                                <option value="200">200개씩 조회</option>
                            </select>
                            <button class="btn btn-md btn-primary" type="button" onclick="fn_fileSave();">파일저장</button>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center">
                                <colgroup>
                                    <col width="50">
                                    <col width="180">
                                    <col width="180">
                                    <col width="180">
                                    <col width="180">
                                    <col width="390">
                                    <col width="250">
                                    <col width="180">
                                    <col width="150">
                                    <col width="180">
                                    <col width="200">
                                </colgroup>

                                <thead>
                                <tr>
                                    <th>NO</th>
                                    <th>입금일자</th>
                                    <th>은행코드</th>
                                    <th>기관코드</th>
                                    <th>기관명</th>
                                    <th>입금계좌번호</th>
                                    <th>정산금액(원)</th>
                                    <th>처리상태</th>
                                    <th>처리일시</th>
                                    <th>결과</th>
                                    <th>이력</th>
                                </tr>
                                </thead>

                                <tbody id="resultBody">
                                <tr>
                                    <td colspan=10 style="text-align: center;">[조회된 내역이 없습니다.]</td>
                                </tr>
                                </tbody>
                            </table>

                            <div class="col-lg-12 text-center">
                                <div class="btn-group" id="PageArea"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 어드민용 스피너 추가 -->
<div class="spinner-area" style="display:none;">
    <div class="sk-spinner sk-spinner-cube-grid">
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
    </div>
    <div class="modal-backdrop fade in"></div>
</div>
<!-- 어드민용 스피너 추가 -->

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>

<!-- 실패 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/vano-result-fail.jsp" flush="false"/>

<!-- 이력보기 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/vano-result-log.jsp" flush="false"/>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<script type="text/javascript">
    var oneDepth = "adm-nav-5";
    var twoDepth = "adm-sub-35";

    $(document).ready(function () {
        setMonthTerm(1);

        //입금일자 기능
        $('.input-daterange').datepicker({
            format: "yyyy.mm.dd",
            maxDate: "+0d",
            keyboardNavigation: false,
            forceParse: false,
            autoclose: true
        });

        //기관검색을 눌렀을 경우
        $(".btn-lookup-collecter").click(function () {
            $("#lookup-collecter-popup").modal({
                backdrop: 'static',
                keyboard: false
            });
            $("#popChacd").val($("#chacd").val());
            $("#popChaname").val($("#chaname").val());
            fn_ListCollector();
        });

        //-------------------------처리상태 디폴트 세팅-------------------------------
        $("#resultAll").click(function () { //만약 전체 선택 체크박스가 체크된상태일경우
            if ($("#resultAll").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
                $("input[name=result]").prop("checked", true); // 전체선택 체크박스가 해제된 경우
            } else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
                $("input[name=result]").prop("checked", false);
            }
        });

        $("input[name=result]").prop("checked", true);
        //-------------------------가상계좌 상태 디폴트 세팅-------------------------------

        fnSearch();
    });

    //페이징 버튼
    function list(page, val) {
        if (val == '55') {
            fn_ListCollector(page); // 기관검색 - modal
        } else {
            fnSearch(page);
        }
    }

    //검색
    function fnSearch(page) {
        var startday = $('#startday').val().replace(/\./gi, "");
        var endday   = $("#endday").val().replace(/\./gi, "");

        if((startday > endday)) {
            swal({
                type: 'info',
                text: '입금일자를 확인해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }

        var url = "/sys/rcpMgmt/getVirtualAccountCalculate";
        var stList = [];
        var checkbox = $("input[name=result]:checked");
        checkbox.map(function (i) {
            if ($(this).val() != '' && $(this).val() != null) {
                stList.push($(this).val());
            }
        });

        var param = {
            startDt: startday,
            endDt: endday,
            chaCd: $('#chacd').val(),
            chaName: $('#chaname').val(),
            stList: stList,
            searchOrderBy: $('#vSearchOrderBy option:selected').val(),
            curPage: cuPage,
            pageScale: $('#pageScale option:selected').val()
        };

        $('.spinner-area').css('display', 'block');

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                $('.spinner-area').css('display', 'none');
                if (result.retCode == "0000") {
                    fnGrid(result, 'resultBody'); // 현재 데이터로 셋팅
                    sysajaxPaging(result, 'PageArea');
                } else {
                    swal({
                        type: 'error',
                        text: result.retMsg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function (result) {
                        fnSearch();
                    });
                }
            },
            error: function (result) {
                swal({
                    type: 'error',
                    text: JSON.stringify(result),
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function (result) {
                    fnSearch();
                });
            }

        });
    }

    function fnGrid(result, obj) {
        var str = '';
        $('#totalCount').text(result.totalItemCount);
        if (result == null || result.totalItemCount <= 0) {
            str += '<tr><td colspan="14" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.itemList, function (i, v) {
                str += '<tr>';
                str += '<td>' + NVL(v.rn) + '</td>';
                str += '<td>' + moment(v.rcpdate, 'YYYYMMDD').format('YYYY.MM.DD') + '</td>';
                str += '<td>' + NVL(v.fgcd) + '</td>';
                str += '<td>' + NVL(v.chaCd) + '</td>';
                str += '<td >' + NVL(v.chaName) + '</td>';
                str += '<td>' + NVL(v.settleaccno) + '</td>';

                if(v.settleamount >= 0){
                    str += '<td>' + numberToCommas(nullValueChange(v.settleamount)) + '</td>';
                }else{
                    str += '<td>' + -numberToCommas(nullValueChange(-v.settleamount)) + '</td>';
                }

                if (NVL(v.settlestatus) == 'W') {
                    str += '<td class="text-success">대기</td>';
                } else if (NVL(v.settlestatus) == 'S') {
                    str += '<td class="text-success">성공</td>';
                } else if (NVL(v.settlestatus) == 'F') {
                    str += '<td class="text-danger">실패</td>';
                } else {
                    str += '<td class="text-danger">' + v.settlestatus + '</td>';
                }

                if(v.settledate == null || v.settledate == ''){
                    str += '<td></td>';
                }else{
                    str += '<td>' + moment(v.settledate, 'YYYY-MM-DD HH:mm:ss').format('YYYY.MM.DD HH:mm:ss') + '</td>';
                }

                if(NVL(v.settlestatus) == 'W' || NVL(v.settlestatus) == 'F'){
                    str += '<td>';
                    str += '<button type="button" class="btn btn-xs btn-primary" onclick="fnSuccess(\'' + v.settleseq + '\')">성공</button>';

                    if(NVL(v.settlestatus) != 'F'){
                        str += '<button type="button" class="btn btn-xs btn-danger" onclick="fnFail(\'' + v.settleseq + '\',\'' + v.chaCd + '\', \'' + v.chaName + '\', \'' + v.chast + '\')">실패</button>';
                    }
                    str += '</td>';
                }else {
                    str += '<td></td>';
                }

                str += '<td>';
                str += '<button type="button" class="btn btn-xs btn-primary" onclick="fnResultLog(\'' + v.settleseq + '\',\'' + v.chaCd + '\', \'' + v.chaName + '\', \'' + v.chast + '\')">보기</button>';
                str += '</td>';

                str += '</tr>';
            });
        }

        $('#' + obj).html(str);
    }

    function fnSuccess(settleseq){
        var param = {
            settleseq : settleseq,
            settlestatus : 'S'
        };

        swal({
            type: 'question',
            html: "가상계좌 정산 결과를 성공으로 바꾸시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                $('.spinner-area').css('display', 'block');

                var url = "/sys/rcpMgmt/updateVirtualAccountResult";
                $.ajax({
                    type: "post",
                    url: url,
                    async: true,
                    data: JSON.stringify(param),
                    contentType: "application/json; charset=utf-8",
                    success: function (result) {
                        if (result.retCode == "0000") {
                            $('.spinner-area').css('display', 'none');

                            swal({
                                type: 'success',
                                text: '가상계좌 정산 결과가 성공으로 처리 완료되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    fnSearch();
                                }
                            });
                        } else {
                            $('.spinner-area').css('display', 'none');
                            swal({
                                type: 'warning',
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

    function fnFail(settleseq, chacd, chaname, chast){
        $('#popup-vano-result-fail').modal({
            backdrop: 'static',
            keyboard: false
        });

        $('#failsettleseq').val(settleseq);
        $('#failchacd').text(chacd);
        $('#failchaname').text(chaname);
        $('#failchast').text(getChastName(chast));
    }

    function fnRegistFail(){
        var param = {
            settleseq : $('#failsettleseq').val(),
            resultmsg : $('#failmemo').val(),
            settlestatus : 'F'
        };

        swal({
            type: 'question',
            html: "가상계좌 정산 결과를 실패로 바꾸시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                $('.spinner-area').css('display', 'block');

                var url = "/sys/rcpMgmt/updateVirtualAccountResult";
                $.ajax({
                    type: "post",
                    url: url,
                    async: true,
                    data: JSON.stringify(param),
                    contentType: "application/json; charset=utf-8",
                    success: function (result) {
                        if (result.retCode == "0000") {

                            $('.spinner-area').css('display', 'none');
                            $("#popup-vano-result-fail").modal("hide");

                            swal({
                                type: 'success',
                                text: '가상계좌 정산 결과가 실패로 처리 완료되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    $('#failmemo').val("");
                                    fnSearch();
                                }
                            });
                        } else {
                            $('.spinner-area').css('display', 'none');
                            swal({
                                type: 'warning',
                                text: result.retMsg,
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    $('#failmemo').val("");
                                    fnSearch();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    function fnResultLog(settleseq, chacd, chaname, chast){
        var param = {
            settleseq : settleseq,
        };

        /*if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }*/

        var url = "/sys/rcpMgmt/getVirtualAccountSettleDet";

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                $('.spinner-area').css('display', 'none');
                if (result.retCode == "0000") {
                    fnGrid2(result, 'resultLog'); // 현재 데이터로 셋팅
                    //sysajaxPaging(result, 'PageArea');
                } else {
                    swal({
                        type: 'error',
                        text: result.retMsg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function (result) {
                        $("#popup-vano-result-log").modal("hide");
                        fnSearch();
                    });
                }
            },
            error: function (result) {
                swal({
                    type: 'error',
                    text: JSON.stringify(result),
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                }).then(function (result) {
                    $("#popup-vano-result-log").modal("hide");
                    fnSearch();
                });
            }

        });

        $('#popup-vano-result-log').modal({
            backdrop: 'static',
            keyboard: false
        });

        $('#logsettleseq').val(settleseq);
        $('#logchacd').text(chacd);
        $('#logchaname').text(chaname);
        $('#logchast').text(getChastName(chast));
    }

    function fnGrid2(result, obj) {
        var str = '';
        if (result == null || result.itemCount <= 0) {
            str += '<tr><td colspan="14" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.itemList, function (i, v) {
                str += '<tr>';
                str += '<td>' + moment(v.rcpdate, 'YYYYMMDD').format('YYYY.MM.DD') + '</td>';
                str += '<td>' + NVL(v.fgcd) + '</td>';
                str += '<td>' + NVL(v.chaCd) + '</td>';
                str += '<td >' + NVL(v.chaName) + '</td>';
                str += '<td>' + NVL(v.settleaccno) + '</td>';
                str += '<td>' + numberToCommas(nullValueChange(v.settleamount)) + '</td>';

                if (NVL(v.settlestatus) == 'W') {
                    str += '<td class="text-success">대기</td>';
                } else if (NVL(v.settlestatus) == 'S') {
                    str += '<td class="text-success">성공</td>';
                } else if (NVL(v.settlestatus) == 'F') {
                    str += '<td class="text-danger">실패</td>';
                } else {
                    str += '<td class="text-danger">' + v.settlestatus + '</td>';
                }

                if(v.settledate == null || v.settledate == ''){
                    str += '<td></td>';
                }else{
                    str += '<td>' + moment(v.settledate, 'YYYY-MM-DD HH:mm:ss').format('YYYY.MM.DD HH:mm:ss') + '</td>';
                }

                str += '<td>' + NVL(v.resultmsg) + '</td>';

                str += '</tr>';
            });
        }

        $('#' + obj).html(str);
    }

    //modal 페이징 버튼
    function modalList(num, val) {
        if (val == '55') {
            fn_ListCollector(num); // 기관검색
        } else if (val == '66') {
            fn_ListBranch(num); // 지점검색
        }
    }

    function pageChange() {
        cuPage = 1;
        fnSearch(cuPage);
    }

    function getChastName(chast) {
        var retVal = "";

        if (chast == 'ST06') {
            retVal = "정상";
        } else if (chast == 'ST08') {
            retVal = "정지";
        } else if (chast == 'ST02') {
            retVal = "해지";
        } else {
            retVal = chast;
        }

        return retVal;
    }

    function NVL(value) {
        if (value == ""
            || value == null
            || value == undefined
            || (value != null && typeof value == "object" && !Object
                .keys(value).length)) {
            return "";
        } else {
            return value;
        }
    }

    //파일저장
    function fn_fileSave() {
        var startday = $('#startday').val().replace(/\./gi, "");
        var endday   = $("#endday").val().replace(/\./gi, "");

        if((startday > endday)) {
            swal({
                type: 'info',
                text: '입금일자를 확인해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var rowCnt = $('#totalCount').text();
        if (rowCnt == 0) {
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
        }).then(function (result) {
            if (result.value) {
                // 다운로드
                $('#searchStartday').val(startday);
                $('#searchEndday').val(endday);
                $('#fchacd').val($('#chacd').val());
                $('#fchaname').val($('#chaname').val());
                var stList = [];
                var checkbox = $("input[name=result]:checked");
                checkbox.map(function (i) {
                    if ($(this).val() != '' && $(this).val() != null) {
                        stList.push($(this).val());
                    }
                });
                $('#stList').val(stList);
                $('#searchOrderBy').val($('#vSearchOrderBy option:selected').val());

                document.fileForm.action = "/sys/rcpMgmt/getVirtualAccountCalculateExcel";
                document.fileForm.submit();
            }
        });
    }

    function fn_reqChecked() {
        if (!$('#resultW').is(':checked') || !$('#resultS').is(':checked') || !$('#resultF').is(':checked')) {
            $('#resultAll').prop('checked', false);
        } else {
            $('#resultAll').prop('checked', true);
        }
    }
</script>

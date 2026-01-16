<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<script type="text/javascript">
    var oneDepth = "adm-nav-7";
    var twoDepth = "adm-sub-22";
    var cuPage = 1;
</script>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>가상계좌거래내역</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>설정관리</a>
            </li>
            <li class="active">
                <strong>가상계좌거래내역</strong>
            </li>
        </ol>
        <p class="page-description">이용기관별 가상계좌 거래내역을 관리하는 화면입니다.</p>
    </div>
    <div class="col-lg-2">

    </div>
</div>

<form id="fileForm" name="fileForm" method="post">
    <input type="hidden" name="chaCd" id="chaCd">
    <input type="hidden" name="chaName" id="chaName">
    <input type="hidden" name="searchGb" id="searchGb">
    <input type="hidden" name="searchValue" id="searchValue">
    <input type="hidden" name="paydayFrom" id="paydayFrom">
    <input type="hidden" name="paydayTo" id="paydayTo">
    <input type="hidden" name="paytimeFrom" id="paytimeFrom">
    <input type="hidden" name="paytimeTo" id="paytimeTo">
    <input type="hidden" name="searchOrderBy" id="searchOrderBy">
</form>

<input type="hidden" id="curPage" name="curPage"/>

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
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control" id="startday" name="startday" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="endday" name="endday" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline active" name="btnSetMonth"  id="btnSetMonth0"  onclick="setMonthTerm(0);">전체</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"        name="btnSetMonth"  id="btnSetMonth1"  onclick="setMonthTerm(1);">1개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"        name="btnSetMonth"  id="btnSetMonth6"  onclick="setMonthTerm(6);">6개월</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">입금시간</label>
                                    <div class="form-group form-group-sm form-inline">
                                        <select class="form-control" id="hourBoxFrom">
                                            <option value="" selected>선택</option>
                                            <option value="00">00</option>
                                        </select>
                                        <select class="form-control" id="minuteBoxFrom">
                                            <option value="">선택</option>
                                        </select>
                                        <span> ~ </span>
                                        <select class="form-control" id="hourBoxTo">
                                            <option value="">선택</option>
                                        </select>
                                        <select class="form-control" id="minuteBoxTo">
                                            <option value="">선택</option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chacd" id="chacd" maxlength="50">
                                            <span class="input-group-btn">
                                                <button class="btn btn-primary btn-lookup-collecter no-margins" type="button">기관검색</button>
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">검색구분</label>
                                    <div class="input-group col-md-12">
                                         <span class="input-group-select">
                                            <select class="form-control" name="SRCHsearchGb" id="SRCHsearchGb">
                                                <option value="vano">가상계좌번호</option>
                                                <option value="cusname">납부자명</option>
											</select>
                                        </span>
                                        <input type="text" class="form-control" name="SRCHsearchValue" id="SRCHsearchValue" maxlength="100">
                                    </div>
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
                            전체 건수 : <strong class="text-success" id="totCnt">0</strong> 건
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="vSearchOrderBy" id="vSearchOrderBy" onchange="pageChange();">
                                <option value="payday">입금일순 정렬</option>
                                <option value="vano">가상계좌번호순 정렬</option>
                            </select>
                            <select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
                                <option value="10">10개씩 조회</option>
                                <option value="20">20개씩 조회</option>
                                <option value="50">50개씩 조회</option>
                                <option value="100">100개씩 조회</option>
                                <option value="200">200개씩 조회</option>
                            </select>
                            <button class="btn btn-sm btn-primary" type="button" onclick="fn_fileSave();">파일저장</button>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center"><!-- 2018.05.11 클래스 수정 -->
                                <colgroup>
                                    <col width="50">
                                    <col width="180">
                                    <col width="120">
                                    <col width="150">
                                    <col width="170">
                                    <col width="200">
                                    <col width="140">
                                    <col width="250">
                                    <col width="200">
                                    <col width="150">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>입금일자</th>
                                        <th>입금시간</th>
                                        <th>입금은행</th>
                                        <th>입금인</th>
                                        <th>가상계좌 번호</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>납부자명</th>
                                        <th>금액(원)</th>
                                    </tr>
                                </thead>

                                <tbody id="resultBody">
                                    <tr>
                                        <td colspan=8 style="text-align: center;">[조회된 내역이 없습니다.]</td>
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

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>


<script type="text/javascript">
    $(document).ready(function () {
        setMonthTerm(1);

        $('.input-daterange').datepicker({
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
            keyboardNavigation: false,
            forceParse: false,
            autoclose: true
        });

        $('.input-group.date').datepicker({
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
            todayBtn: "linked",
            keyboardNavigation: false,
            forceParse: false,
            calendarWeeks: true,
            autoclose: true
        });

        $(".btn-lookup-collecter").click(function () {
            $("#lookup-collecter-popup").modal({
                backdrop: 'static',
                keyboard: false
            });
            $("#popChacd").val($('#chacd').val());
            $("#popChaname").val($('#chaname').val());
            fn_ListCollector();
        });

        for (var i = 1; i < 24; i++) {
            var str = "";
            if (i < 10) {
                str += "<option value='0" + i + "'>0" + i + "</option>";
            } else {
                str += "<option value='" + i + "'>" + i + "</option>";
            }
            $('#hourBoxFrom').append(str);
        }
        $("#hourBoxFrom").val('');
        getMinuteBox('minuteBoxFrom');
        $("#minuteBoxFrom").val('');

        for (var i = 1; i < 24; i++) {
            var str = "";
            if (i < 10) {
                str += "<option value='0" + i + "'>0" + i + "</option>";
            } else {
                str += "<option value='" + i + "'>" + i + "</option>";
            }
            $('#hourBoxTo').append(str);
        }
        $("#hourBoxTo").val('');
        getMinuteBox('minuteBoxTo');
        $("#minuteBoxTo").val('');

        fnSearch();
    });

//    function setMonthTerm(month) {
//
//        var calDateFrom = "";
//        var calDateTo = "";
//        var toDate = new Date();
//        var fromDate = new Date();
//
//
//        fromDate.setMonth(fromDate.getMonth() - month);
//
//        $('#calDateFrom').val(getDateFormatDot(fromDate));
//        $('#calDateTo').val(getDateFormatDot(toDate));
//
//        $("button[name=btnSetMonth]").removeClass('active');
//        $('#btnSetMonth' + month).addClass('active');
//    }

//    function setMonthTermSys(month) {
//        $('.btn-preset-month').removeClass('active');
//        var calDateFrom = "";
//        var calDateTo = "";
//        var toDate = new Date();
//        var fromDate = new Date();
//
//        fromDate.setMonth(fromDate.getMonth() - month);
//
//        $('#calDateFrom').val(getDateFormatSlash(fromDate));
//        $('#calDateTo').val(getDateFormatSlash(toDate));
//
//        $("button[name=btnSetMonth]").removeClass('active');
//        $('#btnSetMonth' + month).addClass('active');
//    }

    // 페이징 버튼
    function list(page, val) {
        if (val == '55') {
            fn_ListCollector(page); // 기관검색 - modal
        } else {
            fnSearch(page);
        }
    }

    //파일저장
    function fn_fileSave() {
        if ($('#totCnt').text() == "0") {
            swal({
                type: 'info',
                text: '다운로드할 데이터가 없습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        if (fnValidation()) {
            var startday = $("#startday").val().replace(/\./gi, "");
            var endday = $("#endday").val().replace(/\./gi, "");
            var startTime = $("#hourBoxFrom").val() + $("#minuteBoxFrom").val();
            var endTime = $("#hourBoxTo").val() + $("#minuteBoxTo").val();
            if (($("#hourBoxFrom").val() && !$("#minuteBoxFrom").val()) || (!$("#hourBoxFrom").val() && $("#minuteBoxFrom").val()) ||
                ($("#hourBoxTo").val() && !$("#minuteBoxTo").val()) || (!$("#hourBoxTo").val() && $("#minuteBoxTo").val())) {
                swal({
                    type: 'info',
                    text: '입금시간을 확인해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
            if (startTime) {
                startTime = startTime + "00";
            }
            if (endTime) {
                endTime = endTime + "00";
            }
            if ((startday + startTime) > (endday + endTime)) {
                swal({
                    type: 'info',
                    text: '입금시간을 확인해 주세요.',
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
                    $('#chaCd').val($('#chacd').val());
                    $('#chaName').val($('#chaname').val());
                    $('#searchGb').val($('#SRCHsearchGb option:selected').val());
                    $('#searchValue').val($('#SRCHsearchValue').val());
                    $('#paydayFrom').val(startday);
                    $('#paydayTo').val(endday);
                    $('#paytimeFrom').val(startTime);
                    $('#paytimeTo').val(endTime);
                    $('#searchOrderBy').val($('#vSearchOrderBy option:selected').val());

                    document.fileForm.action = "/sys/vanoMgmt/getVanoTranHisListExcel";
                    document.fileForm.submit();
                }
            });
        }
    }

    // 검색
    function fnSearch(page) {
        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }

        if (fnValidation()) {

            var startday = $("#startday").val().replace(/\./gi, "");
            var endday = $("#endday").val().replace(/\./gi, "");
            var startTime = $("#hourBoxFrom").val() + $("#minuteBoxFrom").val();
            var endTime = $("#hourBoxTo").val() + $("#minuteBoxTo").val();

            if (($("#hourBoxFrom").val() && !$("#minuteBoxFrom").val()) || (!$("#hourBoxFrom").val() && $("#minuteBoxFrom").val()) ||
                ($("#hourBoxTo").val() && !$("#minuteBoxTo").val()) || (!$("#hourBoxTo").val() && $("#minuteBoxTo").val())) {
                swal({
                    type: 'info',
                    text: '입금시간을 확인해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
            if (startTime) {
                startTime = startTime + "00";
            }
            if (endTime) {
                endTime = endTime + "00";
            }

            if ((startday + startTime) > (endday + endTime)) {
                swal({
                    type: 'info',
                    text: '입금시간을 확인해 주세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

            var url = "/sys/vanoMgmt/getVanoTranHisList";

            var param = {
                chacd: $('#chacd').val(),
                chaname: $('#chaname').val(),
                searchGb: $('#SRCHsearchGb option:selected').val(),
                searchValue: $('#SRCHsearchValue').val(), //검색구분 텍스트값
                paydayFrom: startday,
                paydayTo: endday,
                paytimeFrom: startTime,
                paytimeTo: endTime,
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
                        fnGrid(result, 'resultBody');		// 현재 데이터로 셋팅
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
    }

    // validation
    function fnValidation() {

        var fromDt = $("#startday").val().replace(/\./gi, "");
        var toDt = $("#endday").val().replace(/\./gi, "");

//        if ((!fromDt || !toDt) || (fromDt > toDt)) {
//            swal({
//                type: 'info',
//                text: '입금일자를 확인해 주세요.',
//                confirmButtonColor: '#3085d6',
//                confirmButtonText: '확인'
//            });
//            return;
//        }

        return true;
    }

    // 데이터 새로고침
    function fnGrid(result, obj) {
        var str = '';
        $('#totCnt').text();
        if (result == null || result.count <= 0) {
            str += '<tr><td colspan="14" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + NVL(v.rn) + '</td>';
                str += '<td>' + changeDateFormat(NVL(v.payday)) + '</td>';
                str += '<td>' + getTimeFmtBasic(NVL(v.paytime)) + '</td>';
                if (v.bnkcd != null && v.bnkcd != ' ') {
                    str += '<td>' + NVL(v.bnkcd) + '</td>';
                }else{
                	 str += '<td>' + NVL(v.ficd) + '</td>';	
                }
                str += '<td>' + NVL(v.rcpusrname) + '</td>';
                str += '<td>' + NVL(v.vano) + '</td>';
                str += '<td>' + NVL(v.chacd) + '</td>';
                str += '<td>' + NVL(v.chaName) + '</td>';
                str += '<td>' + NVL(v.cusname) + '</td>';
                str += '<td class="text-success" style="text-align:right;">' + comma(v.rcpamt) + '</td>';
                str += '</tr>';
            });
            $('#totCnt').text(comma(result.count));
        }
        $('#' + obj).html(str);
    }

    function getChastNameDamoa(chast) {
        var retVal = "";
        if (chast == 'ST01') {
            retVal = "승인대기";
        } else if (chast == 'ST06') {
            retVal = "승인";
        } else if (chast == 'ST07') {
            retVal = "이용거부";
        } else if (chast == 'ST08') {
            retVal = "정지";
        } else if (chast == 'ST02') {
            retVal = "해지";
        } else if (chast == 'ST03') {
            retVal = "승인거부";
        } else if (chast == 'ST04') {
            retVal = "정지";
        } else if (chast == 'ST05') {
            retVal = "승인대기";
        } else {
            retVal = chast;
        }

        return retVal;
    }

    //modal 페이징 버튼
    function modalList(num, val) {
        if (val == '55') {
            fn_ListCollector(num);	// 기관검색
        } else if (val == '66') {
            fn_ListBranch(num);     // 지점검색
        }
    }

    function pageChange() {
        cuPage = 1;
        fnSearch(cuPage);
    }

    //금액 콤마
    function comma(str) {
        str = String(str);
        return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
    }

    function NVL(value) {
        if (value == "" || value == null || value == undefined
            || ( value != null && typeof value == "object" && !Object.keys(value).length )) {
            return "";
        } else {
            return value;
        }
    }
</script>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<script>
    var oneDepth = "adm-nav-2";
    var twoDepth = "adm-sub-34";
</script>

</div>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>변경대기목록</h2>
        <ol class="breadcrumb">
            <li><a href="/sys/index">대시보드</a></li>
            <li><a>기관관리</a></li>
            <li class="active"><strong>변경대기목록</strong></li>
        </ol>
        <p class="page-description">변경대기목록 입니다.</p>
    </div>
</div>

<input type="hidden" id="curPage" name="curPage"/>

<form id="downForm" name="downForm" method="post">
    <input type="hidden" id="chaCd" name="chaCd">
    <input type="hidden" id="chaName" name="chaName">
    <input type="hidden" id="statusCd" name="statusCd">
    <input type="hidden" id="typeCd" name="typeCd">
    <input type="hidden" id="calDateFrom" name="calDateFrom">
    <input type="hidden" id="calDateTo" name="calDateTo">
    <input type="hidden" id="searchOrderBy" name="searchOrderBy">
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
                                    <label class="form-label block">수신일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control" id="startday" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="endday" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline active" name="btnSetMonth" id="btnSetMonth0" onclick="setMonthTerm(0);">전체</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth" id="btnSetMonth1" onclick="setMonthTerm(1);">1개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth" id="btnSetMonth6" onclick="setMonthTerm(6);">6개월</button>
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
                                        <div class="input-group col-md-12">
                                            <input type="text" class="form-control" name="clientId" id="clientId">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">기관명</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <input type="text" class="form-control" name="clientName" id="clientName">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">분류</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-group">
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="typeCdAll" name="typeCdList" value="" checked>
                                                    <label for="typeCdAll"> 전체 </label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="C20001" name="typeCdList" value="C20001">
                                                    <label for="C20001"> 신규 </label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="C20002" name="typeCdList" value="C20002">
                                                    <label for="C20002"> 변경 </label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="C20003" name="typeCdList" value="C20003">
                                                    <label for="C20003"> 해지 </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-group">
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="statusCdAll" name="statusCdList" value="" checked>
                                                    <label for="statusCdAll"> 전체 </label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="C10001" name="statusCdList" value="C10001">
                                                    <label for="C10001"> 대기 </label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="C10002" name="statusCdList" value="C10002">
                                                    <label for="C10002"> 준비 </label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="C10003" name="statusCdList" value="C10003">
                                                    <label for="C10003"> 실행중 </label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="C10004" name="statusCdList" value="C10004">
                                                    <label for="C10004"> 성공 </label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="C10005" name="statusCdList" value="C10005">
                                                    <label for="C10005"> 실패 </label>
                                                </div>
                                            </div>
                                        </div>
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
                            <span class="m-l-sm">조회결과 : <strong class="text-success" id="count"></strong> 건</span>
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="vSearchOrderBy" id="vSearchOrderBy" onchange="pageChange();">
                                <option value="pullDt">수신일자순 정렬</option>
                                <option value="clientName">기관명순 정렬</option>
                                <option value="modifyDt">처리일자순 정렬</option>
                            </select>

                            <select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
                                <option value="10">10개씩 조회</option>
                                <option value="20">20개씩 조회</option>
                                <option value="50">50개씩 조회</option>
                                <option value="100">100개씩 조회</option>
                                <option value="200">200개씩 조회</option>
                            </select>

                            <button type="button" class="btn btn-md btn-primary" onclick="fn_fileSave();">파일저장</button>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center">
                                <colgroup>
                                    <col width="50">
                                    <col width="220">
                                    <col width="200">
                                    <col width="403">
                                    <col width="140">
                                    <col width="140">
                                    <col width="200">
                                    <col width="220">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>수신일자</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>분류</th>
                                        <th>상태</th>
                                        <th>비고</th>
                                        <th>처리일자</th>
                                    </tr>
                                </thead>

                                <tbody id="resultBody">
                                    <tr>
                                        <td colspan="8" class="text-center">[조회된 내역이 없습니다.]</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                        <div class="row m-b-lg">
                            <div class="col-lg-12 text-center">
                                <div class="row">
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
    <div class="modal-backdrop-back-spinner"></div>
</div>
<!-- 어드민용 스피너 추가 -->


<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>

<!-- 기관정보 상세보기 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/collecter-info.jsp" flush="false"/>

<!-- 비교 상세보기 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/changeCha-info.jsp" flush="false"/>

<script>
    $(document).ready(function () {
        var cuPage = 1;
        var memoChacd = "";

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

        setMonthTerm(0);
        fnSearch();
    });


    /**
     * 조회
     */
    function fnSearch(page) {
        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }

        var url = "/sys/chaMgmt/getChangeChaList";
        
        var startday = $('#startday').val() == '' || $('#startday').val() == null ? '' : moment($('#startday').val(), 'YYYY.MM.DD').format('YYYYMMDD');
        var endday = $('#endday').val() == '' || $('#endday').val() == null ? '' : moment($('#endday').val(), 'YYYY.MM.DD').format('YYYYMMDD');

        if (startday != '' && endday != '') {
            if((startday > endday) || (startday.length < 6) || (endday.length < 6)){
                swal({
                    type : 'info',
                    text : '수신일자를 확인해 주세요.',
                    confirmButtonColor : '#3085d6',
                    confirmButtonText : '확인'
                });
                return;
            }
        }
        
        if(startday == '' && endday != ''){
            swal({
                type : 'info',
                text : '수신 시작일자를 확인해 주세요.',
                confirmButtonColor : '#3085d6',
                confirmButtonText : '확인'
            });
            return;
        }

        if(endday == '' && startday != ''){
            swal({
                type : 'info',
                text : '수신 마지막일자를 확인해 주세요.',
                confirmButtonColor : '#3085d6',
                confirmButtonText : '확인'
            });
            return;
        }

        var param = {
            calDateFrom: startday,
            calDateTo: endday,
            chaCd : $('#clientId').val(),
            chaName : $('#clientName').val(),
            statusCd: $("input[name=statusCdList]:checked").val(),
            typeCd: $("input[name=typeCdList]:checked").val(),
            searchOrderBy: $('#vSearchOrderBy option:selected').val(),
            curPage: cuPage,
            pageScale: $('#pageScale option:selected').val()
        };

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    fnGrid(result, 'resultBody');
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

    function fnGrid(result, obj) {
        var str = '';
        if (result.count == 0 || result == null) {
            str += '<tr><td colspan="8" class="text-center">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + NVL(v.rn) + '</td>';
                if (NVL(v.pullDt) != '') {
                    str += '<td id="pullDt">' + NVL(v.pullDt) + '</td>';
                } else {
                    str += '<td id="pullDt">' + NVL(v.pullDt) + '</td>';
                }
                str += '<td>' + NVL(v.chaCd) + '</td>';
                str += '<td >' + NVL(v.chaName);
                str += '<button type="button" class="btn btn-xs btn-primary pull-right" onclick="viewChaInfoPop(\'' + v.chaCd + '\')"><i class="fa fa-file-text-o"></i></button>';
                str += '</td>';
                str += '<td>' + v.typeName + '</td>';
                str += '<td>' + v.statusName + '</td>';
                str += '<td>';
                str += '<button type="button" class="btn btn-xs btn-primary" onclick="getChaInfo(\'' + v.pullDt + '\', \'' + v.clientId + '\')">상세보기</button>';
                if (v.statusCd == 'C10001' || v.statusCd == 'C10005') {
                    str += '<button type="button" class="btn btn-xs btn-danger" onclick="execute(\'' + v.pullDt + '\', \'' + v.clientId + '\')">실행</button>';
                }
                str += '</td>';
                if(v.statusCd == 'C10004' || v.statusCd == 'C10005') {
                    str += '<td>' + v.modifyDt + '</td>';
                } else {
                    str += '<td></td>';
                }
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
        $('#count').text(comma(result.count));
        sysajaxPaging(result, 'PageArea');
    }

    function pageChange() {
        cuPage = 1;
        fnSearch(cuPage);
    }

    function NVL(value) {
        if (value == "" || value == null || value == undefined
            || ( value != null && typeof value == "object" && !Object.keys(value).length )) {
            return "";
        } else {
            return value;
        }
    }

    /**
     * 기관 상세보기
     */
    function viewChaInfoPop(chacd) {
        $("#popup-collecter-info").modal({
            backdrop: 'static',
            keyboard: false
        });
        MODE = "N";
        collector_info_init(MODE, chacd);
    }

    /**
     * 엑셀 파일저장
     */
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
                $('#calDateFrom').val($('#startday').val().replace(/\./gi, ""));
                $('#calDateTo').val($('#endday').val().replace(/\./gi, ""));
                $('#chaCd').val($('#clientId').val());
                $('#chaName').val($('#chaName').val());
                $('#statusCd').val($("input[name=statusCdList]:checked").val());
                $('#typeCd').val($("input[name=typeCdList]:checked").val());
                $('#searchOrderBy').val($('#vSearchOrderBy option:selected').val());

                document.downForm.action = "/sys/chaMgmt/changeChaListExcel";
                document.downForm.submit();
            }
        });
    }

    /**
     * 페이징 리스트 불러오기
     */
    function list(page, val) {
        if (val == '55') {
            fn_ListCollector(page); // 기관검색 - modal
        } else {
            fnSearch(page);
        }
    }

    /**
     * 상세보기
     */
    function getChaInfo(pullDt, clientId) {
        // as-is 차리스트, to-be 현재 내역 테이블
        var pullDt = moment(pullDt, 'YYYY.MM.DD').format('YYYYMMDD');

        var url = "/sys/chaMgmt/changeChaListInfo";
        var param = {
            pullDt : pullDt,
            clientId : clientId
        };

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.retCode == "0000") {
                    var map = result.list;

                    // 기관 기본 정보
                    $("#changeCha-info #asisClientId").text(map.clientId);
                    $("#changeCha-info #asisClientIdNo").text(map.asisClientIdNo);
                    $("#changeCha-info #asisClientName").text(map.asisClientName);
                    $("#changeCha-info #asisStatusName").text(map.asisStatusName);
                    $("#changeCha-info #asisPaymentFeeAmt").text(map.asisPaymentFeeAmt);
                    $("#changeCha-info #asisFingerFeeRate").text(map.asisFingerFeeRate);
                    $("#changeCha-info #asisPaymentFinerFeeAmt").text(map.asisPaymentFinerFeeAmt);

                    $("#changeCha-info #clientId").text(map.clientId);
                    $("#changeCha-info #clientIdNo").text(map.clientIdNo);
                    $("#changeCha-info #clientName").text(map.clientName);
                    $("#changeCha-info #clientStatusName").text(map.clientStatusName);
                    $("#changeCha-info #paymentFeeAmt").text(map.paymentFeeAmt);
                    $("#changeCha-info #fingerFeeRate").text(map.fingerFeeRate)
                    $("#changeCha-info #paymentFinerFeeAmt").text(map.paymentFinerFeeAmt);

                    // 다계좌 정보
                    $("#changeCha-info #asisCode1").text(map.asisCode1);
                    $("#changeCha-info #asisCode2").text(map.asisCode2);
                    $("#changeCha-info #asisCode3").text(map.asisCode3);
                    $("#changeCha-info #asisCode4").text(map.asisCode4);
                    $("#changeCha-info #asisCode5").text(map.asisCode5);
                    $("#changeCha-info #asisCode6").text(map.asisCode6);
                    $("#changeCha-info #asisCode7").text(map.asisCode7);
                    $("#changeCha-info #asisCode8").text(map.asisCode8);
                    $("#changeCha-info #asisCode9").text(map.asisCode9);
                    $("#changeCha-info #asisCode10").text(map.asisCode10);

                    $("#changeCha-info #asisName1").text(map.asisName1);
                    $("#changeCha-info #asisName2").text(map.asisName2);
                    $("#changeCha-info #asisName3").text(map.asisName3);
                    $("#changeCha-info #asisName4").text(map.asisName4);
                    $("#changeCha-info #asisName5").text(map.asisName5);
                    $("#changeCha-info #asisName6").text(map.asisName6);
                    $("#changeCha-info #asisName7").text(map.asisName7);
                    $("#changeCha-info #asisName8").text(map.asisName8);
                    $("#changeCha-info #asisName9").text(map.asisName9);
                    $("#changeCha-info #asisName10").text(map.asisName10);

                    $("#changeCha-info #tobeCode1").text(map.tobeCode1);
                    $("#changeCha-info #tobeCode2").text(map.tobeCode2);
                    $("#changeCha-info #tobeCode3").text(map.tobeCode3);
                    $("#changeCha-info #tobeCode4").text(map.tobeCode4);
                    $("#changeCha-info #tobeCode5").text(map.tobeCode5);
                    $("#changeCha-info #tobeCode6").text(map.tobeCode6);
                    $("#changeCha-info #tobeCode7").text(map.tobeCode7);
                    $("#changeCha-info #tobeCode8").text(map.tobeCode8);
                    $("#changeCha-info #tobeCode9").text(map.tobeCode9);
                    $("#changeCha-info #tobeCode10").text(map.tobeCode10);

                    $("#changeCha-info #tobeName1").text(map.tobeName1);
                    $("#changeCha-info #tobeName2").text(map.tobeName2);
                    $("#changeCha-info #tobeName3").text(map.tobeName3);
                    $("#changeCha-info #tobeName4").text(map.tobeName4);
                    $("#changeCha-info #tobeName5").text(map.tobeName5);
                    $("#changeCha-info #tobeName6").text(map.tobeName6);
                    $("#changeCha-info #tobeName7").text(map.tobeName7);
                    $("#changeCha-info #tobeName8").text(map.tobeName8);
                    $("#changeCha-info #tobeName9").text(map.tobeName9);
                    $("#changeCha-info #tobeName10").text(map.tobeName10);

                    // 지점 정보
                    $("#changeCha-info #asisBrchCode").text(map.asisBrchCode);
                    $("#changeCha-info #brchCode").text(map.brchCode);
                    $("#changeCha-info #asisAgtName").text(map.asisAgtName);
                    $("#changeCha-info #agtName").text(map.agtName);

                    // 다계좌 사용 여부에 따른 테이블 출력
                    var agencyStatus = map.agencyStatus;
                    var adjAccYn = map.adjAccYn;

                    if (agencyStatus == 'N' && adjAccYn == 'N') {
                        $('#changeCha-info #accountUseYn').css('display', 'none');
                    } else {
                        $('#changeCha-info #accountUseYn').css('display', 'block');
                    }

                    $("#changeCha-info").modal({
                        backdrop: 'static',
                        keyboard: false
                    });
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

    /**
     * 실행
     */
    function execute(pullDt, clientId) {
        // PULL_HIST 테이블 STATUS_CD C10002로 변경 ( 작업 대기상태 )
        var pullDt = moment(pullDt, 'YYYY.MM.DD').format('YYYYMMDD');

        swal({
            type: 'question',
            html: "이용기관의 정보를 업데이트 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                var url = "/sys/chaMgmt/updateChangeChaInfo";
                var param = {
                    pullDt : pullDt,
                    clientId : clientId
                };

                $.ajax({
                    type: "post",
                    async: true,
                    url: url,
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (result) {
                        if (result.retCode == "0000") {
                            swal({
                                type: 'success',
                                text: '이용기관의 정보가 업데이트 되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });

                            list(1);
                        } else {
                            swal({
                                type: 'error',
                                text: '이용기관 정보 업데이트에 실패하였습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        }
                    }
                });
            }
        });
    }
</script>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<script>
    var oneDepth = "adm-nav-11";
    var twoDepth = "adm-sub-44";
</script>

</div>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>PG 신규기관목록</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>PG 기관관리</a>
            </li>
            <li class="active">
                <strong>신규기관목록</strong>
            </li>
        </ol>
        <p class="page-description">PG 가입 승인이 완료된 신규 이용기관을 서비스 등록하는 화면입니다.</p>
    </div>
</div>

<input type="hidden" id="curPage" name="curPage"/>

<form id="downForm" name="downForm" method="post">
    <input type="hidden" id="dchaCd" name="chaCd">
    <input type="hidden" id="dchaName" name="chaName">
    <input type="hidden" id="calDateFrom" name="calDateFrom">
    <input type="hidden" id="calDateTo" name="calDateTo">
    <input type="hidden" id="damtchkTy" name="amtchkTy">
    <input type="hidden" id="dchatrty" name="chatrty">
    <input type="hidden" id="dchaCloseChk" name="chaCloseChk">
    <input type="hidden" id="dsearchOrderBy" name="searchOrderBy">
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
                                    <label class="form-label block">등록일자</label>
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
                                        <div class="input-group col-md-12">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chacd" id="chacd" maxlength="50">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">기관명</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chacd" id="chaName" maxlength="50">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">이용방식</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-group">
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="amtchkTyAll" name="amtchkty" value="All" checked>
                                                    <label for="amtchkTyAll"> 전체 </label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="amtchkTyY" name="amtchkty" value="Y">
                                                    <label for="amtchkTyY"> 승인 </label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="amtchkTyL" nme="amtchkty" value="L">
                                                    <label for="amtchkTyL"> 한도 </label>
                                                </div>
                                                <div class="radio radio-primary radio-inline">
                                                    <input type="radio" id="amtchkTyN" name="amtchkty" value="N">
                                                    <label for="amtchkTyN"> 통지 </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">기관분류</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="chatrTyChkAll" name="chatrTySearch" value="All" checked>
                                                <label for="chatrTyChkAll"> 전체 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="chatrTyW" name="chatrTySearch" value="01">
                                                <label for="chatrTyW"> WEB </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="chatrTyA" name="chatrTySearch" value="03">
                                                <label for="chatrTyA"> API </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관검증</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="chaCloseChkAll" name="chaClose" value="All" checked>
                                                <label for="chaCloseChkAll"> 전체 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="chaCloseChkAllN" name="chaClose" value="N">
                                                <label for="chaCloseChkAllN"> 적합 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="chaCloseChkAllY" name="chaClose" value="Y">
                                                <label for="chaCloseChkAllY"> 부적합 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6"></div>
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
                            <span class="m-l-sm">전체 기관 : <strong class="text-success" id="count"></strong> 건</span>
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="vSearchOrderBy" id="vSearchOrderBy" onchange="pageChange();">
                                <option value="regDt">등록일순 정렬</option>
                                <option value="chaName">기관명순 정렬</option>
                                <option value="chaCloseChk">기관검증순 정렬</option>
                                <option value="chaTrTy">고객분류순 정렬</option>
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
                                <%--<colgroup>--%>
                                    <%--<col width="50">--%>
                                    <%--<col width="130">--%>
                                    <%--<col width="140">--%>
                                    <%--<col width="310">--%>
                                    <%--<col width="140">--%>
                                    <%--<col width="150">--%>
                                    <%--<col width="130">--%>
                                    <%--<col width="110">--%>
                                    <%--<col width="110">--%>
                                    <%--<col width="100">--%>
                                    <%--<col width="150">--%>
                                    <%--<col width="90">--%>
                                <%--</colgroup>--%>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>등록일자</th>
                                        <th>승인일자</th>
                                        <th>기관코드</th>
                                        <th>가맹점코드</th>
                                        <th>기관명</th>
                                        <th>담당자핸드폰번호</th>
                                        <th>발급요청 좌수</th>
                                        <th>이용방식</th>
                                        <th>기관검증</th>
                                        <th>고객분류</th>
                                        <th>이용승인</th>
                                    </tr>
                                </thead>

                                <tbody id="resultBody">
                                    <tr>
                                        <td colspan=13>[조회된 내역이 없습니다.]</td>
                                    </tr>
                                </tbody>
                            </table>

                            <input type="hidden" id="orgid"><input type="hidden" id="orgpw">
                        </div>

                        <div class="row m-b-lg">
                            <div class="col-lg-12 text-center">
                                <!-- paging -->
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

<!-- 메모 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/new-collecter-memo.jsp" flush="false"/>

<!-- 기관정보 상세보기 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/new-collecter-info.jsp" flush="false"/>

<!-- 이메일 보내기 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/send-email.jsp" flush="false"/>

<!-- 출금 동의서 업로드 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/withdrawal-agreement-upload.jsp" flush="false"/>

<!-- 출금동의서 보기 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/autoTranView.jsp" flush="false"/>

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

        $(".btn-lookup-collecter").click(function () {
            $("#chaGb").val("new");
            $("#lookup-collecter-popup").modal({
                backdrop: 'static',
                keyboard: false
            });
            $("#popChacd").val($('#chacd').val());
            $("#popChaname").val('');
            fn_ListCollector();
        });

        setMonthTerm(1);
        $('#chaCloseChkAll').prop('checked', true);
        $('#chatrTyChkAll').prop('checked', true);
        $('#amtchkTyAll').prop('checked', true);
        fnSearch();
    });

    function list(page, val) {
        if (val == '55') {
            fn_ListCollector(page); // 기관검색
        } else {
            fnSearch(page);
        }
    }

    //검색
    function fnSearch(page) {
        var checkArr = [];
        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }

        var url = "/sys/chaMgmt/getNewChaList";

        var startday = $('#startday').val().replace(/\./gi, "");
        var endday = $("#endday").val().replace(/\./gi, "");

        var param = {
            chaCd: $('#chacd').val(),
            chaName: $('#chaName').val(),
            amtchkTy: $("input[name=amtchkty]:checked").val(),
            chaCloseChk: $("input[name=chaClose]:checked").val(),
            chatrty: $("input[name=chatrTySearch]:checked").val(),
            calDateFrom: startday,
            calDateTo: endday,
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

    //데이터 새로고침
    function fnGrid(result, obj) {
        var str = '';
        $('#count').text(result.count);
        if (result == null || result.count <= 0) {
            str += '<tr><td colspan="14" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + NVL(v.rn) + '</td>';
                if (NVL(v.bnkRegiDt) != '') {
                    str += '<td id="regDt">' + NVL(v.chaNewRegDt) + '</td>';
                } else {
                    str += '<td id="regDt">' + NVL(v.chaNewRegDt) + '</td>';
                }
                str += '<td>' + NVL(v.bnkAcptDt) + '</td>';
                str += '<td>' + NVL(v.chaCd) + '</td>';
                str += '<td>' + NVL(v.mchtId) + '</td>';
                str += '<td>' + NVL(v.chaName);
                str += '<button type="button" class="btn btn-xs btn-primary pull-right" onclick="viewChaInfoPop(\'' + v.chaCd + '\')"><i class="fa fa-file-text-o"></i></button>';
                str += '</td>';
                str += '<td>' + NVL(v.chrHp) + '</td>';
                str += '<td>' + NVL(v.accNoCnt) + '</td>';
                if (NVL(v.amtchkTy) == 'Y') {
                    str += '<td class="text-success">승인</td>';
                }else if (NVL(v.amtchkTy) == 'L'){
                    str += '<td class="text-success">한도</td>';
                } else {
                    str += '<td class="text-success">통지</td>';
                }
                if (NVL(v.chaCloseChk) == 'Y') {
                    str += '<td class="text-warning">부적합</td>';
                } else {
                    str += '<td class="text-success">적합</td>';
                }
                // 고객분류 코드 정의되면 수정해야 함.
                if (NVL(v.chatrty) == '01') {
                    str += '<td><button type="button" class="btn btn-xs btn-success btn-outline" onclick="updateChaClass(\'' + v.chaCd + '\' , \'03\' )" >WEB</button></td>';
                } else {
                    str += '<td><button type="button" class="btn btn-xs btn-primary btn-outline"  onclick="updateChaClass(\'' + v.chaCd + '\', \'01\' );"  >API</button></td>';
                }
                // // 출금동의서
                // if (NVL(v.cmsFileName) == '') {
                //     str += '<td><button type="button" class="btn btn-xs btn-success btn-outline"  onclick="updateCmsFileUpload(\'' + v.chaCd + '\',  \'cmsfilename\' );" >업로드</button></td>';
                // } else {
                //     str += '<td><button type="button" class="btn btn-xs btn-success" onclick="fn_fileView(\'' + v.chaCd + '\', \'img\', \'' + v.waId + '\')">완료</button>'
                //     str += 	'<button type="button" class="btn btn-xs btn-danger" onclick="doDeleteCmsFile('+v.chaCd+');">삭제</button></td>';
                // }
                //해피콜
                // if (NVL(v.preChast) == '10' || v.preChast == null || v.preChast == '') {
                //     str += '<td><button type="button" class="btn btn-xs btn-success btn-outline" onclick="updateHappyCall(\'' + v.chaCd + '\' , \'' + v.chrMail + '\', \'' + v.chaOffNo + '\', \'' + v.cmsFileName + '\' )" >미완료</button></td>';
                // } else {
                //     str += '<td><button type="button" class="btn btn-xs btn-success">완료</button></td>';
                // }
                //은행 승인완료 == 다모아 이용대기
                if (NVL(v.chast) == 'ST01') {
                    str += '<td><button class="btn btn-xs btn-primary  btn-outline"  onclick="updateChaAssign(\'' + v.chaCd + '\' , \'ST06\', \'' + v.cmsFileName + '\'  , \'' + v.preChast + '\', \''+ NVL(v.agtCd) + '\' , \''+ NVL(v.mchtId) + '\'  )" >승인</button>';
                    str += '<button class="btn btn-xs btn-danger btn-outline"  onclick="updateChaAssign(\'' + v.chaCd + '\' , \'ST07\', \'' + v.cmsFileName + '\'  , \'' + v.preChast + '\', \''+ NVL(v.agtCd) + '\' , \''+ NVL(v.mchtId) + '\'  )">거부</button></td>';
                } else if (NVL(v.chast) == 'ST06') {
                    str += '<td><button class="btn btn-xs btn-primary" >승인</button></td>';
                } else {
                    str += '<td><button class="btn btn-xs btn-danger"  >' + getChastNameDamoa(NVL(v.chast)) + '</button></td>';
                }
                // str += '<td><button type="button" class="btn btn-xs btn-info " onclick="popup_new_collecter_memo(\'' + v.chaCd + '\' ,\'' + NVL(v.daMngMemo) + '\')" ><i class="fa fa-pencil-square-o"></i>메모</button><td>'
                str += '</tr>';
            });
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

    // 기관 상세보기
    function viewChaInfoPop(chacd) {
        $("#popup-new-collecter-info").modal({
            backdrop: 'static',
            keyboard: false
        });
        MODE = "N";
        collector_info_init(MODE, chacd);
    }

    //고객분류 정정 처리
    function updateChaClass(chacd, cd ) {
        var param = {
            chaCd: chacd,
            flag: 1,
            // chatrty: cd.replace(/(\s*)/g, ""),
            regDt: $("#regDt").val(),
            chast: cd

    };

        swal({
            type: 'question',
            html: "선택하신 기관정보의 고객분류 설정을 수정하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                updateChaListConfig(param, "C");
            }
        });
    }

    function updateChaListConfig(inParam, str) {
        var msg = "기관정보수정 완료되었습니다.";
        var param = inParam;
        var url = "/sys/chaMgmt/updateChaInfoConfig";
        if (str == 'Y') { //승인
            msg = "최종 승인이 완료되었습니다.";
        } else if (str == 'N') {
            msg = "승인이 거부처리되었습니다. 할당된 기관코드는 삭제됩니다.";
        }

        $.ajax({
            type: "post",
            url: url,
            async: true,
            data: JSON.stringify(param),
            contentType: "application/json; charset=utf-8",
            success: function (result) {
                if (result.retCode == "0000") {

                    $("#popup-withdrawal-agreement-upload").modal("hide");
                    swal({
                        type: 'success',
                        text: msg,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: '확인'
                    }).then(function (result) {
                        if (result.value) {
                            $("#popup-new-collecter-memo").modal("hide");
                            pageChange();
                        }
                    });
                } else {
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

    //출금동의서 업로드 처리
    function updateCmsFileUpload(chacd, cd) {
        $("#popup-withdrawal-agreement-upload").modal({
            backdrop: 'static',
            keyboard: false
        });

        $('#fileFormChacd').val(chacd);
        $('#filename').val("");
    }

    function doUploadCmsFile() {
        if ($('#filename').val() == "") {
            swal({
                type: 'info',
                text: '업로드할 출금동의서를 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var fileext = getExtensionOfFilename($('#filename').val());

        if (fileext == "" || fileext.toLowerCase() != ".jpg") {
            swal({
                type: 'info',
                text: 'jpg 파일만 업로드 가능합니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        swal({
            type: 'question',
            html: "출금동의서를 등록하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                var formData = new FormData($("#fileForm")[0]);
                formData.append("chaCd", $('#fileFormChacd').val());
                formData.append("flag", 2);
                $.ajax({
                    type: "POST",
                    url: "/sys/chaMgmt/uploadCmsFile",
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (result) {
                        if (result.retCode == "0000") {
                            swal({
                                type: 'success',
                                text: '출금동의서가 등록되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    $("#popup-withdrawal-agreement-upload").modal("hide");
                                    pageChange();
                                }
                            });
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
        });
    }
    
    //출금동의서 삭제 처리
    function doDeleteCmsFile(chaCd) {
		var chaCd = chaCd;
        swal({
            type: 'question',
            html: "출금동의서를 삭제하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
            	var url = "/sys/chaMgmt/deleteCmsFile";
            	var param = {
            			chaCd : chaCd,
            			flag : 22
            		};
                $.ajax({
                    type: "post",
                    async : true,
                    url: url,
                    contentType : "application/json; charset=utf-8",
                    data: JSON.stringify(param),
                    success: function (result) {
                        if (result.retCode == "0000") {
                            swal({
                                type: 'success',
                                text: '출금동의서가 삭제되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    pageChange();
                                }
                            });
                        } else {
                            swal({
                                type: 'warning',
                                text: '처리중 시스템 오류가 발생 하였습니다.\n지속적으로 오류가 발생 할 경우 시스템관리자에게 문의 하십시오.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            });
                        }
                    }
                });
            }
        });
    }

    //해피콜 처리
    function updateHappyCall(chacd, email, chaOffNo, fileNm) {
        var stList = [];
        var ecareNo = "200";
        var lgpw = chacd + "_" + chaOffNo.substring(chaOffNo.length - 4, chaOffNo.length);
        stList.push(chacd);

        // // 출금동의서 완료확인
        // if (fileNm == null || fileNm == 'null' || fileNm == '') {
        //     swal({
        //         type: 'info',
        //         text: '출금동의서가 등록되지 않았습니다.',
        //         confirmButtonColor: '#3085d6',
        //         confirmButtonText: '확인'
        //     });
        //     return;
        // }


        $("#popup-send-email").modal({
            backdrop: 'static',
            keyboard: false
        });
        fn_emailInit(stList, ecareNo, lgpw);
    }

    //승인 및 거부 처리
    function updateChaAssign(chacd, cd, fileYn, call, agtcd ,mchtId) {
        var msgYes = "선택하신 기관정보를 승인 하시겠습니까?";
        var msgNo = "선택하신 기관정보를 승인거부 하시겠습니까? 할당된 기관코드가 삭제됩니다.";
        var msg = "";
        var str = "";
        var flag = "";

        cd = cd.replace(/(\s*)/g, "");
        // if(cd != "ST07"){
        //     if (fileYn == '' || fileYn == null || fileYn == 'null') { // 출금동의서
        //         swal({
        //             type: 'info',
        //             text: '출금동의서가 등록되지 않았습니다.',
        //             confirmButtonColor: '#3085d6',
        //             confirmButtonText: '확인'
        //         });
        //         return;
        //     }
        //     if (call == '10') { // 해피콜
        //         swal({
        //             type: 'info',
        //             text: '해피콜이 완료되지 않았습니다.',
        //             confirmButtonColor: '#3085d6',
        //             confirmButtonText: '확인'
        //         });
        //         return;
        //     }
        //     if (agtcd == "" || agtcd == null) {
        //         swal({
        //             type: 'info',
        //             text: '지점 코드가 없어서 승인할 수 없습니다.',
        //             confirmButtonColor: '#3085d6',
        //             confirmButtonText: '확인'
        //         });
        //         return;
        //     }
        // }

        if (cd == 'ST06') {    //이용승인시에 기관등록일자설정
            msg = msgYes;
            str = "Y";
            flag = 3;
        } else {
            msg = msgNo;
            str = "N";
            flag = 7;
        }

        var param = {
            chaCd: chacd,
            chast: cd,
            flag: flag,
            mchtId : mchtId
        };
        swal({
            type: 'question',
            html: msg,
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                updateChaListConfig(param, str);
            }
        });
    }

    function popup_new_collecter_memo(chacd, remark) {
        memoChacd = chacd;
        $('#daMngMemo').val('');
        var noMemo = "";
        remark = remark.trim();
        if (remark == '') {
            remark = noMemo;
        }
        $("#popup-new-collecter-memo").modal({
            backdrop: 'static',
            keyboard: false
        });
        $('#daMngMemo').val(remark);
    }

    //다모아 메모 정정 처리
    function updateDaMngMemo() {
        if (!$('#daMngMemo').val()) {
            swal({
                type: 'info',
                text: '저장할 내용이 없습니다.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        var param = {
            chaCd: memoChacd,
            flag: 4,
            daMngMemo: $('#daMngMemo').val()
        };

        swal({
            type: 'question',
            html: "입력하신 내용을 저장하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                updateChaListConfig(param);
            }
        });
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
                $('#dchaCd').val($('#chacd').val());
                $('#dchaName').val($('#chaName').val());
                $('#calDateFrom').val($('#startday').val().replace(/\./gi, ""));
                $('#calDateTo').val($('#endday').val().replace(/\./gi, ""));
                $('#damtchkTy').val($("input[name=amtchkty]:checked").val());
                $('#dchatrty').val($("input[name=chatrTySearch]:checked").val());
                $('#dchaCloseChk').val($("input[name=chaClose]:checked").val());
                $('#dsearchOrderBy').val($('#vSearchOrderBy option:selected').val());

                // 다운로드
                document.downForm.action = "/sys/chaMgmt/newChaListExcel";
                document.downForm.submit();
            }
        });
    }
</script>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>

<jsp:include page="/WEB-INF/views/include/modal/admin/upload-eb14.jsp" flush="false"/>



<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<script type="text/javascript">
    var oneDepth = "adm-nav-7";
    var twoDepth = "adm-sub-23";
    var cuPage = 1;
</script>
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>가상계좌발급요청관리</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>설정관리</a>
            </li>
            <li class="active">
                <strong>가상계좌발급요청관리</strong>
            </li>
        </ol>
        <p class="page-description">이용기관별 가상계좌 발급현황을 관리하는 화면입니다.</p>
    </div>
    <div class="col-lg-2 text-right m-t-xl">
        <button type="button" class="btn btn-md btn-w-m btn-primary" onclick="fileUploadPop('C');"><i class="fa fa-fw fa-cloud-upload"></i>등록</button>
    </div>
</div>
<input type="hidden" id="curPage" name="curPage"/>

<form id="fileForm" name="fileForm" method="post">
    <input type="hidden" name="fchacd" id="fchacd"/>
    <input type="hidden" name="fchaname" id="fchaname"/>
    <input type="hidden" name="stList" id="stList"/>
    <input type="hidden" name="searchOrderBy" id="searchOrderBy"/>
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
                                    <label class="form-label block">기관명</label>
                                    <input type="text" class="form-control" name="chaname" id="chaname" maxlength="100" >
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" name="chaStItem" id="chaStItemAll" value="">
                                            <label for="chaStItemAll"> 전체 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" name="chaStItem" id="ST06" value="ST06"
                                                   onclick="fn_reqChecked();">
                                            <label for="ST06"> 정상 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" name="chaStItem" id="ST08" value="ST08"
                                                   onclick="fn_reqChecked();">
                                            <label for="ST08"> 정지 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" name="chaStItem" id="ST02" value="ST02"
                                                   onclick="fn_reqChecked();">
                                            <label for="ST02"> 해지 </label>
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
                            전체건수 : <strong class="text-success"><span id="totalvanocnt">0</span></strong> 건
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="vSearchOrderBy" id="vSearchOrderBy" onchange="pageChange();">
                                <option value="chaname">기관명순 정렬</option>
                                <option value="chast">기관상태순 정렬</option>
                            </select>
                            <select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
                                <option value="10">10개씩 조회</option>
                                <option value="20">20개씩 조회</option>
                                <option value="50">50개씩 조회</option>
                                <option value="100">100개씩 조회</option>
                                <option value="200">200개씩 조회</option>
                            </select>
                            <button class="btn btn-md btn-primary" type="button" onclick="fn_fileSave();">파일저장</button>
                            <button class="btn btn-md btn-primary" type="button" onclick="fn_fileSave();">가상계좌업로드</button>

                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center">
                                <colgroup>
                                    <col width="50">
                                    <col width="100">
                                    <col width="100">
                                    <col width="200">
                                    <col width="120">
                                    <col width="120">
                                    <col width="120">
                                    <col width="120">
                                    <col width="120">
                                    <col width="120">
                                    <col width="120">
                                    <col width="100">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>은행코드</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>업종</th>
                                        <th>업태</th>
                                        <th>기관상태</th>
                                        <th>할당</th>
                                        <th>사용</th>
                                        <th>대기</th>
                                        <th>잔여</th>
                                        <th>추가</th>
                                    </tr>
                                </thead>

                                <tbody id="resultBody">
                                    <tr>
                                        <td colspan=13 style="text-align: center;">[조회된 내역이 없습니다.]</td>
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
    <div class="modal-backdrop-back-spinner"></div>
</div>

<!-- 어드민용 스피너 추가 -->
<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>

<!-- 추가발급 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/vano-acc-req.jsp" flush="false"/>

<script type="text/javascript">
    $(document).ready(function () {
        $(".btn-lookup-collecter").click(function () {
            $("#lookup-collecter-popup").modal({
                backdrop: 'static',
                keyboard: false
            });
            $("#popChacd").val($("#chacd").val());
            $("#popChaname").val('');
            fn_ListCollector();
        });

        //기관상태 전체선택 토글
        $("#chaStItemAll").click(function () { //만약 전체 선택 체크박스가 체크된상태일경우
            if ($("#chaStItemAll").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
                $("input[name=chaStItem]").prop("checked", true); // 전체선택 체크박스가 해제된 경우
            } else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
                $("input[name=chaStItem]").prop("checked", false);
            }
        });

        $("#chaStItemAll").prop("checked", true);

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
        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }

        var stList = [];
        var checkbox = $("input[name=chaStItem]:checked");

        checkbox.map(function (i) {
            if ($(this).val() != '' && $(this).val() != null) {
                stList.push($(this).val());
            }
        });

        var url = "/sys/vanoMgmt/getVanoIssuedList";
        var param = {
            chacd: $('#chacd').val(),
            chaname: $('#chaname').val(),
            stList: stList,
            searchOrderBy: $('#vSearchOrderBy').val(),
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

    //데이터 새로고침
    function fnGrid(result, obj) {
        var str = '';
        $('#totalvanocnt').text(result.count);

        var normal = '';
        var highway = '';

        for(var i = 0; i < result.valist.length; i++){
            if(result.valist[i].fgcd == '20008155'){
                normal = result.valist[i].vacnt;
            }else if(result.valist[i].fgcd == '20008153'){
                highway = result.valist[i].vacnt;
            }
        }

        if (result == null || result.count <= 0) {
            str += '<tr><td colspan="14" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + NVL(v.rn) + '</td>';
                str += '<td>' + NVL(v.fgcd) + '</td>';
                str += '<td>' + NVL(v.chaCd) + '</td>';
                str += '<td>' + NVL(v.chaName) + '</td>';
                str += '<td>' + NVL(v.chastatus) + '</td>';
                str += '<td>' + NVL(v.chatype) + '</td>';

                if (NVL(v.chast) == 'ST06') {
                    str += '<td class="text-success">' + getChastName(NVL(v.chast)) + '</td>';
                } else {
                    str += '<td class="text-warning">' + getChastName(NVL(v.chast)) + '</td>';
                }

                str += '<td class="text-success">' + v.acnt + '</td>';
                str += '<td class="text-success">' + v.ycnt + '</td>';
                str += '<td class="text-success">' + v.wcnt + '</td>';
                str += '<td class="text-success">' + v.rcnt + '</td>';

                if (NVL(v.chast) == "ST06") {
                    str += '<td style="text-align: center;">'

                    if(v.fgcd == '20008155'){
                        str += '<button type="button" class="btn btn-xs btn-primary pull-right" onclick="setChacdChaname(\'' + v.fgcd + '\' , \'' + v.chaCd + '\' , \'' + v.chaName + '\' , \'' + v.chast + '\', \'' + normal + '\', \'' + v.chatrty + '\')">';
                    }else if(v.fgcd == '20008153'){
                        str += '<button type="button" class="btn btn-xs btn-primary pull-right" onclick="setChacdChaname(\'' + v.fgcd + '\' , \'' + v.chaCd + '\' , \'' + v.chaName + '\' , \'' + v.chast + '\', \'' + highway + '\', \'' + v.chatrty + '\')">';
                    }

                    str += '<i class="fa fa-file-text-o"></i> 발급요청</button>';
                } else {
                    str += '<td>'
                }

                str += '</td>'
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
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

    function setChacdChaname(fgcd, chacd, chaname, chast, vacnt, chatrty) {
        if(vacnt == '' || vacnt == null){
            vacnt = '0';
        }

        $('#popup-vano-acc-req').modal({
            backdrop: 'static',
            keyboard: false
        });

        $('#regfgcd').text(fgcd);
        $('#regchacd').text(chacd);
        $('#regchaname').text(chaname);
        $('#regvacnt').text("(추가발급가능개수 : "+vacnt+")");
        $('#regvacnt').val(vacnt);
        $('#regchast').text(getChastName(chast));
        $('#regchatrty').val(chatrty);

        $('#acccnt').focus();
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

    //고객분류 정정 처리
    function doVanoIssueReq() {
        if ($('#acccnt').val() == "" || $('#acccnt').val() == "0") {
            swal({
                type: 'info',
                text: "추가 발급계좌 수를 확인해 주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function (result) {
                if (result.value) {
                    init();
                }
            });

            return;
        }

        if(Number($('#acccnt').val()) > Number($('#regvacnt').val())){
            swal({
                type: 'info',
                text: "가상계좌가 부족합니다. 잔여 가상계좌를 확인해주세요.",
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            }).then(function (result) {
                if (result.value) {
                    init();
                }
            });

            return;
        }

        var param = {
            fgcd: $('#regfgcd').text(),
            chacd: $('#regchacd').text(),
            acccnt: $('#acccnt').val(),
            remark: $('#reqmemo').val(),
            chatrty: $('#regchatrty').val()
        };

        swal({
            type: 'question',
            html: "가상계좌 추가 발급을 요청하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                $('.spinner-area').css('display', 'block');

                var url = "/sys/vanoMgmt/doVanoIssueReq";
                $.ajax({
                    type: "post",
                    url: url,
                    async: true,
                    data: JSON.stringify(param),
                    contentType: "application/json; charset=utf-8",
                    success: function (result) {
                        if (result.retCode == "0000") {
                            $('.spinner-area').css('display', 'none');
                            $("#popup-new-collecter-memo").modal("hide");
                            $("#popup-withdrawal-agreement-upload").modal("hide");
                            swal({
                                type: 'success',
                                text: '가상계좌 발급요청을 완료되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    init();
                                    $('#popup-vano-acc-req').modal('hide');
                                    fnSearch();
                                }
                            });
                        }else if (result.retCode == "9999"){
                            $('.spinner-area').css('display', 'none');
                            $("#popup-new-collecter-memo").modal("hide");
                            $("#popup-withdrawal-agreement-upload").modal("hide");
                            swal({
                                type: 'warning',
                                text: '고속도로 전송 대기중인 가상계좌 추가 채번 건이 존재합니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    init();
                                    $('#popup-vano-acc-req').modal('hide');
                                    fnSearch();
                                }
                            });

                        }else {
                            swal({
                                type: 'warning',
                                text: result.retMsg,
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    init();
                                    $('#popup-vano-acc-req').modal('hide');
                                    $('.spinner-area').css('display', 'none');
                                    fnSearch();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    // 파일저장
    function fn_fileSave() {
        if ($('#totalvanocnt').text() == 0) {
            swal({
                type: 'info',
                text: "다운로드할 데이터가 없습니다.",
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
                $('#fchaCd').val($('#chacd').val());
                $('#fchaName').val($('#chaname').val());
                
                //기관상태
                var stList = [];
                var checkbox = $("input[name=chaStItem]:checked");
                checkbox.map(function (i) {
                    if ($(this).val() != '' && $(this).val() != null) {
                        stList.push($(this).val());
                    }
                });

                $('#stList').val(stList);
                $('#searchOrderBy').val($('#vSearchOrderBy option:selected').val());

                document.fileForm.action = "/sys/vanoMgmt/getVanoIssuedListExcel";
                document.fileForm.submit();
            }
        });
    }



    // 파일업로드
    // 파일업로드 팝업
    function fileUploadPop(type) {
        $('#fileNM').val("");
        $("#uploadTy").val(type);
        $("#popup-upload-eb14").modal({
            backdrop: 'static',
            keyboard: false
        });
    }


    function fn_reqChecked() {
        if (!$('#ST06').is(':checked') || !$('#ST08').is(':checked') || !$('#ST02').is(':checked')) {
            $('#chaStItemAll').prop('checked', false);
        } else {
            $('#chaStItemAll').prop('checked', true);
        }
    }

    function init(){
        $('#acccnt').val("");
        $('#reqmemo').val("");
    }
</script>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<script>
    var oneDepth = "adm-nav-9";
    var twoDepth = "adm-sub-27";
</script>

<form id="fForm" name="fForm" method="post">
    <input type="hidden" id="fstartDt" name="startDt">
    <input type="hidden" id="fendDt" name="endDt">
    <input type="hidden" id="fchaCd" name="chaCd">
    <input type="hidden" id="fchaName" name="chaName">
    <input type="hidden" id="fsearchOption" name="searchOption">
    <input type="hidden" id="fkeyword" name="keyword">
    <input type="hidden" id="fstList" name="stList">
    <input type="hidden" id="fconsentTy" name="consentTy">
    <input type="hidden" id="forderBy" name="orderBy">
</form>

</div>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>자동이체대상생성</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>설정관리</a>
            </li>
            <li class="active">
                <strong>자동이체대상생성</strong>
            </li>
        </ol>
        <p class="page-description">자동이체 신청 및 대상을 등록/조회하는 화면입니다.</p>
    </div>
    <div class="col-lg-2 text-right m-t-xl">
        <button type="button" class="btn btn-md btn-w-m btn-primary" onclick="fileUploadPop('C');"><i class="fa fa-fw fa-cloud-upload"></i>등록</button>
    </div>
</div>

<div class="wrapper-content">
    <div class="animated fadeInRight article">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-content">
                        <form>
                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">동의일자</label>
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
                                    <label class="form-label block">동의방법</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="consentAll" value="All" name="consentTy">
                                                <label for="consentAll"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="signed" value="signed" name="consentTy"
                                                       onclick="fn_consentTyChked();">
                                                <label for="signed"> 서면 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="ars" value="ars" name="consentTy"
                                                       onclick="fn_consentTyChked();">
                                                <label for="ars"> 녹취 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-3">
                                    <label class="form-label block">기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-10">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid"
                                                   name="chacd" id="chacd" maxlength="50">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-3">
                                    <label class="form-label block">기관명</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-10">
                                            <input type="text" class="form-control ng-untouched ng-pristine ng-valid"
                                                   name="chacd" id="chaname" maxlength="50">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">조회조건</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="reqSt" value="" name="cmsReqSt">
                                                <label for="reqSt"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CST01" value="CST01" name="cmsReqSt" onclick="fn_checked();">
                                                <label for="CST01"> 미등록 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CST02" value="CST02" name="cmsReqSt" onclick="fn_checked();">
                                                <label for="CST02"> 대기 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CST03" value="CST03" name="cmsReqSt" onclick="fn_checked();">
                                                <label for="CST03"> 등록중 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CST04" value="CST04" name="cmsReqSt" onclick="fn_checked();">
                                                <label for="CST04"> 등록 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CST00" name="cmsReqSt">
                                                <label for="CST00"> 해지 </label>
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
                                                    <option value="feeOwnNo">납부자번호</option>
                                                    <option value="feeAccIden">사업자번호</option>
                                                </select>
                                            </span>
                                        <input type="text" class="form-control" id="keyword" maxlength="100">
                                    </div>
                                </div>

                                <div class="col-md-6">
                                </div>
                            </div>

                            <hr>

                            <div class="text-center">
                                <button class="btn btn-primary" type="button" onclick="fn_search();">대상조회</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <div class="ibox">
                <div class="ibox-title">
                    <div class="col-lg-6">
                        전체 건수 : <strong class="text-success" id="count"></strong> 건
                    </div>
                    <div class="col-lg-6 form-inline form-searchOrderBy">
                        <select class="form-control" id="orderBySel" onchange="fn_search();">
                            <option value="reqDt">동의일순</option>
                            <option value="chaCd">기관코드순</option>
                            <option value="chaName">기관명순</option>
                            <option value="bnkCd">은행명순</option>
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
                        <table class="table table-bordered table-hover table-stripped table-align-center has-ellipsis">
                            <colgroup>
                                <col width="20">
                                <col width="30">
                                <col width="100">
                                <col width="100">
                                <col width="300">
                                <col width="130">
                                <col width="130">
                                <col width="300">
                                <col width="130">
                                <col width="130">
                                <col width="150">
                                <col width="130">
                                <col width="90">
                                <col width="150">
                            </colgroup>
                            <thead>
                                <tr>
                                    <th>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="row-th" value="option1">
                                            <label for="row-th"></label>
                                        </div>
                                    </th>
                                    <th>NO</th>
                                    <th>동의일자</th>
                                    <th>기관코드</th>
                                    <th>기관명</th>
                                    <th>사업자번호</th>
                                    <th>납부자번호</th>
                                    <th>예금주</th>
                                    <th>사업자번호(생년월일)</th>
                                    <th>출금은행</th>
                                    <th>출금계좌</th>
                                    <th>신청상태</th>
                                    <th>동의방법</th>
                                    <th>상태변경</th>
                                </tr>
                            </thead>

                            <tbody id="resultBody"></tbody>
                        </table>
                    </div>

                    <div class="row m-b-lg">
                        <div class="col-lg-12 text-center">
                            <div class="btn-group" id="PageArea"></div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-6 text-left">
                            <button type="button" class="btn btn-primary" onclick="fn_reqAtOnce();">일괄등록</button>
                        </div>
                        <div class="col-lg-6 text-right">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
        <div class="ibox float-e-margins">
            <div class="ibox-content table-responsive">
                <form>
                <div class="col-lg-3">
                    <span><h3>자동이체 파일 생성</h3></span>
                </div>
                <div class="col-lg-6 text-left">
                    <button class="btn btn-w-m btn-primary" onclick="fn_targetDownload('EB13');">출금이체신청내역 다운로드</button>
                    <button class="btn btn-w-m btn-primary" onclick="fn_targetDownload('EI13');">출금동의증빙자료 다운로드</button>
                </div>
                </form>
            </div>
        </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<jsp:include page="/WEB-INF/views/include/modal/admin/upload-eb14.jsp" flush="false"/>

<script>
    $(function () {
        $('input[name=cmsReqSt]').prop('checked', true);
        $("input[name=consentTy]").prop("checked", true);
        setMonthTerm(0);
        fn_search();

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

        $("#reqSt").click(function () {
            if ($("#reqSt").prop("checked")) {
                $("input[name=cmsReqSt]").prop("checked", true);
            } else {
                $("input[name=cmsReqSt]").prop("checked", false);
            }
        });

        $("#consentAll").click(function () {
            if ($("#consentAll").prop("checked")) {
                $("input[name=consentTy]").prop("checked", true);
            } else {
                $("input[name=consentTy]").prop("checked", false);
            }
        });

        $("#row-th").click(function(){
            if($("#row-th").prop("checked")) {
                $("input[name=checkOne]").prop("checked",true);
            } else {
                $("input[name=checkOne]").prop("checked",false);
            }
        });

    });

    // 대상조회
    function fn_search(page) {
        var cuPage = 1;
        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }
        var startday = $('#startday').val().replace(/\./gi, "");
        var endday = $('#endday').val().replace(/\./gi, "");


        var stList = [];
        var checkbox = $("input[name='cmsReqSt']:checked");
        checkbox.map(function (i) {
            if ($(this).val() != '' && $(this).val() != null) {
                stList.push($(this).val());
            }
        });

        var consentTy = ""
        if($("#consentAll").prop("checked")){
            consentTy = "All";
        }else{
            consentTy = $("input[name='consentTy']:checked").val();
        }

        var url = "/sys/auto/selAutoTranTarget";
        var param = {
            startDt: startday,
            endDt: endday,
            chaCd: $('#chacd').val(),
            chaName: $('#chaname').val(),
            stList: stList,
            consentTy : consentTy,
            searchOption: $('#searchOpt').val(),
            keyword: $('#keyword').val(),
            curPage: cuPage,
            orderBy: $("#orderBySel option:selected").val(),
            pageScale: $("#pageScale option:selected").val(),
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
        $('#count').text(data.count);
        var str = '';
        if (data.count <= 0) {
            str += '<tr><td colspan="14" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(data.list, function (k, v) {
                str += '<tr>';
                str += '<td>';
                str += '<div class="checkbox checkbox-primary checkbox-inline">';
                str += '<input type="checkbox" id="row-' + v.rn + '" name="checkOne" value="' + v.chaCd + '" onclick="fn_listCheck();">';
                str += '<label for="row-' + v.rn + '"></label>';
                str += '</div>';
                str += '</td>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + nullValueChange(v.cmsReqDt) + '</td>';
                str += '<td>' + v.chaCd + '</td>';
                str += '<td>' + v.chaName + '</td>';
                str += '<td>' + nullValueChange(v.chaOffNo) + '</td>';
                str += '<td>' + nullValueChange(v.finFeeOwnNo) + '</td>'; //납부자번호
                str += '<td>' + nullValueChange(v.feeAccName) + '</td>';
                str += '<td>' + nullValueChange(v.finFeeAccIdent) + '</td>';
                str += '<td>' + nullValueChange(v.bnkCd) + '</td>';
                str += '<td>' + nullValueChange(v.feeAccNo) + '</td>';
                if (v.cmsReqSt == 'CST02') {
                    str += '<td>등록대기</td>';
                } else if (v.cmsReqSt == 'CST03') {
                    str += '<td>등록중</td>';
                } else if (v.cmsReqSt == 'CST04') {
                    str += '<td>등록</td>';
                } else {
                    str += '<td>미등록</td>';
                }
                if(v.consentTy == "W00002") {
                    str += '<td>녹취</td>';
                }else{
                    str += '<td>서면</td>';
                }
                str += '<td>';
                if(v.cmsReqSt == 'CST02'){
                    str += '<button type="button" class="btn btn-xs btn-success" onclick="fn_cancelReq(\'' + v.chaCd + '\', \'' + v.finFeeOwnNo + '\')">등록취소</button>';
                }else if(v.cmsReqSt == 'CST01'){
                    str += '<button type="button" class="btn btn-xs btn-success" onclick="fn_reqOneEach(\'' + v.chaCd + '\')">등록</button>';
                }else{
                    str += '<button type="button" class="btn btn-xs btn-success" onclick="fn_cancelReq(\'' + v.chaCd + '\', \'' + v.finFeeOwnNo + '\')" disabled></button>';
                }
                str += '</td>';
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    function list(page, val) {
        fn_search(page);
    }

    function fn_cancelReq(val, lue){
        swal({
            type: 'question',
            html: 'CMS 등록을 취소 하시겠습니까?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value) {
                var url = "/sys/auto/cancelCMS";
                var param = {
                    chaCd: val,
                    finFeeOwnNo: lue
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
                            text: '취소 되었습니다',
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

    // 출금동의증빙자료(EI13), 출금이체신청내역(EB13), 수수료출금(EB21) 다운로드
    function fn_targetDownload(str) {
        if (str == 'EI13') {
            window.open('/sys/rest/kftc-payer/ei13');
        } else {
            window.open('/sys/rest/kftc-payer/eb13');
        }
    }

    // 파일업로드 팝업
    function fileUploadPop(type) {
        $('#fileNM').val("");
        $("#uploadTy").val(type);
        $("#popup-upload-eb14").modal({
            backdrop: 'static',
            keyboard: false
        });
    }


    function fn_checked() {
        if (!$('#CST01').is(':checked') || !$('#CST02').is(':checked') || !$('#CST03').is(':checked') || !$('#CST04').is(':checked')) {
            $('#reqSt').prop('checked', false);
        } else {
            $('#reqSt').prop('checked', true);
        }
    }

    function fn_consentTyChked() {
        if (!$('#ars').is(':checked') || !$('#signed').is(':checked')) {
            $('#consentAll').prop('checked', false);
        } else {
            $('#consentAll').prop('checked', true);
        }
    }

    function fn_listCheck() {
        var stList = [];
        var idx = 0;
        var num = 0;
        var check = $("input[name='checkOne']");
        check.map(function (i) {
            if ($(this).is(':checked') == true) {
                idx++;
            }
            num++;
        });

        if (num == idx) {
            $('#row-th').prop('checked', true);
        } else {
            $('#row-th').prop('checked', false);
        }
    }

    /**
     * 일괄등록
     */
    function fn_reqAtOnce() {
        var url = "/sys/auto/autoTranReqAtOnce";
        var param = {};

        var startday = $('#startday').val().replace(/\./gi, "");
        var endday = $('#endday').val().replace(/\./gi, "");

        var stList = [];
        var checkbox = $("input[name='cmsReqSt']:checked");
        checkbox.map(function (i) {
            if ($(this).val() != '' && $(this).val() != null) {
                stList.push($(this).val());
            }
        });

        var consentTy = ""
        if ($("#consentAll").prop("checked")) {
            consentTy = "All";
        } else {
            consentTy = $("input[name='consentTy']:checked").val();
        }

        var param = {
            startDt: startday,
            endDt: endday,
            chaCd: $('#chacd').val(),
            chaName: $('#chaname').val(),
            stList: stList,
            consentTy: consentTy,
            searchOption: $('#searchOpt').val(),
            keyword: $('#keyword').val(),
            orderBy: $("#orderBy option:selected").val()
        };

        swal({
            type: 'question',
            html: "등록 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (result.value !== true) {
                return;
            }
            $.ajax({
                type: "post",
                url: url,
                data: param,
                contentType: "application/json; charset=UTF-8",
                data: JSON.stringify(param),
                success: function (data) {
                    if (data.retCode == "0000") {
                        swal({
                            type: 'success',
                            text: '등록요청 되었습니다.',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        }).then(function (result) {
                            if (result.value) {
                                fn_search();
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
        });
    }

    /**
     * 개별등록
     */
    function fn_reqOneEach(val) {
        var url = "/sys/auto/autoTranReqAtOnce";
        var param = {
            chaCd: val
        };

        swal({
            type: 'question',
            html: "등록 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소',
            reverseButtons: true
        }).then(function (result) {
            if (!result.value) {
                return;
            }

            $.ajax({
                type: "post",
                url: url,
                data: param,
                contentType: "application/json; charset=UTF-8",
                data: JSON.stringify(param),
                success: function (data) {
                    if (data.retCode == "0000") {
                        swal({
                            type: 'success',
                            text: '등록요청 되었습니다.',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        }).then(function (result) {
                            if (result.value) {
                                fn_search();
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
                $('#fstartDt').val($('#startday').val().replace(/\./gi, ""));
                $('#fendDt').val($('#endday').val().replace(/\./gi, ""));
                $('#fchaCd').val($('#chacd').val());
                $('#fchaName').val($('#chaname').val());
                $('#fsearchOption').val($('#searchOpt').val());
                $('#fkeyword').val($('#keyword').val());

                var stList = [];
                var checkbox = $("input[name='cmsReqSt']:checked");
                checkbox.map(function (i) {
                    if ($(this).val() != '' && $(this).val() != null) {
                        stList.push($(this).val());
                    }
                });

                var consentTy = ""
                if ($("#consentAll").prop("checked")) {
                    consentTy = "All";
                } else {
                    consentTy = $("input[name='consentTy']:checked").val();
                }
                $('#fstList').val(stList);
                $('#fconsentTy').val(consentTy);
                $('#forderBy').val($("#orderBySel option:selected").val());

                // 다운로드
                document.fForm.action = "/sys/auto/autoTranTargetChaDownload";
                document.fForm.submit();
            }
        });
    }
</script>

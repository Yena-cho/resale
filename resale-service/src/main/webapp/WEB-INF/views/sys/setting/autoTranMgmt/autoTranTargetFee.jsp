<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<script>
    var oneDepth = "adm-nav-9";
    var twoDepth = "adm-sub-29";
</script>

<script type="text/javascript">

    // 수수료 출금 조회
    var acPage = 1;

    function fn_accSearch(page) {
        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            acPage = page;
        }

        var startday = $('#startday').val().replace(/\./gi, "");
        var endday = $('#endday').val().replace(/\./gi, "");

        var idx=0;
        var stList = [];
        var checkbox = $("input[name='rcpSt']:checked");
        checkbox.map(function (i) {
            stList.push($(this).val());
            idx++;
        });

        if(idx == 0 ){
            checkbox = $("input[name='rcpSt']");
            checkbox.map(function (i) {
                stList.push($(this).val());
                idx++;
            });
        }

        var orderBy = $("#orderBySel option:selected").val();
        var url = "/sys/auto/selAutoTranAccNo";
        var param = {
            startDt: startday,
            endDt: endday,
            stList: stList,
            chaCd: $('#chacd').val(),
            chaName: $('#chaname').val(),
            searchOption: $('#searchOpt').val(),
            keyword: $('#keyword').val(),
            curPage: acPage,
            pageScale: $("#pageScale option:selected").val(),
            orderBy: orderBy
        };
        $.ajax({
            type: "post",
            url: url,
            data: param,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                fn_accGrid(data, 'accGrid');
                sysajaxPaging(data, 'accPageArea');
            }
        });
    }

    function fn_accGrid(data, obj) {
        $('#accCount').text(data.count);
        $('#failCount').text(data.failCnt);
        var str = '';
        if (data.count <= 0) {
            str += '<tr><td colspan="14" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(data.list, function (k, v) {
                str += '<tr>';
                str += '<td>';
                str += '<div class="checkbox checkbox-primary checkbox-inline">';
                str += '<input type="checkbox" id="row-' + v.rn + '" name="checkOne" value="' + v.iD + '" onclick="fn_listCheck();">';
                str += '<label for="row-' + v.rn + '"></label>';
                str += '</div>';
                str += '</td>';
                str += '<td>' + v.rn + '</td>';
                str += '<td>' + nullValueChange(getDateFormatDot(v.rcpDt)) + '</td>';
                str += '<td>' + v.chaCd + '</td>';
                str += '<td>' + v.chaName + '</td>';
                str += '<td>' + nullValueChange(v.chaOffNo) + '</td>';
                str += '<td>' + nullValueChange(v.finFeeOwnNo) + '</td>'; //납부자번호
                str += '<td>' + nullValueChange(v.feeAccName) + '</td>';
                str += '<td>' + nullValueChange(v.finFeeAccIdent) + '</td>';
                str += '<td>' + nullValueChange(v.bankCd) + '</td>';
                str += '<td>' + nullValueChange(v.feeAccNo) + '</td>';
                if (v.rcpSt == 'K00001') {
                    str += '<td>대기</td>';
                } else if (v.rcpSt == 'K00002') {
                    str += '<td>처리 중</td>';
                } else if (v.rcpSt == 'K00003') {
                    str += '<td>처리 완료</td>';
                } else {
                    str += '<td>요청 전</td>';
                }
                str += '<td style="text-align: right;">' + v.feeSum + '</td>';
                str += '<td>' + nullValueChange(v.resultName) + '</td>';
                str += '</tr>';
            });
        }
        $('#' + obj).html(str);
    }

    function list(page, val) {
        fn_accSearch(page);
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

    // 출금동의증빙자료(EI13), 출금이체신청내역(EB13), 수수료출금(EB21) 다운로드
    function fn_targetDownload(str) {
        window.open('/sys/rest/kftc-withdraw/eb21?payday='+replaceDot($("#payday").val()));
    }

    function fn_checked() {
        if (!$('#OST01').is(':checked') || !$('#OST02').is(':checked') || !$('#OST03').is(':checked') || !$('#OST04').is(':checked')) {
            $('#rcpSt').prop('checked', false);
        } else {
            $('#rcpSt').prop('checked', true);
        }
    }

    function fn_cancelSel() {
        var url = "/sys/auto/autoTranFeeCancel";
        var stList = [];
        var idx = 0;
        var checkbox = $("input[name='checkOne']:checked");
        checkbox.map(function (i) {
            stList.push($(this).val());
            idx++
        });

        if(idx == 0){
            swal({
                type: 'info',
                html: "하나 이상 선택해주세요",
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                reverseButtons: true
            })
            return;
        }else{
        var param = {
            stList: stList
        };
        swal({
            type: 'question',
            html: "취소 하시겠습니까?",
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
                                text: '취소 되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    fn_accSearch();
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
    }

</script>
</div>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-8">
        <h2>자동이체출금대상</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>설정관리</a>
            </li>
            <li class="active">
                <strong>자동이체출금대상</strong>
            </li>
        </ol>
        <p class="page-description">자동이체 출금요청 대상을 등록/조회/처리내역을 관리하는 화면입니다.</p>
    </div>
    <div class="col-lg-2 text-right m-t-xl">
            <button type="button" class="btn btn-md btn-w-m btn-primary" onclick="fileUploadPop('U');"><i class="fa fa-fw fa-cloud-upload"></i> 출금등록</button>
    </div>
    <div class="col-lg-2 text-left m-t-xl">
            <button type="button" class="btn btn-md btn-w-m btn-primary" onclick="fileUploadPop('R');"><i class="fa fa-fw fa-cloud-upload"></i> 결과등록</button>
    </div>
</div>

            <div class="animated fadeInRight article">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="ibox float-e-margins">
                            <div class="ibox-content">
                                <form>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <label class="form-label block">등록일자</label>
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
                                            <label class="form-label block">처리여부</label>
                                            <div class="form-group form-group-sm">
                                                <div class="checkbox checkbox-primary checkbox-inline">
                                                    <input type="checkbox" id="rcpSt" value="">
                                                    <label for="rcpSt"> 전체 </label>
                                                </div>
                                                <div class="checkbox checkbox-primary checkbox-inline">
                                                    <input type="checkbox" id="OST01" value="K00000" name="rcpSt" onclick="fn_checked();">
                                                    <label for="OST01"> 요청전 </label>
                                                </div>
                                                <div class="checkbox checkbox-primary checkbox-inline">
                                                    <input type="checkbox" id="OST02" value="K00001" name="rcpSt" onclick="fn_checked();">
                                                    <label for="OST02"> 대기 </label>
                                                </div>
                                                <div class="checkbox checkbox-primary checkbox-inline">
                                                    <input type="checkbox" id="OST03" value="K00002" name="rcpSt" onclick="fn_checked();">
                                                    <label for="OST03"> 처리 중 </label>
                                                </div>
                                                <div class="checkbox checkbox-primary checkbox-inline">
                                                    <input type="checkbox" id="OST04" value="K00003" name="rcpSt" onclick="fn_checked();">
                                                    <label for="OST04"> 처리완료 </label>
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
                                        <button class="btn btn-primary" type="button" onclick="fn_accSearch();">대상조회</button>
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
                                전체 건수 : <strong class="text-success" id="accCount"></strong> 건
                                <span class="m-r-sm m-l-sm">불능 건수 : <strong class="text-success" id="failCount"></strong> 건</span>
                            </div>
                            <div class="col-lg-6 form-inline form-searchOrderBy">
                                <select class="form-control" id="orderBySel" onchange="fn_accSearch();">
                                    <option value="reqDt">등록일순</option>
                                    <option value="chaCd">기관코드순</option>
                                    <option value="chaName">기관명순</option>
                                    <option value="bnkCd">은행명순</option>
                                </select>
                                <select class="form-control" id="pageScale" onchange="fn_accSearch();">
                                    <option value="10">10개씩 조회</option>
                                    <option value="20">20개씩 조회</option>
                                    <option value="50">50개씩 조회</option>
                                    <option value="100">100개씩 조회</option>
                                    <option value="200">200개씩 조회</option>
                                </select>
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
                                        <col width="150">
                                        <col width="300">
                                        <col width="130">
                                        <col width="130">
                                        <col width="150">
                                        <col width="130">
                                        <col width="100">
                                        <col width="100">
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
                                        <th>등록일자</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>사업자번호</th>
                                        <th>납부자번호</th>
                                        <th>예금주</th>
                                        <th>사업자번호(생년월일)</th>
                                        <th>출금은행</th>
                                        <th>출금계좌</th>
                                        <th>요청상태</th>
                                        <th>출금요청금액</th>
                                        <th>불능사유</th>
                                    </tr>
                                    </thead>
                                    <tbody id="accGrid"></tbody>
                                </table>
                            </div>

                            <div class="row m-b-lg">
                                <div class="col-lg-12 text-center">
                                    <div class="btn-group" id="accPageArea"></div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-6 text-left">
                                    <button type="button" class="btn btn-primary" onclick="fn_cancelSel();">선택취소</button>
                                </div>
                                <div class="col-lg-6 text-right">
                                    <%--<button type="button" class="btn btn-primary" onclick="fn_updateSt();">상태변경</button>--%>
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
                        <div class="col-lg-3 text-left">
                            <label class="form-label block">요청출금일자(월,일 만 적용)</label>
                            <div class="input-daterange input-group float-left" id="datepicker">
                                <input type="text" class="input-sm form-control" id="payday" name="payday" readonly="readonly"/>
                            </div>
                        </div>
                        <div class="col-lg-6 text-left">
                            <button class="btn btn-w-m btn-primary text-center" onclick="fn_targetDownload('EB21');">수수료출금 요청파일 다운로드</button>
                        </div>
                        </form>
                    </div>
                </div>
                </div>
            </div>


<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>


<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<jsp:include page="/WEB-INF/views/include/modal/admin/autoTranFeeUpload.jsp" flush="false"/>

<script>
    $(document).ready(function () {
        $("input[name=rcpSt]").prop("checked", true);
        setMonthTerm(0);
        fn_accSearch();
        $("#payday").val(getFormatCurrentDate());

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

        $("#rcpSt").click(function () { //만약 전체 선택 체크박스가 체크된상태일경우
            if ($("#rcpSt").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
                $("input[name=rcpSt]").prop("checked", true); // 전체선택 체크박스가 해제된 경우
            } else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
                $("input[name=rcpSt]").prop("checked", false);
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

    function fileUploadPop(type) {
        fn_checkType(type);
        $('#fileNM').val("");
        $("#uploadTy").val(type);
        $("#popup-autoTranFeeUpload").modal({
            backdrop: 'static',
            keyboard: false
        });

    }
</script>

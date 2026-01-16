<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<script>
    var oneDepth = "adm-nav-9";
    var twoDepth = "adm-sub-26";
</script>


<script type="text/javascript">
    // 조회
    function fn_search(page) {
        var cuPage = 1;
        if (page == null || page == 'undefined') {
            cuPage = 1;
        } else {
            cuPage = page;
        }
        var startDt = $('#startday').val().replace(/\./gi, "");
        var endDt = $('#endday').val().replace(/\./gi, "");

        var consentTy = ""
        if($("#consentAll").prop("checked")){
            consentTy = "All";
        }else{
            consentTy = $("input[name='consentTy']:checked").val();
        }

        var chaList = [];
        var stList = [];
        var cmsList = [];
        var check = $("input[name='chaSt']:checked");
        check.map(function (i) {
            if ($(this).val() != '' && $(this).val() != null) {
                chaList.push($(this).val());
            }
        });
        var checkbox = $("input[name='cmsReqSt']:checked");
        checkbox.map(function (i) {
            if ($(this).val() != '' && $(this).val() != null) {
                stList.push($(this).val());
            }
        });
        var cmsbox = $("input[name='chkcmsYN']:checked");
        cmsbox.map(function (i) {
            if ($(this).val() != '' && $(this).val() != null) {
                cmsList.push($(this).val());
            }
        });

        var chatrty = ""
        if($("#chatrtyAll").prop("checked")){
            chatrty = "All";
        }else{
            chatrty = $("input[name='chatrty']:checked").val();
        }

        var url = "/sys/auto/autoTranRegSel";
        var param = {
            startDt: startDt,
            endDt: endDt,
            chaCd: $('#chacd').val(),
            chaName: $('#chaname').val(),
            searchOption: $('#searchOpt').val(),
            keyword: $('#keyword').val(),
            consentTy: consentTy,
            cmsList: cmsList,
            stList: stList,
            chaList: chaList,
            chatrty: chatrty,
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
                $("#pageNo").val(cuPage);
            }
        });
    }

    function fn_grid(data, obj) {
        var str = '';
        $('#count').text(data.count);

        if (data.count <= 0) {
            str += '<tr><td colspan="13" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            var pos = "";
            var ext = "";
            $.each(data.list, function (k, v) {
                str += '<tr>';
                str += '<td>';
                str += '<div class="checkbox checkbox-primary checkbox-inline">';
                str += '<input type="checkbox" id="row-' + v.rn + '" name="checkOne" value="' + v.chaCd + '" onclick="fn_listCheck();">';
                str += '<label for="row-' + v.rn + '"></label>';
                str += '</div>';
                str += '</td>';
                str += '<td>' + v.rn + '</td>';
                if(v.chaSt == "ST06"){
                    str += '<td>정상</td>';
                }else if(v.chaSt == "ST04"){
                    str += '<td>정지</td>';
                }else{
                    str += '<td>해지</td>';
                }

                if(v.chatrty == "03"){
                    str += '<td>API</td>';
                }else{
                    str += '<td>WEB</td>';
                }
                str += '<td>' + v.chaCd + '</td>';
                str += '<td>' + nullValueChange(v.chaName) + '</td>';
                str += '<td>' + nullValueChange(v.chaOffNo) + '</td>';
                str += '<td>' + nullValueChange(v.bnkCd)+ '</td>';
                str += '<td>' + nullValueChange(v.feeAccNo) + '</td>';
                // cms 신청상태
                if (v.cmsReqSt == 'CST02') {
                    str += '<td class="text-warning">등록대기</td>';
                } else if (v.cmsReqSt == 'CST03') {
                    str += '<td class="text-success">등록중</td>';
                } else if (v.cmsReqSt == 'CST04') {
                    str += '<td>등록</td>';
                } else if(v.cmsReqSt == 'CST01' && v.chkCms == '동의'){
                    str += '<td class="text-danger">등록가능</td>';
                } else{
                    str += '<td class="text-danger">미등록</td>';
                }
                str += '<td>' + nullValueChange(v.chkCms) + '</td>';
                str += '<td>' + nullValueChange(v.cmsReqDt) + '</td>';
                if (v.waId != null && v.waId != '') {
                    str += '<td>';
                    if(v.consentTy == "W00002"){
                        str += '<button type="button" class="btn btn-xs btn-info" onclick="fn_fileView(\'' + v.chaCd + '\', \'aud\', \'' + v.waId + '\')">재생</button>';
                    }else{
                        str += '<button type="button" class="btn btn-xs btn-info" onclick="fn_fileView(\'' + v.chaCd + '\', \'img\', \'' + v.waId + '\')">보기</button>';
                    }
                    if(v.chkCms == '동의' && (v.cmsReqSt == 'CST02' || v.cmsReqSt == 'CST03')){
                        str += '&nbsp;<button type="button" class="btn btn-xs btn-danger" onclick="doDeleteCmsFile(\'' + v.chaCd + '\');" disabled>삭제</button>';
                    }else{
                        str += '&nbsp;<button type="button" class="btn btn-xs btn-danger" onclick="doDeleteCmsFile(\'' + v.chaCd + '\');">삭제</button>';
                    }
                    str += '</td>';
                }else {
                    str += '<td>';
                    str += '<button type="button" class="btn btn-xs btn-success btn-outline"  onclick="cmsFileUpload(\'' + v.chaCd + '\');" >업로드</button>';
                    str += '</td>';
                }
                str += '<td>';
                if(v.chkCms == '승인대기' || v.cmsReqSt == 'CST02'){
                    str += '<button type="button" class="btn btn-xs btn-success" onclick=cmsDetailInfo(\'' + v.chaCd + '\') disabled>상세보기</button>';
                }else{
                    str += '<button type="button" class="btn btn-xs btn-success" onclick="cmsDetailInfo(\'' + v.chaCd + '\', \'' + v.waId + '\');">상세보기</button>';
                }
                if(v.chkCms == '동의' && v.cmsReqSt == 'CST02'){
                    str += '<button type="button" class="btn btn-xs btn-success" onclick="fn_updAgreement(\'' + v.chaCd + '\', \'N\');" disabled>동의취소</button>';
                } else if(v.chkCms == '미동의'){
                    str += '';
                } else if(v.chkCms == '승인대기'){
                    str += '<button type="button" class="btn btn-xs btn-success" onclick="fn_updAgreement(\'' + v.chaCd + '\', \'Y\');">동의승인</button>';
                } else{
                    str += '<button type="button" class="btn btn-xs btn-success" onclick="fn_updAgreement(\'' + v.chaCd + '\', \'N\');">동의취소</button>';
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
</script>

<form id="fForm" name="fForm" method="post">
    <input type="hidden" id="startDt" name="startDt">
    <input type="hidden" id="endDt" name="endDt">
    <input type="hidden" id="fchaCd" name="chaCd">
    <input type="hidden" id="fchaName" name="chaName">
    <input type="hidden" id="searchOption" name="searchOption">
    <input type="hidden" id="fkeyword" name="keyword">
    <input type="hidden" id="cmsList" name="cmsList">
    <input type="hidden" id="stList" name="stList">
    <input type="hidden" id="chaList" name="chaList">
    <input type="hidden" id="fconsentTy" name="consentTy">
    <input type="hidden" id="fchatrty" name="chatrty">
    <input type="hidden" id="orderBy" name="orderBy">
</form>
<input type="hidden" id="pageNo"  name="pageNo"  value="1">
</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>자동출금동의관리</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>자동이체관리</a>
            </li>
            <li class="active">
                <strong>자동출금동의관리</strong>
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
                                    <label class="form-label block">동의일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control" id="startday"
                                                       readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="endday"
                                                       readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline active"
                                                        name="btnSetMonth" id="btnSetMonth0" onclick="setMonthTerm(0);">
                                                    전체
                                                </button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"
                                                        name="btnSetMonth" id="btnSetMonth1" onclick="setMonthTerm(1);">
                                                    1개월
                                                </button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline"
                                                        name="btnSetMonth" id="btnSetMonth6" onclick="setMonthTerm(6);">
                                                    6개월
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">동의상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="chkcmsAll" name="chkcmsYN" value="">
                                                <label for="chkcmsAll"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="chkcmsY" value="A00000" name="chkcmsYN" onclick="fn_chkcmsChked();">
                                                <label for="chkcmsY"> 동의 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="chkcmsN" value="A00001" name="chkcmsYN" onclick="fn_chkcmsChked();">
                                                <label for="chkcmsN"> 미동의 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="chkcmsP" value="A00002" name="chkcmsYN" onclick="fn_chkcmsChked();">
                                                <label for="chkcmsP"> 승인대기 </label>
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
                                <div class="col-md-6">
                                    <label class="form-label block">검색구분</label>
                                    <div class="input-group col-md-12">
                                         <span class="input-group-select">
                                            <select class="form-control" id="searchOpt">
                                                <option value="All">전체</option>
                                                <option value="feeAccNo">출금계좌번호</option>
                                                <option value="chaOffNo">사업자번호</option>
                                            </select>
                                        </span>
                                        <input type="text" class="form-control" id="keyword" maxlength="100">
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">CMS 신청상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="cmsReqStAll" name="cmsReqSt" value="">
                                                <label for="cmsReqStAll"> 전체</label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CST01" value="CST01" name="cmsReqSt"
                                                       onclick="fn_stChecked();">
                                                <label for="CST01"> 미등록 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CST02" value="CST02" name="cmsReqSt"
                                                       onclick="fn_stChecked();">
                                                <label for="CST02"> 등록대기 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CST03" value="CST03" name="cmsReqSt"
                                                       onclick="fn_stChecked();">
                                                <label for="CST03"> 등록중 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="CST04" value="CST04" name="cmsReqSt"
                                                       onclick="fn_stChecked();">
                                                <label for="CST04"> 등록 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="chaStAll" name="chaSt" value="">
                                                <label for="chaStAll"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="ST06" name="chaSt" value="ST06" onclick="fn_chastChked();">
                                                <label for="ST06"> 정상 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="ST04" name="chaSt" value="ST04" onclick="fn_chastChked();">
                                                <label for="ST04"> 정지 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="ST02" name="chaSt" value="ST02" onclick="fn_chastChked();">
                                                <label for="ST02"> 해지 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">고객분류</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="chatrtyAll" name="chatrty" value="All">
                                                <label for="chatrtyAll"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="01" name="chatrty" value="01" onclick="fn_chatrTyChked();">
                                                <label for="01"> WEB </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="03" name="chatrty" value="03" onclick="fn_chatrTyChked();">
                                                <label for="03"> API </label>
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
                                <option value="chaCd">기관코드순</option>
                                <option value="chaName">기관명순</option>
                                <%--<option value="reqDt">동의일순</option>--%>
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
                            <table class="table table-stripped table-align-center">
                                <colgroup>
                                    <col width="30">
                                    <col width="30">
                                    <col width="100">
                                    <col width="100">
                                    <col width="130">
                                    <col width="170">
                                    <col width="200">
                                    <col width="130">
                                    <col width="200">
                                    <col width="130">
                                    <col width="130">
                                    <col width="150">
                                    <col width="150">
                                    <col width="130">
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
                                    <th>기관상태</th>
                                    <th>고객분류</th>
                                    <th>기관코드</th>
                                    <th>기관명</th>
                                    <th>사업자번호</th>
                                    <th>은행코드</th>
                                    <th>출금계좌</th>
                                    <th>CMS신청상태</th>
                                    <th>동의상태</th>
                                    <th>동의일자</th>
                                    <th>제출서류</th>
                                    <th></th>
                                </tr>
                                </thead>

                                <tbody id="resultBody"></tbody>
                            </table>
                        </div>

                        <jsp:include page="/WEB-INF/views/include/sysPaging.jsp" flush="false"/>
                        <jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

                        <div class="row">
                            <div class="col-lg-12 text-right">
                                <button type="button" class="btn btn-primary" onclick="fn_smsNoti();"><i class="fa fa-fw fa-mobile"></i>문자발송</button>
                                <button type="button" class="btn btn-primary" onclick="fn_emailNoti();"><i class="fa fa-fw fa-envelope-o"></i>E-MAIL발송</button>
                            </div>
                        </div>
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

<!-- 출금 동의서 업로드 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/cms-upload.jsp" flush="false"/>

<!-- SMS 보내기 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/send-sms.jsp" flush="false"/>

<!-- 이메일 보내기 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/send-email.jsp" flush="false"/>

<!-- 상세보기 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/cms-detail-info.jsp" flush="false"/>

<script>
    $(document).ready(function () {
        $("input[name=chkcmsYN]").prop("checked", true);
        $("input[name=consentTy]").prop("checked", true);
        $("#CST01").prop("checked", true);
        $("#ST06").prop("checked", true);
        $("input[name=chatrty]").prop("checked", true);

        setMonthTerm(0);
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

        $("#chkcmsAll").click(function () {
            if ($("#chkcmsAll").prop("checked")) {
                $("input[name=chkcmsYN]").prop("checked", true);
            } else {
                $("input[name=chkcmsYN]").prop("checked", false);
            }
        });

        $("#consentAll").click(function () {
            if ($("#consentAll").prop("checked")) {
                $("input[name=consentTy]").prop("checked", true);
            } else {
                $("input[name=consentTy]").prop("checked", false);
            }
        });

        $("#cmsReqStAll").click(function () { //만약 전체 선택 체크박스가 체크된상태일경우
            if ($("#cmsReqStAll").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
                $("input[name=cmsReqSt]").prop("checked", true); // 전체선택 체크박스가 해제된 경우
            } else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
                $("input[name=cmsReqSt]").prop("checked", false);
            }
        });

        $("#chaStAll").click(function () { //만약 전체 선택 체크박스가 체크된상태일경우
            if ($("#chaStAll").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
                $("input[name=chaSt]").prop("checked", true); // 전체선택 체크박스가 해제된 경우
            } else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
                $("input[name=chaSt]").prop("checked", false);
            }
        });

        $("#chatrtyAll").click(function () { //만약 전체 선택 체크박스가 체크된상태일경우
            if ($("#chatrtyAll").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
                $("input[name=chatrty]").prop("checked", true); // 전체선택 체크박스가 해제된 경우
            } else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
                $("input[name=chatrty]").prop("checked", false);
            }
        });

        $("#row-th").click(function(){
            if($("#row-th").prop("checked")) {
                $("input[name=checkOne]").prop("checked",true);
            } else {
                $("input[name=checkOne]").prop("checked",false);
            }
        });

    }); //r

    function fn_chkcmsChked() {
        if (!$('#chkcmsY').is(':checked') || !$('#chkcmsN').is(':checked') || !$('#chkcmsP').is(':checked')) {
            $('#chkcmsAll').prop('checked', false);
        } else {
            $('#chkcmsAll').prop('checked', true);
        }
    }

    function fn_consentTyChked() {
        if (!$('#ars').is(':checked') || !$('#signed').is(':checked')) {
            $('#consentAll').prop('checked', false);
        } else {
            $('#consentAll').prop('checked', true);
        }
    }

    function fn_stChecked() {
        if (!$('#CST01').is(':checked') || !$('#CST02').is(':checked') || !$('#CST03').is(':checked') || !$('#CST04').is(':checked')) {
            $('#cmsReqStAll').prop('checked', false);
        } else {
            $('#cmsReqStAll').prop('checked', true);
        }
    }

    function fn_chastChked() {
        if (!$('#ST06').is(':checked') || !$('#ST04').is(':checked') || !$('#ST02').is(':checked')) {
            $('#chaStAll').prop('checked', false);
        } else {
            $('#chaStAll').prop('checked', true);
        }
    }

    function fn_chatrTyChked() {
        if (!$('#01').is(':checked') || !$('#03').is(':checked')) {
            $('#chatrtyAll').prop('checked', false);
        } else {
            $('#chatrtyAll').prop('checked', true);
        }
    }

    //출금동의서 업로드 처리
    function cmsFileUpload(chacd) {
        $("#popup-cms-upload").modal({
            backdrop: 'static',
            keyboard: false
        });
        $('#fileFormChacd').val(chacd);
        $('#uploadTy').val("U");
    }

    //출금동의서 변경 처리
    function updateCmsFileUpload(chacd) {
        $("#popup-cms-upload").modal({
            backdrop: 'static',
            keyboard: false
        });
        $('#fileFormChacd').val(chacd);
        $('#uploadTy').val("A");
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
                $('#fchaCd').val($('#chacd').val());
                $('#fchaName').val($('#chaname').val());
                $('#searchOption').val($('#searchOpt').val());
                $('#fkeyword').val($('#keyword').val());

                var consentTy = ""
                if($("#consentAll").prop("checked")){
                    consentTy = "All";
                }else{
                    consentTy = $("input[name='consentTy']:checked").val();
                }
                $('#fconsentTy').val(consentTy);

                var chatrty = ""
                if($("#chatrtyAll").prop("checked")){
                    chatrty = "All";
                }else{
                    chatrty = $("input[name='chatrty']:checked").val();
                }
                $('#fchatrty').val(chatrty);

                var chaList = [];
                var stList = [];
                var cmsList = [];
                var check = $("input[name='chaSt']:checked");
                check.map(function (i) {
                    if ($(this).val() != '' && $(this).val() != null) {
                        chaList.push($(this).val());
                    }
                });
                var checkbox = $("input[name='cmsReqSt']:checked");
                checkbox.map(function (i) {
                    if ($(this).val() != '' && $(this).val() != null) {
                        stList.push($(this).val());
                    }
                });
                var cmsbox = $("input[name='chkcmsYN']:checked");
                cmsbox.map(function (i) {
                    if ($(this).val() != '' && $(this).val() != null) {
                        cmsList.push($(this).val());
                    }
                });
                $('#cmsList').val(cmsList);
                $('#stList').val(stList);
                $('#chaList').val(chaList);
                $('#orderBy').val($("#orderBySel option:selected").val());

                // 다운로드
                document.fForm.action = "/sys/auto/autoTranRegDownload";
                document.fForm.submit();
            }
        });
    }

    function fn_updAgreement(idx, upty) {
        var msg = "";
        var msg1 = "";
        if(upty == "N"){
            msg = "등록되어 있는 동의 상태를 취소하시겠습니까?";
            msg1 = "취소완료 되었습니다.";
        }else{
            msg = "동의 대기 중인 서류를 승인하시겠습니까?";
            msg1 = "승인완료 되었습니다.";
        }
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
                var url = "/sys/auto/updAgreement";
                var param = {
                    chaCd: idx,
                    chkcmsYN: upty
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
                            text: msg1,
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: '확인'
                        }).then(function (result) {
                            fn_search($("#pageNo").val());
                        });
                    }
                });
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
                            fn_search($("#pageNo").val());
                        });
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
            html: "업로드한 파일을 삭제하시겠습니까?",
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
                    chaCd: chaCd,
                    flag: 22
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
                                text: '삭제되었습니다.',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: '확인'
                            }).then(function (result) {
                                if (result.value) {
                                    fn_search($("#pageNo").val());
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

    function fn_smsNoti() {
        var stList = [];
        var idx = 0;
        var checkbox = $("input[name=checkOne]:checked");
        checkbox.each(function(i) {
            stList.push($(this).val());
            idx++;
        });
        if(idx == 0) {
            swal({
                type: 'info',
                text: '문자 발송할 기관을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        $("#popup-send-sms").modal({
            backdrop: 'static',
            keyboard: false
        });
        fn_smsInit(stList);
    }

    function fn_emailNoti() {
        var ecareNo = "300";
        var idx = 0;
        var stList = [];
        var checkbox = $("input[name=checkOne]:checked");
        checkbox.each(function(i) {
            stList.push($(this).val());
            idx++;
        });
        if(idx == 0) {
            swal({
                type: 'info',
                text: 'E-MAIL 발송할 기관을 선택해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }
        $("#popup-send-email").modal({
            backdrop: 'static',
            keyboard: false
        });
        fn_emailInit(stList, ecareNo);
    }

    function fn_listCheck() {
        var stList = [];
        var idx = 0;
        var num = 0;

        var check = $("input[name='checkOne']");
        check.map(function(i) {
            if($(this).is(':checked') == true) {
                idx++;
            }
            num++;
        });

        if(num == idx) {
            $('#row-th').prop('checked', true);
        } else{
            $('#row-th').prop('checked', false);
        }
    }

    function cmsDetailInfo(chacd, favail) {

        $("#popup-cms-detail-info").modal({
            backdrop: 'static',
            keyboard: false
        });
        collector_info_init(chacd, favail);
    }
</script>

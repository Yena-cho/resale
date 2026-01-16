<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<script type="text/javascript">
    var oneDepth = "adm-nav-7";
    var twoDepth = "adm-sub-21";
    var cuPage = 1;
</script>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>기관별 가상계좌 보유현황</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>설정관리</a>
            </li>
            <li class="active">
                <strong>기관별가상계좌보유현황</strong>
            </li>
        </ol>
        <p class="page-description">이용기관별 가상계좌 보유현황을 관리하는 화면입니다.</p>
    </div>
    <div class="col-lg-2 text-left m-t-xl">
        <button class="btn btn-md btn-w-m btn-primary" id="registBtn">등록</button>
    </div>
</div>
<input type="hidden" id="curPage" name="curPage"/>

<form id="fileForm" name="fileForm" method="post">
	<input type="hidden" name="searchStartday" id="searchStartday">
	<input type="hidden" name="searchEndday" id="searchEndday">
    <input type="hidden" name="fchacd" id="fchacd">
    <input type="hidden" name="fchaname" id="fchaname"/>
    <input type="hidden" name="searchGb" id="searchGb"/>
    <input type="hidden" name="searchValue" id="searchValue"/>
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
                        			<label class="form-label block">등록일자</label>
                        			<div id="datepicker" class="input-daterange input-group float-left" >
		                                <input id="startday" name="startday" type="text" class="input-sm form-control" readonly="readonly"/>
		                                <span class="input-group-addon">to </span>
		                                <input id="endday" name="endday" type="text" class="input-sm form-control" readonly="readonly"/>
		                            </div>
                        		</div>
                        		<div class="col-md-6">
                        			<label class="form-label block">가상계좌상태</label>
                        			<div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" name="rdoUseyn" id="rdoUseynAll" value="">
                                                <label for="rdoUseynAll"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" name="rdoUseyn" id="rdoUseynY" value="Y" onclick="fn_reqChecked();">
                                                <label for="rdoUseynY"> 사용 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" name="rdoUseyn" id="rdoUseynW" value="W" onclick="fn_reqChecked();">
                                                <label for="rdoUseynW"> 대기 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" name="rdoUseyn" id="rdoUseynA" value="A" onclick="fn_reqChecked();">
                                                <label for="rdoUseynA"> 할당 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" name="rdoUseyn" id="rdoUseynN" value="N" onclick="fn_reqChecked();">
                                                <label for="rdoUseynN"> 미할당 </label>
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
                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">검색구분</label>
                                    <div class="input-group col-md-12">
                                         <span class="input-group-select">
                                            <select class="form-control" name="SRCHsearchGb" id="SRCHsearchGb">
												<option value="all">전체</option>
												<option value="cusname">고객명</option>
												<option value="vano">가상계좌번호</option>
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
                            <span>전체건수 : <strong class="text-success"><span id="totalCount">0</span></strong> 건</span>
                            <span class="col-sm-offset-4">미할당계좌 : <strong class="text-success"><span id="misassignCount">0</span></strong> 건</span>
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="vSearchOrderBy" id="vSearchOrderBy" onchange="pageChange();">
                                <option value="regdt">등록일순</option>
                                <option value="vano">가상계좌번호순</option>
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
                                    <col width="390">
                                    <col width="180">
                                    <col width="250">
                                    <col width="180">
                                    <col width="150">
                                    <col width="180">
                                    <col width="200">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>등록일자</th>
                                        <th>은행코드</th>
                                        <th>가상계좌번호</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>상태</th>
                                        <th>고객명</th>
                                        <th>할당일자</th>
                                        <th>최근입금일자</th>
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

<!-- 등록 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/vano-regist.jsp" flush="false"/>

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        //등록일자 기능
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

        //등록을 눌렀을 경우
        $("#registBtn").click(function () {
            $('#normal').prop('checked', true);
            $('#uploadfile').val('');
            $('#filename').val('');

            $("#popup-virtual-account-regist").modal({
                backdrop: 'static',
                keyboard: false
            });
        });

        //-------------------------가상계좌 상태 디폴트 세팅-------------------------------
        $("#rdoUseynAll").click(function () { //만약 전체 선택 체크박스가 체크된상태일경우
            if ($("#rdoUseynAll").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
                $("input[name=rdoUseyn]").prop("checked", true); // 전체선택 체크박스가 해제된 경우
            } else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
                $("input[name=rdoUseyn]").prop("checked", false);
            }
        });

        $("input[name=rdoUseyn]").prop("checked", true);
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
               text: '등록일자를 확인해 주세요.',
               confirmButtonColor: '#3085d6',
               confirmButtonText: '확인'
           });
    		return;
    	}
    	
        var checkArr = [];
        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }

        var url = "/sys/vanoMgmt/getChaVanoOwnsList";
        var stList = [];
        var checkbox = $("input[name=rdoUseyn]:checked");
        checkbox.map(function (i) {
            if ($(this).val() != '' && $(this).val() != null) {
                stList.push($(this).val());
            }
        });

        var param = {
            searchStartday: startday,
            searchEndday: endday,
            chacd: $('#chacd').val(),
            chaname: $('#chaname').val(),
            searchGb: $('#SRCHsearchGb option:selected').val(),
            searchValue: $('#SRCHsearchValue').val(), //검색구분 텍스트값
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
        $('#totalCount').text(result.totalCount);
        $('#misassignCount').text(result.misassignCount);
        if (result == null || result.totalCount <= 0) {
            str += '<tr><td colspan="14" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + NVL(v.rn) + '</td>';
                str += '<td>' + v.regdt + '</td>';
                str += '<td>' + NVL(v.fgcd) + '</td>';
                str += '<td>' + NVL(v.vano) + '</td>';
                str += '<td>' + NVL(v.chacd) + '</td>';
                str += '<td >' + NVL(v.chaname) + '</td>';

                if (NVL(v.vastate) == 'Y') {
                    str += '<td class="text-success">사용</td>';
                } else if (NVL(v.vastate) == 'W') {
                    str += '<td class="text-success">대기</td>';
                } else if (NVL(v.vastate) == 'A') {
                    str += '<td class="text-danger">할당</td>';
                } else if (NVL(v.vastate) == 'N') {
                    str += '<td class="text-danger">미할당</td>';
                } else {
                    str += '<td class="text-danger">' + v.vastate + '</td>';
                }

                str += '<td>' + NVL(v.cusname) + '</td>';
                str += '<td>' + NVL(v.assigndt) + '</td>';
                str += '<td>' + NVL(v.lastrcpdate) + '</td>';
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
               text: '등록일자를 확인해 주세요.',
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
                $('#searchGb').val($('#SRCHsearchGb option:selected').val());
                $('#searchValue').val($('#SRCHsearchValue').val());
                var stList = [];
                var checkbox = $("input[name=rdoUseyn]:checked");
                checkbox.map(function (i) {
                    if ($(this).val() != '' && $(this).val() != null) {
                        stList.push($(this).val());
                    }
                });
                $('#stList').val(stList);
                $('#searchOrderBy').val($('#vSearchOrderBy option:selected').val());

                document.fileForm.action = "/sys/vanoMgmt/getChaVanoListExcel";
                document.fileForm.submit();
            }
        });
    }

    function fn_reqChecked() {
        if (!$('#rdoUseynY').is(':checked') || !$('#rdoUseynW').is(':checked') || !$('#rdoUseynA').is(':checked') || !$('#rdoUseynN').is(':checked')) {
            $('#rdoUseynAll').prop('checked', false);
        } else {
            $('#rdoUseynAll').prop('checked', true);
        }
    }
</script>

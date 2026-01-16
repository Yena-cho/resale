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
    var twoDepth = "adm-sub-20";
    var cuPage = 1;
</script>

</div>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>가상계좌보유현황</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>설정관리</a>
            </li>
            <li class="active">
                <strong>가상계좌보유현황</strong>
            </li>
        </ol>
        <p class="page-description">가상계좌 보유 및 이용현황을 관리하는 화면입니다.</p>
    </div>
    <div class="col-lg-2">

    </div>
</div>

<input type="hidden" id="curPage" name="curPage"/>

<form id="downForm" name="downForm" method="post">
    <input type="hidden" name="searchGb" id="searchGb"/>
    <input type="hidden" name="searchValue" id="searchValue"/>
    <input type="hidden" name="chaCd" id="chaCd"/>
    <input type="hidden" name="chaName" id="chaName"/>
    <input type="hidden" name="stList" id="stList"/>
    <input type="hidden" name="fromNotUseCnt" id="fromNotUseCnt"/>
    <input type="hidden" name="toNotUseCnt" id="toNotUseCnt"/>
    <input type="hidden" name="searchOrderBy" id="searchOrderBy"/>
</form>

<div class="wrapper-content">
    <div class="row">
        <div class="col-lg-3">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>발급 가상계좌</h5>
                </div>
                <div class="ibox-content">
                    <h1 class="no-margins">
                        <div class="counter"><span id="totcnt"><fmt:formatNumber pattern="#,###" value="${info.totcnt}"/>0</span>건</div>
                    </h1>
                </div>
            </div>
        </div>

        <div class="col-lg-3">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>사용 가상계좌</h5>
                </div>
                <div class="ibox-content">
                    <h1 class="no-margins">
                        <div class="counter"><span id="usedcnt"><fmt:formatNumber pattern="#,###" value="${info.usedcnt}"/>0</span>건</div>
                    </h1>
                </div>
            </div>
        </div>

        <div class="col-lg-3">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>삭제 가상계좌</h5>
                </div>
                <div class="ibox-content">
                    <h1 class="no-margins">
                        <div class="counter"><span id="delcnt"><fmt:formatNumber pattern="#,###" value="${info.delcnt}"/>0</span>건</div>
                    </h1>
                </div>
            </div>
        </div>

        <div class="col-lg-3">
            <div class="ibox">
                <div class="ibox-title">
                    <h5>잔여 가상계좌</h5>
                </div>
                <div class="ibox-content">
                    <h1 class="no-margins">
                        <div class="counter"><span id="notusedcnt"><fmt:formatNumber pattern="#,###" value="${info.notusedcnt}"/>0</span>건</div>
                    </h1>
                </div>
            </div>
        </div>
    </div>

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
                                    <label class="form-label block">기관상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" name="chaStItem" id="chaStItemAll" value="">
                                                <label for="chaStItemAll"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" name="chaStItem" id="ST06" value="ST06" onclick="fn_reqChecked();">
                                                <label for="ST06"> 정상 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" name="chaStItem" id="ST08" value="ST08" onclick="fn_reqChecked();">
                                                <label for="ST08"> 정지 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" name="chaStItem" id="ST02" value="ST02" onclick="fn_reqChecked();">
                                                <label for="ST02"> 해지 </label>
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
                                            <select class="form-control" name="SRCHsearchGb" id="SRCHsearchGb">
                                            <option value="chrname">담당자명</option>
                                            <option value="chrtelno">담당자핸드폰번호</option>
                                        </select>
                                        </span>
                                        <input type="text" class="form-control" name="SRCHsearchValue" id="SRCHsearchValue" maxlength="100">
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">잔여계좌 보유수</label>
                                    <div class="form-group form-group-sm form-inline">
                                        <input type="text" class="form-control" id="vFromNotUseCnt" style="width: 30%;">
                                        <span> ~ </span>
                                        <input type="text" class="form-control" id="vToNotUseCnt" style="width: 30%;">
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
                                <option value="chaCd">기관코드순정렬</option>
                                <option value="chaSt">기관상태순정렬</option>
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
                            <table class="table table-stripped table-align-center"><!-- 2018.05.11 클래스 수정 -->
                                <colgroup>
                                    <col width="50">
                                    <col width="120">
                                    <col width="350">
                                    <col width="210">
                                    <col width="160">
                                    <col width="120">
                                    <col width="150">
                                    <col width="150">
                                    <col width="150">
                                    <col width="150">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>기관코드</th>
                                        <th>기관명</th>
                                        <th>담당자명</th>
                                        <th>담당자핸드폰번호</th>
                                        <th>기관상태</th>
                                        <th>총 보유</th>
                                        <th>사용</th>
                                        <th>잔여</th>
                                        <th>삭제</th>
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

<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false"/>

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>

<script>
    $(document).ready(function () {
        $(".btn-lookup-collecter").click(function () {
            $("#lookup-collecter-popup").modal({
                backdrop: 'static',
                keyboard: false
            });
            $("#popChacd").val($("#chacd").val());
            $("#popChaname").val($("#chaname").val());
            fn_ListCollector();
        });

        $("#chaStItemAll").click(function () { //만약 전체 선택 체크박스가 체크된상태일경우
            if ($("#chaStItemAll").prop("checked")) { //해당화면에 전체 checkbox들을 체크해준다
                $("input[name=chaStItem]").prop("checked", true); // 전체선택 체크박스가 해제된 경우
            } else { //해당화면에 모든 checkbox들의 체크를해제시킨다.
                $("input[name=chaStItem]").prop("checked", false);
            }
        });

        $("#ST06").prop("checked", true);

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

        var url = "/sys/vanoMgmt/getvanoOwnsList";
        var stList = [];
        var checkbox = $("input[name=chaStItem]:checked");
        checkbox.map(function (i) {
            if ($(this).val() != '' && $(this).val() != null) {
                stList.push($(this).val());
            }
        });

        var param = {
            searchGb: $('#SRCHsearchGb option:selected').val(),  	// 검색구분
            searchValue: $('#SRCHsearchValue').val(), 				//검색구분 텍스트값
            chacd: $('#chacd').val(),
            chaname: $('#chaname').val(),
            stList: stList,
            fromNotUseCnt: $('#vFromNotUseCnt').val(),
            toNotUseCnt: $('#vToNotUseCnt').val(),
            searchOrderBy: $('#vSearchOrderBy option:selected').val(),
            curPage: cuPage,
            pageScale: $('#pageScale option:selected').val()
        };

// 	$('.spinner-area').css('display', 'block');

        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
// 			$('.spinner-area').css('display', 'none');
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
            ,
            error: function (result) {
                swal({
                    type: 'error',
                    text: JSON.stringify(result),
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
            }
        });
    }

    //데이터 새로고침
    function fnGrid(result, obj) {
        var str = '';
        $('#totcnt').text(numberToCommas(result.info.totcnt));
        $('#usedcnt').text(numberToCommas(result.info.usedcnt));
        $('#notusedcnt').text(numberToCommas(result.info.notusedcnt));
        $('#delcnt').text(numberToCommas(result.info.delcnt));
        $('#totalvanocnt').text(result.count);
        if (result == null || result.count <= 0) {
            str += '<tr><td colspan="10" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + NVL(v.rn) + '</td>';
                str += '<td>' + NVL(v.chacd) + '</td>';
                str += '<td>' + NVL(v.chaName) + '</td>';
                str += '<td>' + NVL(v.chrname) + '</td>';
                str += '<td>' + NVL(v.chrTelNo) + '</td>';
                //은행 승인완료 == 다모아 이용대기
                if (NVL(v.chast) == 'ST06') {
                    str += '<td class="text-success">' + getChastNameDamoa(NVL(v.chast)) + '</td>';
                } else {
                    str += '<td class="text-warning">' + getChastNameDamoa(NVL(v.chast)) + '</td>';
                }
                str += '<td class="text-success">' + v.totcnt + '</td>';
                str += '<td class="text-success">' + v.usedcnt + '</td>';
                str += '<td class="text-success">' + v.notusedcnt + '</td>';
                str += '<td class="text-success">' + v.delcnt + '</td>';
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
            retVal = "정상";
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
            || ( value != null && typeof value == "object" && !Object.keys(value).length )
        ) {
            return "";
        }
        else {
            return value;
        }
    }

    // 파일저장
    function fn_fileSave() {

        if ($('#totalvanocnt').text() == 0) {
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
                $('#searchGb').val($('#SRCHsearchGb option:selected').val());
                $('#searchValue').val($('#SRCHsearchValue').val());
                $('#chaCd').val($('#chacd').val());
                $('#chaName').val($('#chaname').val());
                var stList = [];
                var checkbox = $("input[name=chaStItem]:checked");
                checkbox.map(function (i) {
                    if ($(this).val() != '' && $(this).val() != null) {
                        stList.push($(this).val());
                    }
                });
                $('#stList').val(stList);
                $('#fromNotUseCnt').val($('#vFromNotUseCnt').val());
                $('#toNotUseCnt').val($('#vToNotUseCnt').val());
                $('#searchOrderBy').val($('#vSearchOrderBy option:selected').val());

                document.downForm.action = "/sys/vanoMgmt/getVano01ListExcel";
                document.downForm.submit();
            }
        });
    }

    function fn_reqChecked() {
        if (!$('#ST06').is(':checked') || !$('#ST08').is(':checked') || !$('#ST02').is(':checked')) {
            $('#chaStItemAll').prop('checked', false);
        } else {
            $('#chaStItemAll').prop('checked', true);
        }
    }
</script>

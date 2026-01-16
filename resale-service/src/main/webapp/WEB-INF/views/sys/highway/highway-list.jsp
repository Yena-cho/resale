<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false" />
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>
<script src="/assets/js/paginate.js?version=${project.version}"></script>

<script>
    var oneDepth = "adm-nav-10";
    var twoDepth = "adm-sub-41";``
</script>

<style>
    #resultBody td {
        vertical-align: middle;
    }
</style>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>수납 누락 정보확인 및 재전송</h2>
        <ol class="breadcrumb">
            <li>
                <a>고속도로 관리</a>
            </li>
            <li class="active">
                <strong>수납누락 정보확인 및 재전송</strong>
            </li>
        </ol>
        <p class="page-description">고속도로에 송신되지않은 수납정보를 확인하고 재전송처리하는 화면입니다. </p>
    </div>
    <div class="col-lg-2">

    </div>
</div>

<form id="MainfileForm" name="MainfileForm" method="post">
    <input type="hidden" name="pageNo" id="pageNo" value="" />
    <input type="hidden" name="searchOrderBy"	id="searchOrderBy" />
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
                                <div class="col-md-12">
                                    <label class="form-label block">조작일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control" id="startDate" name="startDate" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="endDate" name="endDate" readonly="readonly"/>
                                            </div>

                                            <div class="daterange-setMonth">
                                                <button type="button" class="btn btn-sm btn-primary btn-outline active" name="btnSetMonth" id="btnSetMonth0" onclick="setMonthTerm(0);">전체</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth"  id="btnSetMonth1"  onclick="setMonthTerm(1);">1개월</button>
                                                <button type="button" class="btn btn-sm btn-primary btn-outline" name="btnSetMonth"  id="btnSetMonth6"  onclick="setMonthTerm(6);">6개월</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chaCd" id="chaCd"  />
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">기관명</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chaName" id="chaName"  />
                                    </div>
                                </div>
                            </div>


                            <hr>

                            <div class="text-center">
                                <button class="btn btn-primary" type="button" onclick="fnSearch('1');" >조회</button>
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
                            전체 건수 : <strong class="text-success" id="totCnt"></strong> 건
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="nSearchOrderBy" id="orderBy" onchange="pageChange();">
                                <option value="clientId">거래일시 내림차순</option>
                                <option value="clientName">기관코드 오름차순</option>
                            </select>
                            <select class="form-control"  name="pageScale" id="pageScale" onchange="pageChange();">
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
                            <table class="table table-stripped table-align-center" style="table-layout: fixed">
                                <colgroup>
                                    <col width="100" />
                                    <col width="200" />
                                    <col width="200" />
                                    <col width="100" />
                                    <col width="200" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="100" />
                                    <col width="100" />
                                </colgroup>

                                <thead>
                                <tr>
                                    <th>기관코드</th>
                                    <th>기관명</th>
                                    <th>고객명</th>
                                    <th>가상계좌번호</th>
                                    <th>수납번호</th>
                                    <th>납부자명</th>
                                    <th>수납일</th>
                                    <th>납부금액</th>
                                    <th>수납상태</th>
                                    <th>재발송버튼</th>
                                </tr>
                                </thead>

                                <tbody id="resultBody">
                                </tbody>
                            </table>
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
<!-- ##################################################################### -->


<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false" />

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>


<script>

    $(document).ready(function(){
        $('.input-daterange').datepicker({
            format: 'yyyy.mm.dd',
            maxDate: "+0d",
            keyboardNavigation : false,
            forceParse : false,
            autoclose : true
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

        $("#row-th").click(function(){
            if($("#row-th").prop("checked")) {
                $("input[name=checkOne]").prop("checked",true);
            } else {
                $("input[name=checkOne]").prop("checked",false);
            }
        });

        setMonthTerm(1);
        fnSearch(1);
    });

    function list(page){

        page = page.toString();


        var param = {
            curPage: page,
            pageScale :"10",
        };

        $.ajax({
            type: "post",
            async: true,
            url: "/sys/highway/rcpLoseList",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (response) {
                fnGrid(response, 'resultBody');

            },
            error: (log) => {
                swal({
                    type:'error',
                    text:'관리자에게 문의하세요.',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인' });
            }
        });


    }

    function fnSearch(page) {
        var pageNo = page || 1;
        $('#pageNo').val(pageNo);
        var startday = $('#startDate').val().replace(/\./gi, "");
        var endday   = $("#endDate").val().replace(/\./gi, "");

        if((startday > endday)) {
            swal({
                type: 'info',
                text: '누락일자를 확인해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }


        var param = {
            curPage: page,
            chaCd: $('#chaCd').val(),
            chaName: $('#chaName').val(),
            startDate: startday,
            endDate: endday,
            orderBy: $('#orderBy option:selected').val(),
            pageNo: pageNo,
            pageScale: $('#pageScale option:selected').val()
        };



        $.ajax({
            type : "post",
            async : true,
            url : '/sys/highway/rcpLoseList',
            contentType : "application/json; charset=utf-8",
            data : JSON.stringify(param),
            success : function(result) {

                fnGrid(result, 'resultBody');		// 현재 데이터로 셋팅
                // fnPaginate(result, pageNo, 'PageArea');
            },
            error:function(result){
                swal({
                    type: 'info',
                    text: '로딩 실패 관리자에게 문의하세요',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
            }
        });
    }

    function fnGrid(result, obj) {
        var $tbody = $('#' + obj).empty();

        var str = '';

        $('#totCnt').text(result.totalItemCount);

        if (result == null || result.totalItemCount <= 0) {
            $('<tr><td colspan="15" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>').appendTo($tbody);
            return;
        }

        $.each(result.resultList, function (i, v) {
            str += '<tr>';
            str += '<td>' + v.CHACD + '</td>'; //기관코드
            str += '<td>' + v.CHANAME + '</td>'; //기관명
            str += '<td>' + v.CUSNAME + '</td>'; //고객명
            str += '<td>' + v.VANO + '</td>'; //가상계좌번호
            str += '<td>' + v.RCPMASCD + '</td>'; //누락된수납번호
            str += '<td>' + v.RCPUSRNAME + '</td>'; //납부자명
            str += '<td>' + v.PAYDAY + '</td>'; //수납일자
            str += '<td>' + v.RCPAMT + '</td>'; //납부금액

            if(v.RCPMASST == "PA03"){
                str += '<td>완납</td>';
            }else if(v.RCPMASST == "PA02"){
                str += '<td>미납</td>';
            }else{
                str += '<td>확인필요</td>';
            }
            if(v.CHECKYN == "N") {
                str += '<td>'
                str += '<button type="button" class="btn btn-xs btn-success" onclick=retryRcpTranSend(\'' + v.RCPMASCD + '\')>재전송</button>';
                str += '</td>';
            }else{
                str += '<td>' + '전송완료' + '</td>';

            }
            str += '</tr>';
        });

        $('#' + obj).html(str);

    }

    function  retryRcpTranSend (val){
        var rcpMasCd = val ;


    }

    function setMonthTerm (val) {
        $('#searchDate').val(val);
        var toDate = new Date();

        if(val != 0) {
            $('#startDate').attr('disabled', false);
            $('#endDate').attr('disabled', false);

            $('#endDate').val(getDateFormatDot(toDate));
            $('#startDate').val(monthAgo($('#endDate').val(), val));

            $("button[name=btnSetMonth]").removeClass('active');
            $('#btnSetMonth' + val).addClass('active');
        } else {
            $('#startDate').attr('disabled', true);
            $('#endDate').attr('disabled', true);

            $('#startDate').val('');
            $('#endDate').val('');

            $("button[name=btnSetMonth]").removeClass('active');
            $('#btnSetMonthAll').addClass('active');
        }
    }



    function pageChange() {
        cuPage = 1;
        fnSearch(cuPage);
    }

    function NVL(value) {
        if( value == "" || value == null || value == undefined
            ||( value != null && typeof value == "object" && !Object.keys(value).length )) {
            return "";
        } else {
            return value;
        }
    }

    function fnPaginate(result, pageNo, obj) {
        var $paginate = $('#' + obj);
        $paginate.empty();

        if(!result) {
            return;
        }

        var pageSize = $('#pageScale').val();
        var totalItemCount = result.totalItemCount;

        if(totalItemCount <= pageSize) {
            return;
        }

        var paginate = window.paginate(pageNo*1, totalItemCount, pageSize);

        var val = result.modalNo;
        var str = '';
        str += '<button type="button" class="btn btn-white" onclick="list(' + paginate.previousPage + ', ' + val + ')"><i class="fa fa-chevron-left"></i></button>';
        for (var i = paginate.startPage; i <= paginate.endPage; i++) {
            if (i == result.pageNo) {
                str += '<button class="btn btn-white  active">' + i + '</button>'; // 현재 페이지인 경우 하이퍼링크 제거
            } else {
                str += '<button class="btn btn-white" onclick="list(' + i + ', ' + val + ')">' + i + '</button>';
            }
        }
        str += '<button type="button" class="btn btn-white" onclick="list(' + paginate.nextPage + ', ' + val + ')"><i class="fa fa-chevron-right"></i> </button>';
        $('#' + obj).html(str);
    }
</script>

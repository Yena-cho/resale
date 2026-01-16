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
    var twoDepth = "adm-sub-41";
</script>

<style>
    #resultBody td {
        vertical-align: middle;
    }
</style>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>고속도로 가용가상계좌수</h2>
        <ol class="breadcrumb">
            <li>
                <a>고속도로 관리</a>
            </li>
            <li class="active">
                <strong>고속도로 가용가상계좌수</strong>
            </li>
        </ol>
        <p class="page-description">분기별 가상계좌 사용건 수 보고를 위한 페이지</p>
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
                                    <label class="form-label block">가상계좌 부여 기간 설정</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control" id="assignStDay" name="assignStDay" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="assignDtDay" name="assignDtDay" readonly="readonly"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <label class="form-label block">미사용 계좌 기간 설정</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker2">
                                                <input type="text" class="input-sm form-control" id="masStDay" name="masStDay" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="masDtDay" name="masDtDay" readonly="readonly"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">대상기관코드</label>
                                    <div class="form-group form-group-sm">
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" name="chaCd" id="chaCd"  />
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
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center" style="table-layout: fixed">
                                <colgroup>
                                    <col width="100" />
                                    <col width="200" />
                                    <col width="200" />

                                </colgroup>

                                <thead>
                                <tr>
                                    <th>기관코드</th>
                                    <th>해당기간 미사용 가상계좌 수</th>
                                    <th>해당기간 가상계좌 채번 수</th>
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

        // setMonthTerm(1);
        // fnSearch(1);
    });


    function fnSearch(page) {
        var pageNo = page || 1;
        $('#pageNo').val(pageNo);
        var assignStDay = $('#assignStDay').val().replace(/\./gi, "");
        var assignDtDay   = $("#assignDtDay").val().replace(/\./gi, "");

        var masStDay = $('#masStDay').val().replace(/\./gi, "");
        var masDtDay   = $("#masDtDay").val().replace(/\./gi, "");

        if((assignStDay > assignDtDay)) {
            swal({
                type: 'info',
                text: ' 가상계좌 부여 기간을 확인해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }

        if((masStDay > masDtDay)) {
            swal({
                type: 'info',
                text: ' 미사용 계좌 기간을 확인해 주세요.',
                confirmButtonColor: '#3085d6',
                confirmButtonText: '확인'
            });
            return;
        }


        var param = {
            assignStDay : assignStDay ,
            assignDtDay : assignDtDay ,
            masStDay : masStDay ,
            masDtDay : masDtDay ,
            chaCd: $('#chaCd').val()
        };



        $.ajax({
            type : "post",
            async : true,
            url : '/sys/highway/highwayUsedVanoInfo',
            contentType : "application/json; charset=utf-8",
            data : JSON.stringify(param),
            success : function(result) {
                fnGrid(result, 'resultBody');		// 현재 데이터로 셋팅
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

        $.each(result.list, function (i, v) {
            str += '<tr>';
            str += '<td>' + v.chaCd + '</td>'; //기관코드
            str += '<td>' + v.highwayUnUsedCnt + '</td>'; //기관명
            str += '<td>' + v.highwayAssignTotalCnt + '</td>'; //고객명

            str += '</tr>';
        });

        $('#' + obj).html(str);

    }

    function  retryRcpTranSend (val){
        var rcpMasCd = val ;


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

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false"/>
<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<script type="text/javascript">
    var oneDepth = "adm-nav-7";
    var twoDepth = "adm-sub-32";
    var cuPage = 1;
</script>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>가상계좌거래 입금전문내역</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>설정관리</a>
            </li>
            <li class="active">
                <strong>가상계좌거래입금전문내역</strong>
            </li>
        </ol>
        <p class="page-description">가상계좌거래입금,수취내역을 조회하는 화면입니다.</p>
    </div>
    <div class="col-lg-2">

    </div>
</div>

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
                                    <label class="form-label block">거래일자</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="input-daterange input-group float-left" id="datepicker">
                                                <input type="text" class="input-sm form-control" id="fDate" name="fDate" readonly="readonly"/>
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" id="tDate" name="tDate" readonly="readonly"/>
                                            </div>
                                            <div class="daterange-setMonth">
                                                <button id="pMonth1" type="button" class="btn btn-sm btn-primary btn-outline preset-month active" onclick="setInputDateField(this, 0, 'd')">오늘</button>
                                                <button id="pMonth2" type="button" class="btn btn-sm btn-primary btn-outline preset-month" onclick="setInputDateField(this, -1,'d')">어제</button>
                                                <button id="pMonth3" type="button" class="btn btn-sm btn-primary btn-outline preset-month" onclick="setInputDateField(this, -7, 'd')">1주일</button>
                                                <button id="pMonth4" type="button" class="btn btn-sm btn-primary btn-outline preset-month" onclick="setInputDateField(this, 1, 'm')">1개월</button>
                                                <button id="pMonth5" type="button" class="btn btn-sm btn-primary btn-outline preset-month" onclick="setInputDateField(this, 3, 'm')">3개월</button>
                                            </div>
                                        </div>
                                    </div>
                            </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="col-md-6" style="padding-left: 0px">
                                        <label class="form-label block">기관코드</label>
                                        <div class="form-group form-group-lg">
                                            <div class="input-group">
                                                <input type="text" class="form-control ng-untouched ng-pristine ng-valid"
                                                       name="chacd" id="chacd" maxlength="50">
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-md-6">
                                        <label class="form-label block">가상계좌</label>
                                        <div class="form-group form-group-lg">
                                            <div class="input-group">
                                                <input type="text" class="form-control ng-untouched ng-pristine ng-valid"
                                                       name="chacd" id="vano" maxlength="50">
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label block">전문타입</label>
                                    <div class="form-group form-group-sm">
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="rcpTy" value="">
                                            <label for="rcpTy"> 전체 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="mt4110" value="4110" name="rcpTy" onclick="fn_rcpTychecked();">
                                            <label for="mt4110"> 수취조회 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="mt1100" value="1100" name="rcpTy" onclick="fn_rcpTychecked();">
                                            <label for="mt1100"> 무통장입금 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="mt3110" value="3110" name="rcpTy" onclick="fn_rcpTychecked();">
                                            <label for="mt3110"> 계좌이체 </label>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">성공여부</label>
                                    <div class="form-group form-group-sm">
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="succSt" value="">
                                            <label for="succSt"> 전체 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="succ0000" value="0000" name="succSt" onclick="fn_succStchecked();">
                                            <label for="succ0000"> 정상 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="succVxxx" value="1111" name="succSt" onclick="fn_succStchecked();">
                                            <label for="succVxxx"> 불능 </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label block">송수신구분</label>
                                    <div class="form-group form-group-sm">
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="sendReceive" value="">
                                            <label for="sendReceive"> 전체 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="sendS" value="0200" name="sendReceive" onclick="fn_sendReceivechecked();">
                                            <label for="sendS"> 수신 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="receiveR" value="0210" name="sendReceive" onclick="fn_sendReceivechecked();">
                                            <label for="receiveR"> 송신 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="cancelS" value="0400" name="sendReceive" onclick="fn_sendReceivechecked();">
                                            <label for="cancelS"> 취소수신 </label>
                                        </div>
                                        <div class="checkbox checkbox-primary checkbox-inline">
                                            <input type="checkbox" id="cancelR" value="0410" name="sendReceive" onclick="fn_sendReceivechecked();">
                                            <label for="cancelR"> 취소송신 </label>
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
                            전체 건수 : <strong class="text-success" id="totCnt">0</strong> 건
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="searchOrderBy" id="searchOrderBy" onchange="pageChange();">
                                <option value="dtDESC">거래일시 내림차순</option>
                                <option value="dtASC">거래일시 오름차순</option>
                            </select>
                            <select class="form-control" name="pageScale" id="pageScale" onchange="pageChange();">
                                <option value="50">50개씩 조회</option>
                                <option value="100">100개씩 조회</option>
                                <option value="200">200개씩 조회</option>
                            </select>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center">
                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>거래일자</th>
                                        <th>거래시각</th>
                                        <th>송수신구분</th>
                                        <th>전문타입</th>
                                        <th>은행코드</th>
                                        <th>기관코드</th>
                                        <th>입금계좌</th>
                                        <th>수취인명</th>
                                        <th>출금기관</th>
                                        <th>출금계좌주명</th>
                                        <th>거래금액</th>
                                        <th>응답코드(메세지)</th>
                                    </tr>
                                </thead>

                                <tbody id="resultBody">
                                    <tr>
                                        <td colspan=12 style="text-align: center;">[조회된 내역이 없습니다.]</td>
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

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        setInputDateField($("#pMonth1"), 0, 'd');
        $("#succSt").prop("checked", true);
        $("input[name=succSt]").prop("checked", true);
        $("#sendReceive").prop("checked", true);
        $("input[name=sendReceive]").prop("checked", true);
        $("#rcpTy").prop("checked", true);
        $("input[name=rcpTy]").prop("checked", true);

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

        $("#succSt").click(function () {
            if ($("#succSt").prop("checked")) {
                $("input[name=succSt]").prop("checked", true);
            } else {
                $("input[name=succSt]").prop("checked", false);
            }
        });

        $("#sendReceive").click(function () {
            if ($("#sendReceive").prop("checked")) {
                $("input[name=sendReceive]").prop("checked", true);
            } else {
                $("input[name=sendReceive]").prop("checked", false);
            }
        });

        $("#rcpTy").click(function () {
            if ($("#rcpTy").prop("checked")) {
                $("input[name=rcpTy]").prop("checked", true);
            } else {
                $("input[name=rcpTy]").prop("checked", false);
            }
        });

        fnSearch();
    });

    function fn_succStchecked() {
        if (!$('#succ0000').is(':checked') || !$('#succVxxx').is(':checked')) {
            $('#succSt').prop('checked', false);
        } else {
            $('#succSt').prop('checked', true);
        }
    }

    function fn_sendReceivechecked() {
        if (!$('#sendS').is(':checked') || !$('#receiveR').is(':checked') || !$('#cancelS').is(':checked') || !$('#cancelR').is(':checked')) {
            $('#sendReceive').prop('checked', false);
        } else {
            $('#sendReceive').prop('checked', true);
        }
    }

    function fn_rcpTychecked() {
        if (!$('#mt4110').is(':checked') || !$('#mt1100').is(':checked') || !$('#mt3110').is(':checked')) {
            $('#rcpTy').prop('checked', false);
        } else {
            $('#rcpTy').prop('checked', true);
        }
    }

    // 페이징 버튼
    function list(page, val) {
        if (val == '55') {
            fn_ListCollector(page); // 기관검색 - modal
        } else {
            fnSearch(page);
        }
    }

    /**
     * datepicker 날짜 세팅
     */
    function setInputDateField(obj, num, field){

        var date = new Date();
        toDay = $.datepicker.formatDate("yy.mm.dd", date );

        $('#tDate').val(toDay);

        if (field == "m" && num == 0) {
            date.setDate(1);
            $('#fDate').val( $.datepicker.formatDate("yy.mm.dd", date ) );
        } else if (field == "m" && num > 0) {
            var fd = new Date();
            fd.setMonth(fd.getMonth() - num);
            $('#fDate').val(getDateFormatDot(fd));
            $('#tDate').val(toDay);
        } else {
            $('#fDate').val( addDate(toDay, field, num, ".") );
        }

        $('.preset-month').removeClass('active');
        $(obj).addClass('active');
    }

    // 검색
    function fnSearch(page) {
        if (page == null || page == 'undefined') {
            cuPage = '1';
        } else {
            cuPage = page;
        }

        var startday = replaceDot($('#fDate').val());
        var endday = replaceDot($('#tDate').val());

        if (startday != "" && endday != "") {
            var vdm = "ok";
            if (startday == "" || startday == null || startday == undefined) vdm = "거래일자 시작일을 입력하세요.";
            if (endday == "" || endday == null || endday == undefined) vdm = "거래일자 종료일을 입력하세요.";
            if (startday > endday) vdm = "거래일자 시작일은 종료일보다 클 수 없습니다.";
            if (vdm != 'ok') {
                swal({
                    type: 'info',
                    text: vdm,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }
        }

        var succSt = $("input[name='succSt']:checked").val();

        if($('#succSt').is(':checked')){
            succSt = $('#succSt').val();
        }

        // if($('#sendReceive').is(':checked')){
        //     sndrcvSt = $('#sendReceive').val();
        // }

        var sndrcvList = [];
        var sndrcvSt = $("input[name='sendReceive']:checked");
        sndrcvSt.map(function (i) {
            sndrcvList.push($(this).val());
        });

        var tyList = [];
        var checkbox = $("input[name='rcpTy']:checked");
        checkbox.map(function (i) {
            tyList.push($(this).val());
        });

            var url = "/sys/vanoMgmt/getVanoTranCheckList";

            var param = {
                chacd: $('#chacd').val(),
                vano: $('#vano').val(),
                startday: startday,
                endday: endday,
                succSt : succSt,
                sndrcvList : sndrcvList,
                tyList : tyList,
                searchOrderBy: $('#searchOrderBy option:selected').val(),
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

    // 데이터 새로고침
    function fnGrid(result, obj) {
        var str = '';
        $('#totCnt').text();
        if (result == null || result.count <= 0) {
            str += '<tr><td colspan="12" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (i, v) {
                str += '<tr>';
                str += '<td>' + NVL(v.rn) + '</td>';
                str += '<td>' + changeDateFormat(NVL(v.transDt)) + '</td>';
                str += '<td>' + changeTimeFormat(NVL(v.transTime)) + '</td>';

                if (v.msgTy == '0210') {
                    str += '<td>응답</td>';
                } else if(v.msgTy == '0200') {
                    str += '<td>요청</td>';
                } else if(v.msgTy == '0410') {
                    str += '<td>취소응답</td>';
                } else if(v.msgTy == '0400') {
                    str += '<td>취소요청</td>';
                }

                if (v.msgTy1 == '4110') {
                    str += '<td>수취조회</td>';
                }else if (v.msgTy1 == '3110'){
                    str += '<td>계좌이체</td>';
                }else{
                    str += '<td>무통장입금</td>';
                }
                str += '<td>' + NVL(v.fgcd) + '</td>';
                str += '<td>' + NVL(v.chacd) + '</td>';
                str += '<td>' + NVL(v.vano) + '</td>';
                str += '<td>' + NVL(v.depositNm) + '</td>';
                if(NVL(v.bnkCd) == ""){
                    str += '<td></td>';
                }else{
                    str += '<td>' + NVL(v.bnkNm) + '('+ NVL(v.bnkCd) +')</td>';
                }
                str += '<td>' + NVL(v.withDrawalNm) + '</td>';
                str += '<td class="text-success" style="text-align:right;">' + comma(v.money) + '</td>';
                if (v.msgTy == '0210') {
                    if (NVL(v.resCd) != '0000') {
                        str += '<td>' + NVL(v.resCd) + '(' + NVL(v.resMsg) + ')</td>';
                    } else {
                        str += '<td>' + NVL(v.resCd) + '(정상)</td>';
                    }
                }else{
                    str += '<td></td>';
                }
                str += '</tr>';
            });
        }
        $('#totCnt').text(comma(result.count));
        $('#' + obj).html(str);
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
</script>

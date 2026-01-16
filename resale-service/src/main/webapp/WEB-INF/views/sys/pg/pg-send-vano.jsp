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
  var twoDepth = "adm-sub-40";
</script>

<style>
  #resultBody td {
    vertical-align: middle;
  }
</style>

</div>

<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-lg-10">
    <h2>PG 가상계좌 전송</h2>
    <ol class="breadcrumb">
      <li>
        <a>고속도로 관리</a>
      </li>
      <li class="active">
        <strong>고속도로 가상계좌 전송및 리스트</strong>
      </li>
    </ol>
    <p class="page-description">채번된 가상계좌를 고속도로에 전송하는 화면입니다.  </p>
  </div>
  <div class="col-lg-2">

  </div>
</div>

<form id="MainfileForm" name="MainfileForm" method="post">
  <input type="hidden" name="pageNo" id="pageNo" value="" />
  <input type="hidden" name="searchOrderBy"	id="searchOrderBy" />
</form>

<input type="hidden" id="curPage" name="curPage"/>
<input type="hidden" name="vanoTot" id="vanoTot" value="" />

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
              전체 건수 : <strong class="text-success" id="totCnt"></strong> 건<br>
              사용가능 가상계좌 건수 : <strong class="text-success" id="totVanoCnt"></strong> 건

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
                  <col width="200" />
                  <col width="300" />
                  <col width="200" />
                </colgroup>

                <thead>
                <tr>
                  <th>기관코드</th>
                  <th>가맹점아이디</th>
                  <th>가상계좌 할당일</th>
                  <th>가상계좌 채번수</th>
                  <th>메모</th>
                  <th>가상계좌 기관 요청일</th>
                  <th>가상계좌 기관 전송일</th>
                  <th>전송상태</th>
                  <th>발송버튼</th>
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
      url: "/sys/vanoMgmt/pgVanoSendList",
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
        text: '조작일자를 확인해 주세요.',
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
      url: "/sys/vanoMgmt/pgVanoSendList",
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
    $('#totVanoCnt').text(result.totVanoCount);

    $('#totVanoCnt').val(result.totVanoCount);


    if (result == null || result.totalItemCount <= 0) {
      $('<tr><td colspan="15" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>').appendTo($tbody);
      return;
    }

    $.each(result.resultList, function (i, v) {
      str += '<tr>';
      str += '<td>' + v.chaCd + '</td>'; //기관코드
      str += '<td>' + v.mchtId + '</td>'; //가맹점아이디
      str += '<td>' + v.assignDt + '</td>'; //가상계좌 할당일
      str += '<td>' + v.vanoCount + '</td>'; //가상계좌채번수
      str += '<td>' + v.remark + '</td>'; //메모
      str += '<td>' + v.recvDt + '</td>'; //가상계좌 기관 요청일
      str += '<td>' + v.sendDt + '</td>'; //가상계좌 기관 전송일
      if(v.appendYn == "R"){
        str += '<td>전송대기</td>';
      }else if(v.appendYn == "Y"){
        str += '<td>전송성공</td>';
      }else if(v.appendYn == "N"){
        str += '<td>전송실패</td>';
      }

      if(v.appendYn == "R"){
        str += '<td>'
        str += '<button type="button" class="btn btn-xs btn-success" onclick=sendVano(\'' + v.pgvareqId + '\',\'' + v.vanoCount + '\')>전송</button>';
        str += '</td>';
      }else if(v.appendYn == "N") {
        str += '<td>'
        str += '<button type="button" class="btn btn-xs btn-success" onclick=sendVano(\'' + v.pgvareqId + '\',\'' + v.vanoCount + '\')>재전송</button>';
        str += '</td>';
      }
      str += '</tr>';
    });

    $('#' + obj).html(str);

  }


  function sendVano(pgvareqid,vanoCount){

   var totVanoCnt =  $('#totVanoCnt').val();


    if(Number(vanoCount) > Number(totVanoCnt)){
      swal({
        type: 'info',
        text: '요청건수가 잔여가상계좌 수를 넘을수 없습니다.',
        confirmButtonColor: '#3085d6',
        confirmButtonText: '확인'
      });
      return;

    }



    var param = {
      pgvareqId : pgvareqid //가상계좌 전송 고유번호
    };


    $.ajax({
      type: "post",
      async: true,
      url: "/sys/vanoMgmt/pgVanoSendAjax",
      contentType: "application/json; charset=utf-8",
      data: JSON.stringify(param),
      success: function (response) {
        if(response.retCode =='0000') {
          swal({
            type: 'success',
            text: '가상계좌가 PG 전송 요청 되었습니다.',
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
          }).then(function () {
            fnSearch(1);
          });
        }else if(response.retCode =='9999'){

          swal({
            type: 'success',
            text: '가상계좌 PG송신중 에러발생.',
            confirmButtonColor: '#3085d6',
            confirmButtonText: '확인'
          })

          return;

        }

      },
      error: (log) => {
        swal({
          type:'error',
          text:'관리자에게 문의하세요.',
          confirmButtonColor: '#3085d6',
          confirmButtonText: '확인' }).then(function () {
          fnSearch(1);
        });
      }
    });


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

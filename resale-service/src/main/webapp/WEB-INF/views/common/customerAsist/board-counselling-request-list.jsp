<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false" />
<link href="/html/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/html/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<link href="/html/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

        </div>
            <div class="row wrapper border-bottom white-bg page-heading">
                <div class="col-lg-10">
                    <h2>고객상담 대기목록</h2>
                    <ol class="breadcrumb">
                        <li>
                            <a href="/sys/index">대시보드</a>
                        </li>
                        <li>
                            <a>게시판관리</a>
                        </li>
                        <li class="active">
                            <strong>고객상담 대기목록</strong>
                        </li>
                    </ol>
                    <p class="page-description">전화상담 예약 고객 목록을 확인하고 등록/조회하는 화면입니다.</p>
                </div>
                <div class="col-lg-2">

                </div>
            </div>

            <div class="wrapper wrapper-content animated fadeInRight article">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="ibox">
                            <div class="ibox-content">
                                <form method="get" class="form-horizontal">
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">신청일자</label>
                                        <div class="col-sm-4">
                                            <div class="input-daterange input-group" id="datepicker">
                                                <input type="text" class="input-sm form-control" name="start" value="" placeholder="yyyy/mm/dd" />
                                                <span class="input-group-addon">to</span>
                                                <input type="text" class="input-sm form-control" name="end" value="" placeholder="yyyy/mm/dd" />
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <button class="btn btn-sm btn-primary btn-outline">1달</button>
                                            <button class="btn btn-sm btn-primary btn-outline">2달</button>
                                            <button class="btn btn-sm btn-primary btn-outline">3달</button>
                                        </div>
                                    </div>
                                    <div class="hr-line-dashed"></div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">기관코드</label>
                                        <div class="col-sm-4">
                                            <div class="input-group">
                                                <input type="text" class="form-control">
                                                <span class="input-group-btn">
                                                    <button class="btn btn-primary">기관검색</button>
                                                </span>
                                            </div>
                                        </div>
                                        <label class="col-sm-2 control-label">신청구분</label>
                                        <div class="col-sm-4">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="inlineCheckbox1-1" value="option1">
                                                <label for="inlineCheckbox1-1"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="inlineCheckbox1-2" value="option1">
                                                <label for="inlineCheckbox1-2"> 이용기관 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="inlineCheckbox1-3" value="option1">
                                                <label for="inlineCheckbox1-3"> 납부자/신규 </label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="hr-line-dashed"></div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">검색구분</label>
                                        <div class="col-sm-4">
                                            <div class="input-group">
                                                <span class="input-group-select">
                                                    <select class="form-control">
                                                        <option>신청자</option>
                                                        <option>신청자</option>
                                                        <option>신청자</option>
                                                    </select>
                                                </span>
                                                <input type="text" class="form-control">
                                            </div>
                                        </div>
                                        <label class="col-sm-2 control-label">상태</label>
                                        <div class="col-sm-4">
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="inlineCheckbox2-1" value="option1">
                                                <label for="inlineCheckbox2-1"> 전체 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="inlineCheckbox2-2" value="option1">
                                                <label for="inlineCheckbox2-2"> 대기 </label>
                                            </div>
                                            <div class="checkbox checkbox-primary checkbox-inline">
                                                <input type="checkbox" id="inlineCheckbox2-3" value="option1">
                                                <label for="inlineCheckbox2-3"> 완료 </label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="hr-line-dashed"></div>
                                    <div class="form-group text-center">
                                        <button class="btn btn-lg btn-w-m btn-primary">조회</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="wrapper wrapper-content animated fadeInRight">
                <div class="row">
                    <div class="col-lg-12">
                        <h4>상담대기 목록</h4>
                    </div>
                </div>
                <div class="row m-b-xs">
                    <div class="col-lg-6 text-right">
                        <span class="m-l-sm">
                            전체 건수 : <strong class="text-success">90</strong> 건
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-12">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center">
                                <thead>
                                <tr>
                                    <th>NO</th>
                                    <th>신청일시</th>
                                    <th>신청구분</th>
                                    <th>기관코드</th>
                                    <th>신청자</th>
                                    <th>전화번호</th>
                                    <th>예약사유</th>
                                    <th>상담진행상황</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>1</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>신동엽</td>
                                    <td>010-5554-5628</td>
                                    <td>사이트이용</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary btn-open-reg-counselling-request">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>2</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>이용기관</td>
                                    <td>20001342</td>
                                    <td>서장훈</td>
                                    <td>010-1454-5578</td>
                                    <td>고지/수납</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>3</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>이용기관</td>
                                    <td>20001345</td>
                                    <td>이수근</td>
                                    <td>010-2334-5678</td>
                                    <td>결제</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>4</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>김종민</td>
                                    <td>010-2334-2378</td>
                                    <td>사이트이용</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>5</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>이수근</td>
                                    <td>010-2334-5678</td>
                                    <td>결제</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>신동엽</td>
                                    <td>010-5554-5628</td>
                                    <td>사이트이용</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>2</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>이용기관</td>
                                    <td>20001342</td>
                                    <td>서장훈</td>
                                    <td>010-1454-5578</td>
                                    <td>고지/수납</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>3</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>이용기관</td>
                                    <td>20001345</td>
                                    <td>이수근</td>
                                    <td>010-2334-5678</td>
                                    <td>결제</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>4</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>김종민</td>
                                    <td>010-2334-2378</td>
                                    <td>사이트이용</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>5</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>이수근</td>
                                    <td>010-2334-5678</td>
                                    <td>결제</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>신동엽</td>
                                    <td>010-5554-5628</td>
                                    <td>사이트이용</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>2</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>이용기관</td>
                                    <td>20001342</td>
                                    <td>서장훈</td>
                                    <td>010-1454-5578</td>
                                    <td>고지/수납</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>3</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>이용기관</td>
                                    <td>20001345</td>
                                    <td>이수근</td>
                                    <td>010-2334-5678</td>
                                    <td>결제</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>4</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>김종민</td>
                                    <td>010-2334-2378</td>
                                    <td>사이트이용</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>5</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>이수근</td>
                                    <td>010-2334-5678</td>
                                    <td>결제</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>신동엽</td>
                                    <td>010-5554-5628</td>
                                    <td>사이트이용</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>2</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>이용기관</td>
                                    <td>20001342</td>
                                    <td>서장훈</td>
                                    <td>010-1454-5578</td>
                                    <td>고지/수납</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>3</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>이용기관</td>
                                    <td>20001345</td>
                                    <td>이수근</td>
                                    <td>010-2334-5678</td>
                                    <td>결제</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>4</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>김종민</td>
                                    <td>010-2334-2378</td>
                                    <td>사이트이용</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>5</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>이수근</td>
                                    <td>010-2334-5678</td>
                                    <td>결제</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>신동엽</td>
                                    <td>010-5554-5628</td>
                                    <td>사이트이용</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>2</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>이용기관</td>
                                    <td>20001342</td>
                                    <td>서장훈</td>
                                    <td>010-1454-5578</td>
                                    <td>고지/수납</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>3</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>이용기관</td>
                                    <td>20001345</td>
                                    <td>이수근</td>
                                    <td>010-2334-5678</td>
                                    <td>결제</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>4</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>김종민</td>
                                    <td>010-2334-2378</td>
                                    <td>사이트이용</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>5</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>이수근</td>
                                    <td>010-2334-5678</td>
                                    <td>결제</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>신동엽</td>
                                    <td>010-5554-5628</td>
                                    <td>사이트이용</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>2</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>이용기관</td>
                                    <td>20001342</td>
                                    <td>서장훈</td>
                                    <td>010-1454-5578</td>
                                    <td>고지/수납</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>3</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>이용기관</td>
                                    <td>20001345</td>
                                    <td>이수근</td>
                                    <td>010-2334-5678</td>
                                    <td>결제</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>4</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>김종민</td>
                                    <td>010-2334-2378</td>
                                    <td>사이트이용</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>5</td>
                                    <td>2018.05.18 12:00</td>
                                    <td>납부자/신규</td>
                                    <td></td>
                                    <td>이수근</td>
                                    <td>010-2334-5678</td>
                                    <td>결제</td>
                                    <td>
                                        <button class="btn btn-xs btn-primary">등록</button>
                                    </td>
                                </tr>
                                </tbody>
                                <tfoot>
                                <tr>
                                    <td colspan="9">
                                        <ul class="pagination pull-right"></ul>
                                    </td>
                                </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </div>


<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false" />

<script src="/html/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<script src="/html/assets/js/plugins/footable/footable.all.min.js"></script>

<%-- 전화상담 진행상황 등록 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/reg-counselling-request.jsp" flush="false"/>

<script>

$(document).ready(function() {

    //  2018.05.11 아래 삭제
    //  $('.footable').footable();

});

$('.input-daterange').datepicker({
    keyboardNavigation: false,
    forceParse: false,
    autoclose: true
});

$('.btn-open-reg-counselling-request').click(function(){
    $("#modal-reg-counselling-request").modal({
        backdrop : 'static',
        keyboard : false
    });
});

$('.input-group.date').datepicker({
    todayBtn: "linked",
    keyboardNavigation: false,ㅜ
    forceParse: false,
    calendarWeeks: true,
    autoclose: true
});


</script>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:authentication property="principal.username" var="userId" />
<s:authentication property="principal.name" var="userName" />

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false" />

<style>
	.hidden-01 {
		display: none;
	}
</style>

<script type="text/javascript">
	var firstDepth = "nav-link-35";
	var secondDepth = "sub-05";
	var cuPage = 1;
	var toDay = getFormatCurrentDate();
	var toDay2 = getFormatCurrentDate();
	var rcpRatio1 = 0;
	var rcpRatio2 = 0;
	var rcpRatio3 = 0;
	var rcpRatio4 = 0;

	$(function() {
		$('#fDate').val(monthAgo(toDay,1));
		$('#tDate').val(toDay);
		getYearsBox('yearsBox');
		getMonthBox2('monthBox',1);
		getYearsBox('yearsBox2');
		getMonthBox('monthBox2');

	    fnSearch('', 'month');
	});

	function prevDate(num){
		var toDay = $.datepicker.formatDate("yy.mm.dd", new Date() );
		var vdm = dateValidity($('#fDate').val(), toDay);
	 	$('#tDate').val(toDay);
		$('#fDate').val(monthAgo(toDay,num));
		$('.btn-preset-month').removeClass('active');
		$('#pMonth'+num+'').addClass('active');
	}

	function prevDate2(num){
		toDay2 = $.datepicker.formatDate("yy.mm.dd", new Date() );
		$('#fDate2').val(monthAgo(toDay,num));
		$('.btn-preset-month').removeClass('active');
		$('#pMonth2'+num+'').addClass('active');
	}

	// 검색
	function fnSearch(page, gubn) {
		var startDate;
		var endDate;

		if(page == null || page == 'undefined'){
			cuPage = "";
			cuPage = 1;
		}else{
			cuPage = page;
		}

		if(gubn == 'day'){
			startDate = replaceDot($('#fDate').val());
			endDate = replaceDot($('#tDate').val());
			var vdm = dateValidity(startDate, endDate);
			if (vdm != 'ok'){
				swal({
			           type: 'info',
			           text: vdm,
			           confirmButtonColor: '#3085d6',
			           confirmButtonText: '확인'
			       });
				return false;
			}
		}else{
			startDate = $('#yearsBox option:selected').val()+""+$('#monthBox option:selected').val();
			endDate = $('#yearsBox2 option:selected').val()+""+$('#monthBox2 option:selected').val();
		}

		if(startDate > endDate){
			swal({
			       type: 'info',
			       text: '시작일이 종료일보다 클 수 없습니다.',
			       confirmButtonColor: '#3085d6',
			       confirmButtonText: '확인'
				});
			return false;
		}

        if($('.hidden-01').is(":visible")){
            $('.btn-open-hidden-cell').html("-");
            $('.hidden-01').toggle();
        } else {
            $('.btn-open-hidden-cell').html("+");
        }

		var url = "/org/myPage/rcpStateAjax";
		param = {
			startDate : startDate,
			endDate : endDate,
			gubn : gubn
		};
		$.ajax({
			type : "post",
			async : true,
			url : url,
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(param),
			success : function(result) {
				if (result.retCode == "0000") {
					if(gubn == 'day'){
						fnGrid(result, 'resultBody');		// 현재 데이터로 셋팅
					}else{
						fnGrid(result, 'resultBody2');		// 현재 데이터로 셋팅
						fnChart(result);
					}
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
	
	function emptyChart() {
        var html2='';
        html2+='<div class="container mt-3" style="text-align:center;">';
        html2+='<img src="/assets/imgs/common/mobile-icon-exclamation-mark.png" class="icon mb-3" alt="느낌표 마크" style="width: 100px;">';
        html2+='<br/>';
        html2+='조회된 내역이 없습니다.';
        html2+='</div>';

	    return html2; 
	}
	
	// 파이차트
	function fnPieChart(result) {
	    var $canvas = $('#pCanvas');
	    if(result.count < 1) {
            $canvas.html(emptyChart());
            return;
        }

        var rcpRatio1 = result.pstDcs;
        var rcpRatio2 = result.pstVas;
        var rcpRatio3 = result.pstDcd;
        var rcpRatio4 = result.pstOcd;

        if(rcpRatio1 == 0 && rcpRatio2 == 0 && rcpRatio3 == 0 && rcpRatio4 == 0) {
            $canvas.html(emptyChart());
            return;
        }


        var pCanvas = '<canvas id="payMethod" class="chartjs-render-monitor" style="display: block;"></canvas>';
        $canvas.html(pCanvas);

        var ctx3 = document.getElementById('payMethod');

        new Chart(ctx3, {
            type: 'pie',
            data: {
                labels: ["가상계좌", "온라인카드", "현금", "오프라인카드"],
                datasets: [{
                    label: ["수납매체활용율"],
                    data: [rcpRatio2, rcpRatio4, rcpRatio1, rcpRatio3],
                    backgroundColor: [
                        '#ff7f00',
                        '#a65628',
                        '#984ea3',
                        '#ffd92f'
                    ]
                }]
            }
        });
    }
    
    // 바차트
	function fnBarChart(result) {
	    var $canvas = $('#canvas');
	    
		if(result.count < 1) {
            $canvas.html(emptyChart());
			return;
		}
		
		if(result.mList.length < 1) {
            $canvas.html(emptyChart());
            return;
		}

        var arrLabel = result.mList.map(function(v) {
            return v.masMonth;
		});
        var arrRcp  = result.mList.map(function(v) {
            return v.rcp;
		});
        
        if(result.totCnt == 0 && result.totAmt == 0) {
            $('#canvas').html(emptyChart());
            return;
		}
        
		var barChartData = {
			labels: arrLabel,
			datasets: [{
				label: '입금',
				backgroundColor: '#8da0cb',
				data: arrRcp,
				borderWidth: 1
			}]
		};
        
		var canvas ='<canvas id="monthly" class="chartjs-render-monitor mb-5" style="display: block;"></canvas>';
        $canvas.html(canvas);

		var ctx2 = document.getElementById('monthly');

		new Chart(ctx2, {
			type: 'bar',
			data: barChartData,
			stacked: true
		});
	}

	// 차트
	function fnChart(result) {
	    fnPieChart(result);
        fnBarChart(result);
        $('#graph').css('display','');
	}

	/**
	 * 2018.8.3. 정미래 수정
	 *
	 * 수납상세 팝업
     * @param type VAS 가상계좌, DCS 현금, OCD 온라인카드, DCD 오프라인카드, CASH 현금영수증 신청
	 **/
	function rcpDetail(idx, type){
		var payDay = $('input[name=payDay]:eq('+idx+')').val();
		var cnt;
		switch(type){
			case "VAS" : cnt = $('input[name=vasValue]:eq('+idx+')').val();
				break;
			case "DCS" : cnt = $('input[name=dcsValue]:eq('+idx+')').val();
				break;
			case "OCD" : cnt = $('input[name=ocdValue]:eq('+idx+')').val();
				break;
			case "DCD" : cnt = $('input[name=dcdValue]:eq('+idx+')').val();
				break;	
			case "CASH" : cnt = $('input[name=cashValue]:eq('+idx+')').val();
				break;
		}

		// 현금영수증 신청건이 0 일 때, 얼럿창 안뜸. cnt값을 가져오지 않는 듯 함. undefined 뜸.
		if(cnt <= 0){
			swal({
			       type: 'info',
			       text: '조회된 결과가 없습니다.',
			       confirmButtonColor: '#3085d6',
			       confirmButtonText: '확인'
				});
			return;
		}
		
		var url = "/org/myPage/getRcpDetail";
		var param = {
			payDay : $('input[name=payDay]:eq('+idx+')').val(),
			gubn : type
		};
		$.ajax({
			type : "post",
			async : true,
			url : url,
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(param),
			success : callbackRcpDetail(payDay, type)
		});
	}

	function callbackRcpDetail(payDay, type) {
	    return function(result) {
            if(result.retCode !== '0000') {
                swal({
                    type: 'error',
                    text: result.retMsg,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
                return;
            }

			var dateString = moment(payDay, 'YYYYMMDD').format('M월 D일');
            if (type == "VAS") {
                $('#rcpTitle').text(dateString + ' 가상계좌 수납내역');
            } else if (type == "DCS") {
                $('#rcpTitle').text(dateString + ' 현금 수납내역');
            } else if (type == "OCD") {
                $('#rcpTitle').text(dateString + ' 온라인카드 수납내역');
            } else if (type == "DCD") {
                $('#rcpTitle').text(dateString + ' 오프라인카드 수납내역');
            } else if (type == "CASH") {
                $('#rcpTitle').text(dateString + ' 현금영수증 신청내역');
            }

            fnGridPopup(result, 'pResultBody', type);

            $("#book-keeping-unit-list").modal({
                backdrop:'static',
                keyboard : false
            });
		};
	}
	
    /**
     * 2018. 8. 3 정미래 수정
     *
     * 수납상세 팝업
     * @param type VAS 가상계좌, DCS 현금, OCD 온라인카드, DCD 오프라인카드, CASH 현금영수증 신청
     **/
	function fnGridPopup(result, obj, type) {
        if(type === 'VAS') {
            fnVasGridPopup(result, obj)
        } else {
            fnDefaultGridPopup(result, obj)
		}
	}
	
	function fnDefaultGridPopup(result, obj) {
	    var head = '';
        head+='<tr>';
        head+='<th>입금일자</th>';
        head+='<th>청구월</th>';
        head+='<th>고객명</th>';
        head+='<th>입금자명</th>';
        head+='<th>청구금액</th>';
        head+='<th>입금액(원)</th>';
        head+='</tr>';
        $('#pResultHead').html(head);

        // tbody
        var str = '';
        if(result.count <= 0){
            str+='<tr><td colspan="6" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        }else{
            $.each(result.list, function(i, v){
                str+='<tr>';
                str+='<td>'+moment(v.payDay, 'YYYYMMDD').format('YYYY.MM.DD')+'</td>';
                str+='<td>'+(v.noticeMonth ? moment(v.noticeMonth, 'YYYYMM').format('YYYY.MM') : '')+'</td>';
                str+='<td>'+(v.customerName || '')+'</td>';
                str+='<td>'+(v.rcpName || '')+'</td>';
                str+='<td class="text-right">'+numberToCommas(v.payAmount || 0)+'</td>';
                str+='<td class="text-right">'+numberToCommas(v.rcpAmount || 0)+'</td>';
                str+='</tr>';
            });
        }
        $('#' + obj).html(str);
	}
    
	function fnVasGridPopup(result, obj) {
        var head = '';
        head+='<tr>';
        head+='<th>입금일자</th>';
        head+='<th>청구월</th>';
        head+='<th>고객명</th>';
        head+='<th>입금자명</th>';
        head+='<th>입금은행</th>';
        head+='<th>청구금액</th>';
        head+='<th>입금액(원)</th>';
        head+='</tr>';
        $('#pResultHead').html(head);

        // tbody
        var str = '';
        if(result.count <= 0){
            str+='<tr><td colspan="6" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        }else{
            $.each(result.list, function(i, v){
                str+='<tr>';
                str+='<td>'+moment(v.payDay, 'YYYYMMDD').format('YYYY.MM.DD')+'</td>';
                str+='<td>'+(v.noticeMonth ? moment(v.noticeMonth, 'YYYYMM').format('YYYY.MM') : '')+'</td>';
                str+='<td>'+(v.customerName || '')+'</td>';
                str+='<td>'+(v.rcpName || '')+'</td>';
                str+='<td>'+(v.bankName || '')+'</td>';
                str+='<td class="text-right">'+numberToCommas(v.payAmount || 0)+'</td>';
                str+='<td class="text-right">'+numberToCommas(v.rcpAmount || 0)+'</td>';
                str+='</tr>';
            });
        }
        $('#' + obj).html(str);
	}

	// 데이터 새로고침
	function fnGrid(result, obj) {
		var str = '';
		if(obj == 'resultBody'){
			$('#totCnt').text(result.count);
			if(result.count <= 0){
				str+='<tr><td colspan="13" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
				$('#' + obj).html(str);

				str = '';
				str+='<tr>';
				str+='<td class="summary-bg">합계</td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
                str+='<td class="text-right summary-bg"></td>';
                str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='</tr>';
				$('#resultFoot').html(str);
			}else{
				$('#totCnt').text(result.count);
				$.each(result.list, function(i, v){
					str+='<tr>';
					str+='<td>'+changeDateFormat(v.payDay)+'</td>';

					str+='<td class="text-right"><a href="javascript:;" onclick="rcpDetail('+i+',\'VAS\');" class="book-keeping-unit-list-popup">'+numberToCommas(v.vasCnt)+'</a></td>';
					str+='<td class="text-right">'+numberToCommas(v.vasAmt)+'</td>';
                    str+='<td class="text-right"><a href="javascript:;" onclick="rcpDetail('+i+',\'OCD\');" class="book-keeping-unit-list-popup">'+numberToCommas(v.ocdCnt)+'</a></td>';
                    str+='<td class="text-right">'+numberToCommas(v.ocdAmt)+'</td>';
					str+='<td class="text-right"><a href="javascript:;" onclick="rcpDetail('+i+',\'DCS\');" class="book-keeping-unit-list-popup">'+numberToCommas(v.dcsCnt)+'</a></td>';
					str+='<td class="text-right">'+numberToCommas(v.dcsAmt)+'</td>';
					str+='<td class="text-right"><a href="javascript:;" onclick="rcpDetail('+i+',\'DCD\');" class="book-keeping-unit-list-popup">'+numberToCommas(v.dcdCnt)+'</a></td>';
                    str+='<td class="text-right">'+numberToCommas(v.dcdAmt)+'</td>';
					str+='<td class="text-right"><a href="javascript:;" onclick="rcpDetail('+i+',\'CASH\');" class="book-keeping-unit-list-popup">'+numberToCommas(v.cashCnt)+'</a></td>';
					str+='<td class="text-right">'+numberToCommas(v.cashAmt)+'</td>';
					str+='<td class="text-right">'+numberToCommas(v.totCnt)+'</td>';
					str+='<td class="text-right">'+numberToCommas(v.totAmt)+'</td>';
					str+='<input type="hidden" name="vasValue" value="'+numberToCommas(v.vasCnt)+'"/>';
					str+='<input type="hidden" name="dcsValue" value="'+numberToCommas(v.dcsCnt)+'"/>';
					str+='<input type="hidden" name="ocdValue" value="'+numberToCommas(v.ocdCnt)+'"/>';
					str+='<input type="hidden" name="dcdValue" value="'+numberToCommas(v.dcdCnt)+'"/>';
					str+='<input type="hidden" name="cashValue" value="'+numberToCommas(v.cashCnt)+'"/>';
					str+='<input type="hidden" name="payDay" value="'+v.payDay+'"/>';
					str+='</tr>';
				});
				$('#' + obj).html(str);
				str = '';
				str+='<tr>';
				str+='<td class="summary-bg">합계</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.vasCnt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.vasAmt)+'</td>';
                str+='<td class="text-right summary-bg">'+numberToCommas(result.ocdCnt)+'</td>';
                str+='<td class="text-right summary-bg">'+numberToCommas(result.ocdAmt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.dcsCnt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.dcsAmt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.dcdCnt)+'</td>';
                str+='<td class="text-right summary-bg">'+numberToCommas(result.dcdAmt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.cashCnt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.cashAmt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.totCnt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.totAmt)+'</td>';
				str+='</tr>';
				$('#resultFoot').html(str);
			}
		} else {
			$('#totCnt2').text(result.count);

			if(result.count <= 0){
				str+='<tr><td colspan="13" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
				$('#' + obj).html(str);

				str = '';
				str+='<tr>';
				str+='<td class="summary-bg">합계</td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='<td class="text-right summary-bg"></td>';
				str+='</tr>';
				$('#resultFoot2').html(str);
			}else{
				str = '';
				$.each(result.list, function(i, v){
					str+='<tr>';
					str+='<td>'+v.masMonth+'</td>';
					str+='<td class="text-right">'+numberToCommas(v.vasCnt)+'</td>';
					str+='<td class="text-right">'+numberToCommas(v.vasAmt)+'</td>';
					str+='<td class="text-right">'+numberToCommas(v.ocdCnt)+'</td>';
					str+='<td class="text-right">'+numberToCommas(v.ocdAmt)+'</td>';
					str+='<td class="text-right">'+numberToCommas(v.dcsCnt)+'</td>';
					str+='<td class="text-right">'+numberToCommas(v.dcsAmt)+'</td>';
					str+='<td class="text-right">'+numberToCommas(v.dcdCnt)+'</td>';
					str+='<td class="text-right">'+numberToCommas(v.dcdAmt)+'</td>';
					str+='<td class="text-right">'+numberToCommas(v.cashCnt)+'</td>';
					str+='<td class="text-right">'+numberToCommas(v.cashAmt)+'</td>';
                    str+='<td class="text-right">'+numberToCommas(v.totCnt)+'</td>';
                    str+='<td class="text-right">'+numberToCommas(v.totAmt)+'</td>';
					str+='</tr>';
				});
				$('#' + obj).html(str);

				str = '';
				str+='<tr>';
				str+='<td class="summary-bg">합계</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.vasCnt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.vasAmt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.ocdCnt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.ocdAmt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.dcsCnt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.dcsAmt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.dcdCnt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.dcdAmt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.cashCnt)+'</td>';
				str+='<td class="text-right summary-bg">'+numberToCommas(result.cashAmt)+'</td>';
                str+='<td class="text-right summary-bg">'+numberToCommas(result.totCnt)+'</td>';
                str+='<td class="text-right summary-bg">'+numberToCommas(result.totAmt)+'</td>';
				str+='</tr>';
				$('#resultFoot2').html(str);
			}
		}
	}

	// 파일저장
	function fn_fileSave(gubn) {
		var totCnt = 0;
		if(gubn == 'day'){
			totCnt = $('#totCnt').text();
		}else{
			totCnt = $('#totCnt2').text();
		}
		if(totCnt <= 0){
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
		}).then(function(result) {
			if (result.value) {
				if(gubn == 'day'){
					$('#fStartDate').val(replaceDot($('#fDate').val()));
					$('#fEndDate').val(replaceDot($('#tDate').val()));
				}else{
					$('#fStartDate').val($('#yearsBox option:selected').val()+""+$('#monthBox option:selected').val());
					$('#fEndDate').val($('#yearsBox2 option:selected').val()+""+$('#monthBox2 option:selected').val());
				}
				$('#gubn').val(gubn);
				// 다운로드
			 	$('#fileForm').submit();
			}
	    });
	}
</script>
<form id="fileForm" name="fileForm" method="post" action="/org/myPage/rcpExcelDown">
	<input type="hidden" id="fStartDate" name="fStartDate" />
	<input type="hidden" id="fEndDate" name="fEndDate" />
	<input type="hidden" id="gubn" name="gubn" />
</form>

<div id="contents">
	<div id="damoa-breadcrumb">
		<nav class="nav container">
			<a class="nav-link" href="#">이용료조회</a>
			<a class="nav-link" href="#">관리자설정</a>
			<a class="nav-link active" href="#">수납현황분석</a>
			<a class="nav-link" href="#">고지상세설정</a>
		</nav>
	</div>
	<div class="container">
		<div id="page-title">
			<div id="breadcrumb-in-title-area" class="row align-items-center">
				<div class="col-12 text-right">
					<img src="/assets/imgs/common/icon-home.png" class="mr-2">
					<span> > </span>
					<span class="depth-1">수납관리</span>
					<span> > </span>
					<span class="depth-2 active">직접수납관리</span>
				</div>
			</div>
			<div class="row">
				<div class="col-6">
					<h2>수납현황분석</h2>
				</div>
				<div class="col-6 text-right">
					<%--<button type="button" class="btn btn-img">--%>
						<%--<img src="/assets/imgs/common/btn-typo-control.png">--%>
					<%--</button>--%>
					<%--<button type="button" class="btn btn-img">--%>
						<%--<img src="/assets/imgs/common/btn-print.png">--%>
					<%--</button>--%>
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<div id="page-description">
			<div class="row">
				<div class="col-12">
					<p>수납현황을 분석하는 화면입니다.</p>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="tab-selecter type-3">
					<ul class="nav nav-tabs" role="tablist">
						<li class="nav-item"><a id="showDay" class="nav-link active" data-toggle="tab" href="#daily-status">일별수납현황</a></li>
						<li class="nav-item"><a id="showMonth" class="nav-link" data-toggle="tab" href="#monthly-status">월별수납현황</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<div class="container tab-content">
		<div id="daily-status" class="tab-pane fade show active">
			<div class="search-box">
				<form>
					<div class="row">
						<label class="col col-md-2 col-sm-2 col-3 col-form-label">
							수납일자
						</label>
						<div class="col col-md-4 col-sm-10 col-9 form-inline">
							<div class="date-input">
								<label class="sr-only" for="inlineFormInputGroupUsername">From</label>
								<div class="input-group">
									<input type="text" id="fDate" class="form-control date-picker-input" placeholder="YYYY.MM.DD" aria-label="From"
										aria-describedby="basic-addon2" maxlength="8">
								</div>
							</div>
							<span class="range-mark"> ~ </span>
							<div class="date-input">
								<label class="sr-only" for="inlineFormInputGroupUsername">To</label>
								<div class="input-group">
									<input type="text" id="tDate" class="form-control date-picker-input" placeholder="YYYY.MM.DD" aria-label="To"
										aria-describedby="basic-addon2" maxlength="8">
								</div>
							</div>
						</div>
						<div class="col-md-6 col-sm-10 offset-md-0 offset-sm-2 offset-3">
							<button type="button" id="pMonth1" class="btn btn-sm btn-preset-month active" onclick="prevDate(1)">1개월</button>
							<button type="button" id="pMonth2" class="btn btn-sm btn-preset-month" onclick="prevDate(2)">2개월</button>
							<button type="button" id="pMonth3" class="btn btn-sm btn-preset-month" onclick="prevDate(3)">3개월</button>
						</div>
					</div>

					<div class="row form-inline mt-3">
						<div class="col-12 text-center">
							<button type="button" class="btn btn-primary btn-wide" onclick="fnSearch('', 'day');">조회</button>
						</div>
					</div>
				</form>
			</div>

			<div class="table-option row mb-2">
				<div class="col-md-6 col-sm-12 form-inline">
					<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="totCnt"><fmt:formatNumber pattern="#,###" value="${map.count}"/></em>건]</span>
				</div>
				<div class="col-md-6 col-sm-12 text-right mt-1 hidden-on-mobile">
					<button class="btn btn-sm btn-d-gray" type="button" onclick="fn_fileSave('day')">파일저장</button>
				</div>
			</div>

			<div id="search-result-daily">
				<div class="row">
					<div class="col">
						<div class="table-responsive  mb-3">
							<table class="table table-sm table-hover table-primary">
								<colgroup>
									<col width="114">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
								</colgroup>

								<thead>
									<tr>
										<th rowspan="2">수납일자</th>
										<th colspan="2">가상계좌</th>
										<th colspan="2">온라인카드</th>
										<th colspan="2">현금</th>
										<th colspan="2">오프라인카드</th>
										<th colspan="2" class="border-r-e6">현금영수증 신청</th>
										<th colspan="2" class="border-l-n">합계</th>
									</tr>
									<tr>
										<th class="border-l">건수(건)</th>
										<th>금액(원)</th>
										<th>건수(건)</th>
										<th>금액(원)</th>
										<th>건수(건)</th>
										<th>금액(원)</th>
										<th>건수(건)</th>
										<th>금액(원)</th>
										<th>건수(건)</th>
										<th>금액(원)</th>
										<th>건수(건)</th>
										<th>금액(원)</th>
									</tr>
								</thead>

								<tbody id="resultBody">
									<c:choose>
										<c:when test="${map.count > 0}">
											<c:forEach var="row" items="${map.list}" varStatus="status">
												<tr>
													<td><fmt:parseDate pattern="yyyyMMdd" var="payDay" value="${row.payDay}"/><fmt:formatDate pattern="yyyy.MM.dd" value="${payDay}"/></td>
													<td class="text-right"><a href="javascript:;" onclick="rcpDetail('${status.count-1}','VAS');" class="book-keeping-unit-list-popup"><fmt:formatNumber pattern="#,###" value="${row.vasCnt}"/></a></td>
													<td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.vasAmt}"/></td>
													<td class="text-right"><a href="javascript:;" onclick="rcpDetail('${status.count-1}','OCD');" class="book-keeping-unit-list-popup" class="book-keeping-unit-list-popup"><fmt:formatNumber pattern="#,###" value="${row.ocdCnt}"/></a></td>
													<td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.ocdAmt}"/></td>
													<td class="text-right"><a href="javascript:;" onclick="rcpDetail('${status.count-1}','DCS');" class="book-keeping-unit-list-popup" class="book-keeping-unit-list-popup"><fmt:formatNumber pattern="#,###" value="${row.dcsCnt}"/></a></td>
													<td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.dcsAmt}"/></td>
													<td class="text-right"><a href="javascript:;" onclick="rcpDetail('${status.count-1}','DCD');" class="book-keeping-unit-list-popup" class="book-keeping-unit-list-popup"><fmt:formatNumber pattern="#,###" value="${row.dcdCnt}"/></a></td>
													<td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.dcdAmt}"/></td>
													<td class="text-right"><a href="javascript:;" onclick="rcpDetail('${status.count-1}','CASH');" class="book-keeping-unit-list-popup" class="book-keeping-unit-list-popup"><fmt:formatNumber pattern="#,###" value="${row.cashCnt}"/></a></td>
													<td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.cashAmt}"/></td>
													<td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.totCnt}"/></td>
													<td class="text-right"><fmt:formatNumber pattern="#,###" value="${row.totAmt}"/></td>
													<input type="hidden" name="vasValue" value="${row.vasCnt}"/>
													<input type="hidden" name="dcsValue" value="${row.dcsCnt}"/>
													<input type="hidden" name="ocdValue" value="${row.ocdCnt}"/>
													<input type="hidden" name="dcdValue" value="${row.dcdCnt}"/>
													<input type="hidden" name="cashValue" value="${row.dcdCnt}"/>
													<input type="hidden" name="payDay" value="${row.payDay}"/>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr>
												<td colspan="17">[조회된 내역이 없습니다.]</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>

								<tfoot id="resultFoot">
									<tr>
										<td class="summary-bg">합계</td>
										<td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${map.vasCnt}"/></td>
										<td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${map.vasAmt}"/></td>
										<td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${map.ocdCnt}"/></td>
										<td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${map.ocdAmt}"/></td>
										<td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${map.dcsCnt}"/></td>
										<td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${map.dcsAmt}"/></td>
										<td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${map.dcdCnt}"/></td>
										<td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${map.dcdAmt}"/></td>
										<td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${map.cashCnt}"/></td>
										<td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${map.cashAmt}"/></td>
										<td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${map.totCnt}"/></td>
										<td class="text-right summary-bg"><fmt:formatNumber pattern="#,###" value="${map.totAmt}"/></td>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div id="monthly-status" class="tab-pane fade show">
			<div class="search-box">
				<form>
				<div class="row">
					<label class="col col-md-2 col-sm-2 col-3 col-form-label">
						수납월
					</label>

					<div class="col-md-10 col-sm-10 col-9 form-inline">
						<select class="form-control" id="yearsBox">
							<option>선택</option>
						</select>
						<span class="ml-1 mr-1">년</span>
						<select class="form-control" id="monthBox">
							<option>선택</option>
						</select>
						<span class="ml-1">월</span>
						<span class="range-mark"> ~ </span>
						<select class="form-control" id="yearsBox2">
							<option>선택</option>
						</select>
						<span class="ml-1 mr-1">년</span>
						<select class="form-control" id="monthBox2">
							<option>선택</option>
						</select>
						<span class="ml-1 mr-auto">월</span>
					</div>
				</div>

				<div class="row form-inline mt-3">
					<div class="col-12 text-center">
						<button type="button" class="btn btn-primary btn-wide" onclick="fnSearch('', 'month');">조회</button>
					</div>
				</div>
				</form>
			</div>

			<div style="display: none;" id="graph">
				<div class="row no-gutters mt-3 mb-2">
					<div class="col-6">
						<h5 class="font-weight-600">현황 그래프</h5>
					</div>
				</div>

				<div class="row no-gutters mt-3 mb-2">
					<div class="col-md-6 col-sm-12 mb-5">
						<label>입금금액 증감현황</label>
						<div id="canvas"></div>

						<%--<label>청구 및 미납 금액 증감 내역</label>--%>
						<%--<canvas id="charge" width="400" height="180" class="chartjs-render-monitor" style="display: block;"></canvas>--%>
					</div>
					<div class="col-md-6 col-sm-12 mb-5">
						<label>수납매체활용율</label>
						<div id="pCanvas"></div>
					</div>
				</div>
			</div>

			<div class="table-option row mb-2">
				<div class="col-md-6 col-sm-12 form-inline">
					<span class="amount mr-2">조회결과 [총 <em class="font-blue" id="totCnt2"></em>건]</span>
				</div>
				<div class="col-md-6 col-sm-12 text-right mt-1">
					<button class="btn btn-sm btn-d-gray hidden-on-mobile" type="button" onclick="fn_fileSave('month')">파일저장</button>
				</div>
			</div>

			<div id="search-result-monthly">
				<div class="row">
					<div class="col">
						<div class="table-responsive mb-3">
							<table class="table table-sm table-hover table-primary">
								<colgroup>
									<col width="114">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
									<col width="83">
								</colgroup>

								<thead>
									<tr>
										<th rowspan="2">수납월</th>
										<th colspan="2">가상계좌</th>
										<th colspan="2">온라인카드</th>
										<th colspan="2">현금</th>
										<th colspan="2">오프라인카드</th>
										<th colspan="2" class="border-r-e6">현금영수증 신청</th>
										<th colspan="2" class="border-l-n">합계</th>
									</tr>

									<tr>
										<th class="border-l">건수(건)</th>
										<th>금액(원)</th>
										<th>건수(건)</th>
										<th>금액(원)</th>
										<th>건수(건)</th>
										<th>금액(원)</th>
										<th>건수(건)</th>
										<th>금액(원)</th>
										<th>건수(건)</th>
										<th>금액(원)</th>
										<th>건수(건)</th>
										<th>금액(원)</th>
									</tr>
								</thead>

								<tbody id="resultBody2">
									<tr>
										<td colspan="13">[조회된 내역이 없습니다.]</td>
									</tr>
								</tbody>

								<tfoot id="resultFoot2">
									<tr>
										<td>합계</td>
										<td class="text-right"></td>
										<td class="text-right"></td>
										<td class="text-right"></td>
										<td class="text-right"></td>
										<td class="text-right"></td>
										<td class="text-right"></td>
										<td class="text-right"></td>
										<td class="text-right"></td>
										<td class="text-right"></td>
										<td class="text-right"></td>
										<td class="text-right"></td>
										<td class="text-right"></td>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false" />

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.js"></script>

<jsp:include page="/WEB-INF/views/include/modal/book-keeping-unit-list.jsp" flush="false" />

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript">

// 조회내역
function fn_ListCollector(num, callback) {

	moPage = '';
	if(num == null || num == 'undefined'){
		moPage = '1';
	} else {
		moPage = num;
	}

	var url = "";

	url = "/sys/chaMgmt/getCollectorListAjax";

	var param = {
			curPage : moPage,
			chaCd   : $('#popChacd').val(),
			chaName : $('#popChaname').val(),
			searchGb   : $('#chaGb').val()
	};
	$.ajax({
		type : "post",
		async : true,
		url : url,
		contentType : "application/json; charset=UTF-8",
		data : JSON.stringify(param),
		success: function(data){
			fnGridCollector(data, 'ResultBodyCollector', callback);
			sysajaxPaging(data, 'ModalPageAreaCollector'); //modal paging
		}
	});
	return false;

}


function fnGridCollector(result, obj, callback) {
	var str = '';
	if(result.count <= 0){
		str += '<tr><td colspan="5" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
	}else{
		$.each(result.list, function(k, v){
			str += '<tr>';
			str +=  '<td class="text-cetner">'+ v.rn +'</td>';
			str +=  '<td class="text-cetner"><a href="#" onclick="fn_setCollectorVal(\'' + nullValueChange(v.chaCd) + '\',\'' + v.chaName + '\',' + callback + ');">' + nullValueChange(v.chaCd) +'</a></td>';
			str +=  '<td>'+ nullValueChange(v.chaName) +'</td>';
			str +=  '<td class="text-cetner">'+ nullValueChange(v.ownerTel) +'</td>';
			str +=  '<td class="text-cetner">'+ nullValueChange(v.agtCd) +'</td>';
			str += '</tr>';
		});
		$('#totCntLookupCollector').text(result.count);
	}
	$('#' + obj).html(str);
}

function  fn_setCollectorVal(chacd, chaname, callback)
{
	if(callback) {
		callback(chacd, chaname);
	} else {
		$('#chacd').val(chacd);
		$('#chaCd').val(chacd);
		//$('#selChaName').val(chaname);
	}
	$("#lookup-collecter-popup").modal("hide");
}

</script>
<input type="hidden" id="chaGb" name="chaGb" />

<div class="inmodal modal fade" id="lookup-collecter-popup" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title">기관검색</h5>
            </div>

            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row m-b-md m-t-md">

                        <div class="col-sm-12">
                            <div class="ibox float-e-margins">
                                <div class="ibox-content">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <label class="form-label block">기관코드</label>
                                            <div class="form-group form-group-sm">
                                                <input type="text" class="form-control"  id="popChacd" maxlength="20" onclick="onlyNumber(this)">
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label block">기관명</label>
                                            <div class="form-group form-group-sm">
                                                <input type="text" class="form-control" id="popChaname" maxlength="100" >
                                            </div>
                                        </div>
                                    </div>

                                    <div class="text-center">
                                        <button type="button" class="btn btn-primary btn-wide" onclick="fn_ListCollector('1');" >조회</button>
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
                                        <span class="amount mr-2">전체 기관 [총 <em class="text-primary" id="totCntLookupCollector">0</em>건]</span>
                                    </div>
                                </div>

                                <div class="ibox-content">
                                    <div class="table-responsive">
                                        <table class="table table-sm table-striped table-hover">
                                            <thead>
                                                <tr>
                                                    <th>NO</th>
                                                    <th>기관코드</th>
                                                    <th>기관명</th>
                                                    <th>기관 전화번호</th>
                                                    <th>등록 지점코드</th>
                                                </tr>
                                            </thead>
                                            <tbody id="ResultBodyCollector"></tbody>
                                        </table>
                                    </div>

                                    <div class="row m-b-lg">
                                        <div class="col-lg-12 text-center">
                                            <div class="btn-group" id="ModalPageAreaCollector">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="text-danger">
                                                * 검색결과 중 해당 기관코드를 클릭하시면 자동으로 선택됨
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

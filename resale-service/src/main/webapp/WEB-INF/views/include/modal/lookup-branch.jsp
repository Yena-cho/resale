<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript">

// 조회내역
function fn_ListBranch(num) { 
	moPage = '';
	if(num == null || num == 'undefined'){
		moPage = '1';
	} else {
		moPage = num;
	}
	
	var url = "/bank/getBranchListAjax"; 
	var param = {
			curPage : moPage,
			agtCd   : $('#popAgtcd').val(),
			agtName : $('#popAgtname').val()
	};
	$.ajax({
		type : "post",
		async : true,
		url : url,
		contentType : "application/json; charset=UTF-8",
		data : JSON.stringify(param),
		success: function(data){
			fnGridBranch(data, 'ResultBodyBranch');
			ajaxModalPaging(data, 'ModalPageAreaBranch'); //modal paging
		}
	});
}

function fnGridBranch(result, obj) {
	var str = '';
	if(result.count <= 0){
		str += '<tr><td colspan="3" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
	}else{
		$.each(result.list, function(k, v){
			str += '<tr>';
			str +=  '<td>'+ v.rn +'</td>';
			str +=  '<td><a href="#" onclick="fn_setBranchVal(\'' + nullValueChange(v.agtCd) + '\', \'' + v.agtName + '\', \'' + v.grpusrcd + '\', \'' + v.agttelno + '\');">' + nullValueChange(v.agtCd) +'</a></td>';
			str +=  '<td>'+ nullValueChange(v.agtName) +'</td>';
			str += '</tr>';
		});
		$('#totCntLookupBranch').text(result.count);
	}
	$('#' + obj).html(str);
}

function  fn_setBranchVal(agtcd, agtname, grpusrcd, agttelno)
{
	$('#agtcd').val(agtcd);
	$('#agtname').val(agtname);
	$('#grpusrcd').val(grpusrcd);
	$('#agttelno').val(agttelno);
	$("#lookup-branch-popup").modal("hide");
}
</script>

<div class="modal fade" id="lookup-branch-popup" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">지점검색</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col">
                            <table class="table table-form mt-4">
                                <tbody class="container-fluid">
                                    <tr class="row no-gutters">
                                        <th class="col-md-2 col-sm-4 col-4">지점코드</th>
                                        <td class="col-md-4 col-sm-8 col-8">
                                            <input type="text" class="form-control" id="popAgtcd" maxlength="20" onkeydown="onlyNumber(this)" onkeypress="if(event.keyCode == 13){fn_ListBranch();}">
                                        </td>
                                        <th class="col-md-2 col-sm-4 col-4">지점명</th>
                                        <td class="col-md-4 col-sm-8 col-8">
                                            <input type="text" class="form-control" id="popAgtname" maxlength="50" onkeypress="if(event.keyCode == 13){fn_ListBranch();}">
                                        </td>
                                    </tr>
                                </tbody>
                            </table>

                            <div class="row list-button-group-bottom mt-3">
                                <div class="col text-center">
                                    <button type="button" class="btn btn-primary btn-wide" onclick="fn_ListBranch('1');" >조회</button>
                                </div>
                            </div>

							<div>
								<div class="row no-gutters mt-4 mb-2">
                                    <span class="amount mr-2">전체지점 [총 <em class="font-blue" id="totCntLookupBranch">0</em>건]</span>
                                </div>
                            </div>
							<div>
								<div class="table-responsive mb-3">
		                            <table class="table table-sm table-primary">
                                        <colgroup>
                                            <col width="70">
                                            <col width="340">
                                            <col width="400">
                                        </colgroup>

		                                <thead>
		                                    <tr>
		                                        <th>NO</th>
		                                        <th>지점코드</th>
		                                        <th>지점명</th>
		                                    </tr>
		                                </thead>
		                                <tbody id="ResultBodyBranch">
		                                	<tr>
		                                		<td colspan="3">[조회된 내역이 없습니다.]</td>
		                                	</tr>
		                                </tbody>
		                            </table>
								</div>
							</div>
							<nav aria-label="Page navigation example">
								<ul class="pagination justify-content-center" id="ModalPageAreaBranch">
								</ul>
							</nav>
                            <div class="font-blue">
	                           * 검색결과 중 해당 지점코드를 클릭하시면 자동으로 선택됩니다.
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

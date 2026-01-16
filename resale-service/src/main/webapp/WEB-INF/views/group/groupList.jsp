<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:include page="/WEB-INF/views/include/group/header.jsp" flush="false"/>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript">
    var firstDepth = "nav-link-36";
    var secondDepth = false;
    
    $(document).ready(function() {
    	
    	fn_search();
    });
    
    var cuPage = 1;
    function fn_search(page) {
    	
    	if(page == null || page == 'undefined'){
    		cuPage = '1';
    	}else{
    		cuPage = page;
    	}
    	var url = "/group/ajaxGroupList";
    	var param = {
   			searchOrderBy : $('#searchOrderBy option:selected').val(),
   			curPage       : cuPage
   		};
    	
    	$.ajax({
			type : "post",
			async : true,
			url : url,
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(param),
			success : function(result) {
				fnGrid(result);		// 현재 데이터로 셋팅
				ajaxPaging(result, 'PageArea');
			}
		});
    	
    }
    
    function fnGrid(data) {
    	$('#count').text(data.count);
    	var str = '';
    	if(data.count <= 0){
    		str+='<tr><td colspan="5" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
    	}else{
    		$.each(data.list, function(i, v){
    			str += '<tr>';
    			str += '<td><a href="#" onclick="fn_orgMove(\'' + v.loginId + '\');">' + v.chaCd + '</a></td>';
    			str += '<td>' + v.chaName + '</td>';
    			str += '<td>' + nullValueChange(v.chrName) + '</td>';
    			str += '<td>' + nullValueChange(v.ownerTel) + '</td>';
    			str += '<td>' + nullValueChange(v.masMonth) + '</td>';
    			str += '</tr>';
    		});
    	}
    	$('#resultBody').html(str);
    }
    
    function list(page) {
    	fn_search(page);
    }
    
    function fn_orgMove(loginId) {
    	$("#loginId").val(loginId);
    	document.frm.action = "/group/moveAuth";
    	document.frm.target = '_blank';
    	document.frm.submit();
    }
    
</script>

</div>

<form id="frm" name="frm" method="post">
	<input type="hidden" id="loginId" name="loginId">
</form>

<div id="contents" class="group-contents">
    <div id="damoa-breadcrumb">
        <nav class="nav container">
            <a class="nav-link active" href="#">기관목록</a>
        </nav>
    </div>
    <div class="container">
        <div id="page-title">
            <div id="breadcrumb-in-title-area" class="row align-items-center">
                <div class="col-12 text-right">
                    <img src="/assets/imgs/common/icon-home.png" class="mr-2">
                    <span> > </span> <span class="depth-1">기관목록</span>
                </div>
            </div>
            <div class="row">
                <div class="col-12">
                    <h2>기관목록</h2>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div id="page-description">
            <div class="row">
                <div class="col-12">
                    <p>기관 그룹 기관 정보입니다.</p>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div id="search-result" class="list-id">
            <div class="table-option row mb-2">
                <div class="col-md-6 col-sm-12 form-inline">
                    <span class="amount mr-2" >조회결과 [총 <em class="font-blue" id="count">0</em>건]</span>
                </div>
                <div class="col-md-6 col-sm-12 text-right mt-1">
                    <select class="form-control" name="searchOrderBy" id="searchOrderBy" onchange="fn_search();">
                        <option value="chaCd">기관코드순 정렬</option>
                        <option value="chaName">기관명순 정렬</option>
                        <option value="masMonth">최근 청구월순 정렬</option>
                    </select>
                </div>
            </div>

            <div class="row">
                <div class="table-responsive col mb-3 pd-n-mg-o">
                    <table class="table table-sm table-hover table-primary">
                        <colgroup>
                            <col width="150">
                            <col width="430">
                            <col width="150">
                            <col width="180">
                            <col width="200">
                        </colgroup>

                        <thead>
                            <tr>
                                <th>기관코드</th>
                                <th>기관명</th>
                                <th>담당자</th>
                                <th>대표번호</th>
                                <th>최근 청구월</th>
                            </tr>
                        </thead>

                        <tbody id="resultBody">
                        </tbody>
                    </table>
                </div>
            </div>

            <jsp:include page="/WEB-INF/views/include/paging.jsp" flush="false" />
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/>

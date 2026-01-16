<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript">
    /**
     * 조회
     */
    function fn_ListCollector(num) {
        moPage = '';
        if (num == null || num == 'undefined') {
            moPage = '1';
        } else {
            moPage = num;
        }

        var url = "/group/getCollectorListAjax";
        var param = {
            curPage: moPage,
            chaCd: $('#popChacd').val(),
            chaName: $('#popChaname').val(),
            chaGb: $('#chaGb').val()
        };
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                fnGridCollector(data, 'ResultBodyCollector');
                ajaxModalPaging(data, 'ModalPageAreaCollector'); //modal paging
            }
        });
    }

    function fnGridCollector(result, obj) {
        var str = '';
        if (result.count <= 0) {
            str += '<tr><td colspan="5" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>';
        } else {
            $.each(result.list, function (k, v) {
                str += '<tr>';
                str += '<td>' + v.rn + '</td>';
                str += '<td><a href="#" onclick="fn_setCollectorVal(\'' + nullValueChange(v.chaCd) + '\',\'' + v.chaName + '\');">' + nullValueChange(v.chaCd) + '</a></td>';
                str += '<td>' + nullValueChange(v.chaName) + '</td>';
                str += '<td>' + nullValueChange(v.ownerTel) + '</td>';
                str += '</tr>';
            });
            $('#totCntLookupCollector').text(result.count);
        }
        $('#' + obj).html(str);
    }

    function fn_setCollectorVal(chacd, chaname) {
        $('#chaCode').val(chacd);
        // $('#chaname').val(chaname);
        $("#lookup-collecter-popup").modal("hide");
    }

    function modalList(num, val) {
        if (val == '55') {
            fn_ListCollector(num); // 기관검색
        } else if (val == '66') {
            fn_ListBranch(num); // 지점검색
        }
    }
</script> 

<input type="hidden" id="chaGb" name=chaGb>

<div class="modal fade" id="lookup-collecter-popup" tabindex="-1" role="dialog" aria-labelledby="regPayer"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">기관검색</h5>
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
                                        <th class="col-md-2 col-sm-4 col-4">기관코드</th>
                                        <td class="col-md-4 col-sm-8 col-8">
                                            <input type="text" class="form-control" id="popChacd" maxlength="20" onkeydown="onlyNumber(this)" onkeypress="if(event.keyCode == 13){fn_ListCollector();}">
                                        </td>
                                        <th class="col-md-2 col-sm-4 col-4">기관명</th>
                                        <td class="col-md-4 col-sm-8 col-8">
                                            <input type="text" class="form-control" id="popChaname" maxlength="100" onkeypress="if(event.keyCode == 13){fn_ListCollector();}">
                                        </td>
                                    </tr>
                                </tbody>
                            </table>

                            <div class="row list-button-group-bottom mt-3">
                                <div class="col text-center">
                                    <button type="button" class="btn btn-primary btn-wide" onclick="fn_ListCollector('1');">조회</button>
                                </div>
                            </div>

                            <div>
                                <div class="row no-gutters mt-4 mb-2">
                                    <span class="amount mr-2">전체지점 [총 <em class="font-blue" id="totCntLookupCollector">0</em>건]</span>
                                </div>
                            </div>

                            <div>
                                <div class="table-responsive mb-3">
                                    <table class="table table-sm table-primary">
                                        <colgroup>
                                            <col width="60">
                                            <col width="140">
                                            <col width="400">
                                            <col width="210">
                                        </colgroup>

                                        <thead>
                                            <tr>
                                                <th>NO</th>
                                                <th>기관코드</th>
                                                <th>기관명</th>
                                                <th>기관 전화번호</th>
                                            </tr>
                                        </thead>

                                        <tbody id="ResultBodyCollector">
                                            <tr>
                                                <td colspan="4">[조회된 내역이 없습니다.]</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <nav aria-label="Page navigation example">
                                <ul class="pagination justify-content-center" id="ModalPageAreaCollector"></ul>
                            </nav>

                            <div class="font-blue">* 검색결과 중 해당 기관코드를 클릭하시면 자동으로 선택됩니다.</div>
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

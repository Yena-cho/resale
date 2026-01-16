<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<style>
    #resultBody_regDt td {
        vertical-align: middle;
        text-align: center;
    }

    table.white-bg tr th{
        text-align: center;
    }
</style>

<script type="text/javascript">
    function funDetailPopUp_regDt(param) {
        var param = {
            rcpmasCd: param
        };

        if(history.pushState) {
            history.pushState(param, '거래일시 상세보기', '/sys/cash-receipt/datailPopUpRegDt?' + $.param(param));
        }

        $.ajax({
            type : "get",
            async : true,
            url : '/sys/rest/cash-receipt/datailPopUpRegDt',
            contentType : "application/json; charset=utf-8",
            data : param,
            success : function(result) {
                fnGrid_regDt(result, 'resultBody_regDt');		// 현재 데이터로 셋팅
            },
            error:function(result){
                swal({
                    type: 'info',
                    text: JSON.stringify(result),
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '확인'
                });
            }
        });
    }

    function fnGrid_regDt(result, obj) {
        var $tbody = $('#' + obj).empty();

        if (result == null) {
            $('<tr><td colspan="15" style="text-align: center;">[조회된 내역이 없습니다.]</td></tr>').appendTo($tbody);
            return;
        }

        $.each(result.itemList, function (i, v) {
            var $tr = $('<tr></tr>').appendTo($tbody);
            $('<td>' + NVL(v.rcpmasCd) + '</td>').appendTo($tr);
            $('<td>' + NVL(v.regDt) + '</td>').appendTo($tr);
            $('<td>' + NVL(v.payDay) + '</td>').appendTo($tr);
            $('<td>' + NVL(v.rcpAmt) + '</td>').appendTo($tr);
        });
    }
</script>

<div class="inmodal modal fade" id="detail-regDt" tabindex="-1" role="dialog" aria-labelledby="regPayer"
     aria-hidden="true">
    <div class="modal-dialog modal-big" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">상세보기</h5>
            </div>

            <div>
                <div>
                    <div class="row">
                        <div class="col-lg-12">
                            <table class="table table-bordered white-bg" style="margin-bottom: 0px;">
                                <tr>
                                    <th>거래번호</th>
                                    <th>거래일시</th>
                                    <th>수납일자</th>
                                    <th>거래금액</th>
                                </tr>
                                <tbody id="resultBody_regDt">
                                <tr>
                                    <td colspan=15>[조회된 내역이 없습니다.]</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer" style="text-align: center;">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>
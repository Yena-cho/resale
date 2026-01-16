<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="modal fade" id="request-cash-receipt-popup" tabindex="-1" role="dialog" aria-hidden="true">
    <input type="hidden" class="client-id" />
    <input type="hidden" class="start-date" />
    <input type="hidden" class="end-date" />
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">현금영수증 생성</h5>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-8 col-md-12">
                            <div class="row m-b-sm">
                                <label class="col-sm-12">수납 방법</label>
                                <div class="form-group form-group-sm">
                                    <div class="input-group col-md-12">
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" name="svecd" value="VAS">
                                            <label> 가상계좌 </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" name="svecd" value="DCS">
                                            <label> 창구현금 </label>
                                        </div>
                                        <div class="radio radio-primary radio-inline">
                                            <input type="radio" name="svecd" value="DVA">
                                            <label> 온라인현금 </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-outline close-button" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary submit-button">생성</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    (function($) {
        var $popup = $('#request-cash-receipt-popup');
        $popup.find('.close-button').click(function() {
            $popup.modal('hide');
        });
        $popup.find('.submit-button').click(function() {
            var svecd = $popup.find('input[name=svecd]:checked').val();
            var clientId = $popup.find('.client-id').val();
            var startDate = $popup.find('.start-date').val();
            var endDate = $popup.find('.end-date').val();

            if(!svecd) {
                alert('수납방법을 선택해주세요.');
                return;
            }

            var param = {
                clientId : clientId,
                startDate : startDate,
                endDate : endDate,
                sveCd: svecd
            };

            $.ajax({
                type : "post",
                async : true,
                url : '/sys/rest/cash-receipt/request-with-client-and-period',
                contentType : 'application/x-www-form-urlencoded',
                data : param,
                success : function(result) {
                    $popup.modal('hide');
                },
                error:function(result){
                }
            });
        });
    })(jQuery);
</script>

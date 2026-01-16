<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
    // 배너 미리보기
    function getBannerPreview(fileId) {
        var str = '';
        var param = {
            fileId: fileId
        };
        $.ajax({
            type: "POST",
            async: true,
            url: "/sys/bannerInfo",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(param),
            success: function (result) {
                if (result.list.viewTypeCd == "S10001") {
                    $('#modalSize').css('width', '895px');
                    str += '<div class="row" style="background: #fff; height: 400px; padding-top: 3rem;">'
                    str += '<span style="display: inline-block; position: relative; z-index: 100;"><h5 style="margin-bottom: 13px; padding: 0 15px; font-weight: 500; color: #fb6e04; font-size: 42px;">' + result.list.title + '</h5>';
                    str += '<p style="padding: 0 20px; font-size: 16px;">' + result.list.content + '</p></span>';
                    str += '<img src="/sys/banner/image?id=' + result.list.fileId + '" style="top: 20px; right: 20px; position: absolute; width: auto; height: 400px;">';
                    str += '</div>'
                } else {
                    $('#modalSize').css('width', '395px');
                    str += '<div class="row" style="background: #fff; height: 370px; padding-top: 3rem;">'
                    str += '<span style="display: inline-block; position: relative; z-index: 100;"><h5 style="margin-bottom: 13px; padding: 0 15px; font-weight: 500; color: #fb6e04; font-size: 42px;">' + result.list.title + '</h5>';
                    str += '<p style="padding: 0 20px; font-size: 16px;">' + result.list.content + '</p></span>';
                    str += '<img src="/sys/banner/image?id=' + result.list.fileId + '" style="bottom: 20px; right: 20px; position: absolute; width: 345px; height: auto;">';
                    str += '</div>'
                }

                $("#imgDiv").html(str);
            }
        });
    }
</script>

<div class="inmodal modal fade" id="banner-preview" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content" id="modalSize">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">배너 미리보기</h5>
            </div>

            <div class="modal-body" id="imgDiv">

            </div>
        </div>
    </div>
</div>
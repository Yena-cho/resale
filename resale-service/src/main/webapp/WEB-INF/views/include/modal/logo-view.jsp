<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
    $(function () {
        $.ajax({
            dataType: "html",
            url: "/common/base64Images?fileTy=logo&fileName=" + $('#chaCd').val(),
            success: function (data) {
                var imageString = "data:image/jpeg;base64,";
                var img = document.createElement('img'); // 이미지 객체 생성
                img.src = imageString + data;
                document.getElementById('logodiv').appendChild(img); // 이미지 동적 추가
                $(img).attr("width", "150");
                $(img).attr("height", "30");
                $(img).css("cursor", "pointer");
            }
        });
    });
</script>

<div class="modal fade" id="logo-view-popup" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">로고 미리보기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col text-center" id="logodiv"></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col text-center">
                            <button type="button" class="btn btn-primary btn-outlined" data-dismiss="modal" aria-label="Close">닫기</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

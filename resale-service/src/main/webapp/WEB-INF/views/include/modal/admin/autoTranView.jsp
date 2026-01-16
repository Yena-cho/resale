<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script type="text/javascript">
    //파일보기
    function fn_fileView(file, fileTy, ext) {

    	$('#cmsPlayContainer').empty();

    	var url = "";
    	var type = "";
    	var fmime = "";

        if(fileTy == "aud"){
            type = "audio";
            url = "/sys/streamWithdrawAgreement?id="+ext;
            var itag = document.createElement(type); // 이미지 객체 생성
            $(itag).attr("src", url);
            $(itag).attr("controls","controls");
            $(itag).attr("width", "100%");
            $(itag).attr("height", "100%");
            $(itag).css({"margin" : "0 auto", "cursor": "pointer"});
            document.getElementById('cmsPlayContainer').appendChild(itag); // 이미지 동적 추가
        }else{
            type = "img";
            url = "/sys/streamWithdrawAgreement?id="+ext;
            var itag = document.createElement(type); // 이미지 객체 생성
            $(itag).attr("src", url);
            $(itag).attr("width", "100%");
            $(itag).attr("height", "100%");
            $(itag).css({"margin" : "0 auto", "cursor": "pointer"});
            document.getElementById('cmsPlayContainer').appendChild(itag); // 이미지 동적 추가
        }

        $('#autoTranView').modal({backdrop: 'static', keyboard: false});
    }

</script>

<div class="modal fade" id="autoTranView" tabindex="-1" role="dialog" aria-labelledby="regPayer" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title">출금동의서등록</h5>
            </div>
            <div class="modal-body">
                <div id="page-description" class="container-fluid">
                    <div class="row">
                        <div class="col-12">
                            <p>출금동의서를 확인하는 화면입니다.</p>
                        </div>
                    </div>
                </div>
                <div class="container-fluid">
                    <div id="img_view">
<!--                         <img id="img" width="500" height="700"> -->
                        <div id="cmsPlayContainer" style="padding-top:10px;"></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-md btn-default" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

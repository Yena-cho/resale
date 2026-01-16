<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<link rel="stylesheet" type="text/css" href="/assets/bootstrap/bootstrap.css">
<link rel="stylesheet" type="text/css" href="/assets/css/damoa.min.css">

<jsp:include page="/WEB-INF/views/include/org/header.jsp" flush="false"/>

<script>
    var firstDepth = null;
    var secondDepth = null;
</script>

<script>
    $(document).ready(function () {
        var url = "/common/login/cmsChaInfo";
        var param = {};
        $.ajax({
            type: "post",
            async: true,
            url: url,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(param),
            success: function (data) {
                $(".chaName").text(data.chaName);
                $(".chaOffNo").text(data.chaOffNo);
                $(".chrHp").text(data.chrHp);
                $(".feeBankCd").text(data.feeBankCd);
                $(".feeVano").text(data.feeVano);
            }
        });
    });

    // ARS 동의하기
    function fn_arsAgree() {
        $("#btn-ars").html('');
        $("#btn-ars").html('<button type="button" class="btn btn-primary save-payer-information btn-agree" onclick="fn_telArs();">ARS 동의하기</button>');
        $('#cms-ars-agree').modal({backdrop: 'static', keyboard: false});
    }

    // 서면 동의하기
    function fn_fileAgree() {
        $('#cms-file-agree').modal({backdrop: 'static', keyboard: false});
    }

    // 메인으로 이동
    function fn_goMain() {
        swal({
            type: 'question',
            html: "출금동의 미완료시 서비스 이용이 정지될 수 있습니다.<p>메인화면으로 이동하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        }).then(function (result) {
            if (result.value) {
                window.location.href = '/org/main/index';
            }
        });
    }
</script>

<div id="contents">
    <div class="container">
        <div id="page-title">
            <div class="row">
                <div class="col-12">
                    <h2>가상계좌 수납관리 서비스 수수료 자동출금 동의</h2>
                </div>
            </div>
        </div>

        <div class="row mt-3">
            <div class="col-12 ars-content">
                <ul>
                    <li>부당인출 사고 방지를 위하여 전자금융거래법 제15조 제1항 및 동법 시행령 제 10조에 의해 요금 청구기관은 지급인으로부터 출금에 대한 동의가 반드시 필요하며, 동의에 관한 증거자료를 보관하여야 합니다.</li>
                </ul>
            </div>
        </div>

        <div class="row mt-3 mb-1 text-center">
            <%--<div class="cms-agree-step mr-2" style="cursor: pointer;"><strong>ARS 동의 절차</strong></div>--%>
            <div class="cms-agree-step" style="cursor: pointer;"><strong>서면 동의 절차</strong></div>
        </div>

        <div class="row">
            <%--<div class="cms-agree-step mr-2">--%>
                <%--<div class="row align-items-center">--%>
                    <%--<div class="col col-md-12 col-sm-12 col-12 text-center">--%>
                        <%--<h5>Step1</h5>--%>
                        <%--<p>ARS 동의하기</p>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="row align-items-center">--%>
                    <%--<div class="col col-md-12 col-sm-12 col-12 text-center">--%>
                        <%--<h5>Step2</h5>--%>
                        <%--<p>ARS 동의 요청</p>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="row align-items-center">--%>
                    <%--<div class="col col-md-12 col-sm-12 col-12 text-center">--%>
                        <%--<h5>Step3</h5>--%>
                        <%--<p>담당자 핸드폰번호로 ARS 동의 진행</p>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="row align-items-center">--%>
                    <%--<div class="col col-md-12 col-sm-12 col-12 text-center">--%>
                        <%--<h5>Step4</h5>--%>
                        <%--<p>ARS 동의 완료</p>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="row align-items-center">--%>
                    <%--<div class="col col-md-12 col-sm-12 col-12 text-center">--%>
                        <%--<h5>Step5</h5>--%>
                        <%--<p>신한 다모아 수수료 자동출금 동의 완료</p>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>

            <div class="cms-agree-step">
                <div class="row align-items-center">
                    <div class="col col-md-12 col-sm-12 col-12 text-center">
                        <h5>Step1</h5>
                        <p>서면 동의하기</p>
                    </div>
                </div>
                <div class="row align-items-center">
                    <div class="col col-md-12 col-sm-12 col-12 text-center">
                        <h5>Step2</h5>
                        <p>동의서 다운로드</p>
                    </div>
                </div>
                <div class="row align-items-center">
                    <div class="col col-md-12 col-sm-12 col-12 text-center">
                        <h5>Step3</h5>
                        <p>동의서 출금 내용 작성</p>
                    </div>
                </div>
                <div class="row align-items-center">
                    <div class="col col-md-12 col-sm-12 col-12 text-center">
                        <h5>Step4</h5>
                        <p>가상계좌 수납관리 서비스 이메일 또는 팩스 전송</p>
                    </div>
                </div>
                <div class="row align-items-center">
                    <div class="col col-md-12 col-sm-12 col-12 text-center">
                        <h5>Step5</h5>
                        <p>가상계좌 수납관리 서비스 수수료 자동출금 동의 완료</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="row mt-2 mb-3 text-center">
            <%--<div class="cms-agree-step btn-primary mr-2" onclick="fn_arsAgree();" style="cursor: pointer;">ARS 동의하기</div>--%>
            <div class="cms-agree-step btn-primary" onclick="fn_fileAgree()" style="cursor: pointer;">서면 동의하기</div>
        </div>

        <div class="row mb-3">
            <div class="col-12 ars-content">
                <ul>
                    <li class="text-danger"><strong>자동출금 동의가 진행되지 않은 기관에 한하여, 본 화면이 노출됩니다.</strong></li>
                    <li class="text-danger"><strong>빠른 시일 내에 출금동의를 진행해주시기 바라며, 출금 미동의로 인해 2개월 이상 미납이 지속될 경우 서비스이용약관 제6조7항에 의해 이용의사가 없는 것으로 간주하여 서비스가 중지될 수 있습니다.</strong></li>
                </ul>
            </div>
        </div>

        <div class="row text-center" style="justify-content: center;">
            <button class="btn btn-wide btn-outline-primary" onclick="fn_goMain();">메인으로 이동</button>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/include/org/footer.jsp" flush="false"/>

<%-- ARS 동의 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/cms-ars.jsp" flush="false"/>

<%-- 서면 동의 팝업 --%>
<jsp:include page="/WEB-INF/views/include/modal/cms-fileDownload.jsp" flush="false"/>

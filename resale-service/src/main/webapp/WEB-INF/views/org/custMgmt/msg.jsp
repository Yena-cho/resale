<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<title>전문테스트</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="theme-color" content="#ffffff" />
<link rel="icon" type="image/png" sizes="16x16" href="/assets/imgs/favicon.ico/favicon-16x16.png" />
<link rel="stylesheet" type="text/css" href="/assets/bootstrap/bootstrap-for-admin.min.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/style.min.css" />
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10">
			<h2>전문테스트</h2>
			<p class="page-description">전문을 테스트하는 화면입니다.</p>
		</div>
		<div class="col-lg-2"></div>
	</div>

	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row">
			<div class="col-md-12">
				<div class="ibox">
					<div class="ibox-content">
						<form action="/msgtest2" method="post" class="form-horizontal">
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<label class="col-sm-2 control-label">전문종류</label>
								<div class="col-sm-4">
									<select id="type" name="type" class="form-control">
										<option value="REQ">REQ</option>
										<option value="START">START</option>
										<option value="AGGREGATE">AGGREGATE</option>
										<option value="LONG">LONG</option>
									</select>
								</div>
								<label class="col-sm-2 control-label">기관코드</label>
								<div class="col-sm-4">
									<input type="text" id="corpCode" name="corpCode" class="form-control" value="${corpCode}"/>
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<label class="col-sm-2 control-label">가상계좌번호</label>
								<div class="col-sm-4">
									<input type="text" id="vano" name="vano" class="form-control" value="${vano}" />
								</div>
								<label class="col-sm-2 control-label">거래금액</label>
								<div class="col-sm-4 form-inline">
									<input type="text" id="amount" name="amount" class="form-control text-right" value="${amount}"/><span></span>
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="form-group text-center">
								<button class="btn btn-lg btn-w-m btn-primary">전문데이터 발송</button>
							</div>
							<div class="col-md-12">
								<div class="ibox dashboard-item">
									<div class="ibox-title">
										<h3 class="dashboard-title">전문송신결과</h3>
									</div>
									<div class="ibox-content">
										<div class="row m-t-lg">
											<div class="col-sm-12">
												<textarea class="form-control" id="result" name="result" rows="10" >${result}</textarea>
											</div>
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
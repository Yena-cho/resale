<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE HTML>
<html lang="ko">
<head>
    <meta charset="utf-8"/>

    <link rel="stylesheet" type="text/css" href="/assets/bootstrap/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/swiper.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/damoa.min.css">

    <script src="/assets/js/jquery.min.js"></script>
    <script src="/assets/js/jquery-ui.min.js"></script>

    <script src="/assets/bootstrap/popper.js"></script>
    <script src="/assets/bootstrap/bootstrap.min.js"></script>
    <script src="/assets/js/swiper.js"></script>
    <script src="/assets/js/common.js?version=${project.version}"></script>
    <script src="/assets/js/footer.js"></script>

    <%-- Global site tag (gtag.js) - Google Analytics --%>
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-122322587-1"></script>
    <script>
        window.dataLayer = window.dataLayer || [];

        function gtag() {
            dataLayer.push(arguments);
        }

        gtag('js', new Date());

        gtag('config', 'UA-122322587-1');
    </script>

</head>


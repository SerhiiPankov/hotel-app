<%@ page import="hotel.util.Constant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8>

    <title>Error page</title>
</head>

<body class="align-items-center">

    <%@include file='resources/nav.jsp' %>

    <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
            && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.CSRF_ERROR)) {%>
        <h6 class="text-center"
            style="color: red">
            <fmt:message key="csrf.error.message"/>
        </h6>
    <%}%>

    <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
            && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.ACCESS_ERROR_MSG)) {%>
        <h6 class="text-center"
            style="color: red">
            <fmt:message key="db.processing.error.message"/>
        </h6>
    <%}%>

    <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
            && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.NO_PARAMETER_ERROR_MSG)) {%>
        <h6 class="text-center"
            style="color: red">
            <fmt:message key="no.parameter.error.message"/>
        </h6>
    <%}%>

    <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
            && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.WRONG_PARAMETER_ERROR_MSG)) {%>
        <h6 class="text-center"
            style="color: red">
            <fmt:message key="wrong.parameter.error.message"/>
        </h6>
    <%}%>
</body>
</html>

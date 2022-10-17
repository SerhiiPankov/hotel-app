<%@ page import="hotel.util.Constant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8>

    <title>Login</title>
</head>

<body class="align-items-center">

    <%@include file="resources/nav.jsp"%>

    <div class="container-xl">
        <h3 class="text-center">
            <fmt:message key="login.title"/>
        </h3>
    </div>

    <div class="container-sm w-25 align-items-center">
        <form class="was-validated"
              method="post"
              name="login"
              action="${pageContext.request.contextPath}/authorization"
              autocomplete="off">
            <input type="hidden"
                   name="csrfSalt"
                   value="<c:out value='${csrfSalt}'/>"/>

            <div class="mb-3">
                <label class="form-label"
                       for="email">
                    <fmt:message key="login.form.label.username"/>
                </label>

                <input class="form-control"
                       id="email"
                       name="email"
                       type="email"
                       required
                       autocomplete="off">

                <div class="invalid-feedback">
                    <fmt:message key="login.form.error.email"/>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label"
                       for="password">
                    <fmt:message key="login.form.label.password"/>
                </label>

                <input class="form-control"
                       id="password"
                       name="password"
                       type="password"
                       required
                       autocomplete="new-password">

                <div class="invalid-feedback">
                    <fmt:message key="login.form.error.password"/>
                </div>
            </div>

            <div class="mb-3 text-center">
                <button class="btn btn-outline-dark"
                        type="submit">
                    <fmt:message key="login.form.button.submit"/>
                </button>
            </div>
        </form>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.ACCESS_ERROR_MSG)) {%>
            <h6 class="text-center"
                style="color: red">
                <fmt:message key="login.db.processing.error.message"/>
            </h6>
        <%}%>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.AUTH_ERROR_MSG)) {%>
            <h6 class="text-center"
                style="color: red">
                <fmt:message key="login.authentication.error.message"/>
            </h6>
        <%}%>
    </div>
</body>
</html>

<%@ page import="hotel.model.enums.Role" %>
<%@ page import="hotel.util.Constant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8>

    <title>Register</title>
</head>

<body class="align-items-center">

    <%@include file="resources/nav.jsp"%>

    <div class="container-sm" >
        <% if (request.getSession().getAttribute(Constant.SESSION_ATTRIBUTE_USER_ROLE) == null
                || request.getSession().getAttribute(Constant.SESSION_ATTRIBUTE_USER_ROLE).equals(Role.MANAGER)
                || request.getSession().getAttribute(Constant.SESSION_ATTRIBUTE_USER_ROLE).equals(Role.CUSTOMER)) { %>
            <h3 class="text-center"><fmt:message key="register.customer"/></h3>
        <% } else { %>
            <h3 class="text-center"><fmt:message key="register.manager"/></h3>
        <% } %>
    </div>

    <div class="container-sm w-25 align-items-center">
        <form class="was-validated"
              method="post"
              autocomplete="off"
              action="${pageContext.request.contextPath}/register/do">
            <input type="hidden"
                   name="csrfSalt"
                   value="<c:out value='${csrfSalt}'/>"/>

            <div class="mb-3">
                <label class="form-label"
                       for="newUserEmail">
                    <fmt:message key="register.form.label.email"/>
                </label>

                <input class="form-control"
                       id="newUserEmail"
                       name="newUserEmail"
                       type="email"
                       required
                       autocomplete="off">

                <div class="invalid-feedback">
                    <fmt:message key="register.form.error.email"/>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label"
                       for="newUserPassword">
                    <fmt:message key="register.form.label.password"/>
                </label>

                <input class="form-control"
                       id="newUserPassword"
                       name="newUserPassword"
                       type="password"
                       minlength="4"
                       required
                       autocomplete="new-password">

                <div class="invalid-feedback">
                    <fmt:message key="register.form.error.password"/>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label"
                       for="newUserPasswordRepeat">
                    <fmt:message key="register.form.label.password.repeat"/>
                </label>

                <input class="form-control"
                       id="newUserPasswordRepeat"
                       name="newUserPasswordRepeat"
                       type="password"
                       minlength="4"
                       required>

                <div class="invalid-feedback">
                    <fmt:message key="register.form.error.password.repeat"/>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label"
                       for="newUserName">
                    <fmt:message key="register.form.label.name"/>
                </label>

                <input class="form-control"
                       id="newUserName"
                       name="newUserName"
                       type="text"
                       minlength="2"
                       required>

                <div class="invalid-feedback">
                    <fmt:message key="register.form.error.name"/>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label"
                       for="newUserCountryCode">
                    <fmt:message key="register.form.label.country.code"/>
                </label>

                <input class="form-control"
                       id="newUserCountryCode"
                       name="newUserCountryCode"
                       type="text"
                       minlength="2"
                       maxlength="4"
                       required
                       pattern="[0-9]{2,4}"
                       onkeyup="this.value=this.value.replace(/\D/g,'')"
                       onchange="this.value=this.value.replace(/\D/g,'')">

                <div class="invalid-feedback">
                    <fmt:message key="register.form.error.country.code"/>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label"
                       for="newUserPhoneNumber">
                    <fmt:message key="register.form.label.phone"/>
                </label>

                <input class="form-control"
                       id="newUserPhoneNumber"
                       name="newUserPhoneNumber"
                       type="text"
                       minlength="9"
                       maxlength="9"
                       required
                       pattern="[0-9]{9}"
                       onkeyup="this.value=this.value.replace(/\D/g,'')"
                       onchange="this.value=this.value.replace(/\D/g,'')">

                <div class="invalid-feedback">
                    <fmt:message key="register.form.error.phone"/>
                </div>
            </div>

            <div class="mb-3 text-center">
                <button class="btn btn-outline-dark"
                        type="submit" >
                    <fmt:message key="register.form.button.submit"/>
                </button>
            </div>
        </form>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.REGISTER_OK_MSG)) {%>
        <h4 class="text-center"
            style="color: red">
            <fmt:message key="register.form.ok.register"/>
        </h4>
        <%}%>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.NO_MATCH_PASSWORDS_ERROR_MSG)) {%>
            <h4 class="text-center"
                style="color: red">
                <fmt:message key="register.form.error.not.equals.password"/>
            </h4>
        <%}%>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.REGISTER_ERROR_MSG)) {%>
            <h4 class="text-center"
                style="color: red">
                <fmt:message key="register.form.error.not.register"/>
            </h4>
        <%}%>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.ACCESS_ERROR_MSG)) {%>
            <h4 class="text-center"
                style="color: red">
                <fmt:message key="register.form.error.cannot.create.user"/>
                <% if (request.getParameter(Constant.PARAMETER_EMAIL) != null) { %>
                    ${param.email}
                <%}%>
                <fmt:message key="register.form.error.cannot.create.user.2"/>
            </h4>
        <%}%>
    </div>
</body>
</html>

<%@ page import="hotel.model.enums.Role"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="language" value="${not empty param.language ? param.language : not empty sessionScope.language ? sessionScope.language : pageContext.request.locale}" scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="language"/>

<!DOCTYPE html>
<html lang="${language}">
<head>
    <title>NAV</title>

    <style>
        <%@include file='../css/bootstrap.min.css'%>
    </style>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js">
    </script>


</head>

<body>
    <div class="container-xl">
        <nav class="navbar navbar-expand-lg bg-light">
            <div class="container-fluid a">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                    <fmt:message key="navbar.main.title"/>
                </a>

                <button class="navbar-toggler"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent"
                        aria-expanded="false"
                        aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse navbar-nav justify-content-start">
                    <% if (request.getSession().getAttribute("userRole") != null
                            && request.getSession().getAttribute("userRole").equals(Role.CUSTOMER)) {%>

                        <a class="nav-link" href="${pageContext.request.contextPath}/booking/request/create">
                            <fmt:message key="navbar.booking"/>
                        </a>

                        <a class="nav-link" href="${pageContext.request.contextPath}/booking/select">
                            <fmt:message key="navbar.select"/>
                        </a>
                    <%}%>

                    <% if (request.getSession().getAttribute("userRole") != null
                            && (request.getSession().getAttribute("userRole").equals(Role.MANAGER)
                            || request.getSession().getAttribute("userRole").equals(Role.ADMIN))) {%>
                        <div class="dropdown">
                            <button class="dropdown-item nav-link dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <fmt:message key="navbar.dropdown.title"/>
                            </button>

                            <ul class="dropdown-menu">
                                <li>
                                    <a class="dropdown-item  nav-link" href="${pageContext.request.contextPath}/users">
                                        <% if (request.getSession().getAttribute("userRole").equals(Role.MANAGER)) {%>
                                            <fmt:message key="navbar.customers.title"/>
                                        <%}%>

                                        <% if (request.getSession().getAttribute("userRole").equals(Role.ADMIN)) {%>
                                        <fmt:message key="navbar.users.title"/>
                                        <%}%>
                                    </a>
                                </li>

                                <li><hr class="dropdown-divider"></li>

                                <li>
                                    <a class="dropdown-item  nav-link" href="${pageContext.request.contextPath}/rooms/class">
                                        <fmt:message key="navbar.room.class.title"/>
                                    </a>
                                </li>

                                <li>
                                    <a class="dropdown-item  nav-link" href="${pageContext.request.contextPath}/rooms">
                                        <fmt:message key="navbar.room.title"/>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    <%}%>
                </div>

                <div class=" collapse navbar-collapse navbar-nav justify-content-end">
                    <% if (request.getSession().getAttribute("userRole") != null
                        && (request.getSession().getAttribute("userRole").equals(Role.ADMIN)
                            || request.getSession().getAttribute("userRole").equals(Role.MANAGER))) {%>
                        <div>
                            <a class="nav-link" href="${pageContext.request.contextPath}/booking/request/all">
                                <fmt:message key="navbar.booking.request.all"/> (<span style="color: #bf8104">${numberOfRequests}</span>)
                            </a>
                        </div>

                        <div>
                            <a class="nav-link" href="${pageContext.request.contextPath}/booking/all">
                                <fmt:message key="navbar.booking.all"/> (<span style="color: #bf8104">${numberOfBooking}</span>)
                            </a>
                        </div>
                    <%}%>

                    <% if (request.getSession().getAttribute("userRole") == null) {%>
                        <a class="nav-link" href="${pageContext.request.contextPath}/login">
                            <fmt:message key="navbar.login.title"/>
                        </a>
                    <%}%>

                    <% if (request.getSession().getAttribute("userRole") == null
                        || request.getSession().getAttribute("userRole").equals(Role.ADMIN)
                        || request.getSession().getAttribute("userRole").equals(Role.MANAGER)) {%>
                        <a class="nav-link" href="${pageContext.request.contextPath}/register">
                            <% if (request.getSession().getAttribute("userRole") == null) {%>
                            <fmt:message key="navbar.register.new.title"/>
                            <% } else if (request.getSession().getAttribute("userRole").equals(Role.ADMIN)) {%>
                            <fmt:message key="navbar.register.manager.title"/>
                            <% } else { %>
                            <fmt:message key="navbar.register.customer.title"/>
                            <%}%>
                        </a>
                    <%}%>

                    <% if (request.getSession().getAttribute("userRole") != null
                            && request.getSession().getAttribute("userRole").equals(Role.CUSTOMER)) {%>
                            <a class="nav-link" href="${pageContext.request.contextPath}/booking/request/all">
                                <fmt:message key="navbar.booking.request.my"/> (<span style="color: #bf8104">${numberOfRequests}</span>)
                            </a>

                            <a class="nav-link" href="${pageContext.request.contextPath}/booking/all">
                                <fmt:message key="navbar.booking.my"/> (<span style="color: #bf8104">${numberOfBooking}</span>)
                            </a>
                    <%}%>

                    <form>
<%--                        <input type="hidden"--%>
<%--                               name="csrfSalt"--%>
<%--                               value="<c:out value='${csrfSalt}'/>"/>--%>

                        <label for="language">
                            <select class="form-select form-select-sm"
                                    id="language"
                                    name="language"
                                    onchange="submit()">
                                <option value="en" ${language == 'en' ? 'selected' : ''}>EN</option>
                                <option value="uk" ${language == 'uk' ? 'selected' : ''}>UA</option>
                            </select>
                        </label>
                    </form>

                    <% if (request.getSession().getAttribute("userId") != null
                            && request.getSession().getAttribute("userName") != null) {%>

                        <div class="dropdown">
                            <button id="refresh" class="dropdown-item nav-link dropdown-toggle" type="button"
                                    data-bs-toggle="dropdown" aria-expanded="false">
                                ${userName}
                            </button>

                            <ul class="dropdown-menu">
                                <li>
                                    <a class="dropdown-item nav-link" href="${pageContext.request.contextPath}/users/account">
                                        <fmt:message key="navbar.manage.account.title"/>
                                    </a>
                                </li>

                                <li><hr class="dropdown-divider"></li>

                                <li>
                                    <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                                        <fmt:message key="navbar.logout.title"/>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    <%}%>
                </div>
            </div>
        </nav>
    </div>
</body>
</html>

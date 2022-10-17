<%@ page import="hotel.util.Constant" %>
<%@ page pageEncoding="UTF-8" contentType="text/html" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8>

    <title>All users</title>

    <style>
        <%@include file='../css/bootstrap.min.css' %>
    </style>
</head>
<body class="align-items-center">

    <%@include file='../resources/nav.jsp' %>

    <div class="container-xl">
        <h3 class="text-center">
            <fmt:message key="users.title"/>
        </h3>
    </div>

    <div class="container-xl align-items-center">
        <form action="${pageContext.request.contextPath}/users"
              method="post">
            <input type="hidden"
                   name="csrfSalt"
                   value="<c:out value='${csrfSalt}'/>"/>

            <label for="sortUser">
                <fmt:message key="user.sort.label"/>
            </label>

            <select class="form-select form-select-sm w-25"
                    aria-label=".form-select-lg example"
                    id="sortUser"
                    name="sortUser"
                    onchange="submit()">
                <option value=""></option>

                <option <c:if test="${sortUser == 1}" >selected</c:if> value=1>
                    <fmt:message key="user.sort.label.name"/>
                </option>

                <option <c:if test="${sortUser == 2}" >selected</c:if> value=2>
                    <fmt:message key="user.sort.label.email"/>
                </option>

                <option <c:if test="${sortUser == 3}" >selected</c:if> value=3>
                    <fmt:message key="user.sort.label.is.deleted"/>
                </option>
            </select>
        </form>
    </div>

    <div class="container-xl align-items-center">
        <nav aria-label="...">
            <ul class="pagination justify-content-center">
                <c:if test="${userCurrentPage != 1 && userNumberOfPages > 1}">
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/users?page=${userCurrentPage - 1}">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                </c:if>

                <c:if test="${userCurrentPage == 1 && userNumberOfPages > 1}">
                    <li class="page-item disabled">
                                <span class="page-link">
                                    <span aria-hidden="true">&laquo;</span>
                                </span>
                    </li>
                </c:if>

                <c:forEach begin="1" end="${userNumberOfPages}" var="i">

                    <c:if test="${i <= userCurrentPage + 4 && i >= userCurrentPage - 4}">
                        <c:choose>
                            <c:when test="${userCurrentPage == i}">
                                <li class="page-item disabled">
                                            <span class="page-link ">
                                                    ${i}
                                            </span>
                                </li>
                            </c:when>

                            <c:otherwise>
                                <li class="page-item">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/users?page=${i}">
                                            ${i}
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </c:forEach>

                <c:if test="${userCurrentPage < userNumberOfPages}">
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/users?page=${userCurrentPage + 1}">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </c:if>

                <c:if test="${userCurrentPage == userNumberOfPages && userNumberOfPages != 1}">
                    <li class="page-item">
                                <span class="page-link disabled">
                                    <span aria-hidden="true">&raquo;</span>
                                </span>
                    </li>
                </c:if>
            </ul>
        </nav>

        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th class="col-2"
                        scope="col">
                        <fmt:message key="users.label.name" />
                    </th>

                    <th class="col-1"
                        scope="col">
                        <fmt:message key="users.label.email" />
                    </th>

                    <th class="col-1"
                        scope="col">
                        <fmt:message key="users.label.phone" />
                    </th>

                    <th class="col-1"
                        scope="col">
                        <fmt:message key="users.label.role" />
                    </th>

                    <th class="col-1"
                        scope="col">
                        <fmt:message key="users.label.action" />
                    </th>
                </tr>
            </thead>

            <tbody class="table-group-divider">
                <c:forEach var="user" items="${allUsers}">
                    <tr class="
                            <c:choose>
                                <c:when test="${user.isDeleted == true}">table-danger</c:when>

                                <c:when test="${user.isDeleted == false}">table-success</c:when>
                            </c:choose>">
                        <td>
                            <c:out value="${user.name}"/>
                        </td>

                        <td>
                            <c:out value="${user.email}"/>
                        </td>

                        <td>
                            <c:out value="${user.phone}"/>
                        </td>

                        <td>
                            <c:out value="${user.role}"/>
                        </td>

                        <td>
                            <c:if test="${user.isDeleted == false}">
                                <button class="btn btn-outline-secondary"
                                        type="button"
                                        data-bs-toggle="modal"
                                        data-bs-target="#deleteModal${user.id}">
                                    <fmt:message key="users.button.delete" />
                                </button>

                                <div class="modal fade"
                                     id="deleteModal${user.id}"
                                     tabindex="-1"
                                     aria-labelledby="deleteModalLabel${user.id}"
                                     aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title"
                                                    id="deleteModalLabel${user.id}">
                                                    <fmt:message key="users.delete.modal.title" />
                                                </h5>

                                                <button type="button"
                                                        class="btn-close"
                                                        data-bs-dismiss="modal"
                                                        aria-label="Close">
                                                </button>
                                            </div>

                                            <div class="modal-body text-start">
                                                <h6><fmt:message key="users.delete.modal.text" /></h6>
                                            </div>

                                            <div class="modal-footer">
                                                <button class="btn btn-outline-secondary"
                                                        type="button"
                                                        data-bs-dismiss="modal">

                                                    <fmt:message key="users.delete.button.modal.close" />
                                                </button>

                                                <form id="delete${user.id}"
                                                      method="post"
                                                      action="${pageContext.request.contextPath}/users/delete">
                                                    <input type="hidden"
                                                           name="csrfSalt"
                                                           value="<c:out value='${csrfSalt}'/>"/>

                                                    <input type="hidden"
                                                           name="userId"
                                                           value="${user.id}"/>

                                                    <button class="btn btn-outline-dark"
                                                            form="delete${user.id}">
                                                        <fmt:message key="users.delete.modal.button.delete"/>
                                                    </button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <c:if test="${user.isDeleted == true}">
                                <button class="btn btn-outline-secondary"
                                        type="button"
                                        data-bs-toggle="modal"
                                        data-bs-target="#restoreModal${user.id}">
                                    <fmt:message key="users.button.restore" />
                                </button>

                                <div class="modal fade"
                                     id="restoreModal${user.id}"
                                     tabindex="-1"
                                     aria-labelledby="restoreModalLabel${user.id}"
                                     aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title"
                                                    id="restoreModalLabel${user.id}">
                                                    <fmt:message key="users.restore.modal.title" />
                                                </h5>

                                                <button type="button"
                                                        class="btn-close"
                                                        data-bs-dismiss="modal"
                                                        aria-label="Close">
                                                </button>
                                            </div>

                                            <div class="modal-body text-start">
                                                <h6><fmt:message key="users.restore.modal.text" /></h6>
                                            </div>

                                            <div class="modal-footer">
                                                <button class="btn btn-outline-secondary"
                                                        type="button"
                                                        data-bs-dismiss="modal">

                                                    <fmt:message key="users.restore.button.modal.close" />
                                                </button>

                                                <form id="restore${user.id}"
                                                      method="post"
                                                      action="${pageContext.request.contextPath}/users/restore" >
                                                    <input type="hidden"
                                                           name="userId"
                                                           value="${user.id}">

                                                    <input type="hidden"
                                                           name="csrfSalt"
                                                           value="<c:out value='${csrfSalt}'/>"/>

                                                    <button class="btn btn-outline-dark"
                                                            form="restore${user.id}">
                                                        <fmt:message key="users.restore.modal.button.delete"/>
                                                    </button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.ACCESS_ERROR_MSG)) {%>
            <h6 class="text-center"
                style="color: red">
                <fmt:message key="users.error.message"/>
            </h6>
        <%}%>
    </div>
</body>
</html>

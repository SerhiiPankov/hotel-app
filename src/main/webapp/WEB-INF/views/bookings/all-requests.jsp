<%@ page import="hotel.util.Constant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8>

    <title>Request</title>
</head>

<body class="align-items-center">

    <%@include file='../resources/nav.jsp' %>

    <div class="container-xl">
        <h3 class="text-center">
            <fmt:message key="request.all.title"/>
        </h3>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.DELETE_ERROR_MSG)) {%>
            <h6 class="text-center"
                style="color: red">
                <fmt:message key="request.all.error.delete.message"/>
            </h6>
        <%}%>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.CREATE_ERROR_MSG)) {%>
            <h6 class="text-center"
                style="color: red">
                <fmt:message key="request.all.error.create.message"/>
            </h6>
        <%}%>
    </div>

    <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
            && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.SELECTION_HOTEL_ROOM_ERROR_MSG)) {%>
        <h6 class="text-center"
            style="color: red">
            <fmt:message key="request.error.select.message"/>
        </h6>
    <%}%>

    <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
            && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.BUSY_HOTEL_ROOM_ERROR_MSG)) {%>
        <h6 class="text-center"
            style="color: red">
            <fmt:message key="request.error.busy.message"/>
        </h6>
    <%}%>

    <div class="container-xl align-items-center">
        <form action="${pageContext.request.contextPath}/booking/request/all"
              method="post">
            <label for="filterRequest">
                <fmt:message key="request.all.sort.label"/>
            </label>

            <select class="form-select form-select-sm w-25"
                    aria-label=".form-select-lg example"
                    id="filterRequest"
                    name="filterRequest"
                    onchange="submit()">
                <option <c:if test="${filterRequest == 1}" >selected</c:if> value=1>
                    <fmt:message key="request.all.sort.label.on.anvil"/>
                </option>

                <option <c:if test="${filterRequest == 2}" >selected</c:if> value=2>
                    <fmt:message key="request.all.sort.label.processed"/>
                </option>
            </select>
        </form>
    </div>

    <div class="container-xl align-items-center">
        <nav aria-label="...">
            <ul class="pagination justify-content-center">
                <c:if test="${requestCurrentPage != 1 && requestNumberOfPages > 1}">
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/booking/request/all?page=${requestCurrentPage - 1}">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                </c:if>

                <c:if test="${requestCurrentPage == 1 && requestNumberOfPages > 1}">
                    <li class="page-item disabled">
                                <span class="page-link">
                                    <span aria-hidden="true">&laquo;</span>
                                </span>
                    </li>
                </c:if>

                <c:forEach begin="1" end="${requestNumberOfPages}" var="i">

                    <c:if test="${i <= requestCurrentPage + 4 && i >= requestCurrentPage - 4}">
                        <c:choose>
                            <c:when test="${requestCurrentPage == i}">
                                <li class="page-item disabled">
                                            <span class="page-link ">
                                                    ${i}
                                            </span>
                                </li>
                            </c:when>

                            <c:otherwise>
                                <li class="page-item">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/booking/request/all?page=${i}">
                                            ${i}
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </c:forEach>

                <c:if test="${requestCurrentPage < requestNumberOfPages}">
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/booking/request/all?page=${requestCurrentPage + 1}">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </c:if>

                <c:if test="${requestCurrentPage == requestNumberOfPages && requestCurrentPage != 1}">
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
                    <th class="col"
                        scope="col">
                        <fmt:message key="request.all.label.request" />
                    </th>

                    <th class="col"
                        scope="col">
                        <fmt:message key="request.all.label.room" />
                    </th>

                    <th class="col"
                        scope="col">
                        <fmt:message key="request.all.label.period" />
                    </th>

                    <th class="col-1"
                        scope="col">
                        <fmt:message key="request.all.label.is.processed" />
                    </th>

                    <th class="col-1"
                        scope="col">
                        <fmt:message key="request.all.label.action" />
                    </th>
                </tr>
            </thead>

            <tbody>
                <c:forEach var="request" items="${allRequests}">
                    <tr>
                        <td>
                            <p>
                                <fmt:message key="request.all.label.hotel.room.date"/> :
                                <tags:localDate date="${request.date}" pattern="dd.MM.yyyy" />
                            </p>

                            <p>
                                <fmt:message key="request.all.label.id"/> :
                                ${request.requestId}-<tags:localDate date="${request.date}" pattern="dd.MM.yyyy" />
                            </p>

                            <p>

                            </p>

                            <c:if test="${userRole != Role.CUSTOMER}">
                                <p>
                                    <fmt:message key="request.all.label.email"/> :
                                    <c:out value="${request.email}"/>
                                </p>
                            </c:if>
                        </td>

                        <td>
                            <p>
                                <fmt:message key="request.all.label.hotel.room.class" /> :
                                <c:out value="${request.hotelRoomClassName}"/>
                            </p>


                            <p>
                                <fmt:message key="request.all.label.number.guests" /> :
                                <c:out value="${request.numberOfGuests}"/>
                            </p>
                        </td>

                        <td>
                            <p>
                                <fmt:message key="request.all.label.checkin" /> :
                                <tags:localDate date="${request.checkIn}" pattern="dd.MM.yyyy" /> -
                                <fmt:message key="request.all.label.checkout" /> :
                                <tags:localDate date="${request.checkOut}" pattern="dd.MM.yyyy" />
                            </p>
                        </td>

                        <td>
                            <c:if test="${request.isProcessed == true}" >
                                <fmt:message key="request.all.label.is.processed.true" />
                            </c:if>
                            <c:if test="${request.isProcessed == false}" >
                                <fmt:message key="request.all.label.is.processed.false" />
                            </c:if>
                        </td>

                        <c:if test="${userRole == Role.CUSTOMER}">
                            <td >
                                <c:if test="${request.isProcessed == false}" >
                                    <button class="btn btn-outline-secondary"
                                            type="button"
                                            data-bs-toggle="modal"
                                            data-bs-target="#deleteModal${request.requestId}">
                                        <fmt:message key="request.all.button.delete" />
                                    </button>

                                    <div class="modal fade"
                                         id="deleteModal${request.requestId}"
                                         tabindex="-1"
                                         aria-labelledby="deleteModalLabel${request.requestId}"
                                         aria-hidden="true">
                                        <div class="modal-dialog modal-dialog-centered">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title"
                                                        id="deleteModalLabel${request.requestId}">
                                                        <fmt:message key="request.all.delete.modal.title" />
                                                    </h5>

                                                    <button type="button"
                                                            class="btn-close"
                                                            data-bs-dismiss="modal"
                                                            aria-label="Close">
                                                    </button>
                                                </div>

                                                <div class="modal-body text-start">
                                                    <h6><fmt:message key="request.all.delete.title" /></h6>
                                                </div>

                                                <div class="modal-footer">
                                                    <button class="btn btn-outline-secondary"
                                                            type="button"
                                                            data-bs-dismiss="modal">

                                                        <fmt:message key="request.all.button.close" />
                                                    </button>

                                                    <form id="request${request.requestId}"
                                                          action="${pageContext.request.contextPath}/booking/request/delete"
                                                          method="post">
                                                        <input type="hidden"
                                                               name="requestId"
                                                               value="${request.requestId}">

                                                        <button class="btn btn-outline-dark"
                                                                form="request${request.requestId}">
                                                            <fmt:message key="request.all.button.delete"/>
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                            </td>
                        </c:if>

                        <c:if test="${userRole == Role.ADMIN || userRole == Role.MANAGER}">
                            <td >
                                <c:if test="${request.isProcessed == false}" >
                                    <a class="btn btn-outline-dark"
                                       href="${pageContext.request.contextPath}/booking/selection?requestId=${request.requestId}">
                                        <fmt:message key="request.all.button.handle"/>
                                    </a>
                                </c:if>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>

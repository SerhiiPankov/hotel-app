<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import="hotel.model.enums.PaymentStatus" %>
<%@ page import="hotel.util.Constant" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8>

    <title>Booking</title>
</head>

<body class="align-items-center">

    <%@include file="../resources/nav.jsp"%>

    <div class="container-sm" >
        <% if (request.getSession().getAttribute(Constant.SESSION_ATTRIBUTE_USER_ROLE) == null
                || request.getSession().getAttribute(Constant.SESSION_ATTRIBUTE_USER_ROLE).equals(Role.MANAGER)
                || request.getSession().getAttribute(Constant.SESSION_ATTRIBUTE_USER_ROLE).equals(Role.ADMIN)) { %>
            <h3 class="text-center"><fmt:message key="booking.all.title"/></h3>
        <% } else { %>
            <h3 class="text-center"><fmt:message key="booking.all.title.customer"/></h3>
        <% } %>
    </div>

    <div class="container-xl">
        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.ACCESS_ERROR_MSG)) {%>
            <h6 class="text-center"
                style="color: red">
                <fmt:message key="booking.all.error"/>
            </h6>
        <%}%>
    </div>

    <div class="container-xl align-items-center">
        <form action="${pageContext.request.contextPath}/booking/all"
              method="post">
            <label for="filterBooking">
                <fmt:message key="booking.all.sort.label"/>
            </label>

            <select class="form-select form-select-sm w-25"
                    aria-label=".form-select-lg example"
                    id="filterBooking"
                    name="filterBooking"
                    onchange="submit()">
                <c:choose>
                    <c:when test="${userRole == Role.ADMIN || userRole == Role.MANAGER}">
                        <option <c:if test="${filterBooking == 1}" >selected</c:if> value=1>
                            <fmt:message key="booking.all.sort.label.wait"/>
                        </option>

                        <option <c:if test="${filterBooking == 2}" >selected</c:if> value=2>
                            <fmt:message key="booking.all.sort.label.invoice"/>
                        </option>

                        <option <c:if test="${filterBooking == 3}" >selected</c:if> value=3>
                            <fmt:message key="booking.all.sort.label.proposal"/>
                        </option>
                    </c:when>

                    <c:when test="${userRole == Role.CUSTOMER}">
                        <option <c:if test="${filterBooking == 2}" >selected</c:if> value=2>
                            <fmt:message key="booking.all.sort.label.invoice"/>
                        </option>

                        <option <c:if test="${filterBooking == 3}" >selected</c:if> value=3>
                            <fmt:message key="booking.all.sort.label.proposal"/>
                        </option>

                        <option <c:if test="${filterBooking == 1}" >selected</c:if> value=1>
                            <fmt:message key="booking.all.sort.label.wait"/>
                        </option>
                    </c:when>
                </c:choose>

                <option <c:if test="${filterBooking == 4}" >selected</c:if> value=4>
                    <fmt:message key="booking.all.sort.label.paid"/>
                </option>

                <option <c:if test="${filterBooking == 5}" >selected</c:if> value=5>
                    <fmt:message key="booking.all.sort.label.delete"/>
                </option>
            </select>
        </form>
    </div>

    <div class="container-sm align-items-center">
        <nav aria-label="...">
            <ul class="pagination justify-content-center">
                <c:if test="${bookingCurrentPage != 1 && bookingNumberOfPages > 1}">
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/booking/all?page=${bookingCurrentPage - 1}">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                </c:if>

                <c:if test="${bookingCurrentPage == 1 && bookingNumberOfPages > 1}">
                    <li class="page-item disabled">
                                <span class="page-link">
                                    <span aria-hidden="true">&laquo;</span>
                                </span>
                    </li>
                </c:if>

                <c:forEach begin="1" end="${bookingNumberOfPages}" var="i">

                    <c:if test="${i <= bookingCurrentPage + 4 && i >= bookingCurrentPage - 4}">
                        <c:choose>
                            <c:when test="${bookingCurrentPage == i}">
                                <li class="page-item disabled">
                                            <span class="page-link ">
                                                    ${i}
                                            </span>
                                </li>
                            </c:when>

                            <c:otherwise>
                                <li class="page-item">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/booking/all?page=${i}">
                                            ${i}
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </c:forEach>

                <c:if test="${bookingCurrentPage < bookingNumberOfPages}">
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/booking/all?page=${bookingCurrentPage + 1}">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </c:if>

                <c:if test="${bookingCurrentPage == bookingNumberOfPages && bookingNumberOfPages != 1}">
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
                    <th scope="col">
                        <fmt:message key="booking.all.booking"/>
                    </th>

                    <th scope="col">
                        <fmt:message key="booking.all.hotel.room"/>
                    </th>

                    <th scope="col">
                        <fmt:message key="booking.all.period"/>
                    </th>

                    <th class="col-1"
                        scope="col">
                        <fmt:message key="booking.all.status"/>
                    </th>

                    <th class="col-1"
                        scope="col">
                        <fmt:message key="booking.all.action"/>
                    </th>
                </tr>
            </thead>

            <tbody class="table-group-divider">
                <c:forEach var="booking" items="${allBookings}">
                    <tr>
                        <td>
                            <p>
                                <fmt:message key="booking.all.date"/> :
                                <tags:localDate date="${booking.date}" pattern="dd.MM.yyyy" />
                            </p>

                            <p>
                                <c:choose>
                                    <c:when test="${booking.paymentStatus == PaymentStatus.INVOICE
                                        || booking.paymentStatus == PaymentStatus.PAID}">
                                        <fmt:message key="booking.all.invoice"/>
                                    </c:when>

                                    <c:otherwise>
                                        <fmt:message key="booking.all.id"/>
                                    </c:otherwise>
                                </c:choose>
                                №
                                ${booking.id}-<tags:localDate date="${booking.date}" pattern="dd.MM.yyyy" />
                            </p>
                            <c:if test="${userRole != Role.CUSTOMER}">
                                <p>
                                    <fmt:message key="booking.all.user"/> :
                                    ${booking.email}
                                </p>
                            </c:if>
                        </td>

                        <td>
                            <p>
                                <fmt:message key="booking.all.room.number"/> :
                                ${booking.number}
                            </p>

                            <p>
                                <fmt:message key="booking.all.room.class"/> :
                                ${booking.apartmentClassName}
                            </p>

                            <p>
                                <fmt:message key="booking.all.number.guests"/> :
                                ${booking.numberOfGuests}
                            </p>
                        </td>

                        <td>
                            <p>
                                <fmt:message key="booking.all.checkin"/> :
                                <tags:localDate date="${booking.checkin}" pattern="dd.MM.yyyy" /> -
                                <fmt:message key="booking.all.checkout"/> :
                                <tags:localDate date="${booking.checkout}" pattern="dd.MM.yyyy" />
                            </p>

                            <p>
                                <fmt:message key="booking.all.total.price"/> :
                                    ${booking.total}
                            </p>
                        </td>

                        <td>
                            <c:if test="${booking.paymentStatus == PaymentStatus.WAIT}">
                                <fmt:message key="booking.all.status.wait"/>
                            </c:if>

                            <c:if test="${booking.paymentStatus == PaymentStatus.PAID}">
                                <fmt:message key="booking.all.status.paid"/>
                            </c:if>

                            <c:if test="${booking.paymentStatus == PaymentStatus.PROPOSAL}">
                                <fmt:message key="booking.all.status.proposal"/>
                            </c:if>

                            <c:if test="${booking.paymentStatus == PaymentStatus.INVOICE}">
                                <fmt:message key="booking.all.status.invoice"/>
                            </c:if>
                        </td>

                        <td>
                            <c:if test="${userRole == Role.ADMIN || userRole == Role.MANAGER}">
                                <c:if test="${booking.paymentStatus == PaymentStatus.WAIT}">
                                    <button class="btn btn-outline-secondary"
                                            type="button"
                                            data-bs-toggle="modal"
                                            data-bs-target="#waitModal${booking.id}">
                                        <fmt:message key="booking.button.wait"/>
                                    </button>

                                    <div class="modal fade"
                                         id="waitModal${booking.id}"
                                         tabindex="-1"
                                         aria-labelledby="waitModalLabel${booking.id}"
                                         aria-hidden="true">
                                        <div class="modal-dialog modal-dialog-centered">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="waitModalLabel${booking.id}">
                                                        <fmt:message key="booking.wait.modal.title"/>
                                                    </h5>

                                                    <button type="button"
                                                            class="btn-close"
                                                            data-bs-dismiss="modal"
                                                            aria-label="Close">
                                                    </button>
                                                </div>

                                                <div class="modal-body text-start">
                                                    <h6>
                                                        <fmt:message key="booking.wait.modal.text"/>
                                                    </h6>
                                                </div>

                                                <div class="modal-footer">
                                                    <button class="btn btn-outline-secondary"
                                                            type="button"
                                                            data-bs-dismiss="modal">
                                                        <fmt:message key="booking.wait.modal.button.close"/>
                                                    </button>

                                                    <form id="wait"
                                                          action="${pageContext.request.contextPath}/booking/invoice"
                                                          method="post">
                                                        <input type="hidden" name="bookingId" value="${booking.id}">
                                                    </form>

                                                    <button class="btn btn-outline-dark"
                                                            form="wait">
                                                        <fmt:message key="booking.wait.modal.button.wait"/>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <c:if test="${booking.paymentStatus == PaymentStatus.INVOICE}">
                                    <button class="btn btn-outline-secondary"
                                            type="button"
                                            data-bs-toggle="modal"
                                            data-bs-target="#invoiceModal${booking.id}">
                                        <fmt:message key="booking.all.button.set.paid"/>
                                    </button>

                                    <div class="modal fade"
                                         id="invoiceModal${booking.id}"
                                         tabindex="-1"
                                         aria-labelledby="invoiceModalLabel${booking.id}"
                                         aria-hidden="true">
                                        <div class="modal-dialog modal-dialog-centered">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title"
                                                        id="invoiceModalLabel${booking.id}">
                                                        <fmt:message key="booking.set.paid.modal.title"/>
                                                    </h5>

                                                    <button type="button"
                                                            class="btn-close"
                                                            data-bs-dismiss="modal"
                                                            aria-label="Close">
                                                    </button>
                                                </div>

                                                <div class="modal-body text-start">
                                                    <h6>
                                                        <fmt:message key="booking.set.paid.modal.text"/>
                                                    </h6>
                                                </div>

                                                <div class="modal-footer">
                                                    <button class="btn btn-outline-secondary"
                                                            type="button"
                                                            data-bs-dismiss="modal">
                                                        <fmt:message key="booking.set.paid.modal.button.close"/>
                                                    </button>

                                                    <form id="invoice${booking.id}"
                                                          action="${pageContext.request.contextPath}/booking/pay"
                                                          method="post">
                                                        <input type="hidden"
                                                               name="bookingId"
                                                               value="${booking.id}">

                                                        <button class="btn btn-outline-dark"
                                                                form="invoice${booking.id}">
                                                            <fmt:message key="booking.set.paid.modal.button.set.paid"/>
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                            </c:if>

                            <c:if test="${userRole == Role.CUSTOMER}">
                                <c:if test="${booking.paymentStatus == PaymentStatus.PROPOSAL}">
                                    <button class="btn btn-outline-secondary"
                                            type="button"
                                            data-bs-toggle="modal"
                                            data-bs-target="#proposalModal${booking.id}">
                                        <fmt:message key="booking.all.button.proposal"/>
                                    </button>

                                    <div class="modal fade"
                                         id="proposalModal${booking.id}"
                                         tabindex="-1"
                                         aria-labelledby="proposalModalLabel${booking.id}"
                                         aria-hidden="true">
                                        <div class="modal-dialog modal-dialog-centered">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title"
                                                        id="proposalModalLabel${booking.id}">
                                                        <fmt:message key="booking.proposal.modal.title"/>
                                                    </h5>

                                                    <button type="button"
                                                            class="btn-close"
                                                            data-bs-dismiss="modal"
                                                            aria-label="Close">
                                                    </button>
                                                </div>

                                                <div class="modal-body text-start">
                                                    <h6>
                                                        <fmt:message key="booking.proposal.modal.text"/>
                                                    </h6>
                                                </div>

                                                <div class="modal-footer">
                                                    <button class="btn btn-outline-secondary"
                                                            type="button"
                                                            data-bs-dismiss="modal">
                                                        <fmt:message key="booking.proposal.modal.button.close"/>
                                                    </button>

                                                    <form id="proposal${booking.id}"
                                                          action="${pageContext.request.contextPath}/booking/accept"
                                                          method="post">
                                                        <input type="hidden"
                                                               name="bookingId"
                                                               value="${booking.id}">

                                                        <button class="btn btn-outline-dark"
                                                                form="proposal${booking.id}">
                                                            <fmt:message key="booking.proposal.modal.button.proposal"/>
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <c:if test="${booking.paymentStatus == PaymentStatus.INVOICE}">
                                    <button class="btn btn-outline-secondary"
                                            type="button"
                                            data-bs-toggle="modal"
                                            data-bs-target="#invoiceModal${booking.id}">
                                        <fmt:message key="booking.all.button.invoice"/>
                                    </button>

                                    <div class="modal fade"
                                         id="invoiceModal${booking.id}"
                                         tabindex="-1"
                                         aria-labelledby="invoiceModalLabel${booking.id}"
                                         aria-hidden="true">
                                        <div class="modal-dialog modal-dialog-centered">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="invoiceModalLabel${booking.id}">
                                                        <fmt:message key="booking.invoice.modal.title"/>
                                                    </h5>

                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>

                                                <div class="modal-body text-start">
                                                    <h6>
                                                        <fmt:message key="booking.invoice.modal.text"/>
                                                    </h6>
                                                </div>

                                                <div class="modal-footer">
                                                    <button class="btn btn-outline-secondary"
                                                            type="button"
                                                            data-bs-dismiss="modal">
                                                        <fmt:message key="booking.invoice.modal.button.close"/>
                                                    </button>

                                                    <form id="invoice${booking.id}" action="${pageContext.request.contextPath}/booking/pay" method="post">
                                                        <input type="hidden" name="bookingId" value="${booking.id}">

                                                        <button class="btn btn-outline-dark"
                                                                form="invoice${booking.id}">
                                                            <fmt:message key="booking.invoice.modal.button.invoice"/>
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <a class="btn btn-outline-dark"
                                       href="${pageContext.request.contextPath}/download?bookingId=${booking.id}">
                                        <fmt:message key="booking.pdf.download"/>
                                    </a>
                                </c:if>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>

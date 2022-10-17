<%@ page import="hotel.util.Constant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8>

    <title>Manage schedule</title>

    <script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>

    <script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>

    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>

    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
</head>

<body class="align-items-center">

    <%@include file="../resources/nav.jsp"%>

    <div class="container-sm" >
        <h3 class="text-center">
            <fmt:message key="schedule.title" />
        </h3>
    </div>

    <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
            && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.CREATE_ERROR_MSG)) {%>
        <h6 class="text-center"
            style="color: red">
            <fmt:message key="schedule.error.create.message"/>
        </h6>
    <%}%>

    <div class="container-sm  align-items-center w-50">
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th class="col-1"
                        scope="col">
                        <fmt:message key="schedule.label.room.number" />
                    </th>

                    <th class="col-1"
                        scope="col">
                        <fmt:message key="schedule.label.room.class" />
                    </th>

                    <th class="col-1"
                        scope="col">
                        <fmt:message key="schedule.label.number.guests" />
                    </th>
                </tr>
            </thead>

            <tbody class="table-group-divider">
                <tr>
                    <td>
                        ${hotelRoom.number}
                    </td>

                    <td>
                        ${hotelRoom.hotelRoomClassName}
                    </td>

                    <td>
                        ${hotelRoom.numberOfGuests}
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="container-xl">
        <div class="row">
            <c:if test="${hotelRoom.isDeleted == false}">
                <div class="col-3 align-items-center">
                    <form method="post"
                          id="schedule"
                          autocomplete="off"
                          action="${pageContext.request.contextPath}/set/schedule">

                        <input type="hidden"
                               name="csrfSalt"
                               value="<c:out value='${csrfSalt}'/>"/>

                        <div class="mb-3">
                            <label class="form-label"
                                   for="dateRange">
                                <fmt:message key="schedule.label.date.range"/>
                            </label>

                            <input class="form-control form-control-solid"
                                   name="dateRange"
                                   id="dateRange"
                                   oninput="this.setCustomValidity('')"
                                   oninvalid="this.setCustomValidity('<fmt:message key="schedule.validate.message.date"/>')"/>
                        </div>

                        <script type="text/javascript">
                            $(function() {
                                $("#dateRange").daterangepicker({
                                    minDate: moment(),
                                    autoUpdateInput: false,
                                    autoApply: true,
                                    locale: {
                                        cancelLabel: 'Clear'
                                    }
                                });

                                $("#dateRange").on('apply.daterangepicker', function(ev, picker) {
                                    $(this).val(picker.startDate.format('DD.MM.YYYY') + ' - ' + picker.endDate.format('DD.MM.YYYY'));
                                });

                                $("#dateRange").on('cancel.daterangepicker', function(ev, picker) {
                                    $(this).val('');
                                });
                            });
                        </script>

                        <div class="mb-3">
                            <label class="form-label"
                                   for="weekdayPrice">
                                <fmt:message key="schedule.label.weekday.price" />
                            </label>

                            <input class="form-control"
                                   id="weekdayPrice"
                                   name="weekdayPrice"
                                   type="number"
                                   step="0.01"
                                   min="0.01"
                                   required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label"
                                   for="holidayPrice">
                                <fmt:message key="schedule.label.holiday.price" />
                            </label>

                            <input class="form-control"
                                   id="holidayPrice"
                                   name="holidayPrice"
                                   type="number"
                                   step="0.01"
                                   min="0.01"
                                   required>
                        </div>
                    </form>

                    <div class="mb-3 text-center">
                        <button class="btn btn-outline-dark"
                                type="submit"
                                form="schedule">
                            <fmt:message key="schedule.button.confirm" />
                        </button>
                    </div>
                </div>
            </c:if>

            <div class="col align-items-center">
                <nav aria-label="...">
                    <ul class="pagination justify-content-center">
                        <c:if test="${currentPage != 1 && numberOfPages > 1}">
                            <li class="page-item">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/rooms/schedule?hotelRoomId=${hotelRoom.id}&page=${currentPage - 1}">
                                    <span aria-hidden="true">&laquo;</span>
                                </a>
                            </li>
                        </c:if>

                        <c:if test="${currentPage == 1 && numberOfPages > 1}">
                            <li class="page-item disabled">
                                <span class="page-link">
                                    <span aria-hidden="true">&laquo;</span>
                                </span>
                            </li>
                        </c:if>

                        <c:forEach begin="1" end="${numberOfPages}" var="i">

                            <c:if test="${i <= currentPage + 4 && i >= currentPage - 4}">
                                <c:choose>
                                    <c:when test="${currentPage == i}">
                                        <li class="page-item disabled">
                                            <span class="page-link ">
                                                    ${i}
                                            </span>
                                        </li>
                                    </c:when>

                                    <c:otherwise>
                                        <li class="page-item">
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/rooms/schedule?hotelRoomId=${hotelRoom.id}&page=${i}">
                                                    ${i}
                                            </a>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </c:forEach>

                        <c:if test="${currentPage < numberOfPages}">
                            <li class="page-item">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/rooms/schedule?hotelRoomId=${hotelRoom.id}&page=${currentPage + 1}">
                                    <span aria-hidden="true">&raquo;</span>
                                </a>
                            </li>
                        </c:if>

                        <c:if test="${currentPage == numberOfPages}">
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
                        <th class="col-1"
                            scope="col">
                            <fmt:message key="schedule.label.date" />
                        </th>

                        <th class="col-1"
                            scope="col">
                            <fmt:message key="schedule.label.day.week" />
                        </th>

                        <th class="col-1"
                            scope="col">
                            <fmt:message key="schedule.label.price" />
                        </th>

                        <th class="col-1"
                            scope="col">
                            <fmt:message key="schedule.label.status" />
                        </th>
                    </tr>
                    </thead>

                    <tbody class="table-group-divider">
                        <c:forEach var="schedule" items="${schedules}">
                            <tr>
                                <td>
                                    ${schedule.day}
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${schedule.day.getDayOfWeek() == 'MONDAY'}">
                                            <fmt:message key="schedule.monday" />
                                        </c:when>

                                        <c:when test="${schedule.day.getDayOfWeek() == 'TUESDAY'}">
                                            <fmt:message key="schedule.tuesday" />
                                        </c:when>

                                        <c:when test="${schedule.day.getDayOfWeek() == 'WEDNESDAY'}">
                                            <fmt:message key="schedule.wednesday" />
                                        </c:when>

                                        <c:when test="${schedule.day.getDayOfWeek() == 'THURSDAY'}">
                                            <fmt:message key="schedule.thursday" />
                                        </c:when>

                                        <c:when test="${schedule.day.getDayOfWeek() == 'FRIDAY'}">
                                            <fmt:message key="schedule.friday" />
                                        </c:when>

                                        <c:when test="${schedule.day.getDayOfWeek() == 'SATURDAY'}">
                                            <fmt:message key="schedule.saturday" />
                                        </c:when>

                                        <c:when test="${schedule.day.getDayOfWeek() == 'SUNDAY'}">
                                            <fmt:message key="schedule.sunday" />
                                        </c:when>
                                    </c:choose>
                                </td>

                                <td>
                                    ${schedule.price}
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${schedule.bookingStatus == 'FREE'}">
                                            <fmt:message key="schedule.free" />
                                        </c:when>

                                        <c:when test="${schedule.bookingStatus == 'BOOK'}">
                                            <fmt:message key="schedule.book" />
                                        </c:when>

                                        <c:when test="${schedule.bookingStatus == 'BUSY'}">
                                            <fmt:message key="schedule.busy" />
                                        </c:when>

                                        <c:when test="${schedule.bookingStatus == 'OFFER'}">
                                            <fmt:message key="schedule.offer" />
                                        </c:when>

                                        <c:when test="${schedule.bookingStatus == 'ACCEPT'}">
                                            <fmt:message key="schedule.accept" />
                                        </c:when>

                                        <c:when test="${schedule.bookingStatus == 'UNAVAILABLE'}">
                                            <fmt:message key="schedule.unavailable" />
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>

<%@ page import="hotel.util.Constant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8>

    <title>Create request</title>
</head>

<body class="align-items-center">

    <%@include file="../resources/nav.jsp"%>

    <div class="container-sm" >
        <h3 class="text-center">
            <fmt:message key="selected.title"/>
        </h3>
    </div>

    <div class="container-sm align-items-center w-50">
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th class="col-1"
                        scope="col">
                        <fmt:message key="selected.label.checkin"/>
                    </th>

                    <th class="col-1"
                        scope="col">
                        <fmt:message key="selected.label.checkout"/>
                    </th>

                    <th class="col-1"
                        scope="col">
                        <fmt:message key="selected.label.number.guests"/>
                    </th>

                    <th class="col-1"
                        scope="col">
                        <fmt:message key="selected.label.room.class"/>
                    </th>
                </tr>
            </thead>

            <tbody class="table-group-divider">
                <tr>
                    <td>
                        <tags:localDate date="${request.checkIn}" pattern="dd.MM.yyyy"/>
                    </td>

                    <td>
                        <tags:localDate date="${request.checkOut}" pattern="dd.MM.yyyy"/>
                    </td>

                    <td>
                        ${request.numberOfGuests}
                    </td>

                    <td>
                        ${hotelRoomClassName}
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
            && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.FAILED_TO_SEND_NOTIFICATION)) {%>
        <h6 class="text-center"
            style="color: red">
            <fmt:message key="selected.book.failed.send.notification"/>
        </h6>
    <%}%>

    <div class="container-md align-items-center text-center">
        <% if (request.getAttribute(Constant.ATTRIBUTE_MESSAGE) != null
                && request.getAttribute(Constant.ATTRIBUTE_MESSAGE).equals(Constant.ATTRIBUTE_NO_AVAILABLE_ROOM)) {%>
            <h6 class="text-center"
                style="color: red">
                <fmt:message key="selected.book.not.possible.title"/>
            </h6>
            <form action="${pageContext.request.contextPath}/booking/not-possible" method="post">
                <input type="hidden" name="requestId" value="${request.id}">

                <button class="btn btn-outline-dark">
                    <fmt:message key="selected.book.button.not.possible"/>
                </button>
            </form>
        <%} else {%>
            <div class="container-sm" >
                <h3 class="text-center">
                    <fmt:message key="selected.title.book"/>
                </h3>
            </div>

            <div class="container-sm align-items-center w-50">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th class="col-1"
                            scope="col">
                            <fmt:message key="selected.book.label.room.number"/>
                        </th>

                        <th class="col-1"
                            scope="col">
                            <fmt:message key="selected.book.label.room.class"/>
                        </th>

                        <th class="col-1"
                            scope="col">
                            <fmt:message key="selected.book.label.number.guests"/>
                        </th>

                        <th class="col-1"
                            scope="col">
                            <fmt:message key="selected.book.label.action"/>
                        </th>
                    </tr>
                    </thead>

                    <tbody class="table-group-divider">
                        <c:forEach var="hotelRoom" items="${hotelRooms}">
                            <tr>
                                <td>
                                    <c:out value="${hotelRoom.number}"/>
                                </td>

                                <td>
                                    <c:out value="${hotelRoom.hotelRoomClassName}"/>
                                </td>

                                <td>
                                    <c:out value="${hotelRoom.numberOfGuests}"/>
                                </td>

                                <td>
                                    <form action="${pageContext.request.contextPath}/booking/selection" method="post">
                                        <input type="hidden" name="hotelRoomId" value="${hotelRoom.id}">

                                        <button class="btn btn-outline-dark">
                                            <fmt:message key="selected.book.button.offer"/>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        <%}%>
    </div>
</body>
</html>

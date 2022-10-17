<%@ page import="hotel.util.Constant" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8>

    <title>Room management</title>
</head>

<body class="align-items-center">

    <%@include file='../resources/nav.jsp' %>

    <div class="container-xl">
        <h3 class="text-center">
            <fmt:message key="room.title"/>
        </h3>
    </div>

    <div class="container-xl align-items-center">
        <form method="post" id="room" action="${pageContext.request.contextPath}/rooms"></form>

        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th scope="col"
                        class="col-1">
                        <fmt:message key="room.label.number"/>
                    </th>

                    <th scope="col"
                        class="col-1">
                        <fmt:message key="room.label.room.class"/>
                    </th>

                    <th scope="col"
                        class="col-1">
                        <fmt:message key="room.label.max.number.guests"/>
                    </th>

                    <th scope="col"
                        class="col-4">
                        <fmt:message key="room.label.description"/>
                    </th>

                    <th scope="col"
                        class="col-1"></th>

                    <th scope="col"
                        class="col-1">
                        <fmt:message key="room.label.action"/>
                    </th>
                </tr>
            </thead>

            <tbody class="table-group-divider">
                <tr>
                    <td><input type="text"
                               name="hotelRoomNumber"
                               form="room"
                               class="form-control"
                               required>
                    </td>

                    <td>
                        <select name="hotelRoomClassId"
                                form="room"
                                class="form-select"
                                required>

                        <option selected="selected"></option>

                        <c:forEach items="${hotelRoomClasses}" var="hotel_room_class">
                            <option value="${hotel_room_class.id}">${hotel_room_class.name}</option>
                        </c:forEach>
                        </select>
                    </td>

                    <td>
                        <input type="number"
                               name="maxNumberOfGuests"
                               form="room"
                               min="0"
                               class="form-control"
                               required>
                    </td>

                    <td>
                        <input type="text"
                               name="hotelRoomDescription"
                               form="room"
                               class="form-control"
                               required>
                    </td>

                    <td></td>

                    <td>
                        <button class="btn btn-outline-dark w-100"
                                type="submit"
                                form="room">
                            <fmt:message key="room.button.add.update"/>
                        </button>
                    </td>
                </tr>

                <c:forEach var="room" items="${hotelRooms}">
                    <tr>
                        <td>
                            <c:out value="${room.number}"/>
                        </td>

                        <td>
                            <c:out value="${room.hotelRoomClassName}"/>
                        </td>

                        <td>
                            <c:out value="${room.numberOfGuests}"/>
                        </td>

                        <td>
                            <c:out value="${room.description}"/>
                        </td>

                        <td>
                            <a class="btn btn-light w-100" href="${pageContext.request.contextPath}/rooms/schedule?hotelRoomId=${room.id}">
                                <fmt:message key="room.button.schedule"/>
                            </a>
                        </td>

                        <td class="text-center">
                            <c:if test="${room.isDeleted == false}">
                                <form action="${pageContext.request.contextPath}/rooms/delete"
                                      method="post">
                                    <input type="hidden"
                                           name="hotelRoomId"
                                           value="${room.id}">

                                    <button class="btn btn-light w-100">
                                        <fmt:message key="room.button.delete"/>
                                    </button>
                                </form>
                            </c:if>

                            <c:if test="${room.isDeleted == true}">
                                <fmt:message key="room.status.deactivated"/>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.CREATE_ERROR_MSG)) {%>
            <h6 class="text-center"
                style="color: red">
                <fmt:message key="room.error.create.message"/>
            </h6>
        <%}%>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.DELETE_ERROR_MSG)) {%>
            <h6 class="text-center"
                style="color: red">
                <fmt:message key="room.error.delete.message"/>
            </h6>
        <%}%>
    </div>
</body>
</html>

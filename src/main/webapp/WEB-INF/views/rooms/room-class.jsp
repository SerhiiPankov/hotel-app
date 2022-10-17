<%@ page import="hotel.util.Constant" %>
<%@ page pageEncoding="UTF-8" contentType="text/html" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8>

    <title>Room class management</title>
</head>

<body class="align-items-center">

    <%@include file='../resources/nav.jsp' %>

    <div class="container-xl">
        <h3 class="text-center">
            <fmt:message key="room.class.title"/>
        </h3>
    </div>

    <div class="container-xl align-items-center w-50">
        <form method="post"
              id="roomClass"
              action="${pageContext.request.contextPath}/rooms/class"></form>

        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th scope="col"><fmt:message key="room.class.label.name" /></th>

                    <th scope="col"><fmt:message key="room.class.label.description" /></th>

                    <th scope="col"><fmt:message key="room.class.label.action" /></th>
                </tr>
            </thead>

            <tbody class="table-group-divider">
                <tr>
                    <td class="w-10">
                        <input type="text" name="hotelRoomClassName" form="roomClass" class="form-control" required>
                    </td>

                    <td class="w-50">
                        <input type="text" name="hotelRoomClassDescription" form="roomClass" class="form-control" required>
                    </td>

                    <td class="w-25">
                        <button class="btn btn-outline-dark w-100"
                                type="submit"
                                form="roomClass">
                            <fmt:message key="room.class.button.add_update" />
                        </button>
                    </td>
                </tr>

                <c:forEach var="room_class" items="${hotelRoomClasses}">
                    <tr>
                        <td class="w-25">
                            <c:out value="${room_class.name}"/>
                        </td>

                        <td  class="w-50">
                            <c:out value="${room_class.description}"/>
                        </td>

                        <td class="text-center">
                            <c:if test="${room_class.isDeleted == false}">
                                <form action="${pageContext.request.contextPath}/rooms/class/delete"
                                      method="post">
                                    <input type="hidden"
                                           name="hotelRoomClassId"
                                           value="${room_class.id}">

                                    <button class="btn btn-light w-100">
                                        <fmt:message key="room.class.button.deactivate" />
                                    </button>
                                </form>
                            </c:if>

                            <c:if test="${room_class.isDeleted == true}">
                                <fmt:message key="room.class.button.deactivated"/>
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
                <fmt:message key="room.class.error.create.message"/>
            </h6>
        <%}%>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.DELETE_ERROR_MSG)) {%>
            <h6 class="text-center"
                style="color: red">
                <fmt:message key="room.class.error.delete.message"/>
            </h6>
        <%}%>
    </div>
</body>
</html>


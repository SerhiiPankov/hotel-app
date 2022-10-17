<%@ page import="hotel.util.Constant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8>

    <title>Request & Selection</title>

    <script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
</head>

<body class="align-items-center">

    <%@include file="../resources/nav.jsp"%>

    <div class="container-sm" >
       <h3 class="text-center">
           <fmt:message key="request.title"/>
       </h3>
    </div>

    <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
            && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.ACCESS_ERROR_MSG)) {%>
        <h6 class="text-center"
            style="color: red">
            <fmt:message key="request.error.reserve.message"/>
        </h6>
    <%}%>

    <div class="container-sm w-25 align-items-center">
        <form method="post"
              action="${pageContext.request.contextPath}/booking/request/create"
              autocomplete="off">

            <div class="mb-3">
                <label class="form-label"
                       for="dateRange">
                    <fmt:message key="request.form.label.date"/>
                </label>

                <input class="form-control form-control-solid"
                       name="dateRange"
                       id="dateRange">
            </div>

            <script type="text/javascript">
                $(function() {
                    $("#dateRange").daterangepicker({
                        minDate: moment(),
                        autoUpdateInput: false,
                        autoApply: true,
                        locale: {
                            cancelLabel: 'Clear'
                        },
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
                       for="numberOfGuests">
                    <fmt:message key="request.form.label.number.guests"/>
                </label>

                <input class="form-control"
                       id="numberOfGuests"
                       name="numberOfGuests"
                       type="number"
                       min="1"
                       required
                       oninput="this.setCustomValidity('')"
                       oninvalid="this.setCustomValidity('<fmt:message key="request.form.validate.message.number.guests"/>')">
            </div>

            <div class="mb-3">
                <label class="form-label"
                       for="hotelRoomClassId">
                    <fmt:message key="request.form.label.room.class"/>
                </label>

                <select class="form-select"
                        id="hotelRoomClassId"
                        name="hotelRoomClassId"
                        required
                        oninput="this.setCustomValidity('')"
                        oninvalid="this.setCustomValidity('<fmt:message key="request.form.validate.message.room.class"/>')">
                    <option selected="selected"></option>

                    <c:forEach items="${hotelRoomClasses}" var="hotel_room_class">
                        <option value="${hotel_room_class.id}">${hotel_room_class.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-3 text-center">
                <button class="btn btn-outline-dark"
                        type="submit">
                    <fmt:message key="request.button.request"/>
                </button>
            </div>
        </form>
    </div>
</body>
</html>

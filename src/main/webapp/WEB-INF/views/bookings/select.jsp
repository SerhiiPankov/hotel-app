<%@ page import="hotel.util.Constant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8>

    <title>Select room</title>

    <script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>

    <script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>

    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>

    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
</head>

<body class="align-items-center">
    <%@include file='../resources/nav.jsp' %>

    <div class="container-xl">
        <h3 class="text-center">
            <fmt:message key="select.room.title"/>
        </h3>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.ACCESS_ERROR_MSG)) {%>
            <h6 class="text-center"
                style="color: red">
                <fmt:message key="select.room.error.access"/>
            </h6>
        <%}%>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.NO_PARAMETER_ERROR_MSG)) {%>
            <h6 class="text-center"
                style="color: red">
                <fmt:message key="select.room.error.no.parameter"/>
            </h6>
        <%}%>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.BUSY_HOTEL_ROOM_ERROR_MSG)) {%>
            <h6 class="text-center"
                style="color: red">
                <fmt:message key="select.room.busy.message"/>
            </h6>
        <%}%>

        <div class="container-xl align-items-center">
            <div class="mb-3 text-center">
                <button class="btn btn-outline-secondary"
                        type="button"
                        data-bs-toggle="modal"
                        data-bs-target="#selectModal">
                    <fmt:message key="select.room.button"/>
                </button>

                <div class="modal fade"
                     id="selectModal"
                     tabindex="-1"
                     aria-labelledby="selectModalLabel"
                     aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title"
                                    id="selectModalLabel">
                                    <fmt:message key="select.room.modal.title"/>
                                </h5>

                                <button type="button"
                                        class="btn-close"
                                        data-bs-dismiss="modal"
                                        aria-label="Close">
                                </button>
                            </div>

                            <div class="modal-body text-start">
                                <h6>
                                    <fmt:message key="select.room.modal.text"/>
                                </h6>

                                <form id="select"
                                      action="${pageContext.request.contextPath}/booking/select"
                                      method="post">
                                    <div class="mb-3">
                                        <label class="form-label"
                                               for="dateRange">
                                            <fmt:message key="select.room.modal.label.date.range"/>
                                        </label>

                                        <input class="form-control form-control-solid"
                                               name="dateRange"
                                               id="dateRange"
                                               oninput="this.setCustomValidity('')"
                                               oninvalid="this.setCustomValidity('<fmt:message key="select.room.validate.message.date"/>')"/>
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
                                               for="numberOfGuests">
                                            <fmt:message key="select.room.modal.label.number.guests"/>
                                        </label>

                                        <input class="form-control form-control-solid"
                                               name="numberOfGuests"
                                               id="numberOfGuests"
                                               type="number"
                                               min="1"
                                               required
                                               oninput="this.setCustomValidity('')"
                                               oninvalid="this.setCustomValidity('Enter valid number')"/>
                                    </div>
                                </form>
                            </div>

                            <div class="modal-footer">
                                <button class="btn btn-outline-secondary"
                                        type="button"
                                        data-bs-dismiss="modal">
                                    <fmt:message key="select.room.modal.button.close"/>
                                </button>

                                <button class="btn btn-outline-dark"
                                        form="select">
                                    <fmt:message key="select.room.modal.button.search"/>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <c:if test="${fromDateSelect != null
                && toDateSelect !=null && numberOfGuestsSelect != null}">
            <h5 class="text-center">
                <p>
                    <fmt:message key="select.room.text.from"/>
                    <tags:localDate date="${fromDateSelect}" pattern="dd.MM.yyyy"/>
                </p>

                <p>
                    <fmt:message key="select.room.text.to"/>
                    <tags:localDate date="${toDateSelect}" pattern="dd.MM.yyyy"/>
                </p>

                <p>
                    <fmt:message key="select.room.text.numbers.guests"/>
                    ${numberOfGuestsSelect}
                </p>
            </h5>

            <div class="container-xl align-items-center">
                <div class="row">
                    <div class="col-3">
                        <form action="${pageContext.request.contextPath}/booking/select/sort"
                              method="post">
                            <label for="sortRoom">
                                <fmt:message key="room.sort.label"/>
                            </label>

                            <select class="form-select form-select-sm"
                                    aria-label=".form-select-lg example"
                                    id="sortRoom"
                                    name="sortRoom"
                                    onchange="submit()">
                                <option <c:if test="${sortRoom == 1}" >selected</c:if> value=1>
                                    <fmt:message key="room.sort.label.number.person"/>
                                </option>

                                <option <c:if test="${sortRoom == 2}" >selected</c:if> value=2>
                                    <fmt:message key="room.sort.label.number.person.desc"/>
                                </option>

                                <option <c:if test="${sortRoom == 3}" >selected</c:if> value=3>
                                    <fmt:message key="room.sort.label.price.asc"/>
                                </option>

                                <option <c:if test="${sortRoom == 4}" >selected</c:if> value=4>
                                    <fmt:message key="room.sort.label.price.desc"/>
                                </option>

                                <option <c:if test="${sortRoom == 5}" >selected</c:if> value=5>
                                    <fmt:message key="room.sort.label.class.asc"/>
                                </option>

                                <option <c:if test="${sortRoom == 6}" >selected</c:if> value=6>
                                    <fmt:message key="room.sort.label.class.desc"/>
                                </option>
                            </select>
                        </form>
                    </div>

                    <div class="col-8">
                        <br>
                        <nav aria-label="..."
                             id="pagination">
                            <ul class="pagination justify-content-center pagination-sm">
                                <c:if test="${roomCurrentPage != 1 && roomNumberOfPages > 1}">
                                    <li class="page-item">
                                        <a class="page-link"
                                           href="${pageContext.request.contextPath}/booking/select?page=${roomCurrentPage - 1}">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>
                                </c:if>

                                <c:if test="${roomCurrentPage == 1 && roomNumberOfPages > 1}">
                                    <li class="page-item disabled">
                                        <span class="page-link">
                                            <span aria-hidden="true">&laquo;</span>
                                        </span>
                                    </li>
                                </c:if>

                                <c:forEach begin="1" end="${roomNumberOfPages}" var="i">

                                    <c:if test="${i <= roomCurrentPage + 4 && i >= roomCurrentPage - 4}">
                                        <c:choose>
                                            <c:when test="${roomCurrentPage == i}">
                                                <li class="page-item disabled">
                                                    <span class="page-link ">
                                                            ${i}
                                                    </span>
                                                </li>
                                            </c:when>

                                            <c:otherwise>
                                                <li class="page-item">
                                                    <a class="page-link"
                                                       href="${pageContext.request.contextPath}/booking/select?page=${i}">
                                                            ${i}
                                                    </a>
                                                </li>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </c:forEach>

                                <c:if test="${roomCurrentPage < roomNumberOfPages}">
                                    <li class="page-item">
                                        <a class="page-link"
                                           href="${pageContext.request.contextPath}/booking/select?page=${roomCurrentPage + 1}">
                                            <span aria-hidden="true">&raquo;</span>
                                        </a>
                                    </li>
                                </c:if>

                                <c:if test="${roomCurrentPage == roomNumberOfPages && roomNumberOfPages != 1}">
                                    <li class="page-item">
                                        <span class="page-link disabled">
                                            <span aria-hidden="true">&raquo;</span>
                                        </span>
                                    </li>
                                </c:if>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>

            <div class="container-xl align-items-center">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th scope="col"
                            class="col-1 text-center">
                            <fmt:message key="select.room.label.number"/>
                        </th>

                        <th scope="col"
                            class="col-1 text-center">
                            <fmt:message key="select.room.label.class"/>
                        </th>

                        <th scope="col"
                            class="col-1 text-center">
                            <fmt:message key="select.room.label.max.number.guests"/>
                        </th>

                        <th scope="col"
                            class="col-4 text-center">
                            <fmt:message key="select.room.label.description"/>
                        </th>

                        <th scope="col"
                            class="col-1 text-center">
                            <fmt:message key="select.room.label.description"/>
                        </th>

                        <th scope="col"
                            class="col-1 text-center">
                            <fmt:message key="select.room.label.action"/>
                        </th>
                    </tr>
                    </thead>

                    <tbody class="table-group-divider">
                    <c:forEach var="hotelRoom" items="${hotelRooms}">
                        <tr>
                            <td class="text-center">
                                <c:out value="${hotelRoom.number}"/>
                            </td>

                            <td class="text-center">
                                <c:out value="${hotelRoom.hotelRoomClassName}"/>
                            </td>

                            <td class="text-center">
                                <c:out value="${hotelRoom.numberOfGuests}"/>
                            </td>

                            <td class="text-center">
                                <c:out value="${hotelRoom.description}"/>
                            </td>

                            <td class="text-center">
                                <c:out value="${hotelRoom.totalPrice}"/>
                            </td>

                            <td>
                                <div class="container-sm text-center">
                                    <form id="reserve"
                                          method="post"
                                          action="${pageContext.request.contextPath}/booking/create">
                                        <input type="hidden" name="hotelRoomId" value="${hotelRoom.id}">
                                    </form>

                                    <button form="reserve"
                                            class="btn btn-outline-dark"
                                            type="submit">
                                        <fmt:message key="select.room.button.reserve"/>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>
</body>
</html>

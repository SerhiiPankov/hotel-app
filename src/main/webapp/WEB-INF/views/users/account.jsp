<%@ page import="hotel.util.Constant" %>
<%@ page contentType="text/html" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8>

    <title>Personal account</title>

    <style><%@include file='../css/bootstrap.min.css' %></style>
</head>

<body class="align-items-center">

    <%@include file="../resources/nav.jsp"%>

    <div class="container-sm" >
        <h3 class="text-center"><fmt:message key="account.title"/></h3>
    </div>

    <div class="container-sm w-25 align-items-center">
        <form method="post"
              id="update_form"
              autocomplete="off"
              action="${pageContext.request.contextPath}/users/account/update">
            <input type="hidden"
                   name="csrfSalt"
                   value="<c:out value='${csrfSalt}'/>"/>

            <div class="mb-3">
                <label class="form-label"
                       for="userEmail">
                    <fmt:message key="account.form.label.email"/>
                </label>

                <input class="form-control"
                       id="userEmail"
                       name="userEmail"
                       type="email"
                       required
                       value="${accountUserEmail}">
            </div>

            <div class="mb-3">
                <label class="form-label"
                       for="userName">
                    <fmt:message key="account.form.label.name"/>
                </label>

                <input class="form-control"
                       id="userName"
                       name="userName"
                       type="text"
                       value="${accountUserName}">
            </div>

            <div class="mb-3">
                <label class="form-label"
                       for="userCountryCode">
                    <fmt:message key="account.form.label.country.code"/>
                </label>

                <input class="form-control"
                       id="userCountryCode"
                       name="userCountryCode"
                       type="text"
                       minlength="2"
                       maxlength="4"
                       required
                       pattern="[0-9]{2,4}"
                       onkeyup="this.value=this.value.replace(/\D/g,'')"
                       onchange="this.value=this.value.replace(/\D/g,'')"
                       oninput="this.setCustomValidity('')"
                       oninvalid="setCustomValidity('<fmt:message key="account.form.error.country.code"/>')"
                       value="${accountUserCountryCode}">
            </div>

            <div class="mb-3">
                <label class="form-label"
                       for="userPhoneNumber">
                    <fmt:message key="account.form.label.phone"/>
                </label>

                <input class="form-control"
                       id="userPhoneNumber"
                       name="userPhoneNumber"
                       type="text"
                       minlength="9"
                       maxlength="9"
                       required
                       pattern="[0-9]{9}"
                       onkeyup="this.value=this.value.replace(/\D/g,'')"
                       onchange="this.value=this.value.replace(/\D/g,'')"
                       oninput="this.setCustomValidity('')"
                       oninvalid="setCustomValidity('<fmt:message key="account.form.error.phone"/>')"
                       value="${accountUserPhoneNumber}">
            </div>


            <div class="mb-3 text-center">
                <button class="btn btn-outline-dark"
                        type="button"
                        data-bs-toggle="modal"
                        data-bs-target="#updateModal">
                    <fmt:message key="account.button.update" />
                </button>

                <div class="modal fade"
                     id="updateModal"
                     tabindex="-1"
                     aria-labelledby="updateModalLabel"
                     aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="updateModalLabel">
                                    <fmt:message key="account.update.modal.title" />
                                </h5>

                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>

                            <div class="modal-body text-start">
                                <h6><fmt:message key="account.update.modal.text" /></h6>

                                <div class="mb-3 align-items-start">
                                    <label class="form-label"
                                           for="updateFormPassword">
                                        <fmt:message key="account.update.modal.label.password"/>
                                    </label>

                                    <input class="form-control"
                                           id="updateFormPassword"
                                           name="updateFormPassword"
                                           type="password"
                                           required
                                           autocomplete="new-password">
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"
                                           for="userNewPasswordUpdate">
                                        <fmt:message key="account.update.modal.label.new.password"/>
                                    </label>

                                    <input class="form-control"
                                           id="userNewPasswordUpdate"
                                           name="userNewPasswordUpdate"
                                           type="password">
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"
                                           for="userNewPasswordRepeatUpdate">
                                        <fmt:message key="account.update.modal.label.new.password.repeat"/>
                                    </label>

                                    <input class="form-control"
                                           id="userNewPasswordRepeatUpdate"
                                           name="userNewPasswordRepeatUpdate"
                                           type="password">
                                </div>
                            </div>

                            <div class="modal-footer">
                                <button class="btn btn-outline-secondary"
                                        type="button"
                                        data-bs-dismiss="modal">
                                    <fmt:message key="account.update.modal.button.close" />
                                </button>

                                <button class="btn btn-outline-dark"
                                        type="submit"
                                        form="update_form"
                                        formaction="${pageContext.request.contextPath}/users/account/update">
                                    <fmt:message key="account.update.modal.button.update" />
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>

        <c:if test="${userRole != Role.ADMIN}">
            <div class="mb-3 text-center">
                <button class="btn btn-outline-secondary"
                        type="button"
                        data-bs-toggle="modal"
                        data-bs-target="#deleteModal">
                    <fmt:message key="account.button.delete" />
                </button>

                <div class="modal fade"
                     id="deleteModal"
                     tabindex="-1"
                     aria-labelledby="deleteModalLabel"
                     aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="deleteModalLabel">
                                    <fmt:message key="account.delete.modal.title" />
                                </h5>

                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>

                            <div class="modal-body text-start">
                                <h6><fmt:message key="account.delete.modal.text" /></h6>

                                <form action="${pageContext.request.contextPath}/users/account/delete"
                                      method="post"
                                      id="user_delete_form">
                                    <input type="hidden"
                                           name="csrfSalt"
                                           value="<c:out value='${csrfSalt}'/>"/>

                                    <div class="mb-3 align-items-start">
                                        <label class="form-label"
                                               for="userPasswordDelete">
                                            <fmt:message key="account.delete.modal.label.password"/>
                                        </label>

                                        <input class="form-control"
                                               id="userPasswordDelete"
                                               name="userPasswordDelete"
                                               type="password"
                                               required
                                               autocomplete="new-password">
                                    </div>
                                </form>
                            </div>

                            <div class="modal-footer">
                                <button class="btn btn-outline-secondary"
                                        type="button"
                                        data-bs-dismiss="modal">
                                    <fmt:message key="account.delete.modal.button.close" />
                                </button>

                                <button class="btn btn-outline-dark"
                                        type="submit"
                                        form="user_delete_form">
                                    <fmt:message key="account.delete.modal.button.delete" />
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.NO_VALID_PASSWORD_ERROR_MSG)) {%>
            <h4 class="text-center"
                style="color: red">
                <fmt:message key="account.wrong.password"/>
            </h4>
        <%}%>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.NO_MATCH_PASSWORDS_ERROR_MSG)) {%>
            <h4 class="text-center"
                style="color: red">
                <fmt:message key="account.not.equal.new.password"/>
            </h4>
        <%}%>

        <% if (request.getParameter(Constant.PARAMETER_MESSAGE) != null
                && request.getParameter(Constant.PARAMETER_MESSAGE).equals(Constant.ACCESS_ERROR_MSG)) {%>
            <h4 class="text-center"
                style="color: red">
                <fmt:message key="account.error.cannot.update.user"/>
            </h4>
        <%}%>
    </div>
</body>
</html>

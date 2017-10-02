<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:url value="/api/get_contacts?page=1" var="getContactsUrl" />
<c:url value="/api/search_contact" var="searchContactUrl">
    <c:param name="submit" value="false" />
</c:url>
<c:url value="/api/send_email_to_contacts" var="sendEmailUrl">
    <c:param name="submit" value="false" />
</c:url>
<c:url value="/api/send_email_to_contacts" var="sendEmailToContactsUrl">
    <c:param name="submit" value="true" />
</c:url>

<html lang="en">
<head>
    <link href="../../css/bootstrap.min.css" rel="stylesheet">
    <link href="../../css/font-awesome.min.css" rel="stylesheet">
    <link href="../../css/navbar.css" rel="stylesheet">
    <link href="../../css/email.css" rel="stylesheet">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>

<!-- Navigation -->
<%@include file="components/contact-navbar.jsp"%>

<div class="container">
    <div class="row">

        <div class="col-lg-8 col-lg-offset-2">
            <div class="panel panel-default text-left">
                <div class="panel-body">

                    <div class="row">
                        <div class="col-md-6">
                            <h2>Sending email </h2><br>
                        </div>
                        <div class="col-md-6 select-template">
                            <div class="form-group">
                                <label for="sel1">Select template:</label>
                                <select class="form-control" id="sel1" onChange="selectTemplate(this.value)">
                                    <option selected>Default</option>
                                    <option>Birthday</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="controls">
                        <div class="email-header">
                          <div class="row">
                            <div class="col-md-6">
                                <label for="form-email-to">To email *</label>
                                <div class="input-group">
                                    <input id="form-email-to" type="email" name="email-to" class="form-control"
                                           placeholder="Please enter email *" required="required"
                                           data-error="Valid email is required.">
                                    <span class="input-group-addon" onclick="addEmailToSendList()">
                                        <i class="fa fa-plus" aria-hidden="true"></i>
                                     </span>
                                </div>
                                <div class="hidden" id="email-error-box">
                                    <div class="alert alert-danger">
                                        <strong>Invalid input data!</strong> check input email address...
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <table class="table emails">
                                    <tbody id="table-body-emails-to-send">
                                    <c:forEach var="email" items="${emailsToSend}">
                                        <tr>
                                            <td class="col-md-11"><h6>${email}</h6></td>
                                            <td class="col-md-1">
                                                <button class="btn btn-sm btn-danger" onclick="removeEmailFromSendList(this)">
                                                    <i class="fa fa-times"></i>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                          </div>
                        </div>

                        <form id="contact-form" method="post" action="${sendEmailToContactsUrl}" role="form">

                            <div class="messages"></div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="form-name">Firstname *</label>
                                        <input id="form-name" type="text" name="firstname" class="form-control" placeholder="Please enter your firstname *" required="required" data-error="Firstname is required.">
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="form-lastname">Lastname *</label>
                                        <input id="form-lastname" type="text" name="lastname" class="form-control" placeholder="Please enter your lastname *" required="required" data-error="Lastname is required.">
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="form-email">Email *</label>
                                        <input id="form-email" type="email" name="email" class="form-control" placeholder="Please enter your email *" required="required" data-error="Valid email is required.">
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="form-phone">Phone</label>
                                        <input id="form-phone" type="tel" name="phone" class="form-control" placeholder="Please enter your phone">
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>

                            <div class="row" id="birthday-form-group" style="visibility: hidden">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="birthday-first-name">Birthday person first name *</label>
                                        <input id="birthday-first-name" type="text" name="birthday-first-name" class="form-control"
                                               placeholder="Please enter first name who celebrates birthday *"
                                               data-error="first name is required.">
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="birthday-last-name">Birthday person last name *</label>
                                        <input id="birthday-last-name" type="text" name="birthday-last-name"
                                               class="form-control" placeholder="Please enter last name who celebrates birthday *"
                                               data-error="last name is required.">
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>
                            <div class ="row">
                                <div class="col-md-6 select-template">
                                    <div class="form-group">
                                        <label for="form-subject">Subject *</label>
                                        <input id="form-subject" type="text" name="subject" class="form-control" placeholder="Please enter subject *" required="required" data-error="Subject is required.">
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                            </div>

                            <div class="hidden">
                                <input type="text" id="emails-to-send" name="emails-to-send">
                                <input type="text" id="email-template" name="email-template" value="Default">
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label for="form-message">Message *</label>
                                        <textarea id="form-message" name="message" class="form-control" placeholder="Message body *" rows="4"
                                                  required="required" data-error="Please,leave us a message."></textarea>
                                        <div class="help-block with-errors"></div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <input type="submit" class="btn btn-success btn-send" value="Send message">
                                </div>
                                <div class="col-md-6">
                                <c:if test="${wasSend == true}">
                                    <div class="alert alert-success">
                                        <strong>Email was successfully sent!</strong>
                                    </div>
                                </c:if>
                                <c:if test="${wasSend == false}">
                                    <div class="alert alert-warning">
                                        <strong>Email wasn't sent!</strong>
                                    </div>
                                </c:if>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <br>
                                    <p class="text-muted"><strong>*</strong> These fields are required.</p>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div><!-- /.8 -->

        </div> <!-- /.row-->
    </div>
</div> <!-- /.container-->
</body>
<script src="../../js/email.js"></script>
</html>

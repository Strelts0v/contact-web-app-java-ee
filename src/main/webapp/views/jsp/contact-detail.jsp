<!DOCTYPE html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<c:url value="/api/get_contacts?offset=0&count=20" var="getContactsUrl" />
<c:url value="/api/update_contact" var="updateContactUrl" />
<c:url value="/api/" var="var2" />
<c:url value="/api/create_contact?contact_submit=" var="createContactUrl"/>

<html lang="en">
<head>
    <link href="../../css/contact-detail.css" rel="stylesheet">
    <link href="../../css/modal.css" rel="stylesheet">
    <link href="../../css/bootstrap.min.css" rel="stylesheet">
    <link href="../../css/font-awesome.min.css" rel="stylesheet">
    <link href="../../css/navbar.css" rel="stylesheet">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>

<!-- Navigation -->
<%@include file="components/contact-navbar.jsp"%>

<div class="container-fluid">
    <div class="row">

        <!-- left side -->
        <div class="col-sm-2 text-center">
            <div class="well">
                <img src="../../pictures/no-person.jpg" class="img-circle" height="150" width="150" alt="Avatar">
            </div>
            <div class="alert alert-success">
                Don't forger to save changes!
            </div>
            <button type="button" class="btn btn-lg btn-success" onclick="updateContact()">
                <i class="fa fa-cloud fa-lg" aria-hidden="true"></i> Save
            </button>
        </div>

        <!-- central side -->
        <div class="col-sm-4">

            <div class="panel panel-default text-left">
                <div class="panel-body">
                    <h3>Contact details</h3>
                </div>
            </div>

            <div class="form-group">
                <input type="hidden" class="form-control" name="id_contact" id="id_contact"
                <c:if test="${not empty contact}">
                       value="${contact.contactId}"
                </c:if>
                >
            </div>

            <div class="form-group">
                <label for="first_name">First name: </label>
                <input type="text" class="form-control" id="first_name" name="first_name"
                <c:if test="${not empty contact}">
                       value="${contact.firstName}"
                </c:if>
                >
            </div>

            <div class="form-group">
                <label for="surname">Surname: </label>
                <input type="text" class="form-control" id="surname" name="surname"
                <c:if test="${not empty contact}">
                       value="${contact.surname}"
                </c:if>
                >
            </div>

            <div class="form-group">
                <label for="patronymic">Patronymic: </label>
                <input type="text" class="form-control" id="patronymic" name="patronymic"
                <c:if test="${not empty contact}">
                       value="${contact.patronymic}"
                </c:if>
                >
            </div>

            <div class="form-group">
                <label for="birthday">Date: </label>
                <input class="form-control" type="date" id="birthday" name="birthday"
                <c:choose>
                <c:when test="${not empty contact}">
                       value="${contact.birthday}"
                </c:when>
                <c:otherwise>
                       value="2011-08-19"
                </c:otherwise>
                </c:choose>
                >
            </div>

            <div class="form-group">
                <label for="gender">Select gender: </label>
                <select class="form-control" id="gender" name="gender">
                    <c:choose>
                        <c:when test="${not empty contact && contact.gender == 'Male' }">
                            <option selected>Male</option>
                            <option>Female</option>
                        </c:when>
                        <c:when test="${not empty contact && contact.gender == 'Female' }">
                            <option>Male</option>
                            <option selected>Female</option>
                        </c:when>
                        <c:otherwise>
                            <option>Male</option>
                            <option>Female</option>
                        </c:otherwise>
                    </c:choose>
                </select>
            </div>

            <div class="form-group">
                <label for="nationality">Nationality: </label>
                <input type="text" class="form-control" id="nationality" name="nationality"
                <c:if test="${not empty contact}">
                       value="${contact.nationality}"
                </c:if>
                >
            </div>

            <div class="form-group">
                <label for="marital_status">Select marital status: </label>
                <select class="form-control" id="marital_status" name="marital_status">
                    <c:choose>
                        <c:when test="${not empty contact && contact.maritalStatus == 'Single' }">
                            <option selected>Single</option>
                            <option>Married</option>
                            <option>In a relationship</option>
                        </c:when>
                        <c:when test="${not empty contact && contact.gender == 'Married' }">
                            <option>Single</option>
                            <option selected>Married</option>
                            <option>In a relationship</option>
                        </c:when>
                        <c:when test="${not empty contact && contact.gender == 'In a relationship' }">
                            <option>Single</option>
                            <option>Married</option>
                            <option selected>In a relationship</option>
                        </c:when>
                        <c:otherwise>
                            <option>Single</option>
                            <option>Married</option>
                            <option>In a relationship</option>
                        </c:otherwise>
                    </c:choose>
                </select>
            </div>

            <div class="form-group">
                <label for="website">Website: </label>
                <input type="text" class="form-control" id="website" name="website"
                <c:if test="${not empty contact}">
                       value="${contact.website}"
                </c:if>
                >
            </div>

            <div class="form-group">
                <label for="email">Email address: </label>
                <input type="email" class="form-control" id="email" name="email"
                <c:if test="${not empty contact}">
                       value="${contact.email}"
                </c:if>
                >
            </div>

            <div class="form-group">
                <label for="company">Company: </label>
                <input type="text" class="form-control" id="company" name="company"
                <c:if test="${not empty contact}">
                       value="${contact.company}"
                </c:if>
                >
            </div>

            <div class="form-group">
                <label for="country">Country: </label>
                <input type="text" class="form-control" id="country" name="country"
                <c:if test="${not empty contact}">
                       value="${contact.address.country}"
                </c:if>
                >
            </div>
            <div class="form-group">
                <label for="city">City: </label>
                <input type="text" class="form-control" id="city" name="city"
                <c:if test="${not empty contact}">
                       value="${contact.address.city}"
                </c:if>
                >
            </div>

            <div class="form-group">
                <label for="address">Address: </label>
                <input type="text" class="form-control" id="address" name="address"
                <c:if test="${not empty contact}">
                       value="${contact.address.address}"
                </c:if>
                >
            </div>

            <div class="form-group">
                <label for="index">Index: </label>
                <input type="text" class="form-control" id="index" name="index"
                <c:if test="${not empty contact}">
                       value="${contact.address.index}"
                </c:if>
                >
            </div>
        </div>

        <div class="col-sm-6 well">

            <div class="thumbnail info-table-area">
                <h3>Attachment table</h3>

                <a href="#" class="btn btn-danger btn-sm control-button" id="" role="button">
                    <i class="fa fa-trash-o fa-lg" aria-hidden="true"></i> Delete
                </a>
                <a href="#" class="btn btn-warning btn-sm control-button" role="button">
                    <i class="fa fa-pencil fa-lg" aria-hidden="true"></i> Edit
                </a>
                <a href="#" class="btn btn-success btn-sm control-button" id="add-attachment-button" role="button">
                    <i class="fa fa-plus" aria-hidden="true"></i> Add
                </a>

                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th>File name</th>
                        <th>Download date</th>
                        <th>Comment</th>
                    </tr>
                    </thead>
                    <tbody id="attachment-table-body">
                    <c:forEach var="attachment" items="${contact.attachments}">
                        <tr>
                            <td>${attachment.fileName}</td>
                            <td>${attachment.downloadDate}</td>
                            <td>${attachment.comment}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="thumbnail info-table-area">
                <h3>Phones table</h3>

                <a href="#" class="btn btn-danger btn-sm control-button" role="button">
                    <i class="fa fa-trash-o fa-lg" aria-hidden="true"></i> Delete
                </a>
                <a href="#" class="btn btn-warning btn-sm control-button" role="button">
                    <i class="fa fa-pencil fa-lg" aria-hidden="true"></i> Edit
                </a>
                <a href="#" class="btn btn-success btn-sm control-button" role="button">
                    <i class="fa fa-plus" aria-hidden="true"></i> Add
                </a>

                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th>Select all</th>
                        <th>Phone number</th>
                        <th>Phone type</th>
                        <th>Comment</th>
                    </tr>
                    </thead>
                    <tbody id="phones-table-body">
                    <c:forEach var="phone" items="${contact.phones}">
                        <tr class="phoneItem">
                            <td><input type="checkbox" value="${phone.phoneId}" /></td>
                            <td>${phone.phoneNumber}</td>
                            <td>${phone.phoneType}</td>
                            <td>${phone.comment}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Modals -->

    <!-- Modal for uploading attachments -->
    <div id="fileUploadModal" class="modal">

        <!-- Modal content -->
        <div class="modal-content">
            <div class="modal-header">
                <span class="close">&times;</span>
                <h2>Select file</h2>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label for="attachment_name">Attachment name: </label>
                    <input type="text" class="form-control" id="attachment_name" name="attachment_name">
                </div>

                <div class="form-group">
                    <label for="comment">Comment: </label>
                    <input type="text" class="form-control" id="comment" name="comment">
                </div>

                <label for="upload_file">Select attachment: </label>
                <input type="file" class="file" id="upload_file" name="upload_file" value="Select file">
            </div>
            <div class="modal-footer">
                <h3>Modal Footer</h3>
                <button type="button" class="btn btn-success btn-md" onclick="addAttachment()">Add</button>
                <button type="button" class="btn btn-danger btn-md" onclick="cancelAttachment()">Cancel</button>
            </div>
        </div>
    </div>

    <hr>

    <c:choose>
        <c:when test="${update_success == true}">
            <div class="alert alert-success" id="update_success">
                <strong>Updating of contact was successful!</strong>
            </div>
        </c:when>
        <c:when test="${update_success == false}">
            <div class="alert alert-danger" id="update_failed">
                <strong>Updating of contact was failed!</strong>
            </div>
        </c:when>
    </c:choose>
</div>
<!-- /.container -->
</body>

<script src="../../js/contact-detail.js"></script>

<html lang="en">
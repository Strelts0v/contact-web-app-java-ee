<!DOCTYPE html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<c:url value="/api/get_contacts?offset=0&count=20" var="getContactsUrl" />
<c:url value="/api/update_contact" var="updateContactUrl" />
<c:url value="/api/" var="var2" />
<c:url value="/api/create_contact?contact_submit=" var="createContactUrl"/>

<c:url value="/upload/add_attachment_to_contact" var="addAttachmentToContactUrl"/>

<html lang="en">
<head>
    <link href="../../css/styles.css" rel="stylesheet">
    <link href="../../css/bootstrap.min.css" rel="stylesheet">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>

<div class="container">
    <!-- Navigation -->
    <%@include file="components/contact-navbar.jsp"%>

    <!-- Content Row -->
    <div class="row">
        <div class="col-md-6">

        <c:choose>
            <c:when test="${update_success == true}">
                <div class="alert alert-success" id="update_success">
                    <strong>Updating of contact was successful!</strong>
                    <script>
                        var id = "update_success";
                        var updateSuccessAlert = document.getElementById(id);
                        window.hidePopupElement(updateSuccessAlert);
                    </script>
                </div>
            </c:when>
            <c:when test="${update_success == false}">
                <div class="alert alert-danger" id="update_failed">
                    <strong>Updating of contact was failed!</strong>
                </div>
            </c:when>
        </c:choose>

        <c:choose>
            <%-- form action url updating contact--%>
            <c:when test="${not empty contact}">
                <form method="POST" action="${updateContactUrl}">
            </c:when>
            <%-- form action url creating contact--%>
            <c:otherwise>
                 <form method="POST" action="/api/create_contact?contact_submit=true">
            </c:otherwise>
        </c:choose>

            <div class="form-group">
                <input type="hidden" class="form-control" name="id_contact"
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

            <button type="submit" class="btn btn-md btn-success">Save</button>
        </form>
        </div>
    </div>
    <!-- /.row -->

    <!-- Table with attachments -->
    <h3>Attachment table</h3>
    <div class="row">
        <a href="#" class="btn btn-danger" role="button">Delete</a>
        <a href="#" class="btn btn-warning" role="button">Edit</a>
        <a href="#" class="btn btn-success" role="button">Create</a>
    </div>
    <div class="row">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>File name</th>
                <th>Download date</th>
                <th>Comment</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="attachment" items="${contact.attachments}">
            <tr>
                <td>${attachment.fileName}</td>
                <td>${attachment.downloadDate}</td>
                <td>${attachment.comment}</td>
            </tr>
            </c:forEach>
        </table>
    </div>

    <form method="post" action="${addAttachmentToContactUrl}" enctype="multipart/form-data">

        <div class="form-group">
            <input type="hidden" class="form-control" name="id_contact"
            <c:if test="${not empty contact}">
                   value="${contact.contactId}"
            </c:if>
            >
        </div>

        <div class="form-group">
            <label for="attachment_name">Attachment name: </label>
            <input type="text" class="form-control" id="attachment_name" name="attachment_name"
            <%--<c:if test="${not empty contact}">--%>
                   <%--value="${contact.address.index}"--%>
            <%--</c:if>--%>
            >
        </div>

        <div class="form-group">
            <label for="comment">Comment: </label>
            <input type="text" class="form-control" id="comment" name="comment"
            <%--<c:if test="${not empty contact}">--%>
            <%--value="${contact.address.index}"--%>
            <%--</c:if>--%>
            >
        </div>

        <label for="upload_file">Select attachment: </label>
        <input type="file" class="file" id="upload_file" name="uploadFile">

        <br/>
        <input type="submit" value="Attach" />
    </form>

    <h3>Phones table</h3>
    <div class="row">
        <a href="#" class="btn btn-danger" role="button">Delete</a>
        <a href="#" class="btn btn-warning" role="button">Edit</a>
        <a href="#" class="btn btn-success" role="button">Create</a>
    </div>
    <div class="row">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>Select all</th>
                <th>Phone number</th>
                <th>Phone type</th>
                <th>Comment</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="phone" items="${contact.phones}">
            <tr>
                <td><input type="checkbox" value="${phone.phoneId}" /></td>
                <td>${phone.phoneNumber}</td>
                <td>${phone.phoneType}</td>
                <td>${phone.comment}</td>
            </tr>
            </c:forEach>
        </table>
    </div>

    <hr>
</div>
<!-- /.container -->

<script>
    function hidePopupElement(elementId) {
        var displayTimeInMillis = 2000;
        var alertElement = document.getElementById(elementId);

        setTimeout(function(alertElement){
            alertElement.style.display = "none";
        }, displayTimeInMillis, alertElement); // <-- time in milliseconds
    }
</script>

<html lang="en">
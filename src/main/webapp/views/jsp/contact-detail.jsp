<!DOCTYPE html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<c:url value="/api/get_contacts?offset=0&count=20" var="getContactsUrl" />
<c:url value="/api/create_contact" var="createContactUrl" />
<c:url value="/api/" var="var2" />

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
        <form method="POST" action="/api/create_contact?contact_submit=true">

            <div class="form-group">
                <label for="first_name">First name: </label>
                <input type="text" class="form-control" id="first_name" name="first_name">
            </div>

            <div class="form-group">
                <label for="surname">Surname: </label>
                <input type="text" class="form-control" id="surname" name="surname">
            </div>

            <div class="form-group">
                <label for="patronymic">Patronymic: </label>
                <input type="text" class="form-control" id="patronymic" name="patronymic">
            </div>

            <div class="form-group">
                <label for="birthday">Date: </label>
                <input class="form-control" type="date" value="2011-08-19" id="birthday" name="birthday">
            </div>

            <div class="form-group">
                <label for="gender">Select gender: </label>
                <select class="form-control" id="gender" name="gender">
                    <option>male</option>
                    <option>female</option>
                </select>
            </div>

            <div class="form-group">
                <label for="nationality">Nationality: </label>
                <input type="text" class="form-control" id="nationality" name="nationality">
            </div>

            <div class="form-group">
                <label for="marital_status">Select marital status: </label>
                <select class="form-control" id="marital_status" name="marital_status">
                    <option>Single</option>
                    <option>Married</option>
                    <option>In a relationship</option>
                </select>
            </div>

            <div class="form-group">
                <label for="website">Website: </label>
                <input type="text" class="form-control" id="website" name="website">
            </div>

            <div class="form-group">
                <label for="email">Email address: </label>
                <input type="email" class="form-control" id="email" name="email">
            </div>

            <div class="form-group">
                <label for="company">Company: </label>
                <input type="text" class="form-control" id="company" name="company">
            </div>

            <div class="form-group">
                <label for="country">Country: </label>
                <input type="text" class="form-control" id="country" name="country">
            </div>
            <div class="form-group">
                <label for="city">City: </label>
                <input type="text" class="form-control" id="city" name="city">
            </div>

            <div class="form-group">
                <label for="address">Address: </label>
                <input type="text" class="form-control" id="address" name="address">
            </div>

            <div class="form-group">
                <label for="index">Index: </label>
                <input type="text" class="form-control" id="index" name="index">
            </div>

            <button type="submit" class="btn btn-lg btn-success">Save</button>
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
<html lang="en">
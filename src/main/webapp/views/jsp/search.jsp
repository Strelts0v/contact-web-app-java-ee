<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:url value="/api/get_contacts?page=1" var="getContactsUrl" />
<c:url value="/api/search_contact" var="searchContactUrl">
    <c:param name="submit" value="false" />
</c:url>
<c:url value="/api/send_email_to_contacts" var="sendEmailUrl">
    <c:param name="submit" value="false" />
</c:url>

<html>
<head>
    <title>Search</title>
    <link href="../../css/bootstrap.min.css" rel="stylesheet">
    <link href="../../css/font-awesome.min.css" rel="stylesheet">
    <link href="../../css/navbar.css" rel="stylesheet">
    <link href="../../css/search.css" rel="stylesheet">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>

<!-- Navigation -->
<%@include file="components/contact-navbar.jsp"%>

<body>
<div class="container">
    <div class="row center-block">
        <div class="col-sm-7 col-md-offset-2">
            <div class="panel panel-default text-left">
                <div class="panel-body">
                    <h3>Searching details</h3>
                </div>
            </div>

            <div class="well text-left">
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

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="birthday-from">Birthday from: </label>
                            <input class="form-control" type="date" id="birthday-from" name="birthday-from">
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="birthday-to">Birthday to: </label>
                            <input class="form-control" type="date" id="birthday-to" name="birthday-to">
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="gender">Select gender: </label>
                    <select class="form-control" id="gender" name="gender">
                        <option>Male</option>
                        <option>Female</option>
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
            </div>
            <div class="col-md-offset-5">
                <button type="button" class="btn btn-info btn-lg search-button" onclick="">
                    <i class="fa fa-search fa-lg" aria-hidden="true"></i>
                </button>
            </div>
        </div>

    </div>
</div>
</body>
</html>

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
    <title>Error!</title>
    <link href="../../css/bootstrap.min.css" rel="stylesheet">
    <link href="../../css/error.css" rel="stylesheet">
    <link href="../../css/navbar.css" rel="stylesheet">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>

<!-- Navigation -->
<%@include file="components/contact-navbar.jsp"%>

        <div class="container">
            <div class="box">
        <h1>404</h1>
        <h2>The page could not be found</h2>
        <hr />
        <p>The page you are looking for might have been removed had its name changed or is temporarily unavailable</p>
        <p>Please try the following:</p>
        <ul>
            <li>If you type the page address in the <strong>Address bar</strong>, make sure that it is spelled correctly.</li>
            <li>Click the <strong>Back button</strong> to return to your previously visited page</li>
            <li>If you were linked to this page, contact the administrator and make them aware of this issue.</li>
        </ul>
    </div>
</div>
</body>
</html>

<c:url value="/api/get_contacts?page=1" var="getContactsUrl" />
<c:url value="/api/create_contact" var="createContactUrl">
    <c:param name="contact_submit" value="false" />
</c:url>
<c:url value="/api/" var="var2" />

<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">Contact book</h1>
    </div>
</div>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <%--<div class="navbar-header">--%>
            <%--<a class="navbar-brand" href="${getContactsUrl}">Contacts</a>--%>
        <%--</div>--%>
        <ul class="nav navbar-nav">
            <!--<li class="active"><a href="#">Home</a></li>-->
            <li><a href="${getContactsUrl}">Contacts</a></li>
            <li><a href="${createContactUrl}">New contact</a></li>
        </ul>
    </div>
</nav>
<hr>

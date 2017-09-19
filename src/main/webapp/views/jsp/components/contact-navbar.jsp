<c:url value="/api/get_contacts?page=1" var="getContactsUrl" />
<c:url value="/api/create_contact" var="createContactUrl">
    <c:param name="contact_submit" value="false" />
</c:url>
<c:url value="/api/" var="var2" />

<nav class="navbar contact-navbar">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">Contact book</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li><a href="${getContactsUrl}">
                    <i class="fa fa-users fa-lg" aria-hidden="true"></i> Contacts
                </a></li>
                <li><a href="${createContactUrl}">
                    <i class="fa fa-search fa-lg" aria-hidden="true"></i> Search
                </a></li>
                <li><a href="#">
                    <i class="fa fa-envelope-o fa-lg" aria-hidden="true"></i> Email
                </a></li>
            </ul>
        </div>
    </div>
</nav>

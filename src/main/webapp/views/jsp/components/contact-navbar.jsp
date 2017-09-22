<c:url value="/api/get_contacts?page=1" var="getContactsUrl" />
<c:url value="/api/search_contact" var="searchContactUrl">
    <c:param name="submit" value="false" />
</c:url>
<c:url value="/api/send_email_to_contacts" var="sendEmailUrl">
    <c:param name="submit" value="false" />
</c:url>

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
                <li><a href="${searchContactUrl}">
                    <i class="fa fa-search fa-lg" aria-hidden="true"></i> Search
                </a></li>
                <li><a href="${sendEmailUrl}">
                    <i class="fa fa-envelope-o fa-lg" aria-hidden="true"></i> Email
                </a></li>
            </ul>
        </div>
    </div>
</nav>

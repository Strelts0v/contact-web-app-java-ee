function addEmailToSendList(){
    const EMAIL_TO_SEND_INPUT_ID = "form-email-to";
    var emailToInput = document.getElementById(EMAIL_TO_SEND_INPUT_ID);
    var emailAddress = emailToInput.value;

    const EMAIL_TO_SEND_TABLE_BODY_ID = "table-body-emails-to-send";
    var emailTableBody = document.getElementById(EMAIL_TO_SEND_TABLE_BODY_ID);

    // creating of table row in emails to send table
    var tr = document.createElement("tr");

    // 1st td
    var tdEmail = document.createElement("td");
    tdEmail.className = "col-md-11";

    var h6 = document.createElement("h6");
    h6.className = "list-group-item-text";
    h6.innerText = emailAddress;

    tdEmail.appendChild(h6);
    tr.appendChild(tdEmail);

    // 2nd td
    var tdDelete = document.createElement("td");
    tdDelete.className = "col-md-1";

    var buttonDelete = document.createElement("button");
    buttonDelete.className = "btn btn-sm btn-danger";
    buttonDelete.onclick = function(){removeEmailFromSendList(this)};

    var icon = document.createElement("i");
    icon.className = "fa fa-times";

    buttonDelete.appendChild(icon);
    tdDelete.appendChild(buttonDelete);
    tr.appendChild(tdDelete);

    emailTableBody.appendChild(tr);

    addEmailToForm(emailAddress);
}

function removeEmailFromSendList(element){
    var tr = element.parentElement.parentElement;
    var tBody = tr.parentElement;
    tBody.removeChild(tr);
}

const EMAIL_TEMPLATE_INPUT_ID = "email-template";

function selectTemplate(templateValue){
    const BIRTHDAY = "Birthday";
    const DEFAULT = "Default";

    const BIRTHDAY_FORM_GROUP_ID = "birthday-form-group";
    var birthdayFormGroup = document.getElementById(BIRTHDAY_FORM_GROUP_ID);

    switch (templateValue){
        case BIRTHDAY:
            birthdayFormGroup.style.visibility = "visible";
            break;
        case DEFAULT:
            birthdayFormGroup.style.visibility = "hidden";
            break;
    }
    var emailTemplateInput = document.getElementById(EMAIL_TEMPLATE_INPUT_ID);
    emailTemplateInput.value = templateValue;
}

const EMAILS_TO_SEND_INPUT_ID = "emails-to-send";

function addEmailToForm(emailAddress){
    var emailsToSendInput = document.getElementById(EMAILS_TO_SEND_INPUT_ID);
    emailsToSendInput.value += emailAddress + ";";
}

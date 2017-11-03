const EMAIL_TO_SEND_TABLE_BODY_ID = "table-body-emails-to-send";
const EMAIL_TO_SEND_INPUT_ID = "form-email-to";
const EMAILS_TO_SEND_INPUT_ID = "emails-to-send";

(function initializeEmailToSendListInput(){
    var emailTableBody = document.getElementById(EMAIL_TO_SEND_TABLE_BODY_ID);
    var emailTrList = emailTableBody.children;
    for(var i = 0; i < emailTrList.length; i++) {
        var emailTd = emailTrList[i].children[0];   // get td with email
        var email = emailTd.innerText;
        addEmailToForm(email);
    }
})();

function addEmailToSendList(){
    var emailToInput = document.getElementById(EMAIL_TO_SEND_INPUT_ID);
    var emailAddress = emailToInput.value;

    if(checkEmailField(emailAddress) === false){
        setInvalidEmailMessage();
        return;
    }
    disableInvalidEmailMessage();

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

function checkEmailField(emailValue){
    var isEmailFieldValid = true;
    if(emailValue == null){
        isEmailFieldValid = false;
    } else if(emailValue.localeCompare("") === 0){
        isEmailFieldValid = false;
    } else if(emailValue.indexOf("@") === -1){
        isEmailFieldValid = false;
    }
    return isEmailFieldValid;
}

const EMAIL_ERROR_DIV_ID = "email-error-box";

function setInvalidEmailMessage(){
    var errorBoxDiv = document.getElementById(EMAIL_ERROR_DIV_ID);
    errorBoxDiv.className = "";
}

function disableInvalidEmailMessage(){
    var errorBoxDiv = document.getElementById(EMAIL_ERROR_DIV_ID);
    errorBoxDiv.className = "hidden";
}

function removeEmailFromSendList(element){
    var tr = element.parentElement.parentElement;
    var tBody = tr.parentElement;
    tBody.removeChild(tr);
}

const EMAIL_TEMPLATE_INPUT_ID = "email-template";

function selectTemplate(templateValue){
    var emailTemplateInput = document.getElementById(EMAIL_TEMPLATE_INPUT_ID);
    emailTemplateInput.value = templateValue;
}

function addEmailToForm(emailAddress){
    var emailsToSendInput = document.getElementById(EMAILS_TO_SEND_INPUT_ID);
    emailsToSendInput.value += emailAddress + ";";
}

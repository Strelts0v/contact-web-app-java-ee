// Get the modal
var modal = document.getElementById('fileUploadModal');

// Get the button that opens the modal
var btn = document.getElementById("add-attachment-button");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// ids for contact info inputs
// also they represent param names for sending request to server
const ID_CONTACT_INPUT_ID = "id_contact";
const SURNAME_INPUT_ID = "surname";
const FIRSTNAME_INPUT_ID = "first_name";
const PATRONYMIC_INPUT_ID = "patronymic";
const BIRTHDAY_INPUT_ID = "birthday";
const GENDER_INPUT_ID = "gender";
const NATIONALITY_INPUT_ID = "nationality";
const MARITAL_STATUS_INPUT_ID = "marital_status";
const WEBSITE_INPUT_ID = "website";
const EMAIL_INPUT_ID = "email";
const COMPANY_INPUT_ID = "company";
const COUNTRY_INPUT_ID = "country";
const CITY_INPUT_ID = "city";
const ADDRESS_INPUT_ID = "address";
const INDEX_INPUT_ID = "index";

// param names for sending request to server
const PHONES_PARAM_NAME = "phones";
const ATTACHMENTS_PARAM_NAME = "attachments";

// ids for attachment html inputs from modal
const ATTACHMENT_NAME_ID = "attachment_name";
const COMMENT_ID = "comment";
const UPLOAD_FILE_INPUT_ID = "upload_file";

// attachments storage
var formData = new FormData();

// ids from table with attachments
const ATTACHMENT_TABLE_BODY_ID = "attachment-table-body";

// css class names
const PHONE_CLASS_NAME = "phoneItem";
const ATTACHMENT_CLASS_NAME = "attachmentItem";

// When the user clicks the button, open the modal
btn.onclick = function() {
    modal.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
    modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

function addAttachment(){
    var attachmentName = document.getElementById(ATTACHMENT_NAME_ID).value;
    var comment = document.getElementById(COMMENT_ID).value;
    var uploadFileInput = document.getElementById(UPLOAD_FILE_INPUT_ID);
    var downloadDate = new Date();

    if(uploadFileInput.value) {
        formData.append(uploadFileInput.value, uploadFileInput.files[0]);
    }

    var attachmentTableBody = document.getElementById(ATTACHMENT_TABLE_BODY_ID);
    var tr = document.createElement("tr");

    var fileNameTd = document.createElement("td");
    fileNameTd.innerHTML = attachmentName;
    var downloadDateTd = document.createElement("td");
    downloadDateTd.innerHTML = downloadDate.toDateString();
    var commentTd = document.createElement("td");
    commentTd.innerHTML = comment;

    tr.appendChild(fileNameTd);
    tr.appendChild(downloadDateTd);
    tr.appendChild(commentTd);

    attachmentTableBody.appendChild(tr);

    // clean up modal fields
    document.getElementById(ATTACHMENT_NAME_ID).value = "";
    document.getElementById(COMMENT_ID).value = "";
    document.getElementById(UPLOAD_FILE_INPUT_ID).value = null;

    modal.style.display = "none";
}

function cancelAttachment(){
    // clean up modal fields
    document.getElementById(ATTACHMENT_NAME_ID).value = "";
    document.getElementById(COMMENT_ID).value = "";
    document.getElementById(UPLOAD_FILE_INPUT_ID).value = null;

    modal.style.display = "none";
}

function updateContact(){
    // contact info properties
    const contactId = document.getElementById(ID_CONTACT_INPUT_ID).value;
    const surname = document.getElementById(SURNAME_INPUT_ID).value;
    const firstName = document.getElementById(FIRSTNAME_INPUT_ID).value;
    const patronymic = document.getElementById(PATRONYMIC_INPUT_ID).value;
    const birthday = document.getElementById(BIRTHDAY_INPUT_ID).value;
    const gender = document.getElementById(GENDER_INPUT_ID).value;
    const nationality = document.getElementById(NATIONALITY_INPUT_ID).value;
    const marital_status = document.getElementById(MARITAL_STATUS_INPUT_ID).value;
    const website = document.getElementById(WEBSITE_INPUT_ID).value;
    const email = document.getElementById(EMAIL_INPUT_ID).value;
    const company = document.getElementById(COMPANY_INPUT_ID).value;
    const country = document.getElementById(COUNTRY_INPUT_ID).value;
    const city = document.getElementById(CITY_INPUT_ID).value;
    const address = document.getElementById(ADDRESS_INPUT_ID).value;
    const index = document.getElementById(INDEX_INPUT_ID).value;

    var phoneTableRows = document.getElementsByClassName(PHONE_CLASS_NAME);
    var phones = [];
    for(var i = 0; i < phoneTableRows.length; i++){
        phones[i] = parsePhone(phoneTableRows[i].innerText, phoneTableRows[i].cells[0].innerHTML)
    }

    // get all attachments
    var attachmentTableBody = document.getElementById(ATTACHMENT_TABLE_BODY_ID);
    var attachmentTableRows = attachmentTableBody.childNodes;
    var attachments = [];
    for(var i = 0; i < attachmentTableRows.length; i++){
        attachments[i] = [];
        var tdItems = attachmentTableRows.childNodes;
        if(!tdItems){
            continue;
        }
        for(var j = 0; j < tdItems.length; j++){
            attachments[i][j] = tdItems[j].innerHTML;
        }
    }

    // var filesXhr = new XMLHttpRequest();
    // filesXhr.open("post", "/upload/add_attachment_to_contact");
    // filesXhr.send(formData);

    var xhr = new XMLHttpRequest();
    xhr.open("post", "/upload/add_attachment_to_contact");
    
    formData.append(ID_CONTACT_INPUT_ID, contactId);
    formData.append(SURNAME_INPUT_ID, surname);
    formData.append(FIRSTNAME_INPUT_ID, firstName);
    formData.append(PATRONYMIC_INPUT_ID, patronymic);
    formData.append(BIRTHDAY_INPUT_ID, birthday);
    formData.append(GENDER_INPUT_ID, gender);
    formData.append(NATIONALITY_INPUT_ID, nationality);
    formData.append(MARITAL_STATUS_INPUT_ID, marital_status);
    formData.append(WEBSITE_INPUT_ID, website);
    formData.append(EMAIL_INPUT_ID, email);
    formData.append(COMPANY_INPUT_ID, company);
    formData.append(COUNTRY_INPUT_ID, country);
    formData.append(CITY_INPUT_ID, city);
    formData.append(ADDRESS_INPUT_ID, address);
    formData.append(INDEX_INPUT_ID, index);

    var phonesJson = JSON.stringify(phones);
    formData.append(PHONES_PARAM_NAME,phonesJson);
    
    xhr.send(formData);

    // xhr.open("post", "/upload/add_attachment_to_contact");
//    xhr.send(
//        ID_CONTACT_INPUT_ID + "=" + contactId + "&" +
//        SURNAME_INPUT_ID + "=" + surname + "&" +
//        FIRSTNAME_INPUT_ID + "=" + firstName + "&" +
//        PATRONYMIC_INPUT_ID + "=" + patronymic + "&" +
//        BIRTHDAY_INPUT_ID + "=" + birthday + "&" +
//        GENDER_INPUT_ID + "=" + gender + "&" +
//        NATIONALITY_INPUT_ID + "=" + nationality + "&" +
//        MARITAL_STATUS_INPUT_ID + "=" + marital_status + "&" +
//        WEBSITE_INPUT_ID + "=" + website + "&" +
//        EMAIL_INPUT_ID + "=" + email + "&" +
//        COMPANY_INPUT_ID + "=" + company + "&" +
//        COUNTRY_INPUT_ID + "=" + country + "&" +
//        CITY_INPUT_ID + "=" + city + "&" +
//        ADDRESS_INPUT_ID + "=" + address + "&" +
//        INDEX_INPUT_ID + "=" + index
//    );

    // var filesXhr = new XMLHttpRequest();
    // filesXhr.open("post", "/upload/add_attachment_to_contact");
    // filesXhr.send(formData);
}

function parsePhone(innerText, innerTextWithPhoneId){
    var phone = {};
    phone.phoneId = parsePhoneId(innerTextWithPhoneId);
    var words = parseStringIntoWords(innerText);

    phone.phoneNumber = words[0];
    phone.phoneType = words[1];
    phone.comment = "";
    for(var i = 2; i < words.length; i++){
        phone.comment = phone.comment + " " + words[i];
    }
    return phone;
}

function parsePhoneId(innerTextWithPhoneId){
    var indexOfId = innerTextWithPhoneId.indexOf("value=");
    // go to phone id value
    const incrementValueToSkipParamName = 7;
    indexOfId += incrementValueToSkipParamName;

    var idStr = "";
    while(innerTextWithPhoneId.charAt(indexOfId) !== "\""){
        idStr = idStr + innerTextWithPhoneId.charAt(indexOfId++);
    }
    return idStr;
}

function parseStringIntoWords(str){
    var words = str.match(/\b(\w+)\b/g);
    return words;
}

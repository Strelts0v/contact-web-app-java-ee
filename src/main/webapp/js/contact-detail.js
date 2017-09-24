// modals
const ATTACHMENT_MODAL = document.getElementById('attachment-modal');

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

// css class names
const PHONE_CLASS_NAME = "phoneItem";

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

    // TODO: append files from attachments

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
    var incrementValueToSkipParamName = 7;
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

function setCheckboxesChecked(controlCheckBoxId, checkBoxName){
    var isAllCheckboxesChecked = document.getElementById(controlCheckBoxId).checked;
    var checkBoxes = document.getElementsByName(checkBoxName);
    // loop over them all and set checked
    for(var i = 0; i < checkBoxes.length; i++){
        checkBoxes[i].checked = isAllCheckboxesChecked;
    }
}

function deleteTrFromTbody(element){
    // go to the parent tr tag
    var tr = element
        .parentElement
        .parentElement
        .parentElement;

    // delete selected tr from tbody
    var tbody = tr.parentElement;
    tbody.removeChild(tr);
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    switch(event.target) {
        case ATTACHMENT_MODAL:
            ATTACHMENT_MODAL.style.display = "none";
            break;
        case PHONE_MODAL:
            PHONE_MODAL.style.display = "none";
            break;
    }
}

// ///////////////////////////////
// ATTACHMENT MODAL FUNCTIONS   //
//////////////////////////////////

// ids for attachment html inputs from modal
const MODAL_ATTACHMENT_NAME_ID = "attachment-name";
const MODAL_ATTACHMENT_COMMENT_ID = "attachment-comment";
const MODAL_ATTACHMENT_FILE_ID = "upload-attachment";

// attachments storage for sending data to the server
var formData = new FormData();

// attachments storage for preliminary storing file before sending
var attachments = {};

// table body id from table with attachments
const ATTACHMENT_TABLE_BODY_ID = "attachment-table-body";

const ATTACHMENT_MODAL_OK_BUTTON_ID = "attachment-ok-button";

// attachment td indexes of properties from attachment table
const ATTACHMENT_ID_TD_INDEX = 0;
const ATTACHMENT_NAME_TD_INDEX = 1;
const ATTACHMENT_DOWNLOAD_DATE_TD_INDEX = 2;
const ATTACHMENT_COMMENT_TD_INDEX = 3;

// When the user clicks the button, open the modal
// clickedButton - clicked button object
function showAttachmentModal(okOnclickFunction, clickedButton) {
    var okButton = document.getElementById(ATTACHMENT_MODAL_OK_BUTTON_ID);
    okButton.onclick = okOnclickFunction;
    okButton.data = clickedButton;

    // if okOnclickFunction is editAttachment then add existed values to
    // inputs of modal
    if(okOnclickFunction === editAttachment){
        // get values from attachment tr
        var tr = clickedButton
            .parentElement
            .parentElement
            .parentElement;

        var attachmentName = tr.children[ATTACHMENT_NAME_TD_INDEX].innerHTML;
        var comment = tr.children[ATTACHMENT_COMMENT_TD_INDEX].innerHTML;

        document.getElementById(MODAL_ATTACHMENT_NAME_ID).value = attachmentName;
        document.getElementById(MODAL_ATTACHMENT_COMMENT_ID).value = comment;
    }

    ATTACHMENT_MODAL.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
function closeAttachmentModal() {
    ATTACHMENT_MODAL.style.display = "none";
}

function cancelAttachment(){
    // clean up modal fields
    document.getElementById(MODAL_ATTACHMENT_NAME_ID).value = "";
    document.getElementById(MODAL_ATTACHMENT_COMMENT_ID).value = "";
    document.getElementById(MODAL_ATTACHMENT_FILE_ID).value = null;

    closeAttachmentModal();
}

function addAttachment(){
    var attachmentName = document.getElementById(MODAL_ATTACHMENT_NAME_ID).value;
    var comment = document.getElementById(MODAL_ATTACHMENT_COMMENT_ID).value;
    var uploadFileInput = document.getElementById(MODAL_ATTACHMENT_FILE_ID);
    var downloadDate = new Date();

    if(uploadFileInput.value){
        attachments[attachmentName] = uploadFileInput.files[0];
    }

    var attachmentTableBody = document.getElementById(ATTACHMENT_TABLE_BODY_ID);
    // create all html elements for attachment table row
    var tr = createAttachmentTr(attachmentName, downloadDate, comment);
    attachmentTableBody.appendChild(tr);

    cancelAttachment();
}

function createAttachmentTr(attachmentName, downloadDate, attachmentComment){
    var tr = document.createElement("tr");

    var checkBoxTd = createAttachmentCheckBoxTd();
    var fileNameTd = createAttachmentFileNameTd(attachmentName);
    var downloadDateTd = createAttachmentDownloadDateTd(downloadDate);
    var commentTd = createAttachmentCommentTd(attachmentComment);
    var controlTd = createAttachmentControlTd();

    tr.appendChild(checkBoxTd);
    tr.appendChild(fileNameTd);
    tr.appendChild(downloadDateTd);
    tr.appendChild(commentTd);
    tr.appendChild(controlTd);

    return tr;
}

function createAttachmentCheckBoxTd(){
    var checkBoxTd = document.createElement("td");
    var checkBoxInput = document.createElement("input");
    checkBoxInput.setAttribute("type", "checkbox");
    checkBoxInput.setAttribute("name", "is_attachment_selected");
    checkBoxInput.setAttribute("value", "0");
    checkBoxTd.appendChild(checkBoxInput);

    return checkBoxTd;
}

function createAttachmentFileNameTd(attachmentName){
    var fileNameTd = document.createElement("td");
    fileNameTd.innerHTML = attachmentName;

    return fileNameTd;
}

function createAttachmentDownloadDateTd(downloadDate){
    var downloadDateTd = document.createElement("td");
    downloadDateTd.innerHTML = downloadDate.toDateString();

    return downloadDateTd;
}

function createAttachmentCommentTd(comment){
    var commentTd = document.createElement("td");
    commentTd.innerHTML = comment;

    return commentTd;
}

function createAttachmentControlTd(){
    var controlTd = document.createElement("td");
    controlTd.className = "col-xs-2";

    var btnGroupDiv = document.createElement("div");
    btnGroupDiv.className = "btn-group";

    var deleteButton = document.createElement("button");
    deleteButton.className = "btn btn-sm btn-danger";
    deleteButton.onclick = function(){deleteAttachment(this)};

    var deleteIcon = document.createElement("i");
    deleteIcon.className = "fa fa-trash-o fa-lg";
    deleteButton.appendChild(deleteIcon);

    var editButton = document.createElement("button");
    editButton.className = "btn btn-sm btn-warning";
    editButton.onclick = function(){showAttachmentModal(editAttachment, editButton)};

    var editIcon = document.createElement("i");
    editIcon.className = "fa fa-pencil fa-lg";
    editButton.appendChild(editIcon);

    btnGroupDiv.appendChild(deleteButton);
    btnGroupDiv.appendChild(editButton);

    controlTd.appendChild(btnGroupDiv);

    return controlTd;
}

function deleteAttachment(buttonClickedElement){
    // go to the parent tr tag
    var tr = buttonClickedElement
        .parentElement
        .parentElement
        .parentElement;

    // delete field from attachments object associated with that attachment
    var attachmentName = tr.children[ATTACHMENT_NAME_TD_INDEX].innerHTML;
    if(attachmentName in attachments){
        delete attachments[attachmentName];
    }
    // delete tr associated with that attachment
    deleteTrFromTbody(buttonClickedElement);
}

function editAttachment(){
    // get clicked ok button
    var okButton = document.getElementById(ATTACHMENT_MODAL_OK_BUTTON_ID);

    // go to the parent tr tag
    var tr = okButton.data
        .parentElement
        .parentElement
        .parentElement;

    var newAttachmentName = document.getElementById(MODAL_ATTACHMENT_NAME_ID).value;
    var newComment = document.getElementById(MODAL_ATTACHMENT_COMMENT_ID).value;
    var newUploadFileInput = document.getElementById(MODAL_ATTACHMENT_FILE_ID);
    var oldAttachmentName = tr.children[ATTACHMENT_NAME_TD_INDEX].innerHTML;

    // if attachment name was changed remove old and add new one
    if(oldAttachmentName.localeCompare(newAttachmentName) !== 0){
        var file = attachments[oldAttachmentName];
        attachments[newAttachmentName] = file;
        delete attachments[oldAttachmentName];

        // change attachment name it tr
        tr.children[ATTACHMENT_NAME_TD_INDEX].innerHTML = newAttachmentName;
    }

    // if user change file then update attachments associate object
    if(newUploadFileInput.value){
        attachments[newAttachmentName] = newUploadFileInput.files[0];
        tr.children[ATTACHMENT_DOWNLOAD_DATE_TD_INDEX].innerHTML = new Date().toDateString();
    }

    tr.children[ATTACHMENT_COMMENT_TD_INDEX].innerHTML = newComment;

    cancelAttachment();
}

// ///////////////////////////////
// PHONE MODAL FUNCTIONS        //
//////////////////////////////////

const PHONE_MODAL = document.getElementById('phone-modal');

// inputs ids from phone modal
const MODAL_PHONE_COUNTRY_CODE_ID = "country-code";
const MODAL_PHONE_OPERATOR_CODE_ID = "operator-code";
const MODAL_PHONE_NUMBER_ID = "phone-number";
const MODAL_PHONE_TYPE_ID = "phone-type";
const MODAL_PHONE_COMMENT_ID = "phone-comment";

// attachment td indexes of properties from attachment table
const PHONE_ID_TD_INDEX = 0;
const PHONE_NUMBER_TD_INDEX = 1;
const PHONE_TYPE_TD_INDEX = 2;
const PHONE_COMMENT_TD_INDEX = 3;

const PHONE_MODAL_OK_BUTTON_ID = "phone-ok-button";

// table body id from table with phones
const PHONE_TABLE_BODY_ID = "phone-table-body";

function cancelPhone(){
    // clean up modal fields
    document.getElementById(MODAL_PHONE_COUNTRY_CODE_ID).value = "";
    document.getElementById(MODAL_PHONE_OPERATOR_CODE_ID).value = "";
    document.getElementById(MODAL_PHONE_NUMBER_ID).value = "";
    document.getElementById(MODAL_PHONE_COMMENT_ID).value = "";

    PHONE_MODAL.style.display = "none";
}

// when the user clicks add or edit buttons on attachment
function showPhoneModal(okOnclickFunction, clickedButton){
    var okButton = document.getElementById(PHONE_MODAL_OK_BUTTON_ID);
    okButton.onclick = okOnclickFunction;
    okButton.data = clickedButton;

    // if okOnclickFunction is editPhone then add existed values to
    // inputs of modal
    if(okOnclickFunction === editPhone){
        // get values from attachment tr
        var tr = clickedButton
            .parentElement
            .parentElement
            .parentElement;

        var phoneNumber = tr.children[PHONE_NUMBER_TD_INDEX].innerHTML;
        var phoneCountryCode = parseCountryCodeFromFullPhoneNumber(phoneNumber);
        var phoneOperatorCode = parseOperatorCodeFromFullPhoneNumber(phoneNumber);
        phoneNumber = parsePhoneNumberFromFullPhoneNumber(phoneNumber);

        var phoneType = tr.children[PHONE_TYPE_TD_INDEX].innerHTML;
        var comment = tr.children[PHONE_COMMENT_TD_INDEX].innerHTML;

        document.getElementById(MODAL_PHONE_COUNTRY_CODE_ID).value = phoneCountryCode;
        document.getElementById(MODAL_PHONE_OPERATOR_CODE_ID).value = phoneOperatorCode;
        document.getElementById(MODAL_PHONE_NUMBER_ID).value = phoneNumber;
        document.getElementById(MODAL_PHONE_TYPE_ID).value = phoneType;
        document.getElementById(MODAL_PHONE_COMMENT_ID).value = comment;
    }

    PHONE_MODAL.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
function closePhoneModal(){
    PHONE_MODAL.style.display = "none";
}

function addPhone(){
    var countryCode = document.getElementById(MODAL_PHONE_COUNTRY_CODE_ID).value;
    var operatorCode = document.getElementById(MODAL_PHONE_OPERATOR_CODE_ID).value;
    var phoneNumber = document.getElementById(MODAL_PHONE_NUMBER_ID).value;
    var phoneType = document.getElementById(MODAL_PHONE_TYPE_ID).value;
    var phoneComment = document.getElementById(MODAL_PHONE_COMMENT_ID).value;

    var tbody = document.getElementById(PHONE_TABLE_BODY_ID);
    var tr = createPhoneTr(
        countryCode,
        operatorCode,
        phoneNumber,
        phoneType,
        phoneComment
    );

    tbody.appendChild(tr);

    cancelPhone();
}

function createPhoneTr(countryCode, operatorCode, phoneNumber, phoneType, phoneComment){
    var tr = document.createElement("tr");

    var checkBoxTd = createPhoneCheckBoxTd();
    var phoneNumberTd = createPhoneNumberTd(countryCode, operatorCode, phoneNumber);
    var phoneTypeTd = createPhoneTypeTd(phoneType);
    var commentTd = createPhoneCommentTd(phoneComment);
    var controlTd = createPhoneControlTd();

    tr.appendChild(checkBoxTd);
    tr.appendChild(phoneNumberTd);
    tr.appendChild(phoneTypeTd);
    tr.appendChild(commentTd);
    tr.appendChild(controlTd);

    return tr;
}

function createPhoneCheckBoxTd(){
    var checkBoxTd = document.createElement("td");
    var checkBoxInput = document.createElement("input");
    checkBoxInput.setAttribute("type", "checkbox");
    checkBoxInput.setAttribute("name", "is_phone_selected");
    checkBoxInput.setAttribute("value", "0");
    checkBoxTd.appendChild(checkBoxInput);

    return checkBoxTd;
}

function createPhoneNumberTd(countryCode, operatorCode, phoneNumber){
    var phoneNumberTd = document.createElement("td");
    phoneNumberTd.innerHTML = countryCode + operatorCode + phoneNumber;

    return phoneNumberTd;
}

function createPhoneTypeTd(phoneType){
    var phoneTypeTd = document.createElement("td");
    phoneTypeTd.innerHTML = phoneType;

    return phoneTypeTd;
}

function createPhoneCommentTd(phoneComment){
    var phoneCommentTd = document.createElement("td");
    phoneCommentTd.innerHTML = phoneComment;

    return phoneCommentTd;
}

function createPhoneControlTd(){
    var controlTd = document.createElement("td");
    controlTd.className = "col-xs-2";

    var btnGroupDiv = document.createElement("div");
    btnGroupDiv.className = "btn-group";

    var deleteButton = document.createElement("button");
    deleteButton.className = "btn btn-sm btn-danger";
    deleteButton.onclick = function(){deletePhone(this)};

    var deleteIcon = document.createElement("i");
    deleteIcon.className = "fa fa-trash-o fa-lg";
    deleteButton.appendChild(deleteIcon);

    var editButton = document.createElement("button");
    editButton.className = "btn btn-sm btn-warning";
    editButton.onclick = function(){showPhoneModal(editPhone, editButton)};

    var editIcon = document.createElement("i");
    editIcon.className = "fa fa-pencil fa-lg";
    editButton.appendChild(editIcon);

    btnGroupDiv.appendChild(deleteButton);
    btnGroupDiv.appendChild(editButton);

    controlTd.appendChild(btnGroupDiv);

    return controlTd;
}

function editPhone(){
    // get clicked ok button
    var okButton = document.getElementById(PHONE_MODAL_OK_BUTTON_ID);

    // go to the parent tr tag
    var tr = okButton.data
        .parentElement
        .parentElement
        .parentElement;

    const MODAL_COUNTRY_CODE_ID = "country-code";
    const MODAL_OPERATOR_CODE_ID = "operator-code";
    const MODAL_PHONE_NUMBER_ID = "phone-number";
    const MODAL_PHONE_TYPE_ID = "phone-type";
    const MODAL_PHONE_COMMENT_ID = "phone-comment";


    var newPhoneNumber =
        document.getElementById(MODAL_COUNTRY_CODE_ID).value +
        document.getElementById(MODAL_OPERATOR_CODE_ID).value +
        document.getElementById(MODAL_PHONE_NUMBER_ID).value;

    var newPhoneType = document.getElementById(MODAL_PHONE_TYPE_ID).value;
    var newPhoneComment = document.getElementById(MODAL_PHONE_COMMENT_ID).value;

    tr.children[PHONE_NUMBER_TD_INDEX].innerHTML = newPhoneNumber;
    tr.children[PHONE_TYPE_TD_INDEX].innerHTML = newPhoneType;
    tr.children[PHONE_COMMENT_TD_INDEX].innerHTML = newPhoneComment;

    cancelPhone();
}

function deletePhone(buttonClickedElement){
    // delete tr associated with that attachment
    deleteTrFromTbody(buttonClickedElement);
}

function parseCountryCodeFromFullPhoneNumber(fullPhoneNumber){
    const countryCodeBeginIndex = 0;
    const countryCodeEndIndex = 4;
    return fullPhoneNumber.substring(countryCodeBeginIndex, countryCodeEndIndex);
}

function parseOperatorCodeFromFullPhoneNumber(fullPhoneNumber){
    const operatorCodeBeginIndex = 4;
    const operatorCodeEndIndex = 6;
    return fullPhoneNumber.substring(operatorCodeBeginIndex, operatorCodeEndIndex);
}

function parsePhoneNumberFromFullPhoneNumber(fullPhoneNumber){
    const phoneNumberBeginIndex = 6;
    const phoneNumberEndIndex = fullPhoneNumber.length;
    return fullPhoneNumber.substring(phoneNumberBeginIndex, phoneNumberEndIndex);
}

//////////////////////////////////////////
// PHOTO MODAL FUNCTIONS            //////
//////////////////////////////////////////

const PHOTO_MODAL = document.getElementById('photo-modal');

const PHOTO_MODAL_FILE_ID = "upload-photo";

const CONTACT_PHOTO_DIV_ID = "contact-photo";

var currentPhoto;

function showPhotoModal(){
    PHOTO_MODAL.style.display = "block";
}

function closePhotoModal(){
    PHOTO_MODAL.style.display = "none";
}

function savePhoto(){
    var photoInput = document.getElementById(PHOTO_MODAL_FILE_ID);

    // save photo in currentPhoto
    if(photoInput.value){
        var photo = photoInput.files[0];
        currentPhoto = photoInput.files[0];
    }
    // display photo in contact-detail form
    var photoDiv = document.getElementById(CONTACT_PHOTO_DIV_ID);
    const photoImgDivIndex = 0;
    var photoImg = photoDiv.children[photoImgDivIndex];
    var photoData = window.URL.createObjectURL(photo);
    photoImg.src = photoData;

    cancelPhoto();
}

function getBase64(file) {
    var reader = new FileReader();
    reader.readAsDataURL(file);
    return reader.result;
}

function cancelPhoto(){
    document.getElementById(PHOTO_MODAL_FILE_ID).value = null;

    closePhotoModal();
}
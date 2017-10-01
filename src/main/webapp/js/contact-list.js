const CONTACT_CHECK_BOXES_NAME = "is_contact_selected";

function deleteCheckedContacts(){
    var checkBoxes = document.getElementsByName(CONTACT_CHECK_BOXES_NAME);
    var checkBoxesCheckedContactIds = [];
    // loop over them all and push only selected values
    for (var i = 0; i < checkBoxes.length; i++) {
        if (checkBoxes[i].checked) {
            checkBoxesCheckedContactIds.push(checkBoxes[i].value);
        }
    }
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/delete_contact?selectedContacts=' + checkBoxesCheckedContactIds, false);
    xhr.send();
    location.reload();
}

function setAllContactsChecked(){
    var isAllCheckboxesSelect = document.getElementById("check-all-contacts").checked;
    var checkBoxes = document.getElementsByName(CONTACT_CHECK_BOXES_NAME);
    // loop over them all and set checked
    for(var i = 0; i < checkBoxes.length; i++){
        checkBoxes[i].checked = isAllCheckboxesSelect;
    }
}

const CONTACT_ID_TD_INDEX = 0;
const CONTACT_URL_TD_INDEX = 1;

function deleteContact(clickedButton){
    var tr = clickedButton
        .parentElement
        .parentElement
        .parentElement;
    var td = tr.children[CONTACT_ID_TD_INDEX];
    var contactId = td.children[0].value;
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/delete_contact?selectedContacts=' + contactId, false);
    xhr.send();
    location.reload();
}

function editContact(clickedButton){
    var tr = clickedButton
        .parentElement
        .parentElement
        .parentElement;
    var td = tr.children[CONTACT_URL_TD_INDEX];
    var href = td.children[0].href;
    window.location.href = href;
}
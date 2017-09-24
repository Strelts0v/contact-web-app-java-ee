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
    xhr.open('POST', '${deleteContactsUrl}?selectedContacts=' + checkBoxesCheckedContactIds, false);
    xhr.send();
}

function setAllContactsChecked(){
    var isAllCheckboxesSelect = document.getElementById("check-all-contacts").checked;
    var checkBoxes = document.getElementsByName(CONTACT_CHECK_BOXES_NAME);
    // loop over them all and set checked
    for(var i = 0; i < checkBoxes.length; i++){
        checkBoxes[i].checked = isAllCheckboxesSelect;
    }
}
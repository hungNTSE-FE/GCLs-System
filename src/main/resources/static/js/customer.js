var URL_SAVE_CUSTOMER = '/customer/saveCustomer';
var URL_REGISTER_CUSTOEMR = '/customer/registerCustomer';

var CUSTOMER_FORM_ID = '#customerForm';
var BTN_REGISTER_CUSTOMER_ID = '#registerCustomer';

var CUSTOMER_FORM = {
    BROKER_CMB_ID : '#brokerCmb',
    SOURCE_CMB_ID : '#sourceCmb',
    GENDER_CMB_ID : '#genderCmb',
    CREATE_DATE_PICKER_ID : '#createDatePicker',
    DATE_OF_ISSUE_PICKER_ID : '#dateOfIssuePicker',
    HDN_SOURCE_ID : '#hdnSourceId',
    HDN_EMPLOYEE_ID : '#hdnEmployeeId',
    GENDER_ID : '#gender'
}

$(document).ready(function () {
    $(BTN_REGISTER_CUSTOMER_ID).on('click', function (){
        registerCustomer();
    });
});

function registerCustomer(){
    setValueForm();
    $(CUSTOMER_FORM_ID).attr('action', URL_REGISTER_CUSTOEMR);
    $(CUSTOMER_FORM_ID).submit();
}

function setValueForm() {
    var hdnSelectedEmployeeId = $(CUSTOMER_FORM.BROKER_CMB_ID).val();
    var hdnSelectedSourceId = $(CUSTOMER_FORM.SOURCE_CMB_ID).val();
    var hdnSelectedGender = $(CUSTOMER_FORM.GENDER_CMB_ID).val();

    $(CUSTOMER_FORM.HDN_EMPLOYEE_ID).val(hdnSelectedEmployeeId);
    $(CUSTOMER_FORM.HDN_SOURCE_ID).val(hdnSelectedSourceId);
    $(CUSTOMER_FORM.GENDER_ID).val(hdnSelectedGender);
}
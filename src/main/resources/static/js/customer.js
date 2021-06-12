var CUSTOMER_FORM_ID = '#customerForm';
var URL_SAVE_CUSTOMER = '/customer/saveCustomer';
var URL_REGISTER_CUSTOEMR = '/customer/registerCustomer';
var BTN_REGISTER_CUSTOMER_ID = '#registerCustomer';

$(document).ready(function () {
    initCombobox();
    $(BTN_REGISTER_CUSTOMER_ID).on('click', function (){
        registerCustomer();
    });
});

function initCombobox() {
    $("#overlay").fadeIn();
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/customer/initCombobox",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            renderCombobox(data);
        },
        error: function (e) {
            console.log("ERROR : ", e);
        },
        complete: function (){
            setTimeout(function(){
                $("#overlay").fadeOut(300);
            },500);
        }
    });
}

function renderCombobox(data) {
    var sourceCmbData = data['listSource'];
    var brokerCmb = data['listBrokerName'];
    $.each(sourceCmbData, function (index, data) {
        $('#source').append($("<option />").val(data['key']).text(data['value']));
    });
    $.each(brokerCmb, function (index, data) {
        $('#brokerCmb').append($("<option />").val(data['key']).text(data['value']));
    });
}

function registerCustomer(){
    $(CUSTOMER_FORM_ID).attr('action', URL_REGISTER_CUSTOEMR);
    $(CUSTOMER_FORM_ID).submit();
}
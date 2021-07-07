$('#sharingLeads').on('click', function(){
    var optsListBox2 = $('#lstBox2 option:not([disabled])');
    if (optsListBox2.length === 0) {
        alert("Vui lòng chọn nhân viên để chia");
        return;
    }
    var listSelectedEmployeId = [];

    $.each(optsListBox2, function(){
        listSelectedEmployeId.push(Number($(this).val()));
    })
    $('#empIdList').val(listSelectedEmployeId.join());
    $('#customerDistributionForm').submit();
})

$('#modalShareLead').on('shown.bs.modal', function () {
    var listSelectedPotentailId = [];
    $('input[name="potential-id"]:checked').each(function() {
        listSelectedPotentailId.push(Number(this.value));
    });

    var customerDistributionForm = {
        'potentialIdList' : listSelectedPotentailId
    }
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/potential/getPotentialToShare",
        dataType: 'json',
        data: JSON.stringify(listSelectedPotentailId),
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("Success : ", e);
        },
        error: function (e) {
            console.log("ERROR : ", e);
        },
    });
})
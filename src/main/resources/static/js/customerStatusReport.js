var CUSTOMER_STS_RPT_TABLE_ID = '#customerStatusReportArea';
var SEARCH_DATE = '#searchDate';
var FROM_DATE = '#fromDate';
var TO_DATE = '#toDate';
$(document).ready(function (){

})

$(SEARCH_DATE).on('click', function (){
    doLoadTable();
})

function doLoadTable(){
    var fromDate, toDate;
    fromDate = $(FROM_DATE).val();
    toDate = $(FROM_DATE).val();

    // if (fromDate === undefined || fromDate === '') {
    //     return;
    // }
    //
    // if (fromDate > toDate) {
    //     return;
    // }
    //
    var data = {
        'fromDate' : fromDate,
        'toDate' : toDate
    }

    $("#overlay").fadeIn();
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/marketing/getListCustomerStatusReport",
        dataType: 'json',
        data: data,
        cache: false,
        timeout: 600000,
        success: function (data) {
            renderDataTable();
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

function renderDataTable(data){
    $(CUSTOMER_STS_RPT_TABLE_ID).DataTable( {
        scrollY: "500px",
        scrollX: true,
        scrollCollapse: true,
        paging: true,
        bInfo: false,
        columnDefs: [
        ],
        "data": data,
        "columns": [
            { "data": "emp_name" },
            { "data": "startDate" },
            { "data": "endDate" },
            { "data": "content" },
            { "data": "status" },
            { "data": "assumptionResult"},
            { "data": "result" },
            { "data": "budget",
                render: $.fn.dataTable.render.number( ',', null, 0, null )
            },
            { "data": "actualExpense",
                render: $.fn.dataTable.render.number( ',', null, 0, null )
            },
            { "data": "averageExpense",
                render: $.fn.dataTable.render.number( ',', '.', 2, null )
            },
        ],
        order: [[1, "asc"]]
    } );
}
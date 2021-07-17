//Count Checkbox
$(document).ready(function() {
    var $checkboxes = $('#devel-generate-content-form td .d-flex input[type="checkbox"]');
    var rowCount = $('#datatablesPotentials tr').length - 1;
    $('#count-all-table').text(rowCount);

    $checkboxes.change(function() {
        var countCheckedCheckboxes = $checkboxes.filter(':checked').length;
        $('#count-checked-checkboxes').text(countCheckedCheckboxes);
    })
});

//Select all checkbox with button
var clicked = false;
$("#selectAll").on("click", function () {
    $('#datatablesPotentials tr td input[type="checkbox"]').prop("checked", !clicked);
    clicked = !clicked;
});
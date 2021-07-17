var SELECTEDOPTS_1 = "#lstBox1 option:selected";
var SELECTEDOPTS_2 = "#lstBox2 option:selected";
var BTN_RIGHT = "#btnRight";
var BTN_LEFT = "#btnLeft";
var BTN_RIGHTALL = "#btnRightall";
var BTN_LEFTALL = "#btnLeftall";
var LISTBOX_2 = "#lstBox2";
var LISTBOX_1 = "#lstBox1";

$(document).ready(function () {
    $(BTN_RIGHT).click(function (e) {
        var selectedOpts = $(SELECTEDOPTS_1);
        if (selectedOpts.length == 0) {
            alert("Nothing to move.");
            e.preventDefault();
        }

        $(LISTBOX_2).append($(selectedOpts).clone());
        $(selectedOpts).remove();
        e.preventDefault();
        updateIDs();
    });

    $(BTN_LEFT).click(function (e) {
        var selectedOpts = $(SELECTEDOPTS_2);
        if (selectedOpts.length == 0) {
            alert("Nothing to move.");
            e.preventDefault();
        }

        $(LISTBOX_1).append($(selectedOpts).clone());
        $(selectedOpts).remove();
        e.preventDefault();
        updateIDs();
    });
    $(BTN_RIGHTALL).click(function (e) {
        var selectedOpts = $("#lstBox1 option:not([disabled])");
        if (selectedOpts.length == 0) {
            alert("Nothing to move.");
            e.preventDefault();
        }
        $(LISTBOX_2).append($(selectedOpts).clone());
        $(selectedOpts).remove();
        e.preventDefault();
        updateIDs();
    });

    $(BTN_LEFTALL).click(function (e) {
        var selectedOpts = $("#lstBox2 option:not([disabled])");
        if (selectedOpts.length == 0) {
            alert("Nothing to move.");
            e.preventDefault();
        }
        $(LISTBOX_1).append($(selectedOpts).clone());
        $(selectedOpts).remove();
        e.preventDefault();
        updateIDs();
    });

});

function updateIDs() {
    $("#values").val("");
    $("#lstBox2 option").each(function (index) {
        $("#values").val($("#values").val() + $(this).val() + ",");
    });
}
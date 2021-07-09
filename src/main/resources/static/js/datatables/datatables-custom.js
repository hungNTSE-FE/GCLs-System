let urlNewStatistic = 'http://localhost:8081/statistic/potential/today';

let dataTableStatisticPotential = new simpleDatatables.DataTable("#datatablesStatisticPotential", {
    fixedHeight: true,
    searchable: false,
    perPageSelect: false,
    perPage: 3,
    ajax: {
        url: urlNewStatistic
    }
});

fetch(urlNewStatistic).then(
    res=>{
        res.json().then(
            data=>{
                console.log(data);
                if (data.length > 0) {
                    var slag = data.map(doc => Object.values(doc));
                    dataTableStatisticPotential.rows().add(slag);
                }
            }
        )
    }
)

$(function() {
    $('#reloadNewPotentials').on('click', function () {
        $.ajax({
            url: urlNewStatistic,
            dataType: "text/json",
            success: function(result){
                console.log(result);
            }
        })
    });
})

const dataTableTakeCare = new simpleDatatables.DataTable(
    "#datatablesTakeCareOfPotential",
    {
        fixedHeight: true,
        searchable: false,
        perPageSelect: false,
        perPage: 3,
    }
);

const datatablesPreQualifiedOfPotential = new simpleDatatables.DataTable(
    "#datatablesPreQualifiedOfPotential",
    {
        fixedHeight: true,
        searchable: false,
        perPageSelect: false,
        perPage: 3,
    }
);
const datatablesQualifiedOfPotential = new simpleDatatables.DataTable(
    "#datatablesQualifiedOfPotential",
    {
        fixedHeight: true,
        searchable: false,
        perPageSelect: false,
        perPage: 3,
    }
);
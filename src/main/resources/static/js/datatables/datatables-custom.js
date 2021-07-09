let urlNewStatistic = 'http://localhost:8081/statistic/potential/today';

if (document.getElementById("datatablesStatisticPotential") != null) {
    let dataTableStatisticPotential = new simpleDatatables.DataTable("#datatablesStatisticPotential", {
        fixedHeight: true,
        searchable: false,
        perPageSelect: false,
        perPage: 3,
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
} else if (document.getElementById("datatablesTakeCareOfPotential") != null) {
    const dataTableTakeCare = new simpleDatatables.DataTable(
        "#datatablesTakeCareOfPotential",
        {
            fixedHeight: true,
            searchable: false,
            perPageSelect: false,
            perPage: 3,
        }
    );
} else if (document.getElementById("datatablesManageTakeCareOfPotential") != null) {
    const datatablesTakeCareOfPotential = new simpleDatatables.DataTable(
        "#datatablesManageTakeCareOfPotential",
        {
            searchable: false,
            fixedHeight: true,
            perPageSelect: false,
            labels: {
                info: "",
            }
        }
    );
}

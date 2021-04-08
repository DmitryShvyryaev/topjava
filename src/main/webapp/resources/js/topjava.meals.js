const mealsAjaxUrl = "profile/meals/";
let isFilter = false;

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealsAjaxUrl
};

const datatableApi = $("#datatable").DataTable({
    "paging": false,
    "info": true,
    "columns": [
        {
            "data": "dateTime"
        },
        {
            "data": "description"
        },
        {
            "data": "calories"
        },
        {
            "defaultContent": "Edit",
            "orderable": false
        },
        {
            "defaultContent": "Delete",
            "orderable": false
        }
    ],
    "order": [
        [
            0,
            "asc"
        ]
    ]
});

// $(document).ready(function () {
$(function () {
    makeEditable(datatableApi);
});

function filter() {
    isFilter = true;
    updateTable();
}

function clearFilter() {
    $('#filter').find(":input").val("");
    isFilter = false;
    updateTable();
}

function updateTable() {
    if (isFilter) {
        $.ajax({
            type: 'GET',
            url: ctx.ajaxUrl + 'filter',
            data: $('#filter').serialize()
        }).done(function (data) {
            ctx.datatableApi.clear().rows.add(data).draw();
            successNoty("Filtered");
        })
    } else {
        $.get(ctx.ajaxUrl, function (data) {
            ctx.datatableApi.clear().rows.add(data).draw();
        });
    }
}
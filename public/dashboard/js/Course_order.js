function deleteEditedItem() {
    var Items = new Array;
    Items.push($('.datatable').DataTable().$('td', 'tr.edited').eq(1).text());
    deleteItems(Items);
}

function deleteSelectedItems() {
    var Items = new Array;
    var selectedItemsNum = $('.datatable').DataTable().row('.selected').length;
    for(var i=0;i<selectedItemsNum;i++)
        Items.push($('.datatable').DataTable().$('td', 'tr.selected').eq(1+i*10).text());
    deleteItems(Items);
}

function deleteItems(Items) {}
function addItem(){}
function updateItem(){}


function addRow(v1, v2, v3, v4, v5, v6, v7, v8) {
    if (('0' == v8) || ('Confirmed' == v8)) {
        $('#orderTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7,
            '<span class="label-warning label">Confirmed</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
    }
    if (('1' == v8) || ('Completed' == v8)) {
        $('#orderTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7,
            '<span class="label-success label">Completed</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
    }
    if (('2' == v8) || ('Canceled' == v8)) {
        $('#orderTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7,
            '<span class="label-danger label">Canceled</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
    }
}

function initOrderPage() {
    $.getJSON('../test_json/order.json', function (data) {

        $.each(data, function (i, item) {
            addRow(item.orderId,
                item.userName,
                item.courseName,
                item.courseDate,
                item.trainerName,
                item.orderDate,
                item.price, item.orderStatus);
        });
    });
}

function getItemFromServer(orderId) {

}

function setDetailPage(item) {
    $("#orderId").val(item.orderId);
    $("#userName").val(item.userName);
    $("#courseName").val(item.courseName);
    $("#courseDate").val(item.courseDate);
    $("#trainerName").val(item.trainerName);
    $("#orderDate").val(item.orderDate);
    $("#price").val(item.price);
    $("#gross").val(item.gross);
    $("#orderStatus").val(item.orderStatus);

    disableFields();
}

function disableFields() {
    $("#orderId").attr("readonly", true);
    $("#userName").attr("readonly", true);
    $("#courseName").attr("readonly", true);
    $("#courseDate").attr("readonly", true);
    $("#trainerName").attr("readonly", true);
    $("#orderDate").attr("readonly", true);
    $("#price").attr("readonly", true);
    $("#gross").attr("readonly", true);
    $("#orderStatus").attr("disabled", true);
}

function enableFields() {
    $("#orderId").attr("readonly", false);
    $("#userName").attr("readonly", false);
    $("#courseName").attr("readonly", false);
    $("#courseDate").attr("readonly", false);
    $("#trainerName").attr("readonly", false);
    $("#orderDate").attr("readonly", false);
    $("#price").attr("readonly", false);
    $("#gross").attr("readonly", false);
    $("#orderStatus").attr("disabled", false);
}

function emptyDetailPage(item) {
    $("#orderId").val('');
    $("#userName").val('');
    $("#courseName").val('');
    $("#courseDate").val('');
    $("#trainerName").val('');
    $("#orderDate").val('');
    $("#price").val('');
    $("#gross").val('');
    $("#orderStatus").val('');
}


$(document).ready(function () {
    initOrderPage();
    var html = "<a class='btn btn-danger btndelete'><i class='glyphicon glyphicon-trash icon-white'></i>Delete</a>";
    $(".btns").append(html);


    //click function for every row
    $("#orderTable").on('click', 'tr', function () {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            $('td input', this).eq(0).prop("checked", false);
            $('td input', this).eq(0).attr("checked", false);
        } else {
            $(this).addClass('selected');
            $('td input', this).eq(0).prop("checked", true);
            $('td input', this).eq(0).attr("checked", true);
        }
    });

    //check all checkbox
    $(document).on("click", ".chkAll", function () {
        if (('true' == $(".chkAll").prop('checked')) || ('checked' == $(".chkAll").attr('checked'))) {
            $(".chkAll").prop("checked", false);
            $("[name = chkItem]:checkbox").prop("checked", false);

            $(".chkAll").attr("checked", false);
            $("[name = chkItem]:checkbox").attr("checked", false);
            $("[name = chkItem]:checkbox").parents('tr').removeClass('selected');
        } else {
            $(".chkAll").prop("checked", true);
            $("[name = chkItem]:checkbox").prop("checked", true);

            $(".chkAll").attr("checked", true);
            $("[name = chkItem]:checkbox").attr("checked", true);
            $("[name = chkItem]:checkbox").parents('tr').addClass('selected');
        }
    });

    //view button function
    $(document).on("click", ".viewbtn", function () {

        //mark this row as edited
        $('#orderTable').DataTable().$('tr.edited').removeClass('edited');
        $(this).parents('tr').addClass('edited');

        $("#orderEdit").text("Edit");
        $("#orderDelete").text("Delete");
        var orderId = $(this).parents('tr').children('td').eq(1).text();
        getItemFromServer(orderId);

        disableFields();
        $("#orderInfo").modal('toggle');
    });


    //edit button function
    $(document).on("click", "#orderEdit", function () {
        if ("Add" == $("#orderEdit").text()) {

            addRow($("#orderId").val(), $("#userName").val(), $("#courseName").val(), $("#courseDate").val(), $("#trainerName").val(), $("#orderDate").val(), $("#price").val(), $("#orderStatus").val());

            $("#orderInfo").modal('toggle');
            $("#orderEdit").text("Edit");
            addItem();
        }
        if ("Update" == $("#orderEdit").text()) {
            $('#orderTable').DataTable().$('td', 'tr.edited').eq(1).text($("#orderId").val());
            $('#orderTable').DataTable().$('td', 'tr.edited').eq(2).text($("#userName").val());
            $('#orderTable').DataTable().$('td', 'tr.edited').eq(3).text($("#courseName").val());
            $('#orderTable').DataTable().$('td', 'tr.edited').eq(4).text($("#courseDate").val());
            $('#orderTable').DataTable().$('td', 'tr.edited').eq(5).text($("#trainerName").val());
            $('#orderTable').DataTable().$('td', 'tr.edited').eq(6).text($("#orderDate").val());
            $('#orderTable').DataTable().$('td', 'tr.edited').eq(7).text($("#price").val());
            if ('Completed' == $("#orderStatus").val())
                $('#orderTable').DataTable().$('td', 'tr.edited').eq(8).html('<span class="label-success label">paid</span>');
            if ('Canceled' == $("#orderStatus").val())
                $('#orderTable').DataTable().$('td', 'tr.edited').eq(8).html('<span class="label-danger label">unpaid</span>');
            if ('Confirmed' == $("#orderStatus").val())
                $('#orderTable').DataTable().$('td', 'tr.edited').eq(8).html('<span class="label-warning label">pending</span>');

            $("#orderInfo").modal('toggle');
            $("#orderEdit").text("Edit");
            updateItem();
        }
        if ("Edit" == $("#orderEdit").text()) {
            enableFields();
            $("#orderEdit").text("Update");
        }
    });


    $(document).on("click", ".btnadd", function () {
        $("#orderInfo").modal('toggle');
        enableFields();
        emptyDetailPage();
        $("#orderEdit").text("Add");
        $("#orderDelete").text("Cancel");
    });

});
function initOrderPage() {
    $.getJSON('../test_json/order.json', function (data) {

        var html = '';
        var t = $('#orderTable').DataTable();

        $.each(data, function (i, item) {
            if ("paid" == item.status) {
                t.row.add([
                '<input type="checkbox" name="chkItem" value="' + item.id + '">',
                    item.id,
                    item.username,
                    item.coursename,
                    item.trainer,
                    item.price,
                    '<span class="label-success label">' + item.status + '</span>',
                    '<a class="viewbtn btn btn-success" value="' + item.id + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }

            if ("unpaid" == item.status) {
                t.row.add([
                '<input type="checkbox" name="chkItem" value="' + item.id + '">',
                    item.id,
                    item.username,
                    item.coursename,
                    item.trainer,
                    item.price,
                    '<span class="label-danger label">' + item.status + '</span>',
                    '<a class="viewbtn btn btn-success" value="' + item.id + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }

            if ("pending" == item.status) {
                t.row.add([
                '<input type="checkbox" name="chkItem" value="' + item.id + '">',
                    item.id,
                    item.username,
                    item.coursename,
                    item.trainer,
                    item.price,
                    '<span class="label-warning label">' + item.status + '</span>',
                    '<a class="viewbtn btn btn-success" value="' + item.id + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }
        });

    });

}



$(document).ready(function () {
    initOrderPage();
    var html = "<a class='btn btn-info btnadd'><i class='glyphicon glyphicon-edit icon-white'></i>add</a> <a class='btn btn-danger btndelete'><i class='glyphicon glyphicon-trash icon-white'></i>Delete</a>";
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

        var orderId = $(this).parents('tr').children('td').eq(1).text();
        var userName = $(this).parents('tr').children('td').eq(2).text();
        var courseName = $(this).parents('tr').children('td').eq(3).text();
        var trainer = $(this).parents('tr').children('td').eq(4).text();
        var price = $(this).parents('tr').children('td').eq(5).text();
        var status = $(this).parents('tr').children('td').eq(6).text();

        //mark this row as edited
        $('#orderTable').DataTable().$('tr.edited').removeClass('edited');
        $(this).parents('tr').addClass('edited');

        $("#orderEdit").text("Edit");
        $("#orderDelete").text("Delete");
        if ('' != orderId) {
            $("#orderId").val(orderId);
            $("#userName").val(userName);
            $("#courseName").val(courseName);
            $("#trainerName").val(trainer);
            $("#coursePrice").val(price);
            $("#orderStatus").val(status);
            $("#orderInfo").modal('toggle');

            $("#orderId").attr("readonly", true);
            $("#userName").attr("readonly", true);
            $("#courseName").attr("readonly", true);
            $("#trainerName").attr("readonly", true);
            $("#coursePrice").attr("readonly", true);
            $("#orderStatus").attr("disabled", true);
        }
    });


    //edit button function
    $(document).on("click", "#orderEdit", function () {
        if ("Add" == $("#orderEdit").text()) {

            if ("paid" == $("#orderStatus").val()) {
                $('#orderTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + $("#orderId").val() + '">',
                $("#orderId").val(),
                $("#userName").val(),
                $("#courseName").val(),
                $("#trainerName").val(),
                $("#coursePrice").val(),
                '<span class="label-success label">paid</span>',
                '<a class="viewbtn btn btn-success" value="' + $("#orderId").val() + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }
            
            if ("unpaid" == $("#orderStatus").val()) {
                $('#orderTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + $("#orderId").val() + '">',
                $("#orderId").val(),
                $("#userName").val(),
                $("#courseName").val(),
                $("#trainerName").val(),
                $("#coursePrice").val(),
                '<span class="label-danger label">unpaid</span>',
                '<a class="viewbtn btn btn-success" value="' + $("#orderId").val() + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }
            
            if ("pending" == $("#orderStatus").val()) {
                $('#orderTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + $("#orderId").val() + '">',
                $("#orderId").val(),
                $("#userName").val(),
                $("#courseName").val(),
                $("#trainerName").val(),
                $("#coursePrice").val(),
                '<span class="label-warning label">pending</span>',
                '<a class="viewbtn btn btn-success" value="' + $("#orderId").val() + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }


            $("#orderInfo").modal('toggle');
            $("#orderEdit").text("Edit");
        }
        if ("Update" == $("#orderEdit").text()) {
            $('#orderTable').DataTable().$('td', 'tr.edited').eq(1).text($("#orderId").val());
            $('#orderTable').DataTable().$('td', 'tr.edited').eq(2).text($("#userName").val());
            $('#orderTable').DataTable().$('td', 'tr.edited').eq(3).text($("#courseName").val());
            $('#orderTable').DataTable().$('td', 'tr.edited').eq(4).text($("#trainerName").val());
            $('#orderTable').DataTable().$('td', 'tr.edited').eq(5).text($("#coursePrice").val());
            if ('paid' == $("#orderStatus").val())
                $('#orderTable').DataTable().$('td', 'tr.edited').eq(6).html('<span class="label-success label">paid</span>');
            if ('unpaid' == $("#orderStatus").val())
                $('#orderTable').DataTable().$('td', 'tr.edited').eq(6).html('<span class="label-danger label">unpaid</span>');
            if ('pending' == $("#orderStatus").val())
                $('#orderTable').DataTable().$('td', 'tr.edited').eq(6).html('<span class="label-warning label">pending</span>');

            $("#orderInfo").modal('toggle');
            $("#orderEdit").text("Edit");
        }
        if ("Edit" == $("#orderEdit").text()) {
            $("#orderId").attr("readonly", false);
            $("#userName").attr("readonly", false);
            $("#courseName").attr("readonly", false);
            $("#trainerName").attr("readonly", false);
            $("#coursePrice").attr("readonly", false);
            $("#orderStatus").attr("disabled", false);
            $("#orderEdit").text("Update");
        }
    });

    $(document).on("click", "#orderDelete", function () {
        $('#orderTable').DataTable().row('.edited').remove().draw(false);
        $("#orderInfo").modal('toggle');
    });

    $(document).on("click", ".btnadd", function () {
        $("#orderInfo").modal('toggle');
        $("#orderId").attr("readonly", false);
        $("#userName").attr("readonly", false);
        $("#courseName").attr("readonly", false);
        $("#trainerName").attr("readonly", false);
        $("#coursePrice").attr("readonly", false);
        $("#orderStatus").attr("disabled", false);
        $("#orderId").val('');
        $("#userName").val('');
        $("#courseName").val('');
        $("#trainerName").val('');
        $("#coursePrice").val('');
        $("#orderStatus").val('');
        $("#orderEdit").text("Add");
        $("#orderDelete").text("Cancel");
    });
    $(document).on("click", ".btndelete", function () {
        $('#orderTable').DataTable().row('.selected').remove().draw(false);
    });
});
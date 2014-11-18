function getUserInfo() {}

//function initUserPage() {
//    $.getJSON('info.json', function (data) {
//
//        var html = '';
//        $.each(data, function (i, item) {
//            html += '<tr><td>' + item.username + '</td>' +
//                '<td class="center">' + item.date + '</td>' +
//                '<td class="center">' + item.role + '</td>' +
//                '<td class="center"><span class="label-success label label-default">' + item.status + '</span></td>' +
//                '<td class="center"><a class="btn btn-success"' + item.id + '><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>' +
//                '<a class="btn btn-info"' + item.id + '> <i class="glyphicon glyphicon-edit icon-white"></i>Edit </a>' +
//                '<a class="btn btn-danger"' + item.id + '><i class="glyphicon glyphicon-trash icon-white"></i>Delete</a>' +
//                '</td></tr>';
//        });
//        $("#userTableBody").append(html);
//    });
//
//}

function initUserPage() {
    $.getJSON('../test_json/attendee.json', function (data) {

        var t = $('#userTable').DataTable();

        $.each(data, function (i, item) {
            if ('active' == item.status) {
                t.row.add([
                '<input type="checkbox" name="chkItem" value="' + item.id + '">',
                item.id,
                item.username,
                item.date,
                item.role,
                '<span class="label-success label">' + item.status + '</span>',
                '<a class="viewbtn btn btn-success"value="' + item.id + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }

            if ('inactive' == item.status) {
                t.row.add([
                '<input type="checkbox" name="chkItem" value="' + item.id + '">',
                item.id,
                item.username,
                item.date,
                item.role,
                '<span class="label-default label">' + item.status + '</span>',
                '<a class="viewbtn btn btn-success" value="' + item.id + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }
            if ('pending' == item.status) {
                t.row.add([
                '<input type="checkbox" name="chkItem" value="' + item.id + '">',
                item.id,
                item.username,
                item.date,
                item.role,
                '<span class="label-warning label">' + item.status + '</span>',
                '<a class="viewbtn btn btn-success" value="' + item.id + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }
        });

    });

}



$(document).ready(function () {
    initUserPage();

    //add addbtn & delbtn in the top and bottom
    var html = "<a class='btn btn-info btnadd'><i class='glyphicon glyphicon-edit icon-white'></i>add</a> <a class='btn btn-danger btndelete'><i class='glyphicon glyphicon-trash icon-white'></i>Delete</a>";
    $(".btns").append(html);


    //click function for every row
    $("#userTable").on('click', 'tr', function () {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            $('td input', this).eq(0).prop("checked", false);
            $('td input', this).eq(0).attr("checked", false);
        } else {
            //$('#userTable').DataTable().$('tr.selected').removeClass('selected');
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

        var id = $(this).parents('tr').children('td').eq(1).text();
        var name = $(this).parents('tr').children('td').eq(2).text();
        var role = $(this).parents('tr').children('td').eq(4).text();
        var status = $(this).parents('tr').children('td').eq(5).text();

        //mark this row as edited
        $('#userTable').DataTable().$('tr.edited').removeClass('edited');
        $(this).parents('tr').addClass('edited');

        $("#userEdit").text("Edit");
        $("#userDelete").text("Delete");
        if ('' != id) {
            $("#userId").val(id);
            $("#userName").val(name);
            $("#userRole").val(role);
            $("#userStatus").val(status);
            $("#userInfo").modal('toggle');
            $("#userId").attr("readonly", true);
            $("#userName").attr("readonly", true);
            $("#userRole").attr("disabled", true);
            $("#userStatus").attr("disabled", true);
        }
    });

    //edit button function
    $(document).on("click", "#userEdit", function () {
        if ("Add" == $("#userEdit").text()) {

            if ('active' == $("#userStatus").val()) {
                $('#userTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + $("#userId").val() + '">',
                $("#userId").val(),
                $("#userName").val(),
                '',
                $("#userRole").val(),
                '<span class="label-success label">' + $("#userStatus").val() + '</span>',
                '<a class="viewbtn btn btn-success"value="' + $("#userId").val() + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }

            if ('inactive' == $("#userStatus").val()) {
                $('#userTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + $("#userId").val() + '">',
                $("#userId").val(),
                $("#userName").val(),
                '',
                $("#userRole").val(),
                '<span class="label-default label">' + $("#userStatus").val() + '</span>',
                '<a class="viewbtn btn btn-success"value="' + $("#userId").val() + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }

            if ('pending' == $("#userStatus").val()) {
                $('#userTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + $("#userId").val() + '">',
                $("#userId").val(),
                $("#userName").val(),
                '',
                $("#userRole").val(),
                '<span class="label-warning label">' + $("#userStatus").val() + '</span>',
                '<a class="viewbtn btn btn-success"value="' + $("#userId").val() + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }

            $("#userInfo").modal('toggle');
        }
        if ("Update" == $("#userEdit").text()) {
            alert("TODO: update user infomation to server");
            $('#userTable').DataTable().$('td', 'tr.edited').eq(1).text($("#userId").val());
            $('#userTable').DataTable().$('td', 'tr.edited').eq(2).text($("#userName").val());
            $('#userTable').DataTable().$('td', 'tr.edited').eq(4).text($("#userRole").val());

            if ('active' == $("#userStatus").val())
                $('#userTable').DataTable().$('td', 'tr.edited').eq(5).html('<span class="label-success label">active</span>');
            if ('inactive' == $("#userStatus").val())
                $('#userTable').DataTable().$('td', 'tr.edited').eq(5).html('<span class="label-default label">inactive</span>');
            if ('pending' == $("#userStatus").val())
                $('#userTable').DataTable().$('td', 'tr.edited').eq(5).html('<span class="label-warning label">pending</span>');

            $("#userInfo").modal('toggle');
        }
        if ("Edit" == $("#userEdit").text()) {
            $("#userId").attr("readonly", false);
            $("#userName").attr("readonly", false);
            $("#userRole").attr("disabled", false);
            $("#userStatus").attr("disabled", false);
            $("#userEdit").text("Update");
        }
    });

    //delete button on detail page
    $(document).on("click", "#userDelete", function () {
        if ("Cancel" == $("#userDelete").text()) {
            $("#userInfo").modal('toggle');

        } else {
            $('#userTable').DataTable().row('.edited').remove().draw(false);
            $("#userInfo").modal('toggle');
        }
    });

    //add button function
    $(document).on("click", ".btnadd", function () {
        $("#userInfo").modal('toggle');
        $("#userId").attr("readonly", false);
        $("#userName").attr("readonly", false);
        $("#userRole").attr("disabled", false);
        $("#userStatus").attr("disabled", false);
        $("#userId").val("");
        $("#userName").val("");
        $("#userRole").val("");
        $("#userStatus").val("");
        $("#userEdit").text("Add");
        $("#userDelete").text("Cancel");
    });
    
    //delete button function
    $(document).on("click", ".btndelete", function () {
        $('#userTable').DataTable().row('.selected').remove().draw(false);
    });

});
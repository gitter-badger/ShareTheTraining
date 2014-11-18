function initCoursePage() {
    $.getJSON('../test_json/course.json', function (data) {

        var t = $('#courseTable').DataTable();

        $.each(data, function (i, item) {
            t.row.add([
                '<input type="checkbox" name="chkItem" value="' + item.id + '">',
                item.id,
                item.coursename,
                item.trainer,
                item.price,
            '<a class="viewbtn btn btn-success" value="' + item.id + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        });

    });

}



$(document).ready(function () {
    initCoursePage();
    var html = "<a class='btn btn-info btnadd'><i class='glyphicon glyphicon-edit icon-white'></i>add</a> <a class='btn btn-danger btndelete'><i class='glyphicon glyphicon-trash icon-white'></i>Delete</a>";
    $(".btns").append(html);

    //click function for every row
    $("#courseTable").on('click', 'tr', function () {
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

        var courseId = $(this).parents('tr').children('td').eq(1).text();
        var courseName = $(this).parents('tr').children('td').eq(2).text();
        var trainer = $(this).parents('tr').children('td').eq(3).text();
        var price = $(this).parents('tr').children('td').eq(4).text();

        //mark this row as edited
        $('#courseTable').DataTable().$('tr.edited').removeClass('edited');
        $(this).parents('tr').addClass('edited');

        $("#courseEdit").text("Edit");
        $("#courseDelete").text("Delete");
        if ('' != courseId) {
            $("#courseId").val(courseId);
            $("#courseName").val(courseName);
            $("#trainerName").val(trainer);
            $("#coursePrice").val(price);

            $("#courseInfo").modal('toggle');
            $("#courseId").attr("readonly", true);
            $("#courseName").attr("readonly", true);
            $("#trainerName").attr("readonly", true);
            $("#coursePrice").attr("readonly", true);
        }
    });

    //edit button function
    $(document).on("click", "#courseEdit", function () {
        if ("Add" == $("#courseEdit").text()) {

            $('#courseTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + $("#courseId").val() + '">',
                $("#courseId").val(),
                $("#courseName").val(),
                $("#trainerName").val(),
                $("#coursePrice").val(),
            '<a class="viewbtn btn btn-success" value="' + $("#courseId").val() + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();

            $("#courseInfo").modal('toggle');
        }
        if ("Update" == $("#courseEdit").text()) {

            $('#courseTable').DataTable().$('td', 'tr.edited').eq(1).text($("#courseId").val());
            $('#courseTable').DataTable().$('td', 'tr.edited').eq(2).text($("#courseName").val());
            $('#courseTable').DataTable().$('td', 'tr.edited').eq(3).text($("#trainerName").val());
            $('#courseTable').DataTable().$('td', 'tr.edited').eq(4).text($("#coursePrice").val());

            $("#courseInfo").modal('toggle');
        }
        if ("Edit" == $("#courseEdit").text()) {
            $("#courseId").attr("readonly", false);
            $("#courseName").attr("readonly", false);
            $("#trainerName").attr("readonly", false);
            $("#coursePrice").attr("readonly", false);
            $("#courseEdit").text("Update");
        }
    });

    //delete button on detail page
    $(document).on("click", "#courseDelete", function () {
        if ("Cancel" == $("#courseDelete").text()) {
            $("#courseInfo").modal('toggle');

        } else {
            $('#courseTable').DataTable().row('.edited').remove().draw(false);
            $("#courseInfo").modal('toggle');
        }
    });

    //add button function
    $(document).on("click", ".btnadd", function () {
        $("#courseInfo").modal('toggle');
        $("#courseId").attr("readonly", false);
        $("#courseName").attr("readonly", false);
        $("#trainerName").attr("readonly", false);
        $("#coursePrice").attr("readonly", false);
        $("#courseId").val("");
        $("#courseName").val("");
        $("#trainerName").val("");
        $("#coursePrice").val("");
        $("#courseEdit").text("Add");
        $("#courseDelete").text("Cancel");
    });
    
    //delete button function
    $(document).on("click", ".btndelete", function () {
        $('#courseTable').DataTable().row('.selected').remove().draw(false);
    });   
});
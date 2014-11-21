function initCourseRequestPage() {
jsRoutes.controllers.Application.dashConcreteCourseRequest().ajax({
    success :function (data) {
    	console.log(data);
        var t = $('#requestTable').DataTable();
        $.each(data, function (i, item) {

            if ('approved' == item.status) {
                t.row.add([
                '<input type="checkbox" name="chkItem" value="' + item.concreteCourseId + '">',
                item.concreteCourseId,
                item.courseName,
                item.trainerName,
                item.price,
                '<span class="label-success label">' + item.status + '</span>',
            '<a class="viewbtn btn btn-success" value="' + item.concreteCourseId + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }

            if ('rejected' == item.status) {
                t.row.add([
                '<input type="checkbox" name="chkItem" value="' + item.concreteCourseId + '">',
                item.concreteCourseId,
                item.courseName,
                item.trainerName,
                item.price,
                '<span class="label-danger label">' + item.status + '</span>',
            '<a class="viewbtn btn btn-success" value="' + item.concreteCourseId + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }

            if (0 == item.status) {
                t.row.add([
                '<input type="checkbox" name="chkItem" value="' + item.concreteCourseId + '">',
                item.concreteCourseId,
                item.courseName,
                item.trainerName,
                item.price,
                '<span class="label-warning label">' + item.status + '</span>',
            '<a class="viewbtn btn btn-success" value="' + item.concreteCourseId + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
            }
        });

    }});
}
//function initCourseRequestPage() {
//    $.getJSON('../test_json/request.json', function (data) {
//        var t = $('#requestTable').DataTable();
//        $.each(data, function (i, item) {
//
//            if ('approved' == item.status) {
//                t.row.add([
//                '<input type="checkbox" name="chkItem" value="' + item.id + '">',
//                item.courseId,
//                item.coursename,
//                item.trainerName,
//                item.price,
//                '<span class="label-success label">' + item.status + '</span>',
//            '<a class="viewbtn btn btn-success" value="' + item.id + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
//            }
//
//            if ('rejected' == item.status) {
//                t.row.add([
//                '<input type="checkbox" name="chkItem" value="' + item.id + '">',
//                item.id,
//                item.coursename,
//                item.trainer,
//                item.price,
//                '<span class="label-danger label">' + item.status + '</span>',
//            '<a class="viewbtn btn btn-success" value="' + item.id + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
//            }
//
//            if ('pending' == item.status) {
//                t.row.add([
//                '<input type="checkbox" name="chkItem" value="' + item.id + '">',
//                item.id,
//                item.coursename,
//                item.trainer,
//                item.price,
//                '<span class="label-warning label">' + item.status + '</span>',
//            '<a class="viewbtn btn btn-success" value="' + item.id + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
//            }
//        });
//
//    });
//
//}



$(document).ready(function () {
    initCourseRequestPage();
    var html = "<a class='btn btn-danger btndelete'><i class='glyphicon glyphicon-trash icon-white'></i>Delete</a>";
    $(".btns").append(html);

    //click function for every row
    $("#requestTable").on('click', 'tr', function () {
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
        var status = $(this).parents('tr').children('td').eq(5).text();

        //mark this row as edited
        $('#requestTable').DataTable().$('tr.edited').removeClass('edited');
        $(this).parents('tr').addClass('edited');

        $("#courseRequestEdit").text("Edit");
        if ('' != courseId) {
            $("#courseId").val(courseId);
            $("#courseName").val(courseName);
            $("#trainerName").val(trainer);
            $("#coursePrice").val(price);
            $("#requestStatus").val(status);

            $("#requestInfo").modal('toggle');
            $("#courseId").attr("readonly", true);
            $("#courseName").attr("readonly", true);
            $("#trainerName").attr("readonly", true);
            $("#coursePrice").attr("readonly", true);
            $("#requestStatus").attr("disabled", true);
        }
    });

    $(document).on("click", "#courseRequestApprove", function () {
        $('#requestTable').DataTable().$('td', 'tr.edited').eq(5).html('<span class="label-success label">approved</span>');
        $("#requestInfo").modal('toggle');
    });

    $(document).on("click", "#courseRequestReject", function () {
        $('#requestTable').DataTable().$('td', 'tr.edited').eq(5).html('<span class="label-danger label">rejected</span>');
        $("#requestInfo").modal('toggle');
    });

    //edit button function
    $(document).on("click", "#courseRequestEdit", function () {
        if ("Edit" == $("#courseRequestEdit").text()) {
            $("#courseId").attr("readonly", false);
            $("#courseName").attr("readonly", false);
            $("#trainerName").attr("readonly", false);
            $("#coursePrice").attr("readonly", false);
            $("#requestStatus").attr("disabled", false);
            $("#courseRequestEdit").text("Update");
        } else {
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(1).text($("#courseId").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(2).text($("#courseName").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(3).text($("#trainerName").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(4).text($("#coursePrice").val());
            if ("approved" == $("#requestStatus").val())
                $('#requestTable').DataTable().$('td', 'tr.edited').eq(5).html('<span class="label-success label">approved</span>');
            if ("rejected" == $("#requestStatus").val())
                $('#requestTable').DataTable().$('td', 'tr.edited').eq(5).html('<span class="label-danger label">rejected</span>');
            if ("pending" == $("#requestStatus").val())
                $('#requestTable').DataTable().$('td', 'tr.edited').eq(5).html('<span class="label-warning label">pending</span>');
            $("#requestInfo").modal('toggle');
            $("#courseRequestEdit").text("Edit");
        }
    });

    //delete button on detail page
    $(document).on("click", "#courseRequestDelete", function () {
        $('#requestTable').DataTable().row('.edited').remove().draw(false);
        $("#requestInfo").modal('toggle');
    });


    //delete button function
    $(document).on("click", ".btndelete", function () {
        $('#requestTable').DataTable().row('.selected').remove().draw(false);
    });
});
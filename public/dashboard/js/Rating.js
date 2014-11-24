function deleteEditedItem() {
    var Items = new Array;
    Items.push($('.datatable').DataTable().$('td', 'tr.edited').eq(1).text());
    deleteItems(Items);
}

function deleteSelectedItems() {
    var Items = new Array;
    var selectedItemsNum = $('.datatable').DataTable().row('.selected').length;
    for (var i = 0; i < selectedItemsNum; i++)
        Items.push($('.datatable').DataTable().$('td', 'tr.selected').eq(1 + i * 9).text());
    deleteItems(Items);
}

function deleteItems(Items) {}

function addItem() {}

function updateItem() {}

function addRow(v1, v2, v3, v4, v5, v6, v7) {
    $('#ratingTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7,
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();

}

function initRatingPage() {
    $.getJSON('../test_json/rating.json', function (data) {

        $.each(data, function (i, item) {
            addRow(
                item.reviewId,
                item.concreteCourseId,
                item.courseName,
                item.trainerName,
                item.userName,
                item.courseRating,
                item.trainerRating);
        });

    });

}

function getItemFromServer(reviewId) {

}

function setDetailPage(item) {
    $("#reviewId").val(item.courseId);
    $("#concreteCourseId").val(item.concreteCourseId);
    $("#courseName").val(item.courseName);
    $("#trainerName").val(item.trainerName);
    $("#userName").val(item.userName);
    $("#courseRating").val(item.courseRating);
    $("#trainerRating").val(item.trainerRating);

    for (var i = 0; i < item.courseRatings.length; i++) {
        $("#courseRating" + (i + 1)).val(item.courseRatings[i]);
        $("#trainerRating" + (i + 1)).val(item.trainerRatings[i]);
    }

    disableFields();
}

function disableFields() {
    $("#reviewId").attr("readonly", true);
    $("#concreteCourseId").attr("readonly", true);
    $("#courseName").attr("readonly", true);
    $("#trainerName").attr("readonly", true);
    $("#userName").attr("readonly", true);
    $("#courseRating").attr("readonly", true);
    $("#trainerRating").attr("readonly", true);
}

function enableFields() {
    $("#reviewId").attr("readonly", false);
    $("#concreteCourseId").attr("readonly", false);
    $("#courseName").attr("readonly", false);
    $("#trainerName").attr("readonly", false);
    $("#userName").attr("readonly", false);
    $("#courseRating").attr("readonly", false);
    $("#trainerRating").attr("readonly", false);
}

function emptyDetailPage(item) {
    $("#reviewId").val('');
    $("#concreteCourseId").val('');
    $("#courseName").val('');
    $("#trainerName").val('');
    $("#userName").val('');
    $("#courseRating").val('');
    $("#trainerRating").val('');
}


$(document).ready(function () {
    initRatingPage();
    var html = "<a class='btn btn-danger btndelete'><i class='glyphicon glyphicon-trash icon-white'></i>Delete</a>";
    $(".btns").append(html);

    //click function for every row
    $("#ratingTable").on('click', 'tr', function () {
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

        var reviewId = $(this).parents('tr').children('td').eq(1).text();

        //mark this row as edited
        $('#ratingTable').DataTable().$('tr.edited').removeClass('edited');
        $(this).parents('tr').addClass('edited');

        $("#ratingEdit").text("Edit");
        $("#ratingDelete").text("Delete");

        getItemFromServer(reviewId);
        disableFields();

        $("#ratingInfo").modal('toggle');

    });


    //edit button function
    $(document).on("click", "#ratingEdit", function () {
        if ("Add" == $("#ratingEdit").text()) {
            addRow($("#reviewId").val(),
                $("#concreteCourseId").val(),
                $("#courseName").val(),
                $("#trainerName").val(),
                $("#userName").val());

            $("#ratingInfo").modal('toggle');
            $("#ratingEdit").text("Edit");
            addItem();
        }
        if ("Update" == $("#ratingEdit").text()) {
            $('#ratingTable').DataTable().$('td', 'tr.edited').eq(1).text($("#reviewId").val());
            $('#ratingTable').DataTable().$('td', 'tr.edited').eq(2).text($("#concreteCourseId").val());
            $('#ratingTable').DataTable().$('td', 'tr.edited').eq(3).text($("#courseName").val());
            $('#ratingTable').DataTable().$('td', 'tr.edited').eq(4).text($("#trainerName").val());
            $('#ratingTable').DataTable().$('td', 'tr.edited').eq(5).text($("#userName").val());

            $("#ratingInfo").modal('toggle');
            $("#ratingEdit").text("Edit");
            updateItem();
        }
        if ("Edit" == $("#ratingEdit").text()) {
            enableFields();
            $("#ratingEdit").text("Update");
        }
    });


    //    $(document).on("click", "#ratingDelete", function () {
    //        $('#ratingTable').DataTable().row('.edited').remove().draw(false);
    //        $("#ratingInfo").modal('toggle');
    //    });


    $(document).on("click", ".btnadd", function () {
        alert("add")
    });
    //    $(document).on("click", ".btndelete", function () {
    //        $('#ratingTable').DataTable().row('.selected').remove().draw(false);
    //    });

});
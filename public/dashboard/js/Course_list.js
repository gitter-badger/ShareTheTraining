function deleteEditedItem() {
    var Items = new Array;
    Items.push($('.datatable').DataTable().$('td', 'tr.edited').eq(1).text());
    deleteItems(Items);
}

function deleteSelectedItems() {
    var Items = new Array;
    var selectedItemsNum = $('.datatable').DataTable().row('.selected').length;
    for(var i=0;i<selectedItemsNum;i++)
        Items.push($('.datatable').DataTable().$('td', 'tr.selected').eq(1+i*13).text());
    deleteItems(Items);
}

function deleteItems(Items) {}
function addItem(){
getJsonData();
    //data:JsonData
}
function updateItem(){
    getJsonData();
    //data:JsonData
}

function getNewId(){}



function addRow(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10,v11) {
    if (1 == v10) {
        if ((0 == v11) || ("Pending" == v11)) {
            $('#courseTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, v8, v9,'<span class="label-success label">Yes</span>',
            '<span class="label-warning label">Pending</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }
        if ((1 == v11) || ("Approved" == v11)) {
            $('#courseTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, v8,v9, '<span class="label-success label">Yes</span>',
            '<span class="label-success label">Approved</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }
        if ((2 == v11) || ("Completed" == v11)) {
            $('#courseTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, v8,v9, '<span class="label-success label">Yes</span>',
            '<span class="label-danger label">Completed</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }
    } else {
        if ((0 == v11) || ("Pending" == v11)) {
            $('#courseTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, v8,v9, '<span class="label-danger label">No</span>',
            '<span class="label-warning label">Pending</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }
        if ((1 == v11) || ("Approved" == v11)) {
            $('#courseTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, v8,v9, '<span class="label-danger label">No</span>',
            '<span class="label-success label">Approved</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }
        if ((2 == v11) || ("Completed" == v11)) {
            $('#courseTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, v8,v9, '<span class="label-danger label">No</span>',
            '<span class="label-danger label">Completed</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }

    }
}

function initCoursePage() {
    $.getJSON('../test_json/course.json', function (data) {
        $.each(data, function (i, item) {
            addRow(item.concreteCourseId,
                item.courseName,
                item.trainerName,
                item.courseCategory,
                item.location.city,
                item.location.region,
                item.courseDate,
                item.soldSeat,
                item.minimum,
                item.popular,
                item.status);
        });

    });
}

function getItemFromServer(courseId) {

}

function setDetailPage(item) {
    $("#courseId").val(item.courseId);
    $("#courseName").val(item.courseName);
    $("#eventbriteId").val(item.eventbriteId);
    $("#trainerId").val(item.trainerId);
    $("#trainerName").val(item.trainerName);
    $("#courseCategory").val(item.courseCategory);
    $("#detailedLoc").val(item.detailedLoc);
    $("#city").val(item.city);
    $("#state").val(item.state);
    $("#zipCode").val(item.zipCode);
    $("#soldSeat").val(item.soldSeat);
    $("#minimum").val(item.minimum);
    $("#maximal").val(item.maximal);
    $("#ratingNum").val(item.maximal);
    $("#length").val(item.courseLength);
    $("#courseDate").val(item.courseDate);
    $("#status").val(item.status);
    $("#popular").val(item.popular);
    $("#methods").val(item.methods);

    disableFields();
}

function disableFields() {
    $("#courseId").attr("readonly", true);
    $("#courseName").attr("readonly", true);
    $("#eventbriteId").attr("readonly", true);
    $("#trainerId").attr("readonly", true);
    $("#trainerName").attr("readonly", true);
    $("#courseCategory").attr("readonly", true);
    $("#detailedLoc").attr("readonly", true);
    $("#city").attr("readonly", true);
    $("#state").attr("readonly", true);
    $("#zipCode").attr("readonly", true);
    $("#soldSeat").attr("readonly", true);
    $("#minimum").attr("readonly", true);
    $("#maximal").attr("readonly", true);
    $("#ratingNum").attr("readonly", true);
    $("#courseLength").attr("readonly", true);
    $("#courseDate").attr("readonly", true);
    $("#status").attr("disabled", true);
    $("#popular").attr("disabled", true);
    $("#methods").attr("readonly", true);
}

function enableFields() {
    $("#courseId").attr("readonly", false);
    $("#courseName").attr("readonly", false);
    $("#eventbriteId").attr("readonly", false);
    $("#trainerId").attr("readonly", false);
    $("#trainerName").attr("readonly", false);
    $("#courseCategory").attr("readonly", false);
    $("#detailedLoc").attr("readonly", false);
    $("#city").attr("readonly", false);
    $("#state").attr("readonly", false);
    $("#zipCode").attr("readonly", false);
    $("#soldSeat").attr("readonly", false);
    $("#minimum").attr("readonly", false);
    $("#maximal").attr("readonly", false);
    $("#ratingNum").attr("readonly", false);
    $("#courseLength").attr("readonly", false);
    $("#courseDate").attr("readonly", false);
    $("#status").attr("disabled", false);
    $("#popular").attr("disabled", false);
    $("#methods").attr("readonly", false);
}

function emptyDetailPage(item) {
    $("#courseId").val('');
    $("#courseName").val('');
    $("#eventbriteId").val('');
    $("#trainerId").val('');
    $("#trainerName").val('');
    $("#courseCategory").val('');
    $("#detailedLoc").val('');
    $("#city").val('');
    $("#state").val('');
    $("#zipCode").val('');
    $("#soldSeat").val('');
    $("#minimum").val('');
    $("#maximal").val('');
    $("#ratingNum").val('');
    $("#courseLength").val('');
    $("#courseDate").val('');
    $("#status").val('');
    $("#popular").val('');
    $("#methods").val('');
}

$(document).ready(function () {
    initCoursePage();
    //initSearchBox();
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
        //mark this row as edited
        $('#courseTable').DataTable().$('tr.edited').removeClass('edited');
        $(this).parents('tr').addClass('edited');

        $("#courseEdit").text("Edit");
        $("#courseDelete").text("Delete");

        //set detail infomation into detail page
        var courseId = $(this).parents('tr').children('td').eq(1).text();
        getItemFromServer(courseId);

        disableFields();
        $("#courseInfo").modal('toggle');
    });

    //edit button function
    $(document).on("click", "#courseEdit", function () {
        if ("Add" == $("#courseEdit").text()) {

            addRow($("#courseId").val(),
                $("#courseName").val(),
                $("#trainerName").val(),
                $("#courseCategory").val(),
                $("#city").val() + $("#state").val(),
                $("#courseDate").val(),
                $("#soldSeat").val(),
                $("#minimum").val(),
                $("#popular").val());
            $("#courseInfo").modal('toggle');
            addItem();
        }
        if ("Update" == $("#courseEdit").text()) {

            $('#courseTable').DataTable().$('td', 'tr.edited').eq(1).text($("#courseId").val());
            $('#courseTable').DataTable().$('td', 'tr.edited').eq(2).text($("#courseName").val());
            $('#courseTable').DataTable().$('td', 'tr.edited').eq(3).text($("#trainerName").val());
            $('#courseTable').DataTable().$('td', 'tr.edited').eq(4).text($("#courseCategory").val());
            $('#courseTable').DataTable().$('td', 'tr.edited').eq(5).text($("#city").val());
            $('#courseTable').DataTable().$('td', 'tr.edited').eq(6).text($("#state").val());
            $('#courseTable').DataTable().$('td', 'tr.edited').eq(7).text($("#courseDate").val());
            $('#courseTable').DataTable().$('td', 'tr.edited').eq(8).text($("#soldSeat").val());
            $('#courseTable').DataTable().$('td', 'tr.edited').eq(9).text($("#minimum").val());
            if ('Yes' == $("#popular").val())
                $('#courseTable').DataTable().$('td', 'tr.edited').eq(9).html('<span class="label-success label">Yes</span>');
            else
                $('#courseTable').DataTable().$('td', 'tr.edited').eq(9).html('<span class="label-danger label">No</span>');

            if ('Pending' == $("#status").val())
                $('#courseTable').DataTable().$('td', 'tr.edited').eq(10).html('<span class="label-warning label">Pending</span>');
            if ('Approved' == $("#status").val())
                $('#courseTable').DataTable().$('td', 'tr.edited').eq(10).html('<span class="label-success label">Approved</span>');
            if ('Completed' == $("#status").val())
                $('#courseTable').DataTable().$('td', 'tr.edited').eq(10).html('<span class="label-danger label">Completed</span>');

            $("#courseInfo").modal('toggle');
            updateItem();
        }
        if ("Edit" == $("#courseEdit").text()) {
            enableFields();
            $("#courseEdit").text("Update");
        }
    });

    //add button function
    $(document).on("click", ".btnadd", function () {
        emptyDetailPage();
        getNewId();
        enableFields();
        $("#courseInfo").modal('toggle');
        $("#courseEdit").text("Add");
        $("#courseDelete").text("Cancel");
    });

//    $(document).on("click", "#addDates", function () {
//        $("#dates").append($("#datepicker").val() + " ");
//        if ('' != $("#datepicker").val())
//            dates.push($("#datepicker").val());
//    });
//    $(document).on("click", "#clearDates", function () {
//        $("#dates").text("");
//        dates=[];
//    });


});
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

function deleteItems(Items) {
    alert("hehe");
    getJsonData();
    jsRoutes.controllers.Application.dashCourseDelete().ajax({
        data : JsonData,
        success : {}
    });
    
    
}

function updateItem() {
	alert("hehe");
	getJsonData();
	jsRoutes.controllers.Application.dashCourseUpdate().ajax({
		data : JsonData,
		suceess : {}
	});
}

function addRow(v1, v2, v3, v4, v5, v6, v7, v8) {
    if ('0' == v8) {
        $('#requestTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7,
            '<span class="label-warning label">Pending</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
    }
    if ('1' == v8) {
        $('#requestTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7,
            '<span class="label-success label">Approved</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
    }
    if ('2' == v8) {
        $('#requestTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7,
            '<span class="label-danger label">Rejected</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
    }
}

function initCourseRequestPage() {
    console.log("hehe");
    var urlParameter = getParameter();
    if((0!=urlParameter.length)&&("1"==urlParameter[0]["new"]))
    {}
    else{
    jsRoutes.controllers.Application.dashCourse().ajax(
        {
            success :  function (data) {
               console.log(data);
            $.each(data, function (i, item) {
                addRow(
                    item.courseId,
                    item.courseName,
                    item.trainerName,
                    item.trainerEmail,
                    item.minimum,
                    item.maximum,
                    item.price, 
                    item.status);
            });
            initSearchSelect();
         }});
    }
}

function getItemFromServer(courseId) {
    jsRoutes.controllers.Application.dashCourseDetail(courseId).ajax({
        success : function(data) {
            console.log(data);
            setDetailPage(data);
        }
    });
}

function setDetailPage(item) {
    $("#courseId").val(item.courseId);
    $("#courseName").val(item.courseName);
    $("#courseCategory").val(item.courseCategory);
    $("#courseDesc").val(item.courseDesc);
    $("#trainerId").val(item.trainerId);
    $("#trainerName").val(item.trainerName);
    $("#methods").val(item.methods);
    $("#detailedLoc").val(item.detailedLoc);
    $("#city").val(item.city);
    $("#state").val(item.state);
    $("#zipCode").val(item.zipCode);
    $("#length").val(item.courseLength);
    $("#courseDate").val(item.courseDate);
    $("#minimum").val(item.minimum);
    $("#maximal").val(item.maximal);
    $("#price").val(item.price);
    $("#status").val(item.status);

    disableFields();
}

function disableFields() {
    $("#courseId").attr("readonly", true);
    $("#courseName").attr("readonly", true);
    $("#courseCategory").attr("readonly", true);
    $("#courseDesc").attr("readonly", true);
    $("#trainerId").attr("readonly", true);
    $("#trainerName").attr("readonly", true);
    $("#methods").attr("readonly", true);
    $("#detailedLoc").attr("readonly", true);
    $("#city").attr("readonly", true);
    $("#state").attr("readonly", true);
    $("#zipCode").attr("readonly", true);
    $("#minimum").attr("readonly", true);
    $("#maximal").attr("readonly", true);
    $("#courseLength").attr("readonly", true);
    $("#courseDate").attr("readonly", true);
    $("#price").attr("readonly", true);
    $("#status").attr("disabled", true);
}

function enableFields() {
    $("#courseId").attr("readonly", false);
    $("#courseName").attr("readonly", false);
    $("#courseCategory").attr("readonly", false);
    $("#courseDesc").attr("readonly", false);
    $("#trainerId").attr("readonly", false);
    $("#trainerName").attr("readonly", false);
    $("#methods").attr("readonly", false);
    $("#detailedLoc").attr("readonly", false);
    $("#city").attr("readonly", false);
    $("#state").attr("readonly", false);
    $("#zipCode").attr("readonly", false);
    $("#courseLength").attr("readonly", false);
    $("#courseDate").attr("readonly", false);
    $("#minimum").attr("readonly", false);
    $("#maximal").attr("readonly", false);
    $("#price").attr("readonly", false);
    $("#status").attr("disabled", false);
}

function emptyDetailPage(item) {
    $("#courseId").val('');
    $("#courseName").val('');
    $("#courseCategory").val('');
    $("#courseDesc").val('');
    $("#trainerId").val('');
    $("#trainerName").val('');
    $("#methods").val('');
    $("#detailedLoc").val('');
    $("#city").val('');
    $("#state").val('');
    $("#zipCode").val('');
    $("#courseLength").val('');
    $("#courseDate").val('');
    $("#minimum").val('');
    $("#maximal").val('');
    $("#price").val('');
    $("#status").val('');
}


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

        //mark this row as edited
        $('#requestTable').DataTable().$('tr.edited').removeClass('edited');
        $(this).parents('tr').addClass('edited');

        $("#courseRequestEdit").text("Edit");

        var courseId = $(this).parents('tr').children('td').eq(1).text();
        getItemFromServer(courseId);

        disableFields();
        $("#requestInfo").modal('toggle');
    });

    $(document).on("click", "#courseRequestApprove", function () {
        $('#requestTable').DataTable().$('td', 'tr.edited').eq(8).html('<span class="label-success label">approved</span>');
        //todo add this course into course list
        $("#requestInfo").modal('toggle');
    });

    $(document).on("click", "#courseRequestReject", function () {
        $('#requestTable').DataTable().$('td', 'tr.edited').eq(8).html('<span class="label-danger label">rejected</span>');
        $("#requestInfo").modal('toggle');
    });

    $(document).on("click", "#courseRequestPend", function () {
        $('#requestTable').DataTable().$('td', 'tr.edited').eq(8).html('<span class="label-warning label">Pending</span>');
        $("#requestInfo").modal('toggle');
    });

    //edit button function
    $(document).on("click", "#courseRequestEdit", function () {
        if ("Edit" == $("#courseRequestEdit").text()) {
            enableFields();
            $("#courseRequestEdit").text("Update");
        } else {
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(1).text($("#courseId").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(2).text($("#courseName").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(3).text($("#trainerName").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(4).text($("#courseCategory").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(5).text($("#minimum").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(6).text($("#maximal").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(7).text($("#price").val());

            if ("approved" == $("#requestStatus").val())
                $('#requestTable').DataTable().$('td', 'tr.edited').eq(8).html('<span class="label-success label">approved</span>');
            if ("rejected" == $("#requestStatus").val())
                $('#requestTable').DataTable().$('td', 'tr.edited').eq(8).html('<span class="label-danger label">rejected</span>');
            if ("pending" == $("#requestStatus").val())
                $('#requestTable').DataTable().$('td', 'tr.edited').eq(8).html('<span class="label-warning label">pending</span>');
            $("#requestInfo").modal('toggle');

            $("#courseRequestEdit").text("Edit");
            updateItem();
        }
    });



    //    //delete button on detail page
    //    $(document).on("click", "#courseRequestDelete", function () {
    //        $('#requestTable').DataTable().row('.edited').remove().draw(false);
    //        $("#requestInfo").modal('toggle');
    //    });
    //
    //
    //    //delete button function
    //    $(document).on("click", ".btndelete", function () {
    //        $('#requestTable').DataTable().row('.selected').remove().draw(false);
    //    });

});
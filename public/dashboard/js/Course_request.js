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
    getJsonData();
    jsRoutes.controllers.Application.dashConcreteCourseDelete().ajax({
        data : JsonData,
        success : function(data){
        	if(false==data.result)
        		errorMsgAlert('delete failed, there may be subcourse still exist');
        }
    });
    
 
}
function addItem(){
	getJsonData();
	jsRoutes.controllers.Application.dashConcreteCourseAdd().ajax({
		data : JsonData,
		suceess : {}
	});
	
}

function updateItem() {
	getJsonData();
	jsRoutes.controllers.Application.dashCourseUpdate().ajax({
		data : JsonData,
		suceess : {}
	});
}

function addRow(v1, v2, v3, v4, v5, v6, v7, v8, v9) {
    if (1 == v8) {
        if ('0' == v9) {
            $('#requestTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, "Yes",
            '<span class="label-warning label">Pending</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }
        if ('1' == v9) {
            $('#requestTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, "Yes",
            '<span class="label-success label">Approved</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }
        if ('2' == v9) {
            $('#requestTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, "Yes",
            '<span class="label-danger label">Rejected</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }
    } else {
    if ('0' == v9) {
            $('#requestTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, "No",
            '<span class="label-warning label">Pending</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }
        if ('1' == v9) {
            $('#requestTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, "No",
            '<span class="label-success label">Approved</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }
        if ('2' == v9) {
            $('#requestTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, "No",
            '<span class="label-danger label">Rejected</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }}
}

function initCourseRequestPage() {
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
                    item.courseCategory,
                    item.minimum,
                    item.maximum,
                    item.price, 
                    item.popular,
                    item.status);
            });
            initSearchSelect();
            addDBtn();
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
    $("#trainerEmail").val(item.trainerEmail);
    $("#methods").val(item.methods);
    $("#detailedLoc").val(item.detailedLoc);
    $("#city").val(item.location.city);
    $("#state").val(item.location.region);
    $("#zipCode").val(item.zipCode);
    $("#length").val(item.courseLength);
    $("#courseDate").val(item.courseDate);
    $("#minimum").val(item.minimum);
    $("#maximum").val(item.maximum);
    $("#price").val(item.price);
    $("#popular").val(item.popular);
    $("#status").val(item.status);
    $("#displayRating").val(item.displayRating);
    var len=item.courseDates.length;
    for(var i=0;i<len;i++)
    	{
    	$("#dates").append(item.courseDates[i]+" ");
    	dates.push(item.courseDates[i]);
    	}
    
    keyPoints = item.keyPoints.split(',');
    len=keyPoints.length;
    for(var i=0;i<len;i++)
    	{
    	$("#keyPoints").append(keyPoints[i]+" ");
    	}
    

    disableFields();
}

function disableFields() {
    $("#courseId").attr("readonly", true);
    $("#courseName").attr("readonly", true);
    $("#courseCategory").attr("readonly", true);
    $("#courseDesc").attr("readonly", true);
    $("#trainerId").attr("readonly", true);
    $("#trainerName").attr("readonly", true);
    $("#trainerEmail").attr("readonly", true);
    $("#methods").attr("readonly", true);
    $("#detailedLoc").attr("readonly", true);
    $("#city").attr("readonly", true);
    $("#state").attr("readonly", true);
    $("#zipCode").attr("readonly", true);
    $("#minimum").attr("readonly", true);
    $("#maximum").attr("readonly", true);
    $("#courseLength").attr("readonly", true);
    $("#courseDate").attr("readonly", true);
    $("#price").attr("readonly", true);
    $("#status").attr("disabled", true);
    $("#popular").attr("disabled", true);
    $("#displayRating").attr("disabled", true);
    $("#keyPoint").attr("readonly", true);
    $("#datepicker").attr("readonly", true);
}

function enableFields() {
    $("#courseId").attr("readonly", false);
    $("#courseName").attr("readonly", false);
    $("#courseCategory").attr("readonly", false);
    $("#courseDesc").attr("readonly", false);
    $("#trainerId").attr("readonly", false);
    $("#trainerName").attr("readonly", false);
    $("#trainerEmail").attr("readonly", false);
    $("#methods").attr("readonly", false);
    $("#detailedLoc").attr("readonly", false);
    $("#city").attr("readonly", false);
    $("#state").attr("readonly", false);
    $("#zipCode").attr("readonly", false);
    $("#courseLength").attr("readonly", false);
    $("#courseDate").attr("readonly", false);
    $("#minimum").attr("readonly", false);
    $("#maximum").attr("readonly", false);
    $("#price").attr("readonly", false);
    $("#status").attr("disabled", false);
    $("#popular").attr("disabled", false);
    $("#displayRating").attr("disabled", false);
    $("#keyPoint").attr("readonly", false);
    $("#datepicker").attr("readonly", false);
}

function disableFieldsForAdd() {
    $("#courseId").attr("readonly", true);
    $("#courseName").attr("readonly", true);
    $("#courseCategory").attr("readonly", true);
    $("#courseDesc").attr("readonly", true);
    $("#trainerId").attr("readonly", true);
    $("#trainerName").attr("readonly", true);
    $("#trainerEmail").attr("readonly", true);
    $("#methods").attr("readonly", true);
    $("#minimum").attr("readonly", true);
    $("#maximum").attr("readonly", true);
    $("#price").attr("readonly", true);
    $("#popular").attr("disabled", true);
    $("#displayRating").attr("disabled", true);
    $("#keyPoint").attr("readonly", true);
    
}

function enableFieldsForAdd() {
    
    $("#detailedLoc").attr("readonly", false);
    $("#city").attr("readonly", false);
    $("#state").attr("readonly", false);
    $("#zipCode").attr("readonly", false);
    $("#courseLength").attr("readonly", false);
    $("#status").attr("disabled", false);
    $("#datepicker").attr("readonly", false);
}
function disableFieldsForEdit() {
    
    $("#detailedLoc").attr("readonly", true);
    $("#city").attr("readonly", true);
    $("#state").attr("readonly", true);
    $("#zipCode").attr("readonly", true);
    
    $("#courseLength").attr("readonly", true);
    
    $("#status").attr("disabled", true);
    $("#datepicker").attr("readonly", true);
}

function enableFieldsForEdit() {
    $("#courseId").attr("readonly", false);
    $("#courseName").attr("readonly", false);
    $("#courseCategory").attr("readonly", false);
    $("#courseDesc").attr("readonly", false);
    $("#trainerId").attr("readonly", false);
    $("#trainerName").attr("readonly", false);
    $("#trainerEmail").attr("readonly", false);
    $("#methods").attr("readonly", false);
    
    $("#minimum").attr("readonly", false);
    $("#maximum").attr("readonly", false);
    $("#price").attr("readonly", false);
    
    $("#popular").attr("disabled", false);
    $("#displayRating").attr("disabled", false);
    $("#keyPoint").attr("readonly", false);
}
function emptyDetailPage(item) {
    $("#courseId").val('');
    $("#courseName").val('');
    $("#courseCategory").val('');
    $("#courseDesc").val('');
    $("#trainerId").val('');
    $("#trainerName").val('');
    $("#trainerEmail").val('');
    $("#methods").val('');
    $("#detailedLoc").val('');
    $("#city").val('');
    $("#state").val('');
    $("#zipCode").val('');
    $("#courseLength").val('');
    $("#courseDate").val('');
    $("#minimum").val('');
    $("#maximum").val('');
    $("#price").val('');
    $("#status").val('');
    $("#popular").val('');
    $("#displayRating").val('');
}


$(document).ready(function () {
    initCourseRequestPage();
    //var html = "<a class='btn btn-danger btndelete'><i class='glyphicon glyphicon-trash icon-white'></i>Delete</a>";
    //$(".btns").append(html);

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
        $("#courseRequestApprove").text("Add");
        var courseId = $(this).parents('tr').children('td').eq(1).text();
        getItemFromServer(courseId);

        disableFields();
        $("#requestInfo").modal('toggle');
    });

    $(document).on("click", "#courseRequestApprove", function () {
    	if("Add" == $("#courseRequestApprove").text())
    		{
    		
    		enableFieldsForAdd();
    		disableFieldsForAdd();
    		$("#courseRequestApprove").text("Approve");
    		}
    	else
    		{
        $('#requestTable').DataTable().$('td', 'tr.edited').eq(9).html('<span class="label-success label">approved</span>');
        //todo add this course into course list
        addItem();
        $("#requestInfo").modal('toggle');
    		}
    });

    $(document).on("click", "#courseRequestReject", function () {
        $('#requestTable').DataTable().$('td', 'tr.edited').eq(9).html('<span class="label-danger label">rejected</span>');
        $("#requestInfo").modal('toggle');
    });

    $(document).on("click", "#courseRequestPend", function () {
        $('#requestTable').DataTable().$('td', 'tr.edited').eq(9).html('<span class="label-warning label">Pending</span>');
        $("#requestInfo").modal('toggle');
    });

    //edit button function
    $(document).on("click", "#courseRequestEdit", function () {
        if ("Edit" == $("#courseRequestEdit").text()) {
            enableFieldsForEdit();
            disableFieldsForEdit();
            $("#courseRequestEdit").text("Update");
        } else {
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(1).text($("#courseId").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(2).text($("#courseName").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(3).text($("#trainerName").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(4).text($("#courseCategory").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(5).text($("#minimum").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(6).text($("#maximum").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(7).text($("#price").val());
            $('#requestTable').DataTable().$('td', 'tr.edited').eq(8).text($("#popular").val());

            if ("approved" == $("#requestStatus").val())
                $('#requestTable').DataTable().$('td', 'tr.edited').eq(9).html('<span class="label-success label">approved</span>');
            if ("rejected" == $("#requestStatus").val())
                $('#requestTable').DataTable().$('td', 'tr.edited').eq(9).html('<span class="label-danger label">rejected</span>');
            if ("pending" == $("#requestStatus").val())
                $('#requestTable').DataTable().$('td', 'tr.edited').eq(9).html('<span class="label-warning label">pending</span>');
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
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

function deleteItems(Items) {
	 getJsonData();
	    jsRoutes.controllers.Application.dashConcreteCourseDelete().ajax({
	        data : JsonData,
	        success : {}
	    });
}

function addItem(){
getJsonData();
    //data:JsonData
}
function updateItem(){
	getJsonData();
	jsRoutes.controllers.Application.dashConcreteCourseUpdate().ajax({
		data : JsonData,
		suceess : {}
	});
}

function getNewId(){}



function addRow(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10) {
    
        if ((0 == v10)|| ("Pending" == v10)) {
            $('#courseTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, v8, v9,
            '<span class="label-warning label">Pending</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }
        if ((1 == v10) || ("Approved" == v10)) {
            $('#courseTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, v8,v9, 
            '<span class="label-success label">Approved</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }
        if ((2 == v10) || ("Completed" == v10)) {
            $('#courseTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6, v7, v8,v9, 
            '<span class="label-danger label">Completed</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
        }
   
}

function initCoursePage() {
    var urlParameter = getParameter();
    if((0!=urlParameter.length)&&("1"==urlParameter[0]["new"]))
    {}
    else{
    	  jsRoutes.controllers.Application.dashConcreteCourse().ajax(
     {	success :  function (data){
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
                item.status);
        });
        initSearchSelect();
        addDBtn();
    }});
    }
}

function getItemFromServer(courseId) {
	 jsRoutes.controllers.Application.dashConcreteCourseDetail(courseId).ajax({
	        success : function(data) {
	            console.log(data);
	            setDetailPage(data);
	        }
	    });
}

function setDetailPage(item) {
    $("#courseId").val(item.concreteCourseId);
    $("#courseName").val(item.courseName);
    $("#eventbriteId").val(item.eventbriteId);
    $("#trainerId").val(item.trainerId);
    $("#trainerName").val(item.trainerName);
    $("#trainerEmail").val(item.trainerEmail);
    $("#courseCategory").val(item.courseCategory);
    $("#detailedLoc").val(item.detailedLoc);
    $("#city").val(item.location.city);
    $("#state").val(item.location.region);
    $("#zipCode").val(item.zipCode);
    $("#soldSeat").val(item.soldSeat);
    $("#minimum").val(item.minimum);
    $("#maximum").val(item.maximum);
    $("#ratingNum").val(item.ratingNum);
    $("#length").val(item.courseLength);
    $("#courseDate").val(item.courseDate);
    $("#status").val(item.status);
    $("#popular").val(item.popular);
    $("#methods").val(item.methods);
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
    $("#eventbriteId").attr("readonly", true);
    $("#trainerId").attr("readonly", true);
    $("#trainerName").attr("readonly", true);
    $("#trainerEmail").attr("readonly", true);
    $("#courseCategory").attr("readonly", true);
    $("#detailedLoc").attr("readonly", true);
    $("#city").attr("readonly", true);
    $("#state").attr("readonly", true);
    $("#zipCode").attr("readonly", true);
    $("#soldSeat").attr("readonly", true);
    $("#minimum").attr("readonly", true);
    $("#maximum").attr("readonly", true);
    $("#ratingNum").attr("readonly", true);
    $("#courseLength").attr("readonly", true);
    $("#courseDate").attr("readonly", true);
    $("#status").attr("disabled", true);
    $("#popular").attr("disabled", true);
    $("#displayRating").attr("disabled", true);
    $("#methods").attr("readonly", true);
    $("#datepicker").attr("readonly",true);
}

function enableFields() {
    
    $("#detailedLoc").attr("readonly", false);
    $("#city").attr("readonly", false);
    $("#state").attr("readonly", false);
    $("#zipCode").attr("readonly", false);
    
    $("#courseLength").attr("readonly", false);
    $("#status").attr("disabled", false);
    $("#datepicker").attr("readonly",false);
    $("#displayRating").attr("disabled", false);
    
}

function emptyDetailPage(item) {
    $("#courseId").val('');
    $("#courseName").val('');
    $("#eventbriteId").val('');
    $("#trainerId").val('');
    $("#trainerName").val('');
    $("#trainerEmail").val('');
    $("#courseCategory").val('');
    $("#detailedLoc").val('');
    $("#city").val('');
    $("#state").val('');
    $("#zipCode").val('');
    $("#soldSeat").val('');
    $("#minimum").val('');
    $("#maximum").val('');
    $("#ratingNum").val('');
    $("#courseLength").val('');
    $("#courseDate").val('');
    $("#status").val('');
    $("#popular").val('');
    $("#methods").val('');
    $("#displayRating").val('');
}

$(document).ready(function () {
    initCoursePage();
    //initSearchSelect();
    //initSearchBox();
    //var html = "<a class='btn btn-info btnadd'><i class='glyphicon glyphicon-edit icon-white'></i>add</a> <a class='btn btn-danger btndelete'><i class='glyphicon glyphicon-trash icon-white'></i>Delete</a>";
    //$(".btns").append(html);

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
           
            if ('Pending' == $("#status").val())
                $('#courseTable').DataTable().$('td', 'tr.edited').eq(9).html('<span class="label-warning label">Pending</span>');
            if ('Approved' == $("#status").val())
                $('#courseTable').DataTable().$('td', 'tr.edited').eq(9).html('<span class="label-success label">Approved</span>');
            if ('Completed' == $("#status").val())
                $('#courseTable').DataTable().$('td', 'tr.edited').eq(9).html('<span class="label-danger label">Completed</span>');

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
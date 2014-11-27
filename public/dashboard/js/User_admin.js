function deleteEditedItem() {
    var Items = new Array;
    Items.push($('.datatable').DataTable().$('td', 'tr.edited').eq(1).text());
    deleteItems(Items);
}
function deleteSelectedItems(){
       var Items = new Array;
    var selectedItemsNum = $('.datatable').DataTable().row('.selected').length;
    for(var i=0;i<selectedItemsNum;i++)
        Items.push($('.datatable').DataTable().$('td', 'tr.selected').eq(1+i*9).text());
    deleteItems(Items);
}
function deleteItems(Items){
	
}

function addItem(){
	getJsonData();
	addLocation();
	jsRoutes.controllers.Application.dashAdminAdd().ajax({
		data : JsonData,
		suceess : {}
	});
}

function updateItem(){
	getJsonData();
	addLocation();
	jsRoutes.controllers.Application.dashAdminUpdate().ajax({
		data : JsonData,
		suceess : {}
	});
}

function getNewId(){}

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
function addRow(v1, v2, v3, v4, v5, v6, v7) {
    if (('0' == v7) || ('Inactive' == v7)) {
        $('#userTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6,
            '<span class="label-danger label">Inactive</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
    }
    if (('1' == v7) || ('Active' == v7)) {
        $('#userTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + v1 + '">',
                v1, v2, v3, v4, v5, v6,
            '<span class="label-success label">Active</span>',
            '<a class="viewbtn btn btn-success" value="' + v1 + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>']).draw();
    }
}

function initUserPage() {
	 var urlParameter = getParameter();
	    if((0!=urlParameter.length)&&("1"==urlParameter[0]["new"]))
	    {}
	    else{
	    jsRoutes.controllers.Application.dashAdmin().ajax(
	        {
	            success :  function (data) {
	               console.log(data);

        $.each(data, function (i, item) {
            addRow(item.id,
                item.name,
                item.email,
                item.cellPhone,
                item.location.region,
                item.location.city, item.userStatus);
        });
        initSearchSelect();
        addDBtn();

    }});
}
}

function getItemFromServer(id) {
	jsRoutes.controllers.Application.dashAdminDetail(orderId).ajax({
        success : function(data) {
            console.log(data);
            setDetailPage(data);
        }
    });
}

function setDetailPage(item) {
    $("#id").val(item.id);
    $("#name").val(item.name);
    $("#email").val(item.eventbriteId);
    $("#cellPhone").val(item.trainerId);
    $("#state").val(item.location.region);
    $("#city").val(item.location.city);
    $("#detailedLoc").val(item.location.detailedLoc);
    $("#zipCode").val(item.location.zipCode);
    $("#userStatus").val(item.userStatus);
    
    $("#userName").val(item.userName);
    $("#password").val(item.password);
    $("#phone").val(item.phone);

    disableFields();
}

function disableFields() {
    $("#id").attr("readonly", true);
    $("#name").attr("readonly", true);
    $("#email").attr("readonly", true);
    $("#cellPhone").attr("readonly", true);
    $("#state").attr("readonly", true);
    $("#city").attr("readonly", true);
    $("#detailedLoc").attr("readonly", true);
    $("#zipCode").attr("readonly", true);
    $("#userStatus").attr("disabled", true);
    $("#userName").attr("readonly", true);
    $("#password").attr("readonly", true);
    $("#phone").attr("readonly", true);
}

function enableFields() {
    $("#id").attr("readonly", false);
    $("#name").attr("readonly", false);
    $("#email").attr("readonly", false);
    $("#cellPhone").attr("readonly", false);
    $("#state").attr("readonly", false);
    $("#city").attr("readonly", false);
    $("#detailedLoc").attr("readonly", false);
    $("#zipCode").attr("readonly", false);
    $("#userStatus").attr("disabled", false);
    $("#userName").attr("readonly", false);
    $("#password").attr("readonly", false);
    $("#phone").attr("readonly", false);
}

function emptyDetailPage(item) {
    $("#id").val('');
    $("#name").val('');
    $("#email").val('');
    $("#cellPhone").val('');
    $("#state").val('');
    $("#city").val('');
    $("#detailedLoc").val('');
    $("#zipCode").val('');
    $("#userStatus").val('');
    $("#userName").val('');
    $("#password").val('');
    $("#phone").val('');
}


$(document).ready(function () {
    initUserPage();

    //add addbtn & delbtn in the top and bottom
//    var html = "<a class='btn btn-info btnadd'><i class='glyphicon glyphicon-edit icon-white'></i>add</a> <a class='btn btn-danger btndelete'><i class='glyphicon glyphicon-trash icon-white'></i>Delete</a>";
//    $(".btns").append(html);


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

        //mark this row as edited
        $('#userTable').DataTable().$('tr.edited').removeClass('edited');
        $(this).parents('tr').addClass('edited');

        $("#userEdit").text("Edit");
        $("#userDelete").text("Delete");

        getItemFromServer(id);

        disableFields();
        $("#userInfo").modal('toggle');

    });

    //edit button function
    $(document).on("click", "#userEdit", function () {
        if ("Add" == $("#userEdit").text()) {

            addRow($("#id").val(),
                $("#name").val(),
                $("#email").val(),
                $("#cellPhone").val(),
                $("#state").val(),
                $("#city").val(),
                $("#userStatus").val());

            $("#userInfo").modal('toggle');
            addItem();
        }
        if ("Update" == $("#userEdit").text()) {
            alert("TODO: update user infomation to server");
            $('#userTable').DataTable().$('td', 'tr.edited').eq(1).text($("#id").val());
            $('#userTable').DataTable().$('td', 'tr.edited').eq(2).text($("#name").val());
            $('#userTable').DataTable().$('td', 'tr.edited').eq(3).text($("#email").val());
            $('#userTable').DataTable().$('td', 'tr.edited').eq(4).text($("#cellPhone").val());
            $('#userTable').DataTable().$('td', 'tr.edited').eq(5).text($("#state").val());
            $('#userTable').DataTable().$('td', 'tr.edited').eq(6).text($("#city").val());

            if ('Active' == $("#userStatus").val())
                $('#userTable').DataTable().$('td', 'tr.edited').eq(7).html('<span class="label-success label">active</span>');
            if ('Inactive' == $("#userStatus").val())
                $('#userTable').DataTable().$('td', 'tr.edited').eq(7).html('<span class="label-danger label">inactive</span>');
         

            $("#userInfo").modal('toggle');
            updateItem();
        }
        if ("Edit" == $("#userEdit").text()) {
           enableFields();
            $("#userEdit").text("Update");
        }
    });

//    //delete button on detail page
//    $(document).on("click", "#userDelete", function () {
//        if ("Cancel" == $("#userDelete").text()) {
//            $("#userInfo").modal('toggle');
//
//        } else {
//            $('#userTable').DataTable().row('.edited').remove().draw(false);
//            $("#userInfo").modal('toggle');
//        }
//    });

    //add button function
    $(document).on("click", ".btnadd", function () {
        $("#userInfo").modal('toggle');
        enableFields();
        emptyDetailPage();
        getNewId();
        $("#userEdit").text("Add");
        $("#userDelete").text("Cancel");
    });

//    //delete button function
//    $(document).on("click", ".btndelete", function () {
//        $('#userTable').DataTable().row('.selected').remove().draw(false);
//    });

});
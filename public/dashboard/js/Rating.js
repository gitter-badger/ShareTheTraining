function initRatingPage() {
    $.getJSON('../test_json/rating.json', function (data) {

        var html = '';
        var t = $('#ratingTable').DataTable();

        $.each(data, function (i, item) {
            t.row.add([
                '<input type="checkbox" name="chkItem" value="' + item.id + '">',
                item.id,
                item.coursename,
                item.trainer,
                item.rating,
                item.review,
                '<a class="viewbtn btn btn-success" value="' + item.id + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>'
            ]).draw();
        });

    });

}



$(document).ready(function () {
    initRatingPage();
    var html="<a class='btn btn-danger btndelete'><i class='glyphicon glyphicon-trash icon-white'></i>Delete</a>";
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

        var ratingId = $(this).parents('tr').children('td').eq(1).text();
        var courseName = $(this).parents('tr').children('td').eq(2).text();
        var trainerName = $(this).parents('tr').children('td').eq(3).text();
        var ratingScore = $(this).parents('tr').children('td').eq(4).text();
        var review = $(this).parents('tr').children('td').eq(5).text();
        

        //mark this row as edited
        $('#ratingTable').DataTable().$('tr.edited').removeClass('edited');
        $(this).parents('tr').addClass('edited');

        $("#ratingEdit").text("Edit");
        $("#ratingDelete").text("Delete");
        if ('' != ratingId) {
            $("#ratingId").val(ratingId);
            $("#courseName").val(courseName);
            $("#trainerName").val(trainerName);
            $("#ratingScore").val(ratingScore);
            $("#review").val(review);
            
            $("#ratingInfo").modal('toggle');
            $("#ratingId").attr("readonly", true);
            $("#courseName").attr("readonly", true);
            $("#trainerName").attr("readonly", true);
            $("#ratingScore").attr("readonly", true);
            $("#review").attr("readonly", true);
            
            if ("Update" == $("#ratingEdit").text())
                $("#ratingEdit").text("Edit");          
            
        }
    });

    
    //edit button function
    $(document).on("click", "#ratingEdit", function () {
        if ("Add" == $("#ratingEdit").text()) {            
            
            $('#ratingTable').DataTable().row.add([
                '<input type="checkbox" name="chkItem" value="' + $("#ratingId").val() + '">',
            $("#ratingId").val(),
            $("#courseName").val(),
            $("#trainerName").val(),
            $("#ratingScore").val(),
            $("#review").val(),
                '<a class="viewbtn btn btn-success" value="' + $("#ratingId").val() + '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>'
        ]).draw();            
            
            $("#ratingInfo").modal('toggle');
            $("#ratingEdit").text("Edit");
        } 
        if ("Update" == $("#ratingEdit").text()) {
            $('#ratingTable').DataTable().$('td', 'tr.edited').eq(1).text($("#ratingId").val());
            $('#ratingTable').DataTable().$('td', 'tr.edited').eq(2).text($("#courseName").val());
            $('#ratingTable').DataTable().$('td', 'tr.edited').eq(3).text($("#trainerName").val());
            $('#ratingTable').DataTable().$('td', 'tr.edited').eq(4).text($("#ratingScore").val());
            $('#ratingTable').DataTable().$('td', 'tr.edited').eq(5).text($("#review").val());
            
            $("#ratingInfo").modal('toggle');
            $("#ratingEdit").text("Edit");
        } 
        if ("Edit" == $("#ratingEdit").text()) {
            $("#ratingId").attr("readonly", false);
            $("#courseName").attr("readonly", false);
            $("#trainerName").attr("readonly", false);
            $("#ratingScore").attr("readonly", false);
            $("#review").attr("readonly", false);
                        
            $("#ratingEdit").text("Update");
        }
    });

    
    $(document).on("click", "#ratingDelete", function () {
        $('#ratingTable').DataTable().row('.edited').remove().draw(false);
        $("#ratingInfo").modal('toggle');
    });

  
     $(document).on("click", ".btnadd", function () {alert("add")});
     $(document).on("click", ".btndelete", function () {
        $('#ratingTable').DataTable().row('.selected').remove().draw(false);
    });
    
});
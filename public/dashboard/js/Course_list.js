function addRow(v1, v2, v3, v4, v5, v6, v7, v8) {
	$('#courseTable').DataTable().row
			.add(
					[
							'<input type="checkbox" name="chkItem" value="'
									+ v1 + '">',
							v1,
							v2,
							v3,
							v4,
							v5,
							v6,
							v7,
							v8,
							'<a class="viewbtn btn btn-success" value="'
									+ v1
									+ '"><i class="glyphicon glyphicon-zoom-in icon-white"></i>View</a>' ])
			.draw();
}

function initCoursePage() {
	console.log("hehe");
	jsRoutes.controllers.Application.dashConcreteCourseRequest().ajax(
			{
				success : function(data) {
					$.each(data,
							function(i, item) {
								addRow(item.concreteCourseId, item.courseName,
										item.trainerName, item.trainerEmail,
										item.location.city + ' '
												+ item.location.region,
										item.courseDate, item.soldSeat,
										item.minimum);
							});
				},
				error : function(data) {
					$.each(data,
							function(i, item) {
								addRow(item.concreteCourseId, item.courseName,
										item.trainerName, item.trainerEmail,
										item.location.city + ' '
												+ item.location.region,
										item.courseDate, item.soldSeat,
										item.minimum);
							});
				}

			});

}

function getItemsFromServer(concreteCourseId) {
	jsRoutes.controllers.Application.dashConcreteCourseRequestdetail(concreteCourseId).ajax({
		success : function(data) {
			console.log(data);
			setDetailPage(data);
		}
	})
}
function setDetailPage(item) {
	$("#courseId").val(item.courseId);
	$("#courseName").val(item.courseName);
	$("#eventbriteId").val(item.eventbriteId);
	$("#trainerId").val(item.trainerId);
	$("#trainerName").val(item.trainerName);
	$("#trainerEmail").val(item.trainerEmail);
	$("#detailedLoc").val(item.detailedLoc);
	$("#city").val(item.city);
	$("#state").val(item.state);
	$("#zipCode").val(item.zipCode);
	$("#soldSeat").val(item.soldSeat);
	$("#minimum").val(item.minimum);
	$("#maximal").val(item.maximal);
	$("#length").val(item.courseLength);
	$("#courseDate").val(item.courseDate);
	$("#status").val(item.status);

	enableFields();
}

function disableFields() {
	$("#courseId").attr("readonly", true);
	$("#courseName").attr("readonly", true);
	$("#eventbriteId").attr("readonly", true);
	$("#trainerId").attr("readonly", true);
	$("#trainerName").attr("readonly", true);
	$("#trainerEmail").attr("readonly", true);
	$("#detailedLoc").attr("readonly", true);
	$("#city").attr("readonly", true);
	$("#state").attr("readonly", true);
	$("#zipCode").attr("readonly", true);
	$("#soldSeat").attr("readonly", true);
	$("#minimum").attr("readonly", true);
	$("#maximal").attr("readonly", true);
	$("#courseLength").attr("readonly", true);
	$("#courseDate").attr("readonly", true);
	$("#status").attr("readonly", true);
}

function enableFields() {
	$("#courseId").attr("readonly", false);
	$("#courseName").attr("readonly", false);
	$("#eventbriteId").attr("readonly", false);
	$("#trainerId").attr("readonly", false);
	$("#trainerName").attr("readonly", false);
	$("#trainerEmail").attr("readonly", false);
	$("#detailedLoc").attr("readonly", false);
	$("#city").attr("readonly", false);
	$("#state").attr("readonly", false);
	$("#zipCode").attr("readonly", false);
	$("#soldSeat").attr("readonly", false);
	$("#minimum").attr("readonly", false);
	$("#maximal").attr("readonly", false);
	$("#courseLength").attr("readonly", false);
	$("#courseDate").attr("readonly", false);
	$("#status").attr("readonly", false);
}

function emptyDetailPage(item) {
	$("#courseId").val('');
	$("#courseName").val('');
	$("#eventbriteId").val('');
	$("#trainerId").val('');
	$("#trainerName").val('');
	$("#trainerEmail").val('');
	$("#detailedLoc").val('');
	$("#city").val('');
	$("#state").val('');
	$("#zipCode").val('');
	$("#soldSeat").val('');
	$("#minimum").val('');
	$("#maximal").val('');
	$("#courseLength").val('');
	$("#courseDate").val('');
	$("#status").val('');
}

$(document)
		.ready(
				function() {
					initCoursePage();
					var html = "<a class='btn btn-info btnadd'><i class='glyphicon glyphicon-edit icon-white'></i>add</a> <a class='btn btn-danger btndelete'><i class='glyphicon glyphicon-trash icon-white'></i>Delete</a>";
					$(".btns").append(html);

					// click function for every row
					$("#courseTable").on('click', 'tr', function() {
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

					// check all checkbox
					$(document)
							.on(
									"click",
									".chkAll",
									function() {
										if (('true' == $(".chkAll").prop(
												'checked'))
												|| ('checked' == $(".chkAll")
														.attr('checked'))) {
											$(".chkAll").prop("checked", false);
											$("[name = chkItem]:checkbox")
													.prop("checked", false);

											$(".chkAll").attr("checked", false);
											$("[name = chkItem]:checkbox")
													.attr("checked", false);
											$("[name = chkItem]:checkbox")
													.parents('tr').removeClass(
															'selected');
										} else {
											$(".chkAll").prop("checked", true);
											$("[name = chkItem]:checkbox")
													.prop("checked", true);

											$(".chkAll").attr("checked", true);
											$("[name = chkItem]:checkbox")
													.attr("checked", true);
											$("[name = chkItem]:checkbox")
													.parents('tr').addClass(
															'selected');
										}
									});

					// view button function
					$(document).on(
							"click",
							".viewbtn",
							function() {
								// mark this row as edited
								$('#courseTable').DataTable().$('tr.edited')
										.removeClass('edited');
								$(this).parents('tr').addClass('edited');

								$("#courseEdit").text("Edit");
								$("#courseDelete").text("Delete");

								// set detail infomation into detail page
								var courseId = $(this).parents('tr').children(
										'td').eq(1).text();
								getItemsFromServer(courseId);

								disableFields();
								$("#courseInfo").modal('toggle');
							});

					// edit button function
					$(document).on(
							"click",
							"#courseEdit",
							function() {
								if ("Add" == $("#courseEdit").text()) {

									addRow($("#courseId").val(), $(
											"#courseName").val(), $(
											"#trainerName").val(), $(
											"#trainerEmail").val(), $("#city")
											.val()
											+ $("#state").val(), $(
											"#courseDate").val(),
											$("#soldSeat").val(), $("#minimum")
													.val());
									$("#courseInfo").modal('toggle');
								}
								if ("Update" == $("#courseEdit").text()) {

									$('#courseTable').DataTable().$('td',
											'tr.edited').eq(1).text(
											$("#courseId").val());
									$('#courseTable').DataTable().$('td',
											'tr.edited').eq(2).text(
											$("#courseName").val());
									$('#courseTable').DataTable().$('td',
											'tr.edited').eq(3).text(
											$("#trainerName").val());
									$('#courseTable').DataTable().$('td',
											'tr.edited').eq(4).text(
											$("#trainerEmail").val());
									$('#courseTable').DataTable().$('td',
											'tr.edited').eq(5).text(
											$("#city").val()
													+ $("#state").val());
									$('#courseTable').DataTable().$('td',
											'tr.edited').eq(6).text(
											$("#courseDate").val());
									$('#courseTable').DataTable().$('td',
											'tr.edited').eq(7).text(
											$("#soldSeat").val());
									$('#courseTable').DataTable().$('td',
											'tr.edited').eq(8).text(
											$("#minimum").val());

									$("#courseInfo").modal('toggle');
								}
								if ("Edit" == $("#courseEdit").text()) {
									enableFields();
									$("#courseEdit").text("Update");
								}
							});

					// delete button on detail page
					$(document).on(
							"click",
							"#courseDelete",
							function() {
								if ("Cancel" == $("#courseDelete").text()) {
									$("#courseInfo").modal('toggle');

								} else {
									$('#courseTable').DataTable()
											.row('.edited').remove()
											.draw(false);
									$("#courseInfo").modal('toggle');
								}
							});

					// add button function
					$(document).on("click", ".btnadd", function() {
						emptyDetailPage();
						enableFields();
						$("#courseInfo").modal('toggle');
						$("#courseEdit").text("Add");
						$("#courseDelete").text("Cancel");
					});

					// delete button function
					$(document).on(
							"click",
							".btndelete",
							function() {
								$('#courseTable').DataTable().row('.selected')
										.remove().draw(false);
							});
				})

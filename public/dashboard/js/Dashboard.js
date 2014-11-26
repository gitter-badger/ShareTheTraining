function initDashboard()
{
    var userNumber=1;
    var trainerNumber=1;
    var ratingNumber=1;
    var courseNumber=1;
    var userNotf=1;
    var trainerNotf=1;
    var ratingNotf=1;
    var courseNotf=1;
    $("#userNumber").text(userNumber);
    $("#trainerNumber").text(trainerNumber);
    $("#ratingNumber").text(ratingNumber);
    $("#courseNumber").text(courseNumber); 
    $("#userNotf").text(userNotf);
    $("#trainerNotf").text(trainerNotf);
    $("#ratingNotf").text(ratingNotf);
    $("#courseNotf").text(courseNotf);
}

 $(document).ready(function() {
 initDashboard();
 });
 
 function addItem(){
		getJsonData();
		jsRoutes.controllers.Application.dashAdminAdd().ajax({
			data : JsonData,
			suceess : {}
		});
	}
 function validate(){
	 
//	    $("#name").val();
//	    $("#email").val();
//	    $("#cellPhone").val();
//	    $("#state").val();
//	    $("#city").val();
//	    $("#userStatus").val()
//	    $("#userName").val();
	 
	   if((''== $("#password").val())||(''==$("#password1").val()))
	   { 
		   alert('please type in password!');
		   return false;
	   }
	   
	   if($("#password").val()!=$("#password1").val())
		   {
		   alert('two passwords are different!');
		   return false;
		   }
	   
 }
 
 $(document).on("click", "#userAdd", function () {
	 if(false == validate())
		 {
		 return;
		 }
        addItem();
   });
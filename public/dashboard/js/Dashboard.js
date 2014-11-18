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
var alertPosition = function(box) {
    var x = $(box).offset().left + $(box).width()/2;
    var y = $(box).offset().top ;    
    return {x:x, y:y};
};

var moveAlert = function(id, position) {
        var alert = $("#" + id);
        alert.css("left", position.x + 14);
        alert.css("top", position.y - 14);
};

var makeAlert = function(text, id, position, length) {
        var alertbox = "<div class=\"alert\" id=\"" + id +
            "\"><p>" + text + "</p></div>";
        $("body").append(alertbox);
        moveAlert(id, position);
        var alert = $("#" + id);
        alert.css("width", length);
        alert.fadeIn(200);
};

var username = function() {
    var text = $("#username").val();
    var alertMessage = "", alertLength = 0;
    var alphaNumPatt = /^[a-z0-9]*$/i;
    var problems = false;
    if(text.length < 6 || text.length > 12) {
        alertMessage = "Username should be 6-12 characters.";
        alertLength = 215;
        problems = true;
    }
    if(!alphaNumPatt.test(text)) {
        if(problems) alertMessage += "<br>";
        alertMessage += "Please use letters and numbers only.";
        alertLength = 215;
        problems = true;
    }
    if(text.length === 0) {
        alertMessage = "Please enter a valid username.";
        alertLength = 180;
        problems = true;
    }
    if(!problems) return true;
    makeAlert(alertMessage, "username-alert",
        alertPosition("#username"), alertLength);
    return false;
};

var email = function() {
    var text = $("#email").val();
    var alertMessage = "", alertLength = 0;
    var emailPatt = /^[a-z0-9!#\$%&'\*\+\-\/=\?\^_`{\|}~]+(\.[a-z0-9!#\$%&'\*\+\-\/=\?\^_`{\|}~])*@([a-z0-9]+([a-z0-9\-]*[a-z0-9]+)*)+(\.([a-z0-9]+([a-z0-9\-]*[a-z0-9]+)*))*$/i;
    var problems = false;
    if(!emailPatt.test(text)) {
        alertMessage += "Please enter a valid email address.";
        alertLength = 205;
        problems = true;
    }
    if(!problems) return true;
    makeAlert(alertMessage, "email-alert",
        alertPosition("#email"), alertLength);
    return false;
};

var pass = function() {
    var text = $("#pass").val();
    var alertMessage = "", alertLength = 0;
    var problems = false;
    if(text.length === 0) {
        alertMessage = "Please enter a valid password.";
        alertLength = 175;
        problems = true;
    } else if(text.length < 6 || text.length > 20) {
        alertMessage = "Password should be 6-20 characters.";
        alertLength = 210;
        problems = true;
    }
    if(!problems) return true;
    makeAlert(alertMessage, "pass-alert",
        alertPosition("#pass"), alertLength);
    return false;
};

var repass = function() {
    var ptext = $("#pass").val();
    var rtext = $("#repass").val();
    var alertMessage = "", alertLength = 0;
    var problems = false;
    if(rtext.length === 0) {
        alertMessage = "Please repeat the password.";
        alertLength = 165;
        problems = true;
    } else if(ptext !== rtext) {
        alertMessage = "Passwords must be identical.";
        alertLength = 170;
        problems = true;
    }
    if(!problems) return true;
    makeAlert(alertMessage, "repass-alert",
        alertPosition("#repass"), alertLength);
    return false;
};

var checkInput = function() {
    $(".alert").remove();
    var u = username();
    var e = email();
    var p = pass();
    var r = repass();
    $(".alert").click(function() {
        $(this).fadeOut(200, function(){$(this).remove();});
    });
    return u && e && p && r;
};

$(window).resize(function() {
    moveAlert("username-alert", alertPosition("#username"));
    moveAlert("email-alert", alertPosition("#email"));
    moveAlert("pass-alert", alertPosition("#pass"));
    moveAlert("repass-alert", alertPosition("#repass"));
});
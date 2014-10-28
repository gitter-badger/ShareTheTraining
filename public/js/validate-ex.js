/*
 * jQuery validation 
 */
 // zipcode    
jQuery.validator.addMethod("isZipCode", function(value, element) {    
  var zip = /^[0-9]{5}$/;    
  return this.optional(element) || (zip.test(value));    
}, "Please input valid zipcode!");        

// username   
jQuery.validator.addMethod("validUserName", function(value, element) {    
  return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);    
}, "Invalid username!");   

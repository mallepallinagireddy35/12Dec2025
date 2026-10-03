'use strict';

var validation;
var hms;
/**
 * common generic functions for alert, logging, debug, error, loading, icon information, messages
 */
var ENABLE_LOGGING = true;

if(typeof validation === 'undefined' || typeof validation === undefined)
	'use strict'
	validation = {
		validate_loginFunction : function() {
			
			return validateForm("login_screen", validate_loginForm_rules);
		}
	}

if (typeof validation === 'undefined' || typeof validation === undefined )
    'use strict'
	hms = {
    log : function(msg) {
        if (ENABLE_LOGGING)
            console.log(msg);
    },
	    /*
		sCallback -> Success callback
		eCallback -> Error callback
		tCallback -> Timeout callback
	
		timeout -> The timeout value in ms, default - 5s
		async -> Asynchronous param (true || false), default true
		method -> GET | POST, default GET
		headers -> List of HTTP headers to set [{key: "Accept", value: "text/xml"}, {key: "Content-Type", value: "text/xml"}]
		bodyParams -> Params to be passed in the request body should be an object otherwise throws rise 400 Bad Request
	
	*/
	makeAjax: function(url, options){
		var sCallback, eCallback, tCallback, xhr, method, timeout, async, headers, bodyParams, urlParams, data = null;
		sCallback = options.sCallback;
		eCallback = options.eCallback;
		tCallback = options.tCallback;
		timeout = options.timeout || 5000; // default - 5s
		async = options.async || true;
		bodyParams = options.bodyParams;
		method = options.method;
		headers = options.headers || [];
		
		//alert("we are in");
		if(bodyParams){
			data = bodyParams, true;
		}
		
		if(window.XMLHttpRequest)
			xhr = new XMLHttpRequest();
		else
			xhr = new ActiveXObject("Microsoft.XMLHttp");	// <=IE6
	
		try{
			xhr.open(method, url, async);
		
			// Params
			xhr.timeout = timeout;
			xhr.onreadystatechange = function()
			{
				
			},
			xhr.onload = function()
			{
				//alert("we are in:OL data -"+this.responseText);
				
				if(this.readyState == 4) {
					if(this.status == 200){
						//alert("we are in: data -"+this.responseText);
						// success
						sCallback && sCallback.call(this, this.responseText);
					} else {
						// failure
						eCallback && eCallback.call(this, this.statusText);
					}
				}
			},
			xhr.onerror = function() {
				// failure
				eCallback && eCallback.call(this, this.statusText);
			},
			xhr.ontimeout = function() {
				tCallback & tCallback.call(this);
			};
			
			// Request headers
			for(var i=0; i<headers.length; i++)
			{
				var key = headers[i].key;
				var value = headers[i].value;
				xhr.setRequestHeader(key, value);
			}
	
			// Finally send
			xhr.send(data);
		}catch (e) {
			if(typeof console != "undefined")
				school.log(e);
				
		}
		
	},
    closeInSeconds: 4,
    success: "success",
    error: "danger",
    icon: "warning",
    showLoader: function() {
        $("#preloader").show();
    },
    hideLoader: function() {
        $("#preloader").hide();
    },
    messages: {
        INVALID_USER_OR_PASSWORD: "Invalid User Name or Password",
        INVALID_INCIDENT_NUMBER: "Please Enter Valid Incident Number !",
        INVALID_PLANT_CODE: "Please Enter a Valid plant code!",
        INVALID_EMPLOYEE_ID: "Please Enter a Valid Eployee Id!",
        INVALID_INCIDENT_CLONEID: "Please Enter valid Incident Number to clone data"
    },
    isNumberKey : function(evt) 
	{
		if(!evt)
			evt = window.event;
		var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57))
	        return false;
	    return true;
	},
	isDecimalKey : function(evt) 
	{
		if(!evt)
			evt = window.event;
		var charCode = (evt.which) ? evt.which : evt.keyCode;
		var srcElement = evt.srcElement || evt.target;
	    if (charCode == 46 && srcElement.value.split('.').length>1)
	        return false;
	    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57))
	        return false;
	    return true;
	},
	// zip code TODO: Revisit
	isAlphaNumeric :function(evt) {
		var specialKeys = new Array();
	    specialKeys.push(8); //Backspace
	    specialKeys.push(9); //Tab
	    specialKeys.push(46); //Delete
	    specialKeys.push(36); //Home
	    specialKeys.push(35); //End
	    specialKeys.push(37); //Left
	    specialKeys.push(39); //Right
	    specialKeys.push(46); //DOT
	    specialKeys.push(95); //UNDER SCORE
	    
        var keyCode = evt.keyCode == 0 ? evt.charCode : evt.keyCode;
        return ((keyCode >= 48 && keyCode <= 57) || (keyCode == 46) || (keyCode == 95) || (keyCode >= 65 && keyCode <= 90) || (keyCode >= 97 && keyCode <= 122) || (specialKeys.indexOf(evt.keyCode) != -1 && evt.charCode != evt.keyCode));
    },
    isUserIdKey : function(evt) 
	{
		var charCode = (evt.which) ? evt.which : event.keyCode;
	    if (charCode == 32 || charCode == 64)
	        return false;
	    
	    return true;
	},
    isAlphaNumericWithSpace :function(evt) {
		var specialKeys = new Array();
	    specialKeys.push(8); //Backspace
	    specialKeys.push(9); //Tab
	    specialKeys.push(46); //Delete
	    specialKeys.push(36); //Home
	    specialKeys.push(35); //End
	    specialKeys.push(37); //Left
	    specialKeys.push(39); //Right
	    
        var keyCode = evt.keyCode == 0 ? evt.charCode : evt.keyCode;
        return ((keyCode == 32) || (keyCode >= 48 && keyCode <= 57) || (keyCode >= 65 && keyCode <= 90) || (keyCode >= 97 && keyCode <= 122) || (specialKeys.indexOf(evt.keyCode) != -1 && evt.charCode != evt.keyCode));
    },
    isZipCode :function(evt) {
		var specialKeys = new Array();
	    specialKeys.push(8); //Backspace
	    specialKeys.push(9); //Tab
	    specialKeys.push(46); //Delete
	    specialKeys.push(36); //Home
	    specialKeys.push(35); //End
	    specialKeys.push(37); //Left
	    specialKeys.push(39); //Right
	    
        var keyCode = evt.keyCode == 0 ? evt.charCode : evt.keyCode;
        return ((keyCode == 45) || (keyCode == 32) || (keyCode >= 48 && keyCode <= 57) || (keyCode >= 65 && keyCode <= 90) || (keyCode >= 97 && keyCode <= 122) || (specialKeys.indexOf(evt.keyCode) != -1 && evt.charCode != evt.keyCode));
    },
    isDialogBox : function(msg) {
    	return bootbox.dialog({
            message: '<div class="row">  ' +
                '<div class="col-md-12" style="font-size: 22px;text-align: center;"> ' +
                msg +
                '</div>  </div>',
            buttons: {
                success: {
                    label: "OK",
                    className: "btn-primary btn-sm"
                }
            }
        }
    );
    }
};


// generates a Unique Id for alert messages
function getUniqueID(prefix) {
    return 'prefix_' + Math.floor(Math.random() * (new Date()).getTime());
}
// Core methods
// Common validate method
function validateForm(formName, rules, messages) {

    return $('#' + formName).validate({
        rules: rules,
        messages: messages,

        // Misc
        onkeyup: jQueryValidator_onkeyupHandler,
        highlight: jQueryValidator_highlight,
        unhighlight: jQueryValidator_unhighlight,
        errorPlacement: jQueryValidator_errorPlacement,
        errorElement: 'span',
        errorClass: 'help-block'
    });
}

// Misc
function jQueryValidator_onkeyupHandler(element) {
    var element_id = $(element).attr('name');
    if (this.settings.rules[element_id] && this.settings.rules[element_id].onkeyup !== false) {
        jQuery.validator.defaults.onkeyup.apply(this, arguments);
    }
}

function jQueryValidator_highlight(element) {
    $(element).closest('input').addClass('error');
    $(element).closest('textarea').addClass('error');
    $(element).closest('select').addClass('error');
}

function jQueryValidator_unhighlight(element) {
    $(element).closest('input').removeClass('error');
    $(element).closest('textarea').removeClass('error');
    $(element).closest('select').removeClass('error');
}

function jQueryValidator_errorPlacement(error, element) {
    var after = element.attr('error-after-parent');
    var afterGP = element.attr('error-after-grandparent');
    var parent = element.parent();
    var gp = parent.parent();

    if (after)
        error.insertAfter(parent);
    else if (afterGP)
        error.insertAfter(gp);
    else
        error.insertAfter(element);
}

function validateLoginRules() {
	
	return validateForm("login_screen", validateLoginPage);
}

var validateLoginPage = {
        userName : {
                required : true,
                minlength : 5,
                maxlength : 12
        },
        password : {
                required : true,
                minlenght : 5,
                maxlength : 16
        }
}

function check_login_form() {
	
	return validateForm("login_screen", validate_loginForm_rules);
}

var validate_loginForm_rules = {
        userName : {
                required : true,
                minlength : 5,
                maxlength : 10
        },
        password : {
                required : true,
                minlength : 5,
                maxlength : 10
        }
}

function validate_srlmValidation(){
	return validateForm("centreInchargeValidate", validate_srlmValidation_rules);
}

var validate_srlmValidation_rules = {
        scheme : {
                required : true,
        },
        sectionName : {
                required : true,
        }
}




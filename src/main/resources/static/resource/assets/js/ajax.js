/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



var http = getXMLHTTPRequest();
function getXMLHTTPRequest() {
    var req = false;
    try {
        req = new XMLHttpRequest();
    }
    catch (err1) {
        try {
            req = new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch (err2) {
            try {
                req = new ActiveXObject("Microsoft.XMLHTTP");
            }
            catch (err3) {
                req = false;
            }
        }
    }
    return req;
}
function getSubsequentOptions(fromId, typeId, type, callback) {
   http = getXMLHTTPRequest();
    var code = document.getElementById(fromId).value;
    var url = "ReportExecution1.jsp?type=" + type + "&param=" + code;
    
//    if(type == "entitles")
//   {
//        var entitles = document.getElementById('funding_year').value;
//       
//        url += "&entitles=" + entitles;
//
//   }





        
    http.open("GET", url, true);
    http.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    http.onreadystatechange = function () {
        return populateOptions(typeId, callback)
    };
    http.send(null);
}




function getSubsequentOptioned(fromId, typeId, type, callback) {
   http = getXMLHTTPRequest();
    var code = document.getElementById(fromId).value;
    var url = "ReportExecution1.jsp?type=" + type + "&param=" + code;
//    if (type == "sample") {
//        var code2 = document.getElementById('constituency_code').value;
//        url += "&sample=" + code2;
//    }     

    http.open("GET", url, true);
    http.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    http.onreadystatechange = function () {
        return populateOptions(typeId, callback)
    };
    http.send(null);
}

function populateOptions(typeId, callback) {
    if (http.readyState == 4) {
        var response = http.responseText;
        document.getElementById(typeId).innerHTML = response;
        if (callback != undefined) {
            setTimeout(callback, 10);
        }
    }
}

function getSubsequentOptions1(fromId, typeId, type, callback) {
    http = getXMLHTTPRequest();

    var code = document.getElementById(fromId).value;
    var url = "AjaxExecutionForDuplicates.jsp?type=" + type + "&param=" + code;
    http.open("GET", url, true);
    http.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    http.onreadystatechange = function () {
        return populateOptions(typeId, callback)
    };
    http.send(null);
}

        
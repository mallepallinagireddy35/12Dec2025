'use strict';
ajssample.controller('svpnpaController', ['$scope', '$http', '$rootScope', '$window', '$filter'
            , function ($scope, $http, $rootScope, $window, $filter) {
	
var csrfToken = $('meta[name="_csrf"]').attr('content');
var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

var app = angular.module('ajssample', []);
app.controller('myCtrl', function($scope, $http) {
    $http.defaults.headers.common['X-CSRF-TOKEN'] = csrfToken;
    $http.defaults.headers.common['X-CSRF-HEADER'] = csrfHeader;
});	
	
	
//--------------------Dynamic Form-Fields--------------------------------
    $scope.getFormStructure= function () {
		//var csrfToken = $("meta[name='_csrf']").attr("content");
		//var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
             type: "POST",
             url: "getFormStructure?form_id=" + $scope.formDetails.form_id,
             beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeader, csrfToken);
				},
				headers: {
				    'X-CSRF-TOKEN': csrfToken,
				    'X-XSRF-HEADER' : csrfHeader
				},
            // headers: {
       		// 'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content'),
        	// 'X-CSRF-HEADER': $('meta[name="_csrf_header"]').attr('content')
    		//},
             
         //  enctype: 'multipart/form-data',
             dataType: 'json',
             contentType: false,
             processData: false,             
             success: function (data, status) {
//           $scope.successMessage = data.success;
                 if($scope.formDetails.form_id==''){
                 alert("Select Form Name");
                 return false;    
                 }else{
                 $scope.formDetails = data;
                 if(data.success=='success'){
//                 $scope.IsDisabled = true; 
//                	 document.getElementById('npa').style.display='block';
                     document.getElementById('ssr').innerHTML = data.jsonstring;                	 
                 }else{
                 alert(data.success);
                 return false;
                 }
             }
                 $scope.$apply();
//                 jQuery.noConflict();
             }
         });                   
     };     
    

  //--------------------Dynamic Form-Records--------------------------------
     $scope.getListOfRecords= function () {
         $.ajax({
              type: "POST",              
              url: "getListOfRecords?form_id=" + $scope.recordDetails.form_id,
              beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeader, csrfToken);
				},
				headers: {
				    'X-CSRF-TOKEN': csrfToken,
				    'X-XSRF-HEADER' : csrfHeader
				},            
          //  enctype: 'multipart/form-data',
              dataType: 'json',
              contentType: false,
              processData: false,             
              success: function (data, status) {
//            $scope.successMessage = data.success;
                  if($scope.recordDetails.form_id==''){
                  alert("Select Form Name");
                  return false;    
                  }else{
                  $scope.recordDetails = data;
                  if(data.success=='success'){
//                  $scope.IsDisabled = true; 
//                 	 document.getElementById('npa').style.display='block';
                	 
                  }else{
                  alert(data.success);
                  document.getElementById("record_id").length = 1;
                  document.getElementById("record_id").options[0].value = "";
                  document.getElementById("record_id").options[0].text = "Select Record";

                  return false;
                  }
              }
                  $scope.$apply();
//                  jQuery.noConflict();
              }
          });                   
      };     
     
//--------------------get-Form-Expiry-Date-Details--------------------------------
    $scope.getFormExpiryDetails= function () {
        $.ajax({
             type: "POST",             
             url: "getFormExpiryDetails?form_id=" + $scope.formExpiryDetails.form_id,
             beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeader, csrfToken);
				},
				headers: {
				    'X-CSRF-TOKEN': csrfToken,
				    'X-XSRF-HEADER' : csrfHeader
				},            
         //  enctype: 'multipart/form-data',
             dataType: 'json',
             contentType: false,
             processData: false,             
             success: function (data, status) {
//           $scope.successMessage = data.success;
                 if($scope.formExpiryDetails.form_id==''){
                 alert("Select Form Name");
                 return false;    
                 }else{
                 $scope.formExpiryDetails = data;
                 if(data.success=='success'){
//                 $scope.IsDisabled = true; 
                	 document.getElementById('ssr1').style.display='block';
                     document.getElementById('ssr').style.display = 'none';
                     document.getElementById('form_name').value = $scope.formExpiryDetails.form_id;
                     document.getElementById('from_date').value = data.from_date;
                     document.getElementById('to_date').value = data.to_date;                     
                     $scope.IsDisabled = true;                 	 
                 }else{
                 alert(data.success);
                 document.getElementById('ssr1').style.display='none';
                 document.getElementById('ssr').style.display = 'block';
                 return false;
                 }
             }
                 $scope.$apply();
//                 jQuery.noConflict();
             }
         });                   
     };  
     
//--------------------Form-Verify-Fields--------------------------------
    $scope.getFormStructureVerify= function () {
		//var csrfToken = $("meta[name='_csrf']").attr("content");
		//var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
             type: "POST",
             url: "getVerifyFormStructure?form_id=" + $scope.formDetailsVerify.form_id,
             beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeader, csrfToken);
				},
				headers: {
				    'X-CSRF-TOKEN': csrfToken,
				    'X-XSRF-HEADER' : csrfHeader
				},
            // headers: {
       		// 'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content'),
        	// 'X-CSRF-HEADER': $('meta[name="_csrf_header"]').attr('content')
    		//},
             
         //  enctype: 'multipart/form-data',
             dataType: 'json',
             contentType: false,
             processData: false,             
             success: function (data, status) {
//           $scope.successMessage = data.success;
                 if($scope.formDetailsVerify.form_id==''){
                 alert("Select Form Name");
                 return false;    
                 }else{
                 $scope.formDetailsVerify = data;
                 if(data.success=='success'){
//                 $scope.IsDisabled = true; 
                	 document.getElementById('npa').style.display='block';
                     document.getElementById('ssr').innerHTML = data.jsonstring;                	 
                 }else{
				 document.getElementById('npa').style.display='none';
                 alert(data.success);
                 return false;
                 }
             }
                 $scope.$apply();
//                 jQuery.noConflict();
             }
         });                   
     };     
    
     
    
        
     
     
}]);

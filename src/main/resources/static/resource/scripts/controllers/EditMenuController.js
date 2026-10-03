'use strict';
ajssample.controller('editMenuController', ['$scope', '$http', '$rootScope', '$window', '$filter', function($scope, $http, $rootScope, $window, $filter) {
	
var csrfToken = $('meta[name="_csrf"]').attr('content');
var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

var app = angular.module('ajssample', []);
app.controller('myCtrl', function($scope, $http) {
    $http.defaults.headers.common['X-CSRF-TOKEN'] = csrfToken;
    $http.defaults.headers.common['X-CSRF-HEADER'] = csrfHeader;
});		
	
	$scope.getFormFillValues = function(mainlevel, level1, level2, level3) {
		
		var mainlevel = mainlevel;
		var level1 = level1;
		var level2 = level2;
		var level3 = level3;
		//alert('Data Found--- ' + mainlevel + "  --  " + level1 + "  --  " + level2 + "  --  " + level3);
		if (mainlevel !== '' && level1 !== '' && level2 !== '' && level3 !== '') {
			//alert('Data Found--- ' + mainlevel + "  --  " + level1 + "  --  " + level2 + "  --  " + level3);
			$.ajax({
				type: "POST",
				url: "getFormFillValues?mainlevel=" + mainlevel + "&level1=" + level1 + "&level2=" + level2 + "&level3=" + level3,
             beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeader, csrfToken);
				},
				headers: {
				    'X-CSRF-TOKEN': csrfToken,
				    'X-XSRF-HEADER' : csrfHeader
				},				
				//enctype: 'multipart/form-data',
				dataType: 'json',
				contentType: false,
				processData: false,
				success: function(data, status) {
					 //alert('Data Found--- ' + data.message);
					if (data.message === 'success') {
						$scope.TablefillDetails = data.fillDetails;
						//alert(data.fillDetails);
						console.log("data.fillDetails....."+data.fillDetails);
					} else {
						alert('No Data Found');
					}
					$scope.$apply();
					//jQuery.noConflict();
				}
			});
		}
	};

}]);


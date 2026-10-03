'use strict';
ajssample.controller('editMenuController', ['$scope', '$http', '$rootScope', '$window', '$filter', function($scope, $http, $rootScope, $window, $filter) {
alert("Prakash");
	$scope.getFormFillValues = function(mainlevel, level1, level2) {
		var mainlevel = mainlevel;
		var level1 = level1;
		var level2 = level2;
		//alert('Data Found--- ' + mainlevel + "  --  " + level1 + "  --  " + level2);
		if (mainlevel !== '' && level1 !== '' & level2 !== '') {
			$.ajax({
				type: "GET",
				url: "getFormFillValues?mainlevel=" + mainlevel + "&level1=" + level1 + "&level2=" + level2,
				//enctype: 'multipart/form-data',
				dataType: 'json',
				contentType: false,
				processData: false,
				success: function(data, status) {
					// alert('Data Found--- ' + data.message);
					if (data.message === 'success') {
						$scope.TablefillDetails = data.fillDetails;
					} else {
						alert('No Data Found');
					}
					$scope.$apply();
					jQuery.noConflict();
				}
			});
		}
	};

}]);


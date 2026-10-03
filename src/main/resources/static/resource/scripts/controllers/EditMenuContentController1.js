'use strict';
ajssample.controller('editMenuContentController', ['$scope', '$http', '$rootScope', '$window', '$filter', function($scope, $http, $rootScope, $window, $filter) {
alert("Prakash editMenuContentController1");
	$scope.getLevelOneData = function() {
		var mainlevel = $scope.NpaDetails.mainlevel_desc;
		if (mainlevel !== '') {
			$.ajax({
				type: "POST",
				url: "getLevelOneData?mainlevel=" + mainlevel,
				//enctype: 'multipart/form-data',
				dataType: 'json',
				contentType: false,
				processData: false,
				success: function(data, status) {
					if (data.message === 'success') {
						$scope.listofLevelOneMenu = data.levelOneList;
					} else {

					}
					$scope.$apply();
					jQuery.noConflict();
				}
			});
		}
	};


	$scope.getLevelTwoData = function() {
		var mainlevel = $scope.NpaDetails.mainlevel_desc;
		var level1 = $scope.NpaDetails.level1_desc;
		if (mainlevel !== '' && level1 !== '') {
			$.ajax({
				type: "POST",
				url: "getLevelTwoData?mainlevel=" + mainlevel + "&level1=" + level1,
				//enctype: 'multipart/form-data',
				dataType: 'json',
				contentType: false,
				processData: false,
				success: function(data, status) {
					if (data.message === 'success') {
						$scope.listofLevelTwoMenu = data.levelTwoList;
						$scope.listofLevelTwoSize = data.levelTwoSize;
					} else {

					}
					$scope.$apply();
					jQuery.noConflict();
				}
			});
		}
	};

	$scope.searchMainLevel = function() {
		var mainlevel = $scope.NpaDetails.mainlevel_desc;
		if (mainlevel !== '') {
			$.ajax({
				type: "POST",
				url: "searchMainLevel?mainlevel=" + mainlevel,
				//enctype: 'multipart/form-data',
				dataType: 'json',
				contentType: false,
				processData: false,
				success: function(data, status) {
					if (data.message === 'success') {
						document.getElementById('mainlevel_desc').value = mainlevel;
					} else {
						document.getElementById('mainlevel_desc').value = '';
					}
					$scope.$apply();
					jQuery.noConflict();
				}
			});
		}
	};


}]);


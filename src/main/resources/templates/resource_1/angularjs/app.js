/***
hospital AngularJS App Main Script
***/
 var ajssamplelog;
/* hospital App */
var ajssample = angular.module("ajssample", []); 




ajssample.directive('onlyDigits', function () {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attr, ctrl) {
        function inputValue(val) {
          if (val) {
            var digits = val.replace(/[^0-9.]/g, '');

            if (digits.split('.').length > 2) {
              digits = digits.substring(0, digits.length - 1);
            }

            if (digits !== val) {
              ctrl.$setViewValue(digits);
              ctrl.$render();
            }
            return parseFloat(digits);
          }
          return undefined;
        }            
        ctrl.$parsers.push(inputValue);
      }
    };
 });
ajssample.directive('onlyNum', function() {
    return function(scope, element, attrs) {

        var keyCode = [8,9,37,39,48,49,50,51,52,53,54,55,56,57,96,97,98,99,100,101,102,103,104,105,110];
        element.bind("keydown", function(event) {
            if($.inArray(event.which,keyCode) == -1) {
                scope.$apply(function(){
                    scope.$eval(attrs.onlyNum);
                    event.preventDefault();
                });
                event.preventDefault();
            }
        });
    };
}) 

ajssample.filter('unique', function() {

  // Take in the collection and which field
  //   should be unique
  // We assume an array of objects here
  // NOTE: We are skipping any object which
  //   contains a duplicated value for that
  //   particular key.  Make sure this is what
  //   you want!
  return function (arr, targetField) {

    var values = [],
        i, 
        unique,
        l = arr.length, 
        results = [],
        obj;

    // Iterate over all objects in the array
    // and collect all unique values
    for( i = 0; i < arr.length; i++ ) {

      obj = arr[i];

      // check for uniqueness
      unique = true;
      for( v = 0; v < values.length; v++ ){
        if( obj[targetField] == values[v] ){
          unique = false;
        }
      }

      // If this is indeed unique, add its
      //   value to our values and push
      //   it onto the returned array
      if( unique ){
        values.push( obj[targetField] );
        results.push( obj );
      }

    }
    return results;
  };
})

ajssample.directive('range', function() {
	return {
		restrict: 'A',
		require: '?ngModel',
		link: function (scope, element, attrs, ngModel) {
			if (!ngModel) return;
			ngModel.$parsers.push(function(val) {
				var parsed = val.replace(/[^0-9999]+/g, '');
				if (parseInt(parsed) > parseInt(attrs.rangeMax)) parsed = attrs.rangeMax;
				if (parseInt(parsed) < parseInt(attrs.rangeMin)) parsed = attrs.rangeMin
				if (val !== parsed) {
					ngModel.$setViewValue(parsed);
					ngModel.$render();
				}
				return parsed;
			});
		}
	};
});

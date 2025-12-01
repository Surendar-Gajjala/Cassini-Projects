define(['app/app.modules'], function (app) {
    app.directive('tableFilter', ['$compile', '$timeout', function ($compile, $timeout) {
        return {
            templateUrl: 'app/shared/directives/templates/tableFilter.html',
            restrict: 'E',
            replace: false,
            scope: {
                'placeholder': '@',
                'align': '@',
                'filters': '=',
                'property': '@',
                'applyFilters': '&',
                'disabled': '='
            },
            link: function(scope, element, attrs) {
                $compile(element.contents())(scope);
                scope.clearFilter = function() {
                    scope.filters[scope.property] = "";
                    scope.applyFilters();
                };
            }
        };
    }]);

    app.directive('tableFiltersActions', ['$compile', '$timeout', function ($compile, $timeout) {
        return {
            templateUrl: 'app/shared/directives/templates/tableFiltersActions.html',
            restrict: 'E',
            replace: false,
            scope: {
                'applyFilters': '&',
                'resetFilters': '&'
            }
        };
    }]);

    app.directive('tableSorter', ['$compile', '$timeout', function ($compile, $timeout) {
        return {
            templateUrl: 'app/shared/directives/templates/tableSorter.html',
            restrict: 'E',
            replace: false,
            scope: {
                'label': '@',
                'sort': '='
            }
        };
    }]);
});

define(['app/desktop/desktop.app'], function (module) {
    module.directive('tableFilter', ['$compile', '$timeout', function ($compile, $timeout) {
        return {
            templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/directives/tableFilter/templates/tableFilter.html',
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

    module.directive('tableFiltersActions', ['$compile', '$timeout', function ($compile, $timeout) {
        return {
            templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/directives/tableFilter/templates/tableFiltersActions.html',
            restrict: 'E',
            replace: false,
            scope: {
                'applyFilters': '&',
                'resetFilters': '&'
            }
        };
    }]);

    module.directive('tableSorter', ['$compile', '$timeout', function ($compile, $timeout) {
        return {
            templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/directives/tableFilter/templates/tableSorter.html',
            restrict: 'E',
            replace: false,
            scope: {
                'label': '@',
                'sort': '='
            }
        };
    }]);
});

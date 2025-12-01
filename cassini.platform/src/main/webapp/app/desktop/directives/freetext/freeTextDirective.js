define(
    [
        'app/desktop/desktop.app',
        '../../../../../jquery-ui/jquery-ui'
    ],

    function (module) {
        module.directive('freeTextSearch', ['$compile', '$timeout', '$rootScope', '$translate', '$sce', function ($compile, $timeout, $rootScope, $translate, $sce) {


            return {
                templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextTemplate.jsp',
                restrict: 'E',
                replace: false,

                controller: function ($scope) {
                    var parsed = angular.element("<div></div>");
                    $scope.clearTitleSearch = parsed.html($translate.instant("CLEAR_SEARCH")).html();
                },
                scope: {
                    onSearch: '=',
                    onClear: '=',
                    searchTerm: '=',
                    clearSearch: '=',
                    filterSearch: '=',
                    clearTitleSearch: '='
                },
                link: function (scope, elem, attrs) {
                    scope.clear = function () {
                        scope.searchTerm = "";
                        scope.onClear();
                        scope.onSearch("");
                    }

                }
            };


        }]);
    }
);

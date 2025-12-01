define(
    [
        'app/desktop/desktop.app'
    ],

    function (module) {
        module.directive('personData', ['$rootScope', '$compile', '$timeout', '$application', function ($rootScope, $compile, $timeout, $application) {

            return {
                templateUrl: 'app/desktop/directives/person-data/personDataDirectiveView.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    person: '=',
                    disable: '='
                },
                link: function ($scope, elem, attr) {

                    (function () {

                    })();
                }
            };
        }]);
    }
);

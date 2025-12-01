define(
    [
        'app/desktop/modules/pdm/pdm.module'
    ],
    function (module) {
        module.directive('noResults', NoResultsDirective);

        function NoResultsDirective() {

            return {
                templateUrl: 'app/desktop/directives/no-results/noResultsDirective.jsp',
                restrict: 'E',
                scope: {

                },

                link: function (scope, element, attrs) {

                }
            }

        }
    }
);
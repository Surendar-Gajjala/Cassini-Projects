define(
    [
        'app/desktop/modules/pdm/pdm.module'
    ],
    function (module) {
        module.directive('tableFooter', TableFooterDirective);

        function TableFooterDirective() {

            return {
                templateUrl: 'app/desktop/directives/table-footer/tableFooterDirective.jsp',
                restrict: 'E',
                scope: {
                    objects: "=",
                    pageable: "=",
                    previousPage: "=",
                    nextPage: "=",
                    pageSize: "=",
                    searchResponse: "="
                },

                link: function ($scope, element, attrs) {
                    $scope.previousObjectPage = previousObjectPage;
                    $scope.nextObjectPage = nextObjectPage;
                    $scope.setObjectPageSize = setObjectPageSize;

                    $scope.pageNumbers = [10, 20, 30, 40, 50];

                    function previousObjectPage() {
                        $scope.previousPage();
                    }

                    function nextObjectPage() {
                        $scope.nextPage();
                    }

                    function setObjectPageSize(page) {
                        $scope.pageSize(page);
                    }
                }
            }

        }
    }
);
define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/relatedItems/relatedItemsDirectiveController'
    ],
    function (module) {
        module.controller('PrRelatedItemsController', PrRelatedItemsController);

        function PrRelatedItemsController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, $window, DialogService) {
            var vm = this;

            var parsed = angular.element("<div></div>");
            vm.problemReportId = $stateParams.problemReportId;

            (function () {
                $scope.$on('app.problemReport.tabActivated', function (event, data) {
                    if (data.tabId == 'details.relatedItem') {
                        $scope.$broadcast('app.relatedItem.tabActivated', {load: true});
                    }
                })
            })();
        }
    }
)
;
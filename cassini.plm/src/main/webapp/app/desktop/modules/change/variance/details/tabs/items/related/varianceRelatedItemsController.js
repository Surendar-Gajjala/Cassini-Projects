define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/relatedItems/relatedItemsDirectiveController'

    ],
    function (module) {
        module.controller('VarianceRelatedItemsController', VarianceRelatedItemsController);

        function VarianceRelatedItemsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                                $translate, DialogService) {
            var vm = this;

            vm.varianceId = $stateParams.varianceId;
            var parsed = angular.element("<div></div>");

            (function () {
                $scope.$on('app.variance.tabactivated', function (event, args) {
                    if (args.tabId == 'details.relateditems') {
                        $scope.$broadcast('app.relatedItem.tabActivated', {load: true});
                    }
                });
            })();
        }
    }
);
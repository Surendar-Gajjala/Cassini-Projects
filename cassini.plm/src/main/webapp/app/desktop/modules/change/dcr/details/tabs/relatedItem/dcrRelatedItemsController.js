define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/dcrService',
        'app/desktop/modules/directives/relatedItems/relatedItemsDirectiveController'

    ],
    function (module) {
        module.controller('DCRRelatedItemsController', DCRRelatedItemsController);

        function DCRRelatedItemsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                           $translate, DialogService) {
            var vm = this;

            vm.dcrId = $stateParams.dcrId;

            var parsed = angular.element("<div></div>");

            (function () {
                $scope.$on('app.dcr.tabActivated', function (event, args) {
                    if (args.tabId == 'details.relatedItems') {
                        $scope.$broadcast('app.relatedItem.tabActivated', {load: true});
                    }
                });
            })();
        }
    }
);
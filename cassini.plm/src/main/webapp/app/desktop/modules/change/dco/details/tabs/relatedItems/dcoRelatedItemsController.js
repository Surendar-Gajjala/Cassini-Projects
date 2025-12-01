define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/dcrService',
        'app/shared/services/core/dcrService',
        'app/desktop/modules/directives/relatedItems/relatedItemsDirectiveController'

    ],
    function (module) {
        module.controller('DCORelatedItemsController', DCORelatedItemsController);

        function DCORelatedItemsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                           $translate, DialogService) {
            var vm = this;

            vm.dcoId = $stateParams.dcoId;


            var parsed = angular.element("<div></div>");

            (function () {
                $scope.$on('app.dco.tabActivated', function (event, args) {
                    if (args.tabId == 'details.relatedItems') {
                        $scope.$broadcast('app.relatedItem.tabActivated', {load: true});
                    }
                });
            })();
        }
    }
);
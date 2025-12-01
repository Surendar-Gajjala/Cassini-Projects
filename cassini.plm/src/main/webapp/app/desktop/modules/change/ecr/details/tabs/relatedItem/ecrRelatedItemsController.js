define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecrService',
        'app/desktop/modules/directives/relatedItems/relatedItemsDirectiveController'

    ],
    function (module) {
        module.controller('ECRRelatedItemsController', ECRRelatedItemsController);

        function ECRRelatedItemsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                           $translate, DialogService) {
            var vm = this;

            vm.ecrId = $stateParams.ecrId;

            var parsed = angular.element("<div></div>");

            (function () {
                //if ($application.homeLoaded == true) {
                    $scope.$on('app.ecr.tabActivated', function (event, args) {
                        if (args.tabId == 'details.relatedItems') {
                            $scope.$broadcast('app.relatedItem.tabActivated', {load: true});
                        }
                    });
                //}
            })();
        }
    }
);
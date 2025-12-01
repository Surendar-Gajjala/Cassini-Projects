define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecrService',
        'app/desktop/modules/directives/changeRequestItems/changeRequestDirectiveController'

    ],
    function (module) {
        module.controller('ECOChangeRequestController', ECOChangeRequestController);

        function ECOChangeRequestController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                            $translate, ECRService, CommentsService, DialogService) {
            var vm = this;

            vm.ecoId = $stateParams.ecoId;
            var parsed = angular.element("<div></div>");

            (function () {
                $scope.$on('app.eco.tabactivated', function (event, args) {
                    if (args.tabId == 'details.changeRequests') {
                        $scope.$broadcast('app.changeRequestItem.tabActivated', {load: true});
                    }
                });
            })();
        }
    }
);
define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/dcoService',
        'app/desktop/modules/directives/changeRequestItems/changeRequestDirectiveController'

    ],
    function (module) {
        module.controller('DCOChangeRequestController', DCOChangeRequestController);

        function DCOChangeRequestController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                            $translate, DCOService, DialogService, CommentsService) {
            var vm = this;

            vm.dcoId = $stateParams.dcoId;

            var parsed = angular.element("<div></div>");

            (function () {
                    $scope.$on('app.dco.tabActivated', function (event, args) {
                        if (args.tabId == 'details.changeRequest') {
                            $scope.$broadcast('app.changeRequestItem.tabActivated', {load: true});
                        }
                    });
            })();
        }
    }
);
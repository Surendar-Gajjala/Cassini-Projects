define(
    [
        'app/desktop/modules/item/item.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('ECOHistoryController', ECOHistoryController);

        function ECOHistoryController($scope, $rootScope, $timeout, $application, $sce, $state, $stateParams, $cookies, $window, ItemTypeService, $uibModal, $translate, ObjectTypeAttributeService,
                                      CommonService, ItemService) {
            var vm = this;

            vm.loading = true;
            vm.ecoId = $stateParams.ecoId;
            vm.ecoHistory = [];


            (function () {
                $scope.$on('app.eco.tabactivated', function (event, data) {

                    if (data.tabId == 'details.history') {
                        $scope.$broadcast('app.object.timeline', {});
                    }

                });
            })();
        }
    }
);
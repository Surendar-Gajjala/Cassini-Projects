define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('QcrHistoryController', QcrHistoryController);

        function QcrHistoryController($scope, $rootScope, $timeout, $application, $sce, $state, $stateParams, $cookies, $window, ItemTypeService, $uibModal, $translate, ObjectTypeAttributeService,
                                      CommonService, ItemService, LoginService, QualityTypeService) {
            var vm = this;

            vm.loading = true;
            vm.qcrId = $stateParams.qcrId;

            (function () {
                $scope.$on('app.qcr.tabActivated', function (event, data) {

                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }

                });
            })();
        }
    }
);
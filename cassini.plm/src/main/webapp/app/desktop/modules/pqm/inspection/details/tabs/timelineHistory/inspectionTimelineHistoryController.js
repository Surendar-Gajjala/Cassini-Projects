define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('InspectionTimelineHistoryController', InspectionTimelineHistoryController);

        function InspectionTimelineHistoryController($scope, $rootScope, $timeout, $application, $sce, $state, $stateParams, $cookies, $window, ItemTypeService, $uibModal, $translate, ObjectTypeAttributeService,
                                                     CommonService, ItemService, LoginService, QualityTypeService) {
            var vm = this;

            vm.loading = true;
            vm.inspectionId = $stateParams.inspectionId;

            (function () {
                $scope.$on('app.inspection.tabActivated', function (event, data) {

                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }

                });
            })();
        }
    }
);
define(
    [
        'app/desktop/modules/item/item.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('ProjectHistoryController', ProjectHistoryController);

        function ProjectHistoryController($scope, $rootScope, $timeout, $application, $sce, $state, $stateParams, $cookies, $window, ItemTypeService, $uibModal, $translate, ObjectTypeAttributeService,
                                          CommonService, ItemService, ProjectService) {
            var vm = this;

            vm.loading = true;
            vm.projectId = $stateParams.projectId;

            (function () {
                $scope.$on('app.project.tabactivated', function (event, data) {

                    if (data.tabId == 'details.projectHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }

                });
            })();
        }
    }
);
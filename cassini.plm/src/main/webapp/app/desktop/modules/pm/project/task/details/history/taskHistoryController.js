define(
    [
        'app/desktop/modules/item/item.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('TaskHistoryController', TaskHistoryController);

        function TaskHistoryController($scope, $rootScope, $timeout, $application, $sce, $state, $stateParams, $cookies, $window, ItemTypeService, $uibModal, $translate,
                                          CommonService) {
            var vm = this;

            vm.loading = true;
            vm.taskId = $stateParams.taskId;

            (function () {
                $scope.$on('app.activity.tasks.tabActivated', function (event, data) {

                    if (data.tabId == 'details.taskHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }

                });
            })();
        }
    }
);
define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/recentlyVisitedService'
    ],
    function (module) {
        module.controller('ProjectItemController', ProjectItemController);

        function ProjectItemController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, ItemService, RecentlyVisitedService) {
            var vm = this;

            vm.loading = true;
            vm.itemId = $stateParams.itemId;

            function loadProjectItem() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                ItemService.getProjectItem(vm.itemId).then(
                    function (data) {
                        vm.itemDeliverables = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            vm.openProjectDetails = openProjectDetails;
            function openProjectDetails(project) {
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                $state.go('app.pm.project.details', {projectId: project.id})
                vm.recentlyVisited.objectId = project.id;
                vm.recentlyVisited.objectType = project.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            vm.openActivityDetails = openActivityDetails;
            function openActivityDetails(activity) {
                $state.go('app.pm.project.activity.details', {activityId: activity.id})
            }

            vm.openTaskDetails = openTaskDetails;
            function openTaskDetails(activity, task) {
                $state.go('app.pm.project.activity.task.details', {activityId: activity.id, taskId: task.id})
            }

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId == 'details.projectItem') {
                        vm.projectItemTab = data.tabId;
                        if ($rootScope.selectedMasterItemId != null) {
                            vm.itemId = $rootScope.selectedMasterItemId;
                            loadProjectItem();

                        }
                        if ($rootScope.selectedMasterItemId == null) {
                            loadProjectItem();

                        }
                    }
                });
            })();
        }
    }
);
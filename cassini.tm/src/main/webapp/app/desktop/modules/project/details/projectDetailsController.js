define(
    [
        'app/desktop/modules/project/project.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/activity/activityStreamDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/project/details/tabs/basic/projectBasicInfoController',
        'app/desktop/modules/project/details/tabs/tasks/projectTasksController',
        'app/shared/services/projectService'
    ],
    function (module) {
        module.controller('ProjectDetailsController', ProjectDetailsController);

        function ProjectDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal,ProjectService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa flaticon-businessman277";
            $rootScope.viewInfo.title = "Project Details";


            var vm = this;

            vm.projectId = $stateParams.projectId;
            vm.projectDetailsTabActivated = projectDetailsTabActivated;
            vm.tabLoaded = tabLoaded;
            vm.newTask = newTask;
            vm.resetPage = resetPage;
            vm.freeTextSearch = freeTextSearch;
            vm.updateProject = updateProject;
            vm.back = back;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/project/details/tabs/basic/projectBasicInfoView.jsp',
                    active: true
                },
                tasks: {
                    id: 'details.tasks',
                    heading: 'Tasks',
                    template: 'app/desktop/modules/project/details/tabs/tasks/projectTasksView.jsp',
                    active: false
                }
            };

            function back() {
                window.history.back();
                loadProject();
            }

            function loadProject() {
                vm.loading = true;
                ProjectService.getProjectById(vm.projectId).then(
                    function (data) {
                        vm.project = data;
                        vm.loading = false;
                    }
                )
            }


            function activateTab(tab) {
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        vm.tabs[t].active = (t != undefined && t == tab);
                    }
                }
            }

            function projectDetailsTabActivated(tabId) {
                $scope.$broadcast('app.project.tabactivated', {tabId: tabId})

                var tab = getTabById(tabId);
                if (tab != null) {
                    activateTab(tab);
                }

            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = t;
                    }
                }

                return tab;
            }

            function tabLoaded(tabId) {
                $scope.$broadcast('app.project.tabloaded', {tabId: tabId});
            }

            function resetPage() {
                $scope.$broadcast('app.project.resettasks');
            }

            function freeTextSearch(searchString) {
                $scope.$broadcast('app.project.freetextsearch', {searchString: searchString});
            }

            function updateProject(){
                $scope.$broadcast('app.project.update');
            }

            function newTask() {
                $rootScope.$broadcast('app.task.addTask')
            }

            (function () {
            })();
        }
    }
);
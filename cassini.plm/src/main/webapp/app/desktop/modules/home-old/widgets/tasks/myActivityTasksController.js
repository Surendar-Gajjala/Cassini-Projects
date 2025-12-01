define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/activityService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/workflowService',
        'app/desktop/modules/pm/project/projectController',
        'app/shared/services/core/inspectionPlanService',
        'app/desktop/modules/pm/project/activity/details/activityDetailsController'
    ],
    function (module) {
        module.controller('MyActivityTasksController', MyActivityTasksController);

        function MyActivityTasksController($scope, $stateParams, $state, $window, $rootScope, $translate, ActivityService, InspectionPlanService, WorkflowService) {

            var vm = this;
            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            var parsed = angular.element("<div></div>");
            $rootScope.loginPersonDetails = session.login;
            var login = $rootScope.loginPersonDetails;
            vm.loading = true;
            vm.personTasks = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.taskTabActivated = taskTabActivated;
            vm.activateTab = activateTab;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.workflowPageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "assignmentType",
                    order: "DESC"
                }
            };

            vm.pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.personTasks = angular.copy(vm.pagedResults);

            function nextPage() {
                vm.pageable.page++;
                loadAssignedTasks();
            }

            function previousPage() {
                vm.pageable.page--;
                loadAssignedTasks();
            }

            var Projects = parsed.html($translate.instant("PROJECTS")).html();
            var Workflows = parsed.html($translate.instant("WORKFLOWS")).html();
            var InspectionPlan = parsed.html($translate.instant("INSPECTION_PLAN")).html();
            vm.tabs = {
                projects: {
                    id: 'projects',
                    heading: Projects,
                    name: "Projects",
                    template: 'app/desktop/modules/home/widgets/tasks/tabs/myProjectTasksView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                workflows: {
                    id: 'workflows',
                    heading: Workflows,
                    name: "WORKFLOWS",
                    template: 'app/desktop/modules/home/widgets/tasks/tabs/myWorkflowTasksView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                inspectionPlan: {
                    id: 'inspectionPlan',
                    heading: InspectionPlan,
                    name: "INSPECTIONPLAN",
                    template: 'app/desktop/modules/home/widgets/tasks/tabs/myInspectionPlanTasksView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }
            };

            function taskTabActivated(tabId) {
                var tab = getTabById(tabId);
                resizeMyTasksTabs();
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = vm.tabs[t];
                    }
                }

                return tab;
            }

            function resizeMyTasksTabs() {
                var height1 = $(".widget-panel").outerHeight();
                $('.tab-content').height(height1 - 43);
                $('.tab-pane').height(height1 - 43);
            }

            vm.workflowTasks = [];
            vm.filters = {};
            function loadWorkflowTasks() {
                vm.filters.personId = login.person.id;
                WorkflowService.getActiveWorkflowTasks(vm.workflowPageable, vm.filters).then(
                    function (data) {
                        vm.workflowTasks = data;
                        $scope.$evalAsync();
                        vm.loading = false;
                    }
                )
            }

            function loadAssignedTasks() {
                ActivityService.getAssignedTasks(login.person.id, vm.pageable).then(
                    function (data) {
                        vm.personTasks = data;
                        vm.loading = false;
                    }
                )
            }

            function loadInspectionPlanTasks() {
                InspectionPlanService.getInspectionPlanTasks(login.person.id, vm.pageable).then(
                    function (data) {
                        vm.inspectionTasks = data;
                        vm.loading = false;
                    }
                )
            }

            vm.showTaskDetails = showTaskDetails;
            function showTaskDetails(task) {
                $state.go('app.pm.project.activity.task.details', {activityId: task.activity, taskId: task.id})
            }

            (function () {
                loadAssignedTasks();
                loadWorkflowTasks();
                loadInspectionPlanTasks();
                $(window).resize(resizeMyTasksTabs);
                $window.localStorage.setItem("lastSelectedActivityTab", JSON.stringify('details.tasks'));
            })();
        }
    }
)
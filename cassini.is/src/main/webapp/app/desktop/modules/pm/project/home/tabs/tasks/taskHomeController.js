define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/tm/taskService',
        'app/shared/services/core/storeService',
        'app/shared/services/store/topInventoryService',
        'app/shared/services/pm/project/bomService',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/projectSiteService',
        'app/shared/services/pm/project/wbsService',
        'app/shared/services/core/itemService',
        'app/shared/services/store/topStoreService'
    ],
    function (module) {
        module.controller('TaskHomeController', TaskHomeController);

        function TaskHomeController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, LoginService, TopStoreService, TaskService, CommonService, BomService, ProjectSiteService, WbsService, TopInventoryService, StoreService) {

            {
                $rootScope.viewInfo.icon = "fa fa-home";
                $rootScope.viewInfo.title = "Home";

                var vm = this;

                var taskIds = [];
                var materialIds = [];
                var storeIds = [];

                vm.tasks = [];
                vm.overDueTasks = [];
                vm.unfinishedTasks = [];
                vm.newTasks = [];
                vm.assignedToMe = [];

                vm.resourceMaterials = [];
                vm.resourceMaterialsList = [];
                vm.itemInventries = [];
                vm.materialShortage = [];

                vm.pageable = {
                    page: 0,
                    size: 20,
                    sort: {
                        field: "modifiedDate"

                    }
                };

                vm.assignedByTasks = [];

                var pagedResults = {
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

                vm.assignedByTasks = pagedResults;

                vm.emptyFilters = {
                    name: null,
                    description: null,
                    site: null,
                    person: "",
                    wbsItem: null,
                    status: null,
                    percentComplete: null,
                    siteObject: null,
                    personObject: null,
                    wbsItemObject: null,
                    percentCompleteObject: null,
                    plannedStartDate: null,
                    plannedFinishDate: null,
                    actualStartDate: null,
                    actualFinishDate: null,
                    searchQuery: null,
                    project: $stateParams.projectId
                };

                vm.filters = angular.copy(vm.emptyFilters);

                function findResourceInventory() {
                    TopInventoryService.getProjectMaterialShortageDTO($stateParams.projectId).then(
                        function (data) {
                            vm.materialShortage = data;
                        }
                    )

                }

                function unFinishedTasks() {
                    vm.filters.status = "ASSIGNED,INPROGRESS";
                    TaskService.getListTasks($stateParams.projectId, vm.filters).then(
                        function (data) {
                            vm.unfinishedTasks = data;
                            angular.forEach(vm.unfinishedTasks, function (task) {
                                taskIds.push(task.id);
                            });
                        })
                }

                function loadOverdueTasks() {
                    TaskService.getListProjectTasks($stateParams.projectId).then(
                        function (data) {
                            vm.tasks = data;
                            angular.forEach(vm.tasks, function (task) {
                                /* Getting today Date With format And Moment */
                                var today = moment(new Date());
                                var todayStr = today.format('DD/MM/YYYY');
                                var todayDate = moment(todayStr, 'DD/MM/YYYY');
                                /* Getting plannedFinishDate Date With format And Moment */
                                var plannedFinishDate = (task.plannedFinishDate);
                                var plannedFinishDateStr = plannedFinishDate.format('DD/MM/YYYY');
                                var plannedFnDate = moment(plannedFinishDateStr, 'DD/MM/YYYY');
                                if (plannedFnDate.isBefore(todayDate)) {
                                    vm.overDueTasks.push(task);
                                }
                                else {
                                }
                            });
                            setValues(vm.overDueTasks);
                        }
                    );
                }

                function setValues(tasks) {
                    TaskService.getSiteReferences(tasks, 'site');
                    CommonService.getPersonReferences(tasks, 'person');
                    WbsService.getMultipleWbsWithTasks($stateParams.projectId, tasks, 'wbsItem');
                }

                function loadNewTasks() {
                    TaskService.getListTasks($stateParams.projectId, vm.filters).then(
                        function (data) {
                            vm.newTasks = data;
                            setValues(vm.newTasks);
                            angular.forEach(vm.newTasks, function (task) {
                                if (task.person == $rootScope.login.person.id) {
                                    vm.assignedToMe.push(task);
                                }
                            });
                        }
                    )
                }

                (function () {
                    if ($application.homeLoaded == true) {
                        loadOverdueTasks();
                        unFinishedTasks();
                        loadNewTasks();
                        findResourceInventory();
                    }
                })();

            }
        }
    }
)
;
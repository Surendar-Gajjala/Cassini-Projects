define(['app/desktop/modules/department/department.module',
        'app/shared/services/departmentService',
        'app/desktop/modules/department/details/tabs/basic/departmentBasicInfoController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/modules/department/details/tabs/tasks/departmentTasksController',
        'app/desktop/modules/department/details/tabs/persons/departmentPersonsController'
    ],
    function(module) {
        module.controller('DepartmentsDetailsController', DepartmentsDetailsController);

        function DepartmentsDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, DepartmentService) {
            if ($application.homeLoaded == false) {
                return;
            }
            $rootScope.viewInfo.icon = "fa flaticon-businessman278";
            $rootScope.viewInfo.title = "Department Details";

            var vm = this;

            vm.loading = true;
            var deptId = $stateParams.departmentId;
            vm.department = null;
            vm.loadDepartment = loadDepartment;
            vm.back = back;
            vm.updateDepartment = updateDepartment;
            vm.newTask = newTask;
            vm.resetPage = resetPage;
            vm.freeTextSearch = freeTextSearch;
            vm.departmentDetailsTabActivated = departmentDetailsTabActivated;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/department/details/tabs/basic/departmentBasicInfoView.jsp',
                    active: true
                },
                tasks: {
                    id: 'details.tasks',
                    heading: 'Tasks',
                    template: 'app/desktop/modules/department/details/tabs/tasks/departmentTasksView.jsp',
                    active: false
                },
                persons: {
                    id: 'details.persons',
                    heading: 'Persons',
                    template: 'app/desktop/modules/department/details/tabs/persons/departmentPersonsView.jsp',
                    active: false
                }
            };


            function freeTextSearch(searchString) {
                if(vm.tabs.persons.active) {
                    $scope.$broadcast('app.department.persons.freetextsearch', searchString);
                }

                if(vm.tabs.tasks.active) {
                    $scope.$broadcast('app.department.tasks.freetextsearch', searchString);
                }
            }


            function resetPage() {
                $scope.$broadcast('app.department.resettasks');
            }


            function newTask() {
                $rootScope.$broadcast('app.task.addTask')
            }

            function loadDepartment() {
                vm.loading = true;
                DepartmentService.getDepartmentById(deptId).then(
                    function (data) {
                        vm.department = data;
                        vm.loading = false;

                    }
                )
            }

            function updateDepartment() {
                $scope.$broadcast('app.department.update');
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        vm.tabs[t].active = (t != undefined && t == tab);
                    }
                }
            }

            function departmentDetailsTabActivated(tabId) {
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

            function back() {
                window.history.back();
                loadDepartment();
            }

            (function() {
                loadDepartment();
            })();
        }
    }
);


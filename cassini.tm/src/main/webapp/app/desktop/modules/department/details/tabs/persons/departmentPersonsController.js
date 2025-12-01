define(
    [
        'app/desktop/modules/department/department.module',
        'app/desktop/modules/task/new/newTaskDialogueController',
        'app/shared/services/projectService',
        'app/shared/services/taskService',
        'app/shared/services/departmentService',
        'app/desktop/modules/department/changeDepartmentController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('DepartmentPersonsController', DepartmentPersonsController);

        function DepartmentPersonsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal, ProjectService,
                                             TaskService, CommonService, DialogService, DepartmentService) {
            var vm = this;

            vm.deptId = $stateParams.departmentId;
            vm.persons = null;
            vm.previousPage = previousPage;
            vm.nextPage = nextPage;
            vm.loading = true;
            vm.deleteDeptPerson = deleteDeptPerson;
            vm.freeTextSearch = freeTextSearch;
            vm.openDepartmentDialogue = openDepartmentDialogue;

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                }
            };

            vm.filters = {
                project: vm.projectId,
                name: null,
                description: null,
                status: null,
                assignedTo: null,
                verifiedBy: null,
                approvedBy: null,
                searchQuery: null,
                location: null
            };

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.filters.searchQuery = freeText;
                    CommonService.freeTextSearch(vm.filters, pageable).then(
                        function (data) {
                            var ids = [];
                            var persons = data.content;
                            angular.forEach(persons, function (person) {
                                ids.push(person.id);
                            })
                            DepartmentService.getMultiplePersons(ids).then(
                                function (data) {
                                    var deptPersons = data;
                                    var personIds = [];
                                    angular.forEach(deptPersons, function (deptPerson) {
                                        if (deptPerson.department == vm.deptId) {
                                            personIds.push(deptPerson.person);
                                        }
                                    })
                                    CommonService.getPersons(personIds).then(function (data) {
                                        vm.persons = data;
                                    });
                                })
                        })
                } else {
                    resetPage();
                    loadDepartmentPersons();
                }
            }

            function deleteDeptPerson(person) {
                var options = {
                    title: 'Delete Person',
                    message: 'Are you sure you want to delete this Person?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        DepartmentService.deleteDepartmentPerson(vm.deptId, person.id).then(
                            function (data) {
                                var index = vm.persons.indexOf(person);
                                vm.persons.splice(index, 1);
                                $rootScope.showErrorMessage("Deleted Successfully!");
                                loadDepartmentPersons();
                            }
                        )
                    }
                });
            }

            function loadDepartmentPersons() {
                DepartmentService.getPersonsByDepartment(vm.deptId).then(
                    function (data) {
                        vm.persons = data;
                        vm.ids = [];
                        angular.forEach(vm.persons, function (person) {
                            vm.ids.push(person.person);
                        });
                        CommonService.getPersons(vm.ids).then(function (data) {
                            vm.persons = data;
                        });

                        /* TaskService.getDepartmentTasksByPersonIds(vm.ids).then(function(data){
                         vm.DepartmentTasks = data;
                         });*/
                    }
                )
                vm.loading = false;
            }

            function nextPage() {
                if (vm.departments.last != true) {
                    pageable.page++;
                }
            }

            function resetPage() {
                pageable.page = 0;
            }

            /*function search(freeText) {
             if (freeText != null && freeText != undefined && freeText.trim() != "") {
             vm.filters.searchQuery = freeText;
             TaskService.freeTextSearch(vm.filters, pageable, freeText).then(
             function (data) {
             vm.tasks = data;
             vm.clear = true;
             }
             )
             } else {
             resetPage();
             }
             }*/

            function previousPage() {
                if (vm.departments.first != true) {
                    pageable.page--;
                }
            }

            function openDepartmentDialogue(person) {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/department/changeDepartmentDialogue.jsp',
                    controller: 'ChangeDepartmentController as changeDepartmentVm',
                    size: 'md'
                });
                modalInstance.result.then(
                    function (result) {
                        var departmentPerson = {
                            person: person.id,
                            department: result.id
                        }
                        DepartmentService.deleteDepartmentPerson(vm.deptId, person.id).then(
                            function (data) {
                                var index = vm.persons.indexOf(data);
                                vm.persons.splice(index, 1);
                                DepartmentService.createPersonsByDepartment(result.id, departmentPerson).then(
                                    function (data) {
                                        loadDepartmentPersons();
                                    }
                                )
                            }
                        )
                        $rootScope.showSuccessMessage("Department changed Successfully ");
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadDepartmentPersons();
                    $scope.$on('app.department.persons.freetextsearch', function (event, data) {
                        freeTextSearch(data);
                    })
                }
            })();
        }
    }
);
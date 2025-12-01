define(['app/desktop/modules/department/department.module',
        'app/desktop/modules/department/new/newDepartmentController.js',
        'app/shared/services/departmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/department/personsSelectionController'
    ],
    function (module) {
        module.controller('AllDepartmentsController', AllDepartmentsController);

        function AllDepartmentsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal, DepartmentService,
                                          DialogService, CommonService) {

            var vm = this;

            $rootScope.viewInfo.icon = "fa flaticon-businessman278";
            $rootScope.viewInfo.title = "Departments";

            vm.createDepartment = createDepartment;
            vm.deleteDepartment = deleteDepartment;
            vm.deletePerson = deletePerson;
            vm.departments = [];
            vm.persons = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.loadDepartments = loadDepartments;
            vm.freeTextSearch = freeTextSearch;
            vm.loading = true;
            vm.addPerson = addPerson;
            vm.showDepartmentDetails = showDepartmentDetails;
            vm.hideDeptPersons = hideDeptPersons;
            vm.showDeptPersons = showDeptPersons;
            vm.resetPage = resetPage;
            vm.clearFilter = clearFilter;
            vm.clear = false;
            vm.filters = {
                searchQuery: null
            }
            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                }
            };
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.departments = angular.copy(pagedResults);

            function clearFilter(){
                loadDepartments();
                vm.clear = false;
            }

            function createDepartment() {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/department/new/newDepartmentView.jsp',
                    controller: 'NewDepartmentController as newDepartmentVm',
                    size: 'md'
                });
                modalInstance.result.then(
                    function (result) {
                        DepartmentService.createDepartment(result).then(
                            function (data) {
                                vm.departments.content.unshift(data);
                                $rootScope.showSuccessMessage("Department Created Successfully ");
                            }
                        )

                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.filters.searchQuery = freeText;
                    DepartmentService.freeTextSearch(vm.filters, pageable).then(
                        function (data) {
                            vm.departments = data;
                        }
                    )
                } else {
                    resetPage();
                    loadDepartments();
                }
                vm.clear = true;
            }

            /*function freeTextSearch(freeText) {

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
             var depts = [];
             angular.forEach(deptPersons, function (deptPerson) {
             if (depts.indexOf(deptPerson.department) == -1) {
             depts.push(deptPerson.department);
             }
             })
             DepartmentService.getMultiple(depts).then(
             function (data) {
             vm.departments = data;
             if (vm.departments.length == 1) {
             angular.forEach(vm.departments, function (department) {
             department.persons = persons;
             })
             } else {
             var personIds = [];
             angular.forEach(vm.departments, function (department) {
             personIds = [];
             angular.forEach(deptPersons, function (deptPerson) {
             if (department.id == deptPerson.department) {
             personIds.push(deptPerson.person);
             }
             })
             CommonService.getPersons(personIds).then(function (data) {
             department.persons = data;
             });
             })
             }
             }
             )
             }
             );
             }
             )
             } else {
             resetPage();
             loadDepartments();
             }
             }*/

            function resetPage() {
                pageable.page = 0;
            }

            function nextPage() {
                if (vm.departments.last != true) {
                    pageable.page++;
                    loadDepartments();
                }
            }

            function previousPage() {
                if (vm.departments.first != true) {
                    pageable.page--;
                    loadDepartments();
                }
            }

            /*function resetPersons(department) {
             getPersonsByDepartment(department);
             }*/

            /*function freeSearchForPersons(freeText, department) {
             if (freeText != null && freeText != undefined && freeText.trim() != "") {
             vm.filters.searchQuery = freeText;
             CommonService.freeTextSearch(vm.filters, pageable).then(
             function (data) {
             var persons = data.content;
             getPersonsByDepartment(department);
             var filterPersons = [];
             DepartmentService.getPersonsByDepartment(department.id).then(
             function (data) {
             department.persons = data;
             var ids = [];
             angular.forEach(department.persons, function (person) {
             ids.push(person.person);
             });
             CommonService.getPersons(ids).then(function (data) {
             department.persons = data;
             angular.forEach(persons, function (person) {
             angular.forEach(department.persons, function (deptPerson) {
             if (person.id == deptPerson.id) {
             filterPersons.push(deptPerson);
             }
             })
             }
             )
             department.persons = filterPersons;
             });
             }
             )

             }
             )
             } else {
             resetPersons(department);
             }
             }*/

            function loadDepartments() {
                DepartmentService.getPagedDepartments(pageable).then(
                    function (data) {
                        vm.departments = data;
                    });
                vm.loading = false;
            }

            function getPersonsByDepartment(department) {
                DepartmentService.getPersonsByDepartment(department.id).then(
                    function (data) {
                        department.persons = data;
                        var ids = [];
                        angular.forEach(department.persons, function (person) {
                            ids.push(person.person);
                        });
                        CommonService.getPersons(ids).then(function (data) {
                            department.persons = data;
                        });
                    }
                )
            }

            function showDeptPersons(department) {
                getPersonsByDepartment(department);
            }

            function hideDeptPersons(department) {
                department.persons = null;
            }

            function deleteDepartment(department) {
                var options = {
                    title: 'Delete Department',
                    message: 'Are you sure you want to delete this Department?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        DepartmentService.deleteDepartment(department.id).then(
                            function (data) {
                                var index = vm.departments.content.indexOf(department);
                                vm.departments.content.splice(index, 1);
                                $rootScope.showErrorMessage(" Deleted Successfully!");
                                //loadDepartments();
                            }
                        )
                    }
                });
            }

            function deletePerson(person, department) {
                var options = {
                    title: 'Delete Person',
                    message: 'Are you sure you want to delete this Person?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        DepartmentService.deleteDepartmentPerson(department.id, person.id).then(
                            function (data) {
                                var index = department.persons.indexOf(person);
                                department.persons.splice(index, 1);
                                $rootScope.showErrorMessage("Person deleted Successfully in the Department!");
                            }
                        )
                    }
                });
            }


            function showDepartmentDetails(department) {
                $state.go('app.department.details', {departmentId: department.id});
            }


            function addPerson(department) {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/department/personsSelectionView.jsp',
                    controller: 'PersonsSelectionController as personsSelectionVm',
                    size: 'lg'
                });

                modalInstance.result.then(
                    function (result) {
                        angular.forEach(result, function (person) {
                            var departmentPerson = {
                                department: department.id,
                                person: person.id
                            }
                            DepartmentService.createPersonsByDepartment(department.id, departmentPerson).
                                then(function (data) {
                                    CommonService.getPerson(departmentPerson.person).then(function (data) {
                                        department.persons.unshift(data);
                                    });
                                });
                        })
                        $rootScope.showSuccessMessage(result.length + " persons added to " + department.name);
                    }
                );
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadDepartments();
                }
            })();
        }
    }
);

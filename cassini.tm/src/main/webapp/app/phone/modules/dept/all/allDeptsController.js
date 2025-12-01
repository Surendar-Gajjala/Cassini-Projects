define(
    [
        'app/phone/modules/dept/dept.module',
        'app/shared/services/departmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('AllDeptsController', AllDeptsController);

        function AllDeptsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application,
                                    DepartmentService, CommonService) {

            var vm = this;

            vm.departments = [];

            vm.togglePersons = togglePersons;


            function togglePersons(department) {
                department.showPersons = !department.showPersons;
            }


            function loadDepartments() {
                DepartmentService.getAllDepartments().then(
                    function (data) {
                        vm.departments = data.content;
                        angular.forEach(vm.departments, function (department) {
                            department.persons = [];
                            department.showPersons = false;

                            DepartmentService.getPersonsByDepartment(department.id).then(
                                function (data) {
                                    department.persons = data;
                                    var ids = [];

                                    angular.forEach(department.persons, function (person) {
                                        ids.push(person.person);
                                    });
                                    CommonService.getPersons(ids).then(function (data) {
                                        department.persons = data;
                                        sortPersons(department.persons);
                                    });
                                }
                            )
                        });

                        if(vm.departments.length == 1) {
                            togglePersons(vm.departments[0]);
                        }
                    }
                );
            }

            function sortPersons(persons) {
                persons.sort(function(p1, p2) {
                    return p1.firstName.toLowerCase().localeCompare(p2.firstName.toLowerCase())
                });
            }


            (function () {
                loadDepartments();
            })();
        }
    }
);
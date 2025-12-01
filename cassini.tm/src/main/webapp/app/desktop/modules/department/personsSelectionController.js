define([
        'app/desktop/modules/department/department.module',
        'app/shared/services/departmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('PersonsSelectionController', PersonsSelectionController);

        function PersonsSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModalInstance, DepartmentService,
                                            CommonService) {
            var vm = this;

            var pageable = {
                page: 0,
                size: 30,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
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

            vm.persons = angular.copy(pagedResults);
            vm.cancel = cancel;
            vm.selectCheck = selectCheck;
            vm.onOk = onOk;
            vm.selectedPersons = [];

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            function loadPersons() {
                CommonService.getAllPersonsByPersonType(1).then(
                    function (data) {
                        vm.persons = data;
                    }
                )
            }

            function onOk() {
                $uibModalInstance.close(vm.selectedPersons);
            }

            function selectCheck(person) {
                var flag = true;
                angular.forEach(vm.selectedPersons, function (selectedPerson) {
                    if (selectedPerson.id == person.id) {
                        flag = false;
                        var index = vm.selectedPersons.indexOf(person);
                        vm.selectedPersons.splice(index, 1);
                    }
                })
                if (flag) {
                    vm.selectedPersons.push(person);
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadPersons();
                }
            })();
        }
    }
);
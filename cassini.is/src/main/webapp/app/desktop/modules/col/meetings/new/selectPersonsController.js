define(['app/desktop/modules/col/col.module',
        'app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('SelectPersonsController', SelectPersonsController);

        function SelectPersonsController($scope, $rootScope, $timeout, $state, $stateParams,
                                         $cookies, attendes, $uibModalInstance, CommonService) {
            var vm = this;
            vm.cancel = cancel;
            vm.onOk = onOk;
            vm.selectCheckBox = selectCheckBox;
            vm.persons = null;
            vm.selectPersons = attendes;

            function selectCheckBox(index, person) {
                var flag = true;

                for (var i = 0; i < vm.selectPersons.length; i++) {
                    var selectedPerson = vm.selectPersons[i];
                    if (selectedPerson.id == person.id) {
                        flag = false;
                        var index = i;
                        vm.selectPersons.splice(index, 1);
                    }
                }
                if (flag) {
                    vm.selectPersons.push(person);
                }

            }

            function onOk() {
                $uibModalInstance.close(vm.selectPersons);
            }

            function loadPersons() {
                CommonService.getAllPersons().then(
                    function (data) {
                        vm.persons = data;
                        if (vm.selectPersons.length > 0) {
                            angular.forEach(vm.persons, function (person) {
                                    if (findById(vm.selectPersons, person.id)) {
                                        person.check = true;
                                    } else {
                                        person.check = false;
                                    }
                                }
                            )
                        } else {
                            angular.forEach(vm.persons, function (person) {
                                person.check = false;
                            })
                        }
                    })
            }

            function findById(source, id) {
                var value = false;
                for (var i = 0; i < source.length; i++) {
                    if (source[i].id === id) {
                        value = true;
                    }
                }
                return value;
            }

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadPersons();
                }
            })();
        }
    }
);

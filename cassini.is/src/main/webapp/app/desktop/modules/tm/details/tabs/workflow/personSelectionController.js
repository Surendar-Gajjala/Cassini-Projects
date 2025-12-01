define(
    [
        'app/desktop/modules/tm/tm.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/pm/project/projectService'
    ],
    function (module) {
        module.controller('PersonSelectionController', PersonSelectionController);

        function PersonSelectionController($scope, $rootScope, $stateParams, $translate, $timeout, CommonService, ProjectService) {
            var vm = this;

            var personType = $scope.data.selectedPersonType;
            var type = $scope.data.selectedType;

            var project = $stateParams.projectId;
            vm.persons = [];
            /*  vm.persons = [];
             var personType = $scope.data.selectedPersonType;
             var type = $scope.data.selectedType;
             var parsed = angular.element("<div></div>");
             var personSelection = parsed.html($translate.instant("PLEASE_SELECT_ONE_PERSON")).html();
             var personAddedApprovers = parsed.html($translate.instant("ADDED_APPROVERS")).html();
             var personAddedObserver = parsed.html($translate.instant("ADDED_OBSERVERS")).html();
             var personAddedAcknowledger = parsed.html($translate.instant("ADDED_ACKNOWLEDGER")).html();

             vm.onOk = onOk;
             vm.selectCheck = selectCheck;
             vm.selectAll = selectAll;

             function loadPersons() {
             LoginService.getAllLogins().then(
             function (data) {
             angular.forEach(data, function (person) {
             if (person.external == false) {
             person.selected = false;

             if (personType == "approvers") {
             if (type.approvers.length > 0) {
             var personExit = false;
             angular.forEach(type.approvers, function (approver) {
             if (approver.personObject.id == person.person.id) {
             personExit = true;
             }
             })

             if (!personExit) {
             vm.persons.push(person.person)
             }
             } else {
             vm.persons.push(person.person)
             }
             }
             if (personType == "observers") {
             if (type.observers.length > 0) {
             var personExit = false;
             angular.forEach(type.observers, function (observer) {
             if (observer.personObject.id == person.person.id) {
             personExit = true;
             }
             })
             if (!personExit) {
             vm.persons.push(person.person)
             }
             } else {
             vm.persons.push(person.person)
             }
             }
             if (personType == "acknowledgers") {
             if (type.acknowledgers.length > 0) {
             var personExit = false;
             angular.forEach(type.acknowledgers, function (acknowledger) {
             if (acknowledger.personObject.id == person.person.id) {
             personExit = true;
             }
             })
             if (!personExit) {
             vm.persons.push(person.person)
             }
             } else {
             vm.persons.push(person.person)
             }
             }

             }
             })
             }
             )
             }

             vm.existedNumbers = [];
             var persons = "";

             function onOk() {
             vm.existedNumbers = [];
             persons = "";
             if (vm.selectPersons.length == 0) {
             $rootScope.showWarningMessage(personSelection);
             }
             if (vm.selectPersons.length > 0) {
             angular.forEach(vm.selectPersons, function (person) {
             if (vm.existedNumbers.length == 0) {
             persons = persons + " " + person.firstName;
             } else {
             persons = persons + ", " + person.firstName;
             }
             vm.existedNumbers.push(person);
             });
             $scope.callback(vm.selectPersons);
             if (personType == 'approvers') {
             $rootScope.showSuccessMessage(persons + ":" + personAddedApprovers);
             } else if (personType == 'observers') {
             $rootScope.showSuccessMessage(persons + ":" + personAddedObserver);
             } else if (personType == 'acknowledgers') {
             $rootScope.showSuccessMessage(persons + ":" + personAddedAcknowledger);
             }
             }
             }

             vm.selectAllCheck = false;
             vm.selectPersons = [];
             function selectCheck(person) {
             var flag = true;
             angular.forEach(vm.selectPersons, function (persons) {
             if (persons.id == person.id) {
             flag = false;
             var index = vm.selectPersons.indexOf(person);
             vm.selectPersons.splice(index, 1);
             }
             });
             if (flag) {
             vm.selectPersons.push(person);
             }

             if (vm.selectPersons.length == vm.persons.length) {
             vm.selectAllCheck = true;
             } else {
             vm.selectAllCheck = false;
             }
             }


             function selectAll(check) {
             vm.selectPersons = [];
             if (check) {
             $scope.check = false;
             angular.forEach(vm.persons, function (person) {
             person.selected = false;
             })
             } else {
             $scope.check = true;
             vm.error = "";
             angular.forEach(vm.persons, function (person) {
             person.selected = true;
             vm.selectPersons.push(person);
             })
             }
             }*/

            vm.selectAllCheck = false;
            vm.selectPersons = [];
            vm.selectCheck = selectCheck;
            function selectCheck(person) {
                var flag = true;
                angular.forEach(vm.selectPersons, function (persons) {
                    if (persons.id == person.id) {
                        flag = false;
                        var index = vm.selectPersons.indexOf(person);
                        vm.selectPersons.splice(index, 1);
                    }
                });
                if (flag) {
                    vm.selectPersons.push(person);
                }

                if (vm.selectPersons.length == vm.persons.length) {
                    vm.selectAllCheck = true;
                } else {
                    vm.selectAllCheck = false;
                }
            }

            vm.selectAll = selectAll;
            function selectAll(check) {
                vm.selectPersons = [];
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.persons, function (person) {
                        person.selected = false;
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.persons, function (person) {
                        person.selected = true;
                        vm.selectPersons.push(person);
                    })
                }
            }

            function loadProjectPersons() {
                ProjectService.getProjectPersons(project).then(
                    function (data) {
                        vm.projectPersons = data;
                        angular.forEach(vm.projectPersons, function (obj) {
                            loadPersonsById(obj.person);
                        })
                    }
                );
            }

            function loadPersonsById(person) {
                vm.loading = false;
                CommonService.getPerson(person).then(
                    function (data) {
                        vm.persons.push(data);
                    }
                )
            }

            function onOk() {
                if (vm.selectPersons.length > 0) {
                    $scope.callback(vm.selectPersons);
                } else {
                    $rootScope.showWarningMessage("Please select any person")
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.workflow.persons', onOk);
                    loadProjectPersons();

                }
            })();
        }
    }
);
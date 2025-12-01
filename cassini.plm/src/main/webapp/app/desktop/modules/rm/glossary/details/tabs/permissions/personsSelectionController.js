define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/glossaryService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('PersonsSelectionController', PersonsSelectionController);

        function PersonsSelectionController($scope, $rootScope, $stateParams, $timeout, $translate, CommonService, GlossaryService) {
            var vm = this;

            vm.persons = [];

            vm.onOk = onOk;
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;

            var parsed = angular.element("<div></div>");
            var pleaseSelectAtleastOnePerson = parsed.html($translate.instant("PLEASE_SELECT_ONE_PERSON")).html();

            function loadPersons() {
                GlossaryService.getAllPersons($stateParams.glossaryId).then(
                    function (data) {
                        vm.logins = data;
                        angular.forEach(vm.logins, function (login) {
                            vm.persons.push(login.person);
                            login.person.selected = false;
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.existedNumbers = [];
            var persons = "";

            function onOk() {
                if (vm.selectPersons.length == 0) {
                    $rootScope.showWarningMessage(pleaseSelectAtleastOnePerson)
                } else {
                    GlossaryService.createGlossaryPersons($stateParams.glossaryId, vm.selectPersons).then(
                        function (data) {
                            $rootScope.hideSidePanel();
                            $scope.callback(vm.selectPersons);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
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
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    $rootScope.$on('app.add.person', onOk);
                    loadPersons();
                //}
            })();
        }
    }
);
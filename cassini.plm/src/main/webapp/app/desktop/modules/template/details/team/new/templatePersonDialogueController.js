define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectTemplateService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'

    ],
    function (module) {
        module.controller('TemplatePersonDialogueController', TemplatePersonDialogueController);

        function TemplatePersonDialogueController($scope, $rootScope, $timeout, $translate, $state, LoginService, ProjectTemplateService, $stateParams, CommonService, ItemService) {

            var vm = this;
            vm.select = select;
            vm.create = create;
            vm.checkAll = checkAll;

            vm.loadPersons = loadPersons;

            var projectPersons = $scope.data.projecPersons;
            vm.selectedAll = false;
            vm.selectedPersons = [];
            vm.persons = [];
            vm.persons = [];

            var parsed = angular.element("<div></div>");
            var personAddedSuccesMessage = parsed.html($translate.instant("PERSON_ADD_SUCCESS")).html();
            var personAddMsg = parsed.html($translate.instant("PERSON_ADD_MSG")).html();

            function select(person) {
                var flag = true;
                if (person.selected == false) {
                    vm.selectedAll = false;
                    var index = vm.selectedPersons.indexOf(person);
                    vm.selectedPersons.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedPersons, function (selectPerson) {
                        if (selectPerson.id == person.id) {
                            flag = false;
                            var index = vm.selectedPersons.indexOf(person);
                            vm.selectedPersons.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedPersons.push(person);
                    }
                    if (vm.selectedPersons.length == vm.persons.length) {
                        vm.selectedAll = true;
                    }

                }
            }

            function checkAll() {
                if (vm.selectedAll) {
                    vm.selectedPersons = [];
                    vm.selectedAll = true;
                    angular.forEach(vm.persons, function (person) {
                        person.selected = vm.selectedAll;
                        vm.selectedPersons.push(person);
                    });
                } else {
                    vm.selectedAll = false;
                    angular.forEach(vm.persons, function (person) {
                        person.selected = vm.selectedAll;
                        vm.selectedPersons = [];
                    });
                }
            }

            function create() {
                if (vm.selectedPersons.length != 0) {
                    /*ProjectTemplateService.createProjectTemplateMembers($stateParams.templateId, vm.selectedPersons).then(
                     function (data) {
                     $rootScope.hideSidePanel('right');
                     $scope.callback(data);
                     vm.selectedPersons = [];
                     vm.creating = false;
                     $rootScope.showSuccessMessage(personAddedSuccesMessage);
                     $scope.$off('app.template.person.new', create);
                     }, function (error) {
                     $rootScope.showErrorMessage(error.message);
                     $rootScope.hideBusyIndicator();
                     $scope.$off('app.template.person.new', create);
                     }
                     )*/
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedPersons);
                    $scope.$off('app.template.person.new', create);

                } else {
                    $rootScope.showWarningMessage(personAddMsg);
                }
            }


            function loadPersons() {
                vm.loading = true;
                CommonService.getAllActivePersons().then(
                    function (data) {
                        vm.loading = false;
                        vm.persons = data;
                        angular.forEach(projectPersons.content, function (projectPerson) {
                            angular.forEach(vm.persons, function (person) {
                                if (person.id == projectPerson.person) {
                                    var index = vm.persons.indexOf(person);
                                    vm.persons.splice(index, 1);
                                }
                            });

                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadPersons();
                $scope.$on('app.template.person.new', create);
                //}
            })();
        }

    }
);
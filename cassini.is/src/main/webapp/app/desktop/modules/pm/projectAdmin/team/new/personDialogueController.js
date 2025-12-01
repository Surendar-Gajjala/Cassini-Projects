define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/login/loginService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'

    ],
    function (module) {
        module.controller('PersonDialogueController', PersonDialogueController);

        function PersonDialogueController($scope, $rootScope, $timeout, $state, ProjectService, $stateParams, CommonService, ItemService, LoginService) {

            var vm = this;
            vm.select = select;
            vm.create = create;
            vm.checkAll = checkAll;

            vm.loadManpower = loadManpower;

            var projectPersons = $scope.data.projecPersons;
            vm.selectedAll = false;
            vm.selectedPersons = [];
            vm.persons = [];
            vm.manpower = [];

            vm.pageable = {
                page: 0,
                size: 20,
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
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

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
                }
                if (vm.selectedPersons.length == vm.manpower.length) {
                    vm.selectedAll = true;
                }
            }

            function checkAll() {
                if (vm.selectedAll) {
                    vm.selectedPersons = [];
                    vm.selectedAll = true;
                    angular.forEach(vm.manpower, function (person) {
                        person.selected = vm.selectedAll;
                        vm.selectedPersons.push(person);
                    });
                } else {
                    vm.selectedAll = false;
                    angular.forEach(vm.manpower, function (person) {
                        person.selected = vm.selectedAll;
                        vm.selectedPersons = [];
                    });
                }
            }

            function create() {
                if (vm.selectedPersons.length != 0) {
                    var projectPersons = [];
                    angular.forEach(vm.selectedPersons, function (person) {
                        var obj = {
                            project: $stateParams.projectId,
                            person: person.person.id
                        };
                        projectPersons.push(obj);
                    });
                    if (projectPersons.length > 0) {
                        ProjectService.createProjectPerson($stateParams.projectId, projectPersons).then(
                            function (data) {
                                $rootScope.hideSidePanel('right');
                                $scope.callback(data);
                                vm.selectedPersons = [];
                                vm.creating = false;
                                $rootScope.showSuccessMessage("Person(s) added to the Team successfully")
                            }
                        )
                    }
                } else {
                    $rootScope.showErrorMessage("Please add atleast one Person");
                }
            }

            /*function loadManpower() {
             vm.loading = true;
             ItemService.getManpower(vm.pageable).then(
             function (data) {
             vm.loading = false;
             vm.manpower = data;
             CommonService.getPersonReferences(vm.manpower.content, 'createdBy');
             CommonService.getPersonReferences(vm.manpower.content, 'modifiedBy');

             });
             }*/

            function loadManpower() {
                vm.loading = true;
                ItemService.getActiveManpower().then(
                    function (data) {
                        vm.loading = false;
                        vm.manpower = data;

                        CommonService.getPersonReferences(vm.manpower, 'createdBy');
                        CommonService.getPersonReferences(vm.manpower, 'modifiedBy');

                        angular.forEach(projectPersons.content, function (projectPerson) {
                            angular.forEach(vm.manpower, function (person) {
                                if (person.person.id == projectPerson.personObject.id) {
                                    var index = vm.manpower.indexOf(person);
                                    vm.manpower.splice(index, 1);
                                }
                            });

                        });

                    }
                );

            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadManpower();
                    $scope.$on('app.person.new', create);
                }
            })();
        }

    }
);
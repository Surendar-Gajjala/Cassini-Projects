/**
 * Created by swapna on 06/09/18.
 */
define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/login/loginService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/proc/manpower/directive/manpowerDirective'

    ],
    function (module) {
        module.controller('UserDialogController', UserDialogController);

        function UserDialogController($scope, $rootScope, $timeout, $state, ProjectService, $stateParams, CommonService, ItemService, LoginService) {

            var vm = this;
            var manpowerItems = $scope.data.manpower;
            vm.select = select;
            vm.checkAll = checkAll;

            vm.selectedAll = false;
            vm.selectedPersons = [];
            vm.persons = [];
            vm.logins = [];
            vm.manpower = [];
            vm.selectedManpowerType = null;
            vm.onSelectType = onSelectType;
            vm.newManpower = {
                person: null,
                personType: null,
                itemType: null,
                objectType: "MANPOWERTYPE"
            };

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedManpowerType = itemType;
                    loadSystemUsers();
                }
            }

            function select(person) {
                if (!person.selected) {
                    vm.selectedAll = false;
                    var index = vm.selectedPersons.findIndex(item >= item.person.id == person.person.id
                )
                    ;
                    vm.selectedPersons.splice(index, 1);
                } else {
                    var manpower = angular.copy(vm.newManpower);
                    manpower.person = person.person;
                    manpower.itemType = vm.selectedManpowerType;
                    manpower.personType = person.person.personType;
                    vm.selectedPersons.push(manpower);
                }
                if (vm.selectedPersons.length == vm.persons.length) {
                    vm.selectedAll = true;
                }
            }

            function checkAll(value) {
                vm.selectedPersons = [];
                if (value) {
                    angular.forEach(vm.persons, function (person) {
                        var manpower = angular.copy(vm.newManpower);
                        manpower.itemType = vm.selectedManpowerType;
                        manpower.personType = person.person.personType;
                        manpower.person = person.person;
                        vm.selectedPersons.push(manpower);
                        person.selected = value;
                    });
                } else {
                    angular.forEach(vm.persons, function (person) {
                        person.selected = value;
                    });
                }
            }

            function loadSystemUsers() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        vm.persons = data;
                        vm.logins = angular.copy(data);
                        angular.forEach(manpowerItems.content, function (manpower) {
                            angular.forEach(vm.persons, function (person) {
                                if (person.isActive == true) {
                                    if ((vm.selectedManpowerType.id == manpower.itemType.id) && (manpower.person.id == person.person.id)) {
                                        var index = vm.persons.indexOf(person);
                                        vm.persons.splice(index, 1);
                                    }
                                }
                            });
                        });
                    }
                )
            }

            $rootScope.$on('app.manpower.items', function createItems(event) {
                if (vm.selectedPersons.length > 0) {
                    $scope.callback(vm.selectedPersons);
                } else {
                    $rootScope.showWarningMessage("Please select atleast one person to Import");
                }
            });

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }

    }
);
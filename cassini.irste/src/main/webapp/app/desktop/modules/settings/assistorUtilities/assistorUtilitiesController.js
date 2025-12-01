/**
 * Created by SRAVAN on 12/11/2018.
 */
define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/shared/services/core/userService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.controller('AssistorUtilitiesController', AssistorUtilitiesController);

        function AssistorUtilitiesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                              UserService, LovService) {
            var vm = this;
            vm.allUtilities = [];
            vm.showGrid = false;
            vm.utilities = [];
            vm.toggleResponderUtilities = toggleResponderUtilities;
            vm.getResponderIndexInGroup = getResponderIndexInGroup;

            function getResponderIndexInGroup(assistor, utility) {
                var index = -1;
                if (assistor.utilities != null && assistor.utilities != undefined) {
                    for (var i = 0; i < assistor.utilities.length; i++) {
                        if (assistor.utilities[i].name == utility.name) {
                            index = i;
                        }
                    }
                }
                return index;
            }

            function toggleResponderUtilities(assistor, utility) {
                $rootScope.showBusyIndicator($("#assistorUtilitiesId"));
                utility.name = utility.name.replace("/", "%2F");
                utility.name = utility.name.replace("/", "%2F");
                utility.name = utility.name.replace("/", "%2F");
                if (utility.selected == true) {
                    UserService.addResponderUtility(utility.name, assistor.id, assistor.personType).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
                else {
                    UserService.deleteResponderUtility(utility.name, assistor.id).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            function loadUtilities() {
                LovService.getLovByName("Utilities").then(
                    function (data) {
                        vm.utilities = data.values;
                        vm.utilityObjects = [];
                        angular.forEach(vm.utilities, function (utility) {
                            var util = {
                                name: utility,
                                selected: false
                            };
                            vm.utilityObjects.push(util);
                        });
                        loadResponders('Assistor');

                    }
                )
            }

            function loadResponders() {
                UserService.getAllResponders('Assistor').then(
                    function (data) {
                        vm.assistors = data;
                        vm.showGrid = true;
                        setValuesFromDBToResponder();
                    }
                );
            }

            function setValuesFromDBToResponder() {
                for (var i = 0; i < vm.assistors.length; i++) {
                    for (var j = 0; j < vm.utilityObjects.length; j++) {
                        vm.utilityObjects[j]["selected"] = isResponderAssignedToUtility(vm.utilityObjects[j], vm.assistors[i]);
                    }
                    vm.assistors[i].utilities = angular.copy(vm.utilityObjects);
                }
            }

            function isResponderAssignedToUtility(utility, assistor) {
                var assigned = false;
                if(assistor.utilities != null){
                    for (var j = 0; j < assistor.utilities.length; j++) {
                        if ((assistor.utilities[j]).toLowerCase() == (utility.name).toLowerCase()) {
                            assigned = true;
                            break;
                        }
                    }
                }
                return assigned;
            }

            (function () {
                loadUtilities();
            })();
        }
    }
);
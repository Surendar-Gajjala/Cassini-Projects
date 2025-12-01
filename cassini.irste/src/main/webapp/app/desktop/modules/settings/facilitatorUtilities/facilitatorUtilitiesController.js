/**
 * Created by SRAVAN on 12/11/2018.
 */

/**
 * Created by Nageshreddy on 20-11-2018.
 */
define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/shared/services/core/userService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.controller('FacilitatorUtilitiesController', FacilitatorUtilitiesController);

        function FacilitatorUtilitiesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                              UserService, LovService) {
            var vm = this;
            vm.allUtilities = [];
            vm.showGrid = false;
            vm.utilities = [];
            vm.toggleResponderUtilities = toggleResponderUtilities;
            vm.getResponderIndexInGroup = getResponderIndexInGroup;

            function getResponderIndexInGroup(facilitator, utility) {
                var index = -1;
                if (facilitator.utilities != null && facilitator.utilities != undefined) {
                    for (var i = 0; i < facilitator.utilities.length; i++) {
                        if (facilitator.utilities[i].name == utility.name) {
                            index = i;
                        }
                    }
                }
                return index;
            }

            function toggleResponderUtilities(facilitator, utility) {
                $rootScope.showBusyIndicator($("#facilitatorUtilitiesId"));
                utility.name = utility.name.replace("/", "%2F");
                utility.name = utility.name.replace("/", "%2F");
                utility.name = utility.name.replace("/", "%2F");
                if (utility.selected == true) {
                    UserService.addResponderUtility(utility.name, facilitator.id, facilitator.personType).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
                else {
                    UserService.deleteResponderUtility(utility.name, facilitator.id).then(
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
                        loadResponders('Facilitator');

                    }
                )
            }

            function loadResponders() {
                UserService.getAllResponders('Facilitator').then(
                    function (data) {
                        vm.facilitators = data;
                        vm.showGrid = true;
                        setValuesFromDBToResponder();
                    }
                );
            }

            function setValuesFromDBToResponder() {
                for (var i = 0; i < vm.facilitators.length; i++) {
                    for (var j = 0; j < vm.utilityObjects.length; j++) {
                        vm.utilityObjects[j]["selected"] = isResponderAssignedToUtility(vm.utilityObjects[j], vm.facilitators[i]);
                    }
                    vm.facilitators[i].utilities = angular.copy(vm.utilityObjects);
                }
            }

            function isResponderAssignedToUtility(utility, facilitator) {
                var assigned = false;
                if(facilitator.utilities != null){
                    for (var j = 0; j < facilitator.utilities.length; j++) {
                        if ((facilitator.utilities[j]).toLowerCase() == (utility.name).toLowerCase()) {
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
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
        module.controller('ResponderUtilitiesController', ResponderUtilitiesController);

        function ResponderUtilitiesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                              UserService, LovService) {
            var vm = this;
            vm.allUtilities = [];
            vm.showGrid = false;
            vm.utilities = [];
            vm.toggleResponderUtilities = toggleResponderUtilities;
            vm.getResponderIndexInGroup = getResponderIndexInGroup;

            function getResponderIndexInGroup(responder, utility) {
                var index = -1;
                if (responder.utilities != null && responder.utilities != undefined) {
                    for (var i = 0; i < responder.utilities.length; i++) {
                        if (responder.utilities[i].name == utility.name) {
                            index = i;
                        }
                    }
                }
                return index;
            }

            function toggleResponderUtilities(responder, utility) {
                $rootScope.showBusyIndicator($("#responderUtilitiesId"));
                utility.name = utility.name.replace("/", "%2F");
                utility.name = utility.name.replace("/", "%2F");
                utility.name = utility.name.replace("/", "%2F");
                if (utility.selected == true) {
                    UserService.addResponderUtility(utility.name, responder.id, responder.personType).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
                else {
                    UserService.deleteResponderUtility(utility.name, responder.id).then(
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
                        loadResponders('Responder');

                    }
                )
            }

            function loadResponders() {
                UserService.getAllResponders('Responder').then(
                    function (data) {
                        vm.responders = data;
                        vm.showGrid = true;
                        setValuesFromDBToResponder();
                    }
                );
            }

            function setValuesFromDBToResponder() {
                for (var i = 0; i < vm.responders.length; i++) {
                    for (var j = 0; j < vm.utilityObjects.length; j++) {
                        vm.utilityObjects[j]["selected"] = isResponderAssignedToUtility(vm.utilityObjects[j], vm.responders[i]);
                    }
                    vm.responders[i].utilities = angular.copy(vm.utilityObjects);
                }
            }

            function isResponderAssignedToUtility(utility, responder) {
                var assigned = false;
                if(responder.utilities != null){
                    for (var j = 0; j < responder.utilities.length; j++) {
                        if ((responder.utilities[j]).toLowerCase() == (utility.name).toLowerCase()) {
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
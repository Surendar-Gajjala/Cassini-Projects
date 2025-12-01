/**
 * Created by Nageshreddy on 14-11-2018.
 */
define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/shared/services/core/groupLocationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.controller('UtilityLocationController', UtilityLocationController);

        function UtilityLocationController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                           GroupLocationService, LovService) {
            var vm = this;
            vm.allUtilities = [];
            vm.showGrid = false;
            vm.utilities = [];
            vm.toggleLocationUtilities = toggleLocationUtilities;
            vm.getLocationIndexInGroup = getLocationIndexInGroup;

            function getLocationIndexInGroup(location, utility) {
                var index = -1;
                if (location.utilities != null && location.utilities != undefined) {
                    for (var i = 0; i < location.utilities.length; i++) {
                        if (location.utilities[i].name == utility.name) {
                            index = i;
                        }
                    }
                }
                return index;
            }

            function toggleLocationUtilities(location, utility) {
                $rootScope.showBusyIndicator($("#locationUtilitiesId"));
                utility.name = utility.name.replace("/", "%2F");
                utility.name = utility.name.replace("/", "%2F");
                utility.name = utility.name.replace("/", "%2F");
                if (utility.selected == true) {
                    GroupLocationService.addLocationUtility(utility.name, location.id).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
                else {
                    GroupLocationService.deleteLocationUtility(utility.name, location.id).then(
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
                        loadGroupLocation();
                    }
                )
            }

            function loadGroupLocation() {
                GroupLocationService.getGroups().then(
                    function (data) {
                        vm.groups = data;
                        vm.showGrid = true;
                        setValuesFromDBToLocation();
                    }
                );
            }

            function setValuesFromDBToLocation() {
                for (var i = 0; i < vm.groups.length; i++) {
                    for (var k = 0; k < vm.groups[i].locations.length; k++) {
                        for (var j = 0; j < vm.utilityObjects.length; j++) {
                            vm.utilityObjects[j]["selected"] = isLocationAssignedToUtility(vm.utilityObjects[j], vm.groups[i].locations[k]);
                        }
                        vm.groups[i].locations[k].utilities = angular.copy(vm.utilityObjects);
                    }
                }
            }

            function isLocationAssignedToUtility(utility, location) {
                var assigned = false;
                for (var j = 0; j < location.utilities.length; j++) {
                    if ((location.utilities[j]).toLowerCase() == (utility.name).toLowerCase()) {
                        assigned = true;
                        break;
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
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/proc/classification/directive/classificationTreeController',
        'app/desktop/modules/proc/classification/directive/classificationTreeController'
    ],
    function (module) {
        module.controller('EditMachineController', EditMachineController);

        function EditMachineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                       ItemTypeService, ItemService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-truck";
            $rootScope.viewInfo.title = "Edit Machine";

            var vm = this;

            vm.machine = null;
            vm.machineId = $stateParams.machineId;
            vm.attributes = [];
            vm.saveMachineItem = saveMachineItem;
            vm.back = back;
            vm.update = update;

            function back() {
                window.history.back();
            }

            function update() {
                ItemService.updateMachine(vm.machine).then(
                    function (data) {

                    }
                )
            }

            function saveMachineItem() {
                if (validate()) {
                    ItemService.updateMachine(vm.machine).then(
                        function (data) {
                            /*saveAttributes();*/
                            $rootScope.showSuccessMessage("Machine updated successfully");
                        }
                    )
                }
            }

            function saveAttributes() {
                if (vm.attributes.length > 0) {
                    ItemService.saveItemAttributes(vm.machine.id, vm.attributes).then(
                        function (data) {
                            $timeout(function () {
                                $state.go('app.proc.items.all');
                            }, 1000)
                        }
                    )
                }
            }

            function validate() {
                var valid = true;

                if (vm.machine.itemType == null) {
                    valid = false;
                    $rootScope.showErrorMessage('Machine Type cannot be empty');
                }
                else if (vm.machine.itemNumber == null) {
                    valid = false;
                    $rootScope.showErrorMessage('Machine Number cannot be empty');
                }
                else if (vm.machine.units == null) {
                    valid = false;
                    $rootScope.showErrorMessage('Machine Units cannot be empty');
                }

                return valid;
            }

            function loadMachineItem() {
                vm.loading = true;
                ItemService.getMachineItem(vm.machineId).then(
                    function (data) {
                        vm.machine = data;
                        vm.loading = false;
                        /* loadAttributeDefs();*/
                    }
                )
            }

            function loadAttributeDefs() {
                ItemTypeService.getAttributesWithHierarchy(vm.machine.itemType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.machine.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute
                            };

                            vm.attributes.push(att);
                        });

                        loadAttributes();
                    }
                )
            }

            function loadAttributes() {
                ItemService.getItemAttributes(vm.machine.id).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.attributes, function (attribute) {
                            var value = map.get(attribute.attributeDef.id);
                            if (value != null) {
                                attribute.stringValue = value.stringValue;
                                attribute.integerValue = value.integerValue;
                                attribute.doubleValue = value.doubleValue;
                                attribute.doubleDecimalValue = 0;
                                attribute.booleanValue = value.booleanValue;
                                attribute.dateValue = value.dateValue;
                            }
                        });
                    }
                )
            }

            (function () {
                loadMachineItem();
            })();
        }
    }
);
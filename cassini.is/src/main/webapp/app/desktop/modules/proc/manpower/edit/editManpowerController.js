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
        module.controller('EditManpowerController', EditManpowerController);

        function EditManpowerController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                        ItemTypeService, ItemService) {

            $rootScope.viewInfo.icon = "fa fa-users";
            $rootScope.viewInfo.title = "Edit Manpower";

            var vm = this;

            vm.manpower = null;
            vm.manpowerId = $stateParams.manpowerId;
            vm.attributes = [];
            vm.saveManpower = saveManpower;
            vm.update = update;
            vm.back = back;

            function back() {
                window.history.back();
            }

            function saveManpower() {
                if (validate()) {
                    ItemService.updateManpower(vm.manpower).then(
                        function (data) {
                            /*  saveAttributes();*/
                            $rootScope.showSuccessMessage("Manpower updated successfully");
                            $state.go('app.proc.manpower.all');
                        }
                    )
                }
            }

            function update() {
                ItemService.updateManpower(vm.manpower).then(
                    function (data) {

                    }
                )

            }

            function saveAttributes() {
                if (vm.attributes.length > 0) {
                    ItemService.saveItemAttributes(vm.manpower.id, vm.attributes).then(
                        function (data) {
                            $timeout(function () {
                                $state.go('app.proc.manpower.all');
                            }, 1000)
                        }
                    )
                }
            }

            function validate() {
                var valid = true;

                if (vm.manpower.itemType == null) {
                    valid = false;
                    $rootScope.showErrorMessage('Manpower Type cannot be empty');
                }
                else if (vm.manpower.itemNumber == null) {
                    valid = false;
                    $rootScope.showErrorMessage('Manpower Number cannot be empty');
                }
                return valid;
            }

            function loadManpower() {
                vm.loading = true;
                ItemService.getManpowerItem(vm.manpowerId).then(
                    function (data) {
                        vm.manpower = data;
                        vm.loading = false;
                        /* loadAttributeDefs();*/
                    }
                )
            }

            function loadAttributeDefs() {
                ItemTypeService.getAttributesWithHierarchy(vm.manpower.itemType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.manpower.id,
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
                ItemService.getItemAttributes(vm.manpower.id).then(
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
                loadManpower();
            })();
        }
    }
);
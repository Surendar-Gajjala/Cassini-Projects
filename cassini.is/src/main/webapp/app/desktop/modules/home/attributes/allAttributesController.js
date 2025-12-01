define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService'
    ],
    function (module) {
        module.controller('AllAttributesController', AllAttributesController);

        function AllAttributesController($scope, $rootScope, $timeout, $state, ItemService, ItemTypeService, ObjectTypeAttributeService) {

            var vm = this;
            vm.typeAttributes = [];
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.selectedAttributes = [];
            vm.allAttributes = [];
            vm.allTypeAttributes = [];
            var objectType = $scope.data.attributesMode;
            var type = null;
            if (objectType == 'MATERIAL') {
                type = 'MATERIALTYPE';
            } else if (objectType == 'MACHINE') {
                type = 'MACHINETYPE';
            } else if (objectType == 'MANPOWER') {
                type = 'MANPOWERTYPE';
            } else if (objectType == 'RECEIVE') {
                type = 'MATERIALRECEIVETYPE';
            } else if (objectType == 'ISSUE') {
                type = 'MATERIALISSUETYPE';
            }

            function loadAttributes() {
                if (type != null) {
                    ObjectTypeAttributeService.getObjectTypeAttributesByType(type).then(
                        function (data) {
                            angular.forEach(data, function (attribute) {
                                var flag = false;
                                angular.forEach(vm.selectedAttributes, function (att) {
                                    if (att.id == attribute.id) {
                                        flag = true;
                                    }
                                });
                                if (!flag) {
                                    vm.allAttributes.push(attribute);
                                }
                            });
                            if (objectType == 'RECEIVE') {
                                ItemTypeService.getReceiveTypeReferences(vm.allAttributes, 'itemType');
                            } else if (objectType == 'ISSUE') {
                                ItemTypeService.getIssueTypeReferences(vm.allAttributes, 'itemType');
                            } else if (objectType == 'MATERIAL') {
                                ItemTypeService.getMaterialTypeReferences(vm.allAttributes, 'itemType');
                            } else if (objectType == 'MACHINE') {
                                ItemTypeService.getMachineTypeReferences(vm.allAttributes, 'itemType');
                            } else if (objectType == 'MANPOWER') {
                                ItemTypeService.getManpowerTypeReferences(vm.allAttributes, 'itemType');
                            }
                        }
                    )
                }
                ItemService.getAllTypeAttributes(objectType).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var flag = false;
                            angular.forEach(vm.selectedAttributes, function (att) {
                                if (att.id == attribute.id) {
                                    flag = true;
                                }
                            });
                            if (!flag) {
                                vm.allAttributes.push(attribute);
                            }
                        })
                    }
                );

            }

            function selectAll() {
                if (vm.selectedAll) {
                    vm.selectedAttributes = [];
                    vm.selectedAll = true;
                    angular.forEach(vm.allAttributes, function (attribute) {
                        attribute.checked = vm.selectedAll;
                        vm.selectedAttributes.push(attribute);
                    });
                } else {
                    vm.selectedAll = false;
                    // vm.selectedAttributes = [];
                    angular.forEach(vm.allAttributes, function (attribute) {
                        attribute.checked = vm.selectedAll;
                        vm.selectedAttributes = [];
                    });
                }
            }

            function selectCheck(attribute) {
                var flag = true;
                if (attribute.checked == false) {
                    vm.selectedAll = false;
                    var index = vm.selectedAttributes.indexOf(attribute);
                    vm.selectedAttributes.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedAttributes, function (selectAttr) {
                        if (selectAttr.id == attribute.id) {
                            flag = false;
                            var index = vm.selectedAttributes.indexOf(attribute);
                            vm.selectedAttributes.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedAttributes.push(attribute);
                    }
                }
                if (vm.selectedAttributes.length == vm.allAttributes.length) {
                    vm.selectedAll = true;
                }
            }

            function addAttributes() {
                if (vm.selectedAttributes.length == 0) {
                    $rootScope.showWarningMessage("Please select atleast one attribute")
                } else {
                    $scope.callback(vm.selectedAttributes);
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    if ($scope.data.selectedAttributes != null) {
                        vm.selectedAttributes = $scope.data.selectedAttributes;
                    }
                    loadAttributes();
                    $rootScope.$on('app.items.attributes.select', addAttributes);
                }
            })();
        }
    }
);
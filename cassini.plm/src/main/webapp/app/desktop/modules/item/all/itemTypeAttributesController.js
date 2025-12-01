define([
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/ecoService'
    ],
    function (module) {
        module.controller('ItemTypeAttributesController', ItemTypeAttributesController);

        function ItemTypeAttributesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, ItemService, ItemTypeService, ECOService) {

            var vm = this;
            var pageable = {
                page: 0,
                size: 30,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.error = "";

            vm.filters = {
                name: null,
                itemType: null
            };

            vm.typeAttributes = [];
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.onOk = onOk;
            vm.selectedItemAttributes = [];
            vm.itemAttributes = [
                {
                    name: "Modified By",
                    objectType: "ITEM"
                },
                {
                    name: "Created Date",
                    objectType: "ITEM"
                },
                {
                    name: "Created By",
                    objectType: "ITEM"
                }
            ]

            $scope.check = false;

            var parsed = angular.element("<div></div>");
            var pleaseSelectOneItem = parsed.html($translate.instant("ATLEAST_ONE_ATTRIBUTE_VALIDATION")).html();

            function selectAll(check) {
                vm.selectedItemAttributes = [];
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.customItemAttributes, function (attribute) {
                        attribute.checked = false;
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.customItemAttributes, function (attribute) {
                        attribute.checked = true;
                        vm.selectedItemAttributes.push(attribute);
                    })
                }
            }

            function selectCheck(attribute) {
                vm.error = "";
                if (attribute.checked) {
                    vm.selectedItemAttributes.push(attribute);
                } else {
                    attribute.checked = false;
                    angular.forEach(vm.selectedItemAttributes, function (selected) {
                        if (attribute.id == selected.id) {
                            var index = vm.selectedItemAttributes.indexOf(selected);
                            vm.selectedItemAttributes.splice(index, 1);
                        }
                    })
                }
                if (vm.selectedItemAttributes.length == vm.customItemAttributes.length) {
                    vm.selectAllCheck = true;
                } else {
                    vm.selectAllCheck = false;
                }
            }

            function onOk() {
                if (vm.selectedItemAttributes.length > 0) {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedItemAttributes);
                } else {
                    $rootScope.showWarningMessage(pleaseSelectOneItem)
                }
            }

            function loadCustomItemAttributes() {
                ItemTypeService.getAllTypeAttributes("ITEM").then(
                    function (data) {
                        vm.customItemAttributes = vm.itemAttributes.concat(data);
                        angular.forEach(vm.customItemAttributes, function (typeAttribute) {
                            typeAttribute.checked = false;
                            angular.forEach(vm.selectedItemAttributes, function (attribute) {
                                if (typeAttribute.name == attribute.name && attribute.checked == true) {
                                    typeAttribute.checked = true;
                                }
                            })
                        })
                        loadCustomItemRevisionAttributes();
                        loadTypeAttributes();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadCustomItemRevisionAttributes() {
                ItemTypeService.getAllTypeAttributes("ITEMREVISION").then(
                    function (data) {
                        vm.customItemRevisionAttributes = data;
                        angular.forEach(vm.customItemRevisionAttributes, function (typeAttribute) {
                            typeAttribute.checked = false;
                            vm.customItemAttributes.push(typeAttribute);
                            angular.forEach(vm.selectedItemAttributes, function (attribute) {
                                if (typeAttribute.name == attribute.name && attribute.checked == true) {
                                    typeAttribute.checked = true;
                                }
                            })
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadTypeAttributes() {
                ItemTypeService.getAllTypeAttributes("ITEMTYPE").then(
                    function (data) {
                        vm.typeAttributes = data;
                        ItemTypeService.getItemTypeReferences(vm.typeAttributes, 'itemType');
                        angular.forEach(vm.typeAttributes, function (typeAttribute) {
                            typeAttribute.checked = false;
                            vm.customItemAttributes.push(typeAttribute);

                            angular.forEach(vm.selectedItemAttributes, function (attribute) {
                                if (typeAttribute.id == attribute.id) {
                                    typeAttribute.checked = true;
                                }
                            })
                        })
                        if (vm.selectedItemAttributes.length == vm.customItemAttributes.length) {
                            vm.selectAllCheck = true;
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    loadCustomItemAttributes();
                    if ($scope.data.selectedAttributes != null) {
                        vm.selectedItemAttributes = $scope.data.selectedAttributes;
                    }
                    $rootScope.$on("add.select.attributes", onOk);
                //}
            })();
        }
    }
)
;
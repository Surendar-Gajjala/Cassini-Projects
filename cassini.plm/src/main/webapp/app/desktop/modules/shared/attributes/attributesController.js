define([
        'app/desktop/modules/main/main.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/itemBomService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService'
    ],
    function (module) {
        module.controller('AttributesController', AttributesController);

        function AttributesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, ItemService, ItemTypeService,
                                      ECOService, MfrPartsService, MfrService, $translate, ItemBomService, CustomObjectTypeService, ObjectTypeAttributeService) {

            var vm = this;
            var pageable = {
                page: 0,
                size: 30,
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
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.selectAllCheck = false;
            $scope.typeAttributes = angular.copy(pagedResults);
            $scope.selectCheck = selectCheck;
            $scope.selectAll = selectAll;
            $scope.onOk = onOk;
            $scope.selectedAttributes = [];
            $scope.selectedAttributes = ($scope.data != null && $scope.data.selectedAttributes != null && $scope.data.selectedAttributes != undefined) ? $scope.data.selectedAttributes : [];
            $scope.selectionType = $scope.data.selectionType;
            $scope.check = false;
            vm.type = $scope.data.type;
            vm.objectType = $scope.data.objectType;
            vm.bomItem = $scope.data.bomItem;

            var parsed = angular.element("<div></div>");
            var pleaseSelectOneItem = parsed.html($translate.instant("ATLEAST_ONE_ATTRIBUTE_VALIDATION")).html();

            function selectAll(check) {
                $scope.selectedAttributes = [];
                if (check) {
                    $scope.check = false;
                    angular.forEach($scope.customProperties, function (attribute) {
                        attribute.checked = false;
                    })
                } else {
                    $scope.check = true;
                    angular.forEach($scope.customProperties, function (attribute) {
                        attribute.checked = true;
                        $scope.selectedAttributes.push(attribute);
                    })
                }

            }

            function selectCheck(attribute) {
                if (attribute.checked) {
                    $scope.selectedAttributes.push(attribute);
                } else {
                    attribute.checked = false;
                    angular.forEach($scope.selectedAttributes, function (selected) {
                        if (attribute.id == selected.id) {
                            var index = $scope.selectedAttributes.indexOf(selected);
                            $scope.selectedAttributes.splice(index, 1);
                        }
                    })
                }
                if ($scope.selectedAttributes.length == $scope.customProperties.length) {
                    vm.selectAllCheck = true;
                } else {
                    vm.selectAllCheck = false;
                }
            }

            function onOk() {
                if ($scope.selectedAttributes.length > 0) {
                    $rootScope.hideSidePanel();
                    $scope.callback($scope.selectedAttributes);
                } else {
                    $rootScope.showWarningMessage(pleaseSelectOneItem)
                }
            }

            function loadTypeAttributes() {
                $scope.typeAttributes = [];
                if (vm.type != null && vm.type != undefined && vm.type != "") {
                    ItemTypeService.getAllTypeAttributes(vm.type).then(
                        function (data) {
                            $scope.typeAttributes = data;
                            angular.forEach($scope.typeAttributes, function (typeAttribute) {
                                typeAttribute.checked = false;
                                $scope.customProperties.push(typeAttribute);
                                angular.forEach($scope.selectedAttributes, function (attribute) {
                                    if (typeAttribute.id == attribute.id) {
                                        typeAttribute.checked = true;
                                    }
                                })
                            })
                            if ($scope.selectedAttributes.length == $scope.customProperties.length) {
                                vm.selectAllCheck = true;
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadProperties() {
                ObjectTypeAttributeService.getObjectTypeAttributesByType(vm.objectType).then(
                    function (data) {
                        $scope.customProperties = [];
                        angular.forEach(data, function (typeAttribute) {
                            if (typeAttribute.plugin == false) {
                                $scope.customProperties.push(typeAttribute);
                                typeAttribute.checked = false;
                                angular.forEach($scope.selectedAttributes, function (attribute) {
                                    if (typeAttribute.name == attribute.name && attribute.checked) {
                                        typeAttribute.checked = true;
                                    }
                                })
                            }
                        });
                        if ($scope.selectedAttributes.length == $scope.customProperties.length) {
                            vm.selectAllCheck = true;
                        }
                        if (vm.objectType == 'CUSTOMOBJECT') {
                            loadAttributeDefs();
                        } else {
                            loadTypeAttributes();
                        }
                        if (vm.bomItem == true) {
                            loadItemTypeAttributes();
                        }
                    }
                )
            }

            function loadAttributeDefs() {
                CustomObjectTypeService.getCustomObjectAttributesWithHierarchy(vm.type).then(
                    function (data) {
                        vm.customAttributes = [];
                        angular.forEach(data, function (typeAttribute) {
                            if (typeAttribute.showInTable == false && typeAttribute.plugin == false) {
                                typeAttribute.checked = false;
                                $scope.customProperties.push(typeAttribute);
                                angular.forEach($scope.selectedAttributes, function (attribute) {
                                    if (typeAttribute.name == attribute.name && attribute.checked == true) {
                                        typeAttribute.checked = true;
                                    }
                                })
                            }
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadItemTypeAttributes() {
                ItemTypeService.getAllTypeAttributes("ITEMTYPE").then(
                    function (data) {
                        vm.typeAttributes = data;
                        ItemTypeService.getItemTypeReferences(vm.typeAttributes, 'itemType');
                        angular.forEach(vm.typeAttributes, function (typeAttribute) {
                            typeAttribute.checked = false;
                            $scope.customProperties.push(typeAttribute);
                            angular.forEach($scope.selectedAttributes, function (attribute) {
                                if (typeAttribute.id == attribute.id && attribute.checked) {
                                    typeAttribute.checked = true;
                                }
                            })
                        });
                        if ($scope.selectedAttributes.length == $scope.customProperties.length) {
                            vm.selectAllCheck = true;
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadBomRollUpAttributes() {
                ItemTypeService.getBomRollupAttributes().then(
                    function (data) {
                        $scope.customProperties = data;
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                if ($scope.selectionType == "ROLLUP") {
                    loadBomRollUpAttributes();
                } else {
                    loadProperties();
                }
                $rootScope.$on("add.select.attributes", onOk);
                //}
            })();
        }
    }
)
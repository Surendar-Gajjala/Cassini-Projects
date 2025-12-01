define([
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/qualityTypeService'
    ],
    function (module) {
        module.controller('QualityTypeAttributeSelectionController', QualityTypeAttributeSelectionController);

        function QualityTypeAttributeSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, QualityTypeService) {

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
            $scope.parentObjectType = $scope.data.selectedParentObjectType;
            $scope.objectType = $scope.data.selectedObjectType;
            $scope.object = $scope.data.selectedObject;
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
                    name: "Created Date",
                    objectType: $scope.data.selectedObject
                },
                {
                    name: "Created By",
                    objectType: $scope.data.selectedObject
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
                QualityTypeService.getAllQualityTypeAttributes($scope.data.selectedParentObjectType, $scope.data.selectedObjectType).then(
                    function (data) {
                        vm.customItemAttributes = vm.itemAttributes.concat(data.objectTypeAttributes);
                        if ($scope.data.selectedParentObjectType == "QUALITY") {
                            vm.customItemAttributes = vm.customItemAttributes.concat(data.qualityTypeAttributes);
                        } else if ($scope.data.selectedParentObjectType == "CHANGE") {
                            vm.customItemAttributes = vm.customItemAttributes.concat(data.changeTypeAttributes);
                        } else if ($scope.data.selectedParentObjectType == "MESOBJECT") {
                            vm.customItemAttributes = vm.customItemAttributes.concat(data.mesObjectTypeAttributes);
                        }

                        angular.forEach(vm.customItemAttributes, function (typeAttribute) {
                            typeAttribute.checked = false;
                            angular.forEach(vm.selectedItemAttributes, function (attribute) {
                                if (typeAttribute.name == attribute.name && attribute.checked == true) {
                                    typeAttribute.checked = true;
                                }
                            })
                        })
                        if (vm.selectedItemAttributes.length == vm.customItemAttributes.length) $scope.check = true;
                    }, function (error) {
                          $rootScope.showErrorMessage(error.message);
                          $rootScope.hideBusyIndicator();
                     }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadCustomItemAttributes();
                if ($scope.data.selectedAttributes != null) {
                    vm.selectedItemAttributes = angular.copy($scope.data.selectedAttributes);
                }
                $rootScope.$on("add.quality.attributes.select", onOk);
                //}
            })();
        }
    }
)
;
define([
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/requirementsTypeService'
    ],
    function (module) {
        module.controller('RequirementTypeAttributesController', RequirementTypeAttributesController);

        function RequirementTypeAttributesController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $cookies, SpecificationsService, RequirementsTypeService) {

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
            vm.selectedAttributes = [];
            vm.itemAttributes = [
                {
                    name: "Modified Date",
                    objectType: "REQUIREMENT"
                },
                {
                    name: "Modified By",
                    objectType: "REQUIREMENT"
                },
                {

                    name: "Created Date",
                    objectType: "REQUIREMENT"
                },
                {
                    name: "Created By",
                    objectType: "REQUIREMENT"
                }
            ]

            $scope.check = false;
            var parsed = angular.element("<div></div>");
            var pleaseSelectOneItem = parsed.html($translate.instant("ATLEAST_ONE_ATTRIBUTE_VALIDATION")).html();

            function selectAll(check) {
                vm.selectedAttributes = [];
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.customItemAttributes, function (attribute) {
                        attribute.checked = false;
                    })
                } else {
                    $scope.check = true;
                    vm.selectedAttributes = [];
                    vm.error = "";
                    angular.forEach(vm.customItemAttributes, function (attribute) {
                        attribute.checked = true;
                        vm.selectedAttributes.push(attribute);
                    })
                }

            }

            function selectCheck(attribute) {
                vm.error = "";
                if (attribute.checked) {
                    vm.selectedAttributes.push(attribute);
                } else {
                    attribute.checked = false;
                    angular.forEach(vm.selectedAttributes, function (selected) {
                        if (attribute.id == selected.id) {
                            var index = vm.selectedAttributes.indexOf(selected);
                            vm.selectedAttributes.splice(index, 1);
                        }
                    })
                }
                if (vm.selectedAttributes.length == vm.customItemAttributes.length) {
                    vm.selectAllCheck = true;
                } else {
                    vm.selectAllCheck = false;
                }
            }

            function onOk() {
                if (vm.selectedAttributes.length > 0) {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedAttributes);
                } else {
                    $rootScope.showWarningMessage(pleaseSelectOneItem);
                }
            }

            function loadCustomItemAttributes() {
                RequirementsTypeService.getAllTypeAttributes("REQUIREMENT").then(
                    function (data) {
                        vm.customItemAttributes = vm.itemAttributes.concat(data);
                        angular.forEach(vm.customItemAttributes, function (typeAttribute) {
                            typeAttribute.checked = false;
                            angular.forEach(vm.selectedAttributes, function (attribute) {
                                if (typeAttribute.id == attribute.id && typeAttribute.name == attribute.name) {
                                    typeAttribute.checked = true;
                                }
                            })
                        })
                        loadTypeAttributes();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadTypeAttributes() {
                RequirementsTypeService.getAllTypeAttributes("REQUIREMENTTYPE").then(
                    function (data) {
                        vm.typeAttributes = data;
                        RequirementsTypeService.getItemTypeReferences(vm.typeAttributes, 'rmObjectType');
                        angular.forEach(vm.typeAttributes, function (typeAttribute) {
                            typeAttribute.checked = false;
                            vm.customItemAttributes.push(typeAttribute);
                            angular.forEach(vm.selectedAttributes, function (attribute) {
                                if (typeAttribute.id == attribute.id) {
                                    typeAttribute.checked = true;
                                }
                            })
                        })
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
                        vm.selectedAttributes = $scope.data.selectedAttributes;
                    }
                    $rootScope.$on("add.select.attributes", onOk);
                //}
            })();
        }
    }
)
;
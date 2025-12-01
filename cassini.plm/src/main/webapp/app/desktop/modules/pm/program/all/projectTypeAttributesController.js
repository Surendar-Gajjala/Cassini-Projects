define([
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService'
    ],
    function (module) {
        module.controller('ProjectTypeAttributesController', ProjectTypeAttributesController);

        function ProjectTypeAttributesController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, ProjectService) {

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
                    objectType: "PROJECT"
                },
                {
                    name: "Modified By",
                    objectType: "PROJECT"
                },
                {
                    name: "Created Date",
                    objectType: "PROJECT"
                },
                {
                    name: "Created By",
                    objectType: "PROJECT"
                }
            ]

            $scope.check = false;

            var parsed = angular.element("<div></div>");
            var pleaseSelectAttrItem = parsed.html($translate.instant("ATLEAST_ONE_ATTRIBUTE_VALIDATION")).html();

            function selectAll(check) {
                vm.selectedAttributes = [];
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
                    $rootScope.showWarningMessage(pleaseSelectAttrItem)
                }
            }

            function loadCustomItemAttributes() {
                if (vm.objectType != null) {
                    ProjectService.getAllTypeAttributes("PROJECT").then(
                        function (data) {
                            vm.customItemAttributes = vm.itemAttributes.concat(data);
                            angular.forEach(vm.customItemAttributes, function (typeAttribute) {
                                typeAttribute.checked = false;
                                angular.forEach(vm.selectedAttributes, function (attribute) {
                                    if (typeAttribute.name == attribute.name && attribute.checked == true) {
                                        typeAttribute.checked = true;
                                    }
                                })
                            })
                            if (vm.selectedAttributes.length == vm.customItemAttributes.length) $scope.check = true;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadCustomTaskAttributes() {
                ProjectService.getAllTypeAttributes("PROJECT").then(
                    function (data) {
                        vm.customItemAttributes = data;
                        angular.forEach(vm.customItemAttributes, function (typeAttribute) {
                            typeAttribute.checked = false;
                            angular.forEach(vm.selectedAttributes, function (attribute) {
                                if (typeAttribute.name == attribute.name && attribute.checked == true) {
                                    typeAttribute.checked = true;
                                }
                            })
                        })
                        if (vm.selectedAttributes.length == vm.customItemAttributes.length) $scope.check = true;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                vm.objectType = $scope.data.objectType;

                if ($scope.data.selectedAttributes != null) {
                    vm.selectedAttributes = $scope.data.selectedAttributes;
                }
                if (vm.objectType == "PROJECT") {
                    loadCustomItemAttributes();
                } else if (vm.objectType == "PROJECTTASK") {
                    loadCustomTaskAttributes();
                }
                $rootScope.$on("add.select.attributes", onOk);
                //}
            })();
        }
    }
)
;
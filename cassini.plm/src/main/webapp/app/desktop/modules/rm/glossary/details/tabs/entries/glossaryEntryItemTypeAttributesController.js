define([
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/glossaryService',
    ],
    function (module) {
        module.controller('GlossarEntryTypeAttributesController', GlossarEntryTypeAttributesController);

        function GlossarEntryTypeAttributesController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, GlossaryService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var pleaseSelectOneItem = parsed.html($translate.instant("ATLEAST_ONE_ITEM_VALIDATION")).html();
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
                    objectType: "TERMINOLOGYENTRY"
                },
                {
                    name: "Modified By",
                    objectType: "TERMINOLOGYENTRY"
                },
                {
                    name: "Created Date",
                    objectType: "TERMINOLOGYENTRY"
                },
                {
                    name: "Created By",
                    objectType: "TERMINOLOGYENTRY"
                }
            ]

            $scope.check = false;

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
                    $rootScope.showWarningMessage(pleaseSelectOneItem);
                }

            }

            function loadCustomItemAttributes() {
                GlossaryService.getAllTypeAttributes("TERMINOLOGYENTRY").then(
                    function (data) {
                        vm.customItemAttributes = vm.itemAttributes.concat(data);
                        angular.forEach(vm.customItemAttributes, function (typeAttribute) {
                            typeAttribute.checked = false;
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
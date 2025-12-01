define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/proc/classification/directive/classificationTreeController',
        'app/desktop/modules/proc/classification/directive/classificationTreeController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('EditMaterialController', EditMaterialController);

        function EditMaterialController($scope, $rootScope, $timeout, $state, $stateParams, AttributeAttachmentService, $cookies,
                                        ItemTypeService, ItemService) {

            $rootScope.viewInfo.icon = "fa fa-th";
            $rootScope.viewInfo.title = "Edit Material";

            var vm = this;

            vm.materialItem = null;
            vm.materialItemId = $stateParams.materialId;
            vm.saveMaterialItem = saveMaterialItem;
            vm.back = back;
            vm.update = update;
            vm.selectedAttributes = [];
            vm.materialAttributes = [];
            vm.materialTypeAttributes = [];

            function loadMaterialAttributes() {
                ItemService.getTypeAttributesRequiredFalse("MATERIAL").then(
                    function (data) {
                        vm.materialAttributes = data;
                        angular.forEach(vm.selectedAttributes, function (attribute) {
                            angular.forEach(vm.materialAttributes, function (materialAttribute) {
                                if (materialAttribute.id == attribute.id) {
                                    var index = vm.materialAttributes.indexOf(materialAttribute);
                                    vm.materialAttributes.splice(index, 1);
                                }
                            })
                        });
                        loadMaterialTypeAttributes();
                    }
                )
            }

            function loadMaterialTypeAttributes() {
                ItemTypeService.getMaterialTypeAttributesRequiredFalse("MATERIAL").then(
                    function (data) {
                        vm.materialTypeAttributes = data;
                        ItemTypeService.getMaterialTypeReferences(vm.materialTypeAttributes, 'itemType');
                        angular.forEach(vm.selectedAttributes, function (attribute) {
                            angular.forEach(vm.materialTypeAttributes, function (materialAttribute) {
                                if (materialAttribute.id == attribute.id) {
                                    var index = vm.materialTypeAttributes.indexOf(materialAttribute);
                                    vm.materialTypeAttributes.splice(index, 1);
                                }
                            })
                        });
                    }
                )
            }

            function back() {
                window.history.back();
            }

            function update() {
                ItemService.updateMaterial(vm.materialItem).then(
                    function (data) {

                    }
                )
            }

            function saveMaterialItem() {
                if (validate()) {
                    ItemService.updateMaterial(vm.materialItem).then(
                        function (data) {
                            // saveAttributes();
                            $rootScope.showSuccessMessage("Material updated successfully");
                        }
                    )
                }
            }

            function saveAttributes() {
                if (vm.attributes.length > 0) {
                    ItemService.saveItemAttributes(vm.materialItem.id, vm.attributes).then(
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

                if (vm.materialItem.itemType == null) {
                    valid = false;
                    $rootScope.showErrorMessage('Item Type cannot be empty');
                }
                else if (vm.materialItem.itemNumber == null) {
                    valid = false;
                    $rootScope.showErrorMessage('Item Number cannot be empty');
                }
                else if (vm.materialItem.units == null) {
                    valid = false;
                    $rootScope.showErrorMessage('Material Units cannot be empty');
                }

                return valid;
            }

            function loadMaterialItem() {
                vm.loading = true;
                vm.itemIds = [];
                ItemService.getMaterialItem(vm.materialItemId).then(
                    function (data) {
                        vm.materialItem = data;
                        vm.loading = false;
                        loadMaterialAttributes();
                    }
                )
            }

            (function () {
                loadMaterialItem();
            })();
        }
    }
);
define(
    [
        'app/desktop/modules/item/item.module',
        'app/desktop/modules/directives/customObjectTypeDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService'
    ],
    function (module) {

        module.controller('EditAttributeGroupController', EditAttributeGroupController);

        function EditAttributeGroupController($scope, $q, $rootScope, $translate, $timeout, ObjectTypeAttributeService, ClassificationService, CustomObjectTypeService) {

            var vm = this;
            vm.attribute = $scope.data.attribute;
            vm.selectedType = $scope.data.selectedType;
            vm.newGroupName = null;
            var attributeGroupUpdatedMessage = $translate.instant("ATTRIBUTE_GROUP_UPDATED");

            function editAttributeGroup() {
                if (vm.newGroupName != null && vm.newGroupName != "") {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    ObjectTypeAttributeService.getAttributeGroupsByName(vm.newGroupName).then(
                        function (data) {
                            if (data.length > 0) {
                                $rootScope.showErrorMessage(vm.newGroupName + " : Group name already exist");
                                $rootScope.hideBusyIndicator();
                            } else {
                                if (vm.selectedType != null && vm.selectedType.objectType != "CUSTOMOBJECTTYPE") {
                                    vm.attribute.attributeGroup = vm.newGroupName;
                                    ClassificationService.updateAttribute(vm.selectedType.objectType, vm.selectedType.id, vm.attribute).then(
                                        function (data) {
                                            $scope.callback(data);
                                            $rootScope.showSuccessMessage(attributeGroupUpdatedMessage);
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                } else {
                                    vm.attribute.attributeGroup = vm.newGroupName;
                                    CustomObjectTypeService.updateAttribute(vm.selectedType.id, vm.attribute).then(
                                        function (data) {
                                            $scope.callback(data);
                                            $rootScope.showSuccessMessage(attributeGroupUpdatedMessage);
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                }
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    $rootScope.showWarningMessage("Please enter new attribute group");
                }
            }

            (function () {
                $rootScope.$on('app.attributes.group.update', editAttributeGroup);
            })();
        }
    }
)
;
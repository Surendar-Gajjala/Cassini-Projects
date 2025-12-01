define(['app/desktop/modules/classification/classification.module',
        'app/shared/services/itemTypeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('ItemTypeDetailsController', ItemTypeDetailsController);

        function ItemTypeDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                           ItemTypeService, AutonumberService, CommonService, DialogService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.show = true;
            vm.valid = true;
            vm.error = "";
            vm.itemType = null;
            vm.autoNumbers = [];
            vm.revSequences = [];
            vm.lifecycleStates = [];

            vm.addAttribute = addAttribute;
            vm.deleteAttribute = deleteAttribute;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.editAttribute = editAttribute;
            vm.onSave = onSave;

            var newAttribute = {
                id: null,
                name: null,
                description: null,
                dataType: 'STRING',
                required: false,
                objectType: 'ITEMTYPE',
                editMode: true,
                showValues: false
            };

            function itemTypeSelected(event, args) {
                vm.itemType = args.itemType;
                if (vm.itemType != null && vm.itemType != undefined) {
                    vm.itemType.attributes = [];
                    loadAttributes();
                    loadAutoNumbers();
                    loadLovs();
                }
            }

            function loadAttributes() {
                ItemTypeService.getAttributes(vm.itemType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            attribute.showValues = true;
                            attribute.editMode = false;
                        });
                        vm.itemType.attributes = data;
                    }
                );
            }

            function loadAutoNumbers() {
                AutonumberService.getAutonumbers().then(
                    function (data) {
                        vm.autoNumbers = data;
                        angular.forEach(data, function (item) {
                            if (vm.itemType.itemNumberSource == null &&
                                item.name == "Default Part Number Source") {
                                vm.itemType.itemNumberSource = item;
                            }
                        });
                    }
                )
            }

            function loadLovs() {
                CommonService.getLovByType('Revision Sequence').then(
                    function (data) {
                        vm.revSequences = data;
                        angular.forEach(data, function (item) {
                            if (item.name == 'Default Revision Sequence') {
                                vm.itemType.revisionSequence = item;
                            }
                        });
                    }
                );

                CommonService.getLovByType('Lifecycle States').then(
                    function (data) {
                        vm.lifecycleStates = data;
                        angular.forEach(data, function (item) {
                            if (item.name == 'Default Lifecycle States') {
                                vm.itemType.lifeCycleStates = item;
                            }
                        });
                    }
                );
            }

            /*function onSave() {
             if (vm.itemType.parentType == null && vm.itemType != null && validate()) {
             var options = {
             title: 'Save Classification',
             message: '"Type" cannot be modified once saved.' + '  ' +
             'Are you sure you want to save this Classification ?',
             okButtonClass: 'btn-danger'
             };
             DialogService.confirm(options, function (yes) {
             if (yes == true) {

             if (vm.itemType != null && validate()) {
             ItemTypeService.updateItemType(vm.itemType).then(
             function (data) {
             vm.itemType.flag = true;
             $rootScope.showSuccessMessage("Item type saved successfully");
             }
             );
             }
             }
             });
             }

             else if (vm.itemType != null && validate() && vm.itemType.parentType != null) {
             ItemTypeService.updateItemType(vm.itemType).then(
             function (data) {
             vm.itemType.flag = true;
             $rootScope.showSuccessMessage("Sub item type saved successfully");
             }
             );
             }
             }*/

            function onSave() {
                if (vm.itemType != null && validate()) {
                    if (vm.itemType.id == null || vm.itemType.id == undefined) {
                        ItemTypeService.createItemType(vm.itemType).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Item type saved successfully");
                            }
                        )
                    } else {
                        ItemTypeService.updateItemType(vm.itemType).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Item type updated successfully");
                            }
                        )
                    }
                }
            }

            function validate() {
                vm.valid = true;

                $rootScope.closeNotification();

                if (vm.itemType.name == null || vm.itemType.name == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Name cannot be empty");
                }
                if (vm.itemType.description == null || vm.itemType.description == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Description cannot be empty");
                }
                return vm.valid;
            }

            function addAttribute() {
                var att = angular.copy(newAttribute);
                att.itemType = vm.itemType.id;
                vm.itemType.attributes.unshift(att);
                $rootScope.closeNotification();
            }

            function deleteAttribute(attribute) {
                var options = {
                    title: 'Delete Attribute',
                    message: 'Are you sure you want to delete this attribute?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        if (attribute.id != null && attribute.id != undefined) {
                            ItemTypeService.deleteAttribute(vm.itemType.id, attribute.id).then(
                                function (data) {
                                    vm.itemType.attributes.splice(vm.itemType.attributes.indexOf(attribute), 1);
                                    $rootScope.showErrorMessage("Attribute deleted successfully");
                                }
                            )
                        }
                    }
                });
            }

            function editAttribute(attribute) {
                attribute.showValues = false;
                attribute.newName = attribute.name;
                attribute.newDescription = attribute.description;
                attribute.newDataType = attribute.dataType;

                $timeout(function () {
                    attribute.editMode = true;
                }, 500);
            }

            function validateAttribute(attribute) {
                var valid = true;
                if (attribute.newName == null || attribute.newName == "" || attribute.newName == undefined) {
                    vm.error = "Please enter name";
                    valid = false;
                } else if (attribute.newDescription == null || attribute.newDescription == "" || attribute.newDescription == undefined) {
                    vm.error = "Please enter description ";
                    valid = false;
                } else if (attribute.newDataType == null || attribute.newDataType == "" || attribute.newDataType == undefined) {
                    vm.error = "Please select data type";
                    valid = false;
                }

                return valid;
            }

            function applyChanges(attribute) {
                var promise = null;

                if (attribute.id == null || attribute.id == undefined) {
                    if (validateAttribute(attribute)) {
                        attribute.name = attribute.newName;
                        attribute.description = attribute.newDescription;
                        attribute.dataType = attribute.newDataType;
                        promise = ItemTypeService.createAttribute(vm.itemType.id, attribute);
                        $rootScope.showSuccessMessage("Item attribute(s) created successfully.")
                    }

                }
                else if(validateAttribute(attribute)) {

                    attribute.name = attribute.newName;
                    attribute.description = attribute.newDescription;
                    attribute.dataType = attribute.newDataType;
                    promise = ItemTypeService.updateAttribute(vm.itemType.id, attribute);
                    $rootScope.showSuccessMessage("Item attribute(s) updated successfully.")
                }


                promise.then(
                    function (data) {
                        vm.error = "";
                        attribute.id = data.id;
                        attribute.editMode = false;
                        $timeout(function () {
                            attribute.showValues = true;
                        }, 500);
                    }
                )
            }

            function cancelChanges(attribute) {
                attribute.newName = attribute.name;
                attribute.newDescription = attribute.description;
                attribute.newDataType = attribute.dataType;
                if (attribute.id == null || attribute.id == undefined) {
                    vm.itemType.attributes.splice(vm.itemType.attributes.indexOf(attribute), 1);
                }
                else {
                    attribute.editMode = false;
                    $timeout(function () {
                        attribute.showValues = true;
                    }, 500);
                }

                vm.error = "";
            }


            (function () {
                $scope.$on('app.itemType.selected', itemTypeSelected);
            })();
        }
    }
)
;
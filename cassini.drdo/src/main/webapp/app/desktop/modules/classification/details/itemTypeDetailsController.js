define(
    ['app/desktop/modules/classification/classification.module',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'
    ],
    function (module) {
        module.controller('ItemTypeDetailsController', ItemTypeDetailsController);

        function ItemTypeDetailsController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, $translate,
                                           ItemTypeService, AutonumberService, ItemService,
                                           CommonService, DialogService, LovService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.valid = true;
            vm.error = "";
            vm.itemType = null;
            vm.autoNumbers = [];
            vm.revSequences = [];
            vm.lifecycleStates = [];
            vm.lifecycles = [];
            vm.lovs = [];

            vm.addAttribute = addAttribute;
            vm.deleteAttribute = deleteAttribute;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.editAttribute = editAttribute;
            vm.onSave = onSave;
            vm.error = "";
            var nodeId = null;

            vm.dataTypes = [
                'TEXT',
                'INTEGER',
                'DOUBLE',
                'DATE',
                'BOOLEAN',
                'LIST',
                'TIME',
                'TIMESTAMP',
                'CURRENCY',
                'IMAGE',
                'ATTACHMENT'
            ];

            var newAttribute = {
                id: null,
                name: null,
                description: null,
                dataType: 'TEXT',
                revisionSpecific: false,
                changeControlled: false,
                required: false,
                objectType: 'ITEMTYPE',
                editMode: true,
                showValues: false,
                refType: null,
                lov: null
            };

            vm.editableType = true;

            function itemTypeSelected(event, args) {
                vm.editableType = true;
                vm.itemType = args.typeObject;
                nodeId = args.nodeId;

                if (vm.itemType != null && vm.itemType != undefined) {
                    if (vm.itemType.id == undefined || vm.itemType.id == null) {
                        vm.attributesShow = false;
                    } else {
                        ItemTypeService.getItemType(vm.itemType.id).then(
                            function (data) {
                                vm.itemType = data;
                                vm.itemType.attributes = [];
                                loadAttributes();
                                loadAutoNumbers();
                                loadLovs();
                                loadItemTypeItems();
                                loadSpecs();
                            }
                        );
                        vm.attributesShow = true;
                    }
                }
                $scope.$apply();
            }

            vm.itemTypeItemExist = false;

            function loadItemTypeItems() {
                if (vm.itemType.id != null) {
                    ItemTypeService.getItemTypeItems(vm.itemType.id).then(
                        function (data) {
                            vm.itemTypeItems = data;
                            if (vm.itemTypeItems.length > 0) {
                                vm.itemTypeItemExist = true;
                            } else {
                                vm.itemTypeItemExist = false;
                            }
                        }
                    )
                }
            }

            function onSave() {
                if (vm.itemType != null && validate()) {
                    ItemTypeService.updateItemType(vm.itemType).then(
                        function (data) {
                            vm.itemType.typeCode = data.typeCode;
                            $rootScope.showSuccessMessage(data.name + " : Item Type updated successfully");
                            $rootScope.$broadcast("app.classification.update", {
                                nodeId: nodeId,
                                nodeName: vm.itemType.name,
                                typeObject: data
                            })
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function validate() {
                vm.valid = true;

                $rootScope.closeNotification();

                if (vm.itemType.name == null || vm.itemType.name == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Please enter Name");
                } else if (vm.itemType.hasSpec && vm.itemTypeSpecs.length == 0) {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Please enter at least one Spectification");
                } else if (vm.itemType.typeCode != "" && vm.itemType.typeCode != null && vm.itemType.typeCode != undefined && !validCodeLength()) {
                    vm.valid = false;
                }

                return vm.valid;
            }

            function validCodeLength() {
                var valid = true;
                if (vm.itemType.typeCode.length != 2) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter 2 digits / characters code");
                }

                return valid;
            }

            function loadAttributes() {
                ItemTypeService.getAttributesByTypeId(vm.itemType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            attribute.showValues = true;
                            attribute.editMode = false;
                        });
                        vm.itemType.attributes = data;
                    }
                );
            }

            function addAttribute() {
                var att = angular.copy(newAttribute);
                att.itemType = vm.itemType.id;
                if (vm.itemType.attributes != undefined) {
                    vm.itemType.attributes.unshift(att);
                } else {
                    vm.itemType.attributes = [];
                    vm.itemType.attributes.unshift(att);
                }
            }


            function deleteAttribute(attribute) {
                ItemTypeService.getAttributeValues(attribute.id).then(
                    function (data) {
                        if (data.length == 0) {
                            var options = {
                                title: "Delete Attribute",
                                message: "Please confirm to delete Attribute",
                                okButtonClass: 'btn-danger'
                            };
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    if (attribute.id != null && attribute.id != undefined) {
                                        ItemTypeService.deleteAttribute(vm.itemType.id, attribute.id).then(
                                            function (data) {
                                                vm.itemType.attributes.splice(vm.itemType.attributes.indexOf(attribute), 1);
                                                $rootScope.showSuccessMessage("Attribute deleted successfully");
                                            }
                                        )
                                    }
                                }
                            });
                        } else {
                            $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                        }
                    }
                );

            }

            function editAttribute(attribute) {
                ItemTypeService.getAttributeValues(attribute.id).then(
                    function (data) {
                        if (data.length == 0) {
                            attribute.showValues = false;
                            attribute.newName = attribute.name;
                            attribute.newDescription = attribute.description;
                            attribute.newDataType = attribute.dataType;
                            attribute.newRefType = attribute.refType;
                            attribute.newLov = attribute.lov;
                            $timeout(function () {
                                attribute.editMode = true;
                            }, 500);
                        } else {
                            $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                        }
                    }
                )
            }

            function validateAttribute(attribute) {
                var valid = true;
                if (attribute.newName == null || attribute.newName == "" || attribute.newName == undefined) {
                    $rootScope.showWarningMessage("Please enter Name");
                    valid = false;
                } else if (attribute.newDataType == null || attribute.newDataType == "" || attribute.newDataType == undefined) {
                    $rootScope.showWarningMessage("Please select Date Type");
                    valid = false;
                } else if (attribute.newDataType == 'LIST' &&
                    (attribute.newLov == null || attribute.newLov == "" || attribute.newLov == undefined)) {
                    $rootScope.showWarningMessage("Please select List Type");
                    valid = false;

                } else {
                    angular.forEach(vm.itemType.attributes, function (attr) {
                        if (attr.name == attribute.newName && attr.id != attribute.id) {
                            $rootScope.showWarningMessage(attr.name + ": Attribute Name already exist, please enter another Name");
                            valid = false;
                        }
                    })
                }

                return valid;
            }

            vm.hide = false;
            function applyChanges(attribute) {
                if (validateAttribute(attribute)) {
                    vm.hide = false;
                    var promise = null;
                    attribute.name = attribute.newName;
                    attribute.description = attribute.newDescription;
                    attribute.dataType = attribute.newDataType;
                    attribute.refType = attribute.newRefType;
                    attribute.lov = attribute.newLov;

                    if (attribute.id == null || attribute.id == undefined) {
                        promise = ItemTypeService.createAttribute(vm.itemType.id, attribute);
                        if (validateAttribute(attribute)) {

                        }

                    }
                    else {
                        promise = ItemTypeService.updateAttribute(vm.itemType.id, attribute);
                    }

                    if (promise != null) {
                        promise.then(
                            function (data) {
                                vm.error = "";
                                attribute.id = data.id;
                                attribute.editMode = false;
                                $timeout(function () {
                                    attribute.showValues = true;
                                }, 500);
                                $rootScope.showSuccessMessage(data.name + " : Attribute saved successfully.");
                            }
                        )
                    }
                }
            }

            function cancelChanges(attribute) {
                vm.hide = false;
                if (attribute.id == null || attribute.id == undefined) {
                    vm.itemType.attributes.splice(vm.itemType.attributes.indexOf(attribute), 1);
                    vm.error = null;
                }
                else {
                    attribute.newName = attribute.name;
                    attribute.newDescription = attribute.description;
                    attribute.newDataType = attribute.dataType;
                    attribute.editMode = false;
                    vm.error = null;
                    $timeout(function () {
                        attribute.showValues = true;
                    }, 500);
                }
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

                CommonService.getLovByType('Units of Measure').then(
                    function (data) {
                        vm.unitsOfMeasures = data[0];
                    }
                );
            }

            function loadAllLovs() {
                LovService.getAllLovs().then(
                    function (data) {
                        vm.lovs = data;
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.itemTypeSpecs = [];
            function loadSpecs() {
                if (vm.itemType.id != null && vm.itemType.id != undefined) {
                    ItemTypeService.getItemTypeSpecs(vm.itemType.id).then(
                        function (data) {
                            vm.itemTypeSpecs = data;
                        }
                    )
                }
            }

            $scope.someFocusVariable = true;
            vm.addLovValue = addLovValue;

            vm.emptySpec = {
                id: null,
                itemType: null,
                specName: null,
                newSpecName: null,
                newMode: false,
                editMode: false
            };

            function addLovValue(index) {
                vm.valueIndex = index;
                var newSpec = angular.copy(vm.emptySpec);
                newSpec.editMode = true;
                newSpec.newMode = true;
                vm.itemTypeSpecs.push(newSpec);

                $("#lov-body" + index).stop().animate({scrollTop: $("#lov-body")[0].scrollHeight}, 1000);


            }

            module.directive('focus', function ($timeout, $parse) {
                return {
                    restrict: 'A',
                    link: function (scope, element, attrs) {
                        scope.$watch(attrs.focus, function (newValue, oldValue) {
                            if (newValue) {
                                element[0].focus();
                            }
                        });
                        element.bind("blur", function (e) {
                            $timeout(function () {
                                scope.$apply(attrs.focus + "=false");
                            }, 0);
                        });
                        element.bind("focus", function (e) {
                            $timeout(function () {
                                scope.$apply(attrs.focus + "=true");
                            }, 0);
                        })
                    }
                }
            });


            vm.promptDeleteLov = promptDeleteLov;

            function promptDeleteLov(index) {
                var width = $('#lov' + index).width();
                var height = $('#lov' + index).height();

                $('#lovMask' + index).width(width + 2);
                $('#lovMask' + index).height(height + 2);
                $('#lovMask' + index).css({display: 'table'});
            }

            vm.promptDeleteLovValue = promptDeleteLovValue;
            vm.valueIndex = null;
            function promptDeleteLovValue(value) {

                ItemTypeService.getItemsBySpec(vm.itemType.id, value.id).then(
                    function (data) {
                        if (data.length == 0) {
                            var width = $('#lov').width();
                            var height = $('#lov').height();

                            $('#lovValueMask').width(width + 2);
                            $('#lovValueMask').height(height + 2);
                            $('#lovValueMask').css({display: 'table'});
                        } else {
                            $rootScope.showWarningMessage("Spec already in use");
                        }
                    }
                )
            }

            vm.applyLovChanges = applyLovChanges;

            function applyLovChanges(newValue) {

                if (newValue.newSpecName == "" || newValue.newSpecName == null || newValue.newSpecName == undefined) {
                    $rootScope.showWarningMessage("Please enter value");
                } else {
                    if (newValue.id == null || newValue.id == undefined) {
                        newValue.specName = newValue.newSpecName.replace(/\s/g, '');//To remove all spaces in the middle of words

                        ItemTypeService.createItemTypeSpec(vm.itemType.id, newValue).then(
                            function (data) {
                                newValue.id = data.id;
                                newValue.editMode = false;
                                newValue.newMode = false;
                                $rootScope.showSuccessMessage("Spec created successfully");
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else {
                        newValue.specName = newValue.newSpecName;
                        ItemTypeService.updateItemTypeSpec(vm.itemType.id, newValue).then(
                            function (data) {
                                newValue.id = data.id;
                                newValue.editMode = false;
                                newValue.newMode = false;
                                $rootScope.showSuccessMessage("Spec updated successfully");
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }

            }

            vm.toDeleteValue = null;
            vm.deleteLovValue = deleteLovValue;
            vm.itemTypeSpecMap = new Hashtable();

            function deleteLovValue() {
                if (vm.toDeleteValue != null) {
                    ItemTypeService.deleteItemTypeSpec(vm.itemType.id, vm.toDeleteValue).then(
                        function (data) {
                            vm.itemTypeSpecs.splice(vm.itemTypeSpecs.indexOf(vm.toDeleteValue), 1);
                            $rootScope.showSuccessMessage("Specification deleted successfully");
                            hideMask();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.hideMask = hideMask;
            function hideMask() {
                $('#lovMask').hide();
                $('#lovValueMask').hide();
            }

            vm.cancelLovChanges = cancelLovChanges;

            function cancelLovChanges(value) {
                if (value.newMode == true) {
                    vm.itemTypeSpecs.splice(vm.itemTypeSpecs.indexOf(value), 1);
                }
            }

            vm.cancelLovChange = cancelLovChange;

            function cancelLovChange(lov) {
                lov.newName = lov.name;
                lov.newType = lov.type;
                lov.editType = false;
                if (lov.id == null) {
                    var index = vm.lovs.indexOf(lov);
                    vm.lovs.splice(index, 1);
                }
            }


            (function () {
                $scope.$on('app.itemType.selected', itemTypeSelected);
                $scope.$on('app.itemtype.save', onSave);

                loadAllLovs();
            })();
        }
    }
);
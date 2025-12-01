define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/measurementService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],
    function (module) {
        module.controller('ObjectTypeAttributesController', ObjectTypeAttributesController);

        function ObjectTypeAttributesController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                                ObjectTypeAttributeService, ObjectAttributeService, LovService, $translate, MeasurementService) {
            var vm = this;
            var parsed = angular.element("<div></div>");


            vm.addAttribute = addAttribute;
            vm.deleteAttribute = deleteAttribute;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.loadAttributes = loadAttributes;
            vm.editAttribute = editAttribute;


            $scope.deleteAttributeTitle = parsed.html($translate.instant("DELETE_ATTRIBUTE_DIALOG_TITLE")).html();
            $scope.saveAttributeTitle = parsed.html($translate.instant("SAVE_ATTRIBUTE")).html();
            $scope.cancelChanges = parsed.html($translate.instant("CANCEL_CHANGES")).html();


            vm.attributes = [];

            vm.lovs = null;
            vm.dataTypes = [
                'TEXT',
                'LONGTEXT',
                'RICHTEXT',
                'INTEGER',
                'DOUBLE',
                'DATE',
                'BOOLEAN',
                'LIST',
                'TIME',
                'TIMESTAMP',
                'CURRENCY',
                'OBJECT',
                'IMAGE',
                'ATTACHMENT',
                'HYPERLINK',
                'FORMULA'
            ];


            vm.refTypes = ['ITEM', 'ITEMREVISION', 'CUSTOMOBJECT', 'QUALITY', 'CHANGE', 'MESOBJECT', 'MROOBJECT', 'WORKFLOW',
                'MANUFACTURER', 'MANUFACTURERPART', 'PERSON',
                'PROJECT', 'REQUIREMENTDOCUMENT', 'REQUIREMENT'];


            var newAttribute = parsed.html($translate.instant("NEW_ATTRIBUTE")).html();

            var attribute = {
                id: null,
                objectType: null,
                name: null,
                newName: null,
                description: '',
                dataType: null,
                newDataType: null,
                refType: null,
                lov: null,
                newRefType: null,
                newLov: null,
                required: false,
                defaultValue: false,
                visible: true,
                editMode: true,
                dataTypeMode: true,
                refTypeMode: false,
                lovTypeMode: false,
                newGroup: false,
                attributeGroup: null,
                isRevision: false
            };


            function addAttribute() {
                var att = angular.copy(attribute);
                if (vm.attributeGroups.length == 0) {
                    att.newGroup = true;
                }
                vm.attributes.unshift(att);
                vm.unSavedAttributes.unshift(att);
            }

            var attributeertyDeletedMessage = parsed.html($translate.instant("DELETE_ATTRIBUTE_MESSAGE")).html();

            function deleteAttribute(attribute) {
                var options = {
                    title: $rootScope.deleteAttributeDialogTitle,
                    message: $rootScope.deleteAttributeDialogMessage,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ObjectTypeAttributeService.deleteObjectTypeAttribute(vm.selectedType, attribute.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(attributeertyDeletedMessage);
                                vm.attributes.splice(vm.attributes.indexOf(attribute), 1);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }
                })

            };

            function editAttribute(attribute) {
                ObjectAttributeService.getAttributeValuesByDef(attribute.id).then(
                    function (data) {
                        if (data.length == 0) {
                            attribute.showValues = false;
                            attribute.newName = attribute.name;
                            attribute.newDescription = attribute.description;
                            attribute.newDataType = attribute.dataType;
                            attribute.newRefType = attribute.refType;
                            attribute.newLov = attribute.lov;
                            attribute.newMeasurement = attribute.measurement;
                            attribute.oldAttributeGroup = attribute.attributeGroup;
                            if (vm.attributeGroups.length == 0) {
                                attribute.newGroup = true;
                            }
                            $timeout(function () {
                                attribute.editMode = true;
                                attribute.inVisible = false;
                            }, 500);
                        } else {
                            attribute.newName = attribute.name;
                            attribute.newDescription = attribute.description;
                            attribute.newDataType = attribute.dataType;
                            attribute.newRefType = attribute.refType;
                            attribute.newLov = attribute.lov;
                            attribute.oldAttributeGroup = attribute.attributeGroup;
                            attribute.newMeasurement = attribute.measurement;
                            attribute.inVisible = true;
                            attribute.defaultValue = true;
                            if (vm.attributeGroups.length == 0) {
                                attribute.newGroup = true;
                            }
                            /*$rootScope.showWarningMessage(attribute.name + " : Attribute already in use");*/
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    })

            }

            var attributeertySavedMessage = parsed.html($translate.instant("ATTRIBUTE_SAVED_MESSAGE")).html();


            function applyChanges(attribute) {
                if (attribute.newGroup && attribute.attributeGroup != null && attribute.attributeGroup != "") {
                    ObjectTypeAttributeService.getAttributeGroupsByName(attribute.attributeGroup).then(
                        function (data) {
                            if (data.length > 0) {
                                $rootScope.showErrorMessage(attribute.attributeGroup + " : Group Name already exist");
                            } else {
                                saveAttribute(attribute);
                            }
                        }
                    )
                } else {
                    saveAttribute(attribute);
                }

            }

            function saveAttribute(attribute) {
                if (attribute.isRevision == true) {
                    attribute.objectType = 'ITEMREVISION';
                } else {
                    attribute.objectType = vm.selectedType;
                }
                attribute.name = attribute.newName;
                attribute.description = attribute.newDescription;
                attribute.dataType = attribute.newDataType;
                attribute.refType = attribute.newRefType;
                attribute.lov = attribute.newLov;
                attribute.measurement = attribute.newMeasurement;
                if (validate(attribute)) {
                    var promise = null;
                    if (attribute.id == null) {
                        promise = ObjectTypeAttributeService.createObjectTypeAttribute(vm.selectedType, attribute);
                    }
                    else {
                        promise = ObjectTypeAttributeService.updateObjectTypeAttribute(vm.selectedType, attribute);
                    }
                    promise.then(
                        function (data) {
                            attribute.id = data.id;
                            attribute.editMode = false;
                            attribute.itemMode = false;
                            attribute.inVisible = false;
                            vm.unSavedAttributes.splice(vm.unSavedAttributes.indexOf(attribute), 1);
                            if (vm.unSavedAttributes.length == 0) {
                                loadAttributes();
                            }
                            loadAttributeGroups();
                            $timeout(function () {
                                attribute.showValues = true;
                            }, 500);
                            $rootScope.showSuccessMessage(attributeertySavedMessage);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }

            }

            var attributeertyNameValidation = $translate.instant("ATTRIBUTE_NAME_VALIDATION");
            var attributeertyDataTypeValidation = $translate.instant("ATTRIBUTE_DATA_TYPE_VALIDATION");
            var systemAttributeValidation = $translate.instant("ATTRIBUTE_NEW_NAME_VALIDATION");
            var listValueValidation = parsed.html($translate.instant("LIST_VALUE_VALIDATION")).html();

            function validate(attribute) {
                var valid = true;
                if (attribute.name == null || attribute.name == undefined
                    || attribute.name == "" || attribute.name == newAttribute) {
                    $rootScope.showErrorMessage(attributeertyNameValidation);
                    valid = false;
                }
                else if (attribute.dataType == null || attribute.dataType == undefined
                    || attribute.dataType == "") {
                    $rootScope.showErrorMessage(attributeertyDataTypeValidation);
                    valid = false;
                } else if (attribute.dataType == 'LIST' &&
                    (attribute.newLov == null || attribute.newLov == "" || attribute.newLov == undefined)) {
                    $rootScope.showErrorMessage(listValueValidation);
                    valid = false;
                } else {
                    angular.forEach(vm.attributes, function (itemProp) {
                        if (itemProp.name == attribute.newName && itemProp.id != attribute.id) {
                            $rootScope.showErrorMessage(systemAttributeValidation);
                            valid = false;
                        }
                    })
                }

                return valid;
            }

            function cancelChanges(attribute) {
                attribute.editMode = false;
                if (attribute.id == null) {
                    vm.attributes.remove(attribute);
                    vm.unSavedAttributes.splice(vm.unSavedAttributes.indexOf(attribute), 1);
                } else {
                    loadAttributes();
                    loadAttributeGroups();
                }
            }


            function loadAttributes() {
                $rootScope.showBusyIndicator();
                vm.unSavedAttributes = [];
                vm.selectedType = null;
                if ($rootScope.selectedType.attributes.nodeType == "ITEMTYPE") {
                    vm.selectedType = 'ITEM';
                } else if ($rootScope.selectedType.attributes.nodeType == "CHANGETYPE") {
                    vm.selectedType = 'CHANGE';
                } else if ($rootScope.selectedType.attributes.nodeType == "QUALITY_TYPE") {
                    vm.selectedType = 'QUALITY';
                } else if ($rootScope.selectedType.attributes.nodeType == "MANUFACTURERTYPE") {
                    vm.selectedType = 'MANUFACTURER';
                } else if ($rootScope.selectedType.attributes.nodeType == "MANUFACTURERPARTTYPE") {
                    vm.selectedType = 'MANUFACTURERPART';
                } else if ($rootScope.selectedType.attributes.nodeType == "WORKFLOWTYPE") {
                    vm.selectedType = 'WORKFLOW';
                } else if ($rootScope.selectedType.attributes.nodeType == "MESOBJECTTYPE") {
                    vm.selectedType = 'MESOBJECT';
                } else if ($rootScope.selectedType.attributes.nodeType == "MROOBJECTTYPE") {
                    vm.selectedType = 'MROOBJECT';
                } else if ($rootScope.selectedType.attributes.nodeType == "PGCOBJECTTYPE") {
                    vm.selectedType = 'PGCOBJECT';
                } else if ($rootScope.selectedType.attributes.nodeType == "REQUIREMENTDOCUMENTTYPE") {
                    vm.selectedType = 'REQUIREMENTDOCUMENT';
                } else if ($rootScope.selectedType.attributes.nodeType == "REQUIREMENTTYPE") {
                    vm.selectedType = 'REQUIREMENT';
                } else if ($rootScope.selectedType.attributes.nodeType == "BOMITEM") {
                    vm.selectedType = 'BOMITEM';
                } else if ($rootScope.selectedType.attributes.nodeType == "FILE") {
                    vm.selectedType = 'FILE';
                } else if ($rootScope.selectedType.attributes.nodeType == "CUSTOMOBJECTTYPE") {
                    vm.selectedType = 'CUSTOMOBJECT';
                } else if ($rootScope.selectedType.attributes.nodeType == "SUPPLIERTYPE") {
                    vm.selectedType = 'MFRSUPPLIER';
                }
                ObjectTypeAttributeService.getObjectTypeAttributesByType(vm.selectedType).then(
                    function (data) {
                        vm.attributes = [];
                        angular.forEach(data, function (attribute) {
                            if (!attribute.plugin) {
                                attribute.newName = attribute.name;
                                attribute.newDescription = attribute.description;
                                attribute.newDataType = attribute.dataType;
                                attribute.newRefType = attribute.refType;
                                attribute.NewMeasurement = attribute.measurement;
                                attribute.newLov = attribute.lov;
                                attribute.showValues = true;
                                attribute.editMode = false;
                                attribute.defaultValue = false;
                                vm.attributes.push(attribute);
                            }
                        });
                        if ($rootScope.selectedType.attributes.nodeType == "ITEMTYPE") {
                            loadItemRevisionAttributes();
                        }
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function loadItemRevisionAttributes() {
                ObjectTypeAttributeService.getObjectTypeAttributesByType('ITEMREVISION').then(
                    function (data) {
                        vm.revisionAttri = data;
                        angular.forEach(vm.revisionAttri, function (attribute) {
                            attribute.newName = attribute.name;
                            attribute.newDescription = attribute.description;
                            attribute.newDataType = attribute.dataType;
                            attribute.newRefType = attribute.refType;
                            attribute.newLov = attribute.lov;
                            attribute.showValues = true;
                            attribute.editMode = false;
                            attribute.defaultValue = false;
                            attribute.isRevision = true;
                            vm.attributes.push(attribute);
                        })
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function loadAllLovs() {
                $rootScope.showBusyIndicator();
                LovService.getAllLovs().then(
                    function (data) {
                        $scope.lovs = data;
                        loadAttributes();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $scope.getLimitedWord = getLimitedWord;
            function getLimitedWord(word, size) {
                if (word.length <= size) {
                    return word;
                } else {
                    return word.substr(0, size) + '...';
                }
            }


            function loadAttributeGroups() {
                ObjectTypeAttributeService.getAttributeGroups().then(
                    function (data) {
                        vm.attributeGroups = data;
                        if (vm.attributeGroups.length > 0) {
                            vm.attributeGroups.unshift("--New--");
                        }
                        // $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.selectAttributeGroup = selectAttributeGroup;
            function selectAttributeGroup(attribute, group) {
                if (group == "--New--") {
                    attribute.attributeGroup = null;
                    attribute.newGroup = true;
                } else {
                    attribute.atttributeGroup = group;
                }
            }

            vm.editAttributeGroup = editAttributeGroup;
            function editAttributeGroup(attribute) {
                var options = {
                    title: "Update Attribute Group",
                    template: 'app/desktop/modules/directives/editAttributeGroupView.jsp',
                    controller: 'EditAttributeGroupController as editAttributeGroupVm',
                    resolve: 'app/desktop/modules/directives/editAttributeGroupController',
                    width: 600,
                    showMask: true,
                    data: {
                        attribute: attribute,
                        selectedType: null
                    },
                    buttons: [
                        {text: "Create", broadcast: 'app.attributes.group.update'}
                    ],
                    callback: function () {
                        loadAttributes();
                        loadAttributeGroups();
                    }
                };

                $rootScope.hideSidePanel(options);
            }

            function loadMeasurements() {
                $rootScope.showBusyIndicator();
                MeasurementService.getAllMeasurements().then(
                    function (data) {
                        $scope.measurements = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            (function () {
                $scope.$on('app.classification.object.attribute', function (event, data) {
                    //objectTypeSelectedForAttributes();
                    $rootScope.selectedType = $rootScope.selectedObjectType;
                    loadAttributeGroups();
                    loadMeasurements();
                    loadAllLovs();
                });
            })();
        }
    }
)
;
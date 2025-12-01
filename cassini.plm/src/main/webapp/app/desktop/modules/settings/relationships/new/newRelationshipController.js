define(['app/desktop/modules/settings/settings.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/relationshipService',
        'app/desktop/modules/settings/relationships/new/relationClassificationTreeDirective',
        'app/desktop/modules/settings/relationships/new/relationClassificationTreeController',
        'app/shared/services/core/classificationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'

    ],
    function (module) {
        module.controller('NewRelationshipController', NewRelationshipController);

        function NewRelationshipController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                           CommonService, $translate, ItemTypeService, LovService, RelationshipService, ClassificationService, ObjectTypeAttributeService) {
            var vm = this;

            vm.onSelectFromType = onSelectFromType;
            vm.onSelectToType = onSelectToType;

            vm.validateAttribute = validateAttribute;
            vm.addAttribute = addAttribute;
            vm.applyChanges = applyChanges;
            vm.editAttribute = editAttribute;
            vm.cancelChanges = cancelChanges;
            vm.deleteAttribute = deleteAttribute;
            vm.createdRelationship = null;
            vm.lovs = [];
            vm.saveAttr = false;

            var newAttribute = {
                id: null,
                name: null,
                description: null,
                dataType: 'STRING',
                required: false,
                objectType: 'RELATIONSHIP',
                editMode: true,
                showValues: false,
                refType: null,
                lov: null
            };

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
                'ATTACHMENT'
            ];

            vm.refTypes = ['ITEM', 'ITEMREVISION', 'CHANGE', 'WORKFLOW', 'MANUFACTURER', 'MANUFACTURERPART', 'PERSON'];

            var parsed = angular.element("<div></div>");
            var itemSelectTypeValidationMsg = parsed.html($translate.instant("ITEM_SELECT_TYPE_VALIDATION")).html();

            function onSelectFromType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.relationship.fromType = itemType;


                }
            }

            function onSelectToType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.relationship.toType = itemType;

                }
            }

            var fromTypeValidation = parsed.html($translate.instant("FIRST_TYPE_VALIDATION")).html();
            var toTypeValidation = parsed.html($translate.instant("SECOND_TYPE_VALIDATION")).html();
            var saveAttr = parsed.html($translate.instant("SAVE_ATTRIBUTES")).html();
            var nameValid = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();

            function validate() {
                var valid = true;

                if (vm.relationship.name == null || vm.relationship.name == "" || vm.relationship.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValid);
                } else if (vm.relationship.fromType == null || vm.relationship.fromType == undefined || vm.relationship.fromType == "") {
                    valid = false;
                    $rootScope.showWarningMessage(fromTypeValidation);
                } else if (vm.relationship.toType == null || vm.relationship.toType == undefined || vm.relationship.toType == "") {
                    valid = false;
                    $rootScope.showWarningMessage(toTypeValidation);
                } else if (vm.relationship.attributes != null && vm.relationship.attributes.length > 0 && !validateNotSaveAttributes()) {
                    valid = false;
                }

                return valid;
            }

            function validateNotSaveAttributes() {
                var valid = true;
                for (var i = 0, len = vm.relationship.attributes.length; i < len; i++) {
                    if (vm.relationship.attributes[i].editMode === true) {
                        valid = false;
                        $rootScope.showWarningMessage(saveAttr);
                        break;
                    }
                }
                return valid;
            }

            var relationAlreadyValidation = parsed.html($translate.instant("RELATION_ALREADY_VALIDATION")).html();
            var relationBtwn = parsed.html($translate.instant("RELATION_BETWEEN")).html();
            var and = parsed.html($translate.instant("AND")).html();
            var exist = parsed.html($translate.instant("ALREADY_EXIST")).html();

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    RelationshipService.getRelationshipByName(vm.relationship).then(
                        function (data) {
                            if (data != null && data != "") {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showWarningMessage(vm.relationship.name + " : " + relationAlreadyValidation);
                            } else {
                                RelationshipService.getRelationshipByFromTypeAndToType(vm.relationship.fromType, vm.relationship.toType).then(
                                    function (data) {
                                        if (data != null && data != "") {
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.showWarningMessage(relationBtwn + " (  " + vm.relationship.fromType.name + " ) " + and + " ( " + vm.relationship.toType.name + " ) " + exist);
                                        } else {

                                            RelationshipService.createRelationship(vm.relationship).then(
                                                function (data) {
                                                    vm.relationship = {
                                                        id: null,
                                                        name: null,
                                                        description: null,
                                                        fromType: null,
                                                        toType: null
                                                    };
                                                    vm.createdRelationship = data;
                                                    $scope.callback(data);
                                                    $rootScope.hideBusyIndicator();
                                                    vm.showAttributes = true;
                                                    vm.relationship.attributes = [];
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                    $rootScope.hideBusyIndicator();
                                                }
                                            )
                                        }
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function edit() {
                if (validate()) {
                    RelationshipService.getRelationshipByName(vm.relationship).then(
                        function (data) {
                            if (vm.relationship.id == data.id || data == "") {
                                RelationshipService.getRelationshipByFromTypeAndToType(vm.relationship.fromType, vm.relationship.toType).then(
                                    function (data) {
                                        if (vm.relationship.id == data.id || data == "") {
                                            RelationshipService.updateRelationship(vm.relationship).then(
                                                function (data) {
                                                    loadAttributes();
                                                    $scope.$off('app.relationship.edit', edit);
                                                    $scope.callback(data);
                                                    $rootScope.hideSidePanel();
                                                }
                                            );
                                        } else {
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.showWarningMessage(relationBtwn + " (  " + vm.relationship.fromType.name + " ) " + and + " ( " + vm.relationship.toType.name + " ) " + exist);
                                        }
                                    }
                                );
                            } else {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showWarningMessage(vm.relationship.name + " : " + relationAlreadyValidation);
                            }
                        }
                    );

                }
            }

            function addAttribute() {
                var att = angular.copy(newAttribute);
                if (vm.createdRelationship != undefined && vm.createdRelationship != null) {
                    att.relationship = vm.createdRelationship.id;
                    vm.relationship.attributes.unshift(att);
                } else {
                    att.relationship = vm.relationship.id;
                    vm.relationship.attributes.unshift(att);
                }
            }

            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var descriptionValidation = parsed.html($translate.instant("DESCRIPTION_VALIDATION")).html();
            var dataTypeValidation = parsed.html($translate.instant("DATA_TYPE_VALIDATION")).html();
            var listValueValidation = parsed.html($translate.instant("LIST_VALUE_VALIDATION")).html();
            var referenceTypeValidation = parsed.html($translate.instant("REFERENCE_TYPE_VALIDATION")).html();
            var deleteAttributeDialogTitle = parsed.html($translate.instant("DELETE_ATTRIBUTE_DIALOG_TITLE")).html();
            var deleteAttributeDialogMessage = parsed.html($translate.instant("DELETE_ATTRIBUTE_DIALOG_MESSAGE")).html();
            var deleteAttributeMessage = parsed.html($translate.instant("DELETE_ATTRIBUTE_MESSAGE")).html();

            function validateAttribute(attribute) {
                var valid = true;
                if (attribute.newName == null || attribute.newName == "" || attribute.newName == undefined) {
                    vm.hide = true;
                    $rootScope.showWarningMessage(nameValidation);
                    valid = false;
                } else if (attribute.newDataType == null || attribute.newDataType == "" || attribute.newDataType == undefined) {
                    vm.hide = true;
                    $rootScope.showWarningMessage(dataTypeValidation);
                    valid = false;
                } else if (attribute.newDataType == 'LIST' &&
                    (attribute.newLov == null || attribute.newLov == "" || attribute.newLov == undefined)) {
                    vm.hide = true;
                    $rootScope.showWarningMessage(listValueValidation);
                    valid = false;

                } else if (attribute.newDataType == 'OBJECT' &&
                    (attribute.newRefType == null || attribute.newRefType == "" || attribute.newRefType == undefined)) {
                    vm.hide = true;
                    $rootScope.showWarningMessage(referenceTypeValidation);
                    valid = false;
                }

                return valid;
            }

            var attributeSavedMessage = $translate.instant("ATTRIBUTE_SAVED_MESSAGE");

            function applyChanges(attribute) {
                if (validateAttribute(attribute)) {
                    vm.hide = false;
                    var promise = null;

                    if (attribute.id == null || attribute.id == undefined) {
                        attribute.name = attribute.newName;
                        attribute.description = attribute.newDescription;
                        attribute.dataType = attribute.newDataType;
                        attribute.refType = attribute.newRefType;
                        attribute.lov = attribute.newLov;
                        promise = RelationshipService.createRelationshipAttribute(attribute);
                    }
                    else {
                        attribute.name = attribute.newName;
                        attribute.description = attribute.newDescription;
                        attribute.dataType = attribute.newDataType;
                        attribute.refType = attribute.newRefType;
                        attribute.lov = attribute.newLov;
                        promise = RelationshipService.updateRelationshipAttribute(attribute);
                    }

                    promise.then(
                        function (data) {
                            vm.error = "";
                            vm.saveAttr = true;
                            attribute.id = data.id;
                            attribute.editMode = false;
                            $timeout(function () {
                                attribute.showValues = true;
                            }, 500);
                            $rootScope.showSuccessMessage(data.name + " : " + attributeSavedMessage);
                            loadAttributes();
                        }
                    )
                }
            }

            function editAttribute(attribute) {
                attribute.showValues = false;
                attribute.newName = attribute.name;
                attribute.newDescription = attribute.description;
                attribute.newDataType = attribute.dataType;
                attribute.newDefaultTextValue = attribute.defaultTextValue;
                attribute.newRefType = attribute.refType;
                attribute.newRequired = attribute.required;
                attribute.newLov = attribute.lov;
                $timeout(function () {
                    attribute.editMode = true;
                }, 500);
            }

            function cancelChanges(attribute) {
                vm.hide = false;
                if (attribute.id == null || attribute.id == undefined) {
                    vm.relationship.attributes.splice(vm.relationship.attributes.indexOf(attribute), 1);
                    vm.error = null;
                }
                else {
                    attribute.newName = attribute.name;
                    attribute.newDescription = attribute.description;
                    attribute.newDataType = attribute.dataType;
                    attribute.newDefaultTextValue = attribute.defaultTextValue;
                    attribute.required = attribute.newRequired;
                    attribute.editMode = false;
                    vm.error = null;
                    $timeout(function () {
                        attribute.showValues = true;
                    }, 500);
                }
            }

            function deleteAttribute(attribute) {
                RelationshipService.deleteRelationshipAttribute(attribute.id).then(
                    function (data) {
                        vm.relationship.attributes.splice(vm.relationship.attributes.indexOf(attribute), 1);
                        $rootScope.showSuccessMessage(attribute.name + " : " + deleteAttributeMessage);
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function loadAttributes() {
                if (vm.createdRelationship != undefined && vm.createdRelationship != null) {
                    RelationshipService.getAllAttributesByRelationship(vm.createdRelationship.id).then(
                        function (data) {
                            angular.forEach(data, function (attribute) {
                                attribute.showValues = true;
                                attribute.editMode = false;
                            });
                            vm.relationship.attributes = data;
                        }
                    );
                } else {
                    RelationshipService.getAllAttributesByRelationship(vm.relationship.id).then(
                        function (data) {
                            angular.forEach(data, function (attribute) {
                                attribute.showValues = true;
                                attribute.editMode = false;
                            });
                            vm.relationship.attributes = data;
                        }
                    );
                }
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

            (function () {
                $scope.$on('app.relationship.new', create);
                $scope.$on('app.relationship.edit', edit);
                loadAllLovs();
                var selectedRelationship = $scope.data.relationshipData;
                if (selectedRelationship != null && selectedRelationship != undefined) {
                    vm.relationship = {
                        id: selectedRelationship.id,
                        name: selectedRelationship.name,
                        description: selectedRelationship.description,
                        fromType: selectedRelationship.fromType,
                        toType: selectedRelationship.toType
                    };
                    vm.showAttributes = true;
                    vm.relationship.attributes = [];
                    loadAttributes();
                } else {
                    vm.relationship = {
                        id: null,
                        name: null,
                        description: null,
                        fromType: null,
                        toType: null
                    };
                    vm.showAttributes = false;
                    vm.relationship.attributes = [];
                }
            })();
        }
    }
);
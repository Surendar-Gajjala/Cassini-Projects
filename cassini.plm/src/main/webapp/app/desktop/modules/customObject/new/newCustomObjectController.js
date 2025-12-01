define(
    [
        'app/desktop/modules/customObject/customObject.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/desktop/modules/directives/customObjectTypeDirective',
        'app/shared/services/core/workflowService',
        'app/shared/services/core/supplierService',
    ],
    function (module) {
        module.controller('NewCustomObjectController', NewCustomObjectController);

        function NewCustomObjectController($scope, $rootScope, $timeout, $q, $state, $translate, $stateParams, $cookies, ObjectTypeAttributeService, AutonumberService, CustomObjectService, CustomObjectTypeService, WorkflowService, SupplierService) {

            var vm = this;
            vm.isSupplierPerformanceRating = false;
            vm.loadSuppliers = loadSuppliers;
            var parsed = angular.element("<div></div>");

            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var typeValidation = parsed.html($translate.instant("CUSTOM_TYPE_CANNOT_BE_EMPTY")).html();
            var numberValidation = parsed.html($translate.instant("NUMBER_CANNOT_BE_EMPTY")).html();
            var nameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            var descriptionValidation = parsed.html($translate.instant("DESCRIPTION_NOT_EMPTY")).html();
            var customeObjectCreatedSucc = parsed.html($translate.instant("CUSTOM_OBJECT_CREATED_SUCCESS_MESSAGE")).html();
            var selectSupplierValidation = parsed.html($translate.instant("SELECT_SUPPLIER_VALIDATION")).html();

            vm.selectTypeId = $scope.data.selectType;
            vm.creationType = $scope.data.creationType;
            vm.customeObjectTypeName = ""
            vm.onSelectType = onSelectType;
            vm.customObject = {
                id: null,
                type: null,
                name: null,
                supplier: null,
                number: null,
                description: null,
                workflowDefinition: null,
                workflowDefId: null,
                supplierName: null,
                supplierEmail: null
            };

            function onSelectType(customType) {
                if (customType != null && customType != undefined) {
                    vm.customObject.type = customType;
                    vm.customeObjectTypeName = customType.name;
                    vm.type = customType;
                    autoNumber();
                    loadAttributeDefs();
                    loadWorkflows();
                    if (vm.customeObjectTypeName == "Supplier Performance Rating" || vm.customeObjectTypeName == "CPI Form" || vm.customeObjectTypeName == "4MChange-Supplier") loadSuppliers()
                }
            }

            function autoNumber() {
                if (vm.customObject.type != null && vm.customObject.type.numberSource != null) {
                    var source = vm.customObject.type.numberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.customObject.number = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.suppliers = [];
            function loadSuppliers() {
                vm.suppliers = [];
                SupplierService.getApprovedSuppliers().then(
                    function (data) {
                        vm.suppliers = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.onSelectSupplier = onSelectSupplier;
            function onSelectSupplier(supplier) {
                vm.customObject.supplierName = supplier.name;
                vm.customObject.supplierEmail = supplier.email;
            }

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    if (vm.customObject.workflowDefinition != null) {
                        vm.workflowDefId = vm.customObject.workflowDefinition.id;
                    }
                    vm.customObject.type = vm.type;
                    vm.imageAttributes = [];
                    vm.attachmentAttributes = [];
                    vm.images = new Hashtable();
                    vm.attachments = new Hashtable();
                    angular.forEach(vm.attributes, function (attribute) {
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            vm.images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            vm.attachments.put(attribute.id.attributeDef, attribute.attachmentValues);
                            vm.attachmentAttributes.push(attribute);
                            attribute.attachmentValues = [];
                        }
                    });
                    vm.customImageAttributes = [];
                    vm.customAttachmentAttributes = [];
                    vm.customImages = new Hashtable();
                    vm.customAttachments = new Hashtable();
                    angular.forEach(vm.customAttributes, function (attribute) {
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            vm.customImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.customImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            vm.customAttachments.put(attribute.id.attributeDef, attribute.attachmentValues);
                            vm.customAttachmentAttributes.push(attribute);
                            attribute.attachmentValues = [];
                        }
                    });
                    var customObjectDto = {
                        customObject: vm.customObject,
                        customObjectAttributes: vm.attributes,
                        objectAttributes: vm.customAttributes
                    };
                    CustomObjectService.createCustomObject(customObjectDto).then(
                        function (data) {
                            vm.customObject = data.customObject;
                            vm.customObject.supplierEmail = customObjectDto.customObject.supplierEmail;
                            vm.customObject.supplierName = customObjectDto.customObject.supplierName;
                            if (vm.workflowDefId != null) {
                                WorkflowService.attachCustomObjectWorkflow(vm.customObject.id, vm.workflowDefId).then(
                                    function (data) {
                                        vm.workflowDefId = null
                                    })
                            }
                            saveCustomAttributes().then(
                                function (atts) {
                                    saveAttributes().then(
                                        function (attributes) {
                                            CustomObjectService.sendNotification(vm.customObject).then(
                                                function (data) {
                                                    $scope.callback(vm.customObject);
                                                    $rootScope.hideBusyIndicator();
                                                    $rootScope.hideSidePanel();
                                                    $rootScope.showSuccessMessage(customeObjectCreatedSucc);
                                                    vm.customObject = {
                                                        id: null,
                                                        type: null,
                                                        name: null,
                                                        number: null,
                                                        description: null,
                                                        workflowDefinition: null,
                                                        workflowDefId: null
                                                    };
                                                }
                                            )
                                        }
                                    )
                                }
                            )

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.customObject.type == null || vm.customObject.type == undefined ||
                    vm.customObject.type == "") {
                    $rootScope.showErrorMessage(typeValidation);
                    valid = false;
                }
                else if (vm.customObject.number == null || vm.customObject.number == undefined ||
                    vm.customObject.number == "") {
                    $rootScope.showErrorMessage(numberValidation);
                    valid = false;
                }
                else if ((vm.customeObjectTypeName == "Supplier Performance Rating" || vm.customeObjectTypeName == "CPI Form" || vm.customeObjectTypeName == "4MChange-Supplier") && (vm.customObject.supplier == null || vm.customObject.supplier == undefined || vm.customObject.supplier == "")) {
                    $rootScope.showErrorMessage(selectSupplierValidation);
                    valid = false;
                }
                else if (vm.customObject.name == null || vm.customObject.name == undefined ||
                    vm.customObject.name == "") {
                    $rootScope.showErrorMessage(nameValidation);
                    valid = false;
                } else if (vm.customObject.description == null || vm.customObject.description == undefined ||
                    vm.customObject.description == "") {
                    $rootScope.showErrorMessage(descriptionValidation);
                    valid = false;
                }
                else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if (vm.customAttributes.length > 0 && !validateCustomAttributes()) {
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }

                return valid;
            }

            function validateAttributes() {
                var valid = true;
                angular.forEach(vm.attributes, function (attribute) {
                    if (valid) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if (!$rootScope.checkAttribute(attribute)) {
                                valid = false;
                                $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                            }
                        }
                    }
                });
                return valid;
            }

            function validateCustomAttributes() {
                var valid = true;
                angular.forEach(vm.customAttributes, function (attribute) {
                    if (valid) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if (!$rootScope.checkAttribute(attribute)) {
                                valid = false;
                                $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                            }
                        }
                    }
                });
                return valid;
            }

            function saveAttributes() {
                var defered = $q.defer();
                if (vm.imageAttributes.length > 0 || vm.attachmentAttributes.length > 0) {
                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                            CustomObjectService.updateAttributeImageValue("CUSTOMOBJECTTYPE", vm.customObject.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    );

                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            CustomObjectService.updateAttributeAttachmentValues("CUSTOMOBJECTTYPE", vm.customObject.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    )
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function saveCustomAttributes() {
                var defered = $q.defer();
                if (vm.customImageAttributes.length > 0 || vm.customAttachmentAttributes.length > 0) {
                    angular.forEach(vm.customImageAttributes, function (imgAtt) {
                            CustomObjectService.updateAttributeImageValue("CUSTOMOBJECT", vm.customObject.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    )

                    angular.forEach(vm.customAttachmentAttributes, function (imgAtt) {
                            CustomObjectService.updateAttributeAttachmentValues("CUSTOMOBJECT", vm.customObject.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    )
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function loadAttributeDefs() {
                vm.requiredAttributes = [];
                vm.attributes = [];
                CustomObjectTypeService.getCustomObjectAttributesWithHierarchy(vm.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.type.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                refValue: null,
                                timestampValue: null,
                                ref: null,
                                imageValue: null,
                                attachmentValues: []
                            };
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple && attribute.defaultListValue != null) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple && attribute.defaultListValue != null) {
                                att.mlistValue.push(attribute.defaultListValue);
                            }
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.validations != null && attribute.validations != "") {
                                attribute.newValidations = JSON.parse(attribute.validations);
                            }
                            vm.attributes.push(att);

                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            function loadCustomProperties() {
                vm.customAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("CUSTOMOBJECT").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.customObject.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                mlistValue: [],
                                newListValue: null,
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple && attribute.defaultListValue != null) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple && attribute.defaultListValue != null) {
                                att.mlistValue.push(attribute.defaultListValue);
                            }
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.plugin == false) {
                                vm.customAttributes.push(att);
                            }

                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            function loadWorkflows() {
                WorkflowService.getCustomObjectWorkflows(vm.type.id, 'CUSTOM OBJECTS').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadCustomProperties();
                if ($scope.data.creationType == "Supplier") {
                    vm.customObject.supplier = $scope.data.supplierData.id;
                    vm.customObject.supplierEmail = $scope.data.supplierData.email;
                    vm.customObject.supplierName = $scope.data.supplierData.name;
                    vm.customObject.type = $scope.data.customObjectTypeData;
                    vm.type = $scope.data.customObjectTypeData;
                    vm.customeObjectTypeName = vm.customObject.type.name;
                    loadAttributeDefs();
                    loadWorkflows();
                    autoNumber();
                    loadSuppliers();
                }
                $rootScope.$on('app.customObject.new', create);
                //}
            })();
        }
    }
);
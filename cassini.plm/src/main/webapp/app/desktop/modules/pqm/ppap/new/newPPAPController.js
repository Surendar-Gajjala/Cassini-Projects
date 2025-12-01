define([
    'app/desktop/modules/pqm/pqm.module',
    'moment',
    'moment-timezone-with-data',
    'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
    'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
    'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
    'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
    'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
    'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
    'app/desktop/modules/pqm/directives/qualityTypeDirective',
    'app/shared/services/core/supplierService',
    'app/shared/services/core/mfrPartsService',
    'app/shared/services/core/supplierAuditService',
    'app/shared/services/core/ppapService',
    'app/shared/services/core/classificationService'
], function (module) {
    module.controller("NewPPAPController", NewPPAPController);
    function NewPPAPController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, $application, ObjectTypeAttributeService,
                               AutonumberService, SupplierService, MfrPartsService, PpapService, SupplierAuditService, QualityTypeService, LoginService, ClassificationService) {


        var vm = this;
        var parsed = angular.element("<div></div>");
        $scope.selectManufacturer = parsed.html($translate.instant("SELECT")).html();
        var pappTypeValidation = parsed.html($translate.instant("TYPE_VALIDATION")).html();
        var ppapNameValidation = parsed.html($translate.instant("TITTLE_VALIDATION")).html();
        var pappNumberValidation = parsed.html($translate.instant("NUMBER_CANNOT_BE_EMPTY")).html();
        var pappDescriptionValidation = parsed.html($translate.instant("DESCRIPTION_VALIDATION")).html();
        var selectSupplierValidation = parsed.html($translate.instant("SELECT_SUPPLIER_VALIDATION")).html();
        var selectPartValidation = parsed.html($translate.instant("SELECT_PART_VALIDATION")).html();
        var ppapCreated = parsed.html($translate.instant("PPAP_CREATED_MESSAGE")).html();

        vm.newPpap = {
            type: null,
            number: null,
            supplier: null,
            supplierPart: null,
            name: null,

            description: null,

        };
        vm.onSelectType = onSelectType;

        function onSelectType(itemType) {
            if (itemType != null && itemType != undefined) {
                vm.newPpap.type = itemType;
                autoNumber();
            }
        }

        function autoNumber() {
            if (vm.newPpap.type != null && vm.newPpap.type.numberSource != null) {
                var source = vm.newPpap.type.numberSource;
                AutonumberService.getNextNumberByName(source.name).then(
                    function (data) {
                        vm.newPpap.number = data;
                        loadAttributeDefs();
                        loadWorkflows();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }
        }

        function loadWorkflows() {
            QualityTypeService.getQualityTypeWorkflows(vm.newPpap.type.id, 'QUALITY').then(
                function (data) {
                    vm.workflows = data;
                }, function (error) {
                    $rootScope.showErrorMessage(error.message);
                    $rootScope.hideBusyIndicator();
                }
            )
        }

        function loadAttributeDefs() {
            vm.attributes = [];
            ClassificationService.getObjectTypeAttributesWithHierarchy(vm.newPpap.type.qualityType, vm.newPpap.type.id, 0).then(
                function (data) {
                    vm.qualityTypeAttributes = data.qualityTypeAttributes;
                    angular.forEach(vm.qualityTypeAttributes, function (attribute) {
                        var att = {
                            id: {
                                objectId: vm.newPpap.id,
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


        vm.pageable = {
            page: 0,
            size: 20,
            sort: {
                field: "modifiedDate",
                order: "DESC"
            }
        };

        vm.filters = {
            partNumber: null,
            partName: null,
            description: null,
            mfrPartType: '',
            manufacturer: '',
            freeTextSearch: true,
            searchQuery: null
        };


        function create() {
            if (validate()) {
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
                angular.forEach(vm.qcrRequiredProperties, function (reqatt) {
                    vm.qcrProperties.push(reqatt);
                })
                angular.forEach(vm.qcrProperties, function (attribute) {
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
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                var qualityAttributeDto = {
                    ppap: vm.newPpap,
                    ppapAttributes: vm.attributes,
                    objectAttributes: vm.qcrProperties
                }
                QualityTypeService.createQualityObject("PPAP", qualityAttributeDto).then(
                    function (data) {
                        vm.newPpap = data.ppap;
                        saveCustomAttributes().then(
                            function (atts) {
                                saveAttributes().then(
                                    function (attributes) {
                                        $scope.callback(vm.newPpap);
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(ppapCreated);
                                        vm.newPpap = {
                                            id: null,
                                            type: null,
                                            number: null,
                                            supplier: null,
                                            supplierPart: null,
                                            name: null,
                                            description: null,
                                        };
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
            function validate() {
                var valid = true;
                if (vm.newPpap.type == null || vm.newPpap.type == undefined ||
                    vm.newPpap.type == "") {
                    $rootScope.showWarningMessage(pappTypeValidation);
                    valid = false;
                }
                else if (vm.newPpap.number == null || vm.newPpap.number == undefined ||
                    vm.newPpap.number == "") {
                    $rootScope.showWarningMessage(pappNumberValidation);
                    valid = false;
                }
                else if (vm.newPpap.supplier == null || vm.newPpap.supplier == undefined ||
                    vm.newPpap.supplier == "") {
                    $rootScope.showWarningMessage(selectSupplierValidation);
                    valid = false;
                }
                else if (vm.newPpap.supplierPart == null || vm.newPpap.supplierPart == undefined ||
                    vm.newPpap.supplierPart == "") {
                    $rootScope.showWarningMessage(selectPartValidation);
                    valid = false;
                }
                else if (vm.newPpap.name == null || vm.newPpap.name == undefined ||
                    vm.newPpap.name == "") {
                    $rootScope.showWarningMessage(ppapNameValidation);
                    valid = false;
                } else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if (vm.qcrRequiredProperties.length > 0 && !validateCustomAttributes()) {
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }
                return valid;
            }
        }

        var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");

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
            angular.forEach(vm.qcrRequiredProperties, function (attribute) {
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
                        ClassificationService.updateAttributeImageValue("PPAPTYPE", vm.newPpap.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                            function (data) {
                                defered.resolve();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                )

                angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                        ClassificationService.updateAttributeAttachmentValues("PPAPTYPE", vm.newPpap.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
                            function (data) {
                                defered.resolve();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
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
                        ClassificationService.updateAttributeImageValue("PPAP", vm.newPpap.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
                            function (data) {
                                defered.resolve();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                )

                angular.forEach(vm.customAttachmentAttributes, function (imgAtt) {
                        ClassificationService.updateAttributeAttachmentValues("PPAP", vm.newPpap.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
                            function (data) {
                                defered.resolve();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                )
            } else {
                defered.resolve();
            }
            return defered.promise;
        }

        function loadPersons() {
            vm.persons = [];
            LoginService.getAllLogins().then(
                function (data) {
                    angular.forEach(data, function (login) {
                        if (login.isActive == true && login.external == false) {
                            vm.persons.push(login.person);
                        }
                    });
                }, function (error) {
                    $rootScope.showErrorMessage(error.message);
                    $rootScope.hideBusyIndicator();
                }
            )
        }

        vm.loadParts = loadParts;
        vm.parts = [];
        function loadParts(id) {
            vm.parts = [];
            vm.newPpap.supplierPart = null;
            SupplierService.getSupplierParts(id).then(
                function (data) {
                    vm.parts = data;
                }, function (error) {
                    $rootScope.showErrorMessage(error.message);
                    $rootScope.hideBusyIndicator();
                }
            )
        }

        vm.suppliers = [];
        function loadSuppliers() {
            console.log("suppliers loaded");
            SupplierService.getApprovedSuppliers().then(
                function (data) {
                    vm.suppliers = data;
                    console.log(vm.suppliers);
                }, function (error) {
                    $rootScope.showErrorMessage(error.message);
                }
            )
        }

        function loadQualityCustomProperties() {
            vm.qcrProperties = [];
            vm.qcrRequiredProperties = [];
            ObjectTypeAttributeService.getObjectTypeAttributesByType("QUALITY").then(
                function (data) {
                    angular.forEach(data, function (attribute) {
                        var att = {
                            id: {
                                objectId: vm.newPpap.id,
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
                        if (attribute.required == false) {
                            vm.qcrProperties.push(att);
                        } else {
                            vm.qcrRequiredProperties.push(att);
                        }
                    });
                }, function (error) {
                    $rootScope.showErrorMessage(error.message);
                    $rootScope.hideBusyIndicator();
                }
            )
        }

        (function () {
            loadSuppliers();
            loadPersons();
            loadQualityCustomProperties();
            $rootScope.$on('app.ppap.new', create);
        })();


    }

});
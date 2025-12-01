define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/desktop/modules/directives/changeTypeDirective',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/mbomService',
        'app/shared/services/core/bopService',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/itemService'
    ],
    function (module) {

        module.controller('NewBOPController', NewBOPController);

        function NewBOPController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, CommonService, MESObjectTypeService,
                                  AutonumberService, LoginService, WorkflowDefinitionService, ObjectTypeAttributeService, BOPService, ClassificationService,
                                  QualityTypeService, MBOMService, ItemService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            $scope.selectManufacturer = parsed.html($translate.instant("SELECT")).html();
            $scope.selectWorkflowTitle = parsed.html($translate.instant("SELECT_WORKFLOW")).html();
            var bopTypeValidation = parsed.html($translate.instant("TYPE_VALIDATION")).html();
            var bopNumberValidation = parsed.html($translate.instant("PLEASE_ENTER_NUMBER")).html();
            var bopNameValidation = parsed.html($translate.instant("PLEASE_ENTER_NAME")).html();
            var pleaseSelectMbom = parsed.html($translate.instant("PLEASE_SELECT_MBOM")).html();
            var pleaseSelectMbomRevision = parsed.html($translate.instant("PLEASE_SELECT_MBOM_REVISION")).html();
            var selectWorkflow = $translate.instant("WORKFLOW_SELECT_VALID");
            var bopCreatedMsg = parsed.html($translate.instant("BOP_CREATED_MESSAGE")).html();

            vm.newBOP = {
                id: null,
                type: null,
                mbom: null,
                number: null,
                name: null,
                description: null,
                workflow: null,
                mbomRevision: null,
                workflowDefinition: null
            };
            vm.onSelectType = onSelectType;
            vm.creationType = $scope.data.bopCreationType;

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newBOP.type = itemType;
                    vm.newBOP.workflowDefinition = null;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newBOP.type != null && vm.newBOP.type.autoNumberSource != null) {
                    var source = vm.newBOP.type.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newBOP.number = data;
                            loadBOPTypeAttributes();
                            loadWorkflows();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadWorkflows() {
                BOPService.getMESTypeWorkflows(vm.newBOP.type.id, 'MANUFACTURING').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadBOPTypeAttributes() {
                vm.attributes = [];
                MESObjectTypeService.getMesObjectAttributesWithHierarchy(vm.newBOP.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newBOP.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                integerValue: null,
                                stringValue: null,
                                doubleValue: null,
                                imageValue: null,
                                refValue: null,
                                timestampValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST") {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss")
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
                );
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
                    angular.forEach(vm.mbomRequiredProperties, function (reqatt) {
                        vm.mbomProperties.push(reqatt);
                    })
                    angular.forEach(vm.mbomProperties, function (attribute) {
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
                    if (vm.newBOP.workflowDefinition != null) {
                        vm.newBOP.workflowDefId = vm.newBOP.workflowDefinition;
                    }
                    BOPService.createBOP(vm.newBOP).then(
                        function (data) {
                            vm.newBOP = data;
                            saveCustomAttributes().then(
                                saveAttributes().then(
                                    function (attributes) {
                                        $scope.callback(vm.newBOP);
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(bopCreatedMsg);
                                        vm.newBOP = {
                                            id: null,
                                            type: null,
                                            mbom: null,
                                            number: null,
                                            name: null,
                                            description: null,
                                            workflow: null,
                                            mbomRevision: null
                                        };
                                    }
                                )
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }

                function validate() {
                    var valid = true;
                    if (vm.newBOP.type == null || vm.newBOP.type == undefined || vm.newBOP.type == "") {
                        $rootScope.showWarningMessage(bopTypeValidation);
                        valid = false;
                    } else if (vm.newBOP.number == null || vm.newBOP.number == undefined || vm.newBOP.number == "") {
                        $rootScope.showWarningMessage(bopNumberValidation);
                        valid = false;
                    } else if (vm.newBOP.name == null || vm.newBOP.name == undefined || vm.newBOP.name == "") {
                        $rootScope.showWarningMessage(bopNameValidation);
                        valid = false;
                    } else if (vm.newBOP.mbom == null || vm.newBOP.mbom == undefined || vm.newBOP.mbom == "") {
                        $rootScope.showWarningMessage(pleaseSelectMbom);
                        valid = false;
                    } else if (vm.newBOP.mbomRevision == null || vm.newBOP.mbomRevision == undefined || vm.newBOP.mbomRevision == "") {
                        $rootScope.showWarningMessage(pleaseSelectMbomRevision);
                        valid = false;
                    } else if (vm.newBOP.workflowDefinition == null || vm.newBOP.workflowDefinition == undefined || vm.newBOP.workflowDefinition == "") {
                        $rootScope.showWarningMessage(selectWorkflow);
                        valid = false;
                    } else if (vm.attributes.length > 0 && !validateAttributes()) {
                        valid = false;
                    } else if (vm.mbomRequiredProperties.length > 0 && !validateCustomAttributes()) {
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
                angular.forEach(vm.mbomRequiredProperties, function (attribute) {
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
                angular.forEach(vm.attributes, function (attribute) {
                    attribute.id.objectId = vm.newBOP.id;
                });
                BOPService.saveBOPAttributes(vm.attributes).then(
                    function (data) {
                        if (vm.imageAttributes.length > 0 || vm.attachmentAttributes.length > 0) {
                            angular.forEach(vm.imageAttributes, function (imgAtt) {
                                    ClassificationService.updateAttributeImageValue("MESOBJECTTYPE", vm.newBOP.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                        function (data) {
                                            defered.resolve();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            )

                            angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                                    ClassificationService.updateAttributeAttachmentValues("MESOBJECTTYPE", vm.newBOP.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
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
                    }
                )
                return defered.promise;
            }

            function saveCustomAttributes() {
                var defered = $q.defer();
                if (vm.customImageAttributes.length > 0 || vm.customAttachmentAttributes.length > 0) {
                    angular.forEach(vm.customImageAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeImageValue("BOP", vm.newBOP.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.customAttachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("BOP", vm.newBO.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
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

            function loadReleasedMboms() {
                MBOMService.getReleasedMBOMs().then(
                    function (data) {
                        vm.mboms = data;
                        if ($scope.data.bopCreationType == "MBOM") {
                            angular.forEach(vm.mboms, function (mbom) {
                                if (mbom.id == vm.newBOP.mbom) {
                                    vm.selectedMbom = mbom;
                                }
                            })
                        }
                    }
                )
            }

            vm.selectedMbom = null;
            vm.onSelectMbom = onSelectMbom;
            function onSelectMbom(mbom) {
                vm.selectedMbom = mbom;
                vm.newBOP.mbomRevision = null;
                if (mbom.mbomRevisions.length == 1) {
                    vm.newBOP.mbomRevision = mbom.mbomRevisions[0].id;
                }
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

            function loadBOPCustomProperties() {
                vm.mbomProperties = [];
                vm.mbomRequiredProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("BOP").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newBOP.id,
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
                                vm.mbomProperties.push(att);
                            } else {
                                vm.mbomRequiredProperties.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadReleasedMboms();
                loadPersons();
                loadBOPCustomProperties();
                if ($scope.data.bopCreationType == "MBOM") {
                    vm.newBOP.mbom = $scope.data.mbom;
                    vm.newBOP.mbomRevision = $scope.data.mbomRevision;
                }
                $rootScope.$on('app.bop.new', create);
            })();


        }

    });    
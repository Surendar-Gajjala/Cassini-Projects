define(
    [
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
        'app/shared/services/core/ncrService',
        'app/shared/services/core/inspectionService',
        'app/shared/services/core/classificationService'
    ],
    function (module) {

        module.controller('NewNcrController', NewNcrController);

        function NewNcrController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, $application,
                                  AutonumberService, ObjectAttributeService, AttachmentService, AttributeAttachmentService, ClassificationService,
                                  NcrService, LoginService, ItemService, QualityTypeService, ObjectTypeAttributeService, InspectionService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var selectNcrType = parsed.html($translate.instant("SELECT_NCR_TYPE")).html();
            var enterNcrNumber = parsed.html($translate.instant("ENTER_NCR_NUMBER")).html();
            var enterTitle = parsed.html($translate.instant("ENTER_TITLE")).html();
            var selectQualityAnalyst = parsed.html($translate.instant("SELECT_QUALITY_ANALYST")).html();
            var selectFailureType = parsed.html($translate.instant("SELECT_FAILURE_TYPE")).html();
            var selectSeverity = parsed.html($translate.instant("SELECT_SEVERITY")).html();
            var selectDisposition = parsed.html($translate.instant("SELECT_DISPOSITION")).html();
            var enterDescription = parsed.html($translate.instant("ENTER_DESCRIPTION")).html();
            var ncrCreated = parsed.html($translate.instant("NCR_CREATED")).html();
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            var selectWorkflowValid = parsed.html($translate.instant("WORKFLOW_SELECT_VALID")).html();
            $scope.selectQualityAdminTitle = parsed.html($translate.instant("S_QUALITY_ADMINISTRATOR")).html();
            $scope.selectWorkflowTitle = parsed.html($translate.instant("SELECT_WORKFLOW")).html();
            $scope.selectFailureTitle = parsed.html($translate.instant("S_FAILURE_TYPE")).html();
            $scope.selectSeverityTitle = parsed.html($translate.instant("S_SEVERITY")).html();
            $scope.selectDispositionTitle = parsed.html($translate.instant("S_DISPOSITION")).html();
            $scope.selectInspectionTitle = parsed.html($translate.instant("S_INSPECTION")).html();
            var selectDefectType = parsed.html($translate.instant("P_S_DEFECT_TYPE")).html();
            $scope.selectDefectTitle = parsed.html($translate.instant("S_DEFECT_TYPE")).html();
            vm.onSelectType = onSelectType;
            vm.newNcr = {
                id: null,
                ncrNumber: null,
                ncrType: null,
                title: null,
                description: null,
                reportedDate: null,
                reportedBy: null,
                qualityAnalyst: null,
                workflow: null,
                failureType: null,
                severity: null,
                status: "NONE",
                disposition: null
            };

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newNcr.ncrType = itemType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newNcr.ncrType != null && vm.newNcr.ncrType.numberSource != null) {
                    var source = vm.newNcr.ncrType.numberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newNcr.ncrNumber = data;
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
                QualityTypeService.getQualityTypeWorkflows(vm.newNcr.ncrType.id, 'QUALITY').then(
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
                ClassificationService.getObjectTypeAttributesWithHierarchy(vm.newNcr.ncrType.qualityType, vm.newNcr.ncrType.id, 0).then(
                    function (data) {
                        vm.qualityTypeAttributes = data.qualityTypeAttributes;
                        angular.forEach(vm.qualityTypeAttributes, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newNcr.ncrType.id,
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
                    angular.forEach(vm.ncrRequiredProperties, function (reqatt) {
                        vm.ncrProperties.push(reqatt);
                    })
                    angular.forEach(vm.ncrProperties, function (attribute) {
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
                        ncr: vm.newNcr,
                        ncrAttributes: vm.attributes,
                        objectAttributes: vm.ncrProperties
                    }
                    QualityTypeService.createQualityObject("NCR", qualityAttributeDto).then(
                        function (data) {
                            vm.newNcr = data.ncr;
                            saveCustomAttributes().then(
                                function (attr) {
                                    saveAttributes().then(
                                        function (attributes) {
                                            $scope.callback(vm.newNcr);
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(ncrCreated);
                                            vm.newNcr = {
                                                id: null,
                                                ncrNumber: null,
                                                ncrType: null,
                                                title: null,
                                                description: null,
                                                reportedDate: null,
                                                reportedBy: null,
                                                qualityAnalyst: null,
                                                workflow: null,
                                                failureType: null,
                                                severity: null,
                                                disposition: null
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
            }

            function validate() {
                var valid = true;
                if (vm.newNcr.ncrType == null || vm.newNcr.ncrType == "" || vm.newNcr.ncrType == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectNcrType);
                } else if (vm.newNcr.ncrNumber == null || vm.newNcr.ncrNumber == "" || vm.newNcr.ncrNumber == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(enterNcrNumber);
                } else if (vm.newNcr.title == null || vm.newNcr.title == "" || vm.newNcr.title == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(enterTitle);
                } else if (vm.newNcr.description == null || vm.newNcr.description == "" || vm.newNcr.description == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(enterDescription);
                } else if (vm.newNcr.qualityAnalyst == null || vm.newNcr.qualityAnalyst == "" || vm.newNcr.qualityAnalyst == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectQualityAnalyst);
                } else if (vm.newNcr.workflow == null || vm.newNcr.workflow == "" || vm.newNcr.workflow == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectWorkflowValid);
                } else if (vm.newNcr.failureType == null || vm.newNcr.failureType == "" || vm.newNcr.failureType == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectDefectType);
                } else if (vm.newNcr.severity == null || vm.newNcr.severity == "" || vm.newNcr.severity == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectSeverity);
                } else if (vm.newNcr.disposition == null || vm.newNcr.disposition == "" || vm.newNcr.disposition == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectDisposition);
                } else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if (vm.ncrRequiredProperties.length > 0 && !validateCustomAttributes()) {
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
                angular.forEach(vm.ncrRequiredProperties, function (attribute) {
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
                            ClassificationService.updateAttributeImageValue("NCRTYPE", vm.newNcr.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("NCRTYPE", vm.newNcr.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
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
                            ClassificationService.updateAttributeImageValue("NCR", vm.newNcr.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.customAttachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("NCR", vm.newNcr.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
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
                vm.qualityAnalysts = [];
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
                );
                var preference = $application.defaultValuesPreferences.get("DEFAULT_QUALITY_ANALYST_ROLE");
                if (preference != null && preference.defaultValueName != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.ncr.all";
                    QualityTypeService.getPersonByGroupNameAndPermission(groupName, permission).then(
                        function (data) {
                            vm.qualityAnalysts = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadNcrCustomProperties() {
                vm.ncrProperties = [];
                vm.ncrRequiredProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("QUALITY").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newNcr.id,
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
                                vm.ncrProperties.push(att);
                            } else {
                                vm.ncrRequiredProperties.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.loadRejectedInspections = loadRejectedInspections;
            function loadRejectedInspections() {
                $rootScope.showBusyIndicator($("#rightSidePanel"));
                InspectionService.getRejectedMaterialInspections().then(
                    function (data) {
                        vm.inspections = data;
                        if (vm.inspections.length == 0) {
                            $scope.selectInspectionTitle = parsed.html($translate.instant("NO_INSPECTIONS_TITLE")).html();
                        } else {
                            $scope.selectInspectionTitle = parsed.html($translate.instant("S_INSPECTION")).html();
                        }
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showWarningMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadPersons();
                loadNcrCustomProperties();
                loadRejectedInspections();
                $rootScope.$on('app.ncrs.new', create);
            })();
        }
    }
)
;
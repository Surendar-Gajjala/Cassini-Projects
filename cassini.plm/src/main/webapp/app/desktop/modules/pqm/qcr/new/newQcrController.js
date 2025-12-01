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
        'app/shared/services/core/qcrService',
        'app/shared/services/core/classificationService'
    ],
    function (module) {

        module.controller('NewQcrController', NewQcrController);

        function NewQcrController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, $application,
                                  AutonumberService, ObjectAttributeService, AttachmentService, AttributeAttachmentService,
                                  QcrService, LoginService, ItemService, ObjectTypeAttributeService, QualityTypeService, ClassificationService) {
            var vm = this;

            var parsed = angular.element("<div></div>");
            var selectQcrType = parsed.html($translate.instant("SELECT_QCR_TYPE")).html();
            var enterQcrNumber = parsed.html($translate.instant("ENTER_QCR_NUMBER")).html();
            var selectQcrFor = parsed.html($translate.instant("SELECT_QCR_FOR")).html();
            var enterTitle = parsed.html($translate.instant("ENTER_TITLE")).html();
            var selectQualityAdministrator = parsed.html($translate.instant("SELECT_QUALITY_ADMINISTRATOR")).html();
            var enterDescription = parsed.html($translate.instant("ENTER_DESCRIPTION")).html();
            var qcrCreated = parsed.html($translate.instant("QCR_CREATED")).html();
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            var selectWorkflowValid = parsed.html($translate.instant("WORKFLOW_SELECT_VALID")).html();
            $scope.selectQualityAdminTitle = parsed.html($translate.instant("S_QUALITY_ADMINISTRATOR")).html();
            $scope.selectWorkflowTitle = parsed.html($translate.instant("SELECT_WORKFLOW")).html();
            $scope.selectQCRForTitle = parsed.html($translate.instant("S_QCR_FOR")).html();

            vm.onSelectType = onSelectType;
            vm.newQcr = {
                id: null,
                qcrNumber: null,
                qcrType: null,
                qcrFor: null,
                title: null,
                description: null,
                qualityAdministrator: null,
                workflow: null,
                status: "NONE",
                workflowObject: null
            };
            vm.qcrForTypes = ["PR", "NCR"];

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newQcr.qcrType = itemType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newQcr.qcrType != null && vm.newQcr.qcrType.numberSource != null) {
                    var source = vm.newQcr.qcrType.numberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newQcr.qcrNumber = data;
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
                QualityTypeService.getQualityTypeWorkflows(vm.newQcr.qcrType.id, 'QUALITY').then(
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
                ClassificationService.getObjectTypeAttributesWithHierarchy(vm.newQcr.qcrType.qualityType, vm.newQcr.qcrType.id, 0).then(
                    function (data) {
                        vm.qualityTypeAttributes = data.qualityTypeAttributes;
                        angular.forEach(vm.qualityTypeAttributes, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newQcr.qcrType.id,
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
                        qcr: vm.newQcr,
                        qcrAttributes: vm.attributes,
                        objectAttributes: vm.qcrProperties
                    }
                    QualityTypeService.createQualityObject("QCR", qualityAttributeDto).then(
                        function (data) {
                            vm.newQcr = data.qcr;
                            saveCustomAttributes().then(
                                function (atts) {
                                    saveAttributes().then(
                                        function (attributes) {
                                            $scope.callback(vm.newQcr);
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(qcrCreated);
                                            vm.newQcr = {
                                                id: null,
                                                qcrNumber: null,
                                                qcrType: null,
                                                qcrFor: null,
                                                title: null,
                                                description: null,
                                                qualityAdministrator: null,
                                                workflow: null
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
                if (vm.newQcr.qcrType == null || vm.newQcr.qcrType == "" || vm.newQcr.qcrType == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectQcrType);
                } else if (vm.newQcr.qcrNumber == null || vm.newQcr.qcrNumber == "" || vm.newQcr.qcrNumber == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(enterQcrNumber);
                } else if (vm.newQcr.qcrFor == null || vm.newQcr.qcrFor == "" || vm.newQcr.qcrFor == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectQcrFor);
                } else if (vm.newQcr.title == null || vm.newQcr.title == "" || vm.newQcr.title == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(enterTitle);
                } else if (vm.newQcr.description == null || vm.newQcr.description == "" || vm.newQcr.description == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(enterDescription);
                } else if (vm.newQcr.qualityAdministrator == null || vm.newQcr.qualityAdministrator == "" || vm.newQcr.qualityAdministrator == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectQualityAdministrator);
                } else if (vm.newQcr.workflow == null || vm.newQcr.workflow == "" || vm.newQcr.workflow == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectWorkflowValid);
                } else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if (vm.qcrRequiredProperties.length > 0 && !validateCustomAttributes()) {
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
                            ClassificationService.updateAttributeImageValue("QCRTYPE", vm.newQcr.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("QCRTYPE", vm.newQcr.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
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
                            ClassificationService.updateAttributeImageValue("QCR", vm.newQcr.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.customAttachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("QCR", vm.newQcr.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
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
                )
                var preference = $application.defaultValuesPreferences.get("DEFAULT_QUALITY_ADMINISTRATOR_ROLE");
                if (preference != null && preference.defaultValueName != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.qcr.all";
                    QualityTypeService.getPersonByGroupNameAndPermission(groupName, permission).then(
                        function (data) {
                            vm.qualityAnalysts = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadQcrCustomProperties() {
                vm.qcrProperties = [];
                vm.qcrRequiredProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("QUALITY").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newQcr.id,
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
                //if ($application.homeLoaded == true) {
                loadPersons();
                loadQcrCustomProperties();
                $rootScope.$on('app.qcrs.new', create);
                //}
            })();
        }
    }
)
;
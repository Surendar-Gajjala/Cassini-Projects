define(
    [
        'app/desktop/modules/change/change.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/workflowDefinitionService',
        'app/desktop/modules/directives/changeTypeDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/shared/services/core/varianceService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'

    ],
    function (module) {

        module.controller('NewVarianceController', NewVarianceController);

        function NewVarianceController($sce, $scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $q,
                                       VarianceService, ECOService, CommonService, AutonumberService, WorkflowDefinitionService, $translate,
                                       LoginService, ObjectAttributeService, ItemService, ObjectTypeAttributeService, AttachmentService, AttributeAttachmentService) {

            var vm = this;

            var parsed = angular.element("<div></div>");

            vm.select = parsed.html($translate.instant("SELECT")).html();
            $scope.effectiveFromPlaceholder = parsed.html($translate.instant("EFFECTIVE_FROM_PLACEHOLDER")).html();
            $scope.effectiveToPlaceholder = parsed.html($translate.instant("EFFECTIVE_TO_PLACEHOLDER")).html();
            $scope.reasonForDeviation = parsed.html($translate.instant("ENTER_REASON_FOR_DEVIATION")).html();
            $scope.reasonForWaiver = parsed.html($translate.instant("ENTER_REASON_FOR_WAIVER")).html();

            vm.persons = [];
            vm.workflows = [];
            vm.attributes = [];
            vm.ecoProperties = [];
            vm.ecoRequiredProperties = [];
            vm.workflowObjectAttributes = [];
            vm.workflowReqObjectAttributes = [];
            vm.workflowAttributes = [];
            vm.workflowReqAttributes = [];
            vm.newVariance = {
                id: null,
                title: null,
                description: null,
                varianceType: null,
                varianceNumber: null,
                changeType: null,
                varianceFor: "ITEMS",
                status: "NONE",
                reasonForVariance: null,
                currentRequirement: null,
                requirementDeviation: null,
                originator: null,
                changeClass: null,
                effectivityType: "QUANTITY",
                effectiveFrom: null,
                effectiveTo: null,
                notes: null,
                ecoOwnerObject: $rootScope.localStorageLogin.login.person,
                workflowDefinition: null
            };
            vm.onSelectType = onSelectType;
            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newVariance.changeType = itemType;
                    vm.newVariance.changeClass = itemType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newVariance.changeType != null && vm.newVariance.changeType.autoNumberSource != null) {
                    var source = vm.newVariance.changeType.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newVariance.varianceNumber = data;
                            loadWorkflows();
                            loadAttributeDefs();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadWorkflows() {
                ECOService.getWorkflows(vm.newVariance.changeClass.id, 'CHANGES').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadEcoCustomProperties() {
                ObjectTypeAttributeService.getObjectTypeAttributesByType("CHANGE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newVariance.id,
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
                                vm.ecoProperties.push(att);
                            } else {
                                vm.ecoRequiredProperties.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            function loadAttributeDefs() {
                vm.attributes = [];
                ECOService.getEcoAttributesWithHierarchy(vm.newVariance.changeType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newVariance.changeType.id,
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
                    }
                )
            }

            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            var varianceAlreadyExist = $translate.instant("VARIANCE_ALREADY_EXIST");
            var deviationSuccess = $translate.instant("DEVIATION_SUCCESS");
            var waiverSuccess = $translate.instant("WAIVER_SUCCESS");

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    VarianceService.getVarianceByNumber(vm.newVariance.varianceNumber).then(
                        function (data) {
                            if (data != 0) {
                                $rootScope.showErrorMessage(varianceAlreadyExist);
                                $rootScope.hideBusyIndicator();
                            } else {
                                var workflowDefinition = vm.newVariance.workflowDefinition;
                                vm.newVariance.ecoOwner = vm.newVariance.ecoOwnerObject.id;
                                delete vm.newVariance['workflowDefinition'];
                                vm.validattributes = [];
                                vm.validObjectAttributes = [];
                                vm.validWorkflowAttributes = [];
                                angular.forEach(vm.attributes, function (attribute) {
                                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                                        if ($rootScope.checkAttribute(attribute)) {
                                            vm.validattributes.push(attribute);
                                        }
                                        else {
                                            $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                                        }
                                    } else {
                                        vm.validattributes.push(attribute);
                                    }
                                });
                                vm.objectAttributes = [];
                                if (vm.ecoProperties != null && vm.ecoProperties != undefined) {
                                    vm.objectAttributes = vm.objectAttributes.concat(vm.ecoProperties);
                                }
                                if (vm.ecoRequiredProperties != null && vm.ecoRequiredProperties != undefined) {
                                    vm.objectAttributes = vm.objectAttributes.concat(vm.ecoRequiredProperties);
                                }
                                angular.forEach(vm.objectAttributes, function (attribute) {
                                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                                        if ($rootScope.checkAttribute(attribute)) {
                                            vm.validObjectAttributes.push(attribute);
                                        }
                                        else {
                                            $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + propertyRequired);
                                        }
                                    } else {
                                        vm.validObjectAttributes.push(attribute);
                                    }
                                });

                                vm.workflowRequiredAttributes = [];
                                if (vm.workflowReqObjectAttributes != null && vm.workflowReqObjectAttributes != undefined) {
                                    vm.workflowRequiredAttributes = vm.workflowRequiredAttributes.concat(vm.workflowReqObjectAttributes);
                                }
                                if (vm.workflowReqAttributes != null && vm.workflowReqAttributes != undefined) {
                                    vm.workflowRequiredAttributes = vm.workflowRequiredAttributes.concat(vm.workflowReqAttributes);
                                }
                                angular.forEach(vm.workflowRequiredAttributes, function (attribute) {
                                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                                        if ($rootScope.checkAttribute(attribute)) {
                                            vm.validWorkflowAttributes.push(attribute);
                                        }
                                        else {
                                            $rootScope.showErrorMessage(attribute.attributeDef.name + ":" + propertyRequired);
                                        }
                                    } else {
                                        vm.validWorkflowAttributes.push(attribute);
                                    }
                                });
                                if ((vm.attributes.length == vm.validattributes.length) &&
                                    (vm.objectAttributes.length == vm.validObjectAttributes.length) &&
                                    (vm.validWorkflowAttributes.length == vm.workflowRequiredAttributes.length)) {
                                    vm.newVariance.originator = vm.newVariance.originator.id;
                                    if ($rootScope.varianceType == "Deviation") {
                                        vm.newVariance.varianceType = "DEVIATION";
                                        vm.newVariance.changeType = "DEVIATION";
                                    }
                                    if ($rootScope.varianceType == "Waiver") {
                                        vm.newVariance.varianceType = "WAIVER";
                                        vm.newVariance.changeType = "WAIVER";
                                        vm.newVariance.effectivityType = "QUANTITY";
                                    }
                                    VarianceService.createVariance(vm.newVariance).then(
                                        function (data) {
                                            vm.newVariance = data;
                                            VarianceService.attachWorkflow(data.id, workflowDefinition.id).then(
                                                function (workflow) {
                                                    vm.newVariance.workflowDefinition = workflow;
                                                    saveWorkflowProperties().then(
                                                        function (workflowProp) {
                                                            saveWorkflowAttributes().then(
                                                                function (workflowAtt) {
                                                                    saveEcoProperties().then(
                                                                        function (properties) {
                                                                            saveEcoAttributes().then(
                                                                                function (attributes) {
                                                                                    $rootScope.hideBusyIndicator();
                                                                                    $rootScope.hideSidePanel();
                                                                                    if ($rootScope.varianceType == "Deviation") {
                                                                                        $rootScope.showSuccessMessage(deviationSuccess);
                                                                                    } else if ($rootScope.varianceType == "Waiver") {
                                                                                        $rootScope.showSuccessMessage(waiverSuccess);
                                                                                    }
                                                                                    $scope.callback(vm.newVariance);
                                                                                }, function (error) {
                                                                                    $rootScope.hideBusyIndicator();
                                                                                    $rootScope.showErrorMessage(error.message);
                                                                                }
                                                                            )
                                                                        }, function (error) {
                                                                            $rootScope.hideBusyIndicator();
                                                                            $rootScope.showErrorMessage(error.message);
                                                                        }
                                                                    );
                                                                }, function (error) {
                                                                    $rootScope.hideBusyIndicator();
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        }, function (error) {
                                                            $rootScope.hideBusyIndicator();
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                }
                                            )
                                        }, function (error) {
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                } else {
                                    $rootScope.hideBusyIndicator();
                                }
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        });
                }
            }

            function saveWorkflowProperties() {
                var defered = $q.defer();
                angular.forEach(vm.workflowReqObjectAttributes, function (reqAtt) {
                    vm.workflowObjectAttributes.push(reqAtt);
                });
                if (vm.workflowObjectAttributes.length > 0) {
                    vm.workflowImageAttributes = [];
                    var workflowPropertyImages = new Hashtable();
                    angular.forEach(vm.workflowObjectAttributes, function (attribute) {
                        attribute.id.objectId = vm.newVariance.workflowDefinition.id;
                        /* if (attribute.timeValue != null) {
                         attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                         }*/
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            workflowPropertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.workflowImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addWorkflowAttachment(attribute);
                        }
                    });
                    $timeout(function () {
                        ObjectAttributeService.saveItemObjectAttributes(vm.newVariance.workflowDefinition.id, vm.workflowObjectAttributes).then(
                            function (data) {
                                if (vm.workflowImageAttributes.length > 0) {
                                    angular.forEach(vm.workflowImageAttributes, function (propImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, workflowPropertyImages.get(propImgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                defered.reject();
                            }
                        )
                    }, 3000);
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function addWorkflowAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'PLMWORKFLOW', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }
                    )
                })

                return propertyAttachmentIds;
            }

            function addWorkflowAttributeAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'PLMWORKFLOW', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }
                    )
                })

                return propertyAttachmentIds;
            }

            function saveWorkflowAttributes() {
                var defered = $q.defer();
                vm.workflowImageAttributes = [];
                var images = new Hashtable();
                angular.forEach(vm.workflowReqAttributes, function (reqatt) {
                    vm.workflowAttributes.push(reqatt);
                });

                if (vm.workflowAttributes.length > 0) {
                    angular.forEach(vm.workflowAttributes, function (attribute) {
                        attribute.id.objectId = vm.newVariance.workflowDefinition.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        /*if (attribute.timeValue != null) {
                         attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                         }*/
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addWorkflowAttributeAttachment(attribute);
                        }
                    });

                    $timeout(function () {
                        WorkflowDefinitionService.saveWorkflowTypeAttributes(vm.newVariance.workflowDefinition.id, vm.workflowAttributes).then(
                            function (data) {
                                if (vm.workflowImageAttributes.length > 0) {
                                    angular.forEach(vm.workflowImageAttributes, function (imgAtt) {
                                        WorkflowDefinitionService.updateWorkflowImageValue(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                            })
                                    })
                                } else {
                                    defered.resolve();
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                defered.reject();
                            })
                    }, 2000)

                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            $scope.setImageValue = function (attribute, files) {
                attribute.imageValue = files[0];
            }

            function saveEcoAttributes() {
                var defered = $q.defer();
                vm.imageAttributes = [];
                var images = new Hashtable();
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        attribute.id.objectId = vm.newVariance.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        /* if (attribute.timeValue != null) {
                         attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                         }*/
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addAttachment(attribute);
                        }
                    });

                    $timeout(function () {
                        ECOService.saveECOAttributes(vm.newVariance.id, vm.attributes).then(
                            function (data) {
                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        VarianceService.updateVarianceImageValue(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                            })
                                    })
                                } else {
                                    defered.resolve();
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                defered.reject();
                            })
                    }, 2000)

                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function addAttachment(attribute) {
                var attachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'CHANGE', attachmentFile).then(
                        function (data) {
                            attachmentIds.push(data[0].id);
                        }
                    )
                });

                return attachmentIds;
            }

            function saveEcoProperties() {
                var defered = $q.defer();
                angular.forEach(vm.ecoRequiredProperties, function (reqatt) {
                    vm.ecoProperties.push(reqatt);
                });
                if (vm.ecoProperties.length > 0) {
                    vm.propertyImageAttributes = [];
                    var propertyImages = new Hashtable();
                    angular.forEach(vm.ecoProperties, function (attribute) {
                        attribute.id.objectId = vm.newVariance.id;
                        /*if (attribute.timeValue != null) {
                         attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                         }*/

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            propertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addEcoPropertyAttachment(attribute);
                        }
                    });
                    $timeout(function () {
                        ObjectAttributeService.saveItemObjectAttributes(vm.newVariance.id, vm.ecoProperties).then(
                            function (data) {
                                if (vm.propertyImageAttributes.length > 0) {
                                    angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                defered.reject();
                            }
                        )
                    }, 3000);
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function addEcoPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'CHANGE', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }
                    )
                })

                return propertyAttachmentIds;
            }

            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };

            function loadPersons() {
                LoginService.getActiveLogins().then(
                    function (data) {
                        angular.forEach(data, function (person) {
                            if (!person.external) {
                                vm.persons.push(person.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            var ecoNumberValidation = $translate.instant("ECO_NUMBER_VALIDATION");
            var titleValidation = $translate.instant("TITLE_VALIDATION");
            var reasonForChangeValidation = $translate.instant("REASONFOR_CHANGE_VALIDATION");
            var typeValidation = parsed.html($translate.instant("SPECIFICATION_TYPE_VALIDATION")).html();
            var workflowValidation = $translate.instant("WORKFLOW_VALIDATION");
            var descriptionValidation = $translate.instant("DESCRIPTION_VALIDATION");
            var currentReqValidation = $translate.instant("CURRENT_REQUIREMENT_VALIDATION");
            var reqDeviationValidation = $translate.instant("REQUIREMENT_DEVIATION_VALIDATION");
            var originatorValidation = $translate.instant("ORIGINATOR_VALIDATION");
            var effectiveTypeValidation = $translate.instant("EFFECTIVE_TYPE_VALIDATION");
            var deviationForValidation = $translate.instant("DEVIATION_FOR_VALIDATION");
            var waiverForValidation = $translate.instant("WAIVER_FOR_VALIDATION");


            function validate() {
                var valid = true;
                if (vm.newVariance.changeType == null || vm.newVariance.changeType == undefined ||
                    vm.newVariance.changeType == "") {
                    $rootScope.showWarningMessage(typeValidation);
                    valid = false;
                } else if (vm.newVariance.varianceNumber == null || vm.newVariance.varianceNumber == undefined ||
                    vm.newVariance.varianceNumber == "") {
                    $rootScope.showWarningMessage(ecoNumberValidation);
                    valid = false;
                } else if (vm.newVariance.varianceFor == null || vm.newVariance.varianceFor == undefined ||
                    vm.newVariance.varianceFor == "" && $rootScope.varianceType != "Deviation") {
                    $rootScope.showWarningMessage(deviationForValidation);
                    valid = false;
                } else if (vm.newVariance.varianceFor == null || vm.newVariance.varianceFor == undefined ||
                    vm.newVariance.varianceFor == "" && $rootScope.varianceType != "Waiver") {
                    $rootScope.showWarningMessage(waiverForValidation);
                    valid = false;
                } else if (vm.newVariance.title == null || vm.newVariance.title == undefined ||
                    vm.newVariance.title == "") {
                    $rootScope.showWarningMessage(titleValidation);
                    valid = false;
                } else if (vm.newVariance.description == null || vm.newVariance.description == undefined ||
                    vm.newVariance.description == "") {
                    $rootScope.showWarningMessage(descriptionValidation);
                    valid = false;
                } else if (vm.newVariance.reasonForVariance == null || vm.newVariance.reasonForVariance == undefined ||
                    vm.newVariance.reasonForVariance == "") {
                    $rootScope.showWarningMessage(reasonForChangeValidation);
                    valid = false;
                } else if (vm.newVariance.currentRequirement == null || vm.newVariance.currentRequirement == undefined ||
                    vm.newVariance.currentRequirement == "") {
                    $rootScope.showWarningMessage(currentReqValidation);
                    valid = false;
                } else if (vm.newVariance.requirementDeviation == null || vm.newVariance.requirementDeviation == undefined ||
                    vm.newVariance.requirementDeviation == "") {
                    $rootScope.showWarningMessage(reqDeviationValidation);
                    valid = false;
                } else if (vm.newVariance.originator == null || vm.newVariance.originator == undefined ||
                    vm.newVariance.originator == "") {
                    $rootScope.showWarningMessage(originatorValidation);
                    valid = false;
                } else if ((vm.newVariance.effectivityType == null || vm.newVariance.effectivityType == undefined ||
                    vm.newVariance.effectivityType == "") && $rootScope.varianceType != "Waiver") {
                    $rootScope.showWarningMessage(effectiveTypeValidation);
                    valid = false;
                } else if (vm.newVariance.workflowDefinition == null || vm.newVariance.workflowDefinition == undefined ||
                    vm.newVariance.workflowDefinition == "") {
                    $rootScope.showWarningMessage(workflowValidation);
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }

                return valid;
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.$on('app.variance.new', create);
                loadEcoCustomProperties();
                loadPersons();
                //}
            })();
        }
    }
)
;
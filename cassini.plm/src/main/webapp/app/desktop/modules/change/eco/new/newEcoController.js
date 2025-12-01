define(
    [
        'app/desktop/modules/change/change.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/qualityTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/workflowService',
        'app/desktop/modules/change/eco/directive/changeDirective',
        'app/desktop/modules/directives/changeTypeDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService'
    ],
    function (module) {
        module.controller('NewECOController', NewECOController);

        function NewECOController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $q,
                                  ECOService, CommonService, AutonumberService, WorkflowDefinitionService, $translate, LoginService,
                                  ObjectAttributeService, QualityTypeService, ObjectTypeAttributeService, WorkflowService, AttributeAttachmentService) {

            var vm = this;
            $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
            vm.newECO = {
                id: null,
                ecoType: null,
                ecoNumber: null,
                title: null,
                description: null,
                reasonForChange: null,
                ecoOwner: null,
                ecoOwnerObject: null,
                status: 'NONE',
                workflow: null,
                revisionCreationType: "WORKFLOW_START",
                workflowStatus: null,
                workflowDefinition: null,
                changeClass: null
            };
            vm.workflow = {
                id: null
            };
            vm.persons = [];
            vm.workflows = [];
            vm.attributes = [];
            vm.ecoProperties = [];
            vm.ecoRequiredProperties = [];
            vm.workflowObjectAttributes = [];
            vm.workflowReqObjectAttributes = [];
            vm.workflowAttributes = [];
            vm.workflowReqAttributes = [];
            vm.createECO = createECO;
            vm.autoNumber = autoNumber;
            vm.onSelectWorkflow = onSelectWorkflow;
            vm.onSelectType = onSelectType;

            var parsed = angular.element("<div></div>");
            vm.selectWorkflow = parsed.html($translate.instant("SELECT")).html();

            var ecoAlreadyExist = $translate.instant("ECO_ALREADY_EXIST");
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            var propertyRequired = $translate.instant("PROPERTY_REQUIRED");
            var ecoCreatedMessage = $translate.instant("ECO_CREATED_MESSAGE");
            var ecoNumberValidation = $translate.instant("ECO_NUMBER_VALIDATION");
            var changeAnalystValidation = $translate.instant("CHANGE_ANALYST_VALIDATION");
            var ecoTitleValidation = $translate.instant("ECO_TITLE_VALIDATION");
            var descriptionValidation = $translate.instant("ECO_DESCRIPTION_VALIDATION");
            var reasonForChangeValidation = $translate.instant("ECO_REASONFOR_CHANGE_VALIDATION");
            var typeValidation = parsed.html($translate.instant("SPECIFICATION_TYPE_VALIDATION")).html();
            var workflowValidation = $translate.instant("ECO_WORKFLOW_VALIDATION");
            var selectRevisionCreationType = $translate.instant("SELECT_REVISION_CREATION_TYPE");
            $scope.selectWorkflowActivityTitle = $translate.instant("S_WORKFLOW_ACTIVITY");
            var selectWorkflowActivity = $translate.instant("SELECT_WORKFLOW_ACTIVITY");
            $scope.enterReasonForChange = parsed.html($translate.instant("ENTER_REASON_FOR_CHANGE")).html();
            $scope.select = parsed.html($translate.instant("SELECT")).html();


            var pageable = {
                page: 0,
                size: 30,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.choices = [
                {
                    name: "True",
                    value: true
                },
                {
                    name: "False",
                    value: false
                }
            ];
            var ecoAutoNumberSource = null;
            vm.changeType = null;

            function onSelectType(type) {
                if (type != null && type != undefined) {
                    vm.changeType = type;
                    vm.newECO.ecoType = type;
                    vm.newECO.changeClass = type;
                    autoNumber();
                }

            }

            function loadEcoCustomProperties() {
                ObjectTypeAttributeService.getObjectTypeAttributesByType("CHANGE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newECO.id,
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
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadPersons() {
                vm.changeAnalysts = [];
                var preference = $application.defaultValuesPreferences.get("DEFAULT_CHANGE_ANALYST_ROLE");
                if (preference != null && preference.defaultValueName != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.change.eco.all";
                    QualityTypeService.getPersonByGroupNameAndPermission(groupName, permission).then(
                        function (data) {
                            vm.changeAnalysts = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }


            function loadWorkflows() {
                ECOService.getWorkflows(vm.newECO.ecoType.id, 'CHANGES').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function autoNumber() {
                if (vm.newECO.ecoType != null && vm.newECO.ecoType != undefined) {
                    AutonumberService.getNextNumberByName(vm.newECO.ecoType.autoNumberSource.name).then(
                        function (data) {
                            vm.newECO.ecoNumber = data;
                            loadWorkflows();
                            loadAttributeDefs();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadAttributeDefs() {
                vm.attributes = [];
                ECOService.getEcoAttributesWithHierarchy(vm.changeType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.changeType.id,
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

            function onSelectWorkflow(workflow) {
                vm.workflowAttributes = [];
                vm.workflowReqAttributes = [];
                vm.workflowObjectAttributes = [];
                vm.workflowReqObjectAttributes = [];
                vm.newECO.workflowDefinition = workflow;
                vm.newECO.workflowStatus = null;
                if (vm.newECO.revisionCreationType == "ACTIVITY_COMPLETION") {
                    getNormalWorkflowStatuses();
                } else {
                    getNormalWorkflowStatuses();
                }
                /*ObjectTypeAttributeService.getObjectTypeAttributesByType("WORKFLOW").then(
                 function (data) {
                 angular.forEach(data, function (attribute) {
                 var att = {
                 id: {
                 objectId: vm.newECO.workflow,
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

                 if (attribute.dataType == "TIMESTAMP") {
                 att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                 }
                 if (attribute.required == false) {
                 vm.workflowObjectAttributes.push(att);
                 } else {
                 vm.workflowReqObjectAttributes.push(att);
                 }
                 if (attribute.lov != null) {
                 att.lovValues = attribute.lov.values;
                 }
                 }
                 );
                 return WorkflowDefinitionService.getWorkflowAttributesWithHierarchy(vm.newECO.workflowDefinition.workflowType.id);
                 }, function (error) {
                 $rootScope.showErrorMessage(error.message);
                 }
                 ).then(
                 function (data) {
                 vm.selectedWorkflowAttributes = data;
                 angular.forEach(vm.selectedWorkflowAttributes, function (attribute) {
                 var att = {
                 id: {
                 objectId: vm.newECO.workflow,
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
                 if (attribute.dataType == "TIMESTAMP") {
                 att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                 }
                 if (attribute.lov != null) {
                 att.lovValues = attribute.lov.values;
                 }
                 if (attribute.required == false) {
                 vm.workflowAttributes.push(att);
                 } else {
                 vm.workflowReqAttributes.push(att);
                 }
                 })
                 }, function (error) {
                 $rootScope.showErrorMessage(error.message);
                 }
                 );*/
            }

            function createECO() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    ECOService.getECOByNumber(vm.newECO.ecoNumber).then(
                        function (data) {
                            if (data != 0) {
                                $rootScope.showErrorMessage(ecoAlreadyExist);
                                $rootScope.hideBusyIndicator();
                            } else {
                                var workflowDefinition = vm.newECO.workflowDefinition;
                                vm.newECO.ecoOwner = vm.newECO.ecoOwnerObject.id;
                                delete vm.newECO['workflowDefinition'];
                                vm.newECO.ecoType = vm.changeType.id;
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
                                    ECOService.createECO(vm.newECO).then(
                                        function (data) {
                                            vm.newECO = data;
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
                                                                            $rootScope.showSuccessMessage(ecoCreatedMessage);
                                                                            $scope.callback(vm.newECO);
                                                                        }
                                                                    )
                                                                }
                                                            );
                                                        }
                                                    )
                                                }
                                            )
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                } else {
                                    $rootScope.hideBusyIndicator();
                                }
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        })
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
                        attribute.id.objectId = vm.newECO.workflow;
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
                        ObjectAttributeService.saveItemObjectAttributes(vm.newECO.workflow, vm.workflowObjectAttributes).then(
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
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
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
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
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
                        attribute.id.objectId = vm.newECO.workflow;

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
                        WorkflowDefinitionService.saveWorkflowTypeAttributes(vm.newECO.workflow, vm.workflowAttributes).then(
                            function (data) {
                                if (vm.workflowImageAttributes.length > 0) {
                                    angular.forEach(vm.workflowImageAttributes, function (imgAtt) {
                                        WorkflowDefinitionService.updateWorkflowImageValue(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
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
                        attribute.id.objectId = vm.newECO.id;

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
                        ECOService.saveECOAttributes(vm.newECO.id, vm.attributes).then(
                            function (data) {
                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        ECOService.updateEcoImageValue(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
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
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
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
                        attribute.id.objectId = vm.newECO.id;
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
                        ObjectAttributeService.saveItemObjectAttributes(vm.newECO.id, vm.ecoProperties).then(
                            function (data) {
                                if (vm.propertyImageAttributes.length > 0) {
                                    angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
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

            function addEcoPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'CHANGE', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                })

                return propertyAttachmentIds;
            }


            function validate() {
                var valid = true;

                if (vm.newECO.ecoType == null || vm.newECO.ecoType == undefined ||
                    vm.newECO.ecoType == "") {
                    $rootScope.showWarningMessage(typeValidation);
                    valid = false;
                }
                else if (vm.newECO.ecoNumber == null || vm.newECO.ecoNumber == undefined ||
                    vm.newECO.ecoNumber == "") {
                    $rootScope.showWarningMessage(ecoNumberValidation);
                    valid = false;
                }
                else if (vm.newECO.ecoOwnerObject == null || vm.newECO.ecoOwnerObject == undefined ||
                    vm.newECO.ecoOwnerObject == "") {
                    $rootScope.showWarningMessage(changeAnalystValidation);
                    valid = false;
                }

                else if (vm.newECO.title == null || vm.newECO.title == undefined ||
                    vm.newECO.title == "") {
                    $rootScope.showWarningMessage(ecoTitleValidation);
                    valid = false;
                }
                else if (vm.newECO.reasonForChange == null || vm.newECO.reasonForChange == undefined ||
                    vm.newECO.reasonForChange == "") {
                    $rootScope.showWarningMessage(reasonForChangeValidation);
                    valid = false;
                }
                else if (vm.newECO.workflow == null || vm.newECO.workflow == undefined ||
                    vm.newECO.workflow == "") {
                    $rootScope.showWarningMessage(workflowValidation);
                    valid = false;
                } else if (vm.newECO.revisionCreationType == null || vm.newECO.revisionCreationType == undefined ||
                    vm.newECO.revisionCreationType == "") {
                    $rootScope.showWarningMessage(selectRevisionCreationType);
                    valid = false;
                } else if (vm.newECO.revisionCreationType == "ACTIVITY_COMPLETION" && (vm.newECO.workflowStatus == null || vm.newECO.workflowStatus == undefined ||
                    vm.newECO.workflowStatus == "")) {
                    $rootScope.showWarningMessage(selectWorkflowActivity);
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }

                return valid;
            }

            vm.selectRevisionCreationRule = selectRevisionCreationRule;
            function selectRevisionCreationRule(value) {
                if (value == 'workflowStart') {
                    vm.newECO.revisionCreationType = "WORKFLOW_START";
                    vm.newECO.workflowStatus = null;
                }
                if (value == 'activityCompletion') {
                    vm.newECO.workflowStatus = null;
                    vm.newECO.revisionCreationType = "ACTIVITY_COMPLETION";
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    getNormalWorkflowStatuses();
                }
                $scope.$evalAsync();

            }

            vm.workflowStatuses = [];
            function getNormalWorkflowStatuses() {
                WorkflowDefinitionService.getNormalWorkflowStatuses(vm.newECO.workflow).then(
                    function (data) {
                        vm.workflowStatuses = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadEcoCustomProperties();
                loadPersons();
                /*        loadECOType();*/
                $rootScope.$on('app.changes.ecos.new', createECO);
                //}
            })();
        }
    }
)
;

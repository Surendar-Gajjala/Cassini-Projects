define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/qcrService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/desktop/modules/directives/changeTypeDirective',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/customerSupplierService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'
    ],
    function (module) {

        module.controller('NewEcrController', NewEcrController);

        function NewEcrController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, $application, AutonumberService,
                                  LoginService, ObjectTypeAttributeService, ECOService, DCOService, QcrService, QualityTypeService, CustomerSupplierService, LovService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var ecrCreated = parsed.html($translate.instant("ECR_CREATED")).html();
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            var selectOriginator = $translate.instant("ORIGINATOR_VALIDATION");
            var selectRequestedBy = $translate.instant("SELECT_REQUESTED_BY");
            var enterTitle = $translate.instant("ENTER_TITLE");
            var selectEcrType = $translate.instant("SELECT_CHANGE_TYPE");
            var enterEcrNumber = $translate.instant("ENTER_ECR_NUMBER");
            var selectChangeReason = $translate.instant("SELECT_CHANGE_REASON");
            var selectWorkflow = $translate.instant("WORKFLOW_SELECT_VALID");
            var selectUrgency = $translate.instant("P_S_URGENCY");
            var selectChangeAnalyst = $translate.instant("SELECT_CHANGE_ANALYST");
            $scope.selectWorkflowTitle = parsed.html($translate.instant("SELECT_WORKFLOW_TITLE")).html();
            $scope.select = parsed.html($translate.instant("SELECT")).html();
            $scope.enterDescriptionofChange = parsed.html($translate.instant("ENTER_DESCRIPTION_OF_CHANGE")).html();
            $scope.enterReasonForChange = parsed.html($translate.instant("ENTER_REASON_FOR_CHANGE")).html();
            $scope.enterPraposedChanges = parsed.html($translate.instant("ENTER_PROPOSED_CHANGES")).html();
            $scope.selectCustomerTitle = parsed.html($translate.instant("SELECT_CUSTOMER_TITLE")).html();
            $scope.enterOtherRequested = parsed.html($translate.instant("ENTER_OTHER_REQUESTED")).html();
            var pleaseEnterOtherRequested = parsed.html($translate.instant("P_E_OTHER_REQUESTED")).html();

            vm.onSelectType = onSelectType;
            vm.newEcr = {
                id: null,
                crType: null,
                crNumber: null,
                title: null,
                descriptionOfChange: null,
                reasonForChange: null,
                proposedChanges: null,
                originator: null,
                requestedBy: null,
                status: "NONE",
                changeAnalyst: null,
                urgency: null,
                impactAnalysis: null,
                rejectionReason: null,
                changeReasonType: null,
                notes: null,
                workflow: null,
                qcr: null,
                changeClass: null,
                requesterType: "INTERNAL",
                otherRequested: null
            };
            vm.workflows = [];
            //vm.urgencys = ["CRITICAL", "HIGH", "MEDIUM", "LOW"];
            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newEcr.crType = itemType.id;
                    vm.selectedEcrType = itemType;
                    vm.newEcr.changeClass = itemType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.selectedEcrType != null && vm.selectedEcrType.autoNumberSource != null) {
                    var source = vm.selectedEcrType.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newEcr.crNumber = data;
                            loadAttributeDefs();
                            loadWorkflows();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadWorkflows() {
                ECOService.getWorkflows(vm.newEcr.crType, 'CHANGES').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadAttributeDefs() {
                vm.attributes = [];
                ECOService.getEcoAttributesWithHierarchy(vm.newEcr.crType).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newEcr.id,
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
                    angular.forEach(vm.ecrProperties, function (attribute) {
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
                    if (vm.newEcr.requestedBy === 0) {
                        vm.newEcr.requestedBy = null;
                    }
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    var changeAttributeDto = {
                        plmecr: vm.newEcr,
                        changeAttributes: vm.attributes,
                        objectAttributes: vm.ecrProperties
                    }
                    DCOService.createDCOObject("ECR", changeAttributeDto).then(
                        function (data) {
                            vm.newEcr = data.plmecr;
                            saveCustomAttributes().then(
                                function (attr) {
                                    saveAttributes().then(
                                        function (attributes) {
                                            $scope.callback(vm.newEcr);
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(ecrCreated);
                                            vm.newEcr = {
                                                id: null,
                                                crNumber: null,
                                                crType: null,
                                                title: null,
                                                description: null,
                                                reportedDate: null,
                                                reportedBy: null,
                                                qualityAnalyst: null,
                                                qualityAnalystObject: null,
                                                workflow: null,
                                                workflowObject: null,
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
                if (vm.newEcr.crType == null || vm.newEcr.crType == undefined || vm.newEcr.crType == "") {
                    $rootScope.showWarningMessage(selectEcrType);
                    valid = false;
                } else if (vm.newEcr.crNumber == null || vm.newEcr.crNumber == undefined || vm.newEcr.crNumber == "") {
                    $rootScope.showWarningMessage(enterEcrNumber);
                    valid = false;
                } else if (vm.newEcr.title == null || vm.newEcr.title == undefined || vm.newEcr.title == "") {
                    $rootScope.showWarningMessage(enterTitle);
                    valid = false;
                } else if (vm.newEcr.changeReasonType == null || vm.newEcr.changeReasonType == undefined || vm.newEcr.changeReasonType == "") {
                    $rootScope.showWarningMessage(selectChangeReason);
                    valid = false;
                } else if (vm.newEcr.originator == null || vm.newEcr.originator == "" || vm.newEcr.originator == undefined) {
                    $rootScope.showWarningMessage(selectOriginator);
                    valid = false;
                } else if ((vm.newEcr.requestedBy != 0) && (vm.newEcr.requestedBy == null || vm.newEcr.requestedBy == "" || vm.newEcr.requestedBy == undefined)) {
                    $rootScope.showWarningMessage(selectRequestedBy);
                    valid = false;
                } else if ((vm.newEcr.requestedBy == 0) && (vm.newEcr.otherRequested == null || vm.newEcr.otherRequested == "" || vm.newEcr.otherRequested == undefined)) {
                    $rootScope.showWarningMessage(pleaseEnterOtherRequested);
                    valid = false;
                } else if (vm.newEcr.changeAnalyst == null || vm.newEcr.changeAnalyst == "" || vm.newEcr.changeAnalyst == undefined) {
                    $rootScope.showWarningMessage(selectChangeAnalyst);
                    valid = false;
                } else if (vm.newEcr.urgency == null || vm.newEcr.urgency == "" || vm.newEcr.urgency == undefined) {
                    $rootScope.showWarningMessage(selectUrgency);
                    valid = false;
                } else if (vm.newEcr.workflow == null || vm.newEcr.workflow == "" || vm.newEcr.workflow == undefined) {
                    $rootScope.showWarningMessage(selectWorkflow);
                    valid = false;
                } else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if (vm.ecrProperties.length > 0 && !validateCustomAttributes()) {
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
                angular.forEach(vm.ecrProperties, function (attribute) {
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
                            DCOService.updateAttributeImageValue("ECRTYPE", vm.newEcr.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            DCOService.updateAttributeAttachmentValues("ECRTYPE", vm.newEcr.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
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
                            DCOService.updateAttributeImageValue("ECR", vm.newEcr.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.customAttachmentAttributes, function (imgAtt) {
                            DCOService.updateAttributeAttachmentValues("ECR", vm.newEcr.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
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
                vm.originators = [];
                vm.changeAnalysts = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                                vm.originators.push(login.person);
                            }
                        });
                        vm.persons.push({id: 0, fullName: "Other"});
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
                var preference = $application.defaultValuesPreferences.get("DEFAULT_CHANGE_ANALYST_ROLE");
                if (preference != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.change.ecr.all";
                    QualityTypeService.getPersonByGroupNameAndPermission(groupName, permission).then(
                        function (data) {
                            vm.changeAnalysts = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadEcrCustomProperties() {
                vm.ecrProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("CHANGE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newEcr.id,
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
                            vm.ecrProperties.push(att);
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadQCRs() {
                QcrService.getReleasedByQcrFor("PR").then(
                    function (data) {
                        vm.qcrs = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.selectRequestedType = selectRequestedType;
            function selectRequestedType(value) {
                if (value == 'customer') {
                    vm.newEcr.requestedBy = null;
                    vm.newEcr.requesterType = "CUSTOMER"
                }
                if (value == 'internal') {
                    vm.newEcr.requestedBy = null;
                    vm.newEcr.requesterType = "INTERNAL";
                }
                if (value == 'others') {
                    vm.newEcr.requestedBy = null;
                    vm.newEcr.requesterType = "OTHER";
                }
                $scope.$evalAsync();

            }

            function loadCustomers() {
                CustomerSupplierService.getCustomers().then(
                    function (data) {
                        vm.customers = data;
                        vm.customers.push({id: 0, name: "Other"});
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadChangeRequestUrgency() {
                vm.urgencys = [];
                var preference = $application.defaultValuesPreferences.get("DEFAULT_CHANGE_REQUEST_URGENCY");
                var name = preference.defaultValueName;
                LovService.getLovByName(name).then(
                    function (data) {
                        if (data != null && data != "") {
                            vm.newEcr.urgency = data.defaultValue;
                            vm.urgencys = data.values;
                        }
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadChangeRequestUrgency();
                loadPersons();
                loadEcrCustomProperties();
                loadQCRs();
                loadCustomers();
                $rootScope.$on('app.ecrs.new', create);
            })();
        }
    }
)
;
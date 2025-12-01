define(
    [
        'app/desktop/modules/change/change.module',
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
        'app/shared/services/core/dcrService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/qualityTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'
    ],
    function (module) {

        module.controller('NewDCRController', NewDCRController);

        function NewDCRController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, $application, CommonService,
                                  AutonumberService, DCRService, DCOService, ECOService, LoginService, ObjectTypeAttributeService,
                                  QualityTypeService, LovService) {

            var vm = this;


            var parsed = angular.element("<div></div>");

            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var titleValidation = parsed.html($translate.instant("TITLE_VALIDATION")).html();
            var selectChangeReason = $translate.instant("SELECT_CHANGE_REASON");
            var workflowValidation = parsed.html($translate.instant("WORKFLOW_VALIDATION")).html();
            var reasonforchange = parsed.html($translate.instant("REASON_FOR_CHANGE_VALIDATION")).html();
            var dcrNumberValidation = parsed.html($translate.instant("DCR_NUMBER_VALIDATION")).html();
            var dcrTypeValidation = parsed.html($translate.instant("DCR_TYPE_VALIDATION")).html();
            var originatorValidation = parsed.html($translate.instant("DCR_ORIGINATOR_VALIDATION")).html();
            var requestedByValidation = parsed.html($translate.instant("DCR_REQUESTED_BY_VALIDATION")).html();
            var dcrCreatedmsg = parsed.html($translate.instant("DCR_CREATED_MSG")).html();
            var selectUrgency = $translate.instant("P_S_URGENCY");
            $scope.select = parsed.html($translate.instant("SELECT")).html();
            $scope.selectWorkflow = parsed.html($translate.instant("SELECT_WORKFLOW")).html();
            $scope.enterReasonForChange = parsed.html($translate.instant("ENTER_REASON_FOR_CHANGE")).html();
            $scope.enterPraposedChanges = parsed.html($translate.instant("ENTER_PROPOSED_CHANGES")).html();
            $scope.enterDescriptionofChange = parsed.html($translate.instant("ENTER_DESCRIPTION_OF_CHANGE")).html();


            vm.onSelectType = onSelectType;
            vm.newDCR = {
                id: null,
                crType: null,
                crNumber: null,
                title: null,
                descriptionOfChange: null,
                reasonForChange: null,
                proposedChanges: null,
                changeReasonType: null,
                originator: null,
                requestedBy: null,
                status: "NONE",
                requestedByObject: null,
                originatorObject: null,
                ecoOwner: null,
                workflow: null,
                urgency: null,
                dcrOwnerObject: $rootScope.localStorageLogin.login.person,
                changeClass: null
            };


            //vm.urgencys = ["CRITICAL", "HIGH", "MEDIUM", "LOW"];

            function onSelectType(changeType) {
                if (changeType != null && changeType != undefined) {
                    vm.newDCR.crType = changeType;
                    vm.newDCR.changeClass = changeType;
                    vm.selectedDcrType = changeType;
                    vm.type = changeType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newDCR.crType != null && vm.newDCR.crType.autoNumberSource != null) {
                    var source = vm.newDCR.crType.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newDCR.crNumber = data;
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
                ECOService.getWorkflows(vm.newDCR.crType.id, 'CHANGES').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newDCR.crType = vm.type.id;
                    vm.newDCR.changeAnalyst = vm.newDCR.dcrOwnerObject.id;
                    if (vm.newDCR.originatorObject != null) {
                        vm.newDCR.originator = vm.newDCR.originatorObject.id;
                    }
                    if (vm.newDCR.requestedByObject != null) {
                        vm.newDCR.requestedBy = vm.newDCR.requestedByObject.id;
                    }
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
                    angular.forEach(vm.dcrRequiredProperties, function (reqatt) {
                        vm.dcrProperties.push(reqatt);
                    })
                    angular.forEach(vm.dcrProperties, function (attribute) {
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
                    var changeAttributeDto = {
                        plmdcr: vm.newDCR,
                        changeAttributes: vm.attributes,
                        objectAttributes: vm.dcrProperties
                    };
                    DCOService.createDCOObject("DCR", changeAttributeDto).then(
                        function (data) {
                            vm.newDCR = data.plmdcr;
                            saveCustomAttributes().then(
                                function (atts) {
                                    saveAttributes().then(
                                        function (attributes) {
                                            $scope.callback(vm.newDCR);
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(dcrCreatedmsg);
                                            vm.newDCR = {
                                                id: null,
                                                crType: null,
                                                crNumber: null,
                                                title: null,
                                                descriptionOfChange: null,
                                                reasonForChange: null,
                                                proposedChanges: null,
                                                changeReasonType: null,
                                                originator: null,
                                                requestedBy: null,
                                                status: "-",
                                                requestedByObject: null,
                                                originatorObject: null,
                                                ecoOwner: null,
                                                workflow: null,
                                                dcrOwnerObject: $rootScope.localStorageLogin.login.person
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
                if (vm.newDCR.crType == null || vm.newDCR.crType == undefined ||
                    vm.newDCR.crType == "") {
                    $rootScope.showWarningMessage(dcrTypeValidation);
                    valid = false;
                }
                else if (vm.newDCR.crNumber == null || vm.newDCR.crNumber == undefined ||
                    vm.newDCR.crNumber == "") {
                    $rootScope.showWarningMessage(dcrNumberValidation);
                    valid = false;
                }

                else if (vm.newDCR.title == null || vm.newDCR.title == undefined ||
                    vm.newDCR.title == "") {
                    $rootScope.showWarningMessage(titleValidation);
                    valid = false;
                }
                else if (vm.newDCR.changeReasonType == null || vm.newDCR.changeReasonType == undefined || vm.newDCR.changeReasonType == "") {
                    $rootScope.showWarningMessage(selectChangeReason);
                    valid = false;
                }
                else if (vm.newDCR.reasonForChange == null || vm.newDCR.reasonForChange == undefined ||
                    vm.newDCR.reasonForChange == "") {
                    $rootScope.showWarningMessage(reasonforchange);
                    valid = false;
                }
                else if (vm.newDCR.originatorObject == null || vm.newDCR.originatorObject == undefined) {
                    $rootScope.showWarningMessage(originatorValidation);
                    valid = false;
                }
                else if (vm.newDCR.requestedByObject == null || vm.newDCR.requestedByObject == undefined) {
                    $rootScope.showWarningMessage(requestedByValidation);
                    valid = false;
                }
                else if (vm.newDCR.urgency == null || vm.newDCR.urgency == undefined || vm.newDCR.urgency == "") {
                    $rootScope.showWarningMessage(selectUrgency);
                    valid = false;
                }
                else if (vm.newDCR.workflow == null || vm.newDCR.workflow == undefined) {
                    $rootScope.showWarningMessage(workflowValidation);
                    valid = false;
                } else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if (vm.dcrRequiredProperties.length > 0 && !validateCustomAttributes()) {
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }

                return valid;
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
                    }
                )

                var preference = $application.defaultValuesPreferences.get("DEFAULT_CHANGE_ANALYST_ROLE");
                if (preference != null && preference.defaultValueName != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.change.dcr.all";
                    QualityTypeService.getPersonByGroupNameAndPermission(groupName, permission).then(
                        function (data) {
                            vm.changeAnalysts = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
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
                angular.forEach(vm.dcrRequiredProperties, function (attribute) {
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
                            DCOService.updateAttributeImageValue("DCRTYPE", vm.newDCR.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    )

                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            DCOService.updateAttributeAttachmentValues("DCRTYPE", vm.newDCR.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
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
                            DCOService.updateAttributeImageValue("DCR", vm.newDCR.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
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
                            DCOService.updateAttributeAttachmentValues("DCR", vm.newDCR.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
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
                vm.attributes = [];
                ECOService.getEcoAttributesWithHierarchy(vm.type.id).then(
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

            function loadDcrCustomProperties() {
                vm.dcrProperties = [];
                vm.dcrRequiredProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("CHANGE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newDCR.id,
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
                                vm.dcrProperties.push(att);
                            } else {
                                vm.dcrRequiredProperties.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadChangeRequestUrgency() {
                vm.urgencys = [];
                LovService.getLovByName("Default Change Request Urgency").then(
                    function (data) {
                        if (data != null && data != "") {
                            vm.newDCR.urgency = data.defaultValue;
                            vm.urgencys = data.values;
                        }
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadChangeRequestUrgency();
                loadPersons();
                loadDcrCustomProperties();
                $rootScope.$on('app.dcr.new', create);
                //}
            })();
        }
    }
)
;
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
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/problemReportService',
        'app/shared/services/core/customerSupplierService',
        'app/shared/services/core/inspectionService',
        'app/shared/services/core/supplierService',
        'app/shared/services/core/classificationService'
    ],
    function (module) {

        module.controller('NewProblemReportController', NewProblemReportController);

        function NewProblemReportController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, $application,
                                            AutonumberService, LoginService, ItemService, QualityTypeService, ObjectTypeAttributeService,
                                            CustomerSupplierService, InspectionService, ProblemReportService, SupplierService, ClassificationService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var enterProblem = parsed.html($translate.instant("ENTER_PROBLEM")).html();
            var selectProduct = parsed.html($translate.instant("SELECT_PRODUCT")).html();
            var selectReportedBy = parsed.html($translate.instant("SELECT_REPORTED_BY")).html();
            var enterPrNumber = parsed.html($translate.instant("ENTER_PR_NUMBER")).html();
            var selectPrType = parsed.html($translate.instant("SELECT_PR_TYPE")).html();
            var selectQualityAnalyst = parsed.html($translate.instant("SELECT_QUALITY_ANALYST")).html();
            var selectFailureType = parsed.html($translate.instant("SELECT_FAILURE_TYPE")).html();
            var selectDefectType = parsed.html($translate.instant("P_S_DEFECT_TYPE")).html();
            var selectSeverity = parsed.html($translate.instant("SELECT_SEVERITY")).html();
            var selectDisposition = parsed.html($translate.instant("SELECT_DISPOSITION")).html();
            var enterDescription = parsed.html($translate.instant("ENTER_DESCRIPTION")).html();
            var prCreated = parsed.html($translate.instant("PR_CREATED")).html();
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            var selectWorkflowValid = parsed.html($translate.instant("WORKFLOW_SELECT_VALID")).html();
            $scope.selectPersonTitle = parsed.html($translate.instant("SELECT_PERSONS")).html();
            $scope.selectCustomerTitle = parsed.html($translate.instant("SELECT_CUSTOMER_TITLE")).html();
            $scope.selectProductTitle = parsed.html($translate.instant("SELECT_PRODUCT_TITLE")).html();
            $scope.selectQualityAnalystTitle = parsed.html($translate.instant("S_QUALITY_ANALYST")).html();
            $scope.selectWorkflowTitle = parsed.html($translate.instant("SELECT_WORKFLOW")).html();
            $scope.selectFailureTitle = parsed.html($translate.instant("S_FAILURE_TYPE")).html();
            $scope.selectDefectTitle = parsed.html($translate.instant("S_DEFECT_TYPE")).html();
            $scope.selectSeverityTitle = parsed.html($translate.instant("S_SEVERITY")).html();
            $scope.selectDispositionTitle = parsed.html($translate.instant("S_DISPOSITION")).html();
            $scope.selectProblemReportTitle = parsed.html($translate.instant("S_PROBLEM_REPORT")).html();
            $scope.selectInspectionTitle = parsed.html($translate.instant("S_INSPECTION")).html();
            $scope.selectWorkflow = parsed.html($translate.instant("SELECT_WORKFLOW")).html();
            $scope.enterOtherReported = parsed.html($translate.instant("ENTER_OTHER_REPORTED")).html();
            var pleaseEnterOtherReported = parsed.html($translate.instant("P_E_OTHER_REPORTED")).html();


            vm.onSelectType = onSelectType;
            vm.newProblemReport = {
                id: null,
                prNumber: null,
                prType: null,
                problem: null,
                product: null,
                inspection: null,
                description: null,
                stepsToReproduce: null,
                reporterType: "CUSTOMER",
                reportedBy: null,
                otherReported: null,
                qualityAnalyst: null,
                workflow: null,
                failureType: null,
                severity: null,
                disposition: null
            };

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newProblemReport.prType = itemType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newProblemReport.prType != null && vm.newProblemReport.prType.numberSource != null) {
                    var source = vm.newProblemReport.prType.numberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newProblemReport.prNumber = data;
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
                QualityTypeService.getQualityTypeWorkflows(vm.newProblemReport.prType.id, 'QUALITY').then(
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
                ClassificationService.getObjectTypeAttributesWithHierarchy(vm.newProblemReport.prType.qualityType, vm.newProblemReport.prType.id, 0).then(
                    function (data) {
                        vm.qualityTypeAttributes = data.qualityTypeAttributes;
                        angular.forEach(vm.qualityTypeAttributes, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newProblemReport.prType.id,
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

            vm.onSelectProduct = onSelectProduct;
            function onSelectProduct(product) {
                $rootScope.showBusyIndicator($("#rightSidePanel"));
                InspectionService.getRejectedItemInspectionsByProduct(product.latestRevision).then(
                    function (data) {
                        vm.inspections = data;
                        if (vm.inspections.length == 0) {
                            $scope.selectInspectionTitle = parsed.html($translate.instant("NO_INSPECTIONS")).html();
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

            function loadProductItems() {
                ItemService.getReleasedItemsByItemClass("PRODUCT").then(
                    function (data) {
                        vm.productItems = data;
                        ItemService.getLatestRevisionReferences(vm.productItems, 'latestReleasedRevision');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
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
                        vm.persons.push({id: 0, fullName: "Other"});
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
                var preference = $application.defaultValuesPreferences.get("DEFAULT_QUALITY_ANALYST_ROLE");
                if (preference != null && preference.defaultValueName != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.problemreport.all";
                    QualityTypeService.getPersonByGroupNameAndPermission(groupName, permission).then(
                        function (data) {
                            vm.qualityAnalysts = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
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

            function loadSuplliers() {
                SupplierService.getApprovedSuppliers().then(
                    function (data) {
                        vm.suppliers = data;
                        vm.suppliers.push({id: 0, name: "Other"});
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
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
                    angular.forEach(vm.prRequiredProperties, function (reqatt) {
                        vm.prProperties.push(reqatt);
                    })
                    angular.forEach(vm.prProperties, function (attribute) {
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
                    if (vm.newProblemReport.reportedBy === 0) {
                        vm.newProblemReport.reportedBy = null;
                    }
                    var qualityAttributeDto = {
                        problemReport: vm.newProblemReport,
                        problemReportAttributes: vm.attributes,
                        objectAttributes: vm.prProperties
                    }
                    QualityTypeService.createQualityObject("PROBLEMREPORT", qualityAttributeDto).then(
                        function (data) {
                            vm.newProblemReport = data.problemReport;
                            saveCustomAttributes().then(
                                function (atts) {
                                    saveAttributes().then(
                                        function (attributes) {
                                            $scope.callback(vm.newProblemReport);
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(prCreated);
                                            vm.newProblemReport = {
                                                id: null,
                                                prNumber: null,
                                                prType: null,
                                                problem: null,
                                                product: null,
                                                description: null,
                                                stepsToReproduce: null,
                                                reporterType: null,
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
                if (vm.newProblemReport.reportedBy != 0 && (vm.newProblemReport.reportedBy == null || vm.newProblemReport.reportedBy == "" || vm.newProblemReport.reportedBy == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(selectReportedBy);
                } else if (vm.newProblemReport.reportedBy == 0 && (vm.newProblemReport.otherReported == null || vm.newProblemReport.otherReported == "" || vm.newProblemReport.otherReported == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterOtherReported);
                } else if (vm.newProblemReport.prType == null || vm.newProblemReport.prType == "" || vm.newProblemReport.prType == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectPrType);
                } else if (vm.newProblemReport.prNumber == null || vm.newProblemReport.prNumber == "" || vm.newProblemReport.prNumber == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(enterPrNumber);
                } /*else if (vm.newProblemReport.product == null || vm.newProblemReport.product == "" || vm.newProblemReport.product == undefined) {
                 valid = false;
                 $rootScope.showWarningMessage(selectProduct);
                 }*/ else if (vm.newProblemReport.problem == null || vm.newProblemReport.problem == "" || vm.newProblemReport.problem == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(enterProblem);
                } else if (vm.newProblemReport.description == null || vm.newProblemReport.description == "" || vm.newProblemReport.description == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(enterDescription);
                } else if (vm.newProblemReport.qualityAnalyst == null || vm.newProblemReport.qualityAnalyst == "" || vm.newProblemReport.qualityAnalyst == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectQualityAnalyst);
                } else if (vm.newProblemReport.workflow == null || vm.newProblemReport.workflow == "" || vm.newProblemReport.workflow == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectWorkflowValid);
                } else if (vm.newProblemReport.failureType == null || vm.newProblemReport.failureType == "" || vm.newProblemReport.failureType == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectDefectType);
                } else if (vm.newProblemReport.severity == null || vm.newProblemReport.severity == "" || vm.newProblemReport.severity == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectSeverity);
                } else if (vm.newProblemReport.disposition == null || vm.newProblemReport.disposition == "" || vm.newProblemReport.disposition == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(selectDisposition);
                } else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if (vm.prRequiredProperties.length > 0 && !validateCustomAttributes()) {
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
                angular.forEach(vm.prRequiredProperties, function (attribute) {
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
                            ClassificationService.updateAttributeImageValue("PROBLEMREPORTTYPE", vm.newProblemReport.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("PROBLEMREPORTTYPE", vm.newProblemReport.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
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
                            ClassificationService.updateAttributeImageValue("PROBLEMREPORT", vm.newProblemReport.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.customAttachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("PROBLEMREPORT", vm.newProblemReport.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
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

            function loadPrCustomProperties() {
                vm.prProperties = [];
                vm.prRequiredProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("QUALITY").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newProblemReport.id,
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
                                vm.prProperties.push(att);
                            } else {
                                vm.prRequiredProperties.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.selectReporterType = selectReporterType;
            function selectReporterType(value) {
                if (value == 'customer') {
                    vm.newProblemReport.reportedBy = null;
                    vm.newProblemReport.reporterType = "CUSTOMER"
                }
                if (value == 'supplier') {
                    vm.newProblemReport.reportedBy = null;
                    vm.newProblemReport.reporterType = "SUPPLIER";
                }
                if (value == 'internal') {
                    vm.newProblemReport.reportedBy = null;
                    vm.newProblemReport.reporterType = "INTERNAL";
                }
                $scope.$evalAsync();

            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadCustomers();
                loadPersons();
                loadSuplliers();
                loadProductItems();
                loadPrCustomProperties();
                $rootScope.$on('app.problemReports.new', create);
                //}
            })();
        }
    }
)
;
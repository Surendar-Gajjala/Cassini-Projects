define(
    [
        'app/desktop/modules/mro/mro.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/workOrderService',
        'app/shared/services/core/assetService',
        'app/shared/services/core/workRequestService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/maintenancePlanService',
        'app/shared/services/core/qualityTypeService'

    ],
    function (module) {

        module.controller('NewWorkOrderController', NewWorkOrderController);

        function NewWorkOrderController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, $application, WorkOrderService, WorkRequestService, LoginService,
                                        ObjectTypeAttributeService, AutonumberService, MESObjectTypeService, AttributeAttachmentService, AssetService, ProjectService,
                                        MaintenancePlanService, QualityTypeService) {

            var vm = this;

            var parsed = angular.element("<div></div>");

            vm.attributes = [];
            vm.validattributes = [];

            vm.workOrderMode = $scope.data.workOrderMode;

            var itemTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var pleaseSelectAsset = parsed.html($translate.instant("PLEASE_SELECT_ASSET")).html();
            var pleaseSelectMaintenance = parsed.html($translate.instant("P_SELECT_MAINTENANCE_PLAN")).html();
            var pleaseSelectWorkRequest = parsed.html($translate.instant("PLEASE_SELECT_WORKREQUEST")).html();
            var pleaseSelectAssignedTo = parsed.html($translate.instant("ASSIGNED_TO_VALIDATION")).html();
            $scope.selectAsset = parsed.html($translate.instant("SELECT_ASSET")).html();
            $scope.selectAssignedTo = parsed.html($translate.instant("SELECT_ASSIGNED_TO")).html();
            $scope.selectWorkflow = parsed.html($translate.instant("SELECT_WORKFLOW")).html();

            vm.newWorkOrder = {
                id: null,
                type: null,
                number: null,
                name: null,
                description: null,
                asset: null,
                request: null,
                workflow: null,
                assignedTo: null,
                priority: "LOW",
                status: "OPEN",
                notes: null,
                workflowDefinition: null,
                plan: null
            };

            vm.priorities = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];


            vm.autoNumber = autoNumber;
            vm.onSelectType = onSelectType;
            function onSelectType(workOrderType) {
                if (workOrderType != null && workOrderType != undefined) {
                    vm.newWorkOrder.type = workOrderType;
                    if (vm.newWorkOrder.type.type == "REPAIR") {
                        vm.newWorkOrder.plan = null;
                    } else if (vm.newWorkOrder.type.type == "MAINTENANCE") {
                        vm.newWorkOrder.request = null;
                    }
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newWorkOrder.type != null) {
                    var source = vm.newWorkOrder.type.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newWorkOrder.number = data;
                            loadWorkflows();
                            loadWorkOrderTypeAttributes();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            function createNewWorkOrder() {
                create().then(function () {
                    vm.newWorkOrder = {
                        id: null,
                        type: null,
                        number: null,
                        name: null,
                        description: null,
                        asset: null,
                        request: null,
                        assignedTo: null,
                        priority: "LOW",
                        status: "OPEN",
                        notes: null,
                        workflowDefinition: null,
                        plan: null
                    };
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                })

            }


            function validate() {
                var valid = true;
                if (vm.newWorkOrder.type == null || vm.newWorkOrder.type == undefined || vm.newWorkOrder.type == "") {
                    $rootScope.showWarningMessage(itemTypeValidation);
                    valid = false;
                } else if (vm.newWorkOrder.number == null || vm.newWorkOrder.number == undefined || vm.newWorkOrder.number == "") {
                    $rootScope.showWarningMessage(itemNumberValidation);
                    valid = false;
                } else if (vm.newWorkOrder.asset == null || vm.newWorkOrder.asset == undefined || vm.newWorkOrder.asset == "") {
                    $rootScope.showWarningMessage(pleaseSelectAsset);
                    valid = false;
                } else if (vm.newWorkOrder.type.type == 'MAINTENANCE' && (vm.newWorkOrder.plan == null || vm.newWorkOrder.plan == undefined || vm.newWorkOrder.plan == "")) {
                    $rootScope.showWarningMessage(pleaseSelectMaintenance);
                    valid = false;
                } else if (vm.newWorkOrder.type.type == 'REPAIR' && (vm.newWorkOrder.request == null || vm.newWorkOrder.request == undefined || vm.newWorkOrder.request == "")) {
                    $rootScope.showWarningMessage(pleaseSelectWorkRequest);
                    valid = false;
                } else if (vm.newWorkOrder.name == null || vm.newWorkOrder.name == undefined || vm.newWorkOrder.name == "") {
                    $rootScope.showWarningMessage(itemNameValidation);
                    valid = false;
                } else if (vm.newWorkOrder.assignedTo == null || vm.newWorkOrder.assignedTo == undefined || vm.newWorkOrder.assignedTo == "") {
                    $rootScope.showWarningMessage(pleaseSelectAssignedTo);
                    valid = false;
                }

                else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }

                return valid;
            }

            function validateRequiredAttributes() {
                var valid = true;
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        if (valid) {
                            if ($rootScope.checkAttribute(attribute)) {
                                valid = true;
                            } else {
                                valid = false;
                                $rootScope.showWarningMessage("Please enter" + " " + attribute.attributeDef.name);
                            }
                        }
                    })
                }
                return valid;
            }

            var insertedSuccefully = parsed.html($translate.instant("WORK_ORDER_ADDED_SUCCESS")).html();

            function create() {
                var dfd = $q.defer();
                vm.validattributes = [];
                if (validate() && validateRequiredAttributes()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    WorkOrderService.createWorkOrder(vm.newWorkOrder).then(
                        function (data) {
                            vm.newWorkOrder = data;
                            saveAttributes().then(
                                function (att) {
                                    vm.newWorkOrder = {
                                        id: null,
                                        type: null,
                                        number: null,
                                        name: null,
                                        description: null,
                                        asset: null,
                                        request: null,
                                        plan: null,
                                        assignedTo: null,
                                        priority: "LOW",
                                        status: "OPEN",
                                        notes: null,
                                        workflowDefinition: null
                                    };
                                    vm.selectedWorkOrderType = null;
                                    vm.attributes = [];
                                    $scope.callback(data);
                                    $rootScope.hideSidePanel();
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(insertedSuccefully);
                                }
                            )
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

                return dfd.promise;
            }


            function addWorkOrderPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MROWORKORDER', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                })
                return propertyAttachmentIds;
            }


            function saveAttributes() {
                var defered = $q.defer();
                vm.imageAttributes = [];
                var images = new Hashtable();
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        attribute.id.objectId = vm.newWorkOrder.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'BOOLEAN' && attribute.booleanValue != true) {
                            attribute.booleanValue = false;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addWorkOrderPropertyAttachment(attribute)
                        }
                    });
                    $timeout(function () {
                        WorkOrderService.saveWorkOrderAttributes(vm.attributes).then(
                            function (data) {

                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        WorkOrderService.uploadImageAttribute(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {

                                                defered.resolve();
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }

                            }, function () {
                                defered.reject();
                            })
                    }, 2000)
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }


            function loadWorkOrderTypeAttributes() {
                vm.attributes = [];
                WorkOrderService.getObjectAttributesWithHierarchy(vm.newWorkOrder.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newWorkOrder.id,
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
                            if (attribute.dataType == "LIST" && !attribute.listMultiple && attribute.defaultListValue != null) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple && attribute.defaultListValue != null) {
                                att.mlistValue.push(attribute.defaultListValue);
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

            function loadAssets() {
                AssetService.getAssets().then(
                    function (data) {
                        vm.assets = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadPersons() {
                vm.persons = [];
                /*LoginService.getAllLogins().then(
                 function (data) {
                 angular.forEach(data, function (login) {
                 if (login.isActive == true && login.external == false) {
                 vm.persons.push(login.person);
                 }
                 });
                 }
                 )*/
                var preference = $application.defaultValuesPreferences.get("DEFAULT_MAINTENANCE_TECHNICIAN_ROLE");
                if (preference != null && preference.defaultValueName != null) {
                    var groupName = preference.defaultValueName;
                    var permission = null;
                    QualityTypeService.getPersonByGroupNameAndPermission(groupName, permission).then(
                        function (data) {
                            vm.persons = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadWorkRequests() {
                WorkRequestService.getPendingWorkRequests().then(
                    function (data) {
                        vm.workRequests = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadWorkRequest() {
                WorkRequestService.getWorkRequest($scope.data.workRequestId).then(
                    function (data) {
                        vm.workRequest = data;
                        vm.newWorkOrder.request = vm.workRequest.id;
                        loadAsset();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadAsset() {
                AssetService.getAsset(vm.workRequest.asset).then(
                    function (data) {
                        vm.asset = data;
                        vm.newWorkOrder.asset = vm.asset.id;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadWorkflows() {
                WorkOrderService.getWorkOrderWorkflows(vm.newWorkOrder.type.id, 'MAINTENANCE&REPAIR').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.maintenancePlans = [];
            vm.onSelectAsset = onSelectAsset;
            function onSelectAsset() {
                MaintenancePlanService.getMaintenancePlansByAsset(vm.newWorkOrder.asset).then(
                    function (data) {
                        vm.maintenancePlans = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadRepairWorkOrderType() {
                MESObjectTypeService.getRepairWorkOrderTypeTree().then(
                    function (data) {
                        vm.repairWorkOrderTypes = data;
                        if (vm.repairWorkOrderTypes.length == 1) {
                            vm.newWorkOrder.type = vm.repairWorkOrderTypes[0];
                            autoNumber();
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                $rootScope.hideBusyIndicator();
                loadPersons();
                loadAssets();
                loadWorkRequests();
                if ($scope.data.workOrderMode == "WORKREQUEST") {
                    loadWorkRequest();
                    loadRepairWorkOrderType();
                }
                $rootScope.$on('app.workOrder.new', create);
            })();
        }
    }
);
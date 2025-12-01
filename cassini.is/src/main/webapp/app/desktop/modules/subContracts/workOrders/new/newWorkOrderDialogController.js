/**
 * Created by swapna on 22/01/19.
 */
define(['app/desktop/modules/subContracts/contracts.module',
        'app/shared/services/core/subContractService',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService'
    ],
    function (module) {
        module.controller('NewWorkOrderController', NewWorkOrderController);

        function NewWorkOrderController($scope, $rootScope, $timeout, $state, $cookies, $sce, $q,
                                        ObjectTypeAttributeService, AttributeAttachmentService, ObjectAttributeService, SubContractService, ProjectService) {
            var vm = this;
            vm.contractors = [];
            vm.projects = [];
            vm.attributes = [];
            vm.requiredAttributes = [];
            vm.newWorkOrder = {
                contractor: null,
                status: 'PENDING',
                project: null
            };

            function validateWorkOrder() {
                var flag = true;
                if (vm.newWorkOrder.contractor == null || vm.newWorkOrder.contractor == undefined) {
                    flag = false;
                    $rootScope.showErrorMessage("Please select contractor");
                }
                else if (vm.newWorkOrder.project == null || vm.newWorkOrder.project == undefined) {
                    flag = false;
                    $rootScope.showErrorMessage("Please select project");
                }
                return flag;
            }

            function create() {
                if (validateWorkOrder()) {
                    attributesValidate().then(
                        function () {
                            $rootScope.showBusyIndicator();
                            vm.newWorkOrder.project = vm.newWorkOrder.project.id;
                            vm.newWorkOrder.contractor = vm.newWorkOrder.contractor.id;
                            SubContractService.createWorkOrder(vm.newWorkOrder).then(
                                function (data) {
                                    vm.newWorkOrder = data;
                                    intializationForAttributesSave();
                                    vm.newWorkOrder = {
                                        contractor: null,
                                        status: null,
                                        project: null
                                    };

                                    $rootScope.hideBusyIndicator();
                                    $rootScope.hideSidePanel('right');
                                    $rootScope.$broadcast('app.workOrders.all');
                                }/*,
                                 function (error) {
                                 $rootScope.hideBusyIndicator();
                                 $rootScope.hideSidePanel('right');
                                 $rootScope.showErrorMessage(error.message);
                                 }*/
                            )
                        }
                    );
                }
            }

            function loadActiveContractors() {
                SubContractService.getActiveContractors().then(
                    function (data) {
                        vm.contractors = data;
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadProjects() {
                ProjectService.getAllProjects().then(
                    function (data) {
                        vm.projects = data;
                    }
                )
            }

            /* ............. start attributes methods block ............. */

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'WORKORDER', attachmentFile).then(
                        function (data) {
                            vm.propertyAttachmentIds.push(data[0].id);
                            counter++;
                            if (counter == attribute.attachmentValues.length) {
                                defered.resolve(true);
                            }
                        }
                    )
                });
                return defered.promise;
            }

            function saveObjectAttributes() {
                var defered = $q.defer();
                var countloop = 0;
                if (vm.newWorkOrderAttributes.length > 0) {
                    angular.forEach(vm.newWorkOrderAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                        countloop++;
                        if (countloop == vm.newWorkOrderAttributes.length) {
                            ObjectAttributeService.saveItemObjectAttributes(vm.newWorkOrder.id, vm.newWorkOrderAttributes).then(
                                function (data) {
                                    var secCount = 0;
                                    if (vm.propertyImageAttributes.length > 0) {
                                        angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                            ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, vm.propertyImages.get(propImgAtt.id.attributeDef)).then(
                                                function (data) {
                                                });
                                            secCount++;
                                            if (secCount == vm.propertyImageAttributes.length) {
                                                defered.resolve();
                                            }
                                        });
                                    } else {
                                        defered.resolve();
                                    }
                                },
                                function (error) {
                                    defered.reject();
                                }
                            )
                        }
                    });

                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function attributesValidate() {
                var defered = $q.defer();
                $rootScope.closeNotification();
                vm.objectAttributes = [];
                vm.validObjectAttributes = [];
                vm.validattributes = [];
                angular.forEach(vm.requiredAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validattributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validattributes.push(attribute);
                    }
                });
                vm.objectAttributes = [];
                if (vm.newWorkOrderAttributes != null && vm.newWorkOrderAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newWorkOrderAttributes);
                }
                angular.forEach(vm.objectAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validObjectAttributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validObjectAttributes.push(attribute);
                    }
                });
                if (vm.requiredAttributes.length == vm.validattributes.length && vm.objectAttributes.length == vm.validObjectAttributes.length) {
                    defered.resolve();
                } else {
                    defered.reject();
                }
                return defered.promise;
            }

            function loadObjectAttributeDefs() {
                vm.newWorkOrderAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("WORKORDER").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newWorkOrder.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                timeValue: null,
                                timestampValue: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss"),
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else {
                                vm.requiredAttributes.push(att);
                            }

                            vm.newWorkOrderAttributes.push(att);
                        });
                    }, function (error) {

                    });
            }

            function checkAttribute(attribute) {
                if ((attribute.stringValue != null && attribute.stringValue != undefined && attribute.stringValue != "") ||
                    (attribute.integerValue != null && attribute.integerValue != undefined && attribute.integerValue != "") ||
                    (attribute.doubleValue != null && attribute.doubleValue != undefined && attribute.doubleValue != "") ||
                    (attribute.dateValue != null && attribute.dateValue != undefined && attribute.dateValue != "") ||
                    (attribute.imageValue != null && attribute.imageValue != undefined && attribute.imageValue != "") ||
                    (attribute.currencyValue != null && attribute.currencyValue != undefined && attribute.currencyValue != "") ||
                    (attribute.timeValue != null && attribute.timeValue != undefined && attribute.timeValue != "") ||
                    (attribute.attachmentValues.length != 0) ||
                    (attribute.refValue != null && attribute.refValue != undefined && attribute.refValue != "") ||
                    (attribute.listValue != null && attribute.listValue != undefined && attribute.listValue != "")) {
                    return true;
                } else {
                    return false;
                }
            }

            function intializationForAttributesSave() {
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];

                if (vm.newWorkOrderAttributes.length == 0) {
                    $rootScope.hideBusyIndicator();
                    $rootScope.showSuccessMessage("Work Order (" + vm.newWorkOrder.number + ") created successfully");
                }
                else {
                    angular.forEach(vm.newWorkOrderAttributes, function (attribute) {
                        attribute.id.objectId = vm.newWorkOrder.id;
                        if (attribute.attributeDef.dataType == "IMAGE" && attribute.imageValue != null) {
                            vm.propertyImages.put(attribute.attributeDef.id, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            addAttachment(attribute).then(
                                function (data) {
                                    attribute.attachmentValues = vm.propertyAttachmentIds;
                                    attrCount++;

                                    if (attrCount == vm.newWorkOrderAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newWorkOrderAttributes = [];
                                                loadObjectAttributeDefs();
                                                $rootScope.hideBusyIndicator();
                                                $rootScope.showSuccessMessage("Work Order (" + vm.newWorkOrder.number + ") created successfully");
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.newWorkOrderAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newWorkOrderAttributes = [];
                                        loadObjectAttributeDefs();
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.showSuccessMessage("Work Order (" + vm.newWorkOrder.number + ") created successfully");
                                    }
                                )
                            }
                        }
                    });
                }
            }

            /* ............. attributes methods end block ............. */

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.workOrder.new', function () {
                        create();
                    });
                    loadObjectAttributeDefs();
                    loadActiveContractors();
                    loadProjects();
                }
            })();
        }
    }
);
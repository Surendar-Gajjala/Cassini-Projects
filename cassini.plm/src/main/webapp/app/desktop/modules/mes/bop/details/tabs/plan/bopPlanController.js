define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/bopService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService'
    ],
    function (module) {
        module.controller('BOPPlanController', BOPPlanController);

        function BOPPlanController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal,
                                   httpFactory, BOPService, AutonumberService, DialogService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            var removeOperationTitle = parsed.html($translate.instant("REMOVE_PLAN_DIALOG")).html();
            var removeOperationDialogMsg = parsed.html($translate.instant("REMOVE_PLAN_DIALOG_MESSAGE")).html();
            var operationRemovedMsg = parsed.html($translate.instant("BOP_PLAN_REMOVED_MSG")).html();
            var operationCreatedMsg = parsed.html($translate.instant("BOP_PLAN_CREATE_MSG")).html();
            var operationUpdatedMsg = parsed.html($translate.instant("BOP_PLAN_UPDATE_MSG")).html();
            var pleaseEnterSequence = parsed.html($translate.instant("PLEASE_ENTER_SEQUENCE_NUMBER")).html();
            var pleaseEnterName = parsed.html($translate.instant("PLEASE_ENTER_NAME")).html();
            vm.bopId = $stateParams.bopId;
            vm.addOperation = addOperation;

            var emptyPlan = {
                id: null,
                sequenceNumber: null,
                operation: null,
                bop: vm.bopId,
                parent: null,
                phantom: null,
                type: "OPERATION",
                setupTime: null,
                cycleTime: null
            };

            vm.addedOperations = [];
            function addOperation(plan) {
                var planId = null;
                if (plan != null) {
                    planId = plan.id;
                }
                var options = {
                    title: "Select Operations",
                    template: 'app/desktop/modules/mes/bop/details/tabs/plan/operationsSelectionView.jsp',
                    controller: 'OperationsSelectionController as operationsSelectionVm',
                    resolve: 'app/desktop/modules/mes/bop/details/tabs/plan/operationsSelectionController',
                    width: 650,
                    showMask: true,
                    data: {
                        selectedBopId: vm.bopId,
                        selectedBopPlanId: planId
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.select.bop.plan'}
                    ],
                    callback: function (result) {
                        angular.forEach(result, function (operation) {
                            var bopPlan = angular.copy(emptyPlan);
                            bopPlan.name = operation.name;
                            bopPlan.number = operation.number;
                            bopPlan.description = operation.description;
                            bopPlan.typeName = operation.type.name;
                            bopPlan.parent = planId;
                            bopPlan.operation = operation.id;
                            bopPlan.editMode = true;
                            bopPlan.isNew = true;
                            bopPlan.count = 0;
                            vm.addedOperations.push(bopPlan);
                            if (plan != null) {
                                plan.expanded = true;
                                plan.count = plan.count + 1;
                                plan.bopChildren.push(bopPlan);
                                bopPlan.parentBop = plan;
                                bopPlan.level = plan.level + 1;
                                vm.bopPlans.splice(vm.bopPlans.indexOf(plan) + 1, 0, bopPlan);
                            } else {
                                vm.bopPlans.push(bopPlan);
                            }
                        })
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadPlan() {
                vm.loading = true;
                BOPService.getBopPlans(vm.bopId).then(
                    function (data) {
                        vm.bopPlans = [];
                        angular.forEach(data, function (item) {
                            item.parentBop = null;
                            item.editMode = false;
                            item.expanded = false;
                            item.level = 0;
                            item.count = 0;
                            item.bopChildren = [];
                            item.isNew = false;
                            vm.bopPlans.push(item);
                            var index = vm.bopPlans.indexOf(item);
                            index = populateBOPPlanChildren(item, index);
                        });
                        vm.loading = false;
                    }
                )
            }

            function populateBOPPlanChildren(bopPlan, lastIndex) {
                angular.forEach(bopPlan.children, function (item) {
                    lastIndex++;
                    item.parentBop = bopPlan;
                    item.expanded = true;
                    item.editMode = false;
                    item.level = bopPlan.level + 1;
                    item.count = 0;
                    item.bopChildren = [];
                    item.isNew = false;
                    vm.bopPlans.splice(lastIndex, 0, item);
                    bopPlan.count = bopPlan.count + 1;
                    bopPlan.expanded = true;
                    bopPlan.bopChildren.push(item);
                    lastIndex = populateBOPPlanChildren(item, lastIndex)
                });

                return lastIndex;
            }

            vm.togglePlanNode = togglePlanNode;
            function togglePlanNode(bopPlan) {
                if (bopPlan.expanded == null || bopPlan.expanded == undefined) {
                    bopPlan.expanded = false;
                }
                bopPlan.expanded = !bopPlan.expanded;
                var index = vm.bopPlans.indexOf(bopPlan);
                if (bopPlan.expanded == false) {
                    removePlanChildren(bopPlan);
                }
                else {
                    $rootScope.showBusyIndicator($('.view-container'));
                    BOPService.getBopPlanChildren(vm.bopId, bopPlan.id).then(
                        function (data) {
                            angular.forEach(data, function (item) {
                                item.isNew = false;
                                item.expanded = false;
                                item.editMode = false;
                                item.level = bopPlan.level + 1;
                                item.bopChildren = [];
                                bopPlan.bopChildren.push(item);
                            });

                            angular.forEach(bopPlan.bopChildren, function (item) {
                                index = index + 1;
                                vm.bopPlans.splice(index, 0, item);
                            });
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function removePlanChildren(bopPlan) {
                if (bopPlan != null && bopPlan.bopChildren != null && bopPlan.bopChildren != undefined) {
                    angular.forEach(bopPlan.bopChildren, function (item) {
                        removePlanChildren(item);
                    });

                    var index = vm.bopPlans.indexOf(bopPlan);
                    vm.bopPlans.splice(index + 1, bopPlan.bopChildren.length);
                    bopPlan.bopChildren = [];
                    bopPlan.expanded = false;

                }
            }

            vm.addPhantom = addPhantom;
            function addPhantom(plan) {
                AutonumberService.getNextNumberByName(vm.phantomNumberSource.name).then(
                    function (data) {
                        var newRow = angular.copy(emptyPlan);
                        newRow.number = data;
                        newRow.typeName = "Phantom";
                        newRow.type = "PHANTOM";
                        newRow.editMode = true;
                        newRow.isNew = true;
                        newRow.level = 0;
                        newRow.expanded = false;
                        newRow.count = 0;
                        newRow.bopChildren = [];
                        vm.addedOperations.push(newRow);

                        if (plan != null) {
                            newRow.parent = plan.id;
                            plan.expanded = true;
                            plan.count = plan.count + 1;
                            plan.bopChildren.push(newRow);
                            newRow.parentBop = plan;
                            newRow.level = plan.level + 1;
                            vm.bopPlans.splice(vm.bopPlans.indexOf(plan) + 1, 0, newRow);
                        } else {
                            vm.bopPlans.push(newRow);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.editPlan = editPlan;
            function editPlan(plan) {
                plan.oldSetupTime = plan.setupTime;
                plan.oldCycleTime = plan.cycleTime;
                plan.editMode = true;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(plan) {
                if (plan.isNew) {
                    vm.addedOperations.splice(vm.addedOperations.indexOf(plan), 1);
                    vm.bopPlans.splice(vm.bopPlans.indexOf(plan), 1);
                } else {
                    plan.setupTime = plan.oldSetupTime;
                    plan.cycleTime = plan.oldCycleTime;
                    plan.editMode = false;
                }
            }

            vm.onOk = onOk;
            function onOk(plan) {
                if (plan.id == null || plan.id == "" || plan.id == undefined) {
                    savePlan(plan);
                } else {
                    updatePlan(plan);
                }
            }

            vm.savePlan = savePlan;
            function savePlan(plan) {
                if (validate(plan)) {
                    var parentBop = plan.parentBop;
                    var bopChildren = plan.bopChildren;
                    var children = plan.children;
                    plan.parentBop = null;
                    plan.bopChildren = [];
                    plan.children = [];
                    $rootScope.showBusyIndicator($('.view-container'));
                    BOPService.createBopPlan(vm.bopId, plan).then(
                        function (data) {
                            plan.id = data.id;
                            plan.phantom = data.phantom;
                            plan.editMode = false;
                            plan.isNew = false;
                            plan.parentBop = parentBop;
                            plan.bopChildren = bopChildren;
                            plan.children = children;
                            vm.addedOperations.splice(vm.addedOperations.indexOf(plan), 1);
                            $rootScope.loadBOPTabCounts();
                            $rootScope.showSuccessMessage(operationCreatedMsg);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            plan.parentBop = parentBop;
                            plan.bopChildren = bopChildren;
                            plan.children = children;
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.saveAllPlans = saveAllPlans;
            function saveAllPlans() {
                if (validateAll()) {
                    angular.forEach(vm.addedOperations, function (plan) {
                        plan.parentBop = null;
                        plan.bopChildren = [];
                        plan.children = [];
                    })
                    $rootScope.showBusyIndicator($('.view-container'));
                    BOPService.createMultipleBopPlans(vm.bopId, vm.addedOperations).then(
                        function (data) {
                            vm.addedOperations = [];
                            $rootScope.loadBOPTabCounts();
                            loadPlan();
                            $rootScope.showSuccessMessage(operationCreatedMsg);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.updatePlan = updatePlan;
            function updatePlan(plan) {
                if (validate(plan)) {
                    var parentBop = plan.parentBop;
                    var bopChildren = plan.bopChildren;
                    var children = plan.children;
                    plan.parentBop = null;
                    plan.bopChildren = [];
                    plan.children = [];
                    $rootScope.showBusyIndicator($('.view-container'));
                    BOPService.updateBopPlan(vm.bopId, plan).then(
                        function (data) {
                            plan.id = data.id;
                            plan.editMode = false;
                            plan.isNew = false;
                            plan.parentBop = parentBop;
                            plan.bopChildren = bopChildren;
                            plan.children = children;
                            $rootScope.showSuccessMessage(operationUpdatedMsg);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            plan.parentBop = parentBop;
                            plan.bopChildren = bopChildren;
                            plan.children = children;
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate(plan) {
                var valid = true;
                if (plan.sequenceNumber == null || plan.sequenceNumber == "" || plan.sequenceNumber == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterSequence);
                } else if (plan.name == null || plan.name == "" || plan.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterName);
                }
                return valid;
            }

            function validateAll() {
                var valid = true;
                angular.forEach(vm.addedOperations, function (plan) {
                    if (valid) {
                        if (plan.sequenceNumber == null || plan.sequenceNumber == "" || plan.sequenceNumber == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage(pleaseEnterSequence);
                        } else if (plan.name == null || plan.name == "" || plan.name == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage(pleaseEnterName);
                        }
                    }
                })
                return valid;
            }

            vm.removeAllPlans = removeAllPlans;
            function removeAllPlans() {
                angular.forEach(vm.addedOperations, function (operation) {
                    vm.bopPlans.splice(vm.bopPlans.indexOf(operation), 1);
                });
                vm.addedOperations = [];
            }

            vm.removePlan = removePlan;
            function removePlan(plan) {
                var options = {
                    title: removeOperationTitle,
                    message: removeOperationDialogMsg.format(plan.number),
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        BOPService.deleteBopPlan(vm.bopId, plan.id).then(
                            function (data) {
                                if (plan.parentBop != null) {
                                    plan.parentBop.count = plan.parentBop.count - 1;
                                }
                                removePlanChildren(plan);
                                vm.bopPlans.splice(vm.bopPlans.indexOf(plan), 1);
                                $rootScope.loadBOPTabCounts();
                                $rootScope.showSuccessMessage(operationRemovedMsg);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            function loadAutoNumberSource() {
                AutonumberService.getAutonumberName("Default Phantom Number Source").then(
                    function (data) {
                        vm.phantomNumberSource = data;
                        if (vm.phantomNumberSource == null || vm.phantomNumberSource == "") {
                            addAutoNumber();
                        } else {
                            loadPlan();
                        }
                    }
                )
            }

            function addAutoNumber() {
                var newAutonumber = {
                    id: null,
                    name: "Default Phantom Number Source",
                    description: "Phantom Number Source",
                    numbers: 5,
                    start: 1,
                    increment: 1,
                    nextNumber: 1,
                    padwith: "0",
                    prefix: "PHTM-",
                    suffix: "",
                    newName: "",
                    newDescription: "",
                    newNumber: 5,
                    newStart: 1,
                    newIncrement: 1,
                    newPadwith: "0",
                    newPrefix: "",
                    newSuffix: "",
                    editMode: true,
                    showValues: false
                };
                AutonumberService.createAutonumber(newAutonumber).then(
                    function (data) {
                        vm.phantomNumberSource = data;
                        loadPlan();
                    }
                )
            }

            vm.showBopPlanDetails = showBopPlanDetails;
            function showBopPlanDetails(plan) {
                $state.go('app.mes.bop.planDetails', {bopId: plan.bop, bopPlanId: plan.id})
            }

            vm.showBopPlanResourcesDetails = showBopPlanResourcesDetails;
            function showBopPlanResourcesDetails(plan) {
                $state.go('app.mes.bop.planDetails', {bopId: plan.bop, bopPlanId: plan.id, tab: 'details.resources'})
            }

            vm.showBopPlanPartsDetails = showBopPlanPartsDetails;
            function showBopPlanPartsDetails(plan) {
                $state.go('app.mes.bop.planDetails', {bopId: plan.bop, bopPlanId: plan.id, tab: 'details.items'})
            }

            vm.showOperationDetails = showOperationDetails;
            function showOperationDetails(plan) {
                $state.go('app.mes.masterData.operation.details', {operationId: plan.operation})
            }

            $rootScope.hideValidatePlanResources = hideValidatePlanResources;
            function hideValidatePlanResources() {
                var modal = document.getElementById("validate-plan-resources");
                if (modal != null && modal != undefined) {
                    modal.style.display = "none";
                }
            }

            $rootScope.showValidatePlanResources = showValidatePlanResources;
            function showValidatePlanResources() {
                var modal = document.getElementById("validate-plan-resources");
                if (modal != null && modal != undefined) {
                    modal.style.display = "block";
                    vm.selectedResourceValidate = "resources";
                    $('#plan-resources-table').height($(".validate-plan-resources-content").outerHeight() - 98);
                    document.getElementById("plan-resources").checked = true;
                    loadPlanResourcesValidate();
                }
            }

            vm.loadResourcePartValidate = loadResourcePartValidate;
            function loadResourcePartValidate(value) {
                vm.loadingValidate = true;
                if (value == "resources") {
                    vm.selectedResourceValidate = "resources";
                    loadPlanResourcesValidate();
                } else {
                    vm.selectedResourceValidate = "parts";
                    loadPlanPartsValidate();
                }
            }

            function loadPlanPartsValidate() {
                $rootScope.showBusyIndicator($("#validate-plan-resources"));
                BOPService.getBOPMBOMItems(vm.bopId, 0, $rootScope.bopRevision.mbomRevision, true).then(
                    function (data) {
                        vm.mBomItems = [];
                        angular.forEach(data, function (item) {
                            item.parentBom = null;
                            item.expanded = false;
                            item.level = 0;
                            item.count = 0;
                            item.bomChildren = [];
                            vm.mBomItems.push(item);
                            var index = vm.mBomItems.indexOf(item);
                            index = populateMBOMItemChildren(item, index);
                            vm.loadingValidate = false;
                            $rootScope.hideBusyIndicator();
                        });
                    }
                )
            }

            function populateMBOMItemChildren(mBomItem, lastIndex) {
                angular.forEach(mBomItem.children, function (item) {
                    lastIndex++;
                    item.parentBom = mBomItem;
                    item.expanded = true;
                    item.level = mBomItem.level + 1;
                    item.count = 0;
                    item.bomChildren = [];
                    vm.mBomItems.splice(lastIndex, 0, item);
                    mBomItem.count = mBomItem.count + 1;
                    mBomItem.expanded = true;
                    mBomItem.bomChildren.push(item);
                    lastIndex = populateMBOMItemChildren(item, lastIndex)
                });

                return lastIndex;
            }


            function loadPlanResourcesValidate() {
                $rootScope.showBusyIndicator($("#validate-plan-resources"));
                vm.loadingValidate = true;
                BOPService.getBopPlanResourceValidate(vm.bopId).then(
                    function (data) {
                        vm.bopPlanResources = [];
                        angular.forEach(data, function (item) {
                            item.parentBop = null;
                            item.expanded = false;
                            item.level = 0;
                            item.count = 0;
                            item.bopChildren = [];
                            vm.bopPlanResources.push(item);
                            var index = vm.bopPlanResources.indexOf(item);
                            index = populateBOPPlanValidateChildren(item, index);
                        });
                        vm.loadingValidate = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function populateBOPPlanValidateChildren(bopPlan, lastIndex) {
                angular.forEach(bopPlan.children, function (item) {
                    lastIndex++;
                    item.parentBop = bopPlan;
                    item.expanded = true;
                    item.level = bopPlan.level + 1;
                    item.count = 0;
                    item.bopChildren = [];
                    vm.bopPlanResources.splice(lastIndex, 0, item);
                    bopPlan.count = bopPlan.count + 1;
                    bopPlan.expanded = true;
                    bopPlan.bopChildren.push(item);
                    lastIndex = populateBOPPlanValidateChildren(item, lastIndex)
                });
                angular.forEach(bopPlan.resources, function (resource) {
                    lastIndex++;
                    resource.parentBop = bopPlan;
                    resource.expanded = false;
                    resource.level = resource.level + 1;
                    resource.count = 0;
                    resource.bopChildren = [];
                    vm.bopPlanResources.splice(lastIndex, 0, resource);
                    bopPlan.count = bopPlan.count + 1;
                    bopPlan.expanded = true;
                    bopPlan.bopChildren.push(resource);
                });

                return lastIndex;
            }

            (function () {
                $scope.$on('app.bop.tabActivated', function (event, args) {
                    if (args.tabId == 'details.plan') {
                        loadAutoNumberSource();
                    }
                });
            })();
        }
    }
)
;




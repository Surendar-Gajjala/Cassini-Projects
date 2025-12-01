define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/shared/services/core/itemService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('PlanBasicInfoController', PlanBasicInfoController);

        function PlanBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, $window, CommonService, ItemTypeService,
                                         InspectionPlanService, ItemService, WorkflowService) {
            var vm = this;

            $scope.$sce = $sce;
            vm.loading = true;
            vm.loadingAttributes = true;
            vm.planId = $stateParams.planId;
            vm.inspectionPlan = null;
            vm.inspectionPlanRevision = null;
            vm.lifeCycles = null;
            vm.planRevisionProperties = [];
            vm.planProperties = [];
            $scope.opened = {};
            vm.external = $rootScope.loginPersonDetails;
            $rootScope.external = $rootScope.loginPersonDetails;

            var parsed = angular.element("<div></div>");

            var planUpdatedMessage = parsed.html($translate.instant("PLAN_UPDATE_MESSAGE")).html();
            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            vm.titleImage = parsed.html($translate.instant("CLICK_TO_SHOW_LARGE_IMAGE")).html();

            /*---------  To get Plan Details  ---------*/

            $rootScope.loadPlanBasicInfo = loadPlanBasicInfo;
            function loadPlanBasicInfo() {
                vm.loading = true;
                if (vm.planId != null && vm.planId != undefined) {
                    InspectionPlanService.getInspectionPlanRevision(vm.planId).then(
                        function (data) {
                            vm.inspectionPlanRevision = data;
                            $rootScope.inspectionPlanRevision = data;
                            var promise = null;
                            if (vm.inspectionPlanRevision.plan.objectType == "PRODUCTINSPECTIONPLAN") {
                                promise = InspectionPlanService.getProductInspectionPlan(vm.inspectionPlanRevision.plan.id);
                            } else {
                                promise = InspectionPlanService.getMaterialInspectionPlan(vm.inspectionPlanRevision.plan.id);
                            }
                            if (promise != null) {
                                promise.then(
                                    function (data) {
                                        vm.inspectionPlan = data;
                                        $rootScope.inspectionPlan = data;
                                        $scope.inspectionPlanName = vm.inspectionPlan.name;
                                        if (vm.inspectionPlan.description != null && vm.inspectionPlan.description != undefined) {
                                            vm.inspectionPlan.descriptionHtml = $sce.trustAsHtml(vm.inspectionPlan.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                                        }
                                        if (vm.inspectionPlan.notes != null && vm.inspectionPlan.notes != undefined) {
                                            vm.inspectionPlan.notesHtml = $sce.trustAsHtml(vm.inspectionPlan.notes.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                                        }
                                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                                            "{0}</div> <span class='item-rev'>Rev {1}</span>".
                                                format(vm.inspectionPlan.number, vm.inspectionPlanRevision.revision);
                                        $rootScope.viewInfo.description = "Status: {0}".format(vm.inspectionPlanRevision.status);
                                        loadPersons();
                                        setLifecycles();
                                        loadWorkflow();
                                        if (vm.inspectionPlan.objectType == "PRODUCTINSPECTIONPLAN") {
                                            loadInspectionPlanProduct();
                                        }
                                        vm.loading = false;
                                        $timeout(function () {
                                            $scope.$broadcast('app.attributes.tabActivated', {});
                                        }, 1000);
                                        $scope.$evalAsync();
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            $rootScope.planWorkflowStarted = false;
            function loadWorkflow() {
                if (vm.inspectionPlanRevision.workflow != null) {
                    WorkflowService.getWorkflow(vm.inspectionPlanRevision.workflow).then(
                        function (data) {
                            vm.workflow = data;
                            if (vm.workflow.started) {
                                $rootScope.planWorkflowStarted = true;
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            function loadInspectionPlanProduct() {
                ItemService.getRevisionId(vm.inspectionPlan.product).then(
                    function (data) {
                        vm.itemRevision = data;
                        ItemService.getItem(vm.itemRevision.itemMaster).then(
                            function (data) {
                                vm.inspectionPlan.productObject = data;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function setLifecycles() {
                var phases = [];
                var currentPhase = vm.inspectionPlanRevision.lifeCyclePhase.phase;
                $rootScope.lifeCycleStatus = vm.inspectionPlanRevision.lifeCyclePhase.phase;
                var defs = vm.inspectionPlan.planType.lifecycle.phases;
                defs.sort(function (a, b) {
                    return a.id - b.id;
                });
                var lastPhase = defs[defs.length - 1].phase;
                angular.forEach(defs, function (def) {
                    if (def.phase == currentPhase && lastPhase == def.phase) {
                        phases.push({
                            name: def.phase,
                            finished: true,
                            rejected: (def.phase == currentPhase && vm.inspectionPlanRevision.rejected),
                            current: (def.phase == currentPhase)
                        })
                    } else {
                        phases.push({
                            name: def.phase,
                            finished: false,
                            rejected: (def.phase == currentPhase && vm.inspectionPlanRevision.rejected),
                            current: (def.phase == currentPhase)
                        })
                    }
                });

                var index = -1;
                for (var i = 0; i < phases.length; i++) {
                    if (phases[i].current == true) {
                        index = i;
                    }
                }

                if (index > 0) {
                    for (i = 0; i < index; i++) {
                        phases[i].finished = true;
                    }
                }

                $rootScope.setLifecyclePhases(phases);
            }

            /*------  To get createdBy and CreatedDate by Ids ---------*/

            function loadPersons() {
                var personIds = [vm.inspectionPlan.createdBy];

                if (vm.inspectionPlan.createdBy != vm.inspectionPlan.modifiedBy) {
                    personIds.push(vm.inspectionPlan.modifiedBy);
                }

                CommonService.getPersons(personIds).then(
                    function (persons) {
                        var map = new Hashtable();
                        angular.forEach(persons, function (person) {
                            map.put(person.id, person);
                        });

                        if (vm.inspectionPlan.createdBy != null) {
                            var person = map.get(vm.inspectionPlan.createdBy);
                            if (person != null) {
                                vm.inspectionPlan.createdByPerson = person;
                            }
                            else {
                                vm.inspectionPlan.createdByPerson = {firstName: ""};
                            }
                        }

                        if (vm.inspectionPlan.modifiedBy != null) {
                            person = map.get(vm.inspectionPlan.modifiedBy);
                            if (person != null) {
                                vm.inspectionPlan.modifiedByPerson = person;
                            }
                            else {
                                vm.inspectionPlan.modifiedByPerson = {firstName: ""};
                            }
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            vm.updatePlan = updatePlan;
            function updatePlan() {
                if (validatePlan()) {
                    var promise = null;
                    if (vm.inspectionPlan.objectType == "PRODUCTINSPECTIONPLAN") {
                        promise = InspectionPlanService.updateProductInspectionPlan(vm.planId, vm.inspectionPlan);
                    } else {
                        promise = InspectionPlanService.updateMaterialInspectionPlan(vm.planId, vm.inspectionPlan);
                    }
                    if (promise != null) {
                        promise.then(
                            function (data) {
                                loadPlanBasicInfo();
                                $rootScope.showSuccessMessage(planUpdatedMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }


            function loadLifeCycles() {
                ItemTypeService.getLifeCyclesPhases().then(
                    function (data) {
                        vm.lifeCycles = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function validatePlan() {
                var valid = true;
                if (vm.inspectionPlan.name == null || vm.inspectionPlan.name == ""
                    || vm.inspectionPlan.name == undefined) {
                    valid = false;
                    vm.inspectionPlan.name = $scope.inspectionPlanName;
                    $rootScope.showWarningMessage(nameValidation);

                }
                return valid;
            }

            (function () {
                loadPlanBasicInfo();
                loadLifeCycles();
            })();
        }
    }
)
;
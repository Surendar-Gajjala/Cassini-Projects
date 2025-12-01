define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/shared/services/core/itemService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('InspectionBasicInfoController', InspectionBasicInfoController);

        function InspectionBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, $window, CommonService, ItemTypeService,
                                               InspectionPlanService, InspectionService, ItemService, WorkflowService) {
            var vm = this;

            $scope.$sce = $sce;
            vm.loading = true;
            vm.loadingAttributes = true;
            vm.inspectionId = $stateParams.inspectionId;
            vm.inspection = null;
            vm.inspectionPlanRevision = null;
            vm.lifeCycles = null;
            vm.planRevisionProperties = [];
            vm.planProperties = [];
            $scope.opened = {};
            vm.external = $rootScope.loginPersonDetails;
            $rootScope.external = $rootScope.loginPersonDetails;

            var parsed = angular.element("<div></div>");

            var inspectionUpdated = parsed.html($translate.instant("INSPECTION_UPDATED")).html();


            vm.updateInspection = updateInspection;
            /*---------  To get Inspection Details  ---------*/

            $rootScope.loadInspectionBasicInfo = loadInspectionBasicInfo;
            function loadInspectionBasicInfo() {
                vm.loading = true;
                InspectionService.getInspection(vm.inspectionId).then(
                    function (data) {
                        var promise = null;
                        if (data.objectType == "ITEMINSPECTION") {
                            promise = InspectionService.getItemInspection(vm.inspectionId);
                        } else {
                            promise = InspectionService.getMaterialInspection(vm.inspectionId);
                        }
                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    vm.inspection = data;
                                    $rootScope.inspection = data;
                                    $rootScope.viewInfo.title = "<div class='item-number'>" +
                                        "{0}</div> <span class='item-rev'></span>".
                                            format(vm.inspection.inspectionNumber);
                                    if (vm.inspection.deviationSummary != null && vm.inspection.deviationSummary != undefined) {
                                        vm.inspection.deviationSummaryHtml = $sce.trustAsHtml(vm.inspection.deviationSummary.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                                    }
                                    if (vm.inspection.notes != null && vm.inspection.notes != undefined) {
                                        vm.inspection.notesHtml = $sce.trustAsHtml(vm.inspection.notes.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                                    }
                                    loadPersons();
                                    loadInspectionPlan();
                                    vm.loading = false;
                                    $scope.$evalAsync();
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

            function loadInspectionPlan() {
                InspectionPlanService.getInspectionPlanRevision(vm.inspection.inspectionPlan).then(
                    function (data) {
                        vm.inspectionPlanRevision = data;
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
                                    loadPersons();
                                    loadWorkflow();
                                    if (vm.inspectionPlan.objectType == "PRODUCTINSPECTIONPLAN") {
                                        loadInspectionPlanProduct();
                                    }
                                    vm.loading = false;
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

            $rootScope.inspectionWorkflowStarted = false;
            function loadWorkflow() {
                WorkflowService.getWorkflow(vm.inspection.workflow).then(
                    function (data) {
                        vm.workflow = data;
                        if (vm.workflow.started) {
                            $rootScope.inspectionWorkflowStarted = true;
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            function loadInspectionPlanProduct() {
                ItemService.getRevisionId(vm.inspectionPlan.product).then(
                    function (data) {
                        vm.itemRevision = data;
                        ItemService.getItem(vm.itemRevision.itemMaster).then(
                            function (data) {
                                vm.inspectionPlan.productObject = data;
                            }
                        )
                    }
                )
            }

            /*------  To get createdBy and CreatedDate by Ids ---------*/

            function loadPersons() {
                var personIds = [vm.inspection.createdBy];

                if (vm.inspection.createdBy != vm.inspection.modifiedBy) {
                    personIds.push(vm.inspection.modifiedBy);
                }
                personIds.push(vm.inspection.assignedTo);

                CommonService.getPersons(personIds).then(
                    function (persons) {
                        var map = new Hashtable();
                        angular.forEach(persons, function (person) {
                            map.put(person.id, person);
                        });

                        if (vm.inspection.createdBy != null) {
                            var person = map.get(vm.inspection.createdBy);
                            if (person != null) {
                                vm.inspection.createdByPerson = person;
                            }
                            else {
                                vm.inspection.createdByPerson = {firstName: ""};
                            }
                        }

                        if (vm.inspection.modifiedBy != null) {
                            person = map.get(vm.inspection.modifiedBy);
                            if (person != null) {
                                vm.inspection.modifiedByPerson = person;
                            }
                            else {
                                vm.inspection.modifiedByPerson = {firstName: ""};
                            }
                        }
                        if (vm.inspection.assignedTo != null) {
                            person = map.get(vm.inspection.assignedTo);
                            if (person != null) {
                                vm.inspection.assignedToObject = person;
                            }
                            else {
                                vm.inspection.assignedToObject = {firstName: ""};
                            }
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function updateInspection() {
                $rootScope.showBusyIndicator($(".view-container"));
                var promise = null;
                if (vm.inspection.objectType == "ITEMINSPECTION") {
                    promise = InspectionService.updateItemInspection(vm.inspectionId, vm.inspection);
                } else {
                    promise = InspectionService.updateMaterialInspection(vm.inspectionId, vm.inspection);
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            loadInspectionBasicInfo();
                            $rootScope.showSuccessMessage(inspectionUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                loadInspectionBasicInfo();
            })();
        }
    }
)
;
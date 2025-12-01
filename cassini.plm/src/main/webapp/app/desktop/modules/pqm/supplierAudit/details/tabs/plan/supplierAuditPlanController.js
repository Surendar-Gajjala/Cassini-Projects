define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/shared/services/core/supplierAuditService'
    ],
    function (module) {
        module.controller('SupplierAuditPlanController', SupplierAuditPlanController);

        function SupplierAuditPlanController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application,
                                             SupplierAuditService, DialogService) {
            var vm = this;
            vm.loading = true;
            vm.supplierAuditId = $stateParams.supplierAuditId;
            var parsed = angular.element("<div></div>");
            var selectSuppliersTitle = parsed.html($translate.instant("SELECT_SUPPLIERS")).html();
            var suppliersAddMsg = parsed.html($translate.instant("SUPPLIERS_ADDED_MSG")).html();
            var selectPlanFinishDate = parsed.html($translate.instant("SELECT_PLAN_FINISH_DATE")).html();
            var finishDateValidation = parsed.html($translate.instant("PLAN_COMPLETED_DATE_VALIDATION")).html();
            var supplierUpdateMsg = parsed.html($translate.instant("SUPPLIER_UPDATED_SUCCESS_MESSAGE")).html();
            var supplierRemovedMsg = parsed.html($translate.instant("SUPPLIER_REMOVED_MSG")).html();
            var removeSupplierTitle = parsed.html($translate.instant("REMOVE_SUPPLIER")).html();
            var removeSupplierDialogMsg = parsed.html($translate.instant("REMOVE_SUPPLIER_MSG")).html();
            $scope.showPlanDetailsTitle = parsed.html($translate.instant("SHOW_PLAN_DETAILS")).html();

            var emptyPlan = {
                id: null,
                supplier: null,
                supplierAudit: vm.supplierAuditId
            };

            vm.months = [
                {label: "Jan", value: 0, plannedDate: null, completedDate: null},
                {label: "Feb", value: 1, plannedDate: null, completedDate: null},
                {label: "Mar", value: 2, plannedDate: null, completedDate: null},
                {label: "Apr", value: 3, plannedDate: null, completedDate: null},
                {label: "May", value: 4, plannedDate: null, completedDate: null},
                {label: "Jun", value: 5, plannedDate: null, completedDate: null},
                {label: "Jul", value: 6, plannedDate: null, completedDate: null},
                {label: "Aug", value: 7, plannedDate: null, completedDate: null},
                {label: "Sep", value: 8, plannedDate: null, completedDate: null},
                {label: "Oct", value: 9, plannedDate: null, completedDate: null},
                {label: "Nov", value: 10, plannedDate: null, completedDate: null},
                {label: "Dec", value: 11, plannedDate: null, completedDate: null}
            ];

            vm.planStatuses = ["PLANNED", "COMPLETED"];

            $scope.setPlannedDate = setPlannedDate;
            function setPlannedDate(plan, selectedMonth, dateText) {
                angular.forEach(plan.months, function (month) {
                    if (selectedMonth.value != month.value) {
                        month.plannedDate = null;
                    }
                })
                plan.plannedStartDate = dateText;
            }

            $scope.setCompletedDate = setCompletedDate;
            function setCompletedDate(plan, selectedMonth, dateText) {
                angular.forEach(plan.months, function (month) {
                    if (selectedMonth.value != month.value) {
                        month.completedDate = null;
                    }
                })
                plan.finishedDate = dateText;
            }

            $rootScope.loadPlan = loadPlan;
            function loadPlan() {
                SupplierAuditService.getSupplierAuditPlans(vm.supplierAuditId).then(
                    function (data) {
                        vm.auditPlans = data;
                        angular.forEach(vm.auditPlans, function (plan) {
                            var plannedMonth = null;
                            var finishedMonth = null;
                            plan.editMode = false;
                            plan.months = angular.copy(vm.months);

                            if (plan.plannedStartDate != null && plan.plannedStartDate != "") {
                                plannedMonth = plan.plannedStartDate.split("/")[1];
                            }
                            if (plan.finishedDate != null && plan.finishedDate != "") {
                                finishedMonth = plan.finishedDate.split("/")[1];
                            }
                            angular.forEach(plan.months, function (month) {
                                if (month.value == (parseInt(plannedMonth) - 1)) {
                                    month.plannedDate = plan.plannedStartDate;
                                }
                                if (month.value == (parseInt(finishedMonth) - 1)) {
                                    month.completedDate = plan.finishedDate;
                                }
                            })
                        })
                        vm.loading = false;
                    }
                )
            }

            vm.editPlan = editPlan;
            vm.deletePlan = deletePlan;
            vm.cancelChanges = cancelChanges;
            vm.savePlan = savePlan;

            function editPlan(plan) {
                plan.editMode = true;
                plan.oldMonths = angular.copy(plan.months);
                plan.oldPlannedStartDate = plan.plannedStartDate;
                plan.oldFinishedDate = plan.finishedDate;
            }

            function deletePlan(plan) {
                var options = {
                    title: removeSupplierTitle,
                    message: removeSupplierDialogMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        var promise = null;
                        $rootScope.showBusyIndicator($('.view-container'));
                        SupplierAuditService.deleteSupplierAuditPlan(vm.supplierAuditId, plan).then(
                            function (data) {
                                loadPlan();
                                $rootScope.loadSupplierAuditCounts();
                                $rootScope.showSuccessMessage(supplierRemovedMsg);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            function cancelChanges(plan) {
                plan.editMode = false;
                plan.months = plan.oldMonths;
                plan.plannedStartDate = plan.oldPlannedStartDate;
                plan.finishedDate = plan.oldFinishedDate;
            }

            function savePlan(plan) {
                if (validatePlan(plan)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    SupplierAuditService.updateSupplierAuditPlan(vm.supplierAuditId, plan).then(
                        function (data) {
                            plan.status = data.status;
                            plan.plannedStartDate = data.plannedStartDate;
                            plan.finishedDate = data.finishedDate;
                            var plannedMonth = null;
                            var finishedMonth = null;
                            plan.editMode = false;

                            if (plan.plannedStartDate != null && plan.plannedStartDate != "") {
                                plannedMonth = plan.plannedStartDate.split("/")[1];
                            }
                            if (plan.finishedDate != null && plan.finishedDate != "") {
                                finishedMonth = plan.finishedDate.split("/")[1];
                            }
                            angular.forEach(plan.months, function (month) {
                                if (month.value == (parseInt(plannedMonth) - 1)) {
                                    month.plannedDate = plan.plannedStartDate;
                                }
                                if (month.value == (parseInt(finishedMonth) - 1)) {
                                    month.completedDate = plan.finishedDate;
                                }
                            });
                            $rootScope.loadSupplierAudit();
                            $rootScope.showSuccessMessage(supplierUpdateMsg);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validatePlan(plan) {
                var valid = true;
                if (plan.finishedDate != null && plan.finishedDate != "" && (plan.plannedStartDate == null || plan.plannedStartDate == "")) {
                    valid = false;
                    $rootScope.showWarningMessage(selectPlanFinishDate);
                } else if (plan.plannedStartDate != null && plan.plannedStartDate != "" && plan.finishedDate != null && plan.finishedDate != "") {
                    var today = moment(new Date());
                    var todayStr = today.format($rootScope.applicationDateSelectFormat);
                    var todayDate = moment(todayStr, $rootScope.applicationDateSelectFormat);
                    var plannedFinishDate = moment(plan.finishedDate, $rootScope.applicationDateSelectFormat);
                    var val = plannedFinishDate.isAfter(todayDate);
                    if (val) {
                        valid = false;
                        $rootScope.showWarningMessage(finishDateValidation);
                    }
                }

                return valid;
            }

            vm.addSuppliers = addSuppliers;
            function addSuppliers() {
                vm.selectedSuppliers = [];
                var options = {
                    title: selectSuppliersTitle,
                    template: 'app/desktop/modules/pqm/supplierAudit/details/tabs/plan/supplierSelectionView.jsp',
                    controller: 'SupplierSelectionController as supplierSelectionVm',
                    resolve: 'app/desktop/modules/pqm/supplierAudit/details/tabs/plan/supplierSelectionController',
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: $rootScope.add, broadcast: 'add.select.suppliers'}
                    ],
                    callback: function (result) {
                        angular.forEach(result, function (source) {
                            var plan = angular.copy(emptyPlan);
                            plan.supplier = source.id;
                            vm.selectedSuppliers.push(plan);
                        })
                        SupplierAuditService.createMultipleSupplierAuditPlans(vm.supplierAuditId, vm.selectedSuppliers).then(
                            function (data) {
                                vm.selectedSuppliers = [];
                                $rootScope.showSuccessMessage(suppliersAddMsg);
                                loadPlan();
                                $rootScope.loadSupplierAudit();
                                $rootScope.loadSupplierAuditCounts();
                                $rootScope.hideSidePanel();
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.showSupplierDetails = showSupplierDetails;
            function showSupplierDetails(plan) {
                $state.go('app.mfr.supplier.details', {supplierId: plan.supplier, tab: 'details.basic'});
            }

            vm.showSupplierPlanDetails = showSupplierPlanDetails;
            function showSupplierPlanDetails(plan) {
                var options = {
                    title: plan.name + " Details",
                    template: 'app/desktop/modules/pqm/supplierAudit/details/tabs/plan/supplierPlanDetailsView.jsp',
                    controller: 'SupplierPlanDetailsController as supplierPlanDetailsVm',
                    resolve: 'app/desktop/modules/pqm/supplierAudit/details/tabs/plan/supplierPlanDetailsController',
                    width: 700,
                    showMask: true,
                    data: {
                        supplierAuditPlan: plan
                    }
                };

                $rootScope.showSidePanel(options);
            }

            (function () {
                $scope.$on('app.supplierAudit.tabActivated', function (event, data) {
                    if (data.tabId == 'details.plan') {
                        loadPlan();
                    }
                })
            })();
        }
    }
);


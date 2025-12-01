define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/shared/services/core/customerSupplierService',
        'app/shared/services/core/supplierService',
        'app/shared/services/core/inspectionService',
        'app/shared/services/core/qualityTypeService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ProblemReportBasicInfoController', ProblemReportBasicInfoController);

        function ProblemReportBasicInfoController($scope, $rootScope, $sce, $application, $timeout, $state, $translate, $stateParams, $cookies, $window, ItemTypeService, CommonService,
                                                  ProblemReportService, ItemService, CustomerSupplierService, SupplierService, InspectionService, QualityTypeService) {
            var vm = this;

            $scope.$sce = $sce;
            vm.loading = true;
            vm.loadingAttributes = true;
            vm.problemReportId = $stateParams.problemReportId;
            vm.problemReport = null;
            vm.lifeCycles = null;
            $scope.opened = {};

            vm.external = $rootScope.loginPersonDetails;
            $rootScope.external = $rootScope.loginPersonDetails;

            vm.updateProblemReport = updateProblemReport;
            var parsed = angular.element("<div></div>");
            var prUpdated = parsed.html($translate.instant("PR_UPDATED")).html();
            var problemCannotBeEmpty = parsed.html($translate.instant("PROBLEM_NOT_EMPTY")).html();
            var descriptionCannotBeEmpty = parsed.html($translate.instant("DESCRIPTION_NOT_EMPTY")).html();

            $rootScope.loadProblemReportBasicInfo = loadProblemReportBasicInfo;
            function loadProblemReportBasicInfo() {
                vm.loading = true;
                if (vm.problemReportId != null && vm.problemReportId != undefined) {
                    ProblemReportService.getProblemReport(vm.problemReportId).then(
                        function (data) {
                            vm.problemReport = data;
                            $rootScope.problemReport = vm.problemReport;
                            if (vm.problemReport.problem != null && vm.problemReport.problem != undefined) {
                                vm.problemReport.problemHtml = $sce.trustAsHtml(vm.problemReport.problem.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            if (vm.problemReport.description != null && vm.problemReport.description != undefined) {
                                vm.problemReport.descriptionHtml = $sce.trustAsHtml(vm.problemReport.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            if (vm.problemReport.stepsToReproduce != null && vm.problemReport.stepsToReproduce != undefined) {
                                vm.problemReport.stepsToReproduceHtml = $sce.trustAsHtml(vm.problemReport.stepsToReproduce.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            vm.copyReport = angular.copy(vm.problemReport);
                            $rootScope.problemReport = vm.problemReport;
                            loadPersons();
                            loadProduct();
                            if (vm.problemReport.inspection != null) {
                                loadInspection();
                            }
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            vm.loading = false;
                            $scope.$evalAsync();
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            /*------  To get createdBy and CreatedDate by Ids ---------*/

            function loadPersons() {
                var personIds = [vm.problemReport.createdBy];

                if (vm.problemReport.createdBy != vm.problemReport.modifiedBy) {
                    personIds.push(vm.problemReport.modifiedBy);
                }
                if (vm.problemReport.reportedBy != null && vm.problemReport.reporterType == "INTERNAL") {
                    personIds.push(vm.problemReport.reportedBy);
                }

                personIds.push(vm.problemReport.qualityAnalyst);

                CommonService.getPersons(personIds).then(
                    function (persons) {
                        var map = new Hashtable();
                        angular.forEach(persons, function (person) {
                            map.put(person.id, person);
                        });

                        if (vm.problemReport.createdBy != null) {
                            var person = map.get(vm.problemReport.createdBy);
                            if (person != null) {
                                vm.problemReport.createdByPerson = person;
                            }
                            else {
                                vm.problemReport.createdByPerson = {firstName: ""};
                            }
                        }

                        if (vm.problemReport.modifiedBy != null) {
                            person = map.get(vm.problemReport.modifiedBy);
                            if (person != null) {
                                vm.problemReport.modifiedByPerson = person;
                            }
                            else {
                                vm.problemReport.modifiedByPerson = {firstName: ""};
                            }
                        }
                        if (vm.problemReport.reportedBy != null && vm.problemReport.reporterType == "INTERNAL") {
                            person = map.get(vm.problemReport.reportedBy);
                            if (person != null) {
                                vm.problemReport.reportedByObject = person;
                            }
                            else {
                                vm.problemReport.reportedByObject = {firstName: ""};
                            }
                        }
                        if (vm.problemReport.qualityAnalyst != null) {
                            person = map.get(vm.problemReport.qualityAnalyst);
                            if (person != null) {
                                vm.problemReport.qualityAnalystObject = person;
                            }
                            else {
                                vm.problemReport.qualityAnalystObject = {firstName: ""};
                            }
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
                if (vm.problemReport.reporterType == "CUSTOMER" && vm.problemReport.reportedBy != null) {
                    CustomerSupplierService.getCustomer(vm.problemReport.reportedBy).then(
                        function (data) {
                            vm.problemReport.reportedByObject = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else if (vm.problemReport.reporterType == "SUPPLIER" && vm.problemReport.reportedBy != null) {
                    SupplierService.getSupplier(vm.problemReport.reportedBy).then(
                        function (data) {
                            vm.problemReport.reportedByObject = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadProduct() {
                if (vm.problemReport.product != null && vm.problemReport.product != "") {
                    ItemService.getItemByRevision(vm.problemReport.product).then(
                        function (data) {
                            vm.problemReport.productObject = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadInspection() {
                InspectionService.getInspection(vm.problemReport.inspection).then(
                    function (data) {
                        vm.problemReport.inspectionObject = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadLifeCycles() {
                ItemTypeService.getLifeCyclesPhases().then(
                    function (data) {
                        vm.lifeCycles = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function updateProblemReport() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.problemReport.qualityAnalyst = vm.problemReport.qualityAnalystObject.id;
                    ProblemReportService.updateProblemReport(vm.problemReportId, vm.problemReport).then(
                        function (data) {
                            loadProblemReportBasicInfo();
                            $rootScope.showSuccessMessage(prUpdated);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;

                if (vm.problemReport.problem == null || vm.problemReport.problem == "" || vm.problemReport.problem == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(problemCannotBeEmpty);
                    vm.problemReport.problem = vm.copyReport.problem;
                } else if (vm.problemReport.description == null || vm.problemReport.description == "" || vm.problemReport.description == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(descriptionCannotBeEmpty);
                    vm.problemReport.description = vm.copyReport.description;
                }

                return valid;
            }

            function loadQualityAnalysts() {
                vm.qualityAnalysts = [];
                var preference = $application.defaultValuesPreferences.get("DEFAULT_QUALITY_ANALYST_ROLE");
                if (preference != null && preference.defaultValueName != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.problemreport.all";
                    QualityTypeService.getPersonByGroupNameAndPermission(groupName, permission).then(
                        function (data) {
                            vm.qualityAnalysts = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                loadQualityAnalysts();
                loadProblemReportBasicInfo();
                loadLifeCycles();
            })();
        }
    }
)
;
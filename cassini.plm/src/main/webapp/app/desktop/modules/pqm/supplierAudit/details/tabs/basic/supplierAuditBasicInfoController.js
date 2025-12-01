define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/shared/services/core/supplierAuditService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('SupplierAuditBasicInfoController', SupplierAuditBasicInfoController);

        function SupplierAuditBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, CommonService,
                                                  $translate, SupplierAuditService, LoginService) {
            var vm = this;
            vm.loading = true;
            vm.supplierAuditId = $stateParams.supplierAuditId;
            vm.supplierAudit = null;
            var parsed = angular.element("<div></div>");

            var supplierAuditNameValidation = parsed.html($translate.instant("TITLE_VALIDATION")).html();
            var supplierAuditAssignedToValidation = parsed.html($translate.instant("ASSIGNEDTO_CANNOT_BE_EMPTY")).html();
            var supplierAuditplannedYearValidation = parsed.html($translate.instant("PLANNED_YEAR_CANNOT_BE_EMPTY")).html();
            var supplierAuditUpdated = parsed.html($translate.instant("SUPPLIER_AUDIT_UPDATED")).html();
            vm.updateSupplierAudit = updateSupplierAudit;
            $rootScope.loadSupplierAuditBasicDetails = loadSupplierAuditBasicDetails;

            function loadSupplierAuditBasicDetails() {
                vm.loading = true;
                if (vm.supplierAuditId != null && vm.supplierAuditId != undefined) {
                    SupplierAuditService.getSupplierAudit(vm.supplierAuditId).then(
                        function (data) {
                            vm.supplierAudit = data;
                            $rootScope.supplierAudit = data;
                            $scope.name = vm.supplierAudit.name;
                            CommonService.getPersonReferences([vm.supplierAudit], 'createdBy');
                            CommonService.getPersonReferences([vm.supplierAudit], 'modifiedBy');
                            CommonService.getPersonReferences([vm.supplierAudit], 'assignedTo');

                            if (vm.supplierAudit.description != null && vm.supplierAudit.description != undefined) {
                                vm.supplierAudit.descriptionHtml = $sce.trustAsHtml(vm.supplierAudit.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);

                            vm.loading = false;
                            $rootScope.viewInfo.title = $translate.instant("SUPPLIER_AUDIT_DETAILS");
                            $rootScope.viewInfo.description = "Name: " + data.name;
                            loadPersons();
                            $scope.$evalAsync();
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
                vm.loading = false;
            }

            vm.editPlannedYear = false;
            function updateSupplierAudit() {
                $rootScope.showBusyIndicator();
                if (validate()) {
                    vm.supplierAudit.assignedTo = vm.supplierAudit.assignedToObject.id;
                    SupplierAuditService.updateSupplierAudit(vm.supplierAudit).then(
                        function (data) {
                            loadSupplierAuditBasicDetails();
                            vm.editPlannedYear = false;
                            $rootScope.showSuccessMessage(supplierAuditUpdated);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    loadSupplierAuditBasicDetails();
                }
            }

            function validate() {
                var valid = true;

                if (vm.supplierAudit.name == null || vm.supplierAudit.name == undefined ||
                    vm.supplierAudit.name == "") {
                    $rootScope.showWarningMessage(supplierAuditNameValidation);
                    valid = false;
                }

                else if (vm.supplierAudit.assignedTo == null || vm.supplierAudit.assignedTo == undefined ||
                    vm.supplierAudit.assignedTo == "") {
                    $rootScope.showWarningMessage(supplierAuditAssignedToValidation);
                    valid = false;
                } 
                else if (vm.supplierAudit.plannedYear == null || vm.supplierAudit.plannedYear == undefined ||
                    vm.supplierAudit.plannedYear == "") {
                    $rootScope.showWarningMessage(supplierAuditplannedYearValidation);
                    valid = false;
                }
                return valid;
            }

            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && !login.external) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            (function () {
                $scope.$on('app.supplierAudit.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadSupplierAuditBasicDetails();
                    }
                });

            })();
        }
    }
);
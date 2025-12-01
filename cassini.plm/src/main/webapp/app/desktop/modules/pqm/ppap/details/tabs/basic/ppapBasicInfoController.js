define([
    'app/desktop/modules/pqm/pqm.module',
    'app/shared/services/core/supplierService',
    'app/shared/services/core/mfrPartsService',
    'app/shared/services/core/ppapService',
    'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
    'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'

], function (module) {
    module.controller('PPAPBasicInfoController', PPAPBasicInfoController);

    function PPAPBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $application, $translate, $stateParams, $cookies, $window, ItemTypeService, CommonService, SupplierService, MfrPartsService,
                                     PpapService) {
        var vm = this;
        vm.ppapId = $stateParams.ppapId;
        vm.ppap = null;
        vm.updatePpap = updatePpap;
        vm.updateSupplier = updateSupplier
        vm.loadParts = loadParts;
        vm.ppapCountNumber = $rootScope.ppapCountNumber

        function updateSupplier(id) {
            $rootScope.showBusyIndicator();
            SupplierService.getSupplier(id).then(
                function (data) {
                    vm.supplier = data;
                    vm.ppap.supplierName = vm.supplier.name;
                    vm.ppap.mfrPart.partName = null;
                    loadParts(vm.supplier.id)
                    $rootScope.hideBusyIndicator();
                }, function (error) {
                    $rootScope.showErrorMessage(error.message);
                    $rootScope.hideBusyIndicator();
                })

        }

        function loadParts(id) {
            SupplierService.getSupplierParts(id).then(
                function (data) {
                    vm.supplierParts = data;
                    $rootScope.hideBusyIndicator();
                }, function (error) {
                    $rootScope.showErrorMessage(error.message);
                    $rootScope.hideBusyIndicator();
                }
            )
        }

        vm.pageable = {
            page: 0,
            size: 20,
            sort: {
                field: "modifiedDate",
                order: "DESC"
            }
        };

        vm.filters = {
            partNumber: null,
            partName: null,
            description: null,
            mfrPartType: '',
            manufacturer: '',
            freeTextSearch: true,
            searchQuery: null
        };

        var parsed = angular.element("<div></div>");
        var ppapUpdated = parsed.html($translate.instant("PPAP_UPDATED")).html();
        $scope.selectManufacturer = parsed.html($translate.instant("SELECT")).html();
        var pappTypeValidation = parsed.html($translate.instant("TYPE_VALIDATION")).html();
        var ppapNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
        var pappNumberValidation = parsed.html($translate.instant("NUMBER_CANNOT_BE_EMPTY")).html();
        var selectSupplierValidation = parsed.html($translate.instant("SELECT_SUPPLIER_VALIDATION")).html();
        var selectPartValidation = parsed.html($translate.instant("SELECT_PART_VALIDATION")).html();


        function updatePpap() {
            if (validate()) {
                $rootScope.showBusyIndicator();
                PpapService.updatePpap(vm.ppap).then(
                    function (data) {
                        vm.ppap = data;
                        $rootScope.hideBusyIndicator();
                        $rootScope.showSuccessMessage(ppapUpdated);
                        loadPpap();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            } else {
                loadPpap();
            }
        }

        function validate() {
            var valid = true;
            if (vm.ppap.type == null || vm.ppap.type == undefined ||
                vm.ppap.type == "") {
                $rootScope.showWarningMessage(pappTypeValidation);
                valid = false;
            }
            else if (vm.ppap.number == null || vm.ppap.number == undefined ||
                vm.ppap.number == "") {
                $rootScope.showWarningMessage(pappNumberValidation);
                valid = false;
            }
            else if (vm.ppap.supplier == null || vm.ppap.supplier == undefined ||
                vm.ppap.supplier == "") {
                $rootScope.showWarningMessage(selectSupplierValidation);
                valid = false;
            }
            else if (vm.ppap.supplierPart == null || vm.ppap.supplierPart == undefined ||
                vm.ppap.supplierPart == "") {
                $rootScope.showWarningMessage(selectPartValidation);
                valid = false;
            }
            else if (vm.ppap.name == null || vm.ppap.name == undefined ||
                vm.ppap.name == "") {
                $rootScope.showWarningMessage(ppapNameValidation);
                valid = false;
            }
            return valid;
        }


        vm.suppliers = [];
        function loadSuppliers() {
            SupplierService.getApprovedSuppliers().then(
                function (data) {
                    vm.suppliers = data;
                }, function (error) {
                    $rootScope.showErrorMessage(error.message);
                }
            )
        }


        vm.supplierParts = [];
        function loadSupplierParts() {
            SupplierService.getSupplierParts(vm.supplierId).then(
                function (data) {
                    vm.supplierParts = data;
                    $rootScope.hideBusyIndicator();
                }, function (error) {
                    $rootScope.showErrorMessage(error.message);
                    $rootScope.hideBusyIndicator();
                }
            )
        }


        function loadPpap() {
            $rootScope.showBusyIndicator();
            vm.loading = true;
            PpapService.getPpap(vm.ppapId).then(
                function (data) {
                    vm.ppap = data;
                    vm.supplierId = vm.ppap.supplier;
                    vm.loading = false;
                    $rootScope.ppap = vm.ppap;
                    $scope.name = vm.ppap.name;
                    CommonService.getPersonReferences([vm.ppap], 'modifiedBy');
                    CommonService.getPersonReferences([vm.ppap], 'createdBy');
                    loadSupplierParts();
                    $timeout(function () {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }, 1000);
                    $rootScope.hideBusyIndicator();
                }, function (error) {
                    $rootScope.showErrorMessage(error.message);
                    $rootScope.hideBusyIndicator();
                }
            )
        }


        (function () {
            $scope.$on('app.ppap.tabActivated', function (event, data) {
                if (data.tabId == 'details.basic') {
                    loadSuppliers();
                    loadPpap();
                }
            });
        })();

    }
});
define([
    'app/desktop/modules/pqm/pqm.module',
    'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
    'app/shared/services/core/supplierAuditService'
], function (module) {
    module.controller('AllSupplierAuditController', AllSupplierAuditController);

    function AllSupplierAuditController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce, ObjectTypeAttributeService, SupplierAuditService, CommonService) {
        var vm = this;
        $rootScope.viewInfo.showDetails = false;
        vm.loading = false;
        vm.newSupplierAudit = newSupplierAudit;
        vm.showSupplierAudit = showSupplierAudit;
        vm.deleteSupplierAudit = deleteSupplierAudit;
        vm.objectIds = [];
        vm.selectedAttributes = [];
        vm.pageable = {
            page: 0,
            size: 20,
            sort: {
                field: "modifiedDate",
                order: "DESC"
            }
        };

        var pagedResults = {
            content: [],
            last: true,
            totalPages: 0,
            totalElements: 0,
            size: vm.pageable.size,
            number: 0,
            sort: null,
            first: true,
            numberOfElements: 0
        };

        vm.supplierAuditFilter = {
            number: null,
            name: '',
            description: null,
            searchQuery: null,

        }

        vm.supplierAudits = angular.copy(pagedResults);
        vm.nextPage = nextPage;
        vm.previousPage = previousPage;
        vm.freeTextSearch = freeTextSearch;
        vm.resetPage = resetPage;
        var currencyMap = new Hashtable();


        var parsed = angular.element("<div></div>");
        var createButton = parsed.html($translate.instant("CREATE")).html();
        var newSupplierAuditTitle = parsed.html($translate.instant("NEW_SUPPLIER_AUDIT")).html();
        var supplierAuditDeletedMessage = parsed.html($translate.instant("SUPPLIER_AUDIT_DELETED_MESSAGE")).html();
        var deleteDialogTitle = parsed.html($translate.instant("DELETE_SUPPLIER_AUDIT")).html();
        var deleteSupplierAuditDialogMessage = parsed.html($translate.instant("DELETE_SUPPLIER_AUDIT_DIALOG_MESSAGE")).html();
        $scope.cannotDeleteApprovedSupplierAudit = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_SUPPLIER_AUDIT")).html();


        function nextPage() {
            if (vm.supplierAudits.last != true) {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.pageable.page++;
                vm.flag = false;
                loadAllSupplierAudits();
            }
        }

        function previousPage() {
            if (vm.supplierAudits.first != true) {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.pageable.page--;
                vm.flag = false;
                loadAllSupplierAudits();
            }
        }

        vm.pageSize = pageSize;
        function pageSize(page) {
            vm.pageable.page = 0;
            vm.pageable.size = page;
            loadAllSupplierAudits();
        }

        function freeTextSearch(freeText) {
            vm.pageable.page = 0;
            $rootScope.showBusyIndicator($('.view-container'));
            if (freeText != null && freeText != "" && freeText != undefined) {
                vm.supplierAuditFilter.searchQuery = freeText;
                $scope.freeTextQuery = freeText;
                loadAllSupplierAudits();
            } else {
                resetPage();
            }
        }

        function resetPage() {
            vm.supplierAudits = angular.copy(pagedResults);
            vm.pageable.page = 0;
            vm.supplierAuditFilter.searchQuery = null;
            $scope.freeTextQuery = null;
            $rootScope.showBusyIndicator($('.view-container'));
            loadAllSupplierAudits();
        }


        function newSupplierAudit() {
            var options = {
                title: newSupplierAuditTitle,
                template: 'app/desktop/modules/pqm/supplierAudit/new/newSupplierAuditView.jsp',
                controller: 'NewSupplierAuditController as newSupplierAuditVm',
                resolve: 'app/desktop/modules/pqm/supplierAudit/new/newSupplierAuditController',
                width: 600,
                showMask: true,
                buttons: [
                    {text: createButton, broadcast: 'app.supplierAudit.new'}
                ],
                callback: function (supplierAudit) {
                    $timeout(function () {
                        loadAllSupplierAudits();
                    }, 500);
                }
            };

            $rootScope.showSidePanel(options);
        }


        function loadAllSupplierAudits() {
            $rootScope.showBusyIndicator();
            vm.loading = true;
            SupplierAuditService.getAllSupplierAudits(vm.pageable, vm.supplierAuditFilter).then(
                function (data) {
                    vm.supplierAudits = data;
                    vm.loading = false;
                    CommonService.getMultiplePersonReferences(vm.supplierAudits.content, ['createdBy', 'modifiedBy']);
                    loadAttributeValues();
                    $rootScope.hideBusyIndicator();
                }, function (error) {
                    $rootScope.showErrorMessage(error.message);
                    $rootScope.hideBusyIndicator();
                }
            )
        }
        var supplierAuditAttributeTitle = parsed.html($translate.instant("ATTRIBUTES")).html();
        var selectedAttributesAdded = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
        var addButton = parsed.html($translate.instant("ADD")).html();

        vm.showTypeAttributes = showTypeAttributes;
        function showTypeAttributes() {
            var options = {
                title: supplierAuditAttributeTitle,
                template: 'app/desktop/modules/pqm/attributes/qualityTypeAttributeSelectionView.jsp',
                resolve: 'app/desktop/modules/pqm/attributes/qualityTypeAttributeSelectionController',
                controller: 'QualityTypeAttributeSelectionController as qualityTypeAttributeSelectionVm',
                width: 500,
                showMask: true,
                data: {
                    selectedAttributes: vm.selectedAttributes,
                    selectedObjectType: "SUPPLIERAUDITTYPE",
                    selectedObject: "SUPPLIERAUDIT",
                    selectedParentObjectType: "QUALITY"
                },
                buttons: [
                    {text: addButton, broadcast: 'add.quality.attributes.select'}
                ],
                callback: function (result) {
                    vm.selectedAttributes = result;
                    $window.localStorage.setItem("supplierAuditAttributes", JSON.stringify(vm.selectedAttributes));
                    if (result.length > 0) {
                        $rootScope.showSuccessMessage(selectedAttributesAdded);
                    }
                    loadAllSupplierAudits();
                }
            };

            $rootScope.showSidePanel(options);
        }
            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("supplierAuditAttributes", JSON.stringify(vm.selectedAttributes));

            }
            vm.objectIds = [];
            vm.attributeIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.supplierAudits.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.supplierAudits.content);

            }


        function deleteSupplierAudit(supplierAudit) {
            var options = {
                title: deleteDialogTitle,
                message: deleteSupplierAuditDialogMessage + " [" + supplierAudit.number + "] ?",
                okButtonClass: 'btn-danger'
            };

            DialogService.confirm(options, function (yes) {
                if (yes == true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    SupplierAuditService.deleteSupplierAudit(supplierAudit.id).then(
                        function (data) {
                            $rootScope.showSuccessMessage(supplierAuditDeletedMessage);
                            loadAllSupplierAudits();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            });
        }

        function showSupplierAudit(supplierAudit) {
            $state.go('app.pqm.supplierAudit.details', {
                supplierAuditId: supplierAudit.id,
                tab: 'details.basic'
            });
        }


        function validateJSON() {
            try {
                JSON.parse($window.localStorage.getItem("supplierAuditAttributes"));
            } catch (e) {
                return false;
            }
            return true;
        }

        (function () {
            angular.forEach($application.currencies, function (data) {
                currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
            })
            var setAttributes = null;
            if (validateJSON()) {
                setAttributes = JSON.parse($window.localStorage.getItem("supplierAuditAttributes"));
            } else {
                setAttributes = null;
            }
            if (setAttributes != null && setAttributes != undefined) {
                angular.forEach(setAttributes, function (setAtt) {
                    if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                        vm.objectIds.push(setAtt.id);
                    }
                });
                ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                    function (data) {
                        if (data.length == 0) {
                            setAttributes = null;
                            $window.localStorage.setItem("supplierAuditAttributes", "");
                            vm.selectedAttributes = setAttributes
                        } else {
                            vm.selectedAttributes = setAttributes;
                        }
                        loadAllSupplierAudits();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            } else {
                loadAllSupplierAudits();
            }
        })();

    }

});
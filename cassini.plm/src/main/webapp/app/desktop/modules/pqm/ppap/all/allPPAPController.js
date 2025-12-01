define([
    'app/desktop/modules/pqm/pqm.module',
    'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
    'app/shared/services/core/ppapService'
], function (module) {
    module.controller('AllPPAPController', AllPPAPController);

    function AllPPAPController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, CommonService, ObjectTypeAttributeService, $application, $stateParams, $cookies, $sce,
                               PpapService) {
        var vm = this;
        $rootScope.viewInfo.title = "";
        $rootScope.viewInfo.showDetails = false;
        vm.loading = false;
        vm.newPPap = newPPap;
        vm.showPpap = showPpap;
        vm.deletePPAP = deletePPAP;
        vm.ppap = [];
        vm.objectIds = [];
        vm.selectedAttributes = [];
        var currencyMap = new Hashtable();
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

        vm.ppapFilter = {
            number: null,
            name: '',
            description: null,
            searchQuery: null,

        }

        vm.ppaps = angular.copy(pagedResults);
        vm.nextPage = nextPage;
        vm.previousPage = previousPage;
        vm.freeTextSearch = freeTextSearch;
        vm.resetPage = resetPage;

        var parsed = angular.element("<div></div>");
        var createButton = parsed.html($translate.instant("CREATE")).html();
        var deleteDialogTitle = parsed.html($translate.instant("DELETE_PPAP")).html();
        var deletePpapDialogMessage = parsed.html($translate.instant("DELETE_PPAP_DIALOG_MESSAGE")).html();
        var newPpapTitle = parsed.html($translate.instant("NEW_PPAP")).html();
        var ppapDeletedMessage = parsed.html($translate.instant("PPAP_DELETED_MESSAGE")).html();
        $scope.cannotDeleteApprovedPpap = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_PPAP")).html();


        function nextPage() {
            if (vm.ppaps.last != true) {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.pageable.page++;
                vm.flag = false;
                loadAllPpaps();
            }
        }

        function previousPage() {
            if (vm.ppaps.first != true) {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.pageable.page--;
                vm.flag = false;
                loadAllPpaps();
            }
        }

        vm.pageSize = pageSize;
        function pageSize(page) {
            vm.pageable.page = 0;
            vm.pageable.size = page;
            loadAllPpaps();
        }

        function freeTextSearch(freeText) {
            vm.pageable.page = 0;
            $rootScope.showBusyIndicator($('.view-container'));
            if (freeText != null && freeText != "" && freeText != undefined) {
                vm.ppapFilter.searchQuery = freeText;
                $scope.freeTextQuery = freeText;
                loadAllPpaps();
            } else {
                resetPage();
            }
        }

        function resetPage() {
            vm.ppaps = angular.copy(pagedResults);
            vm.pageable.page = 0;
            vm.pageable.page = 0;
            vm.ppapFilter.searchQuery = null;
            $scope.freeTextQuery = null;
            $rootScope.showBusyIndicator($('.view-container'));
            loadAllPpaps();
        }


        function newPPap() {
            var options = {
                title: newPpapTitle,
                template: 'app/desktop/modules/pqm/ppap/new/newPPAPView.jsp',
                controller: 'NewPPAPController as newPpapVm',
                resolve: 'app/desktop/modules/pqm/ppap/new/newPPAPController',
                width: 600,
                showMask: true,
                buttons: [
                    {text: createButton, broadcast: 'app.ppap.new'}
                ],
                callback: function (ppap) {
                    $timeout(function () {
                        // showNcr(ncr);
                        loadAllPpaps();
                    }, 500);
                }
            };

            $rootScope.showSidePanel(options);
        }

        vm.ppaps = [];
        function loadAllPpaps() {
            //$rootScope.showBusyIndicator();
            vm.loading = true;
            PpapService.getAllPpaps(vm.pageable, vm.ppapFilter).then(
                function (data) {
                    vm.ppaps = data;
                    CommonService.getMultiplePersonReferences(vm.ppaps.content, ['createdBy', 'modifiedBy']);
                    loadAttributeValues();
                    $rootScope.hideBusyIndicator();
                    vm.loading = false;
                }, function (error) {
                    $rootScope.showErrorMessage(error.message);
                    $rootScope.hideBusyIndicator();
                }
            )
        }

        function deletePPAP(ppap) {
            var options = {
                title: deleteDialogTitle,
                message: deletePpapDialogMessage + " [" + ppap.number + "] ?",
                okButtonClass: 'btn-danger'
            };

            DialogService.confirm(options, function (yes) {
                if (yes == true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    PpapService.deletePpap(ppap.id).then(
                        function (data) {
                            $rootScope.showSuccessMessage(ppapDeletedMessage);
                            loadAllPpaps();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            });
        }

        var attributesTitle = $translate.instant("ATTRIBUTES");
        var addButton = parsed.html($translate.instant("ADD")).html();
        var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

        vm.showTypeAttributes = showTypeAttributes;
        vm.removeAttribute = removeAttribute;
        vm.selectedAttributes = [];
        function showTypeAttributes() {
            var options = {
                title: attributesTitle,
                template: 'app/desktop/modules/pqm/attributes/qualityTypeAttributeSelectionView.jsp',
                resolve: 'app/desktop/modules/pqm/attributes/qualityTypeAttributeSelectionController',
                controller: 'QualityTypeAttributeSelectionController as qualityTypeAttributeSelectionVm',
                width: 500,
                showMask: true,
                data: {
                    selectedAttributes: vm.selectedAttributes,
                    selectedObjectType: "PPAPTYPE",
                    selectedObject: "PPAP",
                    selectedParentObjectType: "QUALITY"
                },
                buttons: [
                    {text: addButton, broadcast: 'add.quality.attributes.select'}
                ],
                callback: function (result) {
                    vm.selectedAttributes = result;
                    $window.localStorage.setItem("ppapAttributes", JSON.stringify(vm.selectedAttributes));
                    if (result.length > 0) {
                        $rootScope.showSuccessMessage(selectedAttributesMessage);
                    }
                    loadAllPpaps();
                }
            };

            $rootScope.showSidePanel(options);
        }

        function removeAttribute(att) {
            vm.selectedAttributes.remove(att);
            $window.localStorage.setItem("ppapAttributes", JSON.stringify(vm.selectedAttributes));
        }

        vm.objectIds = [];
        vm.attributeIds = [];
        vm.selectedAttributes = [];
        function loadAttributeValues() {
            vm.objectIds = [];
            vm.attributeIds = [];
            angular.forEach(vm.ppaps.content, function (item) {
                vm.objectIds.push(item.id);
            });
            angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                    vm.attributeIds.push(selectedAttribute.id);
                }
            });
            $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.ppaps.content);

        }

        function showPpap(ppap) {
            $state.go('app.pqm.ppap.details', {
                ppapId: ppap.id,
                tab: 'details.basic'
            });
        }

        vm.showMfrPartDetails = showMfrPartDetails;
        function showMfrPartDetails(mfrpart) {
            $state.go('app.mfr.mfrparts.details', {mfrId: mfrpart.manufacturer, manufacturePartId: mfrpart.id});
        }


        vm.showSuppierDetails = showSuppierDetails;
        function showSuppierDetails(ppap) {
            $state.go('app.mfr.supplier.details', {
                supplierId: ppap.supplier,
                tab: 'details.basic'
            });
        }

        function validateJSON() {
            try {
                JSON.parse($window.localStorage.getItem("ppapAttributes"));
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
                setAttributes = JSON.parse($window.localStorage.getItem("ppapAttributes"));
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
                            $window.localStorage.setItem("ppapAttributes", "");
                            vm.selectedAttributes = setAttributes
                        } else {
                            vm.selectedAttributes = setAttributes;
                        }
                        loadAllPpaps();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            } else {
                loadAllPpaps();
            }
        })();
    }

});
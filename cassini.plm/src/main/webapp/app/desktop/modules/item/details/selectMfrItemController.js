define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrPartsService',
        'app/desktop/modules/mfr/mfrparts/directive/manufacturerPartDirective'
    ],
    function (module) {
        module.controller('SelectMfrItemController', SelectMfrItemController);

        function SelectMfrItemController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                         ItemService, MfrPartsService, $translate) {
            var vm = this;

            vm.loading = true;
            vm.items = [];
            vm.onOk = onOk;
            vm.selectedItems = [];
            vm.itemId = $stateParams.itemId;
            var mfrParts = $scope.data.mfrParts;
            vm.item = null;
            vm.onSelectType = onSelectType;
            vm.clearFilter = clearFilter;
            vm.searchParts = searchParts;
            vm.checkAll = checkAll;
            vm.select = select;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            $scope.check = false;
            var parsed = angular.element("<div></div>");
            vm.partNumber = parsed.html($translate.instant("PART_NUMBER_TITLE")).html();
            vm.partName = parsed.html($translate.instant("PART_NAME_TITLE")).html();
            var SelectAtleastOnePart = parsed.html($translate.instant("SELECT_MFR_PART")).html();
            var ItemsAddedSuccessMsg = parsed.html($translate.instant("RELATED_ITEM_ADDED")).html();
            var PleaseEnterSearchValue = parsed.html($translate.instant("SEARCH_VALUE")).html();
            vm.replacementType = false;

            vm.filters = {
                partNumber: null,
                partName: null,
                mfrPartType: '',
                typeName: null,
                description: null,
                item: '',
                mco: '',
                manufacturer: '',
                mcoReplacement: '',
                mcoAffectedItems: [],
                ncr: '',
                qcr: '',
                inspection: '',
                variance: '',
                supplier: '',
                declaration: '',
                related: false
            };

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
            vm.parts = angular.copy(pagedResults);
            function loadItems() {
                $rootScope.showBusyIndicator();
                MfrPartsService.getItemManufacturerParts(vm.filters, vm.pageable).then(
                    function (data) {
                        vm.parts = data;
                        if (vm.replacementType) {
                            angular.forEach(vm.parts.content, function (part) {
                                part.checked = false;
                            })
                        }
                        MfrPartsService.getManufacturerReference(vm.parts.content, 'manufacturer');
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    })
            }

            function onOk() {
                $rootScope.showBusyIndicator();
                if (vm.replacementType) {
                    if (vm.replacementPart == null || vm.replacementPart == undefined) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showWarningMessage(SelectAtleastOnePart)
                    } else {
                        $rootScope.hideSidePanel();
                        $scope.callback(vm.replacementPart);
                        $rootScope.hideBusyIndicator();
                    }
                } else {
                    if (vm.selectedItems.length == 0) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showWarningMessage(SelectAtleastOnePart);
                    } else {
                        $rootScope.hideSidePanel();
                        $scope.callback(vm.selectedItems);
                        $rootScope.hideBusyIndicator();
                        // $rootScope.showSuccessMessage(ItemsAddedSuccessMsg)
                    }
                }
            }

            function checkAll() {
                if (vm.selectAllCheck) {
                    vm.selectedItems = [];
                    vm.selectAllCheck = true;
                    angular.forEach(vm.parts.content, function (mfrPart) {
                        mfrPart.selected = vm.selectAllCheck;
                        vm.selectedItems.push(mfrPart);
                    });
                } else {
                    vm.selectAllCheck = false;
                    angular.forEach(vm.parts.content, function (mfrPart) {
                        mfrPart.selected = vm.selectAllCheck;
                        vm.selectedItems = [];
                    });
                }
            }

            vm.selectAllCheck = false;
            function select(mfrPart) {
                var flag = true;
                if (mfrPart.selected == false) {
                    vm.selectAllCheck = false;
                    var index = vm.selectedItems.indexOf(mfrPart);
                    vm.selectedItems.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedItems, function (selectedItem) {
                        if (selectedItem.id == mfrPart.id) {
                            flag = false;
                            var index = vm.selectedItems.indexOf(mfrPart);
                            vm.selectedItems.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedItems.push(mfrPart);
                    }

                    if (vm.selectedItems.length == vm.parts.content.length) {
                        vm.selectAllCheck = true;
                    }
                    if (vm.selectedItems.length != vm.parts.content.length) {
                        vm.selectAllCheck = false;
                    }
                }
            }

            vm.replacementPart = null;
            vm.selectReplacementPart = selectReplacementPart;
            function selectReplacementPart(mfrPart) {
                if (vm.replacementPart != null && vm.replacementPart.id != item.id) {
                    vm.replacementPart.checked = false;
                }
                if (vm.replacementPart != null && vm.replacementPart.id == item.id) {
                    vm.replacementPart = mfrPart;
                } else {
                    vm.replacementPart = mfrPart;
                }
            }

            function onSelectType(mfrPartType) {
                if (mfrPartType != null && mfrPartType != undefined) {
                    vm.selectedType = mfrPartType;
                    vm.mfrPartType = mfrPartType;
                    vm.filters.mfrPartType = mfrPartType.id;
                    vm.filters.typeName = mfrPartType.name;
                    searchParts();
                }
            }

            function searchParts() {
                $rootScope.showBusyIndicator();
                if (vm.filters.partName == null && vm.filters.mfrPartType == "" && vm.filters.partNumber == null) {
                    $rootScope.hideBusyIndicator();
                    $rootScope.showWarningMessage(PleaseEnterSearchValue)
                } else {
                    vm.pageable.page = 0;
                    MfrPartsService.getItemManufacturerParts(vm.filters, vm.pageable).then(
                        function (data) {
                            vm.parts = data;
                            MfrPartsService.getManufacturerReference(vm.parts.content, 'manufacturer');
                            vm.clear = true;
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        })
                }
            }

            function clearFilter() {
                vm.filters = {
                    partNumber: null,
                    partName: null,
                    mfrPartType: '',
                    description: null,
                    typeName: null,
                    item: '',
                    mco: '',
                    manufacturer: '',
                    mcoReplacement: '',
                    mcoAffectedItems: [],
                    ncr: '',
                    qcr: '',
                    inspection: '',
                    variance: '',
                    supplier: '',
                    declaration: '',
                    related: false
                };
                vm.selectedType = null;
                vm.selectAllCheck = false;
                vm.selectedItems = [];
                vm.pageable.page = 0;
                setReferenceIds();
                vm.clear = false;
            }

            function nextPage() {
                if (!vm.parts.last) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page++;
                    vm.selectAllCheck = false;
                    vm.selectedItems = [];
                    loadItems();
                }
            }

            function previousPage() {
                if (!vm.parts.first) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.pageable.page--;
                    vm.selectAllCheck = false;
                    vm.selectedItems = [];
                    loadItems();
                }
            }

            function setReferenceIds() {
                if ($scope.data.selectMfrPartsMode == "ITEM") {
                    vm.filters.item = $scope.data.selectedItemId;
                    loadItems();
                } else if ($scope.data.selectMfrPartsMode == "MCO") {
                    vm.filters.mco = $scope.data.selectedMcoId;
                    if ($scope.data.replacementType != undefined && $scope.data.replacementType != null) {
                        vm.replacementType = true;
                        vm.filters.mcoReplacement = $scope.data.replacementType;
                    }

                    if ($scope.data.relatedMfrParts != undefined && $scope.data.relatedMfrParts) {
                        vm.filters.related = true;
                    }
                    vm.filters.mcoAffectedItems = $scope.data.addAffectedParts;
                    loadItems();
                } else if ($scope.data.selectMfrPartsMode == "NCR") {
                    vm.filters.ncr = $scope.data.selectedNcrId;
                    vm.filters.mcoAffectedItems = $scope.data.addAffectedParts;
                    if ($scope.data.relatedMfrParts != undefined && $scope.data.relatedMfrParts) {
                        vm.filters.related = true;
                    }
                    loadItems();
                } else if ($scope.data.selectMfrPartsMode == "MATERIALINSPECTION") {
                    vm.filters.inspection = $scope.data.selectedInspectionId;
                    if ($scope.data.relatedMfrParts != undefined && $scope.data.relatedMfrParts) {
                        vm.filters.related = true;
                    }
                    loadItems();
                } else if ($scope.data.selectMfrPartsMode == "QCR") {
                    vm.filters.qcr = $scope.data.selectedQCR;
                    vm.filters.mcoAffectedItems = $scope.data.addAffectedParts;
                    if ($scope.data.relatedMfrParts != undefined && $scope.data.relatedMfrParts) {
                        vm.filters.related = true;
                    }
                    loadItems();
                } else if ($scope.data.selectMfrPartsMode == "VARIANCE") {
                    vm.filters.variance = $scope.data.selectedVarianceId;
                    loadItems();
                }
                if ($scope.data.selectMfrPartsMode == "SUPPLIER") {
                    vm.filters.supplier = $scope.data.selectedSupplierId;
                    loadItems();
                }
                if ($scope.data.selectMfrPartsMode == "DECLARATION") {
                    vm.filters.declaration = $scope.data.selectedDeclarationId;
                    vm.filters.supplier = $scope.data.selectedSupplierId;
                    loadItems();
                }
            }

            (function () {
                setReferenceIds();
                $rootScope.$on('app.item.mfr.new', onOk);
            })();
        }
    }
);
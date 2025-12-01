define([
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemBomService',
        'app/shared/services/core/dcrService',
        'app/shared/services/core/ecrService',
        'app/shared/services/core/varianceService',
        'app/shared/services/core/problemReportService',
        'app/shared/services/core/qcrService',
        'app/shared/services/core/inspectionService',
        'app/desktop/modules/classification/directive/classificationTreeDirective'
    ],
    function (module) {
        module.controller('DCRRelatedItemsSelectionController', DCRRelatedItemsSelectionController);

        function DCRRelatedItemsSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, ItemService, ItemBomService,
                                                    DCRService, ECRService, VarianceService, ProblemReportService, QcrService, InspectionService) {

            var vm = this;

            vm.dcrId = $stateParams.dcrId;
            var dco = $scope.data.selectedDco;
            var ecr = $scope.data.selectedEcr;
            var object = $scope.data.selectedObject;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.error = "";

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

            vm.items = angular.copy(pagedResults);
            vm.onSelectType = onSelectType;
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.onOk = onOk;

            vm.loadFilteredItems = loadFilteredItems;

            var parsed = angular.element("<div></div>");
            var selectMessage = parsed.html($translate.instant("ATLEAST_ONE_ITEM_VALIDATION")).html();
            var releatedItemsAdded = parsed.html($translate.instant("RELATED_ITEM_ADDED")).html();

            vm.selectedItems = [];
            $scope.check = false;
            var dcr = $scope.data.selectedDcr;

            function selectAll(check) {
                vm.selectedItems = [];
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.items.content, function (item) {
                        item.selected = false;
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.items.content, function (item) {
                        item.selected = true;
                        vm.selectedItems.push(item);
                    })
                }
            }

            function nextPage() {
                if (vm.items.last != true) {
                    vm.pageable.page++;
                    $scope.check = false;
                    vm.selectedItems = [];
                    loadFilteredItems();
                }
            }

            function previousPage() {
                if (vm.items.first != true) {
                    vm.pageable.page--;
                    $scope.check = false;
                    vm.selectedItems = [];
                    loadFilteredItems();
                }
            }

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedType = itemType;
                    vm.itemsFilters.itemType = itemType.id;
                    vm.itemsFilters.typeName = itemType.name;
                    vm.pageable.page = 0;
                    loadFilteredItems();
                    vm.clear = true;
                }
            }

            function clearFilter() {
                vm.itemsFilters.itemName = null;
                vm.itemsFilters.itemNumber = null;
                vm.itemsFilters.itemType = '';
                vm.itemsFilters.typeName = null;

                vm.selectedType = null;
                $scope.check = false;
                vm.selectedItems = [];
                vm.selectAllCheck = false;
                setReferenceIds();
                vm.clear = false;
            }


            function onOk() {
                if (vm.selectedItems.length != 0) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    if ($scope.data.mode == 'DCR') {
                        vm.relatedItems = [];
                        angular.forEach(vm.selectedItems, function (item) {
                            vm.relatedItem = {
                                id: null,
                                item: item.latestRevision,
                                change: object
                            };
                            vm.relatedItems.push(vm.relatedItem);
                        });
                        DCRService.createDcrRelatedItems(object, vm.relatedItems).then(
                            function (data) {
                                $rootScope.hideBusyIndicator();
                                $scope.callback(data);
                                $rootScope.showSuccessMessage(releatedItemsAdded);
                                $rootScope.hideSidePanel()
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else if ($scope.data.mode == 'DCO') {
                        vm.relatedItems = [];
                        angular.forEach(vm.selectedItems, function (item) {
                            vm.relatedItem = {
                                id: null,
                                item: item.latestRevision,
                                change: object
                            };
                            vm.relatedItems.push(vm.relatedItem);
                        });
                        DCRService.createDcrRelatedItems(object, vm.relatedItems).then(
                            function (data) {
                                $rootScope.hideBusyIndicator();
                                $scope.callback(data);
                                $rootScope.showSuccessMessage(releatedItemsAdded);
                                $rootScope.hideSidePanel()
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else if ($scope.data.mode == 'ECR') {
                        vm.relatedItems = [];
                        angular.forEach(vm.selectedItems, function (item) {
                            vm.relatedItem = {
                                id: null,
                                item: item.latestRevision,
                                change: object
                            };
                            vm.relatedItems.push(vm.relatedItem);
                        });
                        ECRService.createEcrRelatedItems(object, vm.relatedItems).then(
                            function (data) {
                                $rootScope.hideBusyIndicator();
                                $scope.callback(data);
                                $rootScope.showSuccessMessage(releatedItemsAdded);
                                $rootScope.hideSidePanel()
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else if ($scope.data.mode == 'VARIANCE') {
                        vm.relatedItems = [];
                        angular.forEach(vm.selectedItems, function (item) {
                            vm.relatedItem = {
                                id: null,
                                item: item.latestRevision,
                                change: object
                            };
                            vm.relatedItems.push(vm.relatedItem);
                        });
                        VarianceService.createVarianceRelatedItem(object, vm.relatedItems).then(
                            function (data) {
                                $rootScope.hideBusyIndicator();
                                $scope.callback(data);
                                $rootScope.showSuccessMessage(releatedItemsAdded);
                                $rootScope.hideSidePanel()
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else if ($scope.data.mode == 'PROBLEMREPORT') {
                        vm.relatedItems = [];
                        angular.forEach(vm.selectedItems, function (item) {
                            vm.relatedItem = {
                                id: null,
                                item: item.latestRevision,
                                problemReport: object
                            };
                            vm.relatedItems.push(vm.relatedItem);
                        });
                        ProblemReportService.createPrRelatedItems(object, vm.relatedItems).then(
                            function (data) {
                                $rootScope.hideBusyIndicator();
                                $scope.callback(data);
                                $rootScope.showSuccessMessage(releatedItemsAdded);
                                $rootScope.hideSidePanel()
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else if ($scope.data.mode == 'QCR') {
                        vm.relatedItems = [];
                        angular.forEach(vm.selectedItems, function (item) {
                            vm.relatedItem = {
                                id: null,
                                item: item.latestRevision,
                                qcr: object
                            };
                            vm.relatedItems.push(vm.relatedItem);
                        });
                        QcrService.createQcrRelatedItems(object, vm.relatedItems).then(
                            function (data) {
                                $rootScope.hideBusyIndicator();
                                $scope.callback(data);
                                $rootScope.showSuccessMessage(releatedItemsAdded);
                                $rootScope.hideSidePanel()
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else if ($scope.data.mode == 'ITEMINSPECTION') {
                        vm.relatedItems = [];
                        angular.forEach(vm.selectedItems, function (item) {
                            vm.relatedItem = {
                                id: null,
                                item: item.latestRevision,
                                inspection: object
                            };
                            vm.relatedItems.push(vm.relatedItem);
                        });
                        InspectionService.createItemRelatedItems(object, vm.relatedItems).then(
                            function (data) {
                                $rootScope.hideBusyIndicator();
                                $scope.callback(data);
                                $rootScope.showSuccessMessage(releatedItemsAdded);
                                $rootScope.hideSidePanel()
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                }

                if (vm.selectedItems.length == 0) {
                    $rootScope.showWarningMessage(selectMessage);
                }

            }

            vm.selectAllCheck = false;

            function selectCheck(item) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedItems, function (selectedItem) {
                    if (selectedItem.id == item.id) {
                        flag = false;
                        var index = vm.selectedItems.indexOf(item);
                        vm.selectedItems.splice(index, 1);
                    }
                });
                if (flag) {
                    vm.selectedItems.push(item);
                }

                if (vm.selectedItems.length != vm.items.content.length) {
                    vm.selectAllCheck = false;
                } else {
                    vm.selectAllCheck = true;
                }
            }


            vm.itemsFilters = {
                itemType: '',
                itemNumber: null,
                itemName: null,
                dcr: '',
                dco: '',
                ecr: '',
                problemReport: '',
                ncr: '',
                qcr: '',
                inspection: '',
                variance: '',
                related: false
            };

            function loadFilteredItems() {
                $rootScope.showBusyIndicator();
                DCRService.getFilteredItems(vm.pageable, vm.itemsFilters).then(
                    function (data) {
                        vm.selectedItems = [];
                        vm.items = data;
                        vm.selectAllCheck = false;
                        ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.searchFilterItem = searchFilterItem;
            function searchFilterItem() {
                vm.pageable.page = 0;
                loadFilteredItems();
                vm.clear = true;
                vm.selectAllCheck = false;
                if ((vm.itemsFilters.itemName == "" || vm.itemsFilters.itemName == null) && (vm.itemsFilters.itemNumber == "" || vm.itemsFilters.itemNumber == null)) {
                    vm.clear = false;
                    vm.selectAllCheck = false;
                }
            }

            function setReferenceIds() {
                var objectId = $scope.data.selectedObject;
                if ($scope.data.mode == 'DCR') {
                    vm.itemsFilters.dcr = objectId;
                } else if ($scope.data.mode == 'DCO') {
                    vm.itemsFilters.dco = objectId;
                } else if ($scope.data.mode == 'ECR') {
                    vm.itemsFilters.ecr = objectId;
                } else if ($scope.data.mode == 'VARIANCE') {
                    vm.itemsFilters.variance = objectId;
                } else if ($scope.data.mode == 'PROBLEMREPORT') {
                    vm.itemsFilters.problemReport = objectId;
                    vm.itemsFilters.related = true;
                } else if ($scope.data.mode == 'QCR') {
                    vm.itemsFilters.qcr = objectId;
                    vm.itemsFilters.related = true;
                } else if ($scope.data.mode == 'ITEMINSPECTION') {
                    vm.itemsFilters.inspection = objectId;
                    vm.itemsFilters.related = true;
                }
                loadFilteredItems();
            }

            (function () {
                setReferenceIds();
                $rootScope.$on("add.dcr.related.items", onOk);
            })();
        }
    }
)
;
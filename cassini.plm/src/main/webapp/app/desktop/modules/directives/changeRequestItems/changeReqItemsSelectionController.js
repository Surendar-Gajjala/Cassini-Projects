define([
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemBomService',
        'app/shared/services/core/dcrService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/ecrService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/directives/changeTypeDirective',
        'app/desktop/modules/directives/changeRequestItems/dcrTypeDirective'
    ],
    function (module) {
        module.controller('ChangeReqItemsSelectionController', ChangeReqItemsSelectionController);

        function ChangeReqItemsSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                                   ECOService, ECRService, CommonService, $translate, ItemService, ItemBomService, DCRService, DCOService) {

            var vm = this;

            var objectId = $scope.data.selectedObjectId;
            var objectType = $scope.data.selectedObjectType;

            $scope.type = $scope.data.selectedObjectType;

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

            vm.itemsFilters = {
                crNumber: null,
                crType: '',
                title: null
            };

            vm.items = angular.copy(pagedResults);
            vm.onSelectType = onSelectType;
            vm.onSelectDcrType = onSelectDcrType;
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.onOk = onOk;

            vm.loadFilteredItems = loadFilteredItems;

            var parsed = angular.element("<div></div>");
            var selectMessage = parsed.html($translate.instant("ATLEAST_ONE_ITEM_VALIDATION")).html();
            var crAddedMsg = parsed.html($translate.instant("CR_ADDED_MSG")).html();
            var selectToLifecycleInEcr = parsed.html($translate.instant("SELECT_TO_LIFECYCLE_IN_ECR")).html();
            var selectToRevisionInEcr = parsed.html($translate.instant("SELECT_TO_REVISION_IN_ECR")).html();

            vm.selectedItems = [];
            $scope.check = false;

            function selectAll(check) {
                vm.selectedItems = [];
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.items.content, function (item) {
                        angular.forEach(item.requestedItemDtos, function (itemAffected) {
                            itemAffected.toLifeCycle = null;
                        })
                        item.selected = false;
                        item.showItems = false;
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.items.content, function (item) {
                        item.selected = true;
                        item.showItems = true;
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

            function onSelectType(crType) {
                if (crType != null && crType != undefined) {
                    vm.selectedType = crType;
                    vm.itemsFilters.crType = crType.id;
                    vm.pageable.page = 0;
                    loadFilteredItems();
                    vm.clear = true;
                }
            }

            function onSelectDcrType(crType) {
                if (crType != null && crType != undefined) {
                    vm.selectedType = crType;
                    vm.itemsFilters.crType = crType.id;
                    vm.pageable.page = 0;
                    loadFilteredItems();
                    vm.clear = true;
                }
            }

            function clearFilter() {
                vm.itemsFilters.crNumber = null;
                vm.itemsFilters.crType = '';
                vm.itemsFilters.title = null;

                vm.selectedType = null;
                $scope.check = false;
                vm.selectedItems = [];
                vm.selectAllCheck = false;
                loadFilteredItems();
                vm.clear = false;
            }


            function onOk() {
                $rootScope.showBusyIndicator();
                if (vm.selectedItems.length != 0) {
                    if (validateItemLifecycles()) {
                        if (objectType == 'ECO') {
                            ECRService.createECOChangeRequest(objectId, vm.selectedItems).then(
                                function (data) {
                                    $scope.callback(data);
                                    $rootScope.showSuccessMessage(crAddedMsg);
                                    $rootScope.hideSidePanel();
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        } else if (objectType == 'DCO') {
                            DCOService.createDCOChangeRequest(objectId, vm.selectedItems).then(
                                function (data) {
                                    $scope.callback(data);
                                    $rootScope.showSuccessMessage(crAddedMsg);
                                    $rootScope.hideSidePanel();
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.hideBusyIndicator()
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                }

                if (vm.selectedItems.length == 0) {
                    $rootScope.showWarningMessage(selectMessage);
                }

            }

            function validateItemLifecycles() {
                var valid = true;

                angular.forEach(vm.selectedItems, function (selectedItem) {
                    if (valid) {
                        angular.forEach(selectedItem.requestedItemDtos, function (affectedItem) {
                            if (valid) {
                                if (affectedItem.toRevision == null || affectedItem.toRevision == "" || affectedItem.toRevision == undefined) {
                                    valid = false;
                                    $rootScope.showWarningMessage(selectToRevisionInEcr.format(affectedItem.number, selectedItem.crNumber));
                                } else if (affectedItem.toLifeCycle == null || affectedItem.toLifeCycle == "" || affectedItem.toLifeCycle == undefined) {
                                    valid = false;
                                    $rootScope.showWarningMessage(selectToLifecycleInEcr.format(affectedItem.number, selectedItem.crNumber));
                                }
                            }
                        })
                    }
                })


                return valid;
            }

            vm.selectAllCheck = false;

            function selectCheck(item) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedItems, function (selectedItem) {
                    if (selectedItem.id == item.id) {
                        angular.forEach(selectedItem.requestedItemDtos, function (itemAffected) {
                            itemAffected.toLifeCycle = null;
                            itemAffected.toRevision = null;
                        });
                        flag = false;
                        item.showItems = false;
                        var index = vm.selectedItems.indexOf(item);
                        vm.selectedItems.splice(index, 1);
                    }
                });
                if (flag) {
                    item.showItems = true;
                    if (objectType == 'ECO') {
                        angular.forEach(vm.selectedItems, function (dcr) {
                            angular.forEach(dcr.requestedItemDtos, function (itemAffected) {
                                angular.forEach(item.requestedItemDtos, function (selectedItem) {
                                    if (itemAffected.ecrAffectedItem.item == selectedItem.ecrAffectedItem.item) {
                                        selectedItem.toLifeCycle = itemAffected.toLifeCycle;
                                        selectedItem.toRevision = itemAffected.toRevision;
                                    }

                                })
                            })
                        })
                    } else if (objectType == 'DCO') {
                        angular.forEach(vm.selectedItems, function (dcr) {
                            angular.forEach(dcr.requestedItemDtos, function (itemAffected) {
                                angular.forEach(item.requestedItemDtos, function (selectedItem) {
                                    if (itemAffected.affectedItem.item == selectedItem.affectedItem.item) {
                                        selectedItem.toLifeCycle = itemAffected.toLifeCycle;
                                        selectedItem.toRevision = itemAffected.toRevision;
                                    }

                                })
                            })
                        })
                    }

                    angular.forEach(item.requestedItemDtos, function (selectedItem) {
                        if (selectedItem.toRevision == null && selectedItem.toRevisions.length == 1) {
                            selectedItem.toRevision = selectedItem.toRevisions[0];
                        }
                    });

                    vm.selectedItems.push(item);
                }

                if (vm.selectedItems.length != vm.items.content.length) {
                    vm.selectAllCheck = false;
                } else {
                    vm.selectAllCheck = true;
                }
            }

            function loadFilteredItems() {
                $rootScope.showBusyIndicator();
                if (objectType == 'ECO') {
                    vm.itemsFilters.eco = objectId;
                    ECRService.getFilteredEcrItems(vm.pageable, vm.itemsFilters).then(
                        function (data) {
                            vm.selectedItems = [];
                            vm.items = data;
                            angular.forEach(vm.items.content, function (item) {
                                item.showItems = false;
                            });
                            vm.selectAllCheck = false;
                            CommonService.getPersonReferences(vm.items.content, 'changeAnalyst');
                            ECOService.getChangeTypeReferences(vm.items.content, 'crType');
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                } else if (objectType == 'DCO') {
                    vm.itemsFilters.dco = objectId;
                    DCOService.getFilteredDcrItems(vm.pageable, vm.itemsFilters).then(
                        function (data) {
                            vm.selectedItems = [];
                            vm.items = data;
                            angular.forEach(vm.items.content, function (item) {
                                item.showItems = false;
                            });
                            vm.selectAllCheck = false;
                            CommonService.getPersonReferences(vm.items.content, 'changeAnalyst');
                            ECOService.getChangeTypeReferences(vm.items.content, 'crType');
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
                $timeout(function () {
                    resizeScreen();
                }, 1000)
            }

            vm.searchFilterItem = searchFilterItem;
            function searchFilterItem() {
                vm.pageable.page = 0;
                loadFilteredItems();
                vm.clear = true;
                vm.selectAllCheck = false;
                if ((vm.itemsFilters.title == "" || vm.itemsFilters.title == null) && (vm.itemsFilters.crNumber == "" || vm.itemsFilters.crNumber == null) && (vm.itemsFilters.crType == "" || vm.itemsFilters.crType == null)) {
                    vm.clear = false;
                    vm.selectAllCheck = false;
                }
            }

            vm.selectPhases = selectPhases;

            function selectPhases(selectedItem, phase) {
                if (objectType == 'ECO') {
                    angular.forEach(vm.selectedItems, function (dcr) {
                        angular.forEach(dcr.requestedItemDtos, function (itemAffected) {
                            if (itemAffected.ecrAffectedItem.item == selectedItem.ecrAffectedItem.item) {
                                itemAffected.toLifeCycle = phase;
                            }

                        })
                    })
                } else if (objectType == 'DCO') {
                    angular.forEach(vm.selectedItems, function (dcr) {
                        angular.forEach(dcr.requestedItemDtos, function (itemAffected) {
                            if (itemAffected.affectedItem.item == selectedItem.affectedItem.item) {
                                itemAffected.toLifeCycle = phase;
                            }

                        })
                    })
                }

            }

            vm.selectRevision = selectRevision;

            function selectRevision(selectedItem, revision) {
                if (objectType == 'ECO') {
                    angular.forEach(vm.selectedItems, function (dcr) {
                        angular.forEach(dcr.requestedItemDtos, function (itemAffected) {
                            if (itemAffected.ecrAffectedItem.item == selectedItem.ecrAffectedItem.item) {
                                itemAffected.toRevision = revision;
                            }

                        })
                    })
                } else if (objectType == 'DCO') {
                    angular.forEach(vm.selectedItems, function (dcr) {
                        angular.forEach(dcr.requestedItemDtos, function (itemAffected) {
                            if (itemAffected.affectedItem.item == selectedItem.affectedItem.item) {
                                itemAffected.toRevision = revision;
                            }

                        })
                    })
                }
            }

            function resizeScreen() {
                var formHeight = $('.form-inline').outerHeight();
                var footerHeight = $("#page-footer").outerHeight();
                var sidePanelHeight = $("#rightSidePanelContent").outerHeight();

                $("#ecr-selection").height(sidePanelHeight - (formHeight + footerHeight + 40));
            }

            (function () {
                loadFilteredItems();
                $(window).resize(resizeScreen);
                $rootScope.$on("add.change.request.items", onOk);
            })();
        }
    }
)
;
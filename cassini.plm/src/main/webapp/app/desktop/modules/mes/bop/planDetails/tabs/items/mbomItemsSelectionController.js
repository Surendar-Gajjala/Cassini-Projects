define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/mbomService',
        'app/shared/services/core/bopService'
    ],
    function (module) {
        module.controller('MBOMItemsSelectionController', MBOMItemsSelectionController);

        function MBOMItemsSelectionController($scope, $q, $rootScope, $timeout, $state, $stateParams, $cookies,
                                              $translate, MBOMService, BOPService) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            var selectMessage = parsed.html($translate.instant("ATLEAST_ONE_PART_VALIDATION")).html();
            var resourceCreatedMsg = parsed.html($translate.instant("BOP_PLAN_RESOURCE_CREATE_MSG")).html();
            vm.bopPlanId = $stateParams.bopPlanId;
            vm.bopId = $stateParams.bopId;

            vm.selectedMbomItems = [];
            vm.loading = false;

            vm.selectAllCheck = false;
            vm.selectCheck = selectCheck;

            var emptyItem = {
                id: null,
                bopOperation: vm.bopPlanId,
                mbomItem: null
            };

            function selectCheck(mbomItem) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedMbomItems, function (selectedOperation) {
                    if (selectedOperation.id == mbomItem.id) {
                        flag = false;
                        var index = vm.selectedMbomItems.indexOf(selectedOperation);
                        if (index != -1) {
                            vm.selectedMbomItems.splice(index, 1);
                            selectedOperationsMap.remove(mbomItem.id);
                        }
                    }
                });
                if (flag) {
                    vm.selectedMbomItems.push(mbomItem);
                    selectedOperationsMap.put(mbomItem.id, mbomItem);
                }
                var count = 0;
                angular.forEach(vm.mBomItems, function (mbomItem) {
                    if (mbomItem.selected) {
                        count++;
                    }
                });
                if (count != vm.mBomItems.length) {
                    vm.selectAllCheck = false;
                } else {
                    vm.selectAllCheck = true;
                }
            }

            vm.clearSelection = clearSelection;
            function clearSelection() {
                selectedOperationsMap = new Hashtable();
                vm.selectedMbomItems = [];
                vm.selectAllCheck = false;
                angular.forEach(vm.mBomItems, function (mbomItem) {
                    mbomItem.selected = false;
                })
            }

            vm.selectAll = selectAll;
            function selectAll(check) {
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.mBomItems, function (mbomItem) {
                        mbomItem.selected = false;
                        vm.selectedMbomItems = [];
                        selectedOperationsMap = new Hashtable();
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.mBomItems, function (mbomItem) {
                        var mbomExist = selectedOperationsMap.get(mbomItem.id);
                        if (mbomExist == null && !mbomItem.hasBom && !mbomItem.alreadyExist && (mbomItem.quantity - mbomItem.consumedQty) > 0) {
                            mbomItem.selected = true;
                            vm.selectedMbomItems.push(mbomItem);
                            selectedOperationsMap.put(mbomItem.id, mbomItem);
                        }
                    })
                }
            }

            vm.toggleMBOMItemNode = toggleMBOMItemNode;
            function toggleMBOMItemNode(mBomItem) {
                if (mBomItem.expanded == null || mBomItem.expanded == undefined) {
                    mBomItem.expanded = false;
                }
                mBomItem.expanded = !mBomItem.expanded;
                var index = vm.mBomItems.indexOf(mBomItem);
                if (mBomItem.expanded == false) {
                    removeMBOMItemChildren(mBomItem);
                }
                else {
                    MBOMService.getMBOMItemChildren(vm.bopRevision.mbomRevision, mBomItem.id).then(
                        function (data) {
                            if (data.length > 0) {
                                mBomItem.hasBom = true;
                            }
                            angular.forEach(data, function (item) {
                                item.parentBom = mBomItem;
                                item.isNew = false;
                                item.expanded = false;
                                item.editMode = false;
                                item.level = mBomItem.level + 1;
                                item.bomChildren = [];
                                mBomItem.bomChildren.push(item);
                            });

                            angular.forEach(mBomItem.bomChildren, function (item) {
                                index = index + 1;
                                vm.mBomItems.splice(index, 0, item);
                            });
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function removeMBOMItemChildren(mBomItem) {
                if (mBomItem != null && mBomItem.bomChildren != null && mBomItem.bomChildren != undefined) {
                    angular.forEach(mBomItem.bomChildren, function (item) {
                        removeMBOMItemChildren(item);
                    });

                    var index = vm.mBomItems.indexOf(mBomItem);
                    vm.mBomItems.splice(index + 1, mBomItem.bomChildren.length);
                    mBomItem.bomChildren = [];
                    mBomItem.expanded = false;

                }
            }

            vm.onOk = onOk;
            function onOk() {
                if (vm.selectedMbomItems.length > 0) {
                    $scope.callback(vm.selectedMbomItems);
                    $rootScope.hideSidePanel();
                }
                if (vm.selectedMbomItems.length == 0) {
                    $rootScope.showWarningMessage(selectMessage);
                }

            }

            var selectedOperationsMap = new Hashtable();

            function loadBOPRevision() {
                BOPService.getBOPRevision(vm.bopId).then(
                    function (data) {
                        vm.bopRevision = data;
                        loadMBOMItems();
                    }
                )
            }

            vm.loadMBOMItems = loadMBOMItems;
            function loadMBOMItems() {
                BOPService.getBOPMBOMItemsByType(vm.bopId, vm.bopPlanId, vm.bopRevision.mbomRevision, $scope.data.operationPartType, true).then(
                    function (data) {
                        vm.mBomItems = [];
                        angular.forEach(data, function (item) {
                            item.parentBom = null;
                            item.expanded = false;
                            item.level = 0;
                            item.count = 0;
                            item.bomChildren = [];
                            vm.mBomItems.push(item);
                            var index = vm.mBomItems.indexOf(item);
                            index = populateMBOMItemChildren(item, index);
                            vm.loading = false;
                        });
                    }
                )
            }

            function populateMBOMItemChildren(mBomItem, lastIndex) {
                angular.forEach(mBomItem.children, function (item) {
                    lastIndex++;
                    item.parentBom = mBomItem;
                    item.expanded = true;
                    item.level = mBomItem.level + 1;
                    item.count = 0;
                    item.bomChildren = [];
                    vm.mBomItems.splice(lastIndex, 0, item);
                    mBomItem.count = mBomItem.count + 1;
                    mBomItem.expanded = true;
                    mBomItem.bomChildren.push(item);
                    lastIndex = populateMBOMItemChildren(item, lastIndex)
                });

                return lastIndex;
            }

            (function () {
                loadBOPRevision();
                $rootScope.$on('app.select.bop.plan.items', onOk);
            })();
        }
    });
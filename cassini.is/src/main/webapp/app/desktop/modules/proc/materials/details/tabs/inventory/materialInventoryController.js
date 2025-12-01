define(['app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/storeService',
        'app/shared/services/core/inventoryService',
        'app/shared/services/store/topInventoryService',
        'app/shared/services/store/topStoreService',
        'app/shared/services/pm/project/bomService'
    ],
    function (module) {
        module.controller('MaterialInventoryController', MaterialInventoryController);

        function MaterialInventoryController($scope, $rootScope, $timeout, $state, $cookies, $stateParams, ItemService,
                                             CommonService, InventoryService, StoreService, TopInventoryService, TopStoreService, BomService) {
            var vm = this;

            vm.materialId = $stateParams.materialId;
            vm.itemInventory = [];
            vm.material = null;
            var map = new Hashtable;

            function loadMaterial() {
                vm.loading = true;
                $rootScope.showBusyIndicator($('.view-content'));
                ItemService.getMaterialItem(vm.materialId).then(
                    function (data) {
                        vm.material = data;
                        vm.loading = false;
                        CommonService.getPersonReferences([vm.material], 'createdBy');
                        CommonService.getPersonReferences([vm.material], 'modifiedBy');
                        //loadItemInventory();
                        loadItemInventories();
                    }
                )
            }

            function loadItemInventory() {
                var boqItemIds = [];
                ItemService.getBoqItemsByItemNumber(1, vm.material.itemNumber).then(
                    function (data) {
                        var map = new Hashtable();
                        angular.forEach(data, function (boqItem) {
                            map.put(boqItem.id, boqItem);
                            boqItemIds.push(boqItem.id);
                        });
                        InventoryService.getInventoryByItems(1, boqItemIds).then(
                            function (data) {
                                angular.forEach(boqItemIds, function (itemId) {
                                    var boqItem = map.get(itemId);
                                    boqItem.storeOnHand = data[itemId].storeOnHand;
                                    vm.itemInventory.push(
                                        {
                                            store: data[itemId].store,
                                            inventoryObject: data[itemId].storeOnHand
                                        }
                                    );
                                });
                                StoreService.getStoreReferences(1, vm.itemInventory, 'store');
                            }
                        );
                    });
            }

            function loadItemInventories() {
                TopInventoryService.getInventoryByItemNumber(vm.material.itemNumber).then(
                    function (data) {
                        vm.materialInventory = data;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                $scope.$on('app.material.tabactivated', function (event, data) {
                    loadMaterial();
                });
            })();
        }
    }
);
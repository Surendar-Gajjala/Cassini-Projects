define(
    [
        'app/desktop/modules/mfr/mfrparts/mfrparts.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrPartsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
    ],
    function (module) {
        module.controller('MfrPartsWhereUsedController', MfrPartsWhereUsedController);

        function MfrPartsWhereUsedController($scope, $rootScope, $timeout, $state, $stateParams, ItemService, MfrPartsService, CommonService) {
            var vm = this;

            vm.loading = true;
            vm.mfrPartId = $stateParams.manufacturePartId;
            vm.items = null;
            vm.itemMfrs = null;
            vm.showItem = showItem;

            function loadItems() {
                var ids = [];
                MfrPartsService.getItemMfrPartByMfrPart(vm.mfrPartId).then(
                    function (data) {
                        vm.itemMfrs = data;
                        angular.forEach(vm.itemMfrs, function (item) {
                            ids.push(item.item);
                        })
                        ItemService.getRevisionsByIds(ids).then(
                            function (data) {
                                /*ItemService.getItemsByIds(ids).then(
                                 function (data) {*/
                                vm.items = data;
                                vm.loading = false;
                                ItemService.getItemReferences(vm.items, 'itemMaster');
                                CommonService.getPersonReferences(vm.items, 'createdBy');
                                CommonService.getPersonReferences(vm.items, 'modifiedBy');
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )

                        /*}
                         )*/
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function showItem(item) {
                $state.go('app.items.details', {itemId: item.id});
            }

            $rootScope.itemMfrStatus = itemMfrStatus;
            function itemMfrStatus() {
                if(vm.selecteditems.length > 0){
                    MfrPartsService.getmfrPartItems(vm.mfrPartId,vm.selecteditems).then(
                        function (data) {
                            angular.forEach(vm.selecteditems, function (item) {
                                    item.selected = false;
                                    vm.flag = false;
                                }
                            );
                            $rootScope.showSuccessMessage("Manufacturer Part Status has changed")
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.flag = false;
            vm.selectAll = selectAll;
            vm.selecteditems = [];
            function selectAll() {
                vm.selecteditems = [];
                //vm.flag = !vm.flag;
                if (vm.flag) {
                    angular.forEach(vm.items, function (item) {
                            vm.selecteditems.push(item);
                            item.selected = true;
                        }
                    )
                } else {
                    vm.selecteditems = [];
                    angular.forEach(vm.items, function (item) {
                        item.selected = false;
                    })
                }

            }

            vm.toggleSelection = toggleSelection;
            function toggleSelection(item) {
                //vm.selecteditems = [];
                if (item.selected == false) {
                    var index = vm.selecteditems.indexOf(item);
                    if (index != -1) {
                        vm.selecteditems.splice(index, 1);
                        vm.flag = false;
                    }
                }
                else {
                    vm.selecteditems.push(item);
                    if (vm.items.length == vm.selecteditems.length) {
                        vm.flag = true;
                    }else {
                        vm.flag = false;
                    }
                }
            }

            (function () {
                $scope.$on('app.mfrPart.tabactivated', function (event, data) {
                    if (data.tabId == 'details.whereUsed') {
                        loadItems();
                    }
                });
            })();
        }
    }
)
;
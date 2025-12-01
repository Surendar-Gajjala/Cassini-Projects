/**
 * Created by Nageshreddy on 02-01-2019.
 */
define([
        'app/desktop/modules/bom/bom.module',
        'app/shared/services/core/listService',
        'app/shared/services/core/bomService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService'
    ],
    function (module) {
        module.controller('ScanFailureListController', ScanFailureListController);

        function ScanFailureListController($scope, $q, $bom, $rootScope, $timeout, $window, ListService,
                                           BomService, ItemService, AttachmentService) {
            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;

            vm.items = [];
            vm.children = null;
            vm.bomId = $scope.data.bomId;

            function loadItems() {
                BomService.getBomTreeChildren(vm.bomId, $bom.bomObject.parent.id).then(
                    function (data) {
                        vm.children = data;
                        addChildren(vm.children);
                        if ($bom.bomObject.item.itemType.parent == 'PART' || child.item.itemType.parent == 'GENERIC'
                            || $bom.bomObject.item.itemType.storeAsLot) {
                            vm.items.unshift($bom.bomObject.item);
                        }
                    }
                );
            }

            function addChildren(children) {
                angular.forEach(children, function (child) {
                    if (child.item.itemType.parent == 'PART' || child.item.itemType.parent == 'GENERIC' || child.item.itemType.storeAsLot) {
                        vm.items.push(child);
                    }
                    if (child.children.length > 0) {
                        addChildren(child.children);
                    }
                });
            }

            function validation() {
                var valid = true;
                angular.forEach(vm.items, function (item) {
                    angular.forEach(item.allocationList, function (allocation) {
                        if (allocation.scannedUpn == null || allocation.scannedUpn == undefined || allocation.scannedUpn == "") {
                            $rootScope.showErrorMessage("Please enter all partNumbers");
                            valid = false;
                        }
                    })
                });
                return valid;
            }

            function verifyPartNumbers() {
                if (validation()) {
                    var valid = true;
                    angular.forEach(vm.items, function (item) {
                        angular.forEach(item.allocationList, function (allocation1) {
                            allocation1.valid = true;
                            angular.forEach(item.allocationList, function (allocation) {
                                if (allocation1.scannedUpn == allocation.upnNumber) {
                                    allocation1.valid = false;
                                }
                            });
                            if (allocation1.valid) {
                                valid = false;
                                $rootScope.showErrorMessage("Part number(s) not valid");
                            }
                        })
                    });
                    $timeout(function () {
                        if (valid) {
                            $rootScope.showSuccessMessage("Parts scanned successfully");
                            $rootScope.hideSidePanel();
                            $scope.callback(true);
                        }
                    })
                }
            }

            (function () {
                loadItems();
                $scope.$on('app.failureList.scan', verifyPartNumbers);
            })();
        }
    }
)
;

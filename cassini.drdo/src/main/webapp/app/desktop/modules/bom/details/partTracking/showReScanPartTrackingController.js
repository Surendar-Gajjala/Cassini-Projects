/**
 * Created by Nageshreddy on 12-06-2018.
 */
define([
        'app/desktop/modules/bom/bom.module',
        'app/shared/services/core/partTrackingService',
        'app/shared/services/core/bomService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService'
    ],
    function (module) {
        module.controller('ShowReScanPartTrackingController', ShowReScanPartTrackingController);

        function ShowReScanPartTrackingController($scope, $q, $bom, $rootScope, $timeout, $window,
                                                  PartTrackingService, ItemBomService) {
            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;

            vm.items = [];
            vm.children = null;
            vm.bomId = $scope.data.bomId;
            var chekedId = $scope.data.chekedId;
            var scannedUpns = null;

            function loadScannedParts() {
                PartTrackingService.getScannedUpnsByChecklistStep(chekedId).then(
                    function (data) {
                        scannedUpns = data;
                        loadItems();
                    }
                )
            }

            function loadItems() {
                ItemBomService.getBomTreeChildren(vm.bomId, $bom.bomObject.parent.id).then(
                    function (data) {
                        vm.children = data;
                        addChildren(vm.children);
                        if ($bom.bomObject.item.itemType.parent == 'GENERIC' || $bom.bomObject.item.itemType.parent == 'PART'
                            || $bom.bomObject.item.itemType.storeAsLot) {
                            vm.items.unshift($bom.bomObject.item);
                        }
                    }
                );
            }

            function addChildren(children) {
                angular.forEach(children, function (child) {
                    if (child.item.itemType.parent == 'PART') {
                        angular.forEach(child.batchItemsIssued, function (item) {
                            item.show = false;
                            angular.forEach(scannedUpns, function (scanUpn) {
                                if (item.upnNumber == scanUpn.upn) {
                                    item.show = true;
                                }
                            })
                        });
                        vm.items.push(child);
                    } else if (child.item.itemType.storeAsLot) {
                        angular.forEach(child.imLotItemsIssueds, function (item) {
                            item.show = false;
                            angular.forEach(scannedUpns, function (scanUpn) {
                                if (item.upnNumber == scanUpn.upn) {
                                    item.show = true;
                                }
                            })
                        });
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
                    angular.forEach(item.batchItemsIssued, function (allocation) {
                        if (!allocation.show && !allocation.hasFailureList && (allocation.scannedUpn == null || allocation.scannedUpn == undefined || allocation.scannedUpn == "")) {
                            $rootScope.showErrorMessage("Please enter all Part no's");
                            valid = false;
                        }
                    })
                });
                return valid;
            }

            function verify() {
                if (validation() == true) {
                    var val = true;
                    var scannedUpns = "";
                    angular.forEach(vm.items, function (item) {
                        angular.forEach(item.batchItemsIssued, function (allocation) {
                            if (!allocation.show) {
                                allocation.valid = true;
                                angular.forEach(item.batchItemsIssued, function (allocation1) {
                                    if (!allocation1.hasFailureList) {
                                        if (allocation.upnNumber == allocation1.scannedUpn) {
                                            allocation.valid = false;
                                            scannedUpns = scannedUpns + allocation.scannedUpn + ';';
                                        }
                                    }
                                });
                                if (!allocation.hasFailureList) {
                                    if (allocation.valid) {
                                        val = false;
                                        $rootScope.showErrorMessage("Part number(s) not valid");
                                    }
                                }
                            }
                        })
                    });
                    //$timeout(function () {
                    if (val) {
                        $rootScope.showSuccessMessage("Parts scanned successfully");
                        $rootScope.hideSidePanel();
                        $scope.callback(scannedUpns);
                    }
                    //}, 1500)
                }

            }

            (function () {
                loadScannedParts();
                $rootScope.$on('app.checklist.verify', verify);
            })();
        }
    }
)
;

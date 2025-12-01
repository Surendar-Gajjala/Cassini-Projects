/**
 * Created by Nageshreddy on 27-03-2018.
 */
define([
        'app/desktop/modules/bom/bom.module',
        'app/shared/services/core/partTrackingService',
        'app/shared/services/core/bomService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService'
    ],
    function (module) {
        module.controller('ScanPartTrackingController', ScanPartTrackingController);

        function ScanPartTrackingController($scope, $q, $bom, $rootScope, $timeout, $window,
                                            BomService, ItemService, AttachmentService) {
            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;

            vm.items = [];
            vm.children = null;
            vm.bomId = $scope.data.bomId;
            var path = $bom.bomObject.bom + "/" + $scope.data.bomId;

            function loadItems() {
                path = path.replace("/", "%2F");
                BomService.getSectionParts(path).then(
                    function (data) {
                        vm.items = data;
                    }
                )
            }

            /*function addChildren(children) {
                angular.forEach(children, function (child) {
                    if (child.item.itemType.parent == 'PART' || child.item.itemType.parent == 'GENERIC' ||
                        child.item.itemType.storeAsLot) {
                        vm.items.push(child);
                    }
                    if (child.children.length > 0) {
                        addChildren(child.children);
                    }
                });
             }*/

            function validation() {
                var valid = true;
                angular.forEach(vm.items, function (item) {
                    if (!item.item.itemMaster.itemType.hasLots) {
                        angular.forEach(item.issuedInstances, function (issued) {
                            if (!issued.hasFailed && (issued.scannedUpn == null || issued.scannedUpn == undefined || issued.scannedUpn == "")) {
                                $rootScope.showErrorMessage("Please enter all Part no's");
                                valid = false;
                            }
                        })
                    }
                });
                return valid;
            }

            function verifyPartNumbers() {
                if (validation() == true) {
                    var val = true;
                    var scannedUpns = "";
                    angular.forEach(vm.items, function (item) {
                        if (item.item.itemMaster.itemType.hasLots) {
                            angular.forEach(item.imLotItemsIssueds, function (allocation) {
                                allocation.valid = true;
                                angular.forEach(item.imLotItemsIssueds, function (allocation1) {
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
                                        $rootScope.showErrorMessage("Lot number(s) not valid");
                                    }
                                }
                            })
                        } else if (!item.item.itemMaster.itemType.hasLots) {
                            angular.forEach(item.issuedInstances, function (allocation) {
                                allocation.valid = true;
                                angular.forEach(item.issuedInstances, function (allocation1) {
                                    if (!allocation1.hasFailed) {
                                        if (allocation.upnNumber == allocation1.scannedUpn) {
                                            allocation.valid = false;
                                            scannedUpns = scannedUpns + allocation.scannedUpn + ';';
                                        }
                                    }
                                });
                                if (!allocation.hasFailed) {
                                    if (allocation.valid) {
                                        val = false;
                                        $rootScope.showErrorMessage("Part number(s) not valid");
                                    }
                                }
                            })
                        }
                    });
                    //$timeout(function () {
                    if (val) {
                        if (scannedUpns != "")
                            $rootScope.showSuccessMessage("Parts scanned successfully");
                        $rootScope.hideSidePanel();
                        $scope.callback(scannedUpns);
                    }
                    //}, 1500)
                }
            }

            (function () {
                loadItems();
                $scope.$on('app.partTracking.scan', verifyPartNumbers);
            })();
        }
    }
)
;

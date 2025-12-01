/**
 * Created by Nageshreddy on 21-05-2018.
 */
define([
        'app/desktop/modules/bom/bom.module',
        'app/shared/services/core/partTrackingService',
        'app/shared/services/core/bomService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService'
    ],
    function (module) {
        module.controller('ShowScanPartTrackingController', ShowScanPartTrackingController);

        function ShowScanPartTrackingController($scope, $q, $bom, $rootScope, $timeout, $window, PartTrackingService,
                                                BomService, ItemService, AttachmentService) {
            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;

            vm.items = [];
            vm.children = null;
            vm.bomId = $scope.data.bomId;
            var chekedId = $scope.data.chekedId;
            var scannedUpns = null;
            var path = $bom.bomObject.bom + "/" + $scope.data.bomId;

            function loadItems() {
                path = path.replace("/", "%2F");
                BomService.getSectionParts(path).then(
                    function (data) {
                        vm.items = data;
                        angular.forEach(vm.items, function (item) {
                            if (!item.item.itemMaster.itemType.hasLots) {
                                angular.forEach(item.issuedInstances, function (item) {
                                    item.show = false;
                                    angular.forEach(scannedUpns, function (scanUpn) {
                                        if (item.upnNumber == scanUpn.upn) {
                                            item.show = true;
                                            item.scannedUpn = scanUpn.upn;
                                        }
                                    })
                                });
                                //vm.items.push(child);
                            }
                            /*else if (item.item.itemMaster.itemType.hasLots) {
                             angular.forEach(child.issuedInstances, function (item) {
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
                             }*/
                        });
                    }
                )
            }

            function addChildren(children) {
                angular.forEach(children, function (child) {
                    if (!item.item.itemMaster.itemType.hasLots) {
                        angular.forEach(child.issuedInstances, function (item) {
                            item.show = false;
                            angular.forEach(scannedUpns, function (scanUpn) {
                                if (item.upnNumber == scanUpn.upn) {
                                    item.show = true;
                                }
                            })
                        });
                        vm.items.push(child);
                    } else if (item.item.itemMaster.itemType.hasLots) {
                        angular.forEach(child.issuedInstances, function (item) {
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

            function ok() {
                $rootScope.hideSidePanel();
            }

            function loadScannedParts() {
                PartTrackingService.getScannedUpnsByPartTrackingStep(chekedId).then(
                    function (data) {
                        scannedUpns = data;
                        loadItems();
                    }
                )
            }

            (function () {
                loadScannedParts();
                $rootScope.$on('app.checklist.scanned', ok);
            })();
        }
    }
)
;

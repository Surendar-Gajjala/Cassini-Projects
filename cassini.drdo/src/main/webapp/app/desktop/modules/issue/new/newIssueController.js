define(
    [
        'app/desktop/modules/issue/issue.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/requestService',
        'app/shared/services/core/issueService',
        'app/shared/services/core/bomService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/inwardService'
    ],
    function (module) {
        module.controller('NewIssueController', NewIssueController);

        function NewIssueController($scope, $window, $rootScope, $timeout, $state, $stateParams, $cookies, ItemTypeService,
                                    ItemService, $uibModal, $interval, AttachmentService, IssueService, RequestService,
                                    CommonService, DialogService, BomService, ObjectAttributeService, AttributeAttachmentService, InwardService) {

            $rootScope.viewInfo.icon = "fa fa-sign-out";
            $rootScope.viewInfo.title = "New Issue";

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.barcodeBust = (new Date()).getTime();

            vm.issueItems = issueItems;
            vm.back = back;
            vm.onSelectRequest = onSelectRequest;
            vm.selectRequestTitle = "Select Request";
            vm.showPrintIssueUpn = false;

            vm.mode = "ISSUE";

            vm.newIssue = {
                id: null,
                number: null,
                request: null,
                bomInstance: null,
                issuedTo: null,
                notes: null
            };

            function back() {
                window.history.back();
            }

            vm.sidePanel = false;
            vm.showIssueInfoPanel = showIssueInfoPanel;

            function showIssueInfoPanel() {
                vm.sidePanel = !vm.sidePanel;
                if (vm.sidePanel) {
                    $('#issueInfoPanel').show('slide', {direction: 'left'}, 600);
                }
                else {
                    $('#issueInfoPanel').hide('slide', {direction: 'left'}, 600);
                }
            }

            function issueItems() {
                if (validate()) {
                    vm.newIssue.issueItemDtos = [];
                    vm.newIssue.issueItemDtos = vm.selectedItemsToIssue;
                    $rootScope.showBusyIndicator($('.view-container'));
                    IssueService.newIssue(vm.newIssue).then(
                        function (data) {
                            $state.go('app.issues.all');
                            $rootScope.showSuccessMessage("New issue created successfully");
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;

                if (vm.newIssue.request == null || vm.newIssue.request == "" || vm.newIssue.request == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Request");
                    showIssueInfoPanel(true);
                } else if (vm.selectedItemsToIssue.length == 0) {
                    valid = false;
                    $rootScope.showWarningMessage("Please issue at least one item");
                }
                /* else {
                    angular.forEach(vm.requestedItemRows, function (item) {
                        if (valid && item.type == "REQITEM") {
                            var count = 0;
                            angular.forEach(item.issuedItemDto, function (reqItem) {
                                if (reqItem.verified || reqItem.issuedItemInstance != null) {
                                    count++;
                                }
                            })

                            if (count > 0 && item.allocateQty != count) {
                                valid = false;
                                $rootScope.showWarningMessage("Please select all serial numbers for [ " + item.requestItem.item.item.itemMaster.itemName + " ]");
                            }
                        }
                    })
                 }*/


                return valid;
            }

            function validateExpiry() {
                var valid = true;

                angular.forEach(vm.selectedItemsToIssue, function (item) {
                    if (valid) {

                    }
                })


                return valid;
            }

            vm.requestedItemRows = [];
            function onSelectRequest(request) {
                $rootScope.showBusyIndicator($('.view-content'));
                vm.selectedItemsToIssue = [];
                vm.selectedItemMap = new Hashtable();
                RequestService.getItemsByRequest(request.id).then(
                    function (data) {
                        vm.requestedItems = data;
                        var count = 1;
                        angular.forEach(vm.requestedItems, function (item) {
                            item.level = 0;
                            item.expanded = true;
                            item.type = 'REQITEM';
                            item.countId = count;
                            item.selectedInstances = 0;

                            var duplicate = angular.copy(item);
                            duplicate.issuedItemDto = [];

                            count++;
                            angular.forEach(item.issuedItemDto, function (issueItem) {
                                issueItem.countId = count;
                                issueItem.parentItem = duplicate;
                                issueItem.type = "REQPART";
                                issueItem.level = item.level + 1;
                                count++;
                            })
                        });

                        updateRows();
                    }
                )
            }

            function updateRows() {
                vm.requestedItemRows = [];
                angular.forEach(vm.requestedItems, function (item) {
                    vm.requestedItemRows.push(item);
                    if (item.expanded) {
                        angular.forEach(item.issuedItemDto, function (issueItem) {
                            if (!issueItem.issued) {
                                issueItem.verified = false;
                            }
                            vm.requestedItemRows.push(issueItem);
                        });
                    }
                })

                $timeout(function () {
                    $rootScope.hideBusyIndicator();
                }, 100);
            }

            vm.checkUpnNumber = checkUpnNumber;
            function checkUpnNumber(item) {
                if (validateUpn(item)) {
                    item.serialNumber = item.scanUpn;
                    RequestService.validateUpnNumber(vm.newIssue.request.id, item.requestItem.item.id, item.scanUpn).then(
                        function (data) {
                            var today = moment(new Date());
                            var todayStr = today.format('DD/MM/YYYY');
                            var todayDate = moment(todayStr, 'DD/MM/YYYY');

                            if (data.expiryDate != null && data.expiryDate != "" && data.expiryDate != undefined) {
                                var instanceExpiryDate = moment(data.expiryDate, 'DD/MM/YYYY');

                                if (instanceExpiryDate.isBefore(todayDate)) {
                                    $rootScope.showErrorMessage("Selected Item already Expired. Please check once");
                                } else {
                                    item.scanUpn = data.upnNumber;
                                    item.itemInstance = data;
                                    item.verified = true;
                                    vm.selectedItemsToIssue.push(item);
                                    $rootScope.showSuccessMessage("Verification success");
                                }
                            } else {
                                item.scanUpn = data.upnNumber;
                                item.itemInstance = data;
                                item.verified = true;
                                vm.selectedItemsToIssue.push(item);
                                $rootScope.showSuccessMessage("Verification success");
                            }


                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.selectedItemMap = new Hashtable();
            vm.addItemInstance = addItemInstance;
            vm.removeItemInstance = removeItemInstance;
            function addItemInstance(item) {
                var exist = vm.selectedItemMap.get(item.selectInstanceToIssue.id);
                if (exist == null) {
                    InwardService.generateRootCardNumber(item.selectInstanceToIssue.id).then(
                        function (data) {
                            item.scanUpn = item.selectInstanceToIssue.upnNumber;
                            item.selectInstanceToIssue.rootCardNo = data.rootCardNo;
                            item.itemInstance = item.selectInstanceToIssue;
                            item.verified = true;
                            vm.selectedItemsToIssue.push(item);
                            vm.selectedItemMap.put(item.selectInstanceToIssue.id, item.selectInstanceToIssue);

                            angular.forEach(vm.requestedItemRows, function (requestedItem) {
                                if (requestedItem.type == "REQPART" && requestedItem.countId != item.countId && requestedItem.parentItem.countId == item.parentItem.countId) {
                                    angular.forEach(requestedItem.inventoryInstances, function (instance) {
                                        if (item.selectInstanceToIssue.id == instance.id) {
                                            requestedItem.inventoryInstances.splice(requestedItem.inventoryInstances.indexOf(instance), 1);
                                        }
                                    })
                                }
                            })
                        }
                    )
                } else {
                    item.selectInstanceToIssue = null;
                    $rootScope.showErrorMessage("Selected serial number already in Issue");
                }
            }

            function removeItemInstance(item) {
                var selectedItem = item.selectInstanceToIssue;
                item.scanUpn = null;
                item.selectInstanceToIssue.rootCardNo = null;
                item.itemInstance = null;
                item.verified = false;
                vm.selectedItemsToIssue.splice(vm.selectedItemsToIssue.indexOf(item), 1);
                vm.selectedItemMap.remove(item.selectInstanceToIssue.id);

                angular.forEach(vm.requestedItemRows, function (requestedItem) {
                    if (requestedItem.type == "REQPART" && requestedItem.countId != item.countId && requestedItem.parentItem.countId == item.parentItem.countId) {
                        requestedItem.inventoryInstances.push(selectedItem);
                    }
                })
            }

            /*vm.onSelectInstanceToIssue = onSelectInstanceToIssue;
             function onSelectInstanceToIssue(item, reqItem) {
             if (item != null) {

             angular.forEach(vm.requestedItemRows, function (requestedItem) {
             if (requestedItem.type == "REQPART" && requestedItem.id != reqItem.id && requestedItem.parentItem.id == reqItem.parentItem.id) {
             angular.forEach(requestedItem.inventoryInstances, function (instance) {
             if (item.id == instance.id) {
             requestedItem.inventoryInstances.splice(requestedItem.inventoryInstances.indexOf(instance), 1);
             }
             })
             }
             })
             }
             }*/

            function validateUpn(requestItem) {
                var valid = true;

                if (requestItem.scanUpn == null || requestItem.scanUpn == "" || requestItem.scanUpn == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Serial Number");
                }
                /* else if (requestItem.scanUpn.length < 16) {
                 valid = false;
                 $rootScope.showWarningMessage("Please enter 16 digit UPN");
                 }*/

                angular.forEach(vm.selectedItemsToIssue, function (item) {
                    if (valid) {
                        if (item.serialNumber == requestItem.scanUpn) {
                            valid = false;
                            $rootScope.showErrorMessage(requestItem.scanUpn + " : Serial Number already verified. Please select another part");
                        }
                    }
                });

                return valid;
            }

            function loadFinishedRequests() {
                $timeout(function () {
                    $rootScope.showBusyIndicator($('.view-container'));
                }, 100);
                RequestService.getFinishedRequests().then(
                    function (data) {
                        vm.requests = data;
                        if (vm.requests.length == 0) {
                            vm.selectRequestTitle = "No Requests";
                        }
                        $rootScope.hideBusyIndicator();
                        showIssueInfoPanel();
                    }
                )
            }

            vm.selectedItemToIssue = null;
            vm.printIssuedUpn = printIssuedUpn;
            function printIssuedUpn(scanUpn) {
                $rootScope.showBusyIndicator($("#newIssueView"));
                vm.selectedItemToIssue = scanUpn;
                vm.showPrintIssueUpn = true;

                $timeout(function () {
                    var divElements = document.getElementById("issueItemBarcode");
                    $rootScope.hideBusyIndicator();
                    var printData = window.open("", "", "left=0,top=0,width=800,height=900,toolbar=0,scrollbars=0,status=0");
                    printData.document.write(divElements.innerHTML);
                    printData.document.close();
                    printData.focus();
                    $timeout(
                        function () {
                            printData.print();
                            printData.close();
                            vm.showPrintIssueUpn = false;
                            vm.selectedItemToIssue = null;
                        }, 1000
                    );
                }, 2000)
            }

            function loadRequest() {
                $timeout(function () {
                    $rootScope.showBusyIndicator($('.view-container'));
                }, 100);
                RequestService.getRequest($stateParams.requestId).then(
                    function (data) {
                        vm.request = data;
                        vm.newIssue.request = vm.request;
                        onSelectRequest(vm.request);
                        showIssueInfoPanel();
                    }
                )
            }


            (function () {
                vm.mode = $stateParams.issueMode;
                if ($stateParams.issueMode == "ISSUE") {
                    loadFinishedRequests();
                } else if ($stateParams.issueMode == "REQ") {
                    loadRequest();
                }
            })();
        }
    }
);
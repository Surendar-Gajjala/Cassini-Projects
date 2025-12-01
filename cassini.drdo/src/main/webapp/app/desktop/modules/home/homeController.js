define(
    [
        'app/desktop/modules/home/home.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/shared/services/core/inwardService',
        'app/shared/services/core/requestService',
        'app/shared/services/core/issueService',
        'app/shared/services/core/dispatchService',
        'app/shared/services/core/failureListService',
        'app/shared/services/core/procurementService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/bomService'
    ],
    function (module) {
        module.controller('HomeController', HomeController);

        function HomeController($scope, $rootScope, $timeout, $state, $cookies, $uibModal, $window, InwardService, RequestService,
                                DispatchService, FailureListService, IssueService, CommonService, DialogService, BomService, ProcurementService) {

            /*$scope.templates = {
             inwardsWidget: "app/desktop/modules/home/widgets/inwards/inwardWidgetView.jsp",
             issueWidget: "app/desktop/modules/home/widgets/issues/issueWidgetView.jsp",
             requestWidget: "app/desktop/modules/home/widgets/requests/requestWidgetView.jsp",
             dispatchWidget: "app/desktop/modules/home/widgets/dispatches/dispatchWidgetView.jsp"
             };*/

            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = "Home";

            var vm = this;
            vm.noPermissionView = false;
            vm.notificationView = null;
            $rootScope.checkWithSystemWithMissile = false;
            $rootScope.checkWithSystem = false;
            $rootScope.checkReqSystemWithMissile = false;
            $rootScope.checkStorage = false;
            $rootScope.selectedType = false;

            vm.pendingPurchaseOrders = [];
            vm.pendingRequisitions = [];
            vm.reorderItems = [];

            vm.gatePassFilter = {
                searchQuery: null,
                gatePassNumber: null,
                gatePassName: null,
                finish: false
            };

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

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

            vm.inwards = angular.copy(pagedResults);
            vm.requests = angular.copy(pagedResults);
            vm.issues = angular.copy(pagedResults);
            vm.returnItems = angular.copy(pagedResults);
            vm.failureItems = angular.copy(pagedResults);
            vm.dispatches = angular.copy(pagedResults);
            vm.expiredItems = angular.copy(pagedResults);

            vm.changeView = changeView;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;

            vm.showInwardDetails = showInwardDetails;
            vm.showRequestDetails = showRequestDetails;
            vm.showIssueDetails = showIssueDetails;
            vm.showDispatchDetails = showDispatchDetails;

            vm.showNewInward = showNewInward;
            vm.showNewRequest = showNewRequest;
            vm.showNewIssue = showNewIssue;
            vm.showNewGatePass = showNewGatePass;
            vm.showFailureList = showFailureList;

            vm.showGatePassView = false;
            vm.showInwardView = false;
            vm.showInwardItemsView = false;
            vm.showRequestView = false;
            vm.showIssuesView = false;
            vm.showDispatchView = false;
            vm.showReturnView = false;
            vm.showFailureView = false;
            vm.showExpiryItemsView = false;


            vm.inwardFilter = {
                notification: true,
                status: null,
                searchQuery: null,
                finishedPage: false,
                gatePassView: false
            };

            vm.requestFilter = {
                notification: true,
                status: null,
                searchQuery: null,
                issued: false,
                fromDate: null,
                toDate: null,
                month: null
            };

            vm.dispatchFilter = {
                notification: true,
                status: null,
                searchQuery: null
            };

            vm.issueFilter = {
                notification: true,
                searchQuery: null,
                fromDate: null,
                toDate: null,
                missile: ''
            };
            var bomInstanceItem = null;

            vm.failedLotsPopover = {
                templateUrl: 'app/desktop/modules/home/widgets/lotInstancesPopover.jsp'
            };
            vm.showFailureListForPart = showFailureListForPart;
            vm.showFailureListForLot = showFailureListForLot;

            function showFailureListForPart(instance) {
                BomService.getBomInstanceItemByItemInstance(instance.id).then(
                    function (data) {
                        bomInstanceItem = data;
                        showFailureList(instance, bomInstanceItem);
                    })
            }

            function showFailureListForLot(lotInstance, instance) {
                BomService.getBomInstanceItemByItemInstance(lotInstance.instance).then(
                    function (data) {
                        bomInstanceItem = data;
                        lotInstance.itemInstance = instance;
                        showFailureList(lotInstance, bomInstanceItem);
                    })
            }

            function showFailureList(instance, bomInstanceItem) {
                var options = {
                    title: 'Failure Report - ' + instance.upnNumber,
                    showMask: true,
                    side: 'left',
                    template: 'app/desktop/modules/bom/details/failureList/showFailureValueListView.jsp',
                    controller: "ShowFailureValueListController as failureValueListVm",
                    resolve: 'app/desktop/modules/bom/details/failureList/showFailureValueListController',
                    width: 700,
                    buttons: [
                        {text: 'Update', broadcast: 'update.failureList'},
                        {text: 'Print', broadcast: 'print.failureList'}
                    ],
                    data: {
                        item: bomInstanceItem,
                        //selectedItem: $rootScope.selectedBomItemDetails,
                        instance: instance
                    },
                    callback: function () {
                        ItemService.updateBomItem(item.id, item).then(
                            function (data) {
                                $rootScope.hideSidePanel('left');
                                $rootScope.showSuccessMessage("Failure report created successfully");
                            }
                        )
                    }
                };
                $rootScope.showSidePanel(options);


            }

            function changeView(notification) {
                vm.notificationView = notification;
                vm.pageable = {
                    page: 0,
                    size: 20,
                    sort: {
                        field: "modifiedDate",
                        order: "DESC"
                    }
                };
                loadNotifications();
                $scope.$evalAsync();
            }

            function nextPage() {
                if (vm.notificationView == "Inward") {
                    if (vm.inwards.last != true) {
                        vm.pageable.page++;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "InwardItems") {
                    if (vm.inwardItems.last != true) {
                        vm.pageable.page++;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "Request") {
                    if (vm.requests.last != true) {
                        vm.pageable.page++;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "Issue") {
                    if (vm.issues.last != true) {
                        vm.pageable.page++;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "Return") {
                    if (vm.returnItems.last != true) {
                        vm.pageable.page++;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "Failure") {
                    if (vm.failureItems.last != true) {
                        vm.pageable.page++;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "FailureProcess") {
                    if (vm.failureProcessItems.last != true) {
                        vm.pageable.page++;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "Dispatch") {
                    if (vm.dispatches.last != true) {
                        vm.pageable.page++;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "GatePass") {
                    if (vm.gatePasses.last != true) {
                        vm.pageable.page++;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "ExpiryItems") {
                    if (vm.expiredItems.last != true) {
                        vm.pageable.page++;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "ToExpireItems") {
                    if (vm.toExpireItems.last != true) {
                        vm.pageable.page++;
                        loadNotifications();
                    }
                }
            }

            function previousPage() {
                if (vm.notificationView == "Inward") {
                    if (vm.inwards.first != true) {
                        vm.pageable.page--;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "InwardItems") {
                    if (vm.inwardItems.first != true) {
                        vm.pageable.page--;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "Request") {
                    if (vm.requests.first != true) {
                        vm.pageable.page--;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "Issue") {
                    if (vm.issues.first != true) {
                        vm.pageable.page--;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "Return") {
                    if (vm.returnItems.first != true) {
                        vm.pageable.page--;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "Failure") {
                    if (vm.failureItems.first != true) {
                        vm.pageable.page--;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "FailureProcess") {
                    if (vm.failureProcessItems.first != true) {
                        vm.pageable.page--;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "Dispatch") {
                    if (vm.dispatches.first != true) {
                        vm.pageable.page--;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "GatePass") {
                    if (vm.gatePasses.first != true) {
                        vm.pageable.page--;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "ExpiryItems") {
                    if (vm.expiredItems.first != true) {
                        vm.pageable.page--;
                        loadNotifications();
                    }
                } else if (vm.notificationView == "ToExpireItems") {
                    if (vm.toExpireItems.first != true) {
                        vm.pageable.page--;
                        loadNotifications();
                    }
                }
            }

            function loadNotifications() {
                switch (vm.notificationView) {
                    case "InwardItems":
                        vm.loading = true;
                        vm.inwardFilter.adminPermission = $rootScope.hasPermission('permission.admin.all');
                        vm.inwardFilter.storeApprove = $rootScope.hasPermission('permission.inward.storeApprove');
                        vm.inwardFilter.ssqagApprove = $rootScope.hasPermission('permission.inward.SSQAGApprove');
                        vm.inwardFilter.bdlApprove = $rootScope.hasPermission('permission.requests.bdlApprove');
                        vm.inwardFilter.casApprove = $rootScope.hasPermission('permission.requests.casApprove');
                        vm.inwardFilter.gatePassPermission = $rootScope.hasPermission('permission.gatePass.view');
                        loadInwardItems();
                        break;
                    case "Inward":
                        vm.loading = true;
                        vm.inwardFilter.adminPermission = $rootScope.hasPermission('permission.admin.all');
                        vm.inwardFilter.storeApprove = $rootScope.hasPermission('permission.inward.storeApprove');
                        vm.inwardFilter.ssqagApprove = $rootScope.hasPermission('permission.inward.SSQAGApprove');
                        vm.inwardFilter.bdlApprove = $rootScope.hasPermission('permission.requests.bdlApprove');
                        vm.inwardFilter.casApprove = $rootScope.hasPermission('permission.requests.casApprove');
                        vm.inwardFilter.gatePassPermission = $rootScope.hasPermission('permission.gatePass.view');
                        loadInwards();
                        break;
                    case "Request":
                        vm.loading = true;
                        loadRequests();
                        break;
                    case "Issue":
                        vm.loading = true;
                        vm.issueFilter.adminPermission = $rootScope.hasPermission('permission.admin.all');
                        vm.issueFilter.storeApprove = $rootScope.hasPermission('permission.inward.storeApprove');
                        vm.issueFilter.bdlQcApprove = $rootScope.hasPermission('permission.issued.BDLQCApprove');
                        vm.issueFilter.bdlPpcReceive = $rootScope.hasPermission('permission.bdlPcc.receive');
                        vm.issueFilter.casApprove = $rootScope.hasPermission('permission.requests.casApprove');
                        vm.issueFilter.bdlApprove = $rootScope.hasPermission('permission.requests.bdlApprove');
                        vm.issueFilter.versity = $rootScope.hasPermission('permission.versity.group');
                        vm.issueFilter.versityApprove = $rootScope.hasPermission('permission.requests.versityApprove');
                        vm.issueFilter.versityQc = $rootScope.hasPermission('permission.issued.versityQCApprove');
                        vm.issueFilter.versityPpc = $rootScope.hasPermission('permission.versityPpc.receive');
                        loadIssues();
                        break;
                    case "Return":
                        vm.loading = true;
                        loadReturnItems();
                        break;
                    case "Failure":
                        vm.loading = true;
                        loadFailureItems();
                        break;
                    case "FailureProcess":
                        vm.loading = true;
                        loadFailureProcessItems();
                        break;
                    case "Dispatch":
                        vm.loading = true;
                        vm.dispatchFilter.adminPermission = $rootScope.hasPermission('permission.admin.all');
                        vm.dispatchFilter.storeApprove = $rootScope.hasPermission('permission.inward.storeApprove');
                        vm.dispatchFilter.ssqagApprove = $rootScope.hasPermission('permission.inward.SSQAGApprove');
                        vm.dispatchFilter.bdlApprove = $rootScope.hasPermission('permission.requests.bdlApprove');
                        vm.dispatchFilter.casApprove = $rootScope.hasPermission('permission.requests.casApprove');
                        loadDispatches();
                        break;
                    case "GatePass":
                        vm.loading = true;
                        vm.gatePassFilter.adminPermission = $rootScope.hasPermission('permission.admin.all');
                        vm.gatePassFilter.storeApprove = $rootScope.hasPermission('permission.inward.storeApprove');
                        vm.gatePassFilter.viewGatePass = $rootScope.hasPermission('permission.inward.newGatePass');
                        loadGatePasses();
                        break;
                    case "ExpiryItems":
                        vm.loading = true;
                        loadExpiryItems();
                        break;
                    case "ToExpireItems":
                        vm.loading = true;
                        loadToExpireItems();
                        break;
                    default :
                }
            }

            function loadExpiryItems() {
                vm.showGatePassView = false;
                vm.showInwardView = false;
                vm.showRequestView = false;
                vm.showIssuesView = false;
                vm.showDispatchView = false;
                vm.showReturnView = false;
                vm.showFailureView = false;
                vm.showInwardItemsView = false;
                vm.showFailureProcessView = false;
                vm.showToExpireItems = false;
                vm.showExpiryItemsView = true;
                vm.pageable.page = 0;
                InwardService.getExpiredItems(vm.pageable).then(
                    function (data) {
                        vm.expiredItems = data;
                        vm.loading = false;
                    }
                );

            }

            function loadToExpireItems() {
                vm.showGatePassView = false;
                vm.showInwardView = false;
                vm.showRequestView = false;
                vm.showIssuesView = false;
                vm.showDispatchView = false;
                vm.showReturnView = false;
                vm.showFailureView = false;
                vm.showInwardItemsView = false;
                vm.showFailureProcessView = false;
                vm.showExpiryItemsView = false;
                vm.showToExpireItems = true;
                vm.pageable.page = 0;
                InwardService.getToExpireItems(vm.pageable).then(
                    function (data) {
                        vm.toExpireItems = data;
                        vm.loading = false;
                    }
                );

            }

            function loadInwardItems() {
                vm.showGatePassView = false;
                vm.showInwardView = false;
                vm.showRequestView = false;
                vm.showIssuesView = false;
                vm.showDispatchView = false;
                vm.showReturnView = false;
                vm.showFailureView = false;
                vm.showToExpireItems = false;
                vm.showInwardItemsView = true;
                vm.showFailureProcessView = false;
                vm.showExpiryItemsView = false;
                InwardService.getAllInwardItems(vm.pageable, vm.inwardFilter).then(
                    function (data) {
                        if (data != "" && data != null) {
                            vm.inwardItems = data;
                        }
                        vm.loading = false;
                        $scope.$evalAsync();
                    }
                );
            }

            function loadInwards() {
                vm.showGatePassView = false;
                vm.showInwardItemsView = false;
                vm.showRequestView = false;
                vm.showIssuesView = false;
                vm.showDispatchView = false;
                vm.showReturnView = false;
                vm.showFailureView = false;
                vm.showInwardView = true;
                vm.showFailureProcessView = false;
                vm.showExpiryItemsView = false;
                vm.showToExpireItems = false;
                InwardService.getAllInwards(vm.pageable, vm.inwardFilter).then(
                    function (data) {
                        if (data != "" && data != null) {
                            vm.inwards = data;
                            CommonService.getPersonReferences(vm.inwards.content, 'createdBy');

                            angular.forEach(vm.inwards.content, function (inward) {
                                var today = moment(new Date());
                                var todayStr = today.format('DD/MM/YYYY');
                                var todayDate = moment(todayStr, 'DD/MM/YYYY');

                                var startDate = moment(inward.createdDate, 'DD/MM/YYYY');

                                if (todayDate.isSame(startDate)) {
                                    inward.showNew = true;
                                } else {
                                    inward.showNew = false;
                                }
                            })
                        }
                        vm.loading = false;
                        $scope.$evalAsync();
                    }
                );
            }

            function loadRequests() {
                vm.showGatePassView = false;
                vm.showInwardView = false;
                vm.showInwardItemsView = false;
                vm.showIssuesView = false;
                vm.showDispatchView = false;
                vm.showReturnView = false;
                vm.showFailureView = false;
                vm.showRequestView = true;
                vm.showFailureProcessView = false;
                vm.showExpiryItemsView = false;
                vm.showToExpireItems = false;
                vm.loading = true;
                RequestService.getAllRequests(vm.pageable, vm.requestFilter).then(
                    function (data) {
                        vm.requests = data;
                        if (vm.requests == "" || vm.requests == null) {
                            vm.requests = angular.copy(pagedResults);
                        }
                        vm.loading = false;
                        $scope.$evalAsync();
                    }
                );
            }

            function loadIssues() {
                vm.showGatePassView = false;
                vm.showInwardView = false;
                vm.showInwardItemsView = false;
                vm.showRequestView = false;
                vm.showDispatchView = false;
                vm.showReturnView = false;
                vm.showFailureView = false;
                vm.showIssuesView = true;
                vm.showFailureProcessView = false;
                vm.showExpiryItemsView = false;
                vm.showToExpireItems = false;
                vm.loading = true;
                IssueService.getAllIssues(vm.pageable, vm.issueFilter).then(
                    function (data) {
                        vm.issues = data;
                        CommonService.getPersonReferences(vm.issues.content, 'modifiedBy');
                        vm.loading = false;
                        $scope.$evalAsync();
                    }
                )
            }

            function loadReturnItems() {
                vm.showGatePassView = false;
                vm.showInwardView = false;
                vm.showInwardItemsView = false;
                vm.showRequestView = false;
                vm.showIssuesView = false;
                vm.showDispatchView = false;
                vm.showFailureView = false;
                vm.showReturnView = true;
                vm.showFailureProcessView = false;
                vm.showExpiryItemsView = false;
                vm.showToExpireItems = false;
                InwardService.getAllReturnItems(vm.pageable).then(
                    function (data) {
                        vm.returnItems = data;
                        vm.loading = false;
                        $scope.$evalAsync();
                    }
                )
            }

            function loadFailureItems() {
                vm.showGatePassView = false;
                vm.showInwardView = false;
                vm.showInwardItemsView = false;
                vm.showRequestView = false;
                vm.showIssuesView = false;
                vm.showDispatchView = false;
                vm.showReturnView = false;
                vm.showFailureView = true;
                vm.showFailureProcessView = false;
                vm.showExpiryItemsView = false;
                vm.showToExpireItems = false;
                InwardService.getAllFailedItems(vm.pageable).then(
                    function (data) {
                        vm.failureItems = data;
                        vm.loading = false;
                        $scope.$evalAsync();
                    }
                )
            }

            function loadFailureProcessItems() {
                vm.showGatePassView = false;
                vm.showInwardView = false;
                vm.showInwardItemsView = false;
                vm.showRequestView = false;
                vm.showIssuesView = false;
                vm.showDispatchView = false;
                vm.showReturnView = false;
                vm.showFailureView = false;
                vm.showFailureProcessView = true;
                vm.showExpiryItemsView = false;
                vm.showToExpireItems = false;
                FailureListService.getFailProcessItems(vm.pageable).then(
                    function (data) {
                        vm.failureProcessItems = data;
                        vm.loading = false;
                        $scope.$evalAsync();
                    }
                )
            }

            function loadDispatches() {
                vm.showGatePassView = false;
                vm.showInwardView = false;
                vm.showInwardItemsView = false;
                vm.showRequestView = false;
                vm.showIssuesView = false;
                vm.showReturnView = false;
                vm.showFailureView = false;
                vm.showDispatchView = true;
                vm.showFailureProcessView = false;
                vm.showExpiryItemsView = false;
                vm.showToExpireItems = false;
                DispatchService.getAllDispatches(vm.pageable, vm.dispatchFilter).then(
                    function (data) {
                        vm.dispatches = data;
                        CommonService.getPersonReferences(vm.dispatches.content, 'createdBy');
                        vm.loading = false;
                        $scope.$evalAsync();
                    }
                )
            }

            function loadGatePasses() {
                vm.pageable.sort.field = "createdDate";
                vm.pageable.sort.order = "Desc";

                vm.showInwardView = false;
                vm.showInwardItemsView = false;
                vm.showRequestView = false;
                vm.showIssuesView = false;
                vm.showDispatchView = false;
                vm.showReturnView = false;
                vm.showFailureView = false;
                vm.showGatePassView = true;
                vm.showFailureProcessView = false;
                vm.showExpiryItemsView = false;
                vm.showToExpireItems = false;
                InwardService.getAllGatePasses(vm.pageable, vm.gatePassFilter).then(
                    function (data) {
                        vm.gatePasses = data;

                        angular.forEach(vm.gatePasses.content, function (gatePass) {
                            var today = moment(new Date());
                            var todayStr = today.format('DD/MM/YYYY');
                            var todayDate = moment(todayStr, 'DD/MM/YYYY');

                            var startDate = moment(gatePass.createdDate, 'DD/MM/YYYY');

                            if (todayDate.isSame(startDate)) {
                                gatePass.showNew = true;
                            } else {
                                gatePass.showNew = false;
                            }
                        })


                        CommonService.getPersonReferences(vm.gatePasses.content, 'createdBy');
                        vm.loading = false;
                        $scope.$evalAsync();
                    }
                )
            }

            function showInwardDetails(inward) {
                $state.go('app.inwards.details', {inwardId: inward.id, mode: 'home'});
            }

            function showRequestDetails(req) {
                $state.go('app.requests.details', {requestId: req.id, mode: 'home'});
            }

            function showIssueDetails(issue) {
                $state.go('app.issues.details', {issueId: issue.id, mode: 'home'});
            }

            function showDispatchDetails(dispatch) {
                var button = null;
                if (dispatch.status == "NEW" && $rootScope.hasPermission('permission.dispatch.edit')) {
                    button = {text: "Update", broadcast: 'app.dispatch.details'};
                } else {
                    button = {text: "Close", broadcast: 'app.dispatch.close'}
                }

                var options = {
                    title: dispatch.number + " Details",
                    template: 'app/desktop/modules/dispatch/details/dispatchDetailsView.jsp',
                    controller: 'DispatchDetailsController as dispatchDetailsVm',
                    resolve: 'app/desktop/modules/dispatch/details/dispatchDetailsController',
                    width: 700,
                    showMask: true,
                    data: {
                        dispatchDetails: dispatch
                    },
                    buttons: [
                        button
                    ],
                    callback: function (result) {
                        loadDispatches();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.showIssue = showIssue;
            function showIssue(issue) {
                $state.go('app.issues.details', {issueId: issue.id});
            }

            vm.notificationFilter = {
                adminPermission: true,
                storeApprove: false,
                ssqagApprove: false,
                bdlApprove: false,
                casApprove: false,
                newRequest: false,
                bdlQcApprove: false
            };

            vm.showNotifications = true;
            function loadAllNotifications() {
                vm.loading = true;
                vm.notificationFilter.adminPermission = $rootScope.hasPermission('permission.admin.all');
                vm.notificationFilter.storeApprove = $rootScope.hasPermission('permission.inward.storeApprove');
                vm.notificationFilter.ssqagApprove = $rootScope.hasPermission('permission.inward.SSQAGApprove');
                vm.notificationFilter.bdlApprove = $rootScope.hasPermission('permission.requests.bdlApprove');
                vm.notificationFilter.casApprove = $rootScope.hasPermission('permission.requests.casApprove');
                vm.notificationFilter.gatePassView = $rootScope.hasPermission('permission.gatePass.view');
                vm.notificationFilter.newRequest = $rootScope.hasPermission('permission.requests.new');
                vm.notificationFilter.bdlQcApprove = $rootScope.hasPermission('permission.issued.BDLQCApprove');
                vm.notificationFilter.bdlPpcReceive = $rootScope.hasPermission('permission.bdlPcc.receive');
                vm.notificationFilter.versity = $rootScope.hasPermission('permission.versity.group');
                vm.notificationFilter.versityApprove = $rootScope.hasPermission('permission.requests.versityApprove');
                vm.notificationFilter.versityQc = $rootScope.hasPermission('permission.issued.versityQCApprove');
                vm.notificationFilter.versityPpc = $rootScope.hasPermission('permission.versityPpc.receive');
                if ($rootScope.hasPermission("permission.gatePass.view")) {
                    vm.notificationView = "GatePass";
                } else if ($rootScope.hasPermission('permission.admin.all') || $rootScope.hasPermission('permission.inward.view')) {
                    vm.notificationView = "Inward";
                } else if ($rootScope.hasPermission('permission.admin.all') || $rootScope.hasPermission('permission.inward.storeApprove') || $rootScope.hasPermission('permission.inward.SSQAGApprove')) {
                    vm.notificationView = "InwardItems";
                } else if ($rootScope.hasPermission('permission.admin.all') || $rootScope.hasPermission('permission.requests.bdlApprove')
                    || $rootScope.hasPermission('permission.requests.casApprove') || $rootScope.hasPermission("permission.requests.new")) {
                    vm.notificationView = "Request";
                } else if ($rootScope.hasPermission('permission.admin.all') || $rootScope.hasPermission('permission.issued.view')) {
                    vm.notificationView = "Issue";
                } else if ($rootScope.hasPermission('permission.admin.all') || $rootScope.hasPermission('permission.dispatch.view')) {
                    vm.notificationView = "Dispatch";
                }
                DispatchService.getAllNotifications(vm.notificationFilter).then(
                    function (data) {
                        vm.notifications = data;
                        vm.loading = false;
                        loadNotifications();
                        $rootScope.personUnreadMessages = vm.notifications.readMessages;
                        /*if (vm.notifications.readMessages > 0) {
                         $rootScope.showUpdates();
                         }*/
                    }
                )
            }


            function reloadNotitfications() {
                DispatchService.getAllNotifications(vm.notificationFilter).then(
                    function (data) {
                        vm.notifications = data;
                        vm.loading = false;
                        loadNotifications();
                    }
                )
            }

            function showNewInward() {
                var options = {
                    title: "New Inward",
                    template: 'app/desktop/modules/inward/new/newInwardView.jsp',
                    controller: 'NewInwardController as newInwardVm',
                    resolve: 'app/desktop/modules/inward/new/newInwardController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.inwards.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            reloadNotitfications();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function showNewRequest() {
                $state.go('app.requests.new');
            }

            function showNewGatePass() {
                var options = {
                    title: "New Gate Pass",
                    template: 'app/desktop/modules/inward/new/newGatePassView.jsp',
                    controller: 'NewGatePassController as newGatePassVm',
                    resolve: 'app/desktop/modules/inward/new/newGatePassController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.inwards.gatePass.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            reloadNotitfications();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function showNewIssue() {
                $state.go('app.issues.new', {issueMode: "ISSUE"});
            }

            vm.downloadGatePass = downloadGatePass;
            function downloadGatePass(gatePass) {
                var url = "{0}//{1}/api/drdo/inwards/gatePass/{2}/{3}/preview".
                    format(window.location.protocol, window.location.host,
                    gatePass.id, gatePass.gatePass.id);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = gatePass.gatePass.name;
                });
                //window.open(url);
                $timeout(function () {
                    window.close();
                }, 2000);
            }

            vm.deleteGatePass = deleteGatePass;
            function deleteGatePass(gatePass) {
                var options = {
                    title: "Delete GatePass",
                    message: "Please confirm to delete this GatePass",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        InwardService.deleteGatePass(gatePass).then(
                            function (data) {
                                loadGatePasses();
                                $rootScope.showSuccessMessage("GatePass deleted successfully");
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                })
            }

            vm.showNewDispatch = showNewDispatch;
            function showNewDispatch() {
                var options = {
                    title: "New Dispatch",
                    template: 'app/desktop/modules/dispatch/new/newDispatchView.jsp',
                    controller: 'NewDispatchController as newDispatchVm',
                    resolve: 'app/desktop/modules/dispatch/new/newDispatchController',
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.dispatch.new'}
                    ],
                    callback: function (result) {
                        loadDispatches();
                    }
                };

                $rootScope.showSidePanel(options);

            }

            vm.showGatePassItems = showGatePassItems;

            function showGatePassItems(gatePass) {
                var options = {
                    title: gatePass.gatePassNumber + " details",
                    template: 'app/desktop/modules/gatePass/all/gatePassDetailsView.jsp',
                    controller: 'GatePassDetailsController as gatePassDetailsVm',
                    resolve: 'app/desktop/modules/gatePass/all/gatePassDetailsController',
                    width: 700,
                    data: {
                        gatePassId: gatePass.id
                    },
                    buttons: [
                        {text: "Close", broadcast: 'app.gatePass.details'}
                    ],
                    callback: function (result) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.deleteRequest = deleteRequest;

            function deleteRequest(request) {
                var options = {
                    title: "Delete Request",
                    message: "Please confirm to delete this request",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            RequestService.deleteRequest(request.id).then(
                                function (data) {
                                    loadRequests();
                                    $rootScope.showSuccessMessage("Request deleted successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                )
            }

            $scope.$on('$viewContentLoaded', function () {
                if ($rootScope.personPermissions.length == 1 && $rootScope.personPermissions[0].id == 'permission.planning.view') {
                    $state.go('app.planning.all');
                    $rootScope.hideToggleNode = true;
                } else {
                    $timeout(function () {
                        loadAllNotifications();
                    }, 500);
                }
            });
        }
    }
)
;
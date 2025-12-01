/**
 * Created by swapna on 12/08/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/loanService'
    ],
    function (module) {
        module.controller('LoanReceivedItemsController', LoanReceivedItemsController);

        function LoanReceivedItemsController($scope, $rootScope, $timeout, $window, $state, $cookies, $stateParams, LoanService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-shopping-cart";

            vm.back = back;
            vm.storeId = $rootScope.storeId;
            $rootScope.loanReceiveItemsList = [];
            vm.store = null;

            var pageable = {
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
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            $rootScope.loanReceiveItemsList = pagedResults;

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'LOANRECEIVE'});
            }

            vm.itemReurnedHistoryFilePopover = {
                templateUrl: 'app/desktop/modules/stores/details/tabs/loanReceived/details/tabs/items/loanReturnItemHistoryView.jsp'
            };

            function loadLoanIssueItems() {
                LoanService.getLoanReturnItemsDetails($rootScope.storeId, $stateParams.loanId, pageable).then(
                    function (items) {
                        $rootScope.loanReceiveItemsList = items;
                        angular.forEach($rootScope.loanReceiveItemsList.content, function (item) {
                            item.hasHistory = false;
                            item.itemHistory = [];
                            LoanService.getLoanReturnItemHistory($rootScope.storeId, $stateParams.loanId, item.id).then(
                                function (data) {
                                    if (data.length > 0) {
                                        item.hasHistory = true;
                                        item.itemHistory = data;
                                    }
                                }
                            )
                        })
                    }
                )
            }

            vm.showItemDetails = showItemDetails;
            function showItemDetails(item) {
                item.loanReceivedOn = new Date();
                item.quantity = item.itemReturnQuantity;
                var options = {
                    title: 'Loan Received Item Details',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/itemBasicInfoView.jsp',
                    controller: 'ItemBasicInfoController as itemBasicInfoVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/itemBasicInfoController',
                    width: 600,
                    data: {
                        item: item
                    },
                    buttons: [
                        {text: 'Update', broadcast: 'app.stock.loanReceivedItems.info'}
                    ],
                    callback: function () {
                        //loadProperties();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function resize() {
                var height = $(window).height();
                var projectHeaderHeight = $("#project-headerbar").outerHeight();
                if (projectHeaderHeight != null) {
                    if ($application.selectedProject != undefined && $application.selectedProject.locked == true) {
                        $('#contentpanel').height(height - 297);
                    } else {
                        $('#contentpanel').height(height - 267);
                    }
                } else if (projectHeaderHeight == null) {
                    $('#contentpanel').height(height - 217);
                }
            }

            function nextPage() {
                if ($rootScope.loanReceiveItemsList.last != true) {
                    pageable.page++;
                    loadLoanIssueItems();
                }
            }

            function previousPage() {
                if ($rootScope.loanReceiveItemsList.first != true) {
                    pageable.page--;
                    loadLoanIssueItems();
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.stock.loanReceivedItems', function (event, data) {
                        loadLoanIssueItems();
                        resize();
                    });
                }
                $scope.$on('app.loanReceived.items.nextPageDetails', nextPage);
                $scope.$on('app.loanReceived.items.previousPageDetails', previousPage);
            })();
        }
    }
)
;
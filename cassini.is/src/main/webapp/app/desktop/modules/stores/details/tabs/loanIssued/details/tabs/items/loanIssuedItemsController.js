/**
 * Created by swapna on 12/08/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/core/storeService',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/bomService',
        'app/shared/services/store/loanService',
        'app/shared/services/pm/project/projectService'

    ],
    function (module) {
        module.controller('LoanItemsController', LoanItemsController);

        function LoanItemsController($scope, $rootScope, $timeout, $window, $state, $cookies, $stateParams, ProjectService,
                                     BomService, TaskService, StoreService, LoanService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-shopping-cart";

            vm.back = back;
            vm.storeId = $rootScope.storeId;
            $rootScope.loanIssuedItemsList = [];
            vm.store = null;

            var pageable = {
                page: 0,
                size: 20
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

            $rootScope.loanIssuedItemsList = angular.copy(pagedResults);

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'LOANISSUE'});
            }

            function loadLoanIssueItems() {
                LoanService.getLoanItems($rootScope.storeId, $stateParams.loanId, pageable).then(
                    function (stockMovements) {
                        $rootScope.loanIssuedItemsList = stockMovements;
                    }
                )
            }

            vm.showItemDetails = showItemDetails;
            function showItemDetails(item) {
                item.itemDTO.loanIssuedOn = new Date();
                item.itemDTO.quantity = item.quantity;
                var options = {
                    title: 'Loan Issued Item Details',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/itemBasicInfoView.jsp',
                    controller: 'ItemBasicInfoController as itemBasicInfoVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/itemBasicInfoController',
                    width: 600,
                    data: {
                        item: item.itemDTO
                    },

                    callback: function () {
                        //loadProperties();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if ($rootScope.loanIssuedItemsList.last != true) {
                    pageable.page++;
                    loadLoanIssueItems();
                }
            }

            function previousPage() {
                if ($rootScope.loanIssuedItemsList.first != true) {
                    pageable.page--;
                    loadLoanIssueItems();
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.stock.loanItems', function (event, data) {
                        loadLoanIssueItems();
                    });
                }
                $scope.$on('app.loanIssued.items.nextPageDetails', nextPage);
                $scope.$on('app.loanIssued.items.previousPageDetails', previousPage);
            })();
        }
    }
)
;
define(
    [
        'app/desktop/modules/npr/npr.module',
        'app/shared/services/core/nprService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('AllNprsController', AllNprsController);

        function AllNprsController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                   $state, $stateParams, $cookies, $uibModal, NprService, CommonService, DialogService) {

            $rootScope.viewInfo.icon = "fa fa-th";
            $rootScope.viewInfo.title = $translate.instant('ITEMS_ALL_TITLE');
            $rootScope.viewInfo.showDetails = false;

            var parsed = angular.element("<div></div>");
            var newPartRequestTitle = parsed.html($translate.instant("NEW_PART_REQUEST")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();
            var deleteDialogTitle = parsed.html($translate.instant("DELETE_NPR")).html();
            var deleteCustomerDialogMessage = parsed.html($translate.instant("DELETE_NPR_DIALOG_MESSAGE")).html();
            var nprDeletedMessage = parsed.html($translate.instant("NPR_DELETED_MESSAGE")).html();
            var cannotDeletedPendingNpr = parsed.html($translate.instant("CANNOT_DELETE_PENDING_NPR")).html();
            var cannotDeletedApprovedNpr = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_NPR")).html();
            var cannotDeletedRejectedNpr = parsed.html($translate.instant("CANNOT_DELETE_REJECTED_NPR")).html();
            $scope.newPartRequest = parsed.html($translate.instant("NEW_PART_REQUEST")).html();

            var vm = this;

            vm.newPartRequest = newPartRequest;
            vm.showNpr = showNpr;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.searchText = null;
            vm.filterSearch = null;

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
            vm.filters = {
                searchQuery: null
            };

            vm.nprs = angular.copy(pagedResults);

            function newPartRequest() {
                var options = {
                    title: newPartRequestTitle,
                    template: 'app/desktop/modules/npr/new/newNprView.jsp',
                    controller: 'NewNprController as newNprVm',
                    resolve: 'app/desktop/modules/npr/new/newNprController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.newNpr.new'}
                    ],
                    callback: function (npr) {
                        showNpr(npr);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadAllNewParts() {
                vm.loading = true;
                NprService.getAllNprs(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.nprs = data;
                        angular.forEach(vm.nprs.content, function (npr) {
                            if (npr.status == "PENDING") {
                                npr.titleMsg = cannotDeletedPendingNpr;
                            } else if (npr.status == "APPROVED") {
                                npr.titleMsg = cannotDeletedApprovedNpr;
                            } else if (npr.status == "REJECTED") {
                                npr.titleMsg = cannotDeletedRejectedNpr;
                            }
                        })
                        CommonService.getPersonReferences(vm.nprs.content, 'requester');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                          $rootScope.showErrorMessage(error.message);
                          $rootScope.hideBusyIndicator();
                     }
                )
            }

            vm.deleteNpr = deleteNpr;
            function deleteNpr(npr) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteCustomerDialogMessage + " [" + npr.number + "] ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        NprService.deleteNpr(npr.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(nprDeletedMessage);
                                loadAllNewParts();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }


            function nextPage() {
                if (vm.nprs.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadAllNewParts();
                }
            }

            function previousPage() {
                if (vm.nprs.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadAllNewParts();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadAllNewParts();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadAllNewParts();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.nprs = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadAllNewParts();
            }

            function showNpr(npr) {
                $state.go('app.nprs.details', {nprId: npr.id, tab: 'details.basic'});
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    loadAllNewParts();
                });
            })();
        }
    }
);
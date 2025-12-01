define(['app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('ItemRevisionSelectionController', ItemRevisionSelectionController);

        function ItemRevisionSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore,
                                                 $uibModal, ItemService) {

            var vm = this;

            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.selectRadio = selectRadio;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            var objectId = $scope.data.existObjectId;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate"
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

            vm.selectedObj = null;
            vm.itemRevisons = angular.copy(pagedResults);

            function nextPage() {
                vm.pageable.page++;
                loadItemRevisions();
            }

            function previousPage() {
                vm.pageable.page--;
                loadItemRevisions();
            }

            vm.filters = {
                searchQuery: null,
                itemClass: '',
                itemId: ''
            }

            vm.searchTerm = null;
            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    vm.loading = true;
                    vm.filters.searchQuery = vm.searchTerm;
                    if (objectId != null && objectId != "" && objectId != undefined) {
                        vm.filters.itemId = objectId;
                    }
                    ItemService.searchItemRevisions(vm.pageable, vm.filters).then(
                        function (data) {
                            vm.itemRevisons = data;
                            ItemService.getItemReferences(vm.itemRevisons.content, 'itemMaster');
                            var existObjectId = false;
                            angular.forEach(vm.itemRevisons.content, function (itemRev) {
                                if (objectId != null && objectId != "" && objectId != undefined && objectId == itemRev.id) {
                                    vm.itemRevisons.content.splice(vm.itemRevisons.content.indexOf(itemRev), 1);
                                    existObjectId = true;
                                }
                                itemRev.checked = false;
                            })
                            if (existObjectId) {
                                vm.itemRevisons.totalElements = vm.itemRevisons.totalElements - 1;
                                vm.itemRevisons.numberOfElements = vm.itemRevisons.numberOfElements - 1;
                            }
                            vm.loading = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    resetPage();
                }
            }

            function clearFilter() {
                loadItemRevisions();
                vm.clear = false;
            }

            function resetPage() {
                vm.searchTerm = null;
                vm.filters.searchQuery = null;
                vm.pageable.page = 0;
                loadItemRevisions();
            }

            function loadItemRevisions() {
                vm.clear = false;
                vm.loading = true;
                ItemService.getAllLatestItemRevisions(vm.pageable).then(
                    function (data) {
                        vm.itemRevisons = data;
                        var existObjectId = false;
                        ItemService.getItemReferences(vm.itemRevisons.content, 'itemMaster');
                        angular.forEach(vm.itemRevisons.content, function (itemRev) {
                            if (objectId != null && objectId != "" && objectId != undefined && objectId == itemRev.id) {
                                vm.itemRevisons.content.splice(vm.itemRevisons.content.indexOf(itemRev), 1);
                                existObjectId = true;
                            }
                            itemRev.checked = false;
                        })
                        if (existObjectId) {
                            vm.itemRevisons.totalElements = vm.itemRevisons.totalElements - 1;
                            vm.itemRevisons.numberOfElements = vm.itemRevisons.numberOfElements - 1;
                        }
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function selectRadioChange(item, $event) {
                radioChange(item, $event);
                selectRadio();
            }

            function radioChange(item, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === item) {
                    item.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = item;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);

                }

                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage("Please select atleast one item revision");
                }
            }

            module.directive('autoFocus', ['$timeout', function ($timeout) {
                return {
                    restrict: 'A',
                    link: function ($scope, $element) {
                        $timeout(function () {
                            $element[0].focus();
                        });
                    }
                }
            }]);
            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.$on('app.select.itemRevision', selectRadio);
                loadItemRevisions();
                //}
            })();
        }
    }
)
;


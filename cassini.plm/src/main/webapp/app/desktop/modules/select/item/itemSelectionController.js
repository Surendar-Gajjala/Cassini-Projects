define(['app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('ItemSelectionController', ItemSelectionController);

        function ItemSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore, $translate,
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
            $scope.freeTextQuery = null;
            var objectId = $scope.data.existObjectId;
            vm.selectAttributeDef = $scope.data.selectAttDef;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
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
            vm.items = angular.copy(pagedResults);
            var parsed = angular.element("<div></div>");
            var pleaseSelectAtleastOneItem = parsed.html($translate.instant("PLEASE_SELECT_ATLEAST_ONE_ITEM")).html();
            $scope.clearTitleSearch = parsed.html($translate.instant("CLEAR_SEARCH")).html();

            function nextPage() {
                vm.pageable.page++;
                loadItems();
            }

            function previousPage() {
                vm.pageable.page--;
                loadItems();
            }

            vm.filters = {
                searchQuery: null,
                type: ''
            };
            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    $scope.freeTextQuery = vm.searchTerm;
                    if (vm.selectAttributeDef.refSubType == null) {
                        promise = ItemService.freeTextSearch(vm.pageable, vm.searchTerm, '')
                    } else if (vm.selectAttributeDef.refSubType != null) {
                        vm.filters.type = vm.selectAttributeDef.refSubType;
                        vm.filters.searchQuery = vm.searchTerm;
                        promise = ItemService.getItemsByTypeId(vm.pageable, vm.filters)
                    }
                    if (promise != null) {
                        promise.then(
                            function (data) {
                                loadSelectedItems(data);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                } else {
                    resetPage();
                    loadItems();
                }
            }

            vm.searchTerm = null;
            function clearFilter() {
                loadItems();
                vm.clear = false;
            }

            function resetPage() {
                vm.items = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.searchTerm = null;
                $scope.freeTextQuery = null;
                loadItems();
            }

            function loadItems() {
                vm.clear = false;
                vm.loading = true;
                if (vm.selectAttributeDef.refSubType == null) {
                    promise = ItemService.getItems(vm.pageable)
                } else if (vm.selectAttributeDef.refSubType != null) {
                    vm.filters.type = vm.selectAttributeDef.refSubType;
                    vm.filters.searchQuery = null;
                    promise = ItemService.getItemsByTypeId(vm.pageable, vm.filters)
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            loadSelectedItems(data);
                            vm.loading = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadSelectedItems(data) {
                vm.items = data;
                var existObjectId = false;
                angular.forEach(vm.items.content, function (item) {
                    if (objectId != null && objectId != "" && objectId != undefined && objectId == item.id) {
                        vm.items.content.splice(vm.items.content.indexOf(item), 1);
                        existObjectId = true;
                    }
                    item.checked = false;
                });
                if (existObjectId) {
                    vm.items.totalElements = vm.items.totalElements - 1;
                    vm.items.numberOfElements = vm.items.numberOfElements - 1;
                }
            }

            function selectRadioChange(item) {
                radioChange(item);
                selectRadio();
            }

            function radioChange(item) {
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
                    $rootScope.showWarningMessage(pleaseSelectAtleastOneItem);
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
                $rootScope.$on('app.attributes.items.selector', selectRadio);
                loadItems();
                //}
            })();
        }
    }
)
;


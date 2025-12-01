/**
 * Created by SRAVAN on 8/20/2019.
 */
define(
    [
        'app/desktop/modules/rm/requirements/requirement.module',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('RequirementsItemDeliverablesController', RequirementsItemDeliverablesController);

        function RequirementsItemDeliverablesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                                        CommonService, DialogService, ItemService, SpecificationsService) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.loading = true;
            vm.loading = true;
            vm.selectedItems = [];
            var reqId = $stateParams.requirementId;
            var AddItemDelivarables = parsed.html($translate.instant("DELIVERABLE_ADD_SUCCESS")).html();
            var selectItem = parsed.html($translate.instant("DELIVERABLE_VALIDATION")).html();

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
            vm.items = angular.copy(pagedResults);

            vm.filters = {
                name: null,
                objectNumber: null,
                itemType: '',
                itemNumber: null,
                description: null
            };

            vm.onSelectType = onSelectType;
            vm.clearFilter = clearFilter;
            vm.searchItems = searchItems;
            vm.checkAll = checkAll;
            vm.select = select;

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedType = itemType;
                    vm.itemType = itemType;
                    vm.filters.itemType = itemType.id;
                    searchItems();
                    vm.clear = true;
                }
            }

            function searchItems() {
                vm.clear = true;
                loadReqirementDeliverables();
            }

            function clearFilter() {
                vm.filters = {
                    name: null,
                    itemType: '',
                    itemNumber: null,
                    itemName: null,
                    latestRevision: null,
                    status: null,
                    objectNumber: null
                };
                vm.selectedType = null;
                loadReqirementDeliverables();
                vm.clear = false;
            }

            function loadReqirementDeliverables() {
                vm.loading = true;
                SpecificationsService.getRequirementItemDeliverables(reqId, pageable, vm.filters).then(
                    function (data) {
                        vm.reqDeliverables = data;
                        vm.loading = false;
                        vm.selectedAll = false;
                        ItemService.getLatestRevisionReferences(vm.reqDeliverables.content, 'latestRevision');
                        vm.showItemDeliverable = true;
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function checkAll() {
                if (vm.selectedAll) {
                    vm.selectedItems = [];
                    vm.selectedAll = true;
                    angular.forEach(vm.reqDeliverables.content, function (item) {
                        item.selected = vm.selectedAll;
                        vm.selectedItems.push(item);
                    });
                } else {
                    vm.selectedAll = false;
                    angular.forEach(vm.reqDeliverables.content, function (item) {
                        item.selected = vm.selectedAll;
                        vm.selectedItems = [];
                    });
                }
            }

            vm.selectedItems = [];
            function select(item) {
                var flag = true;
                if (item.selected == false) {
                    vm.selectedAll = false;
                    var index = vm.selectedItems.indexOf(item);
                    vm.selectedItems.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedItems, function (selectedItem) {
                        if (selectedItem.id == item.id) {
                            flag = false;
                            var index = vm.selectedItems.indexOf(item);
                            vm.selectedItems.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedItems.push(item);
                    }

                    if (vm.selectedItems.length == vm.reqDeliverables.content.length) {
                        vm.selectedAll = true;
                    }
                }
            }

            function create() {
                if (vm.selectedItems.length == 0) {
                    $rootScope.showWarningMessage(selectItem);
                }
                else {
                    SpecificationsService.createRequirementDeliverables(reqId, vm.selectedItems).then(
                        function (data) {
                            $scope.callback(data);
                            $rootScope.hideSidePanel();
                            vm.selectedItems = [];
                            vm.creating = false;
                            $rootScope.showSuccessMessage(AddItemDelivarables);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )

                }

            }

            (function () {
                //if ($application.homeLoaded == true) {
                    $rootScope.$on('app.requirement.items.new', create);
                    loadReqirementDeliverables();
                //}
            })();
        }

    }
)
;

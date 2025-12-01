define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('ItemsController', ItemsController);

        function ItemsController($scope, $rootScope, $sce, $translate, $cookieStore, $window, $timeout, $application, $state, $stateParams, $cookies, $uibModal,
                                 CommonService, ItemTypeService, ItemService, DialogService, ObjectTypeAttributeService,
                                 AttributeAttachmentService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-th";
            $rootScope.viewInfo.title = "Items";

            var vm = this;

            vm.loading = true;
            vm.clear = false;
            $rootScope.searchModeType = false;

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

            vm.items = angular.copy(pagedResults);

            vm.showNewItem = showNewItem;
            vm.deleteItem = deleteItem;
            vm.showItem = showItem;

            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;
            vm.flag = false;

            function resetPage() {
                vm.pageable.page = 0;
            }

            function nextPage() {
                if (vm.items.last != true) {
                    vm.pageable.page++;
                    if ($scope.freeTextQuery != null && $scope.freeTextQuery != "" && $scope.freeTextQuery != undefined) {
                        ItemService.freeTextSearch($scope.freeTextQuery, vm.pageable).then(
                            function (data) {
                                vm.items = data;
                                vm.loading = false;
                                ItemService.getRevisionReferences(vm.items.content, 'latestRevision');
                                CommonService.getPersonReferences(vm.items.content, 'createdBy');
                                CommonService.getPersonReferences(vm.items.content, 'modifiedBy');
                            }
                        )
                    } else {
                        ItemService.getAllItems(vm.pageable).then(
                            function (data) {
                                vm.items = data;
                                vm.loading = false;
                                CommonService.getPersonReferences(vm.items.content, 'createdBy');
                                CommonService.getPersonReferences(vm.items.content, 'modifiedBy');
                                ItemService.getRevisionReferences(vm.items.content, 'latestRevision');

                            }
                        )
                    }
                }
            }

            function previousPage() {
                if (vm.items.first != true) {
                    vm.pageable.page--;
                    if ($scope.freeTextQuery != null && $scope.freeTextQuery != "" && $scope.freeTextQuery != undefined) {
                        ItemService.freeTextSearch($scope.freeTextQuery, vm.pageable).then(
                            function (data) {
                                vm.items = data;
                                vm.loading = false;
                                ItemService.getRevisionReferences(vm.items.content, 'latestRevision');
                                CommonService.getPersonReferences(vm.items.content, 'createdBy');
                                CommonService.getPersonReferences(vm.items.content, 'modifiedBy');
                            }
                        )
                    } else {
                        ItemService.getAllItems(vm.pageable).then(
                            function (data) {
                                vm.items = data;
                                vm.loading = false;
                                CommonService.getPersonReferences(vm.items.content, 'createdBy');
                                CommonService.getPersonReferences(vm.items.content, 'modifiedBy');
                                ItemService.getRevisionReferences(vm.items.content, 'latestRevision');

                            }
                        )
                    }
                }
            }

            function clearFilter() {
                loadItems();
                vm.clear = false;
                $rootScope.showSearch = false;
            }

            function showNewItem() {
                var options = {
                    title: "New Item",
                    template: 'app/desktop/modules/item/new/newItemView.jsp',
                    controller: 'NewItemController as newItemVm',
                    resolve: 'app/desktop/modules/item/new/newItemController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.items.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadItems();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function deleteItem(item) {
                var options = {
                    title: "Delete Item",
                    message: "Please confirm to delete this item",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ItemService.deleteItem(item.id).then(
                            function (data) {
                                var index = vm.items.content.indexOf(item);
                                vm.items.content.splice(index, 1);
                                $rootScope.showSuccessMessage("Item deleted successfully");
                            }
                        )
                    }
                });
            }

            function showItem(item) {
                $state.go('app.items.details', {itemId: item.latestRevision});
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                if (freeText != null && freeText != "" && freeText != undefined) {
                    $scope.freeTextQuery = freeText;
                    ItemService.freeTextSearch(freeText, vm.pageable).then(
                        function (data) {
                            vm.items = data;
                            vm.loading = false;
                            ItemService.getRevisionReferences(vm.items.content, 'latestRevision');
                            CommonService.getPersonReferences(vm.items.content, 'createdBy');
                            CommonService.getPersonReferences(vm.items.content, 'modifiedBy');
                        }
                    )
                } else {
                    resetPage();
                    loadItems();
                    $scope.freeTextQuery = null;
                }
            }


            function loadItems() {

                vm.loading = true;

                ItemService.getAllItems(vm.pageable).then(
                    function (data) {
                        vm.items = data;
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.items.content, 'createdBy');
                        CommonService.getPersonReferences(vm.items.content, 'modifiedBy');
                        ItemService.getRevisionReferences(vm.items.content, 'latestRevision');

                    }
                )
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    loadItems();
                });
            })();
        }
    }
);
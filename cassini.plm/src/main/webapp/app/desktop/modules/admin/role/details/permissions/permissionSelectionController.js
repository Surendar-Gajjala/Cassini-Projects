define([
        'app/desktop/modules/item/item.module',
        'app/desktop/modules/securityPermission/securityPermissionService'
    ],
    function (module) {
        module.controller('PermissionSelectionController', PermissionSelectionController);

        function PermissionSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, SecurityPermissionService) {

            var vm = this;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "id",
                    order: "ASC"
                }
            };
            vm.loading = true;
            vm.error = "";

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
                id: null,
                name: null,
                description: null,
                objectType: null,
                subType: null,
                privilege: null,
                privilegeType: 'GRANTED',
                module: 'ALL',
                attribute: null,
                attributeGroup: null
            };

            vm.items = angular.copy(pagedResults);
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.onOk = onOk;

            vm.loadFilteredPermission = loadFilteredPermission;

            var parsed = angular.element("<div></div>");
            var selectMessage = parsed.html($translate.instant("ATLEAST_ONE_OBJECTTYPE_VALIDATION")).html();
            vm.selectTitle = parsed.html($translate.instant("SELECT")).html();
            $scope.itemHasPendingChangeOrder = parsed.html($translate.instant("ITEM_HAS_PENDING_C_O")).html();

            vm.selectedItems = [];
            vm.similarItems = [];
            $scope.check = false;

            function selectAll(check) {
                vm.selectedItems = [];
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.items.content, function (item) {
                        item.selected = false;
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.items.content, function (item) {
                        item.selected = true;
                        vm.selectedItems.push(item);
                    })
                }
            }

            function nextPage() {
                if (vm.items.last != true) {
                    vm.pageable.page++;
                    $scope.check = false;
                    vm.selectedItems = [];
                    loadFilteredPermission();
                }
            }

            function previousPage() {
                if (vm.items.first != true) {
                    vm.pageable.page--;
                    $scope.check = false;
                    vm.selectedItems = [];
                    loadFilteredPermission();
                }
            }

            function clearFilter() {
                vm.filters.name = null;
                vm.filters.objectType = null;
                vm.filters.subType = null;
                vm.filters.privilege = null;
                vm.filters.attribute = null;
                vm.filters.privilegeType = 'GRANTED';
                vm.filters.module = 'ALL';
                vm.selectedType = null;
                $scope.check = false;
                vm.selectedItems = [];
                vm.selectAllCheck = false;
                vm.clear = false;
                loadFilteredPermission();
            }

            function onOk() {
                if (vm.selectedItems.length != 0) {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedItems);
                }

                if (vm.selectedItems.length == 0) {
                    $rootScope.showWarningMessage(selectMessage);
                }

            }

            vm.selectAllCheck = false;
            function selectCheck(item) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedItems, function (selectedItem) {
                    if (selectedItem.id == item.id) {
                        flag = false;
                        var index = vm.selectedItems.indexOf(item);
                        vm.selectedItems.splice(index, 1);
                    }
                });
                if (flag) {
                    // select all conditions when all is selected
                    /*if (item.privilege == 'all') {
                        angular.forEach(vm.items.content, function (val) {
                            if (val.objectType == item.objectType && val.subType == item.subType) {
                                val.selected = true;
                                var index = vm.selectedItems.indexOf(val);
                                if (index == -1) vm.selectedItems.push(val);
                            }
                        });
                    }*/
                    // selects view if create, edit, delete is selected.
                    if (item.privilege != 'all' && item.privilege != 'view') {
                        var view = false;
                        var index = vm.selectedItems.indexOf(item);
                        if (index == -1) vm.selectedItems.push(item);
                        angular.forEach(vm.items.content, function (val) {
                            if (val.objectType == item.objectType && val.subType == item.subType && val.privilege == 'view') {
                                val.selected = true;
                                view = true;
                                var index = vm.selectedItems.indexOf(val);
                                if (index == -1) vm.selectedItems.push(val);
                            }
                        });
                        //if (!view) vm.selectedItems.push(item);
                        //selects view when edit is selected and vice versa
                        if (item.objectType.endsWith('type')) {
                            angular.forEach(vm.items.content, function (val) {
                                if (val.objectType == item.objectType && val.subType == item.subType && (val.privilege == 'view' || val.privilege == 'edit')) {
                                    val.selected = true;
                                    view = true;
                                    var index = vm.selectedItems.indexOf(val);
                                    if (index == -1) vm.selectedItems.push(val);
                                }
                            });
                        }
                    } else {
                        var index = vm.selectedItems.indexOf(item);
                        if (index == -1) vm.selectedItems.push(item);
                    }
                } else {
                   /* if (item.privilege == 'all') {
                        angular.forEach(vm.items.content, function (val) {
                            if (val.objectType == item.objectType && val.subType == item.subType) {
                                val.selected = false;
                                var index = vm.selectedItems.indexOf(val);
                                if (index != -1) vm.selectedItems.splice(index, 1);
                            }
                        });
                    } else {
                        angular.forEach(vm.items.content, function (val) {
                            if (val.objectType == item.objectType && val.subType == item.subType && val.privilege == 'all') {
                                if (val.selected) val.selected = false;
                                var index = vm.selectedItems.indexOf(val);
                                if (index != -1) vm.selectedItems.splice(index, 1);
                            }
                        });
                    }*/
                }

                if (vm.selectedItems.length != vm.items.content.length) {
                    vm.selectAllCheck = false;
                } else {
                    vm.selectAllCheck = true;
                }
            }

            function loadFilteredPermission() {
                vm.loading = true;
                vm.filters.id = $scope.data.groupId;
                SecurityPermissionService.getFilteredSecurityPermissions(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.selectedItems = [];
                        vm.items = data;
                        vm.selectAllCheck = false;
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showWarningMessage(error.message);
                    }
                )
            }

            vm.searchFilterItem = searchFilterItem;
            function searchFilterItem() {
                vm.pageable.page = 0;
                loadFilteredPermission();
                vm.clear = true;
                vm.selectAllCheck = false;
            }

            (function () {
                loadFilteredPermission();
                $rootScope.$on("add.select.permissions", onOk);
            })();
        }
    }
)
;
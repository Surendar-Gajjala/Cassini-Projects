define(
    [
        'app/desktop/modules/customObject/customObject.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService'
    ],
    function (module) {
        module.controller('CustomObjectWhereUsedController', CustomObjectWhereUsedController);

        function CustomObjectWhereUsedController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $cookies, $window,
                                                 CustomObjectService) {
            var vm = this;

            vm.loading = true;
            vm.customId = $stateParams.customId;
            vm.bomItems = [];
            vm.showAllRevisions = false;
            vm.whereUsedItem = whereUsedItem;
            var parsed = angular.element("<div></div>");
            $scope.expandAllTitle = parsed.html($translate.instant("EXPAND_ALL")).html();
            $scope.collapseAllTitle = parsed.html($translate.instant("COLLAPSE_ALL")).html();
            vm.ExpandCollapse = parsed.html($translate.instant("EXPAND_COLLAPSE")).html();

            vm.expandedAll = false;
            vm.showExpandAll = false;
            function loadWhereUsed() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                var hierarchy = true;
                CustomObjectService.getCustomObjectWhereUsed(vm.customId, hierarchy).then(
                    function (data) {
                        vm.whereUsedData = angular.copy(data);
                        vm.bomItems = [];
                        angular.forEach(data, function (bomItem) {
                            bomItem.expanded = false;
                            bomItem.level = 0;
                            bomItem.bomChildren = [];
                            if (bomItem.children.length > 0) {
                                vm.showExpandAll = true;
                            }
                            vm.bomItems.push(bomItem);
                        });
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.expandAllWhereUsed = expandAllWhereUsed;
            function expandAllWhereUsed() {
                vm.expandedAll = !vm.expandedAll;
                var promise = null;
                if (vm.expandedAll) {
                    vm.bomItems = [];

                    angular.forEach(vm.whereUsedData, function (bomItem) {
                        bomItem.level = 0;
                        bomItem.bomChildren = [];
                        bomItem.expanded = true;
                        vm.bomItems.push(bomItem);
                        var index = vm.bomItems.indexOf(bomItem);
                        index = populateChildren(bomItem, index);
                    });
                    vm.loading = false;
                    $rootScope.hideBusyIndicator();
                } else {
                    vm.bomItems = [];
                    angular.forEach(vm.whereUsedData, function (bomItem) {
                        bomItem.level = 0;
                        bomItem.bomChildren = [];
                        bomItem.expanded = false;
                        vm.bomItems.push(bomItem);
                        if (bomItem.children.length > 0) {
                            vm.showExpandAll = true;
                        }
                    });
                    vm.loading = false;
                    $rootScope.hideBusyIndicator();
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            vm.whereUsedData = data;

                        }
                    )
                }
            }

            function populateChildren(bomItem, lastIndex) {
                angular.forEach(bomItem.children, function (item) {
                    lastIndex++;
                    item.level = bomItem.level + 1;
                    item.expanded = true;
                    item.bomChildren = [];
                    vm.bomItems.splice(lastIndex, 0, item);
                    bomItem.bomChildren.push(item);
                    lastIndex = populateChildren(item, lastIndex)
                });

                return lastIndex;
            }

            vm.toggleNode = toggleNode;

            function toggleNode(item) {
                $rootScope.showBusyIndicator();
                item.expanded = !item.expanded;
                var index = vm.bomItems.indexOf(item);
                if (item.expanded) {
                    angular.forEach(item.children, function (bomItem) {
                        index = index + 1;
                        bomItem.level = item.level + 1;
                        bomItem.expanded = false;
                        bomItem.bomChildren = [];
                        item.bomChildren.push(bomItem);
                        vm.bomItems.splice(index, 0, bomItem);
                    });
                    var count = 0;
                    var levelCount = 0;
                    angular.forEach(vm.bomItems, function (bomItem) {
                        if (bomItem.level == 0 && bomItem.children.length > 0) {
                            levelCount++;
                            if (bomItem.expanded) {
                                count++;
                            }
                        }
                        if (levelCount == count) {
                            vm.expandedAll = true;
                        } else {
                            vm.expandedAll = false;
                        }
                    });
                    vm.loading = false;
                    $rootScope.hideBusyIndicator();
                } else {
                    removeChildren(item);
                    angular.forEach(vm.bomItems, function (bomItem) {
                        if (bomItem.level == 0 && bomItem.children.length > 0 && !bomItem.expanded) {
                            vm.expandedAll = false;
                        }
                    })
                }
                $rootScope.hideBusyIndicator();
            }

            function removeChildren(bomItem) {
                if (bomItem != null && bomItem.bomChildren != null && bomItem.bomChildren != undefined) {
                    angular.forEach(bomItem.bomChildren, function (item) {
                        removeChildren(item);
                    });

                    var index = vm.bomItems.indexOf(bomItem);
                    vm.bomItems.splice(index + 1, bomItem.bomChildren.length);
                    bomItem.bomChildren = [];
                    bomItem.expanded = false;

                }
            }

            function whereUsedItem(item) {
                $state.go('app.customobjects.details', {customId: item.parent.id, tab: 'details.basic'});
            }

            (function () {
                $scope.$on('app.customObj.tabActivated', function (event, data) {
                    if (data.tabId == 'details.whereUsed') {
                        loadWhereUsed();
                    }

                    /*$(window).resize(resizeTable);

                     $scope.$on('$destroy', function () {
                     $(window).off('resize', resizeTable);
                     });*/
                });
            })();
        }
    }
);
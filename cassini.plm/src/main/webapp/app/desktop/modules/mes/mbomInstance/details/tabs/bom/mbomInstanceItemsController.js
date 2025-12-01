define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/mbomInstanceService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService'
    ],
    function (module) {
        module.controller('MBOMInstanceItemsController', MBOMInstanceItemsController);

        function MBOMInstanceItemsController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application, MBOMInstanceService) {
            var vm = this;
            vm.loading = true;
            vm.mbomInstanceId = $stateParams.mbomInstanceId;
            var parsed = angular.element("<div></div>");

            function loadMBOMInstanceItems() {
                MBOMInstanceService.getMBOMInstanceItems(vm.mbomInstanceId, true).then(
                    function (data) {
                        vm.mBomItems = [];
                        angular.forEach(data, function (item) {
                            item.parentBom = null;
                            item.editMode = false;
                            item.expanded = false;
                            item.level = 0;
                            item.count = 0;
                            item.bomChildren = [];
                            item.isNew = false;
                            vm.mBomItems.push(item);
                            var index = vm.mBomItems.indexOf(item);
                            index = populateMBOMItemChildren(item, index);
                        });
                        vm.loading = false;
                    }
                )
            }

            function populateMBOMItemChildren(mBomItem, lastIndex) {
                angular.forEach(mBomItem.children, function (item) {
                    lastIndex++;
                    item.parentBom = mBomItem;
                    item.expanded = true;
                    item.editMode = false;
                    item.level = mBomItem.level + 1;
                    item.count = 0;
                    item.bomChildren = [];
                    item.isNew = false;
                    vm.mBomItems.splice(lastIndex, 0, item);
                    mBomItem.count = mBomItem.count + 1;
                    mBomItem.expanded = true;
                    mBomItem.bomChildren.push(item);
                    lastIndex = populateMBOMItemChildren(item, lastIndex)
                });

                return lastIndex;
            }

            vm.toggleMBOMItemNode = toggleMBOMItemNode;
            function toggleMBOMItemNode(mBomItem) {
                if (mBomItem.expanded == null || mBomItem.expanded == undefined) {
                    mBomItem.expanded = false;
                }
                mBomItem.expanded = !mBomItem.expanded;
                var index = vm.mBomItems.indexOf(mBomItem);
                if (mBomItem.expanded == false) {
                    removeMBOMItemChildren(mBomItem);
                }
                else {
                    MBOMInstanceService.getMBOMInstanceItemChildren(vm.mbomInstanceId, mBomItem.id).then(
                        function (data) {
                            if (data.length > 0) {
                                mBomItem.hasBom = true;
                            }
                            angular.forEach(data, function (item) {
                                item.parentBom = mBomItem;
                                item.isNew = false;
                                item.expanded = false;
                                item.editMode = false;
                                item.level = mBomItem.level + 1;
                                item.bomChildren = [];
                                mBomItem.bomChildren.push(item);
                            });

                            angular.forEach(mBomItem.bomChildren, function (item) {
                                index = index + 1;
                                vm.mBomItems.splice(index, 0, item);
                            });
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function removeMBOMItemChildren(mBomItem) {
                if (mBomItem != null && mBomItem.bomChildren != null && mBomItem.bomChildren != undefined) {
                    angular.forEach(mBomItem.bomChildren, function (item) {
                        removeMBOMItemChildren(item);
                    });

                    var index = vm.mBomItems.indexOf(mBomItem);
                    vm.mBomItems.splice(index + 1, mBomItem.bomChildren.length);
                    mBomItem.bomChildren = [];
                    mBomItem.expanded = false;

                }
            }

            (function () {
                $scope.$on('app.mbomInstance.tabActivated', function (event, data) {
                    if (data.tabId == 'details.bom') {
                        loadMBOMInstanceItems();
                    }
                });
            })();
        }
    }
)
;


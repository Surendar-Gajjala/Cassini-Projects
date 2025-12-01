define(
    [
        'app/desktop/modules/item/item.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('ItemFilesController', ItemFilesController);

        function ItemFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.loading = true;
            vm.files = [];
            vm.showDropzone = false;
            vm.showFileDropzone = false;
            vm.itemId = $stateParams.itemId;

            vm.getFileEditPermission = getFileEditPermission;
            function getFileEditPermission() {
                var permission = true;
                if ($rootScope.itemRevision != null && $rootScope.itemRevision != undefined && $rootScope.itemRevision.released) {
                    if ($rootScope.itemRevision.incorporate) {
                        permission = false;
                    }
                } else {
                    permission = ($rootScope.hasPermission('item', 'edit') || $rootScope.sharedPermission == 'WRITE')
                        && $rootScope.itemRevision != null && $rootScope.itemRevision != undefined && !$rootScope.itemRevision.released && !$rootScope.itemRevision.rejected
                }

                return permission;
            }

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $rootScope.freeTextQuerys = null;
                        if ($rootScope.selectedMasterItemId != null) {
                            vm.itemId = $rootScope.selectedMasterItemId;
                        } else {
                            vm.itemId = $stateParams.itemId;
                        }
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {load: true});
                        }, 500);
                    }
                });
            })();
        }
    }
);
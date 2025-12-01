define(['app/desktop/modules/im/im.module',
        'app/shared/services/core/storeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'
    ],
    function (module) {
        module.controller('StoreBasicController', StoreBasicController);

        function StoreBasicController($scope, $rootScope, $timeout, $state, $cookies, $stateParams, StoreService) {

            var vm = this;

            vm.loading = true;
            vm.store = null;
            vm.updateStore = updateStore;
            vm.back = back;
            vm.storeId = $stateParams.storeId;

            function loadStore() {
                StoreService.getStore($stateParams.projectId, vm.storeId).then(
                    function (data) {
                        vm.store = data;
                        $rootScope.viewInfo.title = vm.store.name;
                    }
                )
            }

            function updateStore() {
                StoreService.updateStore($stateParams.projectId, vm.store).then(
                    function (data) {
                        loadStore();
                        $rootScope.showSuccessMessage("Store updated successfully!");
                    }
                )
            }

            function back() {
                $state.go('app.pm.project.stores.all');
            }

            (function () {
                loadStore();
                $scope.$on('app.Store.update', function () {
                    updateStore();
                })
            })();
        }
    }
);
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStoreService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'
    ],
    function (module) {
        module.controller('StoreBasicController', StoreBasicController);

        function StoreBasicController($scope, $rootScope, $timeout, $state, $sce, $cookies, $stateParams, TopStoreService, CommonService) {

            var vm = this;

            vm.loading = true;
            vm.store = null;
            vm.updateStore = updateStore;
            vm.back = back;
            if ($stateParams.storeId == null || $stateParams.storeId == "") {
                vm.storeId = $rootScope.selectedStore;
            }
            vm.storeId = $stateParams.storeId;
            function loadStore() {
                TopStoreService.getTopStore(vm.storeId).then(
                    function (data) {
                        vm.store = data;
                        $rootScope.selectedStore = data;
                        loadPersons();
                        if ($stateParams.mode == null) {
                            $rootScope.viewInfo.title = "Store Details : " + vm.store.storeName;
                        }
                        $rootScope.viewInfo.description = "Location : " + vm.store.locationName;
                    }
                )
            }

            function loadPersons() {
                var personIds = [vm.store.createdBy];

                if (vm.store.createdBy != vm.store.modifiedBy) {
                    personIds.push(vm.store.modifiedBy);
                }

                CommonService.getPersons(personIds).then(
                    function (persons) {
                        var map = new Hashtable();
                        angular.forEach(persons, function (person) {
                            map.put(person.id, person);
                        });

                        if (vm.store.createdBy != null) {
                            var person = map.get(vm.store.createdBy);
                            if (person != null) {
                                vm.store.createdByPerson = person;
                            }
                            else {
                                vm.store.createdByPerson = {firstName: ""};
                            }
                        }

                        if (vm.store.modifiedBy != null) {
                            person = map.get(vm.store.modifiedBy);
                            if (person != null) {
                                vm.store.modifiedByPerson = person;
                            }
                            else {
                                vm.store.modifiedByPerson = {firstName: ""};
                            }
                        }
                        vm.loading = false;
                    }
                );
            }

            function updateStore() {
                TopStoreService.updateTopStore(vm.store).then(
                    function (data) {
                        loadStore();
                        $rootScope.showSuccessMessage("Store updated successfully");
                    }
                )
            }

            function back() {
                $window.back();
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
define(
    [
        'app/desktop/modules/items/item.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/itemTypeService',
        'app/shared/services/itemService'
    ],
    function (module) {
        module.controller('ItemBasicInfoController', ItemBasicInfoController);

        function ItemBasicInfoController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                         CommonService, ItemTypeService, ItemService) {
            var vm = this;

            vm.loading = true;
            vm.itemId = $stateParams.itemId;
            vm.item = null;
            vm.barcodeImageLoading = true;
            vm.qrcodeImageLoading = true;
            vm.updateItem = updateItem;

            vm.barcodeImageLoaded = barcodeImageLoaded;
            vm.qrcodeImageLoaded = qrcodeImageLoaded;


            function loadItem() {
                vm.loading = true;
                ItemService.getItem(vm.itemId).then(
                    function (data) {
                        vm.item = data;
                        vm.loading = false;
                        loadPersons();
                    }
                )
            }

            function updateItem() {
                ItemService.updateItem(vm.item).then(
                    function (data) {
                        vm.item = data;
                        $rootScope.loadItem();
                        $rootScope.showSuccessMessage("Item updated successfully!");

                    }
                )
            }


            function loadPersons() {
                var personIds = [vm.item.createdBy];

                if (vm.item.createdBy != vm.item.modifiedBy) {
                    personIds.push(vm.item.modifiedBy);
                }

                CommonService.getPersons(personIds).then(
                    function (persons) {
                        var map = new Hashtable();
                        angular.forEach(persons, function (person) {
                            map.put(person.id, person);
                        });

                        if (vm.item.createdBy != null) {
                            var person = map.get(vm.item.createdBy);
                            if (person != null) {
                                vm.item.createdByPerson = person;
                            }
                            else {
                                vm.item.createdByPerson = {firstName: ""};
                            }
                        }

                        if (vm.item.modifiedBy != null) {
                            person = map.get(vm.item.modifiedBy);
                            if (person != null) {
                                vm.item.modifiedByPerson = person;
                            }
                            else {
                                vm.item.modifiedByPerson = {firstName: ""};
                            }
                        }
                    }
                );
            }

            function barcodeImageLoaded() {
                vm.barcodeImageLoading = false;
            }

            function qrcodeImageLoaded() {
                vm.qrcodeImageLoading = false;
            }


            (function () {
                if ($application.homeLoaded == true) {
                    loadItem();
                }
            })();
        }
    }
);
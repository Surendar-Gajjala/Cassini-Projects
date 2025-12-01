define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/ncrService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('NcrRelatedItemsController', NcrRelatedItemsController);

        function NcrRelatedItemsController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, $window, DialogService, NcrService) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var removePartTitle = parsed.html($translate.instant("REMOVE_PART")).html();
            var removePartDialogMsg = parsed.html($translate.instant("REMOVE_PART_TITLE_MSG")).html();
            var partRemovedMessage = parsed.html($translate.instant("REMOVE_PART_SUCCESS_MSG")).html();
            var partAddedMessage = parsed.html($translate.instant("PART_ADDED_MSG")).html();
            var partUpdatedMessage = parsed.html($translate.instant("PART_UPDATED_MSG")).html();
            $scope.addRelatedItemTitle = parsed.html($translate.instant("ADD_RELATED_ITEMS")).html();


            vm.addRelatedItems = addRelatedItems;
            vm.relatedItems = [];
            vm.ncrId = $stateParams.ncrId;

            var emptyItem = {
                id: null,
                ncr: vm.ncrId,
                material: null,
                notes: null
            };

            vm.items = [];
            vm.itemFlag = false;
            vm.selectedParts = [];
            function addRelatedItems() {
                var options = {
                    title: "Select Mfr Parts",
                    template: 'app/desktop/modules/item/details/selectMfrItemView.jsp',
                    controller: 'SelectMfrItemController as mfrItemVm',
                    resolve: 'app/desktop/modules/item/details/selectMfrItemController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedNcrId: vm.ncrId,
                        selectMfrPartsMode: "NCR",
                        relatedMfrParts: true
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.item.mfr.new'}
                    ],
                    callback: function (result) {
                        vm.parts = result;
                        $rootScope.showBusyIndicator($('.view-container'));
                        angular.forEach(vm.parts, function (part) {
                            var newAffectedPart = angular.copy(emptyItem);
                            newAffectedPart.material = part;
                            vm.selectedParts.push(newAffectedPart);
                        });
                        saveAll();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadRelatedItems() {
                $rootScope.showBusyIndicator();
                vm.loading=true;
                NcrService.getNcrRelatedItems(vm.ncrId).then(
                    function (data) {
                        vm.relatedItems = data;
                        angular.forEach(vm.relatedItems, function (relatedItem) {
                            relatedItem.editMode = false;
                            relatedItem.isNew = false;
                        })
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.save = save;
            function save(item) {
                $rootScope.showBusyIndicator($('.view-container'));
                NcrService.createNcrRelatedItem(vm.ncrId, item).then(
                    function (data) {
                        item.id = data.id;
                        item.editMode = false;
                        $rootScope.loadNcrDetails();
                        $rootScope.showSuccessMessage(partAddedMessage);
                        vm.selectedParts.splice(vm.selectedParts.indexOf(item), 1);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.saveAll = saveAll;
            function saveAll() {
                $rootScope.showBusyIndicator($('.view-container'));
                NcrService.createNcrRelatedItems(vm.ncrId, vm.selectedParts).then(
                    function (data) {
                        loadRelatedItems();
                        $rootScope.loadNcrDetails();
                        $rootScope.showSuccessMessage(partAddedMessage);
                        vm.selectedParts = [];
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.updateItem = updateItem;
            function updateItem(item) {
                $rootScope.showBusyIndicator($('.view-container'));
                NcrService.updateNcrRelatedItem(vm.ncrId, item).then(
                    function (data) {
                        item.id = data.id;
                        item.editMode = false;
                        $rootScope.showSuccessMessage(partUpdatedMessage);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.editItem = editItem;
            function editItem(item) {
                item.editMode = true;
                item.isNew = false;
                item.oldNotes = item.notes;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(item) {
                item.editMode = false;
                item.notes = item.oldNotes;
            }

            vm.deleteItem = deleteItem;

            function deleteItem(item) {
                var options = {
                    title: removePartTitle,
                    message: removePartDialogMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        NcrService.deleteNcrRelatedItem(vm.ncrId, item.id).then(
                            function (data) {
                                var index = vm.relatedItems.indexOf(item);
                                vm.relatedItems.splice(index, 1);
                                $rootScope.loadNcrDetails();
                                $rootScope.showSuccessMessage(partRemovedMessage);
                                loadRelatedItems();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });

            }


            $scope.showRelatedItem = showRelatedItem;
            function showRelatedItem() {
                $window.localStorage.setItem("lastSelectedNcrTab", JSON.stringify("details.relatedItem"));
            }

            (function () {
                $scope.$on('app.ncr.tabActivated', function (event, data) {
                    if (data.tabId == 'details.relatedItem') {
                        loadRelatedItems();
                    }
                })
            })();
        }
    }
)
;
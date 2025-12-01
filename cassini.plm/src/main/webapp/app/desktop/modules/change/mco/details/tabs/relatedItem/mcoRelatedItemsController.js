define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/mcoService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'

    ],
    function (module) {
        module.controller('MCORelatedItemsController', MCORelatedItemsController);

        function MCORelatedItemsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                           $translate, MCOService, CommentsService, DialogService) {
            var vm = this;
            vm.loading = true;
            vm.mcoId = $stateParams.mcoId;

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

            var emptyItem = {
                id: null,
                mco: vm.mcoId,
                part: null,
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
                        selectedMcoId: vm.mcoId,
                        selectMfrPartsMode: "MCO",
                        relatedMfrParts: true
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.item.mfr.new'}
                    ],
                    callback: function (result) {
                        vm.parts = result;
                        angular.forEach(vm.parts, function (part) {
                            var newAffectedPart = angular.copy(emptyItem);
                            newAffectedPart.part = part;
                            newAffectedPart.editMode = true;
                            newAffectedPart.isNew = true;
                            vm.relatedItems.unshift(newAffectedPart);
                            vm.selectedParts.push(newAffectedPart);
                        });

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadRelatedItems() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                MCOService.getMcoRelatedItems(vm.mcoId).then(
                    function (data) {
                        vm.relatedItems = data;
                        angular.forEach(vm.relatedItems, function (relatedItem) {
                            relatedItem.editMode = false;
                            relatedItem.isNew = false;
                        });
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.save = save;
            function save(item) {
                $rootScope.showBusyIndicator($('.view-container'));
                MCOService.createMcoRelatedItem(vm.mcoId, item).then(
                    function (data) {
                        item.id = data.id;
                        item.editMode = false;
                        item.isNew = false;
                        $rootScope.loadMcoDetails();
                        vm.selectedParts.splice(vm.selectedParts.indexOf(item), 1);
                        $rootScope.showSuccessMessage(partAddedMessage);
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
                MCOService.createMcoRelatedItems(vm.mcoId, vm.selectedParts).then(
                    function (data) {
                        $rootScope.loadMcoDetails();
                        loadRelatedItems();
                        $rootScope.showSuccessMessage(partAddedMessage);
                        vm.selectedParts = [];
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.removeAll = removeAll;
            function removeAll() {
                angular.forEach(vm.selectedParts, function (item) {
                    vm.relatedItems.splice(vm.relatedItems.indexOf(item), 1);
                })
                vm.selectedParts = [];
            }

            vm.updateItem = updateItem;
            function updateItem(item) {
                $rootScope.showBusyIndicator($('.view-container'));
                MCOService.updateMcoRelatedItem(vm.mcoId, item).then(
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
                item.isNew = false;
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
                        MCOService.deleteMcoRelatedItem(vm.mcoId, item.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(partRemovedMessage);
                                $rootScope.loadMcoDetails();
                                loadRelatedItems();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });

            }


            $scope.showPart = showPart;
            function showPart() {
                $window.localStorage.setItem("lastSelectedMcoTab", JSON.stringify("details.relatedItems"));
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $scope.$on('app.mco.tabActivated', function (event, args) {
                    if (args.tabId == 'details.relatedItems') {
                        loadRelatedItems();
                    }
                });
                //}
            })();
        }
    }
);
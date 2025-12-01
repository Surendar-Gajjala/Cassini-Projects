define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/shared/services/core/qcrService',
        'app/desktop/directives/affected-items/affectedItemsDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('QcrProblemItemsController', QcrProblemItemsController);

        function QcrProblemItemsController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, $window, DialogService, QcrService) {
            var vm = this;
            vm.qcrId = $stateParams.qcrId;

            var parsed = angular.element("<div></div>");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var removePartTitle = parsed.html($translate.instant("REMOVE_PART")).html();
            var removePartDialogMsg = parsed.html($translate.instant("REMOVE_PART_TITLE_MSG")).html();
            var partRemovedMessage = parsed.html($translate.instant("REMOVE_PART_SUCCESS_MSG")).html();
            var removeItems = parsed.html($translate.instant("REMOVE_ITEM")).html();
            var itemRemoveTitle = parsed.html($translate.instant("REMOVE_ITEM_TITLE_MSG")).html();
            var itemRemoveMsg = parsed.html($translate.instant("REMOVE_ITEM_SUCCESS_MSG")).html();
            var partAddedMessage = parsed.html($translate.instant("PART_ADDED_MSG")).html();
            var partUpdatedMessage = parsed.html($translate.instant("PART_UPDATED_MSG")).html();
            $scope.addProblemItemTitle = parsed.html($translate.instant("ADD_PROBLEM_ITEMS")).html();
            $scope.saveItemTitle = parsed.html($translate.instant("SAVE_THIS_ITEM")).html();
            var wfStartedOnePiSbt = parsed.html($translate.instant("WF_STARTED_ONE_PI_SBT")).html();
            vm.addProblemMaterials = addProblemMaterials;

            vm.problemItems = [];
            $scope.qcrFor = null;

            var emptyProblemMaterial = {
                id: null,
                qcr: vm.qcrId,
                material: null,
                notes: null
            };

            vm.selectedParts = [];
            function addProblemMaterials() {
                var options = {
                    title: "Select Mfr Parts",
                    template: 'app/desktop/modules/item/details/selectMfrItemView.jsp',
                    controller: 'SelectMfrItemController as mfrItemVm',
                    resolve: 'app/desktop/modules/item/details/selectMfrItemController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedQCR: vm.qcrId,
                        selectMfrPartsMode: "QCR"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.item.mfr.new'}
                    ],
                    callback: function (result) {
                        vm.parts = result;
                        angular.forEach(vm.parts, function (part) {
                            var newAffectedPart = angular.copy(emptyProblemMaterial);
                            newAffectedPart.material = part;
                            newAffectedPart.editMode = true;
                            newAffectedPart.isNew = true;
                            vm.problemItems.unshift(newAffectedPart);
                            vm.selectedParts.push(newAffectedPart);
                        });

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadProblemItems() {
                QcrService.getQcrProblemMaterials(vm.qcrId).then(
                    function (data) {
                        vm.problemItems = data;
                        angular.forEach(vm.problemItems, function (relatedItem) {
                            relatedItem.editMode = false;
                            relatedItem.isNew = false;
                        })
                        vm.loading = false;
                        $scope.$evalAsync();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.save = save;
            function save(item) {
                QcrService.createQcrProblemMaterial(vm.qcrId, item).then(
                    function (data) {
                        item.id = data.id;
                        item.editMode = false;
                        $rootScope.loadQcrDetails();
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
                QcrService.createQcrProblemMaterials(vm.qcrId, vm.selectedParts).then(
                    function (data) {
                        vm.selectedParts = [];
                        $rootScope.loadQcrDetails();
                        loadProblemItems();
                        $rootScope.showSuccessMessage(partAddedMessage);
                        vm.selectedParts = [];
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.updateItem = updateItem;
            function updateItem(item) {
                QcrService.updateQcrProblemMaterial(vm.qcrId, item).then(
                    function (data) {
                        item.id = data.id;
                        item.editMode = false;
                        vm.selectedParts = [];
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

            vm.onCancel = onCancel;
            function onCancel(item) {
                vm.problemItems.splice(vm.problemItems.indexOf(item), 1);
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(item) {
                item.editMode = false;
                item.isNew = false;
                item.notes = item.oldNotes;
            }

            vm.deleteItem = deleteItem;

            function deleteItem(item) {
                if ($rootScope.qcrWorkflowStarted && $rootScope.qcrDetailCount.problemItems == 1) {
                    $rootScope.showWarningMessage(wfStartedOnePiSbt);
                } else {
                    var options = null;
                    if ($rootScope.qcr.qcrFor == "PR") {
                        options = {
                            title: removeItems,
                            message: itemRemoveTitle,
                            okButtonClass: 'btn-danger'
                        };
                    } else {
                        options = {
                            title: removePartTitle,
                            message: removePartDialogMsg,
                            okButtonClass: 'btn-danger'
                        };
                    }
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            var promise = null;
                            $rootScope.showBusyIndicator($('.view-container'));
                            if ($rootScope.qcr.qcrFor == "PR") {
                                promise = QcrService.deleteQcrProblemItem(vm.qcrId, item.id);
                            } else {
                                promise = QcrService.deleteQcrProblemMaterial(vm.qcrId, item.id);
                            }

                            if (promise != null) {
                                promise.then(
                                    function (data) {
                                        $rootScope.loadQcrDetails();
                                        if ($scope.qcrFor == "PR") {
                                            $rootScope.showSuccessMessage(itemRemoveMsg);
                                        } else {
                                            $rootScope.showSuccessMessage(partRemovedMessage);
                                        }
                                        loadProblemItems();
                                        $scope.$broadcast('app.affectedItems.view', {});
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }
                    });
                }
            }


            $scope.showAffectedItem = showAffectedItem;
            function showAffectedItem() {
                $window.localStorage.setItem("lastSelectedQcrTab", JSON.stringify("details.problemItem"));
            }

            $scope.showProblemReport = showProblemReport;
            function showProblemReport() {
                $window.localStorage.setItem("lastSelectedQcrTab", JSON.stringify("details.problemItem"));
            }

            (function () {
                $scope.$on('app.qcr.tabActivated', function (event, data) {
                    if (data.tabId == 'details.problemItem') {
                        $scope.qcrFor = $rootScope.qcr.qcrFor;
                        if ($scope.qcrFor == "NCR") {
                            loadProblemItems();
                        } else {
                            $scope.$broadcast('app.affectedItems.view', {});
                        }
                    }
                })
            })();
        }
    }
)
;
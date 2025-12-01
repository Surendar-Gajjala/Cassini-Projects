define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'moment',
        'moment-timezone-with-data',
        'app/desktop/modules/directives/relatedItems/relatedItemsDirectiveController',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('QcrRelatedItemsController', QcrRelatedItemsController);

        function QcrRelatedItemsController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, $window, DialogService, QcrService) {
            var vm = this;

            var parsed = angular.element("<div></div>");
            var selectItems = parsed.html($translate.instant("MULTIPLE_ITEM_SELECTION")).html();
            var removePartTitle = parsed.html($translate.instant("REMOVE_PART")).html();
            var removePartDialogMsg = parsed.html($translate.instant("REMOVE_PART_TITLE_MSG")).html();
            var partRemovedMessage = parsed.html($translate.instant("REMOVE_PART_SUCCESS_MSG")).html();
            var removeItems = parsed.html($translate.instant("REMOVE_ITEM")).html();
            var itemRemoveTitle = parsed.html($translate.instant("REMOVE_ITEM_TITLE_MSG")).html();
            var itemRemoveMsg = parsed.html($translate.instant("REMOVE_ITEM_SUCCESS_MSG")).html();
            var partAddedMessage = parsed.html($translate.instant("PART_ADDED_MSG")).html();
            var partUpdatedMessage = parsed.html($translate.instant("PART_UPDATED_MSG")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();
            $scope.addRelatedItemTitle = parsed.html($translate.instant("ADD_RELATED_ITEMS")).html();
            vm.qcrId = $stateParams.qcrId;

            $scope.qcrFor = null;
            vm.relatedItems = [];
            var emptyRelatedMaterial = {
                id: null,
                qcr: vm.qcrId,
                material: null
            };
            vm.selectedParts = [];
            vm.addRelatedMaterials = addRelatedMaterials;
            function addRelatedMaterials() {
                vm.selectedParts = [];
                var options = {
                    title: "Select Mfr Parts",
                    template: 'app/desktop/modules/item/details/selectMfrItemView.jsp',
                    controller: 'SelectMfrItemController as mfrItemVm',
                    resolve: 'app/desktop/modules/item/details/selectMfrItemController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedQCR: vm.qcrId,
                        selectMfrPartsMode: "QCR",
                        relatedMfrParts: true
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.item.mfr.new'}
                    ],
                    callback: function (result) {
                        vm.parts = result;
                        angular.forEach(vm.parts, function (part) {
                            var newAffectedPart = angular.copy(emptyRelatedMaterial);
                            newAffectedPart.material = part;
                            vm.selectedParts.push(newAffectedPart);
                        });
                        QcrService.createQcrRelatedMaterials(vm.qcrId, vm.selectedParts).then(
                            function (data) {
                                vm.selectedParts = [];
                                loadRelatedItems();
                                $rootScope.loadQcrDetails();
                                $rootScope.showSuccessMessage(partAddedMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                };

                $rootScope.showSidePanel(options);
            }


            function loadRelatedItems() {
                QcrService.getQcrRelatedMaterials(vm.qcrId).then(
                    function (data) {
                        vm.relatedItems = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
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
                        QcrService.deleteQcrRelatedMaterial(vm.qcrId, item.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(partRemovedMessage);
                                $rootScope.loadQcrDetails();
                                loadRelatedItems();
                                $rootScope.hideBusyIndicator();
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
                $window.localStorage.setItem("lastSelectedQcrTab", JSON.stringify("details.relatedItem"));
            }

            (function () {
                $scope.$on('app.qcr.tabActivated', function (event, data) {
                    if (data.tabId == 'details.relatedItem') {
                        $scope.qcrFor = $rootScope.qcr.qcrFor;
                        if ($scope.qcrFor == "PR") {
                            $scope.$broadcast('app.relatedItem.tabActivated', {load: true});
                        } else {
                            loadRelatedItems();
                        }
                    }
                })
            })();
        }
    }
);
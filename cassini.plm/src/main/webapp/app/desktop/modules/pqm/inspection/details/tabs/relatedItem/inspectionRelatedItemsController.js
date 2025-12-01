define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/relatedItems/relatedItemsDirectiveController',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('InspectionRelatedItemsController', InspectionRelatedItemsController);

        function InspectionRelatedItemsController($scope, $rootScope, $sce, $timeout, $state, $translate, $stateParams, $cookies, $window, DialogService, InspectionService) {
            var vm = this;

            var parsed = angular.element("<div></div>");
            var selectItems = parsed.html($translate.instant("MULTIPLE_ITEM_SELECTION")).html();
            $scope.addRelatedItemTitle = parsed.html($translate.instant("ADD_RELATED_ITEMS")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();

            vm.inspectionId = $stateParams.inspectionId;

            var emptyItem = {
                id: null,
                inspection: vm.inspectionId,
                material: null,
                notes: null
            };

            vm.relatedItems = [];
            vm.itemFlag = false;
            vm.addMaterialRelatedItems = addMaterialRelatedItems;
            function addMaterialRelatedItems() {
                vm.selectedParts = [];
                var options = {
                    title: "Select Mfr Parts",
                    template: 'app/desktop/modules/item/details/selectMfrItemView.jsp',
                    controller: 'SelectMfrItemController as mfrItemVm',
                    resolve: 'app/desktop/modules/item/details/selectMfrItemController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedInspectionId: vm.inspectionId,
                        selectMfrPartsMode: $rootScope.inspection.objectType,
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
                            newAffectedPart.material = part.id;
                            vm.selectedParts.push(newAffectedPart);
                        });
                        InspectionService.createMaterialRelatedItems(vm.inspectionId, vm.selectedParts).then(
                            function (data) {
                                loadRelatedItems();
                                $rootScope.loadInspectionDetails();
                                $rootScope.hideBusyIndicator();
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
                vm.loading = true;
                InspectionService.getMaterialInspectionRelatedItems(vm.inspectionId).then(
                    function (data) {
                        vm.relatedItems = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    });
            }

            var removeItems = parsed.html($translate.instant("REMOVE_ITEM")).html();
            var itemRemoveTitle = parsed.html($translate.instant("REMOVE_ITEM_TITLE_MSG")).html();
            var itemRemoveMsg = parsed.html($translate.instant("REMOVE_ITEM_SUCCESS_MSG")).html();

            vm.deleteItem = deleteItem;

            function deleteItem(item) {
                var options = {
                    title: removeItems,
                    message: itemRemoveTitle,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($(".view-container"));
                        InspectionService.deleteMaterialRelatedItem(vm.inspectionId, item.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(itemRemoveMsg);
                                loadRelatedItems();
                                $rootScope.loadInspectionDetails();
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
                $window.localStorage.setItem("lastSelectedInspectionTab", JSON.stringify("details.relatedItem"));
            }

            (function () {
                $scope.$on('app.inspection.tabActivated', function (event, data) {
                    if (data.tabId == 'details.relatedItem') {
                        if ($rootScope.inspection.objectType == "ITEMINSPECTION") {
                            $scope.$broadcast('app.relatedItem.tabActivated', {load: true});
                        } else {
                            loadRelatedItems();
                        }
                    }
                })
            })();
        }
    }
)
;
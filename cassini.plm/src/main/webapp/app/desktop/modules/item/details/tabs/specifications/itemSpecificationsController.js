define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/shared/services/core/itemService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('ItemSpecificationsController', ItemSpecificationsController);

        function ItemSpecificationsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window,
                                              DialogService, ItemService, $translate) {
            var vm = this;

            vm.loading = true;
            vm.itemId = $stateParams.itemId;
            vm.addSpecifications = addSpecifications;
            vm.deleteItemSpecification = deleteItemSpecification;

            var parsed = angular.element("<div></div>");
            var operationDeleted = parsed.html($translate.instant("DELETE_TEM_SPECIFICATION_MESSAGE")).html();
            var deleteItemSpecificationTitle = parsed.html($translate.instant("DELETE_TEM_SPECIFICATION_DIALOG_TITLE")).html();
            var deleteItemSpecificationDialogMsg = parsed.html($translate.instant("DELETE_ITEM_SPECIFICATION_DIALOG_MESSAGE")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();
            var specSuccessMsg = parsed.html($translate.instant("SPECIFICATION_ITEM_SUCCESS_MESSAGE")).html();

            function addSpecifications() {
                var options = {
                    title: "Add Specifications",
                    template: 'app/desktop/modules/compliance/declaration/details/tabs/specifications/selectSpecificationsView.jsp',
                    controller: 'SelectSpecificationsController as selectSpecificationsVm',
                    resolve: 'app/desktop/modules/compliance/declaration/details/tabs/specifications/selectSpecificationsController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedItemId: vm.itemId,
                        mode: "ITEM"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.declaration.specifications.add'}
                    ],
                    callback: function (result) {
                        vm.specifications = result;
                        var emptyDecSpec = {
                            id: null,
                            item: vm.itemId,
                            specification: null
                        };
                        vm.selectedSpecifications = [];
                        angular.forEach(vm.specifications, function (specification) {
                            var decSpec = angular.copy(emptyDecSpec);
                            decSpec.specification = specification;
                            vm.selectedSpecifications.push(decSpec);
                        });
                        saveItemSpecifications();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function saveItemSpecifications() {
                ItemService.createMultipleItemSpecification(vm.itemId, vm.selectedSpecifications).then(
                    function (data) {
                        $rootScope.showSuccessMessage(specSuccessMsg);
                        $rootScope.loadItemDetails();
                        loadItemSpecifications();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.itemSpecifications = [];
            function loadItemSpecifications() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                vm.itemSpecifications = [];
                ItemService.getItemSpecifications(vm.itemId).then(
                    function (data) {
                        vm.itemSpecifications = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function deleteItemSpecification(operation) {
                var options = {
                    title: deleteItemSpecificationTitle,
                    message: deleteItemSpecificationDialogMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            ItemService.deleteItemSpecification(vm.itemId, operation.id).then(
                                function (data) {
                                    $rootScope.loadItemDetails();
                                    loadItemSpecifications();
                                    $rootScope.showSuccessMessage(operationDeleted);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                )
            }

            vm.showSpecificationDetails = showSpecificationDetails;
            function showSpecificationDetails(itemSpecification) {
                $state.go('app.compliance.specification.details', {
                    specificationId: itemSpecification.specification.id,
                    tab: 'details.basic'
                });
            }

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId == 'details.specifications') {
                        loadItemSpecifications();
                    }
                });
            })();
        }
    }
);

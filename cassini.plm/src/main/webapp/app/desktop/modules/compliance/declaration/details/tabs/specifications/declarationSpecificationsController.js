define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/shared/services/core/declarationService'
    ],
    function (module) {
        module.controller('DeclarationSpecificationsController', DeclarationSpecificationsController);

        function DeclarationSpecificationsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window,
                                                     DialogService, DeclarationService, $translate) {
            var vm = this;

            vm.loading = true;
            vm.declarationId = $stateParams.declarationId;
            vm.addSpecifications = addSpecifications;
            vm.deleteDeclarationSpecification = deleteDeclarationSpecification;

            var parsed = angular.element("<div></div>");
            var operationDeleted = parsed.html($translate.instant("SPECIFICATION_REMOVED_MSG")).html();
            var deleteDeclarationSpecificationTitle = parsed.html($translate.instant("REMOVE_SPECIFICATION")).html();
            var deleteDeclarationSpecificationDialogMsg = parsed.html($translate.instant("DELETE_SPECIFICATION_DIALOG_MSG")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();
            var addSpecificationTitle = parsed.html($translate.instant("ADD_SPECIFICATIONS")).html();

            var emptyDecSpec = {
                id: null,
                declaration: vm.declarationId,
                specification: null
            }

            function addSpecifications() {
                var options = {
                    title: addSpecificationTitle,
                    template: 'app/desktop/modules/compliance/declaration/details/tabs/specifications/selectSpecificationsView.jsp',
                    controller: 'SelectSpecificationsController as selectSpecificationsVm',
                    resolve: 'app/desktop/modules/compliance/declaration/details/tabs/specifications/selectSpecificationsController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedDeclarationId: vm.declarationId,
                        mode: "DECLARATION"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.declaration.specifications.add'}
                    ],
                    callback: function (result) {
                        vm.selectedSpecifications = [];
                        angular.forEach(result, function (specification) {
                            var decSpec = angular.copy(emptyDecSpec);
                            decSpec.specification = specification;
                            vm.selectedSpecifications.push(decSpec);
                        })
                        saveSpecifications();

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function saveSpecifications() {
                DeclarationService.createMultipleDeclarationSpecification(vm.declarationId, vm.selectedSpecifications).then(
                    function (data) {
                        $rootScope.loadDeclarationTabCounts();
                        loadDeclarationSpecifications();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadDeclarationSpecifications() {
                vm.declarationSpecifications = [];
                vm.loading = true;
                DeclarationService.getDeclarationSpecifications(vm.declarationId).then(
                    function (data) {
                        vm.declarationSpecifications = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function deleteDeclarationSpecification(operation) {
                var options = {
                    title: deleteDeclarationSpecificationTitle,
                    message: deleteDeclarationSpecificationDialogMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            DeclarationService.deleteDeclarationSpecification(vm.declarationId, operation.id).then(
                                function (data) {
                                    $rootScope.loadDeclarationTabCounts();
                                    loadDeclarationSpecifications();
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

            vm.showSpecification = showSpecification;
            function showSpecification(declarationPart) {
                $state.go('app.compliance.specification.details', {
                    specificationId: declarationPart.specification.id,
                    tab: 'details.basic'
                });
            }


            (function () {
                $scope.$on('app.declaration.tabActivated', function (event, data) {
                    if (data.tabId == 'details.specifications') {
                        loadDeclarationSpecifications();
                    }
                });
            })();
        }
    }
);

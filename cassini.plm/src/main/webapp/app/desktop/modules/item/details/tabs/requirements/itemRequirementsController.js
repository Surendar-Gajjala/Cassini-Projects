define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/requirementService'
    ],
    function (module) {
        module.controller('ItemRequirementsController', ItemRequirementsController);

        function ItemRequirementsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, RequirementService) {
            var vm = this;

            vm.loading = true;
            vm.itemId = $stateParams.itemId;

            function loadItemRequirements() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                RequirementService.getItemRequirements(vm.itemId).then(
                    function (data) {
                        vm.itemRequirements = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showRequirementDetails = showRequirementDetails;
            function showRequirementDetails(req) {
                $state.go('app.req.requirements.details', {requirementId: req.documentChildren, tab: 'details.basic'});
            }

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId == 'details.requirements') {
                        if ($rootScope.selectedMasterItemId != null) {
                            vm.itemId = $rootScope.selectedMasterItemId;
                            loadItemRequirements();
                        }
                        if ($rootScope.selectedMasterItemId == null) {
                            loadItemRequirements();
                        }
                    }
                });
            })();
        }
    }
);
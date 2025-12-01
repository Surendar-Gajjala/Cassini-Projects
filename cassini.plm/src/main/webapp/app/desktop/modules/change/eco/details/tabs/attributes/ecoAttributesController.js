define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ECOAttributesController', ECOAttributesController);

        function ECOAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, ObjectAttributeService, $window,
                                         AttributeAttachmentService, SpecificationsService, ItemService, WorkflowDefinitionService, $translate, MfrService, MfrPartsService, DialogService, CommonService) {
            var vm = this;

            vm.ecoId = $stateParams.ecoId;

            (function () {
                $scope.$on('app.eco.tabactivated', function (event, data) {
                    if (data.tabId == 'details.attribute') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                })
            })();
        }
    }
);
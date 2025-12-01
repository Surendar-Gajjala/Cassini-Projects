define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController',
        'app/shared/services/core/itemBomService',
        'app/shared/services/core/ecoService'
    ],
    function (module) {
        module.controller('CustomExtensionController', CustomExtensionController);

        function CustomExtensionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                         ItemService, ItemBomService, ECOService) {

            (function() {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId === 'custom.tab1') {
                        console.log("Custom tab activated");
                    }
                });
            })();
        }
    }
);
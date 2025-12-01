define(
    [
        'app/desktop/modules/item/item.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ItemAttributesController', ItemAttributesController);

        function ItemAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $window, $translate, $cookies) {
            var vm = this;

            vm.itemId = $stateParams.itemId;

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {load: true});
                    }
                });
            })();
        }
    }
);


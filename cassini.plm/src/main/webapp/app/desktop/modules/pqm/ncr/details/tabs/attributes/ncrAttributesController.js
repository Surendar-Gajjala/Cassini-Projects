define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('NcrAttributesController', NcrAttributesController);

        function NcrAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $window, $translate, $cookies) {
            var vm = this;

            vm.ncrId = $stateParams.ncrId;

            (function () {
                $scope.$on('app.ncr.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                });
            })();
        }
    }
)
;
define(
    [
        'app/desktop/modules/req/reqdocs/requirement/requirement.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ReqAttributesController', ReqAttributesController);

        function ReqAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;
            vm.reqId = $stateParams.requirement;

            (function () {
                $scope.$on('app.req.requirement.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});

                    }
                })
            })();
        }
    }
);

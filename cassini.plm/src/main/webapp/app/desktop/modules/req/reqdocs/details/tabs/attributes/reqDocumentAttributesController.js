define(
    [
        'app/desktop/modules/req/req.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ReqDocumentAttributesController', ReqDocumentAttributesController);

        function ReqDocumentAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;
            vm.reqDocId = $stateParams.reqId;

            (function () {
                $scope.$on('app.req.document.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});

                    }
                })
            })();
        }
    }
);

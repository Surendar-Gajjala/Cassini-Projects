define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('SpecificationFilesController', SpecificationFilesController);

        function SpecificationFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.specificationId = $stateParams.specificationId;

            (function () {
                $scope.$on('app.specification.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {});
                        }, 500);
                    }
                });
            })();
        }
    }
);


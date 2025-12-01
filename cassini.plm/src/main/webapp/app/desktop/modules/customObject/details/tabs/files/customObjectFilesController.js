define(
    [
        'app/desktop/modules/customObject/customObject.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/customFiles/customFilesDirectiveController'
    ],
    function (module) {
        module.controller('CustomObjectFilesController', CustomObjectFilesController);

        function CustomObjectFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.customId = $stateParams.customId;

            (function () {
                $scope.$on('app.customObj.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $scope.$broadcast('app.customObj.files.tabActivated', {load: true});
                    }
                });
            })();
        }
    }
);

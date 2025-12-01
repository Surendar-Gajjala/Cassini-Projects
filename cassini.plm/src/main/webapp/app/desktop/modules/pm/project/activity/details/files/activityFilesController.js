define(
    [
        'app/desktop/modules/pm/project/activity/activity.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('ActivityFilesController', ActivityFilesController);

        function ActivityFilesController($scope, $stateParams, $translate, $rootScope, $timeout, $state, $application) {

            var vm = this;
            vm.showDropzone = false;
            vm.activityId = $stateParams.activityId;

            (function () {
                $scope.$on('app.activity.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {load: true});
                        }, 500);
                    }
                })
            })();
        }
    }
);
define(
    [
        'app/desktop/modules/template/templateActivity/templateActivity.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('TemplateActivityFilesController', TemplateActivityFilesController);

        function TemplateActivityFilesController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $application) {
            var vm = this;
            vm.activityId = $stateParams.activityId;

            (function () {
                $scope.$on('app.template.activity.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {load: true});
                        }, 500);
                    }
                });
            })();
        }
    }
);
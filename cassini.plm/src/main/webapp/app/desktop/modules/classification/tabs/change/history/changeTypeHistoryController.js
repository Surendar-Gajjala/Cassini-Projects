define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('ChangeTypeHistoryController', ChangeTypeHistoryController);

        function ChangeTypeHistoryController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies) {

            (function () {
                $scope.$on('app.changeType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.history') {
                        $scope.$broadcast('app.object.timeline', {});

                    }
                })
            })();

        }
    });
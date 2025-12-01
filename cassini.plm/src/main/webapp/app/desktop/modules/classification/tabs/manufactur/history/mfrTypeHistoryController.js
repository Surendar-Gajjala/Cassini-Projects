define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('MfrTypeHistoryController', MfrTypeHistoryController);

        function MfrTypeHistoryController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies) {

            (function () {
                $scope.$on('app.mfrType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.history') {
                        $scope.$broadcast('app.object.timeline', {});

                    }
                })
            })();

        }
    });
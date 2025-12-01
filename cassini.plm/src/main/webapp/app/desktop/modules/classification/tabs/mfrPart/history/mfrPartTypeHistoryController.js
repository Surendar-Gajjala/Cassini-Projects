define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('MfrPartTypeHistoryController', MfrPartTypeHistoryController);

        function MfrPartTypeHistoryController($q, $scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

            (function () {
                $scope.$on('app.mfrPartType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.history') {
                        $scope.$broadcast('app.object.timeline', {});

                    }
                })
            })();

        }
    });
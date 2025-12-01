define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('MESObjectTypeHistoryController', MESObjectTypeHistoryController);

        function MESObjectTypeHistoryController($q, $scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

            (function () {
                $scope.$on('app.objectType.tabActivated', function (event, data) {
                    if (data.tabId == 'details.history') {
                        $scope.$broadcast('app.object.timeline', {});

                    }
                })
            })();

        }
    });
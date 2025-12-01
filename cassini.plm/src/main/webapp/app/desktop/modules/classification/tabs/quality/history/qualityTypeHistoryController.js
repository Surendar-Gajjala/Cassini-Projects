define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('QualityTypeHistoryController', QualityTypeHistoryController);

        function QualityTypeHistoryController($q, $scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

            (function () {
                $scope.$on('app.qualityType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.history') {
                        $scope.$broadcast('app.object.timeline', {});

                    }
                })
            })();

        }
    });
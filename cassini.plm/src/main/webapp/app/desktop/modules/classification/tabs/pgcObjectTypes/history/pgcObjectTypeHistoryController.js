define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('PGCObjectTypeHistoryController', PGCObjectTypeHistoryController);

        function PGCObjectTypeHistoryController($q, $scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

            (function () {
                $scope.$on('app.selectedPgcType.tabActivated', function (event, data) {
                    if (data.tabId == 'details.history') {
                        $scope.$broadcast('app.object.timeline', {});

                    }
                })
            })();

        }
    });
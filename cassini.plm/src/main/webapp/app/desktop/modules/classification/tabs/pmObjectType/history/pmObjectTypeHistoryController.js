define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('PMObjectTypeHistoryController', PMObjectTypeHistoryController);

        function PMObjectTypeHistoryController($q, $scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

            (function () {
                $scope.$on('app.pmType.tabActivated', function (event, data) {
                    if (data.tabId == 'details.history') {
                        $scope.$broadcast('app.object.timeline', {});

                    }
                })
            })();

        }
    });
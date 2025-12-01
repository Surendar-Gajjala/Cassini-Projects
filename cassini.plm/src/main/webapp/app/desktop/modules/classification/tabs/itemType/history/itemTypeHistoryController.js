define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('ItemTypeHistoryController', ItemTypeHistoryController);

        function ItemTypeHistoryController($q, $scope, $rootScope, $timeout, $state, $stateParams, $cookies) {


            var vm = this;

            (function () {
                $scope.$on('app.itemType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.history') {
                        $scope.$broadcast('app.object.timeline', {});

                    }
                })
            })();
        }
    }
)
;
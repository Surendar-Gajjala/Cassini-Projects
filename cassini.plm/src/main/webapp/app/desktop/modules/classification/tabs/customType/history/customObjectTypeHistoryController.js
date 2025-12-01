define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('CustomObjectTypeHistoryController', CustomObjectTypeHistoryController);

        function CustomObjectTypeHistoryController($q, $scope, $rootScope, $timeout, $state, $stateParams, $cookies) {


            var vm = this;

            (function () {
                $scope.$on('app.customType.tabActivated', function (event, data) {
                    if (data.tabId == 'details.history') {
                        $scope.$broadcast('app.object.timeline', {});

                    }
                })
            })();
        }
    }
)
;
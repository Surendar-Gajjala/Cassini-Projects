define(
    [
        'app/desktop/modules/customObject/customObject.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('CustomObjectTimeLineController', CustomObjectTimeLineController);

        function CustomObjectTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                                $translate) {
            var vm = this;
            vm.customId = $stateParams.customId;

            var parsed = angular.element("<div></div>");

            (function () {
                $scope.$on('app.customObj.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);
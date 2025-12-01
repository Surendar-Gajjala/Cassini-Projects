define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('SpecificationTimeLineController', SpecificationTimeLineController);

        function SpecificationTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       $translate) {
            var vm = this;
            vm.specificationId = $stateParams.specificationId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.specification.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);
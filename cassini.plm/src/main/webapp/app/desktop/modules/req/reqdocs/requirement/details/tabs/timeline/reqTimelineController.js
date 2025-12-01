define(
    [
        'app/desktop/modules/req/reqdocs/requirement/requirement.module',
        'app/desktop/modules/directives/timelineDirective',
        'app/shared/services/core/requirementService'


    ],
    function (module) {
        module.controller('ReqTimeLineController', ReqTimeLineController);

        function ReqTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       $translate, RequirementService) {
            var vm = this;
            vm.reqId = $stateParams.requirementId;

            $scope.loading = true;

            var parsed = angular.element("<div></div>");


            function loadRequirementVersions() {
                RequirementService.getReqVersionIds(vm.reqId).then(
                    function (data) {
                        vm.reqVersionIds = data;
                        $timeout(function () {
                            $scope.$broadcast('app.object.timeline', {});
                        }, 500);
                    }
                )
            }

            (function () {

                $scope.$on('app.req.requirement.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        loadRequirementVersions();
                    }
                });

            })();
        }
    }
);
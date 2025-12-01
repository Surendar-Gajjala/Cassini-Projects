define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('PlanFilesController', PlanFilesController);

        function PlanFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.loading = true;
            vm.planId = $stateParams.planId;

            (function () {
                $scope.$on('app.inspectionPlan.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {});
                        }, 500);
                    }
                });
            })();
        }
    }
)
;
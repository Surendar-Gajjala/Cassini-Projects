define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('NcrHistoryController', NcrHistoryController);

        function NcrHistoryController($scope, $rootScope, $timeout, $application, $sce, $state, $stateParams, $cookies, $window, ItemTypeService, $uibModal, $translate, ObjectTypeAttributeService,
                                      CommonService, ItemService, LoginService, QualityTypeService) {
            var vm = this;

            vm.loading = true;
            vm.ncrId = $stateParams.ncrId;

            (function () {
                $scope.$on('app.ncr.tabActivated', function (event, data) {

                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }

                });
            })();
        }
    }
);
define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/specificationsService'
    ],
    function (module) {
        module.controller('SpecificationRevisionHistoryController', SpecificationRevisionHistoryController);

        function SpecificationRevisionHistoryController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $cookies,
                                                        SpecificationsService) {
            var vm = this;

            vm.data = $scope.data;
            vm.specificationRevisionHisotry = [];
            vm.showSpecificationRevision = showSpecificationRevision;

            var parsed = angular.element("<div></div>");
            vm.clickToShowRevision = parsed.html($translate.instant("CLICK_TO_SHOW_REVISIONS")).html();

            function loadSpecificationRevisionHistory() {
                SpecificationsService.getSpecificationRevisionHistory(vm.data.specId).then(
                    function (data) {
                        vm.specificationRevisionHisotry = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            function showSpecificationRevision(req) {
                $state.go('app.rm.specifications.details', {specId: req.id})
            }

            (function () {
                loadSpecificationRevisionHistory();
            })();
        }
    }
);
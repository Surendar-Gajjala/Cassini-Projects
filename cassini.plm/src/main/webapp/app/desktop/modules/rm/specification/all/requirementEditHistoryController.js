define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/requirementsTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('RequirementEditHistoryController', RequirementEditHistoryController);

        function RequirementEditHistoryController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies,
                                                  CommonService, RequirementsTypeService) {
            var vm = this;
            vm.requirement = $scope.data.requirementEditDetails;
            var parsed = angular.element("<div></div>");

            vm.noEdits = parsed.html($translate.instant("NO_EDITS")).html();

            function loadRequirementEditHistory() {
                RequirementsTypeService.getRequirementEditHistory(vm.requirement.id).then(
                    function (data) {
                        vm.entryEditHistories = data;
                    }
                )
            }

            (function () {
                loadRequirementEditHistory();

            })();
        }
    }
);
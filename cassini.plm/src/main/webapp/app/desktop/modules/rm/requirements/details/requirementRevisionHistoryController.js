define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/requirementsTypeService'
    ],
    function (module) {
        module.controller('RequirementRevisionHistoryController', RequirementRevisionHistoryController);

        function RequirementRevisionHistoryController($scope, $rootScope, $timeout, $translate, $state, $stateParams, $cookies,
                                                      SpecificationsService, CommonService, RequirementsTypeService) {
            var vm = this;

            vm.reqId = $scope.data.reqId;
            vm.requirementRevisionHisotry = [];
            var parsed = angular.element("<div></div>");

            vm.showVersionDetails = showVersionDetails;

            var detailsTitle = parsed.html($translate.instant("DETAILS")).html();
            vm.showEditHistoryDetailsTitle = parsed.html($translate.instant("EDIT_DETAILS")).html();

            function loadRequirementVersionHistory() {
                RequirementsTypeService.getRequirementVersionHistory(vm.reqId).then(
                    function (data) {
                        vm.requirementRevisionHisotry = data;
                        angular.forEach(vm.requirementRevisionHisotry, function (obj) {
                            if (obj.createdDate) {
                                obj.createdDatede = moment(obj.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                        });
                        CommonService.getPersonReferences(vm.requirementRevisionHisotry, 'createdBy');

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function showVersionDetails(req) {
                $state.go('app.rm.requirements.details', {requirementId: req.id})
            }

            (function () {
                loadRequirementVersionHistory();
            })();
        }

    });
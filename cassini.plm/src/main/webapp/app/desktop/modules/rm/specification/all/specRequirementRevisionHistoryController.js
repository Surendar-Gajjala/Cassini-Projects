define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/requirementsTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('SpecRequirementRevisionHistoryController', SpecRequirementRevisionHistoryController);

        function SpecRequirementRevisionHistoryController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies,
                                                          RequirementsTypeService, CommonService) {
            var vm = this;

            vm.requirement = $scope.data.requirementId;
            vm.requirementRevisionHisotry = [];
            var parsed = angular.element("<div></div>");

            vm.showRequirementVersionDetails = showRequirementVersionDetails;

            var detailsTitle = parsed.html($translate.instant("DETAILS")).html();
            vm.showEditHistoryDetailsTitle = parsed.html($translate.instant("EDIT_DETAILS")).html();
            vm.showEditHistoryTitle = parsed.html($translate.instant("EDIT_DETAILS")).html();
            vm.showVersionDetails = parsed.html($translate.instant("REQUIREMENT_VERSION_DETAILS")).html();

            function loadRequirementVersionHistory() {
                RequirementsTypeService.getRequirementVersionHistory(vm.requirement.id).then(
                    function (data) {
                        vm.requirementRevisionHisotry = data;
                        CommonService.getPersonReferences(vm.requirementRevisionHisotry, 'createdBy');

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showEditHistory = showEditHistory;
            function showEditHistory(requirement) {
                var options = {
                    title: " Version : " + requirement.version + " " + detailsTitle,
                    template: 'app/desktop/modules/rm/specification/all/requirementEditHistoryView.jsp',
                    controller: 'RequirementEditHistoryController as requirementEditHistoryVm',
                    resolve: 'app/desktop/modules/rm/specification/all/requirementEditHistoryController',
                    width: 700,
                    side: 'left',
                    data: {
                        requirementEditDetails: requirement
                    },
                    buttons: [],
                    callback: function () {
                        $rootScope.showSuccessMessage(entryUpdateMsg);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function showRequirementVersionDetails(req) {
                $state.go('app.rm.requirements.details', {requirementId: req.id})
            }

            (function () {
                loadRequirementVersionHistory();
            })();
        }
    }
);
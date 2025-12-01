define(
    [
        'app/desktop/modules/rm/requirements/requirement.module',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('RequirementsBasicInfoController', RequirementsBasicInfoController);

        function RequirementsBasicInfoController($scope, $stateParams, $rootScope, $timeout, $sce, $state, $translate, LoginService,
                                                 CommonService, ObjectTypeAttributeService, ObjectAttributeService, AttributeAttachmentService, DialogService, SpecificationsService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            vm.changeFinishDate = changeFinishDate;
            vm.editPlannedDate = false;
            vm.cancelFinishDate = cancelFinishDate;
            vm.updateRequirement = updateRequirement;
            $rootScope.finishRequirement = finishRequirement;
            var reqId = $stateParams.requirementId;
            $rootScope.demoteRequirement = demoteRequirement;
            $rootScope.promoteRequirement = promoteRequirement;
            $rootScope.reviseRequirement = reviseRequirement;
            $scope.description=null;
            vm.editAssignedTo = false;

            function changeFinishDate() {
                vm.editPlannedDate = true;
            }

            function cancelFinishDate() {
                vm.editPlannedDate = false;
                $rootScope.loadRequirement();
            }

            /*----------- Promote And Demote Requirement ------*/
            var statusUpdateMsg = parsed.html($translate.instant("STATUS_UPDATE_MSG")).html();
            var newRevisionCreateMsg = parsed.html($translate.instant("NEW_TERMINOLOGY_MSG")).html();
            var requirementUpdateMsg = parsed.html($translate.instant("REQUIREMENT_UPDATE")).html();
            var PlannedfFinishDateSuccessfully = parsed.html($translate.instant("PLANNED_FINISH_DATE_DELETE_MSG")).html();

            var AssignedToDeleteSuccessfully = parsed.html($translate.instant("ASSIGNED_TO_DELETE_MSG")).html();
            var NameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            var descriptionValidation = parsed.html($translate.instant("DESCRIPTION_NOT_EMPTY")).html();
            var finishReqMsg = parsed.html($translate.instant("FINISH_REQ_MSG")).html();

            function promoteRequirement() {
                SpecificationsService.promoteRequirement(reqId).then(
                    function (data) {
                        $rootScope.loadRequirement();
                        $rootScope.showSuccessMessage(statusUpdateMsg);
                    }, function (error) {
                          $rootScope.showErrorMessage(error.message);
                          $rootScope.hideBusyIndicator();
                     }
                )
            }

            function demoteRequirement() {
                SpecificationsService.demoteRequirement(reqId).then(
                    function (data) {
                        $rootScope.loadRequirement();
                        $rootScope.showSuccessMessage(statusUpdateMsg);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function reviseRequirement() {
                SpecificationsService.reviseRequirement(reqId).then(
                    function (data) {
                        $rootScope.selectedRequirement = data;
                        $rootScope.showSuccessMessage(newRevisionCreateMsg + " : " + $rootScope.selectedRequirement.revision);
                        $state.go('app.rm.requirements.details', {requirementId: $rootScope.selectedRequirement.id})
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.deletePlannedFinishDate = deletePlannedFinishDate;
            function deletePlannedFinishDate() {
                $rootScope.selectedRequirement.plannedFinishDate = null;
                SpecificationsService.updateRequirement($rootScope.selectedRequirement).then(
                    function (data) {
                        $rootScope.loadRequirement();
                        vm.editPlannedDate = false;
                        $rootScope.showSuccessMessage(PlannedfFinishDateSuccessfully);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.deleteAssignedTo = deleteAssignedTo;
            function deleteAssignedTo() {
                $rootScope.selectedRequirement.assignedTo = null;
                SpecificationsService.updateRequirement($rootScope.selectedRequirement).then(
                    function (data) {
                        //$rootScope.loadRequirement();
                        $rootScope.selectedRequirement.assignedTo = null;
                        $rootScope.showSuccessMessage(AssignedToDeleteSuccessfully);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function updateRequirement() {
                if (validate()) {
                    SpecificationsService.updateRequirement($rootScope.selectedRequirement).then(
                        function (data) {
                            $rootScope.loadRequirement();
                            vm.editPlannedDate = false;
                            vm.editAssignedTo = false;
                            $rootScope.showSuccessMessage(requirementUpdateMsg);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.editDescription = editDescription;
            vm.cancelEditDescription = cancelEditDescription;
            function editDescription(sel) {
                sel.editMode = true;
                // $rootScope.selectedRequirement.description = sel.description;
                $scope.summernoteDescription = sel.description;
                $timeout(function () {
                    $('.note-current-fontname').text('Arial');
                }, 1000);
            }

            function cancelEditDescription(sel) {
                sel.editMode = false;
                $rootScope.loadRequirement();
                //sel.value.richTextValue = $rootScope.richTextValue;
                /* $(".note-editor").hide();*/
            }

            function validate() {
                var valid = true;
                if ($rootScope.selectedRequirement.name == null || $rootScope.selectedRequirement.name == ""
                    || $rootScope.selectedRequirement.name == undefined) {
                    valid = false;
                    $rootScope.selectedRequirement.name = $rootScope.reqName;
                    $rootScope.showWarningMessage(NameValidation);

                }
                else if ($rootScope.selectedRequirement.description == null || $rootScope.selectedRequirement.description == ""
                    || $rootScope.selectedRequirement.description == undefined) {
                    valid = false;
                    $rootScope.selectedRequirement.description = $rootScope.description;
                    $rootScope.showWarningMessage(descriptionValidation);

                }
                return valid;
            }

            function finishRequirement() {
                SpecificationsService.finishRequirement($rootScope.selectedRequirement).then(
                    function (data) {
                        $rootScope.loadRequirement();
                        $rootScope.showSuccessMessage(finishReqMsg);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

            }

            vm.persons = [];
            function loadPersons() {
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.changeAssignedTo = changeAssignedTo;
            vm.cancelAssignedTo = cancelAssignedTo;

            function changeAssignedTo() {
                vm.editAssignedTo = true;
            }

            function cancelAssignedTo() {
                vm.editAssignedTo = false;
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadPersons();
                //}
            })();
        }
    }
);
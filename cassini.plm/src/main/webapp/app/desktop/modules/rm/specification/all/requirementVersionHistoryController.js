define(
    [
        'app/desktop/modules/rm/rm.module',
        'jsdiff',
        'app/shared/services/core/requirementsTypeService',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module, JsDiff) {
        module.controller('RequirementVersionHistoryController', RequirementVersionHistoryController);

        function RequirementVersionHistoryController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $sce,
                                                     CommonService, RequirementsTypeService, SpecificationsService) {
            var vm = this;
            vm.entry = $scope.data.requirementId;
            vm.selectedMode = $scope.data.entryMode;

            var parsed = angular.element("<div></div>");
            vm.editEntryTitle = parsed.html($translate.instant("EDIT_ENTRY")).html();
            vm.entryDetailsTitle = parsed.html($translate.instant("ENTRY_DETAILS")).html();
            var entryUpdateMsg = parsed.html($translate.instant("ENTRY_UPDATE_MSG")).html();
            vm.clickOnVersionMsg = parsed.html($translate.instant("CLICK_ON_VERSION")).html();
            var detailsTitle = parsed.html($translate.instant("DETAILS")).html();
            var entryAcceptedMsg = parsed.html($translate.instant("REQUIREMENT_EDIT_ACCEPTED_MESSAGE")).html();
            var entryRejectedMsg = parsed.html($translate.instant("REQUIREMENT_REJECTED_MESSAGE")).html();
            vm.clickToAccept = parsed.html($translate.instant("CLICK_TO_ACCEPT")).html();
            vm.clickToReject = parsed.html($translate.instant("CLICK_TO_REJECT")).html();
            vm.acceptAsFinal = parsed.html($translate.instant("ACCEPT_AS_FINAL")).html();
            vm.showEditHistoryTitle = parsed.html($translate.instant("SHOW_ENTRY_EDIT_HISTORY")).html();
            var finalAccepted = parsed.html($translate.instant("REQUIREMENT_FINAL_ACCEPTED")).html();
            vm.noEdits = parsed.html($translate.instant("NO_EDITS")).html();

            vm.showAcceptButton = true;
            vm.atleastOneAccepted = false;
            vm.entryName = null;
            function loadEntryEditHistory() {
                RequirementsTypeService.getLastAcceptedRequirementEdit(vm.entry.requirement.id).then(
                    function (data) {
                        vm.lastAcceptedEntryEdit = data;
                        $rootScope.lastAcceptedRequirement = data;
                        RequirementsTypeService.getRequirementEditHistory(vm.entry.requirement.id).then(
                            function (data) {
                                vm.entryEditHistories = data;
                                if (data.length > 0) {
                                    vm.entryName = data[0].editedName;
                                    vm.entryDescription = data[0].editedDescription;
                                }
                                vm.showAcceptButton = true;
                                angular.forEach(vm.entryEditHistories, function (entryEdit) {
                                    if (vm.lastAcceptedEntryEdit != null && vm.lastAcceptedEntryEdit != ""
                                        && entryEdit.id == vm.lastAcceptedEntryEdit.id) {
                                        entryEdit.lastAcceptedEdit = true;
                                    }
                                    if (entryEdit.status == "NEW") {
                                        vm.showAcceptButton = false;
                                    }

                                    if (entryEdit.status == "ACCEPTED") {
                                        vm.atleastOneAccepted = true;
                                    }
                                });
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }, function (error) {
                          $rootScope.showErrorMessage(error.message);
                          $rootScope.hideBusyIndicator();
                     }
                );
            }

            vm.acceptEntryEdit = acceptEntryEdit;
            vm.rejectEntryEdit = rejectEntryEdit;
            vm.acceptEntry = acceptEntry;

            function acceptEntryEdit(entryEdit) {
                RequirementsTypeService.acceptRequirementEditChange(entryEdit).then(
                    function (data) {
                        entryEdit.status = data.status;
                        $rootScope.showSuccessMessage(entryAcceptedMsg);
                        $rootScope.loadRequirementVersions();
                        loadEntryEditHistory();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function rejectEntryEdit(entryEdit) {
                RequirementsTypeService.rejectRequirementEditChange(entryEdit).then(
                    function (data) {
                        entryEdit.status = data.status;
                        $rootScope.showSuccessMessage(entryRejectedMsg);
                        loadEntryEditHistory();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function acceptEntry(entryEdit) {
                RequirementsTypeService.approveRequirementEdits(vm.entry.id, entryEdit).then(
                    function (data) {
                        vm.showAcceptButton = false;
                        $scope.callback(data);
                        $rootScope.showSuccessMessage(entryAcceptedMsg);
                        $rootScope.loadRequirementVersions();
                        $rootScope.hideSidePanel();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.getEditedDescriptionDiff = getEditedDescriptionDiff;

            function getEditedDescriptionDiff(edit) {

                var diff = JsDiff.diffChars(vm.entry.requirement.description, edit.editedDescription);

                var placeholder = document.createElement("p");
                placeholder.style.display = "none";
                var fragment = document.createDocumentFragment();

                diff.forEach(function (part) {
                    // green for additions, red for deletions
                    // grey for common parts
                    /*  span = document.createElement('span');
                     if (part.added) {
                     span.classList.add("text-edited");
                     span.classList.add("text-added");
                     }
                     else if (part.removed) {
                     span.classList.add("text-edited");
                     span.classList.add("text-removed");
                     }
                     span.appendChild(document.createTextNode(part.value));
                     fragment.appendChild(span);*/
                });

                /*placeholder.appendChild(fragment);*/
                edit.editDiff;

            }

            (function () {
                loadEntryEditHistory();
            })();
        }
    }
);
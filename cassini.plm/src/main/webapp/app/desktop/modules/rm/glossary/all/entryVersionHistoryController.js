define(
    [
        'app/desktop/modules/rm/rm.module',
        'jsdiff',
        'app/shared/services/core/glossaryService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module, JsDiff) {
        module.controller('EntryVersionHistoryController', EntryVersionHistoryController);

        function EntryVersionHistoryController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $sce,
                                               CommonService, GlossaryService) {
            var vm = this;
            vm.entry = $scope.data.glossaryEntryId;
            vm.selectedLanguage = $scope.data.selectedLanguage;
            vm.selectedMode = $scope.data.entryMode;
            vm.showVersionDetails = showVersionDetails;
            vm.showEditHistory = showEditHistory;

            var parsed = angular.element("<div></div>");
            vm.editEntryTitle = parsed.html($translate.instant("EDIT_ENTRY")).html();
            vm.entryDetailsTitle = parsed.html($translate.instant("ENTRY_DETAILS")).html();
            var entryUpdateMsg = parsed.html($translate.instant("ENTRY_UPDATE_MSG")).html();
            vm.clickOnVersionMsg = parsed.html($translate.instant("CLICK_ON_VERSION")).html();
            var detailsTitle = parsed.html($translate.instant("DETAILS")).html();
            var entryAcceptedMsg = parsed.html($translate.instant("ENTRY_ACCEPTED_MESSAGE")).html();
            var entryRejectedMsg = parsed.html($translate.instant("ENTRY_ACCEPTED_MESSAGE")).html();
            vm.clickToAccept = parsed.html($translate.instant("CLICK_TO_ACCEPT")).html();
            vm.clickToReject = parsed.html($translate.instant("CLICK_TO_REJECT")).html();
            vm.acceptAsFinal = parsed.html($translate.instant("ACCEPT_AS_FINAL")).html();
            vm.showEditHistoryTitle = parsed.html($translate.instant("SHOW_ENTRY_EDIT_HISTORY")).html();

            function loadEntryVersions() {
                GlossaryService.getEntryVersionsByLanguage(vm.entry.id, vm.selectedLanguage).then(
                    function (data) {
                        vm.entryVersions = data;
                        vm.entryName = vm.entryVersions[0].defaultDetail.name;
                        CommonService.getPersonReferences(vm.entryVersions, 'createdBy');
                        vm.existedEntries = true;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function showVersionDetails(versionEntry) {
                var options = {
                    title: " Version : " + versionEntry.version + " " + detailsTitle,
                    template: 'app/desktop/modules/rm/glossary/new/versionEntryDetailsView.jsp',
                    controller: 'VersionEntryDetailsController as versionEntryDetailsVm',
                    resolve: 'app/desktop/modules/rm/glossary/new/versionEntryDetailsController',
                    width: 600,
                    side: 'left',
                    data: {
                        glossaryEntryDetails: versionEntry
                    },
                    buttons: [],
                    callback: function () {
                        $rootScope.showSuccessMessage(entryUpdateMsg);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function showEditHistory(version) {
                var options = {
                    title: " Version : " + version.version + " " + detailsTitle,
                    template: 'app/desktop/modules/rm/glossary/all/entryVersionEditHistoryView.jsp',
                    controller: 'EntryVersionEditHistoryController as entryVersionEditHistoryVm',
                    resolve: 'app/desktop/modules/rm/glossary/all/entryVersionEditHistoryController',
                    width: 700,
                    side: 'left',
                    data: {
                        glossaryEntryDetails: version,
                        language: vm.selectedLanguage
                    },
                    buttons: [],
                    callback: function () {
                        $rootScope.showSuccessMessage(entryUpdateMsg);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.showAcceptButton = true;
            vm.atleastOneAccepted = false;

            function loadEntryEditHistory() {
                GlossaryService.getLastAcceptedEntryEdit(vm.entry.id, vm.selectedLanguage).then(
                    function (data) {
                        vm.lastAcceptedEntryEdit = data;
                        GlossaryService.getEntryEditHistory(vm.entry.id, vm.selectedLanguage).then(
                            function (data) {
                                vm.entryName = vm.entry.defaultDetail.name;
                                vm.entryEditHistories = data;
                                vm.showAcceptButton = true;
                                angular.forEach(vm.entryEditHistories, function (entryEdit) {
                                    if (vm.lastAcceptedEntryEdit != null && vm.lastAcceptedEntryEdit != ""
                                        && entryEdit.id == vm.lastAcceptedEntryEdit.id) {
                                        entryEdit.lastAcceptedEdit = true;
                                    }
                                    if (entryEdit.status == "NONE") {
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
                GlossaryService.acceptEntryEditChange(vm.entry.id, entryEdit).then(
                    function (data) {
                        entryEdit.status = data.status;
                        $rootScope.showSuccessMessage(entryAcceptedMsg);
                        loadEntryEditHistory();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function rejectEntryEdit(entryEdit) {
                GlossaryService.rejectEntryEditChange(vm.entry.id, entryEdit).then(
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

            function acceptEntry() {
                GlossaryService.approveEntryEdits($stateParams.glossaryId, vm.entry).then(
                    function (data) {
                        vm.showAcceptButton = false;
                        $rootScope.searchGlossaryEntryItems($rootScope.entrySearchTerm);
                        $rootScope.showSuccessMessage(entryUpdateMsg);
                        $rootScope.hideSidePanel();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.getEditedDescriptionDiff = getEditedDescriptionDiff;

            function getEditedDescriptionDiff(edit) {
                var diff = JsDiff.diffChars(vm.entry.defaultDetail.description, edit.editedDescription);

                var placeholder = document.createElement("p");
                placeholder.style.display = "none";
                var fragment = document.createDocumentFragment();

                diff.forEach(function (part) {
                    // green for additions, red for deletions
                    // grey for common parts
                    span = document.createElement('span');
                    if (part.added) {
                        span.classList.add("text-edited");
                        span.classList.add("text-added");
                    }
                    else if (part.removed) {
                        span.classList.add("text-edited");
                        span.classList.add("text-removed");
                    }
                    span.appendChild(document.createTextNode(part.value));
                    fragment.appendChild(span);
                });

                placeholder.appendChild(fragment)
                edit.editDiff;

            }

            (function () {
                if (vm.selectedMode == "EDIT_HISTORY") {
                    loadEntryEditHistory();
                }

                if (vm.selectedMode == "VERSION") {
                    loadEntryVersions();
                }

            })();
        }
    }
);
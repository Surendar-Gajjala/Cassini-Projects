define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/desktop/modules/rm/glossary/details/tabs/files/glossaryFilesController',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/glossaryService'
    ],
    function (module) {
        module.controller('GlossaryEntryItemController', GlossaryEntryItemController);

        function GlossaryEntryItemController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                             CommonService, DialogService, GlossaryService) {

            var vm = this;
            vm.loading = true;

            var glossaryId = $stateParams.glossaryId;
            $rootScope.addGlossaryEntries = addGlossaryEntries;
            vm.deleteEntryItem = deleteEntryItem;
            vm.showVersionHistory = showVersionHistory;
            vm.showEditHistory = showEditHistory;
            vm.editEntry = editEntry;

            var parsed = angular.element("<div></div>");

            var entriesAddedMsg = parsed.html($translate.instant("ENTRIES_ADDED_MSG")).html();
            var addEntryTitle = parsed.html($translate.instant("ADD_ENTRY")).html();
            var addTitle = parsed.html($translate.instant("ADD")).html();
            vm.deleteTitle = parsed.html($translate.instant("DELETE_ENTRY")).html();
            var deleteEntryItemTitle = parsed.html($translate.instant("DELETE_ENTRY_ITEM")).html();
            var deleteEntryItemDialogMessage = parsed.html($translate.instant("TERMINOLOGY_ITEM_DELETE_DIALOG_MSG")).html();
            var glossaryEntryItemDeletedMessage = parsed.html($translate.instant("TERMINOLOGY_ITEM_DELETED_MESSAGE")).html();
            vm.showVersionHistoryTitle = parsed.html($translate.instant("SHOW_VERSION_HISTORY")).html();
            vm.showEditHistoryTitle = parsed.html($translate.instant("SHOW_ENTRY_EDIT_HISTORY")).html();
            vm.editEntryTitle = parsed.html($translate.instant("EDIT_ENTRY")).html();
            vm.entryEditHistoryTitle = parsed.html($translate.instant("ENTRY_EDIT_HISTORY")).html();
            var entryUpdateMsg = parsed.html($translate.instant("ENTRY_UPDATE_MSG")).html();
            var updateTitle = parsed.html($translate.instant("UPDATE")).html();
            var versionHistory = parsed.html($translate.instant("VERSION_HISTORY")).html();
            var entryEditHistory = parsed.html($translate.instant("ENTRY_EDIT_HISTORY")).html();
            vm.addEntriesTitle = parsed.html($translate.instant("ADD_ENTRY")).html();

            function addGlossaryEntries() {
                var options = {
                    title: addEntryTitle,
                    template: 'app/desktop/modules/rm/glossary/details/tabs/entries/newGlossaryEntryView.jsp',
                    controller: 'NewGlossaryEntryController as newGlossaryEntryVm',
                    resolve: 'app/desktop/modules/rm/glossary/details/tabs/entries/newGlossaryEntryController',
                    width: 700,
                    showMask: true,
                    data: {
                        glossaryId: glossaryId
                    },
                    buttons: [
                        {text: addTitle, broadcast: 'app.glossary.entry.newEntry'}
                    ],
                    callback: function (data) {
                        $rootScope.loadGlossaryDetails();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            /*$rootScope.loadGlossaryEntryItems = loadGlossaryEntryItems;
             function loadGlossaryEntryItems() {
             GlossaryService.getGlossaryEntryItems(glossaryId).then(
             function (data) {
             vm.glossaryEntryItems = data;
             vm.loading = false;
             }
             )
             }*/
            vm.loading = false;

            function deleteEntryItem(entryItem) {
                var options = {
                    title: deleteEntryItemTitle,
                    message: deleteEntryItemDialogMessage + " [ " + entryItem.entry.defaultDetail.name + " ] " + " ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        GlossaryService.deleteGlossaryEntryItem(glossaryId, entryItem).then(
                            function (data) {
                                $rootScope.loadGlossaryDetails();
                                $rootScope.showSuccessMessage(glossaryEntryItemDeletedMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            function editEntry(entry) {
                var options = {
                    title: vm.editEntryTitle,
                    showMask: true,
                    template: 'app/desktop/modules/rm/glossary/details/tabs/entries/editEntryView.jsp',
                    controller: 'EditEntryController as editEntryVm',
                    resolve: 'app/desktop/modules/rm/glossary/details/tabs/entries/editEntryController',
                    width: 700,
                    data: {
                        glossaryEntryDetails: entry
                    },
                    buttons: [
                        {text: updateTitle, broadcast: 'app.glossary.entry.edit'}
                    ],
                    callback: function () {
                        $rootScope.showSuccessMessage(entryUpdateMsg);
                        $rootScope.searchGlossaryEntryItems($rootScope.entrySearchTerm);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function showVersionHistory(entry) {
                var options = {
                    title: versionHistory,
                    template: 'app/desktop/modules/rm/glossary/all/entryVersionHistoryView.jsp',
                    controller: 'EntryVersionHistoryController as entryVersionHistoryVm',
                    resolve: 'app/desktop/modules/rm/glossary/all/entryVersionHistoryController',
                    width: 600,
                    data: {
                        selectedLanguage: $rootScope.selectedLanguage,
                        glossaryEntryId: entry,
                        entryMode: "VERSION"
                    },
                    callback: function (msg) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function showEditHistory(entry) {
                var options = {
                    title: entryEditHistory,
                    template: 'app/desktop/modules/rm/glossary/all/entryVersionHistoryView.jsp',
                    controller: 'EntryVersionHistoryController as entryVersionHistoryVm',
                    resolve: 'app/desktop/modules/rm/glossary/all/entryVersionHistoryController',
                    width: 700,
                    data: {
                        selectedLanguage: $rootScope.selectedLanguage,
                        glossaryEntryId: entry,
                        entryMode: "EDIT_HISTORY"
                    },
                    callback: function (msg) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            (function () {
                $scope.$on('app.glossary.tabactivated', function (event, data) {
                    if (data.tabId == 'details.entries') {
                        $rootScope.loadSelectedGlossary();
                    }
                })
            })();

        }
    }
);
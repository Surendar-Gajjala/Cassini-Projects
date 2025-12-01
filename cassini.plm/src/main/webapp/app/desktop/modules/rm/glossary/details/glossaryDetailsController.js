define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/glossaryService',
        'app/desktop/modules/rm/glossary/details/tabs/basic/glossaryBasicController',
        'app/desktop/modules/rm/glossary/details/tabs/entries/glossaryEntryItemController',
        'app/desktop/modules/rm/glossary/details/tabs/attributes/glossaryAttributesController',
        'app/desktop/modules/rm/glossary/details/tabs/permissions/glossaryPermissionsController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService'

    ],
    function (module) {
        module.controller('GlossaryDetailsController', GlossaryDetailsController);

        function GlossaryDetailsController($scope, $rootScope, $timeout, $window, $sce, $translate, $state, $stateParams, $cookies, GlossaryService, CommonService,
                                           ItemService, ObjectTypeAttributeService, $application,
                                           AttributeAttachmentService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, CommentsService) {
            $rootScope.viewInfo.icon = "fa fa-book";
            $rootScope.viewInfo.showDetails = true;
            var vm = this;
            var glossaryId = $stateParams.glossaryId;
            vm.back = back;
            vm.glossaryDetailsTabActivated = glossaryDetailsTabActivated;
            vm.downloadFilesAsZip = downloadFilesAsZip;
            vm.addGlossaryFiles = addGlossaryFiles;
            vm.showRevisionHistory = showRevisionHistory;
            $rootScope.loadSelectedGlossary = loadSelectedGlossary;
            $rootScope.selectedTab = null;
            var lastSelectedTab = null;
            vm.active = 0;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "rowId",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            function back() {
                window.history.back();
            }

            $rootScope.showCopyGlossaryFilesToClipBoard = false;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;

            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var filesTitle = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var entriesTitle = parsed.html($translate.instant("ENTRIES")).html();
            vm.addFiles = parsed.html($translate.instant("ADD_FILES")).html();
            vm.glossaryEntryTab = parsed.html($translate.instant("GLOSSARY_ENTRY_TAB")).html();
            var revisionHistoryTitle = parsed.html($translate.instant("REVISION_HISTORY_TITLE")).html();
            vm.importTerminologyTitle = parsed.html($translate.instant("IMPORT_TERMINOLOGY_ENTRY_ITEMS")).html();
            vm.exportTerminologyTitle = parsed.html($translate.instant("EXPORT_TERMINOLOGY_ENTRY_ITEMS")).html();
            vm.printTerminologyTitle = parsed.html($translate.instant("PRINT_TERMINOLOGY_ENTRY_ITEMS")).html();
            vm.searchEntriesTitle = parsed.html($translate.instant("SEARCH_ENTRIES")).html();
            vm.clearSearchTitle = parsed.html($translate.instant("CLEAR_SEARCH")).html();
            vm.searchTitle = parsed.html($translate.instant("SEARCH")).html();
            vm.detailsShareTitle = parsed.html($translate.instant("DETAILS_SHARE_TITLE")).html();
            vm.refreshTitle = parsed.html($translate.instant("CLICK_TO_REFRESH")).html();
            var importTitle = parsed.html($translate.instant("IMPORT")).html();
            var permisionTabName = parsed.html($translate.instant("PERMISSIONS")).html();
            vm.promoteTerminology = parsed.html($translate.instant("TERMINOLOGY_PROMOTE")).html();
            vm.demoteTerminology = parsed.html($translate.instant("TERMINOLOGY_DEMOTE")).html();
            vm.entryAttributes = parsed.html($translate.instant("ENTRY_ATTRIBUTES")).html();
            vm.reviseTerminology = parsed.html($translate.instant("REVISE_TERMINOLOGY")).html();

            vm.active = 0;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/rm/glossary/details/tabs/basic/glossaryBasicView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: filesTitle,
                    template: 'app/desktop/modules/rm/glossary/details/tabs/attributes/glossaryAttributeView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTitle,
                    template: 'app/desktop/modules/rm/glossary/details/tabs/files/glossaryFilesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                entries: {
                    id: 'details.entries',
                    heading: entriesTitle,
                    template: 'app/desktop/modules/rm/glossary/details/tabs/entries/glossaryEntryItemView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                permissions: {
                    id: 'details.permissions',
                    heading: permisionTabName,
                    template: 'app/desktop/modules/rm/glossary/details/tabs/permissions/glossaryPermissionsView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                }
            };
            var tabId = $stateParams.tab;
            vm.refreshDetails = refreshDetails;


            vm.onClear = onClear;
            function onClear() {
                $scope.$broadcast('app.activity.tabActivated', {tabId: 'details.files'});
            }

            vm.freeTextSearch = freeTextSearch;
            var searchMode = null;
            var freeTextQuery = null;
            $rootScope.freeTextQuerys = null;
            function freeTextSearch(freeText) {
                searchMode = "freetext";
                $rootScope.freeTextQuerys = freeText;
                freeTextQuery = null;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    $scope.$broadcast('app.glossary.loadFiles', {name: freeText});
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    $scope.$broadcast('app.glossary.tabactivated', {tabId: 'details.files'});
                }
            }

            function refreshDetails() {
                $scope.$broadcast('app.glossary.tabactivated', {tabId: $rootScope.selectedTab.id});
                if ($rootScope.selectedTab.id == "details.basic") {
                    loadSelectedGlossary();
                }
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function addGlossaryFiles() {
                $scope.$broadcast('app.glossary.addFiles');
            }

            function glossaryDetailsTabActivated(tabId) {
                $state.transitionTo('app.rm.glossary.details', {
                    glossaryId: glossaryId,
                    tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    $scope.$broadcast('app.glossary.tabactivated', {tabId: tabId});

                }
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = vm.tabs[t];
                    }
                }

                return tab;
            }

            function showRevisionHistory() {
                var options = {
                    title: revisionHistoryTitle,
                    template: 'app/desktop/modules/rm/glossary/details/glossaryRevisionHistoryView.jsp',
                    controller: 'GlossaryRevisionHistoryController as glossaryRevisionHistoryVm',
                    resolve: 'app/desktop/modules/rm/glossary/details/glossaryRevisionHistoryController',
                    data: {
                        glossaryId: glossaryId
                    },
                    width: 700,
                    showMask: true,
                };

                $rootScope.showSidePanel(options);
            }

            /*document.getElementById("glossaryFile").onchange = function () {
             var file = document.getElementById("glossaryFile");
             vm.importFile = file.files;
             $rootScope.showBusyIndicator($('#glossaryDetailsId'));
             GlossaryService.importGlossaryEntryItems(glossaryId, vm.importFile).then(
             function (data) {
             $rootScope.loadGlossaryDetails();
             $rootScope.showSuccessMessage("File imported successfully");
             $rootScope.hideBusyIndicator();
             }, function (error) {
             $rootScope.hideBusyIndicator();
             $rootScope.showWarningMessage(error.message);
             }
             )
             };*/

            vm.importGlossaryEntryItemsDialog = importGlossaryEntryItemsDialog;
            function importGlossaryEntryItemsDialog() {
                var options = {
                    title: "Import Entries",
                    template: 'app/desktop/modules/rm/glossary/details/importEntryDialogView.jsp',
                    controller: 'ImportEntryDialogController as importEntryDialogVm',
                    resolve: 'app/desktop/modules/rm/glossary/details/importEntryDialogController',
                    width: 700,
                    data: {
                        glossaryId: glossaryId
                    },
                    buttons: [
                        {text: importTitle, broadcast: 'app.glossary.importGlossaryEntry'}
                    ],
                    callback: function (result) {
                        if ($rootScope.entrySearchTerm == null || $rootScope.entrySearchTerm == "") {
                            $rootScope.loadGlossaryDetails();
                        } else {
                            vm.entrySearchTerm = $rootScope.entrySearchTerm;
                            searchGlossaryEntryItems($rootScope.entrySearchTerm);
                        }
                    }

                };

                $rootScope.showSidePanel(options);
            }

            $rootScope.exportGlossaryEntries = exportGlossaryEntries;

            var initColumns = {
                "Name": {
                    "columnName": "Name",
                    "columnValue": null,
                    "columnType": "string"
                },
                "Description": {
                    "columnName": "Description",
                    "columnValue": null,
                    "columnType": "string"
                },
                "Version": {
                    "columnName": "Version",
                    "columnValue": null,
                    "columnType": "integer"
                },
                "Notes": {
                    "columnName": "Notes",
                    "columnValue": null,
                    "columnType": "string"
                }
            };

            var glossaryEntryHeaders = ["Name", "Description", "Version", "Notes"];

            function exportGlossaryEntries() {
                var exportRows = [];

                for (var i = 0; i < $rootScope.glossaryEntryItems.length; i++) {
                    var exportRwDetails = [];
                    var glossaryRow = $rootScope.glossaryEntryItems[i];
                    for (var j = 0; j < glossaryRow.entry.glossaryEntryDetails.length; j++) {
                        var entryDetail = glossaryRow.entry.glossaryEntryDetails[j];
                        var name = {
                            columnName: "Name",
                            columnValue: null,
                            columnType: "string"
                        };

                        name.columnValue = entryDetail.name;

                        var description = {
                            columnName: "Name",
                            columnValue: null,
                            columnType: "string"
                        };

                        description.columnValue = entryDetail.description;

                        var version = {
                            columnName: "Name",
                            columnValue: null,
                            columnType: "string"
                        };

                        version.columnValue = glossaryRow.entry.version;

                        var notes = {
                            columnName: "Name",
                            columnValue: null,
                            columnType: "string"
                        };

                        notes.columnValue = entryDetail.notes;

                        exportRwDetails.push(name);
                        exportRwDetails.push(description);
                        exportRwDetails.push(version);
                        exportRwDetails.push(notes);
                    }
                    var exporter = {
                        exportRowDetails: exportRwDetails
                    };
                    exportRows.push(exporter);
                }

                var exportObject = {
                    "exportRows": exportRows,
                    "fileName": $rootScope.selectedGlossary.defaultDetail.name + "_" + $rootScope.selectedGlossary.revision,
                    "headers": angular.copy(headers)
                };

                CommonService.exportReport("EXCEL", exportObject).then(
                    function (data) {
                        var url = "{0}//{1}//api/common/exports/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        window.open(url);
                        $timeout(function () {
                            window.close();
                        }, 2000);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.printGlossary = printGlossary;
            function printGlossary() {
                GlossaryService.printGlossaryByLanguage(glossaryId, $rootScope.selectedLanguage).then(
                    function (data) {
                        $rootScope.printdata = data;
                        var url = "{0}//{1}/api/rm/glossarys/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        /*launchUrl(url);*/
                        window.open(url);
                        $timeout(function () {
                            window.close();
                        }, 2000);
                        /*    $rootScope.showSuccessMessage("Report Exported successfully");*/

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadGlossaryEntries() {
                GlossaryService.getGlossaryEntryItems(glossaryId).then(
                    function (data) {
                        vm.glossaryItems = data;
                        vm.glossaryItems.sort(function (a, b) {
                            if (a.entry.defaultDetail.name < b.entry.defaultDetail.name) return -1;
                            if (a.entry.defaultDetail.name > b.entry.defaultDetail.name) return 1;
                            return 0;
                        });
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.selectedLanguage = null;

            $rootScope.selectedGlossaryLanguages = [];
            $rootScope.selectedGlossaryLanguage = null;

            $rootScope.applyLanguage = applyLanguage;

            function applyLanguage(detail) {
                $rootScope.selectedLanguage = detail.language.language;
                if (vm.entrySearchTerm == null || vm.entrySearchTerm == "") {
                    loadGlossaryDetails();
                } else {
                    vm.entrySearchTerm = $rootScope.entrySearchTerm;
                    searchGlossaryEntryItems($rootScope.entrySearchTerm);
                }
            }

            $rootScope.loadGlossaryDetails = loadGlossaryDetails;
            vm.entries = [];
            function loadGlossaryDetails() {
                $rootScope.showBusyIndicator($('#glossaryDetailsId'));
                GlossaryService.getGlossaryDetails(glossaryId, $rootScope.selectedLanguage).then(
                    function (data) {
                        $rootScope.selectedGlossaryLanguages = [];
                        $rootScope.selectedGlossary = data.glossary;
                        if ($rootScope.selectedGlossary.releasedDate)
                            $rootScope.selectedGlossary.releasedDatede = moment($rootScope.selectedGlossary.releasedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                        if ($rootScope.selectedGlossary.createdDate)
                            $rootScope.selectedGlossary.createdDatede = moment($rootScope.selectedGlossary.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                        $rootScope.glossaryEntryItems = data.entryItems;
                        angular.forEach($rootScope.selectedGlossary.glossaryDetails, function (detail) {
                            $rootScope.selectedGlossaryLanguages.push(detail);
                        });
                        $rootScope.selectedGlossaryLanguage = $rootScope.selectedGlossary.defaultDetail;
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div> <span class='item-rev'>Rev {1}</span>".
                                format($rootScope.selectedGlossary.defaultDetail.name,
                                $rootScope.selectedGlossary.revision);

                        setLifecycles();
                        $rootScope.viewInfo.description = $rootScope.selectedGlossary.description;
                        CommonService.getPersonReferences([$rootScope.selectedGlossary], 'createdBy');
                        loadExportHeaders();

                        angular.forEach($rootScope.glossaryEntryItems, function (entryItem) {
                            vm.entries.push(entryItem.entry);
                        });
                        CommonService.getPersonReferences(vm.entries, 'createdBy');
                        CommonService.getPersonReferences(vm.entries, 'modifiedBy');
                        loadGlossaryEntryItemsAttributes();
                        loadGlossaryCounts();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.loadGlossaryCounts = loadGlossaryCounts;
            function loadGlossaryCounts() {
                GlossaryService.getGlossaryCounts(glossaryId, $rootScope.selectedLanguage).then(
                    function (data) {
                        vm.glossaryCounts = data;

                        var filesTab = document.getElementById("files");
                        var entries = document.getElementById("entries");

                        filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.glossaryCounts.files);
                        entries.lastElementChild.innerHTML = vm.tabs.entries.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.glossaryCounts.entries);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var headers = [];

            function loadExportHeaders() {
                headers = [];
                angular.forEach($rootScope.selectedGlossaryLanguages, function (detail) {
                    headers.push(detail.language.language + "_Name");
                    headers.push(detail.language.language + "_Description");
                    headers.push("Version");
                    headers.push(detail.language.language + "_Notes");
                })
            }

            function setLifecycles() {
                var phases = [];

                var arr = ['Preliminary', 'Review', 'Released'];
                angular.forEach(arr, function (def) {
                    phases.push({
                        name: def,
                        finished: false,
                        current: (def == $rootScope.selectedGlossary.lifeCyclePhase.phase)
                    })
                });

                var index = -1;
                for (var i = 0; i < phases.length; i++) {
                    if (phases[i].current == true) {
                        index = i;
                    }
                }

                if (index > 0) {
                    for (i = 0; i < index; i++) {
                        phases[i].finished = true;
                    }
                }

                $rootScope.setLifecyclePhases(phases);
            }

            function getStatusLabelStyle() {
                var type = $rootScope.selectedGlossary.lifeCyclePhase.phaseType;
                if (type == 'PRELIMINARY') {
                    return "label-warning";
                }
                else if (type == 'REVIEW') {
                    return "label-info";
                }
                else if (type == 'RELEASED') {
                    return "label-lightblue";
                }
                else if (type == 'OBSOLETE') {
                    return "label-danger";
                }
            }

            vm.resetEntrySearch = resetEntrySearch;
            $rootScope.searchGlossaryEntryItems = searchGlossaryEntryItems;
            vm.entrySearchTerm = "";

            function resetEntrySearch() {
                $rootScope.entrySearchTerm = "";
                vm.entrySearchTerm = "";
                $rootScope.searchModeType = false;
                $rootScope.loadGlossaryDetails();
            }

            function searchGlossaryEntryItems(freetext) {
                vm.entrySearchTerm = freetext;
                if (vm.entrySearchTerm != null && vm.entrySearchTerm != "" && vm.entrySearchTerm != undefined) {
                    $rootScope.entrySearchTerm = vm.entrySearchTerm;
                    $rootScope.showBusyIndicator($('#glossaryDetailsId'));
                    GlossaryService.glossaryEntrySearchItem(glossaryId, vm.entrySearchTerm, $rootScope.selectedLanguage).then(
                        function (data) {
                            $rootScope.searchModeType = true;
                            $rootScope.selectedGlossaryLanguages = [];
                            $rootScope.selectedGlossary = data.glossary;
                            $rootScope.glossaryEntryItems = data.entryItems;
                            angular.forEach($rootScope.selectedGlossary.glossaryDetails, function (detail) {
                                $rootScope.selectedGlossaryLanguages.push(detail);
                            });
                            $rootScope.selectedGlossaryLanguage = $rootScope.selectedGlossary.defaultDetail;
                            $rootScope.viewInfo.title = "<div class='item-number'>" +
                                "{0}</div> <span class='item-rev'>Rev {1}</span>".
                                    format($rootScope.selectedGlossary.defaultDetail.name,
                                    $rootScope.selectedGlossary.revision);

                            setLifecycles();
                            $rootScope.viewInfo.description = $rootScope.selectedGlossary.description;
                            CommonService.getPersonReferences([$rootScope.selectedGlossary], 'createdBy');
                            angular.forEach($rootScope.glossaryEntryItems, function (entryItem) {
                                vm.entries.push(entryItem.entry);
                            });
                            CommonService.getPersonReferences(vm.entries, 'createdBy');
                            CommonService.getPersonReferences(vm.entries, 'modifiedBy');
                            loadGlossaryEntryItemsAttributes();
                            loadExportHeaders();
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    $rootScope.loadGlossaryDetails();
                    $rootScope.entrySearchTerm = "";
                    $rootScope.searchModeType = false;
                }
            }

            function loadSelectedGlossary() {
                GlossaryService.getGlossary(glossaryId).then(
                    function (data) {
                        $rootScope.selectedLanguage = data.defaultLanguage.language;
                        $rootScope.selectGlossaryPermission = data.glossaryEntryPermission;
                        $rootScope.selectGlossaryLifeCyclePhase = data.lifeCyclePhase.phase;
                        loadGlossaryDetails();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            /* ------------- Glossary Custom properties ----------*/

            $rootScope.selectedAttributes = [];
            vm.objectIds = [];
            var currencyMap = new Hashtable();
            vm.showTypeAttributes = showTypeAttributes;
            $rootScope.removeAttribute = removeAttribute;

            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

            function showTypeAttributes() {
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/rm/glossary/details/tabs/entries/glossaryEntryItemTypeAttributesView.jsp',
                    resolve: 'app/desktop/modules/rm/glossary/details/tabs/entries/glossaryEntryItemTypeAttributesController',
                    controller: 'GlossarEntryTypeAttributesController as glossayEntryTypeAttributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: $rootScope.selectedAttributes
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        $rootScope.selectedAttributes = result;
                        $window.localStorage.setItem("allGlossaryEntryItemattributes", JSON.stringify($rootScope.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadSelectedGlossary();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                $rootScope.selectedAttributes.remove(att);
                $window.localStorage.setItem("allGlossaryEntryItemattributes", JSON.stringify($rootScope.selectedAttributes));
            }

            $rootScope.showImage = showImage;
            function showImage(attribute) {
                var modal = document.getElementById('myModal2');
                var modalImg = document.getElementById('img03');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            $('#myModalHorizontal').on('hidden', function () {
                $(this).data('modal').$element.removeData();
            })

            $rootScope.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                //launchUrl(url);
                window.open(url);
                $timeout(function () {
                    window.close();
                }, 2000);

            }

            /*    Show Modal dialogue for RichText*/
            $rootScope.showModal = showModal;
            function showModal(data) {
                $("#myModalHorizontal").show();
                var mymodal = $('#myModalHorizontal');
                $rootScope.modalValue = data
                mymodal.modal('show');
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("allGlossaryEntryItemattributes"));
                    JSON.parse($window.localStorage.getItem("lastSelectedGlossaryTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadGlossaryEntryItemsAttributes() {
                vm.projectIds = [];
                vm.attributeIds = [];
                CommonService.getPersonReferences($rootScope.glossaryEntryItems, 'modifiedBy');
                CommonService.getPersonReferences($rootScope.glossaryEntryItems, 'createdBy');
                angular.forEach($rootScope.glossaryEntryItems, function (entryItem) {
                    vm.projectIds.push(entryItem.entry.id);

                });
                angular.forEach($rootScope.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                if (vm.projectIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.projectIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach($rootScope.selectedAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach($rootScope.glossaryEntryItems, function (item) {
                                var attributes = [];
                                var revisionAttributes = vm.selectedObjectAttributes[item.latestRevision];
                                if (revisionAttributes != null && revisionAttributes != undefined) {
                                    attributes = attributes.concat(revisionAttributes);
                                }
                                var itemAttributes = vm.selectedObjectAttributes[item.entry.id];
                                if (itemAttributes != null && itemAttributes != undefined) {
                                    attributes = attributes.concat(itemAttributes);
                                }
                                angular.forEach(attributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.id;
                                        if (selectatt.dataType == 'TEXT') {
                                            item[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'LONGTEXT') {
                                            item[attributeName] = attribute.longTextValue;
                                        } else if (selectatt.dataType == 'RICHTEXT') {
                                            item[attributeName] = $sce.trustAsHtml(attribute.richTextValue);
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            item[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            item[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            item[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            item[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST') {
                                            if (attribute.listValue != null) {
                                                item[attributeName] = attribute.listValue;
                                            }
                                            else if (attribute.mlistValue.length > 0) {
                                                item[attributeName] = attribute.mlistValue;
                                            }
                                        } else if (selectatt.dataType == 'TIME') {
                                            item[attributeName] = attribute.timeValue;
                                        } else if (selectatt.dataType == 'TIMESTAMP') {
                                            item[attributeName] = attribute.timestampValue;
                                        } else if (selectatt.dataType == 'CURRENCY') {
                                            item[attributeName] = attribute.currencyValue;
                                            if (attribute.currencyType != null) {
                                                item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                            }
                                        } else if (selectatt.dataType == 'ATTACHMENT') {
                                            var revisionAttachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    revisionAttachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(revisionAttachmentIds).then(
                                                    function (data) {
                                                        vm.revisionAttachments = data;
                                                        item[attributeName] = vm.revisionAttachments;
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                     }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        } else if (selectatt.dataType == 'OBJECT') {
                                            if (selectatt.refType != null) {
                                                if (selectatt.refType == 'ITEM' && attribute.refValue != null) {
                                                    ItemService.getItem(attribute.refValue).then(
                                                        function (itemValue) {
                                                            item[attributeName] = itemValue;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (selectatt.refType == 'ITEMREVISION' && attribute.refValue != null) {
                                                    ItemService.getRevisionId(attribute.refValue).then(
                                                        function (revisionValue) {
                                                            item[attributeName] = revisionValue;
                                                            ItemService.getItem(revisionValue.itemMaster).then(
                                                                function (data) {
                                                                    item[attributeName].itemMaster = data.itemNumber;
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (selectatt.refType == 'CHANGE' && attribute.refValue != null) {
                                                    ECOService.getECO(attribute.refValue).then(
                                                        function (changeValue) {
                                                            item[attributeName] = changeValue;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (selectatt.refType == 'WORKFLOW' && attribute.refValue != null) {
                                                    WorkflowDefinitionService.getWorkflowDefinition(attribute.refValue).then(
                                                        function (workflowValue) {
                                                            item[attributeName] = workflowValue;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (selectatt.refType == 'MANUFACTURER' && attribute.refValue != null) {
                                                    MfrService.getManufacturer(attribute.refValue).then(
                                                        function (mfrValue) {
                                                            item[attributeName] = mfrValue;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (selectatt.refType == 'MANUFACTURERPART' && attribute.refValue != null) {
                                                    MfrPartsService.getManufacturepart(attribute.refValue).then(
                                                        function (mfrPartValue) {
                                                            item[attributeName] = mfrPartValue;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (selectatt.refType == 'PERSON' && attribute.refValue != null) {
                                                    CommonService.getPerson(attribute.refValue).then(
                                                        function (person) {
                                                            item[attributeName] = person;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                })
                            })

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('GLOSSARY', glossaryId).then(
                    function (data) {
                        $rootScope.showComments('GLOSSARY', glossaryId, data);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function downloadFilesAsZip() {
                $rootScope.showBusyIndicator($('.view-container'));
                var url = "{0}//{1}/api/rm/glossarys/{2}/files/zip".
                    format(window.location.protocol, window.location.host, glossaryId);

                //launchUrl(url);
                window.open(url);
                $rootScope.hideBusyIndicator();
            }

            $rootScope.selectedGlossary = null;
            function loadGlossary() {
                GlossaryService.getGlossary(glossaryId).then(
                    function (data) {
                        $rootScope.selectedGlossary = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadGlossary();
                loadCommentsCount();
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("allGlossaryEntryItemattributes"));
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedGlossaryTab"));
                }
                else {
                    var setAttributes = null;
                }

                if (setAttributes != null && setAttributes != undefined) {
                    angular.forEach(setAttributes, function (setAtt) {
                        if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                            vm.objectIds.push(setAtt.id);
                        }
                    });
                    ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                        function (data) {
                            if (data.length == 0) {
                                setAttributes = null;
                                $window.localStorage.setItem("allGlossaryEntryItemattributes", "");
                                $rootScope.selectedAttributes = setAttributes
                            } else {
                                $rootScope.selectedAttributes = setAttributes;
                            }
                            loadSelectedGlossary();

                        }, function (error) {
                              $rootScope.showErrorMessage(error.message);
                              $rootScope.hideBusyIndicator();
                         }
                    )
                }

                $window.localStorage.setItem("lastSelectedGlossaryTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    glossaryDetailsTabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.glossary.tabactivated', {tabId: lastSelectedTab});
                    }, 100)
                } else if (tabId != null && tabId != undefined) {
                    glossaryDetailsTabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.glossary.tabactivated', {tabId: tabId});
                    }, 1000)
                } else {
                    loadSelectedGlossary();
                }
            })();
        }
    }
)
;


define(['app/desktop/modules/rm/rm.module',
        'app/shared/services/core/glossaryService',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/folderService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective'

    ],
    function (module) {
        module.controller('GlossarysController', GlossarysController);

        function GlossarysController($scope, $rootScope, $translate, $sce, $timeout, $state, $window, $stateParams, $cookies, DialogService, $application,
                                     GlossaryService, CommonService, RecentlyVisitedService, ItemService, ObjectTypeAttributeService,
                                     AttributeAttachmentService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService) {
            $rootScope.viewInfo.icon = "fa fa-book";
            $rootScope.viewInfo.showDetails = false;
            var parsed = angular.element("<div></div>");
            var terminologyTitle = parsed.html($translate.instant("TERMINOLOGY")).html();
            var terminologyAddedToClipboard = parsed.html($translate.instant("TERMINOLOGY_ADDED_TO_CLIPBOARD")).html();
            $rootScope.viewInfo.title = terminologyTitle;

            var vm = this;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            var searchMode = null;
            vm.showNewGlossary = showNewGlossary;
            vm.deleteGlossary = deleteGlossary;
            vm.editGlossary = editGlossary;
            vm.showNewEntry = showNewEntry;
            //vm.deleteEntry = deleteEntry;
            vm.editEntry = editEntry;
            vm.showEntry = false;
            vm.showEntries = showEntries;
            vm.showGlossaries = showGlossaries;
            vm.showVersionHistory = showVersionHistory;
            vm.showRevisionHistory = showRevisionHistory;

            vm.search = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                objectType: 'GLOSSARY'
            };

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
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

            vm.projects = angular.copy(pagedResults);

            vm.deleteGlossaryTitle = parsed.html($translate.instant("DELETE_TERMINOLOGY")).html();
            vm.editGlossaryTitle = parsed.html($translate.instant("EDIT_TERMINOLOGY")).html();
            vm.glossaryOpenTitle = parsed.html($translate.instant("TERMINOLOGY_OPEN_TITLE")).html();
            vm.entryOpenTitle = parsed.html($translate.instant("ENTRY_OPEN_TITLE")).html();
            var deleteGlossaryDialogMessage = parsed.html($translate.instant("TERMINOLOGY_DELETE_DIALOG_MESSAGE")).html();
            var glossaryDeletedMessage = parsed.html($translate.instant("TERMINOLOGY_DELETED_MESSAGE")).html();
            var createTitle = parsed.html($translate.instant("CREATE")).html();
            var updateTitle = parsed.html($translate.instant("UPDATE")).html();
            var glossaryCreateMsg = parsed.html($translate.instant("TERMINOLOGY_CREATE_MSG")).html();
            var glossaryUpdateMsg = parsed.html($translate.instant("TERMINOLOGY_UPDATE_MSG")).html();
            var newGlossaryTitle = parsed.html($translate.instant("NEW_TERMINOLOGY")).html();

            vm.deleteEntryTitle = parsed.html($translate.instant("DELETE_ENTRY")).html();
            vm.editEntryTitle = parsed.html($translate.instant("EDIT_ENTRY")).html();
            vm.entryOpenTitle = parsed.html($translate.instant("ENTRY_OPEN_TITLE")).html();
            var deleteEntryDialogMessage = parsed.html($translate.instant("ENTRY_DELETE_DIALOG_MESSAGE")).html();
            var entryDeletedMessage = parsed.html($translate.instant("ENTRY_DELETED_MESSAGE")).html();
            var entryCreateMsg = parsed.html($translate.instant("ENTRY_CREATE_MSG")).html();
            var entryUpdateMsg = parsed.html($translate.instant("ENTRY_UPDATE_MSG")).html();
            var newEntryTitle = parsed.html($translate.instant("NEW_ENTRY")).html();

            vm.showGlossariesTitle = parsed.html($translate.instant("SHOW_TERMINOLOGIES_TITLE")).html();
            vm.showEntriesTitle = parsed.html($translate.instant("SHOW_ENTRIES_TITLE")).html();
            vm.showVersionHistoryTitle = parsed.html($translate.instant("SHOW_VERSION_HISTORY")).html();
            vm.showGlossaryPrintTitle = parsed.html($translate.instant("SHOW_TERMINOLOGY_PRINT")).html();
            var revisionHistoryTitle = parsed.html($translate.instant("REVISION_HISTORY_TITLE")).html();
            vm.showGlossaryAttributes = parsed.html($translate.instant("SHOW_GLOSSARY_ATTRIBUTE")).html();

            function resetPage() {
                vm.projects = angular.copy(pagedResults);
                vm.pageable.page = 0;
                $rootScope.showSearch = false;
                $rootScope.searchModeType = false;
                if (vm.showEntry == true) {
                    loadEntries();
                } else {
                    loadGlossaries();
                }
            }

            function nextPage() {
                if (vm.glossarys.last != true) {
                    vm.pageable.page++;

                    if ($scope.glossaryFreeTextSearchText != null && $scope.glossaryFreeTextSearchText != "") {
                        if (vm.showEntry == true) {
                            loadEntries();
                        } else {
                            GlossaryService.glossaryFreeTextSearch($scope.glossaryFreeTextSearchText, vm.pageable).then(
                                function (data) {
                                    vm.glossarys = data;
                                    vm.loading = false;
                                    $rootScope.showSearch = true;
                                    $rootScope.searchModeType = true;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }

                    }
                    else {
                        if (vm.showEntry == true) {
                            loadEntries();
                        } else {
                            loadGlossaries();
                        }
                    }

                }
            }

            function previousPage() {
                if (vm.glossarys.first != true) {
                    vm.pageable.page--;
                    if ($scope.glossaryFreeTextSearchText != null && $scope.glossaryFreeTextSearchText != "") {
                        if (vm.showEntry == true) {
                            loadEntries();
                        } else {
                            GlossaryService.glossaryFreeTextSearch($scope.glossaryFreeTextSearchText, vm.pageable).then(
                                function (data) {
                                    vm.glossarys = data;
                                    vm.loading = false;
                                    $rootScope.showSearch = true;
                                    $rootScope.searchModeType = true;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }

                    }
                    else {
                        if (vm.showEntry == true) {
                            loadEntries();
                        } else {
                            loadGlossaries();
                        }
                    }
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadGlossaries();
            }

            function loadGlossaries() {
                vm.clear = false;
                vm.loading = true;
                GlossaryService.getGlossarys(vm.pageable).then(
                    function (data) {
                        vm.glossarys = data;
                        angular.forEach(vm.glossarys.content, function (obj) {
                            if (obj.releasedDate) {
                                obj.releasedDatede = moment(obj.releasedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                        });
                        loadGlossaryAttributes();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function showNewGlossary() {
                var options = {
                    title: newGlossaryTitle,
                    showMask: true,
                    template: 'app/desktop/modules/rm/glossary/new/newGlossaryView.jsp',
                    controller: 'NewGlossaryController as newGlossaryVm',
                    resolve: 'app/desktop/modules/rm/glossary/new/newGlossaryController',
                    width: 700,
                    data: {
                        glossaryMode: "NEW",
                        glossaryDetails: null
                    },
                    buttons: [
                        {text: createTitle, broadcast: 'app.glossary.new'}
                    ],
                    callback: function () {
                        $rootScope.showSuccessMessage(glossaryCreateMsg);
                        loadGlossaries();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function editGlossary(glossary) {
                var options = {
                    title: vm.editGlossaryTitle,
                    showMask: true,
                    template: 'app/desktop/modules/rm/glossary/new/newGlossaryView.jsp',
                    controller: 'NewGlossaryController as newGlossaryVm',
                    resolve: 'app/desktop/modules/rm/glossary/new/newGlossaryController',
                    width: 700,
                    data: {
                        glossaryMode: "EDIT",
                        glossaryDetails: glossary
                    },
                    buttons: [
                        {text: updateTitle, broadcast: 'app.glossary.new'}
                    ],
                    callback: function () {
                        $rootScope.showSuccessMessage(glossaryUpdateMsg);
                        loadGlossaries();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            vm.openGlossaryDetails = openGlossaryDetails;
            function openGlossaryDetails(glossary) {
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                $state.go('app.rm.glossary.details', {glossaryId: glossary.id})
                vm.recentlyVisited.objectId = glossary.id;
                vm.recentlyVisited.objectType = glossary.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )

            }

            function freeTextSearch(freeText) {
                searchMode = "freetext";
                $scope.freeTextQuery = freeText;
                vm.searchText = freeText;
                $rootScope.glossaryFreeTextSearchText = freeText;
                vm.pageable.page = 0;
                if (vm.showEntry == false) {
                    if (freeText != null && freeText != undefined && freeText.trim() != "") {
                        GlossaryService.glossaryFreeTextSearch(freeText, vm.pageable).then(
                            function (data) {
                                vm.glossarys = data;
                                vm.loading = false;
                                $rootScope.showSearch = true;
                                $rootScope.searchModeType = true;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                    else {
                        vm.resetPage();
                    }
                } else {
                    if (freeText != null && freeText != undefined && freeText.trim() != "") {
                        GlossaryService.glossaryEntryFreeTextSearch(freeText, vm.pageable).then(
                            function (data) {
                                vm.glossaryEntries = data;
                                vm.loading = false;
                                $rootScope.showSearch = true;
                                $rootScope.searchModeType = true;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                    else {
                        vm.resetPage();
                        $rootScope.glossaryFreeTextSearchText = null;
                    }
                }
            }

            function deleteGlossary(glossary) {
                var options = {
                    title: vm.deleteGlossaryTitle,
                    message: deleteGlossaryDialogMessage + " [ " + glossary.defaultDetail.name + " ] " + " ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        GlossaryService.deleteGlossary(glossary).then(
                            function (data) {
                                var index = vm.glossarys.content.indexOf(glossary);
                                vm.glossarys.content.splice(index, 1);
                                $rootScope.showSuccessMessage(glossaryDeletedMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            function showEntries() {
                $rootScope.viewInfo.title = "Entries";
                vm.loading = true;
                vm.showEntry = true;
                vm.pageable.page = 0;
                loadEntries();
            }

            function showGlossaries() {
                $rootScope.viewInfo.title = "Glossaries";
                vm.loading = true;
                vm.showEntry = false;
                vm.pageable.page = 0;
                loadGlossaries();
            }

            function loadEntries() {
                GlossaryService.getAllGlossaryEntries(vm.pageable).then(
                    function (data) {
                        vm.glossaryEntries = data;
                        vm.glossaryEntries.content.sort(function (a, b) {
                            if (a.name < b.name) return -1;
                            if (a.name > b.name) return 1;
                            return 0;
                        });
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function showNewEntry() {
                var options = {
                    title: newEntryTitle,
                    showMask: true,
                    template: 'app/desktop/modules/rm/glossary/new/newEntryView.jsp',
                    controller: 'NewEntryController as newEntryVm',
                    resolve: 'app/desktop/modules/rm/glossary/new/newEntryController',
                    width: 700,
                    data: {
                        glossaryEntryMode: "NEW",
                        glossaryEntryDetails: null
                    },
                    buttons: [
                        {text: createTitle, broadcast: 'app.glossary.entry.new'}
                    ],
                    callback: function () {
                        $rootScope.showSuccessMessage(entryCreateMsg);
                        loadEntries();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function editEntry(entry) {
                var options = {
                    title: vm.editEntryTitle,
                    showMask: true,
                    template: 'app/desktop/modules/rm/glossary/new/newEntryView.jsp',
                    controller: 'NewEntryController as newEntryVm',
                    resolve: 'app/desktop/modules/rm/glossary/new/newEntryController',
                    width: 550,
                    data: {
                        glossaryEntryMode: "EDIT",
                        glossaryEntryDetails: entry
                    },
                    buttons: [
                        {text: updateTitle, broadcast: 'app.glossary.entry.new'}
                    ],
                    callback: function () {
                        $rootScope.showSuccessMessage(entryUpdateMsg);
                        loadEntries();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function showVersionHistory(entry) {
                var options = {
                    title: 'Version History',
                    template: 'app/desktop/modules/rm/glossary/all/entryVersionHistoryView.jsp',
                    controller: 'EntryVersionHistoryController as entryVersionHistoryVm',
                    resolve: 'app/desktop/modules/rm/glossary/all/entryVersionHistoryController',
                    data: {
                        glossaryEntryId: entry
                    },
                    callback: function (msg) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function showRevisionHistory(glossary) {
                var options = {
                    title: revisionHistoryTitle,
                    template: 'app/desktop/modules/rm/glossary/details/glossaryRevisionHistoryView.jsp',
                    controller: 'GlossaryRevisionHistoryController as glossaryRevisionHistoryVm',
                    resolve: 'app/desktop/modules/rm/glossary/details/glossaryRevisionHistoryController',
                    data: {
                        glossaryId: glossary.id
                    },
                    width: 700
                };

                $rootScope.showSidePanel(options);
            }

            vm.printGlossaryItem = printGlossaryItem;
            function printGlossaryItem(glossary) {
                GlossaryService.printGlossaryByLanguage(glossary.id, glossary.defaultDetail.language.language).then(
                    function (data) {
                        $rootScope.printdata = data;
                        var url = "{0}//{1}/api/rm/glossarys/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        window.open(url, '-self');
                        /*  launchUrl(url);*/
                        /*    $rootScope.showSuccessMessage("Report Exported successfully");*/

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.entryDetailsTitle = parsed.html($translate.instant("ENTRY_DETAILS")).html();
            vm.showEntryVersionDetails = showEntryVersionDetails;
            function showEntryVersionDetails(versionEntry) {
                var options = {
                    title: vm.entryDetailsTitle,
                    template: 'app/desktop/modules/rm/glossary/new/versionEntryDetailsView.jsp',
                    controller: 'VersionEntryDetailsController as versionEntryDetailsVm',
                    resolve: 'app/desktop/modules/rm/glossary/new/versionEntryDetailsController',
                    width: 600,
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

            /* ------------- Glossary Custom properties ----------*/

            vm.selectedAttributes = [];
            vm.objectIds = [];
            var currencyMap = new Hashtable();
            vm.showTypeAttributes = showTypeAttributes;
            vm.removeAttribute = removeAttribute;

            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

            function showTypeAttributes() {
                var selectedAttribute = angular.copy(vm.selectedAttributes);
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/rm/glossary/all/glossaryTypeAttributesView.jsp',
                    resolve: 'app/desktop/modules/rm/glossary/all/glossaryTypeAttributesController',
                    controller: 'GlossaryTypeAttributesController as glossayTypeAttributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: selectedAttribute
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("allGlossaryattributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadGlossaries();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("allGlossaryattributes", JSON.stringify(vm.selectedAttributes));
            }

            vm.showImage = showImage;
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

            /*--------- To Download ATTACHMENT Attribute File  --------------*/

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                window.open(url);
                // window.open(url);
                $timeout(function () {
                    window.close();
                }, 2000);
            }

            /*    Show Modal dialogue for RichText*/
            $scope.showModal = showModal;
            function showModal(data) {
                $("#myModalHorizontal").show();
                var mymodal = $('#myModalHorizontal');
                vm.modalValue = data
                mymodal.modal('show');
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("allGlossaryattributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadGlossaryAttributes() {
                vm.projectIds = [];
                vm.attributeIds = [];
                CommonService.getPersonReferences(vm.glossarys.content, 'modifiedBy');
                CommonService.getPersonReferences(vm.glossarys.content, 'createdBy');
                angular.forEach(vm.glossarys.content, function (item) {
                    vm.projectIds.push(item.id);

                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                if (vm.projectIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.projectIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.selectedAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.glossarys.content, function (item) {
                                var attributes = [];
                                var revisionAttributes = vm.selectedObjectAttributes[item.latestRevision];
                                if (revisionAttributes != null && revisionAttributes != undefined) {
                                    attributes = attributes.concat(revisionAttributes);
                                }
                                var itemAttributes = vm.selectedObjectAttributes[item.id];
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
                                        } else if (selectatt.dataType == 'HYPERLINK') {
                                            item[attributeName] = attribute.hyperLinkValue;
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

            vm.clipBoardGlossaries = $application.clipboard.deliverables.glossaryIds;
            vm.showCopyToClipBoard = false;

            vm.selectGlossary = selectGlossary;
            vm.copyToClipBoard = copyToClipBoard;
            vm.clearAndCopyToClipBoard = clearAndCopyToClipBoard;
            vm.selectedGlossaries = [];
            function copyToClipBoard() {
                angular.forEach(vm.selectedGlossaries, function (glossary) {
                    glossary.selected = false;
                    $application.clipboard.deliverables.glossaryIds.push(glossary.id);
                });

                vm.clipBoardGlossaries = $application.clipboard.deliverables.glossaryIds;
                $rootScope.clipBoardDeliverables.glossaryIds = $application.clipboard.deliverables.glossaryIds;
                vm.showCopyToClipBoard = false;
                vm.selectedGlossaries = [];
                $rootScope.showSuccessMessage(terminologyAddedToClipboard);
            }

            function clearAndCopyToClipBoard() {
                $application.clipboard.deliverables.glossaryIds = [];
                copyToClipBoard();
            }

            function selectGlossary(glossary) {
                if (glossary.selected) {
                    vm.selectedGlossaries.push(glossary);
                } else {
                    vm.selectedGlossaries.splice(vm.selectedGlossaries.indexOf(glossary), 1);
                }

                if (vm.selectedGlossaries.length > 0) {
                    vm.showCopyToClipBoard = true;
                } else {
                    vm.showCopyToClipBoard = false;
                }
            }


            vm.showThumbnailImage = showThumbnailImage;
            function showThumbnailImage(item) {
                var modal = document.getElementById('item-thumbnail' + item.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + item.id);
                $("#thumbnail-image" + item.id).width($('#thumbnail-view' + item.id).outerWidth());
                $("#thumbnail-image" + item.id).height($('#thumbnail-view' + item.id).outerHeight());

                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            (function () {
                //if ($application.homeLoaded == true) {
                vm.showEntry = false;

                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })

                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("allGlossaryattributes"));
                } else {
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
                                $window.localStorage.setItem("allGlossaryattributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadGlossaries();

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

                else {
                    loadGlossaries();
                }

                if ($rootScope.glossaryFreeTextSearchText == null) {
                    loadGlossaries();
                } else {
                    freeTextSearch($rootScope.glossaryFreeTextSearchText);
                }
                $window.localStorage.setItem("lastSelectedGlossaryTab", JSON.stringify('details.entries'));
                //}
            })();
        }
    }
)
;




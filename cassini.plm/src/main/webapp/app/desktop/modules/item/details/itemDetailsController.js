define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemBomService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/relatedItemService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/itemReferenceService',
        'app/shared/services/core/shareService',
        'app/desktop/modules/item/details/tabs/basic/itemBasicInfoController',
        'app/desktop/modules/item/details/tabs/quality/itemQualityController',
        'app/desktop/modules/item/details/tabs/variance/itemVarianceController',
        'app/desktop/modules/item/details/tabs/attributes/itemAttributesController',
        'app/desktop/modules/item/details/tabs/bom/itemBomController',
        'app/desktop/modules/item/details/tabs/configured/itemConfiguredController',
        'app/desktop/modules/item/details/tabs/whereused/itemWhereUsedController',
        'app/desktop/modules/item/details/tabs/changes/itemChangesController',
        'app/desktop/modules/item/details/tabs/files/itemFilesController',
        'app/desktop/modules/item/details/tabs/prs/itemPRsController',
        'app/desktop/modules/item/details/tabs/mfr/itemMfrController',
        'app/desktop/modules/item/details/tabs/workflow/itemWorkflowController',
        'app/desktop/modules/item/details/tabs/references/itemReferencesController',
        'app/desktop/modules/item/details/itemsSelectionController',
        'app/desktop/modules/item/details/itemsSelectionController',
        'app/desktop/modules/item/details/selectMfrItemController',
        'app/desktop/modules/item/details/itemSelectionController',
        'app/desktop/modules/item/details/tabs/relatedItems/relatedItemsController',
        'app/desktop/modules/item/details/tabs/projectItem/projectItemController',
        'app/desktop/modules/item/details/tabs/inspections/itemInspectionController',
        'app/desktop/modules/item/details/tabs/requirements/itemRequirementsController',
        'app/desktop/modules/item/details/tabs/specifications/itemSpecificationsController',
        'app/desktop/modules/item/details/tabs/timelineHistory/itemTimelineHistoryController',
        'app/desktop/modules/item/details/tabs/basic/bomItemsCompareController',
        'app/desktop/modules/item/details/tabs/basic/itemToItemController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/directives/dynamic-controller/dynamicCtrl',
        'app/desktop/modules/item/details/customExtensionController',
        'app/desktop/directives/plugin-directive/pluginTabsDirective',
        'app/desktop/directives/plugin-directive/pluginActionDirective'
        //'app/mxgraph/mxClient'

    ],
    function (module) {
        module.controller('ItemDetailsController', ItemDetailsController);

        function ItemDetailsController($scope, $rootScope, $timeout, $interval, $window, $state, $location, $stateParams, $cookies, $translate, $application, $injector,
                                       CommonService, ItemTypeService, ECOService, ItemService, ItemBomService, MfrPartsService, DialogService,
                                       ItemReferenceService, $uibModal, RelatedItemService, ItemFileService, ShareService, CommentsService) {

            $rootScope.viewInfo.icon = "fa fa-th";
            $rootScope.viewInfo.title = $translate.instant("ITEM_DETAILS");
            $rootScope.viewInfo.showDetails = true;


            var vm = this;

            vm.itemId = $stateParams.itemId;
            vm.showItemRevisionHistory = showItemRevisionHistory;
            vm.bomCompare = bomCompare;
            vm.showTypeAttributes = showTypeAttributes;
            vm.copyItem = copyItem;
            vm.shareItem = shareItem;
            vm.freeTextSearch = freeTextSearch;
            vm.onClear = onClear;
            vm.refreshDetails = refreshDetails;
            vm.searchTerm = null;

            vm.customActions = [];
            vm.customActionGroups = [];
            vm.customTabs = [];
            vm.tabId = $stateParams.tab;
            $rootScope.sharedPermission = null;
            var emptyItem = {
                id: null,
                type: 'AFFECTED',
                change: null,
                itemObject: {
                    id: null,
                    itemType: null,
                    itemNumber: null,
                    description: null,
                    revision: null,
                    status: null
                },
                fromRevision: null,
                fromLifeCycle: null,
                toRevision: null,
                toLifeCycle: null,
                notes: null,
                toLifecycles: []
            };

            vm.bomItems = [];
            $rootScope.showCopyItemFilesToClipBoard = false;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            $rootScope.bomLatestState = false;


            var parsed = angular.element("<div></div>");

            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attribute = parsed.html($translate.instant("ATTRIBUTE")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var bom = parsed.html($translate.instant("ITEM_DETAILS_TAB_BOM")).html();
            var changes = parsed.html($translate.instant("ITEM_DETAILS_TAB_CHANGES")).html();
            var quality = parsed.html($translate.instant("QUALITY")).html();
            var variance = parsed.html($translate.instant("VARIANCES")).html();
            var whereUsed = parsed.html($translate.instant("DETAILS_TAB_WHERE_USED")).html();
            vm.addFiles = parsed.html($translate.instant("DETAILS_ADD_FILES")).html();
            var relatedItems = parsed.html($translate.instant("RELATED_ITEMS")).html();
            var relationships = parsed.html($translate.instant("RELATIONSHIPS")).html();
            var mfrParts = parsed.html($translate.instant("ITEM_DETAILS_TAB_MANUFACTURER_PARTS")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var itemTitle = parsed.html($translate.instant("ITEM")).html();
            var unsubscribeMsg = parsed.html($translate.instant("UN_SUBSCRIBE_MSG")).html();
            var subscribeMsg = parsed.html($translate.instant("SUBSCRIBE_MSG")).html();
            var subscribeTitle = parsed.html($translate.instant("SUBSCRIBE_TITLE")).html();
            var unsubscribeTitle = parsed.html($translate.instant("UN_SUBSCRIBE_TITLE")).html();
            var projects = parsed.html($translate.instant("PROJECTS")).html();
            var requirements = parsed.html($translate.instant("REQUIREMENTS")).html();
            var configured = parsed.html($translate.instant("ITEM_DETAILS_TAB_CONFIGURED")).html();
            var instance = parsed.html($translate.instant("ITEM_DETAILS_TAB_INSTANCE")).html();
            vm.getCombination = parsed.html($translate.instant("GET_ITEM_COMBINATIONS")).html();
            vm.saveCombination = parsed.html($translate.instant("SAVE_ITEM_COMBINATIONS")).html();
            vm.bomItemAttributes = parsed.html($translate.instant("BOM_ITEM_ATTRIBUTES")).html();
            vm.bomConfigurations = parsed.html($translate.instant("BOM_CONFIGURATIONS")).html();
            vm.bomItemExport = parsed.html($translate.instant("BOM_EXPORT")).html();
            vm.bomItemImport = parsed.html($translate.instant("BOM_IMPORT")).html();
            vm.promoteItemTitle = parsed.html($translate.instant("PROMOTE_ITEM_LIFECYCLE_PHASE")).html();
            vm.makeRevisionAsAbsolete = parsed.html($translate.instant("MAKE_REVISION_AS_ABSOLETE")).html();
            vm.reviseItemTitle = parsed.html($translate.instant("REVISE_ITEM_TITLE")).html();
            vm.demoteItemTitle = parsed.html($translate.instant("DEMOTE_ITEM_LIFECYCLE_PHASE")).html();
            vm.backTitle = parsed.html($translate.instant("DETAILS_BACK")).html();
            vm.rulesTitle = parsed.html($translate.instant("RULES_TITLE")).html();
            vm.resloveBom = parsed.html($translate.instant("RESOLVE_BOM_ITEM")).html();
            $rootScope.bomConfigurationEditorTitle = parsed.html($translate.instant("BOM_CONFIGURATION_EDITOR")).html();
            $rootScope.bomRollupReportTitle = parsed.html($translate.instant("BOM_ROLL_UP_REPORT")).html();
            $rootScope.bomWhereUsedReportTitle = parsed.html($translate.instant("BOM_WHERE_USED_REPORT")).html();
            $rootScope.newBomConfigurationTitle = parsed.html($translate.instant("NEW_BOM_CONFIGURATION")).html();
            $rootScope.editorConfigurationsTitle = parsed.html($translate.instant("EDITOR_CONFIGURATIONS_TITLE")).html();
            $rootScope.addSpecification = parsed.html($translate.instant("ADD_SPECIFICATION")).html();
            $rootScope.complianceReport = parsed.html($translate.instant("COMPLIANCE_REPORT")).html();
            vm.promoteItemRevisionTitle = vm.promoteItemTitle;

            $rootScope.itemLifeCycleStatus = null;
            var lastSelectedTab = null;
            var lastSelectedWhereUsed = null;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemBasicInfoView.jsp',
                    index: 0,
                    active: false,
                    activated: false
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/item/details/tabs/attributes/itemAttributesView.jsp',
                 index: 1,
                 active: false,
                 activated: false,
                 controller: "ItemAttributesController as itemAttributesVm"
                 }*/,
                bom: {
                    id: 'details.bom',
                    heading: bom,
                    template: 'app/desktop/modules/item/details/tabs/bom/itemBomView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                configured: {
                    id: 'details.configured',
                    heading: instance,
                    template: 'app/desktop/modules/item/details/tabs/configured/itemConfiguredView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                whereUsed: {
                    id: 'details.whereUsed',
                    heading: whereUsed,
                    template: 'app/desktop/modules/item/details/tabs/whereused/itemWhereUsedView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                changes: {
                    id: 'details.changes',
                    heading: changes,
                    template: 'app/desktop/modules/item/details/tabs/changes/itemChangesView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                variance: {
                    id: 'details.variance',
                    heading: variance,
                    template: 'app/desktop/modules/item/details/tabs/variance/itemVarianceView.jsp',
                    index: 5,
                    active: false,
                    activated: false
                },
                quality: {
                    id: 'details.quality',
                    heading: quality,
                    template: 'app/desktop/modules/item/details/tabs/quality/itemQualityView.jsp',
                    index: 6,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/item/details/tabs/files/itemFilesView.jsp',
                    index: 7,
                    active: false,
                    activated: false
                },
                mfr: {
                    id: 'details.mfr',
                    heading: mfrParts,
                    template: 'app/desktop/modules/item/details/tabs/mfr/itemMfrsView.jsp',
                    index: 8,
                    active: false,
                    activated: false
                },
                relatedItems: {
                    id: 'details.relatedItems',
                    heading: relatedItems,
                    template: 'app/desktop/modules/item/details/tabs/relatedItems/relatedItemView.jsp',
                    index: 9,
                    active: false,
                    activated: false
                },
                projectItem: {
                    id: 'details.projectItem',
                    heading: projects,
                    template: 'app/desktop/modules/item/details/tabs/projectItem/projectItemView.jsp',
                    index: 10,
                    active: false,
                    activated: false
                },
                requirements: {
                    id: 'details.requirements',
                    heading: requirements,
                    template: 'app/desktop/modules/item/details/tabs/requirements/itemRequirementsView.jsp',
                    index: 11,
                    active: false,
                    activated: false
                },
                specifications: {
                    id: 'details.specifications',
                    heading: 'Compliance',
                    template: 'app/desktop/modules/item/details/tabs/specifications/itemSpecificationsView.jsp',
                    index: 12,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: 'Workflow',
                    template: 'app/desktop/modules/item/details/tabs/workflow/itemWorkflowView.jsp',
                    index: 13,
                    active: false,
                    activated: false
                },
                inspection: {
                    id: 'details.inspection',
                    heading: "Inspections",
                    template: 'app/desktop/modules/item/details/tabs/inspections/itemInspectionView.jsp',
                    index: 14,
                    active: false,
                    activated: false
                },
                itemTimelineHistory: {
                    id: 'details.itemTimelineHistory',
                    heading: "Timeline",
                    template: 'app/desktop/modules/item/details/tabs/timelineHistory/itemTimelineHistoryView.jsp',
                    index: 15,
                    active: false,
                    activated: false
                }

            };


            vm.active = -1;

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.items.details', {itemId: vm.itemId, tab: 'details.basic'}, {notify: false});
                vm.active = 0;
            }
            else {
                var tab = getTabById(tabId);
                if (tab == null) {
                    console.log("Tab not found: " + tabId);
                    //vm.active = tab.index;
                }
            }


            /*vm.createRelatedItem = createRelatedItem;*/
            $scope.tabActivated = itemDetailsTabActivated;
            vm.onAddItemFiles = onAddItemFiles;
            vm.tabLoaded = tabLoaded;
            vm.reviseItem = reviseItem;
            vm.broadcast = broadcast;
            vm.createEco = createEco;
            vm.selectWorkflow = selectWorkflow;
            vm.bomConfigs = [];

            var searchMode = null;
            var freeTextQuery = null;

            var copyItemTitle = parsed.html($translate.instant("COPY_ITEM")).html();
            var newECO = parsed.html($translate.instant("ECO_ALL_NEW_ECO")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();
            $rootScope.createRelatedItemButtonTitle = parsed.html($translate.instant("CREATE_RELATED_ITEM")).html();
            vm.detailsShareTitle = parsed.html($translate.instant("DETAILS_SHARE_TITLE")).html();
            vm.refreshTitle = parsed.html($translate.instant("CLICK_TO_REFRESH")).html();
            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();

            function selectWorkflow() {

            }

            function onClear() {
                if ($rootScope.selectedTab.id == 'details.files') {
                    $scope.$broadcast('app.item.tabactivated', {tabId: 'details.files'});
                } else {
                    $scope.$broadcast('app.item.searchBom', {name: null});
                }
            }

            $rootScope.freeTextQuerys = null;
            function freeTextSearch(freeText) {
                searchMode = "freetext";
                $rootScope.freeTextQuerys = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    if ($rootScope.selectedTab.id == 'details.files') {
                        $scope.$broadcast('app.details.files.search', {name: freeText});
                    } else {
                        $scope.$broadcast('app.item.searchBom', {name: freeText});
                    }
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    if ($rootScope.selectedTab.id == 'details.files') {
                        $scope.$broadcast('app.item.tabactivated', {tabId: 'details.files'});
                    } else {

                        $scope.$broadcast('app.item.searchBom', {name: null});
                    }
                }
            }

            function copyItem() {
                var options = {
                    title: copyItemTitle,
                    template: 'app/desktop/modules/item/new/itemCopyView.jsp',
                    controller: 'ItemCopyController as itemCopyVm',
                    resolve: 'app/desktop/modules/item/new/itemCopyController',
                    width: 600,
                    showMask: true,
                    data: {
                        itemData: vm.item
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.items.copy'}
                    ],
                    callback: function (item) {
                        ItemService.copyItem(item).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(vm.item.itemNumber + ' : Item saved successfully as : ' + data.itemNumber);
                            }, function (error) {
                                $rootScope.showWarningMessage(error.message);
                            }
                        );

                    }
                };

                $rootScope.showSidePanel(options);

            }

            function createEco() {

                var options = {
                    title: newECO,
                    template: 'app/desktop/modules/change/eco/new/newEcoView.jsp',
                    controller: 'NewECOController as newEcoVm',
                    resolve: 'app/desktop/modules/change/eco/new/newEcoController',
                    width: 700,
                    showMask: true,
                    data: {
                        ecoItem: vm.item
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.changes.ecos.new'}
                    ],
                    callback: function (eco) {
                        $rootScope.showSuccessMessage(itemEcoCreated);
                        emptyItem.change = eco.id;
                        emptyItem.itemObject.itemNumber = vm.item.itemNumber;
                        emptyItem.item = vm.itemRevision.id;
                        emptyItem.fromRevision = vm.itemRevision.revision;
                        emptyItem.fromLifeCycle = vm.itemRevision.lifeCyclePhase;
                        findItem(emptyItem);
                        $timeout(function () {
                            $window.localStorage.setItem("lastSelectedItemTab", JSON.stringify("details.changes"));
                            ECOService.createAffectedItem(eco.id, emptyItem).then(
                                function (data) {
                                    loadItem();
                                    itemDetailsTabActivated("details.changes");
                                    loadItemDetails();
                                    $rootScope.loadChanges(vm.itemId);
                                    $rootScope.hideSidePanel();
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }, 1000);

                    }
                };

                $rootScope.showSidePanel(options);
            }

            var itemEcoCreated = parsed.html($translate.instant("ITEM_ADDED_ECO_MSG")).html();
            var shareItem = parsed.html($translate.instant("SHARE_ITEM")).html();
            var shareButton = parsed.html($translate.instant("SHARE")).html();

            function shareItem() {
                var options = {
                    title: shareItem,
                    template: 'app/desktop/modules/shared/share/shareView.jsp',
                    controller: 'ShareController as shareVm',
                    resolve: 'app/desktop/modules/shared/share/shareController',
                    width: 600,
                    showMask: true,
                    data: {
                        sharedItem: vm.item,
                        itemsSharedType: 'itemSelection',
                        objectType: "ITEM"
                    },
                    buttons: [
                        {text: shareButton, broadcast: 'app.share.item'}
                    ],
                    callback: function (data) {
                        $rootScope.showSuccessMessage(vm.item.itemNumber + " : Shared successfully");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var itemNotExist = $translate.instant("ITEM_NOT_EXIST");

            function findItem(item) {
                if (item.itemObject.itemNumber != null && item.itemObject.itemNumber != undefined) {
                    vm.valid = true;
                    if (vm.valid == true) {
                        ItemService.findByItemNumber(item.itemObject.itemNumber).then(
                            function (data) {
                                if (data.length == 0) {
                                    $rootScope.showErrorMessage(item.itemObject.itemNumber + ' : ' + itemNotExist);
                                }
                                if (data.length == 1) {
                                    var foundItem = data[0];

                                    item.itemType = foundItem.itemType;
                                    item.itemNumber = foundItem.itemNumber;
                                    item.description = foundItem.description;

                                    var mapRevToItem = new Hashtable();
                                    ItemService.getItemRevisions(foundItem.id).then(
                                        function (data) {
                                            vm.revisions = data;
                                            angular.forEach(vm.revisions, function (revision) {
                                                mapRevToItem.put(revision.revision, revision);
                                            });

                                            return ItemService.getRevisionId(foundItem.latestRevision);
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    ).then(
                                        function (data) {
                                            item.id = data.id;
                                            //item.fromRevision = data.revision;
                                            //item.fromLifeCycle = data.lifeCyclePhase;
                                            var revs = item.itemType.revisionSequence.values;
                                            var index = revs.indexOf(data.revision);
                                            if (index != -1) {
                                                for (var i = index + 1; i < revs.length; i++) {
                                                    if (mapRevToItem.get(revs[i]) == null) {
                                                        item.toRevision = revs[i];
                                                        break
                                                    }
                                                }
                                            }
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    );
                                    loadLifecyclePhases(item);
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function loadLifecyclePhases(item) {
                item.toLifecyclePhases = [];

                var arr = item.itemType.lifecycle.phases;
                angular.forEach(arr, function (phase) {
                    if (phase.phaseType == 'RELEASED') {
                        item.toLifecyclePhases.push(phase);
                    }
                });

                if (item.toLifecyclePhases.length > 0) {
                    item.toLifeCycle = item.toLifecyclePhases[0];
                }
            }

            function broadcast(event) {
                $scope.$broadcast(event);
            }

            function editItem() {
                $state.go('app.items.edit', {itemId: vm.item.id});
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function itemDetailsTabActivated(id) {
                $state.transitionTo('app.items.details', {itemId: vm.itemId, tab: id}, {notify: false});
                tabId = id;

                arrangeFreeTextSearch();
                $scope.selectedToRevisionItem = null;
                $rootScope.bomItems = [];
                $rootScope.rootItems = [];
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if ($rootScope.selectedTab != undefined) {
                    $rootScope.selectedTab.active = false;
                }
                $rootScope.selectedTab = tab;
                if (tab != null && !tab.activated) {
                    tab.activated = true;
                    tab.active = true;
                    $scope.$broadcast('app.item.tabactivated', {tabId: tabId});
                    activateTab(tab);
                } else {
                    activateTab(tab);
                }
            }

            vm.showExternalUserItems = showExternalUserItems;
            function showExternalUserItems() {
                $state.go('app.home');

                $rootScope.sharedItem();
            }

            $rootScope.arrangeFreeTextSearch = arrangeFreeTextSearch;
            function arrangeFreeTextSearch() {
                if ($rootScope.loginPersonDetails.external == false) {
                    $timeout(function () {
                        var width = $("#action-buttons").outerWidth();
                        var freeTextSearch = document.getElementById("freeTextSearchDirective");
                        var actionButtons = document.getElementById("action-buttons");
                        if (freeTextSearch != null) {
                            freeTextSearch.style.right = (width + 20) + "px";
                            actionButtons.style.position = "relative";
                        }
                    }, 1000)
                }
            }

            function getTabById(tabId) {
                var tab = null;
                vm.tabCustomActions = [];
                vm.tabCustomActionGroups = [];
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = vm.tabs[t];
                    }
                }

                if (tab == null) {
                    angular.forEach(vm.customTabs, function (customTab) {
                        if (customTab.id === tabId) {
                            tab = customTab;
                            vm.tabCustomActions = customTab.tabCustomActions;
                            vm.tabCustomActionGroups = customTab.tabCustomActionGroups;
                        }
                    });
                }

                return tab;
            }

            function onAddItemFiles() {
                $scope.$broadcast('app.item.addfiles');
            }

            function showTypeAttributes() {
                $scope.$broadcast('app.bomItems.showAttributes');
            }

            function disableBomItems() {
                $scope.$broadcast('app.bomItems.disableEdit');
            }

            $scope.importData1 = importData1;
            function importData1() {
                $scope.$broadcast('app.item.importBom');
            }

            vm.exportData = exportData;
            function exportData() {
                $scope.$broadcast('app.item.exportBom');
            }

            vm.toRevisionBomItems = [];
            function loadAllItemRevisions(id) {
                ItemService.getItemRevisionsForBomCompare(id).then(
                    function (data) {
                        vm.toRevisionBomItems = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $scope.selectedToRevisionItem = null;
            vm.selectToRevisionItem = selectToRevisionItem;
            function selectToRevisionItem(item) {
                if (item.id == null) {
                    bomCompare();
                }
                else {
                    $rootScope.showBusyIndicator();
                    $scope.selectedToRevisionItem = null;
                    $scope.selectedToRevisionItem = item;
                    compareIndividualItemRevisions();
                }

            }


            /**
             * Ite to Item Comparision
             * */

            function individualItemCompare() {
                var options = {
                    title: titleOfBomCompare,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemToItemCompareView.jsp',
                    controller: 'ItemToItemCompareController as itemToItemCompareVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemToItemController',
                    data: {
                        itemId: vm.itemRevision.itemMaster
                    },
                    width: 750,
                    showMask: true,
                    buttons: [
                        {text: bomCompareBtn, broadcast: 'app.item.to.item.compare'}
                    ],
                    callback: function () {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            $rootScope.itemToItemFromItem = null;
            $rootScope.itemToItemToItem = null;
            $rootScope.itemToItemElements = [];
            $rootScope.itemToItemFilterElements = [];
            function individualItemToItemRevisionsCompare() {
                $rootScope.itemToItemFromItem = null;
                $rootScope.itemToItemToItem = null;
                $rootScope.itemToItemElements = [];
                $rootScope.itemToItemFilterElements = [];
                $rootScope.individualItemsFlag = false;
                $rootScope.individualRevFlags = true;
                ItemService.getComparedItems(vm.itemRevIdToCompare, $scope.selectedToRevisionItem.id).then(
                    function (data) {
                        $rootScope.itemToItemFromItem = data.fromItem;
                        $rootScope.itemToItemToItem = data.toItem;
                        $rootScope.itemToItemElements = data.listOfItemsCompared;
                        $rootScope.itemToItemFilterElements = data.listOfItemsCompared;
                        $rootScope.hideBusyIndicator();
                        $rootScope.closeitemCompare = true;
                        $('#myModalForItemCompare').modal('show');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )

            }


            vm.selectRevisionItemToCompare = selectRevisionItemToCompare;
            function selectRevisionItemToCompare(item) {
                if (item.id == null) {
                    individualItemCompare();
                }
                else {
                    $rootScope.showBusyIndicator();
                    $scope.selectedToRevisionItem = null;
                    $scope.selectedToRevisionItem = item;
                    individualItemToItemRevisionsCompare();
                }

            }

            $rootScope.fromItemName = null;
            $rootScope.toItemName = null;
            $rootScope.fromItemNumber = null;
            $rootScope.toItemNumber = null;
            $rootScope.fromItemRev = null;
            $rootScope.toItemRev = null;
            $rootScope.itemFilterList = [];

            $rootScope.individualRevFlags = false;
            function compareIndividualItemRevisions() {
                vm.bomCompareFilter = false;
                $rootScope.individualItemsFlag = false;
                $rootScope.individualRevFlags = true;
                $rootScope.itemList = [];
                $rootScope.itemFilterList = [];
                $rootScope.fromItemName = null;
                $rootScope.toItemName = null;
                $rootScope.fromItemNumber = null;
                $rootScope.toItemNumber = null;
                $rootScope.fromItemRev = null;
                $rootScope.toItemRev = null;
                ItemService.getComparedIndividualRevisions(vm.itemRevIdToCompare, $scope.selectedToRevisionItem.id, $rootScope.bomLatestState).then(
                    function (data) {
                        $rootScope.fromItemName = data.fromItemName;
                        $rootScope.toItemName = data.toItemName;
                        $rootScope.fromItemNumber = data.fromItemNumber;
                        $rootScope.toItemNumber = data.toItemNumber;
                        $rootScope.fromItemRev = data.fromItemRev;
                        $rootScope.toItemRev = data.toItemRev;
                        $rootScope.itemList = data.itemList;
                        $rootScope.itemFilterList = data.itemList;
                        $rootScope.hideBusyIndicator();
                        $rootScope.closebomComp = true;
                        $('#myModal1').modal('show');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )

            }

            $rootScope.rulesPlaceholder = "Rules";
            $rootScope.rulesArray = [{id: 1, value: "Bom Inclusion Rules"}, {
                id: 2,
                value: "Item Exclusion Rules"
            }, {id: 3, value: "Bom Rules"}]
            $rootScope.selectedRule = selectedRule;
            vm.selectRule = null;
            function selectedRule(value) {
                if (value.value == "Bom Inclusion Rules") {
                    $rootScope.showRules();
                }
                else if (value.value == "Bom Rules") {
                    getBomRules();
                }
                else {
                    $rootScope.showItemExclusions();
                }
            }

            vm.closeModal = closeModal;
            function closeModal() {
                /*loadAllItemRevisions(vm.itemId);*/
                vm.selectedValue = null;
                $rootScope.closebomComp = false;
                $rootScope.revItemPlaceHolder = parsed.html($translate.instant("COMPARE_ITEM_PLACEHOLDER")).html();
            }

            $rootScope.loadItem = loadItem;
            vm.itemRevIdToCompare = null;
            $rootScope.checkBomAvailOrNot = false;
            $rootScope.lockedObjectPermission = true;
            function loadItem() {
                vm.loading = true;
                if (vm.itemId != null && vm.itemId != undefined && vm.itemId != "") {

                    ItemService.getRevisionId(vm.itemId).then(
                        function (data) {
                            vm.itemRevIdToCompare = data.id;
                            $rootScope.seletedItemId = data.id;
                            vm.itemRevision = data;
                            $rootScope.itemRevision = data;
                            disableBomItems();
                            vm.inclusionRules = data.inclusionRules;
                            if (vm.itemRevision.plmeco == null) {
                                var ecoNumber = '-'
                            } else {
                                ecoNumber = vm.itemRevision.plmeco.ecoNumber;
                            }
                            if (vm.itemRevision.dco == null) {
                                var dcoNumber = '-'
                            } else {
                                dcoNumber = vm.itemRevision.dco.dcoNumber;
                            }
                            $rootScope.checkBomAvailOrNot = vm.itemRevision.hasBom;
                            vm.lifeCycleStatus = vm.itemRevision.lifeCyclePhase.phaseType;
                            $rootScope.itemLifeCycleStatus = vm.lifeCycleStatus;
                            ItemService.getItem(vm.itemRevision.itemMaster).then(
                                function (data) {
                                    vm.item = data;
                                    $rootScope.item = vm.item;
                                    $rootScope.itemType = vm.item.itemType.itemClass;
                                    $rootScope.startWorkflow = vm.item.startWorkflow;
                                    if (vm.item.lockObject) {
                                        if (vm.item.lockedBy.id != $rootScope.loginPersonDetails.person.id) {
                                            $rootScope.lockedObjectPermission = false;
                                        }
                                    }
                                    loadSubscribe();
                                    $rootScope.loadBomConfigurations();
                                    $rootScope.selectedItemInfo = data;
                                    if (vm.item.itemType.requiredEco) {
                                        if (vm.itemRevision.plmeco != null) {
                                            $rootScope.viewInfo.title = "<div class='item-number'>" +
                                                "{0}</div> <span class='item-rev'>Rev {1}</span> <span class='item-rev'>ECO ({2})</span>".
                                                    format(vm.item.itemNumber,
                                                    vm.itemRevision.revision, ecoNumber);
                                        } else if (vm.itemRevision.dco != null) {
                                            $rootScope.viewInfo.title = "<div class='item-number'>" +
                                                "{0}</div> <span class='item-rev'>Rev {1}</span> <span class='item-rev'>DCO ({2})</span>".
                                                    format(vm.item.itemNumber,
                                                    vm.itemRevision.revision, dcoNumber);
                                        } else if (vm.itemRevision.plmeco == null && vm.itemRevision.dco == null) {
                                            $rootScope.viewInfo.title = "<div class='item-number'>" +
                                                "{0}</div> <span class='item-rev'>Rev {1}</span> <span class='item-rev'>ECO ({2})</span>".
                                                    format(vm.item.itemNumber,
                                                    vm.itemRevision.revision, ecoNumber);
                                        }
                                    } else {
                                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                                            "{0}</div> <span class='item-rev'>Rev {1}</span>".
                                                format(vm.item.itemNumber, vm.itemRevision.revision);
                                    }

                                    $rootScope.viewInfo.description = vm.item.itemName;
                                    setLifecycles();

                                    loadItemDetailsExtensions();
                                    loadCommentsCount();

                                    vm.lastLifecyclePhase = vm.item.itemType.lifecycle.phases[vm.item.itemType.lifecycle.phases.length - 1];
                                    vm.firstLifecyclePhase = vm.item.itemType.lifecycle.phases[0];
                                    /*if (vm.lastLifecyclePhase.phaseType == "OBSOLETE" && vm.itemRevision.lifeCyclePhase.phaseType == "RELEASED") {
                                     vm.promoteItemRevisionTitle = vm.makeRevisionAsAbsolete;
                                     }*/

                                    //itemDetailsTabActivated(tabId);
                                    var tab = getTabById(tabId);
                                    if (tab !== null) {
                                        vm.active = tab.index;
                                    }

                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                    loadAllItemRevisions(vm.itemId);
                }
            }

            $scope.checkTitle = function (title1) {
                var val = $rootScope.bomConfigCombinations.indexOf(title1) != -1;
                return val;
            };

            function getLastTabIndexOfStandardTabs() {
                var index = 0;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        index = vm.tabs[t].index;
                    }
                }

                return index;
            }

            function loadItemDetailsExtensions() {
                vm.customActions = [];
                vm.customActionGroups = [];

                var context = {
                    item: vm.item,
                    itemRevision: vm.itemRevision
                };
                var plugins = $application.plugins;
                angular.forEach(plugins, function (plugin) {
                    var extensions = plugin.extensions;
                    if (extensions != null && extensions !== undefined) {
                        var itemdetails = extensions.objectDetails;
                        if (itemdetails != null && itemdetails !== undefined) {
                            var actionGroups = itemdetails.actiongroups;
                            if (actionGroups != null && actionGroups !== undefined && actionGroups.length > 0) {
                                angular.forEach(actionGroups, function (group) {
                                    var show = true;

                                    if (group.filter != null && group.filter !== undefined) {
                                        show = jexl.evalSync(group.filter, context);
                                    }
                                    if (show) {
                                        vm.customActionGroups.push(group);
                                    }
                                })
                            }
                            var actions = itemdetails.actions;
                            if (actions != null && actions !== undefined && actions.length > 0) {
                                angular.forEach(actions, function (action) {
                                    var show = true;

                                    if (action.filter != null && action.filter !== undefined) {
                                        show = jexl.evalSync(action.filter, context);
                                    }
                                    if (show) {
                                        vm.customActions.push(action);
                                    }
                                })
                            }
                        }
                    }
                });
            }

            vm.performCustomAction = performCustomAction;
            function performCustomAction(action) {
                var service = $injector.get(action.service);
                if (service != null && service !== undefined) {
                    var method = service[action.method];
                    if (method != null && method !== undefined && typeof method === "function") {
                        method(vm.item, vm.itemRevision);
                    }
                }
            }

            vm.hasDisplayTab = hasDisplayTab;
            function hasDisplayTab(tabName) {
                var valid = true;

                if (vm.item != null && vm.item != undefined && vm.item.itemType.tabs != null) {
                    var index = vm.item.itemType.tabs.indexOf(tabName);
                    if (index == -1) {
                        valid = false;
                    }
                }

                return valid;
            }

            $rootScope.loadItemDetails = loadItemDetails;
            function loadItemDetails() {
                ItemService.getItemDetails(vm.itemId).then(
                    function (data) {
                        vm.itemDetails = data;
                        var filesTab = document.getElementById("files");
                        var bomTab = document.getElementById("bom");
                        var whereUsedTab = document.getElementById("whereUser");
                        var mfrTab = document.getElementById("mfr");
                        var relatedItemsTab = document.getElementById("relatedItems");
                        var projectItemTab = document.getElementById("projectItem");
                        var requirementsTab = document.getElementById("requirements-tab");
                        var specTab = document.getElementById("itemSpecifications");
                        var configuredTab = document.getElementById("configured");
                        var changeTab = document.getElementById("changesTab");
                        var varianceTab = document.getElementById("variance");
                        var qualityTab = document.getElementById("qualityTab");
                        var inspectionTab = document.getElementById("inspections");

                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (filesTab != null) {
                            filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                                tmplStr.format(vm.itemDetails.itemFiles);
                        }
                        if (mfrTab != null) {
                            mfrTab.lastElementChild.innerHTML = vm.tabs.mfr.heading +
                                tmplStr.format(vm.itemDetails.manufacturerParts);
                        }
                        if (relatedItemsTab != null) {
                            relatedItemsTab.lastElementChild.innerHTML = vm.tabs.relatedItems.heading +
                                tmplStr.format(vm.itemDetails.relatedItems);
                        }
                        if (projectItemTab != null) {
                            projectItemTab.lastElementChild.innerHTML = vm.tabs.projectItem.heading +
                                tmplStr.format(vm.itemDetails.projectItemsDtos);
                        }
                        if (requirementsTab != null) {
                            requirementsTab.lastElementChild.innerHTML = vm.tabs.requirements.heading +
                                tmplStr.format(vm.itemDetails.requirements);
                        }
                        if (specTab != null) {
                            specTab.lastElementChild.innerHTML = vm.tabs.specifications.heading +
                                tmplStr.format(vm.itemDetails.specifications);
                        }
                        if (bomTab != null) {
                            bomTab.lastElementChild.innerHTML = vm.tabs.bom.heading +
                                tmplStr.format(vm.itemDetails.bom);
                        }
                        if (whereUsedTab != null) {
                            whereUsedTab.lastElementChild.innerHTML = vm.tabs.whereUsed.heading +
                                tmplStr.format(vm.itemDetails.whereUsedItems);
                        }
                        if (configuredTab != null) {
                            configuredTab.lastElementChild.innerHTML = vm.tabs.configured.heading +
                                tmplStr.format(vm.itemDetails.configuredItems);
                        }
                        if (changeTab != null) {
                            changeTab.lastElementChild.innerHTML = vm.tabs.changes.heading +
                                tmplStr.format(vm.itemDetails.changeItems);
                        }
                        if (varianceTab != null) {
                            varianceTab.lastElementChild.innerHTML = vm.tabs.variance.heading +
                                tmplStr.format(vm.itemDetails.varianceItems);
                        }
                        if (qualityTab != null) {
                            qualityTab.lastElementChild.innerHTML = vm.tabs.quality.heading +
                                tmplStr.format(vm.itemDetails.qualityItems);
                        }

                        if (inspectionTab != null) {
                            inspectionTab.lastElementChild.innerHTML = vm.tabs.inspection.heading +
                                tmplStr.format(vm.itemDetails.inspections);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.loadBomConfigurations = loadBomConfigurations;

            function loadBomConfigurations() {
                ItemBomService.getBomConfigurations(vm.itemId).then(
                    function (data) {
                        vm.bomConfigs = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadSubscribe() {
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                ItemService.getSubscribeByPerson(vm.item.id, $rootScope.loginPersonDetails.person.id).then(
                    function (data) {
                        vm.subscribe = data;
                        if (vm.subscribe == null) {
                            $scope.subscribeButtonTitle = subscribeTitle;
                        } else {
                            if (vm.subscribe.subscribe) {
                                $scope.subscribeButtonTitle = unsubscribeTitle;
                            } else {
                                $scope.subscribeButtonTitle = subscribeTitle;
                            }
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function setLifecycles() {
                var phases = [];
                var currentPhase = vm.itemRevision.lifeCyclePhase.phase;
                var currentLifeCyclePhase = vm.itemRevision.lifeCyclePhase;
                $rootScope.lifeCycleStatus = vm.itemRevision.lifeCyclePhase.phase;
                var defs = vm.item.itemType.lifecycle.phases;
                defs.sort(function (a, b) {
                    return a.id - b.id;
                });
                var lastPhase = defs[defs.length - 1].phase;
                var phaseMap = new Hashtable();
                angular.forEach(defs, function (def) {
                    if (def.phaseType === 'OBSOLETE' && currentLifeCyclePhase.phaseType != 'OBSOLETE') return;

                    if (def.phase == currentPhase && lastPhase == def.phase) {
                        if (phaseMap.get(def.phase) == null) {
                            phases.push({
                                name: def.phase,
                                finished: true,
                                rejected: (def.phase == currentPhase && vm.itemRevision.rejected),
                                current: (def.phase == currentPhase)
                            })
                            phaseMap.put(def.phase, def);
                        }
                    } else {
                        if (phaseMap.get(def.phase) == null) {
                            phases.push({
                                name: def.phase,
                                finished: false,
                                rejected: (def.phase == currentPhase && vm.itemRevision.rejected),
                                current: (def.phase == currentPhase)
                            })
                            phaseMap.put(def.phase, def);
                        }
                    }
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
                var type = vm.itemRevision.lifeCyclePhase.phaseType;
                if (type == 'PRELIMINARY') {
                    return "label-warning";
                }
                else if (type == 'RELEASED') {
                    return "label-lightblue";
                }
                else if (type == 'OBSOLETE') {
                    return "label-danger";
                }
            }

            function reviseItem() {
                var options = {
                    title: confirmation,
                    message: doYouWantToRevise,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };

                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator();
                            if (vm.itemId != null && vm.itemId != undefined && vm.itemId != "") {
                                ItemService.getRevisionId(vm.itemId).then(
                                    function (data) {
                                        vm.itemRevision = data;
                                        ItemService.reviseItem(vm.itemRevision.itemMaster).then(
                                            function (revisedItem) {
                                                $timeout(function () {
                                                    vm.customActions = [];
                                                    vm.customActionGroups = [];
                                                    vm.customTabs = [];
                                                    $state.go('app.items.details', {itemId: revisedItem.id});
                                                    $rootScope.hideBusyIndicator();
                                                }, 500)
                                            },
                                            function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }
                    }
                )
            }

            var revisionHistoryTitle = $translate.instant("REVISION_HISTORY_TITLE");

            var selectWorkflowTitle = parsed.html($translate.instant("ITEM_DETAILS_SELECT_WORKFLOW")).html();

            function showItemRevisionHistory() {
                var options = {
                    title: vm.item.itemNumber + " - " + revisionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: vm.itemRevision.itemMaster,
                        revisionHistoryType: "ITEM"
                    },
                    width: 700,
                    showMask: true,
                };

                $rootScope.showSidePanel(options);
            }

            var titleOfBomCompare = parsed.html($translate.instant("SELECT_ITEM_TO_COMPARE")).html();
            var bomCompareBtn = parsed.html($translate.instant("COMPARE")).html();
            $rootScope.revItemPlaceHolder = parsed.html($translate.instant("COMPARE_ITEM_PLACEHOLDER")).html();

            function bomCompare() {
                vm.bomCompareFilter = false;
                var options = {
                    title: titleOfBomCompare,
                    template: 'app/desktop/modules/item/details/tabs/basic/bomItemCompareView.jsp',
                    controller: 'BomItemCompareController as bomItemCompareVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/bomItemsCompareController',
                    data: {
                        itemId: vm.itemRevision.itemMaster
                    },
                    width: 750,
                    showMask: true,
                    buttons: [
                        {text: bomCompareBtn, broadcast: 'app.item.bom.compare'}
                    ],
                    callback: function () {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function tabLoaded(tabId) {
                $scope.$broadcast('app.item.tabloaded', {tabId: tabId});
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedItemTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function refreshDetails() {
                $scope.$broadcast('app.item.tabactivated', {tabId: $rootScope.selectedTab.id});
            }

            vm.subscribeItem = subscribeItem;

            function subscribeItem(item) {
                ItemService.subscribe(item.id).then(
                    function (data) {
                        vm.subscribe = data;
                        if (vm.subscribe.subscribe) {
                            $rootScope.showSuccessMessage(subscribeMsg.format(itemTitle));
                            $scope.subscribeButtonTitle = unsubscribeTitle;
                        } else {
                            $rootScope.showSuccessMessage(unsubscribeMsg.format(itemTitle));
                            $scope.subscribeButtonTitle = subscribeTitle;
                        }

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var itemLifecycleStatusPromoted = parsed.html($translate.instant("ITEM_PROMOTED")).html();
            var itemLifecycleStatusDemoted = parsed.html($translate.instant("ITEM_DEMOTED")).html();
            var confirmation = parsed.html($translate.instant("CONFIRMATION")).html();
            var doYouWantToPromote = parsed.html($translate.instant("DO_YOU_WANT_TO_PROMOTE_STATUS")).html();
            var doYouWantToRevise = parsed.html($translate.instant("DO_YOU_WANT_TO_REVISE_ITEM")).html();
            var doYouWantToDemote = parsed.html($translate.instant("DO_YOU_WANT_TO_DEMOTE_STATUS")).html();
            var yesTitle = parsed.html($translate.instant("YES")).html();
            var noTitle = parsed.html($translate.instant("NO")).html();

            vm.promoteItem = promoteItem;
            function promoteItem() {
                var options = {
                    title: confirmation,
                    message: doYouWantToPromote,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ItemService.promtoItem(vm.itemId, vm.itemRevision).then(
                            function (data) {
                                loadItem();
                                $rootScope.loatItemBasicInfo();
                                $rootScope.showSuccessMessage(itemLifecycleStatusPromoted);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            vm.clearBomCompareFilter = clearBomCompareFilter;
            function clearBomCompareFilter() {
                vm.bomCompareFilter = false;
                $rootScope.itemList = $rootScope.itemFilterList;
            }

            vm.bomCompareFilter = false;
            vm.showAddedItems = showAddedItems;
            function showAddedItems() {
                $rootScope.itemList = [];
                vm.bomCompareFilter = true;
                vm.filterMessage = parsed.html($translate.instant("BOM_COMPARE_FILTER_ADDED_MESSAGE")).html();
                angular.forEach($rootScope.itemFilterList, function (item) {
                    if (item.color != null && item.color != "") {
                        if (item.color == "green_color") {
                            $rootScope.itemList.push(item);
                        }
                    }
                });
            }

            vm.showChangedItems = showChangedItems;
            function showChangedItems() {
                vm.bomCompareFilter = true;
                $rootScope.itemList = [];
                vm.filterMessage = parsed.html($translate.instant("BOM_COMPARE_FILTER_CHANGED_MESSAGE")).html();
                angular.forEach($rootScope.itemFilterList, function (item) {
                    if (item.color != null && item.color != "") {
                        if (item.color == "orange_color") {
                            $rootScope.itemList.push(item);
                        }
                    }
                });
            }


            vm.showDeletedItems = showDeletedItems;
            function showDeletedItems() {
                vm.bomCompareFilter = true;
                $rootScope.itemList = [];
                vm.filterMessage = parsed.html($translate.instant("BOM_COMPARE_FILTER_DELETED_MESSAGE")).html();
                angular.forEach($rootScope.itemFilterList, function (item) {
                    if (item.color != null && item.color != "") {
                        if (item.color == "red_color") {
                            $rootScope.itemList.push(item);
                        }
                    }
                });
            }


            vm.showNoChangeItems = showNoChangeItems;
            function showNoChangeItems() {
                vm.bomCompareFilter = true;
                $rootScope.itemList = [];
                vm.filterMessage = parsed.html($translate.instant("BOM_COMPARE_FILTER_NO_CHANGES_MESSAGE")).html();
                angular.forEach($rootScope.itemFilterList, function (item) {
                    if (item.color != null && item.color != "") {
                        if (item.color == "white_color") {
                            $rootScope.itemList.push(item);
                        }
                    }
                });
            }

            vm.demoteItem = demoteItem;
            function demoteItem() {
                var options = {
                    title: confirmation,
                    message: doYouWantToDemote,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ItemService.demoteItem(vm.itemId, vm.itemRevision).then(
                            function (data) {
                                loadItem();
                                $rootScope.loatItemBasicInfo();
                                $rootScope.showSuccessMessage(itemLifecycleStatusDemoted);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }


            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('ITEM', vm.itemRevision.itemMaster).then(
                    function (data) {
                        $rootScope.showComments('ITEM', vm.itemRevision.itemMaster, data);
                        $rootScope.showTags('ITEM', vm.itemRevision.itemMaster, vm.item.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadAttributeDefs(id, cbom) {
                cbom.attributes = [];
                vm.requiredAttributes = [];
                vm.attributes = [];
                ItemTypeService.getAttributesWithHierarchyFalse(id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: null,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                timestampValue: null,
                                booleanValue: false,
                                dateValue: null,
                                timeValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.configurable == true) {
                                cbom.attributes.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )

            }

            function resizeBomInclusionRules() {
                var modal = document.getElementById("myModalForBomRules");
                if (modal != null) {
                    var headerHeight = $('.bomRulecompareHeadre').outerHeight();
                    var bomContentHeight = $('.bomRulecompareContent').outerHeight();
                    $(".bomRuleexclusionBody").height(bomContentHeight - (headerHeight) - 20);
                }
            }

            function resizeBomItemExclusionRules() {
                var modal = document.getElementById("myModalForItemExclusions");
                if (modal != null) {
                    var headerHeight = $('.bomItemRulecompareHeadre').outerHeight();
                    var bomContentHeight = $('.bomItemRulecompareContent').outerHeight();
                    $(".bomRuleItemexclusionBody").height(bomContentHeight - (headerHeight) - 20);
                }
            }


            function resizeBomCompare() {
                var modal = document.getElementById("myModal1");
                if (modal != null) {
                    var headerHeight = $('.compareHeadre').outerHeight();
                    var bomContentHeight = $('.compareContent').outerHeight();
                    $(".bomCompareModalBody").height(bomContentHeight - (headerHeight) - 20);
                }
            }

            function resizeItemCompare() {
                var modal = document.getElementById("myModalForItemCompare");
                if (modal != null) {
                    var headerHeight = $('.itemCompareHeadre').outerHeight();
                    var bomContentHeight = $('.itemCompareContent').outerHeight();
                    $(".itemCompareModalBody").height(bomContentHeight - (headerHeight) - 20);
                }
            }

            $rootScope.closebomIncl = false;
            $rootScope.closeitemCompare = false;
            $rootScope.closeItemExcl = false;
            $rootScope.closebomComp = false;
            vm.closeBomInlcusionModal = closeBomInlcusionModal;
            vm.closeItemExclusionModal = closeItemExclusionModal;
            vm.closeModalForItemCompare = closeModalForItemCompare;
            function closeBomInlcusionModal() {
                $rootScope.closebomIncl = false;
                $rootScope.closeItemExcl = false;
            }

            function closeItemExclusionModal() {
                $rootScope.closebomIncl = false;
                $rootScope.closeItemExcl = false;
            }

            function closeModalForItemCompare() {
                $rootScope.closebomIncl = false;
                $rootScope.closeItemExcl = false;
            }

            $(window).resize(function () {
                if ($rootScope.closebomIncl == true) {
                    resizeBomInclusionRules();
                }
                if ($rootScope.closeItemExcl == true) {
                    resizeBomItemExclusionRules();
                }

                if ($rootScope.closebomComp == true) {
                    resizeBomCompare();
                }
                if ($rootScope.closeitemCompare == true) {
                    resizeItemCompare();
                }
            });


            vm.updateItemConfigurableAttributes = updateItemConfigurableAttributes;
            function updateItemConfigurableAttributes() {
                $rootScope.showBusyIndicator();
                ItemService.updateAllItemConfigurableAttributes().then(
                    function (data) {
                        $rootScope.showSuccessMessage("Success");
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showItems = showItems;
            function showItems() {
                if ($rootScope.itemDetailsMode == 'products') {
                    $state.go('app.items.all', {itemMode: 'products'});
                }
                else if ($rootScope.itemDetailsMode == 'assemblies') {
                    $state.go('app.items.all', {itemMode: 'assemblies'});
                }
                else if ($rootScope.itemDetailsMode == 'parts') {
                    $state.go('app.items.all', {itemMode: 'parts'});
                }
                else if ($rootScope.itemDetailsMode == 'documents') {
                    $state.go('app.items.all', {itemMode: 'documents'});
                }
                else {
                    $state.go('app.items.all', {itemMode: 'all'});
                }
            }

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addWorkflow = addWorkflow;
            function addWorkflow() {
                $scope.$broadcast('app.add.workflow');
            }

            $rootScope.itemPrintClass = null;

            vm.getPrintOptions = getPrintOptions;
            function getPrintOptions(item) {
                $rootScope.itemPrintClass = item.itemType.itemClass;
                $rootScope.showPrintOptions(item.latestRevision, "ITEM");
            }

            vm.updateIncorporate = updateIncorporate;
            function updateIncorporate() {
                vm.itemRevision.incorporate = true;
                $rootScope.showBusyIndicator($('.view-container'));
                ItemService.updateItem(vm.itemRevision).then(
                    function (data) {
                        vm.itemRevision = data;
                        $rootScope.itemRevision = data;
                        $rootScope.showSuccessMessage("Item incorporated successfully");
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.updateUnIncorporate = updateUnIncorporate;
            function updateUnIncorporate() {
                vm.itemRevision.incorporate = false;
                $rootScope.showBusyIndicator($('.view-container'));
                ItemService.updateItem(vm.itemRevision).then(
                    function (data) {
                        vm.itemRevision = data;
                        $rootScope.itemRevision = data;
                        $rootScope.showSuccessMessage("Item unincorporated successfully");
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadItemDetails();
                loadItem();
                $rootScope.$on("app.plugins.loaded", function () {
                    loadItemDetailsExtensions();
                });

                if ($rootScope.selectedMasterItemId != null) {
                    vm.itemId = $rootScope.selectedMasterItemId;
                }
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedItemTab"));
                    lastSelectedWhereUsed = JSON.parse($window.localStorage.getItem("lastSelectedWhereUsedItem"));
                }
                if ($window.localStorage.getItem("shared-permission") != undefined && $window.localStorage.getItem("shared-permission") != null) {
                    var sharedPermission = $window.localStorage.getItem("shared-permission");
                    if (sharedPermission != null && sharedPermission != "") {
                        $rootScope.sharedPermission = sharedPermission;
                    }
                }
                $window.localStorage.setItem("lastSelectedItemTab", "");

                if (tabId != null && tabId != undefined && lastSelectedTab != null &&
                    lastSelectedTab !== undefined) {
                    itemDetailsTabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.item.tabactivated', {tabId: lastSelectedTab});
                    }, 1000)
                }
                //}
            })();
        }
    }
)
;
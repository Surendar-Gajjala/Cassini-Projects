define(
    [
        'app/desktop/modules/item/item.module',
        'split-pane',
        'jquery-ui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/classification/directive/folderDirective',
        'app/desktop/modules/classification/directive/folderController',
        'app/desktop/modules/item/details/itemDetailsController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/folderService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/recentlyVisitedService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/requirementService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/all-view-icons/allViewIconsDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('ItemsController', ItemsController);

        function ItemsController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                 $state, $stateParams, $cookies, $uibModal, ProjectService, SpecificationsService,
                                 CommonService, ItemTypeService, ItemService, DialogService, FolderService, ObjectTypeAttributeService,
                                 AttributeAttachmentService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService,
                                 ItemFileService, RecentlyVisitedService, RequirementService) {

            $rootScope.viewInfo.icon = "fa fa-th";
            $rootScope.viewInfo.title = $translate.instant('ITEMS_ALL_TITLE');
            $rootScope.viewInfo.showDetails = false;
            $rootScope.searchItem = searchItem;

            var parsed = angular.element("<div></div>");

            var vm = this;
            vm.itemsearch = [];
            vm.advancedsearch = [];
            vm.selectedAttributes = [];
            vm.selectedObjectAttributes = [];
            vm.objectIds = [];
            $scope.pagenumbers = [20, 50, 100, 200, 500];
            vm.loading = true;
            vm.clear = false;
            vm.existedNumbers = [];
            vm.selectedNumbers = [];
            vm.selectedItemType = null;
            vm.mode = null;
            vm.mode = $rootScope.searchType;
            var currencyMap = new Hashtable();
            vm.selecteditems = [];
            vm.selection = null;
            $rootScope.showSearch = false;
            vm.folder = null;
            $rootScope.searchModeType = false;
            vm.filterSearch = false;
            vm.privatefolderSelected = false;
            vm.folderSelected = false;
            vm.publicfolderSelected = false;
            vm.itemsMode = '';
            $rootScope.itemDetailsMode = '';
            $scope.itemsTitle = parsed.html($translate.instant("ITEMS_ALL_TITLE")).html();
            vm.clipBoardItems = $application.clipboard.items;

            $scope.deleteItemTitle = parsed.html($translate.instant("DELETE_THIS_ITEM")).html();
            $scope.itemLockedMsg = parsed.html($translate.instant("ITEM_LOCKED_MSG")).html();
            $scope.itemUnLockedMsg = parsed.html($translate.instant("ITEM_UNLOCKED_MSG")).html();
            $scope.lockedBy = parsed.html($translate.instant("LOCKED_BY")).html();
            var clickToUnlock = parsed.html($translate.instant("CLICK_TO_UNLOCK_ITEM")).html();
            var clickToLock = parsed.html($translate.instant("CLICK_TO_LOCK_ITEM")).html();
            vm.shareItem = parsed.html($translate.instant("SHARE_MESSAGE")).html();
            vm.showFileTitle = parsed.html($translate.instant("SHOW_FILE_TITLE")).html();
            vm.showBomTitle = parsed.html($translate.instant("SHOW_BOM_TITLE")).html();
            vm.showPartTitle = parsed.html($translate.instant("SHOW_PART_TITLE")).html();
            vm.showChangesTitle = parsed.html($translate.instant("SHOW_CHANGES_TITLE")).html();
            vm.showVarianceTitle = parsed.html($translate.instant("SHOW_VARIANCE_TITLE")).html();
            vm.showQualityTitle = parsed.html($translate.instant("SHOW_QUALITY_TITLE")).html();
            vm.FILES = parsed.html($translate.instant("FILES")).html();
            vm.showSubscribers = parsed.html($translate.instant("CLICK_TO_SHOW_SUBSCRIBERS")).html();
            $rootScope.clickToDownloadFile = parsed.html($translate.instant("CLICK_TO_DOWNLOAD")).html();
            vm.showItemsAttributes = parsed.html($translate.instant("SHOW_ITEM_ATTRIBUTES")).html();
            vm.savedSearchItems = parsed.html($translate.instant("SAVED_SEARCH_ITEMS")).html();
            vm.saveSearchItems = parsed.html($translate.instant("SAVE_SEARCH_ITEMS")).html();
            vm.addItemsFolder = parsed.html($translate.instant("ADD_ITEMS_FOLDER")).html();
            vm.itemsExport = parsed.html($translate.instant("ITEMS_EXPORT")).html();
            vm.searchItemType = parsed.html($translate.instant("ITEM_SEARCH")).html();
            $scope.releasedDate = parsed.html($translate.instant("RELEASED_DATE")).html();
            $scope.rejectedDate = parsed.html($translate.instant("REJECTED_DATE")).html();
            var Each = parsed.html($translate.instant("EACH")).html();
            var itemAddedToClipboardAnd = parsed.html($translate.instant("ITEMS_ADDED_TO_CLIPBOARD_AND")).html();
            var youCannotCopyReleasedItems = parsed.html($translate.instant("YOU_CANNOT_COPY_RELEASED_ITEMS")).html();
            var itemAddedToClipboard = parsed.html($translate.instant("ITEMS_ADDED_TO_CLIPBOARD")).html();
            //var pageSize = "20";
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
            if ($rootScope.localStorageLogin.login.person.id != null) {
                var person = $rootScope.localStorageLogin.login.person.id;
            }

            vm.search = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                objectType: 'SAVEDSEARCH',
                searchObjectType: 'ITEM',
                owner: person,
                type: 'PRIVATE'
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

            vm.items = angular.copy(pagedResults);

            vm.showNewItem = showNewItem;
            vm.newItem = newItem;
            vm.editItem = editItem;
            vm.deleteItem = deleteItem;
            vm.pageSize = pageSize;
            vm.showItem = showItem;
            vm.advancedItemSearch = advancedItemSearch;
            vm.freeTextSearch = freeTextSearch;
            vm.freeTextSearchItems = freeTextSearchItems;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.toggleSelection = toggleSelection;
            vm.resetPage = resetPage;
            vm.selectAll = selectAll;
            vm.flag = false;
            vm.saveAs = saveAs;
            vm.showTypeAttributes = showTypeAttributes;
            vm.showSaveSearch = showSaveSearch;
            $rootScope.showItemSavedSearches = showItemSavedSearches;
            vm.removeAttribute = removeAttribute;
            vm.exportItems = exportItems;
            vm.shareSelectedItems = shareSelectedItems;
            vm.showItemView = showItemView;

            var nodeId = 0;
            var rootNode = null;
            var searchMode = null;
            var simpleFilters = null;
            var advancedFilters = null;
            $scope.freeTextQuery = null;
            vm.showCopyToClipBoard = false;

            vm.itemFilter = {
                itemNumber: null,
                itemName: null,
                description: null,
                searchQuery: null,
                itemType: '',
                type: '',
                phase: null,
                itemClass: '',
                revision: null
            };

            function resetPage() {
                vm.flag = false;
                vm.items = angular.copy(pagedResults);
                vm.filterSearch = false;
                vm.pageable.page = 0;
                vm.itemFilter = {
                    itemNumber: null,
                    itemName: null,
                    description: null,
                    searchQuery: null,
                    itemType: '',
                    type: '',
                    phase: null,
                    itemClass: vm.itemsMode,
                    revision: null
                };
                $rootScope.showSearch = false;
                $rootScope.allItemsLoad = true;
                $rootScope.searchModeType = false;
                $rootScope.itemAdvanceSearch = null;
                $rootScope.itemSimpleSearch = null;
                vm.search.searchType = null;
                if (document.getElementById("freeTextSearchInput") != null)
                    document.getElementById("freeTextSearchInput").placeholder = "";
            }

            function nextPage() {
                if (vm.items.last != true) {
                    vm.pageable.page++;
                    vm.flag = false;
                    performSearch();
                }
            }

            function pageSize(page) {
                vm.flag = false;
                vm.filterSearch = false;
                vm.pageable.page = 0;
                vm.pageable.size = page;
                $rootScope.showSearch = false;
                $rootScope.searchModeType = false;
                performSearch();
            }

            function previousPage() {
                if (vm.items.first != true) {
                    vm.pageable.page--;
                    vm.flag = false;
                    performSearch();
                }
            }

            function clearFilter() {
                loadItems();
                vm.clear = false;
                $rootScope.showSearch = false;
            }

            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var bom = parsed.html($translate.instant("ITEM_DETAILS_TAB_BOM")).html();
            var whereUsed = parsed.html($translate.instant("DETAILS_TAB_WHERE_USED")).html();
            var changes = parsed.html($translate.instant("ITEM_DETAILS_TAB_CHANGES")).html();
            vm.itemMainView = parsed.html($translate.instant("ITEM_MAIN_VIEW")).html();
            vm.itemMastersView = parsed.html($translate.instant("ITEM_MASTER_VIEW")).html();
            var relatedItems = parsed.html($translate.instant("RELATED_ITEMS")).html();
            var projects = parsed.html($translate.instant("PROJECTS")).html();
            var requirements = parsed.html($translate.instant("REQUIREMENTS")).html();
            var instance = parsed.html($translate.instant("ITEM_DETAILS_TAB_INSTANCE")).html();
            var quality = parsed.html($translate.instant("QUALITY")).html();
            var variance = parsed.html($translate.instant("VARIANCES")).html();
            $scope.cannotDeleteApprovedItem = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_ITEM")).html();

            vm.active = 0;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                /*attributes: {
                 id: 'details.attributes',
                 heading: 'attribute',
                 template: 'app/desktop/modules/item/details/tabs/attributes/itemAttributesView.jsp',
                 index: 1,
                 active: false,
                 activated: false
                 },*/
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
                    heading: 'Files',
                    template: 'app/desktop/modules/item/details/tabs/files/itemFilesView.jsp',
                    index: 7,
                    active: false,
                    activated: false
                },
                mfr: {
                    id: 'details.mfr',
                    heading: 'Manufacturers',
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
                }, itemTimelineHistory: {
                    id: 'details.itemTimelineHistory',
                    heading: "Timeline",
                    template: 'app/desktop/modules/item/details/tabs/timelineHistory/itemTimelineHistoryView.jsp',
                    index: 12,
                    active: false,
                    activated: false
                }

            };


            vm.customTabs = [];
            vm.customActions = [];
            vm.customActionGroups = [];

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            $rootScope.showCopyItemFilesToClipBoard = false;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            $rootScope.selectedMasterItemId = null;
            vm.itemDetailsTabActivated = itemDetailsTabActivated;
            function itemDetailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    $scope.$broadcast('app.item.tabactivated', {tabId: tabId});
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

                if (tab == null) {
                    angular.forEach(vm.customTabs, function (customTab) {
                        if (customTab.id === tabId) {
                            tab = customTab;
                        }
                    });
                }

                return tab;
            }

            vm.selectItem = selectItem;

            $rootScope.selectedMasterItemId = null;
            vm.selectedItem = null;

            vm.Minimize = Minimize;
            function Minimize() {
                window.innerWidth = 100;
                window.innerHeight = 100;
                window.screenX = screen.width;
                window.screenY = screen.height;
                alwaysLowered = true;
            }

            vm.Maximize = Maximize;
            function Maximize() {
                window.innerWidth = screen.width;
                window.innerHeight = screen.height;
                window.screenX = 0;
                window.screenY = 0;
                alwaysLowered = false;
            }

            $rootScope.masterDetailsEnable = false;
            $rootScope.selectedMasterItemId = null;
            function selectItem(item) {
                vm.selectedItem = item;
                $rootScope.selectedMasterItemId = item.latestRevision;
                vm.itemDetailsTabActivated(vm.tabs.basic.id);
                $rootScope.masterDetailsEnable = true;
                loadItemDetails();
                loadItem();

                $timeout(function () {
                    window.dispatchEvent(new Event('resize'));
                }, 100);
            }

            $rootScope.loadItemDetails = loadItemDetails;
            function loadItemDetails() {
                ItemService.getItemDetails($rootScope.selectedMasterItemId).then(
                    function (data) {
                        vm.itemDetails = data;
                        var filesTab = document.getElementById("files");
                        var bomTab = document.getElementById("bom");
                        var whereUsedTab = document.getElementById("whereUser");
                        var mfrTab = document.getElementById("mfr");
                        var relatedItemsTab = document.getElementById("relatedItems");
                        var projectItemTab = document.getElementById("projectItem");
                        var requirementsTab = document.getElementById("requirements-tab");
                        var configuredTab = document.getElementById("configured");
                        var changesTab = document.getElementById("changesTab");
                        var varianceTab = document.getElementById("variance");
                        var qualityTab = document.getElementById("qualityTab");

                        if (filesTab != null) {
                            filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.itemDetails.itemFiles);
                        }
                        if (mfrTab != null) {
                            mfrTab.lastElementChild.innerHTML = vm.tabs.mfr.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.itemDetails.manufacturerParts);
                        }
                        if (relatedItemsTab != null) {
                            relatedItemsTab.lastElementChild.innerHTML = vm.tabs.relatedItems.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.itemDetails.relatedItems);
                        }
                        if (projectItemTab != null) {
                            projectItemTab.lastElementChild.innerHTML = vm.tabs.projectItem.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.itemDetails.projectItemsDtos);
                        }
                        if (requirementsTab != null) {
                            requirementsTab.lastElementChild.innerHTML = vm.tabs.requirements.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.itemDetails.requirements);
                        }
                        if (bomTab != null) {
                            bomTab.lastElementChild.innerHTML = vm.tabs.bom.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.itemDetails.bom);
                        }
                        if (whereUsedTab != null) {
                            whereUsedTab.lastElementChild.innerHTML = vm.tabs.whereUsed.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.itemDetails.whereUsedItems);
                        }
                        if (configuredTab != null) {
                            configuredTab.lastElementChild.innerHTML = vm.tabs.configured.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.itemDetails.configuredItems);
                        }
                        if (changesTab != null) {
                            changesTab.lastElementChild.innerHTML = vm.tabs.changes.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.itemDetails.changeItems);
                        }
                        if (varianceTab != null) {
                            varianceTab.lastElementChild.innerHTML = vm.tabs.variance.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.itemDetails.varianceItems);
                        }
                        if (qualityTab != null) {
                            qualityTab.lastElementChild.innerHTML = vm.tabs.quality.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.itemDetails.qualityItems);
                        }
                        vm.Minimize();
                        vm.Maximize();

                        loadItemDetailsExtensions();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

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
                vm.customTabs = [];
                vm.customActionGroups = [];
                vm.customActions = [];

                var context = {
                    item: vm.item,
                    itemRevision: vm.itemRevision
                };
                var extensions = $application.extensions;
                if (extensions != null && extensions !== undefined) {
                    var itemdetails = extensions.itemdetails;
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
                        var tabs = itemdetails.tabs;
                        if (tabs != null && tabs !== undefined && tabs.length > 0) {
                            var index = getLastTabIndexOfStandardTabs();
                            angular.forEach(tabs, function (tab) {
                                var show = true;

                                if (tab.filter != null && tab.filter !== undefined) {
                                    show = jexl.evalSync(tab.filter, context);
                                }
                                if (show) {
                                    index = index + 1;
                                    tab.index = index;
                                    tab.active = false;
                                    tab.activated = false;
                                    vm.customTabs.push(tab);
                                }
                            });
                        }
                    }
                }
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

            /*  ReInitialize ColResizable columns*/
            vm.reInitializeColResize = reInitializeColResize;
            function reInitializeColResize() {
                $(".JCLRgrips").css("display", "none");
                $timeout(function () {
                    $scope.$broadcast('reInitializeColResizable', "");
                }, 1000)
            }

            var newSearch = $translate.instant("ALL_VIEW_SAVE_SEARCH");
            var createButton = $translate.instant("CREATE");
            vm.showItemNos = parsed.html($translate.instant("SELECT_ITEM_NO_PER_PAGE")).html();
            vm.showItemMasterView = showItemMasterView;
            vm.showItemView = showItemView;

            function showItemMasterView() {
                vm.itemView = false;
                vm.itemMasterView = true;
            }

            vm.itemView = true;
            function showItemView() {
                vm.itemView = true;
                vm.itemMasterView = false;
                $rootScope.selectedMasterItemId = null;
                loadItems();
            }

            function showSaveSearch() {
                var options = {
                    title: newSearch,
                    template: 'app/desktop/modules/item/newSearchView.jsp',
                    controller: 'NewSearchController as newSearchVm',
                    resolve: 'app/desktop/modules/item/newSearchController',
                    width: 600,
                    data: {
                        search: vm.search
                    },
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.search.new'}
                    ],
                    callback: function () {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            $scope.saved = parsed.html($translate.instant("SAVED_SEARCH_TITLE")).html();

            function showItemSavedSearches() {
                var options = {
                    title: $scope.saved,
                    template: 'app/desktop/modules/item/showSavedSearches.jsp',
                    controller: 'SavedSearchesController as searchVm',
                    resolve: 'app/desktop/modules/item/savedSearchesController',
                    width: 700,
                    showMask: true,
                    data: {
                        type: "ITEM"
                    },
                    callback: function (result) {
                        var query = angular.fromJson(result.query);
                        if (result.searchType == 'freetext') {
                            freeTextSearch(query);
                        } else if (result.searchType == 'simple') {
                            searchItem(query);
                        } else if (result.searchType == 'advanced') {
                            advancedItemSearch(query);
                        }
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function loadSavedSearch(searchType, query) {
                if (searchType == 'freetext') {
                    if (query.itemClass == undefined) {
                        vm.itemFilter.itemClass = '';
                    }
                    freeTextSearch(query);
                } else if (searchType == 'simple') {
                    searchItem(query);
                } else if (searchType == 'advanced') {
                    advancedItemSearch(query);
                }
            }

            function updateFileNumbers() {
                RecentlyVisitedService.updateFileNumbers().then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            var initColumns = {
                "Item Number": {
                    "columnName": "Item Number",
                    "columnValue": null,
                    "columnType": "string"
                },
                "Item Class": {
                    "columnName": "Item Class",
                    "columnValue": null,
                    "columnType": "string"
                },
                "Item Type": {
                    "columnName": "Item Type",
                    "columnValue": null,
                    "columnType": "string"
                }
                ,
                "Item Name": {
                    "columnName": "Item Name",
                    "columnValue": null,
                    "columnType": "string"
                }
                ,
                "Description": {
                    "columnName": "Description",
                    "columnValue": null,
                    "columnType": "string"
                },

                "Revision": {
                    "columnName": "Revision",
                    "columnValue": null,
                    "columnType": "string"
                },
                "LifeCycle": {
                    "columnName": "LifeCycle",
                    "columnValue": null,
                    "columnType": "string"
                },
                "Make/Buy": {
                    "columnName": "Make/Buy",
                    "columnValue": null,
                    "columnType": "string"
                },
                "Released/Rejected Date": {
                    "columnName": "Released/Rejected Date",
                    "columnValue": null,
                    "columnType": "string"
                }
                ,

                "Units": {
                    "columnName": "Units",
                    "columnValue": null,
                    "columnType": "string"
                }
            };

            var inputParamHeaders = ["Item Number", "Item Class", "Item Type", "Item Name", "Description", "Revision", "LifeCycle", "Make/Buy", "Released/Rejected Date", "Units"];
            var inputParamHeaders1 = ["Artikelnummer", "Artikelklasse", "Artikeltyp", "Artikelname", "Beschreibung", "Revision", "Lebenszyklus", "Machen / Kaufen", "Freigabe/Abgelehnt datum", "Einheiten"];

            function exportItems() {
                var languageKey = $translate.storage().get($translate.storageKey());
                var exportRows = [];
                var empty = null;
                $rootScope.showBusyIndicator();
                var promise1 = "";
                if (vm.filterSearch == false && (searchMode == "" || searchMode == null || searchMode == undefined)) {
                    promise1 = ItemService.getTotalItems();
                } else if (vm.search.searchType == "freetext") {
                    promise1 = ItemService.freeTextSearchAll(vm.searchText, vm.itemsMode);
                } else if (vm.filterSearch) {
                    if (searchMode == "advanced") {
                        promise1 = ItemService.advancedSearchAllItems(advancedFilters);
                    } else if (searchMode == "simple") {
                        promise1 = ItemService.searchAllItems(simpleFilters);
                    }
                }

                promise1.then(
                    function (data) {
                        ItemService.getLatestRevisionReferences(data, 'latestRevision');
                        $timeout(function () {
                            for (var i = 0; i < data.length; i++) {
                                var exportRwDetails = [];
                                var emptyColumns = angular.copy(initColumns);
                                angular.forEach(inputParamHeaders, function (header) {
                                    empty = emptyColumns[header];
                                    if (empty != undefined) {
                                        var inputParamRow = data[i];
                                        if (empty.columnName == "Item Number" || empty.columnName == "Item Class" || empty.columnName == "Item Type" || empty.columnName == "Item Name" || empty.columnName == "Description"
                                            || empty.columnName == "Revision" || empty.columnName == "LifeCycle" || empty.columnName == "Make/Buy" || empty.columnName == "Released/Rejected Date" || empty.columnName == "Units") {
                                            if (empty.columnName == "Item Number") {
                                                empty.columnValue = inputParamRow.itemNumber;
                                            }
                                            if (empty.columnName == "Item Class") {
                                                empty.columnValue = inputParamRow.itemType.itemClass;
                                            }
                                            if (empty.columnName == "Item Type") {
                                                empty.columnValue = inputParamRow.itemType.name;
                                            }

                                            if (empty.columnName == "Item Name") {
                                                empty.columnValue = inputParamRow.itemName;
                                            }
                                            if (empty.columnName == "Description") {
                                                empty.columnValue = inputParamRow.description;
                                            }
                                            if (empty.columnName == "Revision") {
                                                empty.columnValue = inputParamRow.latestRevisionObject.revision;
                                            }
                                            if (empty.columnName == "LifeCycle") {
                                                empty.columnValue = inputParamRow.latestRevisionObject.lifeCyclePhase.phase;
                                            }
                                            if (empty.columnName == "Make/Buy") {
                                                empty.columnValue = inputParamRow.makeOrBuy;
                                            }
                                            if (empty.columnName == "Released/Rejected Date") {
                                                empty.columnValue = inputParamRow.latestRevisionObject.releasedDate;
                                            }
                                            if (empty.columnName == "Units") {
                                                empty.columnValue = inputParamRow.units;
                                            }

                                        }

                                        exportRwDetails.push(empty);
                                    }
                                });
                                var exporter = {
                                    exportRowDetails: exportRwDetails
                                };
                                exportRows.push(exporter);
                            }

                            if (languageKey == 'de') {
                                var exportObject = {
                                    "exportRows": exportRows,
                                    "fileName": "Artikel",
                                    "headers": angular.copy(inputParamHeaders1)
                                };
                            }
                            else {

                                var exportObject = {
                                    "exportRows": exportRows,
                                    "fileName": "Items",
                                    "headers": angular.copy(inputParamHeaders)
                                };

                            }

                            ItemService.exportItemReport("CSV", exportObject).then(
                                function (data) {
                                    var url = "{0}//{1}/api/itemexcel/exports/csvFile/".format(window.location.protocol, window.location.host);
                                    url += data + "/download";
                                    window.open(url);
                                    $rootScope.hideBusyIndicator();
                                    if (languageKey == 'de') {
                                        $rootScope.showSuccessMessage("Artikel werden erfolgreich exportiert");
                                    }
                                    else {
                                        $rootScope.showSuccessMessage("Items export successfully");
                                    }

                                    /* $rootScope.hideBusyIndicator();*/

                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }, data.length * 60);
                    }
                )

            }

            var newItemTitle = $translate.instant("NEW_ITEM_TITLE");

            function showNewItem() {
                $rootScope.showBusyIndicator();
                var options = {
                    title: newItemTitle,
                    template: 'app/desktop/modules/item/new/newItemView.jsp',
                    controller: 'NewItemController as newItemVm',
                    resolve: 'app/desktop/modules/item/new/newItemController',
                    width: 600,
                    showMask: true,
                    data: {
                        itemsMode: vm.itemsMode
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.items.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadItems();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);

            }

            var copyItemTitle = $translate.instant("COPY_ITEM");
            var itemCopiedMessage = $translate.instant("ITEM_COPIED_MESSAGE");

            vm.itemNumber = null;
            function saveAs(item) {
                vm.itemNumber = item.itemNumber;
                ItemService.getItem(item.id).then(
                    function (data) {
                        var itemData = data;
                        var options = {
                            title: copyItemTitle,
                            template: 'app/desktop/modules/item/new/itemCopyView.jsp',
                            controller: 'ItemCopyController as itemCopyVm',
                            resolve: 'app/desktop/modules/item/new/itemCopyController',
                            width: 500,
                            showMask: true,
                            data: {
                                itemData: itemData
                            },
                            buttons: [
                                {text: createButton, broadcast: 'app.items.copy'}
                            ],
                            callback: function (item) {
                                ItemService.copyItem(item).then(
                                    function (data) {
                                        loadItems();
                                        $rootScope.showSuccessMessage(vm.itemNumber + ' : ' + itemCopiedMessage + ' : ' + data.itemNumber);
                                        $rootScope.hideSidePanel();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                );
                            }
                        };
                        $rootScope.showSidePanel(options);
                    }
                )
            }

            var deleteDialogTitle = parsed.html($translate.instant("DELETE_ITEM")).html();
            var deleteItemDialogMessage = parsed.html($translate.instant("DELETE_ITEM_DIALOG_MESSAGE")).html();
            var itemDeletedMessage = parsed.html($translate.instant("ITEM_DELETED_MESSAGE")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            vm.configurableItem = parsed.html($translate.instant("CONFIGURABLE_ITEM")).html();
            vm.configuredItem = parsed.html($translate.instant("CONFIGURED_ITEM")).html();

            function deleteItem(item) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteItemDialogMessage + " [" + item.itemNumber + "]" + itemDelete + "?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ItemService.deleteItem(item.id).then(
                            function (data) {
                                var index = vm.items.content.indexOf(item);
                                vm.items.content.splice(index, 1);
                                $rootScope.showSuccessMessage(itemDeletedMessage);
                                loadItems();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };
            $rootScope.seletedItemId = null;

            function showItem(item, event) {
                if (event != undefined) {
                    event.stopPropagation();
                }
                if (item.latestReleasedRevision == null) {
                    $rootScope.seletedItemId = item.id;
                }
                else {
                    $rootScope.seletedItemId = item.latestReleasedRevision;
                }
                $rootScope.selectedMasterItemId = null;
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                vm.recentlyVisited.objectId = item.id;
                vm.recentlyVisited.objectType = "ITEM";
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        //$rootScope.masterDetailsEnable = false;
                        $state.go('app.items.details', {itemId: item.latestRevision, tab: 'details.basic'});
                    }, function (error) {
                        // $rootScope.masterDetailsEnable = false;
                        $state.go('app.items.details', {itemId: item.latestRevision, tab: 'details.basic'});
                    }
                );

            }

            function editItem(item) {
                $state.go('app.items.edit', {itemId: item.id});
            }

            var itemSimpleSearch = $translate.instant("ITEM_SIMPLE_SEARCH_TITLE");
            var searchButton = $translate.instant("SEARCH");

            function searchItem(filters) {
                if (filters.itemClass == undefined) {
                    filters.itemClass = '';
                } else {
                    vm.itemFilter.itemClass = filters.itemClass;
                }
                vm.itemFilter.itemName = filters.itemName;
                vm.itemFilter.itemNumber = filters.itemNumber;
                vm.itemFilter.description = filters.description;
                if (filters.itemType != null && filters.itemType != undefined) {
                    vm.itemFilter.itemType = filters.itemType.id;
                }
                vm.itemFilter.type = '';
                vm.itemFilter.phase = null;
                vm.itemFilter.revision = null;
                vm.selectedPhase = null;
                vm.selectedRevision = null;
                vm.selectedItemType = null;
                vm.search.searchType = "simple";
                searchMode = "simple";
                simpleFilters = filters;
                vm.search.query = angular.toJson(filters);
                if (vm.itemMode != null && vm.itemMode != "") filters.itemClass = vm.itemsMode;
                $rootScope.itemSimpleSearch = filters;
                $rootScope.itemAdvanceSearch = null;
                $stateParams.itemMode = null;
                $rootScope.showBusyIndicator($('.view-container'));
                ItemService.getAllItems(vm.pageable, vm.itemFilter).then(
                    function (data) {
                        vm.items = data;
                        vm.clear = true;
                        vm.loading = false;
                        vm.filterSearch = true;
                        $rootScope.showSearch = true;
                        $rootScope.searchModeType = true;
                        /*ItemService.getRevisionReferences(vm.items, 'id');
                         ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                         CommonService.getPersonReferences(vm.items.content, 'createdBy');
                         CommonService.getPersonReferences(vm.items.content, 'modifiedBy');*/
                        loadItemAttributeValues();
                        if (document.getElementById("freeTextSearchInput") != null)
                            document.getElementById("freeTextSearchInput").placeholder = "Simple Search";
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var itemAdvencedSearchTitle = $translate.instant("ITEM_ADVANCED_SEARCH_TITLE");
            vm.advancedItemSearchFilters = null;
            vm.filters = [];

            function advancedItemSearch(filters) {
                vm.search.searchType = "advanced";
                searchMode = "advanced";
                advancedFilters = filters;
                vm.search.query = angular.toJson(filters);
                if (vm.itemMode != null && vm.itemMode != "") {
                    angular.forEach(filters, function (filter) {
                        filter.itemClass = vm.itemsMode;
                    });
                }
                $rootScope.itemAdvanceSearch = filters;
                $rootScope.itemSimpleSearch = null;
                $stateParams.itemMode = null;
                $rootScope.showBusyIndicator($('.view-container'));
                ItemService.advancedSearchItem(vm.pageable, filters).then(
                    function (data) {
                        vm.items = data;
                        vm.filterSearch = true;
                        vm.clear = true;
                        vm.loading = false;
                        $rootScope.showSearch = true;
                        $rootScope.searchModeType = true;
                        /*ItemService.getRevisionReferences(vm.items.content, 'id');
                         ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                         CommonService.getPersonReferences(vm.items.content, 'createdBy');
                         CommonService.getPersonReferences(vm.items.content, 'modifiedBy');*/
                        loadItemAttributeValues();
                        if (document.getElementById("freeTextSearchInput") != null)
                            document.getElementById("freeTextSearchInput").placeholder = "Advanced Search";
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadItemAttributeValues() {
                vm.itemAttrIds = [];
                angular.forEach(vm.items.content, function (item) {
                    item.openPopover = false;
                    item.thumbnailImage = null;
                    vm.itemAttrIds.push(item.id);
                    vm.itemAttrIds.push(item.latestRevision);
                    if (item.hasThumbnail) {
                        item.thumbnailImage = "api/plm/items/" + item.id + "/itemImageAttribute/download?" + new Date().getTime();
                    }

                    if (item.lockObject) {
                        item.lockTitle = clickToUnlock;
                    } else {
                        item.lockTitle = clickToLock;
                    }
                });
                if (vm.selectedAttributes.length > 0) {
                    angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                        if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                            vm.attributeIds.push(selectedAttribute.id);
                        }
                    });
                    $rootScope.getObjectAttributeValues(vm.itemAttrIds, vm.attributeIds, vm.selectedAttributes, vm.items.content);
                }
            }

            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

            function showTypeAttributes() {
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/item/all/itemTypeAttributesView.jsp',
                    resolve: 'app/desktop/modules/item/all/itemTypeAttributesController',
                    controller: 'ItemTypeAttributesController as typeAttributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("attributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadItems();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("attributes", JSON.stringify(vm.selectedAttributes));
            }

            function freeTextSearchItems(freeText) {
                vm.pageable.page = 0;
                freeTextSearch(freeText)
            }

            function freeTextSearch(freeText) {
                searchMode = "freetext";
                vm.itemFilter = {
                    itemNumber: null,
                    itemName: null,
                    description: null,
                    searchQuery: null,
                    itemType: '',
                    type: '',
                    phase: null,
                    itemClass: vm.itemsMode,
                    revision: null
                };
                $scope.freeTextQuery = freeText;
                vm.itemFilter.searchQuery = freeText;
                vm.searchText = freeText;
                vm.loading = true;
                vm.items = angular.copy(pagedResults);
                $rootScope.itemFreeTextSearchText = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.search.searchType = "freetext";
                    vm.search.query = angular.toJson(freeText);
                    $rootScope.showBusyIndicator($('.view-container'));
                    ItemService.getAllItems(vm.pageable, vm.itemFilter).then(
                        function (data) {
                            vm.items = data;
                            vm.clear = true;
                            vm.loading = false;
                            $rootScope.showSearch = true;
                            $rootScope.searchModeType = true;
                            /*ItemService.getRevisionReferences(vm.items.content, 'id');
                             ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                             CommonService.getPersonReferences(vm.items.content, 'createdBy');
                             CommonService.getPersonReferences(vm.items.content, 'modifiedBy');*/
                            angular.forEach(vm.items.content, function (item) {
                                if (item.hasThumbnail) {
                                    item.thumbnailImage = "api/plm/items/" + item.id + "/itemImageAttribute/download?" + new Date().getTime();
                                }
                            })
                            loadItemAttributeValues();
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
                else {
                    vm.resetPage();
                    vm.itemFilter.searchQuery = null;
                    loadItems();
                    $rootScope.itemFreeTextSearchText = null;
                }
            }

            function selectAll() {
                vm.selecteditems = [];
                //vm.flag = !vm.flag;
                if (vm.flag) {
                    angular.forEach(vm.items.content, function (item) {
                            vm.selecteditems.push(item);
                            item.selected = true;
                            vm.showShareButton = true
                        }
                    )
                    vm.showCopyToClipBoard = true;
                } else {
                    vm.selecteditems = [];
                    vm.showShareButton = false;
                    angular.forEach(vm.items.content, function (item) {
                        item.selected = false;
                    })
                    vm.showCopyToClipBoard = false;
                }

            }

            vm.showShareButton = false;
            function toggleSelection(item) {
                if (item.selected == false) {
                    var index = vm.selecteditems.indexOf(item);
                    if (index != -1) {
                        vm.selecteditems.splice(index, 1);
                        if (vm.selecteditems.length == 0) {
                            vm.showShareButton = false;
                            vm.showCopyToClipBoard = false;
                        }
                        if (vm.items.content.length == vm.selecteditems.length) {
                            vm.flag = true;
                        } else {
                            vm.flag = false;
                        }
                    }
                }
                else {
                    vm.selecteditems.push(item);
                    if (vm.items.content.length == vm.selecteditems.length) {
                        vm.flag = true;
                    } else {
                        vm.flag = false;
                    }
                    vm.selection = 'selected';
                    vm.showShareButton = true;
                    vm.showCopyToClipBoard = true;
                }
            }


            vm.onSelectType = onSelectType;
            function onSelectType(itemType) {
                vm.pageable.page = 0;
                vm.selectedItemType = itemType;
                vm.itemFilter.type = itemType.id;
                loadItems();
            }

            vm.clearTypeSelection = clearTypeSelection;
            function clearTypeSelection() {
                $('.view-content').click();
                vm.pageable.page = 0;
                vm.selectedItemType = null;
                vm.itemFilter.type = '';
                loadItems();
            }

            vm.itemIds = [];
            vm.attributeIds = [];
            function loadItems() {
                $rootScope.showBusyIndicator();
                vm.items = angular.copy(pagedResults);
                searchMode = null;
                vm.clear = false;
                vm.loading = true;
                ItemService.getAllItems(vm.pageable, vm.itemFilter).then(
                    function (data) {
                        vm.items = data;
                        angular.forEach(vm.items.content, function (obj) {
                            if (obj.createdDate) {
                                obj.createdDatede = moment(obj.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                            if (obj.modifiedDate) {
                                obj.modifiedDatede = moment(obj.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                            if (obj.units == "Each") {
                                obj.units = Each;
                            }
                        });
                        vm.loading = false;
                        loadItemAttributeValues();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
                /*var promise = null;
                 if (vm.selectedItemType == null && (vm.itemsMode == "" || vm.itemsMode == null)) {
                 promise = ItemService.getItems(vm.pageable);
                 } else if (vm.itemsMode != null && vm.itemsMode != "") {
                 promise = ItemService.getItemsByClass(vm.itemsMode, vm.pageable);
                 } else {
                 promise = ItemService.getItemsByType(vm.selectedItemType.id, vm.pageable);
                 }

                 if (promise != null) {
                 promise.then(
                 function (data) {
                 vm.items = data;
                 angular.forEach(vm.items.content, function (obj) {
                 if (obj.createdDate) {
                 obj.createdDatede = moment(obj.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                 }
                 if (obj.modifiedDate) {
                 obj.modifiedDatede = moment(obj.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                 }
                 if (obj.units == "Each") {
                 obj.units = Each;
                 }
                 });
                 vm.loading = false;
                 /!*ItemService.getRevisionReferences(vm.items.content, 'id');
                 ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                 CommonService.getPersonReferences(vm.items.content, 'createdBy');
                 CommonService.getPersonReferences(vm.items.content, 'modifiedBy');*!/
                 loadItemAttributeValues();
                 $rootScope.hideBusyIndicator();
                 }, function (error) {
                 $rootScope.showErrorMessage(error.message);
                 $rootScope.hideBusyIndicator();
                 }
                 )
                 }*/
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

            function newItem() {
                $state.go('app.items.new');
            }

            function performSearch() {
                $rootScope.showBusyIndicator($('.view-container'));
                if (searchMode == null) {
                    loadItems();
                }
                else if (searchMode == "simple") {
                    searchItem(simpleFilters);
                }
                else if (searchMode == "advanced") {
                    advancedItemSearch(advancedFilters);
                }
                else if (searchMode == "freetext") {
                    freeTextSearch($scope.freeTextQuery);
                }
            }

            var itemsAlreadyExists = $translate.instant("ITEMS_ALREADY_EXISTS");
            var selectFolderItem = $translate.instant("SELECT_FOLDERITEM");
            var Folder = $translate.instant("FOLDER")
            var numbers = " ";
            var selectedNumbers = " ";


            function onSelectFolder(node) {
                $("#folderContextMenu").hide();
                if (node.attributes.type == 'PUBLIC') {
                    vm.publicfolderSelected = true;
                    vm.privatefolderSelected = false;
                    vm.folderSelected = false;
                }
                if (node.attributes.type == 'PRIVATE') {
                    vm.publicfolderSelected = false;
                    vm.privatefolderSelected = true;
                    vm.folderSelected = false;
                }
                if (node.attributes.type == 'FOLDER') {
                    vm.publicfolderSelected = false;
                    vm.privatefolderSelected = false;
                    vm.folderSelected = true;
                }
                var data = itemFolder.tree('getData', node.target);
                var folder = data.attributes.folder;
                if (!$scope.$$phase) $scope.$apply();
                if (folder != null && folder != undefined) {
                    if (vm.selecteditems.length > 0) {
                        vm.folder = folder;
                        var objectType = null;
                        vm.folderObjects = [];
                        vm.existedNumbers = [];
                        vm.selectedNumbers = [];
                        numbers = " ";
                        selectedNumbers = " ";
                        FolderService.getFolderObjects(folder).then(
                            function (data) {
                                initFoldersTree();
                                vm.existItems = data;
                                angular.forEach(vm.selecteditems, function (item) {
                                    vm.valid = true;
                                    angular.forEach(vm.existItems, function (existItem) {
                                        if (item.latestRevision == existItem.objectId) {
                                            vm.valid = false;
                                            if (vm.existedNumbers.length == 0) {
                                                numbers = numbers + " " + item.itemNumber;
                                            } else {
                                                numbers = numbers + ", " + item.itemNumber;
                                            }
                                            vm.existedNumbers.push(item);
                                            $rootScope.showErrorMessage(parsed.html($translate.instant("ITEMS_ALREADY_ADDED_TO_THE_FOLDER")).html());

                                            /*
                                             $rootScope.showErrorMessage(numbers + " : " + itemsAlreadyExists + " (" + vm.folder.name + ")" + Folder);
                                             */

                                        }
                                    })
                                    if (vm.valid) {
                                        var folderObject = {
                                            folder: 0,
                                            objectType: null,
                                            objectId: 0
                                        };
                                        folderObject.folder = vm.folder.id;
                                        folderObject.objectId = item.latestRevision;
                                        objectType = "ITEM";
                                        vm.folderObjects.push(folderObject);
                                        if (vm.selectedNumbers.length == 0) {
                                            selectedNumbers = selectedNumbers + " " + item.itemNumber;
                                        } else {
                                            selectedNumbers = selectedNumbers + ", " + item.itemNumber;
                                        }
                                        vm.selectedNumbers.push(item);
                                    }
                                });

                                if (vm.existedNumbers.length == 0) {
                                    FolderService.createFolderObject(vm.folderObjects, objectType).then(
                                        function (data) {

                                            $rootScope.showSuccessMessage(parsed.html($translate.instant("SELECTED_ITEMS_ADDED_TO_FOLDER_SUCCESS_MESSAGE")).html());
                                            /*$rootScope.showSuccessMessage(selectedNumbers + " : " + itemsAdded + "(" + vm.folder.name + ")" + folderSuccessfullyMessage);*/
                                            angular.forEach(vm.selecteditems, function (item) {
                                                item.selected = false;
                                                vm.existedNumbers = [];
                                                numbers = " ";
                                                vm.selecteditems = [];
                                                vm.flag = false;
                                            });
                                            vm.showCopyToClipBoard = false;
                                            vm.showShareButton = false;
                                            vm.selecteditems = [];
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    );
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }
                }
            }

            var folderSuccessfullyMessage = parsed.html($translate.instant("FOLDER_SUCCESS_MESSAGE")).html();
            var itemsAdded = parsed.html($translate.instant("ITEMS_ADDED_TO")).html();
            vm.RemoveColumnTitle = parsed.html($translate.instant("REMOVE_ATTRIBUTE_COLUMN")).html();
            vm.Subscribes = parsed.html($translate.instant("SUBSCRIBES")).html();

            vm.showAttributeDetails = showAttributeDetails;
            function showAttributeDetails(attribute) {
                if (attribute.objectType == 'ITEM') {
                    $state.go('app.items.details', {itemId: attribute.latestRevision});
                } else if (attribute.objectType == 'ITEMREVISION') {
                    $state.go('app.items.details', {itemId: attribute.id});
                } else if (attribute.objectType == 'CHANGE') {
                    $state.go('app.changes.ecos.details', {ecoId: attribute.id});
                } else if (attribute.objectType == 'PLMWORKFLOWDEFINITION') {
                    $state.go('app.workflow.editor', {mode: 'edit', workflow: attribute.id});
                } else if (attribute.objectType == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {manufacturerId: attribute.id});
                } else if (attribute.objectType == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: attribute.manufacturer,
                        manufacturePartId: attribute.id
                    });
                } else if (attribute.objectType == 'REQUIREMENT') {
                    $state.go('app.rm.requirements.details', {requirementId: attribute.value.refValue.id});
                } else if (attribute.objectType == 'PROJECT') {
                    $state.go('app.pm.project.details', {projectId: attribute.value.refValue.id});
                }
            }

            /*--------- To Download ATTACHMENT Attribute File  --------------*/

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".format(window.location.protocol, window.location.host,
                    attachment.id);
                $rootScope.downloadFileFromIframe(url);
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("attributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            var shareItem = $translate.instant("SHARE_ITEM");
            var shareButton = $translate.instant("SHARE");

            function shareSelectedItems() {
                var options = {
                    title: shareItem,
                    template: 'app/desktop/modules/shared/share/shareView.jsp',
                    controller: 'ShareController as shareVm',
                    resolve: 'app/desktop/modules/shared/share/shareController',
                    width: 600,
                    showMask: true,
                    data: {
                        selectedShareItems: vm.selecteditems,
                        itemsSharedType: 'itemsSelection',
                        objectType: "ITEM"
                    },
                    buttons: [
                        {text: shareButton, broadcast: 'app.shareSelectedItems.item'}
                    ],
                    callback: function (data) {
                        angular.forEach(vm.selecteditems, function (selectedItem) {
                            selectedItem.selected = false;
                        });
                        vm.showShareButton = false;
                        vm.selecteditems = [];
                        vm.flag = false;
                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.itemFilePopover = {
                templateUrl: 'app/desktop/modules/item/all/itemFilePopoverTemplate.jsp'
            };

            vm.subscribesPopover = {
                templateUrl: 'app/desktop/modules/item/all/subscribesPopoverTemplate.jsp'
            };

            vm.filePopover = filePopover;
            function filePopover(item) {
                item.openPopover = true;
            }

            vm.subscribePopover = subscribePopover;
            function subscribePopover(item) {
                item.openPopover = true;
            }

            vm.downloadFile = downloadFile;
            function downloadFile(file) {
                var url = "{0}//{1}/api/plm/items/{2}/files/{3}/download".format(window.location.protocol, window.location.host,
                    file.item.id, file.id);
                $rootScope.downloadFileFromIframe(url);
                $timeout(function () {
                    window.close();
                }, 1000);
                ItemFileService.updateFileDownloadHistory(file.item.id, file.id).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.lockItem = lockItem;

            function lockItem(item) {
                item.lockObject = !item.lockObject;
                ItemService.lockItem(item).then(
                    function (data) {
                        item.lockedBy = data.lockedBy.id;
                        item.lockedByName = data.lockedBy.fullName;
                        item.lockedDate = data.lockedDate;
                        if (item.lockObject) {
                            item.lockTitle = clickToUnlock;
                            $rootScope.showSuccessMessage($scope.itemLockedMsg);
                        } else {
                            item.lockTitle = clickToLock;
                            $rootScope.showSuccessMessage($scope.itemUnLockedMsg);
                        }
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            /*    Show Modal dialogue for RichText*/
            $scope.showModal = showModal;
            function showModal(data) {
                $("#myModalHorizontal").show();
                var mymodal = $('#myModalHorizontal');
                vm.modalValue = data
                mymodal.modal('show');
            }

            vm.changeSorting = changeSorting;
            function changeSorting(column) {
                var sort = vm.sort;
                vm.pageable.page = 0;
                if (sort.column == column) {
                    sort.descending = !sort.descending;

                    vm.pageable.sort.field = sort.column;
                    if (sort.descending) {
                        vm.pageable.sort.order = "DESC";
                    } else {
                        vm.pageable.sort.order = "ASC";
                    }
                    if ($scope.freeTextQuery != null) {
                        freeTextSearch($scope.freeTextQuery);
                    } else {
                        loadItems();
                    }

                } else {
                    sort.column = column;
                    vm.pageable.sort.field = sort.column;
                    vm.pageable.sort.order = "ASC";
                    if ($scope.freeTextQuery != null) {
                        freeTextSearch($scope.freeTextQuery);
                    } else {
                        loadItems();
                    }
                }
            }

            vm.sort = {
                column: '',
                descending: false

            };

            vm.copyToClipBoard = copyToClipBoard;
            function copyToClipBoard() {
                var releasedItems = 0;
                var configuredItems = 0;
                var addedItems = 0;
                var clipBoardMap = new Hashtable();
                angular.forEach(vm.clipBoardItems, function (item) {
                    clipBoardMap.put(item.id, item);
                })
                angular.forEach(vm.selecteditems, function (selectedItem) {
                    if (selectedItem.configured) {
                        selectedItem.selected = false;
                        configuredItems++;
                    } else if (selectedItem.lifeCyclePhase.phaseType == "OBSOLETE") {
                        selectedItem.selected = false;
                        releasedItems++;
                    } else {
                        addedItems++;
                        selectedItem.selected = false;
                        var clipboarItem = clipBoardMap.get(selectedItem.id);
                        if (clipboarItem == null) {
                            $application.clipboard.items.push(selectedItem);
                        }
                        if ($application.clipboard.deliverables.itemIds.indexOf(selectedItem.latestRevision) == -1) {
                            $application.clipboard.deliverables.itemIds.push(selectedItem.latestRevision);
                        }
                    }
                });
                $rootScope.clipBoardDeliverables.itemIds = $application.clipboard.deliverables.itemIds;
                vm.clipBoardItems = $application.clipboard.items;
                if (releasedItems > 0 || configuredItems > 0) {
                    if (addedItems > 0) {
                        $rootScope.showWarningMessage(itemAddedToClipboardAnd);
                    } else {
                        $rootScope.showWarningMessage(youCannotCopyReleasedItems);
                    }
                } else {
                    $rootScope.showSuccessMessage(itemAddedToClipboard);
                }
                vm.showCopyToClipBoard = false;
                vm.selecteditems = [];
                vm.flag = false;
            }

            vm.clearAndCopyToClipBoard = clearAndCopyToClipBoard;
            function clearAndCopyToClipBoard() {
                $application.clipboard.items = [];
                $application.clipboard.deliverables.itemIds = [];
                vm.clipBoardItems = [];
                copyToClipBoard();
            }

            vm.hasDisplayTab = hasDisplayTab;
            function hasDisplayTab(tabName) {
                var valid = true;

                if (vm.selectedItem != null && vm.selectedItem != undefined && vm.item != null && vm.item.itemType.tabs != null) {
                    var index = vm.item.itemType.tabs.indexOf(tabName);
                    if (index == -1) {
                        valid = false;
                    }
                }

                return valid;
            }

            $rootScope.loadItem = loadItem;
            vm.itemRevIdToCompare = null;
            $rootScope.checkBomAvailOrNot = false;
            function loadItem() {
                vm.loading = true;
                if ($rootScope.selectedMasterItemId != null && $rootScope.selectedMasterItemId != "") {

                    ItemService.getRevisionId($rootScope.selectedMasterItemId).then(
                        function (data) {
                            vm.itemRevIdToCompare = data.id;
                            $rootScope.seletedItemId = data.id;
                            vm.itemRevision = data;
                            $rootScope.itemRevision = data;
                            vm.inclusionRules = data.inclusionRules;
                            if (vm.itemRevision.plmeco == null) {
                                var ecoNumber = '-'
                            } else {
                                ecoNumber = vm.itemRevision.plmeco.ecoNumber;
                            }
                            $rootScope.checkBomAvailOrNot = vm.itemRevision.hasBom;
                            vm.lifeCycleStatus = vm.itemRevision.lifeCyclePhase.phaseType;
                            $rootScope.itemLifeCycleStatus = vm.lifeCycleStatus;
                            ItemService.getItem(vm.itemRevision.itemMaster).then(
                                function (data) {
                                    vm.item = data;
                                    $rootScope.item = vm.item;
                                    loadItemDetails();
                                    $rootScope.selectedItemInfo = data;
                                    if (vm.item.itemType.requiredEco) {
                                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                                            "{0}</div> <span class='item-rev'>Rev {1}</span> <span class='item-rev'>ECO ({2})</span>".format(vm.item.itemNumber,
                                                vm.itemRevision.revision, ecoNumber);
                                    } else {
                                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                                            "{0}</div> <span class='item-rev'>Rev {1}</span>".format(vm.item.itemNumber, vm.itemRevision.revision);
                                    }

                                    $rootScope.viewInfo.description = vm.item.itemName;

                                    loadItemDetailsExtensions();
                                    loadAllItemRevisions($rootScope.selectedMasterItemId);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
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


            /**
             *
             * Creating folder If not exist in the Items
             * */


            var itemNodeId = 0;
            var itemFolder = null;
            var itemRootNode = null;

            vm.addFolder = addFolder;
            vm.deleteFolder = deleteFolder;
            vm.removeItemFromFolder = removeItemFromFolder;

            var myFolders = parsed.html($translate.instant("MY_FOLDERS")).html();
            var newFolder = parsed.html($translate.instant("NEW_FOLDER")).html();
            var deleteFolderTitle = parsed.html($translate.instant("DELETE_FOLDER_TITLE")).html();
            var deleteFolderMessage = parsed.html($translate.instant("DELETE_FOLDER_MESSAGE")).html();
            var folderDeletedMessage = parsed.html($translate.instant("FOLDER_DELETED_MESSAGE")).html();
            var removeObjectFromFolderTitle = parsed.html($translate.instant("DELETE_OBJECT_FROM_FOLDER_TITLE")).html();
            var removeObjectFromFolderMessage = parsed.html($translate.instant("DELETE_OBJECT_FROM_FOLDER_MESSAGE")).html();
            var itemRemovedMessage = parsed.html($translate.instant("ITEM_REMOVED_MESSAGE")).html();
            var ecoRemovedMessage = parsed.html($translate.instant("ECO_REMOVED_MESSAGE")).html();
            var mfrRemovedMessage = parsed.html($translate.instant("MFR_REMOVED_MESSAGE")).html();
            var folder = parsed.html($translate.instant("FOLDER")).html();
            var folderNameCannotBeEmpty = parsed.html($translate.instant("FOLDER_NAME_CANNOT_BE_EMPTY")).html();

            function initFoldersTree() {
                itemNodeId = 0;
                itemFolder = $('#itemFolder').tree({
                    data: [
                        {
                            id: itemNodeId,
                            text: "Folders",
                            iconCls: 'folders-root',
                            attributes: {
                                type: 'ROOT'
                            },
                            children: []
                        }
                    ],
                    onSelect: onSelectFolder,
                    onContextMenu: onContextMenu,
                    onAfterEdit: onAfterEdit,
                    //onBeforeExpand: onBeforeExpand,
                    onDblClick: onDblClick
                });

                //rootNode1 = itemFolder.tree('find', 0);
                var roots = itemFolder.tree('getRoots');
                itemRootNode = roots.length ? roots[0] : null;
                $(document).click(function () {
                    $("#folderContextMenu").hide();
                });

                loadFolders();
            }

            vm.lastSelectedFolderName = null;
            function onDblClick(node) {
                vm.lastSelectedFolderName = null;
                if (node.attributes.type == 'ITEM') {
                    if (node.attributes.item != null && node.attributes.item != undefined) {
                        $state.go('app.items.details', {itemId: node.attributes.item.id});
                    }
                }
                else if (node.attributes.type == 'FOLDER') {
                    var data = itemFolder.tree('getData', node.target);
                    if (data.id != 0) {
                        vm.lastSelectedFolderName = data.text;
                        itemFolder.tree('beginEdit', node.target);
                    }
                    $timeout(function () {
                        $('.tree-editor').focus().select();
                    }, 1);
                }
            }

            vm.itemObjects = [];

            function onBeforeExpand(node) {
                var nodeData = itemFolder.tree('getData', node.target);
                if (!nodeData.attributes.loaded) {
                    vm.itemObjects = [];
                    var folder = nodeData.attributes.folder;
                    if (folder != null && folder != undefined) {
                        if (folder.items == null || folder.items == undefined) {
                            FolderService.getFolderObjects(folder).then(
                                function (data) {
                                    vm.folderContent = data;
                                    if (vm.folderContent.length > 0) {
                                        angular.forEach(vm.folderContent, function (content) {
                                            if (content.objectType == 'ITEM') {
                                                vm.itemObjects.push(content);
                                            }
                                        });
                                        if (vm.itemObjects.length > 0) {
                                            loadObjects(node, folder, vm.itemObjects);
                                        }

                                        nodeData.attributes.loaded = true;
                                    } else {
                                        removeLoadingNode(node);
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                }
                else {
                    removeLoadingNode(node);
                }
            }

            function loadObjects(node, folder, objects) {
                var itemIds = [];
                var map = new Hashtable();
                angular.forEach(objects, function (object) {
                    itemIds.push(object.objectId);
                    map.put(object.objectId, object)
                });

                if (itemIds.length > 0) {
                    ItemService.getRevisionsByIds(itemIds).then(
                        function (data) {
                            ItemService.getItemReferences(data, 'itemMaster', function () {
                                var newNodes = [];
                                angular.forEach(data, function (item) {
                                    var name = item.itemMasterObject.itemNumber + " : " + item.revision + " : " + item.lifeCyclePhase.phase;
                                    var itemNode = {
                                        id: ++nodeId,
                                        text: name,
                                        iconCls: 'folder-item',
                                        attributes: {
                                            item: item,
                                            type: 'ITEM',
                                            object: map.get(item.id)
                                        }
                                    };
                                    newNodes.push(itemNode);
                                });

                                removeLoadingNode(node);

                                itemFolder.tree('append', {
                                    parent: node.target,
                                    data: newNodes
                                });
                            });
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                else {
                    removeLoadingNode(node);
                }
            }


            function removeLoadingNode(node) {
                var loadingId = node.attributes.loadingId;
                if (loadingId != null && loadingId != undefined) {
                    var loadingNode = itemFolder.tree('find', loadingId);
                    if (loadingNode != null) {
                        itemFolder.tree('remove', loadingNode.target);
                    }
                }

            }

            function makeLoadingNode() {
                return {
                    id: ++itemNodeId,
                    text: "Loading...",
                    iconCls: 'items-loading',
                    type: 'LOADING'
                }
            }

            function onContextMenu(e, node) {
                var $contextMenu = $("#folderContextMenu");
                e.preventDefault();
                var menus = ['addFolderItem', 'deleteFolderItem'];
                angular.forEach(menus, function (menu) {
                    $('#' + menu).hide();
                });
                if (node.attributes.type == 'ROOT') {
                    $contextMenu.hide();
                    $('#addFolderItem').hide();
                } else if ((node.attributes.type == "PRIVATE" && vm.privatefolderSelected) || (node.attributes.type == "PUBLIC" && vm.publicfolderSelected)) {
                    $contextMenu.css({
                        display: "block",
                        left: e.pageX,
                        top: e.pageY
                    });
                    $('#addFolderItem').show();
                } else if (node.attributes.type == 'FOLDER' && vm.folderSelected) {
                    $contextMenu.css({
                        display: "block",
                        left: e.pageX,
                        top: e.pageY
                    });
                    $('#addFolderItem').show();
                    $('#deleteFolderItem').show();
                }
                else {
                    $contextMenu.css({
                        display: "none",
                        left: e.pageX,
                        top: e.pageY
                    });
                }


                $contextMenu.on("click", "a", function () {
                    $contextMenu.hide();
                });

                e.stopPropagation();
            }

            vm.selectedFolderName = null;
            function addFolder($event) {
                $event.preventDefault();
                $event.stopPropagation();
                var selectedNode = itemFolder.tree('getSelected');
                vm.selectedFolderName = selectedNode.text;
                if (selectedNode != null) {
                    var nodeid = ++itemNodeId;

                    itemFolder.tree('append', {
                        parent: selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: 'pdm-folder',
                                text: newFolder,
                                attributes: {
                                    folder: null,
                                    type: 'FOLDER'
                                }
                            }
                        ]
                    });

                    var newNode = itemFolder.tree('find', nodeid);
                    itemFolder.tree('expandTo', newNode.target);
                    if (newNode != null) {
                        itemFolder.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                        var $contextMenu = $("#folderContextMenu");
                        $contextMenu.hide();
                    }
                    if ($application.foldersData != null && selectedNode == rootNode) {
                        $application.foldersData.push(newNode);
                    }
                }
            }

            function deleteFolder($event) {
                $event.preventDefault();
                $event.stopPropagation();
                var options = {
                    title: deleteFolderTitle,
                    message: deleteFolderMessage,
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        var selectedNode = itemFolder.tree('getSelected');
                        if (selectedNode != null) {
                            var data = itemFolder.tree('getData', selectedNode.target);
                            if (data != null && data.attributes.folder != null) {
                                FolderService.deleteFolder(data.attributes.folder.id).then(
                                    function (data) {
                                        itemFolder.tree('remove', selectedNode.target);
                                        $rootScope.showSuccessMessage(folderDeletedMessage);
                                        var $contextMenu = $("#folderContextMenu");
                                        $contextMenu.hide();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    });
                                if ($application.foldersData != null) {
                                    var index = $application.foldersData.indexOf(selectedNode);
                                    $application.foldersData.splice(index, 1);
                                }
                            }
                        }
                    }
                });
            }

            function onAfterEdit(node) {

                var data = itemFolder.tree('getData', node.target);
                if (node.text == null || node.text == "") {
                    $timeout(function () {
                        $rootScope.showWarningMessage(folderNameCannotBeEmpty);

                        //data.attributes.folder.name = vm.lastSelectedFolderName;
                        //itemFolder.tree('append', data.target);
                        itemFolder.tree('beginEdit', node.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);

                        /*if (data.attributes.folder.id == undefined || data.attributes.folder.id == null) {
                         FolderService.createFolder(data.attributes.folder).then(
                         function (folderData) {

                         vm.lastSelectedFolderName = folderData.name;
                         node.text = folderData.name;
                         data.attributes.folder = angular.copy(folderData);
                         itemFolder.tree('update', {target: node.target, attributes: data.attributes});
                         itemFolder.tree('select', node.target);
                         }, function (error) {
                         $rootScope.showWarningMessage(error.message);
                         itemFolder.tree('remove', data.target);
                         }
                         )
                         } else {
                         FolderService.updateFolder(data.attributes.folder).then(
                         function (folderData) {
                         vm.lastSelectedFolderName = folderData.name;
                         node.text = folderData.name;
                         data.attributes.folder = angular.copy(folderData);
                         itemFolder.tree('update', {target: node.target, attributes: data.attributes});
                         itemFolder.tree('select', node.target);
                         }, function (error) {
                         $rootScope.showWarningMessage(error.message);
                         data.attributes.folder.name = vm.lastSelectedFolderName;
                         data.text = vm.lastSelectedFolderName;
                         itemFolder.tree('update', {target: node.target, attributes: data.attributes});
                         itemFolder.tree('select', node.target);
                         }
                         )
                         }*/

                    }, 10)

                    // itemFolder.tree('remove', data.target);
                }
                else {

                    if (data.attributes.folder == null) {
                        data.attributes.folder = {}
                    }

                    var parent = itemFolder.tree('getParent', node.target);
                    var parentData = itemFolder.tree('getData', parent.target);
                    //parentData.id = 0;

                    if (parentData.attributes.type == "PRIVATE" || parentData.attributes.type == "PUBLIC") {
                        data.attributes.folder.parent = null;
                        data.attributes.folder.type = parentData.attributes.type;
                    } else {
                        if (parentData.id != 0) {
                            data.attributes.folder.parent = parentData.attributes.folder.id;
                            data.attributes.folder.type = parentData.attributes.folder.type;
                        }
                    }

                    data.attributes.folder.name = node.text;

                    data.attributes.folder.owner = $application.login.person.id;

                    var promise = null;
                    if (data.attributes.folder.id == undefined || data.attributes.folder.id == null) {
                        FolderService.createFolder(data.attributes.folder).then(
                            function (folderData) {
                                data.attributes.folder = angular.copy(folderData);
                                itemFolder.tree('update', {target: node.target, attributes: data.attributes});
                                itemFolder.tree('select', node.target);
                            }, function (error) {
                                $rootScope.showWarningMessage(error.message);
                                itemFolder.tree('remove', data.target);
                            }
                        )
                    } else {
                        FolderService.updateFolder(data.attributes.folder).then(
                            function (folderData) {
                                data.attributes.folder = angular.copy(folderData);
                                itemFolder.tree('update', {target: node.target, attributes: data.attributes});
                                itemFolder.tree('select', node.target);
                            }, function (error) {
                                $rootScope.showWarningMessage(error.message);
                                data.attributes.folder.name = vm.lastSelectedFolderName;
                                data.text = vm.lastSelectedFolderName;
                                itemFolder.tree('update', {target: node.target, attributes: data.attributes});
                                itemFolder.tree('select', node.target);
                            }
                        )
                    }
                }

            }

            function makeFolderNode(folder, iconCls) {
                return {
                    id: ++itemNodeId,
                    text: folder.name,
                    iconCls: iconCls,
                    attributes: {
                        folder: folder,
                        type: 'FOLDER',
                        loaded: false
                    }
                };
            }

            function loadFolders() {
                FolderService.getFoldersTree($application.login.person.id).then(
                    function (data) {
                        var myFoldersRoot = {
                            id: ++itemNodeId,
                            text: "My Folders",
                            iconCls: 'private-folder',
                            attributes: {
                                root: true,
                                type: 'PRIVATE'
                            },
                            children: []
                        };

                        var publicFoldersRoot = {
                            id: ++itemNodeId,
                            text: "Public Folders",
                            iconCls: 'public-folder',
                            attributes: {
                                root: true,
                                type: 'PUBLIC'
                            },
                            children: []
                        };
                        var iconCls = "private-folder";
                        var myFolders = data.myFolders;
                        var nodes = [];
                        angular.forEach(myFolders, function (folder) {
                            var node = makeFolderNode(folder, iconCls);
                            if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, folder.children, iconCls);
                            }
                            /*else {
                             //node.state = 'closed';
                             node.children = [];
                             var loadingNode = makeLoadingNode();
                             node.children.push(loadingNode);
                             node.attributes.loadingId = loadingNode.id;
                             }*/

                            nodes.push(node);
                        });

                        myFoldersRoot.children = nodes;

                        itemFolder.tree('append', {
                            parent: itemRootNode.target,
                            data: myFoldersRoot
                        });

                        iconCls = "public-folder";
                        var publicFolders = data.publicFolders;
                        var publicNodes = [];
                        angular.forEach(publicFolders, function (folder) {
                            var node = makeFolderNode(folder, iconCls);
                            if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, folder.children, iconCls);
                            }
                            /*else {
                             //node.state = 'closed';
                             node.children = [];
                             var loadingNode = makeLoadingNode();
                             node.children.push(loadingNode);
                             node.attributes.loadingId = loadingNode.id;
                             }*/

                            publicNodes.push(node);
                        });

                        publicFoldersRoot.children = publicNodes;

                        itemFolder.tree('append', {
                            parent: itemRootNode.target,
                            data: publicFoldersRoot
                        });

                        //$application.foldersData = nodes;
                    }
                )

            }

            function visitChildren(parent, folders, iconCls) {
                var nodes = [];
                angular.forEach(folders, function (folder) {
                    var node = makeFolderNode(folder, iconCls);
                    node.children = [];
                    if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                        node.state = 'closed';
                        visitChildren(node, folder.children, iconCls);
                    }
                    /*else {
                     var loadingNode = makeLoadingNode();
                     node.children.push(loadingNode);
                     node.attributes.loadingId = loadingNode.id;
                     }*/

                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function removeItemFromFolder() {
                var options = {
                    title: removeObjectFromFolderTitle,
                    message: removeObjectFromFolderMessage,
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        var selectedNode = itemFolder.tree('getSelected');
                        if (selectedNode != null) {
                            var data = itemFolder.tree('getData', selectedNode.target);
                            var selectedType = data.attributes.type;
                            if (data.attributes.type == 'ITEM') {
                                vm.removeItemName = data.attributes.item.itemMasterObject.itemNumber;
                            }
                            FolderService.getFolderById(data.attributes.object.folder).then(
                                function (folderName) {
                                    vm.folderName = folderName.name;
                                    if (data != null) {
                                        FolderService.removeFolderObject(data.attributes.object.id).then(
                                            function (data) {
                                                itemFolder.tree('remove', selectedNode.target);
                                                if (selectedType == 'ITEM') {
                                                    $rootScope.showSuccessMessage(vm.removeItemName + " : " + itemRemovedMessage + "(" + vm.folderName + ")" + folder);
                                                }

                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        );
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )

                        }
                    }
                });
            }

            var revisionHistoryTitle = $translate.instant("REVISION_HISTORY_TITLE");
            vm.showItemRevisionHistory = showItemRevisionHistory;

            function showItemRevisionHistory(item) {
                var options = {
                    title: item.itemNumber + " - " + revisionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: item.id,
                        revisionHistoryType: "ITEM"
                    },
                    width: 700,
                    showMask: true,
                };

                $rootScope.showSidePanel(options);
            }

            vm.newButtonText = parsed.html($translate.instant("ITEM")).html();
            vm.itemSearchTitle = parsed.html($translate.instant("ITEM_SEARCH_TITLE")).html();


            vm.itemsSearch = itemsSearch;

            function itemsSearch() {
                var options = {
                    title: vm.itemSearchTitle,
                    template: 'app/desktop/modules/item/all/searchDialogueView.jsp',
                    controller: 'SearchDialogueController as searchDialogueVm',
                    resolve: 'app/desktop/modules/item/all/searchDialogueController',
                    width: 750,
                    showMask: true,
                    data: {
                        itemsMode: vm.itemsMode,
                        selectedType: "ITEM"
                    },
                    buttons: [
                        {text: searchButton, broadcast: 'app.items.search'}
                    ],
                    callback: function (filter) {
                        if (filter.length > 0) {
                            vm.resetPage();
                            vm.advancedItemSearchFilters = filter;
                            vm.filters = [];
                            angular.forEach(filter, function (item) {
                                var filter = {
                                    field: item.field.field,
                                    operator: item.operator.name,
                                    value: item.value,
                                    attributeId: item.attributeId
                                }
                                vm.filters.push(filter);
                            });
                            $timeout(function () {
                                advancedItemSearch(vm.filters);
                            }, 200);
                        } else {
                            vm.resetPage();
                            $rootScope.showBusyIndicator($('.view-container'));
                            searchItem(filter);
                        }
                    }
                };
                vm.mode = null;
                $rootScope.searchType = null;
                $rootScope.showSidePanel(options);
            }

            $rootScope.itemPrintClass = null;

            vm.getPrintOptions = getPrintOptions;
            function getPrintOptions(item) {
                $rootScope.itemPrintClass = item.itemClass;
                $rootScope.showPrintOptions(item.latestRevision, "ITEM");
            }

            vm.revisions = [];
            function loadItemUniqueRevisions() {
                ItemService.getUniqueRevisions().then(
                    function (data) {
                        vm.revisions = data;
                    }
                )
            }

            vm.lifecyclePhases = [];
            function loadItemTypeLifecycles() {
                vm.lifecyclePhases = [];
                ItemTypeService.getItemTypeLifecycles().then(
                    function (data) {
                        vm.itemTypeLifecycles = data;
                        angular.forEach(vm.itemTypeLifecycles, function (lifecycle) {
                            angular.forEach(lifecycle.phases, function (phase) {
                                if (vm.lifecyclePhases.indexOf(phase.phase) == -1) {
                                    vm.lifecyclePhases.push(phase.phase);
                                }
                            })
                        })
                    }
                )
            }

            vm.selectedPhase = null;
            vm.onSelectPhase = onSelectPhase;
            function onSelectPhase(phase) {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.pageable.page = 0;
                vm.selectedPhase = phase;
                vm.itemFilter.phase = phase;
                loadItems();
            }

            vm.selectedRevision = null;
            vm.onSelectRevision = onSelectRevision;
            function onSelectRevision(phase) {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.pageable.page = 0;
                vm.selectedRevision = phase;
                vm.itemFilter.revision = phase;
                loadItems();
            }

            vm.clearRevision = clearRevision;
            function clearRevision() {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.pageable.page = 0;
                vm.selectedRevision = null;
                vm.itemFilter.revision = null;
                loadItems();
            }


            vm.clearPhase = clearPhase;
            function clearPhase() {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.pageable.page = 0;
                vm.selectedPhase = null;
                vm.itemFilter.phase = null;
                loadItems();
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    if ($stateParams.itemMode != null && $stateParams.itemMode != "" && $stateParams.itemMode != undefined) {
                        //$rootScope.searchQuery = null;
                        vm.filterSearch = false;
                        $rootScope.itemFreeTextSearchText = null;
                        $rootScope.itemDetailsMode = $stateParams.itemMode;
                        vm.searchText = null;
                        if ($stateParams.itemMode == "products") {
                            vm.itemsMode = "PRODUCT";
                            vm.itemFilter.itemClass = "PRODUCT";
                            $scope.itemsTitle = parsed.html($translate.instant("PRODUCTS")).html();
                            vm.newButtonText = parsed.html($translate.instant("PRODUCT")).html();
                            vm.image = "app/assets/no_data_images/Products.png";
                        } else if ($stateParams.itemMode == "assemblies") {
                            vm.itemsMode = "ASSEMBLY";
                            vm.itemFilter.itemClass = "ASSEMBLY";
                            $scope.itemsTitle = parsed.html($translate.instant("ASSEMBLIES")).html();
                            vm.newButtonText = parsed.html($translate.instant("ASSEMBLY")).html();
                            vm.image = "app/assets/no_data_images/Assembly.png";
                        } else if ($stateParams.itemMode == "parts") {
                            vm.itemsMode = "PART";
                            vm.itemFilter.itemClass = "PART";
                            $scope.itemsTitle = parsed.html($translate.instant("PARTS")).html();
                            vm.newButtonText = parsed.html($translate.instant("PART")).html();
                            vm.image = "app/assets/no_data_images/Parts.png";
                        } else if ($stateParams.itemMode == "documents") {
                            vm.itemsMode = "DOCUMENT";
                            vm.itemFilter.itemClass = "DOCUMENT";
                            $scope.itemsTitle = parsed.html($translate.instant("DOCUMENTS")).html();
                            vm.newButtonText = parsed.html($translate.instant("DOCUMENT")).html();
                            vm.image = "app/assets/no_data_images/Documents.png";
                        } else if ($stateParams.itemMode == "others") {
                            vm.itemsMode = "OTHER";
                            vm.itemFilter.itemClass = "OTHER";
                            $scope.itemsTitle = parsed.html($translate.instant("OTHERS")).html();
                        } else {
                            $scope.itemsTitle = parsed.html($translate.instant("ITEMS_ALL_TITLE")).html();
                            vm.image = "app/assets/no_data_images/Items.png";
                        }
                    } else {
                        $scope.itemsTitle = parsed.html($translate.instant("ITEMS_ALL_TITLE")).html();
                        vm.image = "app/assets/no_data_images/Items.png";
                    }


                    $timeout(function () {
                        try {
                            initFoldersTree();
                        }
                        catch (e) {
                        }
                    }, 200);
                    ItemTypeService.getAllTypeAttributes("ITEMTYPE").then(
                        function (data) {
                            vm.typeAttributes = data;
                            angular.forEach(vm.typeAttributes, function (at1) {
                                vm.attributeIds.push(at1.id);
                            });

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        })
                    angular.forEach($application.currencies, function (data) {
                        currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                    })
                    if (validateJSON()) {
                        var setAttributes = JSON.parse($window.localStorage.getItem("attributes"));
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
                                    $window.localStorage.setItem("attributes", "");
                                    vm.selectedAttributes = setAttributes
                                } else {
                                    vm.selectedAttributes = setAttributes;
                                }

                                if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                                    //loadCustomItemAttributes();
                                    if ($rootScope.itemSimpleSearch != null && $rootScope.itemSimpleSearch != "" && $rootScope.itemSimpleSearch != undefined) {
                                        loadSavedSearch("simple", $rootScope.itemSimpleSearch);
                                    } else if ($rootScope.itemAdvanceSearch != null && $rootScope.itemAdvanceSearch != "" && $rootScope.itemAdvanceSearch != undefined) {
                                        loadSavedSearch("advanced", $rootScope.itemAdvanceSearch);
                                    } else if ($rootScope.itemFreeTextSearchText == null) {
                                        loadItems();
                                    } else {
                                        freeTextSearch($rootScope.itemFreeTextSearchText);
                                    }
                                } else if ($rootScope.allItemsLoad == false) {
                                    vm.query = JSON.parse($rootScope.searchQuery);
                                    loadSavedSearch(vm.mode, vm.query);
                                } else {
                                    loadItems();
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else {
                        if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                            //loadCustomItemAttributes();
                            if ($rootScope.itemSimpleSearch != null && $rootScope.itemSimpleSearch != "" && $rootScope.itemSimpleSearch != undefined) {
                                loadSavedSearch("simple", $rootScope.itemSimpleSearch);
                            } else if ($rootScope.itemAdvanceSearch != null && $rootScope.itemAdvanceSearch != "" && $rootScope.itemAdvanceSearch != undefined) {
                                loadSavedSearch("advanced", $rootScope.itemAdvanceSearch);
                            } else if ($rootScope.itemFreeTextSearchText == null) {
                                loadItems();
                            } else {
                                freeTextSearch($rootScope.itemFreeTextSearchText);
                            }
                        } else if ($rootScope.allItemsLoad == false) {
                            vm.query = JSON.parse($rootScope.searchQuery);
                            //$rootScope.searchQuery = null;
                            if (vm.mode != null || vm.mode != undefined) {
                                loadSavedSearch(vm.mode, vm.query);
                            }
                        } else {
                            loadItems();
                        }
                    }
                    loadItemTypeLifecycles();
                    loadItemUniqueRevisions();
                    $timeout(function () {
                        updateFileNumbers();
                    }, 2000)

                });
            })();
        }
    }
);
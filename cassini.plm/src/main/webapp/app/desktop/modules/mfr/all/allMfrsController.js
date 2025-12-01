define(['app/desktop/modules/mfr/mfr.module',
        'jquery-ui',
        'app/shared/services/core/mfrService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/desktop/modules/classification/directive/folderDirective',
        'app/desktop/modules/classification/directive/folderController',
        'app/shared/services/core/folderService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/desktop/modules/classification/workflowDirective/workflowManufacturerDirective'
    ],
    function (module) {
        module.controller('AllMfrsController', AllMfrsController);

        function AllMfrsController($scope, $rootScope, $timeout, $sce, $state, $stateParams, $window, $application, $cookieStore, $translate, CommonService,
                                   $uibModal, MfrService, DialogService, FolderService, ObjectTypeAttributeService, RecentlyVisitedService) {

            $rootScope.viewInfo.icon = "fa flaticon-office42";
            $rootScope.viewInfo.showDetails = false;
            var parse = angular.element("<div></div>");
            $rootScope.viewInfo.title = parse.html($translate.instant("MANUFACTURERS_TITLE")).html();

            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.loading = true;
            vm.newManufacture = newManufacture;
            vm.showNewManufacture = showNewManufacture;
            vm.showMfrDetails = showMfrDetails;
            vm.freeTextSearch = freeTextSearch;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.clearFilter = clearFilter;
            vm.selectCheck = selectCheck;
            vm.resetPage = resetPage;
            vm.selectAll = selectAll;
            vm.selectedMfrType = null;
            vm.showSaveSearch = showSaveSearch;
            $rootScope.showMfrSavedSearches = showMfrSavedSearches;
            var currencyMap = new Hashtable();
            $rootScope.searchModeType = false;
            vm.selectedmfr = [];

            vm.selection = null;
            $rootScope.showSearch = false;
            vm.mode = null;
            vm.mode = $rootScope.searchType;
            vm.flag = false;
            vm.deleteManufacturer = deleteManufacturer;
            vm.removeAttribute = removeAttribute;
            vm.privatefolderSelected = false;
            vm.folderSelected = false;
            vm.publicfolderSelected = false;

            vm.filters = {
                searchQuery: null,
                type: ''
            };
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
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

            vm.search = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                objectType: 'SAVEDSEARCH',
                searchObjectType: 'MANUFACTURER',
                type: 'PRIVATE',
                owner: null
            };

            if ($application.login != undefined && $application.login.person != null) {
                vm.person = $application.login.person;
                vm.search.owner = $application.login.person.id;
            }

            vm.objectIds = [];
            vm.selectedAttributes = [];
            vm.manufacturers = angular.copy(pagedResults);
            $scope.freeTextQuery = null;
            $rootScope.mfrFreeTextSearchText = '';
            var searchMode = null;
            vm.showTypeAttributes = showTypeAttributes;

            var newManufacturerTitle = parse.html($translate.instant("NEW_MANUFACTURER_TITLE")).html();
            var createButton = parse.html($translate.instant("CREATE")).html();
            var addButton = parse.html($translate.instant("ADD")).html();
            var selectedAttributesAdded = parse.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

            $scope.searchItemType = parse.html($translate.instant("ALL_VIEW_SEARCH")).html();
            vm.ecoAttributes = parse.html($translate.instant("ECO_ATTRIBUTES")).html();
            vm.savedSearchItems = parse.html($translate.instant("ALL_VIEW_SAVED_SEARCHES")).html();
            vm.saveSearchMfrs = parse.html($translate.instant("SAVED_SEARCH_MFR")).html();
            vm.mfrPartTitle = parse.html($translate.instant("CREATE_MFR_PARTS")).html();
            vm.mfrAttributesTitle = parse.html($translate.instant("MFR_ATTRIBUTES")).html();
            vm.mfrFolderTitle = parse.html($translate.instant("MFR_FOLDER_TITLE")).html();

            function showNewManufacture() {

                var options = {
                    title: newManufacturerTitle,
                    template: 'app/desktop/modules/mfr/new/newMfrView.jsp',
                    controller: 'NewMfrController as newMfrVm',
                    resolve: 'app/desktop/modules/mfr/new/newMfrController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.mfrs.new'}
                    ],
                    callback: function (mfr) {
                        //$rootScope.hideSidePanel();
                        loadingManufacturers();
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function showSaveSearch() {
                var options = {
                    title: searchTitle,
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

            var attributeTitle = $translate.instant("ATTRIBUTES");
            var searchTitle = $translate.instant("NEW_SEARCH");
            $scope.saved = parse.html($translate.instant("SAVED_SEARCH_TITLE")).html();
            function showMfrSavedSearches() {
                var options = {
                    title: $scope.saved,
                    template: 'app/desktop/modules/item/showSavedSearches.jsp',
                    controller: 'SavedSearchesController as searchVm',
                    resolve: 'app/desktop/modules/item/savedSearchesController',
                    width: 600,
                    showMask: true,
                    data: {
                        type: "MANUFACTURER"
                    },
                    callback: function (result) {
                        var query = angular.fromJson(result.query);
                        if (result.searchType == 'freetext') {
                            freeTextSearch(query);
                        }
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function showTypeAttributes() {

                var options = {
                    title: attributeTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        type: "MANUFACTURERTYPE",
                        objectType: "MANUFACTURER"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("mfrAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadingManufacturers();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if (vm.manufacturers.last != true) {
                    vm.pageable.page++;
                    if ($scope.mfrFreeTextSearchText != null && $scope.mfrFreeTextSearchText != "") {
                        MfrService.freeTextSearch(vm.pageable, $scope.mfrFreeTextSearchText).then(
                            function (data) {
                                vm.manufacturers = data;
                                loadMfrAttributeValues();
                                vm.clear = true;
                                $rootScope.showSearch = true;
                                vm.loading = false;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                    else {
                        $rootScope.showBusyIndicator($('body'));
                        vm.flag = false;
                        loadingManufacturers();
                    }
                }
            }

            function previousPage() {
                if (vm.manufacturers.first != true) {
                    vm.pageable.page--;

                    if ($scope.mfrFreeTextSearchText != null && $scope.mfrFreeTextSearchText != "") {
                        MfrService.freeTextSearch(vm.pageable, $scope.mfrFreeTextSearchText).then(
                            function (data) {
                                vm.manufacturers = data;
                                loadMfrAttributeValues();
                                vm.clear = true;
                                $rootScope.showSearch = true;
                                vm.loading = false;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                    else {
                        $rootScope.showBusyIndicator($('body'));
                        vm.flag = false;
                        loadingManufacturers();
                    }

                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;

                if ($scope.mfrFreeTextSearchText != null && $scope.mfrFreeTextSearchText != "") {
                    MfrService.freeTextSearch(vm.pageable, $scope.mfrFreeTextSearchText).then(
                        function (data) {
                            vm.manufacturers = data;
                            loadMfrAttributeValues();
                            vm.clear = true;
                            $rootScope.showSearch = true;
                            vm.loading = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
                else {
                    $rootScope.showBusyIndicator($('body'));
                    vm.flag = false;
                    loadingManufacturers();
                }


            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("mfrAttributes", JSON.stringify(vm.selectedAttributes));
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $scope.freeTextQuery = freeText;
                vm.searchText = freeText;
                $rootScope.mfrFreeTextSearchText = freeText;
                searchMode = "freetext";
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.search.searchType = "freetext";
                    vm.search.query = freeText;
                    $rootScope.searchModeType = true;
                    vm.search.query = angular.toJson(freeText);
                    MfrService.freeTextSearch(vm.pageable, freeText).then(
                        function (data) {
                            vm.manufacturers = data;
                            loadMfrAttributeValues();
                            vm.clear = true;
                            $rootScope.showSearch = true;
                            vm.loading = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    resetPage();
                    loadingManufacturers();
                    $rootScope.showSearch = false;
                    $rootScope.searchModeType = false;
                    $rootScope.mfrFreeTextSearchText = null;
                }
            }

            function clearFilter() {
                loadingManufacturers();
                vm.clear = false;
            }

            function resetPage() {
                vm.manufacturers = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.flag = false;
            }

            function newManufacture() {
                $state.go('app.mfr.new');
            }

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            function showMfrDetails(manufacturer) {
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                $state.go('app.mfr.details', {manufacturerId: manufacturer.id});
                vm.recentlyVisited.objectId = manufacturer.id;
                vm.recentlyVisited.objectType = manufacturer.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.mfrIds = [];
            vm.attributeIds = [];
            function loadingManufacturers() {
                vm.clear = false;
                vm.loading = true;
                MfrService.getManufacturers(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.manufacturers = data;
                        vm.loading = false;
                        angular.forEach(vm.manufacturers.content, function (mfr) {
                            mfr.newPhoneNumber = mfr.phoneNumber;
                            mfr.newDescription = mfr.description;
                            mfr.newContactPerson = mfr.contactPerson;
                            mfr.editMode = false;
                        })
                        $rootScope.hideBusyIndicator();
                        loadMfrAttributeValues();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            vm.attributeIds = [];
            function loadMfrAttributeValues() {
                angular.forEach(vm.manufacturers.content, function (mfr) {
                    vm.mfrIds.push(mfr.id);
                });
                if (vm.selectedAttributes != null && vm.selectedAttributes.length > 0) {
                    angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                        if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                            vm.attributeIds.push(selectedAttribute.id);
                        }
                    });
                    $rootScope.getObjectAttributeValues(vm.mfrIds, vm.attributeIds, vm.selectedAttributes, vm.manufacturers.content);
                }
            }

            vm.showShareButton = false;
            function selectAll() {
                vm.selecteditems = [];
                if (vm.flag) {
                    angular.forEach(vm.manufacturers.content, function (item) {
                            vm.selectedmfr.push(item);
                            item.selected = true;
                            vm.showShareButton = true
                        }
                    )
                    vm.showCopyToClipBoard = true;
                } else {
                    vm.selectedmfr = [];
                    vm.showShareButton = false;
                    angular.forEach(vm.manufacturers.content, function (item) {
                        item.selected = false;
                    })
                    vm.showCopyToClipBoard = false;
                }

            }

            function selectCheck(manufacturer) {
                if (manufacturer.selected == false) {
                    var index = vm.selectedmfr.indexOf(manufacturer);
                    if (index != -1) {
                        vm.selectedmfr.splice(index, 1);
                        if (vm.selectedmfr.length == 0) {
                            vm.showShareButton = false;
                        }
                        if (vm.manufacturers.content.length == vm.selectedmfr.length) {
                            vm.flag = true;
                        } else {
                            vm.flag = false;
                        }
                    }
                }
                else {
                    vm.selectedmfr.push(manufacturer);
                    if (vm.manufacturers.content.length == vm.selectedmfr.length) {
                        vm.flag = true;
                    } else {
                        vm.flag = false;
                    }
                    vm.selection = 'selected';
                    vm.showShareButton = true;
                }
            }

            var numbers = " ";
            var selectedMfrNumbers = " ";
            vm.existedMfrNumbers = [];
            vm.selectedMfrNumbers = [];
            var foldersuccessfully = parse.html($translate.instant("FOLDER_SUCCESS_MESSAGE")).html();
            var Mfraddedto = parse.html($translate.instant("MFRS_ADDED_TO")).html();
            var MfrsAlreadyExists = parse.html($translate.instant("MFRS_ALREADY_EXISTS")).html();
            var Folder = parse.html($translate.instant("FOLDER")).html();
            var selectFolderMfr = parse.html($translate.instant("SELECT_FOLDERMFR")).html();


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
                var data = mfrFolder.tree('getData', node.target);
                var folder = data.attributes.folder;
                if (!$scope.$$phase) $scope.$apply();
                if (folder != null && folder != undefined) {
                    if (vm.selectedmfr.length > 0) {
                        vm.folder = folder;
                        var objectType = null;
                        vm.folderObjects = [];
                        vm.existedMfrNumbers = [];
                        vm.selectedMfrNumbers = [];
                        numbers = " ";
                        selectedMfrNumbers = " ";
                        FolderService.getFolderObjects(folder).then(
                            function (data) {
                                initFoldersTree();
                                vm.existedMfrs = data;
                                angular.forEach(vm.selectedmfr, function (mfrSelected) {
                                    vm.valid = true;
                                    angular.forEach(vm.existedMfrs, function (existedMfr) {
                                        if (mfrSelected.id == existedMfr.objectId) {
                                            vm.valid = false;
                                            if (vm.existedMfrNumbers.length == 0) {
                                                numbers = numbers + " " + mfrSelected.name;
                                            } else {
                                                numbers = numbers + ", " + mfrSelected.name;
                                            }
                                            vm.existedMfrNumbers.push(mfrSelected);
                                            $rootScope.showErrorMessage(numbers + " : " + MfrsAlreadyExists + " (" + vm.folder.name + ")" + Folder);
                                        }
                                    })
                                    if (vm.valid) {
                                        var folderObject = {
                                            folder: 0,
                                            objectType: null,
                                            objectId: 0
                                        };
                                        folderObject.folder = vm.folder.id;
                                        folderObject.objectId = mfrSelected.id;
                                        objectType = mfrSelected.objectType;
                                        vm.folderObjects.push(folderObject);
                                        if (vm.selectedMfrNumbers.length == 0) {
                                            selectedMfrNumbers = selectedMfrNumbers + " " + mfrSelected.name;
                                        } else {
                                            selectedMfrNumbers = selectedMfrNumbers + ", " + mfrSelected.name;
                                        }
                                        vm.selectedMfrNumbers.push(mfrSelected);
                                    }
                                })
                                if (vm.existedMfrNumbers.length == 0) {
                                    FolderService.createFolderObject(vm.folderObjects, objectType).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(selectedMfrNumbers + " : " + Mfraddedto + "(" + vm.folder.name + ")" + foldersuccessfully);
                                            angular.forEach(vm.selectedmfr, function (manufacturer) {
                                                manufacturer.selected = false;
                                                vm.existedNumbers = [];
                                                numbers = " ";
                                                selectedMfrNumbers = " ";
                                                vm.selectedmfr = [];
                                                vm.flag = false;
                                            })
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    );
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                    /* else {
                     $rootScope.showWarningMessage(selectFolderMfr);
                     }*/
                }
            };

            var deleteManufacturerTitle = parse.html($translate.instant("DELETE_MANUFACTURER")).html();
            var deleteManufacturerMessage = parse.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var manufacturer = parse.html($translate.instant("MANUFACTURER")).html();
            var manufacturerDeletedMessage = parse.html($translate.instant("MANUFACTURER_DELETED_MESSAGE")).html();

            function deleteManufacturer(manufacturer) {
                var options = {
                    title: deleteManufacturerTitle,
                    message: deleteManufacturerMessage + " (" + manufacturer.name + ") ?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        MfrService.deleteManufacture(manufacturer.id).then(
                            function (data) {
                                var index = vm.manufacturers.content.indexOf(manufacturer);
                                vm.manufacturers.content.splice(index, 1);
                                vm.manufacturers.totalElements--;
                                $rootScope.showErrorMessage(manufacturerDeletedMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }


            function loadSavedSearch(searchType, query) {
                if (searchType == 'freetext') {
                    freeTextSearch(query);
                }
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("mfrAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.createParts = createParts;
            var mfrPartTitle = parse.html($translate.instant("NEW_MANUFACTURER_PART_TITLE")).html();

            function createParts() {
                var options = {
                    title: mfrPartTitle,
                    template: 'app/desktop/modules/mfr/all/newManufacturerPartView.jsp',
                    resolve: 'app/desktop/modules/mfr/all/newManufacturerPartController',
                    controller: 'NewManufacturerPartController as newManufacturerPartVm',
                    width: 600,
                    showMask: true,
                    data: {},
                    buttons: [
                        {text: createButton, broadcast: 'app.mfr.mfrPart.new'}
                    ],
                    callback: function (result) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            /*    Show Modal dialogue for RichText*/
            $scope.showModal = showModal;
            function showModal(data) {
                $("#myModalHorizontal").show();
                var mymodal = $('#myModalHorizontal');
                vm.modalValue = data
                mymodal.modal('show');
            }


            /**
             *
             * Creating folder If not exist in the Mfrs
             * */


            var itemNodeId = 0;
            var mfrFolder = null;
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
                mfrFolder = $('#mfrFolder').tree({
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

                var roots = mfrFolder.tree('getRoots');
                itemRootNode = roots.length ? roots[0] : null;
                $(document).click(function () {
                    $("#folderContextMenu").hide();
                });

                loadFolders();
            }

            vm.lastSelectedFolderName = null;
            function onDblClick(node) {
                vm.lastSelectedFolderName = null;
                if (node.attributes.type == 'MANUFACTURER') {
                    if (node.attributes.mfr != null && node.attributes.mfr != undefined) {
                        $state.go('app.mfr.details', {manufacturerId: node.attributes.mfr.id});
                    }
                }
                else if (node.attributes.type == 'FOLDER') {
                    var data = mfrFolder.tree('getData', node.target);
                    if (data.id != 0) {
                        vm.lastSelectedFolderName = data.text;
                        mfrFolder.tree('beginEdit', node.target);
                    }
                    $timeout(function () {
                        $('.tree-editor').focus().select();
                    }, 1);
                }
            }

            vm.itemObjects = [];
            vm.ecoObjects = [];
            vm.mfrObjects = [];

            vm.clearTypeSelection = clearTypeSelection;
            function clearTypeSelection() {
               
                vm.pageable.page = 0;
                vm.selectedMfrType = null;
                vm.filters.type = '';
                loadingManufacturers();
            }


            vm.onSelectType = onSelectType;
            function onSelectType(mfrType) {
                vm.pageable.page = 0;
                vm.selectedMfrType = mfrType;
                vm.filters.type = mfrType.id;
                loadingManufacturers();
            }

            function onBeforeExpand(node) {
                var nodeData = mfrFolder.tree('getData', node.target);
                if (!nodeData.attributes.loaded) {
                    vm.itemObjects = [];
                    vm.ecoObjects = [];
                    vm.mfrObjects = [];
                    var folder = nodeData.attributes.folder;
                    if (folder != null && folder != undefined) {
                        if (folder.items == null || folder.items == undefined) {
                            FolderService.getFolderObjects(folder).then(
                                function (data) {
                                    vm.folderContent = data;
                                    if (vm.folderContent.length > 0) {
                                        angular.forEach(vm.folderContent, function (content) {
                                            if (content.objectType == 'MANUFACTURER') {
                                                vm.mfrObjects.push(content);
                                            }
                                        });
                                        if (vm.mfrObjects.length > 0) {
                                            loadMfrObjects(node, folder, vm.mfrObjects);
                                        }

                                        nodeData.attributes.loaded = true;
                                    } else {
                                        removeLoadingNode(node);
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                }
                else {
                    removeLoadingNode(node);
                }
            }


            function loadMfrObjects(node, folder, objects) {
                var mfrIds = [];
                var map = new Hashtable();
                angular.forEach(objects, function (object) {
                    mfrIds.push(object.objectId);
                    map.put(object.objectId, object)
                });

                if (mfrIds.length > 0) {
                    MfrService.getMultipleManufacturers(mfrIds).then(
                        function (data) {
                            var newNodes = [];
                            angular.forEach(data, function (mfr) {
                                var name = mfr.name;
                                var itemNode = {
                                    id: ++itemNodeId,
                                    text: name,
                                    iconCls: 'folder-mfr',
                                    attributes: {
                                        mfr: mfr,
                                        type: 'MANUFACTURER',
                                        object: map.get(mfr.id)
                                    }
                                };
                                newNodes.push(itemNode);
                            });

                            removeLoadingNode(node);

                            mfrFolder.tree('append', {
                                parent: node.target,
                                data: newNodes
                            });

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
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
                    var loadingNode = mfrFolder.tree('find', loadingId);
                    if (loadingNode != null) {
                        mfrFolder.tree('remove', loadingNode.target);
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
                var menus = ['addFoldermfr', 'deleteFoldermfr'];
                angular.forEach(menus, function (menu) {
                    $('#' + menu).hide();
                });
                if (node.attributes.type == 'ROOT') {
                    $contextMenu.hide();
                    $('#addFoldermfr').hide();
                }
                else if ((node.attributes.type == "PRIVATE" && vm.privatefolderSelected) || (node.attributes.type == "PUBLIC" && vm.publicfolderSelected)) {
                    $contextMenu.css({
                        display: "block",
                        left: e.pageX,
                        top: e.pageY
                    });
                    $('#addFoldermfr').show();
                }
                else if (node.attributes.type == 'FOLDER' && vm.folderSelected) {
                    $contextMenu.css({
                        display: "block",
                        left: e.pageX,
                        top: e.pageY
                    });
                    $('#addFoldermfr').show();
                    $('#deleteFoldermfr').show();
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
                var selectedNode = mfrFolder.tree('getSelected');
                vm.selectedFolderName = selectedNode.text;
                if (selectedNode != null) {
                    var nodeid = ++itemNodeId;

                    mfrFolder.tree('append', {
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

                    var newNode = mfrFolder.tree('find', nodeid);
                    mfrFolder.tree('expandTo', newNode.target);
                    if (newNode != null) {
                        mfrFolder.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                        var $contextMenu = $("#folderContextMenu");
                        $contextMenu.hide();
                    }
                    if ($application.foldersData != null && selectedNode == itemRootNode) {
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
                        var selectedNode = mfrFolder.tree('getSelected');
                        if (selectedNode != null) {
                            var data = mfrFolder.tree('getData', selectedNode.target);
                            if (data != null && data.attributes.folder != null) {
                                FolderService.deleteFolder(data.attributes.folder.id).then(
                                    function (data) {
                                        mfrFolder.tree('remove', selectedNode.target);
                                        $rootScope.showSuccessMessage(folderDeletedMessage);
                                        var $contextMenu = $("#folderContextMenu");
                                        $contextMenu.hide();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
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

                var data = mfrFolder.tree('getData', node.target);
                if (node.text == null || node.text == "") {
                    $timeout(function () {
                        $rootScope.showWarningMessage(folderNameCannotBeEmpty);

                        mfrFolder.tree('beginEdit', node.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }, 10);

                }
                else {

                    if (data.attributes.folder == null) {
                        data.attributes.folder = {}
                    }

                    var parent = mfrFolder.tree('getParent', node.target);
                    var parentData = mfrFolder.tree('getData', parent.target);
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
                                mfrFolder.tree('update', {target: node.target, attributes: data.attributes});
                                mfrFolder.tree('select', node.target);
                            }, function (error) {
                                $rootScope.showWarningMessage(error.message);
                                mfrFolder.tree('remove', data.target);
                            }
                        )
                    } else {
                        FolderService.updateFolder(data.attributes.folder).then(
                            function (folderData) {
                                data.attributes.folder = angular.copy(folderData);
                                mfrFolder.tree('update', {target: node.target, attributes: data.attributes});
                                mfrFolder.tree('select', node.target);
                            }, function (error) {
                                $rootScope.showWarningMessage(error.message);
                                data.attributes.folder.name = vm.lastSelectedFolderName;
                                data.text = vm.lastSelectedFolderName;
                                mfrFolder.tree('update', {target: node.target, attributes: data.attributes});
                                mfrFolder.tree('select', node.target);
                            }
                        )
                    }
                }

            }

            function makeNode(folder, iconCls) {
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
                            var node = makeNode(folder, iconCls);
                            if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, folder.children, iconCls);
                            }

                            nodes.push(node);
                        });

                        myFoldersRoot.children = nodes;

                        mfrFolder.tree('append', {
                            parent: itemRootNode.target,
                            data: myFoldersRoot
                        });

                        iconCls = "public-folder";
                        var publicFolders = data.publicFolders;
                        var publicNodes = [];
                        angular.forEach(publicFolders, function (folder) {
                            var node = makeNode(folder, iconCls);
                            if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, folder.children, iconCls);
                            }

                            publicNodes.push(node);
                        });

                        publicFoldersRoot.children = publicNodes;

                        mfrFolder.tree('append', {
                            parent: itemRootNode.target,
                            data: publicFoldersRoot
                        });

                        //$application.foldersData = nodes;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

            }

            function visitChildren(parent, folders, iconCls) {
                var nodes = [];
                angular.forEach(folders, function (folder) {
                    var node = makeNode(folder, iconCls);
                    node.children = [];
                    if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                        node.state = 'closed';
                        visitChildren(node, folder.children, iconCls);
                    }

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
                        var selectedNode = mfrFolder.tree('getSelected');
                        if (selectedNode != null) {
                            var data = mfrFolder.tree('getData', selectedNode.target);
                            var selectedType = data.attributes.type;
                            if (data.attributes.type == 'MANUFACTURER') {
                                vm.removeItemName = data.attributes.mfr.name;
                            }
                            FolderService.getFolderById(data.attributes.object.folder).then(
                                function (folderName) {
                                    vm.folderName = folderName.name;
                                    if (data != null) {
                                        FolderService.removeFolderObject(data.attributes.object.id).then(
                                            function (data) {
                                                mfrFolder.tree('remove', selectedNode.target);
                                                if (selectedType == 'MANUFACTURER') {
                                                    $rootScope.showSuccessMessage(vm.removeItemName + ":" + mfrRemovedMessage + "(" + vm.folderName + ")" + folder);
                                                }

                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        );
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )

                        }
                    }
                });
            }

            vm.editMfr = editMfr;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            function editMfr(mfr) {
                mfr.editMode = true;
                mfr.newPhoneNumber = mfr.phoneNumber;
                mfr.newDescription = mfr.description;
                mfr.newContactPerson = mfr.contactPerson;
            }


            function cancelChanges(mfr) {
                mfr.phoneNumber = mfr.newPhoneNumber;
                mfr.description = mfr.newDescription;
                mfr.contactPerson = mfr.newContactPerson;
                mfr.editMode = false;

            }

            var mfrUpdateItem = parse.html($translate.instant("MANUFACTURER_UPDATE_MFR_MESSAGE")).html();

            function applyChanges(mfr) {
                //mfr.phoneNumber = mfr.newPhoneNumber;
                //mfr.description = mfr.newDescription;
                //mfr.contactPerson = mfr.newContactPerson;
                MfrService.updateManufacture(mfr).then(
                    function (data) {
                        $rootScope.showSuccessMessage(mfrUpdateItem);
                        mfr.editMode = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    });
            }

            var shareItem = parsed.html($translate.instant("SHARE_MFR")).html();
            var shareButton = parsed.html($translate.instant("SHARE")).html();
            vm.shareSelectedMfrs = shareSelectedMfrs;
            function shareSelectedMfrs() {
                var options = {
                    title: shareItem,
                    template: 'app/desktop/modules/shared/share/shareView.jsp',
                    controller: 'ShareController as shareVm',
                    resolve: 'app/desktop/modules/shared/share/shareController',
                    width: 600,
                    showMask: true,
                    data: {
                        selectedShareItems: vm.selectedmfr,
                        itemsSharedType: 'mfrsSelection',
                        objectType: "MANUFACTURER"
                    },
                    buttons: [
                        {text: shareButton, broadcast: 'app.shareSelectedItems.item'}
                    ],
                    callback: function (data) {
                        angular.forEach(vm.selectedmfr, function (selectedItem) {
                            selectedItem.selected = false;
                        });
                        vm.showShareButton = false;
                        vm.selectedmfr = [];
                        vm.flag = false;
                    }
                };
                $rootScope.showSidePanel(options);
            }

            (function () {


                //if ($application.homeLoaded == true) {
                $timeout(function () {
                    try {
                        initFoldersTree();
                    }
                    catch (e) {
                    }
                }, 200);

                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })

                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("mfrAttributes"));
                } else {
                    var setAttributes = null;
                }
                if (setAttributes != null && setAttributes != undefined) {
                    angular.forEach(setAttributes, function (setAtt) {
                        vm.objectIds.push(setAtt.id);
                    })
                    ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                        function (data) {
                            if (data.length == 0) {
                                setAttributes = null;
                                $window.localStorage.setItem("mfrAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                                if ($rootScope.mfrFreeTextSearchText == null) {
                                    loadingManufacturers();
                                } else {
                                    freeTextSearch($rootScope.mfrFreeTextSearchText);
                                }
                            } else {
                                vm.query = JSON.parse($stateParams.queryData);
                                loadSavedSearch(vm.mode, vm.query);
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                        if ($rootScope.mfrFreeTextSearchText == null) {
                            loadingManufacturers();
                        } else {
                            freeTextSearch($rootScope.mfrFreeTextSearchText);
                        }

                    } else {
                        vm.query = JSON.parse($rootScope.searchQuery);
                        $rootScope.searchQuery = null;
                        loadSavedSearch(vm.mode, vm.query);
                    }
                }
                ObjectTypeAttributeService.getObjectTypeAttributesByType('MANUFACTURERTYPE').then(
                    function (data) {
                        vm.typeAttributes = data;
                        angular.forEach(vm.typeAttributes, function (at1) {
                            vm.attributeIds.push(at1.id);
                        });
                    })
                //}
            })();
        }
    }
);

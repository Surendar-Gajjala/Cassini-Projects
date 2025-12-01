define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/classification/directive/folderDirective',
        'app/desktop/modules/classification/directive/folderController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/folderService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/requirementService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/shared/services/core/qualityTypeService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/desktop/modules/directives/changeObjectTypeDirective',
    ],
    function (module) {
        module.controller('ECOsController', ECOsController);

        function ECOsController($scope, $rootScope, $window, $sce, $cookieStore, $timeout, $state, $stateParams, $translate, $application, $cookies, AttributeAttachmentService,
                                ECOService, WorkflowService, CommonService, MfrPartsService, MfrService, WorkflowDefinitionService, RequirementService, QualityTypeService,
                                DialogService, FolderService, ItemService, ObjectTypeAttributeService, RecentlyVisitedService, ProjectService, SpecificationsService) {
            var vm = this;
            $rootScope.viewInfo.icon = "fa fa-ils";
            $rootScope.viewInfo.title = "ECOs";
            $rootScope.viewInfo.showDetails = false;
            var parsed = angular.element("<div></div>");
            var currencyMap = new Hashtable();
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

            vm.filters = {

                ecoNumber: null,
                ecoType: '',
                title: null,
                searchQuery: null,
                ecoOwner: '',
                status: ''
            };

            $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
            vm.search = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                objectType: 'SAVEDSEARCH',
                searchObjectType: 'CHANGE',
                type: 'PRIVATE',
                owner: $rootScope.localStorageLogin.login.person.id
            }

            vm.selectedAttributes = [];
            $rootScope.showSearch = false;
            vm.mode = $rootScope.searchType;
            vm.loading = true;
            vm.ecos = angular.copy(pagedResults);

            vm.showEcoDetails = showEcoDetails;
            vm.searchEco = searchEco;
            vm.clearFilter = clearFilter;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.editECO = editECO;
            vm.deleteECO = deleteECO;
            vm.freeTextSearch = freeTextSearch;
            vm.removeAttribute = removeAttribute;
            vm.resetPage = resetPage;
            vm.nextPage = nextPage;
            vm.showNewECO = showNewECO;
            vm.newECO = newECO;
            vm.previousPage = previousPage;
            vm.selectAll = selectAll;
            vm.toggleSelection = toggleSelection;
            var searchMode = null;
            var simpleFilters = null;
            var advancedFilters = null;
            $scope.freeTextQuery = null;
            $rootScope.searchModeType = false;
            vm.flag = false;
            vm.selectedEcos = [];
            vm.selection = null;
            vm.showTypeAttributes = showTypeAttributes;
            vm.showSaveSearch = showSaveSearch;
            $rootScope.showEcoSavedSearches = showEcoSavedSearches;
            vm.showAttributeDetails = showAttributeDetails;
            vm.openAttachment = openAttachment;
            vm.showImage = showImage;
            vm.objectIds = [];
            vm.searchText = null;
            vm.filterSearch = false;
            vm.privatefolderSelected = false;
            vm.folderSelected = false;
            vm.publicfolderSelected = false;
            vm.pageSize = pageSize;

            function nextPage() {
                vm.pageable.page++;
                performLoad();
            }

            function previousPage() {
                vm.pageable.page--;
                performLoad();
            }

            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                performLoad();
            }

            function performLoad() {
                if (searchMode == null) {
                    loadEcos();
                } else if (searchMode == "simple") {
                    searchEco(simpleFilters);
                } else if (searchMode == "freetext") {
                    freeTextSearch($scope.freeTextQuery);
                }
            }

            function resetPage() {
                vm.ecos = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.changeSelected = false;
                vm.filterSearch = false;
                $rootScope.showSearch = false;
                $rootScope.searchModeType = false;
                if (document.getElementById("freeTextSearchInput") != null)
                    document.getElementById("freeTextSearchInput").placeholder = "";
            }

            var createButton = $translate.instant("CREATE");
            var eco = $translate.instant("ECO_ALL_NEW_ECO");
            $scope.deleteEcoTitle = parsed.html($translate.instant("DELETE_ECO_TITLE")).html();
            $scope.editEco = parsed.html($translate.instant("EDIT_ECO")).html();
            $scope.saveEco = parsed.html($translate.instant("SAVE_ECO")).html();
            $scope.cancel = parsed.html($translate.instant("CANCEL")).html();
            $scope.searchItemType = parsed.html($translate.instant("ALL_VIEW_SEARCH")).html();
            vm.ecoAttributes = parsed.html($translate.instant("ECO_ATTRIBUTES")).html();
            vm.savedSearchItems = parsed.html($translate.instant("ALL_VIEW_SAVED_SEARCHES")).html();
            vm.saveSearchEcos = parsed.html($translate.instant("SAVED_SEARCH_ECO")).html();
            $scope.cannotDeleteApprovedEco = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_ECO")).html();
            vm.RemoveColumnTitle = parsed.html($translate.instant("REMOVE_ATTRIBUTE_COLUMN")).html();


            function showNewECO() {
                var options = {
                    title: eco,
                    template: 'app/desktop/modules/change/eco/new/newEcoView.jsp',
                    controller: 'NewECOController as newEcoVm',
                    resolve: 'app/desktop/modules/change/eco/new/newEcoController',
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.changes.ecos.new'}
                    ],
                    callback: function (eco) {
                        showEcoDetails(eco);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newSearch = $translate.instant("NEW_SEARCH");

            function showSaveSearch() {
                var options = {
                    title: newSearch,
                    template: 'app/desktop/modules/item/newSearchView.jsp',
                    controller: 'NewSearchController as newSearchVm',
                    resolve: 'app/desktop/modules/item/newSearchController',
                    width: 600,
                    showMask: true,
                    data: {
                        search: vm.search
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.search.new'}
                    ],
                    callback: function () {
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function freeTextSearch(freetext) {
                $rootScope.showBusyIndicator()
                searchMode = "freetext";
                $scope.freeTextQuery = freetext;
                vm.searchText = freetext;
                $rootScope.ecoFreeTextSearchText = freetext;

                if (freetext != undefined && freetext != null && freetext.trim() != "") {
                    vm.search.searchType = "freetext";
                    vm.search.query = freetext;
                    vm.search.query = angular.toJson(freetext);
                    ECOService.freeTextSearch(vm.pageable, freetext).then(
                        function (data) {
                            vm.ecos = data;
                            $rootScope.showSearch = true;
                            $rootScope.searchModeType = true;
                            vm.loading = false;
                            CommonService.getPersonReferences(vm.ecos.content, 'ecoOwner');
                            WorkflowService.getWorkflowReferences(vm.ecos.content, 'workflow');
                            ECOService.getChangeTypeReferences(vm.ecos.content, 'ecoType');
                            angular.forEach(vm.ecos.content, function (eco) {
                                eco.newTitle = eco.title;
                                eco.newDescription = eco.description;
                                eco.newReasonForChange = eco.reasonForChange;
                                eco.showValues = true;
                                eco.editMode = false;
                            });
                            loadAttributeValues();
                            vm.clear = true;
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    resetPage();
                    loadEcos();
                    $rootScope.ecoFreeTextSearchText = null;
                }
            }

            function searchEco(filters) {
                vm.search.searchType = "simple";
                searchMode = "simple";
                simpleFilters = filters;
                vm.search.query = angular.toJson(filters);
                ECOService.searchEco(vm.pageable, filters).then(
                    function (data) {
                        vm.ecos = data;
                        $rootScope.showSearch = true;
                        $rootScope.searchModeType = true;
                        vm.filterSearch = true;
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.ecos.content, 'ecoOwner');
                        WorkflowService.getWorkflowReferences(vm.ecos.content, 'workflow');
                        ECOService.getChangeTypeReferences(vm.ecos.content, 'ecoType');
                        angular.forEach(vm.ecos.content, function (eco) {
                            eco.newTitle = eco.title;
                            eco.newDescription = eco.description;
                            eco.newReasonForChange = eco.reasonForChange;
                            eco.showValues = true;
                            eco.editMode = false;
                        });
                        vm.showValues = true;
                        loadAttributeValues();
                        if (document.getElementById("freeTextSearchInput") != null)
                            document.getElementById("freeTextSearchInput").placeholder = "Simple Search";
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
                vm.clear = true;
            }

            function clearFilter() {
                loadEcos();
                vm.clear = false;
            }

            var Simple = $translate.instant("ECO_SIMPLE_SEARCH");
            var Advanced = $translate.instant("ECO_ADVANCED_SEARCH");
            var SerachButton = $translate.instant("SEARCH_BUTTON");

            vm.filters = [];
            vm.ecoSearchFilters = null;
            function advancedSearch(filters) {
                vm.search.searchType = "advanced";
                searchMode = "advanced";
                advancedFilters = filters;
                vm.search.query = angular.toJson(filters);
                ECOService.advancedSearchEcr(vm.pageable, filters).then(
                    function (data) {
                        vm.ecos = data;
                        vm.clear = true;
                        $rootScope.showSearch = true;
                        $rootScope.searchModeType = true;
                        vm.filterSearch = true;
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.ecos.content, 'ecoOwner');
                        WorkflowService.getWorkflowReferences(vm.ecos.content, 'workflow');
                        ECOService.getChangeTypeReferences(vm.ecos.content, 'ecoType');
                        angular.forEach(vm.ecos.content, function (eco) {
                            eco.newTitle = eco.title;
                            eco.newDescription = eco.description;
                            eco.newReasonForChange = eco.reasonForChange;
                            eco.showValues = true;
                            eco.editMode = false;
                        });
                        loadAttributeValues();
                        if (document.getElementById("freeTextSearchInput") != null)
                            document.getElementById("freeTextSearchInput").placeholder = "Advanced Search";
                        vm.showValues = true;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function newECO() {
                $state.go('app.changes.ecos.new');
            }


            var ecoUpdateMessage = parsed.html($translate.instant("ECO_UPDATE_MESSAGE")).html();

            function applyChanges(eco) {
                var promise = null;
                if (eco.id == null || eco.id == undefined) {
                    eco.title = eco.newTitle;
                    eco.description = eco.newDescription;
                    eco.reasonForChange = eco.newReasonForChange;
                    promise = ECOService.createECO(eco);
                }
                else {
                    eco.title = eco.newTitle;
                    eco.description = eco.newDescription;
                    eco.reasonForChange = eco.newReasonForChange;
                    promise = ECOService.updateECO(eco);
                }

                promise.then(
                    function (data) {
                        eco.id = data.id;
                        eco.editMode = false;
                        $rootScope.showSuccessMessage(ecoUpdateMessage);
                        $timeout(function () {
                            eco.showValues = true;
                        }, 500);
                    }
                )
            }

            $scope.saved = parsed.html($translate.instant("SAVED_SEARCH_TITLE")).html();
            function showEcoSavedSearches() {
                var options = {
                    title: $scope.saved,
                    template: 'app/desktop/modules/item/showSavedSearches.jsp',
                    controller: 'SavedSearchesController as searchVm',
                    resolve: 'app/desktop/modules/item/savedSearchesController',
                    width: 600,
                    showMask: true,
                    data: {
                        type: "CHANGE"
                    },
                    callback: function (result) {
                        var query = angular.fromJson(result.query);
                        if (result.searchType == 'freetext') {
                            freeTextSearch(query);
                        } else if (result.searchType == 'simple') {
                            searchEco(query);
                        } else if (result.searchType == 'advanced') {
                            advancedSearch(query);
                        }
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function cancelChanges(eco) {
                if (eco.id == null || eco.id == undefined) {
                    vm.ecrs.splice(vm.ecrs.indexOf(eco), 1);
                }
                else {
                    eco.newTitle = eco.title;
                    eco.newDescription = eco.description;
                    eco.newReasonForChange = eco.reasonForChange;
                    eco.editMode = false;
                    $timeout(function () {
                        eco.showValues = true;
                    }, 500);
                }
            }

            function editECO(eco) {
                eco.editMode = true;
                eco.showValues = false;
                $timeout(function () {
                }, 500);
            }

            $scope.deleteEco = parsed.html($translate.instant("DELETE_ECO_TITLE")).html();
            var deleteMessages = parsed.html($translate.instant("DELETE_ECO_TITLE_MESSAGE")).html();
            var ecoDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var deleteMessage = parsed.html($translate.instant("DELETE_ECO_MESSAGE")).html();
            $scope.deleteTitle = parsed.html($translate.instant("DELETE_ECO_TITLE_MESSAGES")).html();

            function goAheadAndDeleteEco(eco) {
                if (eco.id != null && eco.id != undefined) {
                    ECOService.deleteECO(eco.id).then(
                        function (data) {
                            var index = vm.ecos.content.indexOf(eco);
                            vm.ecos.content.splice(index, 1);
                            vm.ecos.totalElements--;
                            $rootScope.showSuccessMessage(eco.ecoNumber + deleteMessage);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function deleteECO(eco) {
                if (eco.workflow != null) {
                    WorkflowService.getWorkflow(eco.workflow).then(
                        function (data) {
                            if (data.started === false) {
                                var options = {
                                    title: $scope.deleteEco,
                                    message: deleteMessages + " [ " + eco.ecoNumber + " ] " + ecoDelete + "?",
                                    okButtonClass: 'btn-danger'
                                };
                                DialogService.confirm(options, function (yes) {
                                    if (yes === true) {
                                        goAheadAndDeleteEco(eco);
                                    }
                                });
                            }
                            else {
                                var options = {
                                    title: $scope.deleteEco,
                                    message: $scope.deleteTitle,
                                    okButtonClass: 'btn-primary',
                                    hideCancelButton: true
                                };
                                DialogService.confirm(options, function (yes) {
                                });
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else {
                    var options = {
                        title: $scope.deleteEco,
                        message: deleteMessages + " [ " + eco.ecoNumber + " ] " + ecoDelete + "?",
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes === true) {
                            goAheadAndDeleteEco(eco);
                        }
                    });
                }
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            function showEcoDetails(eco) {
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                vm.recentlyVisited.objectId = eco.id;
                vm.recentlyVisited.objectType = eco.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.changes.eco.details', {ecoId: eco.id, tab: 'details.basic'});
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.ecoIds = [];
            vm.attributeIds = [];
            vm.ecos.ecoType = [];
            function loadEcos() {
                $rootScope.showBusyIndicator();
                vm.clear = false;
                vm.loading = true;
                searchMode = null;
                ECOService.getAllItemECOs(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.ecos = data;
                        angular.forEach(vm.ecos.content, function (eco) {
                            eco.newTitle = eco.title;
                            eco.newDescription = eco.description;
                            eco.newReasonForChange = eco.reasonForChange;
                            eco.showValues = true;
                            eco.editMode = false;
                            eco.workflowObject = null;
                            if (eco.createdDate) {
                                eco.createdDatede = moment(eco.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                            if (eco.releasedDate) {
                                eco.releasedDatede = moment(eco.releasedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                        });
                        CommonService.getPersonReferences(vm.ecos.content, 'ecoOwner');
                        WorkflowService.getWorkflowReferences(vm.ecos.content, 'workflow');
                        ECOService.getChangeTypeReferences(vm.ecos.content, 'ecoType');
                        CommonService.getPersonReferences(vm.ecos.content, 'createdBy');
                        loadAttributeValues();
                        loadChangeAnalysts();
                        loadWorkflowStatus();
                        vm.loading = false;
                        vm.showValues = true;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.objectIds = [];
            vm.attributeIds = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                angular.forEach(vm.ecos.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                if (vm.selectedAttributes.length > 0) {
                    $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.ecos.content);
                }
            }

            function showAttributeDetails(attribute) {
                if (attribute.objectType == 'ITEM') {
                    $state.go('app.items.details', {itemId: attribute.id});
                } else if (attribute.objectType == 'ITEMREVISION') {
                    $state.go('app.items.details', {itemId: attribute.id});
                } else if (attribute.objectType == 'CHANGE') {
                    $state.go('app.changes.ecos.details', {ecoId: attribute.id});
                } else if (attribute.objectType == 'PLMWORKFLOWDEFINITION') {
                    $state.go('app.workflow.editor', {mode: 'edit', workflow: attribute.id})
                } else if (attribute.objectType == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {manufacturerId: attribute.id});
                } else if (attribute.objectType == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: attribute.manufacturer,
                        manufacturePartId: attribute.id
                    });
                } else if (attribute.objectType == 'REQUIREMENT') {
                    $state.go('app.rm.requirements.details', {requirementId: attribute.id});
                } else if (attribute.objectType == 'PROJECT') {
                    $state.go('app.pm.project.details', {projectId: attribute.id});
                }
            }

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                $rootScope.downloadFileFromIframe(url);
            }

            function showImage(attribute) {
                var modal = document.getElementById('myModal2');
                var modalImg = document.getElementById('img03');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            var numbers = " ";
            var selectedECONumbers = " ";
            vm.existedNumbers = [];
            vm.selectedECONumbers = [];
            var folderSuccessfullyMessage = parsed.html($translate.instant("FOLDER_SUCCESS_MESSAGE")).html();
            var ecosAdded = parsed.html($translate.instant("ECOS_ADDED_TO")).html();
            var ecosAlreadyExists = parsed.html($translate.instant("ECOS_ALREADY_EXISTS")).html();
            var Folder = parsed.html($translate.instant("FOLDER")).html();
            var selectFolderEco = $translate.instant("SELECT_FOLDERECO");

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
                var data = ecoFolder.tree('getData', node.target);
                var folder = data.attributes.folder;
                if (!$scope.$$phase) $scope.$apply();
                if (folder != null && folder != undefined) {
                    if (vm.selectedEcos.length > 0) {
                        vm.folder = folder;
                        var objectType = null;
                        vm.folderObjects = [];
                        vm.existedNumbers = [];
                        vm.selectedECONumbers = [];
                        numbers = " ";
                        selectedECONumbers = " ";
                        FolderService.getFolderObjects(folder).then(
                            function (data) {
                                initFoldersTree();
                                vm.existEcos = data;
                                angular.forEach(vm.selectedEcos, function (selectedEco) {
                                    vm.valid = true;
                                    angular.forEach(vm.existEcos, function (existEco) {
                                        if (selectedEco.id == existEco.objectId) {
                                            vm.valid = false;
                                            if (vm.existedNumbers.length == 0) {
                                                numbers = numbers + " " + selectedEco.ecoNumber;
                                            } else {
                                                numbers = numbers + ", " + selectedEco.ecoNumber;
                                            }
                                            vm.existedNumbers.push(selectedEco);
                                            $rootScope.showErrorMessage(numbers + " : " + ecosAlreadyExists + " (" + vm.folder.name + ")" + Folder);

                                        }
                                    })
                                    if (vm.valid) {
                                        var folderObject = {
                                            folder: 0,
                                            objectType: null,
                                            objectId: 0
                                        };
                                        folderObject.folder = vm.folder.id;
                                        folderObject.objectId = selectedEco.id;
                                        objectType = selectedEco.objectType;
                                        vm.folderObjects.push(folderObject);
                                        if (vm.selectedECONumbers.length == 0) {
                                            selectedECONumbers = selectedECONumbers + " " + selectedEco.ecoNumber;
                                        } else {
                                            selectedECONumbers = selectedECONumbers + ", " + selectedEco.ecoNumber;
                                        }
                                        vm.selectedECONumbers.push(selectedEco);
                                    }
                                })
                                if (vm.existedNumbers.length == 0) {
                                    FolderService.createFolderObject(vm.folderObjects, objectType).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(selectedECONumbers + " : " + ecosAdded + "(" + vm.folder.name + ")" + folderSuccessfullyMessage);
                                            angular.forEach(vm.selectedEcos, function (eco) {
                                                eco.selected = false;
                                                vm.existedNumbers = [];
                                                numbers = " ";
                                                selectedECONumbers = " ";
                                                vm.selectedEcos = [];
                                                vm.changeSelected = false;
                                            })
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
                    /*else {
                     $rootScope.showWarningMessage(selectFolderEco)
                     }*/
                }
            };
            vm.changeSelected = false;
            function selectAll() {
                vm.selectedEcos = [];
                if (vm.changeSelected) {
                    angular.forEach(vm.ecos.content, function (eco) {
                            vm.selectedEcos.push(eco);
                            eco.selected = true;
                        }
                    )
                } else {
                    vm.selectedEcos = [];
                    angular.forEach(vm.ecos.content, function (eco) {
                        eco.selected = false;
                    })
                }
            }

            function toggleSelection(eco) {
                if (eco.selected == false) {
                    var index = vm.selectedEcos.indexOf(eco);
                    if (index != -1) {
                        vm.selectedEcos.splice(index, 1);
                    }
                    vm.changeSelected = false;
                }
                else {
                    vm.selectedEcos.push(eco);
                    if (vm.ecos.content.length == vm.selectedEcos.length) {
                        vm.changeSelected = true;
                    }
                    vm.selection = 'selected';
                }
            }

            function loadSavedSearch(searchType, query) {
                if (searchType == 'freetext') {
                    freeTextSearch(query);
                } else if (searchType == 'simple') {
                    searchEco(query);
                } else if (searchType == 'advanced') {
                    advancedSearch(query);
                }
            }

            /**
             *
             * Creating folder If not exist in the Mfrs
             * */


            var itemNodeId = 0;
            var ecoFolder = null;
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
                ecoFolder = $('#ecoFolder').tree({
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

                var roots = ecoFolder.tree('getRoots');
                itemRootNode = roots.length ? roots[0] : null;
                $(document).click(function () {
                    $("#folderContextMenu").hide();
                });

                loadFolders();
            }

            vm.lastSelectedFolderName = null;
            function onDblClick(node) {
                vm.lastSelectedFolderName = null;
                if (node.attributes.type == 'CHANGE') {
                    if (node.attributes.eco != null && node.attributes.eco != undefined) {
                        $state.go('app.changes.ecos.details', {ecoId: node.attributes.eco.id});
                    }
                }
                else if (node.attributes.type == 'FOLDER') {
                    var data = ecoFolder.tree('getData', node.target);
                    if (data.id != 0) {
                        vm.lastSelectedFolderName = data.text;
                        ecoFolder.tree('beginEdit', node.target);
                    }
                    $timeout(function () {
                        $('.tree-editor').focus().select();
                    }, 1);
                }
            }

            vm.ecoObjects = [];

            function onBeforeExpand(node) {
                var nodeData = ecoFolder.tree('getData', node.target);
                if (!nodeData.attributes.loaded) {
                    vm.ecoObjects = [];
                    var folder = nodeData.attributes.folder;
                    if (folder != null && folder != undefined) {
                        if (folder.items == null || folder.items == undefined) {
                            FolderService.getFolderObjects(folder).then(
                                function (data) {
                                    vm.folderContent = data;
                                    if (vm.folderContent.length > 0) {
                                        angular.forEach(vm.folderContent, function (content) {
                                            if (content.objectType == 'CHANGE') {
                                                vm.ecoObjects.push(content);
                                            }
                                        });
                                        if (vm.ecoObjects.length > 0) {
                                            loadEcoObjects(node, folder, vm.ecoObjects);
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


            function loadEcoObjects(node, folder, objects) {
                var ecoIds = [];
                var map = new Hashtable();
                angular.forEach(objects, function (object) {
                    ecoIds.push(object.objectId);
                    map.put(object.objectId, object)
                });

                if (ecoIds.length > 0) {
                    ECOService.getECOsByIds(ecoIds).then(
                        function (data) {
                            var newNodes = [];
                            angular.forEach(data, function (eco) {
                                var name = eco.ecoNumber + " : " + eco.status + " : " + eco.statusType;
                                var itemNode = {
                                    id: ++itemNodeId,
                                    text: name,
                                    iconCls: 'folder-eco',
                                    attributes: {
                                        eco: eco,
                                        type: 'CHANGE',
                                        object: map.get(eco.id)
                                    }
                                };
                                newNodes.push(itemNode);
                            });

                            removeLoadingNode(node);

                            ecoFolder.tree('append', {
                                parent: node.target,
                                data: newNodes
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
                    var loadingNode = ecoFolder.tree('find', loadingId);
                    if (loadingNode != null) {
                        ecoFolder.tree('remove', loadingNode.target);
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
                var menus = ['addFoldereco', 'deleteFoldereco'];
                angular.forEach(menus, function (menu) {
                    $('#' + menu).hide();
                });
                if (node.attributes.type == 'ROOT') {
                    $contextMenu.hide();
                    $('#addFoldereco').hide();
                }
                else if ((node.attributes.type == "PRIVATE" && vm.privatefolderSelected) || (node.attributes.type == "PUBLIC" && vm.publicfolderSelected)) {
                    $contextMenu.css({
                        display: "block",
                        left: e.pageX,
                        top: e.pageY
                    });
                    $('#addFoldereco').show();
                }
                else if (node.attributes.type == 'FOLDER' && vm.folderSelected) {
                    $contextMenu.css({
                        display: "block",
                        left: e.pageX,
                        top: e.pageY
                    });
                    $('#addFoldereco').show();
                    $('#deleteFoldereco').show();
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
                var selectedNode = ecoFolder.tree('getSelected');
                vm.selectedFolderName = selectedNode.text;
                if (selectedNode != null) {
                    var nodeid = ++itemNodeId;

                    ecoFolder.tree('append', {
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

                    var newNode = ecoFolder.tree('find', nodeid);
                    ecoFolder.tree('expandTo', newNode.target);
                    if (newNode != null) {
                        ecoFolder.tree('beginEdit', newNode.target);

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
                        var selectedNode = ecoFolder.tree('getSelected');
                        if (selectedNode != null) {
                            var data = ecoFolder.tree('getData', selectedNode.target);
                            if (data != null && data.attributes.folder != null) {
                                FolderService.deleteFolder(data.attributes.folder.id).then(
                                    function (data) {
                                        ecoFolder.tree('remove', selectedNode.target);
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

                var data = ecoFolder.tree('getData', node.target);
                if (node.text == null || node.text == "") {
                    $timeout(function () {
                        $rootScope.showWarningMessage(folderNameCannotBeEmpty);

                        ecoFolder.tree('beginEdit', node.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);

                    }, 10);
                }
                else {

                    if (data.attributes.folder == null) {
                        data.attributes.folder = {}
                    }

                    var parent = ecoFolder.tree('getParent', node.target);
                    var parentData = ecoFolder.tree('getData', parent.target);

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

                    if (data.attributes.folder.id == undefined || data.attributes.folder.id == null) {
                        FolderService.createFolder(data.attributes.folder).then(
                            function (folderData) {
                                data.attributes.folder = angular.copy(folderData);
                                ecoFolder.tree('update', {target: node.target, attributes: data.attributes});
                                ecoFolder.tree('select', node.target);
                            }, function (error) {
                                $rootScope.showWarningMessage(error.message);
                                ecoFolder.tree('remove', data.target);
                            }
                        )
                    } else {
                        FolderService.updateFolder(data.attributes.folder).then(
                            function (folderData) {
                                data.attributes.folder = angular.copy(folderData);
                                ecoFolder.tree('update', {target: node.target, attributes: data.attributes});
                                ecoFolder.tree('select', node.target);
                            }, function (error) {
                                $rootScope.showWarningMessage(error.message);
                                data.attributes.folder.name = vm.lastSelectedFolderName;
                                data.text = vm.lastSelectedFolderName;
                                ecoFolder.tree('update', {target: node.target, attributes: data.attributes});
                                ecoFolder.tree('select', node.target);
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

                        ecoFolder.tree('append', {
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

                        ecoFolder.tree('append', {
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
                        var selectedNode = ecoFolder.tree('getSelected');
                        if (selectedNode != null) {
                            var data = ecoFolder.tree('getData', selectedNode.target);
                            var selectedType = data.attributes.type;
                            if (data.attributes.type == 'CHANGE') {
                                vm.removeItemName = data.attributes.eco.ecoNumber;
                            }
                            FolderService.getFolderById(data.attributes.object.folder).then(
                                function (folderName) {
                                    vm.folderName = folderName.name;
                                    if (data != null) {
                                        FolderService.removeFolderObject(data.attributes.object.id).then(
                                            function (data) {
                                                ecoFolder.tree('remove', selectedNode.target);
                                                if (selectedType == 'CHANGE') {
                                                    $rootScope.showSuccessMessage(vm.removeItemName + ": " + ecoRemovedMessage + "(" + vm.folderName + ")" + folder);
                                                }

                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        );
                                    }
                                }
                            )

                        }
                    }
                });
            }


            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

            vm.showTypeAttributes = showTypeAttributes;
            vm.removeAttribute = removeAttribute;
            vm.selectedAttributes = [];
            function showTypeAttributes() {
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/pqm/attributes/qualityTypeAttributeSelectionView.jsp',
                    resolve: 'app/desktop/modules/pqm/attributes/qualityTypeAttributeSelectionController',
                    controller: 'QualityTypeAttributeSelectionController as qualityTypeAttributeSelectionVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        selectedObjectType: "ECOTYPE",
                        selectedObject: "ECO",
                        selectedParentObjectType: "CHANGE"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.quality.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("ecoAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadEcos();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("ecoAttributes", JSON.stringify(vm.selectedAttributes));
            }

            $scope.changeAnalysts = [];
            function loadChangeAnalysts() {
                ECOService.getChangeAnalysts().then(
                    function (data) {
                        $scope.changeAnalysts = data;
                    }
                )
            }

            $scope.statuses = [];
            function loadStatus() {
                ECOService.getECOStatus().then(
                    function (data) {
                        $scope.statuses = data;
                    }
                )
            }

            function loadWorkflowStatus() {
                WorkflowService.getObjectWorkflowStatus("ECO").then(
                    function (data) {
                        $scope.statuses = data;
                    }
                )
            }

            $scope.clearChangeAnalyst = clearChangeAnalyst;
            function clearChangeAnalyst() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedPerson = null;
                vm.filters.ecoOwner = '';
                loadEcos();
                $rootScope.hideBusyIndicator();
            }

            $scope.selectedPerson = null;
            $scope.onSelectChangeAnalyst = onSelectChangeAnalyst;
            function onSelectChangeAnalyst(person) {
                vm.pageable.page = 0;
                $scope.selectedPerson = person;
                vm.filters.ecoOwner = person.id;
                loadEcos();
            }

            $scope.selectedStatus = null;
            $scope.onSelectStatus = onSelectStatus;
            function onSelectStatus(status) {
                vm.pageable.page = 0;
                $scope.selectedStatus = status;
                vm.filters.status = status;
                loadEcos();
            }

            $scope.clearStatus = clearStatus;
            function clearStatus() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedStatus = null;
                vm.filters.status = '';
                loadEcos();
                $rootScope.hideBusyIndicator();
            }


            vm.clearTypeSelection = clearTypeSelection;
            function clearTypeSelection() {
                vm.pageable.page = 0;
                vm.selectedEcoType = null;
                vm.filters.ecoType = '';
                loadEcos();
            }

            vm.onSelectType = onSelectType;
            function onSelectType(ecoType) {
                vm.pageable.page = 0;
                vm.selectedEcoType = ecoType;
                vm.filters.ecoType = ecoType.id;
                loadEcos();
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("ecoAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
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

            var searchButton = $translate.instant("SEARCH");
            vm.ecoSearchTitle = parsed.html($translate.instant("ECO_SEARCH_TITLE")).html();
            vm.ecosSearch = ecosSearch;
            function ecosSearch() {
                var options = {
                    title: vm.ecoSearchTitle,
                    template: 'app/desktop/modules/item/all/searchDialogueView.jsp',
                    controller: 'SearchDialogueController as searchDialogueVm',
                    resolve: 'app/desktop/modules/item/all/searchDialogueController',
                    width: 750,
                    showMask: true,
                    data: {
                        selectedType: "ECO"
                    },
                    buttons: [
                        {text: searchButton, broadcast: 'app.items.search'}
                    ],
                    callback: function (filter) {
                        if (filter.length > 0) {
                            resetPage();
                            vm.ecoSearchFilters = filter;
                            vm.filters = [];
                            angular.forEach(filter, function (eco) {
                                var filter = {
                                    field: eco.field.field,
                                    operator: eco.operator.name,
                                    value: eco.value
                                }
                                vm.filters.push(filter);
                            })
                            $timeout(function () {
                                advancedSearch(vm.filters);
                            }, 200);
                        } else {
                            vm.resetPage();
                            searchEco(filter);
                        }
                    }
                };
                $rootScope.showSidePanel(options);
            }

            (function () {
                //if ($application.homeLoaded == true) {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })
                $timeout(function () {
                    try {
                        initFoldersTree();
                    }
                    catch (e) {
                    }
                }, 200);
                $rootScope.getChangeAttributes("CHANGE", "ECOTYPE", vm.attributeIds);
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("ecoAttributes"));
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
                                $window.localStorage.setItem("ecoAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                                if ($rootScope.ecoFreeTextSearchText == null) {
                                    loadEcos();
                                } else {
                                    freeTextSearch($rootScope.ecoFreeTextSearchText);
                                }
                            } else if ($rootScope.allEcosLoad == false) {
                                vm.query = JSON.parse($stateParams.queryData);
                                loadSavedSearch(vm.mode, vm.query);
                            } else {
                                loadEcos();
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                        if ($rootScope.ecoFreeTextSearchText == null) {
                            loadEcos();
                        } else {
                            freeTextSearch($rootScope.ecoFreeTextSearchText);
                        }
                    } else if ($rootScope.allEcosLoad == false) {
                        vm.query = JSON.parse($rootScope.searchQuery);
                        $rootScope.searchQuery = null;
                        loadSavedSearch(vm.mode, vm.query);
                    } else {
                        loadEcos();
                    }
                }
                //}
            })();
        }
    });
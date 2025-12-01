define([
        'app/desktop/modules/main/main.module',
        'app/shared/services/core/documentService'
    ],
    function (module) {
        module.controller('DocumentsSelectionController', DocumentsSelectionController);

        function DocumentsSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                              DocumentService, CommonService) {

            var vm = this;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };
            vm.loading = true;
            vm.error = "";
            vm.selectionMode = null;
            vm.selectAllCheck = false;
            var selectedDocumentsMap = new Hashtable();
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
            var folder = $scope.data.selectedObjectFolder;
            var objectId = $scope.data.objectId;
            var documentType = $scope.data.documentType;
            vm.filters = {
                searchQuery: null,
                objectId: objectId,
                objectFolder: '',
                folder: '',
                documentType: null
            };

            vm.documents = angular.copy(pagedResults);
            vm.onSelectType = onSelectType;
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.addDocuments = addDocuments;

            var parsed = angular.element("<div></div>");
            var selectMessage = parsed.html($translate.instant("ATLEAST_ONE_DOCUMENT_VALIDATION")).html();
            $scope.searchTitle = parsed.html($translate.instant("SEARCH")).html();

            vm.selectedDocuments = [];
            $scope.check = false;

            function selectAll(check) {
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.documents, function (document) {
                        document.selected = false;
                        var itemExist = selectedDocumentsMap.get(document.id);
                        if (itemExist != null) {
                            var index = vm.selectedDocuments.indexOf(itemExist);
                            if (index != -1) {
                                vm.selectedDocuments.splice(index, 1);
                                selectedDocumentsMap.remove(document.id);
                            }
                        }
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.documents, function (document) {
                        document.selected = true;
                        vm.selectedDocuments.push(document);
                    })
                }
            }

            function nextPage() {
                if (vm.documents.last != true) {
                    vm.pageable.page++;
                    $scope.check = false;
                    loadDocuments();
                }
            }

            function previousPage() {
                if (vm.documents.first != true) {
                    vm.pageable.page--;
                    $scope.check = false;
                    loadDocuments();
                }
            }

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedType = itemType;
                    vm.pageable.page = 0;
                    vm.clear = true;
                }
            }

            vm.clearSelection = clearSelection;
            function clearSelection() {
                selectedDocumentsMap = new Hashtable();
                vm.selectedDocuments = [];
                vm.selectAllCheck = false;
                angular.forEach(vm.documents, function (document) {
                    document.selected = false;
                })
            }

            function clearFilter() {
                vm.filters.searchQuery = null;
                $scope.check = false;
                vm.selectAllCheck = false;
                vm.clear = false;
            }

            function addDocuments() {
                if (vm.selectedDocuments.length != 0) {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedDocuments);
                    $rootScope.showBusyIndicator();
                }

                if (vm.selectedDocuments.length == 0) {
                    $rootScope.showWarningMessage(selectMessage);
                }

            }


            function loadDocuments() {
                if (folder != null && folder != undefined && folder != "") {
                    vm.filters.objectFolder = folder.id;
                }
                vm.filters.documentType = "FILE";
                if (documentType == "MFRPARTINSPECTIONREPORT") {
                    vm.filters.documentType = "MFRPARTINSPECTIONREPORT";
                }
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                DocumentService.getFilteredDocuments(vm.filters).then(
                    function (data) {
                        vm.documents = data;
                        CommonService.getPersonReferences(vm.documents, 'modifiedBy');
                        $timeout(function () {
                            calculateColumnWidthForSticky();
                        }, 500);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.freeTextSearch = freeTextSearch;
            function freeTextSearch(freeText) {
                $rootScope.freeTextQuerys = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.filters.searchQuery = freeText;
                    loadDocuments();
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    vm.filters.searchQuery = null;
                    loadDocuments();
                }
            }


            vm.resetPage = resetPage;
            function resetPage() {
                $rootScope.freeTextQuerys = null;
                freeTextSearch($rootScope.freeTextQuerys);
            }

            function selectCheck(document) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedDocuments, function (selectedItem) {
                    if (selectedItem.id == document.id) {
                        flag = false;
                        var index = vm.selectedDocuments.indexOf(selectedItem);
                        if (index != -1) {
                            vm.selectedDocuments.splice(index, 1);
                            selectedDocumentsMap.remove(document.id);
                        }
                    }
                });
                if (flag) {
                    vm.selectedDocuments.push(document);
                    selectedDocumentsMap.put(document.id, document);
                }
                var count = 0;
                angular.forEach(vm.documents, function (document) {
                    if (document.selected) {
                        count++;
                    }
                })
                if (count != vm.documents.length) {
                    vm.selectAllCheck = false;
                } else {
                    vm.selectAllCheck = true;
                }
            }

            vm.toggleNode = toggleNode;
            function toggleNode(document) {
                document.expanded = !document.expanded;
                var index = vm.documents.indexOf(document);
                if (document.expanded == false) {
                    removeChildren(document);
                } else {
                    vm.filters.folder = document.id;
                    loadDocuments();
                }
            }

            function removeChildren(document) {
                if (document != null && document.bomChildren != null && document.bomChildren != undefined) {
                    angular.forEach(document.bomChildren, function (document) {
                        removeChildren(document);
                    });

                    var index = vm.documents.indexOf(document);
                    vm.documents.splice(index + 1, document.bomChildren.length);
                    document.bomChildren = [];
                    document.expanded = false;

                }
            }

            $scope.selectedFolderId = 0;
            var nodeId = 0;
            var documentFolderTree = null;
            var rootNode = null;
            var parsed = angular.element("<div></div>");
            var folderNameCannotBeEmpty = parsed.html($translate.instant("FOLDER_NAME_CANNOT_BE_EMPTY")).html();
            var deleteFolderTitle = parsed.html($translate.instant("DELETE_FOLDER_TITLE")).html();
            var deleteFolderMessage = parsed.html($translate.instant("DELETE_FOLDER_MESSAGE")).html();
            var folderDeletedMessage = parsed.html($translate.instant("FOLDER_DELETED_MESSAGE")).html();

            vm.loading = true;
            function initFoldersTree() {
                documentFolderTree = $('#documentFolderTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: "Root",
                            iconCls: 'folders-root',
                            attributes: {
                                type: 'ROOT'
                            },
                            children: []
                        }
                    ],
                    onSelect: onSelectFolder
                });

                rootNode = documentFolderTree.tree('find', 0);
                loadFolders();
                loadDocuments();
            }

            vm.previousSelectedFolder = 0;
            function onSelectFolder(node) {
                $("#foldersContextMenu").hide();
                vm.previousSelectedFolder = $scope.selectedFolderId;
                if (node.attributes.type == "ROOT") {
                    $scope.selectedFolderId = 0;
                    vm.filters.folder = '';
                    loadDocuments();
                } else {
                    $scope.selectedFolderId = node.attributes.folder.id;
                    vm.filters.folder = $scope.selectedFolderId;
                    loadDocuments();
                }
                $scope.$evalAsync();
            }

            function loadFolders() {
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                var folderId = 0;
                if (folder != null && folder != "") {
                    folderId = folder.id;
                }
                DocumentService.getObjectDocumentFolderTree(objectId, documentType, folderId).then(
                    function (data) {
                        var nodes = [];
                        angular.forEach(data, function (folder) {
                            var node = makeNode(folder);
                            if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, folder.children);
                            }
                            nodes.push(node);
                        });

                        documentFolderTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                        vm.loading = false;
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {load: true});
                            $rootScope.hideBusyIndicator();
                        }, 200);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

            }

            function visitChildren(parent, folders) {
                var nodes = [];
                angular.forEach(folders, function (folder) {
                    var node = makeNode(folder);
                    node.children = [];
                    if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                        node.state = "closed";
                        visitChildren(node, folder.children);
                    }

                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function makeNode(folder) {
                var text = folder.name;
                if (folder.count != null && folder.count != "" && folder.count > 0) {
                    text = folder.name + "<span class='badge badge-info' style='margin-left: 5px;margin-top: -2px' title='Files'>" + folder.count + "</span>";
                }
                return {
                    id: ++nodeId,
                    text: text,
                    iconCls: "pdm-folder",
                    attributes: {
                        folder: folder,
                        type: "FOLDER"
                    }
                };
            }

            vm.searchTree = searchTree;
            vm.searchValue = null;
            function searchTree() {
                if (vm.searchValue != null) {
                    $('#documentFolderTree').tree('expandAll');
                }
                $('#documentFolderTree').tree('doFilter', vm.searchValue);
            }

            function calculateColumnWidthForSticky() {
                var selectInputWidth = $('.select-input-box').outerWidth();
                var fileNameWidth = $('.document-name').outerWidth();

                $('.select-input-box').css("left", -4 + "px");
                $('.select-input-box').width(selectInputWidth);

                $('.document-name').css("left", ((selectInputWidth) - 4) + "px");
                $('.document-name').width(selectInputWidth + fileNameWidth);
            }

            (function () {
                $timeout(function () {
                    try {
                        initFoldersTree();
                    }
                    catch (e) {
                    }
                }, 1000);
                $rootScope.$on("app.object.files.documents.add", addDocuments);
                resetPage();
            })();
        }
    }
)
;
define(
    [
        'app/desktop/modules/item/item.module',
        'split-pane',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/modules/item/all/itemSearchDialogueController',
        'app/desktop/modules/item/all/advancedSearchController',
        'app/shared/services/core/folderService'
    ],
    function (module) {
        module.controller('FoldersController', FoldersController);

        function FoldersController($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, $uibModal, $translate,
                                   CommonService, FolderService) {

            $rootScope.viewInfo.icon = "fa-th";
            $rootScope.viewInfo.title = "Folders";
            $rootScope.searchFolder = searchFolder;

            var vm = this;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.loading = true;
            vm.selectedFolders = null;
            var folderTree = null;

            var pageable = {
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
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.folders = angular.copy(pagedResults);
            vm.items = [];
            var parsed = angular.element("<div></div>");
            var folderDeletedMessage = parsed.html($translate.instant("FOLDER_DELETED_MESSAGE")).html();

            vm.loadFolders = loadFolders;

            vm.deleteFolder = deleteFolder;
            vm.openFolder = openFolder;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.newFolder = newFolder;

            var nodeId = 0;
            var classificationTree = null;
            var rootNode = null;
            var searchMode = null;

            function initFolderTree() {
                classificationTree = $('#folderTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Classification',
                            iconCls: 'classification-root',
                            attributes: {
                                folder: null
                            },
                            children: []
                        }
                    ],
                    onSelect: onSelectFolderType
                });

                rootNode = folderTree.tree('find', 0);
            }

            function loadFolderTree() {
                FolderService.getClassificationTree().then(
                    function (data) {
                        var nodes = [];
                        angular.forEach(data, function (folder) {
                            var node = makeNode(folder);

                            if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                                node.state = "closed";
                                visitFoders(node, folder.children);
                            }

                            nodes.push(node);
                        });

                        classificationTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function visitFoders(parent, folders) {
                var nodes = [];
                angular.forEach(folders, function (folder) {
                    var node = makeNode(folder);

                    if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                        node.state = 'closed';
                        visitFoders(node, folder.children);
                    }

                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function makeNode(folder) {
                return {
                    id: ++nodeId,
                    text: folder.name,
                    iconCls: 'itemtype-node',
                    attributes: {
                        folder: folder
                    }
                };
            }

            function onSelectFolderType(node) {
                var data = classificationTree.tree('getData', node.target);
                vm.selectedFolders = data.attributes.folder;
                loadFolders();
            }

            function loadFolders() {
                searchMode = null;
                vm.folders = angular.copy(pagedResults);
                vm.loading = true;
                var promise = null;

                promise = FolderService.getFolders();
                promise.then(
                    function (data) {
                        vm.loading = false;
                        vm.items = data;
                        CommonService.getPersonReferences(vm.items.content, 'createdBy');
                        CommonService.getPersonReferences(vm.items.content, 'modifiedBy');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function newFolder() {
                $state.go('app.items.new');
            }

            function deleteFolder(item) {
                FolderService.deleteFolder(item.id).then(
                    function (data) {
                        var index = vm.items.content.indexOf(item);
                        vm.items.content.splice(index, 1);
                        $rootScope.showSuccessMessage(folderDeletedMessage);
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function openFolder(item) {
                $state.go('app.items.details', {folderId: item.id});
            }

            function nextPage() {
                if (vm.items.last != true) {
                    pageable.page++;
                }
            }

            function previousPage() {
                if (vm.items.first != true) {
                    pageable.page--;
                }
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();

                    initFolderTree();
                    loadFolderTree();
                    loadFolders();
                });
            })();
        }
    }
);
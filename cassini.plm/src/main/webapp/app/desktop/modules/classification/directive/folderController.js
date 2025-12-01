define(
    [
        'app/desktop/desktop.app',
        'app/shared/services/core/folderService'
    ],
    function (module) {
        module.controller('FolderController', FolderController);

        function FolderController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                  FolderService) {
            var vm = this;

            var nodeId = 0;
            var folderTree = null;
            var rootNode = null;

            function onSelectFolder(node) {
                var data = folderTree.tree('getData', node.target);
                var folder = data.attributes.folder;
                window.$("body").trigger("click");
            }

            function initfolderTree() {
                folderTree = $('#folderTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Folders',
                            //iconCls: 'classification-root',
                            iconCls: 'folder-root',
                            attributes: {
                                folder: null
                            },
                            children: [],
                            onSelect: onSelectFolder
                        }
                    ]
                });

                rootNode = folderTree.tree('find', 0);
            }

            function loadfolderTree() {
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

                        folderTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }, function (error) {
                          $rootScope.showErrorMessage(error.message);
                          $rootScope.hideBusyIndicator();
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
                    //iconCls: 'folder-node',
                    iconCls: 'pdm-folder',
                    attributes: {
                        itemType: folder
                    }
                };
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    initfolderTree();
                    loadfolderTree();
                });
            })();
        }
    }
);
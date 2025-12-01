define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/folderService'
    ],

    function (module) {
        module.directive('folderTree', ['$compile', '$timeout', 'FolderService', function ($compile, $timeout, FolderService) {
            return {
                templateUrl: 'app/desktop/modules/classification/directive/folderDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectFolder': '='
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var folderTree = null;
                    var rootNode = null;

                    function onSelectFolder(node) {
                        var data = folderTree.tree('getData', node.target);
                        var folder = data.attributes.folder;
                        $scope.onSelectFolder(folder);
                        window.$("body").trigger("click");
                    }

                    function initFolderTree() {
                        folderTree = $('#folderTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Folders',
                                    iconCls: 'folders-root',
                                    attributes: {
                                        folder: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectFolder
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
                                        visitChildren(node, folder.children);
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

                    function visitChildren(parent, folders) {
                        var nodes = [];
                        angular.forEach(folders, function (folder) {
                            var node = makeNode(folder);

                            if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, folder.children);
                            }

                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeNode(folder, iconCls) {
                        return {
                            id: ++nodeId,
                            text: folder.name,
                            iconCls: 'pdm-folder',
                            attributes: {
                                folder: folder
                            }
                        };
                    }

                    initFolderTree();
                    loadFolderTree();
                }
            };
        }]);
    }
);

define(['app/desktop/modules/classification/classification.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/folderService'
    ],
    function (module) {
        module.controller('FolderController', FolderController);

        function FolderController($q, $scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                  CommonService, AutonumberService, FolderService) {

            $rootScope.viewInfo.icon = "flaticon-marketing8";
            $rootScope.viewInfo.title = "Folders";

            var vm = this;

            vm.autoNumbers = [];

            vm.addFolder = addFolder;
            vm.deleteFolder = deleteFolder;
            vm.onSave = onSave;

            var nodeId = 0;
            var folderTree = null;
            var rootNode = null;

            function initFolderTree() {
                folderTree = $('#folderTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Folders',
                            iconCls: 'folder-root',
                            attributes: {
                                folder: null
                            },
                            children: []
                        }
                    ],
                    onContextMenu: onContextMenu,
                    onDblClick: onDblClick,
                    onAfterEdit: onAfterEdit,
                    onSelect: onSelectFolder
                });

                rootNode = folderTree.tree('find', 0);

                $(document).click(function () {
                    $("#contextMenu").hide();
                });
            }

            function onDblClick(node) {
                var data = folderTree.tree('getData', node.target);
                if (data.id != 0) {
                    folderTree.tree('beginEdit', node.target);
                }

                $timeout(function () {
                    $('.tree-editor').focus().select();
                }, 1);
            }

            function onAfterEdit(node) {
                var data = folderTree.tree('getData', node.target);
                if (data.attributes.folder == null) {
                    data.attributes.folder = {}
                }

                var parent = folderTree.tree('getParent', node.target);
                var parentData = folderTree.tree('getData', parent.target);

                if (parentData.id != 0) {
                    data.attributes.folder.parentType = parentData.attributes.folder.id;
                }

                data.attributes.folder.name = node.text;

                var promise = null;
                if (data.attributes.folder.id == undefined || data.attributes.folder.id == null) {
                    promise = FolderService.createFolder(data.attributes.folder);
                }
                else {
                    promise = FolderService.updateFolder(data.attributes.folder);
                }
                promise.then(
                    function (folder) {
                        data.attributes.folder = angular.copy(folder);
                        folderTree.tree('update', {target: node.target, attributes: data.attributes});

                        updateFolderInfo(data.attributes.folder);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                );
            }

            function updateFolderInfo(folder) {
                if (folder.folderNumberSource == null) {
                    AutonumberService.getAutonumberByName('Default Folder Number Source').then(
                        function (data) {
                            folder.folderNumberSource = data;
                            return FolderService.updateFolder(folder);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    ).then(
                        function (data) {
                            if (folder.revisionSequence == null) {
                                CommonService.getLovByName('Default Revision Sequence').then(
                                    function (data) {
                                        folder.revisionSequence = data;
                                        FolderService.updateFolder(folder);
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                     }
                                )
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }

                if (folder.revisionSequence == null) {
                    CommonService.getLovByName('Default Revision Sequence').then(
                        function (data) {
                            folder.revisionSequence = data;
                            return FolderService.updateFolder(folder);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    ).then(
                        function (data) {
                            if (folder.folderNumberSource == null) {
                                AutonumberService.getAutonumberByName('Default Item Number Source').then(
                                    function (data) {
                                        folder.folderNumberSource = data;
                                        return FolderService.updateFolder(folder);
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                     }
                                )
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                }
            }

            function onSelectFolder(node) {
                var data = folderTree.tree('getData', node.target);
                var folder = data.attributes.folder;

                $scope.$broadcast('app.folder.selected', {folder: folder});
                $scope.$apply();
            }

            function onContextMenu(e, node) {
                e.preventDefault();
                var $contextMenu = $("#contextMenu");
                $contextMenu.css({
                    left: e.pageX,
                    top: e.pageY
                });

                $contextMenu.show();

                $contextMenu.on("click", "a", function () {
                    $contextMenu.hide();
                });
            }

            function addFolder() {
                var selectedNode = folderTree.tree('getSelected');

                if (selectedNode != null) {
                    var nodeid = ++nodeId;

                    folderTree.tree('append', {
                        parent: selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: 'folder-node',
                                text: 'New Folder',
                                attributes: {
                                    folder: null
                                }
                            }
                        ]
                    });

                    var newNode = folderTree.tree('find', nodeid);
                    if (newNode != null) {
                        //classificationTree.tree('select', newNode.target);
                        folderTree.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                }
            }

            function deleteFolder() {
                var selectedNode = folderTree.tree('getSelected');
                if (selectedNode != null) {
                    var data = folderTree.tree('getData', selectedNode.target);
                    if (data != null && data.attributes.folder != null) {
                        FolderService.deleteFolder(data.attributes.folder.id).then(
                            function (data) {
                                folderTree.tree('remove', selectedNode.target);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                             }
                        )
                    }
                }
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

            function makeNode(folder) {
                return {
                    id: ++nodeId,
                    text: folder.name,
                    iconCls: 'folder-node',
                    attributes: {
                        folder: folder
                    }
                };
            }

            function onSave() {
                $scope.$broadcast('app.folder.save');
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();

                    initFolderTree();
                    loadFolderTree();
                });
            })();
        }
    }
);
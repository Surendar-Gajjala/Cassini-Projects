define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/folderService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/ecoService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService'
    ],
    function (module) {
        module.controller('FoldersWidgetController', FoldersWidgetController);

        function FoldersWidgetController($scope, $rootScope, $timeout, DialogService, $state, $stateParams, $cookies,
                                         FolderService, ItemService, MfrService, ECOService, $translate) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            var nodeId = 0;
            var foldersTree = null;
            var rootNode = null;

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
                foldersTree = $('#foldersTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: "Folders",
                            iconCls: 'folders-root',
                            attributes: {
                                type: 'ROOT'
                            },
                            children: []
                        }
                    ],
                    onContextMenu: onContextMenu,
                    onAfterEdit: onAfterEdit,
                    onBeforeExpand: onBeforeExpand,
                    onDblClick: onDblClick,
                    onSelect: onSelectFolder
                });

                rootNode = foldersTree.tree('find', 0);

                $(document).click(function () {
                    $("#foldersContextMenu").hide();
                });

                loadFolders();
            }

            function onSelectFolder(node) {
                $("#foldersContextMenu").hide();
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
            }

            vm.lastSelectedFolderName = null;
            function onDblClick(node) {
                vm.lastSelectedFolderName = null;
                if (node.attributes.type == 'ITEM') {
                    if (node.attributes.item != null && node.attributes.item != undefined) {
                        $state.go('app.items.details', {itemId: node.attributes.item.id});
                    }
                } else if (node.attributes.type == 'CHANGE') {
                    if (node.attributes.eco != null && node.attributes.eco != undefined) {
                        $state.go('app.changes.ecos.details', {ecoId: node.attributes.eco.id});
                    }
                } else if (node.attributes.type == 'MANUFACTURER') {
                    if (node.attributes.mfr != null && node.attributes.mfr != undefined) {
                        $state.go('app.mfr.details', {manufacturerId: node.attributes.mfr.id});
                    }
                }
                else if (node.attributes.type == 'FOLDER') {
                    var data = foldersTree.tree('getData', node.target);
                    if (data.id != 0) {
                        vm.lastSelectedFolderName = data.text;
                        foldersTree.tree('beginEdit', node.target);
                    }
                    $timeout(function () {
                        $('.tree-editor').focus().select();
                    }, 1);
                }
            }

            vm.itemObjects = [];
            vm.ecoObjects = [];
            vm.mfrObjects = [];

            function onBeforeExpand(node) {
                var nodeData = foldersTree.tree('getData', node.target);
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
                                            if (content.objectType == 'ITEM') {
                                                vm.itemObjects.push(content);
                                            } else if (content.objectType == 'CHANGE') {
                                                vm.ecoObjects.push(content);
                                            } else if (content.objectType == 'MANUFACTURER') {
                                                vm.mfrObjects.push(content);
                                            }
                                        });
                                        if (vm.itemObjects.length > 0) {
                                            loadObjects(node, folder, vm.itemObjects);
                                        }
                                        if (vm.ecoObjects.length > 0) {
                                            loadEcoObjects(node, folder, vm.ecoObjects);
                                        }
                                        if (vm.mfrObjects.length > 0) {
                                            loadMfrObjects(node, folder, vm.mfrObjects);
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
                                    var name = item.itemMasterObject.itemNumber + " : " + item.itemMasterObject.itemName + " : " + item.revision + " : " + item.lifeCyclePhase.phase;
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

                                foldersTree.tree('append', {
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
                                var name = null;
                                if (eco.statusType == "UNDEFINED") {
                                    name = eco.ecoNumber + " : " + eco.status + " : " + 'NORMAL';
                                } else {
                                    name = eco.ecoNumber + " : " + eco.status + " : " + eco.statusType;
                                }
                                var itemNode = {
                                    id: ++nodeId,
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

                            foldersTree.tree('append', {
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
                                    id: ++nodeId,
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

                            foldersTree.tree('append', {
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
                    var loadingNode = foldersTree.tree('find', loadingId);
                    if (loadingNode != null) {
                        foldersTree.tree('remove', loadingNode.target);
                    }
                }

            }

            function makeLoadingNode() {
                return {
                    id: ++nodeId,
                    text: "Loading...",
                    iconCls: 'items-loading',
                    type: 'LOADING'
                }
            }

            function onContextMenu(e, node) {
                e.preventDefault();
                var $contextMenu = $("#foldersContextMenu");
                var menus = ['addFolderMenuItem', 'deleteFolderMenuItem', 'removeItemMenuItem'];
                angular.forEach(menus, function (menu) {
                    $('#' + menu).hide();
                });
                if (node.attributes.type == 'ROOT') {
                    $('#addFolderMenuItem').hide();
                    $contextMenu.hide();
                }
                else if ((node.attributes.type == "PRIVATE" && vm.privatefolderSelected) || (node.attributes.type == "PUBLIC" && vm.publicfolderSelected)) {
                    $contextMenu.css({
                        display: "block",
                        left: e.pageX,
                        top: e.pageY
                    });
                    $('#addFolderMenuItem').show();
                }
                else if (node.attributes.type == 'FOLDER' && vm.folderSelected) {
                    $contextMenu.css({
                        display: "block",
                        left: e.pageX,
                        top: e.pageY
                    });
                    $('#addFolderMenuItem').show();
                    $('#deleteFolderMenuItem').show();
                }
                else if (node.attributes.type == 'ITEM') {
                    $contextMenu.css({
                        display: "block",
                        left: e.pageX,
                        top: e.pageY
                    });
                    $('#removeItemMenuItem').show();
                }
                else if (node.attributes.type == 'CHANGE') {
                    $contextMenu.css({
                        display: "block",
                        left: e.pageX,
                        top: e.pageY
                    });
                    $('#removeItemMenuItem').show();
                }
                else if (node.attributes.type == 'MANUFACTURER') {
                    $contextMenu.css({
                        display: "block",
                        left: e.pageX,
                        top: e.pageY
                    });
                    $('#removeItemMenuItem').show();
                } else {
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
            function addFolder() {
                vm.selectedNode = foldersTree.tree('getSelected');
                vm.selectedFolderName = vm.selectedNode.text;
                if (vm.selectedNode != null) {
                    var nodeid = ++nodeId;

                    foldersTree.tree('append', {
                        parent: vm.selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: vm.selectedNode.iconCls,
                                text: newFolder,
                                attributes: {
                                    folder: null,
                                    type: 'FOLDER'
                                }
                            }
                        ]
                    });

                    var newNode = foldersTree.tree('find', nodeid);
                    if (newNode != null) {
                        foldersTree.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                    if ($application.foldersData != null && vm.selectedNode == rootNode) {
                        $application.foldersData.push(newNode);
                    }
                }
            }

            function deleteFolder() {
                var options = {
                    title: deleteFolderTitle,
                    message: deleteFolderMessage,
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        vm.selectedNode = foldersTree.tree('getSelected');
                        if (vm.selectedNode != null) {
                            var data = foldersTree.tree('getData', vm.selectedNode.target);
                            if (data != null && data.attributes.folder != null) {
                                FolderService.deleteFolder(data.attributes.folder.id).then(
                                    function (data) {
                                        foldersTree.tree('remove', vm.selectedNode.target);
                                        $rootScope.showSuccessMessage(folderDeletedMessage);
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                     });
                                if ($application.foldersData != null) {
                                    var index = $application.foldersData.indexOf(vm.selectedNode);
                                    $application.foldersData.splice(index, 1);
                                }
                            }
                        }
                    }
                });
            }

            function onAfterEdit(node) {

                var data = foldersTree.tree('getData', node.target);
                /* data.attributes.folder.name = node.text;*/
                if (node.text == null || node.text == "") {
                    $timeout(function () {
                        $rootScope.showWarningMessage(folderNameCannotBeEmpty);

                        //data.attributes.folder.name = vm.lastSelectedFolderName;
                        //foldersTree.tree('append', data.target);
                        foldersTree.tree('beginEdit', node.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);

                        /*if (data.attributes.folder.id == undefined || data.attributes.folder.id == null) {
                         FolderService.createFolder(data.attributes.folder).then(
                         function (folderData) {
                         vm.lastSelectedFolderName = folderData.name;
                         node.text = folderData.name;
                         data.attributes.folder = angular.copy(folderData);
                         foldersTree.tree('update', {target: node.target, attributes: data.attributes});
                         //foldersTree.tree('select', node.target);
                         }, function (error) {
                         $rootScope.showWarningMessage(error.message);
                         foldersTree.tree('remove', data.target);
                         }
                         )
                         } else {
                         FolderService.updateFolder(data.attributes.folder).then(
                         function (folderData) {
                         vm.lastSelectedFolderName = folderData.name;
                         node.text = folderData.name;
                         data.attributes.folder = angular.copy(folderData);
                         foldersTree.tree('update', {target: node.target, attributes: data.attributes});
                         //foldersTree.tree('select', node.target);
                         }, function (error) {
                         $rootScope.showWarningMessage(error.message);
                         data.attributes.folder.name = vm.lastSelectedFolderName;
                         data.text = vm.lastSelectedFolderName;
                         foldersTree.tree('update', {target: node.target, attributes: data.attributes});
                         //foldersTree.tree('select', node.target);
                         }
                         )
                         }*/

                    }, 10)

                    // foldersTree.tree('remove', data.target);
                }
                else {

                    if (data.attributes.folder == null) {
                        data.attributes.folder = {}
                    }

                    var parent = foldersTree.tree('getParent', node.target);
                    var parentData = foldersTree.tree('getData', parent.target);

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
                                foldersTree.tree('update', {target: node.target, attributes: data.attributes});
                                foldersTree.tree('select', node.target);
                            }, function (error) {
                                $rootScope.showWarningMessage(error.message);
                                foldersTree.tree('remove', data.target);
                            }
                        )
                    } else {
                        FolderService.updateFolder(data.attributes.folder).then(
                            function (folderData) {
                                data.attributes.folder = angular.copy(folderData);
                                foldersTree.tree('update', {target: node.target, attributes: data.attributes});
                                foldersTree.tree('select', node.target);
                            }, function (error) {
                                $rootScope.showWarningMessage(error.message);
                                data.attributes.folder.name = vm.lastSelectedFolderName;
                                data.text = vm.lastSelectedFolderName;
                                foldersTree.tree('update', {target: node.target, attributes: data.attributes});
                                foldersTree.tree('select', node.target);
                            }
                        )
                    }
                }

                /*if (data.attributes.folder.id == undefined || data.attributes.folder.id == null) {
                 promise = FolderService.createFolder(data.attributes.folder);
                 }
                 else {
                 promise = FolderService.updateFolder(data.attributes.folder);
                 }
                 promise.then(
                 function (folder) {
                 data.attributes.folder = angular.copy(folder);
                 foldersTree.tree('update', {target: node.target, attributes: data.attributes});
                 foldersTree.tree('select', node.target);
                 }, function (error) {
                 if (vm.selectedFolderName == vm.lastSelectedFolderName) {
                 $rootScope.showWarningMessage(error.message);
                 foldersTree.tree('remove', data.target);
                 } else {
                 $rootScope.showWarningMessage(error.message);
                 data.attributes.folder.name = vm.lastSelectedFolderName;
                 data.text = vm.lastSelectedFolderName;
                 foldersTree.tree('update', {target: node.target, attributes: data.attributes});
                 foldersTree.tree('select', node.target);
                 }

                 }
                 );*/
            }

            function makeNode(folder, iconCls) {
                return {
                    id: ++nodeId,
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
                            id: ++nodeId,
                            text: "My Folders",
                            iconCls: 'private-folder',
                            attributes: {
                                root: true,
                                type: 'PRIVATE'
                            },
                            children: []
                        };

                        var publicFoldersRoot = {
                            id: ++nodeId,
                            text: "Public Folders",
                            iconCls: 'public-folder',
                            attributes: {
                                root: true,
                                type: 'PUBLIC'
                            },
                            children: []
                        };
                        var myFolders = data.myFolders;
                        var nodes = [];
                        angular.forEach(myFolders, function (folder) {
                            var node = makeNode(folder, 'private-folder');
                            if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, folder.children);
                            }
                            else {
                                if (folder.objectsExist) {
                                    node.state = 'closed';
                                    node.children = [];
                                    var loadingNode = makeLoadingNode();
                                    node.children.push(loadingNode);
                                    node.attributes.loadingId = loadingNode.id;
                                }
                            }

                            nodes.push(node);
                        });

                        myFoldersRoot.children = nodes;

                        foldersTree.tree('append', {
                            parent: rootNode.target,
                            data: myFoldersRoot
                        });

                        var publicFolders = data.publicFolders;
                        var publicNodes = [];
                        angular.forEach(publicFolders, function (folder) {
                            var node = makeNode(folder, 'public-folder');
                            if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, folder.children);
                            }
                            else {
                                if (folder.objectsExist) {
                                    node.state = 'closed';
                                    node.children = [];
                                    var loadingNode = makeLoadingNode();
                                    node.children.push(loadingNode);
                                    node.attributes.loadingId = loadingNode.id;
                                }
                            }

                            publicNodes.push(node);
                        });

                        publicFoldersRoot.children = publicNodes;

                        foldersTree.tree('append', {
                            parent: rootNode.target,
                            data: publicFoldersRoot
                        });

                        //$application.foldersData = nodes;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )

            }

            function visitChildren(parent, folders) {
                var iconCls = 'private-folder';
                if (parent.type === 'PUBLIC') {
                    iconCls = 'public-folder';
                }

                var nodes = [];
                angular.forEach(folders, function (folder) {
                    var node = makeNode(folder, iconCls);
                    node.children = [];
                    if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                        node.state = "closed";
                        visitChildren(node, folder.children);
                    }
                    else {
                        if (folder.objectsExist) {
                            node.state = 'closed';
                            node.children = [];
                            var loadingNode = makeLoadingNode();
                            node.children.push(loadingNode);
                            node.attributes.loadingId = loadingNode.id;
                        }
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
                        vm.selectedNode = foldersTree.tree('getSelected');
                        if (vm.selectedNode != null) {
                            var data = foldersTree.tree('getData', vm.selectedNode.target);
                            var selectedType = data.attributes.type;
                            if (data.attributes.type == 'ITEM') {
                                vm.removeItemName = data.attributes.item.itemMasterObject.itemNumber;
                            } else if (data.attributes.type == 'CHANGE') {
                                vm.removeItemName = data.attributes.eco.ecoNumber;
                            } else if (data.attributes.type == 'MANUFACTURER') {
                                vm.removeItemName = data.attributes.mfr.name;
                            }
                            FolderService.getFolderById(data.attributes.object.folder).then(
                                function (folderName) {
                                    vm.folderName = folderName.name;
                                    if (data != null) {
                                        FolderService.removeFolderObject(data.attributes.object.id).then(
                                            function (data) {
                                                foldersTree.tree('remove', vm.selectedNode.target);
                                                if (selectedType == 'ITEM') {
                                                    $rootScope.showSuccessMessage(vm.removeItemName + " : " + itemRemovedMessage + "(" + vm.folderName + ")" + folder);
                                                } else if (selectedType == 'CHANGE') {
                                                    $rootScope.showSuccessMessage(vm.removeItemName + ": " + ecoRemovedMessage + "(" + vm.folderName + ")" + folder);
                                                } else if (selectedType == 'MANUFACTURER') {
                                                    $rootScope.showSuccessMessage(vm.removeItemName + ":" + mfrRemovedMessage + "(" + vm.folderName + ")" + folder);
                                                }

                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
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

            (function () {
                $timeout(function () {
                    try {
                        initFoldersTree();
                    }
                    catch (e) {
                    }
                }, 1000);
            })();
        }
    }
)
;
define(
    [
        'app/desktop/modules/dm/dm.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/documentService',
        'app/desktop/modules/securityPermission/securityPermissionService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService'
    ],
    function (module) {
        module.controller('AllDocumentManagementController', AllDocumentManagementController);

        function AllDocumentManagementController($scope, $rootScope, $timeout, $translate, $application, $window, $state, $stateParams, $cookies, DialogService, DocumentService,
                                                 PersonGroupService, SecurityPermissionService) {

            var vm = this;
            $rootScope.viewInfo.showDetails = false;
            $scope.selectedFolderId = 0;
            var nodeId = 0;
            var documentFolderTree = null;
            var rootNode = null;
            var parsed = angular.element("<div></div>");
            var folderNameCannotBeEmpty = parsed.html($translate.instant("FOLDER_NAME_CANNOT_BE_EMPTY")).html();
            var deleteFolderTitle = parsed.html($translate.instant("DELETE_FOLDER_TITLE")).html();
            var deleteFolderMessage = parsed.html($translate.instant("DELETE_FOLDER_MESSAGE")).html();
            var folderDeletedMessage = parsed.html($translate.instant("FOLDER_DELETED_MESSAGE")).html();
            $scope.searchTitle = parsed.html($translate.instant("SEARCH")).html();
            var savedPermission = parsed.html($translate.instant("PERMISSIONS_SAVED")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.dmPrivileges = ['all', 'create', 'view', 'edit', 'rename', 'delete', 'download', 'preview', 'replace', 'promote', 'demote', 'revise'];
            vm.checkAllPrivileges = [];
            vm.roleIds = [];
            vm.checkPrivilege = {
                privilege: "",
                checked: false
            };
            vm.dmPermission = {
                folderId: null,
                groupId: null,
                actions: []
            };
            vm.dmPermissionView = false;

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
                    onContextMenu: onContextMenu,
                    onAfterEdit: onAfterEdit,
                    onDblClick: onDblClick,
                    onSelect: onSelectFolder
                });

                rootNode = documentFolderTree.tree('find', 0);
                /*if (rootNode != null) {
                 rootNode.target.className = "tree-node tree-node-hover tree-node-selected";
                 }*/
                $(document).click(function () {
                    $("#foldersContextMenu").hide();
                });

                loadFolders();
                loadDocumentsCount();
            }

            function onContextMenu(e, node) {
                e.preventDefault();
                var $contextMenu = $("#foldersContextMenu");
                if (node.attributes.type == 'ROOT') {
                    $('#addFolderMenuItem').show();
                    $('#deleteFolderMenuItem').hide();
                    $('#addPermission').hide();
                    $('#shareFolder').hide();
                    $contextMenu.css({
                        display: "block",
                        left: e.pageX,
                        top: e.pageY
                    });
                } else if (node.attributes.type == 'FOLDER') {
                    $contextMenu.css({
                        display: "block",
                        left: e.pageX,
                        top: e.pageY
                    });
                    vm.folderId = node.attributes.folder.id;
                    vm.folderName = node.attributes.folder.name;
                    $('#addFolderMenuItem').show();
                    $('#deleteFolderMenuItem').show();
                    $('#shareFolder').show();
                    if ($rootScope.hasPermission('document', 'permission')) $('#addPermission').show();
                    else $('#addPermission').hide();
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

            function onAfterEdit(node) {

                var data = documentFolderTree.tree('getData', node.target);
                if (node.text == null || node.text == "") {
                    $timeout(function () {
                        $rootScope.showWarningMessage(folderNameCannotBeEmpty);
                        documentFolderTree.tree('beginEdit', node.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }, 10);
                } else {

                    if (data.attributes.folder == null) {
                        data.attributes.folder = {}
                    }

                    var parent = documentFolderTree.tree('getParent', node.target);
                    var parentData = documentFolderTree.tree('getData', parent.target);

                    if (parentData.id != 0) {
                        data.attributes.folder.parentFile = parentData.attributes.folder.id;
                    }

                    data.attributes.folder.name = node.text;
                    data.attributes.folder.fileType = "FOLDER";

                    var promise = null;
                    if (data.attributes.folder.id == undefined || data.attributes.folder.id == null) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        DocumentService.createDocumentFolder(data.attributes.folder).then(
                            function (folderData) {
                                data.attributes.folder = angular.copy(folderData);
                                documentFolderTree.tree('update', {target: node.target, attributes: data.attributes});
                                documentFolderTree.tree('select', node.target);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showWarningMessage(error.message);
                                documentFolderTree.tree('remove', data.target);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        $rootScope.showBusyIndicator($('.view-container'));
                        DocumentService.updateDocumentFolder(data.attributes.folder).then(
                            function (folderData) {
                                data.attributes.folder = angular.copy(folderData);
                                node.text = folderData.name;
                                if (folderData.count != null && folderData.count != "" && folderData.count > 0) {
                                    node.text = folderData.name + "<span class='badge badge-info' style='margin-left: 5px;margin-top: -2px' title='Files'>" + folderData.count + "</span>";
                                }
                                documentFolderTree.tree('update', {target: node.target, attributes: data.attributes});
                                documentFolderTree.tree('select', node.target);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showWarningMessage(error.message);
                                data.attributes.folder.name = vm.lastSelectedFolderName;
                                data.text = vm.lastSelectedFolderName;
                                node.text = data.name;
                                if (data.attributes.folder.count != null && data.attributes.folder.count != "" && data.attributes.folder.count > 0) {
                                    node.text = data.attributes.folder.name + "<span class='badge badge-info' style='margin-left: 5px;margin-top: -2px' title='Files'>" + data.attributes.folder.count + "</span>";
                                }
                                documentFolderTree.tree('update', {target: node.target, attributes: data.attributes});
                                documentFolderTree.tree('select', node.target);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            vm.lastSelectedFolderName = null;

            function onDblClick(node) {
                vm.lastSelectedFolderName = null;
                var data = documentFolderTree.tree('getData', node.target);
                if (data.id != 0) {
                    vm.lastSelectedFolderName = data.text;
                    node.text = node.attributes.folder.name;
                    documentFolderTree.tree('beginEdit', node.target);
                }
                $timeout(function () {
                    $('.tree-editor').focus().select();
                }, 1);
            }

            $rootScope.folderObject = null;
            vm.previousSelectedFolder = 0;
            vm.selectedFolderNode = null;

            function onSelectFolder(node) {
                $rootScope.folderObject = null;
                $("#foldersContextMenu").hide();
                vm.previousSelectedFolder = $scope.selectedFolderId;
                if (node.attributes.type == "ROOT") {
                    $scope.selectedFolderId = 0;
                } else {
                    $scope.selectedFolderId = node.attributes.folder.id;
                    $rootScope.folderObject = node.attributes.folder;
                }
                $scope.$evalAsync();
                if ($scope.selectedFolderId != vm.previousSelectedFolder) {
                    vm.conversationFolder = null;
                    vm.selectedConversationFolderNode = null;
                    vm.selectedFolderNode = node;
                    $timeout(function () {
                        vm.title = $('#' + node.domId + ' .tree-title').outerWidth();
                        vm.indent = $('#' + node.domId + ' .tree-indent').outerWidth();
                        vm.icon = $('#' + node.domId + ' .tree-icon').outerWidth();
                        if (vm.title != null && vm.title > 250) {
                            // $('#' + node.domId + '.tree-node-selected').width(vm.title + (2 * vm.indent) + vm.icon);
                            $('.tree-node-selected').width(vm.title + (2 * vm.indent) + vm.icon);
                        } else {
                            $('.tree-node-selected').width(250);
                        }
                        $scope.$broadcast('app.objectFile.tabActivated', {load: true});
                    }, 500);
                }

            }

            vm.selectedFolderName = null;
            vm.addFolder = addFolder;
            vm.deleteFolder = deleteFolder;

            function addFolder() {
                vm.selectedNode = documentFolderTree.tree('getSelected');
                vm.selectedFolderName = vm.selectedNode.text;
                if (vm.selectedNode != null) {
                    var nodeid = ++nodeId;

                    documentFolderTree.tree('append', {
                        parent: vm.selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: "pdm-folder",
                                text: "New Folder",
                                attributes: {
                                    folder: null,
                                    type: 'FOLDER'
                                }
                            }
                        ]
                    });

                    var newNode = documentFolderTree.tree('find', nodeid);
                    if (newNode != null) {
                        documentFolderTree.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
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
                        vm.selectedNode = documentFolderTree.tree('getSelected');
                        if (vm.selectedNode != null) {
                            var data = documentFolderTree.tree('getData', vm.selectedNode.target);
                            if (data != null && data.attributes.folder != null) {
                                $rootScope.showBusyIndicator($('.view-container'));
                                DocumentService.deleteDocumentFolder(data.attributes.folder.id).then(
                                    function (data) {
                                        documentFolderTree.tree('remove', vm.selectedNode.target);
                                        rootNode = documentFolderTree.tree('find', 0);
                                        if (rootNode != null) {
                                            rootNode.target.className = "tree-node tree-node-hover tree-node-selected";
                                        }
                                        $scope.selectedFolderId = 0;
                                        $timeout(function () {
                                            $scope.$broadcast('app.objectFile.tabActivated', {load: true});
                                        }, 500);
                                        $rootScope.showSuccessMessage(folderDeletedMessage);
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                );
                            }
                        }
                    }
                });
            }

            vm.allDocumentsCount = 0;

            function loadDocumentsCount() {
                DocumentService.getTotalDocumentsCount().then(
                    function (data) {
                        vm.allDocumentsCount = data;
                        loadGroups();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.dmPermissionsTableList = [];
            vm.rolePermissions = {
                role: {
                    id: null,
                    name: ""
                },
                privilege: []
            };
            vm.privilegeList = {
                privilege: "",
                checked: false
            };

            function addDMPermissions() {
                $rootScope.showBusyIndicator($('.view-container'));
                SecurityPermissionService.getDMPermissionByIdAndType(vm.folderId, "folderId").then(
                    function (data) {
                        vm.folderPermissions = [];
                        vm.dmPermissionsTableList = [];
                        vm.checkAllPrivileges = [];
                        if (data.length > 0) vm.folderPermissions = data;
                        parseActions();
                        vm.dmPermissionView = true;
                        checkSelected();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function checkSelected() {
                angular.forEach(vm.roles, function (role) {
                    var rolePermissions = angular.copy(vm.rolePermissions);
                    rolePermissions.role.id = role.groupId;
                    rolePermissions.role.name = role.name;
                    angular.forEach(vm.dmPrivileges, function (privilege) {
                        var privilegeList = angular.copy(vm.privilegeList);
                        privilegeList.privilege = privilege;
                        angular.forEach(vm.folderPermissions, function (permission) {
                            if (permission.groupId == role.groupId && permission.actions.includes(privilege))
                                privilegeList.checked = true;
                        });
                        rolePermissions.privilege.push(privilegeList);
                    });
                    vm.dmPermissionsTableList.push(rolePermissions);
                });

                angular.forEach(vm.dmPrivileges, function (privilege) {
                    var checkPrivilege = angular.copy(vm.checkPrivilege);
                    checkPrivilege.privilege = privilege;
                    vm.checkAllPrivileges.push(checkPrivilege);
                });
            }

            function loadGroups() {
                $rootScope.showBusyIndicator($('.view-container'));
                PersonGroupService.getAllPersonGroups().then(
                    function (data) {
                        vm.roleIds = [];
                        vm.roles = data;
                        angular.forEach(vm.roles, function (role) {
                            vm.roleIds.push(role.groupId);
                        });
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            vm.showDMPermissions = showDMPermissions;

            function showDMPermissions() {
                vm.folderPermissions = [];
                var modal = document.getElementById("dm-permissions");
                modal.style.display = "block";
                $timeout(function () {
                    $scope.errorNotification = $('#configuration-error');
                }, 200)
                addDMPermissions();
            }

            vm.hideDMPermissions = hideDMPermissions;

            function hideDMPermissions() {
                var modal = document.getElementById("dm-permissions");
                modal.style.display = "none";
            }


            vm.saveDMPermissions = saveDMPermissions;

            function saveDMPermissions() {
                stringifyActions();
                SecurityPermissionService.saveDMPermissions(vm.folderPermissions).then(
                    function (data) {
                        addDMPermissions();
                        $scope.errorNotification.show();
                        $scope.errorNotification.removeClass('zoomOut');
                        $scope.errorNotification.addClass('zoomIn');
                        $scope.notificationClass = "fa-check";
                        $scope.notificationBackground = "alert-success";
                        $scope.message = savedPermission;
                        $timeout(function () {
                            closeErrorNotification();
                        }, 3000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $scope.closeErrorNotification = closeErrorNotification;

            function closeErrorNotification() {
                $scope.errorNotification.removeClass('zoomIn');
                $scope.errorNotification.addClass('zoomOut');
                $scope.errorNotification.hide();
            }


            function parseActions() {
                if (vm.folderPermissions.length == 0) vm.isSubFolder = false;
                angular.forEach(vm.folderPermissions, function (permission) {
                    vm.isSubFolder = permission.isSubFolder;
                    permission.actions = JSON.parse(permission.actions);
                });
            }

            function stringifyActions() {
                angular.forEach(vm.folderPermissions, function (permission) {
                    permission.isSubFolder = vm.isSubFolder;
                    permission.actions = JSON.stringify(permission.actions);
                });
            }

            vm.toggleDMPermission = toggleDMPermission;

            function toggleDMPermission(privilege, role) {
                var dmPermission = angular.copy(vm.dmPermission);
                dmPermission.folderId = vm.folderId;
                dmPermission.groupId = role.id;
                dmPermission.actions.push(privilege.privilege);
                var existRole = checkForRoleId(vm.folderPermissions, role.id, "folderPermissions");
                if (privilege.checked) {
                    if (privilege.privilege == 'all') {
                        checkAllOrUncheck("check", "folderPermissions", role, vm.folderPermissions);
                        checkAllOrUncheck("check", "dmPermissionsTableList", role, vm.dmPermissionsTableList);
                    } else {
                        if (vm.folderPermissions.length > 0) {
                            if (existRole != null) existRole.actions.push(privilege.privilege);
                            else vm.folderPermissions.push(dmPermission);
                        } else {
                            vm.folderPermissions.push(dmPermission);
                        }
                    }
                } else {
                    if (privilege.privilege == 'all') {
                        checkAllOrUncheck("uncheck", "folderPermissions", role, vm.folderPermissions);
                        checkAllOrUncheck("uncheck", "dmPermissionsTableList", role, vm.dmPermissionsTableList);
                    } else {
                        var index = existRole.actions.indexOf(privilege.privilege);
                        existRole.actions.splice(index, 1);
                    }
                }
            }

            function checkAllOrUncheck(type, listType, role, list) {
                var existRole1 = checkForRoleId(list, role.id, listType);
                var dmPermission = angular.copy(vm.dmPermission);
                var dmPrivileges = angular.copy(vm.dmPrivileges);
                dmPermission.folderId = vm.folderId;
                dmPermission.groupId = role.id;
                if (type == 'check') {
                    if (listType == 'folderPermissions') {
                        if (existRole1 != null) existRole1.actions = dmPrivileges;
                        else {
                            dmPermission.actions = dmPrivileges;
                            list.push(dmPermission);
                        }
                    } else {
                        angular.forEach(existRole1.privilege, function (val) {
                            val.checked = true;
                        })
                    }
                } else {
                    if (listType == 'folderPermissions') {
                        if (existRole1 != null) existRole1.actions = [];
                        else dmPermission.actions = [];
                    } else {
                        angular.forEach(existRole1.privilege, function (val) {
                            val.checked = false;
                        })
                    }
                }
            }

            vm.checkAllPrivilege = checkAllPrivilege;

            function checkAllPrivilege(val) {
                selectAllPrivilegeByRole(val, vm.dmPermissionsTableList, "dmPermissionsTableList");
                selectAllPrivilegeByRole(val, vm.folderPermissions, "folderPermissions");
            }

            function selectAllPrivilegeByRole(val, permissionList, type) {
                if (type == "dmPermissionsTableList") {
                    angular.forEach(permissionList, function (list) {
                        angular.forEach(list.privilege, function (prv) {
                            if (val.privilege == 'all') {
                                prv.checked = val.checked;
                                angular.forEach(vm.checkAllPrivileges, function (prv1) {
                                    prv1.checked = val.checked;
                                });
                            } else if (prv.privilege == val.privilege) prv.checked = val.checked;
                        });
                    });
                } else {
                    if (val.checked) {
                        angular.forEach(vm.roleIds, function (roleId) {
                            var dmPermission = angular.copy(vm.dmPermission);
                            dmPermission.folderId = vm.folderId;
                            dmPermission.groupId = roleId;
                            var existRole = checkForRoleId(permissionList, roleId, "folderPermissions");
                            if (val.privilege == 'all') {
                                if (permissionList.length > 0) {
                                    if (existRole != null) {
                                        existRole.actions = vm.dmPrivileges;
                                    } else {
                                        dmPermission.actions = vm.dmPrivileges;
                                        permissionList.push(dmPermission);
                                    }
                                } else {
                                    dmPermission.actions = vm.dmPrivileges;
                                    permissionList.push(dmPermission);
                                }
                            } else {
                                if (permissionList.length > 0) {
                                    if (existRole != null) {
                                        if (existRole.actions.indexOf(val.privilege) == -1) existRole.actions.push(val.privilege);
                                    } else {
                                        dmPermission.actions.push(val.privilege);
                                        permissionList.push(dmPermission);
                                    }
                                } else {
                                    dmPermission.actions.push(val.privilege);
                                    permissionList.push(dmPermission);
                                }
                            }
                        });
                    } else {
                        angular.forEach(vm.roleIds, function (roleId) {
                            var existRole = checkForRoleId(permissionList, roleId, "folderPermissions");
                            if (val.privilege == 'all') existRole.actions = [];
                            else {
                                var index = existRole.actions.indexOf(val.privilege);
                                existRole.actions.splice(index, 1);
                            }
                        });
                    }
                }
            }

            function checkForRoleId(permissions, roleId, type) {
                var existRole = null;
                var flag = true;
                angular.forEach(permissions, function (permission) {
                    if (type == "folderPermissions") {
                        if (permission.groupId == roleId && flag) {
                            existRole = permission;
                            flag = false;
                        }
                    } else {
                        if (permission.role.id == roleId && flag) {
                            existRole = permission;
                            flag = false;
                        }
                    }
                });
                return existRole;
            }

            vm.conversationFolder = null;
            vm.selectedConversationFolderNode = null;

            function loadFolders() {
                var documentFolder = $window.localStorage.getItem("document-folder");
                if (documentFolder != null && documentFolder != "") {
                    vm.conversationFolder = parseInt(documentFolder);
                }
                $rootScope.showBusyIndicator($('.view-container'));
                DocumentService.getDocumentFolderTree().then(
                    function (data) {
                        var nodes = [];
                        vm.folderData = data;
                        angular.forEach(data, function (folder) {
                            var node = makeNode(folder);
                            if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                                node = visitChildren(node, folder.children);
                                node.state = "closed";
                            }
                            if (vm.conversationFolder != null && vm.conversationFolder == folder.id) {
                                vm.selectedConversationFolderNode = node;
                                node.conversationFolder = folder.id;
                            } else if (node.conversationFolder != null) {
                                node.state = "open";
                            }
                            nodes.push(node);
                        });

                        documentFolderTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                        vm.loading = false;
                        $window.localStorage.setItem("document-folder", null);
                        if (vm.selectedConversationFolderNode != null) {
                            var selectedFolder = documentFolderTree.tree('find', vm.selectedConversationFolderNode.id);
                            selectedFolder.target.className = "tree-node tree-node-hover tree-node-selected";
                            $scope.selectedFolderId = vm.conversationFolder;
                        } else {
                            rootNode = documentFolderTree.tree('find', 0);
                            if (rootNode != null) {
                                rootNode.target.className = "tree-node tree-node-hover tree-node-selected";
                            }
                        }
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
                        node = visitChildren(node, folder.children);
                    }
                    if (vm.conversationFolder != null && vm.conversationFolder == folder.id) {
                        vm.selectedConversationFolderNode = node;
                        node.conversationFolder = folder.id;
                        parent.conversationFolder = folder.id;
                        parent.state = "open";
                    } else if (node.conversationFolder != null) {
                        parent.conversationFolder = folder.id;
                        parent.state = "open";
                    }
                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
                return parent;
            }

            function makeNode(folder) {
                var text = folder.name;
                if (folder.count != null && folder.count != "" && folder.count > 0) {
                    text = folder.name + "<span class='label label-default label-count' style='margin-left: 5px;margin-top: -2px;font-size: 12px;background-color: #e4dddd;padding: 1px 4px !important;' title='Files'>" + folder.count + "</span>";
                }
                return {
                    id: ++nodeId,
                    text: text,
                    iconCls: "pdm-folder",
                    attributes: {
                        folder: folder,
                        type: "FOLDER"
                    },
                    conversationFolder: null
                };
            }

            vm.freeTextSearch = freeTextSearch;

            function freeTextSearch(freeText) {
                $rootScope.freeTextQuerys = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    $scope.$broadcast('app.details.files.search', {name: freeText});
                } else {
                    $rootScope.freeTextQuerys = null;
                    $scope.$broadcast('app.objectFile.tabActivated', {load: true});
                }
            }


            vm.resetPage = resetPage;

            function resetPage() {
                $rootScope.freeTextQuerys = null;
                freeTextSearch($rootScope.freeTextQuerys);
            }

            vm.searchTree = searchTree;
            vm.searchValue = null;

            function searchTree() {
                var nodes = []
                /*if (vm.searchValue != null) {
                    $('#documentFolderTree').tree('expandAll');
                }
                $('#documentFolderTree').tree('doFilter', vm.searchValue);*/
                nodeId = 0;
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
                    onContextMenu: onContextMenu,
                    onAfterEdit: onAfterEdit,
                    onDblClick: onDblClick,
                    onSelect: onSelectFolder
                });

                rootNode = documentFolderTree.tree('find', 0);
                $(document).click(function () {
                    $("#foldersContextMenu").hide();
                });

                angular.forEach(vm.folderData, function (folder) {
                    folder.searchExist = false;
                    if (folder.name.toLowerCase().includes(vm.searchValue.toLowerCase())) {
                        folder.searchExist = true;
                    }
                    visitChildren1(folder, vm.searchValue);
                });

                angular.forEach(vm.folderData, function (folder) {
                    if (folder.searchExist) {
                        var node = makeNode(folder);
                        if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                            node = visitSearchChildren(node, folder.children);
                            node.state = "open";
                        }
                        nodes.push(node);
                    }
                });
                documentFolderTree.tree('append', {
                    parent: rootNode.target,
                    data: nodes
                });

            }

            function visitSearchChildren(parent, folders) {
                var nodes = [];
                angular.forEach(folders, function (folder) {
                    if (folder.searchExist) {
                        var node = makeNode(folder);
                        node.children = [];
                        if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                            node.state = "open";
                            node = visitSearchChildren(node, folder.children);
                        }
                        nodes.push(node);
                    }
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
                return parent;
            }

            function visitChildren1(parentNode, searchVal) {
                angular.forEach(parentNode.children, function (folder) {
                    folder.searchExist = false;
                    if (folder.name.toLowerCase().includes(searchVal.toLowerCase())) {
                        folder.searchExist = true;
                    }
                    folder = visitChildren1(folder, searchVal);
                });

                angular.forEach(parentNode.children, function (folder) {
                    if (folder.searchExist) {
                        parentNode.searchExist = true;
                    }
                })
                return parentNode;
            }


            function update(event, args) {
                if ($scope.selectedFolderId != 0) {
                    DocumentService.getFolderDocumentsCount($scope.selectedFolderId).then(
                        function (data) {
                            var node = documentFolderTree.tree('find', vm.selectedFolderNode.id);
                            if (node != null) {
                                if (data > 0) {
                                    documentFolderTree.tree('update', {
                                            target: node.target,
                                            text: vm.selectedFolderNode.attributes.folder.name + "<span class='label label-default label-count' style='margin-left: 5px;margin-top: -2px;font-size: 12px;background-color: #e4dddd;padding: 1px 4px !important;' title='Files'>" + data + "</span>"
                                        }
                                    );
                                }
                            }
                        }
                    )
                }
            }

            vm.shareFolder = shareFolder;
            var shareButton = parsed.html($translate.instant("SHARE")).html();
            var shareFolderTitle = parsed.html($translate.instant("SHARE_FOLDER")).html();

            function shareFolder() {
                var options = {
                    title: shareFolderTitle,
                    template: 'app/desktop/modules/shared/share/shareView.jsp',
                    controller: 'ShareController as shareVm',
                    resolve: 'app/desktop/modules/shared/share/shareController',
                    width: 600,
                    showMask: true,
                    data: {
                        sharedItem: $rootScope.folderObject,
                        itemsSharedType: 'itemSelection',
                        objectType: "DOCUMENT"
                    },
                    buttons: [
                        {text: shareButton, broadcast: 'app.share.item'}
                    ],
                    callback: function (data) {
                        $rootScope.showSuccessMessage($rootScope.folderObject.name + " : Shared successfully");
                    }
                };

                $rootScope.showSidePanel(options);
            }


            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $timeout(function () {
                        try {
                            initFoldersTree();
                        } catch (e) {
                        }
                    }, 1000);
                    $scope.$on("app.documents.folder.update", update);
                });
            })();
        }
    }
)
;
define(['app/desktop/modules/vaults/vault.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/shared/services/vaultsDetailsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/roleService',
        'split-pane',
        'jquery.easyui',
        'dropzone',
        'app/shared/services/vaultService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService'

    ],
    function (module) {
        module.controller('VaultDetailsController', VaultDetailsController);

        function VaultDetailsController($scope, $rootScope, $timeout, $window, $q, $state, $uibModal, DialogService, $stateParams, $cookies,
                                        VaultsDetailsService, CommonService, RoleService, VaultService) {
            $rootScope.viewInfo.icon = "fa fa-files-o";
            $rootScope.viewInfo.title = "Vaults";
            $rootScope.viewInfo.showDetails = true;

            var vm = this;

            vm.vaultObj = null;
            vm.rolePermissionActions = [];
            vm.personPermissionActions = [];

            vm.loading = true;
            vm.showDropzone = false;
            vm.show = false;
            vm.selectedFolder = null;
            vm.folderType = null;
            vm.person = null;
            vm.role = null;
            var lastSelectedItem = null;
            vm.permission = false;
            vm.persons = [];
            vm.roles = [];
            vm.objectPermissions = [];
            vm.permissions = [];
            vm.files = [];
            vm.vaultId = $stateParams.vaultId;
            vm.addFolder = addFolder;
            vm.deleteFolder = deleteFolder;
            vm.addFiles = addFiles;
            vm.backToFiles = backToFiles;
            vm.back = back;
            vm.backTo = backTo;
            vm.fileSizeToString = fileSizeToString;
            vm.downloadFile = downloadFile;
            vm.lockFile = lockFile;
            vm.showFileHistory = showFileHistory;
            vm.deleteFile = deleteFile;
            vm.filesSerach = false;

            vm.getPermissionActions = getPermissionActions;
            vm.checkFolderPermission = checkFolderPermission;
            vm.checkPermissionChanges = checkPermissionChanges;
            vm.checkAddFilesPermissions = checkAddFilesPermissions;
            vm.checkFileViewPermissions = checkFileViewPermissions;
            vm.checkDeleteFilesPermissions = checkDeleteFilesPermissions;

            vm.addPermission = addPermission;
            vm.selectPermissions = selectPermissions;
            vm.checkAllRolePermissions = checkAllRolePermissions;
            vm.checkNoneRolePermissions = checkNoneRolePermissions;
            vm.checkAllPersonPermissions = checkAllPersonPermissions;
            vm.checkNonePersonPermissions = checkNonePersonPermissions;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.showFileDropzone = false;
            vm.showNewCommit = showNewCommit;

            var nodeId = 0;
            var rootNode = null;
            var foldersTree = null;
            var fileDropZone = null;
            var userPermissions = [];

            function backTo() {
                vm.showDropzone = false;
                vm.permission = false;
            }

            function back() {
                $window.history.back();
            }

            vm.searchTree = searchTree;
            vm.searchValue = null;
            function searchTree() {
                if (vm.searchValue != null) {
                    $('#vaultFoldersTree').tree('expandAll');
                }
                $('#vaultFoldersTree').tree('doFilter', vm.searchValue);
            }

            function checkAllRolePermissions(role) {
                if (role.allSelected) {
                    role.noneDisabled = true;
                    role.createDisabled = true;
                    role.readDisabled = true;
                    role.deleteDisabled = true;
                }
                else {
                    role.noneDisabled = false;
                    role.createDisabled = false;
                    role.readDisabled = false;
                    role.deleteDisabled = false;
                }

                role.noneSelected = false;
                role.createSelected = false;
                role.readSelected = false;
                role.deleteSelected = false;
            }

            function checkNoneRolePermissions(role) {
                if (role.noneSelected) {
                    role.allDisabled = true;
                    role.createDisabled = true;
                    role.readDisabled = true;
                    role.deleteDisabled = true;
                }
                else {
                    role.allDisabled = false;
                    role.createDisabled = false;
                    role.readDisabled = false;
                    role.deleteDisabled = false;
                }
                role.allSelected = false;
                role.createSelected = false;
                role.readSelected = false;
                role.deleteSelected = false;
            }

            function checkAllPersonPermissions(person) {
                if (person.allSelected) {
                    person.noneDisabled = true;
                    person.createDisabled = true;
                    person.readDisabled = true;
                    person.deleteDisabled = true;
                }
                else {
                    person.noneDisabled = false;
                    person.createDisabled = false;
                    person.readDisabled = false;
                    person.deleteDisabled = false;
                }

                person.noneSelected = false;
                person.createSelected = false;
                person.readSelected = false;
                person.deleteSelected = false;
            }

            function checkNonePersonPermissions(person) {

                if (person.noneSelected) {
                    person.allDisabled = true;
                    person.createDisabled = true;
                    person.readDisabled = true;
                    person.deleteDisabled = true;
                }
                else {
                    person.allDisabled = false;
                    person.createDisabled = false;
                    person.readDisabled = false;
                    person.deleteDisabled = false;
                }

                person.allSelected = false;
                person.createSelected = false;
                person.readSelected = false;
                person.deleteSelected = false;
            }

            function selectPermissions() {
                var objectPermissions = [];

                angular.forEach(vm.roles, function (role) {
                    var objectPermission = {
                        objectType: "FOLDER",
                        objectId: vm.selectedFolder.id,
                        permissionLevel: "ROLE",
                        permissionAssignedTo: role.id,
                        actionTypes: []
                    };
                    if (role.allSelected) {
                        objectPermission.actionTypes.push("ALL");
                    }
                    if (role.noneSelected) {
                        objectPermission.actionTypes.push("NONE");
                    }
                    if (role.createSelected) {
                        objectPermission.actionTypes.push("CREATE");
                    }
                    if (role.readSelected) {
                        objectPermission.actionTypes.push("READ");
                    }
                    if (role.deleteSelected) {
                        objectPermission.actionTypes.push("DELETE");
                    }

                    if (objectPermission.actionTypes.length != 0) {
                        objectPermissions.push(objectPermission);

                    }
                });

                angular.forEach(vm.persons, function (person) {
                    var objectPermission = {
                        objectType: "FOLDER",
                        objectId: vm.selectedFolder.id,
                        permissionLevel: "PERSON",
                        permissionAssignedTo: person.id,
                        actionTypes: []
                    };
                    if (person.noneSelected) {
                        objectPermission.actionTypes.push("NONE");
                    }
                    if (person.createSelected) {
                        objectPermission.actionTypes.push("CREATE");
                    }
                    if (person.readSelected) {
                        objectPermission.actionTypes.push("READ");
                    }
                    if (person.deleteSelected) {
                        objectPermission.actionTypes.push("DELETE");
                    }
                    if (person.allSelected) {
                        objectPermission.actionTypes.push("ALL");
                    }

                    if (objectPermission.actionTypes.length != 0) {
                        objectPermissions.push(objectPermission);

                    }
                });
                VaultsDetailsService.updateRolePermissions($stateParams.vaultId, vm.selectedFolder.id, objectPermissions).then(
                    function (data) {
                        vm.permissions = data;
                        $rootScope.showSuccessMessage("Permissions updated successfully");
                        $timeout(function () {
                            vm.showDropzone = false;
                            vm.permission = false;
                        }, 2000)

                    });
            }

            function loadPermissions() {

                var actionTypes = [];
                VaultsDetailsService.getPermissionsByFolder($stateParams.vaultId, vm.selectedFolder.id).then(
                    function (data) {
                        vm.objectPermissions = data;
                        angular.forEach(vm.objectPermissions, function (objectPermission) {
                            actionTypes = objectPermission.actionTypes;
                            var row = null;
                            if (objectPermission.permissionLevel == 'ROLE') {
                                row = getRole(objectPermission.permissionAssignedTo)
                            }
                            else {
                                row = getPerson(objectPermission.permissionAssignedTo);
                            }

                            angular.forEach(actionTypes, function (actionType) {
                                if (actionType == "ALL") {
                                    row.allSelected = true;
                                }
                                if (actionType == "NONE") {
                                    row.noneSelected = true;
                                }
                                if (actionType == "CREATE") {
                                    row.createSelected = true;
                                }
                                if (actionType == "READ") {
                                    row.readSelected = true;
                                }
                                if (actionType == "DELETE") {
                                    row.deleteSelected = true;
                                }
                            })
                        });

                        if (checkFolderPermission()) {
                            vm.show = true;
                        }
                        else {
                            vm.show = false;
                        }
                        loginPermission();
                        /*var tt=0;
                         angular.forEach(vm.roles, function (role) {
                         checkAllRolePermissions(role);
                         checkNoneRolePermissions(role);
                         });*/
                    });

            }

            function getRole(permissionAssignedTo) {
                var roleObj = null;
                angular.forEach(vm.roles, function (role) {
                    if (role.id == permissionAssignedTo) {
                        roleObj = role;
                    }
                });
                return roleObj;
            }

            function getPerson(permissionAssignedTo) {
                var personObj = null;
                angular.forEach(vm.persons, function (person) {
                    if (person.id == permissionAssignedTo) {
                        personObj = person;
                    }
                });
                return personObj;
            }

            function loadPersons() {
                CommonService.getAllPersons().then(
                    function (data) {
                        vm.persons = data;
                        CommonService.getPersonReferences(vm.persons, 'id');
                    }
                )
            }

            function loadRoles() {
                RoleService.getRolesAll().then(
                    function (data) {
                        vm.roles = data;

                        /* angular.forEach(vm.roles, function (role) {

                         //   checkAllRolePermissions(role);
                         //  checkNoneRolePermissions(role);

                         });
                         */
                    });
            }


            function loginPermission() {
                vm.permissionActions = [];
                vm.rolePermissionActions = [];
                vm.personPermissionActions = [];
                vm.loginPerson = window.$application.login.person;
                var personRoles = [];
                angular.forEach(vm.objectPermissions, function (objectPermission) {
                    if (objectPermission.permissionLevel == "PERSON" && objectPermission.permissionAssignedTo == vm.loginPerson.id) {
                        angular.forEach(objectPermission.actionTypes, function (actionType) {
                            if (checkUserPermission(actionType, "PERSON")) {
                                vm.show = true;
                            }
                            else {
                                vm.show = false;
                            }

                        })
                    }

                    RoleService.getPersonRoles(window.$application.login.person.id).then(
                        function (data) {
                            personRoles = data;
                            angular.forEach(personRoles, function (personRole) {
                                if (objectPermission.permissionLevel == "ROLE" && objectPermission.permissionAssignedTo == personRole.roleId) {
                                    angular.forEach(objectPermission.actionTypes, function (actionType) {
                                        if (checkUserPermission(actionType, "ROLE")) {
                                            vm.show = true;
                                        }
                                        else {
                                            vm.show = false;
                                        }

                                    });
                                }
                            })
                        }
                    )
                });


            }

            function checkUserPermission(actionType, permissionLevel) {
                var exists = false;

                if (permissionLevel == "ROLE") {

                    vm.rolePermissionActions.push(actionType);
                } else if (permissionLevel == "PERSON") {

                    vm.personPermissionActions.push(actionType);
                }


                if (actionType == "ALL" || actionType == "NONE" || actionType == "READ" || actionType == "CREATE" || actionType == "DELETE") {
                    exists = true;
                }

                return exists;
            }

            function checkFolderPermission() {
                var exists = false;
                if (vm.selectedFolder != null) {
                    if (vm.selectedFolder.createdBy != null && vm.selectedFolder.createdBy == window.$application.login.person.id) {
                        exists = true;
                    }
                }
                return exists;
            }


            function checkFileViewPermissions() {
                var exists = true;
                if (vm.selectedFolder != null) {
                    if (vm.selectedFolder.createdBy != null && vm.selectedFolder.createdBy == window.$application.login.person.id) {
                        exists = false;
                    }

                    if (exists == false) {

                        return exists;
                    } else if (vm.getPermissionActions() != undefined && vm.getPermissionActions() != null && vm.getPermissionActions().length > 0) {


                        angular.forEach(vm.getPermissionActions(), function (permissionAction) {

                            if (permissionAction == "ALL" || permissionAction == "READ") {

                                exists = false;
                                return exists;

                            }

                        });

                    }
                }
                return exists;


            }

            function checkAddFilesPermissions() {

                var exists = true;
                if (vm.selectedFolder != null) {
                    if (vm.selectedFolder.createdBy != null && vm.selectedFolder.createdBy == window.$application.login.person.id) {
                        exists = false;
                    }

                    if (exists == false) {

                        return exists;
                    } else if (vm.getPermissionActions() != undefined && vm.getPermissionActions() != null && vm.getPermissionActions().length > 0) {


                        angular.forEach(vm.getPermissionActions(), function (permissionAction) {

                            if (permissionAction == "ALL" || permissionAction == "CREATE") {

                                exists = false;
                                return exists;

                            }

                        });

                    }
                }
                return exists;


            }

            function checkDeleteFilesPermissions() {

                var exists = true;
                if (vm.selectedFolder != null) {
                    if (vm.selectedFolder.createdBy != null && vm.selectedFolder.createdBy == window.$application.login.person.id) {
                        exists = false;
                    }

                    if (exists == false) {

                        return exists;
                    } else if (vm.getPermissionActions() != undefined && vm.getPermissionActions() != null && vm.getPermissionActions().length > 0) {


                        angular.forEach(vm.getPermissionActions(), function (permissionAction) {

                            if (permissionAction == "ALL" || permissionAction == "DELETE") {

                                exists = false;
                                return exists;

                            }

                        });
                    }
                }

                return exists;


            }

            function checkPermissionChanges() {

                var exists = true;
                if (vm.selectedFolder != null) {
                    if (vm.selectedFolder.createdBy != null && vm.selectedFolder.createdBy == window.$application.login.person.id) {
                        exists = false;
                    }

                    if (exists == false) {

                        return exists;
                    } else if (vm.getPermissionActions() != undefined && vm.getPermissionActions() != null && vm.getPermissionActions().length > 0) {


                        angular.forEach(vm.getPermissionActions(), function (permissionAction) {

                            if (permissionAction == "ALL") {

                                exists = false;
                                return exists;

                            }

                        });

                    }
                }
                return exists;

            }

            function getPermissionActions() {

                vm.permissionActions = [];
                if (vm.personPermissionActions != undefined && vm.personPermissionActions != null && vm.personPermissionActions.length > 0) {

                    angular.forEach(vm.personPermissionActions, function (permissionAction) {
                        vm.permissionActions.push(permissionAction);


                    });

                } else if (vm.rolePermissionActions != undefined && vm.rolePermissionActions != null && vm.rolePermissionActions.length > 0) {
                    angular.forEach(vm.rolePermissionActions, function (permissionAction) {
                        vm.permissionActions.push(permissionAction);


                    });

                }

                return vm.permissionActions;
            }

            function addPermission() {
                vm.permission = true;

                angular.forEach(vm.roles, function (role) {
                    if (role.allSelected) {
                        role.noneDisabled = true;
                        role.createDisabled = true;
                        role.readDisabled = true;
                        role.deleteDisabled = true;

                        role.noneSelected = false;
                        role.createSelected = false;
                        role.readSelected = false;
                        role.deleteSelected = false;
                    }
                    else if (role.noneSelected) {
                        role.allDisabled = true;
                        role.createDisabled = true;
                        role.readDisabled = true;
                        role.deleteDisabled = true;

                        role.allSelected = false;
                        role.createSelected = false;
                        role.readSelected = false;
                        role.deleteSelected = false;
                    }
                });

                angular.forEach(vm.persons, function (person) {
                    if (person.allSelected) {

                        person.noneDisabled = true;
                        person.createDisabled = true;
                        person.readDisabled = true;
                        person.deleteDisabled = true;

                        person.noneSelected = false;
                        person.createSelected = false;
                        person.readSelected = false;
                        person.deleteSelected = false;

                    } else if (person.noneSelected) {

                        person.allDisabled = true;
                        person.createDisabled = true;
                        person.readDisabled = true;
                        person.deleteDisabled = true;

                        person.allSelected = false;
                        person.createSelected = false;
                        person.readSelected = false;
                        person.deleteSelected = false;

                    }

                    if (vm.selectedFolder.createdBy == person.id) {

                        person.noneDisabled = true;
                        person.createDisabled = true;
                        person.readDisabled = true;
                        person.deleteDisabled = true;
                        person.allDisabled = true;

                        person.noneSelected = false;
                        person.createSelected = false;
                        person.readSelected = false;
                        person.deleteSelected = false;
                        person.allSelected = true;

                    }
                });

            }


            function showFileHistory(fileId) {
                var options = {
                    title: 'File History',
                    template: 'app/desktop/modules/vaults/details/fileHistoryView.jsp',
                    controller: 'FileHistoryController as fileHistoryVm',
                    resolve: 'app/desktop/modules/vaults/details/fileHistoryController',
                    data: {
                        folderId: vm.selectedFolderId,
                        fileId: fileId
                    },
                    callback: function (msg) {
                        console.log(msg);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function initFoldersTree() {

                foldersTree = $('#vaultFoldersTree').tree({
                    data: [
                        {
                            id: nodeId,
                            /*text: 'Project Folders',*/
                            text: $stateParams.vaultName + ' Vault',
                            iconCls: 'vault-folders-root',
                            attributes: {
                                folder: null,
                                root: true
                            },
                            children: []
                        }
                    ],
                    onContextMenu: onContextMenu,
                    onDblClick: onDblClick,
                    onAfterEdit: onAfterEdit,
                    onSelect: onSelectFolder
                });

                rootNode = foldersTree.tree('find', 0);
                $(document).click(function () {
                    $("#contextMenu").hide();
                });
            }

            vm.selectedFolderId = null;

            function onSelectFolder(node) {
                if (node.attributes.folder != null) {
                    vm.selectedFolderId = node.attributes.folder.id;
                }
                vm.files = [];
                vm.showDropzone = false;
                var data = foldersTree.tree('getData', node.target);

                if (data != null && data.attributes.folder != null) {
                    vm.selectedFolder = data.attributes.folder;
                    fileDropZone.options.url = "api/pdm/vaults/" + $stateParams.vaultId + "/folders/" + vm.selectedFolder.id + "/files";

                    fileDropZone.removeAllFiles();

                    angular.forEach(vm.roles, function (role) {
                        role.allSelected = role.noneSelected = role.readSelected = role.createSelected = role.deleteSelected = false;
                    });
                    angular.forEach(vm.persons, function (person) {
                        person.allSelected = person.noneSelected = person.readSelected = person.createSelected = person.deleteSelected = false;
                    });

                    loadFiles();

                    $timeout(function () {
                        loadPermissions();
                        //
                    }, 100);

                }
                else if (data == null) {
                    vm.selectedFolder = null;
                }
            }

            function loadFiles() {
                vm.loading = false;
                VaultsDetailsService.getFiles($stateParams.vaultId, vm.selectedFolderId).then(
                    function (files) {


                        var personIds = [];
                        angular.forEach(files, function (file) {
                            if (file.createdBy != null && personIds.indexOf(file.createdBy) == -1) {
                                personIds.push(file.createdBy);
                            }

                            if (file.modifiedBy != null && personIds.indexOf(file.modifiedBy) == -1) {
                                personIds.push(file.modifiedBy);
                            }

                            if (file.lockedBy != null && personIds.indexOf(file.lockedBy) == -1) {
                                personIds.push(file.lockedBy);
                            }
                        });

                        if (personIds.length > 0) {
                            CommonService.getPersons(personIds).then(
                                function (persons) {
                                    var map = new Hashtable();
                                    angular.forEach(persons, function (person) {
                                        map.put(person.id, person);
                                    });

                                    angular.forEach(files, function (file) {
                                        if (file.createdBy != null) {
                                            person = map.get(file.createdBy);
                                            if (person != null) {
                                                file.createdByPerson = person;
                                            }
                                            else {
                                                file.createdByPerson = {firstName: ""};
                                            }
                                        }

                                        if (file.modifiedBy != null) {
                                            person = map.get(file.modifiedBy);
                                            if (person != null) {
                                                file.modifiedByPerson = person;
                                            }
                                            else {
                                                file.modifiedByPerson = {firstName: ""};
                                            }
                                        }

                                        if (file.lockedBy != null) {
                                            person = map.get(file.lockedBy);
                                            if (person != null) {
                                                file.lockedByPerson = person;
                                            }
                                            else {
                                                file.lockedByPerson = {firstName: ""};
                                            }
                                        }
                                    });

                                    vm.files = files;
                                    vm.filesSerach = true;
                                    vm.loading = false;
                                }
                            );
                        }
                        else {
                            vm.files = files;
                            vm.loading = false;
                        }
                        //$scope.$apply();


                        /*   CommonService.getPersonReferences(vm.files, 'createdBy');
                         CommonService.getPersonReferences(vm.files, 'modifiedBy');
                         CommonService.getPersonReferences(vm.files, 'lockedBy');*/
                    }
                );

            }

            var pageable = {
                page: 0,
                size: 1000,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            function resetPage() {
                loadFiles();
            }

            var freeTextQuery = null;
            var searchMode = null;

            function freeTextSearch(freeText) {
                freeTextQuery = freeText;
                searchMode = "freetext";
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    VaultsDetailsService.freeTextSearch(freeText, vm.selectedFolderId, pageable).then(
                        function (data) {
                            vm.files = data.content;
                            var personIds = [];
                            CommonService.getPersonReferences(vm.files, 'createdBy');
                            CommonService.getPersonReferences(vm.files, 'modifiedBy');
                            /* vm.clear = true;*/
                            angular.forEach(vm.files, function (file) {
                                if (file.createdBy != null && personIds.indexOf(file.createdBy) == -1) {
                                    personIds.push(file.createdBy);
                                }

                                if (file.modifiedBy != null && personIds.indexOf(file.modifiedBy) == -1) {
                                    personIds.push(file.modifiedBy);
                                }

                                if (file.lockedBy != null && personIds.indexOf(file.lockedBy) == -1) {
                                    personIds.push(file.lockedBy);
                                }
                            });

                            if (personIds.length > 0) {
                                CommonService.getPersons(personIds).then(
                                    function (persons) {
                                        var map = new Hashtable();
                                        angular.forEach(persons, function (person) {
                                            map.put(person.id, person);
                                        });

                                        angular.forEach(vm.files, function (file) {
                                            if (file.createdBy != null) {
                                                person = map.get(file.createdBy);
                                                if (person != null) {
                                                    file.createdByPerson = person;
                                                }
                                                else {
                                                    file.createdByPerson = {firstName: ""};
                                                }
                                            }

                                            if (file.modifiedBy != null) {
                                                person = map.get(file.modifiedBy);
                                                if (person != null) {
                                                    file.modifiedByPerson = person;
                                                }
                                                else {
                                                    file.modifiedByPerson = {firstName: ""};
                                                }
                                            }

                                            if (file.lockedBy != null) {
                                                person = map.get(file.lockedBy);
                                                if (person != null) {
                                                    file.lockedByPerson = person;
                                                }
                                                else {
                                                    file.lockedByPerson = {firstName: ""};
                                                }
                                            }
                                        });
                                    }
                                )
                            }
                        });
                } else {
                    resetPage();
                }
            }


            function onContextMenu(e, node) {
                e.preventDefault();
                var $contextMenu = $("#contextMenu");
                var parent = foldersTree.tree('getData', node.target);
                onSelectFolder(node);

                var permissionsActions = getPermissionActions();

                $('#addFolder').show();
                $('#deleteFolder').show();
                $contextMenu.show();
                if (node.attributes.root == true && node.attributes.folder == null) {
                    $('#addFolder').text('Add Folder');
                    $('#deleteFolder').hide();
                }
                if (node.attributes.root == undefined && parent.id === 0) {
                    $contextMenu.hide();
                }
                $contextMenu.css({
                    left: e.pageX,
                    top: e.pageY
                });

                $contextMenu.on("click", "a", function () {
                    $contextMenu.hide();
                });
            }

            function onDblClick(node) {
                var data = foldersTree.tree('getData', node.target);
                if (data.id != 0) {
                    foldersTree.tree('beginEdit', node.target);
                }

                $timeout(function () {
                    $('.tree-editor').focus().select();
                }, 1);
            }

            function onAfterEdit(node) {
                var data = foldersTree.tree('getData', node.target);
                if (data.attributes.folder == null) {
                    data.attributes.folder = {}
                }

                var parent = foldersTree.tree('getParent', node.target);
                var parentData = foldersTree.tree('getData', parent.target);

                if (parentData.id != 0) {
                    data.attributes.folder.parent = parentData.attributes.folder.id;
                }

                data.attributes.folder.name = node.text;
                data.attributes.folder.folderType = vm.folderType;
                data.attributes.folder.project = parseInt($stateParams.vaultId);
                data.attributes.folder.isLocked = false;


                var promise = null;
                var children = data.attributes.folder.children;
                data.attributes.folder.children = [];
                if (data.attributes.folder.id == undefined || data.attributes.folder.id == null) {
                    promise = VaultsDetailsService.createFolder($stateParams.vaultId, data.attributes.folder);
                }
                else {
                    promise = VaultsDetailsService.updateFolder($stateParams.vaultId, data.attributes.folder);
                }
                promise.then(
                    function (folder) {
                        folder.children = children;
                        data.attributes.folder = angular.copy(folder);
                        foldersTree.tree('update', {target: node.target, attributes: data.attributes});
                    }
                );
            }

            function addFiles() {
                vm.showDropzone = true;
                fileDropZone.removeAllFiles();

            }

            vm.selectFile = selectFile;
            function selectFile() {
                //vm.showDropzone = true;
                $('#itemFiles')[0].click();
            }

            function backToFiles() {
                vm.showDropzone = false;
            }

            function addFolder() {
                var selectedNode = foldersTree.tree('getSelected');

                if (selectedNode != null) {
                    var nodeid = ++nodeId;

                    foldersTree.tree('append', {
                        parent: selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: 'vault-folder',
                                text: 'New Folder',
                                attributes: {
                                    folder: null
                                }
                            }
                        ]
                    });

                    var newNode = foldersTree.tree('find', nodeid);
                    if (newNode != null) {
                        //foldersTree.tree('select', newNode.target);
                        foldersTree.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                }
            }

            /* function deleteFolder() {
             var selectedNode = foldersTree.tree('getSelected');
             if (selectedNode != null) {
             var data = foldersTree.tree('getData', selectedNode.target);
             if (data != null && data.attributes.folder != null) {
             VaultsDetailsService.deleteFolder($stateParams.vaultId, data.attributes.folder.id).then(
             function (data) {
             foldersTree.tree('remove', selectedNode.target);
             }
             )
             }
             }
             }*/
            function isExistsInFiles(folderId) {
                var defered = $q.defer();
                VaultsDetailsService.getFilesByFolder(folderId).then(
                    function (data) {
                        vm.folders = data
                        if (vm.folders.length != 0) {
                            defered.resolve(true);
                        } else {
                            defered.reject(false);
                        }
                    });
                return defered.promise;
            }

            function deleteFolder() {
                /*var options = {
                 title: 'Delete Type',
                 message: 'Are you sure you want to delete this folder?',
                 okButtonClass: 'btn-danger'
                 };
                 DialogService.confirm(options, function (yes) {
                 if (yes == true) {
                 var selectedNode = foldersTree.tree('getSelected');
                 if (selectedNode != null) {
                 var data = foldersTree.tree('getData', selectedNode.target);
                 if (data != null && data.attributes.folder != null) {
                 VaultsDetailsService.deleteFolder($stateParams.vaultId, data.attributes.folder.id).then(
                 function (data) {
                 if(folder.fileId){

                 }
                 foldersTree.tree('remove', selectedNode.target);
                 }
                 )
                 }
                 }
                 }
                 })*/
                var selectedNode = foldersTree.tree('getSelected');
                if (selectedNode != null) {
                    var data = foldersTree.tree('getData', selectedNode.target);
                    if (data != null && data.attributes.folder != null) {
                        isExistsInFiles(data.attributes.folder.id).then(
                            function (success) {
                                $rootScope.showErrorMessage("This folder is assigned to file(s) we can't delete");
                            },
                            function (failure) {
                                var options = {
                                    title: 'Delete folder',
                                    message: 'Are you sure you want to delete this folder?',
                                    okButtonClass: 'btn-danger'
                                };
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        VaultsDetailsService.deleteFolder($stateParams.vaultId, data.attributes.folder.id).then(
                                            function (data) {
                                                foldersTree.tree('remove', selectedNode.target);
                                            });
                                    }
                                });
                            });
                    }
                }
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

            function loadRootFolders() {
                VaultsDetailsService.getVaultRootFolders($stateParams.vaultId).then(
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

                        foldersTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }
                )
            }

            function makeNode(folder) {
                return {
                    id: ++nodeId,
                    text: folder.name,
                    iconCls: 'vault-folder',
                    attributes: {
                        folder: folder
                    }
                };
            }

            function showNewCommit() {
                var options = {
                    title: 'Commits',
                    template: 'app/desktop/modules/vaults/new/newCommitView.jsp',
                    controller: 'NewCommitController as newCommitVm',
                    resolve: 'app/desktop/modules/vaults/new/newCommitController',
                    width: 600,
                    showMask: true,
                    data: {},
                    buttons: [
                        {text: 'Create', broadcast: 'app.commit.new'}
                    ],
                    callback: function () {
                        $rootScope.hideSidePanel();
                    }
                };

                $rootScope.showSidePanel(options);

            }


            vm.commit = {
                id: null,
                comments: null,
                sha: null
            };


            function initFilesTableDropzone() {
                var previewNode = $("#template");
                var previewTemplate = previewNode.parent().html();
                previewNode.remove();
                var myDropzone = new Dropzone(document.querySelector("#itemFiles"), { // Make the whole body a dropzone
                    url: "api/pdm/vaults/folders/" + vm.selectedFolderId + "/files/?commit=" + vm.commit.id, // Set the url
                    thumbnailWidth: 80,
                    thumbnailHeight: 80,
                    parallelUploads: 20,
                    previewTemplate: previewTemplate,
                    autoQueue: false, // Make sure the files aren't queued until manually added
                    previewsContainer: "#previews" // Define the container to display the previews
                });

                myDropzone.on("queuecomplete", function (progress) {
                    $("#itemFilesTableContainer").removeClass('drag-over');
                    vm.showFileDropzone = false;
                    myDropzone.removeAllFiles(true);
                    $scope.$apply();
                    $rootScope.showSuccessMessage("file saved SuccessMessage");
                    loadFiles();
                });

                myDropzone.on("addedfiles", function (files) {
                    var options = {
                        title: 'Commits',
                        template: 'app/desktop/modules/vaults/new/newCommitView.jsp',
                        controller: 'NewCommitController as newCommitVm',
                        resolve: 'app/desktop/modules/vaults/new/newCommitController',
                        width: 600,
                        showMask: true,
                        data: {
                            selectedCommitFiles: myDropzone.files
                        },
                        buttons: [
                            {text: 'Create', broadcast: 'app.commit.new'}
                        ],
                        callback: function (data) {
                            $rootScope.hideSidePanel();
                            vm.commit = data;
                            $rootScope.showBusyIndicator();
                            VaultService.createFolderFiles(vm.selectedFolderId, vm.commit.id, myDropzone.files).then(
                                function (data) {
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage("Files Added successfully");
                                    loadFiles();
                                }
                            );
                            //this.classList.remove('drag-over');
                        }
                    };

                    $rootScope.showSidePanel(options);


                    // Hookup the start button
                    /*  myDropzone.enqueueFile(file);*/
                    /*files.previewElement.querySelector(".start").onclick = function () {
                     myDropzone.enqueueFile(file);

                     };*/

                    $scope.showDropArea = false;

                    document.querySelector("#filesDropZone").style.display = "none";
                    document.querySelector("#primaryBtn").style.display = "inline";
                    document.querySelector("#warningBtn").style.display = "inline";

                });

                $("#itemFilesTableContainer").on('dragover', handleDragEnter);
                $("#itemFilesTableContainer").on('dragleave', handleDragLeave);
                $("#itemFilesTableContainer").on('drop', handleDragLeave);

                fileDropZone = myDropzone;
            }


            function checkAndStartFileUpload() {
                var lockedFiles = [];
                for (var i = 0; i < fileDropZone.files.length; i++) {
                    if (isFileLocked(fileDropZone.files[i].name)) {
                        lockedFiles.push(fileDropZone.files[i].name);
                    }
                }

                if (lockedFiles.length == 0) {
                    fileDropZone.enqueueFiles(fileDropZone.getFilesWithStatus(Dropzone.ADDED));
                }
                else {
                    var fileNames = "";

                    for (i = 0; i < lockedFiles.length; i++) {
                        fileNames += lockedFiles[i];

                        if (i != lockedFiles.length - 1) {
                            fileNames += ", ";
                        }
                    }

                    $rootScope.showErrorMessage("Following files are locked and cannot be updated: " + fileNames);
                }
            }

            function isFileLocked(fileName) {
                var locked = false;

                angular.forEach(vm.files, function (file) {
                    if (file.name == fileName && file.locked == true) {
                        locked = true;
                    }
                });
                return locked;
            }


            function handleDragEnter(e) {
                vm.showFileDropzone = true;
                this.classList.add('drag-over');
            }

            function handleDragLeave(e) {
                vm.showFileDropzone = false;
                var options = {
                    title: 'Commits',
                    template: 'app/desktop/modules/vaults/new/newCommitView.jsp',
                    controller: 'NewCommitController as newCommitVm',
                    resolve: 'app/desktop/modules/vaults/new/newCommitController',
                    width: 600,
                    showMask: true,
                    data: {},
                    buttons: [
                        {text: 'Create', broadcast: 'app.commit.new'}
                    ],
                    callback: function () {
                        $rootScope.hideSidePanel();

                        this.classList.remove('drag-over');
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function fileSizeToString(bytes) {
                if (bytes == 0) {
                    return "0.00 B";
                }
                var e = Math.floor(Math.log(bytes) / Math.log(1024));
                return (bytes / Math.pow(1024, e)).toFixed(2) + ' ' + ' KMGTP'.charAt(e) + 'B';
            }

            function downloadFile(vault) {
                var url = "{0}//{1}/api/pdm/vaults/folders/{2}/files/{3}/download".
                    format(window.location.protocol, window.location.host,
                    vault.folder, vault.id);
                launchUrl(url);
            }

            function deleteFile(file) {

                VaultsDetailsService.deleteFile(vm.selectedFolderId, file).then(
                    function (data) {

                        var index = vm.files.indexOf(file);
                        vm.files.splice(index, 1);
                        // file = data;
                        $rootScope.showSuccessMessage('File deleted successfully.')
                    }
                );

            }

            function lockFile(file, lock) {
                file.locked = lock;
                if (lock) {
                    file.lockedBy = $application.login.person.id;
                    file.lockedByObject = $application.login.person;
                    file.lockedDate = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                }
                else {
                    file.lockedBy = null;
                    file.lockedByObject = null;
                    file.lockedDate = null;
                }
                VaultsDetailsService.updateFile(vm.selectedFolderId, file).then(
                    function (data) {
                        file = data;
                        $rootScope.showSuccessMessage('File updated successfully.')
                    }
                );

                if (file.lockedBy != null) {
                    file.lockedByPerson = $application.login.person;

                } else {
                    file.lockedByPerson = null;

                }
            }

            function getTabById(id) {
                var found = null;
                angular.forEach(vm.navItems, function (item) {
                    if (item.id == id) {
                        found = item;
                    }
                });

                return found;
            }


            function loadVault() {
                vm.clear = false;
                vm.loading = true;
                VaultService.getVault($stateParams.vaultId).then(
                    function (data) {
                        vm.vaultObj = data;
                        $rootScope.viewInfo.title = vm.vaultObj.name;
                        $rootScope.viewInfo.description = vm.vaultObj.description;
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.showComments('VAULT', vm.vaultId);
                    $scope.$on('$viewContentLoaded', function () {
                        $('div.split-pane').splitPane();
                        vm.permissionActions = [];
                        vm.rolePermissionActions = [];
                        vm.personPermissionActions = [];


                        /*if (fileDropZone == null) {
                         initFilesTableDropzone();
                         }
                         else {
                         fileDropZone.removeAllFiles();
                         }*/


                        loadVault();
                        initFoldersTree();
                        loadRootFolders();
                        //initFilesTableDropzone();
                        loadPersons();
                        loadRoles();
                        $timeout(function () {
                            initFilesTableDropzone();
                        }, 1000);
                    });

                }
            })();
        }
    }
)
;

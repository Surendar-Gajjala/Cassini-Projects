define(['app/desktop/modules/dm/dm.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/shared/services/pm/project/documentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'split-pane',
        'jquery.easyui',
        'dropzone',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/pm/project/documentService',
        'app/desktop/modules/shared/comments/commentsButtonDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/autodeskForge/showCADFileDirective'
    ],
    function (module) {
        module.controller('DocumentsController', DocumentsController);

        function DocumentsController($scope, $rootScope, $timeout, $window, $state, $uibModal, $stateParams, $cookies,
                                     DocumentService, CommonService, ProjectService, DialogService) {
            $rootScope.viewInfo.icon = "fa fa-files-o";
            $rootScope.viewInfo.title = "Documents";

            var vm = this;
            vm.loginPerson = window.$application.login.person;

            vm.loading = true;
            vm.showDropzone = false;
            vm.showFileDropzone = false;
            vm.permission = false;
            vm.show = false;
            vm.selectedFolder = null;
            vm.folderType = null;
            vm.person = null;
            vm.projectRole = null;
            var lastSelectedItem = null;

            vm.docType = $stateParams.type;
            vm.documents = [];
            vm.projectPersons = [];
            vm.ProjectRoles = [];
            vm.objectPermissions = [];
            vm.permissions = [];

            vm.addFolder = addFolder;
            vm.deleteFolder = deleteFolder;
            vm.addFiles = addFiles;
            vm.backToFiles = backToFiles;
            vm.back = back;
            vm.backTo = backTo;
            vm.fileSizeToString = fileSizeToString;
            vm.downloadDocument = downloadDocument;
            vm.lockDocument = lockDocument;
            vm.addPermission = addPermission;
            vm.selectPermissions = selectPermissions;
            vm.checkAllRolePermissions = checkAllRolePermissions;
            vm.checkRolePermissions = checkRolePermissions;
            vm.deleteDocument = deleteDocument;
            vm.hasPermission = hasPermission;
            vm.selectFiles = selectFiles;
            vm.showFileHistory = showFileHistory;
            vm.hasError = false;
            vm.message = "";
            vm.closeNotification = closeNotification;
            vm.showComments = showComments;
            vm.showFiles = true;

            var nodeId = 0;
            var rootNode = null;
            var foldersTree = null;
            var fileDropZone = null;

            function backTo() {
                vm.showDropzone = false;
                vm.permission = false;
            }

            function back() {
                $window.history.back();
            }

            $scope.showAutoDeskFile = function (file) {
                $scope.conCallBack(file);
            };

            $scope.registerCallBack = function (callback) {
                $scope.conCallBack = callback;
            };

            function showFileHistory(fileId) {
                var options = {
                    title: 'File History',
                    template: 'app/desktop/modules/dm/documents/projectFileHistoryView.jsp',
                    controller: 'ProjectFileHistoryController as fileHistoryVm',
                    resolve: 'app/desktop/modules/dm/documents/projectFileHistoryController',
                    data: {
                        documentId: fileId,
                        folderId: vm.selectedFolder.id
                    },
                    callback: function (msg) {
                        console.log(msg);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function checkAllRolePermissions(projectRole) {
                if (projectRole.allSelected) {
                    projectRole.noneDisabled = true;
                    projectRole.createSelected = true;
                    projectRole.readSelected = true;
                    projectRole.writeSelected = true;
                    projectRole.deleteSelected = true;
                }
                else {
                    projectRole.noneDisabled = false;
                    projectRole.createSelected = false;
                    projectRole.readSelected = false;
                    projectRole.writeSelected = false;
                    projectRole.deleteSelected = false;
                }
                if (projectRole.noneSelected) {
                    projectRole.noneDisabled = false;
                    projectRole.createDisabled = true;
                    projectRole.readDisabled = true;
                    projectRole.writeDisabled = true;
                    projectRole.deleteDisabled = true;
                    projectRole.allDisabled = true;
                    projectRole.createSelected = false;
                    projectRole.readSelected = false;
                    projectRole.writeSelected = false;
                    projectRole.deleteSelected = false;
                    projectRole.allSelected = false;
                }
                if (!projectRole.noneSelected) {
                    projectRole.createDisabled = false;
                    projectRole.readDisabled = false;
                    projectRole.writeDisabled = false;
                    projectRole.deleteDisabled = false;
                    projectRole.allDisabled = false;
                }
            }

            function checkRolePermissions(projectRole) {
                if (projectRole.createSelected && projectRole.deleteSelected && projectRole.readSelected && projectRole.writeSelected) {
                    projectRole.allSelected = true;
                    projectRole.noneDisabled = true;
                }
                else if (projectRole.createSelected || projectRole.deleteSelected || projectRole.readSelected || projectRole.writeSelected) {
                    projectRole.noneDisabled = true;
                    projectRole.allSelected = false;
                }
                else if (!(projectRole.createSelected && projectRole.deleteSelected && projectRole.readSelected && projectRole.writeSelected)) {
                    projectRole.noneDisabled = false;
                }
            }

            function selectPermissions() {
                var objectPermissions = [];

                angular.forEach(vm.projectRoles, function (projectRole) {
                    var objectPermission = {
                        objectType: "FOLDER",
                        objectId: vm.selectedFolder.id,
                        permissionLevel: "ROLE",
                        permissionAssignedTo: projectRole.id,
                        actionTypes: []
                    };
                    if (projectRole.allSelected) {
                        objectPermission.actionTypes.push("ALL");
                    }
                    if (projectRole.noneSelected) {
                        objectPermission.actionTypes.push("NONE");
                    }
                    if (projectRole.createSelected) {
                        objectPermission.actionTypes.push("CREATE");
                    }
                    if (projectRole.readSelected) {
                        objectPermission.actionTypes.push("READ");
                    }
                    if (projectRole.writeSelected) {
                        objectPermission.actionTypes.push("WRITE");
                    }
                    if (projectRole.deleteSelected) {
                        objectPermission.actionTypes.push("DELETE");
                    }

                    if (objectPermission.actionTypes.length != 0) {
                        objectPermissions.push(objectPermission);

                    }
                });

                angular.forEach(vm.projectPersons, function (projectPerson) {
                    var objectPermission = {
                        objectType: "FOLDER",
                        objectId: vm.selectedFolder.id,
                        permissionLevel: "PERSON",
                        permissionAssignedTo: projectPerson.person,
                        actionTypes: []
                    };
                    if (projectPerson.noneSelected) {
                        objectPermission.actionTypes.push("NONE");
                    }
                    if (projectPerson.createSelected) {
                        objectPermission.actionTypes.push("CREATE");
                    }
                    if (projectPerson.readSelected) {
                        objectPermission.actionTypes.push("READ");
                    }
                    if (projectPerson.writeSelected) {
                        objectPermission.actionTypes.push("WRITE");
                    }
                    if (projectPerson.deleteSelected) {
                        objectPermission.actionTypes.push("DELETE");
                    }
                    if (projectPerson.allSelected) {
                        objectPermission.actionTypes.push("ALL");
                    }

                    if (objectPermission.actionTypes.length != 0) {
                        objectPermissions.push(objectPermission);

                    }
                });
                DocumentService.updateRolePermissions($stateParams.projectId, vm.selectedFolder.id, objectPermissions).then(
                    function (data) {
                        vm.permissions = data;
                        $rootScope.showSuccessMessage("Permissions updated successfully");

                    });
            }

            function loadPermissions() {
                var actionTypes = [];
                DocumentService.getPermissionsByFolder($stateParams.projectId, vm.selectedFolder.id).then(
                    function (data) {
                        vm.objectPermissions = data;
                        angular.forEach(vm.objectPermissions, function (objectPermission) {
                            actionTypes = objectPermission.actionTypes;
                            var row = null;
                            if (objectPermission.permissionLevel == 'ROLE') {
                                getProjectRole(objectPermission.permissionAssignedTo, actionTypes)
                            }
                            else {
                                getProjectPerson(objectPermission.permissionAssignedTo, actionTypes);
                            }
                        });

                        if (checkFolderPermission()) {
                            vm.show = true;
                        }
                        else {
                            vm.show = false;
                        }
                        loginPermission();
                    },
                    function (error) {
                        vm.loading = false;
                        //$rootScope.showErrorMessage(error.message);
                    });

            }

            function getProjectRole(permissionAssignedTo, actionTypes) {
                var role = null;
                angular.forEach(vm.projectRoles, function (projectRole) {
                    if (projectRole.id == permissionAssignedTo) {

                        angular.forEach(actionTypes, function (actionType) {
                            if (actionType == "ALL") {
                                projectRole.allSelected = true;
                                projectRole.noneDisabled = true;
                                projectRole.createSelected = true;
                                projectRole.readSelected = true;
                                projectRole.writeSelected = true;
                                projectRole.deleteSelected = true;
                                projectRole.allDisabled = false;
                            }
                            if (actionType == "NONE") {
                                projectRole.noneSelected = true;
                                projectRole.readDisabled = true;
                                projectRole.writeDisabled = true;
                                projectRole.deleteDisabled = true;
                                projectRole.createDisabled = true;
                                projectRole.allDisabled = true;
                                projectRole.allSelected = false;
                                projectRole.readSelected = false;
                                projectRole.writeSelected = false;
                                projectRole.deleteSelected = false;
                                projectRole.createSelected = false;
                            }
                            if (actionType == "CREATE") {
                                projectRole.createSelected = true;
                                projectRole.noneDisabled = true;
                                projectRole.allDisabled = false;
                            }
                            if (actionType == "READ") {
                                projectRole.readSelected = true;
                                projectRole.noneDisabled = true;
                                projectRole.allDisabled = false;
                            }
                            if (actionType == "WRITE") {
                                projectRole.writeSelected = true;
                                projectRole.noneDisabled = true;
                                projectRole.allDisabled = false;
                            }
                            if (actionType == "DELETE") {
                                projectRole.deleteSelected = true;
                                projectRole.noneDisabled = true;
                                projectRole.allDisabled = false;
                            }
                        })
                    }
                });
                return role;
            }

            function getProjectPerson(permissionAssignedTo, actionTypes) {
                angular.forEach(vm.projectPersons, function (projectPerson) {
                    if (projectPerson.person == permissionAssignedTo) {

                        angular.forEach(actionTypes, function (actionType) {
                            if (actionType == "ALL") {
                                projectPerson.allSelected = true;
                                projectPerson.noneDisabled = true;
                                projectPerson.createDisabled = true;
                                projectPerson.readDisabled = true;
                                projectPerson.writeDisabled = true;
                                projectPerson.deleteDisabled = true;
                                projectPerson.allDisabled = false;
                            }
                            else if (actionType == "NONE") {
                                projectPerson.noneSelected = true;
                                projectPerson.readDisabled = true;
                                projectPerson.writeDisabled = true;
                                projectPerson.deleteDisabled = true;
                                projectPerson.createDisabled = true;
                                projectPerson.allDisabled = true;
                                projectPerson.allSelected = false;
                                projectPerson.readSelected = false;
                                projectPerson.writeSelected = false;
                                projectPerson.deleteSelected = false;
                                projectPerson.createSelected = false;
                            }
                            if (actionType == "CREATE") {
                                projectPerson.createSelected = true;
                                projectPerson.noneDisabled = true;
                            }
                            if (actionType == "READ") {
                                projectPerson.readSelected = true;
                                projectPerson.noneDisabled = true;
                                projectPerson.allDisabled = false;
                            }
                            if (actionType == "WRITE") {
                                projectPerson.writeSelected = true;
                                projectPerson.noneDisabled = true;
                                projectPerson.allDisabled = false;
                            }
                            if (actionType == "DELETE") {
                                projectPerson.deleteSelected = true;
                                projectPerson.noneDisabled = true;
                                projectPerson.allDisabled = false;
                            }
                        })
                    }
                });
            }

            function loadProjectPersons() {
                ProjectService.getProjectPersons($stateParams.projectId).then(
                    function (data) {
                        vm.projectPersons = data;
                        CommonService.getPersonReferences(vm.projectPersons, 'person');
                    }
                )
            }

            function loadProjectRoles() {
                ProjectService.getProjectRoles($stateParams.projectId).then(
                    function (data) {
                        vm.projectRoles = data;
                    });
            }

            function loginPermission() {
                var isadmin = false;
                vm.loginPerson.isAdmin = false;
                vm.loginPerson.isProjectOwner = false;
                if ($rootScope.selectedProject.projectOwner == vm.loginPerson.id) {
                    vm.loginPerson.isProjectOwner = true;
                }
                if (window.$application.login.loginGroup.name == 'Administrator') {
                    isadmin = true;
                    vm.loginPerson.isAdmin = true;
                }
                if (!isadmin) {
                    vm.loginPerson.all = false;
                    vm.loginPerson.read = false;
                    vm.loginPerson.write = false;
                    vm.loginPerson.delete = false;
                    vm.loginPerson.create = false;
                    var personRoles = [];
                    angular.forEach(vm.objectPermissions, function (objectPermission) {
                        if (objectPermission.permissionLevel == "PERSON" && objectPermission.permissionAssignedTo == vm.loginPerson.id) {
                            vm.loginPerson.actionTypes = objectPermission.actionTypes;
                            angular.forEach(objectPermission.actionTypes, function (actionType) {
                                if (checkUserPermission(actionType)) {
                                    vm.show = true;
                                }
                                else {
                                    vm.show = false;
                                }
                            })
                        }

                        ProjectService.getRolesByPersonId($stateParams.projectId, vm.loginPerson.id).then(
                            function (data) {
                                personRoles = data;
                                angular.forEach(personRoles, function (personRole) {
                                    if (objectPermission.permissionLevel == "ROLE" && objectPermission.permissionAssignedTo == personRole.role) {
                                        angular.forEach(objectPermission.actionTypes, function (actionType) {
                                            if (checkUserPermission(actionType)) {
                                                vm.show = true;
                                                if (actionType == "WRITE") {
                                                    vm.loginPerson.write = true;
                                                }

                                                if (actionType == "ALL") {
                                                    vm.loginPerson.all = true;
                                                }

                                                if (actionType == "READ") {
                                                    vm.loginPerson.read = true;
                                                }

                                                if (actionType == "DELETE") {
                                                    vm.loginPerson.delete = true;
                                                }
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
            }

            function checkUserPermission(actionType) {
                var exists = false;

                if (actionType == "ALL" || actionType == "WRITE" || actionType == "READ") {
                    exists = true;
                }

                if (actionType == "WRITE") {
                    vm.loginPerson.write = true;
                }

                if (actionType == "ALL") {
                    vm.loginPerson.all = true;
                }

                if (actionType == "READ") {
                    vm.loginPerson.read = true;
                }

                if (actionType == "DELETE") {
                    vm.loginPerson.delete = true;
                }

                if (actionType == "CREATE") {
                    vm.loginPerson.create = true;
                }

                return exists;
            }

            function checkFolderPermission() {
                var exists = false;
                if (vm.selectedFolder.createdBy == window.$application.login.person.id) {
                    exists = true;
                }
                return exists;
            }

            function addPermission() {
                vm.showFileDropzone = false;
                vm.permission = true;
            }

            function initFoldersTree() {
                foldersTree = $('#docFoldersTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Project Folders',
                            iconCls: 'project-folders-root',
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

                rootNode = foldersTree.tree('find', 0);
                $(document).click(function () {
                    $("#contextMenu").hide();
                });
            }

            function onSelectFolder(node) {
                vm.message = "";
                vm.hasError = false;
                vm.permission = false;
                vm.documents = [];
                vm.showDropzone = false;
                var data = foldersTree.tree('getData', node.target);

                if (data != null && data.attributes.folder != null) {
                    vm.selectedFolder = data.attributes.folder;

                    fileDropZone.options.url = "api/projects/" + $stateParams.projectId + "/folders/" + vm.selectedFolder.id + "/documents";

                    fileDropZone.removeAllFiles();
                    angular.forEach(vm.projectRoles, function (role) {
                        role.allSelected = role.noneSelected = role.writeSelected =
                            role.readSelected = role.createSelected = role.deleteSelected = false;
                    });
                    angular.forEach(vm.projectPersons, function (person) {
                        person.allSelected = person.noneSelected = person.writeSelected =
                            person.readSelected = person.deleteSelected = false;
                        if (person.loginName == 'admin') {
                            person.allSelected = person.writeSelected = person.readSelected = person.deleteSelected = true;
                            person.allDisabled = person.noneDisabled = person.writeDisabled = person.readDisabled = person.deleteDisabled = true;
                        }
                    });

                    if (window.$application.login.loginGroup.name == 'Administrator') {
                        vm.loginPerson.isAdmin = true;
                    }
                    loadDocuments();

                    $timeout(function () {
                        loadPermissions();
                        //$scope.$apply();
                    }, 100);
                }
                else {
                    vm.selectedFolder = null;
                    $scope.$apply();
                }

            }

            function loadDocuments() {
                vm.loading = true;
                DocumentService.getDocuments($stateParams.projectId, vm.selectedFolder.id).then(
                    function (documents) {
                        var personIds = [];
                        angular.forEach(documents, function (document) {
                            if (document.createdBy != null && personIds.indexOf(document.createdBy) == -1) {
                                personIds.push(document.createdBy);
                            }

                            if (document.modifiedBy != null && personIds.indexOf(document.modifiedBy) == -1) {
                                personIds.push(document.modifiedBy);
                            }

                            if (document.lockedBy != null && personIds.indexOf(document.lockedBy) == -1) {
                                personIds.push(document.lockedBy);
                            }
                        });

                        if (personIds.length > 0) {
                            CommonService.getPersons(personIds).then(
                                function (persons) {
                                    var map = new Hashtable();
                                    angular.forEach(persons, function (person) {
                                        map.put(person.id, person);
                                    });

                                    angular.forEach(documents, function (document) {
                                        if (document.createdBy != null) {
                                            person = map.get(document.createdBy);
                                            if (person != null) {
                                                document.createdByPerson = person;
                                            }
                                            else {
                                                document.createdByPerson = {firstName: ""};
                                            }
                                        }

                                        if (document.modifiedBy != null) {
                                            person = map.get(document.modifiedBy);
                                            if (person != null) {
                                                document.modifiedByPerson = person;
                                            }
                                            else {
                                                document.modifiedByPerson = {firstName: ""};
                                            }
                                        }

                                        if (document.lockedBy != null) {
                                            person = map.get(document.lockedBy);
                                            if (person != null) {
                                                document.lockedByPerson = person;
                                            }
                                            else {
                                                document.lockedByPerson = {firstName: ""};
                                            }
                                        }
                                    });

                                    vm.documents = documents;
                                    vm.loading = false;
                                }
                            );
                        }
                        else {
                            vm.documents = documents;
                            vm.loading = false;
                        }
                        //$scope.$apply();

                    },
                    function (error) {
                        vm.loading = false;
                        //$rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function onContextMenu(e, node) {
                e.preventDefault();
                // select the node
                //foldersTree.tree('select', node.target);

                var $contextMenu = $("#contextMenu");
                $contextMenu.css({
                    display: "block",
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
                data.attributes.folder.project = parseInt($stateParams.projectId);
                data.attributes.folder.isLocked = false;

                var promise = null;
                var children = data.attributes.folder.children;
                data.attributes.folder.children = [];
                if (data.attributes.folder.name == '') {
                    $rootScope.showWarningMessage("Folder Name cannot be empty");
                    foldersTree.tree('remove', data.target);
                }
                else {
                    if (data.attributes.folder.id == undefined || data.attributes.folder.id == null) {
                        promise = DocumentService.createFolder($stateParams.projectId, data.attributes.folder);
                    }
                    else {
                        promise = DocumentService.updateFolder($stateParams.projectId, data.attributes.folder);
                    }
                    promise.then(
                        function (folder) {
                            folder.children = children;
                            data.attributes.folder = angular.copy(folder);
                            foldersTree.tree('update', {target: node.target, attributes: data.attributes});
                            foldersTree.tree('select', node.target);
                        },
                        function (error) {
                            $rootScope.showWarningMessage("Folder Name already exists");
                            foldersTree.tree('remove', data.target);
                        }
                    );
                }
            }

            function addFiles() {
                vm.showDropzone = true;
                fileDropZone.removeAllFiles();

            }

            function backToFiles() {
                vm.showDropzone = false;
            }

            function addFolder() {
                var selectedNode = foldersTree.tree('getSelected');

                if (selectedNode != null && $rootScope.selectedProject.locked == false) {
                    var nodeid = ++nodeId;

                    foldersTree.tree('append', {
                        parent: selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: 'project-folder',
                                text: 'New Folder',
                                attributes: {
                                    folder: null
                                }
                            }
                        ]
                    });

                    var newNode = foldersTree.tree('find', nodeid);
                    if (newNode != null && $rootScope.selectedProject.locked == false) {
                        //foldersTree.tree('select', newNode.target);
                        foldersTree.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                }
            }

            function deleteFolder() {
                var selectedNode = foldersTree.tree('getSelected');
                if (selectedNode != null && $rootScope.selectedProject.locked == false) {
                    var data = foldersTree.tree('getData', selectedNode.target);
                    var options = {
                        title: 'Delete Folder',
                        message: 'Are you sure you want to delete this (' + selectedNode.attributes.folder.name + ') folder?',
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            if (data != null && data.attributes.folder != null) {
                                DocumentService.deleteFolder($stateParams.projectId, data.attributes.folder.id).then(
                                    function (data) {
                                        foldersTree.tree('remove', selectedNode.target);
                                        vm.selectedFolder = null
                                    }
                                )
                            }
                        }
                    });
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
                DocumentService.getAllProjectFolders($stateParams.projectId, vm.folderType).then(
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
                    iconCls: 'project-folder',
                    attributes: {
                        folder: folder
                    }
                };
            }

            function selectFiles() {
                vm.message = "";
                vm.hasError = false;
                vm.showFiles = false;
                $('#dropzoneForm')[0].click();
            }

            function initDropzone() {
                $("#documentsDropzoneContainer").on('dragover', handleDragEnter);
                $("#documentsDropzoneContainer").on('dragleave', handleDragLeave);
                $("#documentsDropzoneContainer").on('drop', handleDragLeave);

                var previewNode = $("#template");
                var previewTemplate = previewNode.parent().html();
                previewNode.remove();

                var dropZone = new Dropzone(document.querySelector("#dropzoneForm"), { // Make the whole body a dropzone
                    url: "/target", // Set the url
                    thumbnailWidth: 80,
                    thumbnailHeight: 80,
                    maxFilesize: 2000,
                    timeout: 500000,
                    previewTemplate: previewTemplate,
                    autoQueue: true,// Make sure the files aren't queued until manually added
                    parallelUploads: 1,//Files are sent to the server one by one
                    previewsContainer: "#previews",
                    success: function (file, response) {
                        if (file != undefined) {
                            dropZone.on("queuecomplete", function (progress) {
                                $("#total-progress").hide();
                                $("#itemFilesTableContainer").removeClass('drag-over');
                                vm.showFileDropzone = false;
                                dropZone.removeAllFiles(true);
                                $scope.$apply();
                                loadDocuments();
                                loadPermissions();
                            });
                        }
                    },
                    error: function (file, response) {
                        vm.hasError = true;
                        vm.message = vm.message + file.name + ", ";
                        vm.showFiles = true;
                        //$rootScope.showErrorMessage(response.message);
                        dropZone.removeFile(file);
                        $scope.$apply();
                    }
                });
                dropZone.on("queuecomplete", function (progress) {
                    $timeout(function () {
                        vm.message = "";
                        vm.showFiles = true;
                        if (vm.hasError == false) {
                            $rootScope.showSuccessMessage("File(s) uploaded successfully");
                        }
                        vm.hasError = false;
                    }, 1000);
                });
                $("#itemFilesTableContainer").on('dragover', handleDragEnter);
                $("#itemFilesTableContainer").on('dragleave', handleDragLeave);
                $("#itemFilesTableContainer").on('drop', handleDragLeave);

                fileDropZone = dropZone;
            }

            function handleDragEnter(e) {
                $("#itemFilesTableContainer")[0].classList.add('drag-over');
                vm.showFileDropzone = true;

            }

            function handleDragLeave(e) {
                vm.showFiles = false;
                $("#itemFilesTableContainer")[0].classList.remove('drag-over');
                vm.showFileDropzone = false;
            }

            function fileSizeToString(bytes) {
                if (bytes == 0) {
                    return "0.00 B";
                }
                var e = Math.floor(Math.log(bytes) / Math.log(1024));
                return (bytes / Math.pow(1024, e)).toFixed(2) + ' ' + ' KMGTP'.charAt(e) + 'B';
            }

            function closeNotification() {
                vm.hasError = false;
                vm.message = "";
            }

            function downloadDocument(document) {
                var url = "{0}//{1}/api/projects/{2}/folders/{3}/documents/{4}/download".
                    format(window.location.protocol, window.location.host,
                    $stateParams.projectId, document.folder, document.id);
                window.open(url, '_self');
            }

            function lockDocument(document, lock) {
                document.locked = lock;
                document.modifiedByPerson = $application.login.person;
                if (lock) {
                    document.lockedBy = $application.login.person.id;
                    document.lockedByPerson = $application.login.person;
                    document.lockedDate = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                }
                else {
                    document.lockedBy = null;
                    document.lockedByPerson = null;
                    document.lockedDate = null;
                }
                DocumentService.updateDocument($stateParams.projectId, document.folder, document).then(
                    function (data) {
                        document = data;
                        if (document.locked == true) {
                            $rootScope.showSuccessMessage("File (" + document.name + ") locked successfully");
                        } else if (document.locked == false) {
                            $rootScope.showSuccessMessage("File (" + document.name + ") unlocked successfully");

                        }

                    }
                )
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

            function activateProjectTab(id) {
                var item = getTabById(id);
                if (item != null) {
                    lastSelectedItem.active = false;
                    item.active = true;
                    lastSelectedItem = item;

                    $timeout(function () {
                        $('.project-headerbar').trigger('click');
                    }, 100);

                    if (item.id == 'project.documents') {
                        $state.go(item.state, {type: 'documents'});
                    }
                    else {
                        $state.go(item.state);
                    }
                }
            }

            function deleteDocument(document) {

                var options = {
                    title: 'Delete File',
                    message: 'Are you sure you want to delete this (' + document.name + ') file?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        DocumentService.deleteDocument($stateParams.projectId, vm.selectedFolder.id, document.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage("File deleted successfully");
                                loadDocuments();
                            }
                        )
                    }
                });

            }

            function hasPermission(name) {
                var permission = "permission.files" + "." + name;
                return $rootScope.hasPermission(permission);
            }

            function showComments(document) {
                var options = {
                    title: 'Comments',
                    template: 'app/desktop/modules/shared/comments/commentsView.jsp',
                    controller: 'CommentsController as commentsVm',
                    resolve: 'app/desktop/modules/shared/comments/commentsController',
                    data: {
                        objectType: 'FILE',
                        objectId: document.id
                    }
                };

                $rootScope.showSidePanel(options);
            }

            (function () {
                if ($application.homeLoaded == true) {
                    vm.folderType = 'DEFAULT';
                    $rootScope.viewInfo.title = 'Documents';
                    activateProjectTab('project.documents');

                    $scope.$on('$viewContentLoaded', function () {
                        $('div.split-right-pane').css({left: 300});
                        $('div.split-pane').splitPane();
                        initFoldersTree();
                        loadRootFolders();
                        initDropzone();
                        loadProjectPersons();
                        loadProjectRoles();
                    });
                }
            })();
        }
    }
)
;
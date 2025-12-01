define(['app/desktop/modules/home/home.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/shared/services/documentService',
        'app/shared/services/folderService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'split-pane',
        'jquery.easyui',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/desktop/modules/shared/comments/commentsButtonDirective'

    ],
    function (module) {
        module.controller('HomeController', HomeController);

        function HomeController($scope, $rootScope, $timeout, $window, $state, $uibModal, $stateParams, $cookies,
                                        DocumentService, FolderService, CommonService, DialogService, LoginService) {

            $rootScope.viewInfo.icon = "fa fa-files-o";
            $rootScope.viewInfo.title = "Documents";

            var vm = this;

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
            vm.loginPerson = window.$application.login.person;
            vm.login = window.$application.login;
            vm.docType = $stateParams.type;
            vm.documents = [];
            vm.persons = [];
            vm.ProjectRoles = [];
            vm.objectPermissions = [];
            vm.permissions = [];
            vm.permissionActions = [];
            vm.rolePermissionActions = [];
            vm.personPermissionActions = [];
            vm.hasPermission = hasPermission;

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
            vm.checkAllPersonPermissions = checkAllPersonPermissions;
            vm.deleteDocument = deleteDocument;
            vm.selectFiles = selectFiles;
            vm.showFileHistory = showFileHistory;
            vm.hasError = false;
            vm.message = "";
            vm.closeNotification = closeNotification;

            vm.showAddFolderButton = true;
            vm.showDeleteFolderButton = true;
            vm.previous_node = null;

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

            function checkAllPersonPermissions(person) {

                if (person.allSelected) {
                    person.noneDisabled = true;
                    person.createDisabled = true;
                    person.readDisabled = true;
                    person.writeDisabled = true;
                    person.deleteDisabled = true;
                }
                else if(person.noneSelected) {
                    person.noneDisabled = false;
                    person.createDisabled = true;
                    person.readDisabled = true;
                    person.writeDisabled = true;
                    person.deleteDisabled = true;
                    person.allDisabled = true;
                }
                else if(person.createSelected) {
                    person.createDisabled = false;
                    person.allDisabled = true;
                    person.readDisabled = true;
                    person.writeDisabled = true;
                    person.deleteDisabled = true;
                    person.noneDisabled = true;
                }
                else if(person.createSelected) {
                    person.createDisabled = false;
                    person.allDisabled = true;
                    person.readDisabled = true;
                    person.writeDisabled = true;
                    person.deleteDisabled = true;
                    person.noneDisabled = true;
                }
                else if(person.createSelected) {
                    person.createDisabled = false;
                    person.allDisabled = true;
                    person.readDisabled = true;
                    person.writeDisabled = true;
                    person.deleteDisabled = true;
                    person.noneDisabled = true;
                }
                else if(person.readSelected) {
                    person.readDisabled = false;
                    person.allDisabled = true;
                    person.createDisabled = true;
                    person.writeDisabled = true;
                    person.deleteDisabled = true;
                    person.noneDisabled = true;
                }
                else if(person.writeSelected) {
                    person.writeDisabled = false;
                    person.allDisabled = true;
                    person.createDisabled = true;
                    person.readDisabled = true;
                    person.deleteDisabled = true;
                    person.noneDisabled = true;
                }
                else if(person.deleteSelected) {
                    person.deleteDisabled = false;
                    person.allDisabled = true;
                    person.createDisabled = true;
                    person.writeDisabled = true;
                    person.readDisabled = true;
                    person.noneDisabled = true;
                }
                else {
                    person.allDisabled = false;
                    person.createDisabled = false;
                    person.readDisabled = false;
                    person.writeDisabled = false;
                    person.deleteDisabled = false;
                    person.noneDisabled = false;
                }
            }


            function selectPermissions() {
                var objectPermissions = [];

                angular.forEach(vm.persons, function (person) {
                    var objectPermission = {
                        objectType: "FOLDER",
                        objectId: vm.selectedFolder.id,
                        permissionLevel: "PERSON",
                        permissionAssignedTo: person.person.id,
                        actionTypes: []
                    };
                    if (person.noneSelected) {
                        objectPermission.actionTypes.push("NONE");
                    }
                    if (person.readSelected) {
                        objectPermission.actionTypes.push("READ");
                    }
                    if (person.writeSelected) {
                        objectPermission.actionTypes.push("WRITE");
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
                FolderService.saveFolderPermissions(vm.selectedFolder.id, objectPermissions).then(
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
                DocumentService.getFolderPermissions(vm.selectedFolder.id).then(
                    function (data) {
                        vm.objectPermissions = data;
                        angular.forEach(vm.objectPermissions, function (objectPermission) {
                            actionTypes = objectPermission.actionTypes;
                            var row = getPerson(objectPermission.permissionAssignedTo);

                            angular.forEach(actionTypes, function (actionType) {
                                if (actionType == "ALL") {
                                    row.allSelected = true;
                                    row.noneDisabled = true;
                                    row.readDisabled = true;
                                    row.writeDisabled = true;
                                    row.deleteDisabled = true;
                                    row.allDisabled = false;
                                }
                                else if (actionType == "NONE") {
                                    row.noneSelected = true;
                                    row.readDisabled = true;
                                    row.writeDisabled = true;
                                    row.deleteDisabled = true;
                                    row.allDisabled = true;
                                    row.allSelected = false;
                                    row.readSelected = false;
                                    row.writeSelected = false;
                                    row.deleteSelected = false;
                                }
                                else if (actionType == "READ") {
                                    row.readSelected = true;
                                    row.noneDisabled = true;
                                    row.createDisabled = true;
                                    row.writeDisabled = true;
                                    row.deleteDisabled = true;
                                    row.allDisabled = true;
                                }
                                else if (actionType == "WRITE") {
                                    row.writeSelected = true;
                                    row.noneDisabled = true;
                                    row.readDisabled = true;
                                    row.deleteDisabled = true;
                                    row.allDisabled = true;
                                }
                                else if (actionType == "DELETE") {
                                    row.deleteSelected = true;
                                    row.noneDisabled = true;
                                    row.readDisabled = true;
                                    row.allDisabled = true;
                                    row.writeDisabled = true;
                                }
                                else {
                                    row.readDisabled = false;
                                    row.writeDisabled = false;
                                    row.deleteDisabled = false;
                                    row.allDisabled = false;
                                    row.noneDisabled = false;
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
                    });

            }

            function getPerson(permissionAssignedTo) {
                var personPermission = false;
                angular.forEach(vm.persons, function (person) {
                    if (person.person.id == permissionAssignedTo) {
                        personPermission = person;
                    }
                });
                return personPermission;
            }


            function loadPersons() {
                LoginService.getAllLogins().then(
                    function (data) {
                        vm.persons = data;
                    }
                )
            }

            function loginPermission() {
                vm.loginPerson = window.$application.login.person;
                vm.loginPerson.isAdmin = false;
                vm.loginPerson.isAdmin = false;
                if(window.$application.login.loginGroup.name == 'Administrator') {
                    vm.loginPerson.isAdmin = true;
                }
                else {
                    vm.loginPerson.all == false;
                    vm.loginPerson.read = false;
                    vm.loginPerson.write = false;
                    vm.loginPerson.delete = false;
                    vm.loginPerson.create = false;
                    vm.loginPerson.isAdmin = false;
                    angular.forEach(vm.objectPermissions, function (objectPermission) {
                        if (objectPermission.permissionAssignedTo == vm.loginPerson.id) {
                            angular.forEach(objectPermission.actionTypes, function (actionType) {
                                if (checkUserPermission(actionType)) {
                                    vm.show = true;
                                }
                                else {
                                    vm.show = false;
                                }
                            })
                        }
                    });
                }
            }

            function checkUserPermission(actionType) {
                var exists = false;

                if (actionType == "ALL" || actionType == "WRITE" || actionType == "READ") {
                    exists = true;
                }

                if(actionType == "WRITE") {
                    vm.loginPerson.write = true;
                }

                if(actionType == "ALL") {
                    vm.loginPerson.all = true;
                }

                if(actionType == "READ") {
                    vm.loginPerson.read = true;
                }

                if(actionType == "DELETE") {
                    vm.loginPerson.delete = true;
                }

                if(actionType == "CREATE") {
                    vm.loginPerson.create = true;
                }

                return exists;
            }

            function checkFolderPermission() {
                var exists = false;
                if (vm.selectedFolder.createdBy != null && vm.selectedFolder.createdBy == window.$application.login.person.id) {
                    exists = true;
                }
                if(vm.loginPerson.isAdmin == true) {
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
                            text: 'Document Folders',
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

            function selectFiles() {
                $('#dropzoneForm')[0].click();
            }

            function onSelectFolder(node) {
                vm.previous_node = node.text;

                vm.permission = false;
                vm.documents = [];
                vm.showDropzone = false;
                var data = foldersTree.tree('getData', node.target);

                if (data != null && data.attributes.folder != null) {
                    vm.selectedFolder = data.attributes.folder;

                    fileDropZone.options.url = "api/dm/folders/" + vm.selectedFolder.id + "/documents";

                    fileDropZone.removeAllFiles();
                    angular.forEach(vm.persons, function (person) {
                        person.allSelected = person.noneSelected = person.writeSelected =
                            person.readSelected = person.deleteSelected = false;
                        if (person.loginName == 'admin') {
                            person.allSelected = person.writeSelected = person.readSelected = person.deleteSelected = true;
                            person.allDisabled = person.noneDisabled = person.writeDisabled = person.readDisabled = person.deleteDisabled = true;
                        }
                    });
                    if(window.$application.login.loginGroup.name == 'Administrator') {
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
                if(vm.selectedFolder.id != null && vm.selectedFolder.id != undefined) {
                    DocumentService.getSelectedFolderDocuments(vm.selectedFolder.id).then(
                        function (documents) {
                            var personIds = [];
                            angular.forEach(documents, function (document) {
                                document.enableLockButton = false;
                                if (document.createdBy != null && personIds.indexOf(document.createdBy) == -1) {
                                    personIds.push(document.createdBy);
                                }

                                if (document.modifiedBy != null && personIds.indexOf(document.modifiedBy) == -1) {
                                    personIds.push(document.modifiedBy);
                                }

                                if (document.lockedBy != null && personIds.indexOf(document.lockedBy) == -1) {
                                    personIds.push(document.lockedBy);
                                }
                                if(document.locked && (vm.loginPerson.id == document.lockedBy)) {
                                    document.enableLockButton = true;
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
                        }
                    );
                }
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
                if($rootScope.hasPermission('vm.loginPerson.isAdmin' || 'vm.loginPerson.write' || 'vm.loginPerson.all' || 'vm.loginPerson.create')) {
                    var data = foldersTree.tree('getData', node.target);
                    if (data.id != 0) {
                        foldersTree.tree('beginEdit', node.target);
                    }

                    $timeout(function () {
                        $('.tree-editor').focus().select();
                    }, 1);
                }
            }

            function onAfterEdit(node) {

                if (node.text == "" || node.text == null || node.text == undefined) {

                    $rootScope.showErrorMessage("Folder Name cannot be empty");

                    if (vm.previous_node != null) {
                        node.text = vm.previous_node;
                        var data = foldersTree.tree('getData', node.target);
                        foldersTree.tree('update', {target: node.target, attributes: data.attributes});
                    }

                }
                else {
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
                    data.attributes.folder.isLocked = false;


                    var promise = null;
                    var children = data.attributes.folder.children;
                    data.attributes.folder.children = [];
                    if (data.attributes.folder.id == undefined || data.attributes.folder.id == null) {
                        promise = FolderService.createTopFolder(data.attributes.folder);
                    }
                    else {
                        promise = FolderService.updateTopFolder(data.attributes.folder);
                    }
                    promise.then(
                        function (folder) {
                            folder.children = children;
                            data.attributes.folder = angular.copy(folder);
                            foldersTree.tree('update', {target: node.target, attributes: data.attributes});
                            foldersTree.tree('select', node.target);

                            $rootScope.noFolders = false;
                        },
                        function (error) {
                            $rootScope.showWarningMessage(error.message);
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

                if (selectedNode != null) {
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
                    if (newNode != null) {
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
                if (selectedNode != null) {
                    var data = foldersTree.tree('getData', selectedNode.target);
                    var options = {
                        title: 'Delete Document',
                        message: 'Are you sure you want to delete (' + selectedNode.attributes.folder.name + ') Folder?',
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            if (data != null && data.attributes.folder != null) {
                                FolderService.deleteTopFolder(data.attributes.folder.id).then(
                                    function (data) {
                                        foldersTree.tree('remove', selectedNode.target);
                                        $rootScope.showSuccessMessage(selectedNode.attributes.folder.name + " : Folder deleted successfully.")
                                        vm.selectedFolder = null;
                                    }
                                )
                            }
                        }
                    })
                }
            }

            function visitFolders(parent, folders) {
                var nodes = [];
                angular.forEach(folders, function (folder) {
                    var node = makeNode(folder);

                    if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                        node.state = 'closed';
                        visitFolders(node, folder.children);
                    }

                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            $rootScope.noFolders = false;
            function loadRootFolders() {
                FolderService.getFolderTree().then(
                    function (data) {
                        var nodes = [];
                        if (data.length == 0) {
                            $rootScope.noFolders = true;
                        }
                        angular.forEach(data, function (folder) {
                            var node = makeNode(folder);

                            if (folder.children != null && folder.children != undefined && folder.children.length > 0) {
                                node.state = "closed";
                                visitFolders(node, folder.children);
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
                    success: function(file, response){
                        if(file != undefined) {
                            dropZone.on("queuecomplete", function (progress) {
                                $("#total-progress").hide();
                                $("#itemFilesTableContainer").removeClass('drag-over');
                                vm.showFileDropzone = false;
                                dropZone.removeAllFiles(true);
                                $scope.$apply();
                                $rootScope.showSuccessMessage("Files uploaded successfully");
                                loadDocuments();
                            });
                            $rootScope.showSuccessMessage("Files uploaded successfully");
                        }
                    },
                    error: function(file, response) {
                        vm.hasError = true;
                        vm.message = vm.message + file.name + ", ";
                        //$rootScope.showErrorMessage(response.message);
                        dropZone.removeFile(file);
                        $scope.$apply();
                    }
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

            function downloadDocument(document) {
                var url = "{0}//{1}/api/dm/folders/{2}/documents/{3}/download".
                    format(window.location.protocol, window.location.host,
                    document.folder, document.id);
                window.open(url,'_self');
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
                DocumentService.updateTopDocument(document.folder, document).then(
                    function (data) {
                        document = data;
                        if (lock == true) {
                            $rootScope.showSuccessMessage(document.name + " : Document Locked successfully.");
                        }
                        if (lock == false) {
                            $rootScope.showSuccessMessage(document.name + " : Document Unlocked successfully.");
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
                    else if (item.id == 'project.drawings') {
                        $state.go(item.state, {type: 'drawings'});
                    }
                    else {
                        $state.go(item.state);
                    }
                }
            }

            function deleteDocument(document) {

                var options = {
                    title: 'Delete Document',
                    message: 'Are you sure you want to delete (' + document.name + ') File?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        FolderService.deleteFolderDocument(vm.selectedFolder.id, document.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(document.name + " : File Deleted Successfully");
                                loadDocuments();
                            }
                        )
                    }
                });
            }

            function showFileHistory(fileId) {
                var options = {
                    title: 'File History',
                    template: 'app/desktop/modules/home/fileHistoryView.jsp',
                    controller: 'FileHistoryController as fileHistoryVm',
                    resolve: 'app/desktop/modules/home/fileHistoryController',
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

            function closeNotification() {
                vm.hasError = false;
                vm.message = "";
            }

            function hasPermission(name) {
                var permission = "permission.documents." + name;
                return $rootScope.hasPermission(permission);
            }

            $scope.$on('$viewContentLoaded', function () {
                $('div.split-pane').splitPane();
                initDropzone();
                loadPersons();
                initFoldersTree();
                loadRootFolders();
            });
        }
    }
);
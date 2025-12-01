define(['app/desktop/modules/tm/tm.module',
        'app/shared/services/pm/project/documentService',
        'split-pane',
        'jquery.easyui',
        'app/shared/services/tm/taskService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'

    ],
    function (module) {
        module.controller('DocumentDialogController', DocumentDialogController);

        function DocumentDialogController($scope, $rootScope, $timeout, $state, $stateParams, TaskService, CommonService, $cookies, DocumentService) {

            var vm = this;
            vm.folder = null;
            vm.folderType = null;
            vm.selectedFolder = null;
            vm.selectedAll = false;
            vm.loading = true;

            vm.documents = [];
            vm.selectDocuments = [];
            vm.checkAll = checkAll;
            vm.onSelectFolders = onSelectFolders;
            vm.createTaskDocument = createTaskDocument;
            vm.select = select;
            vm.taskDocuments = $scope.data.taskDocs;

            var nodeId = 0;
            var foldersTree = null;
            var rootNode = null;

            function onSelectFolders(folder) {
                if (folder != null && folder != undefined) {
                    vm.folder = folder;
                    loadDocuments(folder);
                }
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
                    onSelect: onSelectFolder
                });
                rootNode = foldersTree.tree('find', 0);
                $(document).click(function () {
                    $("#contextMenu").hide();
                });

            }

            function onSelectFolder(node) {
                vm.documents = [];
                vm.showDropzone = false;
                var data = foldersTree.tree('getData', node.target)

                if (data != null && data.attributes.folder != null) {
                    vm.selectedFolder = data.attributes.folder;
                    vm.selectedAll = false;

                    /*  fileDropZone.options.url = "api/projects/" + $stateParams.projectId + "/folders/" + vm.selectedFolder.id + "/documents";

                     fileDropZone.removeAllFiles();*/
                    loadDocuments(vm.selectedFolder);

                }
                else {
                    vm.selectedFolder = null;
                    $scope.$apply();
                }
            }

            function loadDocuments(folder) {
                vm.loading = true;
                var promise = null;
                if (folder == null) {
                    promise = DocumentService.getDocumentsByProject($stateParams.projectId);
                }
                else {
                    promise = DocumentService.getDocuments($stateParams.projectId, folder.id);
                }
                promise.then(
                    function (data) {
                        vm.loading = false;
                        vm.documents = data;
                        CommonService.getPersonReferences(vm.documents, 'lockedBy');
                    }
                )
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
                    iconCls: 'project-folder',
                    attributes: {
                        folder: folder
                    }
                };
            }

            function checkAll() {
                if (vm.selectedAll) {
                    vm.selectDocuments = [];
                    vm.selectedAll = true;
                    angular.forEach(vm.documents, function (document) {
                        document.selected = vm.selectedAll;
                        vm.selectDocuments.push(document);
                    });
                } else {
                    vm.selectedAll = false;
                    angular.forEach(vm.documents, function (document) {
                        document.selected = vm.selectedAll;
                        vm.selectDocuments = [];
                    });

                }
            }

            function select(document) {
                var flag = true;
                if (document.selected == false) {
                    vm.selectedAll = false;
                    var index = vm.selectDocuments.indexOf(document);
                    vm.selectDocuments.splice(index, 1);
                } else {
                    angular.forEach(vm.selectDocuments, function (docu) {
                        if (docu.id == document.id) {
                            flag = false;
                            var index = vm.selectDocuments.indexOf(document);
                            vm.selectDocuments.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectDocuments.push(document);
                    }
                }
            }

            function createTaskDocument() {
                var taskFiles = [];
                var obj = null;
                if (vm.selectDocuments.length != 0) {
                    angular.forEach(vm.selectDocuments, function (document) {
                        if (!(isExists(document))) {
                            obj = {
                                task: $stateParams.taskId,
                                type: "DOCUMENT",
                                refItem: document.id
                            };
                            taskFiles.push(obj);
                        }
                        else {
                            $rootScope.showErrorMessage("This document is already exists in Task");
                        }
                    });
                    if (taskFiles.length > 0) {
                        TaskService.createTaskFiles($stateParams.projectId, $stateParams.taskId, taskFiles).then(
                            function (data) {
                                $rootScope.hideSidePanel('left');
                                $scope.callback(data);
                                $rootScope.showSuccessMessage("Document(s) added successfully");
                                vm.selectDocuments = [];
                            }
                        )
                    }
                }
                else {
                    $rootScope.showErrorMessage("Please add  atleast one Document(s)");
                }

            }

            function isExists(document) {
                var exists = false;
                angular.forEach($scope.data.taskDocs, function (taskDocument) {
                    if (taskDocument.refItem == document.id) {
                        exists = true;
                    }
                });
                return exists;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $timeout(function () {
                        initFoldersTree();
                        loadRootFolders();
                    }, 500);
                    $scope.$on('app.task.newDocument', createTaskDocument);
                }
            })();
        }
    }
)
;
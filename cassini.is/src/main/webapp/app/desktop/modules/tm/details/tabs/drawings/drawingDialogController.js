define(['app/desktop/modules/tm/tm.module',
        'app/shared/services/pm/project/documentService',
        'split-pane',
        'jquery.easyui',
        'app/shared/services/tm/taskService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'

    ],
    function (module) {
        module.controller('DrawingDialogController', DrawingDialogController);

        function DrawingDialogController($scope, $rootScope, $timeout, $state, $stateParams, TaskService, CommonService, $cookies, DocumentService) {

            var vm = this;
            vm.loading = true;
            vm.selectedAll = false;
            vm.folder = null;
            vm.selectedFolder = null;

            vm.drawings = [];
            vm.selectDrawings = [];
            vm.onSelectFolders = onSelectFolders;
            vm.createTaskDrawing = createTaskDrawing;
            vm.select = select;
            vm.checkAll = checkAll;
            vm.taskDrawings = $scope.data.taskDrawings;

            var nodeId = 0;
            var foldersTree = null;
            var rootNode = null;

            function onSelectFolders(folder) {
                if (folder != null && folder != undefined) {
                    vm.folder = folder;
                    loadDrawings(folder);
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
                vm.showDropzone = false;
                var data = foldersTree.tree('getData', node.target);

                if (data != null && data.attributes.folder != null) {
                    vm.selectedFolder = data.attributes.folder;
                    vm.selectedAll = false;

                    /*  fileDropZone.options.url = "api/projects/" + $stateParams.projectId + "/folders/" + vm.selectedFolder.id + "/documents";

                     fileDropZone.removeAllFiles();*/
                    loadDrawings(vm.selectedFolder);

                }
                else {
                    vm.selectedFolder = null;
                    $scope.$apply();
                }
            }

            function loadDrawings(folder) {
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
                        vm.drawings = data;
                        CommonService.getPersonReferences(vm.drawings, 'lockedBy');
                    }
                )
            }

            function loadRootFolders() {
                DocumentService.getAllProjectFolders($stateParams.projectId, "DRAWING").then(
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
                    vm.selectDrawings = [];
                    vm.selectedAll = true;
                    angular.forEach(vm.drawings, function (drawing) {
                        drawing.selected = vm.selectedAll;
                        vm.selectDrawings.push(drawing);
                    });
                } else {
                    vm.selectedAll = false;
                    angular.forEach(vm.drawings, function (drawing) {
                        drawing.selected = vm.selectedAll;
                        vm.selectDrawings = [];
                    });
                }

            }

            function select(drawing) {
                var flag = true;
                if (drawing.selected == false) {
                    vm.selectedAll = false;
                    var index = vm.selectDrawings.indexOf(drawing);
                    vm.selectDrawings.splice(index, 1);
                } else {
                    angular.forEach(vm.selectDrawings, function (draw) {
                        if (draw.id == drawing.id) {
                            flag = false;
                            var index = vm.selectDrawings.indexOf(drawing);
                            vm.selectDrawings.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectDrawings.push(drawing);
                    }
                }
            }

            function createTaskDrawing() {
                var taskFiles = [];
                var obj = null;
                if (vm.selectDrawings.length != 0) {
                    angular.forEach(vm.selectDrawings, function (drawing) {
                        if (!(isExists(drawing))) {
                            obj = {
                                task: $stateParams.taskId,
                                type: "DRAWING",
                                refItem: drawing.id
                            };
                            taskFiles.push(obj);
                        }
                        else {
                            $rootScope.showErrorMessage("This drawing is already exists in task");
                        }
                    });
                    if (taskFiles.length > 0) {
                        TaskService.createTaskFiles($stateParams.projectId, $stateParams.taskId, taskFiles).then(
                            function (data) {
                                $rootScope.hideSidePanel('left');
                                $scope.callback(data);
                                $rootScope.showSuccessMessage("Drawing(s) added successfully");
                                vm.selectDrawings = [];
                            }
                        )
                    }
                }
                else {
                    $rootScope.showErrorMessage("Please add  atleast one Drawing(s)");
                }

            }

            function isExists(drawing) {
                var exists = false;
                angular.forEach($scope.data.taskDrawings, function (taskDrawing) {
                    if (taskDrawing.refItem == drawing.id) {
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
                    $scope.$on('app.task.newDrawing', createTaskDrawing);
                }
            })();
        }
    }
)
;
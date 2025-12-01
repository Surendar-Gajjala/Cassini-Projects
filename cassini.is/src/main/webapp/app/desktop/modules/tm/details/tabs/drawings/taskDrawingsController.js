define(['app/desktop/modules/tm/tm.module',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/documentService',
        'app/desktop/modules/tm/details/tabs/drawings/drawingDialogController'

    ],
    function (module) {
        module.controller('TaskDrawingsController', TaskDrawingsController);

        function TaskDrawingsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                        TaskService, DocumentService, DialogService) {
            var vm = this;

            vm.taskDrawings = [];
            vm.document = null;
            vm.downloadDocument = downloadDocument;
            vm.fileSizeToString = fileSizeToString;
            vm.deleteTaskDrawing = deleteTaskDrawing;

            function loadTaskDrawings() {
                TaskService.getTaskFiles($stateParams.projectId, $stateParams.taskId, "DRAWING").then(
                    function (data) {
                        vm.taskDrawings = data;
                        DocumentService.getDocumentReferences($stateParams.projectId, vm.taskDrawings, 'refItem');
                    }
                )
            }

            function downloadDocument(taskDocument) {
                DocumentService.getDocument($stateParams.projectId, taskDocument.refItem).then(
                    function (data) {
                        vm.document = data;
                        var url = "{0}//{1}/api/projects/{2}/folders/{3}/documents/{4}/download".
                            format(window.location.protocol, window.location.host,
                            $stateParams.projectId, vm.document.folder, vm.document.id);
                        launchUrl(url);
                    }
                );

            }

            function addTaskDrawings() {
                var options = {
                    title: 'Add Drawing(s)',
                    side: 'left',
                    showMask: true,
                    template: 'app/desktop/modules/tm/details/tabs/drawings/drawingDialog.jsp',
                    controller: 'DrawingDialogController as drawingVm',
                    resolve: 'app/desktop/modules/tm/details/tabs/drawings/drawingDialogController',
                    width: 800,
                    data: {
                        taskDrawings: vm.taskDrawings
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.task.newDrawing'}
                    ],
                    callback: function () {
                        loadTaskDrawings();
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

            function deleteTaskDrawing(drawing) {
                var options = {
                    title: 'Delete Document',
                    message: 'Are you sure you want to delete this Document?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TaskService.deleteTaskAttachment($stateParams.projectId, drawing.rowId).then(
                            function (data) {
                                var index = vm.taskDrawings.indexOf(drawing);
                                vm.taskDrawings.splice(index, 1);
                                $rootScope.showSuccessMessage("Drawing deleted successfully");
                            }
                        )

                    }
                })
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadTaskDrawings();
                    $scope.$on('app.task.addDrawing', addTaskDrawings)
                }
            })();
        }
    }
)
;

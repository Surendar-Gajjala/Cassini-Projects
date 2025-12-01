define(['app/desktop/modules/tm/tm.module',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/documentService',
        'app/desktop/modules/tm/details/tabs/documents/documentDialogController'

    ],
    function (module) {
        module.controller('TaskDocumentsController', TaskDocumentsController);

        function TaskDocumentsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                         TaskService, DocumentService, DialogService) {
            var vm = this;

            vm.taskDocuments = [];
            vm.document = null;
            vm.downloadDocument = downloadDocument;
            vm.fileSizeToString = fileSizeToString;
            vm.deleteTaskDocument = deleteTaskDocument;

            function loadTaskDocuments() {
                TaskService.getTaskFiles($stateParams.projectId, $stateParams.taskId, "DOCUMENT").then(
                    function (data) {
                        vm.taskDocuments = data;
                        DocumentService.getDocumentReferences($stateParams.projectId, vm.taskDocuments, 'refItem');
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
                        window.open(url, '_self');
                    }
                );
            }

            function addTaskDocuments() {
                var options = {
                    title: 'Add Document(s)',
                    side: 'left',
                    showMask: true,
                    template: 'app/desktop/modules/tm/details/tabs/documents/documentDialog.jsp',
                    controller: 'DocumentDialogController as documentVm',
                    resolve: 'app/desktop/modules/tm/details/tabs/documents/documentDialogController',
                    width: 800,
                    data: {
                        taskDocs: vm.taskDocuments
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.task.newDocument'}
                    ],
                    callback: function () {
                        loadTaskDocuments();
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

            function deleteTaskDocument(document) {
                var options = {
                    title: 'Delete Document',
                    message: 'Are you sure you want to delete this Document?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TaskService.deleteTaskAttachment($stateParams.projectId, document.rowId).then(
                            function (data) {
                                var index = vm.taskDocuments.indexOf(document);
                                vm.taskDocuments.splice(index, 1);
                                $rootScope.showSuccessMessage("Task document deleted successfully");
                            }
                        )

                    }
                })
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadTaskDocuments();
                    $scope.$on('app.task.addDocument', addTaskDocuments);
                }
            })();
        }
    }
)
;

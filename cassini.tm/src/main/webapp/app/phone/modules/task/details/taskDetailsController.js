define(
    [
        'app/phone/modules/task/task.module',
        'app/shared/services/taskService',
        'app/shared/services/projectService',
        'app/phone/modules/task/details/noteDialogController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/noteService'
    ],
    function(module) {
        module.controller('TaskDetailsController', TaskDetailsController);

        function TaskDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $mdDialog,
                                       TaskService, ProjectService, CommonService, NoteService) {
            $rootScope.viewName = "Task Details";

            var vm = this;

            vm.task = null;
            vm.history = [];
            vm.notes = [];

            var emptyNote = {
                objectType: "PROJECTTASK",
                objectId: $stateParams.taskId,
                title: "Project Task",
                details: null
            };

            vm.newNote = angular.copy(emptyNote);

            vm.showNoteDialog = showNoteDialog;
            vm.takePicture = takePicture;

            function loadTask() {
                var taskId = $stateParams.taskId;
                var projectId = $stateParams.projectId;

                TaskService.getProjectTask(projectId, taskId).then (
                    function (data) {
                        vm.task = data;
                        vm.task.pictures = [];
                        CommonService.getPersonReferences([vm.task], 'assignedTo');
                        CommonService.getPersonReferences([vm.task], 'verifiedBy');
                        CommonService.getPersonReferences([vm.task], 'approvedBy');

                        loadHistory();
                        loadNotes();
                        loadTaskImages();
                    }
                )
            }

            function loadTaskImages() {
                TaskService.getTaskImages(vm.task.project, vm.task.id).then(
                    function(data) {
                        vm.task.pictures = data;
                    }
                )
            }

            function loadHistory() {
                TaskService.getTaskHistory(vm.task.project, vm.task.id).then(
                    function (data) {
                        vm.history = data;
                        CommonService.getPersonReferences(vm.history, 'updatedBy');
                    }
                )
            }

            function showNoteDialog() {
                $mdDialog.show({
                    controller: 'NoteDialogController',
                    templateUrl: 'app/phone/modules/task/details/noteDialogView.jsp',
                    parent: angular.element(document.body),
                    targetEvent: null,
                    clickOutsideToClose: true
                }).then(
                    function(note) {
                        if(note != null && note.trim() != "") {
                            addNote(note);
                        }
                    }
                );
            }

            function addNote(note) {
                vm.newNote.details = note;
                NoteService.createNote(vm.newNote).then(
                    function(note) {
                        vm.newNote = angular.copy(emptyNote);
                        CommonService.getPersonReferences([note], 'createdBy');
                        CommonService.getPersonReferences([note], 'modifiedBy');
                        vm.notes.push(note);
                        vm.showForm = false;
                    }
                )
            }

            function loadNotes() {
                NoteService.getNotes('PROJECTTASK', $stateParams.taskId).then(
                    function(notes) {
                        vm.notes = notes;
                        CommonService.getPersonReferences(vm.notes, 'createdBy');
                        CommonService.getPersonReferences(vm.notes, 'modifiedBy');
                    }
                )
            }

            function takePicture() {
                App2AndroidBridge.captureImage(function(imgData) {
                    var base64 = "data:image/png;base64,"+ imgData;
                    var image = {
                        task: vm.task.id,
                        imageData: base64
                    };

                    TaskService.addTaskImage(vm.task.project, vm.task.id, image).then(
                        function(data) {
                            vm.task.pictures.push(data);
                            $scope.$apply();
                        }
                    );
                });
            }

            (function() {
                loadTask();
            })();
        }
    }
);
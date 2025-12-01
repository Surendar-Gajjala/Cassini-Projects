define(['app/desktop/modules/tm/tm.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/tm/taskService',
        'app/desktop/modules/tm/details/tabs/manpower/manpowerDialogueController'

    ],
    function (module) {
        module.controller('ManpowerTaskController', ManpowerTaskController);
        function ManpowerTaskController($scope, $rootScope, $timeout, $state, $stateParams, ProjectService,
                                        CommonService, DialogService, TaskService) {

            var vm = this;

            vm.deletePersonResource = deletePersonResource;
            vm.taskId = $stateParams.taskId;
            vm.projectId = $stateParams.projectId;
            vm.projectResources = [];
            vm.loading = false;
            vm.showComments = showComments;

            function showComments(resource) {
                var options = {
                    title: 'Comments',
                    template: 'app/desktop/modules/shared/comments/commentsView.jsp',
                    controller: 'CommentsController as commentsVm',
                    resolve: 'app/desktop/modules/shared/comments/commentsController',
                    data: {
                        objectType: 'PERSON',
                        objectId: resource.referenceId
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadPersonResources() {
                vm.loading = true;
                TaskService.getProjectAndRoleResourcesByType(vm.projectId, vm.taskId, ['MANPOWERTYPE', 'ROLE']).then(
                    function (data) {
                        vm.projectResources = data;
                        CommonService.getPersonReferences(vm.projectResources, 'referenceId');
                        ProjectService.getRoleReferences(vm.projectId, vm.projectResources, 'referenceId');
                        angular.forEach(vm.projectResources, function (resources) {
                            resources.editMode = false;
                        })
                        $timeout(function () {
                            vm.loading = false;
                        }, 1000);
                    });
            }

            function deletePersonResource(resource) {
                if (resource.resourceType == 'ROLE') {
                    var options = {
                        title: 'Remove Role',
                        message: 'Are you sure you want to remove this Role?',
                        okButtonClass: 'btn-danger'
                    };
                } else {
                    var options = {
                        title: 'Remove Person',
                        message: 'Are you sure you want to Remove this Person?',
                        okButtonClass: 'btn-danger'
                    };
                }
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ProjectService.deleteResource(vm.projectId, resource.id).then(
                            function (data) {
                                var index = vm.projectResources.indexOf(resource);
                                vm.projectResources.splice(index, 1);
                                if (resource.resourceType == 'ROLE') {
                                    $rootScope.showSuccessMessage("Role Removed successfully");
                                } else {
                                    $rootScope.showSuccessMessage("Person Removed successfully");
                                }
                                loadPersonResources();
                                $rootScope.loadDetailsCount();
                            }
                        )
                    }
                });
            }

            function addManpower() {
                var options =
                {
                    title: 'Select People',
                    side: 'left',
                    showMask: true,
                    template: 'app/desktop/modules/tm/details/tabs/manpower/manpowerDialogueView.jsp',
                    controller: 'ManpowerDialogueController as manpowerDialogueVm',
                    resolve: 'app/desktop/modules/tm/details/tabs/manpower/manpowerDialogueController',
                    width: 700,
                    data: {
                        personResources: vm.projectResources
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.manpower.new'}
                    ],
                    callback: function () {
                        loadPersonResources();
                        $rootScope.loadDetailsCount();
                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.editRole = editRole;

            function editRole(resource) {
                resource.editMode = true;
                resource.oldQty = resource.quantity;
            }

            vm.saveRole = saveRole;

            function saveRole(resource) {
                if (resource.quantity < 0) {
                    $rootScope.showWarningMessage("Please enter +ve number");
                    loadPersonResources();
                } else {
                    TaskService.updateResource(vm.projectId, resource).then(
                        function (data) {
                            resource.editMode = false;
                            $rootScope.showSuccessMessage("Role qty updated successfully");
                            loadPersonResources();
                        }
                    )
                }

            }

            vm.cancelChanges = cancelChanges;

            function cancelChanges(resource) {
                resource.editMode = false;
                resource.quantity = resource.oldQty;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.task.tabactivated', function (event, data) {
                        if (data.tabId == 'details.manpower') {
                            loadPersonResources();
                        }
                    });
                    $scope.$on('app.task.addManpower', addManpower)
                }
            })();
        }
    });
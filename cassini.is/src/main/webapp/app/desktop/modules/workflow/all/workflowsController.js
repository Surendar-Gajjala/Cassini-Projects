define(
    [
        'app/desktop/modules/workflow/workflow.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/workflow/workflowDefinitionService'
    ],
    function (module) {
        module.controller('WorkflowsController', WorkflowsController);

        function WorkflowsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, DialogService,
                                     CommonService, WorkflowDefinitionService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var parse = angular.element("<div></div>");
            $rootScope.viewInfo.icon = "fa flaticon-plan2";
            $rootScope.viewInfo.title = parse.html($translate.instant('WORKFLOW_ALL_TITLE')).html();

            var vm = this;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };
            vm.search = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                objectType: 'PLMWORKFLOWDEFINITION'
            };

            vm.loading = true;
            vm.workflows = [];
            var searchMode = null;
            $scope.freeTextQuery = null;

            vm.editWorkflow = editWorkflow;
            vm.newWorkflow = newWorkflow;
            vm.showSaveAsPanel = showSaveAsPanel;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;

            function resetPage() {
                vm.pageable.page = 0;
                $rootScope.showSearch = false;
                $rootScope.searchModeType = false;
            }

            function nextPage() {
                if (vm.workflows.last != true) {
                    vm.pageable.page++;
                    if ($scope.freeTextQuery != null && $scope.freeTextQuery != "") {
                        WorkflowDefinitionService.freeTextSearch(vm.pageable, $scope.freeTextQuery).then(
                            function (data) {
                                vm.workflows = data;
                                vm.clear = true;
                                vm.loading = false;
                                $rootScope.showSearch = true;
                                $rootScope.searchModeType = true;
                                CommonService.getPersonReferences(vm.workflows.content, 'createdBy');
                                CommonService.getPersonReferences(vm.workflows.content, 'modifiedBy');
                            }
                        )
                    }
                    else {
                        loadWorkflows();
                    }
                }
            }

            function previousPage() {
                if (vm.workflows.first != true) {
                    vm.pageable.page--;
                    if ($scope.freeTextQuery != null && $scope.freeTextQuery != "") {
                        WorkflowDefinitionService.freeTextSearch(vm.pageable, $scope.freeTextQuery).then(
                            function (data) {
                                vm.workflows = data;
                                vm.clear = true;
                                vm.loading = false;
                                $rootScope.showSearch = true;
                                $rootScope.searchModeType = true;
                                CommonService.getPersonReferences(vm.workflows.content, 'createdBy');
                                CommonService.getPersonReferences(vm.workflows.content, 'modifiedBy');
                            }
                        )
                    }
                    else {
                        loadWorkflows();
                    }
                }
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            function editWorkflow(workflow) {
                $state.go('app.workflow.editor', {mode: 'edit', workflow: workflow.id});
            }

            function newWorkflow() {
                $state.go('app.workflow.editor');
            }

            function loadWorkflows() {
                WorkflowDefinitionService.getAllWorkflowDefinitions(vm.pageable).then(
                    function (data) {
                        vm.workflows = data;
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.workflows.content, 'createdBy');
                        CommonService.getPersonReferences(vm.workflows.content, 'modifiedBy');
                    }
                )
            }

            var saveAs = $translate.instant("WORKFLOW_SAVEAS_TITLE");
            var save = parse.html($translate.instant('SAVE')).html();

            function showSaveAsPanel(workflow) {
                var options = {
                    title: saveAs,
                    template: 'app/desktop/modules/workflow/all/saveAsView.jsp',
                    controller: 'SaveWorkflowAsController as saveAsVm',
                    resolve: 'app/desktop/modules/workflow/all/saveAsController',
                    showMask: true,
                    width: 400,
                    buttons: [
                        {text: save, broadcast: 'app.workflow.saveas'}
                    ],
                    data: {
                        workflow: workflow
                    },
                    callback: function () {
                        loadWorkflows();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function freeTextSearch(freeText) {
                searchMode = "freetext";
                $scope.freeTextQuery = freeText;
                vm.pageable.page = 0;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.search.searchType = "freetext";
                    vm.search.query = angular.toJson(freeText);
                    WorkflowDefinitionService.freeTextSearch(vm.pageable, freeText).then(
                        function (data) {
                            vm.workflows = data;
                            vm.clear = true;
                            vm.loading = false;
                            $rootScope.showSearch = true;
                            $rootScope.searchModeType = true;
                            CommonService.getPersonReferences(vm.workflows.content, 'createdBy');
                            CommonService.getPersonReferences(vm.workflows.content, 'modifiedBy');
                        }
                    )
                }
                else {
                    vm.resetPage();
                    loadWorkflows();
                }
            }

            vm.deleteWorkflow = deleteWorkflow;

            function deleteWorkflow(workflow) {
                var options = {
                    title: "Delete Workflow",
                    message: 'Are you sure want to delete this (' + workflow.name + ') workflow?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        WorkflowDefinitionService.deleteWorkflow(workflow.id).then(
                            function (data) {
                                var index = vm.workflows.content.indexOf(workflow);
                                vm.workflows.content.splice(index, 1);
                                $rootScope.showSuccessMessage("Workflow deleted successfully");
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            (function () {
                loadWorkflows();
            })();
        }
    }
);
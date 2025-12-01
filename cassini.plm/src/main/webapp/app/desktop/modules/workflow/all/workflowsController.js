define(
    [
        'app/desktop/modules/workflow/workflow.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('WorkflowsController', WorkflowsController);

        function WorkflowsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, DialogService,
                                     CommonService, WorkflowDefinitionService, RecentlyVisitedService) {


            var parse = angular.element("<div></div>");
            $rootScope.viewInfo.icon = "fa flaticon-plan2";
            $rootScope.viewInfo.title = parse.html($translate.instant('WORKFLOW_ALL_TITLE')).html();
            $rootScope.viewInfo.showDetails = false;
            var vm = this;


            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };
            $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
            vm.search = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                objectType: 'PLMWORKFLOWDEFINITION',
                owner: $rootScope.localStorageLogin.login.person.id
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
            $rootScope.allWorkflowType = "templates";

            function nextPage() {
                if (vm.workflows.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadWorkflows();
                }
            }

            function previousPage() {
                if (vm.workflows.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadWorkflows();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadWorkflows();
            }


            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            function editWorkflow(workflow) {
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                vm.recentlyVisited.objectId = workflow.latestRevision;
                vm.recentlyVisited.objectType = "PLMWORKFLOWDEFINITION";
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.workflow.editor', {mode: 'edit', workflow: workflow.latestRevision});
                    }, function (error) {
                        $state.go('app.workflow.editor', {mode: 'edit', workflow: workflow.latestRevision});
                    }
                )
            }

            function newWorkflow() {
                $state.go('app.workflow.editor');
            }

            var saveAs = parse.html($translate.instant("WORKFLOW_SAVEAS_TITLE")).html();
            var copy = parse.html($translate.instant("WORKFLOW_COPY_TITLE")).html();
            var save = parse.html($translate.instant('SAVE')).html();
            $scope.workflowInstanceTitle = parse.html($translate.instant('CLICK_TO_SHOW_WF_INSTANCES')).html();

            function showSaveAsPanel(workflow) {
                WorkflowDefinitionService.getWorkflowDefinition(workflow.latestRevision).then(
                    function (data) {
                        var options = {
                            title: copy,
                            template: 'app/desktop/modules/workflow/all/saveAsView.jsp',
                            controller: 'SaveWorkflowAsController as saveAsVm',
                            resolve: 'app/desktop/modules/workflow/all/saveAsController',
                            showMask: true,
                            width: 400,
                            buttons: [
                                {text: save, broadcast: 'app.workflow.saveas'}
                            ],
                            data: {
                                workflow: data
                            },
                            callback: function () {
                                loadWorkflows();
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }


            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadWorkflows();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadWorkflows();
            }

            var workflowDelete = parse.html($translate.instant("ITEMDELETE")).html();
            var deleteWorkflowDialogTitle = parse.html($translate.instant("DELETE_WORKFLOW")).html();
            var deleteWorkflowDialogMessage = parse.html($translate.instant("DELETE_WORKFLOW_DIALOG_MESSAGE")).html();
            var workflowDeletedMessage = parse.html($translate.instant("WORKFLOW_DELETE_MSG")).html();
            vm.deleteWorkflow = deleteWorkflow;

            function deleteWorkflow(workflow) {
                var options = {
                    title: deleteWorkflowDialogTitle,
                    message: deleteWorkflowDialogMessage + " [ " + workflow.name + " ] " + workflowDelete + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        WorkflowDefinitionService.deleteWorkflow(workflow.id).then(
                            function (data) {
                                var index = vm.workflows.content.indexOf(workflow);
                                vm.workflows.content.splice(index, 1);
                                $rootScope.showSuccessMessage(workflowDeletedMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            var workflowInstances = parse.html($translate.instant("WORKFLOW_INSTANCES_TITLE")).html();
            vm.showInstances = showInstances;
            function showInstances(workflow) {
                var options = {
                    title: workflowInstances,
                    template: 'app/desktop/modules/workflow/all/workflowInstances.jsp',
                    controller: 'WorkflowInstanceController as wfInstanceVm',
                    resolve: 'app/desktop/modules/workflow/all/workflowInstancesController',
                    showMask: true,
                    width: 750,
                    data: {
                        workflow: workflow,
                        mode: "Master"
                    },
                    callback: function () {
                        loadWorkflows();
                    }
                };

                $rootScope.showSidePanel(options);
            }


            vm.filters = {
                searchQuery: null
            };

            function loadWorkflows() {
                WorkflowDefinitionService.getAllWorkflowRevisionDefinitions(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.workflows = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator($('.view-container'));
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            var revisionHistoryTitle = $translate.instant("REVISION_HISTORY_TITLE");
            vm.showWorkflowRevisionHistory = showWorkflowRevisionHistory;

            function showWorkflowRevisionHistory(workflow) {
                var options = {
                    title: workflow.number + " - " + revisionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: workflow.id,
                        revisionHistoryType: "WORKFLOW"
                    },
                    width: 700,
                    showMask: true,
                };

                $rootScope.showSidePanel(options);
            }


            (function () {
                loadWorkflows();
            })();
        }
    }
);
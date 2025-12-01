define(['app/desktop/modules/mfr/mfr.module',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('WorkflowSelectionController', WorkflowSelectionController);

        function WorkflowSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore, $translate,
                                             $uibModal, WorkflowDefinitionService, WorkflowService, CommonService) {

            var vm = this;

            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.resetPage = resetPage;
            vm.freeTextSearch = freeTextSearch;
            var objectId = $scope.data.existObjectId;
            var parse = angular.element("<div></div>");
            var selectOneWorkflow = parse.html($translate.instant('PLEASE_SELECT_ONE_WORKFLOW')).html();
            $scope.clearTitleSearch = parse.html($translate.instant("CLEAR_SEARCH")).html();
            vm.selectAttributeDef = $scope.data.selectAttDef;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                }
            };
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.selectedObj = null;
            vm.manufacturers = angular.copy(pagedResults);

            vm.filters = {
                searchQuery: null,
                type: ''
            };

            function freeTextSearch() {
                vm.pageable.page = 0;
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    $scope.freeTextQuery = vm.searchTerm;
                    vm.filters.searchQuery = vm.searchTerm;
                    if (vm.selectAttributeDef.refSubType == null) {
                        promise = WorkflowDefinitionService.getAllWorkflowDefs(vm.pageable, vm.filters)
                    } else if (vm.selectAttributeDef.refSubType != null) {
                        vm.filters.type = vm.selectAttributeDef.refSubType;
                        promise = WorkflowDefinitionService.getWorkflowDefsByTypeId(vm.pageable, vm.filters)
                    }
                    if (promise != null) {
                        promise.then(
                            function (data) {
                                loadSelectedWorkflows(data);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                } else {
                    vm.resetPage();
                    loadWorkflows();
                }
            }

            function resetPage() {
                vm.workflows = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.searchTerm = null;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                loadWorkflows();
            }

            function nextPage() {
                vm.pageable.page++;
                loadWorkflows();
            }

            function previousPage() {
                vm.pageable.page--;
                loadWorkflows();
            }

            function loadWorkflows() {
                vm.clear = false;
                vm.loading = true;
                if (vm.selectAttributeDef.refSubType == null) {
                    promise = WorkflowDefinitionService.getAllWorkflowDefs(vm.pageable, vm.filters)
                } else if (vm.selectAttributeDef.refSubType != null) {
                    vm.filters.type = vm.selectAttributeDef.refSubType;
                    promise = WorkflowDefinitionService.getWorkflowDefsByTypeId(vm.pageable, vm.filters)
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            loadSelectedWorkflows(data);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadSelectedWorkflows(data) {
                vm.workflows = data;
                vm.loading = false;
                var existObjectId = false;
                angular.forEach(vm.workflows.content, function (workflow) {
                    if (objectId != null && objectId != "" && objectId != undefined && objectId == workflow.id) {
                        vm.workflows.content.splice(vm.workflows.content.indexOf(workflow), 1);
                        existObjectId = true;
                    }
                    workflow.checked = false;
                });
                if (existObjectId) {
                    vm.workflows.totalElements = vm.workflows.totalElements - 1;
                    vm.workflows.numberOfElements = vm.workflows.numberOfElements - 1;
                }
            }

            function selectRadioChange(workflow, $event) {
                radioChange(workflow, $event);
                selectRadio();
            }

            function radioChange(workflow) {
                if (vm.selectedObj === workflow) {
                    workflow.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = workflow;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }
                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage(selectOneWorkflow);
                }
            }

            module.directive('autoFocus', ['$timeout', function ($timeout) {
                return {
                    restrict: 'A',
                    link: function ($scope, $element) {
                        $timeout(function () {
                            $element[0].focus();
                        });
                    }
                }
            }]);

            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.$on('app.select.workflow', selectRadio);
                loadWorkflows();
                //}
            })();
        }
    }
)
;

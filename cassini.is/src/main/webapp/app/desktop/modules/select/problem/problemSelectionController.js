/**
 * Created by swapna on 24/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/issue/issueService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/tm/taskService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('ProblemSelectionController', ProblemSelectionController);

        function ProblemSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore,
                                            IssueService, CommonService, TaskService) {

            var vm = this;

            vm.loading = true;
            vm.selectedObj = null;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                first: true,
                numberOfElements: 0
            };

            vm.problems = angular.copy(pagedResults);

            function loadProblems() {
                vm.clear = false;
                vm.loading = true;
                IssueService.getPageableIssues('PROJECT', '', pageable).then(
                    function (data) {
                        vm.problems = data;
                        CommonService.getPersonReferences(vm.problems.content, 'assignedTo');
                        TaskService.getTaskReferences(null, vm.problems.content, 'task');
                        angular.forEach(vm.problems.content, function (problem) {
                            problem.checked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.problems = [];
                    IssueService.freeSearch(pageable, freeText).then(
                        function (data) {
                            vm.problems = data;
                            vm.clear = true;
                            CommonService.getPersonReferences(vm.problems.content, 'assignedTo');
                            TaskService.getTaskReferences(null, vm.problems.content, 'task');
                            angular.forEach(vm.problems.content, function (problem) {
                                problem.checked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadProblems();
                }
            }

            function clearFilter() {
                loadProblems();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(problem, $event) {
                radioChange(problem, $event);
                selectRadio();
            }

            function radioChange(problem, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === problem) {
                    problem.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = problem;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage("Please select problem");
                }
            }

            function nextPage() {
                if (vm.problems.last != true) {
                    pageable.page++;
                    loadProblems();
                }
            }

            function previousPage() {
                if (vm.problems.first != true) {
                    pageable.page--;
                    loadProblems();
                }
            }

            (function () {
                $rootScope.$on('app.problem.selected', selectRadio);
                loadProblems();
            })();
        }
    }
)
;


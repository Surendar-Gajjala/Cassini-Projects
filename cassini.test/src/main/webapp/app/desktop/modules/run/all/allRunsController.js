define(
    [
        'app/desktop/modules/run/run.module',
        'app/shared/services/core/testRunService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('TestRunController', TestRunController);

        function TestRunController($scope, $rootScope, $timeout, $state, $cookies, DialogService, TestRunService) {

            $rootScope.viewInfo.icon = "fa fa-play";
            $rootScope.viewInfo.title = "Test Run";

            var vm = this;
            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.showRunDetails = showRunDetails;
            vm.loadTestRuns = loadTestRuns;
            vm.testcases = [];
            vm.deleteTestRun = deleteTestRun;
            $scope.importTestRuns = importTestRuns;
            vm.pageable = {
                page: 0,
                size: 8,
                sort: {
                    field: "createdDate",
                    order: "ASC"
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
            vm.testRuns = angular.copy(pagedResults);
            function nextPage() {
                if (vm.testRuns.last != true) {
                    vm.pageable.page++;
                    loadTestRuns();
                }
            }

            function previousPage() {
                if (vm.testRuns.first != true) {
                    vm.pageable.page--;
                    loadTestRuns();
                }
            }


            /* ----- Load Test Runs ----*/
            function loadTestRuns() {
                TestRunService.getTestRuns(vm.pageable).then(
                    function (data) {
                        vm.testRuns = data;
                    }
                )
            }

            /* ------ Method to show TestRunDetails ------*/
            function showRunDetails(testRun) {
                $state.go('app.run.details', {testRunId: testRun.id}
                );

            }

            /* ------ Delete Test Run -----*/
            function deleteTestRun(testRunId) {
                var options = {
                    title: 'Delete TestRun',
                    message: 'All related items should be deleted. Please confirm to delete.',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TestRunService.deleteTestRun(testRunId).then(
                            function (data) {
                                $rootScope.showSuccessMessage("TestRun deleted successfully");
                                loadTestRuns();

                            }
                        )
                    }


                })
            }

            /* ------ Import Test Run -----*/
            function importTestRuns() {
                var file = document.getElementById("file");
                if (vm.importFile = file.files[0]) {
                    $rootScope.showBusyIndicator();
                    TestRunService.importTestRuns(vm.importFile).then(
                        function (data) {
                            loadTestRuns();
                            document.getElementById("file").value = "";
                            $rootScope.showSuccessMessage("Imported successfully");
                            $rootScope.hideBusyIndicator();

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                            document.getElementById("file").value = "";
                        }
                    )
                }

            }


            (function () {
                loadTestRuns();
            })();
        }

    }
);

/**
 * Created by swapna on 12/08/18.
 */
define(
    [
        'app/desktop/modules/stores/store.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/store/loanService',
        'app/shared/services/store/topStoreService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'
    ],
    function (module) {
        module.controller('LoanReceivedBasicController', LoanReceivedBasicController);

        function LoanReceivedBasicController($scope, $rootScope, $timeout, $state, $stateParams, LoanService,
                                             CommonService, ProjectService, TopStoreService) {

            var vm = this;
            vm.back = back;
            vm.loan = null;
            vm.loading = true;
            vm.updateLoan = updateLoan;

            function loadLoan() {
                LoanService.getLoanById($rootScope.storeId, $stateParams.loanId).then(
                    function (data) {
                        vm.loan = data;
                        $rootScope.viewInfo.title = "Loan Receipt :" + vm.loan.loanNumber;
                        vm.loading = false;
                        CommonService.getPerson(vm.loan.createdBy).then(
                            function (person) {
                                vm.loan.issuedBy = person.fullName;
                            }
                        ).then(
                            ProjectService.getProject(vm.loan.fromProject).then(
                                function (project) {
                                    vm.loan.fromProjectName = project.name;
                                }
                            ).then(
                                ProjectService.getProject(vm.loan.toProject).then(
                                    function (project) {
                                        vm.loan.toProjectName = project.name;
                                    }
                                )
                            ).then(
                                TopStoreService.getTopStore(vm.loan.fromStore).then(
                                    function (store) {
                                        vm.loan.fromStore = store.storeName;
                                    }
                                )
                            )
                        );
                    }
                )
            }

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'LOANRECEIVE'});
            }

            function updateLoan() {
                LoanService.updateLoan($rootScope.storeId, vm.loan).then(
                    function (data) {
                        vm.loan = data;
                    });
            }

            function resize() {
                var height = $(window).height();
                var projectHeaderHeight = $("#project-headerbar").outerHeight();
                if (projectHeaderHeight != null) {
                    if ($application.selectedProject != undefined && $application.selectedProject.locked == true) {
                        $('#contentpanel').height(height - 297);
                    } else {
                        $('#contentpanel').height(height - 267);
                    }
                } else if (projectHeaderHeight == null) {
                    $('#contentpanel').height(height - 217);
                }
            }

            (function () {
                loadLoan();
                resize();
            })();
        }
    }
);
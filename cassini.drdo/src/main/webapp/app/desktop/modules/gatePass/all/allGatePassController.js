define(
    [
        'app/desktop/modules/gatePass/gatePass.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/shared/services/core/inwardService'
    ],
    function (module) {
        module.controller('AllGatePassController', AllGatePassController);

        function AllGatePassController($scope, $rootScope, $translate, $window, $timeout, $application, $state,
                                       $stateParams, $cookies, CommonService, DialogService, InwardService,
                                       AttributeAttachmentService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-sign-in";
            $rootScope.viewInfo.title = "Gate Passes";

            var vm = this;

            vm.loading = true;
            vm.clear = false;

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
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.gatePassFilter = {
                searchQuery: null,
                gatePassNumber: null,
                gatePassName: null,
                finish: false,
                fromDate: null,
                toDate: null,
                month: null
            };

            vm.gatePasses = angular.copy(pagedResults);

            vm.showNewGatePass = showNewGatePass;

            vm.showInward = showInward;

            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;
            vm.flag = false;
            vm.finishedInwardsView = false;
            vm.gatePassView = false;
            vm.finishGatePass = finishGatePass;
            vm.addFilter = addFilter;
            vm.getFilterResults = getFilterResults;
            vm.cancelFilter = cancelFilter;
            vm.errorMessage = null;
            vm.clearDateFilter = clearDateFilter;

            function clearDateFilter() {
                vm.errorMessage = null;
                vm.gatePassFilter.fromDate = null;
                vm.gatePassFilter.toDate = null;
                vm.gatePassFilter.month = null;
                vm.filterMode = false;
                loadGatePasses();
            }

            function cancelFilter() {
                var modal = document.getElementById("add-filter");
                modal.style.display = "none";
            }

            vm.filterMode = false;
            function getFilterResults() {
                vm.errorMessage = null;
                if ((vm.gatePassFilter.fromDate != null && vm.gatePassFilter.fromDate != "" && vm.gatePassFilter.fromDate != undefined) &&
                    (vm.gatePassFilter.toDate != null && vm.gatePassFilter.toDate != "" && vm.gatePassFilter.toDate != undefined)) {
                    vm.filterMode = true;
                    vm.gatePassFilter.month = null;
                    loadGatePasses();
                    var modal = document.getElementById("add-filter");
                    modal.style.display = "none";
                } else {
                    vm.filterMode = false;
                    vm.errorMessage = "Please select From Date and To Date";
                }
            }

            vm.getMonthResults = getMonthResults;
            function getMonthResults() {
                vm.errorMessage = null;
                if (vm.gatePassFilter.month != null && vm.gatePassFilter.month != "" && vm.gatePassFilter.month != undefined) {
                    vm.gatePassFilter.fromDate = null;
                    vm.gatePassFilter.toDate = null;
                    loadGatePasses();
                    vm.filterMode = true;
                    var modal = document.getElementById("add-filter");
                    modal.style.display = "none";
                } else {
                    vm.errorMessage = "Please select Month";
                }
            }

            function addFilter() {
                vm.errorMessage = null;
                vm.gatePassFilter.fromDate = null;
                vm.gatePassFilter.toDate = null;
                var modal = document.getElementById("add-filter");
                modal.style.display = "block";
            }

            function resetPage() {
                pageable.page = 0;
                vm.gatePassFilter.searchQuery = null;
            }

            function nextPage() {
                if (vm.gatePasses.last != true) {
                    pageable.page++;
                    loadGatePasses();
                }
            }

            function previousPage() {
                if (vm.gatePasses.first != true) {
                    pageable.page--;
                    loadGatePasses();
                }
            }

            function clearFilter() {
                loadGatePasses();
                vm.clear = false;
                $rootScope.showSearch = false;
            }

            function showNewGatePass() {
                var options = {
                    title: "New Gate Pass",
                    template: 'app/desktop/modules/inward/new/newGatePassView.jsp',
                    controller: 'NewGatePassController as newGatePassVm',
                    resolve: 'app/desktop/modules/inward/new/newGatePassController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.inwards.gatePass.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadGatePasses();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function showInward(inward) {
                $state.go('app.inwards.details', {inwardId: inward.id, mode: 'inwards'});
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != "" && freeText != undefined) {
                    $scope.freeTextQuery = freeText;
                    vm.gatePassFilter.searchQuery = freeText;
                    loadGatePasses();
                } else {
                    resetPage();
                    loadGatePasses();
                    $scope.freeTextQuery = null;
                    vm.gatePassFilter.searchQuery = null;
                }
            }

            vm.changeView = changeView;
            function changeView() {
                vm.errorMessage = null;
                vm.gatePassFilter.fromDate = null;
                vm.gatePassFilter.toDate = null;
                vm.gatePassFilter.finish = !vm.gatePassFilter.finish;
                if (vm.gatePassFilter.finish) {
                    $rootScope.viewInfo.title = "Finished Gate Passes";
                } else {
                    $rootScope.viewInfo.title = "Gate Passes";
                }
                loadGatePasses();
            }

            function loadGatePasses() {
                InwardService.getAllGatePasses(pageable, vm.gatePassFilter).then(
                    function (data) {
                        vm.gatePasses = data;
                        angular.forEach(vm.gatePasses.content, function (gatePass) {
                            var today = moment(new Date());
                            var todayStr = today.format('DD/MM/YYYY');
                            var todayDate = moment(todayStr, 'DD/MM/YYYY');

                            var startDate = moment(gatePass.createdDate, 'DD/MM/YYYY');

                            if (todayDate.isSame(startDate)) {
                                gatePass.showNew = true;
                            } else {
                                gatePass.showNew = false;
                            }
                        })
                        CommonService.getPersonReferences(vm.gatePasses.content, 'createdBy');
                        vm.loading = false;
                    }
                )
            }

            vm.downloadGatePass = downloadGatePass;
            function downloadGatePass(gatePass) {
                var url = "{0}//{1}/api/drdo/inwards/gatePass/{2}/{3}/download".
                    format(window.location.protocol, window.location.host,
                    gatePass.id, gatePass.gatePass.id);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = gatePass.gatePass.name;
                });
                /*window.open(url);
                $timeout(function () {
                    window.close();
                 }, 2000);*/
            }

            vm.deleteGatePass = deleteGatePass;
            function deleteGatePass(gatePass) {
                var options = {
                    title: "Delete GatePass",
                    message: "Please confirm to delete this GatePass",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        InwardService.deleteGatePass(gatePass).then(
                            function (data) {
                                loadGatePasses();
                                $rootScope.showSuccessMessage("GatePass deleted successfully");
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                })
            }

            function finishGatePass(gatePass) {
                InwardService.updateGatePass(gatePass).then(
                    function (data) {
                        loadGatePasses();
                        $rootScope.showSuccessMessage("GatePass updated successfully");
                    }
                )
            }

            vm.showGatePassItems = showGatePassItems;

            function showGatePassItems(gatePass) {
                var options = {
                    title: gatePass.gatePassNumber + " details",
                    template: 'app/desktop/modules/gatePass/all/gatePassDetailsView.jsp',
                    controller: 'GatePassDetailsController as gatePassDetailsVm',
                    resolve: 'app/desktop/modules/gatePass/all/gatePassDetailsController',
                    width: 700,
                    data: {
                        gatePassId: gatePass.id
                    },
                    buttons: [
                        {text: "Close", broadcast: 'app.gatePass.details'}
                    ],
                    callback: function (result) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    loadGatePasses();
                });
            })();
        }
    }
);
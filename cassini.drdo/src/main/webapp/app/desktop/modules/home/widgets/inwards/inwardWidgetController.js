define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/inwardService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService'
    ],
    function (module) {
        module.controller('InwardWidgetController', InwardWidgetController);

        function InwardWidgetController($scope, $rootScope, $timeout, $state, $stateParams, AttachmentService, $cookies,
                                        InwardService, CommonService) {
            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;

            vm.inwards = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.loading = true;
            vm.listStatus = ["STORE", "SSQAG", "STORAGE", "FINISH"];
            vm.clear = false;
            vm.applyFilters = applyFilters;
            vm.clearFilter = clearFilter;
            vm.downloadAttachment = downloadAttachment;

            vm.emptyFilters = {
                status: null,
                searchQuery: null,
                system: null,
                finalStatus: false
            };

            vm.filters = angular.copy(vm.emptyFilters);
            vm.showInward = showInward;


            function applyFilters() {
                vm.pageable.page = 0;
                loadInwards();
            }

            function clearFilter() {
                vm.pageable.page = 0;
                loadInwards();
                vm.clear = false;
            }

            vm.pageable = {
                page: 0,
                size: 6,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.pagedResults = {
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

            vm.inwards = angular.copy(vm.pagedResults);


            function nextPage() {
                vm.pageable.page++;
                loadInwards();
            }

            function previousPage() {
                vm.pageable.page--;
                loadInwards();
            }

            function downloadAttachment(gatePass) {
                var url = "{0}//{1}/api/drdo/inwards/gatePass/{2}/{3}/preview".
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

            function showInward(inward) {
                $state.go('app.inwards.details', {inwardId: inward.id, mode: 'home'});
            }

            function loadInwards() {
                InwardService.getAllInwards(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.inwards = data;
                        angular.forEach(vm.inwards.content, function (inward) {
                            inward.referenceItem = inward.refItem;
                            if (inward.status == "STORE_INTIAL" || inward.status == "STORE_FINAL") {
                                inward.status = "STORE";
                            }
                            inward.gPassFile = null;
                            if (inward.report != null) {
                                AttachmentService.getAttachment(inward.report).then(
                                    function (data) {
                                        inward.reportFile = data;
                                    }
                                );

                            }
                            CommonService.getPersonReferences(vm.inwards.content, 'receivedBy');
                        });
                        vm.loading = false;
                    }
                );
            }

            (function () {
                loadInwards();
            })();
        }

    });

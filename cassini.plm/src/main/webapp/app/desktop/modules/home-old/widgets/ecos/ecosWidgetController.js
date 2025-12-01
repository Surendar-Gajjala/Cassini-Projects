define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('EcoWidgetController', EcoWidgetController);

        function EcoWidgetController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, WorkflowService, ECOService) {
            var vm = this;

            vm.ecos = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.loading = true;
            vm.showEcoDetails = showEcoDetails;

            vm.pageable = {
                page: 0,
                size: 20,
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

            vm.ecos = angular.copy(vm.pagedResults);

            function nextPage() {
                vm.pageable.page++;
                loadEcos();
            }

            function previousPage() {
                vm.pageable.page--;
                loadEcos();
            }

            function loadEcos() {
                vm.clear = false;
                vm.loading = true;
                ECOService.findByReleasedTrue(vm.pageable).then(
                    function (data) {
                        vm.ecos = data;
                        angular.forEach(vm.ecos.content, function (obj) {
                            if (obj.createdDate) {
                                obj.createdDatede = moment(obj.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                            if (obj.releasedDate) {
                                obj.releasedDatede = moment(obj.releasedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                        });
                        WorkflowService.getWorkflowReferences(vm.ecos.content, 'workflow');
                        vm.loading = false;
                    }
                )
            }

            function showEcoDetails(eco) {
                $state.go("app.changes.eco.details", {ecoId: eco.id});
            }

            (function () {
                $timeout(function () {
                    loadEcos();
                });

            })();
        }

    });

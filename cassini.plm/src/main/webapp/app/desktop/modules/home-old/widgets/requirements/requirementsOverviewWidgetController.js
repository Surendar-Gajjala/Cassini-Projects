define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('RequirementsOverviewController', RequirementsOverviewController);

        function RequirementsOverviewController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, SpecificationsService) {
            var vm = this;

            vm.requirements = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.showItem = showItem;
            vm.loading = true;

            var pageable = {
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
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.items = angular.copy(vm.pagedResults);

            function nextPage() {
                vm.pageable.page++;
                loadRequirements();
            }

            function previousPage() {
                vm.pageable.page--;
                loadRequirements();
            }

            function showItem(requirement) {
                $state.go('app.rm.requirements.details', {requirementId: requirement.id});
            }

            /* function loadRequirements() {
             vm.clear = false;
             vm.loading = true;
             SpecificationsService.getAllRequirements(pageable).then(
             function (data) {
             vm.requirements = data;
             vm.loading = false;

             }
             )
             }*/

            (function () {
                // loadRequirements();
            })();
        }

    });

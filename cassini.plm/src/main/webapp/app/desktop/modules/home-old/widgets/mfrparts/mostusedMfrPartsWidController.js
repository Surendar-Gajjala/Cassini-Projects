define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('MostUsedMfrPartsWidController', MostUsedMfrPartsWidController);

        function MostUsedMfrPartsWidController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                               MfrPartsService, RecentlyVisitedService, ItemService) {
            var vm = this;

            vm.items = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.showMfrpartDetails = showMfrpartDetails;
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
                loadMfrParts();
            }

            function previousPage() {
                vm.pageable.page--;
                loadMfrParts();
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            function showMfrpartDetails(mfrpart) {
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                $state.go('app.mfr.mfrparts.details', {mfrId: mfrpart.manufacturer, manufacturePartId: mfrpart.id});
                vm.recentlyVisited.objectId = mfrpart.id;
                vm.recentlyVisited.objectType = mfrpart.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }
                )
            }

            function loadMfrParts() {
                vm.clear = false;
                vm.loading = true;
                MfrPartsService.getMostUsedMfrParts(pageable).then(
                    function (data) {
                        vm.items = data;
                        MfrPartsService.getManufacturerReference(vm.items.content, 'manufacturer');
                        vm.loading = false;
                    })
            }

            (function () {
                loadMfrParts();
            })();
        }

    });

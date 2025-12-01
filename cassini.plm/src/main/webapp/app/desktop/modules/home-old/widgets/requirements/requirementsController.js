define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('RequirementsController', RequirementsController);

        function RequirementsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, RecentlyVisitedService, SpecificationsService) {
            var vm = this;

            vm.requirements = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.showItem = showItem;
            vm.loading = true;
            var owner = null;

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
            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            $rootScope.loginPersonDetails = session.login;
            var personId = $rootScope.loginPersonDetails;

            function nextPage() {
                vm.pageable.page++;
                loadRequirements();
            }

            function previousPage() {
                vm.pageable.page--;
                loadRequirements();
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            function showItem(requirement) {
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                $state.go('app.rm.requirements.details', {requirementId: requirement.id});
                vm.recentlyVisited.objectId = requirement.id;
                vm.recentlyVisited.objectType = requirement.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }
                )
            }

            function loadRequirements() {
                vm.clear = false;
                vm.loading = true;
                SpecificationsService.getAllRequirements(owner, pageable).then(
                    function (data) {
                        vm.requirements = data;
                        vm.loading = false;

                    }
                )
            }

            (function () {
                $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
                owner = $rootScope.localStorageLogin.login.person.id;
                loadRequirements();
            })();
        }

    });

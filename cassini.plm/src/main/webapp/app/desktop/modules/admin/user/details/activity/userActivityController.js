define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/activityStreamService'
    ],
    function (module) {
        module.controller('UserActivityController', UserActivityController);

        function UserActivityController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                        $state, $stateParams, $cookies, ActivityStreamService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            var userId = $stateParams.userId;
            vm.activities = [
                {timestamp: "12/20/20202 10:38 AM"},
                {timestamp: "12/20/20202 10:38 AM"},
                {timestamp: "12/20/20202 10:38 AM"},
                {timestamp: "12/20/20202 10:38 AM"}
            ];
            vm.pageable = {
                page: 0,
                size: 30,
                sort: {
                    field: "timestamp",
                    order: "DESC"
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

            vm.pagenatedResults = {
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
            vm.filters = {
                searchQuery: null
            };
            vm.activities = angular.copy(pagedResults);

            vm.activityStreamFilter = {
                objectId: '',
                date: null,
                user: userId
            };

            function loadUserActivity() {
                ActivityStreamService.getActivityStreamByObjectId(userId, vm.pageable, vm.activityStreamFilter).then(
                    function (data) {
                        vm.pagenatedResults = data;
                        vm.activities = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.loadMoreUserActivity = loadMoreUserActivity;
            function loadMoreUserActivity() {
                $rootScope.showBusyIndicator();
                vm.pageable.page++;
                ActivityStreamService.getActivityStreamByObjectId(userId, vm.pageable, vm.activityStreamFilter).then(
                    function (data) {
                        vm.pagenatedResults = data;
                        Array.prototype.push.apply(vm.activities.content, data.content);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadUserActivity();
            })();
        }
    }
);
define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/issueService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('IssueWidgetController', IssueWidgetController);

        function IssueWidgetController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, IssueService) {

            var vm = this;

            vm.loading = true;
            vm.issues = [];
            vm.pageable = {
                page: 0,
                size: 6,
                sort: {
                    field: "createdDate",
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

            vm.issues = angular.copy(vm.pagedResults);
            function loadIssues() {
                vm.loading = false;
            }


            (function () {
                loadIssues();
            })();
        }
    }
);

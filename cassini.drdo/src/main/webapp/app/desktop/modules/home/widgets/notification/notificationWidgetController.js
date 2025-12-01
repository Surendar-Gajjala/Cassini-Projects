define(
    [
        'app/desktop/modules/home/home.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService'
    ],
    function (module) {
        module.controller('NotificationWidgetController', NotificationWidgetController);

        function NotificationWidgetController($scope, $rootScope, $timeout, $state, $stateParams, AttachmentService, $cookies,
                                              InwardService, CommonService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;

            vm.loading = true;

            vm.pageable = {
                page: 0,
                size: 10,
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

            vm.notifications = angular.copy(vm.pagedResults);


            function nextPage() {
                vm.pageable.page++;
                loadNotifications();
            }

            function previousPage() {
                vm.pageable.page--;
                loadNotifications();
            }

            function loadNotifications() {

            }


            (function () {
                loadNotifications();
            })();
        }

    });

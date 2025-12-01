define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/core/dispatchService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService'
    ],
    function (module) {
        module.controller('DispatchWidgetController', DispatchWidgetController);

        function DispatchWidgetController($scope, $rootScope, $timeout, $state, $stateParams, AttachmentService, $cookies,
                                          DispatchService, CommonService) {
            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.showDispatchDetails = showDispatchDetails;

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

            vm.dispatches = angular.copy(vm.pagedResults);


            function nextPage() {
                vm.pageable.page++;
                loadDispatches();
            }

            function previousPage() {
                vm.pageable.page--;
                loadDispatches();
            }

            function loadDispatches() {
                DispatchService.getAllDispatches(vm.pageable).then(
                    function (data) {
                        vm.dispatches = data;
                        CommonService.getPersonReferences(vm.dispatches.content, 'createdBy');
                        vm.loading = false;
                    }
                )
            }

            function showDispatchDetails(dispatch) {
                var button = null;
                if (dispatch.status == "NEW") {
                    button = {text: "Update", broadcast: 'app.dispatch.details'};
                } else {
                    button = {text: "Close", broadcast: 'app.dispatch.close'}
                }

                var options = {
                    title: dispatch.number + " Details",
                    template: 'app/desktop/modules/dispatch/details/dispatchDetailsView.jsp',
                    controller: 'DispatchDetailsController as dispatchDetailsVm',
                    resolve: 'app/desktop/modules/dispatch/details/dispatchDetailsController',
                    width: 700,
                    showMask: true,
                    data: {
                        dispatchDetails: dispatch
                    },
                    buttons: [
                        button
                    ],
                    callback: function (result) {
                        loadDispatches();
                    }
                };

                $rootScope.showSidePanel(options);

            }

            (function () {
                loadDispatches();
            })();
        }

    });

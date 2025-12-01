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
        module.controller('GatePassDetailsController', GatePassDetailsController);

        function GatePassDetailsController($scope, $rootScope, $translate, $window, $timeout, $application, $state,
                                           $stateParams, $cookies, CommonService, DialogService, InwardService,
                                           AttributeAttachmentService) {
            if ($application.homeLoaded == false) {
                return;
            }

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

            function loadGatePassDetails() {
                InwardService.getGatePassDetails($scope.data.gatePassId).then(
                    function (data) {
                        vm.gatePassDetails = data;
                        vm.loading = false;
                    }
                )
            }

            function close() {
                $rootScope.hideSidePanel();
            }


            (function () {
                loadGatePassDetails();
                $scope.$on('app.gatePass.details', close);
            })();
        }
    }
);
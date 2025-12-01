/**
 * Created by swapna on 22/01/19.
 */
define(['app/desktop/modules/subContracts/contracts.module',
        'app/shared/services/core/subContractService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ContractorDetailsController', ContractorDetailsController);

        function ContractorDetailsController($scope, $rootScope, $timeout, $state, $cookies,
                                             SubContractService, LoginService, CommonService) {

            var vm = this;
            vm.contractor = $scope.data.contractor;
            vm.updatedContractor = null;
            vm.updateContractor = updateContractor;
            vm.flags = [{name: "True", value: true}, {name: "False", value: false}];

            function loadLogins() {
                LoginService.getActiveLogins().then(
                    function (data) {
                        vm.logins = data;
                    }
                )
            }

            function updateContractor() {
                SubContractService.updateContractor(vm.contractor).then(
                    function (data) {
                        vm.updatedContractor = data;
                        CommonService.getPerson(vm.contractor.contact).then(
                            function (data) {
                                $rootScope.showSuccessMessage(vm.contractor.name + " updated successfully");
                                vm.updatedContractor.contactObject = data;
                                vm.contractor.contactObject = data;
                            }
                        );
                        CommonService.getPerson(vm.contractor.createdBy).then(
                            function (data) {
                                vm.updatedContractor.createdByObject = data;
                            }
                        );
                        CommonService.getPerson(vm.contractor.modifiedBy).then(
                            function (data) {
                                vm.updatedContractor.modifiedByObject = data;
                            }
                        );
                        $scope.callback(vm.updatedContractor);
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadLogins();
            })();
        }
    }
);
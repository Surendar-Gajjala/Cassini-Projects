/**
 * Created by swapna on 19/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/customIndentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'

    ],
    function (module) {
        module.controller('IndentBasicDetailsController', IndentBasicDetailsController);

        function IndentBasicDetailsController($scope, $rootScope, $window, $state, $stateParams, CustomIndentService) {

            var vm = this;
            vm.indent = null;
            vm.update = update;
            vm.flags = [{
                name: "True",
                value: true
            },
                {
                    name: "False",
                    value: false
                }
            ];

            function update() {
                CustomIndentService.updateIndent(vm.indent).then(
                    function (data) {
                        $rootScope.indent = data;
                    });
            }

            function loadIndent() {
                CustomIndentService.getIndent($stateParams.indentId).then(
                    function (data) {
                        vm.indent = data;
                    }
                )
            }

            function back() {
                $window.history.back();
            }

            function validateIndent() {
                var valid = true;
                if (vm.indent.approvedBy == null || vm.indent.approvedBy == undefined || vm.indent.approvedBy == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Approved by");
                }

                return valid;
            }

            function approveIndent() {
                if (validateIndent()) {
                    vm.indent.status = 'APPROVED';
                    CustomIndentService.updateIndent(vm.indent).then(
                        function (data) {
                            $rootScope.indent = data;
                            $rootScope.showSuccessMessage("Indent (" + data.indentNumber + ") approved successfully");
                        }, function (error) {

                        });
                }
            }

            (function () {
                loadIndent();
                $scope.$on('app.indent.approve', approveIndent);
            })();
        }
    }
)
;
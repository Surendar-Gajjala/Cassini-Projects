define(
    [
        'app/desktop/modules/main/main.module',
        'app/shared/services/core/printService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/main/sidePanelsController'
    ],
    function (module) {
        module.controller('PrintOptionsPanelController', PrintOptionsPanelController, ['filterFilter']);

        function PrintOptionsPanelController($scope, filterFilter, $rootScope, $translate, $timeout, $state, $window, $stateParams, $cookies, $sce,
                                             PrintService) {

            var vm = this;
            $scope.options = $scope.data.printOptions;
            vm.objectId = $scope.data.objectId;
            vm.objectType = $scope.data.objectType;
            $scope.selectedOption = [];

            /*
             * Filter Option selection
             * */
            module.filter('optionSelection', ['filterFilter', function (optionFilter) {
                return function optionSelection(input, prop) {
                    return filterFilter(input, {selected: true}).map(function (option) {
                        return option[prop];
                    });
                };
            }]);

            // helper method for select default options
            $scope.selectedOptions = function selectedOptions() {
                return filterFilter(options, {selected: true});
            };

            // watch option for changes
            $scope.$watch('options|filter:{selected:true}', function (nv) {
                $scope.selectedOption = nv.map(function (option) {
                    return option.name;
                });
            }, true);
            vm.print = {
                objectId: vm.objectId,
                objectType: vm.objectType,
                options: $scope.selectedOption
            }

            function previewPrint() {
                if ($scope.selectedOption.length > 0) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.print.options = $scope.selectedOption;
                    PrintService.printPreview("HTML", vm.print).then(
                        function (data) {
                            var url = "{0}//{1}/api/plm/print/file/".format(window.location.protocol, window.location.host);
                            url += data + "/preview";
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel();
                            $scope.callback(url);

                        }, function (error) {
                            $rootScope.showWarningMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    $rootScope.showWarningMessage($translate.instant("SELECT_TAB_PRINT_VALIDATION"));
                }

            }

            (function () {
                $rootScope.hideBusyIndicator();
                $rootScope.$on('app.print.options', previewPrint);
            })();
        }
    }
)
;

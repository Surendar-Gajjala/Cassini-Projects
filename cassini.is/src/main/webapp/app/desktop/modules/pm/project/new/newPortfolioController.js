define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/pm/project/projectService'
    ],
    function (module) {
        module.controller('PortfolioController', PortfolioController);

        function PortfolioController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                     ProjectService) {
            $rootScope.viewInfo.icon = "fa fa-home";

            var vm = this;
            var portFolioMap = $scope.data.portFolioMap;
            var mode = $scope.data.modeofObject;

            if (mode == "NEW") {
                vm.portfolio = {
                    name: null,
                    description: ""
                };
            } else {
                vm.portfolio = angular.copy($scope.data.portfolioObject);
            }

            function validate() {
                vm.valid = true;

                if (mode == "NEW") {
                    if (vm.portfolio.name == null || vm.portfolio.name == undefined || vm.portfolio.name == "") {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Name cannot be empty");
                    }
                    else if (portFolioMap.get(vm.portfolio.name) != null) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("{0} Name already exists".format(vm.portfolio.name));
                    }
                } else {
                    if (vm.portfolio.name == null || vm.portfolio.name == undefined || vm.portfolio.name == "") {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Name cannot be empty");
                    }
                }
                return vm.valid;
            }

            function create() {
                if (mode == "NEW") {
                    if (validate()) {
                        $rootScope.showBusyIndicator($("#rightSidePanel"));

                        ProjectService.createPortfolio(vm.portfolio).then(
                            function (data) {
                                vm.portfolio = data;
                                $rootScope.hideSidePanel('right');
                                $scope.callback();
                                vm.creating = false;
                                $rootScope.hideBusyIndicator();
                                $rootScope.showSuccessMessage("Portfolio created successfully");
                            }, function (err) {
                                $rootScope.hideBusyIndicator();
                            }
                        )

                    }
                } else {
                    if (validate()) {
                        ProjectService.updatePortfolio(vm.portfolio).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Portfolio updated successfully");
                                $rootScope.hideSidePanel();
                                $scope.callback();

                            }
                        )
                    }
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.portfolio.new', create);
                }
            })();
        }
    }
);
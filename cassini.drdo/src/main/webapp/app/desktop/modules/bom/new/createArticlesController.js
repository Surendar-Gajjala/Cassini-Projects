define(['app/desktop/modules/bom/bom.module'
    ],
    function (module) {
        module.controller('CreateArticlesController', CreateArticlesController);

        function CreateArticlesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                          $uibModalInstance) {

            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;

            vm.generate = generate;
            vm.cancel = cancel;
            vm.quantity = null;
            $scope.instances = [];
            var instance = {
                name: null
            };
            vm.addInstance = addInstance;

            function addInstance() {
                instance = {
                    name: null
                };
                $scope.instances.push(instance);
            }

            function generate() {
                if (validateInstance()) {
                    var articles = [];
                    angular.forEach($scope.instances, function (inst) {
                        if (inst.name != "" && inst.name != null && inst.name != undefined) {
                            articles.push(inst.name.toUpperCase());
                        }
                    });
                    if (articles.length > 0) {
                        $uibModalInstance.close(articles);
                    } else {
                        $uibModalInstance.close();
                        $rootScope.showInfoMessage("You are not entered any Articles");
                    }
                }

            }

            vm.errorMessage = null;

            function validateInstance() {
                var valid = true;

                angular.forEach($scope.instances, function (inst) {
                    if (valid) {
                        if (inst.name == "" && inst.name == null && inst.name == undefined) {
                            valid = false;
                            vm.errorMessage = "Please enter Instance Name";
                        } else if (inst.name != null && inst.name.length != 2) {
                            valid = false;
                            vm.errorMessage = "Please enter 2 digits or character Instance Name";
                        }
                    }
                });

                return valid;
            }

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            (function () {
                if ($application.homeLoaded == true) {
                    addInstance();
                    $timeout(function () {
                        $('#count').focus();
                    }, 500);
                }
            })();
        }
    }
)


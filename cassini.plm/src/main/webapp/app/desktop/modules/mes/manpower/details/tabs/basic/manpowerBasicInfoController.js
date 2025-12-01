define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/instrumentService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ManpowerBasicInfoController', ManpowerBasicInfoController);

        function ManpowerBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                             ManpowerService, $translate, QualityTypeService, LoginService) {
            var vm = this;
            vm.loading = true;
            vm.manpowerId = $stateParams.manpowerId;
            vm.manpower = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateManpower = updateManpower;

            function loadManpowerBasicDetails() {
                vm.loading = true;
                if (vm.manpowerId != null && vm.manpowerId != undefined) {
                    ManpowerService.getManpower(vm.manpowerId).then(
                        function (data) {
                            vm.manpower = data;
                            $rootScope.manpower = vm.manpower;
                            $scope.name = vm.manpower.name;
                            CommonService.getPersonReferences([vm.manpower], 'modifiedBy');
                            CommonService.getPersonReferences([vm.manpower], 'createdBy');
                            CommonService.getPersonReferences([vm.manpower], 'person');
                            if (vm.manpower.description != null && vm.manpower.description != undefined) {
                                vm.manpower.descriptionHtml = $sce.trustAsHtml(vm.manpower.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            $rootScope.viewInfo.title = $translate.instant("MANPOWER_DETAILS");
                            $rootScope.viewInfo.description = vm.manpower.number + " , " + vm.manpower.name;
                            vm.loading = false;
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var manpowertManagerValidation = parsed.html($translate.instant("MANPOWER_MANAGER_VALIDATION")).html();
            var enterValidEmail = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var manpowerUpdated = parsed.html($translate.instant("MANPOWER_UPDATED")).html();


            function validateManpower() {
                var valid = true;
                if (vm.manpower.name == null || vm.manpower.name == ""
                    || vm.manpower.name == undefined) {
                    valid = false;
                    vm.manpower.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }


                return valid;
            }

            function updateManpower() {
                if (validateManpower()) {
                    ManpowerService.updateManpower(vm.manpower).then(
                        function (data) {
                            vm.manpower = data;
                            $scope.name = data.name;
                            CommonService.getPersonReferences([vm.manpower], 'modifiedBy');
                            CommonService.getPersonReferences([vm.manpower], 'createdBy');
                            CommonService.getPersonReferences([vm.manpower], 'person');
                            if (vm.manpower.description != null && vm.manpower.description != undefined) {
                                vm.manpower.descriptionHtml = $sce.trustAsHtml(vm.manpower.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            vm.editStatus = false;
                            // loadPlantBasicDetails();
                            $rootScope.viewInfo.description = vm.manpower.number + " , " + vm.manpower.name;
                            $rootScope.showSuccessMessage(manpowerUpdated);

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var active = parsed.html($translate.instant("C_ACTIVE")).html();
            var inActive = parsed.html($translate.instant("C_INACTIVE")).html();
            vm.editStatus = false;
            vm.statuses = [
                {label: active, value: true},
                {label: inActive, value: false}
            ]

            vm.changeStatus = changeStatus;
            function changeStatus() {
                vm.editStatus = true;
                vm.oldStatus = vm.manpower.active;
            }

            vm.cancelStatus = cancelStatus;
            function cancelStatus() {
                vm.editStatus = false;
                vm.manpower.active = vm.oldStatus;
            }


            (function () {
                $scope.$on('app.manpower.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadPersons();
                        loadManpowerBasicDetails();
                    }
                });

            })();

        }
    }
);
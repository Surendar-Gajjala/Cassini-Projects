define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/plantService',
        'app/shared/services/core/assetService'
    ],
    function (module) {
        module.controller('PlantBasicInfoController', PlantBasicInfoController);

        function PlantBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, CommonService,
                                          $translate, PlantService, LoginService, AssetService) {
            var vm = this;
            vm.loading = true;
            vm.plantId = $stateParams.plantId;
            vm.plant = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updatePlant = updatePlant;
            function loadPlantBasicDetails() {
                vm.loading = true;
                if (vm.plantId != null && vm.plantId != undefined) {
                    PlantService.getPlant(vm.plantId).then(
                        function (data) {
                            vm.plant = data;
                            $rootScope.plant = data;
                            $scope.name = vm.plant.name;
                            $scope.email = vm.plant.email;
                            $scope.plantPerson = vm.plant.plantPerson;
                            CommonService.getPersonReferences([vm.plant], 'createdBy');
                            CommonService.getPersonReferences([vm.plant], 'modifiedBy');
                            CommonService.getPersonReferences([vm.plant], 'plantPerson');
                            if (vm.plant.description != null && vm.plant.description != undefined) {
                                vm.plant.descriptionHtml = $sce.trustAsHtml(vm.plant.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            if (vm.plant.notes != null && vm.plant.notes != undefined) {
                                vm.plant.notesHtml = $sce.trustAsHtml(vm.plant.notes.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            vm.loading = false;
                            $rootScope.viewInfo.title = $translate.instant("PLANT_DETAILS");
                            $rootScope.viewInfo.description = vm.plant.number + " , " + vm.plant.name;
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var plantManagerValidation = parsed.html($translate.instant("PLANT_MANAGER_VALIDATION")).html();
            var enterValidEmail = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var updatedSuccessMsg = parsed.html($translate.instant("PLANT_UPDATED_SUCCESS_MESSAGE")).html();
            var plantDetails = parsed.html($translate.instant("PLANT_DETAILS")).html();

            function validateEmail(email) {
                var valid = true;
                var atpos = email.indexOf("@");
                var dotpos = email.lastIndexOf(".");
                if (email != null && email != undefined && email != "") {
                    if (atpos < 1 || ( dotpos - atpos < 2 )) {
                        valid = false;
                    }
                }
                return valid
            }

            function validatePlant() {
                var valid = true;
                if (vm.plant.name == null || vm.plant.name == ""
                    || vm.plant.name == undefined) {
                    valid = false;
                    vm.plant.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }
                else if (vm.plant.email != null && !validateEmail(vm.plant.email)) {
                    valid = false;
                    vm.plant.email = $scope.email;
                    $rootScope.showWarningMessage(enterValidEmail);
                }

                else if (vm.plant.plantPerson == null || vm.plant.plantPerson == ""
                    || vm.plant.plantPerson == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(plantManagerValidation);

                }

                return valid;
            }

            function updatePlant() {
                if (validatePlant()) {
                    vm.plant.plantPerson = vm.plant.plantPersonObject.id;
                    PlantService.updatePlant(vm.plant).then(
                        function (data) {
                            vm.plant = data;
                            $scope.name = data.name;
                            if (vm.plant.description != null && vm.plant.description != undefined) {
                                vm.plant.descriptionHtml = $sce.trustAsHtml(vm.plant.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            if (vm.plant.notes != null && vm.plant.notes != undefined) {
                                vm.plant.notesHtml = $sce.trustAsHtml(vm.plant.notes.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            CommonService.getPersonReferences([vm.plant], 'createdBy');
                            CommonService.getPersonReferences([vm.plant], 'modifiedBy');
                            CommonService.getPersonReferences([vm.plant], 'plantPerson');
                            if (vm.plant.createdDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.plant.createDateDe = moment(vm.plant.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.plant.createDateDe = vm.plant.createdDate;
                                }

                            }
                            if (vm.plant.modifiedDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.plant.modifiedDateDe = moment(vm.plant.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.plant.modifiedDateDe = vm.plant.modifiedDate;
                                }
                            }
                            vm.editMaintenance = false;
                            // loadPlantBasicDetails();
                            $rootScope.viewInfo.description = vm.plant.number + " , " + vm.plant.name;
                            $rootScope.showSuccessMessage(updatedSuccessMsg);
                            loadPlantAssets();
                        }, function (error) {
                            vm.plant.name = $scope.name;
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

            var yes = parsed.html($translate.instant("YES")).html();
            var no = parsed.html($translate.instant("NO")).html();

            vm.editMaintenance = false;
            vm.maintenances = [
                {label: yes, value: true},
                {label: no, value: false}
            ];

            vm.changeMaintenance = changeMaintenance;
            function changeMaintenance() {
                if (vm.assets.length == 0) {
                    vm.editMaintenance = true;
                    vm.oldMaintenane = vm.plant.requiresMaintenance;
                }
            }

            vm.cancelMaintenance = cancelMaintenance;
            function cancelMaintenance() {
                vm.editMaintenance = false;
                vm.plant.requiresMaintenance = vm.oldMaintenane;
            }

            function loadPlantAssets() {
                AssetService.getAssetsByResource(vm.plantId).then(
                    function (data) {
                        vm.assets = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $scope.$on('app.plant.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadPersons();
                        loadPlantBasicDetails();
                        loadPlantAssets();
                    }
                });

            })();

        }
    }
);
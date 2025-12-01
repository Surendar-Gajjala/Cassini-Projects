define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/shared/services/core/specificationService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/directives/mes-mfr-data/mesMfrDataDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('SpecificationBasicInfoController', SpecificationBasicInfoController);

        function SpecificationBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, CommonService,
                                                  SpecificationService, $translate, LoginService, DialogService) {
            var vm = this;
            vm.loading = true;
            vm.specificationId = $stateParams.specificationId;
            vm.specification = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateSpecification = updateSpecification;

            function loadSpecificationBasicDetails() {
                vm.loading = true;
                if (vm.specificationId != null && vm.specificationId != undefined) {
                    SpecificationService.getSpecification(vm.specificationId).then(
                        function (data) {
                            vm.specification = data;
                            $rootScope.specification = vm.specification;
                            $scope.name = vm.specification.name;
                            CommonService.getPersonReferences([vm.specification], 'modifiedBy');
                            CommonService.getPersonReferences([vm.specification], 'createdBy');
                            if (vm.specification.createdDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.specification.createDateDe = moment(vm.specification.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.specification.createDateDe = vm.specification.createdDate;
                                }

                            }
                            if (vm.specification.modifiedDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.specification.modifiedDateDe = moment(vm.specification.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.specification.modifiedDateDe = vm.specification.modifiedDate;
                                }
                            }
                            if (vm.specification.description != null && vm.specification.description != undefined) {
                                vm.specification.descriptionHtml = $sce.trustAsHtml(vm.specification.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            if (vm.specification.image != null) {
                                vm.specification.imagePath = "api/compliance/specifications/" + vm.specificationId + "/image/download?" + new Date().getTime();
                            }
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            vm.loading = false;
                            vm.editStatus = false;
                            // $rootScope.viewInfo.title = $translate.instant("SPECIFICATION_DETAILS");
                            // $rootScope.viewInfo.description = vm.specification.number + " , " + vm.specification.name;
                            $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div>".
                                format(vm.specification.number);
                        $rootScope.viewInfo.description = vm.specification.name;
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }


            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var specificationUpdated = parsed.html($translate.instant("SPECIFICATION_UPDATE_MSG")).html();


            function validateSpecification() {
                var valid = true;
                if (vm.specification.name == null || vm.specification.name == ""
                    || vm.specification.name == undefined) {
                    valid = false;
                    vm.specification.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }


                return valid;
            }

            function updateSpecification() {
                if (validateSpecification()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    SpecificationService.updateSpecification(vm.specification).then(
                        function (data) {
                            loadSpecificationBasicDetails();
                            vm.editMaintenance = false;
                            vm.editStatus = false;
                            $rootScope.showSuccessMessage(specificationUpdated);
                            $rootScope.hideBusyIndicator();
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
                    }
                )
            }

            var active = parsed.html($translate.instant("C_ACTIVE")).html();
            var inActive = parsed.html($translate.instant("C_INACTIVE")).html();
            vm.editStatus = false;
            vm.statuses = [
                {label: active, value: true},
                {label: inActive, value: false}
            ];

            vm.changeStatus = changeStatus;
            function changeStatus() {
                vm.editStatus = true;
                vm.oldStatus = vm.specification.active;
            }

            vm.cancelStatus = cancelStatus;
            function cancelStatus() {
                vm.editStatus = false;
                vm.specification.active = vm.oldStatus;
            }

            (function () {
                $scope.$on('app.specification.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadPersons();
                        loadSpecificationBasicDetails();
                    }
                });

            })();

        }
    }
);
define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/operationService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/workCenterService'
    ],
    function (module) {
        module.controller('OperationBasicInfoController', OperationBasicInfoController);

        function OperationBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                              MESObjectTypeService, $translate, LoginService, OperationService,WorkCenterService) {
            var vm = this;
            vm.loading = true;
            vm.operationId = $stateParams.operationId;
            vm.operation = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateOperation = updateOperation;

            function loadOperationBasicDetails() {
                vm.loading = true;
                if (vm.operationId != null && vm.operationId != undefined) {
                    OperationService.getOperation(vm.operationId).then(
                        function (data) {
                            vm.operation = data;
                            $rootScope.operation = vm.operation;
                            $scope.name = vm.operation.name;
                            CommonService.getPersonReferences([vm.operation], 'modifiedBy');
                            CommonService.getPersonReferences([vm.operation], 'createdBy');
                            if (vm.operation.createdDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.operation.createDateDe = moment(vm.operation.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.operation.createDateDe = vm.operation.createdDate;
                                }

                            }
                            if (vm.operation.modifiedDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.operation.modifiedDateDe = moment(vm.operation.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.operation.modifiedDateDe = vm.operation.modifiedDate;
                                }
                            }
                            if (vm.operation.description != null && vm.operation.description != undefined) {
                                vm.operation.descriptionHtml = $sce.trustAsHtml(vm.operation.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            $rootScope.viewInfo.title = $translate.instant("OPERATION_DETAILS");
                            $rootScope.viewInfo.description = vm.operation.number + " , " + vm.operation.name;
                            vm.loading = false;
                            $scope.$evalAsync();
                            loadWorkCenter();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var operationManagerValidation = parsed.html($translate.instant("OPERATION_MANAGER_VALIDATION")).html();
            var enterValidEmail = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();


            function validateOperation() {
                var valid = true;
                if (vm.operation.name == null || vm.operation.name == ""
                    || vm.operation.name == undefined) {
                    valid = false;
                    vm.operation.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }


                return valid;
            }

            var operationUpdated = parsed.html($translate.instant("OPERATION_UPDATED")).html();

            function updateOperation() {
                if (validateOperation()) {
                    OperationService.updateOperation(vm.operation).then(
                        function (data) {
                            vm.operation = data;
                            $scope.name = data.name;
                            CommonService.getPersonReferences([vm.operation], 'modifiedBy');
                            CommonService.getPersonReferences([vm.operation], 'createdBy');
                            if (vm.operation.description != null && vm.operation.description != undefined) {
                                vm.operation.descriptionHtml = $sce.trustAsHtml(vm.operation.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }

                            if (vm.operation.createdDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.operation.createDateDe = moment(vm.operation.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.operation.createDateDe = vm.operation.createdDate;
                                }

                            }
                            if (vm.operation.modifiedDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.operation.modifiedDateDe = moment(vm.operation.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.operation.modifiedDateDe = vm.operation.modifiedDate;
                                }
                            }

                            $rootScope.showSuccessMessage(operationUpdated); 

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

            vm.showWorkCenter = showWorkCenter;
            function showWorkCenter(operation) {
                $state.go('app.mes.masterData.workcenter.details', {
                    workcenterId: operation.workCenterId,
                    tab: 'details.basic'
                });
            }

            function loadWorkCenters() {
                WorkCenterService.getWorkCenters().then(
                    function (data) {
                        vm.workCenters = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadWorkCenter() {
                WorkCenterService.getWorkCenter(vm.operation.workCenter).then(
                    function (data) {
                        vm.operation.workCenterObject = data;
                    })
            }

            (function () {
                $scope.$on('app.operation.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadPersons();
                        loadOperationBasicDetails();
                        loadWorkCenters();
                    }
                });

            })();

        }
    }
);
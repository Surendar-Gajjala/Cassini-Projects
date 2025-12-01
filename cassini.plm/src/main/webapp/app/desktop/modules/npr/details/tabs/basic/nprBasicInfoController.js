define(
    [
        'app/desktop/modules/npr/npr.module',
        'app/shared/services/core/nprService'
    ],
    function (module) {
        module.controller('NprBasicInfoController', NprBasicInfoController);

        function NprBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, LoginService, NprService, CommonService, $translate) {
            var vm = this;
            vm.loading = true;
            vm.nprId = $stateParams.nprId;
            vm.npr = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateNpr = updateNpr;

            $rootScope.loadNprBasicDetails = loadNprBasicDetails;
            function loadNprBasicDetails() {
                vm.loading = true;
                if (vm.nprId != null && vm.nprId != undefined) {
                    NprService.getNpr(vm.nprId).then(
                        function (data) {
                            vm.npr = data;
                            $rootScope.npr = vm.npr;
                            if (vm.npr.status == 'APPROVED' || vm.npr.status == 'REJECTED') {
                                vm.status = true;
                            } else {
                                vm.status = false;
                            }

                            CommonService.getMultiplePersonReferences([vm.npr], ['requester', 'createdBy', 'modifiedBy']);
                            if (vm.npr.description != null && vm.npr.description != undefined) {
                                vm.npr.descriptionHtml = $sce.trustAsHtml(vm.npr.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            vm.loading = false;
                            vm.editStatus = false;
                            $rootScope.viewInfo.title = parsed.html($translate.instant("NEW_PART_DETAILS")).html();
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var nprUpdatedSuccess = parsed.html($translate.instant("NPR_UPDATED_SUCCESS")).html();


            function validateNpr() {
                var valid = true;
                if (vm.npr.name == null || vm.npr.name == ""
                    || vm.npr.name == undefined) {
                    valid = false;
                    vm.npr.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }
                return valid;
            }

            function updateNpr() {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.npr.requester = vm.npr.requesterObject.id;
                NprService.updateNpr(vm.npr).then(
                    function (data) {
                        loadNprBasicDetails();
                        $rootScope.showSuccessMessage(nprUpdatedSuccess);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

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
                );
            }


            (function () {
                $scope.$on('app.npr.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadPersons();
                        loadNprBasicDetails();
                    }
                });

            })();

        }
    }
);
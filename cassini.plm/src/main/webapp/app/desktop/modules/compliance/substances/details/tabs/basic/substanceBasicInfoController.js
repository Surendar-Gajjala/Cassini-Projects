define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/shared/services/core/substanceService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('SubstanceBasicInfoController', SubstanceBasicInfoController);

        function SubstanceBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, SubstanceService, CommonService, $translate) {
            var vm = this;
            vm.loading = true;
            vm.substanceId = $stateParams.substanceId;
            vm.substance = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateSubstance = updateSubstance;

            function loadSubstanceBasicDetails() {
                vm.loading = true;
                if (vm.substanceId != null && vm.substanceId != undefined) {
                    SubstanceService.getSubstance(vm.substanceId).then(
                        function (data) {
                            vm.substance = data;
                            $rootScope.substance = vm.substance;
                            $scope.name = vm.substance.name;
                            CommonService.getPersonReferences([vm.substance], 'modifiedBy');
                            CommonService.getPersonReferences([vm.substance], 'createdBy');
                            if (vm.substance.createdDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.substance.createDateDe = moment(vm.substance.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.substance.createDateDe = vm.substance.createdDate;
                                }

                            }
                            if (vm.substance.modifiedDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.substance.modifiedDateDe = moment(vm.substance.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.substance.modifiedDateDe = vm.substance.modifiedDate;
                                }
                            }
                            if (vm.substance.description != null && vm.substance.description != undefined) {
                                vm.substance.descriptionHtml = $sce.trustAsHtml(vm.substance.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            vm.loading = false;
                            vm.editStatus = false;
                            $rootScope.viewInfo.title = "<div class='item-number'>" +
                                "{0}</div>".
                                    format(vm.substance.number);
                            $rootScope.viewInfo.description = vm.substance.name;
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }


            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var substanceUpdatedSuccess = parsed.html($translate.instant("SUBSTANCE_UPDATED_SUCCESS")).html();


            function validateSubstance() {
                var valid = true;
                if (vm.substance.name == null || vm.substance.name == ""
                    || vm.substance.name == undefined) {
                    valid = false;
                    vm.substance.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }
                return valid;
            }

            function updateSubstance() {
                if (validateSubstance()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    SubstanceService.updateSubstance(vm.substance).then(
                        function (data) {
                            loadSubstanceBasicDetails();
                            $rootScope.showSuccessMessage(substanceUpdatedSuccess);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                $scope.$on('app.substance.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadSubstanceBasicDetails();
                    }
                });

            })();

        }
    }
);
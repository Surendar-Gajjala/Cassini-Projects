define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/shared/services/core/inspectionService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('NcrBasicInfoController', NcrBasicInfoController);

        function NcrBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $application, $translate, $stateParams, $cookies, $window, ItemTypeService, CommonService,
                                        NcrService, InspectionService, QualityTypeService) {
            var vm = this;

            $scope.$sce = $sce;
            vm.loading = true;
            vm.loadingAttributes = true;
            vm.ncrId = $stateParams.ncrId;
            vm.ncr = null;
            vm.lifeCycles = null;
            $scope.opened = {};

            vm.external = $rootScope.loginPersonDetails;
            $rootScope.external = $rootScope.loginPersonDetails;

            var parsed = angular.element("<div></div>");
            var enterTitle = parsed.html($translate.instant("TITLE_VALIDATION")).html();
            var enterDescription = parsed.html($translate.instant("DESCRIPTION_NOT_EMPTY")).html();
            var ncrUpdated = parsed.html($translate.instant("NCR_UPDATED")).html();

            $rootScope.loadNcrBasicInfo = loadNcrBasicInfo;
            function loadNcrBasicInfo() {
                vm.loading = true;
                if (vm.ncrId != null && vm.ncrId != undefined) {
                    NcrService.getNcr(vm.ncrId).then(
                        function (data) {
                            vm.ncr = data;
                            $rootScope.ncr = data;
                            if (vm.ncr.title != null && vm.ncr.title != undefined) {
                                vm.ncr.titleHtml = $sce.trustAsHtml(vm.ncr.title.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            if (vm.ncr.description != null && vm.ncr.description != undefined) {
                                vm.ncr.descriptionHtml = $sce.trustAsHtml(vm.ncr.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            vm.ncrDetails = angular.copy(vm.ncr);
                            if (vm.ncr.inspection != null) {
                                loadInspection();
                            }
                            loadPersons();
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            vm.loading = false;
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadInspection() {
                InspectionService.getInspection(vm.ncr.inspection).then(
                    function (data) {
                        vm.ncr.inspectionObject = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            /*------  To get createdBy and CreatedDate by Ids ---------*/

            function loadPersons() {
                var personIds = [vm.ncr.createdBy];

                if (vm.ncr.createdBy != vm.ncr.modifiedBy) {
                    personIds.push(vm.ncr.modifiedBy);
                }
                personIds.push(vm.ncr.reportedBy);
                personIds.push(vm.ncr.qualityAnalyst);

                CommonService.getPersons(personIds).then(
                    function (persons) {
                        var map = new Hashtable();
                        angular.forEach(persons, function (person) {
                            map.put(person.id, person);
                        });

                        if (vm.ncr.createdBy != null) {
                            var person = map.get(vm.ncr.createdBy);
                            if (person != null) {
                                vm.ncr.createdByPerson = person;
                            }
                            else {
                                vm.ncr.createdByPerson = {firstName: ""};
                            }
                        }

                        if (vm.ncr.modifiedBy != null) {
                            person = map.get(vm.ncr.modifiedBy);
                            if (person != null) {
                                vm.ncr.modifiedByPerson = person;
                            }
                            else {
                                vm.ncr.modifiedByPerson = {firstName: ""};
                            }
                        }
                        if (vm.ncr.reportedBy != null) {
                            person = map.get(vm.ncr.reportedBy);
                            if (person != null) {
                                vm.ncr.reportedByObject = person;
                            }
                            else {
                                vm.ncr.reportedByObject = {firstName: ""};
                            }
                        }
                        if (vm.ncr.qualityAnalyst != null) {
                            person = map.get(vm.ncr.qualityAnalyst);
                            if (person != null) {
                                vm.ncr.qualityAnalystObject = person;
                            }
                            else {
                                vm.ncr.qualityAnalystObject = {firstName: ""};
                            }
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function loadLifeCycles() {
                ItemTypeService.getLifeCyclesPhases().then(
                    function (data) {
                        vm.lifeCycles = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.updateNCR = updateNCR;
            function updateNCR() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('.view-content'));
                    vm.ncr.qualityAnalyst = vm.ncr.qualityAnalystObject.id;
                    NcrService.updateNcr(vm.ncrId, vm.ncr).then(
                        function (data) {
                            vm.ncr = data;
                            loadNcrBasicInfo();
                            $rootScope.showSuccessMessage(ncrUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.ncr.title == null || vm.ncr.title == "" || vm.ncr.title == undefined) {
                    valid = false;
                    vm.ncr.title = vm.ncrDetails.title;
                    $rootScope.showWarningMessage(enterTitle);
                } else if (vm.ncr.description == null || vm.ncr.description == "" || vm.ncr.description == undefined) {
                    valid = false;
                    vm.ncr.description = vm.ncrDetails.description;
                    $rootScope.showWarningMessage(enterDescription);
                }
                return valid;
            }

            function loadAnalysts() {
                vm.qualityAnalysts = [];
                var preference = $application.defaultValuesPreferences.get("DEFAULT_QUALITY_ANALYST_ROLE");
                if (preference != null && preference.defaultValueName != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.ncr.all";
                    QualityTypeService.getPersonByGroupNameAndPermission(groupName, permission).then(
                        function (data) {
                            vm.qualityAnalysts = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                loadNcrBasicInfo();
                loadLifeCycles();
                loadAnalysts();
            })();
        }
    }
)
;
define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/shared/services/core/workflowService',
        'app/shared/services/core/qualityTypeService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('QcrBasicInfoController', QcrBasicInfoController);

        function QcrBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $application, $translate, $stateParams, $cookies, $window, ItemTypeService, CommonService,
                                        QcrService, WorkflowService, QualityTypeService) {
            var vm = this;

            $scope.$sce = $sce;
            vm.loading = true;
            vm.loadingAttributes = true;
            vm.qcrId = $stateParams.qcrId;
            vm.qcr = null;
            vm.lifeCycles = null;
            $scope.opened = {};

            vm.external = $rootScope.loginPersonDetails;
            $rootScope.external = $rootScope.loginPersonDetails;

            var parsed = angular.element("<div></div>");
            var enterTitle = parsed.html($translate.instant("TITLE_VALIDATION")).html();
            var enterDescription = parsed.html($translate.instant("DESCRIPTION_NOT_EMPTY")).html();
            var qcrUpdated = parsed.html($translate.instant("QCR_UPDATED")).html();

            vm.updateQCR = updateQCR;
            $rootScope.loadQcrBasicInfo = loadQcrBasicInfo;
            function loadQcrBasicInfo() {
                vm.loading = true;
                if (vm.qcrId != null && vm.qcrId != undefined) {
                    QcrService.getQcr(vm.qcrId).then(
                        function (data) {
                            vm.qcr = data;
                            $rootScope.qcr = vm.qcr;
                            if (vm.qcr.released || vm.qcr.statusType == "REJECTED") {
                                $rootScope.qcrReleased = true;
                            }
                            if (vm.qcr.workflow != undefined) {
                                WorkflowService.getWorkflow(vm.qcr.workflow).then(function (wf) {
                                    vm.qcr.workflowObject = wf;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                })
                            }

                            if (vm.qcr.title != null && vm.qcr.title != undefined) {
                                vm.qcr.titleHtml = $sce.trustAsHtml(vm.qcr.title.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            if (vm.qcr.description != null && vm.qcr.description != undefined) {
                                vm.qcr.descriptionHtml = $sce.trustAsHtml(vm.qcr.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            vm.qcrDetails = angular.copy(vm.qcr);
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

            /*------  To get createdBy and CreatedDate by Ids ---------*/

            function loadPersons() {
                var personIds = [vm.qcr.createdBy];

                if (vm.qcr.createdBy != vm.qcr.modifiedBy) {
                    personIds.push(vm.qcr.modifiedBy);
                }
                personIds.push(vm.qcr.qualityAdministrator);

                CommonService.getPersons(personIds).then(
                    function (persons) {
                        var map = new Hashtable();
                        angular.forEach(persons, function (person) {
                            map.put(person.id, person);
                        });

                        if (vm.qcr.createdBy != null) {
                            var person = map.get(vm.qcr.createdBy);
                            if (person != null) {
                                vm.qcr.createdByPerson = person;
                            }
                            else {
                                vm.qcr.createdByPerson = {firstName: ""};
                            }
                        }

                        if (vm.qcr.modifiedBy != null) {
                            person = map.get(vm.qcr.modifiedBy);
                            if (person != null) {
                                vm.qcr.modifiedByPerson = person;
                            }
                            else {
                                vm.qcr.modifiedByPerson = {firstName: ""};
                            }
                        }

                        if (vm.qcr.qualityAdministrator != null) {
                            person = map.get(vm.qcr.qualityAdministrator);
                            if (person != null) {
                                vm.qcr.qualityAdministratorObject = person;
                            }
                            else {
                                vm.qcr.qualityAdministratorObject = {firstName: ""};
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

            function updateQCR() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('.view-content'));
                    vm.qcr.qualityAdministrator = vm.qcr.qualityAdministratorObject.id;
                    QcrService.updateQcr(vm.qcrId, vm.qcr).then(
                        function (data) {
                            vm.qcr = data;
                            loadQcrBasicInfo();
                            $rootScope.showSuccessMessage(qcrUpdated);
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
                if (vm.qcr.title == null || vm.qcr.title == "" || vm.qcr.title == undefined) {
                    valid = false;
                    vm.qcr.title = vm.qcrDetails.title;
                    $rootScope.showWarningMessage(enterTitle);
                } else if (vm.qcr.description == null || vm.qcr.description == "" || vm.qcr.description == undefined) {
                    valid = false;
                    vm.qcr.description = vm.qcrDetails.description;
                    $rootScope.showWarningMessage(enterDescription);
                }
                return valid;
            }

            function loadAnalysts() {
                vm.qualityAnalysts = [];
                var preference = $application.defaultValuesPreferences.get("DEFAULT_QUALITY_ADMINISTRATOR_ROLE");
                if (preference != null && preference.defaultValueName != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.qcr.all";
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
                loadAnalysts();
                loadQcrBasicInfo();
                loadLifeCycles();
            })();
        }
    }
)
;
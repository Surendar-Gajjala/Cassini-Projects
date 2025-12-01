define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/mesObjectTypeService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/shared/services/core/workCenterService',
        'app/shared/services/core/plantService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/assetService'
    ],
    function (module) {
        module.controller('WorkCenterBasicInfoController', WorkCenterBasicInfoController);

        function WorkCenterBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                               $translate, MESObjectTypeService, WorkCenterService, PlantService, AssetService) {
            var vm = this;
            vm.loading = true;

            vm.workCenterId = $stateParams.workcenterId;

            vm.updateWorkCenter = updateWorkCenter;

            var parsed = angular.element("<div></div>");
            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var workCenterUpdated = parsed.html($translate.instant("WORKCENTER_UPDATED")).html();

            function loadWorkCenter() {
                WorkCenterService.getWorkCenter(vm.workCenterId).then(
                    function (data) {
                        vm.workCenter = data;
                        $rootScope.workCenter = vm.workCenter;
                        $scope.name = vm.workCenter.name;
                        loadPlant();
                        loadPersons();
                        vm.loading = false;
                        $rootScope.viewInfo.title = $translate.instant("WORK_CENTER_DETAILS");
                        $rootScope.viewInfo.description = vm.workCenter.number + " , " + vm.workCenter.name;
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 1000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadPlant() {
                if (vm.workCenter.plant != undefined && vm.workCenter.plant != null) {
                    PlantService.getPlant(vm.workCenter.plant).then(
                        function (data) {
                            vm.workCenter.plantObject = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadPersons() {
                var personIds = [vm.workCenter.createdBy];

                if (vm.workCenter.createdBy != vm.workCenter.modifiedBy) {
                    personIds.push(vm.workCenter.modifiedBy);
                }

                CommonService.getPersons(personIds).then(
                    function (persons) {
                        var map = new Hashtable();
                        angular.forEach(persons, function (person) {
                            map.put(person.id, person);
                        });

                        if (vm.workCenter.createdBy != null) {
                            var person = map.get(vm.workCenter.createdBy);
                            if (person != null) {
                                vm.workCenter.createdByPerson = person;
                            }
                            else {
                                vm.workCenter.createdByPerson = {firstName: ""};
                            }
                        }

                        if (vm.workCenter.modifiedBy != null) {
                            person = map.get(vm.workCenter.modifiedBy);
                            if (person != null) {
                                vm.workCenter.modifiedByPerson = person;
                            }
                            else {
                                vm.workCenter.modifiedByPerson = {firstName: ""};
                            }
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }


            function updateWorkCenter() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    WorkCenterService.updateWorkCenter(vm.workCenter).then(
                        function (data) {
                            loadWorkCenter();
                            vm.editStatus = false;
                            vm.editMaintenance = false;
                            $rootScope.showSuccessMessage(workCenterUpdated);
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
                if (vm.workCenter.name == null || vm.workCenter.name == "" || vm.workCenter.name == undefined) {
                    valid = false;
                    vm.workCenter.name = $scope.name;
                    $rootScope.showWarningMessage(nameValidation);
                }

                return valid;
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
                vm.oldStatus = vm.workCenter.active;
            }

            vm.cancelStatus = cancelStatus;
            function cancelStatus() {
                vm.editStatus = false;
                vm.workCenter.active = vm.oldStatus;
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
                    vm.oldMaintenane = vm.workCenter.requiresMaintenance;
                }
            }

            vm.cancelMaintenance = cancelMaintenance;
            function cancelMaintenance() {
                vm.editMaintenance = false;
                vm.workCenter.requiresMaintenance = vm.oldMaintenane;
            }

            function loadWorkCenterAssets() {
                AssetService.getAssetsByResource(vm.workCenterId).then(
                    function (data) {
                        vm.assets = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $scope.$on('app.workCenter.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadWorkCenter();
                    }
                });
                loadWorkCenter();
                loadWorkCenterAssets();
            })();

        }
    }
);
define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/assetService',
        'app/shared/services/core/meterService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('AssetBasicInfoController', AssetBasicInfoController);

        function AssetBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                          AssetService, $translate, LoginService, MeterService) {
            var vm = this;
            vm.loading = true;
            vm.assetId = $stateParams.assetId;
            vm.asset = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateAsset = updateAsset;
            vm.removeAssetMeter = removeAssetMeter;
            function loadAssetBasicDetails() {
                vm.loading = true;
                vm.newAssetMeter = {
                    id: vm.assetId,
                    meters: []
                };
                if (vm.assetId != null && vm.assetId != undefined) {
                    AssetService.getAsset(vm.assetId).then(
                        function (data) {
                            vm.asset = data;
                            loadResourceImage(vm.asset);
                            $rootScope.asset = vm.asset;
                            $scope.name = vm.asset.name;
                            CommonService.getPersonReferences([vm.asset], 'modifiedBy');
                            CommonService.getPersonReferences([vm.asset], 'createdBy');
                            if (vm.asset.description != null && vm.asset.description != undefined) {
                                vm.asset.descriptionHtml = $sce.trustAsHtml(vm.asset.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }

                            vm.loading = false;
                            vm.editStatus = false;
                            vm.editMetered = false;
                            $rootScope.viewInfo.title = $translate.instant("ASSET_DETAILS");
                            $rootScope.viewInfo.description = vm.asset.name;
                            //loadMeters();
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

            vm.showResource = showResource;
            function showResource(asset) {
                if (asset.resourceObject.objectType == 'PLANT') {
                    $state.go('app.mes.masterData.plant.details', {plantId: asset.resourceObject.id, tab: 'details.basic'});
                }
                else if (asset.resourceObject.objectType == 'WORKCENTER') {
                    $state.go('app.mes.masterData.workcenter.details', {
                        workcenterId: asset.resourceObject.id,
                        tab: 'details.basic'
                    });
                }
                else if (asset.resourceObject.objectType == 'MACHINE') {
                    $state.go('app.mes.masterData.machine.details', {machineId: asset.resourceObject.id, tab: 'details.basic'});
                } else if (asset.resourceObject.objectType == 'TOOL') {
                    $state.go('app.mes.masterData.tool.details', {toolId: asset.resourceObject.id, tab: 'details.basic'});
                } else if (asset.resourceObject.objectType == 'EQUIPMENT') {
                    $state.go('app.mes.masterData.equipment.details', {
                        equipmentId: asset.resourceObject.id,
                        tab: 'details.basic'
                    });

                } else if (asset.resourceObject.objectType == 'INSTRUMENT') {

                    $state.go('app.mes.masterData.instrument.details', {
                        instrumentId: asset.resourceObject.id,
                        tab: 'details.basic'
                    });
                } else if (asset.resourceObject.objectType == 'JIGFIXTURE') {
                    $state.go('app.mes.masterData.jigsAndFixtures.details', {
                        jigsFixId: asset.resourceObject.id,
                        tab: 'details.basic'
                    });

                }
            }

            vm.showResourceImage = showResourceImage;
            function showResourceImage(item) {
                var modal = document.getElementById('item-thumbnail-basic' + vm.assetId);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close-basic" + vm.assetId);
                $("#thumbnail-image-basic" + vm.assetId).width($('#thumbnail-view-basic' + vm.assetId).outerWidth());
                $("#thumbnail-image-basic" + vm.assetId).height($('#thumbnail-view-basic' + vm.assetId).outerHeight());
                $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            function loadResourceImage(asset) {
                asset.hasImage = false;
                if (asset.resourceObject.objectType == 'PLANT' || asset.resourceObject.objectType == 'WORKCENTER') {
                    asset.showImage = false;
                } else {
                    asset.showImage = true;
                }
                if (asset.resourceObject.objectType == 'MACHINE' && asset.resourceObject.image != null) {
                    asset.hasImage = true;
                    asset.imagePath = "api/mes/machines/" + asset.resourceObject.id + "/image/download?" + new Date().getTime();
                } else if (asset.resourceObject.objectType == 'TOOL' && asset.resourceObject.image != null) {
                    asset.hasImage = true;
                    asset.imagePath = "api/mes/tools/" + asset.resourceObject.id + "/image/download?" + new Date().getTime();
                } else if (asset.resourceObject.objectType == 'EQUIPMENT' && asset.resourceObject.image != null) {
                    asset.hasImage = true;
                    asset.imagePath = "api/mes/equipments/" + asset.resourceObject.id + "/image/download?" + new Date().getTime();
                } else if (asset.resourceObject.objectType == 'INSTRUMENT' && asset.resourceObject.image != null) {
                    asset.hasImage = true;
                    asset.imagePath = "api/mes/instruments/" + asset.resourceObject.id + "/image/download?" + new Date().getTime();
                } else if (asset.resourceObject.objectType == 'JIGFIXTURE' && asset.resourceObject.image != null) {
                    asset.hasImage = true;
                    asset.imagePath = "api/mes/jigsfixs/" + asset.resourceObject.id + "/image/download?" + new Date().getTime();
                } else if (asset.resourceObject.objectType == 'MATERIAL' && asset.resourceObject.image != null) {
                    asset.hasImage = true;
                    asset.imagePath = "api/mes/materials/" + asset.resourceObject.id + "/image/download?" + new Date().getTime();
                }
            }

            function removeAssetMeter(assetId, meterId) {
                AssetService.deleteAssetMeter(assetId, meterId).then(
                    function (data) {
                        loadAssetBasicDetails();
                        $rootScope.showSuccessMessage(parsed.html($translate.instant("METER_REMOVED_SUCCESS_MESSAGE")).html());
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.newAssetMeter = {
                id: vm.assetId,
                meters: []
            };
            var emptyMeter = {
                id: vm.assetId,
                meters: []
            }
            vm.meters = [];
            vm.addMeters = addMeters;
            function addMeters() {
                var options = {
                    title: "Add Meters",
                    template: 'app/desktop/modules/mro/meter/details/tabs/basic/selectMetersView.jsp',
                    controller: 'SelectMeterController as selectMetersVm',
                    resolve: 'app/desktop/modules/mro/meter/details/tabs/basic/selectMetersController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedAssetId: vm.assetId,
                        mode: "ASSET"
                    },
                    buttons: [
                        {text: "Add", broadcast: 'app.meter.meters.add'}
                    ],
                    callback: function (result) {
                        vm.newAssetMeter = {
                            id: vm.assetId,
                            meters: []
                        };
                        angular.forEach(result, function (meter) {
                            vm.newAssetMeter.meters.push(meter.id);
                        });
                        saveAssetMeters(vm.newAssetMeter);

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function saveAssetMeters(assetMeters) {
                MeterService.createAssetMeter(assetMeters).then(
                    function (data) {
                        loadAssetBasicDetails();
                        $rootScope.showSuccessMessage(parsed.html($translate.instant("METER_ADDED_TO_ASSET_SUCCESS_MESSAGE")).html());
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var assetUpdated = parsed.html($translate.instant("ASSET_UPDATED_SUCCESS_MESSAGE")).html();


            function validateAsset() {
                var valid = true;
                if (vm.asset.name == null || vm.asset.name == ""
                    || vm.asset.name == undefined) {
                    valid = false;
                    vm.asset.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }

                return valid;
            }

            function updateAsset() {
                if (validateAsset()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    AssetService.updateAsset(vm.asset).then(
                        function (data) {
                            loadAssetBasicDetails();
                            vm.editMetered = false;
                            vm.editStatus = false;
                            $rootScope.showSuccessMessage(assetUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            var yes = parsed.html($translate.instant("YES")).html();
            var no = parsed.html($translate.instant("NO")).html();

            vm.editMetered = false;
            vm.meteredFlags = [
                {label: yes, value: true},
                {label: no, value: false}
            ]

            vm.changeMeterFlag = changeMeterFlag;
            function changeMeterFlag() {
                vm.editMetered = true;
                vm.oldMetered = vm.asset.metered;
            }

            vm.cancelMeterFlag = cancelMeterFlag;
            function cancelMeterFlag() {
                vm.editMetered = false;
                vm.asset.metered = vm.oldMetered;
            }

            vm.meters = [];
            function loadMeters() {
                vm.meters = [];
                MeterService.getMeters().then(
                    function (data) {
                        vm.meters = data;
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            (function () {
                $scope.$on('app.asset.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadAssetBasicDetails();
                    }
                });

            })();

        }
    }
);
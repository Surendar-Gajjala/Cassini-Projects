define(
    [
        'app/desktop/desktop.app',
        'app/desktop/modules/directives/mroObjectTypeDirective',
        'app/shared/services/core/assetService',
        'app/shared/services/core/meterService'
    ],

    function (module) {
        module.directive('mesAsset', ['$rootScope', '$compile', '$timeout', '$translate', '$application', 'MeterService', 'AutonumberService', function ($rootScope, $compile, $timeout, $translate, $application, MeterService, AutonumberService) {

            return {
                templateUrl: 'app/desktop/directives/mes-asset/mesAssetDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    asset: '='
                },
                link: function ($scope, elem, attr) {
                    var parsed = angular.element("<div></div>");
                    $scope.selectMeterTitle = parsed.html($translate.instant("SELECT_METER")).html();
                    $scope.chooseMeterTitle = parsed.html($translate.instant("CHOOSE_METER")).html();
                    $scope.onSelectAssetType = onSelectAssetType;
                    $scope.generateAssetAutoNumber = generateAssetAutoNumber;
                    function onSelectAssetType(objectType) {
                        if (objectType != null && objectType != undefined) {
                            $scope.asset.type = objectType;
                            generateAssetAutoNumber();
                        }
                    }

                    function generateAssetAutoNumber() {
                        if ($scope.asset.type != null && $scope.asset.type.autoNumberSource != null) {
                            var source = $scope.asset.type.autoNumberSource;
                            AutonumberService.getNextNumberByName(source.name).then(
                                function (data) {
                                    $scope.asset.number = data;
                                }
                            )
                        }
                    }

                    $scope.meters = [];
                    $scope.changeMeter = changeMeter;
                    function changeMeter() {
                        $scope.asset.meters = [];
                    }

                    function loadMeters() {
                        MeterService.getMeters().then(
                            function (data) {
                                $scope.meters = data;

                            },
                            function (error) {

                            }
                        )
                    }

                    (function () {
                        loadMeters();
                    })();
                }
            };
        }]);
    }
);

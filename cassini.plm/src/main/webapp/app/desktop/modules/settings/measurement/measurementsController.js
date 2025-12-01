define(['app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/measurementService',
        'app/shared/services/core/itemTypeService'
    ],
    function (module) {
        module.controller('MeasurementsController', MeasurementsController);

        function MeasurementsController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                        CommonService, MeasurementService, $translate, ItemTypeService) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.measurements = [];
            vm.selectedMeasurement = null;
            vm.selectMeasurement = selectMeasurement;

            function selectMeasurement(measurement) {
                vm.selectedMeasurement = measurement;
                formatSymbol();
            }

            function loadMeasurements() {
                MeasurementService.getAllMeasurements().then(
                    function (data) {
                        vm.measurements = data;
                        if (vm.measurements.length > 0) {
                            vm.selectedMeasurement = vm.measurements[0];
                            formatSymbol();
                        }
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function formatSymbol() {
                angular.forEach(vm.selectedMeasurement.measurementUnits, function (unit) {
                    if (unit.symbol == 'mm3') {
                        unit.symbol = "<span>mm<sup>3</sup></span>";
                    } else if (unit.symbol == 'cm3') {
                        unit.symbol = "<span>cm<sup>3</sup></span>";
                    } else if (unit.symbol == 'in3') {
                        unit.symbol = "<span>in<sup>3</sup></span>";
                    } else if (unit.symbol == 'in2') {
                        unit.symbol = "<span>in<sup>2</sup></span>";
                    } else if (unit.symbol == 'dm3') {
                        unit.symbol = "<span>dm<sup>3</sup></span>";
                    } else if (unit.symbol == 'f2') {
                        unit.symbol = "<span>f<sup>2</sup></span>";
                    } else if (unit.symbol == 'ft3') {
                        unit.symbol = "<span>ft<sup>3</sup></span>";
                    } else if (unit.symbol == 'rd2') {
                        unit.symbol = "<span>rd<sup>2</sup></span>";
                    } else if (unit.symbol == 'yd3') {
                        unit.symbol = "<span>yd<sup>3</sup></span>";
                    } else if (unit.symbol == 'm3') {
                        unit.symbol = "<span>m<sup>3</sup></span>";
                    } else if (unit.symbol == 'm2') {
                        unit.symbol = "<span>m<sup>2</sup></span>";
                    } else if (unit.symbol == 'dam3') {
                        unit.symbol = "<span>dam<sup>3</sup></span>";
                    } else if (unit.symbol == 'km3') {
                        unit.symbol = "<span>km<sup>3</sup></span>";
                    } else if (unit.symbol == 'km2') {
                        unit.symbol = "<span>km<sup>2</sup></span>";
                    } else if (unit.symbol == 'mi3') {
                        unit.symbol = "<span>mi<sup>3</sup></span>";
                    } else if (unit.symbol == 'mi2') {
                        unit.symbol = "<span>mi<sup>2</sup></span>";
                    } else if (unit.symbol == 'mm2') {
                        unit.symbol = "<span>mm<sup>2</sup></span>";
                    } else if (unit.symbol == 'kg/cm3') {
                        unit.symbol = "<span>kg/cm<sup>2</sup></span>";
                    } else if (unit.symbol == 'lb/ft3') {
                        unit.symbol = "<span>lb/ft<sup>3</sup></span>";
                    } else if (unit.symbol == 'g/cm3') {
                        unit.symbol = "<span>g/cm<sup>3</sup></span>";
                    } else if (unit.symbol == 'Mg/cm3') {
                        unit.symbol = "<span>Mg/cm<sup>3</sup></span>";
                    } else if (unit.symbol == 'tn/m3') {
                        unit.symbol = "<span>tn/m<sup>3</sup></span>";
                    } else if (unit.symbol == 'oz/in3') {
                        unit.symbol = "<span>oz/in<sup>3</sup></span>";
                    } else if (unit.symbol == 'gf/m2') {
                        unit.symbol = "<span>gf/m<sup>2</sup></span>";
                    } else if (unit.symbol == 'gf/ft2') {
                        unit.symbol = "<span>gf/ft<sup>2</sup></span>";
                    } else if (unit.symbol == 'pdl/m3') {
                        unit.symbol = "<span>pdl/m<sup>3</sup></span>";
                    } else if (unit.symbol == 'pdl/in2') {
                        unit.symbol = "<span>pdl/in<sup>2</sup></span>";
                    } else if (unit.symbol == 'kn/m2') {
                        unit.symbol = "<span>kn/m<sup>2</sup></span>";
                    } else if (unit.symbol == 'pdl/ft2') {
                        unit.symbol = "<span>pdl/ft<sup>2</sup></span>";
                    } else if (unit.symbol == 'pf/m3') {
                        unit.symbol = "<span>pf/m<sup>3</sup></span>";
                    } else if (unit.symbol == 'kgf/m2') {
                        unit.symbol = "<span>kgf/m<sup>2</sup></span>";
                    } else if (unit.symbol == 'tf/m2') {
                        unit.symbol = "<span>tf/m<sup>2</sup></span>";
                    } else if (unit.symbol == 'N/cm2') {
                        unit.symbol = "<span>N/cm<sup>2</sup></span>";
                    } else if (unit.symbol == 'kf/ft2') {
                        unit.symbol = "<span>kf/ft<sup>2</sup></span>";
                    } else if (unit.symbol == 'kf/m2') {
                        unit.symbol = "<span>kf/m<sup>2</sup></span>";
                    } else if (unit.symbol == 'tf/cm2') {
                        unit.symbol = "<span>tf/cm<sup>2</sup></span>";
                    } else if (unit.symbol == 'kn/mm2') {
                        unit.symbol = "<span>kn/mm<sup>2</sup></span>";
                    } else if (unit.symbol == 'mn/mm2') {
                        unit.symbol = "<span>mn/mm<sup>2</sup></span>";
                    } else if (unit.symbol == " t/in2") {
                        unit.symbol = "<span>t/in<sup>2</sup></span>";
                    } else if (unit.symbol == 'kgf/cm2') {
                        unit.symbol = "<span>kgf/cm<sup>2</sup></span>";
                    } else if (unit.symbol == 't/f2') {
                        unit.symbol = "<span>t/f<sup>2</sup></span>";
                    } else if (unit.symbol == 'mn/m2') {
                        unit.symbol = "<span>mn/m<sup>2</sup></span>";
                    } else if (unit.symbol == 'mn/m2') {
                        unit.symbol = "<span>mn/m<sup>2</sup></span>";
                    } else if (unit.symbol == 'mmH2O') {
                        unit.symbol = "<span>mmH<sub>2</sub>O</span>";
                    } else if (unit.symbol == 'cmH2O') {
                        unit.symbol = "<span>cmH<sub>2</sub>O</span>";
                    } else if (unit.symbol == 'ftH2O') {
                        unit.symbol = "<span>ftH<sub>2</sub>O</span>";
                    } else if (unit.symbol == 'mH2O') {
                        unit.symbol = "<span>mH<sub>2</sub>O</span>";
                    } else if (unit.symbol == 'inH2O') {
                        unit.symbol = "<span>inH<sub>2</sub>O</span>";
                    } else if (unit.symbol == 'mm Hg 00C') {
                        unit.symbol = "<span>mm Hg 0<sup>o</sup>C</span>";
                    } else if (unit.symbol == 'N/m2') {
                        unit.symbol = "<span>N/m<sup>2</sup></span>";
                    } else if (unit.symbol == 'N/mm2') {
                        unit.symbol = "<span>N/mm<sup>2</sup></span>";
                    } else if (unit.symbol == 'lb/in3') {
                        unit.symbol = "<span>lb/in<sup>3</sup></span>";
                    } else if (unit.symbol == 'tn yd3') {
                        unit.symbol = "<span>tn yd<sup>3</sup></span>";
                    } else if (unit.symbol == 'cm2') {
                        unit.symbol = "<span>cm<sup>2</sup></span>";
                    } else if (unit.symbol == 'oR') {
                        unit.symbol = "<span><sup>o</sup>R</span>";
                    } else if (unit.symbol == 'oF') {
                        unit.symbol = "<span><sup>o</sup>F</span>";
                    } else if (unit.symbol == 'oC') {
                        unit.symbol = "<span><sup>o</sup>C</span>";
                    } else if (unit.symbol === "oRé") {
                        unit.symbol = "<span><sup>o</sup>Ré</span>";
                    } else if (unit.symbol == 'oC') {
                        unit.symbol = "<span><sup>o</sup>C</span>";
                    } else if (unit.symbol == 'm3, LHV') {
                        unit.symbol = "<span>m<sup>3</sup> LHV</span>";
                    }
                })
            }

            (function () {
                loadMeasurements();
            })();
        }
    }
)
;
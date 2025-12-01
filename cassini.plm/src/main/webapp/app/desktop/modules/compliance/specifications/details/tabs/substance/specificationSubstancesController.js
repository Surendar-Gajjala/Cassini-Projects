define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/shared/services/core/specificationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/measurementService'
    ],
    function (module) {
        module.controller('SpecificationSubstancesController', SpecificationSubstancesController);

        function SpecificationSubstancesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window,
                                                   DialogService, SpecificationService, $translate, MeasurementService) {
            var vm = this;

            vm.loading = true;
            vm.specificationId = $stateParams.specificationId;
            var parsed = angular.element("<div></div>");

            var deleteSpecSubDialogTitle = parsed.html($translate.instant("DELETE_SPEC_SUBSTANCE_DIALOG_TITLE")).html();
            var deleteSpecSubDialogMessage = parsed.html($translate.instant("DELETE_SPEC_SUBSTANCE_DIALOG_MESSAGE")).html();
            var deleteSpecSubMessage = parsed.html($translate.instant("DELETE_SPEC_SUBSTANCE_MESSAGE")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            vm.addSubstances = parsed.html($translate.instant("ADD_SUBSTANCE")).html();

            vm.selectSubstances = selectSubstances;
            vm.createSubstance = createSubstance;
            vm.deleteSpecSubstance = deleteSpecSubstance;
            vm.saveAll = saveAll;
            vm.removeAll = removeAll;

            function loadSpecSubstances() {
                SpecificationService.getAllSpecSubstances(vm.specificationId).then(
                    function (data) {
                        $rootScope.specSubstances = data;
                        angular.forEach($rootScope.specSubstances, function (sub) {
                            sub.editMode = false;
                        });
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            var substanceSelectionTitle = $translate.instant("SELECT_SUBSTANCE_TITLE");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var specSubstanceAddedSuccess = parsed.html($translate.instant("SPEC_SUBSTANCE_ADDED")).html();
            var allSpecSubstanceAddedSuccess = parsed.html($translate.instant("SPEC_SUBSTANCES_ADDED")).html();
            var specSubstanceUpdatedSuccess = parsed.html($translate.instant("SPEC_SUBSTANCE_UPDATED")).html();
            var specThresholdPpmValidationMsg = parsed.html($translate.instant("THRESHOLD_PPM_VALIDATION_MSG")).html();
            var specThresholdMassValidationMsg = parsed.html($translate.instant("THRESHOLD_MASS_VALIDATION_MSG")).html();

            vm.selectedSubstances = [];
            function selectSubstances() {
                var options = {
                    title: substanceSelectionTitle,
                    template: 'app/desktop/modules/compliance/specifications/details/tabs/substance/selectSubstanceView.jsp',
                    controller: 'SelectSpecSubstanceController as selectSubstanceVm',
                    resolve: 'app/desktop/modules/compliance/specifications/details/tabs/substance/selectSubstanceController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedSpecId: vm.specificationId
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.select.substance.new'}
                    ],
                    callback: function (result) {
                        vm.substances = result;
                        vm.itemFlag = true;
                        var selectSubstance = {
                            substance: null,
                            specification: vm.specificationId,
                            thresholdPpm: 0,
                            thresholdMass: null
                        };
                        angular.forEach(vm.substances, function (substance) {
                            var specSubstance = angular.copy(selectSubstance);
                            specSubstance.editMode = true;
                            specSubstance.substance = substance;
                            $rootScope.specSubstances.unshift(specSubstance);
                            vm.selectedSubstances.unshift(specSubstance);
                        });

                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.onCancel = onCancel;
            function onCancel(item) {
                $rootScope.specSubstances.splice($rootScope.specSubstances.indexOf(item), 1);
                vm.selectedSubstances.splice(vm.selectedSubstances.indexOf(item), 1);
            }

            function createSubstance(substance) {
                if (validate(substance)) {
                    if (substance.id == null) {
                        SpecificationService.createSpecSubstance(vm.specificationId, substance).then(
                            function (data) {
                                substance.id = data.id;
                                substance.editMode = false;
                                vm.selectedSubstances.splice(vm.selectedSubstances.indexOf(substance), 1);
                                $rootScope.showSuccessMessage(specSubstanceAddedSuccess);
                                $rootScope.loadSpecificationTabCounts();
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message)
                            }
                        )
                    } else {
                        SpecificationService.updateSpecSubstance(vm.specificationId, substance).then(
                            function (data) {
                                substance.id = data.id;
                                substance.editMode = false;
                                $rootScope.showSuccessMessage(specSubstanceUpdatedSuccess);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message)
                            }
                        )
                    }
                }
            }

            function saveAll() {
                if (validateSubstances()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    SpecificationService.createSpecSubstances(vm.specificationId, vm.selectedSubstances).then(
                        function (data) {
                            loadSpecSubstances();
                            $rootScope.loadSpecificationTabCounts();
                            vm.selectedSubstances = [];
                            $rootScope.showSuccessMessage(allSpecSubstanceAddedSuccess);
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            var pleaseSelectUnits = parsed.html($translate.instant("PLEASE_SELECT_UNITS")).html();

            function validate(substance) {
                var valid = true;
                /*if (substance.thresholdPpm === null || substance.thresholdPpm === undefined
                 || substance.thresholdPpm === "") {
                 $rootScope.showWarningMessage(specThresholdPpmValidationMsg);
                 valid = false;
                 }*/
                if (substance.thresholdMass === null || substance.thresholdMass === undefined || substance.thresholdMass === "") {
                    $rootScope.showWarningMessage(specThresholdMassValidationMsg);
                    valid = false;
                } else if (substance.uom == null || substance.uom == "" || substance.uom == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseSelectUnits);
                }
                return valid;
            }

            function validateSubstances() {
                var valid = true;
                angular.forEach(vm.selectedSubstances, function (substance) {
                    if (valid) {
                        /*if (substance.thresholdPpm == null || substance.thresholdPpm == undefined
                         || substance.thresholdPpm == "") {
                         $rootScope.showErrorMessage(substance.substance.number + " : " + specThresholdPpmValidationMsg);
                         valid = false;
                         }*/
                        if (substance.thresholdMass == null || substance.thresholdMass == undefined
                            || substance.thresholdMass == "") {
                            $rootScope.showErrorMessage(substance.substance.number + " : " + specThresholdMassValidationMsg);
                            valid = false;
                        } else if (substance.uom == null || substance.uom == "" || substance.uom == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage(pleaseSelectUnits);
                        }
                    }
                });
                return valid;
            }

            function removeAll() {
                angular.forEach(vm.selectedSubstances, function (part) {
                    $rootScope.specSubstances.splice($rootScope.specSubstances.indexOf(part), 1);
                });
                vm.selectedSubstances = [];
            }

            vm.editSpecSubstance = editSpecSubstance;
            function editSpecSubstance(substance) {
                vm.itemFlag = false;
                substance.editMode = true;
                substance.oldThresholdPpm = substance.thresholdPpm;
                substance.oldThresholdMass = substance.thresholdMass;
                substance.oldUom = substance.uom;
                substance.oldUnitName = substance.unitName;
                substance.oldUnitSymbol = substance.unitSymbol;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(substance) {
                substance.editMode = false;
                substance.thresholdPpm = substance.oldThresholdPpm;
                substance.thresholdMass = substance.oldThresholdMass;
                substance.uom = substance.oldUom;
                substance.unitName = substance.oldUnitName;
                substance.unitSymbol = substance.oldUnitSymbol;
            }

            function deleteSpecSubstance(substance) {
                var options = {
                    title: deleteSpecSubDialogTitle,
                    message: deleteSpecSubDialogMessage + " [ " + substance.substance.number + " ] " + itemDelete + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        SpecificationService.deleteSpecSubstance(vm.specificationId, substance.substance.id).then(
                            function (data) {
                                loadSpecSubstances();
                                $rootScope.loadSpecificationTabCounts();
                                var index = $rootScope.specSubstances.indexOf(substance);
                                $rootScope.specSubstances.splice(index, 1);
                                $rootScope.showSuccessMessage(deleteSpecSubMessage);
                            }, function (error) {
                                  $rootScope.showErrorMessage(error.message);
                                  $rootScope.hideBusyIndicator();
                             }
                        )
                    }
                });
            }

            vm.showSubstanceDetails = showSubstanceDetails;
            function showSubstanceDetails(substance) {
                $state.go('app.compliance.substance.details', {
                    substanceId: substance.substance.id,
                    tab: 'details.basic'
                });
            }

            function loadMassMeasurement() {
                MeasurementService.getMeasurementByName("Mass").then(
                    function (data) {
                        vm.measurement = data;
                        if (vm.measurement != null && vm.measurement != "") {
                            vm.measurementUnits = vm.measurement.measurementUnits;
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $scope.$on('app.specification.tabActivated', function (event, data) {
                    if (data.tabId == 'details.substances') {
                        loadSpecSubstances();
                        loadMassMeasurement();
                    }
                });
            })();
        }
    }
);
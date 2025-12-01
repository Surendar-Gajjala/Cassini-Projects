define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/shared/services/core/declarationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/measurementService'
    ],
    function (module) {
        module.controller('DeclarationMfrPartsController', DeclarationMfrPartsController);

        function DeclarationMfrPartsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window,
                                               DialogService, DeclarationService, $translate, MeasurementService) {
            var vm = this;

            vm.loading = true;
            vm.declarationId = $stateParams.declarationId;
            var parsed = angular.element("<div></div>");

            var deleteDecPartDialogTitle = parsed.html($translate.instant("DELETE_DEC_PART_DIALOG_TITLE")).html();
            var deleteDecPartDialogMessage = parsed.html($translate.instant("DELETE_DEC_PART_DIALOG_MESSAGE")).html();
            var deleteDecPartMessage = parsed.html($translate.instant("DELETE_DEC_PART_MESSAGE")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var substanceAdded = parsed.html($translate.instant("SUBSTANCE_ADDED")).html();
            var substanceUpdated = parsed.html($translate.instant("SUBSTANCE_UPDATED")).html();
            var substanceRemoved = parsed.html($translate.instant("SUBSTANCE_REMOVED")).html();
            var addSubstancesTitle = parsed.html($translate.instant("ADD_SUBSTANCES")).html();
            var pleaseEnterMass = parsed.html($translate.instant("PLEASE_ENTER_MASS")).html();
            var pleaseEnterUnits = parsed.html($translate.instant("PLEASE_SELECT_UNITS")).html();
            var removeSubstanceMsg = parsed.html($translate.instant("REMOVE_SUBSTANCE_MSG")).html();
            var removeSubstanceTitle = parsed.html($translate.instant("REMOVE_SUBSTANCE")).html();

            vm.selectParts = selectParts;
            vm.createDeclarationPart = createDeclarationPart;
            vm.deleteDeclarationPart = deleteDeclarationPart;
            vm.saveAll = saveAll;
            vm.removeAll = removeAll;

            function loadDeclarationParts() {
                vm.selectedDeclarationParts = [];
                DeclarationService.getAllDeclarationParts(vm.declarationId).then(
                    function (data) {
                        vm.declarationParts = data;
                        angular.forEach(vm.declarationParts, function (part) {
                            part.editMode = false;
                            part.expanded = false;
                            angular.forEach(part.substances, function (substance) {
                                substance.editMode = false;
                                substance.isNew = false;
                            })
                        });
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
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

            vm.showSubstances = showSubstances;
            function showSubstances(part) {
                part.expanded = !part.expanded;
            }

            var mfrPartsSelectionTitle = $translate.instant("SELECT_MFR_PARTS_SELECTION");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var declarationPartAddedSuccess = parsed.html($translate.instant("DEC_PART_ADDED")).html();
            var allDeclarationPartAddedSuccess = parsed.html($translate.instant("DEC_PARTS_ADDED")).html();
            var declarationPartUpdatedSuccess = parsed.html($translate.instant("DEC_PART_UPDATED")).html();

            vm.selectedDeclarationParts = [];
            function selectParts() {
                var options = {
                    title: mfrPartsSelectionTitle,
                    template: 'app/desktop/modules/item/details/selectMfrItemView.jsp',
                    controller: 'SelectMfrItemController as mfrItemVm',
                    resolve: 'app/desktop/modules/item/details/selectMfrItemController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedSupplierId: $rootScope.declaration.supplier,
                        selectedDeclarationId: $rootScope.declaration.id,
                        selectMfrPartsMode: "DECLARATION"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.item.mfr.new'}
                    ],
                    callback: function (result) {
                        vm.parts = result;
                        vm.itemFlag = true;
                        var selectDeclarationPart = {
                            part: null,
                            declaration: vm.declarationId,
                            bos: null
                        };
                        angular.forEach(vm.parts, function (part) {
                            var declarationPart = angular.copy(selectDeclarationPart);
                            declarationPart.editMode = true;
                            declarationPart.part = part;
                            vm.declarationParts.unshift(declarationPart);
                            vm.selectedDeclarationParts.unshift(declarationPart);
                        });
                        saveAll();

                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.selectedDeclarationParts = [];
            vm.addSubstances = addSubstances;
            function addSubstances(declarationPart) {
                var options = {
                    title: addSubstancesTitle,
                    template: 'app/desktop/modules/compliance/declaration/details/tabs/parts/selectSubstancesView.jsp',
                    controller: 'SelectSubstancesController as selectSubstancesVm',
                    resolve: 'app/desktop/modules/compliance/declaration/details/tabs/parts/selectSubstancesController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedDeclarationPartId: declarationPart.id
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.declaration.substances.add'}
                    ],
                    callback: function (result) {
                        vm.substances = result;
                        var emptyPartSubstance = {
                            id: null,
                            bos: null,
                            bosItemType: "SUBSTANCE",
                            substance: null,
                            mass: null,
                            uom: null
                        }
                        angular.forEach(vm.substances, function (substance) {
                            declarationPart.expanded = true;
                            var partSubstance = angular.copy(emptyPartSubstance);
                            partSubstance.editMode = true;
                            partSubstance.isNew = true;
                            partSubstance.substance = substance.id;
                            partSubstance.substanceName = substance.name;
                            partSubstance.substanceType = substance.type.name;
                            partSubstance.casNumber = substance.casNumber;
                            partSubstance.description = substance.description;
                            declarationPart.substances.push(partSubstance);
                        })
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.onCancel = onCancel;
            function onCancel(item) {
                vm.declarationParts.splice(vm.declarationParts.indexOf(item), 1);
                vm.selectedDeclarationParts.splice(vm.selectedDeclarationParts.indexOf(item), 1);
            }

            function createDeclarationPart(part) {
                if (part.id == null) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    DeclarationService.createDeclarationPart(vm.declarationId, part).then(
                        function (data) {
                            part.id = data.id;
                            part.manufacturerPart = data.manufacturerPart;
                            part.editMode = false;
                            part.isNew = false;
                            vm.selectedDeclarationParts.splice(vm.selectedDeclarationParts.indexOf(part), 1);
                            if (vm.selectedDeclarationParts.length == 0) {
                                loadDeclarationParts();
                            }
                            $rootScope.loadDeclarationTabCounts();
                            $rootScope.showSuccessMessage(declarationPartAddedSuccess);
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    $rootScope.showBusyIndicator($('.view-container'));
                    DeclarationService.updateDeclarationPart(vm.declarationId, part).then(
                        function (data) {
                            part.id = data.id;
                            part.editMode = false;
                            $rootScope.showSuccessMessage(declarationPartUpdatedSuccess);
                            loadDeclarationParts();
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

            }

            function saveAll() {
                $rootScope.showBusyIndicator($('.view-container'));
                DeclarationService.createDeclarationParts(vm.declarationId, vm.selectedDeclarationParts).then(
                    function (data) {
                        loadDeclarationParts();
                        $rootScope.loadDeclarationTabCounts();
                        vm.selectedDeclarationParts = [];
                        $rootScope.showSuccessMessage(allDeclarationPartAddedSuccess);
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

            }


            function removeAll() {
                angular.forEach(vm.selectedDeclarationParts, function (part) {
                    vm.declarationParts.splice(vm.declarationParts.indexOf(part), 1);
                });
                vm.selectedDeclarationParts = [];
            }

            vm.editDeclarationPart = editDeclarationPart;
            function editDeclarationPart(substance) {
                substance.editMode = true;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(substance) {
                substance.editMode = false;
            }

            vm.showMfrPartDetails = showMfrPartDetails;
            function showMfrPartDetails(declarationPart) {
                $state.go('app.mfr.mfrparts.details', {
                    mfrId: declarationPart.part.manufacturer,
                    manufacturePartId: declarationPart.part.id
                });
            }

            vm.showMfrDetails = showMfrDetails;
            function showMfrDetails(declarationPart) {
                $state.go('app.mfr.details', {manufacturerId: declarationPart.part.manufacturer});
            }

            function deleteDeclarationPart(declPart) {
                var options = {
                    title: deleteDecPartDialogTitle,
                    message: deleteDecPartDialogMessage + " [ " + declPart.part.partNumber + " ] " + itemDelete + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        DeclarationService.deleteDeclarationPart(vm.declarationId, declPart.id).then(
                            function (data) {
                                loadDeclarationParts();
                                $rootScope.loadDeclarationTabCounts();
                                var index = vm.declarationParts.indexOf(declPart);
                                vm.declarationParts.splice(index, 1);
                                $rootScope.showSuccessMessage(deleteDecPartMessage);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            vm.createPartSubstance = createPartSubstance;
            vm.updatePartSubstance = updatePartSubstance;
            vm.removePartSubstance = removePartSubstance;
            vm.cancelPartSubstanceChanges = cancelPartSubstanceChanges;
            vm.editPartSubstance = editPartSubstance;
            vm.deletePartSubstance = deletePartSubstance;

            function createPartSubstance(declarationPart, substance) {
                if (validatePartSubstance(substance)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    DeclarationService.createPartSubstance(declarationPart.id, substance).then(
                        function (data) {
                            substance.id = data.id;
                            substance.bos = data.bos;
                            substance.editMode = false;
                            substance.isNew = false;
                            $rootScope.loadDeclarationTabCounts();
                            $rootScope.showSuccessMessage(substanceAdded);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validatePartSubstance(substance) {
                var valid = true;

                if (substance.mass == null || substance.substance == "" || substance.mass == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterMass);
                } else if (substance.uom == null || substance.uom == "" || substance.uom == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterUnits);
                }

                return valid;
            }

            function updatePartSubstance(declarationPart, substance) {
                if (validatePartSubstance(substance)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    DeclarationService.updatePartSubstance(declarationPart.id, substance).then(
                        function (data) {
                            substance.id = data.id;
                            substance.editMode = false;
                            $rootScope.showSuccessMessage(substanceUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function removePartSubstance(declarationPart, substance) {
                declarationPart.substances.splice(declarationPart.substances.indexOf(substance), 1);
                if (declarationPart.substances.length == 0) {
                    declarationPart.expanded = false;
                }
            }

            function cancelPartSubstanceChanges(substance) {
                substance.mass = substance.oldMass;
                substance.uom = substance.oldUom;
                substance.unitName = substance.oldUnitName;
                substance.unitSymbol = substance.oldUnitSymbol;
                substance.editMode = false;
            }

            function editPartSubstance(substance) {
                substance.oldMass = substance.mass;
                substance.oldUom = substance.uom;
                substance.oldUnitName = substance.unitName;
                substance.oldUnitSymbol = substance.unitSymbol;
                substance.editMode = true;
            }

            function deletePartSubstance(declarationPart, substance) {
                var options = {
                    title: removeSubstanceTitle,
                    message: removeSubstanceMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        DeclarationService.deletePartSubstance(declarationPart.id, substance.id).then(
                            function (data) {
                                declarationPart.substances.splice(declarationPart.substances.indexOf(substance), 1);
                                if (declarationPart.substances.length == 0) {
                                    declarationPart.expanded = false;
                                }
                                $rootScope.showSuccessMessage(substanceRemoved);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            (function () {
                $scope.$on('app.declaration.tabActivated', function (event, data) {
                    if (data.tabId == 'details.parts') {
                        loadDeclarationParts();
                        loadMassMeasurement();
                    }
                });
            })();
        }
    }
);

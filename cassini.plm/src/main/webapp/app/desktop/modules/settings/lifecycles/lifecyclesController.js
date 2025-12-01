define(['app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/shared/services/core/lifecycleService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('LifecyclesController', LifecyclesController);

        function LifecyclesController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                      LifecycleService, $translate) {
            var vm = this;

            vm.lifecycles = [];
            vm.phaseTypes = ['PRELIMINARY', 'REVIEW', 'RELEASED', 'OBSOLETE', 'CANCELLED'];
            vm.toDeletePhase = null;

            vm.initDropdown = initDropdown;
            vm.addLifecycle = addLifecycle;
            vm.addLifecyclePhase = addLifecyclePhase;
            vm.promptDeleteLifecycle = promptDeleteLifecycle;
            vm.promptDeletePhase = promptDeletePhase;
            vm.hideMask = hideMask;
            vm.saveLifecycle = saveLifecycle;
            vm.deleteLifecycle = deleteLifecycle;
            vm.saveLifecyclePhase = saveLifecyclePhase;
            vm.deleteLifecyclePhase = deleteLifecyclePhase;
            vm.checkAndRemoveLifecycle = checkAndRemoveLifecycle;
            var parsed = angular.element("<div></div>");
            var newLifeCycleTitle = parsed.html($translate.instant("NEW_LIFE_CYCLE")).html();

            $scope.deleteLifecycleTitle = parsed.html($translate.instant("DELETE_LIFE_CYCLE_PHASE")).html();
            $scope.saveLifecycleTitle = parsed.html($translate.instant("SAVE_LIFE_CYCLE")).html();
            $scope.addLifecycleTitle = parsed.html($translate.instant("ADD_LIFE_CYCLE_PHASE")).html();
            $scope.deletLifecyclePhaseTitle = parsed.html($translate.instant("DELETE_LIFE_CYCLE")).html();
            $scope.cancelChanges = parsed.html($translate.instant("CANCEL_CHANGES")).html();
            $scope.lifecycleAlreadyInUse = parsed.html($translate.instant("LIFECYCLE_ALREADY_IN_USE")).html();

            function addLifecycle() {
                var newLifecycle = {
                    id: null,
                    name: newLifeCycleTitle,
                    newName: newLifeCycleTitle,
                    description: "",
                    phases: [],
                    editTitle: true,
                    showBusy: false
                };

                vm.lifecycles.unshift(newLifecycle);

                $timeout(function () {
                    $('.lc-header input').focus();
                });

                //var div = $("#settingsPane");
                //div.animate({ scrollTop: div.height()+50}, 300);
            }

            function initDropdown(parent, index) {
                var b = '#dropdownMenuButton' + parent + index;
                var m = '#dropdownMenu' + parent + index;

                $(m).css({top: $(b).offset().top + 20, left: $(b).offset().left});
            }

            function addLifecyclePhase(lc, index) {
                var newPhase = {
                    id: null,
                    lifeCycle: lc.id,
                    phase: 'New Phase',
                    newPhase: 'New Phase',
                    newPhaseType: null,
                    phaseType: null,
                    editMode: true,
                    isNew: true
                };
                lc.phases.push(newPhase);
                var body = $("#lc-body" + index);
                body.animate({scrollTop: body.height() + 50}, 300);

                $timeout(function () {
                    $('.lc-phase input').focus();
                });
            }

            var lifecycleSuccessMessage = $translate.instant("LIFE_CYCLE_SUCCESS");
            var lifecycleErrorMessage = $translate.instant("LIFE_CYCLE_ERROR");

            function saveLifecycle(lc) {
                if (lc.newName == '' || lc.newName == null) {
                    lc.editTitle = true;
                    $rootScope.showSuccessMessage(lcNameValidation);
                } else {
                    lc.showBusy = true;
                    lc.name = lc.newName;
                    var promise = null;
                    if (lc.id == null) {
                        promise = LifecycleService.createLifecycle(lc);
                    }
                    else {
                        promise = LifecycleService.updateLifecycle(lc);
                    }

                    promise.then(
                        function (data) {
                            lc.showBusy = false;
                            lc.id = data.id;
                            $rootScope.showSuccessMessage(lifecycleSuccessMessage);
                        },
                        function (error) {
                            lc.showBusy = false;
                            $rootScope.showErrorMessage(lc.name + ":" + lifecycleErrorMessage);
                            vm.lifecycles.remove(lc);
                        }
                    );
                }
            }

            function loadLifecycles() {
                LifecycleService.getLifecycles().then(
                    function (data) {
                        vm.lifecycles = data;
                        angular.forEach(vm.lifecycles, function (lc) {
                            lc.editTitle = false;
                            lc.showBusy = false;
                            lc.newName = lc.name;

                            angular.forEach(lc.phases, function (p) {
                                p.newPhase = p.phase;
                                p.newPhaseType = p.phaseType;
                                p.editMode = false;
                                p.isNew = false;
                            })
                        });
                        $timeout(function () {
                            resizeScreen();
                            $rootScope.hideBusyIndicator();
                        }, 500);
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function promptDeleteLifecycle(index) {
                var width = $('#lifecycle' + index).width();
                var height = $('#lifecycle' + index).height();
                $('#lifecycle' + index).css({position: 'relative'});

                $('#lifecycleMask' + index).width(width + 2);
                $('#lifecycleMask' + index).height(height + 2);
                $('#lifecycleMask' + index).css({display: 'table'});
            }

            function promptDeletePhase(index) {
                var width = $('#lifecycle' + index).width();
                var height = $('#lifecycle' + index).height();

                $('#lifecyclePhaseMask' + index).width(width + 2);
                $('#lifecyclePhaseMask' + index).height(height + 2);
                $('#lifecyclePhaseMask' + index).css({display: 'table'});
            }

            var lifeCycleDeleteMessage = parsed.html($translate.instant("LIFE_CYCLE_DELETE")).html();

            function deleteLifecycle(lc) {
                var index = vm.lifecycles.indexOf(lc);

                if (lc.id == null) {
                    vm.lifecycles.splice(index, 1);
                }
                else {
                    LifecycleService.deleteLifecycle(lc).then(
                        function (data) {
                            vm.lifecycles.remove(lc);
                            $rootScope.showSuccessMessage(lifeCycleDeleteMessage);
                            hideMask(index);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            hideMask(index);
                        }
                    );
                }
            }

            function hideAllMasks() {
                for (var i = 0; i < vm.lifecycles.length; i++) {
                    hideMask(i);
                }
            }

            function checkAndRemoveLifecycle(lc) {
                if (lc.id == null) {
                    vm.lifecycles.remove(lc);
                } else {
                    lc.newName = lc.name;
                }
            }

            function hideMask(index) {
                $('#lifecycleMask' + index).hide();
                $('#lifecyclePhaseMask' + index).hide();
            }

            var phaseNameValidation = parsed.html($translate.instant("PHASE_NAME_VALIDATION")).html();
            var phaseTypeValidation = parsed.html($translate.instant("PHASE_TYPE_VALIDATION")).html();
            var PhaseName = parsed.html($translate.instant("PHASE_NAME")).html();
            var nameValidation = parsed.html($translate.instant("PLEASE_ENTER_NAME")).html();
            var lcNameValidation = parsed.html($translate.instant("LIFECYCLE_NAME_VALIDATION")).html();

            function validatePhase(phase) {
                var valid = true;
                if (phase.newPhase == null || phase.newPhase == "") {
                    $rootScope.showWarningMessage(phaseNameValidation);
                    valid = false;
                } else if (phase.newPhaseType == null) {
                    $rootScope.showWarningMessage(phaseTypeValidation);
                    valid = false;
                }
                return valid;
            }

            function saveLifecyclePhase(lc, phase) {

                if (validatePhase(phase)) {
                    phase.editMode = false;
                    phase.phase = phase.newPhase;
                    phase.phaseType = phase.newPhaseType;
                    lc.showBusy = true;
                    var promise = null;
                    if (phase.id == null) {
                        promise = LifecycleService.createPhase(lc, phase);
                        var message = lifeCyclePhaseCreateMessage;
                    }
                    else {
                        promise = LifecycleService.updatePhase(lc, phase);
                        var message = lifeCyclePhaseUpdateMessage;
                    }

                    promise.then(
                        function (data) {
                            phase.id = data.id;
                            $rootScope.showSuccessMessage(message);
                            lc.showBusy = false;
                        },
                        function (error) {
                            $rootScope.showWarningMessage(error.message);
                            lc.showBusy = false;
                            lc.phases.remove(phase);
                        }
                    );
                }
            }

            var lifeCyclePhaseDeleteMessage = parsed.html($translate.instant("LIFE_CYCLE_PHASE_DELETE")).html();
            var lifeCyclePhaseCreateMessage = parsed.html($translate.instant("LIFE_CYCLE_PHASE_CREATE")).html();
            var lifeCyclePhaseUpdateMessage = parsed.html($translate.instant("LIFE_CYCLE_PHASE_UPDATE")).html();

            function deleteLifecyclePhase(lc, phase) {
                if (phase.id != null) {
                    phase.newPhase = phase.phase;
                    phase.newPhaseType = phase.phaseType;
                    if (vm.toDeletePhase != null) {
                        lc.showBusy = true;
                        LifecycleService.deletePhase(lc, vm.toDeletePhase).then(
                            function (data) {
                                lc.phases.remove(vm.toDeletePhase);
                                $rootScope.showSuccessMessage(lifeCyclePhaseDeleteMessage);
                                lc.showBusy = false;
                                hideMask(vm.lifecycles.indexOf(lc));
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                lc.showBusy = false;
                            }
                        );
                    }
                } else {
                    if (phase.isNew == true) {
                        lc.phases.remove(phase);
                    }
                }
            }

            function resizeScreen() {
                var viewContent = $('.view-content').outerHeight();
                $('.stickyheader').height(viewContent - 65);
            }

            (function () {
                loadLifecycles();
                $(window).resize(function () {
                    resizeScreen();
                });
                $scope.$on("settings.new.lifecycle", function (evnt, args) {
                    addLifecycle();
                });
            })();
        }
    }
);
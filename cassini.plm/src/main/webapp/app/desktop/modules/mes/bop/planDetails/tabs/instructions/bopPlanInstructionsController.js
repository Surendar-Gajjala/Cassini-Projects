define(
    [
        'app/desktop/modules/mes/mes.module'
    ],
    function (module) {
        module.controller('BOPPlanInstructionsController', BOPPlanInstructionsController);

        function BOPPlanInstructionsController($scope, $rootScope, $timeout, $sce, $state, $translate, $stateParams, BOPService, $cookies, $uibModal, httpFactory) {

            var vm = this;
            vm.bopPlanId = $stateParams.bopPlanId;
            var parsed = angular.element("<div></div>");
            var instructionsSavedMsg = parsed.html($translate.instant("INSTRUCTIONS_SAVED_MSG")).html();

            function loadInstructions() {
                BOPService.getBopPlanInstructions(vm.bopPlanId).then(
                    function (data) {
                        vm.planInstructions = data;
                        if (vm.planInstructions == null || vm.planInstructions == "") {
                            vm.planInstructions = {
                                id: null,
                                bopOperation: vm.bopPlanId,
                                instructions: null
                            }
                            $timeout(function () {
                                $('.note-current-fontname').text('Arial');
                            }, 1000);
                        } else if (vm.planInstructions.instructions != null && vm.planInstructions.instructions != "" && ($rootScope.bopRevision.released || $rootScope.bopRevision.released)) {
                            vm.planInstructions.instructionsValue = $sce.trustAsHtml(vm.planInstructions.instructions);
                            $timeout(function () {
                                $('.note-current-fontname').text('Arial');
                            }, 1000);
                        }
                        $timeout(function () {
                            resizeView();
                        }, 500);
                    }
                )
            }

            $rootScope.saveBopPlanInstructions = saveBopPlanInstructions;
            function saveBopPlanInstructions() {
                $rootScope.showBusyIndicator($(".view-container"));
                BOPService.createBopPlanInstructions(vm.bopPlanId, vm.planInstructions).then(
                    function (data) {
                        vm.planInstructions = data;
                        $rootScope.showSuccessMessage(instructionsSavedMsg);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function resizeView() {
                $("#plan-instructions-view").height($(".tab-content").innerHeight());
                $("#plan-instructions").height($(".tab-content").innerHeight() - 5);
                $("#plan-instructions .note-editor").height($("#plan-instructions").innerHeight());

                $("#plan-instructions .note-editor .note-editing-area").height($("#plan-instructions .note-editor").innerHeight() - $("#plan-instructions .note-editor .panel-heading").outerHeight() - 20);
                $("#plan-instructions .note-editor .note-editing-area .note-editable").height($("#plan-instructions .note-editor .note-editing-area").outerHeight() - 13);
            }

            (function () {
                $scope.$on('app.bop.plan.tabActivated', function (event, args) {
                    if (args.tabId == 'details.instructions') {
                        loadInstructions();
                        $(window).resize(function () {
                            resizeView();
                        });
                    }
                });
            })();
        }
    }
)
;




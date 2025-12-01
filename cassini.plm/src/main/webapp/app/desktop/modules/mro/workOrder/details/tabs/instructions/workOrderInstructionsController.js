define(
    [
        'app/desktop/modules/mro/mro.module'
        
    ],
    function (module) {
        module.controller('WorkOrderInstructionsController', WorkOrderInstructionsController);
        function WorkOrderInstructionsController($scope, $rootScope, $timeout, $sce, $state, $translate, $stateParams, $cookies,WorkOrderService, $uibModal, httpFactory) {
       
            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.workOrderId = $stateParams.workOrderId;
            var instructionsSavedMsg = parsed.html($translate.instant("INSTRUCTIONS_SAVED_MSG")).html();
            vm.planInstructions = null;

            function loadInstructions() {
                WorkOrderService.getWorkOrderInstructions(vm.workOrderId).then(
                    function (data) {
                        vm.planInstructions = data;
                        if (vm.planInstructions == null || vm.planInstructions == "") {
                            vm.planInstructions = {
                                id: null,
                                instructions: null
                            }
                            $timeout(function () {
                                $('.note-current-fontname').text('Arial');
                            }, 1000);
                        } 
                        vm.planInstructions.instructionsValue = $sce.trustAsHtml(vm.planInstructions.instructions);
                        $timeout(function () {
                            resizeView();
                        }, 500);
                    }
                )
            }

            vm.planInstructions = {
                id: null,
                workOrder: null,
                instructions: null       
                };

         $rootScope.saveWorkOrderInstructions = saveWorkOrderInstructions;
            function saveWorkOrderInstructions() {
                vm.planInstructions.workOrder = vm.workOrderId;
                WorkOrderService.createWorkOrderInstructions(vm.workOrderId, vm.planInstructions).then(
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
                $("#workOrder-instructions-view").height($(".tab-content").innerHeight());
                $("#workOrder-instructions").height($(".tab-content").innerHeight() - 5);
                $("#workOrder-instructions .note-editor").height($("#workOrder-instructions").innerHeight());

                $("#workOrder-instructions .note-editor .note-editing-area").height($("#workOrder-instructions .note-editor").innerHeight() - $("#workOrder-instructions .note-editor .panel-heading").outerHeight() - 20);
                $("#workOrder-instructions .note-editor .note-editing-area .note-editable").height($("#workOrder-instructions .note-editor .note-editing-area").outerHeight() - 13);
            }
       
            (function () {
                $scope.$on('app.workOrder.tabActivated', function (event, args) {
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








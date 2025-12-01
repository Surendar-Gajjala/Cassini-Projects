define(['app/desktop/modules/tm/tm.module',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/wbsService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.controller('WbsSelectDialogController', WbsSelectDialogController);

        function WbsSelectDialogController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $sce,
                                           $uibModalInstance, TaskService, WbsService) {
            var vm = this;

            vm.select = select;
            vm.cancel = cancel;
            vm.selectedWbs = null;
            vm.showChild = showChild;
            vm.hideChild = hideChild;
            vm.selectRadio = selectRadio;
            var nodeId = 0;

            function showChild(wbs) {
                wbs.showChildren = true;
            }

            function hideChild(wbs) {
                wbs.showChildren = false;
            }

            function selectRadio(wbs) {
                vm.selectedWbs = wbs;
            }

            function select() {
                $uibModalInstance.close(vm.selectedWbs);
            }

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            function visitWbsTree(wbsArray) {
                angular.forEach(wbsArray, function (wbs) {
                    wbs.nodeId = ++nodeId;

                    var children = wbs.children;
                    wbs.showChildren = false;
                    if (children != undefined && children != null && children.length > 0) {
                        visitWbsTree(children);
                    }
                });
            }

            function loadWbs() {
                WbsService.getAllWbs($stateParams.projectId).then(
                    function (data) {
                        visitWbsTree(data);
                        vm.wbsData = data;
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadWbs();
                }
            })();
        }
    }
)
;
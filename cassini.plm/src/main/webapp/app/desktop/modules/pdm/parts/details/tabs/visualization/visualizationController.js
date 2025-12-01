define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'app/shared/services/core/pdmService',
        'app/shared/services/core/pdmVaultService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/forgeService'
    ],
    function (module) {
        module.controller('PartDetailsVisualizationController', PartDetailsVisualizationController);

        function PartDetailsVisualizationController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application,
                                                        PDMService, PDMVaultService, ForgeService) {
            var vm = this;

            vm.fileVersion = null;
            vm.showViewer = false;

            function generateVisualization() {
                PDMVaultService.generateVisualization(vm.fileVersion.id).then (
                    function (data) {
                        vm.fileVersion = data;
                        showForgeViewer();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function processVisualization() {
                if(vm.fileVersion != null) {
                    if(vm.fileVersion.visualizationId === null) {
                        if ($application.forgeToken == null || $application.forgeToken == undefined || $application.forgeToken == "") {
                            ForgeService.getForgeAuthentication().then(
                                function (data) {
                                    $application.forgeToken = data;
                                    generateVisualization();
                                })
                        } else {
                            generateVisualization();
                        }

                    }
                    else {
                        showForgeViewer();
                    }
                }
            }

            function showForgeViewer() {
                vm.showViewer = true;
                var urn = vm.fileVersion.visualizationId;
                var fileUrl = "app/assets/bower_components/cassini-platform/app/desktop/directives/autodeskForge/forgeView.html?url=" +
                    $application.forgeToken + "&urn=" + urn;
                $('#forgeFrame').attr('src', fileUrl);
            }

            (function () {

                $rootScope.$on('bomitem.fileversion.loaded', function(event, data) {
                    $('#forgeFrame').attr('src', "");
                    vm.showViewer = false;
                    vm.fileVersion = data.fileVersion;
                    if($scope.selectedTab.id === 'details.visualization') {
                        processVisualization();
                    }
                });

                $scope.$on("visualization.tab.selected", function(event, data) {
                    $('#forgeFrame').attr('src', "");
                    vm.showViewer = false;
                    processVisualization();
                });

            })();
        }
    }
);
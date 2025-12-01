define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'app/desktop/modules/pdm/parts/details/tabs/basic/basicController',
        'app/desktop/modules/pdm/parts/details/tabs/drawing/drawingController',
        'app/desktop/modules/pdm/parts/details/tabs/timeline/timelineController',
        'app/desktop/modules/pdm/parts/details/tabs/visualization/visualizationController',
        'app/shared/services/core/pdmService',
        'app/shared/services/core/pdmVaultService'
    ],
    function (module) {
        module.controller('PartDetailsController', PartDetailsController);

        function PartDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $sce, $cookies, $window,
                                           PDMService, PDMVaultService) {

            $rootScope.viewInfo.showDetails = false;

            var vm = this;

            vm.partId = $stateParams.partId;
            vm.part = null;
            vm.selectedItem = null;
            vm.selectedFileVersion = null;
            vm.vault = null;
            vm.folder = null;
            vm.breadCrumbs = null;
            vm.loading = true;
            vm.fileVersions = [];
            $scope.fileVersions = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: "Basic",
                    template: 'app/desktop/modules/pdm/parts/details/tabs/basic/basicView.jsp',
                    controller: "PartDetailsBasicController as basicVm",
                    index: 0,
                    active: true,
                    activated: true
                },
                visualization: {
                    id: 'details.visualization',
                    heading: "Visualization",
                    template: 'app/desktop/modules/pdm/parts/details/tabs/visualization/visualizationView.jsp',
                    controller: "PartDetailsVisualizationController as visualizationVm",
                    index: 1,
                    active: false,
                    activated: false
                },
                drawing: {
                    id: 'details.drawing',
                    heading: "Drawing",
                    template: 'app/desktop/modules/pdm/parts/details/tabs/drawing/drawingView.jsp',
                    controller: "PartDrawingController as drawingVm",
                    index: 1,
                    active: false,
                    activated: false
                },
                timeline: {
                    id: 'details.timeline',
                    heading: "History",
                    template: 'app/desktop/modules/pdm/parts/details/tabs/timeline/timelineView.jsp',
                    controller: "TimelineController as timelineVm",
                    index: 2,
                    active: false,
                    activated: false
                }
            };
            vm.activeTab = 0;


            function loadPart() {
                PDMService.getPart(vm.partId).then (
                    function(data) {
                        vm.part = data;
                        vm.selectedItem = vm.part;
                        $scope.selectedItem = vm.selectedItem;
                        vm.selectedItem.selected = true;
                        $scope.$broadcast('app.part.selected', { selectedItem: vm.part });

                        loadFileVersion();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            $scope.selectedItem = null;
            vm.loadSelectedItem = loadSelectedItem;
            function loadSelectedItem(item) {
                if(vm.selectedItem != null) {
                    vm.selectedItem.selected = false;
                }
                vm.selectedItem = item;
                vm.selectedItem.selected = true;
                $scope.selectedItem = vm.selectedItem;

                $scope.$broadcast('app.part.selected', { selectedItem: vm.selectedItem });
            }


            function loadVault() {
                PDMVaultService.getVault(vm.selectedFileVersion.file.vault).then(
                    function (data) {
                        vm.vault = data;
                        loadFolder();
                    }, function (error) {
                        //$rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadFolder() {
                PDMVaultService.getFolder(vm.selectedFileVersion.file.vault, vm.selectedFileVersion.file.folder).then(
                    function (data) {
                        vm.folder = data;
                        vm.breadCrumbs = getBreadCrumbsLink(vm.vault, vm.folder);
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showPartDetails = showPartDetails;
            function showPartDetails(fileVersion) {
                PDMVaultService.getAttachedToObject(fileVersion.id).then(
                    function (data) {
                        $state.go('app.pdm.parts.details', {partId: data.id});
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function loadFileVersion() {
                PDMService.getFileVersionForRevisionedObject(vm.part.id).then (
                    function(data) {
                        vm.selectedFileVersion = data;
                        loadVault();
                        loadFileVersions();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function getBreadCrumbsLink(vault, folder) {
                var link = "";

                var ngClick = "goToAllVaults()";
                link += "<a href='javascript:void(0);' onclick=\"" + ngClick + "\">" + "All Vaults" + "</a>";
                link += " &raquo; ";

                ngClick = "openVaultFolder(null)";
                link += "<a href='javascript:void(0);' onclick=\"" + ngClick + "\">" + vault.name + "</a>";

                var idPath = folder.idPath;
                var namePath = folder.namePath;

                if(idPath != null && namePath != null) {
                    link += " &raquo; ";
                    var arrId = idPath.split('/');
                    var arrName = namePath.split('/');

                    if (arrId.length === arrName.length) {
                        var path = "";

                        for (var i = 0; i < arrName.length; i++) {
                            path += arrId[i];
                            ngClick = "openVaultFolder('" + path + "')";
                            link += "<a href='javascript:void(0);' onclick=\"" + ngClick + "\">" + arrName[i] + "</a>";

                            if (i !== arrName.length - 1) {
                                link += " &raquo; ";
                                path += "/";
                            }
                        }
                    }
                }

                return $sce.trustAsHtml(link);
            }

            $window.openVaultFolder = openFolder;
            function openFolder(path) {
                $state.go('app.pdm.vaultdetails', {
                    vaultId: vm.vault.id, path: path
                }, {notify: true});
            }

            $window.goToAllVaults = goToAllVaults;
            function goToAllVaults() {
                $state.go('app.pdm.vaults', {
                }, {notify: true});
            }

            vm.tabSelected = tabSelected;
            $scope.selectedTab = vm.tabs.basic;
            function tabSelected(id) {
                if(id === 'details.visualization') {
                    $scope.selectedTab = vm.tabs.visualization;
                    $scope.$broadcast("visualization.tab.selected");
                }
                else if(id === 'details.basic') {
                    $scope.selectedTab = vm.tabs.basic;
                }
                else if(id === 'details.drawing') {
                    $scope.selectedTab = vm.tabs.drawing;
                    $scope.$broadcast("drawing.tab.selected");
                }
                else if(id === 'details.timeline') {
                    $scope.selectedTab = vm.tabs.timeline;
                    $scope.$broadcast("timeline.tab.selected");
                }
            }

            function loadFileVersions() {
                PDMVaultService.getFileVersions(vm.selectedFileVersion.file.id).then (
                    function(data) {
                        vm.fileVersions = data;
                        /*vm.fileVersions.sort(function(f1, f2) {
                            return f2.version - f1.version;
                         });*/

                        $scope.fileVersions = vm.fileVersions;
                    },
                    function(error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }


            (function () {
                loadPart();
            })();
        }
    }
);
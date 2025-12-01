define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'app/shared/services/core/pdmService',
        'app/shared/services/core/pdmVaultService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/kistersWebView/kisterWebViewDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('VaultDetailsController', VaultDetailsController);

        function VaultDetailsController($scope, $rootScope, $timeout, $sce, $state, $stateParams, $window, $application, $cookieStore, $translate,
                                        PDMService, PDMVaultService) {
            $rootScope.viewInfo.showDetails = false;

            var vm = this;
            $scope.freeTextQuery = null;
            vm.loading = true;
            vm.vault = {name: null};
            vm.vaultId = $stateParams.vaultId;
            var path = ($stateParams.path != null || $stateParams.path !== undefined) ? $stateParams.path : null;

            vm.viewType = 'table';
            vm.pageable = {
                page: 0,
                size: 30,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.children = angular.copy(pagedResults);
            vm.selectedFile = null;
            vm.folder = {namePath: null, idPath: null};
            vm.showDetailsPanel = false;
            vm.showViewerContainer = false;
            vm.showViewer = false;
            vm.kistersViewer = false;
            vm.filters = {
                searchQuery: null,
                path: null
            };
            function loadVault() {
                PDMVaultService.getVault(vm.vaultId).then(
                    function (data) {
                        vm.vault = data;
                        loadDetails();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.nextPage = nextPage;
            function nextPage() {
                if (vm.children.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    loadDetails();
                }
            }

            vm.previousPage = previousPage;
            function previousPage() {
                if (vm.children.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    loadDetails();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadDetails();
            }

            function loadDetails() {
                vm.filters.path = path;
                PDMVaultService.getSearchVaultChildrenByPath(vm.vaultId, vm.pageable, vm.filters).then(
                    function (data) {
                        angular.forEach(data.content, function (child) {
                            if (child.objectType === 'PDM_FILEVERSION') {
                                child.size = fileSizeToString(child.size);
                                vm.showDetailsPanel = true;
                            }
                        });
                        vm.children = data;
                        loadFolder();
                        loadAttachedToObjects();
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadAttachedToObjects() {
                var fileVersions = [];
                angular.forEach(vm.children.content, function (child) {
                    if (child.objectType === 'PDM_FILEVERSION') {
                        fileVersions.push(child);
                    }
                });

                if (fileVersions.length > 0) {
                    PDMVaultService.getAttachedToReferences(fileVersions, 'attachedTo');
                }
            }

            function loadFolder() {
                if (vm.children != undefined && vm.children.content.length > 0) {
                    var child = vm.children.content[0];
                    var folderId = null;
                    if (child.objectType === 'PDM_FOLDER') {
                        folderId = child.parent;
                    }
                    else if (child.objectType === 'PDM_FILEVERSION') {
                        folderId = child.file.folder;
                    }

                    if (folderId != null) {
                        PDMVaultService.getFolder(vm.vaultId, folderId).then(
                            function (data) {
                                vm.folder = data;
                                vm.loading = false;
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                vm.loading = false;
                            }
                        )
                    }
                    else {
                        vm.loading = false;
                    }
                }
                else {
                    vm.loading = false;
                }
            }

            vm.onClickChild = onClickChild;
            function onClickChild(child) {
                if (child.objectType === 'PDM_FOLDER') {
                    openFolder(child.idPath);
                }
                else if (child.objectType === 'PDM_FILEVERSION') {
                    vm.selectedFile = child;
                }

                if (vm.selectedFile != null && vm.selectedFile.attachedToObject == null) {
                    PDMVaultService.getAttachedToObject(child.id).then(
                        function (data) {
                            vm.selectedFile.attachedToObject = data;
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
            }

            vm.hideDetails = function () {
                vm.selectedFile = null;
            };

            $window.openVaultFolder = openFolder;
            function openFolder(path) {
                $state.transitionTo('app.pdm.vaultdetails', {
                    vaultId: vm.vaultId, path: path
                }, {notify: true});
            }

            $window.goToAllVaults = goToAllVaults;
            function goToAllVaults() {
                $state.transitionTo('app.pdm.vaults', {}, {notify: true});
            }

            vm.getThumbnailUrl = getThumbnailUrl;
            function getThumbnailUrl(fileVersion) {
                return "api/pdm/vaults/0/folders/{0}/files/{1}/thumbnail".format(fileVersion.file.folder, fileVersion.id);
            }

            function fileSizeToString(bytes) {
                if (bytes === 0) {
                    return "0.00 B";
                }
                var e = Math.floor(Math.log(bytes) / Math.log(1024));
                return (bytes / Math.pow(1024, e)).toFixed(2) + ' ' + ' KMGTP'.charAt(e) + 'B';
            }

            vm.getBreadCrumbsLink = getBreadCrumbsLink;
            $window.getBreadCrumbsLink = getBreadCrumbsLink;
            function getBreadCrumbsLink(vault, folder) {
                var link = "";

                var ngClick = "goToAllVaults()";
                link += "<a href='javascript:void(0);' onclick=\"" + ngClick + "\">" + "All Vaults" + "</a>";
                link += " &raquo; ";

                ngClick = "openVaultFolder(null)";
                link += "<a href='javascript:void(0);' onclick=\"" + ngClick + "\">" + vault.name + "</a>";

                var idPath = folder.idPath;
                var namePath = folder.namePath;

                if (idPath != null && namePath != null) {
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

            vm.showAssemblyDetails = showAssemblyDetails;
            function showAssemblyDetails(event, fileVersion) {
                event.preventDefault();
                event.stopPropagation();
                PDMVaultService.getAttachedToObject(fileVersion).then(
                    function (data) {
                        $state.go('app.pdm.assemblies.details', {assemblyId: data.id});
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            vm.showPartDetails = showPartDetails;
            function showPartDetails(event, fileVersion) {
                event.preventDefault();
                event.stopPropagation();
                PDMVaultService.getAttachedToObject(fileVersion).then(
                    function (data) {
                        $state.go('app.pdm.parts.details', {partId: data.id});
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            vm.showInKistersWebViewer = showInKistersWebViewer;
            function showInKistersWebViewer(event, fileVersion) {
                event.stopPropagation();
                if (fileVersion != null) {
                    vm.showViewerContainer = true;
                    vm.kistersViewer = true;
                    showKistersContainer(fileVersion);
                }
                $('body').click();
            }

            $scope.conWidth = 100;
            function showKistersContainer(fileVersion) {
                var path1 = $rootScope.file_system_path + '' + fileVersion.idPath;
                $scope.kisterCallBack(fileVersion.file, path1, $('#WebViewFrame'));
                $('body').click();
            }

            $scope.registerKisterCallBack = function (callback) {
                $scope.kisterCallBack = callback;
            };

            vm.showVisualization = showVisualization;
            function showVisualization(event, fileVersion) {
                event.stopPropagation();

                if (fileVersion != null) {
                    vm.showViewerContainer = true;
                    if (fileVersion.visualizationId === null) {
                        generateVisualization(fileVersion);
                    }
                    else {
                        showForgeViewer(fileVersion);
                    }
                }
                $('body').click();
            }

            function generateVisualization(fileVersion) {
                PDMVaultService.generateVisualization(fileVersion.id).then(
                    function (data) {
                        fileVersion = data;
                        showForgeViewer(fileVersion);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function showForgeViewer(fileVersion) {
                vm.showViewer = true;
                var urn = fileVersion.visualizationId;
                var fileUrl = "app/assets/bower_components/cassini-platform/app/desktop/directives/autodeskForge/forgeView.html?url=" +
                    $application.forgeToken + "&urn=" + urn;
                $('#forgeFrame').attr('src', fileUrl);
            }


            vm.closeViewerContainer = closeViewerContainer;
            function closeViewerContainer() {
                $('#forgeFrame').attr('src', '');
                $('#WebViewFrame').attr('src', '');
                vm.showViewerContainer = false;
                vm.showViewer = false;
                vm.kistersViewer = false;
            }

            vm.freeTextSearch = freeTextSearch;
            function freeTextSearch(freeText) {
                if (freeText != null && freeText != "" && freeText != undefined) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page = 0;
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadDetails();
                } else {
                    resetPage();
                }
            }

            vm.resetPage = resetPage;
            function resetPage() {
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                vm.pageable.page = 0;
                loadDetails();
            }

            vm.resizeContainer = resizeContainer;
            function resizeContainer() {
                $application.viewType = vm.viewType;
                $timeout(function () {
                    window.dispatchEvent(new Event('resize'));
                }, 1000);
            }

            (function () {
                if ($application.viewType != null && $application.viewType != "" && $application.viewType != undefined) {
                    vm.viewType = $application.viewType;
                }
                loadVault();
            })();
        }
    }
);
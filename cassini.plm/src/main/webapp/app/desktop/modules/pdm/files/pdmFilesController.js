define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'app/shared/services/core/pdmVaultService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/kistersWebView/kisterWebViewDirective'
    ],
    function (module) {
        module.controller('PDMFilesController', PDMFilesController);

        function PDMFilesController($scope, $rootScope, $timeout, $sce, $state, $stateParams, $window, $application, $cookieStore, $translate,
                                    PDMVaultService) {
            $rootScope.viewInfo.showDetails = false;

            var vm = this;

            vm.selectedFile = null;
            vm.showDetailsPanel = false;
            vm.showViewerContainer = false;
            vm.showViewer = false;
            vm.kistersViewer = false;
            $scope.freeTextQuery = null;
            vm.filterSearch = null;
            vm.searchText = null;

            vm.filters = {
                fileType: $stateParams.fileType,
                searchQuery: null
            };

            vm.pageable = {
                page: 0,
                size: 50,
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

            vm.files = angular.copy(pagedResults);
            vm.filesList = vm.files.content;

            vm.nextPage = nextPage;
            function nextPage() {
                if (vm.files.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadFiles();
                }
            }

            vm.previousPage = previousPage;
            function previousPage() {
                if (vm.files.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadFiles();
                }
            }

            vm.resetPage = resetPage;
            function resetPage() {
                vm.files = angular.copy(pagedResults);
                vm.filesList = vm.files.content;
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                //loadFiles();
            }

            vm.freeTextSearch = freeTextSearch;
            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                vm.files = angular.copy(pagedResults);
                vm.filesList = vm.files.content;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    $scope.freeTextQuery = freeText;
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    vm.searchText = freeText;
                    loadFiles();
                } else {
                    loadFiles();
                }
            }

            function loadFiles() {
                vm.loading = true;
                vm.selectedFile = null;

                $rootScope.showBusyIndicator();
                PDMVaultService.search(vm.filters, vm.pageable).then(
                    function (data) {
                        vm.files = data;
                        vm.filesList = vm.filesList.concat(vm.files.content);
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            vm.getThumbnailUrl = getThumbnailUrl;
            function getThumbnailUrl(fileVersion) {
                return "api/pdm/vaults/0/folders/{0}/files/{1}/thumbnail".format(fileVersion.file.folder, fileVersion.id);
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
                //if (vm.selectedFile != null) $scope.conWidth = 79;
                var path1 = $rootScope.file_system_path + '' + fileVersion.idPath;
                $scope.kisterCallBack(fileVersion.file, path1, $('#WebViewFrame'));
                $('body').click();
            }

            $scope.registerKisterCallBack = function (callback) {
                $scope.kisterCallBack = callback;
            };

            vm.showAssemblyDetails = showAssemblyDetails;
            function showAssemblyDetails(event, fileVersion) {
                event.preventDefault();
                event.stopPropagation();
                if (fileVersion.file.fileType == "ASSEMBLY") {
                    PDMVaultService.getAttachedToObject(fileVersion.id).then(
                        function (data) {
                            $state.go('app.pdm.assemblies.details', {assemblyId: data.id});
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }


            }

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

            vm.fileSizeToString = fileSizeToString;
            function fileSizeToString(bytes) {
                if (bytes === 0) {
                    return "0.00 B";
                }
                var e = Math.floor(Math.log(bytes) / Math.log(1024));
                return (bytes / Math.pow(1024, e)).toFixed(2) + ' ' + ' KMGTP'.charAt(e) + 'B';
            }

            vm.hideDetails = function () {
                vm.selectedFile = null;
            };

            vm.onClickChild = onClickChild;
            function onClickChild(child) {
                vm.selectedFile = child;
            }

            (function () {
                loadFiles();
            })();
        }
    }
);
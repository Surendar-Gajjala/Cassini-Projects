define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/fileService',
        'app/shared/services/onshapeService'
    ],
    function (module) {
        module.controller('HomeController', HomeController);

        function HomeController($scope, $rootScope, $timeout, $interval, $state, $cookies, $uibModal, $application,
                                FileService, OnshapeService) {

            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = "Home";
            $rootScope.viewInfo.showDetails = true;

            var pageable = {
                page: 0,
                size: 100,
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
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.files = [];//angular.copy(pagedResults);
            vm.searchText = null;


            vm.search = search;
            vm.showFileHistory = showFileHistory;
            vm.translateToOnshape = translateToOnshape;
            vm.getFileUrl = getFileUrl;
            $scope.imgError = imgError;

            function search() {
                if (vm.searchText == "") {
                    //vm.files = angular.copy(pagedResults);
                    loadFiles();
                }
                else {
                    FileService.search(vm.searchText).then(
                        function (data) {
                            angular.forEach(data.content, function (file) {
                                file.onshapeIds = null;
                            });
                            vm.files = data.content;
                            loadOnshapeIds();
                        }
                    );
                }
            }

            function showFileHistory(fileId) {
                var options = {
                    title: 'File History',
                    template: 'app/desktop/modules/vaults/details/fileHistoryView.jsp',
                    controller: 'FileHistoryController as fileHistoryVm',
                    resolve: 'app/desktop/modules/vaults/details/fileHistoryController',
                    data: {
                        folderId: 365,
                        fileId: fileId
                    },
                    callback: function (msg) {
                        console.log(msg);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function imgError(img) {
                img.src = 'app/assets/images/sldprt.png';
                $timeout(function () {
                    var id = img.id;
                    console.log(id);
                    var file = getFileById(id);
                    img.src = "https://cad.onshape.com/api/thumbnails/d/{0}/w/{1}/e/{2}/s/300x300?t={3}"
                        .format(file.onshapeIds.documentId, file.onshapeIds.workspaceId, file.onshapeIds.elementId, new Date().getDate());
                    img.onerror = "this.src='app/assets/images/sldprt.png';angular.element(document.getElementById('searchResults')).scope().imgError(this);";
                }, 10000);
            }

            function getFileById(id) {
                var found = null;
                angular.forEach(vm.files, function (file) {
                    if (file.id == id) {
                        found = file;
                    }
                });
                return found;
            }

            function getFileUrl(file) {
                //var url = "https://cad.onshape.com/api/thumbnails/d/{0}/w/{1}/s/300x300?t={2}"
                 //   .format(file.onshapeIds.documentId, file.onshapeIds.workspaceId, new Date().getDate());
                var url = "api/pdm/vaults/folders/{0}/files/{1}/thumbnail".format(file.folder, file.id);
                return url;
            }

            function loadFiles() {
                vm.files = [];//angular.copy(pagedResults);
                $rootScope.showBusyIndicator();
                FileService.getFiles(pageable).then(
                    function (data) {
                        angular.forEach(data, function (file) {
                            file.onshapeIds = null;
                        });
                        vm.files = data;
                        loadOnshapeIds();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function loadOnshapeIds() {
                var map = new Hashtable();
                var ids = [];
                angular.forEach(vm.files, function (file) {
                    if((file.onshapeIds == null || file.onshapeIds == undefined) &&
                        (file.name.toLowerCase().endsWith(".sldprt") ||
                            file.name.toLowerCase().endsWith(".sldasm") ||
                            file.name.toLowerCase().endsWith(".dxf"))) {
                        map.put(file.id, file);
                        ids.push(file.id);
                    }
                });

                if(ids.length > 0) {
                    OnshapeService.getOnshapeIds(ids).then(
                        function (data) {
                            angular.forEach(data, function (idMap) {
                                var file = map.get(idMap.fileId);
                                if (file != null && file.onshapeIds == undefined && file.onshapeIds == null) {
                                    file.onshapeIds = idMap.onshapeIds;
                                }
                            });
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
                else {
                    $rootScope.hideBusyIndicator();
                }
            }

            function translateToOnshape(pdmFile) {

                if (pdmFile.onshapeIds != null) {
                    $rootScope.showWarningMessage("This file has already been uploaded to Onshape");
                    return;
                }

                $rootScope.showBusyIndicator();
                OnshapeService.translateFile(pdmFile.id).then(
                    function (data) {
                        $rootScope.showSuccessMessage("File '" + pdmFile.name + "' successfully uploaded to Onshape!");
                        pdmFile.onshapeIds = data;
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var onshapeInterval = null;
            $scope.$on('$viewContentLoaded', function () {
                $timeout(function () {
                    $application.homeLoaded = true;
                    window.$("#preloader").hide();
                    window.$("#appview").show();

                    if($application.login != null && $application.login != undefined) {
                        onshapeInterval = $interval(function () {
                            loadOnshapeIds();
                        }, 10 * 1000);

                        loadFiles();
                    }
                }, 1000);

                $scope.$on('$destroy', function() {
                    if(onshapeInterval != null) {
                        $interval.cancel(onshapeInterval);
                    }
                });
            });
        }
    }
);
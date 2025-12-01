define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'app/desktop/modules/pdm/assemblies/details/tabs/basic/basicController',
        'app/desktop/modules/pdm/assemblies/details/tabs/visualization/visualizationController',
        'app/shared/services/core/pdmService',
        'app/shared/services/core/pdmVaultService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.controller('AssemblyDrawingController', AssemblyDrawingController);

        function AssemblyDrawingController($scope, $rootScope, $timeout, $state, $stateParams, $sce, $cookies, $window,
                                           httpFactory, CommonService, PDMService, PDMVaultService) {
            var vm = this;

            vm.selectedItem = null;
            vm.fileVersion = null;
            vm.fileVersions = [];
            var dataLoaded = false;

            var PDFJS;
            var pdfDoc = null;
            var pdfScale = 1;
            var canvasContainer;
            var options;
            vm.isFullScreen = false;
            vm.pdfUrl = null;
            vm.pdfFileName = null;

            vm.fullScreenTitle = "Full Screen";

            function renderPDF() {
                if(vm.selectedItem.drawingRevision === null) return;

                PDFJS = window['pdfjs-dist/build/pdf'];

                // The workerSrc property shall be specified.
                PDFJS.GlobalWorkerOptions.workerSrc = '/app/assets/bower_components/pdfjs-dist/build/pdf.worker.js';

                var selectedItem = $scope.selectedItem;
                var name = selectedItem.name;
                var index = name.lastIndexOf('.');
                if(index != -1) {
                    vm.pdfFileName = name.substring(0, index) + ".pdf";
                }
                vm.pdfUrl = "api/pdm/core/assemblies/{0}/pdf".format(vm.selectedItem.id);
                if(selectedItem['@type'] === "AssemblyDTO") {
                    vm.pdfUrl = "api/pdm/core/assemblies/{0}/pdf".format(vm.selectedItem.id);
                }
                else if(selectedItem['@type'] === "PartDTO") {
                    vm.pdfUrl = "api/pdm/core/parts/{0}/pdf".format(vm.selectedItem.id);
                }
                else {
                    vm.pdfUrl = "api/pdm/core/assemblies/{0}/pdf".format(vm.selectedItem.id);
                }

                $("#drawingView").empty();
                canvasContainer = document.getElementById('drawingView');
                options = { scale: pdfScale };

                //PDFJS.disableWorker = true;
                PDFJS.getDocument(vm.pdfUrl).promise.then(renderPages);
            }

            async function renderPages(doc) {
                pdfDoc = doc;
                $("#drawingView").empty();
                for(var num = 1; num <= pdfDoc.numPages; num++)
                    await pdfDoc.getPage(num).then(renderPage);
            }


            function renderPage(page) {
                var viewport = page.getViewport(options);
                var wrapper = document.createElement("div");
                wrapper.className = "canvas-wrapper";

                var newScale = ((canvasContainer.offsetWidth-40) / viewport.width);
                viewport = page.getViewport({scale: newScale});

                var canvas = document.createElement('canvas');
                var ctx = canvas.getContext('2d');
                var renderContext = {
                    canvasContext: ctx,
                    viewport: viewport
                };

                canvas.height = viewport.height;
                canvas.width = viewport.width;
                wrapper.appendChild(canvas);
                canvasContainer.appendChild(wrapper);

                page.render(renderContext);
            }

            vm.zoomOut = zoomOut;
            function zoomOut() {
                pdfScale = pdfScale + 0.25;
                options.scale = pdfScale;
                renderPages(pdfDoc);
            }

            vm.zoomIn = zoomIn;
            function zoomIn() {
                if (pdfScale <= 0.25) {
                    return;
                }
                pdfScale = pdfScale - 0.25;
                options.scale = pdfScale;
                renderPages(pdfDoc);
            }

            vm.zoomFull = zoomFull;
            function zoomFull() {
                pdfScale = 1;
                options.scale = pdfScale;
                renderPages(pdfDoc);
            }

            vm.toggleFullScreen = toggleFullScreen;
            function toggleFullScreen() {
                if (document.fullscreenEnabled) {
                    vm.isFullScreen = !vm.isFullScreen;
                    if(vm.isFullScreen) {
                        document.querySelector("#pdfViewContainer").requestFullscreen();
                        vm.fullScreenTitle = "Exit Full Screen"
                    }
                    else {
                        document.exitFullscreen();
                        vm.fullScreenTitle = "Full Screen"
                    }
                }
            }

            vm.downloadDrawing = downloadDrawing;
            function downloadDrawing() {
                var downloadUrl = vm.pdfUrl + "/download";
                httpFactory.get(downloadUrl).then(function(data) {
                    var url = window.URL.createObjectURL(new Blob([data]));
                    $('#fileDownloader')[0].href = url;
                    $('#fileDownloader')[0].click();
                });
            }

            function loadDrawing() {
                var selectedItem = $scope.selectedItem;
                if(selectedItem['@type'] === "AssemblyDTO") {
                    vm.selectedItem = selectedItem.assembly;
                }
                else if(selectedItem['@type'] === "PartDTO") {
                    vm.selectedItem = selectedItem.part;
                }
                else {
                    vm.selectedItem = selectedItem;
                }
                $timeout(function () {
                    renderPDF();
                }, 1000);
            }

            (function () {
                $scope.$on("drawing.tab.selected", function(event, data) {
                    loadDrawing();
                });

                $scope.$on("app.assembly.bomitem.selected", function(event, data) {
                    loadDrawing();
                });

                document.addEventListener("fullscreenchange", function (event) {
                    if (document.fullscreenElement) {
                        $('#pdfViewContainer').addClass('full-screen');
                        renderPages(pdfDoc);
                        vm.fullScreenTitle = "Exit Full Screen"
                    } else {
                        $('#pdfViewContainer').removeClass('full-screen');
                        renderPages(pdfDoc);
                        vm.isFullScreen = false;
                        vm.fullScreenTitle = "Full Screen"
                    }
                });
            })();
        }
    }
);
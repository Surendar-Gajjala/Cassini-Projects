define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'app/desktop/modules/pdm/parts/details/tabs/basic/basicController',
        'app/desktop/modules/pdm/parts/details/tabs/visualization/visualizationController',
        'app/shared/services/core/pdmService',
        'app/shared/services/core/pdmVaultService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('PartDrawingController', PartDrawingController);

        function PartDrawingController($scope, $rootScope, $timeout, $state, $stateParams, $sce, $cookies, $window,
                                           CommonService, PDMService, PDMVaultService) {
            var vm = this;

            vm.selectedItem = null;
            vm.fileVersion = null;
            vm.fileVersions = [];
            var dataLoaded = false;

            var PDFJS;

            function renderPDF() {
                if(vm.selectedItem.drawingRevision === null) return;

                PDFJS = window['pdfjs-dist/build/pdf'];

                // The workerSrc property shall be specified.
                PDFJS.GlobalWorkerOptions.workerSrc = '/app/assets/bower_components/pdfjs-dist/build/pdf.worker.js';
                var url = "api/pdm/core/parts/{0}/pdf".format(vm.selectedItem.id);

                $("#drawingView").empty();
                var canvasContainer = document.getElementById('drawingView');

                var options = { scale: 1 };

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

                function renderPages(pdfDoc) {
                    for(var num = 1; num <= pdfDoc.numPages; num++)
                        pdfDoc.getPage(num).then(renderPage);
                }

                //PDFJS.disableWorker = true;
                PDFJS.getDocument(url).promise.then(renderPages);
            }

            function loadDrawing() {
                vm.selectedItem = $scope.selectedItem;
                $timeout(function () {
                    renderPDF();
                }, 1000);
            }

            (function () {
                $scope.$on("drawing.tab.selected", function(event, data) {
                    loadDrawing();
                });

                $scope.$on("app.part.selected", function(event, data) {
                    loadDrawing();
                });
            })();
        }
    }
);
<div>
    <style scoped>

        .pdf-toolbar {
            width: 100%;
            height: 50px;
            background-color: #fff;
            border-bottom: 1px solid #ddd;
            border-top: 0 !important;
            position: absolute;
            top: 50px;
        }

        .full-screen .pdf-toolbar {
            top: 0;
            position: fixed;
        }

        .pdf-toolbar .display-table {
            width: 100%;
        }

        .full-screen {
            overflow-y: auto;
        }

        .la-arrows-alt {
            display: inline;
        }

        .full-screen .la-arrows-alt {
            display: none;
        }

        .la-compress-arrows-alt {
            display: none;
        }

        .full-screen .la-compress-arrows-alt {
            display: inline;
        }

        .full-screen .pdf-toolbar {

        }

        .pdf-toolbar h4 {
            position: absolute;
            left: 0;
            width: 100%;
            top: 0;
            text-align: center;
            font-weight: normal !important;
            font-size: 16px;
        }

        .view-toolbar .btn {
            background-color: #fff !important
        }

        .view-toolbar .btn:hover {
            background-color: #eeeeee !important;
        }

        .drawing-view {
            background: #eee;
            padding: 20px;
            overflow-y: auto;
            margin-top: 50px;
        }

        .drawing-view .canvas-wrapper {
            margin-bottom: 20px;
        }

        .drawing-view .canvas-wrapper:last-child {
            margin-bottom: 0;
        }

        .drawing-view .canvas-wrapper canvas{
            margin: 0 auto;
            display: block;
        }

    </style>

    <!--
    <div id="drawingView" class="drawing-view">
        <div id="viewer" class="pdfViewer"></div>
    </div>
    -->

    <div id="pdfViewContainer" class="">
        <div class="view-toolbar pdf-toolbar">
            <div class="display-table">
                <div style="z-index: 999;position: relative;">
                    <button class="display-cell btn btn-sm" title="Zoom In" ng-click="drawingVm.zoomIn();">
                        <i class="las la-search-plus"></i>
                    </button>

                    <button class="display-cell btn btn-sm" title="Zoom Out" ng-click="drawingVm.zoomOut();">
                        <i class="las la-search-minus"></i>
                    </button>

                    <button class="display-cell btn btn-sm" title="Zoom Full" ng-click="drawingVm.zoomFull();">
                        <i class="las la-compress"></i>
                    </button>

                    <button class="display-cell btn btn-sm" title="Download" ng-click="drawingVm.downloadDrawing();">
                        <i class="las la-download"></i>
                    </button>

                    <button class="display-cell btn btn-sm" title="{{drawingVm.fullScreenTitle}}" ng-click="drawingVm.toggleFullScreen();">
                        <i class="las la-arrows-alt"></i>
                        <i class="las la-compress-arrows-alt"></i>
                    </button>
                </div>
                <h4>{{drawingVm.pdfFileName}}</h4>
            </div>
        </div>
        <div id="drawingView" class="drawing-view" ng-if="drawingVm.selectedItem.drawingRevision.pdfFile != null"></div>
    </div>
    <div ng-if="drawingVm.selectedItem.drawingRevision.pdfFile == null">
        <div class="no-data">
            <img src="app/assets/no_data_images/no-drawing.png" alt="" class="image">
            <div class="message">No Drawing</div>
            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
        </div>
    </div>
    <a id="fileDownloader" href="" download="{{drawingVm.pdfFileName}}" style="display: none"></a>
</div>
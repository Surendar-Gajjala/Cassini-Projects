<div>
    <style scoped>

        .drawing-view {
            background: #eee;
            padding: 20px;
            overflow-y: auto;
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
    <div id="drawingView" class="drawing-view" ng-if="drawingVm.selectedItem.drawingRevision.pdfFile != null">
    </div>
    <div ng-if="drawingVm.selectedItem.drawingRevision.pdfFile == null">
        <div class="no-data">
            <img src="app/assets/no_data_images/no-drawing.png" alt="" class="image">
            <div class="message">No Drawing</div>
            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
        </div>
    </div>
</div>
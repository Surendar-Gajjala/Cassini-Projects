<div>
    <style scoped>
        .drop-area {
            height: 300px;
            border: 2px dashed #ccc;
            margin: 30px;
            cursor: pointer;
        }
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <div style="margin-top: 50px;height:500px;">
        <div class="drop-area">
            <div id="qualityReplaceFiles" style="font-style: italic; text-align: center; line-height: 280px">
                {{"DROP_FILES_OR_CLICK" | translate}}
                <a href="" ng-click="objectFileReplaceVm.selectFiles()"></a>
            </div>
            <div id="attachmentsDropZone" style="display: table; width: 100%;">
                <div id="replacePreviews">
                    <div id="replaceTemplate">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
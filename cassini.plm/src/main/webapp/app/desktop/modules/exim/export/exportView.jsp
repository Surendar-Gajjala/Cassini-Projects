<div>
    <style scoped>
        .options-container {
            display: flex;
            flex-wrap: wrap;
            padding: 20px;
        }

        .option-block {
            width: 30%;
            border: 1px solid #ddd;
            min-height: 100px;
            max-height: 100px;
            margin: 10px;
            padding: 10px;
            cursor: pointer;
            position: relative;
        }

        .option-block.selected::before,
        .option-block.selected::after {
            content: '';
            position: absolute;
            top: 0;
            right: 0;
            border-color: transparent;
            border-style: solid;
        }

        .option-block.selected::after {
            border-width: 18px;
            border-right-color: #718490;
            border-top-color: #718490;
        }

        .option-block h5 {
            margin: 0;
        }

        .option-block p {
            margin-top: 10px;
        }

    </style>

    <div id="export-view" class="view-container" fitcontent>
        <div class="view-toolbar">
            <div class="btn-group">
                <button class="btn btn-sm" title="Export selected objects"
                        ng-click="exportVm.exportData()"
                        translate>EXPORT
                </button>
            </div>
        </div>
        <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
            <div class="container">
                <br>

                <div class="text-center">
                    <i>Click to select the objects to export.</i>
                </div>
                <div class="options-container">
                    <div class="option-block" ng-repeat="option in exportVm.options"
                         ng-click="option.selected = !option.selected"
                         ng-class="{'selected': option.selected }">
                        <h5>{{option.name}}</h5>

                        <p>{{option.description}}</p>
                    </div>
                </div>
            </div>
            <iframe id="exportIframe"
                    ng-src="{{exportVm.exportUrl}}"
                    width="0" height="0" frameborder="0">
            </iframe>
        </div>
    </div>
</div>
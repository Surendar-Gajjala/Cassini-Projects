<div class="view-container" fitcontent>
    <style scoped>
        .help-root {
            background: transparent url("app/assets/images/manual.png") no-repeat !important;
            height: 16px;
        }
        .help-root + .tree-title {
            font-weight: bold !important;
        }
        .help-toc {
            background: transparent url("app/assets/images/book1.png") no-repeat !important;
            height: 16px;
        }
        .help-file {
            background: transparent url("app/assets/images/document.png") no-repeat !important;
            height: 16px;
        }
        .tree-node .tree-title {
            margin-left: 5px;
            text-transform: uppercase;
        }
        .whtbtnshow {
            display: none !important;
        }
    </style>
    <div class="view-content no-padding">
        <div class="split-pane fixed-left">

            <div class="split-pane-component split-left-pane" style="min-width: 305px;">
                <div class="classification-pane">
                    <ul id="helpTree" class="easyui-tree"></ul>
                </div>
            </div>

            <div class="split-pane-divider"></div>

            <div class="split-pane-component split-right-pane noselect" style="left: 300px;">
                <iframe id="helpContentsFrame"
                        src="{{helpDefaultView}}"
                        onload="updateIframeCss()"
                        frameborder="0" height="100%" width="100%"></iframe>
            </div>
        </div>
    </div>
</div>

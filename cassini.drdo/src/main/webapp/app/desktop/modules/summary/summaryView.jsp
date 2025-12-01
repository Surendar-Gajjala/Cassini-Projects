<style scoped>
    .fixed-left:after {
        content: '';
        height: 100%;
        position: absolute;
        border: 1px solid #eeeeee;
        top: 0;
        background: #EEE;
        left: 310px;
        width: 5px;
    }

    .creative-gradient {
        background-image: url(), linear-gradient(to bottom, rgba(21,123,143,1) 0%, rgba(53,172,156,1) 100%);
        background-position: right, 50%;
        background-size: contain, 100% 100%;
    }
</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar">

    </div>

    <div class="view-content no-padding">
        <div class="split-pane fixed-left">
            <div class="split-pane-component" style="width: 300px;top:10px;left: 12px;">
                <ul id="bomTree" class="easyui-tree" close-text="Close"></ul>
            </div>

            <div id="summaryContainer" class="split-pane-component split-right-pane noselect" style="left: 308px;">
                <div ng-if="summaryVm.selectedItem.objectType == 'BOM'"
                     ng-include="'app/desktop/modules/summary/systemSummaryView.jsp'"></div>
                <div ng-if="summaryVm.selectedItem.objectType == 'BOMINSTANCE'"
                     ng-include="'app/desktop/modules/summary/missileSummaryView.jsp'"></div>
            </div>
        </div>
    </div>
</div>
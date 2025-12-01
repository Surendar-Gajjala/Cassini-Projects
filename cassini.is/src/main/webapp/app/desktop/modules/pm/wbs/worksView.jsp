<div>
    <style scoped>
        .gantt_row_project {
            font-weight: bold;
        }

        .gantt-info ul {
            line-height: 150%;
        }

        .gantt_row_placeholder i.action-btn {
            display: none;
            cursor: pointer !important;
        }

        .gantt_row_project .gantt_cell {
            font-weight: bolder !important;
        }

        .gant-editor-frame {
            width: 100%;
            height: 100%;
        }

        .action-btn {
            color: #a1a4a5;
            margin-right: 5px;
            cursor: pointer;
        }

        .gantt_task_progress {
            text-align: left !important;
            padding-left: 10px !important;
        }

        .gantt_tooltip {
            display: none;
            background-color: rgb(248, 228, 147);
            border-radius: 5px;
        }

        .gantt_task_progress_drag {
            display: none !important;
        }

        .gantt_grid_data .gantt_row_placeholder {
            border-bottom: 1px solid #d7d7d7 !important;
        }
        .gantt_task_progress {
            background-color: rgb(54, 54, 54);
            opacity: 0.9;
        }
        .gantt_grid_head_add{
            display: none;
        }
    </style>

    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <div class="btn-group">
                <button class="btn btn-sm btn-default" ng-click="worksVm.saveGantt()">
                    Save
                </button>
            </div>
            <div class="btn-group">
                <button class="btn btn-sm btn-default" ng-click="worksVm.expandAll()">
                    Expand All
                </button>
                <button class="btn btn-sm btn-default" ng-click="worksVm.collapseAll()">
                    Collapse All
                </button>
            </div>
            <div class="btn-group">
                <button class="btn btn-sm btn-default"
                        ng-click="worksVm.showGantt = !worksVm.showGantt;worksVm.toggleGantt(worksVm.showGantt)">
                    {{worksVm.showGantt ? 'Hide Gantt' : 'Show Gantt'}}
                </button>
                <button class="btn btn-sm btn-default" ng-click="worksVm.toggleMode()" ng-disabled="!worksVm.showGantt">
                    {{worksVm.zoomed ? 'Reset Zoom' : 'Zoom to Fit'}}
                </button>
            </div>

        </div>
        <div class="view-content no-padding">
            <div id="gantt_here" style='width:100%; height:100%;'></div>
            <%--<iframe class="gant-editor-frame" src="/app/assets/js/gantt/gantt-editor.html" frameborder="0"></iframe>--%>
        </div>
    </div>
</div>
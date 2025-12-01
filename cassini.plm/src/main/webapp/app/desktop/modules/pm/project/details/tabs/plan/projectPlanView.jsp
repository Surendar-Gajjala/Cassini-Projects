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

        .gantt_task_progress_drag {
            display: none !important;
        }

        .gantt_grid_data .gantt_row {
            background-color: #f9fbfe !important;
        }

        .gantt_task_row {
            background-color: #f9fbfe !important;
        }

        .gantt_grid_data {
            background-color: #f9fbfe !important;
        }

        .gantt_data_area {
            background-color: #f9fbfe !important;
        }

        .gantt_grid_scale {
            background-color: #f9fbfe !important;
        }

        .gantt_task_scale {
            background-color: #f9fbfe !important;
        }

        .gantt_grid_data .gantt_row_placeholder {
            border-bottom: 1px solid #d7d7d7 !important;
        }

        .gantt_task_progress {
            background-color: rgb(54, 54, 54);
            opacity: 0.9;
        }

        .nested_task .gantt_add {
            display: none !important;
        }

        .hide_icon #icon {
            display: none !important;
        }

        .gantt_grid_data .gantt_row.gantt_selected {
            background-color: #fff3a1 !important;
        }

        .gantt_tooltip {
            background-color: rgb(248, 228, 147);
            border-radius: 5px;
            z-index: 9999;
        }

        .gantt_grid_head_object_type {
            text-align: left;
            margin-left: 5px;
        }

        .gantt_task_cell.week_end {
            background-color: #EFF5FD !important;
        }

        .gantt_layout_cell {
            padding: 1px;
        }

    </style>
    <div id="gantt_here" style='width:100%;'></div>

    <ul id="contextMenu" class="dropdown-menu" role="menu">
        <li><a tabindex="-1" href="" ng-click="projectPlanVm.openAssignedTo()" translate>ASSIGNED_TO</a></li>
        <li><a tabindex="-1" href="" ng-if="!loginPersonDetails.external && !projectShared" ng-click="projectPlanVm.shareSelectedItems()" translate>SHARE</a></li>
    </ul>

    <div class="modal fade modal1" id="assignedToModal" tabindex="-1" role="dialog"
         aria-labelledby="assignedToModalLabel"
         data-backdrop="false"
         style="display: none!important;">
        <div class="modal-dialog" role="document">
            <div class="modal-content content1">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            ng-click="projectPlanVm.closeAssignedToModel()">&times;</button>
                    <h4 class="modal-title" style="text-align: center !important;" id="assignedToModalLabel" translate>
                        ASSIGNED_TO_TEAM</h4>
                </div>
                <div class="modal-body" style="padding: 50px; overflow: inherit;">
                    <ui-select ng-model="projectPlanVm.assignedTo"
                               theme="bootstrap" style="width:100%;">
                        <ui-select-match placeholder="{{projectPlanVm.select}}">{{$select.selected.label}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="person in projectPlanVm.persons | filter: $select.search">
                            <div ng-bind="person.label"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-success" ng-disabled="projectPlanVm.assignedTo == null"
                            ng-click="projectPlanVm.updateAssignTo()"
                            data-dismiss="modal" translate>ASSIGN
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
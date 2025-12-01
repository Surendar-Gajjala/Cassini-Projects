<div class="view-container" fitcontent>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;margin-top: -6px !important;">
        <div style="display: inline-flex;border-bottom: 1px solid lightgrey;padding-bottom: 8px;margin-left: -1px !important;width: 100%;"
             class="row">
            <div style="width: 50%; margin-left: 10px;margin-right: 20px;">
                <ui-select class="required-field" ng-model="taskSelectVm.newStockIssue.project"
                           theme="bootstrap" on-select="taskSelectVm.loadProjectWBS($item)">
                    <ui-select-match placeholder="Select Project">{{$select.selected.name}}</ui-select-match>
                    <ui-select-choices repeat="project in taskSelectVm.projects | filter: $select.search"
                                       style="max-height: 120px;">
                        <div ng-bind="project.name | highlight: $select.name.search"></div>
                    </ui-select-choices>
                </ui-select>
            </div>

            <div style="width: 50%; margin-left: 10px;margin-right: 20px;">
                <div class="input-group">
                    <input type="text" class="form-control" name="title"
                           ng-model="taskSelectVm.wbs.name" readonly/>

                    <div class="input-group-btn" uib-dropdown>
                        <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button"
                                ng-click="$event.stopPropagation();">
                            Select WBS<span class="caret" style="margin-left: 4px;"></span>
                        </button>
                        <div class="dropdown-menu" role="menu">
                            <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                                <div class="classification-pane">
                                    <ul id="wbsTree" class="easyui-tree" on-select-wbs="taskSelectVm.onSelectWbs"></ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row" style="padding:10px;">
            <div class="responsive-table">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr>
                        <th style="width: 40px;">Select</th>
                        <th style="width: 200px;">Task Name</th>
                        <th style="width: 200px;">Description</th>
                        <th style="width: 200px;">Assigned To</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr ng-if="taskSelectVm.loading == true">
                        <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading Tasks</span>
                    </span>
                        </td>
                    </tr>
                    <tr ng-if="taskSelectVm.loading == false && taskSelectVm.taskList.length == 0">
                        <td colspan="12">
                            <span translate>No tasks</span>
                        </td>
                    </tr>

                    <tr ng-repeat="task in taskSelectVm.taskList"
                        ng-click="task.isChecked = !task.isChecked; taskSelectVm.radioChange(task, $event)"
                        ng-dblClick="task.isChecked = !task.isChecked; taskSelectVm.selectRadioChange(task, $event)">
                        <td style="width: 40px;">
                            <input type="radio" ng-checked="task.isChecked" name="task" value="task"
                                   ng-dblClick="taskSelectVm.selectRadioChange(task, $event)"
                                   ng-click="taskSelectVm.radioChange(task, $event)">
                        </td>
                        <td style="width: 200px;">{{task.name}}</td>
                        <td style="width: 200px;">{{task.description}}</td>
                        <td style="width: 200px;">{{task.personObject.fullName}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
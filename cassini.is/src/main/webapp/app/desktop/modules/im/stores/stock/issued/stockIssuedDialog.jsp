<div style="position: relative;">
    <div style="overflow-y: auto;overflow-x: hidden;">
        <br>

        <div ng-if="stockIssuedVm.taskList == 0">
            <h3 style="text-align: center">No task is assigned to this Store/Site</h3>
        </div>
        <div ng-if="stockIssuedVm.taskList != 0" class="row" style="margin: 0;">
            <div>
                <div class="row">
                    <label class="col-sm-offset-2 col-sm-2 control-label" style="margin-top: 10px;font-size: 150">
                        <span class="asterisk">* </span> Select Task :</label>

                    <div class="col-sm-6">
                        <ui-select class="required-field" ng-model="stockIssuedVm.task"
                                   theme="bootstrap" on-select="stockIssuedVm.selectTask($item)">
                            <ui-select-match placeholder="Select Task">{{$select.selected.name}}</ui-select-match>
                            <ui-select-choices repeat="task in stockIssuedVm.taskList | filter: $select.search">
                                <div ng-bind="task.name | highlight: $select.name.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
            </div>
            <hr style="margin: 9px;">
            <div class="respaonsive-table" style="padding: 10px;">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>
                            <div class="ckbox ckbox-default" style="display: inline-block;">
                                <input id="resource{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                       ng-model="stockIssuedVm.selectedAll"
                                       ng-click="stockIssuedVm.checkAll();">
                                <label for="resource{{$index}}" class="resource-selection-checkbox"></label>
                            </div>
                        </th>
                        <th style="vertical-align: middle;">Item Number</th>
                        <th style="vertical-align: middle;">Resource Type</th>
                        <th style="vertical-align: middle;">Resource Qty</th>
                        <th style="vertical-align: middle;">Inventory</th>
                        <th style="vertical-align: middle;">Shortage</th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr ng-if="stockIssuedVm.taskResources.length == 0">
                        <td colspan="12">
                            <span>No Items</span>
                        </td>
                    </tr>

                    <tr ng-repeat="resource in stockIssuedVm.taskResources">
                        <td>
                            <div class="ckbox ckbox-default">
                                <input id="resource{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                       ng-model="resource.selected" ng-click="stockIssuedVm.select(resource)">
                                <label for="resource{{$index}}" class="resource-selection-checkbox"></label>
                            </div>
                        </td>

                        <td style="vertical-align: middle;">
                            {{resource.referenceIdObject.itemNumber }}
                        </td>
                        <td style="vertical-align: middle;">
                            {{resource.resourceType}}
                        </td>
                        <td style="vertical-align: middle;">
                            {{resource.quantity}}
                        </td>
                        <td style="vertical-align: middle;">
                            {{resource.inventory}}
                        </td>
                        <td style="vertical-align: middle;">
                            {{resource.shortage}}
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</div>
</div>

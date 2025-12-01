<div style="position: relative;">
    <div style="overflow-y: auto;overflow-x: hidden;height: 100%;">

        <div style="margin: 0;">
            <form style="margin-top: 10px;">
                <div class="form-group">

                    <div class="col-sm-6 col-sm-offset-3">
                        <ui-select class="required-field"
                                   ng-change="allocateItemsVm.getUnallocatedProjectInventoryBoq()"
                                   ng-model="allocateItemsVm.project"
                                   theme="bootstrap">
                            <ui-select-match placeholder="Select Project">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="project in allocateItemsVm.projects | filter: $select.search | orderBy: 'name'"
                                    style="max-height: 120px;">
                                <div ng-bind="project.name | highlight: $select.name.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
            </form>
            <div class="responsive-table" style="padding: 10px;margin-bottom: 50px !important;">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th style="vertical-align: middle; width: 150px;">Item Number</th>
                        <th style="vertical-align: middle; width: 150px;">Item Name</th>
                        <th style="vertical-align: middle; width: 200px">Item Description</th>
                        <th style="vertical-align: middle; width: 150px;">Unallocated <br> Qty</th>
                        <th style="vertical-align: middle; max-width: 100px;">Receive <br> Qty</th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr ng-if="allocateItemsVm.project == null">
                        <td colspan="12">
                            <span>Select project to allocate items</span>
                        </td>
                    </tr>

                    <tr ng-if="allocateItemsVm.loading == true">
                        <td colspan="25">
                                    <span style="font-size: 15px;">
                                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                             class="mr5">Loading Items..
                                    </span>
                        </td>
                    </tr>

                    <tr ng-if="allocateItemsVm.items.length == 0 && allocateItemsVm.loading == false && allocateItemsVm.project != null">
                        <td colspan="12">
                            <span>No Items are available to view</span>
                        </td>
                    </tr>

                    <tr ng-repeat="item in allocateItemsVm.items">

                        <td style="vertical-align: middle; width: 150px;">
                            {{item.itemNumber }}
                        </td>
                        <td style="vertical-align: middle;width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                            title="{{item.itemName}}">
                            {{item.itemName}}
                        </td>

                        <td style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                            title="{{item.description}}">
                            <span>{{item.description}}</span>

                        </td>
                        <td style="vertical-align: middle;text-align: center">
                            {{item.storeInventory }}
                        </td>
                        <td style="width: 100px;">
                            <input placeholder="Qty" class="btn-sm form-control" type="text" style="width:60px;"
                                   data-ng-model="item.itemIssueQuantity">
                        </td>
                        <%--<td style="vertical-align: middle;">--%>
                        <%--{{item.totalReceivedQty}}--%>
                        <%--</td>--%>
                        <%--<td style="vertical-align: middle;">--%>
                        <%--{{item.balanceQty}}--%>
                        <%--</td>--%>
                    </tr>
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>



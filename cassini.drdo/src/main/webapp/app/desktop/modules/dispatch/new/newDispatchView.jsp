<div style="position: relative;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }
    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 10px;min-height: 250px;">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title" style="color: black;">Basic Info</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>BOM</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newDispatchVm.newDispatch.bom" theme="bootstrap" style="width:100%"
                                       on-select="newDispatchVm.onSelectBom($item)">
                                <ui-select-match
                                        placeholder="Select BOM">
                                    {{$select.selected.item.itemMaster.itemName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="bom in newDispatchVm.boms track by bom.id">
                                    <div ng-bind="bom.item.itemMaster.itemName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Dispatch Type</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newDispatchVm.newDispatch.type" theme="bootstrap" style="width:100%"
                                       on-select="newDispatchVm.onSelectType($item)"
                                       ng-disabled="newDispatchVm.newDispatch.bom == null">
                                <ui-select-match
                                        placeholder="Select Type">
                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="type in newDispatchVm.dispatchTypes track by type">
                                    <div ng-bind="type"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Dispatch Date</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" date-picker
                                   placeholder="Select Dispatch Date"
                                   ng-model="newDispatchVm.newDispatch.dispatchDate">
                        </div>
                    </div>

                </form>
                <br>
                <h4 ng-if="newDispatchVm.newDispatch.bom != null && newDispatchVm.newDispatch.type != null">
                    Items to Dispatch</h4>

                <div class="responsive-table"
                     ng-if="newDispatchVm.newDispatch.bom != null && newDispatchVm.newDispatch.type != null">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 25px;">
                                <input type="checkbox" ng-model="newDispatchVm.selectAllCheck"
                                       ng-click="newDispatchVm.selectAll()"/>
                            </th>
                            <th>Nomenclature</th>
                            <th>Type</th>
                            <th>UPN</th>
                            <th>Status</th>
                            <th style="text-align: center;">Quantity</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="newDispatchVm.itemsToDispatch.length == 0">
                            <td colspan="15">No items to dispatch.</td>
                        </tr>
                        <tr ng-repeat="itemInstance in newDispatchVm.itemsToDispatch">
                            <td style="width: 25px;">
                                <input type="checkbox" ng-model="itemInstance.selected"
                                       ng-click="newDispatchVm.selectItem(itemInstance)">
                            </td>
                            <td>{{itemInstance.item.itemMaster.itemName}} {{itemInstance.item.partSpec.specName}}</td>
                            <td>{{itemInstance.item.itemMaster.parentType.name}}</td>
                            <td>
                                <span class="badge badge-success"
                                      style="font-size: 14px;">{{itemInstance.upnNumber}}</span>
                            </td>
                            <td>
                                <item-instance-status object="itemInstance"></item-instance-status>
                            </td>
                            <td style="text-align: center">
                                <span class="badge badge-danger" style="font-size: 14px;"
                                      ng-if="itemInstance.item.itemMaster.itemType.hasLots">{{itemInstance.lotSize}}</span>
                                <span class="badge badge-danger" style="font-size: 14px;"
                                      ng-if="!itemInstance.item.itemMaster.itemType.hasLots">1</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

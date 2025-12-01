<div style="position: relative;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }
    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 10px;">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title" style="color: black;">Basic Info</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Dispatch Number</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7" style="margin-top: 10px;">
                            <p>{{dispatchDetailsVm.dispatch.number}}</p>
                        </div>
                    </div>

                    <div class="form-group" style="margin-top: 10px;">
                        <label class="col-sm-4 control-label">
                            <span>BOM</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7" style="margin-top: 10px;">
                            <p>{{dispatchDetailsVm.dispatch.bom.item.itemMaster.itemName}}</p>
                        </div>
                    </div>

                    <div class="form-group" style="margin-top: 10px;">
                        <label class="col-sm-4 control-label">
                            <span>Status</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7" style="margin-top: 10px;">
                            <span ng-if="dispatchDetailsVm.dispatch.status == 'NEW'" class="badge badge-primary">{{dispatchDetailsVm.dispatch.status}}</span>
                            <span ng-if="dispatchDetailsVm.dispatch.status == 'DISPATCHED'"
                                  class="badge badge-success">{{dispatchDetailsVm.dispatch.status}}</span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Dispatch Date</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7" style="margin-top: 10px;">
                            <p>{{dispatchDetailsVm.dispatch.dispatchDate}}</p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Gate Pass Number</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7" ng-if="dispatchDetailsVm.dispatch.status == 'NEW'">
                            <input type="text" class="form-control"
                                   placeholder="Enter Gate Pass Number"
                                   ng-model="dispatchDetailsVm.dispatch.gatePassNumber"
                                   ng-disabled="!hasPermission('permission.dispatch.edit')">
                        </div>

                        <div class="col-sm-7" style="margin-top: 10px;"
                             ng-if="dispatchDetailsVm.dispatch.status == 'DISPATCHED'">
                            <p>{{dispatchDetailsVm.dispatch.gatePassNumber}}</p>
                        </div>
                    </div>

                </form>
                <br>
                <h4 ng-if="dispatchDetailsVm.dispatch.status == 'NEW'" style="color: black;">Items to Dispatch</h4>
                <h4 ng-if="dispatchDetailsVm.dispatch.status == 'DISPATCHED'" style="color: black;">
                    Dispatched Items</h4>

                <div class="responsive-table">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th>Nomenclature</th>
                            <th>Type</th>
                            <th style="width: 100px;">UPN</th>
                            <th style="width: 80px;">Status</th>
                            <th style="text-align: center;width: 75px;">Quantity</th>
                        </tr>
                        </thead>
                        <tbody>

                        <tr ng-repeat="dispatchItem in dispatchDetailsVm.dispatch.dispatchItems">
                            <td>{{dispatchItem.item.item.itemMaster.itemName}} {{itemInstance.item.partSpec.specName}}
                            </td>
                            <td>{{dispatchItem.item.item.itemMaster.parentType.name}}</td>
                            <td>
                                <span class="badge badge-success"
                                      style="font-size: 14px;">{{dispatchItem.item.upnNumber}}</span>
                            </td>
                            <td>
                                <item-instance-status object="dispatchItem.item"></item-instance-status>
                            </td>
                            <td style="text-align: center">
                                <span class="badge badge-danger" style="font-size: 13px;"
                                      ng-if="dispatchItem.item.item.itemMaster.itemType.hasLots">{{dispatchItem.item.lotSize}}</span>
                                <span class="badge badge-danger" style="font-size: 13px;"
                                      ng-if="!dispatchItem.item.item.itemMaster.itemType.hasLots">1</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

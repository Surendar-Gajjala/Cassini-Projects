<div class="view-container" fitcontent>
    <style scoped>
        .split-pane-divider {
            background: #EEE;
            left: 400px;
            width: 5px;
        }
    </style>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default min-width" ng-click="newRoadChallanVm.back()">Back</button>
            <button class="btn btn-info btn-sm" ng-click="newRoadChallanVm.addItems()">Add Items</button>
            <button ng-if="newRoadChallanVm.roadChallanItems.length > 0 "
                    class="min-width btn btn-sm btn-default"
                    ng-click="newRoadChallanVm.create()">Create
            </button>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;overflow-x: hidden;">
        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane" style="width: 400px;">
                <div class="row" style="margin-left: 10px;">
                    <form class="form-horizontal" style="padding-right: 49px;padding-top: 9px; width: 400px;">


                        <div class="form-group">
                            <label class="col-sm-4 control-label">Auto Number :<span class="asterisk">*</span></label>

                            <div class="col-sm-7">

                                <div class="input-group mb15">
                                    <div class="input-group-btn">
                                        <button class="btn btn-default" type="button" style="width: 85px"
                                                ng-click="newRoadChallanVm.generateAutoNumber()">Auto
                                        </button>
                                    </div>
                                    <input type="text" class="form-control" name="title"
                                           ng-model="newRoadChallanVm.newRoadChallan.chalanNumber">
                                </div>

                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Created On:<span class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="text" id="challanDate" class="form-control" placeholder="dd/mm/yyyy"
                                       inward-date-picker
                                       ng-model="newRoadChallanVm.newRoadChallan.chalanDate">
                            </div>

                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Going From:<span class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" placeholder="Going From"
                                       ng-model="newRoadChallanVm.newRoadChallan.goingFrom">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Going To:<span class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" placeholder="Going To"
                                       ng-model="newRoadChallanVm.newRoadChallan.goingTo">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Means of Transport:<span
                                    class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" placeholder="Means of Transport"
                                       ng-model="newRoadChallanVm.newRoadChallan.meansOfTrans">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Vehicle Details:<span
                                    class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" placeholder="Details"
                                       ng-model="newRoadChallanVm.newRoadChallan.vehicleDetails">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Issuing Authority:<span
                                    class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" placeholder="Issuing Authority"
                                       ng-model="newRoadChallanVm.newRoadChallan.issuingAuthority">
                            </div>
                        </div>

                        <br>
                        <h4 ng-if="newRoadChallanVm.newRoadChallanAttributes.length  > 0 || newRoadChallanVm.attributes.length > 0"
                            class="section-title" style="color: black;">Attributes
                        </h4>
                        <br>

                        <div>
                            <form class="form-horizontal">
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newRoadChallanVm.requiredAttributes"></attributes-view>
                                <br>
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newRoadChallanVm.attributes"></attributes-view>
                                <br>
                                <br>
                            </form>
                        </div>
                    </form>
                </div>
            </div>

            <div class="split-pane-divider"></div>
            <div class="split-pane-component split-right-pane" style="left:400px;">
                <div class="row">
                    <div class="responsive-table" style="padding-top: 5px;">
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th style="width: 150px; text-align: left">Item Number</th>
                                <th style="width: 150px; text-align: left">Item Name</th>
                                <th style="width: 150px; text-align: left">Type</th>
                                <th style="width: 200px; text-align: left">Item Description</th>
                                <th style="width: 150px; text-align: center">Inventory</th>
                                <th style="width: 150px; text-align: center">Qty</th>
                                <th style="width: 200px; text-align: left">Notes</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="newRoadChallanVm.roadChallanItems.length == 0">
                                <td colspan="11">No Items are available to view</td>
                            </tr>
                            <tr data-ng-repeat="item in newRoadChallanVm.roadChallanItems">
                                <td style="vertical-align: middle;width: 150px; text-align: left">
                                    <span>{{item.stockMovementDTO.itemDTO.itemNumber}}</span>
                                </td>

                                <td style="vertical-align: middle;width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                    title="{{item.stockMovementDTO.itemDTO.itemName}}">
                                    <span>{{item.stockMovementDTO.itemDTO.itemName}}</span>
                                </td>

                                <td style="vertical-align: middle;width: 150px; text-align: left">
                                    <span>{{item.stockMovementDTO.itemDTO.itemType.name}}</span>
                                </td>

                                <td style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                    title="{{item.stockMovementDTO.itemDTO.description}}">
                                    <span>{{item.stockMovementDTO.itemDTO.description}}</span>
                                </td>

                                <td style="vertical-align: middle;width: 150px; text-align: center">
                                    <span>{{item.storeOnHand}}</span>
                                </td>

                                <td style="text-align: center;width:50px;">
                                    <input
                                            type="number" min="1" max="item.storeOnHand"
                                            class="form-control input-sm"
                                            ng-model="item.quantity"
                                            style="text-align: center;">
                                </td>

                                <td>
                                    <input
                                            ng-model="item.notes" type="text"
                                            class="form-control input-sm">
                                </td>

                                <td style="vertical-align: middle">
                                    <i title="Remove"
                                       class="fa fa-minus-circle"
                                       style="font-size: 20px;"
                                       ng-click="newRoadChallanVm.remove(item)"
                                       aria-hidden="true"></i>
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

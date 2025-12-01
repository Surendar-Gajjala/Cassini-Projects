<div class="view-container" fitcontent>
    <style scoped>
        .split-pane-divider {
            background: #EEE;
            left: 30%;
            width: 5px;
        }
    </style>
    <div class="view-toolbar">
        <div class="btn-group">
            <div class="btn-group">
                <button class="min-width btn btn-sm btn-default" ng-click="newStockReturnVm.back()">Back</button>
                <button ng-if="newStockReturnVm.newStockReturn.returnedTo != null"
                        class="min-width btn btn-sm btn-default" ng-click="newStockReturnVm.addItems()">Add Items
                </button>
                <button ng-if="newStockReturnVm.stockReturnItemList.length > 0 "
                        class="min-width btn btn-sm btn-default"
                        ng-click="newStockReturnVm.create()">Create
                </button>
            </div>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;overflow-x: hidden;">
        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane" style="width: 30%; top:10px;padding: 20px;">
                <div class="row">
                    <form class="form-horizontal">

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Autonumber :<span class="asterisk">*</span></label>

                            <div class="col-sm-7">

                                <div class="input-group mb15">
                                    <div class="input-group-btn">
                                        <button class="btn btn-default" type="button" style="width: 85px"
                                                ng-click="newStockReturnVm.generateAutoNumber()">Auto
                                        </button>
                                    </div>
                                    <input type="text" class="form-control" name="title"
                                           ng-model="newStockReturnVm.newStockReturn.returnNumberSource">
                                </div>

                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"> Select Project :</label>

                            <div class="col-sm-7">
                                <ui-select class="required-field" ng-model="newStockReturnVm.newStockReturn.project"
                                           theme="bootstrap"
                                           ng-disabled="newStockReturnVm.stockReturnItemList.length > 0">
                                    <ui-select-match placeholder="Select Project">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="project in newStockReturnVm.projects | filter: $select.search |orderBy: 'name'"
                                            style="max-height: 120px;">
                                        <div ng-bind="project.name | highlight: $select.name.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Returned To :<span class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <ui-select class="required-field" ng-model="newStockReturnVm.newStockReturn.returnedTo"
                                           theme="bootstrap">
                                    <ui-select-match placeholder="Returned To">{{$select.selected.fullName}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="person in newStockReturnVm.persons | filter: $select.search |orderBy: 'fullName'">
                                        <div ng-bind="person.fullName | highlight: $select.fullName.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Notes : </label>

                            <div class="col-sm-7">
                                 <textarea class="form-control ng-pristine ng-valid ng-touched"
                                           ng-model="newStockReturnVm.newStockReturn.notes"
                                           style="height: 39px;margin-top: -2px;">
                            </textarea>
                            </div>
                        </div>

                        <br>
                        <h4 ng-if="newStockReturnVm.newStockReturnAttributes.length  > 0 || newStockReturnVm.attributes.length > 0"
                            class="section-title" style="color: black;">Attributes
                        </h4>
                        <br>

                        <div>
                            <form class="form-horizontal">
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newStockReturnVm.requiredAttributes"></attributes-view>
                                <br>
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newStockReturnVm.attributes"></attributes-view>
                                <br>
                                <br>
                            </form>
                        </div>
                    </form>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div class="split-pane-component split-right-pane" style="left:30%;">
                <div class="split-pane-component split-right-pane" style="left:30%;">
                    <div class="row">
                        <div style="padding-top: 5px;">
                            <table class="table table-striped" style="overflow: auto">
                                <thead>
                                <tr>
                                    <th style="width: 150px; text-align: left">Item Number</th>
                                    <th style="width: 150px; text-align: left">Item Name</th>
                                    <th style="width: 150px; text-align: left">Type</th>
                                    <th style="width: 200px; text-align: left">Item Description</th>
                                    <th style="width: 150px; text-align: center">Issued Qty</th>
                                    <th style="width: 150px; text-align: center">Returned Qty</th>
                                    <th style="width: 150px; text-align: center">Return Qty</th>
                                    <th style="width: 200px; text-align: left">Notes</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-if="newStockReturnVm.stockReturnItemList.length == 0">
                                    <td colspan="11">No Items are available to view</td>
                                </tr>
                                <tr data-ng-repeat="item in newStockReturnVm.stockReturnItemList">
                                    <td style="vertical-align: middle;width: 150px; text-align: left">
                                        <span>{{item.itemNumber}}</span>
                                    </td>

                                    <td style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                        title="{{item.itemName}}">
                                        <span>{{item.itemName}}</span>
                                    </td>

                                    <td style="vertical-align: middle;width: 150px; text-align: left">
                                        <span>{{item.itemType}}</span>
                                    </td>

                                    <td style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                        title="{{item.description}}">
                                        <span>{{item.description}}</span>
                                    </td>

                                    <td style="text-align: center;vertical-align: middle">
                                        <span>{{item.itemIssueQuantity != null ? item.itemIssueQuantity : 0}}</span>
                                    </td>

                                    <td style="text-align: center;vertical-align: middle">
                                        <span>{{item.itemReturnQuantity != null ? item.itemReturnQuantity : 0}}</span>
                                    </td>

                                    <td style="text-align: center;width:50px;">
                                        <input
                                                type="number" min="1"
                                                class="form-control input-sm"
                                                ng-model="item.quantity"
                                                style="text-align: center;">
                                    </td>

                                    <td>
                    <textarea class="form-control ng-pristine ng-valid ng-touched"
                              ng-model=" item.notes" style="height: 39px;">
                    </textarea>
                                    </td>

                                    <td style="vertical-align: middle">
                                        <i title="Remove"
                                           class="fa fa-minus-circle"
                                           style="font-size: 20px;"
                                           ng-click="newStockReturnVm.removeFromItemList(item)"
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
</div>

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
                <button class="min-width btn btn-sm btn-default" ng-click="newStockReceiveVm.back()">Back</button>
                <button class="min-width btn btn-sm btn-default" ng-click="newStockReceiveVm.addItems()">Add Items
                </button>
                <button ng-if="newStockReceiveVm.stockReceiveItems.length > 0 "
                        class="min-width btn btn-sm btn-default"
                        ng-click="newStockReceiveVm.create()">Create
                </button>
            </div>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;overflow-x: hidden;">
        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane" style="width: 30%; top:0px;padding: 34px;">
                <div class="row">
                    <form class="form-horizontal">

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Receive Type :<span class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <div class="input-group mb15">
                                    <div class="input-group-btn" uib-dropdown>
                                        <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                                type="button">
                                            Select <span class="caret" style="margin-left: 4px;"></span>
                                        </button>
                                        <div class="dropdown-menu" role="menu" style="width: 280px;">
                                            <div style="padding: 5px; border: 1px solid #FFF;
                                        height: auto; max-height:200px; overflow-x: auto">
                                                <receive-type-tree
                                                        on-select-type="newStockReceiveVm.onSelectType"></receive-type-tree>
                                            </div>
                                        </div>
                                    </div>
                                    <input type="text" class="form-control" name="title"
                                           ng-model="newStockReceiveVm.newStockReceive.materialReceiveType.name"
                                           readonly>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"> Select Project :</label>

                            <div class="col-sm-8">
                                <ui-select class="required-field" ng-model="newStockReceiveVm.newStockReceive.project"
                                           theme="bootstrap"
                                           ng-disabled="newStockReceiveVm.stockReceiveItems.length > 0">
                                    <ui-select-match placeholder="Select Project">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="project in newStockReceiveVm.projects | filter: $select.search |orderBy: 'name'"
                                            style="max-height: 120px;">
                                        <div ng-bind="project.name | highlight: $select.name.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Autonumber :<span class="asterisk">*</span></label>

                            <div class="col-sm-8">

                                <div class="input-group mb15">
                                    <div class="input-group-btn">
                                        <button class="btn btn-default" type="button" style="width: 85px"
                                                ng-click="newStockReceiveVm.autoNumber()">Auto
                                        </button>
                                    </div>
                                    <input type="text" class="form-control" name="title"
                                           ng-model="newStockReceiveVm.newStockReceive.receiveNumberSource">
                                </div>

                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"> Select Supplier :<span
                                    class="asterisk">*</span></label>

                            <div class="col-sm-8">
                                <ui-select class="required-field" ng-model="newStockReceiveVm.newStockReceive.supplier"
                                           theme="bootstrap"
                                           ng-if="newStockReceiveVm.newStockReceive.purchaseOrderNumber == null">
                                    <ui-select-match placeholder="Select Supplier ">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="supplier in newStockReceiveVm.supplierList | filter: $select.search |orderBy: 'name'"
                                            style="max-height: 120px;">
                                        <div ng-bind="supplier.name | highlight: $select.name.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                                <input type="text" class="form-control" name="title"
                                       ng-if="newStockReceiveVm.newStockReceive.purchaseOrderNumber != null"
                                       ng-model="newStockReceiveVm.newStockReceive.supplier.name"
                                       readonly>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Notes : </label>

                            <div class="col-sm-8">
                                 <textarea class="form-control ng-pristine ng-valid ng-touched"
                                           ng-model="newStockReceiveVm.newStockReceive.notes"
                                           style="height: 39px;margin-top: -2px;">
                            </textarea>
                            </div>
                        </div>

                        <br>
                        <h4 ng-if="newStockReceiveVm.requiredAttributes.length > 0 || newStockReceiveVm.attributes.length > 0 "
                            class="section-title"
                            style="color: black;">Attributes</h4>
                        <br>

                        <div>
                            <form class="form-horizontal">
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newStockReceiveVm.receiveTypeAttributes"></attributes-view>
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newStockReceiveVm.requiredReceiveTypeAttributes"></attributes-view>
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newStockReceiveVm.requiredAttributes"></attributes-view>
                                <br>
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newStockReceiveVm.attributes"></attributes-view>
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
                        <div class="responsive-table" style="padding-top: 5px;">
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th style="width: 150px; text-align: left">Item Number</th>
                                    <th style="width: 150px; text-align: left">Item Name</th>
                                    <th style="width: 150px; text-align: left">Type</th>
                                    <th style="width: 200px; text-align: left">Item Description</th>
                                    <th style="width: 100px; text-align: left">Units</th>
                                    <th style="width: 150px; text-align: center">Quantity</th>
                                    <th style="width: 200px; text-align: left">Notes</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-if="newStockReceiveVm.stockReceiveItems.length == 0">
                                    <td colspan="11">No Items are available to view</td>
                                </tr>
                                <tr data-ng-repeat="item in newStockReceiveVm.stockReceiveItems">
                                    <td style="vertical-align: middle;width: 150px; text-align: left">
                                        <span>{{item.itemNumber}}</span>
                                    </td>

                                    <td style="vertical-align: middle;width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                        title="{{item.itemName}}">
                                        <span>{{item.itemName}}</span>
                                    </td>

                                    <td style="vertical-align: middle;width: 150px; text-align: left">
                                        <span>{{item.itemType.name}}</span>
                                    </td>

                                    <td style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                        title="{{item.description}}">
                                        <span>{{item.description}}</span>
                                    </td>

                                    <td style="vertical-align: middle;width: 100px; text-align: left">
                                        <span>{{item.units}}</span>
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
                                           ng-click="newStockReceiveVm.remove(item)"
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

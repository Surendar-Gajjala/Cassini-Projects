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
                <button class="min-width btn btn-sm btn-default" ng-click="newRequestVm.back()">Back</button>
                <button ng-if="newRequestVm.newRequisition.project != null"
                        class="min-width btn btn-sm btn-default" ng-click="newRequestVm.addItems()">Add Items
                </button>
                <button ng-if="newRequestVm.requisitionItems.length > 0 "
                        class="min-width btn btn-sm btn-default"
                        ng-click="newRequestVm.create()">Create
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
                            <label class="col-sm-4 control-label">Auto Number :</label>

                            <div class="col-sm-7">

                                <div class="input-group mb15">
                                    <div class="input-group-btn">
                                        <button class="btn btn-default" type="button" style="width: 85px"
                                                ng-click="newRequestVm.generateAutoNumber()">Auto
                                        </button>
                                    </div>
                                    <input type="text" class="form-control" name="title"
                                           ng-model="newRequestVm.newRequisition.requisitionNumber">
                                </div>

                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"> Select Project :<span
                                    class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <ui-select class="required-field" ng-model="newRequestVm.newRequisition.project"
                                           theme="bootstrap">
                                    <ui-select-match placeholder="Select Project">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="project in newRequestVm.projects | filter: $select.search |orderBy: 'name'"
                                            style="max-height: 120px;">
                                        <div ng-bind="project.name | highlight: $select.name.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Requested By :<span
                                    class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" placeholder="Requested By"
                                       ng-model="newRequestVm.newRequisition.requestedBy">
                            </div>

                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Notes : </label>

                            <div class="col-sm-7">
                                 <textarea class="form-control ng-pristine ng-valid ng-touched"
                                           ng-model="newRequestVm.newRequisition.notes"
                                           style="height: 39px;margin-top: -2px;">
                            </textarea>
                            </div>
                        </div>

                        <br>
                        <h4 ng-if="newRequestVm.newRequisitionAttributes.length  > 0 || newRequestVm.attributes.length > 0"
                            class="section-title" style="color: black;">Attributes
                        </h4>
                        <br>

                        <div>
                            <form class="form-horizontal">
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newRequestVm.requiredAttributes"></attributes-view>
                                <br>
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newRequestVm.attributes"></attributes-view>
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
                                    <th style="width: 150px; text-align: center">Request Qty</th>
                                    <th style="width: 200px; text-align: left">Notes</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-if="newRequestVm.requisitionItems.length == 0">
                                    <td colspan="11">No Items are available to view</td>
                                </tr>
                                <tr data-ng-repeat="item in newRequestVm.requisitionItems">
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
                                           ng-click="newRequestVm.removeFromItemList(item)"
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

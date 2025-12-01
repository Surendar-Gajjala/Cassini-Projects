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
                <button class="min-width btn btn-sm btn-default" ng-click="newStockIssueVm.back()">Back</button>
                <button class="min-width btn btn-sm btn-default" ng-click="newStockIssueVm.addItems()">Add Items
                </button>
                <button ng-if="newStockIssueVm.stockIssueItems.length > 0 "
                        class="min-width btn btn-sm btn-default"
                        ng-click="newStockIssueVm.create()">Create
                </button>
            </div>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;overflow-x: hidden;">
        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane" style="width: 30%; top:0px;padding: 20px;">
                <div class="row">
                    <form class="form-horizontal">

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Issue Type :<span class="asterisk">*</span></label>

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
                                                <issue-type-tree
                                                        on-select-type="newStockIssueVm.onSelectType"></issue-type-tree>
                                            </div>
                                        </div>
                                    </div>
                                    <input type="text" class="form-control" name="title"
                                           ng-model="newStockIssueVm.newStockIssue.materialIssueType.name"
                                           readonly>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"> Select Project :<span
                                    class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <ui-select class="required-field" ng-model="newStockIssueVm.newStockIssue.project"
                                           theme="bootstrap" on-select="newStockIssueVm.loadProjectWBS($item)"
                                           ng-disabled="newStockIssueVm.stockIssueItems.length > 0">
                                    <ui-select-match placeholder="Select Project">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="project in newStockIssueVm.projects | filter: $select.search |orderBy: 'name'"
                                            style="max-height: 120px;">
                                        <div ng-bind="project.name | highlight: $select.name.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Autonumber :<span class="asterisk">*</span></label>

                            <div class="col-sm-7">

                                <div class="input-group mb15">
                                    <div class="input-group-btn">
                                        <button class="btn btn-default" type="button" style="width: 85px"
                                                ng-click="newStockIssueVm.autoNumber()">Auto
                                        </button>
                                    </div>
                                    <input type="text" class="form-control" name="title"
                                           ng-model="newStockIssueVm.newStockIssue.issueNumberSource">
                                </div>

                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-4 control-label">Issued To :<span class="asterisk">*</span></label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="type" placeholder="IssuedTo Name"
                                       ng-model="newStockIssueVm.newStockIssue.issuedToName">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Issue Date :<span class="asterisk">*</span> </label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" inward-date-picker
                                       ng-model="newStockIssueVm.newStockIssue.issueDate"
                                       name="PlannedFinishDate" placeholder="dd/mm/yyyy">
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-4 control-label">Notes : </label>

                            <div class="col-sm-7">
                                 <textarea class="form-control ng-pristine ng-valid ng-touched"
                                           ng-model="newStockIssueVm.newStockIssue.notes"
                                           style="height: 39px;margin-top: -2px;">
                            </textarea>
                            </div>
                        </div>

                        <br>
                        <h4 ng-if="newStockIssueVm.newStockIssueAttributes.length > 0 || newStockIssueVm.attributes.length > 0 "
                            class="section-title"
                            style="color: black;">Attributes</h4>
                        <br>

                        <div>
                            <form class="form-horizontal">
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newStockIssueVm.requiredIssueTypeAttributes"></attributes-view>
                                <br>
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newStockIssueVm.issueTypeAttributes"></attributes-view>
                                <br>
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newStockIssueVm.requiredAttributes"></attributes-view>
                                <br>
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newStockIssueVm.attributes"></attributes-view>
                                <br>
                                <br>
                            </form>
                        </div>
                    </form>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div class="split-pane-component split-right-pane" style="left:30%;"
                 ng-if="newStockIssueVm.stockIssueItems.length > 0">
                <div class="split-pane-component split-right-pane" style="left:30%;">
                    <div class="row">
                        <div class="responsive-table" style="padding-top: 5px;">
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th style="width: 150px; text-align: left">Item Number</th>
                                    <th style="width: 150px; text-align: left">Item Name</th>
                                    <th style="width: 200px; text-align: left">Units</th>
                                    <th style="width: 150px; text-align: left">Inventory</th>
                                    <th style="width: 150px;text-align: center">Issue <br> Qty</th>
                                    <%--<th style="width: 150px;">Issued <br> Quantity</th>--%>
                                    <th style="width: 200px; text-align: left">Notes</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-if="newStockIssueVm.stockIssueItems.length == 0">
                                    <td colspan="11">No Items</td>
                                </tr>
                                <tr data-ng-repeat="item in newStockIssueVm.stockIssueItems">
                                    <td style="vertical-align: middle;width: 150px; text-align: left">
                                        <span>{{item.itemNumber}}</span>
                                    </td>

                                    <td style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                        title="{{item.itemName}}">
                                        <span>{{item.itemName}}</span>
                                    </td>

                                    <td style="vertical-align: middle;width: 100px; text-align: left">
                                        <span>{{item.units}}</span>
                                    </td>

                                    <td style="width: 150px; text-align: left;vertical-align: middle">
                                        {{item.storeInventory != null ?item.storeInventory : 0}}
                                    </td>

                                    <td style="text-align: center;width:50px;">
                                        <input
                                                type="number" min="1"
                                                class="form-control input-sm"
                                                ng-model="item.quantity"
                                                style="text-align: center;">
                                    </td>

                                    <%--<td style="width: 150px; text-align: center">--%>
                                    <%--{{item.itemIssueQuantity != null ?item.itemIssueQuantity : 0}}--%>
                                    <%--</td>--%>

                                    <td>
                                        <textarea class="form-control ng-pristine ng-valid ng-touched"
                                                  ng-model=" item.notes" style="height: 39px;">
                                        </textarea>
                                    </td>

                                    <td style="vertical-align: middle">
                                        <i title="Remove"
                                           class="fa fa-minus-circle"
                                           style="font-size: 20px;"
                                           ng-click="newStockIssueVm.remove(item)"
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

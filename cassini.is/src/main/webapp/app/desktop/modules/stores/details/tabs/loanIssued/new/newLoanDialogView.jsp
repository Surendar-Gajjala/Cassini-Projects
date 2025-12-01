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
                <button class="min-width btn btn-sm btn-default" ng-click="newLoanVm.back()">Back</button>
                <button class="min-width btn btn-sm btn-default" ng-click="newLoanVm.addItems()">Add Items
                </button>
                <button ng-if="newLoanVm.loanIssueItems.length > 0 "
                        class="min-width btn btn-sm btn-default"
                        ng-click="newLoanVm.create()">Create
                </button>
            </div>
        </div>
    </div>
    <div class="view-content no-padding">
        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane" style="width: 30%; top:0px;padding: 40px;">
                <div class="row">
                    <form class="form-horizontal">

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Autonumber :<span class="asterisk">*</span></label>

                            <div class="col-sm-8">

                                <div class="input-group mb15">
                                    <div class="input-group-btn">
                                        <button class="btn btn-default" type="button" style="width: 85px"
                                                ng-click="newLoanVm.autoNumber()">Auto
                                        </button>
                                    </div>
                                    <input type="text" class="form-control" name="title"
                                           ng-model="newLoanVm.newLoan.loanNumber">
                                </div>

                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-4 control-label"> From Project :<span
                                    class="asterisk">* </span></label>

                            <div class="col-sm-8">
                                <ui-select class="required-field" ng-model="newLoanVm.newLoan.fromProjectObject"
                                           theme="bootstrap" on-select="newLoanVm.selectProjects($item)"
                                           ng-disabled="newLoanVm.loanIssueItems.length > 0">
                                    <ui-select-match placeholder="Select Project">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="project in newLoanVm.fromProjects | filter: $select.search | orderBy : 'name'"
                                            style="max-height: 120px;">
                                        <div ng-bind="project.name | highlight: $select.name.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"> To Project :<span class="asterisk">* </span></label>

                            <div class="col-sm-8">
                                <ui-select class="required-field" ng-model="newLoanVm.newLoan.toProjectObject"
                                           theme="bootstrap" ng-disabled="newLoanVm.loanIssueItems.length > 0">
                                    <ui-select-match placeholder="Select Project">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="project in newLoanVm.toProjects | filter: $select.search | orderBy : 'name'"
                                            style="max-height: 120px;">
                                        <div ng-bind="project.name | highlight: $select.name.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"> To Store :<span class="asterisk">* </span></label>

                            <div class="col-sm-8">
                                <ui-select class="required-field" ng-model="newLoanVm.newLoan.toStore"
                                           theme="bootstrap" ng-disabled="newLoanVm.loanIssueItems.length > 0">
                                    <ui-select-match placeholder="Select Store">{{$select.selected.storeName}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="store in newLoanVm.stores | filter: $select.search | orderBy : 'storeName'"
                                            style="max-height: 120px;">
                                        <div ng-bind="store.storeName | highlight: $select.storeName.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <br>
                        <h4 ng-if="newLoanVm.requiredAttributes.length > 0 || newLoanVm.attributes.length > 0 "
                            class="section-title"
                            style="color: black;">Attributes</h4>
                        <br>

                        <div>
                            <form class="form-horizontal">
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newLoanVm.requiredAttributes"></attributes-view>
                                <br>
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newLoanVm.attributes"></attributes-view>
                                <br>
                                <br>
                            </form>
                        </div>
                    </form>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div class="split-pane-component split-right-pane" style="left:31%;">
                <div class="row">
                    <div class="responsive-table" style="padding-top: 5px;">
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th style="text-align: left">Item Number</th>
                                <th style="width: 150px; text-align: left">Item Name</th>
                                <th style="width: 150px; text-align: left">Type</th>
                                <th style="width: 200px; text-align: left">Item Description</th>
                                <th style="text-align: center">Inventory</th>
                                <th style="width: 150px; text-align: center">Qty</th>
                                <th style="width: 200px; text-align: left">Notes</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="newLoanVm.loanIssueItems.length == 0">
                                <td colspan="11">No Items</td>
                            </tr>
                            <tr data-ng-repeat="item in newLoanVm.loanIssueItems">
                                <td style="vertical-align: middle; text-align: left">
                                    <span>{{item.itemDTO.itemNumber}}</span>
                                </td>

                                <td style="vertical-align: middle;width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                    title="{{item.itemDTO.itemName}}">
                                    <span>{{item.itemDTO.itemName}}</span>
                                </td>

                                <td style="vertical-align: middle;width: 80px;max-width:80px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                    title="{{item.itemDTO.resourceType}}">
                                    <span>{{item.itemDTO.resourceType}}</span>
                                </td>

                                <td style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                    title="{{item.itemDTO.description}}">
                                    <span>{{item.itemDTO.description}}</span>
                                </td>

                                <td style="vertical-align: middle; text-align: center;">
                                    <span>{{item.storeOnHand}}</span>
                                </td>

                                <td style="text-align: center;width:50px;">
                                    <input type="number" min="1" class="form-control input-sm" ng-model="item.quantity"
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
                                       ng-click="newLoanVm.remove(item)"
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

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
                <button class="min-width btn btn-sm btn-default" ng-click="newScrapRequestVm.back()">Back</button>
                <button class="min-width btn btn-sm btn-default" ng-click="newScrapRequestVm.addItems()">Add Items
                </button>
                <button ng-if="newScrapRequestVm.scrapItems.length > 0 "
                        class="min-width btn btn-sm btn-default"
                        ng-click="newScrapRequestVm.create()">Create
                </button>
            </div>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;overflow-x: hidden;">
        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane" style="width: 30%; top:0px;padding: 36px;">
                <div class="row">
                    <form class="form-horizontal">

                        <div class="form-group">

                            <label class="col-sm-4 control-label">Scrap No : <span class="asterisk">*</span></label>

                            <div class="col-sm-8">
                                <div class="input-group mb15">

                                    <div class="input-group-btn">
                                        <button class="btn btn-default" type="button" style="width: 85px"
                                                ng-click="newScrapRequestVm.autoNumber()">Auto
                                        </button>
                                    </div>
                                    <input type="text" class="form-control" name="title"
                                           ng-model="newScrapRequestVm.newScrap.scrapNumber">
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Requested By :<span class="asterisk">*</span></label>

                            <div class="col-sm-8">
                                <ui-select class="required-field" ng-model="newScrapRequestVm.newScrap.requestedBy"
                                           theme="bootstrap">
                                    <ui-select-match placeholder="Select Person">{{$select.selected.fullName}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="person in newScrapRequestVm.persons | filter: $select.search |orderBy: 'fullName'">
                                        <div ng-bind="person.fullName | highlight: $select.fullName.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"> Select Project :</label>

                            <div class="col-sm-8">
                                <ui-select class="required-field" ng-model="newScrapRequestVm.newScrap.project"
                                           theme="bootstrap" ng-disabled="newScrapRequestVm.scrapItems.length > 0">
                                    <ui-select-match placeholder="Select Project">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="project in newScrapRequestVm.projects | filter: $select.search |orderBy: 'name'"
                                            style="max-height: 120px;">
                                        <div ng-bind="project.name | highlight: $select.name.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"> Select Store :<span
                                    class="asterisk">* </span></label>

                            <div class="col-sm-8">
                                <ui-select class="required-field" ng-model="newScrapRequestVm.newScrap.store"
                                           theme="bootstrap" ng-disabled="newScrapRequestVm.scrapItems.length > 0">
                                    <ui-select-match placeholder="Select Store">{{$select.selected.storeName}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="store in newScrapRequestVm.stores | filter: $select.search |orderBy: 'storeName'"
                                            style="max-height: 120px;">
                                        <div ng-bind="store.storeName | highlight: $select.storeName.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">Notes : </label>

                            <div class="col-sm-8">
                            <textarea class="form-control" rows="5" style="height: 39px;margin-top: -2px;"
                                      ng-model="newScrapRequestVm.newScrap.notes"></textarea>
                            </div>
                        </div>

                        <br>
                        <h4 ng-if="newScrapRequestVm.newScrapRequestAttributes.length  > 0 || newScrapRequestVm.attributes.length > 0"
                            class="section-title" style="color: black;">Attributes
                        </h4>
                        <br>

                        <div>
                            <form class="form-horizontal">
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newScrapRequestVm.requiredAttributes"></attributes-view>
                                <br>
                                <attributes-view show-objects="selectObjectValues"
                                                 attributes="newScrapRequestVm.attributes"></attributes-view>
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
                                <th style="width: 150px; text-align: left">Item Number</th>
                                <th style="width: 150px; text-align: left">Item Name</th>
                                <th style="width: 150px; text-align: left">Type</th>
                                <th style="width: 200px; text-align: left">Item Description</th>
                                <th style="width: 150px; text-align: center">Qty</th>
                                <th style="width: 200px; text-align: left">Notes</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="newScrapRequestVm.scrapItems.length == 0">
                                <td colspan="11">No Items available to view</td>
                            </tr>
                            <tr data-ng-repeat="item in newScrapRequestVm.scrapItems">
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
                                       ng-click="newScrapRequestVm.remove(item)"
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

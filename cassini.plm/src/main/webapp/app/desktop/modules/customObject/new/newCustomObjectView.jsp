<div style="position: relative;height: 100%;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }
    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;height: 100%;">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title" style="" translate>BASIC_INFO</h4>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>Custom Type</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button"
                                            ng-disabled="newCustomObjectsVm.creationType == 'Supplier'">
                                        <span translate>SELECT</span> <span class="caret"
                                                                            style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <custom-type-tree type-id="newCustomObjectsVm.selectTypeId"
                                                              on-select-type="newCustomObjectsVm.onSelectType"></custom-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newCustomObjectsVm.customObject.type.name" readonly>


                            </div>
                        </div>
                    </div>
                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newCustomObjectsVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newCustomObjectsVm.customObject.number">
                            </div>
                        </div>
                    </div>


                    <div class="form-group"
                         ng-if="newCustomObjectsVm.customeObjectTypeName == 'Supplier Performance Rating' || newCustomObjectsVm.customeObjectTypeName == 'CPI Form'
                                || newCustomObjectsVm.customeObjectTypeName == '4MChange-Supplier'">
                        <label class="col-sm-4 control-label">
                            <span translate>Supplier</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">

                            <ui-select ng-model="newCustomObjectsVm.customObject.supplier" theme="bootstrap"
                                       style="width:100%" ng-disabled="newCustomObjectsVm.creationType == 'Supplier'"
                                       on-select="newCustomObjectsVm.onSelectSupplier($item)">
                                <ui-select-match placeholder="Select Supplier">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="supplier.id as supplier in newCustomObjectsVm.suppliers | filter: $select.search">
                                    <div ng-bind="supplier.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="Enter Item Name" name="title"
                                   ng-model="newCustomObjectsVm.customObject.name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" placeholder="Enter Description" rows="3" style="resize: none"
                                      ng-model="newCustomObjectsVm.customObject.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newCustomObjectsVm.customObject.workflowDefinition" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder=Select>{{$select.selected.name}} [ Revision :
                                    {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow in newCustomObjectsVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newCustomObjectsVm.customAttributes.length > 0"
                                     attributes="newCustomObjectsVm.customAttributes"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newCustomObjectsVm.attributes.length > 0"
                                     attributes="newCustomObjectsVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>

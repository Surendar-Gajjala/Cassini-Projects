<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span translate>SELECT</span> <span class="caret"
                                                                            style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <manufacturing-type-tree
                                                    on-select-type="newMBOMVm.onSelectType"
                                                    object-type="MBOMTYPE"></manufacturing-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newMBOMVm.newMBOM.type.name" readonly>

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
                                            ng-click="newMBOMVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newMBOMVm.newMBOM.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" placeholder="Enter name" ng-model="newMBOMVm.newMBOM.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none" placeholder="Enter description"
                                      ng-model="newMBOMVm.newMBOM.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ITEM</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newMBOMVm.newMBOM.item"
                                       on-select="newMBOMVm.onSelectItem($item)"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select Item">
                                    {{$select.selected.itemNumber}} - {{$select.selected.itemName}}
                                </ui-select-match>
                                <ui-select-choices repeat="item.id as item in newMBOMVm.items | filter: $select.search">
                                    <div>{{item.itemNumber}} - {{item.itemName}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ITEM_REVISION</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newMBOMVm.newMBOM.itemRevision"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select Revision">
                                    Revision : {{$select.selected.revision}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="itemRevision.id as itemRevision in newMBOMVm.selectedItem.itemRevisions | filter: $select.search">
                                    <div>Revision : {{itemRevision.revision}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <!-- <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>
                            : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newMBOMVm.newMBOM.workflow"
                                       on-select="newMBOMVm.onSelectWorkflow($item)"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder={{selectWorkflowTitle}}>
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices style="height: 120px"
                                                   repeat="workflow.id as workflow in newMBOMVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div> -->

                    <attributes-view show-objects="selectObjectValues" ng-if="newMBOMVm.attributes.length > 0"
                                     attributes="newMBOMVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>

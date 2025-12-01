<div style="position: relative;height: 100%;">
    <div style="overflow-x: hidden; padding: 20px;height: 100%;">
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
                                                    on-select-type="newBOPVm.onSelectType"
                                                    object-type="BOPTYPE"></manufacturing-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newBOPVm.newBOP.type.name" readonly>

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
                                            ng-click="newBOPVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newBOPVm.newBOP.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" placeholder="Enter name"
                                   ng-model="newBOPVm.newBOP.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none" placeholder="Enter description"
                                      ng-model="newBOPVm.newBOP.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>MBOM</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newBOPVm.newBOP.mbom"
                                       on-select="newBOPVm.onSelectMbom($item)"
                                       theme="bootstrap" style="width:100%"
                                       ng-disabled="newBOPVm.creationType == 'MBOM'">
                                <ui-select-match placeholder="Select MBOM">
                                    {{$select.selected.number}} - {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices repeat="mbom.id as mbom in newBOPVm.mboms | filter: $select.search">
                                    <div>{{mbom.number}} - {{mbom.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>MBOM_REVISION</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newBOPVm.newBOP.mbomRevision"
                                       theme="bootstrap" style="width:100%"
                                       ng-disabled="newBOPVm.creationType == 'MBOM'">
                                <ui-select-match placeholder="Select Revision">
                                    Revision : {{$select.selected.revision}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="mbomRevision.id as mbomRevision in newBOPVm.selectedMbom.mbomRevisions | filter: $select.search">
                                    <div>Revision : {{mbomRevision.revision}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newBOPVm.newBOP.workflowDefinition"
                                       on-select="newBOPVm.onSelectWorkflow($bop)"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder={{selectWorkflowTitle}}>
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow.id as workflow in newBOPVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>


                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newBOPVm.bopRequiredProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newBOPVm.bopProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newBOPVm.requiredAttributes"></attributes-view>
                    <attributes-view show-objects="selectObjectValues" ng-if="newBOPVm.attributes.length > 0"
                                     attributes="newBOPVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>

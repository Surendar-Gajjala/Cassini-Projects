<div style="position: relative;height: 100%">
    <div style="overflow-y: auto;padding: 20px;height: 100%;">
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
                                                    on-select-type="newProductionOrderVm.onSelectType"
                                                    object-type="PRODUCTIONORDERTYPE"></manufacturing-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newProductionOrderVm.type.name" readonly>

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
                                            ng-click="newProductionOrderVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newProductionOrderVm.productionOrder.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" placeholder="Enter name"
                                   ng-model="newProductionOrderVm.productionOrder.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none" placeholder="Enter description"
                                      ng-model="newProductionOrderVm.productionOrder.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PLANT</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProductionOrderVm.productionOrder.plant" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{newProductionOrderVm.selectPlantTitle}}">
                                    {{$select.selected.number}} - {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="plant.id as plant in newProductionOrderVm.plants | filter: $select.search">
                                    <div>{{plant.number}} - {{plant.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <!-- <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>MBOM</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProductionOrderVm.selectedMbom"
                                       on-select="newProductionOrderVm.onSelectMbom($item)"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select MBOM">
                                    {{$select.selected.number}} - {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="mbom.id as mbom in newProductionOrderVm.mboms | filter: $select.search">
                                    <div>{{mbom.number}} - {{mbom.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>MBOM_REVISION</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProductionOrderVm.productionOrder.mbomRevision"
                                       on-select="newProductionOrderVm.onSelectMbomRevision($item)"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select Revision">
                                    Revision : {{$select.selected.revision}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="mbomRevision.id as mbomRevision in newProductionOrderVm.selectedMbom.mbomRevisions | filter: $select.search">
                                    <div>Revision : {{mbomRevision.revision}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>BOP</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProductionOrderVm.selectedBop"
                                       on-select="newProductionOrderVm.onSelectBop($item)"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select BOP">
                                    {{$select.selected.number}} - {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="bop.id as bop in newProductionOrderVm.bops | filter: $select.search">
                                    <div>{{bop.number}} - {{bop.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>BOP_REVISION</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProductionOrderVm.productionOrder.bopRevision"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select Revision">
                                    Revision : {{$select.selected.revision}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="bopRevision.id as bopRevision in newProductionOrderVm.selectedBop.bopRevisions | filter: $select.search">
                                    <div>Revision : {{bopRevision.revision}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>QUANTITY_PRODUCED</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" numbers-only
                                   placeholder="Enter quantity produce"
                                   ng-model="newProductionOrderVm.productionOrder.qtyProduced">
                        </div>
                    </div> -->
                    <div class="form-group">
                        <label class="col-sm-4 control-label"> <span translate>PLANNED_START_DATE</span></label>

                        <div class="col-sm-7">
                            <input type="text" id="plannedStartDate" class="form-control"
                                   placeholder="Select planned start date"
                                   ng-model="newProductionOrderVm.productionOrder.plannedStartDate"
                                   start-finish-date-picker>
                        </div>


                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>PLANNED_FINISH_DATE</span></label>

                        <div class="col-sm-7">
                            <input type="text" id="plannedFinishDate" class="form-control"
                                   placeholder="Select planned finish date"
                                   ng-model="newProductionOrderVm.productionOrder.plannedFinishDate"
                                   start-finish-date-picker>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProductionOrderVm.productionOrder.workflowDefinition"
                                       on-select="newProductionOrderVm.onSelectWorkflow($bop)"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder={{selectWorkflowTitle}}>
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow.id as workflow in newProductionOrderVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newProductionOrderVm.dcoRequiredProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newProductionOrderVm.dcoProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newProductionOrderVm.attributes.length > 0"
                                     attributes="newProductionOrderVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>

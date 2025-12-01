<div style="position: relative;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }

    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title" style="" translate>BASIC_INFO</h4>
                <br>

                <form class="form-horizontal">
                    <div class="form-group" ng-if="actionType == 'home'">
                        <label class="col-sm-4 control-label">
                            <span translate>INSPECTION_PLAN</span>: </label>

                        <div class="col-sm-7">

                            <div class="switch-toggle switch-candy">
                                <input id="prTypeC" name="view" type="radio" checked
                                       ng-click="selectInspectionPlanType('PRODUCTINSPECTIONPLAN', $event)">
                                <label for="prTypeC" onclick="" translate>PRODUCTS</label>

                                <input id="prTypeI" name="view" type="radio"
                                       ng-click="selectInspectionPlanType('MATERIALINSPECTIONPLAN', $event)">
                                <label for="prTypeI" onclick="" translate="">MATERIALS</label>
                                <a href=""></a>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PLAN_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                        <span translate id="Select">SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <quality-type-tree
                                                    ng-if="selectedInspectionPlanType == 'PRODUCTINSPECTIONPLAN'"
                                                    on-select-type="newInspectionPlanVm.onSelectType"
                                                    quality-type="PRODUCTINSPECTIONPLANTYPE"></quality-type-tree>
                                            <quality-tree
                                                    ng-if="selectedInspectionPlanType == 'MATERIALINSPECTIONPLAN'"
                                                    on-select-type="newInspectionPlanVm.onSelectType"
                                                    quality-type="MATERIALINSPECTIONPLANTYPE"></quality-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newInspectionPlanVm.newInspectionPlan.planType.name" readonly>


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
                                            ng-click="newInspectionPlanVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newInspectionPlanVm.newInspectionPlan.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group" ng-if="selectedInspectionPlanType == 'PRODUCTINSPECTIONPLAN'">
                        <label class="col-sm-4 control-label">
                            <span translate>PRODUCT</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newInspectionPlanVm.newInspectionPlan.product" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectProductTitle}}"
                                                 title="{{$select.selected.itemNumber}} - {{$select.selected.itemName}} - {{$select.selected.latestRevisionObject.revision}}">
                                    {{$select.selected.itemNumber}} - {{$select.selected.itemName}} -
                                    {{$select.selected.latestRevisionObject.revision}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="product.latestRevision as product in newInspectionPlanVm.productItems | filter: $select.search">
                                    <div>{{product.itemNumber}} - {{product.itemName}} -
                                        {{product.latestRevisionObject.revision}}
                                    </div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" ng-if="selectedInspectionPlanType == 'MATERIALINSPECTIONPLAN'">
                        <label class="col-sm-4 control-label">
                            <span translate>MATERIAL</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newInspectionPlanVm.newInspectionPlan.material" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectMaterialTitle}}">
                                    {{$select.selected.partNumber}}-{{$select.selected.partName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="material in newInspectionPlanVm.materials | filter: $select.search">
                                    <div>{{material.partNumber}}-{{material.partName}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_NAME' | translate}}"
                                   ng-model="newInspectionPlanVm.newInspectionPlan.name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newInspectionPlanVm.newInspectionPlan.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newInspectionPlanVm.newInspectionPlan.workflow" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectWorkflow}}">
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow.id as workflow in newInspectionPlanVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NOTES</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_NOTES' | translate}}"
                                      ng-model="newInspectionPlanVm.newInspectionPlan.notes"></textarea>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newInspectionPlanVm.qualityProperties.length > 0"
                                     attributes="newInspectionPlanVm.qualityProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newInspectionPlanVm.attributes.length > 0"
                                     attributes="newInspectionPlanVm.attributes"></attributes-view>
                </form>
                <br><br>
            </div>
        </div>
    </div>
</div>

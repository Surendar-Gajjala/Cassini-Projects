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
                            <span translate>INSPECTION</span>: </label>

                        <div class="col-sm-7">

                            <div class="switch-toggle switch-candy">
                                <input id="prTypeC" name="view" type="radio" checked
                                       ng-click="selectInspectionType('ITEMINSPECTION', $event)">
                                <label for="prTypeC" onclick="" translate>ITEMS</label>

                                <input id="prTypeI" name="view" type="radio"
                                       ng-click="selectInspectionType('MATERIALINSPECTION', $event)">
                                <label for="prTypeI" onclick="" translate="">MATERIALS</label>
                                <a href=""></a>
                            </div>
                        </div>
                    </div>
                    <div class="form-group" ng-show="selectedInspectionType == 'MATERIALINSPECTION'">
                        <label class="col-sm-4 control-label">
                            <span translate>MANUFACTURER_PART_TYPE</span>
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
                                            <manufacturer-part-tree
                                                    on-select-type="newInspectionVm.onSelectPartType">
                                            </manufacturer-part-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newInspectionVm.selectedPartType.name" readonly>


                            </div>
                        </div>
                    </div>
                    <div class="form-group" ng-show="selectedInspectionType == 'ITEMINSPECTION'">

                        <label class="col-sm-4 control-label">
                            <span translate>PRODUCT</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newInspectionVm.newInspection.item" theme="bootstrap"
                                       style="width:100%" on-select="newInspectionVm.onSelectProduct($item)">
                                <ui-select-match placeholder="{{selectProductTitle}}">
                                    {{$select.selected.itemNumber}} - {{$select.selected.itemName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="productItem.latestRevision as productItem in newInspectionVm.productItems | filter: $select.search">
                                    <div>{{productItem.itemNumber}} - {{productItem.itemName}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group" ng-show="selectedInspectionType == 'MATERIALINSPECTION'">

                        <label class="col-sm-4 control-label">
                            <span translate>MATERIAL</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newInspectionVm.newInspection.material" theme="bootstrap"
                                       style="width:100%" on-select="newInspectionVm.onSelectPart($item)">
                                <ui-select-match placeholder="{{selectPartTitle}}">
                                    {{$select.selected.partNumber}}-{{$select.selected.partName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="material in newInspectionVm.materials | filter: $select.search">
                                    <div>{{material.partNumber}} - {{material.partName}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group" ng-show="selectedInspectionType == 'ITEMINSPECTION'">

                        <label class="col-sm-4 control-label">
                            <span translate>INSPECTION_PLAN</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newInspectionVm.newInspection.inspectionPlan" theme="bootstrap"
                                       style="width:100%" on-select="newInspectionVm.onSelectInspectionPlan($item)">
                                <ui-select-match placeholder="{{selectInspectionPlanTitle}}">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="inspectionPlan.latestRevision as inspectionPlan in newInspectionVm.inspectionPlans | filter: $select.search">
                                    <div ng-bind="inspectionPlan.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group" ng-show="selectedInspectionType == 'MATERIALINSPECTION'">

                        <label class="col-sm-4 control-label">
                            <span translate>INSPECTION_PLAN</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newInspectionVm.newInspection.inspectionPlan" theme="bootstrap"
                                       id="selectInspectionPlan"
                                       style="width:100%" on-select="newInspectionVm.onSelectInspectionPlan($item)">
                                <ui-select-match placeholder="{{selectInspectionPlanTitle}}">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="inspectionPlan.latestRevision as inspectionPlan in newInspectionVm.inspectionPlans | filter: $select.search">
                                    <div ng-bind="inspectionPlan.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>INSPECTION_NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7" style="padding-left: 7px;">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newInspectionVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newInspectionVm.newInspection.inspectionNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ASSIGNED_TO</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newInspectionVm.newInspection.assignedTo" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectAssignedToTitle}}">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newInspectionVm.persons | filter: $select.search">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newInspectionVm.newInspection.workflow" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectWorkflowTitle}}">
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow.id as workflow in newInspectionVm.workflows | filter: $select.search">
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
                                      ng-model="newInspectionVm.newInspection.notes"></textarea>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newInspectionVm.qualityProperties"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>

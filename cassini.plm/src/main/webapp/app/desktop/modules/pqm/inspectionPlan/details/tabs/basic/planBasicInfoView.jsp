<div ng-if="planBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_INSPECTION_PLAN_DETAILS</span>
    </span>
    <br/>
</div>
<div ng-if="planBasicVm.loading == false">
    <div class="row row-eq-height">
        <div class="col-sm-12">
            <div class="item-details" style="padding: 30px">
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>TYPE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{planBasicVm.inspectionPlan.planType.name}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>NUMBER</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{planBasicVm.inspectionPlan.number}}</span>
                    </div>
                </div>
                <div class="row" ng-if="planBasicVm.inspectionPlan.objectType == 'PRODUCTINSPECTIONPLAN'">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>PRODUCT</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a ui-sref="app.items.details({itemId: planBasicVm.inspectionPlan.product})"
                           title="{{clickToShowDetails}}">{{planBasicVm.inspectionPlan.productObject.itemName}}</a>
                        <span>
                        </span>
                    </div>
                </div>
                <div class="row" ng-if="planBasicVm.inspectionPlan.objectType == 'MATERIALINSPECTIONPLAN'">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MATERIAL</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{planBasicVm.inspectionPlan.material.partName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>NAME</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="" e-style="width:250px"
                           ng-if="!inspectionPlanRevision.released && hasPermission('inspectionplan','edit') && !inspectionPlanRevision.rejected"
                           onaftersave="planBasicVm.updatePlan()"
                           editable-text="planBasicVm.inspectionPlan.name">{{planBasicVm.inspectionPlan.name}}
                        </a>
                        <span ng-if="inspectionPlanRevision.rejected || inspectionPlanRevision.released || !hasPermission('inspectionplan','edit')">
                            {{planBasicVm.inspectionPlan.name}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>DESCRIPTION</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a class="description-column" href=""
                           ng-if="!inspectionPlanRevision.released && hasPermission('inspectionplan','edit') && !inspectionPlanRevision.rejected"
                           onaftersave="planBasicVm.updatePlan()"
                           editable-textarea="planBasicVm.inspectionPlan.description">
                            <span ng-bind-html=" (planBasicVm.inspectionPlan.description ) || 'ADD_DESCRIPTION' | translate"
                                  title="{{planBasicVm.inspectionPlan.description}}"></span>
                        </a>
                        <span ng-if="inspectionPlanRevision.rejected || inspectionPlanRevision.released || !hasPermission('inspectionplan','edit')">
                            {{planBasicVm.inspectionPlan.descriptionHtml}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>REVISION</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{planBasicVm.inspectionPlanRevision.revision}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>LIFE_CYCLE_PHASE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <item-status item="planBasicVm.inspectionPlanRevision"></item-status>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>WORKFLOW_STATUS</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <workflow-status-settings workflow="planBasicVm.inspectionPlanRevision"></workflow-status-settings>

                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>NOTES</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a class="description-column" href=""
                           ng-if="!inspectionPlanRevision.released && hasPermission('inspectionplan','edit') && !inspectionPlanRevision.rejected"
                           onaftersave="planBasicVm.updatePlan()"
                           editable-textarea="planBasicVm.inspectionPlan.notes">
                            <span ng-bind-html=" (planBasicVm.inspectionPlan.notes) || 'ADD_NOTES' | translate"
                                  title="{{planBasicVm.inspectionPlan.notes}}"></span>
                        </a>
                        <span ng-if="inspectionPlanRevision.rejected || inspectionPlanRevision.released || !hasPermission('inspectionplan','edit')">
                            {{planBasicVm.inspectionPlan.notesHtml}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{planBasicVm.inspectionPlan.createdByPerson.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{planBasicVm.inspectionPlan.createdDate}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{planBasicVm.inspectionPlan.modifiedByPerson.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{planBasicVm.inspectionPlan.modifiedDate}}</span>
                    </div>
                </div>

                <basic-attribute-details-view object-type="QUALITY"
                                              quality-type="{{planBasicVm.inspectionPlan.objectType}}"
                                              has-permission="hasPermission('inspectionplan','edit') && !planBasicVm.inspectionPlanRevision.released && !planBasicVm.inspectionPlanRevision.rejected"
                                              object-id="inspectionPlanRevision.id"></basic-attribute-details-view>
                <%--<object-attribute-details-view object-type="{{inspectionPlan.planType.qualityType}}"
                                                has-permission="hasPermission('inspectionplan','edit') && !inspectionPlanRevision.released && !inspectionPlanRevision.rejected"
                                               object-type-id="inspectionPlan.planType.id" show-attributes="true"
                                               object-id="inspectionPlan.id"></object-attribute-details-view>--%>
                <object-attribute-details-view object-type="{{inspectionPlan.planType.qualityType}}"
                                               has-permission="hasPermission('inspectionplan','edit') && !inspectionPlanRevision.released && !inspectionPlanRevision.rejected"
                                               object-type-id="inspectionPlan.planType.id" show-attributes="false"
                                               object-id="inspectionPlanRevision.id"
                                               master-id="inspectionPlan.id"></object-attribute-details-view>
            </div>
        </div>
    </div>
</div>
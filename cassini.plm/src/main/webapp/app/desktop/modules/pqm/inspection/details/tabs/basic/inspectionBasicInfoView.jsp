<div ng-if="inspectionBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_INSPECTION_DETAILS</span>
    </span>
    <br/>
</div>
<div ng-if="inspectionBasicVm.loading == false">
    <div class="row row-eq-height">
        <div class="col-sm-12">
            <div class="item-details" style="padding: 30px">
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>INSPECTION_NUMBER</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{inspectionBasicVm.inspection.inspectionNumber}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>INSPECTION_PLAN</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="" title="{{clickToShowDetails}}"
                           ui-sref="app.pqm.inspectionPlan.details({planId: inspectionBasicVm.inspection.inspectionPlan})">{{inspectionBasicVm.inspectionPlan.name}}</a>
                    </div>
                </div>
                <div class="row" ng-if="inspectionBasicVm.inspection == 'ITEMINSPECTION'">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>PRODUCT</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{inspectionBasicVm.inspectionPlan.productObject.itemName}}</span>
                    </div>
                </div>
                <div class="row" ng-if="inspectionBasicVm.inspection == 'MATERIALINSPECTION'">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MATERIAL</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{inspectionBasicVm.manufacturerPart.partName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>ASSIGNED_TO</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{inspectionBasicVm.inspection.assignedToObject.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>DEVIATION_SUMMARY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href=""
                           ng-if="!inspection.released && inspection.statusType != 'REJECTED' && hasPermission('inspection','edit')"
                           onaftersave="inspectionBasicVm.updateInspection()"
                           editable-textarea="inspectionBasicVm.inspection.deviationSummary">
                            <span ng-bind-html="(inspectionBasicVm.inspection.deviationSummary ) ||'CLICK_TO_ENTER_DEVIATION_SUMMARY' | translate"
                                  title="{{inspectionBasicVm.inspection.deviationSummary}}"></span>
                            {{inspectionBasicVm.inspection.deviationSummary.length > 20 ? "..." :""}}
                        </a>
                        <span ng-if="inspection.released || inspection.statusType == 'REJECTED' || !hasPermission('inspection','edit')">{{inspectionBasicVm.inspection.deviationSummaryHtml}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>NOTES</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href=""
                           ng-if="!inspection.released && inspection.statusType != 'REJECTED' && hasPermission('inspection','edit')"
                           onaftersave="inspectionBasicVm.updateInspection()"
                           editable-textarea="inspectionBasicVm.inspection.notes">
                            <span ng-bind-html="(inspectionBasicVm.inspection.notes ) ||'CLICK_TO_ENTER_NOTES' | translate"
                                  title="{{inspectionBasicVm.inspection.notes}}"></span>
                        </a>
                        <span ng-if="inspection.released || inspection.statusType == 'REJECTED' || !hasPermission('inspection','edit')">{{inspectionBasicVm.inspection.notesHtml}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>WORKFLOW_STATUS</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <workflow-status-settings workflow="inspectionBasicVm.inspection"></workflow-status-settings>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{inspectionBasicVm.inspection.createdByPerson.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{inspectionBasicVm.inspection.createdDate}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{inspectionBasicVm.inspection.modifiedByPerson.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{inspectionBasicVm.inspection.modifiedDate}}</span>
                    </div>
                </div>

                <basic-attribute-details-view object-type="QUALITY"
                                              quality-type="{{inspectionBasicVm.inspection.objectType}}"
                                              has-permission="hasPermission('inspection','edit') && !inspectionBasicVm.inspection.released && inspectionBasicVm.inspection.statusType != 'REJECTED'"
                                              object-id="inspectionBasicVm.inspectionId"></basic-attribute-details-view>

            </div>
        </div>
    </div>
</div>
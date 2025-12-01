<object-attribute-details-view object-type="{{inspectionPlan.planType.qualityType}}"
                                has-permission="hasPermission('inspectionplan','edit') && !inspectionPlanRevision.released && !inspectionPlanRevision.rejected"
                               object-type-id="inspectionPlan.planType.id" show-attributes="true"
                               object-id="inspectionPlan.id"></object-attribute-details-view>
<object-attribute-details-view object-type="{{inspectionPlan.planType.qualityType}}"
                                has-permission="hasPermission('inspectionplan','edit') && !inspectionPlanRevision.released && !inspectionPlanRevision.rejected"
                               object-type-id="inspectionPlan.planType.id" show-attributes="false"
                               object-id="inspectionPlanRevision.id"></object-attribute-details-view>


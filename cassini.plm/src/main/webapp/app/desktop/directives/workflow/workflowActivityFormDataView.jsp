<div id="workflow-form-data-view" style="width: 500px;">
    <object-attribute-details-view object-type="PLMWORKFLOWSTATUS" show-attributes="true"
                                   has-permission="selectedStatus.flag != 'COMPLETED' && workflow.currentStatus == selectedStatus.id && (object.createdBy == personDetails.person.id || checkHasApprover())"
                                   object-type-id="selectedStatus.id"
                                   object-id="selectedStatus.id"></object-attribute-details-view>
</div>
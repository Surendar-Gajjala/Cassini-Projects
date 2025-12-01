<div class="row row-eq-height" style="margin: 0">
    <div class="item-details col-sm-12" style="padding: 30px;">

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>NAME</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" e-style="width:250px" ng-if="hasPermission('template','edit')"
                   onaftersave="templateBasicInfoVm.updateTemplate()"
                   editable-text="templateBasicInfoVm.template.name">{{templateBasicInfoVm.template.name}}</a>
                <span ng-if="!hasPermission('template','edit')">{{templateBasicInfoVm.template.name}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>DESCRIPTION</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" ng-if="hasPermission('template','edit')"
                   onaftersave="templateBasicInfoVm.updateTemplate()"
                   editable-textarea="templateBasicInfoVm.template.description"><span
                        ng-bind-html="(templateBasicInfoVm.template.description) || 'CLICK_TO_ENTER_DESCRIPTION' | translate"
                        title="{{templateBasicInfoVm.template.description}}"></span>
                </a>
                <span ng-if="!hasPermission('template','edit')">{{templateBasicInfoVm.template.description}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>WORKFLOW_STATUS</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{templateBasicInfoVm.template.workflowStatus}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>CREATED_BY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{templateBasicInfoVm.template.createdByObject.fullName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>CREATED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{templateBasicInfoVm.template.createdDate}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>MODIFIED_BY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{templateBasicInfoVm.template.modifiedByObject.fullName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>MODIFIED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{templateBasicInfoVm.template.modifiedDate}}</span>
            </div>
        </div>

    </div>
</div>

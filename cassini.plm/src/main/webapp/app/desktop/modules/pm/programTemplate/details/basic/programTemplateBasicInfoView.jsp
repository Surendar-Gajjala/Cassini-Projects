<div class="row row-eq-height" style="margin: 0">
    <div class="item-details col-sm-12" style="padding: 30px;">

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>NAME</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" e-style="width:250px" ng-if="hasPermission('programtemplate','edit')"
                   onaftersave="programTemplateBasicInfoVm.updateTemplate()"
                   editable-text="programTemplateBasicInfoVm.template.name">{{programTemplateBasicInfoVm.template.name}}</a>
                <span ng-if="!hasPermission('programtemplate','edit')">{{programTemplateBasicInfoVm.template.name}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>DESCRIPTION</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" ng-if="hasPermission('programtemplate','edit')"
                   onaftersave="programTemplateBasicInfoVm.updateTemplate()"
                   editable-textarea="programTemplateBasicInfoVm.template.description"><span
                        ng-bind-html="(programTemplateBasicInfoVm.template.description) || 'CLICK_TO_ENTER_DESCRIPTION' | translate"
                        title="{{programTemplateBasicInfoVm.template.description}}"></span>
                </a>
                <span ng-if="!hasPermission('programtemplate','edit')">{{programTemplateBasicInfoVm.template.description}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>WORKFLOW_STATUS</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{programTemplateBasicInfoVm.template.workflowStatus}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>CREATED_BY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{programTemplateBasicInfoVm.template.createdByObject.fullName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>CREATED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{programTemplateBasicInfoVm.template.createdDate}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>MODIFIED_BY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{programTemplateBasicInfoVm.template.modifiedByObject.fullName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>MODIFIED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{programTemplateBasicInfoVm.template.modifiedDate}}</span>
            </div>
        </div>

    </div>
</div>

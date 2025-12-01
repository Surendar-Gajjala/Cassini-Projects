<style>

    /* The Close Button */
    .img-model .closeimage {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeimage:hover,
    .img-model .closeimage:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .browse-control {
        -moz-border-radius: 3px;
        -webkit-border-radius: 3px;
        border-radius: 3px;
        padding: 5px;
        height: auto;
        -moz-box-shadow: none;
        -webkit-box-shadow: none;
        box-shadow: none;
        font-size: 13px;
        border: 1px solid #ccc;
    }
</style>

<div ng-if="varianceBasicVm.loading == true && varianceType == 'Deviation'" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_DEVIATION</span>
    </span>
    <br/>
</div>
<div ng-if="varianceBasicVm.loading == true && varianceType == 'Waiver'" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_WAIVER</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="varianceBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TITLE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a ng-if="hasPermission('change','variance','edit')
                && varianceBasicVm.variance.statusType != 'REJECTED' && varianceBasicVm.variance.statusType != 'RELEASED'"
               href=""
               e-style="width:250px"
               ng-hide="external.external == true" onaftersave="varianceBasicVm.updateVariance()"
               editable-text="varianceBasicVm.variance.title"
                    ><span ng-bind-html="varianceBasicVm.variance.title || 'ADD_TITLE' |
                 translate"></span></a>
            <span ng-if="!hasPermission('change','variance','edit') || varianceBasicVm.varianceStatus == true">
                {{varianceBasicVm.variance.title}}
            </span>
        </div>
    </div>
    <div class="row" ng-if="varianceType == 'Deviation'">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DEVIATION_FOR</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <div class="value col-xs-8 col-sm-9">
                <variance-for variance="varianceBasicVm.variance"></variance-for>
            </div>
        </div>
    </div>
    <div class="row" ng-if="varianceType == 'Waiver'">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>WAIVER_FOR</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <div class="value col-xs-8 col-sm-9">
                <variance-for variance="varianceBasicVm.variance"></variance-for>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="hasPermission('change','variance','edit')
               && varianceBasicVm.variance.statusType != 'REJECTED' && varianceBasicVm.variance.statusType != 'RELEASED'"
               href=""
               e-style="width:250px"
               ng-hide="external.external == true" onaftersave="varianceBasicVm.updateVariance()"
               editable-textarea="varianceBasicVm.variance.description"><span ng-bind-html="(varianceBasicVm.variance.description ) || 'ADD_DESCRIPTION' |
                 translate" title="{{varianceBasicVm.variance.description}}"></span></a>
            <span ng-if="!hasPermission('change','variance','edit') || varianceBasicVm.varianceStatus == true">
                {{varianceBasicVm.variance.description}}
            </span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REASON_FOR_CHANGE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="hasPermission('change','variance','edit')
                && varianceBasicVm.variance.statusType != 'REJECTED' && varianceBasicVm.variance.statusType != 'RELEASED'"
               href=""
               e-style="width:250px"
               ng-hide="external.external == true" onaftersave="varianceBasicVm.updateVariance()"
               editable-textarea="varianceBasicVm.variance.reasonForVariance"><span ng-bind-html="(varianceBasicVm.variance.reasonForVariance ) || 'ADD_REASON_FOR_CHANGE' |
                 translate" title="{{varianceBasicVm.variance.reasonForVariance}}"></span> </a>
            <span ng-if="!hasPermission('change','variance','edit') || varianceBasicVm.varianceStatus == true">
                {{varianceBasicVm.variance.reasonForVariance}}
            </span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CURRENT_REQUIREMENT</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="hasPermission('change','variance','edit')
                && varianceBasicVm.variance.statusType != 'REJECTED' && varianceBasicVm.variance.statusType != 'RELEASED'"
               href=""
               e-style="width:250px"
               ng-hide="external.external == true" onaftersave="varianceBasicVm.updateVariance()"
               editable-textarea="varianceBasicVm.variance.currentRequirement"><span ng-bind-html="(varianceBasicVm.variance.currentRequirement ) || 'ADD_CURRENT_REQUIREMENT' |
                 translate" title="{{varianceBasicVm.variance.currentRequirement}}"></span></a>
            <span ng-if="!hasPermission('change','variance','edit') || varianceBasicVm.varianceStatus == true">
                {{varianceBasicVm.variance.currentRequirement}}
            </span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REQUIREMENT_DEVIATION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="hasPermission('change','variance','edit')
                && varianceBasicVm.variance.statusType != 'REJECTED' && varianceBasicVm.variance.statusType != 'RELEASED'"
               href=""
               e-style="width:250px"
               ng-hide="external.external == true" onaftersave="varianceBasicVm.updateVariance()"
               editable-textarea="varianceBasicVm.variance.requirementDeviation"><span ng-bind-html="(varianceBasicVm.variance.requirementDeviation ) || 'ADD_REQUIREMENT_DEVIATION' |
                 translate" title={{varianceBasicVm.variance.requirementDeviation}}"></span> </a>
            <span ng-if="!hasPermission('change','variance','edit') || varianceBasicVm.varianceStatus == true">
                {{varianceBasicVm.variance.requirementDeviation}}
            </span>
        </div>
    </div>
    <div class="row" ng-if="varianceBasicVm.variance.effectivityType == 'DURATION'">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>EFFECTIVE_FROM</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-if="varianceBasicVm.variance.startWorkflow == false">
            <div ng-show="varianceBasicVm.editEffectiveFrom == true" style="display: flex;">
                <input id="effectiveFrom" class="form-control" date-picker placeholder="{{effectiveFromPlaceholder}}"
                       type="text" ng-model="varianceBasicVm.variance.effectiveFrom" style="width: 200px;">
                <button class="btn btn-sm btn-primary"
                        ng-click="varianceBasicVm.updateEffectiveFrom()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" id="cancelEffectiveFrom"
                        ng-click="varianceBasicVm.editEffectiveFrom = false;loadBasicVariance();">
                    <i class="fa fa-times"></i>
                </button>
            </div>
            <div ng-show="hasPermission('change','variance','edit') && varianceBasicVm.editEffectiveFrom == false"
                 style="display: flex;">
                <a href="" ng-click="varianceBasicVm.editEffectiveFrom = true">
                    <span ng-bind-html="varianceBasicVm.variance.effectiveFrom || 'CLICK_TO_SET_EFFECTIVE_FROM' | translate"></span>
                </a>
            </div>
        </div>
        <div class="value col-xs-8 col-sm-9" ng-if="varianceBasicVm.variance.startWorkflow == true">
            <span>{{varianceBasicVm.variance.effectiveFrom}}</span>
        </div>
    </div>
    <div class="row" ng-if="varianceBasicVm.variance.effectivityType == 'DURATION'">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>EFFECTIVE_TO</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-if="varianceBasicVm.variance.startWorkflow == false">
            <div ng-show="varianceBasicVm.editEffectiveTo == true" style="display: flex;">
                <input id="effectiveTo" class="form-control" date-picker placeholder="{{effectiveToPlaceholder}}"
                       type="text" ng-model="varianceBasicVm.variance.effectiveTo" style="width: 200px;">
                <button class="btn btn-sm btn-primary"
                        ng-click="varianceBasicVm.updateEffectiveTo()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default"
                        ng-click="varianceBasicVm.editEffectiveTo = false;loadBasicVariance();">
                    <i class="fa fa-times"></i>
                </button>
            </div>
            <div ng-show="hasPermission('change','variance','edit') && varianceBasicVm.editEffectiveTo == false"
                 style="display: flex;">
                <a href="" ng-click="varianceBasicVm.editEffectiveTo = true">
                    <span ng-bind-html="varianceBasicVm.variance.effectiveTo || 'CLICK_TO_SET_EFFECTIVE_TO' | translate"></span>
                </a>
            </div>
        </div>
        <div class="value col-xs-8 col-sm-9" ng-if="varianceBasicVm.variance.startWorkflow == true">
            <span>{{varianceBasicVm.variance.effectiveTo}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>WORKFLOW_STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <div class="value col-xs-8 col-sm-9">
                <workflow-status-settings workflow="varianceBasicVm.variance"></workflow-status-settings>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ORIGINATOR</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{varianceBasicVm.variance.originatorObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{varianceBasicVm.variance.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{varianceBasicVm.variance.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{varianceBasicVm.variance.modifiedDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{varianceBasicVm.variance.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <basic-attribute-details-view object-type="CHANGE"
                                  quality-type="CHANGE"
                                  has-permission="hasPermission('change','variance','edit') && variance.statusType != 'RELEASED' && variance.statusType != 'REJECTED'"
                                  object-id="variance.id"></basic-attribute-details-view>
    <object-attribute-details-view object-type="CHANGETYPE" show-attributes="true"
                                   has-permission="hasPermission('change','variance','edit') && variance.statusType != 'RELEASED' && variance.statusType != 'REJECTED'"
                                   object-type-id="variance.changeClass.id"
                                   object-id="variance.id"></object-attribute-details-view>

</div>

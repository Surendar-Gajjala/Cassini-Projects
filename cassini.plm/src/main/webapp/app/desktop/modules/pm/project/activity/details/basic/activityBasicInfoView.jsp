<style>
    .attributeTooltip {
        position: relative;
        display: inline-block;
    }

    .attributeTooltip .attributeTooltiptext {
        visibility: hidden;
        width: 200px;
        background-color: #7BB7EB;
        color: #141f9f;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        right: 100%;
        opacity: 0;
        transition: opacity 1s;
    }

    .attributeTooltip .attributeTooltiptext::after {
        content: "";
        position: absolute;
        top: 25%;
        left: 102%;
        margin-left: -5px;
        border-width: 5px;
        border-style: solid;
        border-color: transparent transparent transparent #7BB7EB;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }

    /* The Close Button */
    span.closeimage {
        position: absolute !important;
        top: 50px !important;
        right: 50px !important;

        font-size: 40px !important;
        font-weight: bold !important;
        transition: 0.3s !important;
        cursor: pointer !important;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus,
    .img-model .closeImage:hover,
    .img-model .closeImage:focus {
        color: #bbb !important;
        text-decoration: none !important;
        cursor: pointer !important;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus {
        color: #bbb !important;
        text-decoration: none !important;
        cursor: pointer !important;
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

    /* .btn-default {
         background: #e4e7ea !important;
         color: #636e7b !important;
     }*/
</style>
<div class="row row-eq-height" style="margin: 0">
    <div class="item-details col-sm-12" style="padding: 30px;">

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right" style="font-size: 14px">
                <span translate>NAME</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" e-style="width:250px" ng-hide="activityPercent == 100"
                   ng-if="activityPercent >= 0 && activityWritePermission"
                   onaftersave="activityBasicVm.updateActivity(activityBasicVm.activity)"
                   title="{{'CLICK_TO_SET_VALUE' | translate}}"
                   editable-text="activityBasicVm.activity.name">{{activityBasicVm.activity.name}}</a>
                <span ng-if="activityPercent == 100 || activityReadPermission">{{activityBasicVm.activity.name}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right" style="font-size: 14px">
                <span translate>DESCRIPTION</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" e-style="width:250px" ng-hide="activityPercent == 100"
                   ng-if="activityPercent >= 0 && activityWritePermission"
                   onaftersave="activityBasicVm.updateActivity(activityBasicVm.activity)"
                   editable-text="activityBasicVm.activity.description"
                   ng-bind-html="activityBasicVm.activity.description || 'CLICK_TO_ENTER_DESCRIPTION' | translate"
                   title="{{'CLICK_TO_ENTER_DESCRIPTION' | translate}}">{{activityBasicVm.activity.description}}</a>
                <span ng-if="activityPercent == 100 || activityReadPermission">{{activityBasicVm.activity.description}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>WORKFLOW_STATUS</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{activityBasicVm.activity.workflowStatus}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right" style="font-size: 14px">
                <span translate>CREATED_BY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{activityBasicVm.activity.createdByObject.fullName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right" style="font-size: 14px">
                <span translate>ASSIGNED_TO</span> :
            </div>
            <div class="value col-xs-8 col-sm-9" style="line-height: 20px">

                <a href="#" e-style="width: 50%" ng-hide="activityPercent == 100"
                   ng-if="activityPercent >= 0 && activityWritePermission"
                   onaftersave="activityBasicVm.updateActivity(activityBasicVm.activity)"
                   editable-select="activityBasicVm.activity.person"
                   e-ng-options="person as person.fullName for person in activityBasicVm.persons track by person.id"
                   translate>
                    {{ activityBasicVm.activity.person.fullName || 'CLICK_TO_ADD_PERSON'}}
                </a>
                <span ng-if="activityPercent == 100 || activityReadPermission">
                    {{activityBasicVm.activity.person.fullName}}</span>
            </div>
        </div>


        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right" style="font-size: 14px">
                <span translate>PLANNED_START_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <div>
                    <span>{{activityBasicVm.activity.plannedStartDate}}</span>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right" style="font-size: 14px">
                <span translate>PLANNED_FINISH_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <div>
                    <span>{{activityBasicVm.activity.plannedFinishDate}}</span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right" style="font-size: 14px">
                <span translate>ACTUAL_START_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{activityBasicVm.activity.actualStartDate}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right" style="font-size: 14px">
                <span translate>ACTUAL_FINISH_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{activityBasicVm.activity.actualFinishDate}}</span>
            </div>
        </div>

        <basic-attribute-details-view object-type="PROJECTACTIVITY"
                                      quality-type="PROJECTACTIVITY"
                                      has-permission="hasPermission('project','edit')"
                                      object-id="activityBasicVm.activity.id"></basic-attribute-details-view>
    </div>
</div>
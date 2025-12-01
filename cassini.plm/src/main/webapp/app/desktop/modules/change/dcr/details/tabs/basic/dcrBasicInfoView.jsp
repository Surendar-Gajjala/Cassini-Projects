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

<div ng-if="dcrBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading ECO details...
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="dcrBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcrBasicVm.dcr.crNumber}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DCR_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcrBasicVm.dcr.crTypeObject.name}}</span>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION_OF_CHANGE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && dcrBasicVm.dcrStatus == false"
               href=""
               ng-hide="external.external == true" onaftersave="dcrBasicVm.updateDcr()"
               editable-textarea="dcrBasicVm.dcr.descriptionOfChange"><span ng-bind-html="(dcrBasicVm.dcr.descriptionOfChange  ) || 'ADD_DESCRIPTION' |
                translate" title="{{dcrBasicVm.dcr.descriptionOfChange}}"></span>
            </a>
            <span ng-if="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && dcrBasicVm.dcrStatus == true"
                  ng-bind-html="dcrBasicVm.dcr.descriptionOfChange"></span>

        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CHANGE_REASON_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span ng-bind-html="dcrBasicVm.dcr.changeReasonType"></span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REASON_FOR_CHANGE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && dcrBasicVm.dcrStatus == false"
               href=""
               ng-hide="external.external == true" onaftersave="dcrBasicVm.updateDcr()"
               editable-textarea="dcrBasicVm.dcr.reasonForChange">
                <span ng-bind-html="(dcrBasicVm.dcr.reasonForChange )  || 'ADD_REASON_FOR_CHANGE' |
                translate" title="{{dcrBasicVm.dcr.reasonForChange}}"></span>
            </a>
            <span ng-if="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && dcrBasicVm.dcrStatus == true"
                  ng-bind-html="dcrBasicVm.dcr.reasonForChange"></span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TITLE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a ng-if="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && dcrBasicVm.dcrStatus == false"
               href=""
               e-style="width:250px"
               ng-hide="external.external == true" onaftersave="dcrBasicVm.updateDcr()"
               editable-text="dcrBasicVm.dcr.title"
                    ><span ng-bind-html="dcrBasicVm.dcr.title || 'ADD_TITLE' |
                translate"></span></a>
             <span ng-if="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && dcrBasicVm.dcrStatus == true"
                   ng-bind-html="dcrBasicVm.dcr.reasonForChange"></span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>WORKFLOW_STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <workflow-status-settings workflow="dcrBasicVm.dcr"></workflow-status-settings>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>URGENCY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 250px"
               ng-if="hasPermission('admin','all') && dcrBasicVm.dcrStatus == false"
               onaftersave="dcrBasicVm.updateDcr()"
               editable-select="dcrBasicVm.dcr.urgency"
               e-ng-options="urgency for urgency in dcrBasicVm.urgencys track by urgency">
                {{dcrBasicVm.dcr.urgency}}
            </a>
            <span ng-if="!hasPermission('admin','all') || dcrBasicVm.dcrStatus == true">{{dcrBasicVm.dcr.urgency}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>OWNER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 250px"
               ng-if="hasPermission('admin','all') && dcrBasicVm.dcrStatus == false"
               onaftersave="dcrBasicVm.updateDcr()"
               editable-select="dcrBasicVm.dcr.changeAnalystObject"
               title="{{dcrBasicVm.clickToUpdatePerson}}"
               e-ng-options="person as person.fullName for person in dcrBasicVm.changeAnalysts | orderBy:'fullName' track by person.id">
                {{dcrBasicVm.dcr.changeAnalystObject.fullName}}
            </a>
            <span ng-if="!hasPermission('admin','all') || dcrBasicVm.dcrStatus == true">{{dcrBasicVm.dcr.changeAnalystObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ORIGINATOR</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 250px"
               ng-if="hasPermission('admin','all') && dcrBasicVm.dcrStatus == false"
               onaftersave="dcrBasicVm.updateDcr()"
               editable-select="dcrBasicVm.dcr.originatorObject"
               title="{{dcrBasicVm.clickToUpdatePerson}}"
               e-ng-options="person as person.fullName for person in dcrBasicVm.originators | orderBy:'fullName' track by person.id">
                {{dcrBasicVm.dcr.originatorObject.fullName}}
            </a>
            <span ng-if="!hasPermission('admin','all') || dcrBasicVm.dcrStatus == true">{{dcrBasicVm.dcr.originatorObject.fullName}}</span>
        </div>
    </div>
    <div class="row" ng-if="dcrBasicVm.dcr.isApproved">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>APPROVED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcrBasicVm.dcr.approvedDate}}</span>
        </div>
    </div>
    <div class="row" ng-if="dcrBasicVm.dcr.statusType == 'REJECTED'">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REJECTED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcrBasicVm.dcr.approvedDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REQUESTED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcrBasicVm.dcr.requestedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REQUESTED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcrBasicVm.dcr.requestedDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcrBasicVm.dcr.createdByObject.fullName}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcrBasicVm.dcr.createdDate}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcrBasicVm.dcr.modifiedByObject.fullName}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcrBasicVm.dcr.modifiedDate}}</span>
        </div>
    </div>

    <basic-attribute-details-view object-type="CHANGE"
                                  quality-type="CHANGE"
                                  has-permission="(hasPermission('change','dcr','edit') || hasPermission('change','edit')) && !dcr.isApproved && dcr.statusType != 'REJECTED'"
                                  object-id="dcr.id"></basic-attribute-details-view>
    <object-attribute-details-view object-type="CHANGETYPE" show-attributes="true"
                                   has-permission="(hasPermission('change','dcr','edit') || hasPermission('change','edit')) && !dcr.isApproved && dcr.statusType != 'REJECTED'"
                                   object-type-id="dcr.crType"
                                   object-id="dcr.id"></object-attribute-details-view>

</div>

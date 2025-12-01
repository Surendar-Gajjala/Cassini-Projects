<style>
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

<div ng-if="ecrBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading ECR details...
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="ecrBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ECR_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecrBasicVm.ecr.crNumber}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecrBasicVm.ecr.crTypeObject.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>QCR</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="" ng-click="ecrBasicVm.showQcr(ecrBasicVm.ecr.qcr)">{{ecrBasicVm.ecr.qcr.qcrNumber}}</a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TITLE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a ng-if="hasPermission('change','ecr','edit') && !ecr.isApproved && ecr.statusType != 'REJECTED'" href=""
               e-style="width:250px"
               ng-hide="external.external == true" onaftersave="ecrBasicVm.updateECR()"
               editable-text="ecrBasicVm.ecr.title">
                <span ng-bind-html="ecrBasicVm.ecr.title || 'ADD_TITLE' | translate"></span>
            </a>
            <span ng-if="!hasPermission('change','ecr','edit') || ecr.isApproved || ecr.statusType == 'REJECTED'">{{ecrBasicVm.ecr.title}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION_OF_CHANGE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="hasPermission('change','ecr','edit') && !ecr.isApproved && ecr.statusType != 'REJECTED'" href=""
               onaftersave="ecrBasicVm.updateECR()"
               editable-textarea="ecrBasicVm.ecr.descriptionOfChange">
                <span ng-bind-html="(ecrBasicVm.ecr.descriptionOfChange ) || 'ADD_DESCRIPTION_OF_CHANGE' | translate"
                      title="{{ecrBasicVm.ecr.descriptionOfChange}}"></span>
            </a>
            <span ng-if="!hasPermission('change','ecr','edit') || ecr.isApproved || ecr.statusType == 'REJECTED'"
                  ng-bind-html="ecrBasicVm.ecr.descriptionOfChange"></span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CHANGE_REASON_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span ng-bind-html="ecrBasicVm.ecr.changeReasonType"></span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REASON_FOR_CHANGE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="hasPermission('change','ecr','edit') && !ecr.isApproved && ecr.statusType != 'REJECTED'" href=""
               onaftersave="ecrBasicVm.updateECR()"
               editable-textarea="ecrBasicVm.ecr.reasonForChange">
                <span ng-bind-html=" (ecrBasicVm.ecr.reasonForChange ) || 'ADD_REASON_FOR_CHANGE' | translate"
                      title="{{ecrBasicVm.ecr.reasonForChange}}"></span>
            </a>
            <span ng-if="!hasPermission('change','ecr','edit') || ecr.isApproved || ecr.statusType == 'REJECTED'"
                  ng-bind-html="ecrBasicVm.ecr.reasonForChange"></span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PROPOSED_CHANGES</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="hasPermission('change','ecr','edit') && !ecr.isApproved && ecr.statusType != 'REJECTED'" href=""
               onaftersave="ecrBasicVm.updateECR()"
               editable-textarea="ecrBasicVm.ecr.proposedChanges">
                <span ng-bind-html="( ecrBasicVm.ecr.proposedChanges ) || 'ADD_PROPOSED_CHANGES' | translate"
                      title="{{ecrBasicVm.ecr.proposedChanges}}"></span>
            </a>
            <span ng-if="!hasPermission('change','ecr','edit') || ecr.isApproved || ecr.statusType == 'REJECTED'"
                  ng-bind-html="ecrBasicVm.ecr.proposedChanges"></span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>WORKFLOW_STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <workflow-status-settings workflow="ecrBasicVm.ecr"></workflow-status-settings>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>URGENCY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 250px"
               ng-if="hasPermission('admin','all') && !ecr.isApproved && ecr.statusType != 'REJECTED'"
               onaftersave="ecrBasicVm.updateECR()"
               editable-select="ecrBasicVm.ecr.urgency"
               e-ng-options="urgency for urgency in ecrBasicVm.urgencys track by urgency">
                {{ecrBasicVm.ecr.urgency}}
            </a>
            <span ng-if="!hasPermission('admin','all') || ecr.isApproved || ecr.statusType == 'REJECTED'">{{ecrBasicVm.ecr.urgency}}</span>
        </div>
    </div>

    <div class="row" ng-if="ecrBasicVm.ecr.isApproved">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>RELEASED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecrBasicVm.ecr.approvedDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CHANGE_ANALYST</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 250px"
               ng-if="hasPermission('admin','all') && !ecr.isApproved && ecr.statusType != 'REJECTED'"
               onaftersave="ecrBasicVm.updateECR()"
               editable-select="ecrBasicVm.ecr.changeAnalystObject"
               title="{{ecrBasicVm.clickToUpdatePerson}}"
               e-ng-options="person as person.fullName for person in ecrBasicVm.changeAnalysts | orderBy:'fullName' track by person.id">
                {{ecrBasicVm.ecr.changeAnalystObject.fullName}}
            </a>
            <span ng-if="!hasPermission('admin','all') || ecr.isApproved || ecr.statusType == 'REJECTED'">{{ecrBasicVm.ecr.changeAnalystObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ORIGINATOR</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 250px"
               ng-if="hasPermission('admin','all') && !ecr.isApproved && ecr.statusType != 'REJECTED'"
               onaftersave="ecrBasicVm.updateECR()"
               editable-select="ecrBasicVm.ecr.originatorObject"
               title="{{ecrBasicVm.clickToUpdatePerson}}"
               e-ng-options="person as person.fullName for person in ecrBasicVm.originators | orderBy:'fullName' track by person.id">
                {{ecrBasicVm.ecr.originatorObject.fullName}}
            </a>
            <span ng-if="!hasPermission('admin','all') || ecr.isApproved || ecr.statusType == 'REJECTED'">{{ecrBasicVm.ecr.originatorObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REQUESTER_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <reporter-type reporter-type="ecrBasicVm.ecr.requesterType"></reporter-type>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REQUESTED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span ng-if="ecrBasicVm.ecr.requesterType == 'INTERNAL'">{{ecrBasicVm.ecr.requestedByObject.fullName || ecrBasicVm.ecr.otherRequested}}</span>
            <span ng-if="ecrBasicVm.ecr.requesterType == 'CUSTOMER'">{{ecrBasicVm.ecr.requestedByObject.name || ecrBasicVm.ecr.otherRequested}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REQUESTED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecrBasicVm.ecr.requestedDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecrBasicVm.ecr.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecrBasicVm.ecr.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecrBasicVm.ecr.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecrBasicVm.ecr.modifiedDate}}</span>
        </div>
    </div>
    <basic-attribute-details-view object-type="CHANGE"
                                  quality-type="CHANGE"
                                  has-permission="hasPermission('change','ecr','edit') && !ecrBasicVm.ecr.isApproved && ecrBasicVm.ecr.statusType != 'REJECTED'"
                                  object-id="ecrBasicVm.ecr.id"></basic-attribute-details-view>
    <object-attribute-details-view object-type="CHANGETYPE" show-attributes="true"
                                   has-permission="hasPermission('change','ecr','edit') && !ecr.isApproved && ecr.statusType != 'REJECTED'"
                                   object-type-id="ecr.crType"
                                   object-id="ecr.id"></object-attribute-details-view>

</div>

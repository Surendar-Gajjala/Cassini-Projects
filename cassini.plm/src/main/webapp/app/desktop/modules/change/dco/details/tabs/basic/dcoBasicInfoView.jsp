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

<div ng-if="dcoBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading dco details...
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="dcoBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DCO_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcoBasicVm.dco.dcoNumber}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DCO_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcoBasicVm.dco.dcoTypeObject.name}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TITLE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a ng-if="(hasPermission('change','dco','edit') || hasPermission('change','edit')) && !dco.isReleased && dco.statusType != 'REJECTED'"
               href=""
               e-style="width:250px"
               ng-hide="external.external == true" onaftersave="dcoBasicVm.updateDco()"
               editable-text="dcoBasicVm.dco.title"><span ng-bind-html="dcoBasicVm.dco.title || 'ADD_TITLE' |
                translate"></span>
            </a>
            <span ng-if="(!hasPermission('change','dco','edit') || !hasPermission('change','edit')) || dco.isReleased || dco.statusType == 'REJECTED'">{{dcoBasicVm.dco.title}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="(hasPermission('change','dco','edit') || hasPermission('change','edit')) && !dco.isReleased && dco.statusType != 'REJECTED'"
               href=""
               onaftersave="dcoBasicVm.updateDco()"
               editable-textarea="dcoBasicVm.dco.description"><span ng-bind-html="(dcoBasicVm.dco.description ) || 'ADD_DESCRIPTION' |
                translate" title="{{dcoBasicVm.dco.description}}"></span>
            </a>
            <span ng-if="(!hasPermission('change','dco','edit') || !hasPermission('change','edit')) || dco.isReleased || dco.statusType == 'REJECTED'">{{dcoBasicVm.dco.description}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REASON_FOR_CHANGE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="(hasPermission('change','dco','edit') || hasPermission('change','edit')) && !dco.isReleased && dco.statusType != 'REJECTED'"
               href=""
               onaftersave="dcoBasicVm.updateDco()"
               editable-textarea="dcoBasicVm.dco.reasonForChange"><span ng-bind-html="(dcoBasicVm.dco.reasonForChange  ) || 'ADD_REASON_FOR_CHANGE' |
                translate" title="{{dcoBasicVm.dco.reasonForChange}}"></span>
            </a>
            <span ng-if="(!hasPermission('change','dco','edit') || !hasPermission('change','edit')) || dco.isReleased || dco.statusType == 'REJECTED'">{{dcoBasicVm.dco.reasonForChange}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>WORKFLOW_STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <workflow-status-settings workflow="dcoBasicVm.dco"></workflow-status-settings>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>Revision Creation Rule</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span ng-if="dcoBasicVm.dco.revisionCreationType == 'WORKFLOW_START'">Workflow Start</span>
            <span ng-if="dcoBasicVm.dco.revisionCreationType == 'ACTIVITY_COMPLETION'">Workflow Activity Completion</span>
        </div>
    </div>
    <div class="row" ng-if="dcoBasicVm.dco.isReleased && dcoBasicVm.dco.statusType != 'REJECTED'">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>RELEASED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcoBasicVm.dco.releasedDate}}</span>
        </div>
    </div>
    <div class="row" ng-if="dcoBasicVm.dco.statusType == 'REJECTED'">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REJECTED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcoBasicVm.dco.releasedDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>OWNER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 250px"
               ng-if="hasPermission('admin','all') && !dco.isReleased && dco.statusType != 'REJECTED'"
               onaftersave="dcoBasicVm.updateDco()"
               editable-select="dcoBasicVm.dco.changeAnalystObject"
               title="{{dcoBasicVm.clickToUpdatePerson}}"
               e-ng-options="person as person.fullName for person in dcoBasicVm.changeAnalysts track by person.id">
                {{dcoBasicVm.dco.changeAnalystObject.fullName}}
            </a>
            <span ng-if="!hasPermission('admin','all') || dco.isReleased || dco.statusType == 'REJECTED'">{{dcoBasicVm.dco.changeAnalystObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcoBasicVm.dco.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcoBasicVm.dco.createdByObject.fullName}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span>
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcoBasicVm.dco.modifiedDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span>
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{dcoBasicVm.dco.modifiedByObject.fullName}}</span>
        </div>
    </div>

    <basic-attribute-details-view object-type="CHANGE"
                                  quality-type="CHANGE"
                                  has-permission="(hasPermission('change','dco','edit') || hasPermission('change','edit')) && !dco.isReleased && dco.statusType != 'REJECTED'"
                                  object-id="dco.id"></basic-attribute-details-view>
    <object-attribute-details-view object-type="CHANGETYPE" show-attributes="true"
                                   has-permission="(hasPermission('change','dco','edit') || hasPermission('change','edit')) && !dco.isReleased && dco.statusType != 'REJECTED'"
                                   object-type-id="dco.dcoType"
                                   object-id="dco.id"></object-attribute-details-view>

</div>

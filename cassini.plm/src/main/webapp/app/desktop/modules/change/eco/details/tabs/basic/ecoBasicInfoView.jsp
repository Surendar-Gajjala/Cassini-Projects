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

<div ng-if="ecoBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">{{ 'LOADING_ECO_DETAILS' | translate}}
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="ecoBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ECO_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecoBasicVm.eco.ecoNumber}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ECO_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecoBasicVm.eco.ecoTypeObject.name}}</span>
        </div>
    </div>
    <div class="row" ng-if="ecoBasicVm.eco.released">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>RELEASED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecoBasicVm.eco.releasedDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TITLE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <%-- <span>{{ecoBasicVm.eco.title}}</span>--%>
            <a ng-if="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && ecoStatus == false"
               href=""
               e-style="width:250px"
               ng-hide="external.external == true" onaftersave="ecoBasicVm.updateEco()"
               editable-text="ecoBasicVm.eco.title"
                    ><span ng-bind-html="ecoBasicVm.eco.title || 'ADD_TITLE' |
                translate"></span></a>

                <span ng-if="!(hasPermission('change','eco','edit') || hasPermission('change','edit')) || ecoStatus == true"
                      ng-bind-html="ecoBasicVm.eco.title"></span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && ecoStatus == false"
               href=""
               ng-hide="external.external == true" onaftersave="ecoBasicVm.updateEco()"
               editable-textarea="ecoBasicVm.eco.description"><span ng-bind-html="(ecoBasicVm.eco.description) || 'ADD_DESCRIPTION' |
                translate" title="{{ecoBasicVm.eco.description}}"></span> </a>

            <span ng-if="!(hasPermission('change','eco','edit') || hasPermission('change','edit')) || ecoStatus == true"
                  ng-bind-html="ecoBasicVm.eco.description"></span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REASON_FOR_CHANGE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && ecoStatus == false"
               href=""
               ng-hide="external.external == true" onaftersave="ecoBasicVm.updateEco()"
               editable-textarea="ecoBasicVm.eco.reasonForChange"><span ng-bind-html="(ecoBasicVm.eco.reasonForChange) || 'ADD_REASON_FOR_CHANGE' |
                translate" title="{{ecoBasicVm.eco.reasonForChange}}"></span></a>

            <span ng-if="!(hasPermission('change','eco','edit') || hasPermission('change','edit')) || ecoStatus == true"
                  ng-bind-html="ecoBasicVm.eco.reasonForChange"></span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>WORKFLOW_STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <workflow-status-settings workflow="ecoBasicVm.eco"></workflow-status-settings>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>Revision Creation Rule</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span ng-if="ecoBasicVm.eco.revisionCreationType == 'WORKFLOW_START'">Workflow Start</span>
            <span ng-if="ecoBasicVm.eco.revisionCreationType == 'ACTIVITY_COMPLETION'">Workflow Activity Completion</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>OWNER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 250px"
               ng-if="hasPermission('admin','all') && ecoStatus == false"
               onaftersave="ecoBasicVm.updateEco()"
               editable-select="ecoBasicVm.eco.ecoOwnerObject"
               title="{{ecoBasicVm.clickToUpdatePerson}}"
               e-ng-options="person as person.fullName for person in ecoBasicVm.changeAnalysts | orderBy:'fullName' track by person.id">
                {{ecoBasicVm.eco.ecoOwnerObject.fullName}}
            </a>
            <span ng-if="!hasPermission('admin','all') || ecoStatus == true">{{ecoBasicVm.eco.ecoOwnerObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecoBasicVm.eco.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecoBasicVm.eco.createdByObject.fullName}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecoBasicVm.eco.modifiedDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{ecoBasicVm.eco.modifiedByObject.fullName}}</span>
        </div>
    </div>

    <basic-attribute-details-view object-type="CHANGE"
                                  quality-type="CHANGE"
                                  has-permission="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && !eco.released && eco.statusType != 'REJECTED'"
                                  object-id="eco.id"></basic-attribute-details-view>

    <object-attribute-details-view object-type="CHANGETYPE" show-attributes="true"
                                   has-permission="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && !eco.released && eco.statusType != 'REJECTED'"
                                   object-type-id="eco.ecoType"
                                   object-id="eco.id"></object-attribute-details-view>

</div>

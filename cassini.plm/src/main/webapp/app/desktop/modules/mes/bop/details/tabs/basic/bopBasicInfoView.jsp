<style>
    .thumbnail-container {
        border: 1px solid #ddd;
        height: 380px;
        width: 380px;
        position: absolute;
        right: 30px;
        background-color: #fff;
        z-index: 10 !important;
    }

    .medium-image {
        max-height: 378px;
        width: 378px;
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
    }

    .col-sm-5 .editable-textarea {
        width: 450px;
    }
</style>
<div ng-if="bopBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
       <span translate>LOADING_BOP_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="bopBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MBOM</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="" ng-click="bopBasicVm.showMBOM()"
               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                {{bopBasicVm.bopRevision.mbomNumber}} - {{bopBasicVm.bopRevision.mbomName}} -
                {{bopBasicVm.bopRevision.mbomRevisionName}}
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>BOP_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{bopBasicVm.bop.number}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>BOP_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{bopBasicVm.bop.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5"
             ng-class="{'cursor-override': !hasPermission('bop','edit') || bopRevision.released || bopRevision.rejected}"
             title="{{hasPermission('bop','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px"
               ng-class="{'permission-text-disabled': !hasPermission('bop','edit') || bopRevision.released || bopRevision.rejected}"
               onaftersave="bopBasicVm.updateBOP()"
               editable-text="bopBasicVm.bop.name">
                <span ng-bind-html="bopBasicVm.bop.name"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-4 col-sm-5"
             ng-class="{'cursor-override': !hasPermission('bop','edit') || bopRevision.released || bopRevision.rejected}"
             title="{{hasPermission('bop','edit') ? '' : noPermission}}">

            <a href=""
               ng-class="{'permission-text-disabled': !hasPermission('bop','edit') || bopRevision.released || bopRevision.rejected}"
               onaftersave="bopBasicVm.updateBOP()"
               editable-textarea="bopBasicVm.bop.description"><span ng-bind-html="(bopBasicVm.bop.description) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{bopBasicVm.bop.description}}"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REVISION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            {{bopBasicVm.bopRevision.revision}}
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>LIFECYCLE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <item-status item="bopBasicVm.bopRevision"></item-status>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>WORKFLOW_STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <workflow-status-settings workflow="bopBasicVm.bopRevision"></workflow-status-settings>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span ng-if="bopBasicVm.bopRevision.released || (!bopBasicVm.bopRevision.released && !bopBasicVm.bopRevision.rejected)"
                  translate>RELEASED_DATE</span>
            <span ng-if="bopBasicVm.bopRevision.rejected" translate>REJECTED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            {{bopBasicVm.bopRevision.releasedDate}}
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{bopBasicVm.bop.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{bopBasicVm.bop.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{bopBasicVm.bop.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{bopBasicVm.bop.modifiedDate}}</span>
        </div>
    </div>
    <div ng-if="bopBasicVm.bop.bopData != null">
        <mes-mfr-data update-type="'update'" has-permission="true"
                      update-bop="bopBasicVm.updateBop"
                      bop-data="bopBasicVm.bop.bopData"></mes-bop-data>
    </div>
    <object-attribute-details-view object-type="MESOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{bop.objectType}}"
                                   has-permission="true"
                                   object-type-id="bop.type.id"
                                   object-id="bop.id"></object-attribute-details-view>
</div>

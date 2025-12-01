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

<div ng-if="mcoBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading MCO details...
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="mcoBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MCO_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{mcoBasicVm.mco.mcoNumber}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MCO_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{mcoBasicVm.mco.mcoType.name}}</span>
        </div>
    </div>


    <!-- <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>QCR</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="" ui-sref="app.pqm.qcr.details({qcrId:mcoBasicVm.qcr.id})" title="{{clickToShowDetails}}">{{mcoBasicVm.qcr.qcrNumber}}</a>
        </div>
    </div> -->


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TITLE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a ng-if="(hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mco.released && mco.statusType != 'REJECTED'"
               href=""
               e-style="width:250px"
               ng-hide="external.external == true" onaftersave="mcoBasicVm.updateMco()"
               editable-text="mcoBasicVm.mco.title"><span ng-bind-html="mcoBasicVm.mco.title || 'ADD_TITLE' |
                translate"></span>
            </a>
            <span ng-if="(!hasPermission('change','mco','edit') || !hasPermission('change','edit')) || mco.released || mco.statusType == 'REJECTED'">{{mcoBasicVm.mco.title}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="(hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mco.released && mco.statusType != 'REJECTED'"
               href=""
               onaftersave="mcoBasicVm.updateMco()"
               editable-textarea="mcoBasicVm.mco.description"><span ng-bind-html="(mcoBasicVm.mco.description  )|| 'ADD_DESCRIPTION' |
                translate" title="{{mcoBasicVm.mco.description}}"></span>
            </a>

            <span ng-if="(!hasPermission('change','mco','edit') || !hasPermission('change','edit')) || mco.released || mco.statusType == 'REJECTED'"
                  ng-bind-html="mcoBasicVm.mco.description"></span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REASON_FOR_CHANGE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="(hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mco.released && mco.statusType != 'REJECTED'"
               href=""
               onaftersave="mcoBasicVm.updateMco()"
               editable-textarea="mcoBasicVm.mco.reasonForChange"><span ng-bind-html="(mcoBasicVm.mco.reasonForChange ) || 'ADD_REASON_FOR_CHANGE' |
                translate" title="{{mcoBasicVm.mco.reasonForChange}}"></span>
            </a>

            <span ng-if="(!hasPermission('change','mco','edit') || !hasPermission('change','edit')) || mco.released || mco.statusType == 'REJECTED'"
                  ng-bind-html="mcoBasicVm.mco.reasonForChange"></span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>WORKFLOW_STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <workflow-status-settings workflow="mcoBasicVm.mco"></workflow-status-settings>
            <%--<mco-status mco="mcoBasicVm.mco"></mco-status>--%>
        </div>
    </div>
    <div class="row" ng-if="mcoBasicVm.mco.released">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>RELEASED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{mcoBasicVm.mco.releasedDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CHANGE_ANALYST</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 250px"
               ng-if="hasPermission('admin','all') && !mco.released && mco.statusType != 'REJECTED'"
               onaftersave="mcoBasicVm.updateMco()"
               editable-select="mcoBasicVm.mco.changeAnalystObject"
               title="{{mcoBasicVm.clickToUpdatePerson}}"
               e-ng-options="person as person.fullName for person in mcoBasicVm.changeAnalysts | orderBy:'fullName' track by person.id">
                {{mcoBasicVm.mco.changeAnalystObject.fullName}}
            </a>
            <span ng-if="!hasPermission('admin','all') || mco.released || mco.statusType == 'REJECTED'">{{mcoBasicVm.mco.changeAnalystObject.fullName}}</span>
        </div>
    </div>
    <div class="row" ng-if="mcoBasicVm.mco.mcoType.mcoType == 'ITEMMCO'">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>Revision Creation Rule</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span ng-if="mcoBasicVm.mco.revisionCreationType == 'WORKFLOW_START'">Workflow Start</span>
            <span ng-if="mcoBasicVm.mco.revisionCreationType == 'ACTIVITY_COMPLETION'">Workflow Activity Completion</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{mcoBasicVm.mco.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{mcoBasicVm.mco.createdDate}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{mcoBasicVm.mco.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{mcoBasicVm.mco.modifiedDate}}</span>
        </div>
    </div>

    <basic-attribute-details-view object-type="CHANGE"
                                  quality-type="CHANGE"
                                  has-permission="(hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mcoBasicVm.mco.released && mcoBasicVm.mco.statusType != 'REJECTED'"
                                  object-id="mcoBasicVm.mco.id"></basic-attribute-details-view>
    <object-attribute-details-view object-type="CHANGETYPE" show-attributes="true"
                                   has-permission="(hasPermission('change','mco','edit') || hasPermission('change','edit')) && !mco.released && mco.statusType != 'REJECTED'"
                                   object-type-id="mco.mcoType.id"
                                   object-id="mco.id"></object-attribute-details-view>

</div>

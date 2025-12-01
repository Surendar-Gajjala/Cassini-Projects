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

<div ng-if="reqDocBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_REQ_DOC_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="reqDocBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqDocBasicVm.reqDocumentRevision.master.number}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqDocBasicVm.reqDocumentRevision.master.type.name}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <a href="" ng-if="reqDocumentRevision.lifeCyclePhase.phaseType != 'RELEASED'"
               e-style="width:250px" onaftersave="reqDocBasicVm.updateReqDocument()"
               editable-text="reqDocBasicVm.reqDocumentRevision.master.name">
                <span ng-bind-html="reqDocBasicVm.reqDocumentRevision.master.name || 'ADD_NAME' | translate"></span>
            </a>
            <span ng-if="reqDocumentRevision.lifeCyclePhase.phaseType == 'RELEASED'">
                {{reqDocBasicVm.reqDocumentRevision.master.name }}
            </span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a href="" ng-if="reqDocumentRevision.lifeCyclePhase.phaseType != 'RELEASED'"
               onaftersave="reqDocBasicVm.updateReqDocument()"
               editable-textarea="reqDocBasicVm.reqDocumentRevision.master.description"><span ng-bind-html="(reqDocBasicVm.reqDocumentRevision.master.description ) || 'ADD_DESCRIPTION' |
                translate" title="{{reqDocBasicVm.reqDocumentRevision.master.description}}"></span>
            </a>
                 <span ng-if="reqDocumentRevision.lifeCyclePhase.phaseType == 'RELEASED'">
                {{reqDocBasicVm.reqDocumentRevision.master.description}}
            </span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DOCUMENT_OWNER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 250px" ng-if="reqDocumentRevision.lifeCyclePhase.phaseType != 'RELEASED'"
               onaftersave="reqDocBasicVm.updatePerson()"
               editable-select="reqDocBasicVm.reqDocumentRevision.owner"
               title="{{dcoBasicVm.clickToUpdatePerson}}"
               e-ng-options="person as person.fullName for person in reqDocBasicVm.persons track by person.id">
                {{reqDocBasicVm.reqDocumentRevision.owner.fullName}}
            </a>
                 <span ng-if="reqDocumentRevision.lifeCyclePhase.phaseType == 'RELEASED'">
                {{reqDocBasicVm.reqDocumentRevision.owner.fullName}}
            </span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REVISION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqDocBasicVm.reqDocumentRevision.revision}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>LIFE_CYCLE_PHASE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <item-status item="reqDocBasicVm.reqDocumentRevision"></item-status>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>WORKFLOW_STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <workflow-status-settings workflow="reqDocBasicVm.reqDocumentRevision"></workflow-status-settings>
            <!-- <span>{{reqDocBasicVm.reqDocumentRevision.workflowStatus}}</span> -->
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqDocBasicVm.reqDocumentRevision.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqDocBasicVm.reqDocumentRevision.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqDocBasicVm.reqDocumentRevision.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqDocBasicVm.reqDocumentRevision.modifiedDate}}</span>
        </div>
    </div>
    <basic-attribute-details-view object-type="REQUIREMENTDOCUMENT"
                                  quality-type="REQUIREMENTDOCUMENT"
                                  has-permission="true"
                                  object-id="reqDocumentRevision.master.id"></basic-attribute-details-view>
    <object-attribute-details-view object-type="REQUIREMENTDOCUMENTTYPE" show-attributes="true"
                                   actual-object-type="{{reqDocumentRevision.objectType}}"
                                   has-permission="true"
                                   object-type-id="reqDocumentRevision.master.type.id"
                                   object-id="reqDocumentRevision.id"></object-attribute-details-view>
</div>

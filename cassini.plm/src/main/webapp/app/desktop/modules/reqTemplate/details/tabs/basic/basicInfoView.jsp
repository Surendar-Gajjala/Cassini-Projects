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

<div ng-if="reqDocTemplateBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_REQ_DOC_TEMPLATE_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="reqDocTemplateBasicVm.loading == false">

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqDocTemplateBasicVm.reqDocTemplate.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <a href="" ng-if="reqDocTemplate.lifeCyclePhase.phaseType != 'RELEASED'"
               e-style="width:250px" onaftersave="reqDocTemplateBasicVm.updateReqDocTemplate()"
               editable-text="reqDocTemplateBasicVm.reqDocTemplate.name">
                <span ng-bind-html="reqDocTemplateBasicVm.reqDocTemplate.name || 'ADD_NAME' | translate"></span>
            </a>
            <span ng-if="reqDocTemplate.lifeCyclePhase.phaseType == 'RELEASED'">
                {{reqDocTemplateBasicVm.reqDocTemplate.name }}
            </span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a href="" ng-if="reqDocTemplate.lifeCyclePhase.phaseType != 'RELEASED'"
               onaftersave="reqDocTemplateBasicVm.updateReqDocTemplate()"
               editable-textarea="reqDocTemplateBasicVm.reqDocTemplate.description">
                <span ng-bind-html="(reqDocTemplateBasicVm.reqDocTemplate.description ) || 'ADD_DESCRIPTION' |
                translate" title="{{reqDocTemplateBasicVm.reqDocTemplate.description}}"></span>
            </a>
                 <span ng-if="reqDocTemplate.lifeCyclePhase.phaseType == 'RELEASED'">
                {{reqDocTemplateBasicVm.reqDocTemplate.description}}
            </span>
        </div>
    </div>
    
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>LIFE_CYCLE_PHASE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <item-status item="reqDocTemplateBasicVm.reqDocTemplate"></item-status>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqDocTemplateBasicVm.reqDocTemplate.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqDocTemplateBasicVm.reqDocTemplate.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqDocTemplateBasicVm.reqDocTemplate.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqDocTemplateBasicVm.reqDocTemplate.modifiedDate}}</span>
        </div>
    </div>
   
</div>

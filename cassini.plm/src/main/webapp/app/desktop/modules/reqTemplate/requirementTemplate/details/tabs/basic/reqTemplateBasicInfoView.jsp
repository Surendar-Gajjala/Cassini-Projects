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

    .image.image_resized > img {
        width: 100%;
        height: 100%;
    }

    p .marker-yellow {
        background-color: yellow;
    }

    .ck.ck-reset_all.ck-widget__type-around,
    .ck.ck-widget__selection-handle,
    .ck.ck-reset_all, .ck.ck-reset_all,
    .ck-editor__editable.ck-editor__nested-editable.ck-placeholder {
        display: none !important;
    }

    .item-details .req-description {
        max-width: 1000px;
    }

</style>

<div ng-if="reqTemplateBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_REQUIREMENTS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="reqTemplateBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqTemplateBasicVm.reqTemplate.type.name}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <a href=""
               e-style="width:250px" onaftersave="reqTemplateBasicVm.updateRequirement()"
               editable-text="reqTemplateBasicVm.reqTemplate.name">
                <span ng-bind-html="reqTemplateBasicVm.reqTemplate.name || 'ADD_NAME' | translate"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a class="req-description" href=""
               onaftersave="reqTemplateBasicVm.updateRequirement()"
               editable-textarea="reqTemplateBasicVm.reqTemplate.description">
                <span ng-bind-html="reqTemplateBasicVm.reqTemplate.descriptionHtml ||'CLICK_TO_ENTER_DESCRIPTION' | translate"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PRIORITY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <priority object="reqTemplateBasicVm.reqTemplate"></priority>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ASSIGNED_TO</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 250px"
               onaftersave="reqTemplateBasicVm.updateRequirement()"
               editable-select="reqTemplateBasicVm.reqTemplate.assignedToObject"
               title="{{reqTemplateBasicVm.clickToUpdatePerson}}"
               e-ng-options="person as person.fullName for person in reqTemplateBasicVm.persons track by person.id">
                {{reqTemplateBasicVm.reqTemplate.assignedToObject.fullName}}
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>LIFE_CYCLE_PHASE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <item-status item="reqTemplateBasicVm.reqTemplate"></item-status>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqTemplateBasicVm.reqTemplate.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqTemplateBasicVm.reqTemplate.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqTemplateBasicVm.reqTemplate.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{reqTemplateBasicVm.reqTemplate.modifiedDate}}</span>
        </div>
    </div>

</div>

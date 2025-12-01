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
<div ng-if="assemblyLineBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
          <span translate>LOADING_ASSEMBLY_LINE_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="assemblyLineBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{assemblyLineBasicVm.assemblyLine.number}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TYPE</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <span>{{assemblyLineBasicVm.assemblyLine.type.name}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>

        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('assemblyline','edit')}"
             title="{{hasPermission('assemblyline','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('assemblyline','edit')}"
               onaftersave="assemblyLineBasicVm.updateAssemblyLine()"
               editable-text="assemblyLineBasicVm.assemblyLine.name">
                <span ng-bind-html="assemblyLineBasicVm.assemblyLine.name"></span>
            </a>
        </div>

    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PLANT</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{assemblyLineBasicVm.assemblyLine.plantName}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>

        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('assemblyline','edit')}"
             title="{{hasPermission('assemblyline','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('assemblyline','edit')}"
               onaftersave="assemblyLineBasicVm.updateAssemblyLine()"
               editable-textarea="assemblyLineBasicVm.assemblyLine.description">
                <span ng-bind-html="(assemblyLineBasicVm.assemblyLine.description) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{assemblyLineBasicVm.assemblyLine.description}}"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{assemblyLineBasicVm.assemblyLine.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{assemblyLineBasicVm.assemblyLine.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{assemblyLineBasicVm.assemblyLine.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{assemblyLineBasicVm.assemblyLine.modifiedDate}}</span>
        </div>
    </div>
    <object-attribute-details-view object-type="MESOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{assemblyLine.objectType}}"
                                   has-permission="true"
                                   object-type-id="assemblyLine.type.id"
                                   object-id="assemblyLine.id"></object-attribute-details-view>
</div>

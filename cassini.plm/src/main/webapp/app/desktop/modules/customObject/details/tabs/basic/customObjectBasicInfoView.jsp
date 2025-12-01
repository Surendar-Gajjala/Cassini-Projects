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

<div ng-if="customObjectBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>
            LOADING_CUSTOM_OBJECT_DETAILS
        </span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="customObjectBasicVm.loading == false">

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>Name</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('customObject','edit')}"
               onaftersave="customObjectBasicVm.updateObject()"
               editable-text="customObjectBasicVm.customObject.name"><span ng-bind-html="(customObjectBasicVm.customObject.name ) || 'ADD_NAME' |
                translate" title="{{customObjectBasicVm.customObject.name}}"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>Number</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{customObjectBasicVm.customObject.number}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>Type</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{customObjectBasicVm.customObject.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>Description</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('customObject','edit')}"
               onaftersave="customObjectBasicVm.updateObject()"
               editable-textarea="customObjectBasicVm.customObject.description"><span ng-bind-html="(customObjectBasicVm.customObject.description ) || 'ADD_DESCRIPTION' |
                translate" title="{{customObjectBasicVm.customObject.description}}"></span>
            </a>
        </div>
    </div>
    <div class="row"
         ng-if="customObjectBasicVm.customObject.type.name == 'Supplier Performance Rating' || customObjectBasicVm.customObject.type.name == 'CPI Form' || customObjectBasicVm.customObject.type.name == '4MChange-Supplier'">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>SUPPLIER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" style="line-height: 20px">
            <a href="#" ng-class="{'permission-text-disabled': !hasPermission('customObject','edit')}"
               e-style="width: 50%"
               onaftersave="customObjectBasicVm.updateObject()"
               editable-select="customObjectBasicVm.customObject.supplierObject"
               title="{{customObjectBasicVm.clickToUpdatePerson}}"
               e-ng-options="supplier as supplier.name for supplier in customObjectBasicVm.suppliers track by supplier.id">
                {{customObjectBasicVm.customObject.supplierObject.name}}
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>WORKFLOW_STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <workflow-status-settings workflow="customObjectBasicVm.customObject"></workflow-status-settings>
            <!-- <span>{{customObjectBasicVm.customObject.workflowStatus}}</span> -->
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{customObjectBasicVm.customObject.createdDate}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{customObjectBasicVm.customObject.createdByObject.fullName}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{customObjectBasicVm.customObject.modifiedDate}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{customObjectBasicVm.customObject.modifiedByObject.fullName}}</span>
        </div>
    </div>

    <basic-attribute-details-view object-type="CUSTOMOBJECT"
                                  quality-type="CUSTOMOBJECT"
                                  has-permission="hasPermission('customobject','edit') && loginPersonDetails.external == false"
                                  object-id="customObject.id"></basic-attribute-details-view>

    <object-attribute-details-view object-type="CUSTOMOBJECTTYPE" show-attributes="true"
                                   has-permission="hasPermission('customobject','edit') && loginPersonDetails.external == false"
                                   object-type-id="customObject.type.id"
                                   object-id="customObject.id"></object-attribute-details-view>


</div>

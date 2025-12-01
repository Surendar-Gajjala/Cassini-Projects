<div ng-if="operationBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
      <span translate>LOADING_OPERATION_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="operationBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>OPERATION_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{operationBasicVm.operation.number}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>OPERATION_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{operationBasicVm.operation.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('operation','edit')}"
             title="{{hasPermission('operation','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('operation','edit')}"
               onaftersave="operationBasicVm.updateOperation()"
               editable-text="operationBasicVm.operation.name">
                <span ng-bind-html="operationBasicVm.operation.name"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('operation','edit')}"
             title="{{hasPermission('operation','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('operation','edit')}"
               onaftersave="operationBasicVm.updateOperation()"
               editable-textarea="operationBasicVm.operation.description"><span ng-bind-html="(operationBasicVm.operation.description) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{operationBasicVm.operation.description}}"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{operationBasicVm.operation.createdByObject.fullName}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{operationBasicVm.operation.createDateDe}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{operationBasicVm.operation.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{operationBasicVm.operation.modifiedDate}}</span>
        </div>
    </div>
    <object-attribute-details-view object-type="MESOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{operation.objectType}}"
                                   has-permission="true"
                                   object-type-id="operation.type.id"
                                   object-id="operation.id"></object-attribute-details-view>

</div>

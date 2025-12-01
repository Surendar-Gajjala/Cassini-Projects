<div ng-if="sparePartBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
       <span translate>LOADING_SPARE_PARTS_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="sparePartBasicVm.loading == false">

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>SPARE_PART_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{sparePartBasicVm.sparePart.number}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>SPARE_PART_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{sparePartBasicVm.sparePart.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mrosparepart','edit')}"
             title="{{hasPermission('mrosparepart','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('mrosparepart','edit')}"
               onaftersave="sparePartBasicVm.updateSparePart()"
               editable-text="sparePartBasicVm.sparePart.name">
                <span ng-bind-html="sparePartBasicVm.sparePart.name "
                      title="{{sparePartBasicVm.sparePart.name}}"></span>
                {{sparePartBasicVm.sparePart.name.length > 20 ? '...' : ''}}
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mrosparepart','edit')}"
             title="{{hasPermission('mrosparepart','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('mrosparepart','edit')}"
               onaftersave="sparePartBasicVm.updateSparePart()"
               editable-textarea="sparePartBasicVm.sparePart.description"><span ng-bind-html="(sparePartBasicVm.sparePart.description ) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{sparePartBasicVm.sparePart.description}}"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{sparePartBasicVm.sparePart.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{sparePartBasicVm.sparePart.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{sparePartBasicVm.sparePart.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{sparePartBasicVm.sparePart.modifiedDate}}</span>
        </div>
    </div>
    <object-attribute-details-view object-type="MROOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{sparePart.objectType}}"
                                   has-permission="true"
                                   object-type-id="sparePart.type.id"
                                   object-id="sparePart.id"></object-attribute-details-view>
</div>

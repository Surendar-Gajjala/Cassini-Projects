<div ng-if="maintenancePlanBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
       <span translate>LOADING_MAINTENANCE_PLAN_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="maintenancePlanBasicVm.loading == false">

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{maintenancePlanBasicVm.maintenancePlan.number}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mromaintenanceplan','edit')}"
             title="{{hasPermission('mromaintenanceplan','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('mromaintenanceplan','edit')}"
               onaftersave="maintenancePlanBasicVm.updateMaintenancePlan()"
               editable-text="maintenancePlanBasicVm.maintenancePlan.name">
                <span ng-bind-html="maintenancePlanBasicVm.maintenancePlan.name "
                      title="{{maintenancePlanBasicVm.maintenancePlan.name}}"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mromaintenanceplan','edit')}"
             title="{{hasPermission('mromaintenanceplan','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('mromaintenanceplan','edit')}"
               onaftersave="maintenancePlanBasicVm.updateMaintenancePlan()"
               editable-textarea="maintenancePlanBasicVm.maintenancePlan.description">
                <span ng-bind-html="(maintenancePlanBasicVm.maintenancePlan.description ) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{maintenancePlanBasicVm.maintenancePlan.description}}"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ASSET</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="" ng-click="maintenancePlanBasicVm.showAsset(maintenancePlanBasicVm.maintenancePlan)">
                <span ng-bind-html="maintenancePlanBasicVm.maintenancePlan.assetObject.name"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{maintenancePlanBasicVm.maintenancePlan.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{maintenancePlanBasicVm.maintenancePlan.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{maintenancePlanBasicVm.maintenancePlan.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{maintenancePlanBasicVm.maintenancePlan.modifiedDate}}</span>
        </div>
    </div>
</div>

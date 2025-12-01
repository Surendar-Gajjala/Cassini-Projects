<div ng-if="workCenterBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
         <span translate>LOADING_WORKCENTER_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="workCenterBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{workCenterBasicVm.workCenter.number}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{workCenterBasicVm.workCenter.type.name}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('workcenter','edit')}"
             title="{{hasPermission('workcenter','edit') ? '' : noPermission}}">
            <a href="" onaftersave="workCenterBasicVm.updateWorkCenter()"
               ng-class="{'permission-text-disabled': !hasPermission('workcenter','edit')}"
               editable-text="workCenterBasicVm.workCenter.name">
                <span ng-bind-html="workCenterBasicVm.workCenter.name"
                      title="{{workCenterBasicVm.workCenter.name}}"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PLANT</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{workCenterBasicVm.workCenter.plantObject.name}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ASSEMBLY_LINE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{workCenterBasicVm.workCenter.assemblyLineName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('workcenter','edit')}"
             title="{{hasPermission('workcenter','edit') ? '' : noPermission}}">
            <a href="" onaftersave="workCenterBasicVm.updateWorkCenter()"
               ng-class="{'permission-text-disabled': !hasPermission('workcenter','edit')}"
               editable-textarea="workCenterBasicVm.workCenter.description">
                <span ng-bind-html="(workCenterBasicVm.workCenter.description) || 'ADD_DESCRIPTION' | translate"
                      title="{{workCenterBasicVm.workCenter.description}}"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>LOCATION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('workcenter','edit')}"
             title="{{hasPermission('workcenter','edit') ? '' : noPermission}}">
            <a href="" onaftersave="workCenterBasicVm.updateWorkCenter()"
               ng-class="{'permission-text-disabled': !hasPermission('workcenter','edit')}"
               editable-text="workCenterBasicVm.workCenter.location">
                <span id="location" ng-bind-html="(workCenterBasicVm.workCenter.location ) || 'ADD_LOCATION' | translate"
                      title="{{workCenterBasicVm.workCenter.location}}"></span>
                {{workCenterBasicVm.workCenter.location.length > 20 ? '...' : ''}}
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('workcenter','edit')}"
             title="{{hasPermission('workcenter','edit') ? '' : noPermission}}">
            <span ng-if="workCenterBasicVm.workCenter.active == true && !workCenterBasicVm.editStatus"
                  class="label label-outline bg-light-success" translate>C_ACTIVE</span>
            <span ng-if="workCenterBasicVm.workCenter.active == false && !workCenterBasicVm.editStatus"
                  class="label label-outline bg-light-danger" translate>C_INACTIVE</span>
            <a href="" class="fa fa-pencil row-edit-btn" ng-if="!workCenterBasicVm.editStatus"
               ng-class="{'permission-text-disabled': !hasPermission('workcenter','edit')}"
               ng-click="workCenterBasicVm.changeStatus()" title="{{EDIT | translate}}"></a>

            <div style="display: flex;" ng-if="workCenterBasicVm.editStatus">
                <ui-select ng-model="workCenterBasicVm.workCenter.active" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="status.value as status in workCenterBasicVm.statuses | filter: $select.search">
                        <div>{{status.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="workCenterBasicVm.updateWorkCenter()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="workCenterBasicVm.cancelStatus()">
                    <i class="fa fa-times"></i>
                </button>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REQUIRESMAINTENANCE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('workcenter','edit')}"
             title="{{hasPermission('workcenter','edit') ? '' : noPermission}}">
            <span ng-if="workCenterBasicVm.workCenter.requiresMaintenance && !workCenterBasicVm.editMaintenance"
                  class="label label-outline bg-light-success" translate>YES</span>
            <span ng-if="!workCenterBasicVm.workCenter.requiresMaintenance && !workCenterBasicVm.editMaintenance"
                  class="label label-outline bg-light-danger" translate>NO</span>
            <a href="" ng-if="!workCenterBasicVm.editMaintenance" class="fa fa-pencil row-edit-btn"
               ng-class="{'permission-text-disabled': !hasPermission('workcenter','edit')}"
               ng-click="workCenterBasicVm.changeMaintenance()"
               title="{{workCenterBasicVm.assets.length > 0 ? assetAlreadyAdded : ''}}">
            </a>

            <div style="display: flex;" ng-if="workCenterBasicVm.editMaintenance">
                <ui-select ng-model="workCenterBasicVm.workCenter.requiresMaintenance" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="maintenance.value as maintenance in workCenterBasicVm.maintenances | filter: $select.search">
                        <div>{{maintenance.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="workCenterBasicVm.updateWorkCenter()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="workCenterBasicVm.cancelMaintenance()">
                    <i class="fa fa-times"></i>
                </button>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{workCenterBasicVm.workCenter.createdByPerson.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{workCenterBasicVm.workCenter.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{workCenterBasicVm.workCenter.modifiedByPerson.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{workCenterBasicVm.workCenter.modifiedDate}}</span>
        </div>
    </div>
    <object-attribute-details-view object-type="MESOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{workCenter.objectType}}"
                                   has-permission="true"
                                   object-type-id="workCenter.type.id"
                                   object-id="workCenter.id"></object-attribute-details-view>

</div>

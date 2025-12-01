<div ng-if="manpowerBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
       <span translate>LOADING_MANPOWER_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="manpowerBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MANPOWER_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{manpowerBasicVm.manpower.number}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MANPOWER_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{manpowerBasicVm.manpower.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('manpower','edit')}"
             title="{{hasPermission('manpower','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('manpower','edit')}"
               onaftersave="manpowerBasicVm.updateManpower()"
               editable-text="manpowerBasicVm.manpower.name">
                <span ng-bind-html="manpowerBasicVm.manpower.name"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('manpower','edit')}"
             title="{{hasPermission('manpower','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('manpower','edit')}"
               onaftersave="manpowerBasicVm.updateManpower()"
               editable-textarea="manpowerBasicVm.manpower.description"><span ng-bind-html="(manpowerBasicVm.manpower.description ) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{manpowerBasicVm.manpower.description}}"></span>
            </a>
        </div>
    </div>

    <!-- <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PERSON</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <span>
                {{manpowerBasicVm.manpower.personObject.fullName}}
            </span>
        </div>
    </div> -->

 <%--   <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('manpower','edit')}"
             title="{{hasPermission('manpower','edit') ? '' : noPermission}}">
            <span ng-if="manpowerBasicVm.manpower.active == true && !manpowerBasicVm.editStatus"
                  class="label label-outline bg-light-success" translate>C_ACTIVE</span>
            <span ng-if="manpowerBasicVm.manpower.active == false && !manpowerBasicVm.editStatus"
                  class="label label-outline bg-light-danger" translate>C_INACTIVE</span>
            <a href="" class="fa fa-pencil row-edit-btn" ng-if="!manpowerBasicVm.editStatus" ng-class="{'permission-text-disabled': !hasPermission('manpower','edit')}"
               ng-click="manpowerBasicVm.changeStatus()" title="{{EDIT | translate}}"></a>

            <div style="display: flex;" ng-if="manpowerBasicVm.editStatus">
                <ui-select ng-model="manpowerBasicVm.manpower.active" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="status.value as status in manpowerBasicVm.statuses | filter: $select.search">
                        <div>{{status.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="manpowerBasicVm.updateManpower()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="manpowerBasicVm.cancelStatus()">
                    <i class="fa fa-times"></i>
                </button>
            </div>
        </div>
    </div>--%>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{manpowerBasicVm.manpower.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{manpowerBasicVm.manpower.createdDate}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{manpowerBasicVm.manpower.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{manpowerBasicVm.manpower.modifiedDate}}</span>
        </div>
    </div>
    <object-attribute-details-view object-type="MESOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{manpower.objectType}}"
                                   has-permission="true"
                                   object-type-id="manpower.type.id"
                                   object-id="manpower.id"></object-attribute-details-view>

</div>

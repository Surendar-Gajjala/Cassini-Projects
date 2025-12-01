<div ng-if="plantBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_PLANT_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="plantBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{plantBasicVm.plant.number}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PLANT_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{plantBasicVm.plant.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('plant','edit')}"
             title="{{hasPermission('plant','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px"
               ng-class="{'permission-text-disabled': !hasPermission('plant','edit')}"
               onaftersave="plantBasicVm.updatePlant()"
               editable-text="plantBasicVm.plant.name">
                <span ng-bind-html="plantBasicVm.plant.name"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('plant','edit')}"
             title="{{hasPermission('plant','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('plant','edit')}"
               onaftersave="plantBasicVm.updatePlant()"
               editable-textarea="plantBasicVm.plant.description">
                <span ng-bind-html="(plantBasicVm.plant.description) || 'CLICK_TO_ENTER_DESCRIPTION' | translate"
                      title="{{plantBasicVm.plant.description}}"></span>
            </a>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ADDRESS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('plant','edit')}"
             title="{{hasPermission('plant','edit') ? '' : noPermission}}">
            <a href="" ng-class="{'permission-text-disabled': !hasPermission('plant','edit')}"
               onaftersave="plantBasicVm.updatePlant()"
               editable-textarea="plantBasicVm.plant.address"><span ng-bind-html="(plantBasicVm.plant.address) || 'CLICK_TO_ENTER_ADDRESS' |
                translate" title="{{plantBasicVm.plant.address}}"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CITY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('plant','edit')}"
             title="{{hasPermission('plant','edit') ? '' : noPermission}}">

            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('plant','edit')}"
               onaftersave="plantBasicVm.updatePlant()"
               editable-text="plantBasicVm.plant.city"><span
                    ng-bind-html="(plantBasicVm.plant.city) || 'CLICK_TO_ENTER_CITY' |translate"></span>
            </a>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>COUNTRY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('plant','edit')}"
             title="{{hasPermission('plant','edit') ? '' : noPermission}}">

            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('plant','edit')}"
               onaftersave="plantBasicVm.updatePlant()"
               editable-text="plantBasicVm.plant.country">
                <span ng-bind-html="(plantBasicVm.plant.country) || 'CLICK_TO_ENTER_COUNTRY' |translate"></span>
            </a>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>POSTAL_CODE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('plant','edit')}"
             title="{{hasPermission('plant','edit') ? '' : noPermission}}">

            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('plant','edit')}"
               onaftersave="plantBasicVm.updatePlant()"
               editable-text="plantBasicVm.plant.postalCode">
                <span ng-bind-html="(plantBasicVm.plant.postalCode) || 'CLICK_TO_ENTER_POSTAL_CODE' |translate"></span>
            </a>

        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PHONE_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('plant','edit')}"
             title="{{hasPermission('plant','edit') ? '' : noPermission}}">

            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('plant','edit')}"
               onaftersave="plantBasicVm.updatePlant()"
               editable-text="plantBasicVm.plant.phoneNumber">
                <span ng-bind-html="(plantBasicVm.plant.phoneNumber) || 'CLICK_TO_ENTER_PHONE_NUMBER' |translate"></span>
            </a>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MOBILE_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('plant','edit')}"
             title="{{hasPermission('plant','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('plant','edit')}"
               onaftersave="plantBasicVm.updatePlant()"
               editable-text="plantBasicVm.plant.mobileNumber">
                <span
                        ng-bind-html="(plantBasicVm.plant.mobileNumber) || 'CLICK_TO_ENTER_MOBILE_NUMBER' |translate"></span>
            </a>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>FAX_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('plant','edit')}"
             title="{{hasPermission('plant','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('plant','edit')}"
               onaftersave="plantBasicVm.updatePlant()"
               editable-text="plantBasicVm.plant.faxAddress">
                <span
                        ng-bind-html="(plantBasicVm.plant.faxAddress) || 'CLICK_TO_ENTER_FAX_ADDRESS' |translate"></span>

            </a>

        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>EMAIL</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('plant','edit')}"
             title="{{hasPermission('plant','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('plant','edit')}"
               onaftersave="plantBasicVm.updatePlant()"
               editable-text="plantBasicVm.plant.email"><span
                    ng-bind-html="(plantBasicVm.plant.email) || 'CLICK_TO_ENTER_EMAIL' |translate"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PLANT_MANAGER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('plant','edit')}"
             title="{{hasPermission('plant','edit') ? '' : noPermission}}">
            <a href="" e-style="width: 50%" ng-class="{'permission-text-disabled': !hasPermission('plant','edit')}"
               onaftersave="plantBasicVm.updatePlant()"
               editable-select="plantBasicVm.plant.plantPersonObject"
               e-ng-options="person as person.fullName for person in plantBasicVm.persons track by person.id">
                {{plantBasicVm.plant.person}}
            </a>

        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REQUIRESMAINTENANCE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('plant','edit')}"
             title="{{hasPermission('plant','edit') ? '' : noPermission}}">
            <span ng-if="plantBasicVm.plant.requiresMaintenance && !plantBasicVm.editMaintenance"
                  class="label label-outline bg-light-success" translate>YES</span>
            <span ng-if="!plantBasicVm.plant.requiresMaintenance && !plantBasicVm.editMaintenance"
                  class="label label-outline bg-light-danger" translate>NO</span>
            <a href="" ng-if="!plantBasicVm.editMaintenance" class="fa fa-pencil row-edit-btn"
               ng-class="{'permission-text-disabled': !hasPermission('plant','edit')}"
               ng-click="plantBasicVm.changeMaintenance()"
               title="{{plantBasicVm.assets.length > 0 ? assetAlreadyAdded : ''}}">
            </a>

            <div style="display: flex;" ng-if="plantBasicVm.editMaintenance">
                <ui-select ng-model="plantBasicVm.plant.requiresMaintenance" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="maintenance.value as maintenance in plantBasicVm.maintenances | filter: $select.search">
                        <div>{{maintenance.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="plantBasicVm.updatePlant()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="plantBasicVm.cancelMaintenance()">
                    <i class="fa fa-times"></i>
                </button>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NOTES</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('plant','edit')}"
             title="{{hasPermission('plant','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('plant','edit')}"
               onaftersave="plantBasicVm.updatePlant()"
               editable-textarea="plantBasicVm.plant.notes">
                <span ng-bind-html="(plantBasicVm.plant.notes ) || 'CLICK_TO_ENTER_NOTES' | translate"
                      title="{{plantBasicVm.plant.notesHtml}}"></span>
            </a>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{plantBasicVm.plant.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{plantBasicVm.plant.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{plantBasicVm.plant.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{plantBasicVm.plant.modifiedDate}}</span>
        </div>
    </div>
    <object-attribute-details-view object-type="MESOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{plant.objectType}}"
                                   has-permission="true"
                                   object-type-id="plant.type.id"
                                   object-id="plant.id"></object-attribute-details-view>


</div>

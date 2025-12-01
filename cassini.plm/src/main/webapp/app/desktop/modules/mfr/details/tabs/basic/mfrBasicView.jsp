<style scoped>
    .item-details > div.row.master-att:last-child,
    .item-details > div.row.revision-att:last-child {
        border-bottom: 0 !important;
    }
</style>
<div ng-if="mfrBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5" >
        <span translate>LOADING_MFR_DETAILS</span>
    </span>
    <br/>
</div>
<div class="item-details" style="padding: 30px" ng-if="mfrBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MANUFACTURER_NAME</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('manufacturer','edit')}"
             title="{{hasPermission('manufacturer','edit') ? '' : noPermission}}">
            <span ng-if="external.external == false">
            <a href="#" ng-class="{'permission-text-disabled': !hasPermission('manufacturer','edit')}"
               onaftersave="mfrBasicVm.updateManufacture()"
               editable-text="mfrBasicVm.manufacturer.name">{{mfrBasicVm.manufacturer.name}}</a>
                </span>
            <span ng-if="external.external == true">
                <a ng-if="sharedPermission == 'WRITE' && external.external == true" href="#"
                   onaftersave="mfrBasicVm.updateManufacture()"
                   editable-text="mfrBasicVm.manufacturer.name">{{mfrBasicVm.manufacturer.name}}</a>

                 <div ng-if="sharedPermission == 'READ' && external.external == true">
                     <span ng-bind-html="mfrBasicVm.manufacturer.name"></span>
                 </div>

                <div ng-if="sharedPermission == null && external.external == true">
                    <span ng-bind-html="mfrBasicVm.manufacturer.name"></span>
                </div>
            </span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MANUFACTURER_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            {{mfrBasicVm.manufacturer.mfrType.name}}
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-5 col-sm-5" ng-class="{'cursor-override': !hasPermission('manufacturer','edit')}"
             title="{{hasPermission('manufacturer','edit') ? '' : noPermission}}">
            <span ng-if="external.external == false">
            <a href="#"
               ng-class="{'permission-text-disabled': !hasPermission('manufacturer','edit')}"
               onaftersave="mfrBasicVm.updateManufacture()"
               editable-textarea="mfrBasicVm.manufacturer.description">
                <span ng-bind-html="(mfrBasicVm.manufacturer.description ) ||'CLICK_TO_ADD_DESCRIPTION' | translate"
                      title="{{mfrBasicVm.manufacturer.description}}"></span>
            </a>
            </span>
            <span ng-if="external.external == true">
               <a href="#" e-style="width:250px" ng-if="sharedPermission == 'WRITE' && external.external == true"
                  onaftersave="mfrBasicVm.updateManufacture()"
                  editable-textarea="mfrBasicVm.manufacturer.description">
                <span ng-bind-html="(mfrBasicVm.manufacturer.description ) ||'CLICK_TO_ADD_DESCRIPTION' | translate"
                      title="{{mfrBasicVm.manufacturer.description}}"></span>
               </a>

               <div ng-if="sharedPermission == 'READ' && external.external == true">
                   <span ng-bind-html="mfrBasicVm.manufacturer.description"></span>
               </div>

                <div ng-if="sharedPermission == null && external.external == true">
                    <span ng-bind-html="mfrBasicVm.manufacturer.description"></span>
                </div>
            </span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PHONE_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('manufacturer','edit')}"
             title="{{hasPermission('manufacturer','edit') ? '' : noPermission}}">
            <span ng-if="external.external == false">
                 <a href="#" ng-class="{'permission-text-disabled': !hasPermission('manufacturer','edit')}"
                    onaftersave="mfrBasicVm.updateManufacture()"
                    editable-text="mfrBasicVm.manufacturer.phoneNumber" translate>
                     {{mfrBasicVm.manufacturer.phoneNumber || 'CLICK_TO_ADD_PHONE_NUMBER'}}</a>
            </span>
            <span ng-if="external.external == true">
                 <a href="#" ng-if="sharedPermission == 'WRITE'"
                    onaftersave="mfrBasicVm.updateManufacture()"
                    editable-text="mfrBasicVm.manufacturer.phoneNumber" translate>
                     {{mfrBasicVm.manufacturer.phoneNumber || 'CLICK_TO_ADD_PHONE_NUMBER'}}</a>
                  <span ng-if="sharedPermission == 'READ'">{{mfrBasicVm.manufacturer.phoneNumber}}</span>
            </span>
            <div ng-if="sharedPermission == 'READ' && external.external == true">
                <span ng-bind-html="mfrBasicVm.manufacturer.phoneNumber"></span>
            </div>

             <div ng-if="sharedPermission == null && external.external == true">
                 <span ng-bind-html="mfrBasicVm.manufacturer.phoneNumber"></span>
             </div>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CONTACT_PERSON</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('manufacturer','edit')}"
             title="{{hasPermission('manufacturer','edit') ? '' : noPermission}}">
            <span ng-if="external.external == false">
                <a href="#" ng-class="{'permission-text-disabled': !hasPermission('manufacturer','edit')}"
                   onaftersave="mfrBasicVm.updateManufacture()"
                   editable-text="mfrBasicVm.manufacturer.contactPerson" translate>
                    {{mfrBasicVm.manufacturer.contactPerson || 'CLICK_TO_ADD_CONTACT_PERSON'}}</a>

            </span>
            <span ng-if="external.external == true">
                <a href="#" ng-if="sharedPermission == 'WRITE'"
                   onaftersave="mfrBasicVm.updateManufacture()"
                   editable-text="mfrBasicVm.manufacturer.contactPerson" translate>
                    {{mfrBasicVm.manufacturer.contactPerson || 'CLICK_TO_ADD_CONTACT_PERSON'}}</a>
            <span ng-if="sharedPermission == 'READ'">{{mfrBasicVm.manufacturer.contactPerson}}</span>
            </span>
            <div ng-if="sharedPermission == 'READ' && external.external == true">
                <span ng-bind-html="mfrBasicVm.manufacturer.contactPerson"></span>
            </div>

             <div ng-if="sharedPermission == null && external.external == true">
                 <span ng-bind-html="mfrBasicVm.manufacturer.contactPerson"></span>
             </div>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>LIFE_CYCLE_PHASE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <item-status item="mfrBasicVm.manufacturer"></item-status>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>WORKFLOW_STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <workflow-status-settings workflow="mfrBasicVm.manufacturer"></workflow-status-settings>
            <!-- <span>{{mfrBasicVm.manufacturer.workflowStatus}}</span> -->
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{mfrBasicVm.manufacturer.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{mfrBasicVm.manufacturer.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{mfrBasicVm.manufacturer.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{mfrBasicVm.manufacturer.modifiedDate}}</span>
        </div>
    </div>

    <basic-attribute-details-view object-type="MANUFACTURER"
                                  quality-type="MANUFACTURER"
                                  has-permission="hasPermission('manufacturer','edit')"
                                  object-id="mfrBasicVm.mfrId"></basic-attribute-details-view>
    <object-attribute-details-view object-type="MANUFACTURERTYPE"
                                   has-permission="hasPermission('manufacturer','edit')"
                                   object-type-id="mfr.mfrType.id" show-attributes="true"
                                   object-id="mfrBasicVm.mfrId"></object-attribute-details-view>
</div>



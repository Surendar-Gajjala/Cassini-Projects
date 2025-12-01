<div id="mbom-basic-details" style="overflow: auto;">
    <div ng-if="mbomBasicVm.loading == true" style="padding: 30px;">
        <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading MBOM details...
    </span>
        <br/>
    </div>

    <div class="item-details" style="padding: 30px" ng-if="mbomBasicVm.loading == false">
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>ITEM_NAME</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" ng-click="mbomBasicVm.showItem()"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                    <span>{{mbomBasicVm.mbom.itemName}} - {{mbomBasicVm.mbom.revision}}</span>
                </a>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>NUMBER</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{mbomBasicVm.mbom.number}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>TYPE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{mbomBasicVm.mbom.type.name}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>NAME</span> :
            </div>
            <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mbom','edit')}"
                 title="{{hasPermission('mbom','edit') ? '' : noPermission}}">
                <a href="" e-style="width:250px"
                   ng-class="{'permission-text-disabled': !hasPermission('mbom','edit') || mbomRevision.released || mbomRevision.rejected}"
                   onaftersave="mbomBasicVm.updateMBOM()"
                   editable-text="mbomBasicVm.mbom.name">
                    <span ng-bind-html="mbomBasicVm.mbom.name"></span>
                </a>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>DESCRIPTION</span> :
            </div>
            <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mbom','edit')}"
                 title="{{hasPermission('mbom','edit') ? '' : noPermission}}">

                <a href="" ng-show="!mbomRevision.released"
                   ng-class="{'permission-text-disabled': !hasPermission('mbom','edit')}"
                   onaftersave="mbomBasicVm.updateMBOM()"
                   editable-textarea="mbomBasicVm.mbom.description">
                <span ng-bind-html="(mbomBasicVm.mbom.description) || 'CLICK_TO_ENTER_DESCRIPTION' | translate"
                      title="{{mbomBasicVm.mbom.description}}"></span>
                </a>

                <span ng-show="mbomRevision.released">
                    <span ng-bind-html="(mbomBasicVm.mbom.description)"
                          title="{{mbomBasicVm.mbom.description}}"></span>
                </span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>REVISION</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" ng-click="mbomBasicVm.showMBOMRevisionHistory()"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                    <span>{{mbomBasicVm.mbomRevision.revision}}</span>
                </a>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>LIFECYCLE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <item-status item="mbomBasicVm.mbomRevision"></item-status>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
            <span ng-if="mbomBasicVm.mbomRevision.released || (!mbomBasicVm.mbomRevision.released && !mbomBasicVm.mbomRevision.rejected)"
                  translate>RELEASED_DATE</span>
                <span ng-if="mbomBasicVm.mbomRevision.rejected" translate>REJECTED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                {{mbomBasicVm.mbomRevision.releasedDate}}
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>CREATED_BY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{mbomBasicVm.mbom.createdByObject.fullName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>CREATED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{mbomBasicVm.mbom.createdDate}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>MODIFIED_BY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{mbomBasicVm.mbom.modifiedByObject.fullName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>MODIFIED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{mbomBasicVm.mbom.modifiedDate}}</span>
            </div>
        </div>

        <object-attribute-details-view object-type="MESOBJECTTYPE" show-attributes="true"
                                       actual-object-type="{{mbom.objectType}}"
                                       ng-class="{'disabled': mbomRevision.released }"
                                       has-permission="true"
                                       object-type-id="mbom.type.id"
                                       object-id="mbom.id"></object-attribute-details-view>

    </div>
</div>

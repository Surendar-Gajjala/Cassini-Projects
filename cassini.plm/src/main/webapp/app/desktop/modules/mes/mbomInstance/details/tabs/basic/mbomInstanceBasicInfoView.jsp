<div id="mbomInstance-basic-details" style="overflow: auto;">
    <div ng-if="mbomInstanceBasicVm.loading == true" style="padding: 30px;">
        <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading MBOM instance details...
    </span>
        <br/>
    </div>

    <div class="item-details" style="padding: 30px" ng-if="mbomInstanceBasicVm.loading == false">
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>MBOM</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" ng-click="mbomInstanceBasicVm.showMBOM()"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                    <span>{{mbomInstanceBasicVm.mbomInstance.mbomNumber}} - {{mbomInstanceBasicVm.mbomInstance.mbomName}} - {{mbomInstanceBasicVm.mbomInstance.mbomRevisionName}}</span>
                </a>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>NUMBER</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{mbomInstanceBasicVm.mbomInstance.number}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>NAME</span> :
            </div>
            <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mbominstance','edit')}"
                 title="{{hasPermission('mbominstance','edit') ? '' : noPermission}}">
                <a href="" e-style="width:250px"
                   ng-class="{'permission-text-disabled': !hasPermission('mbominstance','edit') || mbomInstance.released || mbomInstance.rejected}"
                   onaftersave="mbomInstanceBasicVm.updateMBOMInstance()"
                   editable-text="mbomInstanceBasicVm.mbomInstance.name">
                    <span ng-bind-html="mbomInstanceBasicVm.mbomInstance.name || 'CLICK_TO_ENTER_NAME' | translate"></span>
                </a>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>DESCRIPTION</span> :
            </div>
            <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mbominstance','edit')}"
                 title="{{hasPermission('mbominstance','edit') ? '' : noPermission}}">

                <a href="" ng-show="!mbomInstance.released"
                   ng-class="{'permission-text-disabled': !hasPermission('mbominstance','edit')}"
                   onaftersave="mbomInstanceBasicVm.updateMBOMInstance()"
                   editable-textarea="mbomInstanceBasicVm.mbomInstance.description">
                <span ng-bind-html="(mbomInstanceBasicVm.mbomInstance.description) || 'CLICK_TO_ENTER_DESCRIPTION' | translate"
                      title="{{mbomInstanceBasicVm.mbomInstance.description}}"></span>
                </a>

                <span ng-show="mbomInstance.released">
                    <span ng-bind-html="(mbomInstanceBasicVm.mbomInstance.description)"
                          title="{{mbomInstanceBasicVm.mbomInstance.description}}"></span>
                </span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>SERIAL_NUMBER</span> :
            </div>
            <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mbominstance','edit')}"
                 title="{{hasPermission('mbominstance','edit') ? '' : noPermission}}">

                <a href="" ng-show="!mbomInstance.released"
                   ng-class="{'permission-text-disabled': !hasPermission('mbominstance','edit')}"
                   onaftersave="mbomInstanceBasicVm.updateMBOMInstance()"
                   editable-text="mbomInstanceBasicVm.mbomInstance.serialNumber">
                <span ng-bind-html="(mbomInstanceBasicVm.mbomInstance.serialNumber) || 'CLICK_TO_SERIAL_NUMBER' | translate"
                      title="{{mbomInstanceBasicVm.mbomInstance.serialNumber}}"></span>
                </a>

                <span ng-show="mbomInstance.released">
                    <span ng-bind-html="(mbomInstanceBasicVm.mbomInstance.serialNumber)"
                          title="{{mbomInstanceBasicVm.mbomInstance.serialNumber}}"></span>
                </span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>BATCH_NUMBER</span> :
            </div>
            <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mbominstance','edit')}"
                 title="{{hasPermission('mbominstance','edit') ? '' : noPermission}}">

                <a href="" ng-show="!mbomInstance.released"
                   ng-class="{'permission-text-disabled': !hasPermission('mbominstance','edit')}"
                   onaftersave="mbomInstanceBasicVm.updateMBOMInstance()"
                   editable-text="mbomInstanceBasicVm.mbomInstance.batchNumber">
                <span ng-bind-html="(mbomInstanceBasicVm.mbomInstance.batchNumber) || 'CLICK_TO_BATCH_NUMBER' | translate"
                      title="{{mbomInstanceBasicVm.mbomInstance.batchNumber}}"></span>
                </a>

                <span ng-show="mbomInstance.released">
                    <span ng-bind-html="(mbomInstanceBasicVm.mbomInstance.batchNumber)"
                          title="{{mbomInstanceBasicVm.mbomInstance.batchNumber}}"></span>
                </span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>STATUS</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                {{mbomInstanceBasicVm.mbomInstance.status}}
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>MANUFACTURING_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">

            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
            <span ng-if="mbomInstanceBasicVm.mbomInstance.released || (!mbomInstanceBasicVm.mbomInstance.released && !mbomInstanceBasicVm.mbomInstance.rejected)"
                  translate>RELEASED_DATE</span>
                <span ng-if="mbomInstanceBasicVm.mbomInstance.rejected" translate>REJECTED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                {{mbomInstanceBasicVm.mbomInstance.releasedDate}}
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>CREATED_BY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{mbomInstanceBasicVm.mbomInstance.createdByObject.fullName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>CREATED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{mbomInstanceBasicVm.mbomInstance.createdDate}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>MODIFIED_BY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{mbomInstanceBasicVm.mbomInstance.modifiedByObject.fullName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>MODIFIED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{mbomInstanceBasicVm.mbomInstance.modifiedDate}}</span>
            </div>
        </div>

        <%--<object-attribute-details-view object-type="MESOBJECTTYPE" show-attributes="true"
                                       actual-object-type="{{mbomInstance.objectType}}"
                                       ng-class="{'disabled': mbomInstance.released }"
                                       has-permission="true"
                                       object-type-id="mbomInstance.type.id"
                                       object-id="mbomInstance.id"></object-attribute-details-view>--%>

    </div>
</div>

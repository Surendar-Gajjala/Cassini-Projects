<style>
    .thumbnail-container {
        border: 1px solid #ddd;
        height: 380px;
        width: 380px;
        position: absolute;
        right: 30px;
        background-color: #fff;
        z-index: 10 !important;
    }

    .medium-image {
        width: 378px;
        max-height: 378px;
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
    }
</style>
<div ng-if="workOrderBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
       <span translate>LOADING_WORK_ORDER_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="workOrderBasicVm.loading == false">
    <div class="thumbnail-container">
        <div>
            <a ng-if="workOrderBasicVm.workOrder.assetObject.resourceObject.image != null && workOrderBasicVm.workOrder.assetObject.resourceObject.image != ''"
               href=""
               ng-click="workOrderBasicVm.showImage(workOrderBasicVm.workOrder.assetObject)"
               title="{{workOrderBasicVm.titleImage}}">
                <img class="medium-image"
                     ng-src="{{workOrderBasicVm.workOrder.assetObject.imagePath}}">
            </a>

            <div ng-if="workOrderBasicVm.workOrder.assetObject.resourceObject.image == null"
                 class="no-thumbnail">
                <span translate>NO_IMAGE</span>
            </div>
            <div id="item-thumbnail-basic{{workOrderBasicVm.workOrder.assetObject.id}}" class="item-thumbnail modal">
                <div class="item-thumbnail-content">
                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                        <div class="thumbnail-view"
                             id="thumbnail-view-basic{{workOrderBasicVm.workOrder.assetObject.id}}">
                            <div id="thumbnail-image-basic{{workOrderBasicVm.workOrder.assetObject.id}}"
                                 style="display: table-cell;vertical-align: middle;text-align: center;">
                                <img ng-src="{{workOrderBasicVm.workOrder.assetObject.imagePath}}"
                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{workOrderBasicVm.workOrder.assetObject.id}}"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{workOrderBasicVm.workOrder.number}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{workOrderBasicVm.workOrder.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('mroworkorder','edit')}"
             title="{{hasPermission('mroworkorder','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-if="workOrder.status != 'FINISH'"
               ng-class="{'permission-text-disabled': !hasPermission('mroworkorder','edit') || workOrder.status == 'FINISH'}"
               onaftersave="workOrderBasicVm.updateWorkOrder()"
               editable-text="workOrderBasicVm.workOrder.name">
                <span ng-bind-html="workOrderBasicVm.workOrder.name  "
                      title="{{workOrderBasicVm.workOrder.name}}"></span>
            </a>
            <span ng-if="workOrder.status == 'FINISH'">{{workOrderBasicVm.workOrder.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-5 col-sm-5" ng-class="{'cursor-override': !hasPermission('mroworkorder','edit')}"
             title="{{hasPermission('mroworkorder','edit') ? '' : noPermission}}">

            <a href="" ng-if="workOrder.status != 'FINISH'"
               ng-class="{'permission-text-disabled': !hasPermission('mroworkorder','edit') || workOrder.status == 'FINISH'}"
               onaftersave="workOrderBasicVm.updateWorkOrder()"
               editable-textarea="workOrderBasicVm.workOrder.description">
                <span ng-bind-html="(workOrderBasicVm.workOrder.description ) || 'CLICK_TO_ENTER_DESCRIPTION' | translate"
                      title="{{workOrderBasicVm.workOrder.description}}"></span>
            </a>
            <span ng-if="workOrder.status == 'FINISH'">{{workOrderBasicVm.workOrder.description}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ASSET</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <a ui-sref="app.mro.asset.details({assetId:workOrderBasicVm.workOrder.asset,tab:'details.basic'})">
                <span ng-bind-html="workOrderBasicVm.workOrder.assetObject.name"></span>
            </a>
        </div>
    </div>
    <div class="row" ng-if="workOrderBasicVm.workOrder.type.type == 'REPAIR'">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>WORK_REQUEST</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a ui-sref="app.mro.workRequest.details({workRequestId:workOrderBasicVm.workOrder.request,tab:'details.basic'})">
                <span ng-bind-html="workOrderBasicVm.workOrder.requestObject.number"></span>
            </a>
        </div>
    </div>
    <div class="row" ng-if="workOrderBasicVm.workOrder.type.type == 'MAINTENANCE'">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MAINTENANCE_PLAN</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a ui-sref="app.mro.maintenancePlan.details({maintenancePlanId:workOrderBasicVm.workOrder.plan,tab:'details.basic'})">
                <span ng-bind-html="workOrderBasicVm.workOrder.planObject.number"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ASSIGNED_TO</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span ng-bind-html="workOrderBasicVm.workOrder.assignedToObject.fullName"></span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PRIORITY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <priority object="workOrderBasicVm.workOrder"></priority>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <wo-status object="workOrderBasicVm.workOrder"></wo-status>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NOTES</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mroworkorder','edit')}"
             title="{{hasPermission('mroworkorder','edit') ? '' : noPermission}}">

            <a href="" ng-if="workOrder.status != 'FINISH'"
               ng-class="{'permission-text-disabled': !hasPermission('mroworkorder','edit') || workOrder.status == 'FINISH'}"
               onaftersave="workOrderBasicVm.updateWorkOrder()"
               editable-textarea="workOrderBasicVm.workOrder.notes"><span ng-bind-html="(workOrderBasicVm.workOrder.notes ) || 'CLICK_TO_ENTER_NOTES' |
                translate" title="{{workOrderBasicVm.workOrder.notes}}"></span>
            </a>
            <%--<span ng-if="workOrder.status == 'FINISH'">{{workOrderBasicVm.workOrder.notes}}</span>--%>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{workOrderBasicVm.workOrder.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{workOrderBasicVm.workOrder.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{workOrderBasicVm.workOrder.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{workOrderBasicVm.workOrder.modifiedDate}}</span>
        </div>
    </div>
    <object-attribute-details-view object-type="MROOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{workOrder.objectType}}"
                                   has-permission="workOrder.status != 'FINISH'"
                                   object-type-id="workOrder.type.id"
                                   object-id="workOrder.id"></object-attribute-details-view>
</div>

<style>

    /* The Close Button */
    .img-model .closeimage {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeimage:hover,
    .img-model .closeimage:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .browse-control {
        -moz-border-radius: 3px;
        -webkit-border-radius: 3px;
        border-radius: 3px;
        padding: 5px;
        height: auto;
        -moz-box-shadow: none;
        -webkit-box-shadow: none;
        box-shadow: none;
        font-size: 13px;
        border: 1px solid #ccc;
    }
</style>

<div ng-if="productionOrderBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading productionOrders details...
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="productionOrderBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{productionOrderBasicVm.productionOrder.number}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{productionOrderBasicVm.productionOrder.type.name}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5"
             ng-class="{'cursor-override': !hasPermission('productionorder','edit') || productionOrderBasicVm.productionOrder.approved}"
             title="{{hasPermission('productionorder','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px"
               ng-class="{'permission-text-disabled': !hasPermission('productionorder','edit') || productionOrderBasicVm.productionOrder.approved}"
               onaftersave="productionOrderBasicVm.updateProductionOrder()"
               editable-text="productionOrderBasicVm.productionOrder.name">
                <span ng-bind-html="productionOrderBasicVm.productionOrder.name"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">

            <a ng-if="hasPermission('productionorder','edit') && !productionOrderBasicVm.productionOrder.approved && !productionOrderBasicVm.productionOrder.rejected"
               href=""
               onaftersave="productionOrderBasicVm.updateProductionOrder()"
               editable-textarea="productionOrderBasicVm.productionOrder.description"><span ng-bind-html="(productionOrderBasicVm.productionOrder.description ) || 'ADD_DESCRIPTION' |
                translate" title="{{productionOrderBasicVm.productionOrder.description}}"></span>
            </a>
            <span ng-if="(!hasPermission('productionorder','edit')) || productionOrderBasicVm.productionOrder.approved || productionOrderBasicVm.productionOrder.rejected">{{productionOrderBasicVm.productionOrder.description}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PLANT</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="" e-style="width:250px" onaftersave="productionOrderBasicVm.updateProductionOrder()"
               ng-if="hasPermission('productionorder','edit') && !productionOrderBasicVm.productionOrder.approved && !productionOrderBasicVm.productionOrder.rejected"
               editable-select="productionOrderBasicVm.productionOrder.plant"
               e-ng-options="plant.id as plant.name for plant in productionOrderBasicVm.plants track by plant.id">
                {{productionOrderBasicVm.productionOrder.plantName}}
            </a>
            <span ng-if="(!hasPermission('productionorder','edit')) || productionOrderBasicVm.productionOrder.approved || productionOrderBasicVm.productionOrder.rejected">{{productionOrderBasicVm.productionOrder.plantName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>Shift</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="" ng-click="productionOrderBasicVm.showShift()"
               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                <span>{{productionOrderBasicVm.productionOrder.shiftName}}</span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PLANNED_START_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="" e-style="width:250px" style="font-size: 17px"
               ng-if="!productionOrderBasicVm.editStartDate && hasPermission('productionorder','edit') && !productionOrderBasicVm.productionOrder.approved && !productionOrderBasicVm.productionOrder.rejected"
               ng-click="productionOrderBasicVm.changeStartDate()"
               title="{{'UPDATE_PLANNED_START_DATE' | translate}}">
                <span>{{productionOrderBasicVm.productionOrder.plannedStartDate || 'UPDATE_PLANNED_START_DATE' | translate}}</span>

            </a>

            <div ng-if="productionOrderBasicVm.editStartDate == true" style="display: inline-flex" class="btn-group">
                <div style="width: 200px;">
                    <input type="text" class="form-control" date-picker-edit
                           ng-model="productionOrderBasicVm.productionOrder.plannedStartDate"
                           name="attDate" placeholder="Select date">
                </div>
                <div class="btn-group" style="margin-left: 5px;">
                    <button class="btn btn-sm btn-primary"
                            type="button" title="{{'SUBMIT' | translate}}"
                            ng-click="productionOrderBasicVm.updateProductionOrder()"><i class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-sm btn-default"
                            type="button" title="{{'CANCEL' | translate}}"
                            ng-click="productionOrderBasicVm.changeStartDate()"><i class="fa fa-times"></i>
                    </button>
                </div>
            </div>
            <span ng-if="!hasPermission('productionorder','edit') || productionOrderBasicVm.productionOrder.approved || productionOrderBasicVm.productionOrder.rejected">
                {{productionOrderBasicVm.productionOrder.plannedStartDate}}
            </span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PLANNED_FINISH_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="" e-style="width:250px" style="font-size: 17px"
               ng-if="!productionOrderBasicVm.editFinishDate && hasPermission('productionorder','edit') && !productionOrderBasicVm.productionOrder.approved && !productionOrderBasicVm.productionOrder.rejected"
               ng-click="productionOrderBasicVm.changeFinishDate()"
               title="{{'UPDATE_PLANNED_FINISH_DATE' | translate}}">
                <span>{{productionOrderBasicVm.productionOrder.plannedFinishDate || 'UPDATE_PLANNED_FINISH_DATE' | translate}}</span>
            </a>

            <div ng-if="productionOrderBasicVm.editFinishDate == true" style="display: inline-flex"
                 class="btn-group">
                <div style="width: 200px;">
                    <input type="text" class="form-control" date-picker-edit
                           ng-model="productionOrderBasicVm.productionOrder.plannedFinishDate"
                           name="attDate" placeholder="Select date">
                </div>
                <div class="btn-group" style="margin-left: 5px;">
                    <button class="btn btn-sm btn-primary"
                            type="button" title="{{'SUBMIT' | translate}}"
                            ng-click="productionOrderBasicVm.updateProductionOrder()"><i class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-sm btn-default"
                            type="button" title="{{'CANCEL' | translate}}"
                            ng-click="productionOrderBasicVm.changeFinishDate()"><i class="fa fa-times"></i>
                    </button>
                </div>
            </div>
            <span ng-if="!hasPermission('productionorder','edit') || productionOrderBasicVm.productionOrder.approved || productionOrderBasicVm.productionOrder.rejected">
                {{productionOrderBasicVm.productionOrder.plannedFinishDate}}
            </span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>ACTUAL_START_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{productionOrderBasicVm.productionOrder.actualStartDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>ACTUAL_FINISH_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{productionOrderBasicVm.productionOrder.actualFinishDate}}</span>
        </div>
    </div>
    <div class="row" ng-if="productionOrderBasicVm.productionOrder.approved">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" ng-if="productionOrderBasicVm.productionOrder.approved" translate>APPROVED_DATE</span>
            <span style="font-size: 14px" ng-if="!productionOrderBasicVm.productionOrder.approved" translate>REJECTED_DATE</span>
            :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{productionOrderBasicVm.productionOrder.approvedDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <item-status item="productionOrderBasicVm.productionOrder"></item-status>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{productionOrderBasicVm.productionOrder.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{productionOrderBasicVm.productionOrder.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{productionOrderBasicVm.productionOrder.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{productionOrderBasicVm.productionOrder.modifiedDate}}</span>
        </div>
    </div>

    <%--<basic-attribute-details-view object-type="CHANGE"
                                  quality-type="CHANGE"
                                  has-permission="hasPermission('change','dco','edit') && !dco.isReleased && dco.statusType != 'REJECTED'"
                                  object-id="dco.id"></basic-attribute-details-view>--%>

</div>

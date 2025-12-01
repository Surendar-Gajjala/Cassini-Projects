<div>
    <style scoped>
        .ui-select-bootstrap .ui-select-match-text span {
            vertical-align: bottom;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }

        .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
            position: absolute !important;
        }
    </style>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 150px;" translate>NUMBER</th>
                <th class="col-width-150" translate>TYPE</th>
                <th class="col-width-200" translate>WORK_ORDER_NAME</th>
                <th class="col-width-250" translate>DESCRIPTION</th>
                <th style="width: 150px;" translate>STATUS</th>
                <th style="width: 150px;" translate>REQUEST_PLAN</th>
                <th style="width: 150px;" translate>DATE_PERFORMED</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="assetMaintenanceVm.loading == true">
                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_MAINTENANCE</span>
                        </span>
                </td>
            </tr>
            <tr ng-if="assetMaintenanceVm.loading == false && assetMaintenanceVm.assetWorkOrders.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Maintenance.png" alt="" class="image">
                        <div class="message">{{ 'NO_MAINTENANCE' | translate}} </div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="workOrder in assetMaintenanceVm.assetWorkOrders track by $index">
                <td>
                    <a title="{{clickToShowDetails}}" ng-click="assetMaintenanceVm.showWorkOrder(workOrder)">
                        {{workOrder.number}}
                    </a>
                </td>
                <td class="col-width-150">{{workOrder.typeName}}</td>
                <td class="col-width-200">{{workOrder.name}}</td>
                <td class="col-width-250">{{workOrder.description}}</td>
                <td>
                    <wo-status object="workOrder"></wo-status>
                </td>
                <td>
                    <a href="" ng-click="assetMaintenanceVm.showMaintenancePlan(workOrder)"
                       ng-if="workOrder.typeName =='Maintenance'">{{workOrder.planNumber}}</a>
                    <a href="" ng-click="assetMaintenanceVm.showWorkRequest(workOrder)"
                       ng-if="workOrder.typeName =='Repair'">{{workOrder.requestNumber}}</a>

                </td>
                <td>{{workOrder.modifiedDate}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
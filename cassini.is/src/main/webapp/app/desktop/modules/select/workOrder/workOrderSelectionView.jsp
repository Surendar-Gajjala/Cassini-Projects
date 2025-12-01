<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="workOrderSelectVm.resetPage"
                          on-search="workOrderSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{workOrderSelectVm.workOrders.numberOfElements}} of
                                            {{workOrderSelectVm.workOrders.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{workOrderSelectVm.workOrders.totalElements != 0 ? workOrderSelectVm.workOrders.number+1:0}} of {{workOrderSelectVm.workOrders.totalPages}}</span>
                <a href="" ng-click="workOrderSelectVm.previousPage()"
                   ng-class="{'disabled': workOrderSelectVm.workOrders.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="workOrderSelectVm.nextPage()"
                   ng-class="{'disabled': workOrderSelectVm.workOrders.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">Select</th>
                <th style="width: 150px;">Work Order Number</th>
                <th style="width: 150px;">Contractor</th>
                <th style="width: 150px;">Status</th>
                <th style="width: 150px;">Project</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="workOrderSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading workOrders</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="workOrderSelectVm.loading == false && workOrderSelectVm.workOrders.content.length == 0">
                <td colspan="12">
                    <span translate>No workOrders</span>
                </td>
            </tr>

            <tr ng-repeat="workOrder in workOrderSelectVm.workOrders.content | filter: search"
                ng-click="workOrder.isChecked = !workOrder.isChecked; workOrderSelectVm.radioChange(workOrder, $event)"
                ng-dblClick="workOrder.isChecked = !workOrder.isChecked; workOrderSelectVm.selectRadioChange(workOrder, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="workOrder.isChecked" name="workOrder" value="workOrder"
                           ng-dblClick="workOrderSelectVm.selectRadioChange(workOrder, $event)"
                           ng-click="workOrderSelectVm.radioChange(workOrder, $event)">
                </td>
                <td style="width: 150px;"><span ng-bind-html="workOrder.number | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 150px;"><span
                        ng-bind-html="workOrder.contractorObject.name | highlightText: freeTextQuery"></span></td>
                <td style="width: 150px;"><span class="label" style="color: white"
                                                ng-class="{'label-warning': workOrder.status == 'PENDING','label-primary': workOrder.status == 'FINISHED'}">
                            {{workOrder.status}}  </span>
                </td>
                <td style="width: 150px;"><span title="{{workOrder.projectObject.name}}"> {{workOrder.projectObject.name | limitTo: 20}}{{workOrder.projectObject.name.length > 20 ? '...' : ''}}</span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
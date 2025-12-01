<style scoped>

    .sticky-col {
        position: sticky !important;
        position: -webkit-sticky !important;
    }

    .sticky-actions-col {
        right: -10px !important;
    }

    .table-striped > tbody > tr:nth-child(2n) > td.actions-col {
        /*background-color: #fff;*/
    }

    .table-striped > tbody > tr:nth-child(2n):hover > td.sticky-col {
        background-color: #d6e1e0;
    }

    .ml-12 {
        margin-left: 12px;
    }

    .ml-3 {
        margin-left: 3px;
    }
</style>
<div class="responsive-table">
    <table class='table' style="margin: 0;">
        <thead>
        <tr>
            <th class="col-width-150" translate>RESOURCE_TYPE</th>
            <th class="col-width-150" translate>TYPE</th>
            <th class="col-width-100" translate>ALLOCATED_QTY</th>
            <th class="col-width-100" translate>NUMBER</th>
            <th class="col-width-200" translate>NAME</th>
            <th class="col-width-250" translate>DESCRIPTION</th>
            <th class="col-width-250" translate>NOTES</th>
            <th class="actions-col sticky-col sticky-actions-col">
                <span translate>ACTIONS</span>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="mbomInstanceOperationResourcesVm.loading == true">
            <td colspan="14">
                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                     class="mr5"><span translate>LOADING_RESOURCES</span>
            </td>
        </tr>
        <tr ng-if="mbomInstanceOperationResourcesVm.loading == false && mbomInstanceOperationResourcesVm.operationResources.length == 0">
            <td colspan="12">
                {{ 'NO_RESOURCES' | translate}}
            </td>
        </tr>
        <tr ng-if="mbomInstanceOperationResourcesVm.operationResources.length > 0"
            ng-repeat-start="bopPlanResource in mbomInstanceOperationResourcesVm.operationResources">
            <td class="col-width-150"
                rowspan="{{bopPlanResource.count + 1}}">
                {{bopPlanResource.resource}}
            </td>
        </tr>
        <tr ng-if="mbomInstanceOperationResourcesVm.loading == false"
            ng-repeat-start="planResource in bopPlanResource.resourceTypes"
            ng-init="resource = planResource.resources[0]">
            <td class="col-width-150"
                rowspan="{{planResource.resources.length > 0 ? planResource.resources.length : 1}}">
                {{planResource.resourceType}}
            </td>
            <td class="col-width-150"
                rowspan="{{planResource.resources.length > 0 ? planResource.resources.length : 1}}">
                {{planResource.quantity}}
            </td>
            <td class="col-width-100">
                {{resource.number}}
            </td>
            <td class="col-width-200">
                {{resource.name}}
            </td>
            <td class="col-width-250">
                {{resource.description}}
            </td>
            <td class="col-width-250">
                <span ng-if="resource.editMode == false || resource.editMode == undefined">{{resource.notes}}</span>
                <input type="text" class="form-control" ng-if="resource.editMode" ng-model="resource.notes"
                       placeholder="{{'ENTER_NOTES' | translate}}"
                       ng-enter="mbomInstanceOperationResourcesVm.updateResource(resource)"
                       style="width: 99%;text-align: left"/>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
            </td>
        </tr>
        <tr ng-if="mbomInstanceOperationResourcesVm.loading == false"
            ng-repeat-end ng-repeat-start="resource in planResource.resources.slice(1)">
            <td class="col-width-200" style="border-left: 1px solid #eee">
                {{resource.number}}
            </td>
            <td class="col-width-200">
                {{resource.name}}
            </td>
            <td class="col-width-250">
                {{resource.description}}
            </td>
            <td class="col-width-250">
                <span ng-if="resource.editMode == false || resource.editMode == undefined">{{resource.notes}}</span>
                <input type="text" class="form-control" ng-if="resource.editMode" ng-model="resource.notes"
                       placeholder="{{'ENTER_NOTES' | translate}}"
                       ng-enter="mbomInstanceOperationResourcesVm.updateResource(resource)"
                       style="width: 99%;text-align: left"/>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
            </td>
        </tr>
        <tr ng-repeat-end ng-hide="true"></tr>
        <tr ng-repeat-end ng-hide="true"></tr>
        </tbody>
    </table>
</div>
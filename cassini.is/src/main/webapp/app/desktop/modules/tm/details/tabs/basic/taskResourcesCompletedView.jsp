<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }
</style>
<div style="padding: 0px 20px;">
    <div class="row">
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Resource</th>
                    <th>Resource Type</th>
                    <th>Quantity</th>
                </tr>
                </thead>
                <tbody>
                <%--<tr ng-if="taskResourcesVm.machines.content.length == 0 && !machineDialogueVm.loading ">
                    <td colspan="11" style="padding-left: 30px;">No Machines</td>
                </tr>--%>
                <tr ng-repeat="resource in taskResourceCompleteVm.taskCompletionResources">
                    <td>
                    <span ng-if="resource.resourceType == 'MANPOWER'">
                        {{resource.resourceIdObject.fullName}}
                    </span>
                    <span ng-if="resource.resourceType == 'MATERIAL'">
                        {{resource.resourceIdObject.itemNumber}}
                    </span>
                    <span ng-if="resource.resourceType == 'MACHINE'">
                        {{resource.resourceIdObject.itemNumber}}
                    </span>
                    <span ng-if="resource.resourceType == 'ROLE'">
                        {{resource.resourceIdObject.role}}
                    </span>
                    </td>
                    <td>
                    <span ng-if="resource.resourceType == 'MANPOWER'">
                        {{resource.resourceType}}
                    </span>
                    <span ng-if="resource.resourceType == 'MATERIAL'">
                        {{resource.resourceType}}
                    </span>
                    <span ng-if="resource.resourceType == 'MACHINE'">
                        {{resource.resourceType}}
                    </span>
                    <span ng-if="resource.resourceType == 'ROLE'">
                        {{resource.resourceType}}
                    </span>
                    </td>
                    <td>
                        <span ng-if="resource.resourceType == 'MANPOWER'">
                        {{resource.quantity}}
                    </span>
                    <span ng-if="resource.resourceType == 'MATERIAL'">
                        {{resource.quantity}}
                    </span>
                    <span ng-if="resource.resourceType == 'MACHINE'">
                        {{resource.quantity}}
                    </span>
                    <span ng-if="resource.resourceType == 'ROLE'">
                        {{resource.quantity}}
                    </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>

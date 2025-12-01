<div class="responsive-table">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="width: 50px;"></th>
            <th style="width: 200px;">Task</th>
            <th style="width: 200px;">Name</th>
            <th style="width: 200px;">Quantity</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="siteResourcesVm.resources.length == 0">
            <td colspan="16">No Resources are available to view</td>
        </tr>
        <tr ng-repeat="resource in siteResourcesVm.resources">
            <td>
                <span ng-if="resource.resourceType == 'MATERIALTYPE' " style="width: 50px; text-align: center"><img
                        ng-src="app/assets/images/cart.png" style=" height:16px; width:16px;"></span>
                <span ng-if="resource.resourceType == 'MACHINETYPE' " style="width: 50px; text-align: center"><img
                        ng-src="app/assets/images/machine2.png" style=" height:16px; width:16px;"></span>
                <span ng-if="resource.resourceType == 'MANPOWERTYPE' " style="width: 50px; text-align: center"><img
                        ng-src="app/assets/images/group1.png" style=" height:16px; width:16px;">
                </span>
                <span ng-if="resource.resourceType == 'ROLE'" style="width: 50px; text-align: center"><img
                        ng-src="app/assets/images/group1.png" style=" height:16px; width:16px;">
                </span>
            </td>
            <td style="width: 200px;">{{resource.taskObject.name}}</td>

            <td style="width: 200px;">
                <span style="width: 200px;"
                      ng-if="resource.resourceType == 'MATERIALTYPE' || resource.resourceType == 'MACHINETYPE'">
                  {{ resource.referenceIdObject.itemName}}
                </span>
                 <span style="width: 200px;"
                       ng-if="resource.resourceType == 'ROLE'">
                  {{ resource.referenceIdObject.role}}
                </span>
                <span style="width: 200px;" ng-if="resource.resourceType == 'MANPOWERTYPE'">
                  {{resource.referenceIdObject.fullName}}
                </span>
            </td>
            <td style="width: 200px;">
                {{resource.quantity}}
            </td>
        </tr>
        </tbody>
    </table>
</div>
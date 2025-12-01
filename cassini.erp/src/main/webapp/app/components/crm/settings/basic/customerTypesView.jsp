<div>
    <div class="row">
        <div class="col-md-6"><h3>Employee Type</h3></div>

        <div class="col-md-6 text-right mrtop">
            <button class="btn btn-sm btn-primary" ng-click="addCustomerType()">New Type</button>
        </div>
    </div>

    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 100px; text-align: center">Actions</th>
            <th>Name</th>
            <th>Description</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-show="customerTypes.length == 0">
            <td colspan="3">
                <span ng-hide="loading">No customer types</span>
                <span ng-show="loading">
                    <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading customer types...
                </span>
            </td>
        </tr>

        <tr ng-repeat="customerType in customerTypes">
            <td style="width:100px; text-align: center; vertical-align: middle;">
                <div class="btn-group" dropdown style="margin-bottom: 0px;" ng-hide="customerType.editMode">
                    <button type="button" class="btn btn-sm btn-default dropdown-toggle" dropdown-toggle>
                        <i class="fa fa-cog fa-fw"></i>
                    </button>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="" ng-click="showEditMode(customerType)">Edit</a></li>
                    </ul>
                </div>


                <div class="btn-group" style="margin-bottom: 0px;" ng-show="customerType.editMode">
                    <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(customerType)"><i class="fa fa-check"></i></button>
                    <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(customerType);"><i class="fa fa-times"></i></button>
                </div>
            </td>

            <td style="vertical-align: middle;">
                <span ng-show="customerType.showValues">{{ customerType.name }}</span>
                <input placeholder="Enter name" class="form-control" type="text"
                       ng-show="customerType.editMode" ng-model="customerType.name">
            </td>

            <td style="vertical-align: middle;">
                <span ng-show="customerType.showValues">{{ customerType.description }}</span>
                <input placeholder="Enter description" class="form-control" type="text"
                       ng-show="customerType.editMode" ng-model="customerType.description">
            </td>
        </tr>
        </tbody>
    </table>
</div>
<style>
    th {
        vertical-align: middle;
        text-align: center;
        border: none !important;
    }

    td {
        text-align: center;
        border: none !important;
    }

    .table {
        border: none !important;
    }

    .pane-visible {
        visibility: visible;
    }

    .pane-hidden {
        visibility: hidden;
    }

    table.highlight-row tr.dropzone-row:hover, table.highlight-row tr.dropzone-row:hover td {
        background-color: inherit !important;
        background-image: none !important;
        color: rgb(99, 110, 123) !important;
    }

    .responsive-table {
        width: 100%;
        margin-bottom: 0;
        padding-bottom: 20px;
        overflow-x: inherit;
        overflow-y: inherit;
        -webkit-overflow-scrolling: touch;
        -ms-overflow-style: -ms-autohiding-scrollbar;
    }

    .lockedProject {
        background: #ff6666 !important;
        color: black;
    }

    .dropdown-menu li:hover {
        background-color: #dddddd;
    }

    .dropdown:hover .dropdown-menu {
        display: block;
    }

    .dropdown-content {
        position: fixed !important;
    }

    .responsive-table .dropdown-content {
        margin-left: 10px !important;
    }

    th.actions-column, td.actions-column {
        width: 150px;
        text-align: center;
    }

    .responsive-table .dropdown:hover .dropdown-content {
        display: block;
        color: black !important;
    }

    .dropdown {
        /* position: relative;
         display: inline-block;*/
        text-align: center !important;
        margin-left: -2px !important;
        cursor: pointer;
    }

    .dropbtn {
        background-color: #4CAF50;
        color: black;
        padding: 16px;
        font-size: 16px;
        border: none;
    }

    .responsive-table .dropdown-content {
        display: none;
        position: absolute;
        background-color: #fff !important;
        min-width: 100px;
        box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
        z-index: 1 !important;
        border-radius: 5px;
        text-align: left;
        margin-left: -50px;
        color: black !important;
    }

    .responsive-table .dropdown-content a {
        text-decoration: none;
        display: block;
        color: black !important;
    }

    .responsive-table .dropdown-content a:hover {
        background-color: #0f3ff3;
        color: white !important;

    }

    .responsive-table .dropdown-content i:hover {
        background-color: #0f3ff3;
        color: white !important;

    }

    table.highlight-row tbody tr:hover td a {
        color: black !important;
    }

    [comments-button] {
        padding: 0px !important;
    }
</style>
<div class="responsive-table">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="width: 350px;">Name</th>
            <th style="width: 300px;">Phone</th>
            <th style="width: 350px;">Email</th>
            <th style="width: 200px;">Task Qty</th>
            <th style="text-align: center">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="manpowerTaskVm.loading == true">
            <td colspan="12"><img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Manpower ..</td>
        </tr>
        <tr ng-if="manpowerTaskVm.projectResources.length == 0 && manpowerTaskVm.loading == false">
            <td colspan="12">No Manpower is available to view</td>
        </tr>
        <tr ng-if="manpowerTaskVm.loading == false" ng-repeat="resource in manpowerTaskVm.projectResources">
            <td style="width: 350px;">
                <span ng-if="resource.resourceType == 'MANPOWERTYPE'">{{resource.referenceIdObject.fullName}}</span>
                <span ng-if="resource.resourceType == 'ROLE'">{{resource.referenceIdObject.role}}</span>
            </td>
            <td style="width:300px">{{resource.referenceIdObject.phoneMobile}}</td>
            <td style="width:350px">{{resource.referenceIdObject.email}}</td>
            <td style="width: 200px">
                <span ng-if="resource.resourceType == 'ROLE' && resource.editMode == true">
                    <input type="number" class="form-control" ng-model="resource.quantity"/>
                </span>
                <span ng-if="resource.resourceType == 'ROLE' && resource.editMode == false">
                     <a style="color:#428bca;"
                        href="#"
                        editable-number="resource.quantity"
                        onaftersave="manpowerTaskVm.saveRole(resource)">
                         {{resource.quantity}}</a>
                </span>
            </td>
            <td class="text-center">
                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                        <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                            style="z-index: 9999 !important;">

                            <li ng-click='manpowerTaskVm.showComments(resource)'>
                                <a class="dropdown-item" title="Show Comments" style="text-align: left">
                                    <span style="padding-left: 3px;">Comments</span>
                                </a></li>
                            <li ng-click='manpowerTaskVm.saveRole(resource)'
                                ng-if="resource.resourceType == 'ROLE' && resource.editMode == true">
                                <a class="dropdown-item" title="Save Role"
                                   style="text-align: left;cursor: pointer">
                                    <span style="padding-left: 3px;">Save Role</span>
                                </a></li>
                            <li ng-click='manpowerTaskVm.cancelChanges(resource)'
                                ng-if="resource.resourceType == 'ROLE' && resource.editMode == true">
                                <a class="dropdown-item" title="Cancel"
                                   style="text-align: left;cursor: pointer">
                                    <span style="padding-left: 3px;">Cancel</span>
                                </a></li>
                            <li ng-click='manpowerTaskVm.editRole(resource)'
                                ng-if="resource.resourceType == 'ROLE' && resource.editMode == false">
                                <a class="dropdown-item" title="Edit Role"
                                   style="text-align: left;cursor: pointer">
                                    <span style="padding-left: 3px;">Edit Role</span>
                                </a></li>
                            <li ng-click="manpowerTaskVm.deletePersonResource(resource)">
                                <a title="Remove" href=""
                                   style="text-align: left"
                                   type="button"
                                   class="dropdown-item"
                                   ng-disabled="selectedProject.locked == true || hasPermission('permission.tasks.deleteItem') == false || task.status == 'FINISHED'">
                                    <span style="padding-left: 3px;">Remove</span>
                                </a>
                            </li>
                        </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>

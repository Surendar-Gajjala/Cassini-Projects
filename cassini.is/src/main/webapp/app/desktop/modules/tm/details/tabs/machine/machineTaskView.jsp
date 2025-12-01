<style>
    th {
        vertical-align: middle;
        text-align: center;
    }

    td {
        text-align: center;
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

</style>
<div class="responsive-table">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="vertical-align: middle;">Machine Number</th>
            <th style="vertical-align: middle;">Machine Name</th>
            <th style="vertical-align: middle;">Units</th>
            <th style="text-align: center">BOQ Qty</th>
            <th style="text-align: center">Assigned Qty</th>
            <th style="text-align: center">Balance Qty</th>
            <th style="text-align: center">Task Qty</th>
            <th style="text-align: center; min-width: 150px;">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="machineTaskVm.loading == true">
            <td colspan="12"><img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Machines ..</td>
        </tr>
        <tr ng-if="machineTaskVm.resourceMachines.length == 0 && machineTaskVm.loading == false">
            <td colspan="12">No Machines are available to view</td>
        </tr>
        <tr ng-if="machineTaskVm.loading == false" ng-repeat="machine in machineTaskVm.resourceMachines">
            <td style="vertical-align: middle;">{{machine.referenceIdObject.itemNumber}}</td>
            <td style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{machine.referenceIdObject.itemName}}">{{machine.referenceIdObject.itemName}}
            </td>
            <td style="vertical-align: middle;">{{machine.referenceIdObject.units}}</td>
            <td style="text-align:center;">{{machine.referenceIdObject.quantity}}</td>
            <td style="text-align:center;">{{machine.assignedQty}}</td>
            <td style="text-align:center;">{{machine.balanceQty}}</td>
            <td style="text-align:center;" ng-if="task.status != 'FINISHED'">
                <div>
                    <a href="#"
                       editable-text="machine.quantity"
                       onbeforesave="machineTaskVm.updateMachines($data, machine)">{{machine.quantity}}</a>
                </div>
            </td>
            <td style="text-align:center;" ng-if="task.status == 'FINISHED'">
                {{machine.quantity}}
            </td>

            <td class="text-center">
                                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                        style="z-index: 9999 !important;">

                                        <li ng-click="machineTaskVm.deleteMachineResource(machine)">
                                            <a title="Remove" href=""
                                               style="text-align: left;"
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

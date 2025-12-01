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
</style>
<div class="responsive-table">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th>Material Number</th>
            <th>Material Name</th>
            <th>Units</th>
            <th>BOQ Qty</th>
            <th>Assigned BOQ Qty</th>
            <th>Balance BOQ Qty</th>
            <th>Task Qty</th>
            <%--<th>Issued Qty</th>--%>
            <%--<th>Balanced Task Qty</th>--%>
            <th style="min-width: 150px;">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="materialTaskVm.loading == true">
            <td colspan="12"><img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Materials..</td>
        </tr>
        <tr ng-if="materialTaskVm.resourceMaterials.length == 0 && materialTaskVm.loading == false">
            <td colspan="12">No Materials are available to view</td>
        </tr>
        <tr ng-if="materialTaskVm.loading == false" ng-repeat="material in materialTaskVm.resourceMaterials">
            <td style="vertical-align: middle;">{{material.referenceIdObject.itemNumber}}</td>
            <td style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{material.referenceIdObject.itemName}}">{{material.referenceIdObject.itemName}}
            </td>
            <td style="vertical-align: middle;">{{material.referenceIdObject.units}}</td>
            <td style="text-align: center">{{material.referenceIdObject.quantity}}</td>
            <td style="text-align: center">{{material.assignedQty}}</td>
            <td style="text-align: center">{{material.referenceIdObject.quantity - material.assignedQty}}
            </td>
            <td style="text-align: center" ng-if="task.status != 'FINISHED'">
                <div>
                    <a href="#"
                       editable-text="material.quantity" ng-disabled="hasPermission('permission.tasks.edit')"
                       onbeforesave="materialTaskVm.updateMaterials($data,material)">{{material.quantity}}</a>
                </div>
            </td>
            <td style="text-align: center" ng-if="task.status == 'FINISHED'">
                {{material.quantity}}
            </td>
            <%--<td style="text-align: center">{{material.issuedQuantity}}</td>--%>
            <%--<td style="text-align: center">{{material.quantity - material.issuedQuantity}}</td>--%>

            <td class="text-center">
                <span class="row-menu" uib-dropdown dropdown-append-to-body style="width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click="materialTaskVm.deleteMaterialResource(material)">
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
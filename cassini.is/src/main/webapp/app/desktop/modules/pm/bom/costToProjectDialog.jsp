<style>
    table, th, td {
        border: 1px solid black;
        border-collapse: collapse;
    }

    td {
        padding: 0px;
    }
</style>
<div class="modal-header">
    <h3 style="text-align: center">Cost To Project</h3>
    <h5 style="color: red;">{{costToProjectDialogVm.errorMsg}}</h5>

</div>
<div class="modal-body" style="padding: 20px; max-height: 340px;">
    <div class="view-content no-padding" style="overflow-y:auto;">
        <%--<a href="" ng-click="costToProjectDialogVm.addRow()">New Row</a>--%>
        <div style="width: 100%; height: 270px; border: 1px solid #ddd">
            <div>
                <table style="width:100%">
                    <tr ng-mousedown="costToProjectDialogVm.clickEventsOnEmptyrow($event)">
                        <th style="text-align: center">Cost Name</th>
                        <th style="text-align: center">Cost</th>
                    </tr>
                    <tr ng-repeat="row in costToProjectDialogVm.rows"
                        ng-mousedown="costToProjectDialogVm.handleClick($event, row)">
                        <td>
                            <input type="text" class="form-control"
                                   ng-model="row.costName">
                        </td>
                        <td>
                            <input type="number" class="form-control"
                                   ng-model="row.cost">
                        </td>
                    </tr>

                    <tr ng-mousedown="costToProjectDialogVm.clickEventsOnEmptyrow($event)">
                        <td>
                            <input type="text" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[0].costName">
                        </td>
                        <td>
                            <input type="number" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[0].cost">
                        </td>
                    </tr>
                    <tr ng-mousedown="costToProjectDialogVm.clickEventsOnEmptyrow($event)">
                        <td>
                            <input type="text" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[1].costName">
                        </td>
                        <td>
                            <input type="number" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[1].cost">
                        </td>
                    </tr>
                    <tr ng-mousedown="costToProjectDialogVm.clickEventsOnEmptyrow($event)">
                        <td>
                            <input type="text" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[2].costName">
                        </td>
                        <td>
                            <input type="number" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[2].cost">
                        </td>
                    </tr>
                    <tr ng-mousedown="costToProjectDialogVm.clickEventsOnEmptyrow($event)">
                        <td>
                            <input type="text" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[3].costName">
                        </td>
                        <td>
                            <input type="number" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[3].cost">
                        </td>
                    </tr>
                    <tr ng-mousedown="costToProjectDialogVm.clickEventsOnEmptyrow($event)">
                        <td>
                            <input type="text" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[4].costName">
                        </td>
                        <td>
                            <input type="number" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[4].cost">
                        </td>
                    </tr>
                    <tr ng-mousedown="costToProjectDialogVm.clickEventsOnEmptyrow($event)">
                        <td>
                            <input type="text" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[5].costName">
                        </td>
                        <td>
                            <input type="number" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[5].cost">
                        </td>
                    </tr>
                    <tr ng-mousedown="costToProjectDialogVm.clickEventsOnEmptyrow($event)">
                        <td>
                            <input type="text" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[6].costName">
                        </td>
                        <td>
                            <input type="number" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[6].cost">
                        </td>
                    </tr>
                    <tr ng-mousedown="costToProjectDialogVm.clickEventsOnEmptyrow($event)">
                        <td>
                            <input type="text" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[7].costName">
                        </td>
                        <td>
                            <input type="number" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[7].cost">
                        </td>
                    </tr>
                    <tr ng-mousedown="costToProjectDialogVm.clickEventsOnEmptyrow($event)">
                        <td>
                            <input type="text" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[8].costName">
                        </td>
                        <td>
                            <input type="number" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[8].cost">
                        </td>
                    </tr>
                    <tr ng-mousedown="costToProjectDialogVm.clickEventsOnEmptyrow($event)">
                        <td>
                            <input type="text" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[9].costName">
                        </td>
                        <td>
                            <input type="number" class="form-control"
                                   ng-model="costToProjectDialogVm.emptyRows[9].cost">
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<ul id="menu" class="dropdown-menu">
    <li data-item="delete" id="remove" ng-click="costToProjectDialogVm.deleteRow()"><a>Delete</a></li>
</ul>
<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-default"
                    ng-click="costToProjectDialogVm.cancel()">Cancel
            </button>
            <button type="button" class="btn btn-sm btn-success"
                    ng-click="costToProjectDialogVm.onOk()">OK
            </button>
        </div>
    </div>
</div>
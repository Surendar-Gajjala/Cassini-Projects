<div class="modal-header">
    <h3 class="text-center">Department Selection</h3>
</div>

<div class="modal-body" style="padding: 20px; max-height: 500px;">
    <div class="main">
        <div>
            <div class="col-md-12" style="padding:0px;max-height: 300px;overflow: auto;">
                <table class="table table-striped">

                    <tbody>
                    <tr>
                        <th style="vertical-align: middle;">
                            Action
                        </th>
                        <th style="vertical-align: middle;">
                            Name
                        </th>
                        <th style="vertical-align: middle;">
                            Description
                        </th>

                    </tr>
                    <tr ng-repeat="department in changeDepartmentVm.departments">
                        <th style="width: 80px; text-align: center">
                            <input name="department" type="radio" ng-click="changeDepartmentVm.radioChange(department)">
                        </th>
                        <td style="vertical-align: middle;">
                            {{department.name}}
                        </td>
                        <td style="vertical-align: middle;">
                            {{department.description}}
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-default"
                    ng-click="changeDepartmentVm.cancel()">Cancel
            </button>
            <button type="button" class="btn btn-sm btn-success"
                    ng-click="changeDepartmentVm.onOk()">Select
            </button>
        </div>
    </div>
</div>
</div>

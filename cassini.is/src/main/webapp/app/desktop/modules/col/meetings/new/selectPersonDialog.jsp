<div class="modal-header">
    <h3>Select Persons</h3>
</div>
<div class="modal-body">
    <table class="table table-striped">
        <tbody>
        <tr ng-repeat="person in personSelVm.persons">

            <td style="width: 80px; text-align: center">
                <input type="checkbox" ng-model="person.check" name="person"
                       ng-click="personSelVm.selectCheckBox($index, person)"/>
            </td>
            <td style="vertical-align: middle;">
                {{person.fullName<%--+" "+person.fullName--%>}}
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-default"
                    ng-click="personSelVm.cancel()">Cancel
            </button>
            <button type="button" class="btn btn-sm btn-success"
                    ng-click="personSelVm.onOk()">Select
            </button>
        </div>
    </div>
</div>
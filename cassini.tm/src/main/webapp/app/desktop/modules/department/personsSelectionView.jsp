<div class="modal-header">
    <h3 class="text-center">Persons Selection</h3>
</div>

<div class="modal-body" style="padding: 20px; max-height: 500px;">
    <div class="main">
        <div>
            <div>
                <i class="pull-right" style="margin-top: -1px;">Selected
                    persons <span
                        class="badge">{{personsSelectionVm.selectedPersons.length}}</span></i></h4>
            </div>
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
                            PhoneNumber
                        </th>
                        <th style="vertical-align: middle;">
                            Email
                        </th>

                    </tr>
                    <tr ng-repeat="person in personsSelectionVm.persons">
                        <th style="width: 80px; text-align: center">
                            <input name="person" type="checkbox" ng-click="personsSelectionVm.selectCheck(person)">
                        </th>
                        <td style="vertical-align: middle;">
                            {{person.fullName}}
                        </td>
                        <td style="vertical-align: middle;">
                            {{person.phoneMobile}}
                        </td>
                        <td style="vertical-align: middle;">
                            {{person.email}}
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
                    ng-click="personsSelectionVm.cancel()">Cancel
            </button>
            <button type="button" class="btn btn-sm btn-success"
                    ng-click="personsSelectionVm.onOk()">Select
            </button>
        </div>
    </div>
</div>
</div>

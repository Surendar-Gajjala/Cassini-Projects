<div style="padding: 0px 10px;">
    <div class="row">
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <tbody>
                <thead>
                <th style="vertical-align: middle;text-align: center">
                    <div class="ckbox ckbox-default" style="display: inline-block;"
                         ng-if="personDialogueVm.manpower.length > 1">
                        <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                               ng-model="personDialogueVm.selectedAll"
                               ng-click="personDialogueVm.checkAll()">
                        <label for="item{{$index}}" class="item-selection-checkbox"></label>
                    </div>
                </th>
                <th style="vertical-align: middle;">
                    Emp ID
                </th>
                <th style="vertical-align: middle;">
                    Name
                </th>
                <th style="vertical-align: middle;">
                    Phone
                </th>
                <th style="vertical-align: middle;">
                    Email
                </th>
                </thead>
                <tbody>
                <tr ng-if="personDialogueVm.manpower.length == 0">
                    <td colspan="11" style="padding-left: 30px;">No Persons are available to view</td>
                </tr>
                <tr ng-repeat="person in personDialogueVm.manpower">
                    <th style="width: 80px; text-align: center">
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                   ng-click="personDialogueVm.select(person)"
                                   ng-model="person.selected">
                            <label for="item{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <td style="vertical-align: middle;">
                        {{person.itemNumber}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{person.person.fullName}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{person.person.phoneMobile}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{person.person.email}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <br>
        <br>
    </div>
</div>

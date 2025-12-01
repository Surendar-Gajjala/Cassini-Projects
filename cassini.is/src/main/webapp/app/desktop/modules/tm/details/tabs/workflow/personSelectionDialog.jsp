<div class="view-container" fitcontent>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">
        <div>
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 80px; text-align: center">
                        <input ng-if="personSelectionVm.persons.length > 1" name="item" type="checkbox"
                               ng-model="personSelectionVm.selectAllCheck"
                               ng-click="personSelectionVm.selectAll(check);" ng-checked="check">
                    </th>
                    <th style="width: 200px;">Name</th>
                    <th style="width: 200px;">Phone Number</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="personSelectionVm.loading == true">
                    <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">Loading Persons..
                    </span>
                    </td>
                </tr>
                <tr ng-if="personSelectionVm.loading == false && personSelectionVm.persons.length == 0">
                    <td colspan="12">No Persons are available to view</td>
                </tr>

                <tr ng-repeat="person in personSelectionVm.persons track by $index">
                    <td style="width: 80px; text-align: center">
                        <input type="checkbox" name="person" value="person" ng-model="person.selected"
                               ng-click="personSelectionVm.selectCheck(person)">
                    </td>
                    <td style=" width: 200px;">
                        {{person.fullName}}
                    </td>
                    <td style="width: 200px;">{{person.phoneMobile}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>







<div>
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="width: 80px; text-align: center">
                <input name="item" type="checkbox" ng-model="personVm.selectAllCheck"
                       ng-if="personVm.persons.length != 0"
                       ng-click="personVm.selectAll(check);" ng-checked="check">
            </th>
            <th style="width: 200px;" translate>Name</th>
            <th style="width: 200px;" translate>PHONE_NUMBER</th>
        </tr>
        </thead>

        <tbody>
        <tr ng-if="personVm.loading == true">
            <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">Loading persons...
                    </span>
            </td>
        </tr>
        <tr ng-if="personVm.persons.length == 0">
            <td colspan="12" translate>NO_PERSONS</td>
        </tr>

        <tr ng-repeat="person in personVm.persons">
            <td style="width: 80px; text-align: center">
                <input type="checkbox" name="person" value="person" ng-model="person.selected"
                       ng-click="personVm.selectCheck(person)">
            </td>
            <td style=" width: 200px;">
                {{person.fullName}}
            </td>
            <td style="width: 200px;">{{person.phoneMobile}}</td>
        </tr>
        </tbody>
    </table>
</div>








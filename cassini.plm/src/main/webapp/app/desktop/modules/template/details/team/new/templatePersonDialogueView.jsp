<div style="overflow-y: auto;padding: 10px !important;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="width: 80px; text-align: center">
                <input ng-if="templatePersonVm.persons.length != 0" name="item" type="checkbox"
                       ng-model="templatePersonVm.selectedAll"
                       ng-click="templatePersonVm.checkAll()" ng-checked="check">
            </th>
            <th style="vertical-align: middle;">
                Name
            </th>
            <th style="vertical-align: middle;">
                Email
            </th>
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
        <tr ng-if="templatePersonVm.persons.length == 0">
            <td colspan="12" translate>NO_PERSONS</td>
        </tr>

        <tr ng-repeat="person in templatePersonVm.persons">
            <td style="width: 80px; text-align: center">
                <input type="checkbox" name="person" value="person" ng-click="templatePersonVm.select(person)"
                       ng-model="person.selected">
            </td>
            <td style="vertical-align: middle;">
                {{person.fullName}}
            </td>
            <td style="vertical-align: middle;">
                {{person.email}}
            </td>
        </tr>
        </tbody>
    </table>
</div>










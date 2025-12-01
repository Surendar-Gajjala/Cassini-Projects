<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="manpowerSelectVm.resetPage"
                          on-search="manpowerSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{manpowerSelectVm.manpowerList.numberOfElements}} of
                                            {{manpowerSelectVm.manpowerList.totalElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{manpowerSelectVm.manpowerList.totalElements != 0 ? manpowerSelectVm.manpowerList.number+1:0}} of {{manpowerSelectVm.manpowerList.totalPages}}</span>
                <a href="" ng-click="manpowerSelectVm.previousPage()"
                   ng-class="{'disabled': manpowerSelectVm.manpowerList.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="manpowerSelectVm.nextPage()"
                   ng-class="{'disabled': manpowerSelectVm.manpowerList.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px" translate>SELECT</th>
                <th style="width: 200px;" translate>Manpower Number</th>
                <th style="width: 200px;" translate>Manpower Type</th>
                <th style="width: 200px;" translate>Manpower Name</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="manpowerSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading manpower</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="manpowerSelectVm.loading == false && manpowerSelectVm.manpowerList.content.length == 0">
                <td colspan="12">
                    <span translate>No manpower</span>
                </td>
            </tr>

            <tr ng-repeat="manpower in manpowerSelectVm.manpowerList.content | filter: search"
                ng-click="manpower.isChecked = !manpower.isChecked; manpowerSelectVm.radioChange(manpower, $event)"
                ng-dblClick="manpower.isChecked = !manpower.isChecked; manpowerSelectVm.selectRadioChange(manpower, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="manpower.isChecked" name="manpower" value="manpower"
                           ng-dblClick="manpowerSelectVm.selectRadioChange(manpower, $event)"
                           ng-click="manpowerSelectVm.radioChange(manpower, $event)">
                </td>
                <td style="width: 200px;">
                    {{manpower.itemNumber}}
                </td>
                <td style="width: 200px;">{{manpower.itemType.name}}</td>
                <td style="width: 200px;">{{manpower.person.fullName}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
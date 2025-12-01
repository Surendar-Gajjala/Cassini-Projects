<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector">
        <free-text-search on-clear="machineSelectVm.resetPage"
                          on-search="machineSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                <span>
                    <medium>
                        <span style="margin-right: 10px;">
                                        Displaying {{machineSelectVm.machines.numberOfElements}} of
                                            {{machineSelectVm.machines.totalElements}}
                        </span>
                    </medium>
                </span>
                <span class="mr10">Page {{machineSelectVm.machines.totalElements != 0 ? machineSelectVm.machines.number+1:0}} of {{machineSelectVm.machines.totalPages}}</span>
                <a href="" ng-click="machineSelectVm.previousPage()"
                   ng-class="{'disabled': machineSelectVm.machines.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="machineSelectVm.nextPage()"
                   ng-class="{'disabled': machineSelectVm.machines.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px" translate>SELECT</th>
                <th>Item Number</th>
                <th>Item Type</th>
                <th>Description</th>
                <th>Item Name</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="machineSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading Machines</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="machineSelectVm.loading == false && machineSelectVm.machines.content.length == 0">
                <td colspan="12">
                    <span translate>No Machines</span>
                </td>
            </tr>

            <tr ng-repeat="machine in machineSelectVm.machines.content | filter: search"
                ng-click="machine.isChecked = !machine.isChecked; machineSelectVm.radioChange(machine, $event)"
                ng-dblClick="machine.isChecked = !machine.isChecked; machineSelectVm.selectRadioChange(machine, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="machine.isChecked" name="machine" value="machine"
                           ng-dblClick="machineSelectVm.selectRadioChange(machine, $event)"
                           ng-click="machineSelectVm.radioChange(machine, $event)">
                </td>
                <td>{{machine.itemNumber}}</td>
                <td>{{machine.itemType.name}}</td>
                <td title="{{machine.description}}">{{machine.description | limitTo: 20}}{{machine.description.length >
                    20 ? '...' : ''}}
                </td>
                <td title="{{machine.itemName}}">{{machine.itemName | limitTo: 20}}{{machine.itemName.length > 20 ?
                    '...' : ''}}
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
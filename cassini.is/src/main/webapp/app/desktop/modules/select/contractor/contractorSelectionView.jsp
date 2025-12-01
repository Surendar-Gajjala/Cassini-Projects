<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="contractorSelectVm.resetPage"
                          on-search="contractorSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{contractorSelectVm.contractors.numberOfElements}} of
                                            {{contractorSelectVm.contractors.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{contractorSelectVm.contractors.totalElements != 0 ? contractorSelectVm.contractors.number+1:0}} of {{contractorSelectVm.contractors.totalPages}}</span>
                <a href="" ng-click="contractorSelectVm.previousPage()"
                   ng-class="{'disabled': contractorSelectVm.contractors.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="contractorSelectVm.nextPage()"
                   ng-class="{'disabled': contractorSelectVm.contractors.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">Select</th>
                <th style="width:150px;">Contractor Name</th>
                <th style="width:150px;">Contact Person</th>
                <th style="width:150px;text-align: center">Work Orders</th>
                <th style="width:100px;text-align: center">Active</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="contractorSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading Contractors</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="contractorSelectVm.loading == false && contractorSelectVm.contractors.content.length == 0">
                <td colspan="12">
                    <span translate>No Contractors are available to view</span>
                </td>
            </tr>

            <tr ng-repeat="contractor in contractorSelectVm.contractors.content | filter: search"
                ng-click="contractor.isChecked = !contractor.isChecked; contractorSelectVm.radioChange(contractor, $event)"
                ng-dblClick="contractor.isChecked = !contractor.isChecked; contractorSelectVm.selectRadioChange(contractor, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="contractor.isChecked" name="contractor" value="contractor"
                           ng-dblClick="contractorSelectVm.selectRadioChange(contractor, $event)"
                           ng-click="contractorSelectVm.radioChange(contractor, $event)">
                </td>
                <td style="width: 150px;"><span
                        ng-bind-html="contractor.name | highlightText: freeTextQuery"></span></a></td>
                <td style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"><span
                        ng-bind-html="contractor.contactObject.fullName | highlightText: freeTextQuery"></span></td>
                <td style="width: 150px; text-align: center; cursor: pointer">
                    <span>{{contractor.workOrders.length}}</span></a>
                </td>
                <td style="text-align: center" ng-if="contractor.active">Yes</td>
                <td style="text-align: center" ng-if="!contractor.active">No</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
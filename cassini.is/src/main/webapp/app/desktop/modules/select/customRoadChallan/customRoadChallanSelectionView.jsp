<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="customRoadChallanSelectVm.resetPage"
                          on-search="customRoadChallanSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{customRoadChallanSelectVm.customRoadChallans.numberOfElements}} of
                                            {{customRoadChallanSelectVm.customRoadChallans.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{customRoadChallanSelectVm.customRoadChallans.totalElements != 0 ? customRoadChallanSelectVm.customRoadChallans.number+1:0}} of {{customRoadChallanSelectVm.customRoadChallans.totalPages}}</span>
                <a href="" ng-click="customRoadChallanSelectVm.previousPage()"
                   ng-class="{'disabled': customRoadChallanSelectVm.customRoadChallans.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="customRoadChallanSelectVm.nextPage()"
                   ng-class="{'disabled': customRoadChallanSelectVm.customRoadChallans.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">Select</th>
                <th>Road Challan No</th>
                <th>Store</th>
                <th>Going From</th>
                <th>Going To</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="customRoadChallanSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading Custom Road Challans..</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="customRoadChallanSelectVm.loading == false && customRoadChallanSelectVm.customRoadChallans.content.length == 0">
                <td colspan="12">
                    <span translate>No Custom Road Challans are available to view</span>
                </td>
            </tr>

            <tr ng-repeat="customRoadChallan in customRoadChallanSelectVm.customRoadChallans.content | filter: search"
                ng-click="customRoadChallan.isChecked = !customRoadChallan.isChecked; customRoadChallanSelectVm.radioChange(customRoadChallan, $event)"
                ng-dblClick="customRoadChallan.isChecked = !customRoadChallan.isChecked; customRoadChallanSelectVm.selectRadioChange(customRoadChallan, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="customRoadChallan.isChecked" name="customRoadChallan"
                           value="customRoadChallan"
                           ng-dblClick="customRoadChallanSelectVm.selectRadioChange(customRoadChallan, $event)"
                           ng-click="customRoadChallanSelectVm.radioChange(customRoadChallan, $event)">
                </td>
                <td>{{customRoadChallan.chalanNumber}}</td>
                <td>{{customRoadChallan.store.storeName}}</td>
                <td>{{customRoadChallan.goingFrom}}</td>
                <td>{{customRoadChallan.goingTo}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
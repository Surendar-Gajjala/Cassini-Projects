<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="scrapRequestSelectVm.resetPage"
                          on-search="scrapRequestSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{scrapRequestSelectVm.scrapRequests.numberOfElements}} of
                                            {{scrapRequestSelectVm.scrapRequests.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{scrapRequestSelectVm.scrapRequests.totalElements != 0 ? scrapRequestSelectVm.scrapRequests.number+1:0}} of {{scrapRequestSelectVm.scrapRequests.totalPages}}</span>
                <a href="" ng-click="scrapRequestSelectVm.previousPage()"
                   ng-class="{'disabled': scrapRequestSelectVm.scrapRequests.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="scrapRequestSelectVm.nextPage()"
                   ng-class="{'disabled': scrapRequestSelectVm.scrapRequests.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">Select</th>
                <th>Name</th>
                <th>Project</th>
                <th>Store</th>
                <th>Notes</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="scrapRequestSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading Scrap Requests</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="scrapRequestSelectVm.loading == false && scrapRequestSelectVm.scrapRequests.content.length == 0">
                <td colspan="12">
                    <span translate>No Scrap Requests</span>
                </td>
            </tr>

            <tr ng-repeat="scrapRequest in scrapRequestSelectVm.scrapRequests.content | filter: search"
                ng-click="scrapRequest.isChecked = !scrapRequest.isChecked; scrapRequestSelectVm.radioChange(scrapRequest, $event)"
                ng-dblClick="scrapRequest.isChecked = !scrapRequest.isChecked; scrapRequestSelectVm.selectRadioChange(scrapRequest, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="scrapRequest.isChecked" name="scrapRequest" value="scrapRequest"
                           ng-dblClick="scrapRequestSelectVm.selectRadioChange(scrapRequest, $event)"
                           ng-click="scrapRequestSelectVm.radioChange(scrapRequest, $event)">
                </td>
                <td>{{scrapRequest.scrapNumber}}</td>
                <td>{{scrapRequest.projectObject.name}}</td>
                <td>{{scrapRequest.storeObject.storeName}}</td>
                <td>{{scrapRequest.notes}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
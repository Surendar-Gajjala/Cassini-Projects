<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="storeSelectVm.resetPage"
                          on-search="storeSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{storeSelectVm.stores.numberOfElements}} of
                                            {{storeSelectVm.stores.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{storeSelectVm.stores.totalElements != 0 ? storeSelectVm.stores.number+1:0}} of {{storeSelectVm.stores.totalPages}}</span>
                <a href="" ng-click="storeSelectVm.previousPage()"
                   ng-class="{'disabled': storeSelectVm.stores.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="storeSelectVm.nextPage()"
                   ng-class="{'disabled': storeSelectVm.stores.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">Select</th>
                <th style="min-width: 150px;">Name</th>
                <th style="min-width: 150px;">Description</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="storeSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading stores</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="storeSelectVm.loading == false && storeSelectVm.stores.content.length == 0">
                <td colspan="12">
                    <span translate>No stores</span>
                </td>
            </tr>

            <tr ng-repeat="store in storeSelectVm.stores.content | filter: search"
                ng-click="store.isChecked = !store.isChecked; storeSelectVm.radioChange(store, $event)"
                ng-dblClick="store.isChecked = !store.isChecked; storeSelectVm.selectRadioChange(store, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="store.isChecked" name="store" value="store"
                           ng-dblClick="storeSelectVm.selectRadioChange(store, $event)"
                           ng-click="storeSelectVm.radioChange(store, $event)">
                </td>
                <td style="width: 200px;">{{store.storeName}}</td>
                <td style="width: 200px;">{{store.description}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
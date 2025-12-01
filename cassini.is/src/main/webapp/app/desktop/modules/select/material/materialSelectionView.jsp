<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="materialSelectVm.resetPage"
                          on-search="materialSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{materialSelectVm.materials.numberOfElements}} of
                                            {{materialSelectVm.materials.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{materialSelectVm.materials.totalElements != 0 ? materialSelectVm.materials.number+1:0}} of {{materialSelectVm.materials.totalPages}}</span>
                <a href="" ng-click="materialSelectVm.previousPage()"
                   ng-class="{'disabled': materialSelectVm.materials.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="materialSelectVm.nextPage()"
                   ng-class="{'disabled': materialSelectVm.materials.last}"><i
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
            <tr ng-if="materialSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading materials</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="materialSelectVm.loading == false && materialSelectVm.materials.content.length == 0">
                <td colspan="12">
                    <span translate>No materials</span>
                </td>
            </tr>

            <tr ng-repeat="material in materialSelectVm.materials.content | filter: search"
                ng-click="material.isChecked = !material.isChecked; materialSelectVm.radioChange(material, $event)"
                ng-dblClick="material.isChecked = !material.isChecked; materialSelectVm.selectRadioChange(material, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="material.isChecked" name="material" value="material"
                           ng-dblClick="materialSelectVm.selectRadioChange(material, $event)"
                           ng-click="materialSelectVm.radioChange(material, $event)">
                </td>
                <td>{{material.itemNumber}}</td>
                <td>{{material.itemType.name}}</td>
                <td title="{{material.description}}">{{material.description | limitTo: 20}}{{material.description.length
                    > 20 ? '...' : ''}}
                </td>
                <td title="{{material.itemName}}">{{material.itemName | limitTo: 20}}{{material.itemName.length > 20 ?
                    '...' : ''}}
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
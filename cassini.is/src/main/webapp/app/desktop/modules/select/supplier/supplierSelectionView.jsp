<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="supplierSelectVm.resetPage"
                          on-search="supplierSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{supplierSelectVm.suppliers.numberOfElements}} of
                                            {{supplierSelectVm.suppliers.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{supplierSelectVm.suppliers.totalElements != 0 ? supplierSelectVm.suppliers.number+1:0}} of {{supplierSelectVm.suppliers.totalPages}}</span>
                <a href="" ng-click="supplierSelectVm.previousPage()"
                   ng-class="{'disabled': supplierSelectVm.suppliers.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="supplierSelectVm.nextPage()"
                   ng-class="{'disabled': supplierSelectVm.suppliers.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="padding: 10px !important;">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">Select</th>
                <th>Supplier Name</th>
                <th>Description</th>
                <th>Contact Person</th>
                <th>Contact Phone</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="supplierSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading suppliers</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="supplierSelectVm.loading == false && supplierSelectVm.suppliers.content.length == 0">
                <td colspan="12">
                    <span translate>No suppliers</span>
                </td>
            </tr>

            <tr ng-repeat="supplier in supplierSelectVm.suppliers.content | filter: search"
                ng-click="supplier.isChecked = !supplier.isChecked; supplierSelectVm.radioChange(supplier, $event)"
                ng-dblClick="supplier.isChecked = !supplier.isChecked; supplierSelectVm.selectRadioChange(supplier, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="supplier.isChecked" name="supplier" value="supplier"
                           ng-dblClick="supplierSelectVm.selectRadioChange(supplier, $event)"
                           ng-click="supplierSelectVm.radioChange(supplier, $event)">
                </td>
                <td>{{supplier.name}}</td>
                <td class="description" style="width: 150px;">{{supplier.description}}</td>
                <td>{{supplier.contactPerson}}</td>
                <td>{{supplier.contactPhone}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }
</style>
<div style="padding: 0px 10px;">
    <form class="form-inline ng-pristine ng-valid" style="margin: 10px 0px;display: flex;flex-direction: row;">
        <div class="form-group" style="flex-grow: 1;margin-right: 0;">
            <div class="input-group input-group-sm" style="">
                <div class="input-group-btn dropdown" uib-dropdown="" style="">
                    <button uib-dropdown-toggle="" class="btn btn-default dropdown-toggle" type="button"
                            aria-haspopup="true" aria-expanded="false"
                            style="background-color: #fff;color: #333 !important">
                        <span class="ng-scope" translate>SELECT</span> <span class="caret"
                                                                             style="margin-left: 4px;"></span>
                    </button>
                    <div class="dropdown-menu" role="menu">
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                            <supplier-tree
                                    on-select-type="supplierSelectionVm.onSelectType"
                                    object-type="SUPPLIERTYPE"></supplier-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       style="background-color: #fff;" ng-model="supplierSelectionVm.selectedType.name"
                       readonly="">

            </div>
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="supplierSelectionVm.filters.name"
                   ng-change="supplierSelectionVm.searchFilterItem()"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;" placeholder="Name"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="supplierSelectionVm.filters.city"
                   ng-change="supplierSelectionVm.searchFilterItem()"
                   style="margin-left: -16px; height: 30px; margin-top: 0px; width: 100%;"
                   placeholder="City" class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">

            <!-- ngIf: supplierSelectionVm.clear == true -->
            <button ng-click="supplierSelectionVm.clearFilter()" translate="" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="supplierSelectionVm.clear">Clear
            </button>
            <!-- end ngIf: supplierSelectionVm.clear == true -->
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
                <div style="">
                    <span style="" translate>SELECTED_SUPPLIERS</span>
                    <span class="badge">{{supplierSelectionVm.selectedSuppliers.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                             <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="supplierSelectionVm.suppliers.numberOfElements ==0">
                            {{(supplierSelectionVm.pageable.page*supplierSelectionVm.pageable.size)}}
                        </span>

                                    <span ng-if="supplierSelectionVm.suppliers.numberOfElements > 0">
                            {{(supplierSelectionVm.pageable.page*supplierSelectionVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="supplierSelectionVm.suppliers.last ==false">{{((supplierSelectionVm.pageable.page+1)*supplierSelectionVm.pageable.size)}}</span>
                                    <span ng-if="supplierSelectionVm.suppliers.last == true">{{supplierSelectionVm.suppliers.totalElements}}</span>

                                 <span translate> OF </span>
                                {{supplierSelectionVm.suppliers.totalElements}}<span
                                        translate>AN</span>
                                </span>
                             </medium>
                        </span>
                        <span class="mr10">  Page {{supplierSelectionVm.suppliers.totalElements != 0 ?
                        supplierSelectionVm.suppliers.number+1:0}} <span translate>OF</span> {{supplierSelectionVm.suppliers.totalPages}}</span>
                        <a href="" ng-click="supplierSelectionVm.previousPage()"
                           ng-class="{'disabled': supplierSelectionVm.suppliers.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="supplierSelectionVm.nextPage()"
                           ng-class="{'disabled': supplierSelectionVm.suppliers.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="text-align: center;width: 20px !important;">
                        <input name="item" type="checkbox" ng-model="supplierSelectionVm.selectAllCheck"
                               ng-if="supplierSelectionVm.suppliers.content.length != 0"
                               ng-click="supplierSelectionVm.selectAll(check);" ng-checked="check">
                    </th>
                    <th style="width: 150px" translate>NUMBER</th>
                    <th style="width: 180px;" translate>NAME</th>
                    <th style="width: 150px;" translate>TYPE</th>
                    <th style="width: 200px;" translate>CITY</th>
                    <th style="width: 200px;" translate>STATUS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="supplierSelectionVm.suppliers.content.length == 0">
                    <td colspan="15" translate>NO_SUPPLIERS</td>
                </tr>
                <tr ng-if="supplierSelectionVm.suppliers.content.length > 0"
                    ng-repeat="supplier in supplierSelectionVm.suppliers.content">
                    <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                        <input name="item" type="checkbox" ng-model="supplier.selected"
                               ng-click="supplierSelectionVm.selectCheck(supplier)">
                    </td>

                    <td style="vertical-align: middle;">
                        {{supplier.number}}
                    </td>
                    <td style="vertical-align: middle;" title="{{}}">
                        {{supplier.name}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{supplier.supplierType.name}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{supplier.city}}
                    </td>
                    <td>
                        <item-status item="supplier"></item-status>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>

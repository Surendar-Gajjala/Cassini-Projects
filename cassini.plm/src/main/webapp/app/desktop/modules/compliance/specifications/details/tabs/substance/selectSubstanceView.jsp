<div style="padding: 0px 10px;">
    <style scoped>
        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }
    </style>
    <form class="form-inline ng-pristine ng-valid" style="margin: 10px 0px;display: flex;flex-direction: row;">
        <div class="form-group" style="flex-grow: 1;margin-right: 0;">
            <div class="input-group input-group-sm" style="">
                <div class="input-group-btn dropdown" uib-dropdown="" style="">
                    <button uib-dropdown-toggle="" class="btn btn-default dropdown-toggle" type="button"
                            aria-haspopup="true" aria-expanded="false" style="background-color: #fff;">
                        <span translate="" class="ng-scope">Select</span> <span class="caret"
                                                                                style="margin-left: 4px;"></span>
                    </button>
                    <div class="dropdown-menu" role="menu">
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                            <compliance-type-tree
                                    on-select-type="selectSubstanceVm.onSelectType" class="ng-isolate-scope"
                                    object-type="PGCSUBSTANCETYPE"></compliance-type-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       style="background-color: #fff;" ng-model="selectSubstanceVm.type.name" readonly="">

            </div>
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="selectSubstanceVm.filters.number"
                   ng-change="selectSubstanceVm.searchSubstances()"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                   placeholder="{{selectSubstanceVm.number}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="selectSubstanceVm.filters.name"
                   ng-change="selectSubstanceVm.searchSubstances()"
                   style="margin-left: -16px; height: 30px; margin-top: 0px; width: 100%;margin-top: 0;"
                   placeholder="{{selectSubstanceVm.name}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">

            <!-- ngIf: selectSubstanceVm.clear == true -->
            <button ng-click="selectSubstanceVm.clearFilter()" translate="" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="selectSubstanceVm.clear">Clear
            </button>
            <!-- end ngIf: selectSubstanceVm.clear == true -->
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
                <div style="">
                    <span style="" translate>SELECTED_SUBSTANCES</span>
                    <span class="badge">{{selectSubstanceVm.selectedItems.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="selectSubstanceVm.substances.numberOfElements ==0">
                            {{(selectSubstanceVm.pageable.page*selectSubstanceVm.pageable.size)}}
                        </span>

                                    <span ng-if="selectSubstanceVm.substances.numberOfElements > 0">
                            {{(selectSubstanceVm.pageable.page*selectSubstanceVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="selectSubstanceVm.substances.last ==false">{{((selectSubstanceVm.pageable.page+1)*selectSubstanceVm.pageable.size)}}</span>
                                    <span ng-if="selectSubstanceVm.substances.last == true">{{selectSubstanceVm.substances.totalElements}}</span>

                                 <span translate> OF </span>
                                {{selectSubstanceVm.substances.totalElements}}<span
                                        translate>AN</span>
                                </span>
                            </medium>
                        </span>
                        <span class="mr10">  Page {{selectSubstanceVm.substances.totalElements != 0 ?
                        selectSubstanceVm.substances.number+1:0}} <span translate>OF</span> {{selectSubstanceVm.substances.totalPages}}</span>
                        <a href="" ng-click="selectSubstanceVm.previousPage()"
                           ng-class="{'disabled': selectSubstanceVm.substances.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="selectSubstanceVm.nextPage()"
                           ng-class="{'disabled': selectSubstanceVm.substances.last}"><i
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
                        <input name="item" type="checkbox" ng-if="selectSubstanceVm.substances.content.length > 0"
                               ng-model="selectSubstanceVm.selectAllCheck"
                               ng-click="selectSubstanceVm.checkAll(check);" ng-checked="check">
                    </th>
                    <th style="width: 150px" translate>NUMBER</th>
                    <th style="width: 180px;" translate>TYPE</th>
                    <th style="width: 150px;" translate>NAME</th>
                    <th style="width: 200px;" translate>CAS Number</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="selectSubstanceVm.substances.content.length == 0">
                    <td colspan="15" translate>NO_SUBSTANCES</td>
                </tr>
                <tr ng-if="selectSubstanceVm.substances.content.length > 0"
                    ng-repeat="substance in selectSubstanceVm.substances.content">
                    <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                        <input name="substance" type="checkbox" ng-model="substance.selected"
                               ng-click="selectSubstanceVm.select(substance)">
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="substance.number | highlightText: selectSubstanceVm.filters.number"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="substance.type.name | highlightText: selectSubstanceVm.filters.typeName"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="substance.name | highlightText: selectSubstanceVm.filters.name"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="substance.casNumber | highlightText: freeTextQuery"></span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>


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
                        <span class="ng-scope" translate>SELECT</span> <span class="caret"
                                                                             style="margin-left: 4px;"></span>
                    </button>
                    <div class="dropdown-menu" role="menu">
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                            <compliance-type-tree
                                    on-select-type="selectSubstancesVm.onSelectType"
                                    object-type="PGCSUBSTANCETYPE"></compliance-type-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       placeholder="{{'SELECT_TYPE' | translate}}"
                       style="background-color: #fff;" ng-model="selectSubstancesVm.partType.name" readonly="">

            </div>
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="selectSubstancesVm.filters.casNumber"
                   ng-change="selectSubstancesVm.searchSubstances()"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                   placeholder="{{'CAS_NUMBER' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="selectSubstancesVm.filters.name"
                   ng-change="selectSubstancesVm.searchSubstances()"
                   style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                   placeholder="{{'ENTER_NAME' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">
            <button ng-click="selectSubstancesVm.clearFilter()" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="selectSubstancesVm.clear" translate>CLEAR
            </button>
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
                <div style="">
                    <span style="" translate>SELECTED_SUBSTANCES</span>
                    <span class="badge badge-success">{{selectSubstancesVm.selectedSubstances.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="selectSubstancesVm.substances.numberOfElements == 0">
                                    {{(selectSubstancesVm.pageable.page*selectSubstancesVm.pageable.size)}}
                                </span>
                                <span ng-if="selectSubstancesVm.substances.numberOfElements > 0">
                                    {{(selectSubstancesVm.pageable.page*selectSubstancesVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="selectSubstancesVm.substances.last ==false">{{((selectSubstancesVm.pageable.page+1)*selectSubstancesVm.pageable.size)}}</span>
                                <span ng-if="selectSubstancesVm.substances.last == true">{{selectSubstancesVm.substances.totalElements}}</span>
                                <span translate> OF </span> {{selectSubstancesVm.substances.totalElements}}<span
                                        translate>AN</span></span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{selectSubstancesVm.substances.totalElements != 0 ? selectSubstancesVm.substances.number+1:0}}
                            <span translate>OF</span> {{selectSubstancesVm.substances.totalPages}}
                        </span>
                        <a href="" ng-click="selectSubstancesVm.previousPage()"
                           ng-class="{'disabled': selectSubstancesVm.substances.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="selectSubstancesVm.nextPage()"
                           ng-class="{'disabled': selectSubstancesVm.substances.last}"><i
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
                        <input name="item" type="checkbox" ng-if="selectSubstancesVm.substances.content.length > 0"
                               ng-model="selectSubstancesVm.selectAllCheck"
                               ng-click="selectSubstancesVm.checkAll(check);" ng-checked="check">
                    </th>
                    <th style="width: 150px" translate>CAS_NUMBER</th>
                    <th style="width: 180px;" translate>NAME</th>
                    <th style="width: 180px;" translate>TYPE</th>
                    <th style="width: 150px;" translate>DESCRIPTION</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="selectSubstancesVm.substances.content.length == 0">
                    <td colspan="15" translate>NO_SUBSTANCES</td>
                </tr>
                <tr ng-if="selectSubstancesVm.substances.content.length > 0"
                    ng-repeat="substance in selectSubstancesVm.substances.content">
                    <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                        <input name="substance" type="checkbox" ng-model="substance.selected"
                               ng-click="selectSubstancesVm.select(substance)">
                    </td>

                    <td style="vertical-align: middle;">
                        <span ng-bind-html="substance.casNumber | highlightText: selectSubstancesVm.filters.casNumber"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="substance.name | highlightText: selectSubstancesVm.filters.name"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="substance.type.name | highlightText: selectSubstancesVm.filters.typeName"></span>
                    </td>
                    <td style="vertical-align: middle;" title="{{substance.description}}">
                        {{substance.description}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>


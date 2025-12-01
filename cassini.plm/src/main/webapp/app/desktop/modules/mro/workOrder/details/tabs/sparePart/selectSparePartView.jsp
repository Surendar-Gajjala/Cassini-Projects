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
                            <manufacturing-type-tree
                                    on-select-type="selectSparePartsVm.onSelectType"
                                    object-type="SPAREPARTTYPE"></manufacturing-type-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       placeholder="{{'SELECT_TYPE' | translate}}"
                       style="background-color: #fff;" ng-model="selectSparePartsVm.partType.name" readonly="">

            </div>
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="selectSparePartsVm.filters.number" ng-change="selectSparePartsVm.searchParts()"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                   placeholder="{{'ENTER_NUMBER' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="selectSparePartsVm.filters.name" ng-change="selectSparePartsVm.searchParts()"
                   style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                   placeholder="{{'ENTER_NAME' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">
            <button ng-click="selectSparePartsVm.clearFilter()" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="selectSparePartsVm.clear" translate>CLEAR
            </button>
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
                <div style="">
                    <span style="" translate>SELECTED_PARTS</span>
                    <span class="badge badge-success">{{selectSparePartsVm.selectedParts.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="selectSparePartsVm.spareParts.numberOfElements == 0">
                                    {{(selectSparePartsVm.pageable.page*selectSparePartsVm.pageable.size)}}
                                </span>
                                <span ng-if="selectSparePartsVm.spareParts.numberOfElements > 0">
                                    {{(selectSparePartsVm.pageable.page*selectSparePartsVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="selectSparePartsVm.spareParts.last ==false">{{((selectSparePartsVm.pageable.page+1)*selectSparePartsVm.pageable.size)}}</span>
                                <span ng-if="selectSparePartsVm.spareParts.last == true">{{selectSparePartsVm.spareParts.totalElements}}</span>
                                <span translate> OF </span> {{selectSparePartsVm.spareParts.totalElements}}<span
                                        translate>AN</span></span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{selectSparePartsVm.spareParts.totalElements != 0 ? selectSparePartsVm.spareParts.number+1:0}}
                            <span translate>OF</span> {{selectSparePartsVm.spareParts.totalPages}}
                        </span>
                        <a href="" ng-click="selectSparePartsVm.previousPage()"
                           ng-class="{'disabled': selectSparePartsVm.spareParts.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="selectSparePartsVm.nextPage()"
                           ng-class="{'disabled': selectSparePartsVm.spareParts.last}"><i
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
                        <input name="item" type="checkbox" ng-if="selectSparePartsVm.spareParts.content.length > 0"
                               ng-model="selectSparePartsVm.selectAllCheck"
                               ng-click="selectSparePartsVm.checkAll(check);" ng-checked="check">
                    </th>
                    <th style="width: 150px" translate>NUMBER</th>
                    <th style="width: 180px;" translate>NAME</th>
                    <th style="width: 180px;" translate>TYPE</th>
                    <th style="width: 150px;" translate>DESCRIPTION</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="selectSparePartsVm.spareParts.content.length == 0">
                    <td colspan="15" translate>NO_SPARE_PARTS</td>
                </tr>
                <tr ng-if="selectSparePartsVm.spareParts.content.length > 0"
                    ng-repeat="sparePart in selectSparePartsVm.spareParts.content">
                    <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                        <input name="sparePart" type="checkbox" ng-model="sparePart.selected"
                               ng-click="selectSparePartsVm.select(sparePart)">
                    </td>

                    <td style="vertical-align: middle;">
                        <span ng-bind-html="sparePart.number | highlightText: selectSparePartsVm.filters.number"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="sparePart.name | highlightText: selectSparePartsVm.filters.name"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="sparePart.type.name | highlightText: selectSparePartsVm.filters.typeName"></span>
                    </td>
                    <td style="vertical-align: middle;" title="{{sparePart.description}}">
                        {{sparePart.description}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>


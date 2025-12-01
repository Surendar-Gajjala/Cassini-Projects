<div style="padding: 0px 10px;">
    <style scoped>
        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }
    </style>
    <form class="form-inline ng-pristine ng-valid" style="margin: 10px 0px;display: flex;flex-direction: row;">
        <%--<div class="form-group" style="flex-grow: 1;margin-right: 0;">
            <div class="input-group input-group-sm" style="">
                <div class="input-group-btn dropdown" uib-dropdown="" style="">
                    <button uib-dropdown-toggle="" class="btn btn-default dropdown-toggle" type="button"
                            aria-haspopup="true" aria-expanded="false" style="background-color: #fff;">
                        <span translate="" class="ng-scope">Select</span> <span class="caret"
                                                                                style="margin-left: 4px;"></span>
                    </button>
                    <div class="dropdown-menu" role="menu">
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                            <manufacturing-type-tree ng-show="selectResourcesVm.objectType=='MACHINETYPE'"
                                                     ng-hide="selectResourcesVm.objectType=='EQUIPMENTTYPE'"
                                                     on-select-type="selectResourcesVm.onSelectType"
                                                     object-type="MACHINETYPE"></manufacturing-type-tree>
                            <manufacturing-type-tree ng-show="selectResourcesVm.objectType=='EQUIPMENTTYPE'"
                                                     ng-hide="selectResourcesVm.objectType=='MACHINETYPE'"
                                                     on-select-type="selectResourcesVm.onSelectType"
                                                     object-type="EQUIPMENTTYPE"></manufacturing-type-tree>
                            <manufacturing-type-tree ng-if="selectResourcesVm.objectType=='INSTRUMENTTYPE'"
                                                     on-select-type="selectResourcesVm.onSelectType"
                                                     object-type="INSTRUMENTTYPE"></manufacturing-type-tree>
                            <manufacturing-type-tree ng-if="selectResourcesVm.objectType=='TOOLTYPE'"
                                                     on-select-type="selectResourcesVm.onSelectType"
                                                     object-type="TOOLTYPE"></manufacturing-type-tree>
                            <manufacturing-type-tree ng-if="selectResourcesVm.objectType=='MATERIALTYPE'"
                                                     on-select-type="selectResourcesVm.onSelectType"
                                                     object-type="MATERIALTYPE"></manufacturing-type-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       placeholder="{{'SELECT_TYPE' | translate}}"
                       style="background-color: #fff;" ng-model="selectResourcesVm.type.name" readonly="">

            </div>
        </div>--%>


        <%-- <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
             <input type="text" ng-model="selectResourcesVm.filters.type" ng-change="selectResourcesVm.searchResources()"
                    style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                    placeholder="{{'TYPE' | translate}}"
                    class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
         </div>--%>

        <div class="form-group" style="margin-top: -6px !important;">

            <ui-select ng-model="selectResourcesVm.selectedLabel" theme="bootstrap"
                       style="width:100%;padding-top: 4px;" on-select="onSelected($item)">
                <ui-select-match placeholder="Select">{{$select.selected.lable}}
                </ui-select-match>
                <ui-select-choices
                        repeat="object.lable as object in selectResourcesVm.objectTypes | filter: $select.search">
                    <div ng-bind-html="object.lable"></div>
                </ui-select-choices>
            </ui-select>

        </div>

        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="selectResourcesVm.filters.number"
                   ng-change="selectResourcesVm.searchResources()"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                   placeholder="{{'ENTER_NUMBER' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="selectResourcesVm.filters.name" ng-change="selectResourcesVm.searchResources()"
                   style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                   placeholder="{{'ENTER_NAME' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">
            <button ng-click="selectResourcesVm.clearFilter()" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="selectResourcesVm.clear" translate>CLEAR
            </button>
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
                <div style="">
                    <span style="" translate>SELECTED_RESOURCES</span>
                    <span class="badge badge-success">{{selectResourcesVm.selectedResources.length}}</span>
                </div>

            </div>
            <%--<div class="col-md-3">
                <div style="">
                    <ui-select ng-model="selectResourcesVm.selectedLabel" theme="bootstrap"
                               style="width:100%;padding-top: 4px;" on-select="onSelected($item)">
                        <ui-select-match placeholder="Select">{{$select.selected.lable}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="object.lable as object in selectResourcesVm.objectTypes | filter: $select.search">
                            <div ng-bind-html="object.lable"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>

            </div>--%>

            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="selectResourcesVm.resources.numberOfElements == 0">
                                    {{(selectResourcesVm.pageable.page*selectResourcesVm.pageable.size)}}
                                </span>
                                <span ng-if="selectResourcesVm.resources.numberOfElements > 0">
                                    {{(selectResourcesVm.pageable.page*selectResourcesVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="selectResourcesVm.resources.last ==false">{{((selectResourcesVm.pageable.page+1)*selectResourcesVm.pageable.size)}}</span>
                                <span ng-if="selectResourcesVm.resources.last == true">{{selectResourcesVm.resources.totalElements}}</span>
                                <span translate> OF </span> {{selectResourcesVm.resources.totalElements}}<span
                                        translate>AN</span></span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{selectResourcesVm.resources.totalElements != 0 ? selectResourcesVm.resources.number+1:0}}
                            <span translate>OF</span> {{selectResourcesVm.resources.totalPages}}
                        </span>
                        <a href="" ng-click="selectResourcesVm.previousPage()"
                           ng-class="{'disabled': selectResourcesVm.resources.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="selectResourcesVm.nextPage()"
                           ng-class="{'disabled': selectResourcesVm.resources.last}"><i
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
                        <input name="item" type="checkbox" ng-if="selectResourcesVm.resources.content.length > 0"
                               ng-model="selectResourcesVm.selectAllCheck"
                               ng-click="selectResourcesVm.checkAll(check);" ng-checked="check">
                    </th>
                    <th style="width: 150px" translate>NUMBER</th>
                    <th style="width: 180px;" translate>NAME</th>
                    <th style="width: 180px;" translate>TYPE</th>
                    <th style="width: 180px;" translate>DESCRIPTION</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="selectResourcesVm.resources.content.length == 0">
                    <td colspan="15" translate>NO_RESOURCES</td>
                </tr>
                <tr ng-if="selectResourcesVm.resources.content.length > 0"
                    ng-repeat="resource in selectResourcesVm.resources.content">
                    <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                        <input name="resource" type="checkbox" ng-model="resource.selected"
                               ng-click="selectResourcesVm.select(resource)">
                    </td>

                    <td style="vertical-align: middle;">
                        <span ng-bind-html="resource.number | highlightText: selectResourcesVm.filters.number"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="resource.name | highlightText: selectResourcesVm.filters.name"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="resource.type.name | highlightText: selectResourcesVm.filters.type"></span>
                    </td>
                    <td style="vertical-align: middle;" title="{{resource.description}}">
                        {{resource.description}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>


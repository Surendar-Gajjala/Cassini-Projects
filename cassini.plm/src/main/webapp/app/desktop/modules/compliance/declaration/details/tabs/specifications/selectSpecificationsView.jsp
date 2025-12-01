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
                            aria-haspopup="true" aria-expanded="false" style="background-color: #fff;color: black !important;">
                        <span class="ng-scope" translate>SELECT</span> <span class="caret"
                                                                             style="margin-left: 4px;"></span>
                    </button>
                    <div class="dropdown-menu" role="menu">
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                            <compliance-type-tree
                                    on-select-type="selectSpecificationsVm.onSelectType"
                                    object-type="PGCSPECIFICATIONTYPE"></compliance-type-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       placeholder="{{select}}"
                       style="background-color: #fff;" ng-model="selectSpecificationsVm.partType.name" readonly="">

            </div>
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="selectSpecificationsVm.filters.number"
                   ng-change="selectSpecificationsVm.searchSpecifications()"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                   placeholder="{{'ENTER_NUMBER' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="selectSpecificationsVm.filters.name"
                   ng-change="selectSpecificationsVm.searchSpecifications()"
                   style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                   placeholder="{{'ENTER_NAME' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">
            <button ng-click="selectSpecificationsVm.clearFilter()" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="selectSpecificationsVm.clear" translate>CLEAR
            </button>
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
                <div style="">
                    <span style="" translate>SELECTED_SPECIFICATIONS</span>
                    <span class="badge badge-success">{{selectSpecificationsVm.selectedSpecifications.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="selectSpecificationsVm.specifications.numberOfElements == 0">
                                    {{(selectSpecificationsVm.pageable.page*selectSpecificationsVm.pageable.size)}}
                                </span>
                                <span ng-if="selectSpecificationsVm.specifications.numberOfElements > 0">
                                    {{(selectSpecificationsVm.pageable.page*selectSpecificationsVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="selectSpecificationsVm.specifications.last ==false">{{((selectSpecificationsVm.pageable.page+1)*selectSpecificationsVm.pageable.size)}}</span>
                                <span ng-if="selectSpecificationsVm.specifications.last == true">{{selectSpecificationsVm.specifications.totalElements}}</span>
                                <span translate> OF </span> {{selectSpecificationsVm.specifications.totalElements}}<span
                                        translate>AN</span></span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{selectSpecificationsVm.specifications.totalElements != 0 ? selectSpecificationsVm.specifications.number+1:0}}
                            <span translate>OF</span> {{selectSpecificationsVm.specifications.totalPages}}
                        </span>
                        <a href="" ng-click="selectSpecificationsVm.previousPage()"
                           ng-class="{'disabled': selectSpecificationsVm.specifications.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="selectSpecificationsVm.nextPage()"
                           ng-class="{'disabled': selectSpecificationsVm.specifications.last}"><i
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
                        <input name="item" type="checkbox"
                               ng-if="selectSpecificationsVm.specifications.content.length > 0"
                               ng-model="selectSpecificationsVm.selectAllCheck"
                               ng-click="selectSpecificationsVm.checkAll(check);" ng-checked="check">
                    </th>
                    <th style="width: 150px" translate>NUMBER</th>
                    <th style="width: 180px;" translate>TYPE</th>
                    <th style="width: 180px;" translate>NAME</th>
                    <th style="width: 150px;" translate>DESCRIPTION</th>
                    <th style="width: 150px;" translate>STATUS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="selectSpecificationsVm.specifications.content.length == 0">
                    <td colspan="15" translate>NO_SPECIFICATIONS</td>
                </tr>
                <tr ng-if="selectSpecificationsVm.specifications.content.length > 0"
                    ng-repeat="specification in selectSpecificationsVm.specifications.content">
                    <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                        <input name="specification" type="checkbox" ng-model="specification.selected"
                               ng-click="selectSpecificationsVm.select(specification)">
                    </td>

                    <td style="vertical-align: middle;">
                        <span ng-bind-html="specification.number | highlightText: selectSpecificationsVm.filters.number"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="specification.type.name | highlightText: selectSpecificationsVm.filters.typeName"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="specification.name | highlightText: selectSpecificationsVm.filters.name"></span>
                    </td>
                    <td style="vertical-align: middle;" title="{{specification.description}}">
                        {{specification.description}}
                    </td>
                    <td style="vertical-align: middle;">
                         <span ng-if="specification.active == true"  class="label label-outline bg-light-success"
                               translate>C_ACTIVE</span>
                        <span ng-if="specification.active == false" class="label label-outline bg-light-danger"
                              translate>C_INACTIVE</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>


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
                                    on-select-type="selectAssemLineWorkCentersVm.onSelectType" class="ng-isolate-scope"
                                    object-type="WORKCENTERTYPE"></manufacturing-type-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       style="background-color: #fff;" ng-model="selectAssemLineWorkCentersVm.type.name" readonly="">

            </div>
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="selectAssemLineWorkCentersVm.filters.number"
                   ng-change="selectAssemLineWorkCentersVm.searchWorkCenters()"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                   placeholder="{{selectAssemLineWorkCentersVm.number}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="selectAssemLineWorkCentersVm.filters.name"
                   ng-change="selectAssemLineWorkCentersVm.searchWorkCenters()"
                   style="margin-left: -16px; height: 30px; margin-top: 0px; width: 100%;margin-top: 0;"
                   placeholder="{{selectAssemLineWorkCentersVm.name}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">

            <!-- ngIf: selectAssemLineWorkCentersVm.clear == true -->
            <button ng-click="selectAssemLineWorkCentersVm.clearFilter()" translate="" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="selectAssemLineWorkCentersVm.clear">Clear
            </button>
            <!-- end ngIf: selectAssemLineWorkCentersVm.clear == true -->
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
                <div style="">
                    <span style="" translate>SELECTED_WORK_CENTERS</span>
                    <span class="badge">{{selectAssemLineWorkCentersVm.selectedItems.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="selectAssemLineWorkCentersVm.workCenters.numberOfElements ==0">
                            {{(selectAssemLineWorkCentersVm.pageable.page*selectAssemLineWorkCentersVm.pageable.size)}}
                        </span>

                                    <span ng-if="selectAssemLineWorkCentersVm.workCenters.numberOfElements > 0">
                            {{(selectAssemLineWorkCentersVm.pageable.page*selectAssemLineWorkCentersVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="selectAssemLineWorkCentersVm.workCenters.last ==false">{{((selectAssemLineWorkCentersVm.pageable.page+1)*selectAssemLineWorkCentersVm.pageable.size)}}</span>
                                    <span ng-if="selectAssemLineWorkCentersVm.workCenters.last == true">{{selectAssemLineWorkCentersVm.workCenters.totalElements}}</span>

                                 <span translate> OF </span>
                                {{selectAssemLineWorkCentersVm.workCenters.totalElements}}<span
                                        translate>AN</span>
                                </span>
                            </medium>
                        </span>
                        <span class="mr10">  Page {{selectAssemLineWorkCentersVm.workCenters.totalElements != 0 ?
                        selectAssemLineWorkCentersVm.workCenters.number+1:0}} <span translate>OF</span> {{selectAssemLineWorkCentersVm.workCenters.totalPages}}</span>
                        <a href="" ng-click="selectAssemLineWorkCentersVm.previousPage()"
                           ng-class="{'disabled': selectAssemLineWorkCentersVm.workCenters.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="selectAssemLineWorkCentersVm.nextPage()"
                           ng-class="{'disabled': selectAssemLineWorkCentersVm.workCenters.last}"><i
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
                               ng-if="selectAssemLineWorkCentersVm.workCenters.content.length > 0"
                               ng-model="selectAssemLineWorkCentersVm.selectAllCheck"
                               ng-click="selectAssemLineWorkCentersVm.checkAll(check);" ng-checked="check">
                    </th>
                    <th style="width: 150px" translate>NUMBER</th>
                    <th style="width: 180px;" translate>TYPE</th>
                    <th style="width: 150px;" translate>NAME</th>
                    <th style="width: 150px;" translate>PLANT</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="selectAssemLineWorkCentersVm.workCenters.content.length == 0">
                    <td colspan="15" translate>NO_WORK_CENTER</td>
                </tr>
                <tr ng-if="selectAssemLineWorkCentersVm.workCenters.content.length > 0"
                    ng-repeat="workCenter in selectAssemLineWorkCentersVm.workCenters.content">
                    <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                        <input name="workCenter" type="checkbox" ng-model="workCenter.selected"
                               ng-click="selectAssemLineWorkCentersVm.select(workCenter)">
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="workCenter.number | highlightText: selectAssemLineWorkCentersVm.filters.number"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="workCenter.type.name | highlightText: selectAssemLineWorkCentersVm.filters.typeName"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="workCenter.name | highlightText: selectAssemLineWorkCentersVm.filters.name"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="workCenter.plantName"></span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>


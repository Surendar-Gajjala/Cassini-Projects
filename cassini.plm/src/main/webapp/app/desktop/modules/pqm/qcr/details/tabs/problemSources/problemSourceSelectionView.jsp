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
                            <quality-type-tree ng-if="qcrFor == 'PR'"
                                               on-select-type="problemSourcesSelectionVm.onSelectType"
                                               quality-type="PROBLEMREPORTTYPE"></quality-type-tree>
                            <quality-type-tree ng-if="qcrFor == 'NCR'"
                                               on-select-type="problemSourcesSelectionVm.onSelectType"
                                               quality-type="NCRTYPE"></quality-type-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       style="background-color: #fff;" ng-model="problemSourcesSelectionVm.selectedType.name"
                       readonly="">

            </div>
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="problemSourcesSelectionVm.filters.field1"
                   ng-change="problemSourcesSelectionVm.searchFilterItem()"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;" placeholder="Number"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="problemSourcesSelectionVm.filters.field2"
                   ng-change="problemSourcesSelectionVm.searchFilterItem()"
                   style="margin-left: -16px; height: 30px; margin-top: 0px; width: 100%;"
                   placeholder="{{field2Title}}" class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">

            <!-- ngIf: problemSourcesSelectionVm.clear == true -->
            <button ng-click="problemSourcesSelectionVm.clearFilter()" translate="" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="problemSourcesSelectionVm.clear">Clear
            </button>
            <!-- end ngIf: problemSourcesSelectionVm.clear == true -->
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
                <div style="">
                    <span style="" translate>SELECTED_PROBLEM_SOURCES</span>
                    <span class="badge">{{problemSourcesSelectionVm.selectedItems.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                             <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="problemSourcesSelectionVm.problemSources.numberOfElements ==0">
                            {{(problemSourcesSelectionVm.pageable.page*problemSourcesSelectionVm.pageable.size)}}
                        </span>

                                    <span ng-if="problemSourcesSelectionVm.problemSources.numberOfElements > 0">
                            {{(problemSourcesSelectionVm.pageable.page*problemSourcesSelectionVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="problemSourcesSelectionVm.problemSources.last ==false">{{((problemSourcesSelectionVm.pageable.page+1)*problemSourcesSelectionVm.pageable.size)}}</span>
                                    <span ng-if="problemSourcesSelectionVm.problemSources.last == true">{{problemSourcesSelectionVm.problemSources.totalElements}}</span>

                                 <span translate> OF </span>
                                {{problemSourcesSelectionVm.problemSources.totalElements}}<span
                                        translate>AN</span>
                                </span>
                             </medium>
                        </span>
                        <span class="mr10">  Page {{problemSourcesSelectionVm.problemSources.totalElements != 0 ?
                        problemSourcesSelectionVm.problemSources.number+1:0}} <span translate>OF</span> {{problemSourcesSelectionVm.problemSources.totalPages}}</span>
                        <a href="" ng-click="problemSourcesSelectionVm.previousPage()"
                           ng-class="{'disabled': problemSourcesSelectionVm.problemSources.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="problemSourcesSelectionVm.nextPage()"
                           ng-class="{'disabled': problemSourcesSelectionVm.problemSources.last}"><i
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
                        <input name="item" type="checkbox" ng-model="problemSourcesSelectionVm.selectAllCheck"
                               ng-if="problemSourcesSelectionVm.problemSources.content.length != 0"
                               ng-click="problemSourcesSelectionVm.selectAll(check);" ng-checked="check">
                    </th>
                    <th style="width: 150px">
                        <span ng-if="qcrFor == 'PR'" translate>PR_NUMBER</span>
                        <span ng-if="qcrFor == 'NCR'" translate>NCR_NUMBER</span>
                    </th>
                    <th style="width: 180px;" translate>TYPE</th>
                    <th style="width: 150px;">
                        <span ng-if="qcrFor == 'PR'" translate>PRODUCT</span>
                        <span ng-if="qcrFor == 'NCR'" translate>TITLE</span>
                    </th>
                    <th style="width: 200px;">
                        <span ng-if="qcrFor == 'PR'" translate>PROBLEM</span>
                        <span ng-if="qcrFor == 'NCR'" translate>DESCRIPTION</span>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="problemSourcesSelectionVm.problemSources.content.length == 0">
                    <td colspan="15" translate>NO_ITEMS</td>
                </tr>
                <tr ng-if="problemSourcesSelectionVm.problemSources.content.length > 0"
                    ng-repeat="source in problemSourcesSelectionVm.problemSources.content">
                    <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                        <input name="item" type="checkbox" ng-model="source.selected"
                               ng-click="problemSourcesSelectionVm.selectCheck(source)">
                    </td>

                    <td style="vertical-align: middle;">
                        <span ng-if="qcrFor == 'PR'"
                              ng-bind-html="source.prNumber | highlightText: problemSourcesSelectionVm.prFilter.prNumber"></span>
                        <span ng-if="qcrFor == 'NCR'"
                              ng-bind-html="source.ncrNumber | highlightText: problemSourcesSelectionVm.ncrFilter.ncrNumber"></span>
                    </td>
                    <td style="vertical-align: middle;" title="{{}}">
                        <span ng-if="qcrFor == 'PR'"
                              ng-bind-html="source.prType | highlightText: problemSourcesSelectionVm.prFilter.prTypeName"
                                >{{source.prType }}</span>
                        <span ng-if="qcrFor == 'NCR'"
                              ng-bind-html="source.ncrType | highlightText: problemSourcesSelectionVm.ncrFilter.ncrTypeName"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-if="qcrFor == 'PR'"
                              ng-bind-html="source.product | highlightText: problemSourcesSelectionVm.prFilter.product"></span>
                        <span ng-if="qcrFor == 'NCR'"
                              ng-bind-html="source.title | highlightText: problemSourcesSelectionVm.ncrFilter.title"></span>
                    </td>
                    <td style="vertical-align: middle;">

                        <span ng-if="qcrFor == 'PR'"
                              ng-bind-html="source.problem | highlightText: problemSourcesSelectionVm.prFilter.problem"></span>
                        <span ng-if="qcrFor == 'NCR'">{{source.description}}</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>

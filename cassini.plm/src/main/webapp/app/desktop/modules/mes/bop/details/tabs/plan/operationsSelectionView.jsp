<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    #rightSidePanelContent {
        overflow: hidden !important;
    }

    .item-selection-table table th {
        position: -webkit-sticky;
        position: sticky;
        top: -1px !important;
        z-index: 5 !important;
    }

    .table-div {
        position: absolute;
        top: 145px !important;
        bottom: 50px !important;
        left: 0;
        right: 0;
    }

    .open > .dropdown-toggle.btn {
        color: #091007 !important;
    }
</style>
<div style="padding: 0px 10px;">
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
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                            <manufacturing-type-tree on-select-type="operationsSelectionVm.onSelectType"
                                                     object-type="OPERATIONTYPE">
                            </manufacturing-type-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       style="background-color: #fff;" ng-model="operationsSelectionVm.operationsFilters.typeName"
                       readonly="">
            </div>
        </div>

        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="operationsSelectionVm.operationsFilters.number"
                   ng-change="operationsSelectionVm.searchFilterOperations()"
                   ng-enter="operationsSelectionVm.searchFilterOperations()"
                   ng-model-options="{ debounce: 500 }"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                   placeholder="{{'OPERATION_NUMBER_TITLE' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="operationsSelectionVm.operationsFilters.name"
                   ng-change="operationsSelectionVm.searchFilterOperations()"
                   ng-enter="operationsSelectionVm.searchFilterOperations()" ng-model-options="{ debounce: 500 }"
                   style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                   placeholder="{{'OPERATION_NAME_PLACEHOLDER_TITLE' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">
            <button ng-click="operationsSelectionVm.clearFilter()" translate="" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="operationsSelectionVm.clear">Clear
            </button>
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-5" style="padding-left: 0;padding-top: 10px;">
                <div style="" ng-if="itemsSelectionVm.selectionMode != 'substituteItem'">
                    <span style="" translate>SELECTED_OPERATIONS</span>
                    <span class="badge">{{operationsSelectionVm.selectedOperations.length}}</span>
                    <a href="" ng-click="operationsSelectionVm.clearSelection()"
                       ng-if="operationsSelectionVm.selectedOperations.length > 0">Clear</a>
                </div>
            </div>
            <div class="col-md-7">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="operationsSelectionVm.operations.numberOfElements ==0">
                            {{(operationsSelectionVm.pageable.page*operationsSelectionVm.pageable.size)}}
                        </span>

                                    <span ng-if="operationsSelectionVm.operations.numberOfElements > 0">
                            {{(operationsSelectionVm.pageable.page*operationsSelectionVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="operationsSelectionVm.operations.last ==false">{{((operationsSelectionVm.pageable.page+1)*operationsSelectionVm.pageable.size)}}</span>
                                    <span ng-if="operationsSelectionVm.operations.last == true">{{operationsSelectionVm.operations.totalElements}}</span>

                                 <span translate> OF </span>
                                {{operationsSelectionVm.operations.totalElements}}<span
                                        translate>AN</span>
                                </span>
                            </medium>
                        </span>
                        <span class="mr10">  Page {{operationsSelectionVm.operations.totalElements != 0 ?
                        operationsSelectionVm.operations.number+1:0}} <span translate>OF</span> {{operationsSelectionVm.operations.totalPages}}</span>
                        <a href="" ng-click="operationsSelectionVm.previousPage()"
                           ng-class="{'disabled': operationsSelectionVm.operations.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="operationsSelectionVm.nextPage()"
                           ng-class="{'disabled': operationsSelectionVm.operations.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="col-md-12 table-div" style="padding:0px; height: auto;overflow: auto;">
    <div class="operation-selection-table">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="text-align: center;width: 20px !important;">
                    <input name="operation"
                           type="checkbox" ng-model="operationsSelectionVm.selectAllCheck"
                           ng-click="operationsSelectionVm.selectAll(check)" ng-checked="check">
                </th>
                <th translate>NUMBER</th>
                <th translate>TYPE</th>
                <th translate>NAME</th>
                <!-- <th translate>WORKCENTER</th> -->
                <th translate>DESCRIPTION</th>

            </tr>
            </thead>
            <tbody>
            <tr ng-if="operationsSelectionVm.loading == true">
                <td colspan="25">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>LOADING_OPERATIONS</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="operationsSelectionVm.loading == false && operationsSelectionVm.operations.content.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/MBOM.png" alt="" class="image">

                        <div class="message" translate>NO_OPERATIONS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="operation in operationsSelectionVm.operations.content">
                <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                    <input name="operation" type="checkbox"
                           ng-model="operation.selected"
                           ng-click="operationsSelectionVm.selectCheck(operation)">
                </td>
                <td class="col-width-100">
                    <span ng-bind-html="operation.number | highlightText: operationsSelectionVm.operationsFilters.number"></span>
                </td>

                <td class="col-width-150">
                    <span ng-bind-html="operation.type.name | highlightText: operationsSelectionVm.operationsFilters.typeName"></span>
                </td>
                <td class="col-width-150" title="{{operation.name}}">
                    <span ng-bind-html="operation.name | highlightText: operationsSelectionVm.operationsFilters.name"></span>
                </td>
                <!-- <td class="col-width-150">{{operation.workCenterName}}</td> -->
                <td class="col-width-150" title="{{operation.description}}">
                    <span ng-bind-html="operation.description  | highlightText: freeTextQuery"></span>
                </td>
            </tr>

            </tbody>
        </table>
    </div>
</div>
</div>
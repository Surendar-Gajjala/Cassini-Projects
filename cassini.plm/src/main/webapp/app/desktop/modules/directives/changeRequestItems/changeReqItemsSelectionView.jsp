<div style="padding: 0 10px;">
    <style scoped>
        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        tr.itemDisable {
            cursor: not-allowed;
            background-color: orange;
        }

        .ui-select-bootstrap .ui-select-match-text span {
            vertical-align: bottom;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }

        .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
            position: absolute !important;
        }
    </style>
    <%--<h4 class="section-title" style="margin: 0px;" translate>FILTERS</h4>--%>

    <form class="form-inline ng-pristine ng-valid" style="margin: 10px 0px;display: flex;flex-direction: row;">
        <div class="form-group" style="flex-grow: 1;margin-right: 0;">
            <div ng-show="type == 'ECO'" class="input-group input-group-sm" style="">
                <div class="input-group-btn dropdown" uib-dropdown="" style="">
                    <button uib-dropdown-toggle="" class="btn btn-default dropdown-toggle" type="button"
                            aria-haspopup="true" aria-expanded="false"
                            style="background-color: #fff;color: #000000 !important;">
                        <span class="ng-scope" translate>SELECT</span> <span class="caret"
                                                                             style="margin-left: 4px;"></span>
                    </button>
                    <div class="dropdown-menu" role="menu">
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                            <change-type-tree on-select-type="changeItemSelectionVm.onSelectType"
                                              change-type="ECR"></change-type-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       style="background-color: #fff;" ng-model="changeItemSelectionVm.selectedType.name"
                       readonly="">

            </div>

            <div ng-show="type == 'DCO'" class="input-group input-group-sm" style="">
                <div class="input-group-btn dropdown" uib-dropdown="" style="">
                    <button uib-dropdown-toggle="" class="btn btn-default dropdown-toggle" type="button"
                            aria-haspopup="true" aria-expanded="false"
                            style="background-color: #fff;color:black !important;">
                        <span class="ng-scope" translate>SELECT</span> <span class="caret"
                                                                             style="margin-left: 4px;"></span>
                    </button>
                    <div class="dropdown-menu" role="menu">
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                            <dcr-type-tree on-select-type="changeItemSelectionVm.onSelectDcrType"
                                           change-type="DCR"></dcr-type-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       style="background-color: #fff;" ng-model="changeItemSelectionVm.selectedType.name"
                       readonly="">

            </div>
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="changeItemSelectionVm.itemsFilters.crNumber"
                   ng-change="changeItemSelectionVm.searchFilterItem()"
                   style="width: 100%;height: 30px;margin-top: 0;" placeholder="Number"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="changeItemSelectionVm.itemsFilters.title"
                   ng-change="changeItemSelectionVm.searchFilterItem()"
                   style="margin-left: -8px; height: 30px; margin-top: 0px; width: 95%;"
                   placeholder="Title" class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">

            <!-- ngIf: changeItemSelectionVm.clear == true -->
            <button ng-click="changeItemSelectionVm.clearFilter()" translate="" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="changeItemSelectionVm.clear">Clear
            </button>
            <!-- end ngIf: changeItemSelectionVm.clear == true -->
        </div>
    </form>
    <div class="col-md-12" id="page-footer" style="border: 1px solid lightgrey;">
        <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
            <div style="">
                <span style="" translate>SELECTED_ITEMS</span>
                <span class="badge">{{changeItemSelectionVm.selectedItems.length}}</span>
            </div>
        </div>
        <div class="col-md-8">
            <div class="pull-right text-center" style="padding: 10px;">
                <div>
                        <span>
                             <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="changeItemSelectionVm.items.numberOfElements ==0">
                            {{(changeItemSelectionVm.pageable.page*changeItemSelectionVm.pageable.size)}}
                        </span>

                                    <span ng-if="changeItemSelectionVm.items.numberOfElements > 0">
                            {{(changeItemSelectionVm.pageable.page*changeItemSelectionVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="changeItemSelectionVm.items.last ==false">{{((changeItemSelectionVm.pageable.page+1)*changeItemSelectionVm.pageable.size)}}</span>
                                    <span ng-if="changeItemSelectionVm.items.last == true">{{changeItemSelectionVm.items.totalElements}}</span>

                                 <span translate> OF </span>
                                {{changeItemSelectionVm.items.totalElements}}<span
                                        translate>AN</span>
                                </span>
                             </medium>
                        </span>
                        <span class="mr10">  Page {{changeItemSelectionVm.items.totalElements != 0 ?
                        changeItemSelectionVm.items.number+1:0}} <span translate>OF</span> {{changeItemSelectionVm.items.totalPages}}</span>
                    <a href="" ng-click="changeItemSelectionVm.previousPage()"
                       ng-class="{'disabled': changeItemSelectionVm.items.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="changeItemSelectionVm.nextPage()"
                       ng-class="{'disabled': changeItemSelectionVm.items.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
    <div id="ecr-selection" class="responsive-table" style="overflow: auto;">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="text-align: center;width: 20px !important;">
                    <input name="item" type="checkbox" ng-model="changeItemSelectionVm.selectAllCheck"
                           ng-if="changeItemSelectionVm.items.content.length != 0"
                           ng-click="changeItemSelectionVm.selectAll(check);" ng-checked="check">
                </th>
                <th style="width: 1% !important;white-space: nowrap;" translate>NUMBER</th>
                <th style="width: 130px;" translate>Title</th>
                <th style="width: 80px;text-align: center;" translate>From Rev</th>
                <th style="width: 110px" translate>From LifeCycle</th>
                <th style="width: 120px;text-align: center;" translate>To Rev</th>
                <th style="width: 110px" translate>To LifeCycle</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="changeItemSelectionVm.items.content.length == 0">
                <td colspan="15" translate>NO_ITEMS</td>
            </tr>
            <tr ng-if="changeItemSelectionVm.items.content.length > 0" style="font-weight: bold"
                ng-repeat-start="item in changeItemSelectionVm.items.content">
                <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                    <input name="item" type="checkbox" ng-model="item.selected"
                           ng-click="changeItemSelectionVm.selectCheck(item)">
                </td>

                <td style="width: 1% !important;white-space: nowrap;">
                    <span ng-bind-html="item.crNumber | highlightText: changeItemSelectionVm.itemsFilters.crNumber"></span>
                </td>
                <td style="vertical-align: middle;width: 130px;">
                    <span ng-bind-html="item.title | highlightText: changeItemSelectionVm.itemsFilters.title"></span>
                </td>
                <td style="width: 80px;"></td>
                <td style="width: 110px;"></td>
                <td style="width: 120px;"></td>
                <td style="width: 120px;"></td>
            </tr>
            <tr ng-if="item.showItems" ng-repeat-end="">
                <td colspan="7">
                    <table style="width: 100%;">
                        <tbody>
                        <tr ng-repeat="requestedItemDto in item.requestedItemDtos"
                            ng-class="{'itemDisable': requestedItemDto.itemExist == true}">
                            <td style="width: 20px"></td>
                            <td style="width: 1% !important;white-space: nowrap;">{{requestedItemDto.number}}</td>
                            <td style="width: 130px" title="{{requestedItemDto.name}}">
                                {{requestedItemDto.name}}
                            </td>
                            <td style="width: 80px;text-align: center">{{requestedItemDto.fromRevision}}</td>
                            <td style="width: 110px">{{requestedItemDto.fromLifeCycle.phase}}</td>
                            <td style="width: 120px;text-align: center">
                                <span ng-if="requestedItemDto.toRevisions.length == 1">{{requestedItemDto.toRevision}}</span>
                                <ui-select ng-model="requestedItemDto.toRevision" theme="bootstrap"
                                           style="width:100%" ng-if="requestedItemDto.toRevisions.length > 1"
                                           on-select="changeItemSelectionVm.selectRevision(requestedItemDto,$item)">
                                    <ui-select-match placeholder="Select Revision">{{$select.selected}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="revision in requestedItemDto.toRevisions | filter: $select.search">
                                        <div ng-bind="revision"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </td>
                            <td style="width: 120px">
                                <ui-select ng-model="requestedItemDto.toLifeCycle" theme="bootstrap"
                                           style="width:100%"
                                           on-select="changeItemSelectionVm.selectPhases(requestedItemDto,$item)">
                                    <ui-select-match placeholder="Select">{{$select.selected.phase}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="phase in requestedItemDto.toLifeCyclePhases | filter: $select.search">
                                        <div ng-bind="phase.phase"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

<div style="padding: 0px 10px;">
    <style scoped>
        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        .open > .dropdown-toggle.btn {
            color: #091007 !important;
        }
    </style>
    <%--<h4 class="section-title" style="margin: 0px;" translate>FILTERS</h4>--%>

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
                            <manufacturer-part-tree
                                    on-select-type="mfrItemVm.onSelectType" class="ng-isolate-scope">
                                <%--<div class="classification-pane">
                                </div>--%>
                            </manufacturer-part-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       style="background-color: #fff;" ng-model="mfrItemVm.selectedType.name" readonly="">

            </div>
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="mfrItemVm.filters.partNumber" ng-change="mfrItemVm.searchParts()"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                   placeholder="{{mfrItemVm.partNumber}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="mfrItemVm.filters.partName" ng-change="mfrItemVm.searchParts()"
                   style="margin-left: -16px; height: 30px; margin-top: 0px; width: 100%;margin-top: 0;"
                   placeholder="{{mfrItemVm.partName}}" class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">

            <!-- ngIf: mfrItemVm.clear == true -->
            <button ng-click="mfrItemVm.clearFilter()" translate="" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="mfrItemVm.clear">Clear
            </button>
            <!-- end ngIf: mfrItemVm.clear == true -->
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
                <div style="">
                    <span style="" translate>SELECTED_PARTS</span>
                    <span class="badge">{{mfrItemVm.selectedItems.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="mfrItemVm.parts.numberOfElements ==0">
                            {{(mfrItemVm.pageable.page*mfrItemVm.pageable.size)}}
                        </span>

                                    <span ng-if="mfrItemVm.parts.numberOfElements > 0">
                            {{(mfrItemVm.pageable.page*mfrItemVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="mfrItemVm.parts.last ==false">{{((mfrItemVm.pageable.page+1)*mfrItemVm.pageable.size)}}</span>
                                    <span ng-if="mfrItemVm.parts.last == true">{{mfrItemVm.parts.totalElements}}</span>

                                 <span translate> OF </span>
                                {{mfrItemVm.parts.totalElements}}<span
                                        translate>AN</span>
                                </span>
                            </medium>
                        </span>
                        <span class="mr10">  Page {{mfrItemVm.parts.totalElements != 0 ?
                        mfrItemVm.parts.number+1:0}} <span translate>OF</span> {{mfrItemVm.parts.totalPages}}</span>
                        <a href="" ng-click="mfrItemVm.previousPage()"
                           ng-class="{'disabled': mfrItemVm.parts.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="mfrItemVm.nextPage()"
                           ng-class="{'disabled': mfrItemVm.parts.last}"><i
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
                               ng-model="mfrItemVm.selectAllCheck"
                               ng-if="!mfrItemVm.replacementType && mfrItemVm.parts.content.length != 0"
                               ng-click="mfrItemVm.checkAll(check);" ng-checked="check">
                        <span ng-if="mfrItemVm.replacementType" translate>SELECT</span>
                    </th>
                    <th style="width: 150px" translate>PART_NUMBER</th>
                    <th style="width: 180px;" translate>MANUFACTURER_PART_TYPE</th>
                    <th style="width: 150px;" translate>PART_NAME</th>
                    <th style="width: 200px;" translate>MANUFACTURER</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="mfrItemVm.parts.content.length == 0">
                    <td colspan="15" translate>NO_MFR_PARTS</td>
                </tr>
                <tr ng-if="mfrItemVm.parts.content.length > 0"
                    ng-repeat="mfrPart in mfrItemVm.parts.content">
                    <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                        <input name="mfrPart" type="checkbox" ng-model="mfrPart.selected"
                               ng-if="!mfrItemVm.replacementType"
                               ng-click="mfrItemVm.select(mfrPart)">
                        <input name="mfrPart" type="radio" value="false" ng-model="mfrPart.checked"
                               ng-if="mfrItemVm.replacementType"
                               ng-click="mfrItemVm.selectReplacementPart(mfrPart)">
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="mfrPart.partNumber | highlightText: mfrItemVm.filters.partNumber"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="mfrPart.mfrPartType.name | highlightText: mfrItemVm.filters.typeName"></span>
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-bind-html="mfrPart.partName | highlightText: mfrItemVm.filters.partName"></span>
                    </td>
                    </td>
                    <td style="vertical-align: middle;">
                        {{mfrPart.manufacturerObject.name}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>


<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .tooltip {
        position: relative;
        display: inline-block;
        border-bottom: 1px dotted black;
        /* If you want dots under the hoverable text */
    }

    .pending-eco-tooltip {
        position: relative;
        display: inline-block;
    }

    .pending-eco-tooltip .pending-eco-tooltip-text {
        visibility: hidden;
        width: 300px;
        background-color: white;
        text-align: left;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        left: 100%;
        opacity: 0;
        transition: opacity 1s;
        padding: 2px;
        border: 1px solid black;
    }

    .pending-eco-tooltip:hover .pending-eco-tooltip-text {
        visibility: visible;
        opacity: 1;
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
                            <manufacturing-type-tree on-select-type="mbomSelectionVm.onSelectType"
                                                     object-type="MBOMTYPE">
                            </manufacturing-type-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                       style="background-color: #fff;" ng-model="mbomSelectionVm.mbomsFilters.typeName" readonly="">
            </div>
        </div>

        <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
            <input type="text" ng-model="mbomSelectionVm.mbomsFilters.number"
                   ng-change="mbomSelectionVm.searchFilterMbom()" ng-enter="mbomSelectionVm.searchFilterMbom()"
                   ng-model-options="{ debounce: 500 }"
                   style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                   placeholder="{{'MBOM_NUMBER_TITLE' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
        </div>
        <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
            <input type="text" ng-model="mbomSelectionVm.mbomsFilters.name"
                   ng-change="mbomSelectionVm.searchFilterMbom()"
                   ng-enter="mbomSelectionVm.searchFilterMbom()" ng-model-options="{ debounce: 500 }"
                   style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                   placeholder="{{'MBOM_NAME_PLACEHOLDER_TITLE' | translate}}"
                   class="input-sm form-control ng-pristine ng-valid ng-touched">
        </div>
        <div style="margin-top: 0;flex-grow: 1;" class="">
            <button ng-click="mbomSelectionVm.clearFilter()" translate="" style="height: 29px;width: 100%"
                    class="btn btn-xs btn-danger ng-scope" ng-if="mbomSelectionVm.clear">Clear
            </button>
        </div>
    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4" style="padding-left: 0;padding-top: 10px;">
                <div style="" ng-if="itemsSelectionVm.selectionMode != 'substituteItem'">
                    <span style="" translate>SELECTED_MBOMS</span>
                    <span class="badge">{{mbomSelectionVm.selectedMboms.length}}</span>
                    <a href="" ng-click="mbomSelectionVm.clearSelection()"
                       ng-if="mbomSelectionVm.selectedMboms.length > 0">Clear</a>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="mbomSelectionVm.mboms.numberOfElements ==0">
                            {{(mbomSelectionVm.pageable.page*mbomSelectionVm.pageable.size)}}
                        </span>

                                    <span ng-if="mbomSelectionVm.mboms.numberOfElements > 0">
                            {{(mbomSelectionVm.pageable.page*mbomSelectionVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="mbomSelectionVm.mboms.last ==false">{{((mbomSelectionVm.pageable.page+1)*mbomSelectionVm.pageable.size)}}</span>
                                    <span ng-if="mbomSelectionVm.mboms.last == true">{{mbomSelectionVm.mboms.totalElements}}</span>

                                 <span translate> OF </span>
                                {{mbomSelectionVm.mboms.totalElements}}<span
                                        translate>AN</span>
                                </span>
                            </medium>
                        </span>
                        <span class="mr10">  Page {{mbomSelectionVm.mboms.totalElements != 0 ?
                        mbomSelectionVm.mboms.number+1:0}} <span translate>OF</span> {{mbomSelectionVm.mboms.totalPages}}</span>
                        <a href="" ng-click="mbomSelectionVm.previousPage()"
                           ng-class="{'disabled': mbomSelectionVm.mboms.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="mbomSelectionVm.nextPage()"
                           ng-class="{'disabled': mbomSelectionVm.mboms.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="col-md-12 table-div" style="padding:0px; height: auto;overflow: auto;">
    <div class="mbom-selection-table">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="text-align: center;width: 20px !important;">
                    <input ng-if=" mbomSelectionVm.selectionMode != 'substituteItem' && mbomSelectionVm.mboms.content.length != 0 "
                           name="mbom"
                           type="checkbox" ng-model="mbomSelectionVm.selectAllCheck"
                           ng-click="mbomSelectionVm.selectAll(check);" ng-checked="check">
                </th>
                <th translate>NUMBER</th>
                <th translate>TYPE</th>
                <th translate>NAME</th>
                <th translate>ITEM</th>
                <th translate>DESCRIPTION</th>
                <th translate>REVISION</th>
                <th translate>LIFECYCLE</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="mbomSelectionVm.loading == true">
                <td colspan="25">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>LOADING_MBOM</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="mbomSelectionVm.loading == false && mbomSelectionVm.mboms.content.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/MBOM.png" alt="" class="image">

                        <div class="message" translate>NO_MBOM</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="mbom in mbomSelectionVm.mboms.content">
                <td style="vertical-align: middle;text-align: center;width: 20px !important;"
                    class="pending-eco-tooltip">
                    <input name="mbom" type="checkbox"
                           ng-model="mbom.selected" ng-disabled="mbom.pendingMco"
                           ng-click="mbomSelectionVm.selectCheck(mbom)">

                    <div class="pending-eco-tooltip-text" ng-if="mbom.pendingMco">
                            <span>{{itemHasPendingChangeOrder}} :
                                <a href="" title="{{clickToShowDetails}}"
                                   ui-sref="app.changes.mco.details({mcoId:mbom.mco,tab:'details.affectedItems'})">{{mbom.mcoNumber}}</a>
                            </span>
                    </div>
                </td>
                <td style="vertical-align: middle;width: 1% !important;white-space: nowrap;">
                    <span ng-bind-html="mbom.number | highlightText: mbomSelectionVm.mbomsFilters.number"></span>
                </td>
                <td style="vertical-align: middle;">
                    <span ng-bind-html="mbom.typeName | highlightText: mbomSelectionVm.mbomsFilters.typeName"></span>
                </td>
                <td style="vertical-align: middle;" class="col-width-150">
                    <span ng-bind-html="mbom.name | highlightText: mbomSelectionVm.mbomsFilters.name"></span>
                </td>
                <td class="col-width-150">{{mbom.itemName}} - {{mbom.itemRevision}}</td>
                <td style="vertical-align: middle;" class="col-width-150" title="{{mbom.description}}">
                    <span ng-bind-html="mbom.description | highlightText: mbomSelectionVm.mbomsFilters.description"></span>
                </td>
                <td style="text-align: center" class="col-width-75">
                    {{mbom.revision}}
                </td>
                <td class="col-width-75">
                    <item-status item="mbom"></item-status>
                </td>
            </tr>

            </tbody>
        </table>
    </div>
</div>
</div>
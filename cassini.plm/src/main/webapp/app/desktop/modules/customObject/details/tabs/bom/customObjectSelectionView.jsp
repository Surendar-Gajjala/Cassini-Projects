<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .tooltip {
        position: relative;
        display: inline-block;
        border-bottom: 1px dotted black; /* If you want dots under the hoverable text */
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
<div style="padding: 0 10px;">
    <%--<h4 class="section-title" style="margin: 0px;" translate>FILTERS</h4>--%>

    <form class="form-inline" style="margin: 5px 0px">
        <div class="form-group">
            <div class="input-group">
                <div class="input-group-btn" uib-dropdown>
                    <div class="dropdown-menu" role="menu">
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                            <custom-type-tree
                                    on-select-type="customObjectSelectionVm.onSelectType"></custom-type-tree>
                        </div>
                    </div>
                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                        <span translate>SELECT</span> <span class="caret" style="margin-left: 4px;"></span>
                    </button>
                </div>
                <input type="text" class="form-control" name="title"
                       style="width: 140px !important;height: 41px !important;"
                       ng-model="customObjectSelectionVm.customFilter.typeName" readonly>

            </div>
        </div>


        <div class="form-group">
            <input type="text" ng-model="customObjectSelectionVm.customFilter.number" style="width: 165px;"
                   ng-change="customObjectSelectionVm.searchItems()"
                   placeholder="{{'NUMBER' | translate}}"
                   class="form-control">
        </div>
        <div class="form-group">
            <input type="text" ng-model="customObjectSelectionVm.customFilter.name"
                   ng-change="customObjectSelectionVm.searchItems()"
                   style="width: 165px;"
                   placeholder="{{'NAME' | translate}}"
                   class="form-control">
        </div>

        <div class="pull-right" style="margin-top: 5px;">
            <button class="btn btn-sm btn-danger" ng-if="customObjectSelectionVm.clear == true"
                    ng-click="customObjectSelectionVm.clearFilter()" translate>CLEAR
            </button>
        </div>

    </form>
    <hr style="margin: 0px;">
    <div class="row">

        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4">
                <div style="padding: 10px;">
                    <span style="color:#1877f2e6" translate>SELECTED_OBJECTS</span>
                    <span class="badge">{{customObjectSelectionVm.selectedItems.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="customObjectSelectionVm.customObjects.numberOfElements ==0">
                            {{(customObjectSelectionVm.pageable.page*customObjectSelectionVm.pageable.size)}}
                        </span>

                                    <span ng-if="customObjectSelectionVm.customObjects.numberOfElements > 0">
                            {{(customObjectSelectionVm.pageable.page*customObjectSelectionVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="customObjectSelectionVm.customObjects.last ==false">{{((customObjectSelectionVm.pageable.page+1)*customObjectSelectionVm.pageable.size)}}</span>
                                    <span ng-if="customObjectSelectionVm.customObjects.last == true">{{customObjectSelectionVm.customObjects.totalElements}}</span>

                                 <span translate> OF </span>
                                {{customObjectSelectionVm.customObjects.totalElements}}<span
                                        translate>AN</span>
                                </span>
                            </medium>
                        </span>
                        <span class="mr10"> Page  {{customObjectSelectionVm.customObjects.totalElements != 0 ? customObjectSelectionVm.customObjects.number+1:0}} <span
                                translate> OF </span> {{customObjectSelectionVm.customObjects.totalPages}} </span>
                        <a href="" ng-click="customObjectSelectionVm.previousPage()"
                           ng-class="{'disabled': customObjectSelectionVm.customObjects.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="customObjectSelectionVm.nextPage()"
                           ng-class="{'disabled': customObjectSelectionVm.customObjects.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-12 table-div" style="padding:0px; height: auto;overflow: auto;">
            <div class="item-selection-table">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th style="vertical-align: top;text-align: left">
                            <input name="itemSelected" ng-value="true" type="checkbox"
                                   ng-model="customObjectSelectionVm.selectAllCheck"
                                   ng-if="customObjectSelectionVm.customObjects.content.length != 0"
                                   ng-click="customObjectSelectionVm.selectAll()" style="margin-left: 16px">
                        </th>
                        <th class="col-width-100" translate>NUMBER</th>
                        <th class="col-width-150" translate>NAME</th>
                        <th class="col-width-150" translate>TYPE</th>
                        <th class="col-width-250" translate>DESCRIPTION</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="customObjectSelectionVm.customObjects.content.length == 0">
                        <td colspan="15" translate>NO_OBJECTS</td>
                    </tr>
                    <tr ng-if="customObjectSelectionVm.customObjects.content.length > 0"
                        ng-repeat="customObject in customObjectSelectionVm.customObjects.content">
                        <th style="vertical-align: top; text-align: left">
                            <input  name="item" type="checkbox" ng-model="customObject.selected"
                                   ng-click="customObjectSelectionVm.selectCheck(customObject)" style="margin-left: 16px">
                        </th>

                        <td class="col-width-100">
                            <a href="" ng-click="customObjectSelectionVm.showCustomObject(bomObject)"
                               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                <span ng-bind-html="customObject.number | highlightText: freeTextQuery"></span>
                            </a>
                        </td>
                        <td class="col-width-150">
                            <span ng-bind-html="customObject.name | highlightText: freeTextQuery"></span>
                        </td>
                        <td class="col-width-150">
                            <span ng-bind-html="customObject.type.name | highlightText: freeTextQuery"></span></td>
                        <td class="col-width-250">
                            <span ng-bind-html="customObject.description | highlightText: freeTextQuery"></span></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <br>
    <br>
</div>

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
                            <project-management-type-tree
                                    on-select-type="reqDocumentsSelectionVm.onSelectType"
                                    object-type="REQUIREMENTDOCUMENTTYPE"></project-management-type-tree>
                        </div>
                    </div>
                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                        <span translate>SELECT</span> <span class="caret" style="margin-left: 4px;"></span>
                    </button>
                </div>
                <input type="text" class="form-control" name="title"
                       style="width: 140px !important;height: 41px !important;"
                       ng-model="reqDocumentsSelectionVm.filters.reqType.name" readonly>

            </div>
        </div>


        <div class="form-group">
            <input type="text" ng-model="reqDocumentsSelectionVm.filters.number" style="width: 165px;"
                   ng-change="reqDocumentsSelectionVm.searchItems()"
                   placeholder="{{'NUMBER' | translate}}"
                   class="form-control">
        </div>
        <div class="form-group">
            <input type="text" ng-model="reqDocumentsSelectionVm.filters.name"
                   ng-change="reqDocumentsSelectionVm.searchItems()"
                   style="width: 165px;"
                   placeholder="{{'NAME' | translate}}"
                   class="form-control">
        </div>

        <div class="pull-right" style="margin-top: 5px;">
            <button class="btn btn-sm btn-danger" ng-if="reqDocumentsSelectionVm.clear == true"
                    ng-click="reqDocumentsSelectionVm.clearFilter()" translate>CLEAR
            </button>
        </div>

    </form>
    <hr style="margin: 0px;">
    <div class="row">

        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4">
                <div style="padding: 10px;">
                    <span style="color:#1877f2e6" translate>SELECTED_ITEMS</span>
                    <span class="badge">{{reqDocumentsSelectionVm.selectedItems.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="reqDocumentsSelectionVm.reqDocuments.numberOfElements ==0">
                            {{(reqDocumentsSelectionVm.pageable.page*reqDocumentsSelectionVm.pageable.size)}}
                        </span>

                                    <span ng-if="reqDocumentsSelectionVm.reqDocuments.numberOfElements > 0">
                            {{(reqDocumentsSelectionVm.pageable.page*reqDocumentsSelectionVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="reqDocumentsSelectionVm.reqDocuments.last ==false">{{((reqDocumentsSelectionVm.pageable.page+1)*reqDocumentsSelectionVm.pageable.size)}}</span>
                                    <span ng-if="reqDocumentsSelectionVm.reqDocuments.last == true">{{reqDocumentsSelectionVm.reqDocuments.totalElements}}</span>

                                 <span translate> OF </span>
                                {{reqDocumentsSelectionVm.reqDocuments.totalElements}}<span
                                        translate>AN</span>
                                </span>
                            </medium>
                        </span>
                        <span class="mr10"> Page  {{reqDocumentsSelectionVm.reqDocuments.totalElements != 0 ? reqDocumentsSelectionVm.reqDocuments.number+1:0}} <span
                                translate> OF </span> {{reqDocumentsSelectionVm.reqDocuments.totalPages}} </span>
                        <a href="" ng-click="reqDocumentsSelectionVm.previousPage()"
                           ng-class="{'disabled': reqDocumentsSelectionVm.reqDocuments.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="reqDocumentsSelectionVm.nextPage()"
                           ng-class="{'disabled': reqDocumentsSelectionVm.reqDocuments.last}"><i
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
                                   ng-model="reqDocumentsSelectionVm.selectAllCheck"
                                   ng-if="reqDocumentsSelectionVm.reqDocuments.content.length != 0"
                                   ng-click="reqDocumentsSelectionVm.selectAll()">
                        </th>
                        <th style="width: 150px" translate>NUMBER</th>
                        <th class="col-width-150" translate>TYPE</th>
                        <th class="col-width-200" translate>NAME</th>
                        <th class="col-width-250" translate>DESCRIPTION</th>
                        <th style="width: 150px; text-align: center;z-index: auto !important;"
                            translate="ITEM_ALL_REVISION"></th>
                        <th style="width: 150px;z-index: auto !important;" translate="ITEM_ALL_LIFECYCLE"></th>
                        <th style="width: 150px" translate>DOCUMENT_OWNER</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="reqDocumentsSelectionVm.reqDocuments.content.length == 0">
                        <td colspan="15" translate>NO_REQ_DOCUMENT</td>
                    </tr>
                    <tr ng-if="reqDocumentsSelectionVm.reqDocuments.content.length > 0"
                        ng-repeat="reqDocument in reqDocumentsSelectionVm.reqDocuments.content">
                        <th style="vertical-align: top; text-align: left">
                            <input name="item" type="checkbox" ng-model="reqDocument.selected"
                                   ng-click="reqDocumentsSelectionVm.selectCheck(reqDocument)">
                        </th>

                        <td style="width: 150px;">
                            
                                <span ng-bind-html="reqDocument.number | highlightText: freeTextQuery"></span>
                          
                        </td>
                        <td class="col-width-200">
                            <span ng-bind-html="reqDocument.type | highlightText: freeTextQuery"></span>
                        </td>
                        <td class="col-width-200">
                            <span ng-bind-html="reqDocument.name | highlightText: freeTextQuery"></span>
                        </td>
                        <td title="{{reqDocument.description}}" class="col-width-250">
                            <span ng-bind-html="reqDocument.description  | highlightText: freeTextQuery"></span>
                        </td>
                        <td style="width: 150px; text-align: center;">
                            {{reqDocument.latestRevisionObject.revision}}
                        </td>
                        <td style="width: 150px">
                            <item-status item="reqDocument.latestRevisionObject"></item-status>
                        </td>
                        <td>{{reqDocument.latestRevisionObject.owner.fullName}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <br>
    <br>
</div>

<style>
    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0px !important;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    .view-content .table-footer > div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer > div h5 {
        margin: 0;
    }

    .view-content .table-footer > div > div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer > div > div > i {
        font-size: 16px;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .sticky-col {
        position: sticky !important;
        position: -webkit-sticky !important;
    }

    .sticky-actions-col {
        right: -10px !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }

    .attributeTooltip .attributeTooltiptext {
        visibility: hidden;
        width: 200px;
        background-color: #7BB7EB;
        color: #141f9f;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        right: 100%;
        opacity: 0;
        transition: opacity 1s;
    }

    .attributeTooltip .attributeTooltiptext::after {
        content: "";
        position: absolute;
        top: 25%;
        left: 102%;
        margin-left: -5px;
        border-width: 5px;
        border-style: solid;
        border-color: transparent transparent transparent #7BB7EB;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>REQ_DOCUMENTS</span>

        <button class="btn btn-sm new-button" ng-click="reqDocsVm.newReqDocument()" id="newReqDocument"
                title="{{'NEW_REQ_DOC' | translate}}">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW_REQ_DOC' | translate}}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="reqDocsVm.showTypeAttributes()" id="attributesButton"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-default" id="preferredPageButton"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="reqDocsVm.resetPage" search-term="reqDocsVm.searchText"
                          on-search="reqDocsVm.freeTextSearch"
                          filter-search="reqDocsVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 150px" translate>NUMBER</th>
                    <!-- <th class="col-width-200" translate>TYPE</th> -->
                    <th>
                        <span ng-if="reqDocsVm.selectedReqDocType != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                            ({{reqDocsVm.selectedReqDocType.name}})
                            <i class="fa fa-times-circle" ng-click="reqDocsVm.clearTypeSelection()"
                               title="{{removeTitle}}"></i>
                      </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                                <span uib-dropdown-toggle><span translate>TYPE</span>
                                    <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                </span>

                            <div class="dropdown-menu" role="menu">
                                <div
                                        style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                    <req-management-type-tree
                                            on-select-type="reqDocsVm.onSelectType"
                                            object-type="REQUIREMENTDOCUMENTTYPE"></req-management-type-tree>
                                </div>
                            </div>
                        </div>
                    </th>
                    <th class="col-width-200" translate>NAME</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 150px; text-align: center;z-index: auto !important;"
                        translate="ITEM_ALL_REVISION"></th>

                    <th style="width: 150px;z-index: auto !important;">
                            <span ng-if="reqDocsVm.selectedPhase != null"
                                  style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                        ({{reqDocsVm.selectedPhase}})
                                        <i class="fa fa-times-circle" ng-click="reqDocsVm.clearPhase()"
                                           title="{{removeTitle}}"></i>
                                </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                                    <span uib-dropdown-toggle><span translate>ITEM_ALL_LIFECYCLE</span>
                                        <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                    </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat="phase in reqDocsVm.lifecyclePhases"
                                    ng-click="reqDocsVm.onSelectPhase(phase)" style="text-transform: uppercase;"><a
                                        href="">{{phase}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>

                    <!-- <th style="width: 150px" translate>DOCUMENT_OWNER</th> -->
                    <th style="width: 150px;z-index: auto !important;">
                        <span ng-if="selectedPerson != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{selectedPerson.fullName}})
                                <i class="fa fa-times-circle" ng-click="clearOwner()"
                                   title="{{removeTitle}}"></i>
                        </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                            <span uib-dropdown-toggle><span translate>DOCUMENT_OWNER</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                            </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat="person in owners"
                                    ng-click="onSelectOwner(person)"><a
                                        href="">{{person.fullName}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in reqDocsVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="reqDocsVm.removeAttribute(selectedAttribute)"
                           title={{reqDocsVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="reqDocsVm.loading == true">
                    <td colspan="25">
            <span style="font-size: 15px;">
              <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                   class="mr5">
              <span translate>LOADING_REQ_DOCS</span>
            </span>
                    </td>
                </tr>

                <tr ng-if="reqDocsVm.loading == false && reqDocsVm.reqDocuments.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Documents.png" alt="" class="image">

                            <div class="message" translate>NO_REQ_DOCUMENT</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="reqDocument in reqDocsVm.reqDocuments.content">
                    <td style="width: 150px;">
                        <a href="" ng-click="reqDocsVm.showReqDocument(reqDocument)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="reqDocument.number | highlightText: freeTextQuery"></span>
                        </a>
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
                        <a href="" ng-click="reqDocsVm.showDocumentRevisionHistory(reqDocument)"
                           title="{{'ITEM_DETAILS_REVISION_HISTORY' | translate}}">
                            {{reqDocument.revision.revision}}
                        </a>
                    </td>

                    <td style="width: 150px">
                        <item-status item="reqDocument.revision"></item-status>
                    </td>
                    <td>{{reqDocument.owner}}</td>
                    <td>{{reqDocument.modifiedByName}}</td>
                    <td>{{reqDocument.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in reqDocsVm.selectedAttributes">
                        <all-view-attributes object="reqDocument"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
            <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
              <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
              <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                  style="z-index: 9999 !important;">
                  <tags-button object-type="'REQUIREMENTDOCUMENT'" object="reqDocument.id"
                               tags-count="reqDocument.tagsCount"></tags-button>
                  <li title="{{reqDocument.lifeCyclePhase.phaseType == 'RELEASED'? cannotDeleteApprovedDoc:''}}">
                      <a href=""
                         ng-class="{'disabled':reqDocument.lifeCyclePhase.phaseType == 'RELEASED'}"
                         ng-click="reqDocsVm.deleteReqDocument(reqDocument)" translate>DELETE</a>
                  </li>
                  <plugin-table-actions context="reqDoc.all" object-value="reqDocument"></plugin-table-actions>
              </ul>
            </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="reqDocsVm.reqDocuments" pageable="reqDocsVm.pageable"
                          page-size="reqDocsVm.pageSize"
                          previous-page="reqDocsVm.previousPage"
                          next-page="reqDocsVm.nextPage"></table-footer>
        </div>
    </div>
</div>


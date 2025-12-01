<style>
    .description {
        word-wrap: break-word;
        max-width: 400px;
        white-space: normal !important;
        text-align: left;
    }

    .name {
        word-wrap: break-word;
        width: 400px;
        white-space: normal !important;
        text-align: left;
    }

    .notes {

        word-wrap: break-word;
        width: 350px;
        white-space: normal !important;
        text-align: left;
    }

    .added-column {
        text-align: left;
        width: 150px;
    }

    .added-column i {
        display: none;
        cursor: pointer;
        margin-left: 5px;
    }

    .added-column:hover i {
        display: inline-block;
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

    /* The Close Button */
    .img-model .closeImage {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .glossary-progress.progress {
        background-color: #B0C7CF;
        height: 20px !important;
        margin: 0 !important;
    }

    .revision-column {
        width: 100px;
        text-align: center;
    }

    .actions-column {
        width: 150px;
        text-align: center;
    }

    .truncate {
        width: 250px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

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

    .responsive-table .dropdown-content {
        margin-left: -90px !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>TERMINOLOGY</span>

        <button class="btn btn-sm new-button"
                ng-if="glossarysVm.showEntry == false && hasPermission('terminology','create')"
                ng-click="glossarysVm.showNewGlossary()" title="{{'NEW_TERMINOLOGY' | translate}}">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'GLOSSARY' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-maroon" ng-click="glossarysVm.showTypeAttributes()"
                    title="{{glossarysVm.showGlossaryAttributes}}">
                <i class="fa fa-newspaper-o" style="" aria-hidden="true"></i>
            </button>
            <button class="btn btn-default btn-sm"
                    ng-if="glossarysVm.showCopyToClipBoard && glossarysVm.clipBoardGlossaries.length == 0"
                    ng-click="glossarysVm.copyToClipBoard()" title="Copy Terminology to Clipboard">
                <i class="fa fa-copy" style="font-size: 20px;"></i>
            </button>
            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
            <div class="btn-group"
                 ng-if="glossarysVm.showCopyToClipBoard && glossarysVm.clipBoardGlossaries.length > 0">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="fa fa-copy" style="font-size: 20px;"></span><span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li ng-click="glossarysVm.clearAndCopyToClipBoard()"><a href="">Clear and Add Terminologies</a></li>
                    <li ng-click="glossarysVm.copyToClipBoard()"><a href="">Add to Existing Terminologies</a></li>
                </ul>
            </div>
        </div>

        <free-text-search on-clear="glossarysVm.resetPage" search-term="glossarysVm.searchText"
                          on-search="glossarysVm.freeTextSearch"></free-text-search>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">

        <%---------------------- Glossaries View ----------------------------------%>

        <div class="responsive-table" style="padding: 10px;" ng-if="glossarysVm.showEntry == false">
            <table class="table table-striped highlight-row">
                <%--<div class="row">
                    <p class="col-sm-11"
                       ng-show="searchModeType == true"
                       style="color: #0390fd;font-size: 16px;text-align: center;" translate>
                        ALL_VIEW_ALERT</p>
                </div>--%>
                <thead>
                <tr>
                    <th style="width: 25px;"></th>
                    <th class="name" translate>NAME</th>
                    <th class="description" translate>DESCRIPTION</th>
                    <th class="revision-column" translate>REVISION</th>
                    <th translate>LIFE_CYCLE</th>
                    <th translate>RELEASED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;"
                        ng-repeat="selectedAttribute in glossarysVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="glossarysVm.removeAttribute(selectedAttribute)"
                           title="Remove this column"></i>
                    </th>
                    <th style="width: 80px;text-align: center" translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="glossarysVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_TERMINOLOGIES</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="glossarysVm.loading == false && glossarysVm.glossarys.content.length == 0">
                    <td colspan="25" translate>NO_TERMINOLOGIES</td>
                    <%-- <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                         <div class="no-data">
                             <img src="app/assets/no_data_images/AffectedItems.png" alt="" class="image">

                             <div class="message">{{ 'NO_TERMINOLOGIES' | translate}} </div>
                         </div>
                     </td>--%>
                </tr>

                <tr ng-repeat="glossary in glossarysVm.glossarys.content">
                    <td style="width: 25px">
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="glossary{{$index}}" name="glossarySelected" type="checkbox" ng-value="true"
                                   ng-model="glossary.selected" ng-change="glossarysVm.selectGlossary(glossary)">
                            <label for="glossary{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>
                    <td class="name">
                        <a href=""<%-- ng-if="hasPermission('terminology','view')"--%>
                           ng-click="glossarysVm.openGlossaryDetails(glossary)"
                           title="{{glossarysVm.glossaryOpenTitle}}">
                            <span ng-bind-html="glossary.defaultDetail.name | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="description" title="{{glossary.defaultDetail.description}}">
                        <span ng-bind-html="glossary.defaultDetail.description | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="revision-column">{{glossary.revision}}</td>
                    <td>
                        <item-status item="glossary"></item-status>
                    </td>
                    <td>{{glossary.releasedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in glossarysVm.selectedAttributes">
                        <all-view-attributes object="glossary"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>

                    <td class="text-center">
                     <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-click="glossarysVm.showRevisionHistory(glossary)">
                                    <a href="" translate>SHOW_REVISIONHISTORY</a>
                                </li>
                                <li ng-click="glossarysVm.printGlossaryItem(glossary)">
                                    <a href="" translate>PRINT_TERMINOLOGY</a>
                                </li>
                                <li ng-if="hasPermission('terminology','edit')"
                                    ng-show="glossary.isReleased != true"
                                    ng-click="glossarysVm.editGlossary(glossary)">
                                    <a href="" translate>EDIT_TERMINOLOGY</a>
                                </li>
                                <li ng-if="hasPermission('terminology','all') || hasPermission('terminology','delete')"
                                    ng-show="glossary.isReleased != true"
                                    ng-click="glossarysVm.deleteGlossary(glossary)">
                                    <a href="" translate>DELETE_TERMINOLOGY</a>
                                </li>
                            </ul>
                       </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer" ng-if="glossarysVm.showEntry == false">
            <table-footer objects="glossarysVm.glossarys" pageable="glossarysVm.pageable"
                          previous-page="glossarysVm.previousPage"
                          next-page="glossarysVm.nextPage"></table-footer>
        </div>
        <%----------------------  Entries View ----------------------%>
        <div class="responsive-table" style="padding: 10px;" ng-if="glossarysVm.showEntry == true">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="name" translate>NAME</th>
                    <th class="description" translate>DESCRIPTION</th>
                    <th class="revision-column" translate>VERSION</th>
                    <th class="notes" translate>COMMENTS</th>
                    <th class="actions-column" translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="glossarysVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_ENTRIES</span>
                        </span>
                    </td>
                </tr>
                <tr ng-if="glossarysVm.loading == false && glossarysVm.glossaryEntries.content.length == 0">
                    <td colspan="25" translate>NO_ENTRIES</td>
                </tr>
                <tr ng-repeat="entry in glossarysVm.glossaryEntries.content">
                    <td class="name">
                        <a href="" ng-click="glossarysVm.showEntryVersionDetails(entry)"
                           ng-if="hasPermission('entry','view')"
                           title="{{glossarysVm.entryOpenTitle}}">
                            <span ng-bind-html="entry.name | highlightText: freeTextQuery"></span>
                        </a>
                        <span ng-if="!hasPermission('entry','view')">
                            <span ng-bind-html="entry.name | highlightText: freeTextQuery"></span>
                        </span>
                    </td>
                    <td><span class="description"
                              ng-bind-html="entry.description |highlightText: freeTextQuery"></span>
                    </td>
                    <td class="revision-column">{{entry.version}}</td>
                    <td class="notes">{{entry.notes}}</td>
                    <td class="actions-column" style="width: 80px;text-align: center">
                        <div class="btn-group">
                            <button title={{glossarysVm.showVersionHistoryTitle}}
                                    ng-if="hasPermission('entry','view')"
                                    class="btn btn-xs btn-info"
                                    ng-click="glossarysVm.showVersionHistory(entry)">
                                <i class="fa fa-copy"></i>
                            </button>
                            <button title={{glossarysVm.editEntryTitle}}
                                    ng-if="hasPermission('entry','edit')"
                                    class="btn btn-xs btn-warning"
                                    ng-click="glossarysVm.editEntry(entry)">
                                <i class="fa fa-edit"></i>
                            </button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer" ng-if="glossarysVm.showEntry == true">
            <table-footer objects="glossarysVm.glossaryEntries" pageable="glossarysVm.pageable"
                          previous-page="glossarysVm.previousPage"
                          next-page="glossarysVm.nextPage" page-size="glossarysVm.pageSize"></table-footer>
        </div>
    </div>
</div>
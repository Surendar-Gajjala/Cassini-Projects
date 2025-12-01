<style scoped>
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

    .notification1 {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    .notification1 .badge1 {
        position: absolute;
        top: -10px;
        left: -10px;
        padding: 2px 6px;
        border-radius: 50%;
        background-color: orange;
        color: white;
        font-size: 14px;
    }

    .popover-title {
        font-size: 14px;
        font-weight: 600;
        text-align: center;
        line-height: 25px;
    }

    .popover {
        max-width: 500px;
        width: 500px;
    }

    .popover-content {
        max-height: 250px;
        overflow-y: auto;
    }

    .popover table {
        width: 497px;
        max-width: 100% !important;
    }

    .popover.bottom > .arrow::after {
        border-bottom-color: #f7f7f7;
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

    .table {
        width: 100% !important;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    th.number-column, td.number-column {
        width: 150px;
    }

    th.name-column2, td.name-column2 {
        width: 200px !important;
    }

    th.description-column2, td.description-column2 {
        width: 200px !important;

    }

    th.revision-column, td.revision-column {
        width: 150px;
        text-align: center;
    }

    th.lifecycle-column, td.lifecycle-column {
        width: 150px;

    }

    th.actions-column, td.actions-column {
        width: 150px;
        text-align: center;
    }

    th.modifiedby-column, td.modifiedby-column {
        width: 150px;
    }

    th.modifieddate-column, td.modifieddate-column {
        width: 150px;
    }

    .added-column {
        text-align: left;
        width: 200px;
    }

    .added-column i {
        display: none;
        cursor: pointer;
        margin-left: 5px;
    }

    .added-column:hover i {
        display: inline-block;
    }

    /* The Close Button */
    .img-model .closeImage,
    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus,
    .img-model .closeImage1:hover,
    .img-model .closeImage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage1:hover,
    .img-model .closeImage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }

    .attributeTooltip .attributeTooltiptext {
        visibility: hidden;
        background-color: #7BB7EB;
        color: #141f9f;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        opacity: 0;
        transition: opacity 1s;
        margin-left: -25px !important;
        word-break: break-all !important;

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
        position: relative !important;
        visibility: visible;
        opacity: 1;
    }

    p.ng-binding.ng-scope {
        min-width: 200px !important;
        max-width: 200px;;
        margin-bottom: 12px;
        word-break: break-all;
        overflow: visible;
        white-space: normal;
        height: auto; /* just added this line */
    }

    /*    .JCLRLastGrip .grip {
            display: none;
        }

        .grip {
            width: 100px;
            height: 10px;
            margin-top: -10px;
            /!*background-image: url('app/assets/images/favicon.png');*!/
            margin-left: 13px;
            position: relative;
            z-index: 88;
            cursor: move !important;
        }

        .grip:hover {
            background-position: 20px 0px;
            cursor: move !important;
        }

        .dragging .grip {
            background-position: 40px 0px;
        }

        .JColResizer {
            table-layout: auto !important;
        }*/

    .JCLRLastGrip {
        display: none !important;
    }

    /* .grip {

         width: 100px;
         height: 10px;
         margin-top: -10px;
         background-image: url('app/assets/images/favicon.png');
         margin-left: 13px;
         position: relative;
         z-index: 88;
         cursor: move !important;
         background-repeat: no-repeat;

     }*/

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 4;
    }

    /*.JCLRgrip {
        width: 11px;
        margin-left: -6px !important;
        position: absolute !important;
        height: 47px !important;
        cursor: col-resize !important;
        background-image: url(app/assets/images/rangeGrip.png);
        background-repeat: no-repeat;
        top: 7px;
    }*/

    .JCLRgrip {
        width: 13px;
        margin-left: -5px !important;
        position: absolute !important;
        height: 18px !important;
        cursor: col-resize !important;
        border-left: 4px solid #a0a0a0;
        background-repeat: no-repeat;
        margin-bottom: 0;
        margin-top: 10px;
    }

    .JCLRgrip:before {
        /*content: "\f7a5";
        font-family: FontAwesome;*/
        left: -5px;
        position: absolute;
        top: 0;
    }

    .JCLRdisabledGrip {
        display: none !important;
    }

    .grip:hover {
        background-position: 20px 0px;
        cursor: e-resize !important;

    }

    .grip:active {
        background-position: 20px 0px;
        cursor: e-resize !important;

    }

    .dragging .grip {
        background-position: 40px 0;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>SPECIFICATIONS</span>

        <button class="btn btn-sm new-button" title="{{allSpecificationsVm.newSpecTitle}}"
                ng-click="allSpecificationsVm.showNewItem()"
                ng-if="hasPermission('admin','all') || hasPermission('pgcspecification','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'SPECIFICATION' | translate }}</span>
        </button>


        <div class="btn-group">
            <button class="btn btn-sm btn-maroon" ng-click="allSpecificationsVm.showTypeAttributes()"
                    title="{{allSpecificationsVm.specAttributesTitle}}">
                <i class="fa fa-newspaper-o"></i>
            </button>
            <button class="btn btn-default btn-sm"
                    ng-if="allSpecificationsVm.showCopyToClipBoard && allSpecificationsVm.clipBoardSpecs.length == 0"
                    ng-click="allSpecificationsVm.copyToClipBoard()" title="Copy Specification to Clipboard">
                <i class="fa fa-copy"></i>
            </button>
            <div class="btn-group"
                 ng-if="allSpecificationsVm.showCopyToClipBoard && allSpecificationsVm.clipBoardSpecs.length > 0">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="fa fa-copy" style="font-size: 20px;"></span><span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li ng-click="allSpecificationsVm.clearAndCopyToClipBoard()"><a href="">Clear and Add
                        Specifications</a></li>
                    <li ng-click="allSpecificationsVm.copyToClipBoard()"><a href="">Add to Existing Specifications</a>
                    </li>
                </ul>
            </div>
            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>

        <!--
        <div class="btn-group">
            <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
                <span class="mr5" translate>IMPORT</span><span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <li onclick="$('#fileExcel').click()"><a href="">Excel</a></li>
                <li onclick="$('#fileReqIf').click();">
                    <a href="">ReqIF</a>
                </li>
            </ul>
        </div>

        <input type="file" id="fileExcel" value="file" onchange="angular.element(this).scope().importExcel()"
               style="display: none">

        <input type="file" id="fileReqIf" value="file" onchange="angular.element(this).scope().importReqIF()"
               style="display: none">
        -->


        <free-text-search on-clear="allSpecificationsVm.resetPage" search-term="allSpecificationsVm.searchText"
                          on-search="allSpecificationsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row" col-resizeable style="width: 100% !important;"
                   id="specifications"
                    >
                <thead>
                <tr>
                    <th style="width: 25px;"></th>
                    <th class="number-column" translate>NUMBER</th>
                    <th class="" translate style="z-index: auto !important;">NAME</th>
                    <th class="" translate style="z-index: auto !important;">DESCRIPTION</th>
                    <th class="revision-column" style="z-index: auto !important;" translate>ITEM_ALL_REVISION</th>
                    <th class="lifecycle-column" style="z-index: auto !important;" translate>ITEM_ALL_LIFECYCLE</th>
                    <th class="lifecycle-column" style="z-index: auto !important;" translate>ITEM_ALL_RELEASED_DATE</th>
                    <th class="modifiedby-column" style="z-index: auto !important;" translate>MODIFIED_BY</th>
                    <th class="modifieddate-column" style="z-index: auto !important;" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style=" min-width: 200px !important; max-width: 200px; margin-bottom: 12px; word-break: break-all;
                        overflow: visible; white-space: normal;height: auto;"
                        ng-repeat="selectedAttribute in allSpecificationsVm.selectedAttribute">
                        <span ng-bind-html="selectedAttribute.name"></span>
                        <i class="fa fa-times-circle"
                           ng-click="allSpecificationsVm.removeAttribute(selectedAttribute)"
                           title="Remove this column"></i>
                    </th>
                    <th class="actions-column" style="z-index: auto !important;" translate>ITEM_ALL_ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allSpecificationsVm.loading">
                    <td colspan="10" translate>LOADING</td>
                </tr>
                <tr ng-if="!allSpecificationsVm.loading && allSpecificationsVm.specifications.totalElements == 0">
                    <td colspan="10" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Specification.png" alt="" class="image">

                            <div class="message">{{ 'NO_SPECIFICATIONS' | translate}} </div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>
                <tr ng-repeat="spec in allSpecificationsVm.specifications.content">
                    <td style="width: 25px">
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="spec{{$index}}" name="specSelected" type="checkbox" ng-value="true"
                                   ng-model="spec.selected" ng-change="allSpecificationsVm.selectSpecification(spec)">
                            <label for="spec{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>
                    <td class="number-column">
                        <a href=""
                           ng-click="allSpecificationsVm.showDetails(spec)"><span
                                ng-bind-html="spec.objectNumber |highlightText: freeTextQuery"></span></a>
                        <%--<span ng-if="!hasPermission('pgcspecification','view')">--%>
                            <%--<span ng-bind-html="spec.objectNumber | highlightText: freeTextQuery"></span>--%>
                        <%--</span>--%>
                    </td>
                    <td class=""><span style="white-space: normal;word-wrap: break-word;">

                         <div style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                     webkit-column-count: 1;-moz-column-count: 1;column-count: 1;">
                             <span ng-bind-html="spec.name |highlightText: freeTextQuery"></span>
                         </div>


                    </span>
                    </td>
                    <td class=""><span style="white-space: normal;word-wrap: break-word;">

                        <div style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                     webkit-column-count: 1;-moz-column-count: 1;column-count: 1;" title="{{spec.description}}">
                            <span ng-bind-html="spec.description  | highlightText: freeTextQuery"></span>
                        </div>

                    </span>
                    </td>
                    <td class="revision-column">{{spec.revision}}</td>
                    <%--<td class="lifecycle-column">{{spec.lifecyclePhase.phase}}</td>--%>
                    <td class="lifecycle-column">
                        <spec-status spec="spec"></spec-status>
                    </td>
                    <td class="lifecycle-column">
                        <span ng-if="spec.releasedDate != null">{{spec.releasedDate}}</span>
                        <span ng-if="spec.releasedDate == null">-</span>
                    </td>
                    <td class="modifiedby-column"><span ng-bind-html="spec.modifiedByObject.fullName"></span></td>
                    <td class="modifieddate-column">{{spec.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allSpecificationsVm.selectedAttribute">
                        <all-view-attributes object="spec"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-class="{'disabled':spec.lifecyclePhase.phaseType == 'RELEASED' && (hasPermission('admin','all') || hasPermission('pgcspecification','delete'))}"
                                    ng-click="allSpecificationsVm.deleteSpecification(spec)"><a href="" translate>DELETE_SPECIFICATION</a>
                                </li>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="table-footer">
            <table-footer objects="allSpecificationsVm.specifications" pageable="allSpecificationsVm.pageable"
                          previous-page="allSpecificationsVm.previousPage"
                          next-page="allSpecificationsVm.nextPage" page-size="allSpecificationsVm.pageSize"></table-footer>
        </div>
    </div>
</div>
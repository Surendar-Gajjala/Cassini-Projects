<style>
    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 42px;
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
        bottom: 0 !important;
        height: 40px !important;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    .view-content .table-footer>div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer>div h5 {
        margin: 0;
    }

    .view-content .table-footer>div>div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer>div>div>i {
        font-size: 16px;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    #td {
        word-wrap: break-word;
        width: 300px;
        white-space: normal;
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

    .project-progress.progress {
        background-color: #B0C7CF;
        height: 20px !important;
        margin: 0 !important;
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

    .projectPlan-progress.progress {
        background-color: #B0C7CF;
        height: 20px !important;
        margin: 0 !important;
    }

    .activityTask-progress.progress {
        background-color: #B0C7CF;
        height: 20px !important;
        margin: 0 !important;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }

    .product-image {
        width: 100%;
        height: 300px;
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

    .widget {
        padding-right: 50px !important;
        cursor: pointer;
    }

    span.activeView {
        text-decoration: none;
        font-weight: bold;
        color: #467aff;
        font-size: 16px !important;
    }

    .item-notification {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    #freeTextSearchDirective {
        top: 7px !important;
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
        max-height: 220px;
        overflow-y: auto;
    }

    .popover table {
        width: 497px;
        max-width: 100% !important;
    }

    .popover.bottom>.arrow::after {
        border-bottom-color: #f7f7f7;
    }

    .view-content {
        height: 400px;
    }
</style>


<div class="no-padding" style="overflow-y: auto;padding: 10px;" ng-show="itemsView" ng-if="sharedVm.sharedObjects.content.length > 0">
    <div class="responsive-table" style="padding: 10px;margin-top: -45px;">
        <table class="table table-striped highlight-row">
            <thead>
                <tr>
                    <th style="width:1px;white-space:nowrap;"></th>
                    <th style="width: 1% !important;white-space: nowrap;" translate="ITEM_ALL_ITEM_NUMBER"></th>
                    <th style="width: 150px" translate="ITEM_CLASS"></th>
                    <th style="width: 150px" translate="ITEM_ALL_ITEM_TYPE"></th>
                    <th style="width: 150px" translate="ITEM_ALL_ITEM_NAME"></th>
                    <th style="" translate="ITEM_ALL_DESCRIPTION"></th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_REVISION"></th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <%--<th style="width: 100px;" translate="THUMBNAIL">
                        </th>--%>
                        <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
                        <th style="width: 150px" translate="ITEM_ALL_LIFECYCLE"></th>
                        <th style="width: 150px" translate="ITEM_ALL_RELEASED_DATE"></th>
                        <th style="width: 100px" translate="SHARED_TO"></th>
                        <th style="width: 100px" translate="SHARED_BY"></th>
                        <th style="width: 100px" translate="SHARED_ON"></th>
                </tr>
            </thead>
            <tbody>
                <tr ng-if="sharedVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="sharedVm.loading == false && sharedVm.sharedCounts.item == 0">
                    <td colspan="25" translate>NO_SHARING_ITEMS</td>
                </tr>

                <tr ng-repeat="item in sharedVm.sharedObjects.content">
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <all-view-icons object="item.objectType"></all-view-icons>
                    </td>
                    <td style="width: 1% !important;white-space: nowrap;">
                        <a href="" ng-click="sharedVm.showItem(item)" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span
                                ng-bind-html="item.number  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                        </a>
                    </td>
                    <td style="width:150px;">
                        <item-class item="item.itemClass"></item-class>
                    </td>
                    <td class="column-width-150" title="{{item.type}}"><span
                            ng-bind-html="item.type  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                        {{item.type.length > 20 ? '...' : ''}}
                    </td>
                    <td class="col-width-200" title="{{item.name}}"><span
                            ng-bind-html="item.name  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                        {{item.name.length > 20 ? '...' : ''}}
                    </td>
                    <td class="col-width-200" title="{{item.objectIdObject.description}}"><span
                            ng-bind-html="item.description  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td style="width: 150px; text-align: center;">
                        {{item.revision}}
                    </td>
                    <td style="width: 100px; text-align: center;">{{item.shareType}}</td>


                    <%-- <td style="width: 100px">
                        <div>
                            <a ng-if="item.thumbnailImage != null" href="" ng-click="sharedVm.showThumbnailImage(item)"
                                title="{{'CLICK_TO_SHOW_LARGE_IMAGE' | translate}}">
                                <img ng-src="{{item.thumbnailImage}}"
                                    style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>
                            <a ng-if="item.thumbnailImage == null" href="">
                                <img src="app/assets/images/cassini-logo-greyscale.png" title="No thumbnail" alt=""
                                    class="no-thumbnail-preview">
                            </a>
                            &lt;%&ndash;
                            <div id="myModal234" class="img-model modal">
                                <span class="closeImage">&times;</span>
                                <img class="modal-content" id="img134">
                            </div>&ndash;%&gt;
                            <div id="item-thumbnail{{item.id}}" class="item-thumbnail modal">
                                <div class="item-thumbnail-content">
                                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                                        <div class="thumbnail-view" id="thumbnail-view{{item.id}}">
                                            <div id="thumbnail-image{{item.id}}"
                                                style="display: table-cell;vertical-align: middle;text-align: center;">
                                                <img ng-src="{{item.thumbnailImage}}"
                                                    style="height: auto;width: auto;max-width: 100%;max-height: 90%" />
                                                <span class="thumbnail-close" id="thumbnail-close{{item.id}}"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        </td>--%>
                        <td>
                            <div class="label" style="width: 100px; text-align: center;"
                                ng-class="{'label-info': item.permission === 'READ','label-success': item.permission === 'WRITE'}">
                                {{item.permission}}
                            </div>
                        </td>
                        <td style="width: 150px">
                            <item-status item="item"></item-status>
                        </td>
                        <td style="width: 150px">
                            <span>{{item.releaseDate}}</span>
                        </td>
                        <td style="width: 100px">{{item.sharedTo}}</td>
                        <td style="width: 100px">{{item.sharedBy}}</td>
                        <td style="width: 100px">{{item.sharedOn}}</td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="table-footer">
        <table-footer objects="sharedVm.sharedObjects" pageable="sharedVm.pageable"
            previous-page="sharedVm.previousPage" next-page="sharedVm.nextPage" page-size="sharedVm.pageSize">
        </table-footer>
    </div>
</div>

<div class="no-padding" style="overflow-y: auto;padding: 10px;" ng-show="projectView" ng-if="sharedVm.sharedProjects.content.length > 0">
    <div class="responsive-table" style="padding: 10px;margin-top: -45px">
        <table class="table table-striped highlight-row">
            <thead>
                <tr>
                    <th style="width: 150px" translate="PROJECT_NAME"></th>
                    <th style="" translate="ITEM_ALL_DESCRIPTION"></th>
                    <th style="width: 150px" translate>PROJECT_MANAGER</th>
                    <th style="width: 150px" translate>PERCENT_COMPLETE</th>
                    <th style="width: 150px" translate>PLANNED_START_DATE</th>
                    <th style="width: 150px" translate>PLANNED_FINISH_DATE</th>
                    <th style="width: 150px" translate>ACTUAL_START_DATE</th>
                    <th style="width: 150px" translate>ACTUAL_FINISH_DATE</th>
                    <th style="width: 100px" translate="SHARED_TO"></th>
                    <th style="width: 100px" translate="SHARED_BY"></th>
                    <th style="width: 100px" translate="SHARED_ON"></th>
                </tr>
            </thead>
            <tbody>
                <tr ng-if="sharedVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="sharedVm.loading == false && sharedVm.sharedCounts.project == 0">
                    <td colspan="25" translate>NO_SHARING_MFRS</td>
                </tr>

                <tr ng-repeat="project in sharedVm.sharedProjects.content">
                    <td style="width: 150px">
                        <a href="" ng-click="sharedVm.showProject(project)"
                            title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{project.name}}</a>
                    </td>

                    <td id="td">{{project.description}}</td>
                    <td style="width: 150px">{{project.managerFirstName}}</td>
                    <td style="width: 150px">{{project.percentComplete}}</td>
                    <td style="width: 150px">{{project.plannedStartDate}}</td>
                    <td style="width: 150px">{{project.plannedFinishDate}}</td>
                    <td style="width: 150px">{{project.actualStartDate}}</td>
                    <td style="width: 150px">{{project.actualFinishDate}}</td>
                    <td style="width: 100px">{{project.sharedTo}}</td>
                    <td style="width: 100px">{{project.sharedBy}}</td>
                    <td style="width: 100px">{{project.sharedOn}}</td>
                </tr>
            </tbody>
        </table>


    </div>
    <div class="table-footer">
        <table-footer objects="sharedVm.sharedProjects" pageable="sharedVm.pageable"
            previous-page="sharedVm.previousPage" next-page="sharedVm.nextPage" page-size="sharedVm.pageSize">
        </table-footer>
    </div>
</div>

<div class="no-padding" style="overflow-y: auto;padding: 10px;height: 485px" ng-show="mfrView" ng-if="sharedVm.sharedMfrs.content.length > 0">
    <div class="responsive-table" style="padding: 10px;margin-top: -45px">
        <table class="table table-striped highlight-row" >
            <thead>
                <tr>
                    <th class="col-width-250" translate>MANUFACTURER_NAME</th>
                    <th style="width: 200px;" translate>MANUFACTURER_TYPE</th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th translate="ITEM_ALL_LIFECYCLE"></th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
                    <th style="width: 100px" translate="SHARED_TO"></th>
                    <th style="width: 100px" translate="SHARED_BY"></th>
                    <th style="width: 100px" translate="SHARED_ON"></th>
                </tr>
            </thead>
            <tbody>
                <tr ng-if="sharedVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="sharedVm.loading == false && sharedVm.sharedCounts.mfr == 0">
                    <td colspan="25" translate>NO_SHARING_MFRS</td>
                </tr>

                <tr ng-repeat="mfr in sharedVm.sharedMfrs.content">
                    <td style="width: 150px">
                        <a href="" ng-click="sharedVm.showMfrDetails(mfr)"
                            title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{mfr.name}}</a>
                    </td>
                    <td>{{mfr.type}}</td>
                    <td id="td">{{mfr.description}}</td>
                    <td>
                        <item-status item="mfr"></item-status>
                    </td>
                    <td style="width: 100px; text-align: center;">{{mfr.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                            ng-class="{'label-info': mfr.permission === 'READ','label-success': mfr.permission === 'WRITE'}">
                            {{mfr.permission}}
                        </div>
                    </td>
                    <td style="width: 100px">{{mfr.sharedTo}}</td>
                    <td style="width: 100px">{{mfr.sharedBy}}</td>
                    <td style="width: 100px">{{mfr.sharedOn}}</td>
                </tr>
            </tbody>
        </table>


    </div>
    <div class="table-footer">
        <table-footer objects="sharedVm.sharedMfrs" pageable="sharedVm.pageable" previous-page="sharedVm.previousPage"
            next-page="sharedVm.nextPage" page-size="sharedVm.pageSize"></table-footer>
    </div>
</div>

<div class="no-padding" style="overflow-y: auto;padding: 10px;height: 485px" ng-show="mfrPartView" ng-if="sharedVm.sharedMfrParts.content.length > 0">
    <div class="responsive-table" style="padding: 10px;margin-top: -45px">
        <table class="table table-striped highlight-row" >
            <thead>
                <tr>
                    <th style="width: 200px;" translate>PART_NUMBER</th>
                    <th style="width: 200px;" translate>MANUFACTURER</th>
                    <th style="width: 200px;" translate>TYPE</th>
                    <th class="col-width-250" translate>PART_NAME</th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th translate="ITEM_ALL_LIFECYCLE"></th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
                    <th style="width: 100px" translate="SHARED_TO"></th>
                    <th style="width: 100px" translate="SHARED_BY"></th>
                    <th style="width: 100px" translate="SHARED_ON"></th>
                </tr>
            </thead>
            <tbody>
                <tr ng-if="sharedVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="sharedVm.loading == false && sharedVm.sharedCounts.mfrPart == 0">
                    <td colspan="25" translate>NO_SHARING_MFR_PARTS</td>
                </tr>

                <tr ng-repeat="mfrPart in sharedVm.sharedMfrParts.content">
                    <td style="width: 150px">
                        <a href="" ng-click="sharedVm.showMfrPartDetails(mfrPart)"
                            title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span
                                ng-bind-html="mfrPart.number  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                        </a>
                    </td>
                    <td ng-bind-html="mfrPart.mfr  | highlightText: sharedVm.sharedObjectFilter.searchQuery">
                    </td>
                    <td class="col-width-150">
                        <span
                            ng-bind-html="mfrPart.type  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td class="col-width-200">
                        <span
                            ng-bind-html="mfrPart.name  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td class="col-width-250">
                        <span
                            ng-bind-html="mfrPart.description  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td>
                        <item-status item="mfrPart"></item-status>
                    </td>
                    <td style="width: 100px; text-align: center;">{{mfrPart.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                            ng-class="{'label-info': mfrPart.permission === 'READ','label-success': mfrPart.permission === 'WRITE'}">
                            {{mfrPart.permission}}
                        </div>
                    </td>
                    <td style="width: 100px">{{mfrPart.sharedTo}}</td>
                    <td style="width: 100px">{{mfrPart.sharedBy}}</td>
                    <td style="width: 100px">{{mfrPart.sharedOn}}</td>
                </tr>
            </tbody>
        </table>


    </div>
    <div class="table-footer">
        <table-footer objects="sharedVm.sharedMfrParts" pageable="sharedVm.pageable"
            previous-page="sharedVm.previousPage" next-page="sharedVm.nextPage" page-size="sharedVm.pageSize">
        </table-footer>
    </div>
</div>

<div class="no-padding" style="overflow-y: auto;padding: 10px;height: 485px" ng-show="programsView" ng-if="sharedVm.sharedPrograms.content.length > 0">
    <div class="responsive-table" style="padding: 10px;margin-top: -45px">
        <table class="table table-striped highlight-row" >
            <thead>
                <tr>
                    <th style="width: 200px;" translate>PROGRAM_NAME</th>
                    <th style="width: 200px;" translate>TYPE</th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th class="col-width-250" translate>PROGRAM_MANAGER</th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
                    <th style="width: 100px" translate="SHARED_TO"></th>
                    <th style="width: 100px" translate="SHARED_BY"></th>
                    <th style="width: 100px" translate="SHARED_ON"></th>
                </tr>
            </thead>
            <tbody>
                <tr ng-if="sharedVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="sharedVm.loading == false && sharedVm.sharedPrograms.content.length == 0">
                    <td colspan="25" translate>NO_SHARING_PROGRAMS</td>
                </tr>

                <tr ng-repeat="program in sharedVm.sharedPrograms.content">
                    <td style="width: 150px">
                        <a href="" ng-click="sharedVm.showProgramDetails(program)"
                            title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span
                                ng-bind-html="program.name  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                        <span
                            ng-bind-html="program.type  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td class="col-width-250">
                        <span
                            ng-bind-html="program.description  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td class="col-width-200">
                        <span
                            ng-bind-html="program.manager  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td style="width: 100px; text-align: center;">{{program.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                            ng-class="{'label-info': program.permission === 'READ','label-success': program.permission === 'WRITE'}">
                            {{program.permission}}
                        </div>
                    </td>
                    <td style="width: 100px">{{program.sharedTo}}</td>
                    <td style="width: 100px">{{program.sharedBy}}</td>
                    <td style="width: 100px">{{program.sharedOn}}</td>
                </tr>
            </tbody>
        </table>


    </div>
    <div class="table-footer">
        <table-footer objects="sharedVm.sharedPrograms" pageable="sharedVm.pageable"
            previous-page="sharedVm.previousPage" next-page="sharedVm.nextPage" page-size="sharedVm.pageSize">
        </table-footer>
    </div>
</div>

<div class="no-padding" style="overflow-y: auto;padding: 10px;height: 485px" ng-show="supplierView" ng-if="sharedVm.sharedSuppliers.content.length > 0">
    <div class="responsive-table" style="padding: 10px;margin-top: -45px">
        <table class="table table-striped highlight-row" >
            <thead>
                <tr>


                    <th class="col-width-150" translate>NAME</th>
                    <th class="col-width-150" translate>TYPE</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th class="col-width-150" translate>LIFECYCLE</th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
                    <th style="width: 100px" translate="SHARED_TO"></th>
                    <th style="width: 100px" translate="SHARED_BY"></th>
                    <th style="width: 100px" translate="SHARED_ON"></th>
                </tr>
            </thead>
            <tbody>
                <tr ng-if="sharedVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="sharedVm.loading == false && sharedVm.sharedCounts.supplier == 0">
                    <td colspan="25" translate>NO_SHARING_SUPPLIERS</td>
                </tr>

                <tr ng-repeat="supplier in sharedVm.sharedSuppliers.content">
                    <td style="width: 150px">
                        <a href="" ng-click="sharedVm.showSupplierDetails(supplier)"
                            title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span
                                ng-bind-html="supplier.name  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                        </a>
                    </td>

                    <td class="col-width-150">
                        <span
                            ng-bind-html="supplier.type  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                    </td>

                    <td class="col-width-250">
                        <span
                            ng-bind-html="supplier.description  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td>
                        <item-status item="supplier"></item-status>
                    </td>
                    <td style="width: 100px; text-align: center;">{{supplier.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                            ng-class="{'label-info': supplier.permission === 'READ','label-success': supplier.permission === 'WRITE'}">
                            {{supplier.permission}}
                        </div>
                    </td>
                    <td style="width: 100px">{{supplier.sharedTo}}</td>
                    <td style="width: 100px">{{supplier.sharedBy}}</td>
                    <td style="width: 100px">{{supplier.sharedOn}}</td>
                </tr>
            </tbody>
        </table>


    </div>
    <div class="table-footer">
        <table-footer objects="sharedVm.sharedSuppliers" pageable="sharedVm.pageable"
            previous-page="sharedVm.previousPage" next-page="sharedVm.nextPage" page-size="sharedVm.pageSize">
        </table-footer>
    </div>
</div>

<div class="no-padding" style="overflow-y: auto;padding: 10px;height: 485px" ng-show="sharedVm.customObjectView" ng-if="sharedVm.sharedCustomObjects.content.length > 0">
    <div class="responsive-table" style="padding: 10px;margin-top: -45px">
        <table class="table table-striped highlight-row" >
            <thead>
                <tr>


                    <th class="col-width-150" translate>NAME</th>
                    <th class="col-width-150" translate>TYPE</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
                    <th style="width: 100px" translate="SHARED_TO"></th>
                    <th style="width: 100px" translate="SHARED_BY"></th>
                    <th style="width: 100px" translate="SHARED_ON"></th>
                </tr>
            </thead>
            <tbody>
                <tr ng-if="sharedVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="sharedVm.loading == false && sharedVm.sharedCounts.customObject == 0">
                    <td colspan="25" translate>NO_SHARING_CUSTOM_OBJECTS</td>
                </tr>

                <tr ng-repeat="supplier in sharedVm.sharedCustomObjects.content">
                    <td style="width: 150px">
                        <a href="" ng-click="sharedVm.showCustomObjectDetails(supplier)"
                            title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span
                                ng-bind-html="supplier.name  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                        </a>
                    </td>

                    <td class="col-width-150">
                        <span
                            ng-bind-html="supplier.type  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                    </td>

                    <td class="col-width-250">
                        <span
                            ng-bind-html="supplier.description  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                    </td>

                    <td style="width: 100px; text-align: center;">{{supplier.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                            ng-class="{'label-info': supplier.permission === 'READ','label-success': supplier.permission === 'WRITE'}">
                            {{supplier.permission}}
                        </div>
                    </td>
                    <td style="width: 100px">{{supplier.sharedTo}}</td>
                    <td style="width: 100px">{{supplier.sharedBy}}</td>
                    <td style="width: 100px">{{supplier.sharedOn}}</td>
                </tr>
            </tbody>
        </table>


    </div>
    <div class="table-footer">
        <table-footer objects="sharedVm.sharedSuppliers" pageable="sharedVm.pageable"
            previous-page="sharedVm.previousPage" next-page="sharedVm.nextPage" page-size="sharedVm.pageSize">
        </table-footer>
    </div>
</div>

<div class="no-padding" style="overflow-y: auto;padding: 10px;height: 485px" ng-show="folderView"  ng-if="sharedVm.sharedFolders.content.length > 0">
    <div class="responsive-table" style="padding: 10px;margin-top: -45px">
        <table id="itemFilesTable" class="table table-striped highlight-row">
            <thead>
                <tr>
                    <th class="col-width-250 file-name sticky-col sticky-actions-col" style="z-index: 11;" translate>
                        TAB_FILES_FILE_NAME
                    </th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th style="text-align: center" translate>TAB_FILES_VERSION</th>
                    <th style="text-align: center" translate>LIFECYCLE
                    </th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
                    <th style="width: 100px" translate="SHARED_TO"></th>
                    <th style="width: 100px" translate="SHARED_BY"></th>
                    <th style="width: 100px" translate="SHARED_ON"></th>
                </tr>
            </thead>
            <tbody>
                <tr ng-if="loading == true">
                    <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                            class="mr5"><span translate>LOADING_FILES</span>
                    </td>
                </tr>

                <tr id="{{$index}}" ng-show="sharedVm.sharedFolders.content.length > 0"
                    ng-repeat="file in sharedVm.sharedFolders.content">

                    <td class="file-name sticky-col sticky-actions-col" style="z-index: 12">
                        <span class="level{{file.level}}" ng-show="file.fileDto.fileType == 'FOLDER'"
                            ng-click="toggleNode(file)">
                            <i class="mr5 fa" title="{{ExpandCollapse}}"
                                style="cursor: pointer !important; font-size: 18px;color: limegreen !important;"
                                ng-class="{'fa-folder': (file.expanded == false || file.expanded == null || file.expanded == undefined),
                           'fa-folder-open': file.expanded == true}">
                            </i>
                            <span class="autoClick" title="{{file.fileDto.name}}"
                                style="cursor: pointer !important;white-space: normal;word-wrap: break-word;"
                                ng-bind-html="file.fileDto.name">
                            </span>

                        </span>

                        <span class="imageTooltip level{{file.level}}" ng-show="file.fileDto.fileType == 'FILE'">
                            <a href="" ng-click="downloadFile(file.fileDto)" title="{{downloadFileTitle}}">
                                <span ng-if="file.fileDto.fileType == 'FILE'" ng-bind-html="file.fileDto.name"></span>
                            </a>

                        </span>
                    </td>
                    <td class="description-column">
                        <span ng-if="file.fileDto.fileType == 'FILE'">{{file.fileDto.description}}</span>
                        <span ng-if="file.fileDto.fileType == 'FOLDER'">{{file.fileDto.description}}</span>
                    </td>


                    <td style="text-align: center">
                        <span ng-if="file.fileDto.fileType == 'FILE'">
                            <span>{{file.fileDto.revision}}.{{file.fileDto.version}}
                            </span>
                        </span>
                    </td>
                    <td style="text-align: center">
                        <span ng-if="file.fileDto.fileType == 'FILE'">
                            <item-status item="file.fileDto"></item-status>
                        </span>
                        </span>
                    </td>
                    <td style="width: 100px; text-align: center;">{{file.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                            ng-class="{'label-info': file.permission === 'READ','label-success': file.permission === 'WRITE'}">
                            {{file.permission}}
                        </div>
                    </td>
                    <td style="width: 100px">{{file.sharedTo}}</td>
                    <td style="width: 100px">{{file.sharedBy}}</td>
                    <td style="width: 100px">{{file.sharedOn}}</td>
                </tr>
            </tbody>
        </table>


    </div>
    <div class="table-footer">
        <table-footer objects="sharedVm.sharedFolders" pageable="sharedVm.pageable"
            previous-page="sharedVm.previousPage" next-page="sharedVm.nextPage" page-size="sharedVm.pageSize">
        </table-footer>
    </div>
</div>

<div class="no-padding" style="overflow-y: auto;padding: 10px;height: 485px" ng-show="declarationView" ng-if="sharedVm.sharedDeclarations.content.length > 0">
    <div class="responsive-table" style="padding: 10px;margin-top: -45px">
        <table class="table table-striped highlight-row">
            <thead>
                <tr>
                    <th style="width: 200px;" translate>NUMBER</th>
                    <th style="width: 200px;" translate>TYPE</th>
                    <th class="col-width-250" translate>NAME</th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th style="width: 100px;" translate>STATUS</th>
                    <th class="description-column" translate>SUPPLIER</th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
                    <th style="width: 100px" translate="SHARED_TO"></th>
                    <th style="width: 100px" translate="SHARED_BY"></th>
                    <th style="width: 100px" translate="SHARED_ON"></th>
                </tr>
            </thead>
            <tbody>
                <tr ng-if="sharedVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                class="mr5">
                            <span translate>LOADING_DECLARATIONS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="sharedVm.loading == false && sharedVm.sharedCounts.declaration == 0">
                    <td colspan="25" translate>NO_SHARING_DECLARATIONS</td>
                </tr>

                <tr ng-repeat="declaration in sharedVm.sharedDeclarations.content">
                    <td style="width: 150px">
                        <a href="" ng-click="sharedVm.showDeclaration(declaration)"
                            title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span
                                ng-bind-html="declaration.number  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                        <span
                            ng-bind-html="declaration.type  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td class="col-width-200">
                        <span
                            ng-bind-html="declaration.name  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td class="col-width-250"><span
                            ng-bind-html="declaration.description  | highlightText: sharedVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td>
                        <declaration-status object="declaration"></declaration-status>
                    </td>
                    <td>{{declaration.supplier}}</td>
                    <td style="width: 100px; text-align: center;">{{declaration.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                            ng-class="{'label-info': declaration.permission === 'READ','label-success': declaration.permission === 'WRITE'}">
                            {{declaration.permission}}
                        </div>
                    </td>
                    <td style="width: 100px">{{declaration.sharedTo}}</td>
                    <td style="width: 100px">{{declaration.sharedBy}}</td>
                    <td style="width: 100px">{{declaration.sharedOn}}</td>
                </tr>
            </tbody>
        </table>

    </div>
    <div class="table-footer">
        <table-footer objects="sharedVm.sharedDeclarations" pageable="sharedVm.pageable"
            previous-page="sharedVm.previousPage" next-page="sharedVm.nextPage" page-size="sharedVm.pageSize">
        </table-footer>
    </div>
</div>


<div class="no-padding" ng-show="sharedVm.sharedFoldersView == true">

    <style scoped>
        #classificationContainer .search-input i.fa-search {
            position: absolute;
            margin-top: 13px;
            margin-left: 10px;
            color: grey;
            opacity: 0.5;
            font-size: 12px;
        }

        #classificationContainer .search-input .search-form {
            padding-left: 25px;
            padding-right: 25px;
        }

        .search-form {
            border-radius: 3px;
            background-color: #eaeaea;
            border: 1px solid #fff;
        }

        .search-form:focus {
            box-shadow: none;
            border: 1px solid #c5cfd5;
        }

        #classificationContainer .search-input .search-form {
            padding-left: 25px;
            padding-right: 25px;
        }

        i.clear-search {
            margin-left: 205px;
            color: #aab4b7;
            cursor: pointer;
            z-index: 4 !important;
            position: absolute;
            margin-top: -26px;
        }

        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -5px;
        }
    </style>

    <div id="foldersContextMenu" class="context-menu dropdown clearfix"
        style="position: fixed;display:none; z-index: 9999">
    </div>
    <div class="split-pane fixed-left">
        <div class="split-pane-component split-left-pane"
            style="padding: 0;min-width: 250px;max-width: 250px;overflow: auto;">
            <div id="classificationContainer" class="classification-pane" data-toggle="context"
                data-target="#context-menu">
                <div class="search-input" style="height: 30px;margin: 10px 10px 20px 10px;width: 230px;">
                    <i class="fa fa-search"></i>
                    <input type="search" class="form-control input-sm search-form" ng-model="sharedVm.searchValue"
                        ng-change="sharedVm.searchTree()">
                    <i class="las la-times-circle clear-search" ng-show="sharedVm.searchValue.length > 0"
                        ng-click="sharedVm.searchValue = '';sharedVm.searchTree()"></i>
                </div>
                <ul id="documentFolderTree" class="easyui-tree classification-tree">
                </ul>
                <ul id="sharedFolderTree" class="easyui-tree classification-tree">
                </ul>
            </div>
        </div>
        <div class="split-pane-divider" style="left: 250px;z-index: 1;">
        </div>
        <div class="split-pane-component split-right-pane noselect" style="left:250px;padding: 0;overflow-y: hidden;">


            <div class="responsive-table">

                <table id="externalFilesTable" class="table table-striped highlight-row">
                    <thead>
                        <tr>
                            <th></th>
                            <th class="col-width-250 file-name sticky-col sticky-actions-col" style="z-index: 99;"
                                translate>
                                TAB_FILES_FILE_NAME
                            </th>
                            <th translate>TAB_FILES_FILE_SIZE</th>
                            <th class="description-column" translate>DESCRIPTION</th>
                            <th style="text-align: center" translate>TAB_FILES_VERSION</th>
                            <th class="description-column"
                                ng-if="folderObject.objectType == 'FILE' || selectedType=='FILE'" translate>Object
                                Name
                            </th>
                            <th style="text-align: center"
                                ng-if="folderObject.objectType == 'FILE' || selectedType=='FILE'" translate>Object
                                Type
                            </th>
                            <th style="text-align: center" ng-if="folderObject.objectType=='DOCUMENT'" translate>
                                LIFECYCLE
                            </th>
                            <th translate>CREATED_DATE</th>
                            <th translate>CREATED_BY</th>
                            <th translate>MODIFIED_DATE</th>
                            <th translate>MODIFIED_BY</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr ng-if="sharedVm.loading == true">
                            <td colspan="10"><img
                                    src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_FILES</span>
                            </td>
                        </tr>

                        <tr ng-if="sharedVm.sharedExternalUserFolders.length == 0">
                            <td colspan="10" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Files.png" alt="" class="image">

                                    <div class="message">{{
                                        'NO_FILES' | translate}}
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-show="sharedVm.sharedExternalUserFolders.length > 0"
                            ng-repeat="file in sharedVm.sharedExternalUserFolders">
                            <td style="width: 20px;">
                                <span style="text-align: center;width: 20px;" ng-show="file.fileType != 'FOLDER'"
                                    ng-click="filePreview(file)" title="Preview File">
                                    <i class="{{getFileIcon(file.name)}}"
                                        ng-style="{{getFileIconColor(file.name)}}"></i>
                                </span>
                            </td>
                            <td class="file-name sticky-col sticky-actions-col" style="z-index: 12">
                                <a href="" ng-click="sharedVm.sharedFileDownload(file)" title="{{downloadFileTitle}}">
                                    <span ng-bind-html="file.name | highlightText: searchText"></span>
                                </a>

                            </td>
                            <td>
                                <span style="cursor: move">{{file.size.toFileSize()}}</span>
                            </td>
                            <td class="description-column">
                                <span>{{file.description}}</span>
                            </td>
                            <td style="text-align: center">
                                <span
                                    ng-if="folderObject.objectType=='FILE' || selectedType=='FILE'">{{file.version}}</span>
                                <span
                                    ng-if="folderObject.objectType=='DOCUMENT'">{{file.revision}}.{{file.version}}</span>
                            </td>
                            <td ng-if="folderObject.objectType=='FILE' || selectedType=='FILE'"
                                class="description-column">
                                <span>{{file.parentObjectName}}</span>
                            </td>
                            <td ng-if="folderObject.objectType=='FILE' || selectedType=='FILE'"
                                style="text-align: center">
                                <object-type-status object="file"></object-type-status>
                            </td>


                            <td ng-if="folderObject.objectType=='DOCUMENT'" style="text-align: center">
                                <item-status item="file"></item-status>
                            </td>

                            <td>{{file.createdDate}}</td>
                            <td>{{file.createdByName}}</td>
                            <td>{{file.modifiedDate}}</td>
                            <td>{{file.modifiedByName}}</td>
                        </tr>
                    </tbody>
                </table>
            </div>


        </div>
    </div>
</div>
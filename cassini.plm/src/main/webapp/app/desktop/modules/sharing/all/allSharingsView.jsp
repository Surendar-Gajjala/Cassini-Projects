<style>
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

    .product-image {
        width: 100%;
        height: 300px;
    }

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
        bottom: 0;
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

    .view-toolbar .ui-select-bootstrap .ui-select-toggle > a.btn {
        margin-right: 11px;
        max-height: 29px;
        padding: 3px 5px;
    }

    i.activeView {
        text-decoration: none;
        font-weight: bold;
        color: #467aff;
        font-size: 16px !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

    .project-progress.progress {
        background-color: #B0C7CF;
        height: 20px !important;
        margin: 0 !important;
    }

    .activityTask-progress.progress {
        background-color: #B0C7CF;
        height: 20px !important;
        margin: 0 !important;
    }

    .projectPlan-progress.progress {
        background-color: #B0C7CF;
        height: 20px !important;
        margin: 0 !important;
    }
</style>

<div class="view-container" fitcontent>
    <!-- <div class="view-toolbar">
        <span style="line-height: 35px; font-weight: 600;font-size: 23px;padding-right: 10px;" translate>SHARING</span>

        <button class="btn btn-sm btn-info" ng-if="allSharesVm.sharedCounts.item > 0"
                ng-class="{'activeView':allSharesVm.itemsView == true}" ng-click='allSharesVm.sharedAllItems()'
                title="Items">
            <i ng-class="{'activeView':allSharesVm.itemsView == true}" class="fa fa-cogs" style=""></i>
        </button>

        <%-- <button class="btn btn-sm btn-info" ng-if="allSharesVm.sharedMfrs.content.length > 0"
            ng-class="{'activeView':allSharesVm.mfrView == true}" ng-click='allSharesVm.sharedMfr()'
            title="Manufacturer">
            <i ng-class="{'activeView':allSharesVm.mfrView == true}" class="fa flaticon-office42" style=""></i>
            </button>--%>
        <button class="btn btn-sm btn-info" ng-if="allSharesVm.sharedCounts.mfrPart > 0"
                ng-class="{'activeView':allSharesVm.mfrPartView == true}" ng-click='allSharesVm.sharedMfrPart()'
                title="Manufacturer Parts">
            <i ng-class="{'activeView':allSharesVm.mfrPartView == true}" class="fa fa-cubes" style=""></i>
        </button>

        <button class="btn btn-sm btn-info" 
             ng-click='allSharesVm.sharedProgramObjects()'
            title="Programs">
        <i ng-class="{'activeView':allSharesVm.programsView == true}" class="fa fa-calendar-check-o" style=""></i>
        </button>


        <button class="btn btn-sm btn-info" ng-if="allSharesVm.sharedCounts.supplier > 0"
                ng-class="{'activeView':allSharesVm.supplierView == true}" ng-click='allSharesVm.sharedSupplier()'
                title="Suppliers">
            <i ng-class="{'activeView':allSharesVm.supplierView == true}" class="fa flaticon-office42" style=""></i>
        </button>


        <button class="btn btn-sm btn-info" ng-if="allSharesVm.sharedCounts.folder > 0">
            <span class="item-notification" ng-class="{'activeView':allSharesVm.folderView == true}"
                  ng-click='allSharesVm.sharedFolder()'
                  title="Folders">
                <i class="fa fa-folder-open-o" style=""></i>
            </span>
        </button>

        <button class="btn btn-sm btn-info" ng-if="allSharesVm.sharedCounts.declaration > 0">
            <a class="item-notification" ng-class="{'activeView':allSharesVm.declarationView == true}"
               ng-click='allSharesVm.sharedDeclaration()' title="Declarations">
                <i class="fa fa-newspaper-o" style=""></i>
            </a>
        </button>

        <button class="btn btn-sm btn-info"
                ng-if="allSharesVm.sharedCounts.project > 0 || allSharesVm.sharedCounts.activity > 0 || allSharesVm.sharedCounts.task > 0">
            <a class="item-notification" ng-class="{'activeView':allSharesVm.projectView == true}"
               ng-click='allSharesVm.sharedProjectsObjects()' title="Projects">
                <i ng-class="{'activeView':allSharesVm.projectView == true}" class="fa fa-calendar" style=""></i>
            </a>
        </button>

        <button class="btn btn-sm btn-default" style="" title="{{preferredPage}}" ng-click="savePreferredPage()">
            <i class="fa fa fa-anchor" style=""></i>
        </button>

        <div class="pull-right text-center" style="width: 165px;margin-right: 116px;"
             ng-hide="allSharesVm.folderView == true">
            <ui-select ng-model="allSharesVm.externalUser" on-select="allSharesVm.loadSharedObjectsBySharedTo()"
                       theme="bootstrap" style="width:158%;">
                <ui-select-match allow-clear="true" placeholder={{allSharesVm.externalUsers}}>
                    {{$select.selected.personName}}
                </ui-select-match>
                <ui-select-choices repeat="user.personId as user in allSharesVm.external | filter: $select.search">
                    <div ng-bind="user.personName | highlight: $select.search"></div>
                </ui-select-choices>
            </ui-select>
        </div>

        <div class="pull-right text-center"  ng-hide="allSharesVm.folderView == true"
             style="width: 165px;margin-right: 116px;/*height: 30px!important;text-align:center;*/">
            <ui-select ng-model="allSharesVm.internalUser" on-select="allSharesVm.loadSharedObjectsBySharedBy()"
                       theme="bootstrap" style="width:158%">
                <ui-select-match allow-clear="true" placeholder={{allSharesVm.internalUsers}}>
                    {{$select.selected.person.firstName}}
                </ui-select-match>
                <ui-select-choices repeat="user.person.id as user in allSharesVm.users | filter: $select.search">
                    <div ng-bind="user.person.firstName | highlight: $select.search"></div>
                </ui-select-choices>
            </ui-select>
        </div>

    </div> -->
    <div class="view-content no-padding" ng-show= "itemsView">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width:1px;white-space:nowrap;"></th>
                    <th style="width: 1% !important;white-space: nowrap;" translate="ITEM_ALL_ITEM_NUMBER"></th>
                    <th style="width: 150px" translate="ITEM_ALL_ITEM_TYPE"></th>
                    <th style="width: 150px" translate="ITEM_ALL_ITEM_NAME"></th>
                    <th style="" translate="ITEM_ALL_DESCRIPTION"></th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_REVISION"></th>
                    <th style="width: 150px" translate="ITEM_ALL_LIFECYCLE"></th>
                    <%--<th style="width: 150px" translate="THUMBNAIL">
                        </th>--%>
                    <th style="width: 150px" translate="ITEM_ALL_RELEASED_DATE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="SHARED_TO"></th>
                    <th style="width: 100px" translate="SHARED_BY"></th>
                    <th style="width: 100px" translate="SHARED_ON"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allSharesVm.loading == true">
                    <td colspan="25">
                            <span style="font-size: 15px;">
                                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                                <span translate>LOADING_ITEMS</span>
                            </span>
                    </td>
                </tr>

                <tr ng-if="allSharesVm.loading == false && allSharesVm.sharedObjects.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Items.png" alt="" class="image">

                            <div class="message">{{ 'NO_SHARING_ITEMS' | translate}}</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="sharedItem in allSharesVm.sharedObjects.content">
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <all-view-icons object="sharedItem.objectIdObject"></all-view-icons>
                    </td>

                    <td style="width: 1% !important;white-space: nowrap;">
                        <a href="" ng-click="allSharesVm.showItem(sharedItem)" ng-if="hasPermission('item','view')"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{sharedItem.number}}
                        </a>
                        <span ng-if="!hasPermission('item','view')">{{sharedItem.number}}</span>
                    </td>
                    <td style="width: 150px" title="{{sharedItem.type}}">
                        <span ng-bind-html="sharedItem.type"></span>
                    </td>
                    <td style="width: 150px" title="{{sharedItem.name}}">
                        <span ng-bind-html="sharedItem.name"></span>
                    </td>
                    <td class="description-column" title="{{sharedItem.description}}"><span
                            ng-bind-html="sharedItem.description "></span>
                    </td>
                    <td style="width: 150px; text-align: center;">
                        {{sharedItem.revision}}
                    </td>
                    <td style="width: 150px">
                        <item-status item="sharedItem"></item-status>
                    </td>

                    <%-- <td style="width: 100px">
                        <div>
                            <a ng-if="sharedItem.thumbnailImage != null" href=""
                                ng-click="allSharesVm.showThumbnailImage(sharedItem)"
                                title="{{clickToShowLargeImage}}">
                                <img ng-src="{{sharedItem.thumbnailImage}}"
                                    style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>
                            <a ng-if="sharedItem.thumbnailImage == null" href="">
                                <img src="app/assets/images/cassini-logo-greyscale.png" title="No thumbnail" alt=""
                                    class="no-thumbnail-preview">
                            </a>
                            &lt;%&ndash;
                            <div id="myModal234" class="img-model modal">
                                <span class="closeImage">&times;</span>
                                <img class="modal-content" id="img134">
                            </div>&ndash;%&gt;

                            <div id="item-thumbnail{{sharedItem.id}}" class="item-thumbnail modal">
                                <div class="item-thumbnail-content">
                                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                                        <div class="thumbnail-view" id="thumbnail-view{{sharedItem.id}}">
                                            <div id="thumbnail-image{{sharedItem.id}}"
                                                style="display: table-cell;vertical-align: middle;text-align: center;">
                                                <img ng-src="{{sharedItem.thumbnailImage}}"
                                                    style="height: auto;width: auto;max-width: 100%;max-height: 90%" />
                                                <span class="thumbnail-close"
                                                    id="thumbnail-close{{sharedItem.id}}"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                        </td>--%>

                    <td style="width: 150px">
                        <span>{{sharedItem.objectIdObject.latestRevisionObject.releasedDate}}</span>
                    </td>
                    <td>
                        <div class="label" style="text-align: center;"
                             ng-class="{'label-info': sharedItem.permission === 'READ','label-success': sharedItem.permission === 'WRITE'}">
                            {{sharedItem.permission}}
                        </div>
                    </td>
                    <td style="width: 100px; text-align: center;">{{sharedItem.shareType}}</td>
                    <td style="width: 100px">{{sharedItem.shareType == 'GROUP' ? sharedItem.sharedTo :
                        sharedItem.sharedTo}}
                    </td>
                    <td style="width: 100px">{{sharedItem.sharedBy}}</td>
                    <td style="width: 100px">{{sharedItem.sharedOn}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allSharesVm.sharedObjects" pageable="allSharesVm.pageable"
                          previous-page="allSharesVm.previousPage" next-page="allSharesVm.nextPage"
                          page-size="allSharesVm.pageSize"></table-footer>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-show="mfrView">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
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
                <tr ng-if="allSharesVm.loading == true">
                    <td colspan="25">
                            <span style="font-size: 15px;">
                                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                                <span translate>LOADING_ITEMS</span>
                            </span>
                    </td>
                </tr>

                <tr ng-if="allSharesVm.loading == false && allSharesVm.sharedMfrs.content.length == 0">
                    <td colspan="25" translate>NO_SHARING_MFRS</td>
                </tr>

                <tr ng-repeat="mfr in allSharesVm.sharedMfrs.content">
                    <td style="width: 150px">
                        <a href="" ng-click="allSharesVm.showMfrDetails(mfr)"
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
            <table-footer objects="allSharesVm.sharedMfrs" pageable="allSharesVm.pageable"
                          previous-page="allSharesVm.previousPage" next-page="allSharesVm.nextPage"
                          page-size="allSharesVm.pageSize"></table-footer>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-show="mfrPartView">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
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
                <tr ng-if="allSharesVm.loading == true">
                    <td colspan="25">
                            <span style="font-size: 15px;">
                                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                                <span translate>LOADING_ITEMS</span>
                            </span>
                    </td>
                </tr>

                <tr ng-if="allSharesVm.loading == false && allSharesVm.sharedMfrParts.content.length == 0">
                    <td colspan="25" translate>NO_SHARING_MFR_PARTS</td>
                </tr>

                <tr ng-repeat="mfrPart in allSharesVm.sharedMfrParts.content">
                    <td style="width: 150px">
                        <a href="" ng-click="allSharesVm.showMfrPartDetails(mfrPart)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{mfrPart.number}}</a>
                    </td>
                    <td>{{mfrPart.mfr}}</td>
                    <td>{{mfrPart.type}}</td>
                    <td>{{mfrPart.name}}</td>
                    <td id="td">{{mfrPart.description}}</td>
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
            <table-footer objects="allSharesVm.sharedMfrParts" pageable="allSharesVm.pageable"
                          previous-page="allSharesVm.previousPage" next-page="allSharesVm.nextPage"
                          page-size="allSharesVm.pageSize"></table-footer>
        </div>
    </div>


    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-show="supplierView">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="description-column" translate>NAME</th>
                    <th style="width: 200px;" translate>TYPE</th>
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
                <tr ng-if="allSharesVm.loading == true">
                    <td colspan="25">
                            <span style="font-size: 15px;">
                                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                                <span translate>LOADING_ITEMS</span>
                            </span>
                    </td>
                </tr>

                <tr ng-if="allSharesVm.loading == false && allSharesVm.sharedSuppliers.content.length == 0">
                    <td colspan="25" translate>NO_SHARING_SUPPLIERS</td>
                </tr>

                <tr ng-repeat="supplier in allSharesVm.sharedSuppliers.content">
                    <td class="description-column">
                        <a href="" ng-click="allSharesVm.showSupplierDetails(supplier)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{supplier.name}}</a>
                    </td>
                    <td>{{supplier.type}}</td>
                    <td id="td">{{supplier.description}}</td>
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
            <table-footer objects="allSharesVm.sharedSuppliers" pageable="allSharesVm.pageable"
                          previous-page="allSharesVm.previousPage" next-page="allSharesVm.nextPage"
                          page-size="allSharesVm.pageSize"></table-footer>
        </div>
    </div>

    <%--  Folder View --%>


    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-show="folderView">


        <div class="responsive-table" style="padding: 10px;">

            <table id="itemFilesTable" class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="col-width-250 file-name sticky-col sticky-actions-col" style="z-index: 11;" translate>
                        TAB_FILES_FILE_NAME
                    </th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th style="text-align: center" translate>TAB_FILES_VERSION</th>
                    <th style="text-align: center"
                        translate>LIFECYCLE
                    </th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
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

                <tr id="{{$index}}" ng-show="allSharesVm.sharedFolders.content.length > 0"
                    ng-repeat="file in allSharesVm.sharedFolders.content">

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
                                <a href="" ng-click="downloadFile(file.fileDto)"
                                   title="{{downloadFileTitle}}">
                                    <span ng-if="file.fileDto.fileType == 'FILE'"
                                          ng-bind-html="file.fileDto.name"></span>
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
                           <item-status
                                   item="file.fileDto"></item-status>
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
                    <td style="width: 100px">{{file.sharedBy}}</td>
                    <td style="width: 100px">{{file.sharedOn}}</td>
                </tr>
                </tbody>
            </table>


        </div>

        <div class="table-footer">
            <table-footer objects="allSharesVm.sharedFolders" pageable="allSharesVm.pageable"
                          previous-page="allSharesVm.previousPage" next-page="allSharesVm.nextPage"
                          page-size="allSharesVm.pageSize"></table-footer>
        </div>
    </div>


    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-show="declarationView">
        <div class="responsive-table" style="padding: 10px;">
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
                <tr ng-if="allSharesVm.loading == true">
                    <td colspan="25">
                            <span style="font-size: 15px;">
                                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                                <span translate>LOADING_ITEMS</span>
                            </span>
                    </td>
                </tr>

                <tr ng-if="allSharesVm.loading == false && allSharesVm.sharedDeclarations.content.length == 0">
                    <td colspan="25" translate>NO_SHARING_MFR_PARTS</td>
                </tr>

                <tr ng-repeat="declaration in allSharesVm.sharedDeclarations.content">
                    <td class="col-width-150">
                        <a href="" ng-click="allSharesVm.showDeclaration(declaration)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{declaration.number}}</a>
                    </td>
                    <td>{{declaration.type}}</td>
                    <td class="col-width-150">{{declaration.name}}</td>
                    <td class="col-width-250">{{declaration.description}}</td>
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
            <table-footer objects="allSharesVm.sharedDeclarations" pageable="allSharesVm.pageable"
                          previous-page="allSharesVm.previousPage" next-page="allSharesVm.nextPage"
                          page-size="allSharesVm.pageSize"></table-footer>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow: hidden;" ng-show="projectView">
        <div class="row row-eq-height">
            <div class="col-sm-12" style="padding: 0px;">
                <div class="item-details-tabs">
                    <uib-tabset active="allSharesVm.active">
                        <uib-tab id="sharedProject1" heading="{{allSharesVm.projectHeading}}"
                                 active="allSharesVm.tabs.project.active" ng-show="allSharesVm.sharedCounts.project > 0"
                                 select="allSharesVm.selectTab(allSharesVm.tabs.project.id)">
                            <div ng-include="allSharesVm.tabs.project.template"></div>
                        </uib-tab>
                        <uib-tab id="sharedActivity1" heading="{{allSharesVm.activityHeading}}"
                                 active="allSharesVm.tabs.activity.active"
                                 ng-show="allSharesVm.sharedCounts.activity > 0"
                                 select="allSharesVm.selectTab(allSharesVm.tabs.activity.id)">
                            <div ng-include="allSharesVm.tabs.activity.template"></div>
                        </uib-tab>
                        <uib-tab id="sharedTask1" heading="{{allSharesVm.taskHeading}}"
                                 active="allSharesVm.tabs.task.active" ng-show="allSharesVm.sharedCounts.task > 0"
                                 select="allSharesVm.selectTab(allSharesVm.tabs.task.id)">
                            <div ng-include="allSharesVm.tabs.task.template"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-show="programsView">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
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
                <tr ng-if="allSharesVm.loading == true">
                    <td colspan="25">
                            <span style="font-size: 15px;">
                                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                                <span translate>LOADING_ITEMS</span>
                            </span>
                    </td>
                </tr>

                <tr ng-if="allSharesVm.loading == false && allSharesVm.sharedPrograms.content.length == 0">
                    <td colspan="25" translate>NO_SHARING_PROGRAMS</td>
                </tr>

                <tr ng-repeat="program in allSharesVm.sharedPrograms.content">
                    <td style="width: 150px">
                        <a href="" ng-click="allSharesVm.showProgramDetails(program)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                <span
                                        ng-bind-html="program.name  | highlightText: allSharesVm.sharedObjectFilter.searchQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                            <span
                                    ng-bind-html="program.type  | highlightText: allSharesVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td class="col-width-200">
                            <span
                                    ng-bind-html="program.description  | highlightText: allSharesVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td class="col-width-150">
                        <span
                                ng-bind-html="program.manager  | highlightText: allSharesVm.sharedObjectFilter.searchQuery"></span>
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
            <table-footer objects="allSharesVm.sharedPrograms" pageable="allSharesVm.pageable"
                          previous-page="allSharesVm.previousPage" next-page="allSharesVm.nextPage"
                          page-size="allSharesVm.pageSize"></table-footer>
        </div>
    </div>
</div>
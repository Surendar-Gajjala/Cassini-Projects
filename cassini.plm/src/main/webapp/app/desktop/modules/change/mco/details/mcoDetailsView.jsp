<div class="view-container" fitcontent>
    <style>
        .item-details-tabs .tab-content {
            padding: 0 !important;
        }

        #freeTextSearchDirective {
            top: 7px !important;
        }

        .tab-content {
            padding: 0px !important;
        }

        .tab-content .tab-pane {
            overflow: auto !important;
        }

        .tab-pane {
            position: relative;
        }

        .tab-content .tab-pane .responsive-table {
            height: 100%;
            position: absolute;
            overflow: auto !important;
            padding: 5px;
        }

        .tab-content .tab-pane .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px !important;
            z-index: 5;
        }

        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -10px !important;
        }

    </style>
    <div class="view-toolbar">
        <!-- <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{mco.mcoNumber}}
            <span title="{{mco.title.length > 30 ? mco.title : ' '}}"> {{mco.title | limitTo:30}} {{mco.title.length > 30 ? '...' : ' '}}</span>
        </span> -->

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.changes.mco.all')"
                    ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

            <button ng-if="mcoDetailsVm.tabs.files.active && (hasPermission('change','dcr','edit') || hasPermission('change','edit')) && hasFiles == true"
                    title="{{downloadTitle}}"
                    class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                <i class="fa fa-download" aria-hidden="true" style=""></i>
            </button>
            <button class="btn btn-default btn-sm"
                    ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                    ng-click="copyChangeFilesToClipBoard()" title="{{copyFileToClipboard}}">
                <i class="fa fa-copy" style="font-size: 16px;"></i>
            </button>
            <div class="btn-group" ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="fa fa-copy" style="font-size: 16px;"></span><span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li ng-click="clearAndCopyChangeFilesToClipBoard()"><a href=""
                                                                           translate>CLEAR_AND_ADD_FILES</a>
                    </li>
                    <li ng-click="copyChangeFilesToClipBoard()"><a href=""><span translate>ADD_TO_EXISTING_FILES</span>
                        ({{clipBoardObjectFiles.length}})</a></li>
                </ul>
            </div>

            <button title="{{mcoDetailsVm.amlRedLine}}" ng-if="mcoDetailsVm.tabs.affectedItems.active"
                    class="btn btn-sm btn-success" ng-click="showAmlRedLine()">
                <i class="fa fa-exchange" aria-hidden="true" style=""></i>
            </button>

            <button ng-if="mcoDetailsVm.tabs.workflow.active"
                    ng-show="!mco.startWorkflow"
                    title="{{mcoDetailsVm.changeWorkflowTitle}}"
                    class="btn btn-sm btn-success" ng-click="mcoDetailsVm.changeWorkflow()">
                <i class="fa fa-indent" aria-hidden="true" style=""></i>
            </button>

            <button
                    title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                    class="btn btn-sm" ng-click="showPrintOptions(mcoDetailsVm.mcoId,'MCO')">
                <i class="fa fa-print" aria-hidden="true" style=""></i>
            </button>

        </div>
        <div class="pull-right">

        </div>

        <!-- <comments-btn ng-if="!mcoDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!mcoDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn> -->
        <free-text-search ng-if="mcoDetailsVm.tabs.files.active" on-clear="mcoDetailsVm.onClear"
                          on-search="mcoDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="mcoDetailsVm.active">
                        <uib-tab heading="{{mcoDetailsVm.tabs.basic.heading}}"
                                 active="mcoDetailsVm.tabs.basic.active"
                                 select="mcoDetailsVm.tabActivated(mcoDetailsVm.tabs.basic.id)">
                            <div ng-include="mcoDetailsVm.tabs.basic.template"
                                 ng-controller="MCOBasicInfoController as mcoBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{mcoDetailsVm.tabs.attributes.heading}}"
                                 active="mcoDetailsVm.tabs.attributes.active"
                                 select="mcoDetailsVm.tabActivated(mcoDetailsVm.tabs.attributes.id)">
                            <div ng-include="mcoDetailsVm.tabs.attributes.template"
                                 ng-controller="MCOAttributesController as mcoAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="affectedItems" heading="{{mcoDetailsVm.tabs.affectedItems.heading}}"
                                 active="mcoDetailsVm.tabs.affectedItems.active"
                                 select="mcoDetailsVm.tabActivated(mcoDetailsVm.tabs.affectedItems.id)">
                            <div ng-include="mcoDetailsVm.tabs.affectedItems.template"
                                 ng-controller="MCOAffectedItemsController as mcoAffectedItemsVm"></div>
                        </uib-tab>

                        <uib-tab id="relatedItems" heading="{{mcoDetailsVm.tabs.relatedItems.heading}}"
                                 ng-hide="mcoDetailsVm.mco.mcoType.mcoType == 'ITEMMCO'"
                                 active="mcoDetailsVm.tabs.relatedItems.active"
                                 select="mcoDetailsVm.tabActivated(mcoDetailsVm.tabs.relatedItems.id)">
                            <div ng-include="mcoDetailsVm.tabs.relatedItems.template"
                                 ng-controller="MCORelatedItemsController as mcoRelatedItemsVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{mcoDetailsVm.tabs.workflow.heading}}"
                                 active="mcoDetailsVm.tabs.workflow.active"
                                 select="mcoDetailsVm.tabActivated(mcoDetailsVm.tabs.workflow.id)">
                            <div ng-include="mcoDetailsVm.tabs.workflow.template"
                                 ng-controller="MCOWorkflowController as mcoWorkflowVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{mcoDetailsVm.tabs.files.heading}}"
                                 active="mcoDetailsVm.tabs.files.active"
                                 select="mcoDetailsVm.tabActivated(mcoDetailsVm.tabs.files.id)">
                            <div ng-include="mcoDetailsVm.tabs.files.template"
                                 ng-controller="MCOFilesController as mcoFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="ecoHistory" heading="{{mcoDetailsVm.tabs.timeLine.heading}}"
                                 active="mcoDetailsVm.tabs.timeLine.active"
                                 select="mcoDetailsVm.tabActivated(mcoDetailsVm.tabs.timeLine.id)">
                            <div ng-include="mcoDetailsVm.tabs.timeLine.template"
                                 ng-controller="MCOTimeLineController as mcoTimeLineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
<div>
    <style scoped>
        .mco-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 20px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .mco-model .mcoRollup-content {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .mco-header {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        .configuration-header {
            font-weight: bold;
            font-size: 22px;
            /*position: absolute;*/
            display: inline-block;
            /*left: 44%;*/
            margin-top: 7px;
        }

        /*.mco-content {
            padding: 10px;
            overflow: auto;
            min-width: 100%;
            width: auto;
        }*/

        .mco-content {
            width: 100%;
            position: relative;
            padding: 0 10px;
            overflow: auto;
        }

        .config-close {
            position: absolute;
            right: 35px;
            top: 25px;
            width: 38px;
            height: 38px;
            opacity: 0.3;
        }

        .config-close:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .config-close:before, .config-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .config-close:before {
            transform: rotate(45deg) !important;
        }

        .config-close:after {
            transform: rotate(-45deg) !important;
        }

        .mco-content table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
            background-color: #fff;
        }
    </style>
    <div id="mco-rollup" class="mco-model modal">
        <div class="mcoRollup-content">
            <div class="mco-header">
                <span class="configuration-header">
                    {{mco.mcoNumber}}- AMLRedLine
                </span>
                <a href="" ng-click="hideMcoPreview()" class="config-close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="mco-content">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr>
                        <th style="width: 150px" translate>PART_NUMBER</th>
                        <th style="width: 150px" translate>PART_NAME</th>
                        <th style="width: 150px" translate>MANUFACTURER</th>
                        <th style="width: 150px" translate>CHANGE_TYPE</th>
                        <th style="width: 150px" translate>REPLACED_PART_NUMBER</th>
                        <th style="width: 150px" translate>REPLACED_PART_NAME</th>
                        <th style="width: 150px" translate>MANUFACTURER</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="mcoDetailsVm.parts.length == 0">
                        <td colspan="25" translate>No Parts</td>
                    </tr>
                    <tr ng-repeat-start="part in mcoDetailsVm.parts" style="font-weight: bold">
                        <td style="width: 150px">
                            <i class="mr5 fa"
                               style="cursor: pointer;"
                               ng-class="{'fa-caret-right': (part.expanded == false || part.expanded == null || part.expanded == undefined),
                                              'fa-caret-down': part.expanded == true}"
                               ng-click="mcoDetailsVm.showAmlItems(part)"></i>

                            <a href="" ng-click="mcoDetailsVm.showMfrPartsDetails(part)"
                               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                {{part.materialNumber}}
                            </a>
                        </td>
                        <td style="width: 150px">{{part.materialName}}</td>
                        <td style="width: 150px">
                            {{part.manufacturer}}
                        </td>
                        <td style="width: 150px">
                            <mco-change-type type="part.changeType"></mco-change-type>
                        </td>
                        <td style="width: 150px">
                            {{part.replacementNumber}}
                        </td>
                        <td style="width: 150px">{{part.replacementName}}</td>
                        <td style="width: 150px">{{part.replaceMfr}}</td>
                    </tr>

                    <tr ng-if="part.expanded" ng-repeat-end="">
                        <td colspan="7">
                            <table style="width: 100%">
                                <tbody>
                                <tr ng-repeat="item in part.items">
                                    <td style="width: 150px">
                                        <span class="level1">{{item.itemNumber}}</span></td>
                                    <td style="width: 150px">{{item.itemName}}</td>
                                    <td style="width: 150px">{{item.rev.revision}}</td>
                                    <td style="width: 150px">
                                        <item-status item="item.rev"></item-status>
                                    </td>
                                    <td style="width: 150px"></td>
                                    <td style="width: 150px"></td>
                                    <td style="width: 150px"></td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

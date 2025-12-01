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
       <%-- <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{substance.number}}
            <span title="{{substance.name.length > 30 ? substance.name : ' '}}"> {{substance.name | limitTo:30}} {{substance.name.length > 30 ? '...' : ' '}}</span>
        </span>--%>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.compliance.substance.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>

        <%--<button ng-if="substanceDetailsVm.tabs.files.active && hasPermission('change','dcr','edit') && hasFiles == true"
                title="{{downloadTitle}}"
                class="btn btn-sm btn-success" >
          <i class="fa fa-download" aria-hidden="true" style=""></i>
        </button>--%>


        <button ng-if="substanceDetailsVm.tabs.files.active && hasFiles == true"
                title="{{downloadTitle}}"
                class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
            <i class="fa fa-download" aria-hidden="true" style=""></i>
        </button>
        <button class="btn btn-default btn-sm"
                ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
            <i class="fa fa-copy" style="font-size: 16px;"></i>
        </button>
        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm" ng-click="showPrintOptions(substanceDetailsVm.substance.id,'PGCSUBSTANCE')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
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

      <%--  <comments-btn ng-if="!substanceDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!substanceDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>--%>
        <free-text-search ng-if="substanceDetailsVm.tabs.files.active" on-clear="substanceDetailsVm.onClear"
                          on-search="substanceDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="substanceDetailsVm.active">
                        <uib-tab heading="{{substanceDetailsVm.tabs.basic.heading}}"
                                 active="substanceDetailsVm.tabs.basic.active"
                                 select="substanceDetailsVm.tabActivated(substanceDetailsVm.tabs.basic.id)">
                            <div ng-include="substanceDetailsVm.tabs.basic.template"
                                 ng-controller="SubstanceBasicInfoController as substanceBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{substanceDetailsVm.tabs.attributes.heading}}"
                                 active="substanceDetailsVm.tabs.attributes.active"
                                 select="substanceDetailsVm.tabActivated(substanceDetailsVm.tabs.attributes.id)">
                            <div ng-include="substanceDetailsVm.tabs.attributes.template"
                                 ng-controller="SubstanceAttributesController as substanceAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="files" heading="{{substanceDetailsVm.tabs.files.heading}}"
                                 active="substanceDetailsVm.tabs.files.active"
                                 select="substanceDetailsVm.tabActivated(substanceDetailsVm.tabs.files.id)">
                            <div ng-include="substanceDetailsVm.tabs.files.template"
                                 ng-controller="SubstanceFilesController as substanceFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="substanceDetailsVm.tabs" custom-tabs="substanceDetailsVm.customTabs"
                                     object-value="substanceDetailsVm.substance" tab-id="substanceDetailsVm.tabId" active="substanceDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{substanceDetailsVm.tabs.timelineHistory.heading}}"
                                 active="substanceDetailsVm.tabs.timelineHistory.active"
                                 select="substanceDetailsVm.tabActivated(substanceDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="substanceDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="SubstanceTimeLineController as substanceTimeLineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
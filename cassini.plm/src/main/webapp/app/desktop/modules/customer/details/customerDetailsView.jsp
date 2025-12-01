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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{customer.name}}</span>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.customers.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>

        <button ng-if="customerDetailsVm.tabs.files.active && hasFiles == true"
                title="{{downloadTitle}}"
                class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
            <i class="fa fa-download" aria-hidden="true" style=""></i>
        </button>
        <button class="btn btn-default btn-sm"
                ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
            <i class="fa fa-copy" style="font-size: 16px;"></i>
        </button>
        <div class="btn-group" ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
            <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
                <span class="fa fa-copy" style="font-size: 16px;"></span><span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <li ng-click="clearAndCopyObjectFilesToClipBoard()"><a href=""
                                                                       translate>CLEAR_AND_ADD_FILES</a>
                </li>
                <li ng-click="copyObjectFilesToClipBoard()"><a href=""><span translate>ADD_TO_EXISTING_FILES</span>
                    ({{clipBoardObjectFiles.length}})</a></li>
            </ul>
        </div>

        <comments-btn ng-if="!customerDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!customerDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="customerDetailsVm.tabs.files.active" on-clear="customerDetailsVm.onClear"
                          on-search="customerDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="customerDetailsVm.active">
                        <uib-tab heading="{{customerDetailsVm.tabs.basic.heading}}"
                                 active="customerDetailsVm.tabs.basic.active"
                                 select="customerDetailsVm.tabActivated(customerDetailsVm.tabs.basic.id)">
                            <div ng-include="customerDetailsVm.tabs.basic.template"
                                 ng-controller="CustomerBasicInfoController as customerBasicVm"></div>
                        </uib-tab>
                        <uib-tab id="prs" heading="{{customerDetailsVm.tabs.problemReport.heading}}"
                                 active="customerDetailsVm.tabs.problemReport.active"
                                 select="customerDetailsVm.tabActivated(customerDetailsVm.tabs.problemReport.id)">
                            <div ng-include="customerDetailsVm.tabs.problemReport.template"
                                 ng-controller="CustomerProblemReportController as customerPrVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{customerDetailsVm.tabs.files.heading}}"
                                 active="customerDetailsVm.tabs.files.active"
                                 select="customerDetailsVm.tabActivated(customerDetailsVm.tabs.files.id)">
                            <div ng-include="customerDetailsVm.tabs.files.template"
                                 ng-controller="CustomerFilesController as customerFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="customerDetailsVm.tabs" custom-tabs="customerDetailsVm.customTabs"
                                     object-value="customerDetailsVm.customer" tab-id="customerDetailsVm.tabId" active="customerDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{customerDetailsVm.tabs.timelineHistory.heading}}"
                                 active="customerDetailsVm.tabs.timelineHistory.active"
                                 select="customerDetailsVm.tabActivated(customerDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="customerDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="CustomerTimeLineController as customerTimeLineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
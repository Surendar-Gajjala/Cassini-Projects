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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{shift.number}}
            <span title="{{shift.name.length > 30 ? shift.name : ' '}}"> {{shift.name | limitTo:30}} {{shift.name.length > 30 ? '...' : ' '}}</span>
        </span>


        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.mes.masterData.shift.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>
        <button ng-if="shiftDetailsVm.tabs.workflow.active"
                ng-show="!shift.startWorkflow"
                class="btn btn-sm btn-success">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>

        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm" ng-click="showPrintOptions(shiftDetailsVm.shiftId,'SHIFT')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>
        <button ng-if="shiftDetailsVm.tabs.files.active && hasFiles == true"
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
        <comments-btn ng-if="!shiftDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!shiftDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn>
        <free-text-search ng-if="shiftDetailsVm.tabs.files.active" on-clear="shiftDetailsVm.onClear"
                          on-search="shiftDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="shiftDetailsVm.active">
                        <uib-tab heading="{{shiftDetailsVm.tabs.basic.heading}}"
                                 active="shiftDetailsVm.tabs.basic.active"
                                 select="shiftDetailsVm.tabActivated(shiftDetailsVm.tabs.basic.id)">
                            <div ng-include="shiftDetailsVm.tabs.basic.template"
                                 ng-controller="ShiftBasicInfoController as shiftBasicVm"></div>
                        </uib-tab>
                         <uib-tab id="persons" heading="{{shiftDetailsVm.tabs.persons.heading}}"
                                  active="shiftDetailsVm.tabs.persons.active"
                                  select="shiftDetailsVm.tabActivated(shiftDetailsVm.tabs.persons.id)">
                           <div ng-include="shiftDetailsVm.tabs.persons.template"
                                ng-controller="ShiftPersonsController as shiftPersonsVm"></div>
                         </uib-tab>
                        <uib-tab id="files" heading="{{shiftDetailsVm.tabs.files.heading}}"
                                 active="shiftDetailsVm.tabs.files.active"
                                 select="shiftDetailsVm.tabActivated(shiftDetailsVm.tabs.files.id)">
                            <div ng-include="shiftDetailsVm.tabs.files.template"
                                 ng-controller="ShiftFilesController as shiftFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="shiftDetailsVm.tabs" custom-tabs="shiftDetailsVm.customTabs"
                                     object-value="shiftDetailsVm.shift" tab-id="shiftDetailsVm.tabId" active="shiftDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{shiftDetailsVm.tabs.timelineHistory.heading}}"
                                 active="shiftDetailsVm.tabs.timelineHistory.active"
                                 select="shiftDetailsVm.tabActivated(shiftDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="shiftDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="ShiftTimeLineController as shiftTimeLineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>

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
        <!-- <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{declaration.number}}
            <span title="{{declaration.name.length > 30 ? declaration.name : ' '}}"> {{declaration.name | limitTo:30}} {{declaration.name.length > 30 ? '...' : ' '}}</span>
        </span> -->

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-if="!loginPersonDetails.external"
                    ng-click="showAll('app.compliance.declaration.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
            <button class="btn btn-sm btn-default" ng-if="loginPersonDetails.external"
                    ng-click="declarationDetailsVm.showExternalUserDeclarations()"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>

        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm" ng-click="showPrintOptions(declarationDetailsVm.declaration.id,'PGCDECLARATION')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="declarationDetailsVm.tabs.files.active && hasFiles == true"
                title="{{downloadTitle}}"
                class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
            <i class="fa fa-download" aria-hidden="true" style=""></i>
        </button>
        <button class="btn btn-default btn-sm"
                ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
            <i class="fa fa-copy" style="font-size: 16px;"></i>
        </button>

        <button ng-if="declaration.status == 'OPEN'"
                class="btn btn-sm btn-success"
                ng-disabled="declarationDetailsVm.tabCounts.parts == 0 || declarationDetailsVm.tabCounts.specifications == 0"
                ng-click="declarationDetailsVm.submitDeclaration()"
                title="{{declarationDetailsVm.tabCounts.parts == 0 || declarationDetailsVm.tabCounts.specifications == 0 ? 'Add parts and specifications' : 'Submit to supplier'}}"
                translate>SUBMIT
        </button>
        <button ng-if="declaration.status == 'SUBMITTED' && declaration.supplierContact == loginPersonDetails.person.id"
                class="btn btn-sm btn-success"
                ng-click="declarationDetailsVm.submitDeclaration()"
                title="Submit to Compliance Manager" translate>SUBMIT
        </button>
        <button ng-if="declaration.status == 'RECEIVED' && !loginPersonDetails.external"
                class="btn btn-sm btn-success"
                ng-click="declarationDetailsVm.generateReport()"
                translate>ACCEPT
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
        <!-- <comments-btn ng-if="!declarationDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>
        <tags-btn ng-if="!declarationDetailsVm.tabs.files.active" id="details-tag"
                  object-type="mainVm.tags.objectType"
                  object="mainVm.tags.object"
                  tags-count="mainVm.tags.tagsCount"></tags-btn> -->
        <free-text-search ng-if="declarationDetailsVm.tabs.files.active" on-clear="declarationDetailsVm.onClear"
                          on-search="declarationDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="declarationDetailsVm.active">
                        <uib-tab heading="{{declarationDetailsVm.tabs.basic.heading}}"
                                 active="declarationDetailsVm.tabs.basic.active"
                                 select="declarationDetailsVm.tabActivated(declarationDetailsVm.tabs.basic.id)">
                            <div ng-include="declarationDetailsVm.tabs.basic.template"
                                 ng-controller="DeclarationBasicInfoController as declarationBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{declarationDetailsVm.tabs.attributes.heading}}"
                                 active="declarationDetailsVm.tabs.attributes.active"
                                 select="declarationDetailsVm.tabActivated(declarationDetailsVm.tabs.attributes.id)">
                            <div ng-include="declarationDetailsVm.tabs.attributes.template"
                                 ng-controller="DeclarationAttributesController as declarationAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="declarationParts" heading="{{declarationDetailsVm.tabs.parts.heading}}"
                                 active="declarationDetailsVm.tabs.parts.active"
                                 select="declarationDetailsVm.tabActivated(declarationDetailsVm.tabs.parts.id)">
                            <div ng-include="declarationDetailsVm.tabs.parts.template"
                                 ng-controller="DeclarationMfrPartsController as declarationMfrPartsVm"></div>
                        </uib-tab>
                        <uib-tab id="declarationSpecifications"
                                 heading="{{declarationDetailsVm.tabs.specifications.heading}}"
                                 active="declarationDetailsVm.tabs.specifications.active"
                                 select="declarationDetailsVm.tabActivated(declarationDetailsVm.tabs.specifications.id)">
                            <div ng-include="declarationDetailsVm.tabs.specifications.template"
                                 ng-controller="DeclarationSpecificationsController as declarationSpecificationsVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{declarationDetailsVm.tabs.files.heading}}"
                                 active="declarationDetailsVm.tabs.files.active"
                                 select="declarationDetailsVm.tabActivated(declarationDetailsVm.tabs.files.id)">
                            <div ng-include="declarationDetailsVm.tabs.files.template"
                                 ng-controller="DeclarationFilesController as declarationFilesVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="declarationDetailsVm.tabs" custom-tabs="declarationDetailsVm.customTabs"
                                     object-value="declarationDetailsVm.declaration" tab-id="declarationDetailsVm.tabId" active="declarationDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{declarationDetailsVm.tabs.timelineHistory.heading}}"
                                 active="declarationDetailsVm.tabs.timelineHistory.active"
                                 select="declarationDetailsVm.tabActivated(declarationDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="declarationDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="DeclarationTimeLineController as declarationTimelineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>

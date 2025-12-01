<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    .fileContainer {
        overflow: hidden;
        position: relative;
    }

    .fileContainer [type=file] {
        cursor: inherit;
        display: block;
        font-size: 999px;
        filter: alpha(opacity=0);
        min-height: 100%;
        min-width: 100%;
        opacity: 0;
        position: absolute;
        right: 0;
        text-align: right;
        top: 0;
    }

    .fileContainer {
        /*background: #5B8F5B;*/
        border-radius: .3em;
        float: left;
        padding: .3em;
        text-align: center;
        color: #636e7b;
        height: 28px;
    }

    .fileContainer [type=file] {
        cursor: pointer;
    }

    #freeTextSearchDirective {
        /*margin-right: 60px !important;*/
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

    #freeTextSearchDirective {
        top: 8px !important;
    }

</style>
<div id="glossaryDetailsId" class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="showAll('app.rm.glossary.all')"
                    ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
            <button class="btn btn-sm btn-success" ng-click="glossaryDetailsVm.showRevisionHistory()"
                    title="{{'ITEM_DETAILS_REVISION_HISTORY' | translate}}">
                <i class="fa fa-history" aria-hidden="true" style=""></i>
            </button>
            <button ng-hide='selectedGlossary.latest == false'
                    ng-if='selectedGlossary.lifeCyclePhase.phaseType == "RELEASED" && hasPermission("permission.terminology.edit")'
                    title="{{glossaryDetailsVm.reviseTerminology}}"
                    class="btn btn-sm btn-success" ng-click="reviseGlossary()">
                <i class="fa fa-repeat"></i>
            </button>
            <button ng-hide='selectedGlossary.lifeCyclePhase.phaseType == "RELEASED"'
                    ng-if='hasPermission("permission.terminology.edit") || selectGlossaryPermission.statusChangePermission'
                    class="btn btn-sm btn-success" ng-click="promoteGlossary()"
                    title="{{glossaryDetailsVm.promoteTerminology}}">
                <i class="fa fa-toggle-right" style=""></i>
            </button>
            <button ng-hide='selectedGlossary.lifeCyclePhase.phaseType == "PRELIMINARY" || selectedGlossary.isReleased'
                    ng-if='hasPermission("permission.terminology.edit") || selectGlossaryPermission.statusChangePermission'
                    class="btn btn-sm btn-success" ng-click="demoteGlossary()"
                    title="{{glossaryDetailsVm.demoteTerminology}}">
                <i class="fa fa-toggle-left" style=""></i>
            </button>

            <%--   <button ng-if="glossaryDetailsVm.tabs.entries.active && hasPermission('terminology','edit')"
                       title="{{glossaryDetailsVm.addEntriesTitle}}"
                       ng-disabled='selectedGlossary.lifeCyclePhase.phaseType == "RELEASED"'
                       class="btn btn-sm btn-success min-width" ng-click="addGlossaryEntries()"
                       translate>
                   ADD_ENTRY
               </button>--%>


            <button class="btn btn-sm btn-success"
                    ng-if='hasPermission("permission.terminology.view") || selectGlossaryPermission.exportPermission'
                    title="{{glossaryDetailsVm.exportTerminologyTitle}}"
                    ng-click="exportGlossaryEntries()">
                <i class="fa fa-upload"></i>
            </button>
            <button class="btn btn-sm btn-success"
                    ng-if='hasPermission("permission.terminology.view") || selectGlossaryPermission.importPermission'
                    title="{{glossaryDetailsVm.importTerminologyTitle}}"
                    ng-disabled='selectedGlossary.lifeCyclePhase.phaseType == "RELEASED"'
                    ng-click="glossaryDetailsVm.importGlossaryEntryItemsDialog()">
                <i class="fa fa-download"></i>
            </button>
            <%--  <button ng-if="glossaryDetailsVm.tabs.files.active && hasPermission('project','edit')"
                      title="{{addFolder}}"
                      class="btn btn-sm btn-success" ng-click="addGlossaryFolder()">
                  <i class="fa fa-folder"></i>
              </button>--%>
            <button ng-if="glossaryDetailsVm.tabs.files.active && hasPermission('item','edit') && hasFiles == true"
                    class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()"
                    title="{{downloadTitle}}">
                <i class="fa fa-download"></i>
            </button>
            <%--<label ng-show="hasPermission('glossary','edit')" class="btn fileContainer"
                   style="margin-bottom: 0px !important;padding-top: 5px;"
                   ng-disabled='selectedGlossary.lifeCyclePhase.phaseType == "RELEASED"'
                   title="{{glossaryDetailsVm.importTerminologyTitle}}">Import
                <input type="file" id="glossaryFile" value="file" style="display: none"/>
            </label>--%>

            <button class="btn btn-sm btn-info" title="{{glossaryDetailsVm.printTerminologyTitle}}"
                    ng-click="printGlossary()">
                <i class="fa fa-print"></i>
            </button>

            <button class="btn btn-sm btn-maroon" ng-click="glossaryDetailsVm.showTypeAttributes()"
                    title="{{glossaryDetailsVm.entryAttributes}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>

            <button class="btn btn-default btn-sm"
                    ng-click="glossaryDetailsVm.refreshDetails()"
                    title="{{glossaryDetailsVm.refreshTitle}}">
                <i class="fa fa-refresh"></i>
            </button>
            <button class="btn btn-default btn-sm"
                    ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                    ng-click="copyObjectFilesToClipBoard()" title="Copy Files to Clipboard">
                <i class="fa fa-copy"></i>
            </button>
            <div class="btn-group" ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="fa fa-copy" style="font-size: 20px;"></span><span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li ng-click="clearAndCopyObjectFilesToClipBoard()"><a href="">Clear and Add Files</a></li>
                    <li ng-click="copyObjectFilesToClipBoard()"><a href="">Add to Existing Files</a></li>
                </ul>
            </div>
            <div class="btn-group" style="height:100%; width: auto;margin-top: 5px;margin-left: 10px;" uib-dropdown>
                <span style="font-size: 16px;" uib-dropdown-toggle><a href="">{{selectedGlossary.defaultDetail.language.language}}
                    <span class="caret" style="border-top-font-size: 14px;"></span> </a>
                </span>
                <ul class="uib-dropdown-menu dropdown-menu-usermenu pull-right"
                    style="overflow-y: auto;height: auto;width: auto"
                    uib-dropdown-menu>

                    <li style="padding: 0px;" ng-repeat="detail in selectedGlossaryLanguages" highlight-row>
                        <a href=""
                           ng-click="applyLanguage(detail)">{{detail.language.language}}</a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="pull-right">

            <free-text-search ng-if="glossaryDetailsVm.tabs.files.active" on-clear="glossaryDetailsVm.onClear"
                              on-search="glossaryDetailsVm.freeTextSearch"></free-text-search>

            <%--<button class="btn btn-default" ng-if="hasPermission('change','eco','edit')"
                    ng-click="glossaryDetailsVm.shareItem()"
                    style="border-radius:4px;"
                    title="{{glossaryDetailsVm.detailsShareTitle}}">
                <i class="fa fa-share-alt" style="font-size: 16px;margin-top:-6px;"></i></button>--%>

        </div>
        <free-text-search ng-if="glossaryDetailsVm.tabs.entries.active"
                          on-clear="glossaryDetailsVm.resetEntrySearch"
                          on-search="searchGlossaryEntryItems"
                          search-term="glossaryDetailsVm.entrySearchTerm">
        </free-text-search>
        <%--<div class="input-group input-group-sm mb15 pull-right" ng-if="glossaryDetailsVm.tabs.entries.active"
             style="margin-top: 2px;width: 400px;margin-right: 20px;position: absolute;top: 40px;right: 30px;">
                <span class="input-group-btn">
                    <button type="button" ng-click="glossaryDetailsVm.resetEntrySearch()"
                            class="btn btn-danger"
                            title="{{glossaryDetailsVm.clearSearchTitle}}"
                            style="height: 30px !important;">
                        <i class="fa fa-times-circle" style="font-size:16px"></i>
                    </button>
                </span>
            <input class="form-control" type="text" ng-change="glossaryDetailsVm.searchEntry()"
                   placeholder="{{glossaryDetailsVm.searchEntriesTitle}}"
                   ng-model="glossaryDetailsVm.entrySearchTerm">
                <span class="input-group-btn">
                    <button type="button" ng-click="glossaryDetailsVm.searchEntry()"
                            class="btn btn-primary"
                            title="{{glossaryDetailsVm.searchTitle}}"
                            style="height: 30px !important;">
                        <i class="fa fa-search" style="font-size:15px"></i>
                    </button>
                </span>
        </div>--%>
    </div>

    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="glossaryDetailsVm.active">
                        <uib-tab heading="{{glossaryDetailsVm.tabs.basic.heading}}"
                                 active="glossaryDetailsVm.tabs.basic.active"
                                 select="glossaryDetailsVm.glossaryDetailsTabActivated(glossaryDetailsVm.tabs.basic.id)">
                            <div ng-include="'app/desktop/modules/rm/glossary/details/tabs/basic/glossaryBasicView.jsp'"
                                 ng-controller="GlossaryBasicController as glossaryBasicVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{'DETAILS_TAB_ATTRIBUTES' | translate}}"
                                 active="glossaryDetailsVm.tabs.attributes.active"
                                 select="glossaryDetailsVm.glossaryDetailsTabActivated(glossaryDetailsVm.tabs.attributes.id)">
                            <div ng-include="glossaryDetailsVm.tabs.attributes.template"
                                 ng-controller="GlossaryAttributesController as glossaryAttributesVm"></div>
                        </uib-tab>
                        <uib-tab id="files" heading="{{'DETAILS_TAB_FILES' | translate}}"
                                 active="glossaryDetailsVm.tabs.files.active"
                                 select="glossaryDetailsVm.glossaryDetailsTabActivated(glossaryDetailsVm.tabs.files.id)">
                            <div ng-include="glossaryDetailsVm.tabs.files.template"
                                 ng-controller="GlossaryFilesController as glossaryFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="entries" heading="{{glossaryDetailsVm.glossaryEntryTab}}"
                                 active="glossaryDetailsVm.tabs.entries.active"
                                 select="glossaryDetailsVm.glossaryDetailsTabActivated(glossaryDetailsVm.tabs.entries.id)">
                            <div ng-include="glossaryDetailsVm.tabs.entries.template"
                                 ng-controller="GlossaryEntryItemController as glossaryEntryItemVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{glossaryDetailsVm.tabs.permissions.heading}}"
                                 active="glossaryDetailsVm.tabs.permissions.active"
                                 select="glossaryDetailsVm.glossaryDetailsTabActivated(glossaryDetailsVm.tabs.permissions.id)">
                            <div ng-include="glossaryDetailsVm.tabs.permissions.template"
                                 ng-controller="GlossaryPermissionsController as glossaryPermissionVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>


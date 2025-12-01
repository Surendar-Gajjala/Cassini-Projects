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

        .pull-right-300 {
            margin-right: 300px !important;
        }
    </style>
    <div class="view-toolbar">
        <%--<span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{supplier.number}} {{supplier.name}}</span>--%>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-if="loginPersonDetails.external == false"
                    ng-click="showAll('app.mfr.supplier.all')"
                    title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
            <button class="btn btn-sm btn-default" ng-if="loginPersonDetails.external == true"
                    ng-click='supplierDetailsVm.showExternalUserSuppliers()' title="{{'SHOW_ALL' | translate}}">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>

        </div>
        <button ng-if="supplierDetailsVm.tabs.workflow.active"
                ng-show="!plant.startWorkflow"
                class="btn btn-sm btn-success">
            <i class="fa fa-indent" aria-hidden="true" style=""></i>
        </button>

        <button
                title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                class="btn btn-sm" ng-click="showPrintOptions(supplierDetailsVm.supplierId,'MFRSUPPLIER')">
            <i class="fa fa-print" aria-hidden="true" style=""></i>
        </button>

        <button ng-if="supplierDetailsVm.tabs.files.active && hasFiles == true"
                title="{{downloadTitle}}"
                class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
            <i class="fa fa-download" aria-hidden="true" style=""></i>
        </button>


        <button class="btn btn-sm btn-success"
                title="{{hasPermission('mfrsupplier','demote') ? 'Demote' : noPermission}}"
                ng-hide="supplier.lifeCyclePhase.phase == supplierDetailsVm.firstLifecyclePhase.phase || loginPersonDetails.external"
                ng-class="{'cursor-override': !hasPermission('mfrsupplier','demote')}"
                ng-click="demoteSupplier()">
            <i class="fa fa-toggle-left" ng-class="{'disabled': !hasPermission('mfrsupplier','demote')}"
               style=""></i>
        </button>
        <button class="btn btn-sm btn-success"
                title="{{hasPermission('mfrsupplier','promote') ? 'Promote' : noPermission}}"
                ng-if="supplier.lifeCyclePhase.phase != supplierDetailsVm.lastLifecyclePhase.phase && !loginPersonDetails.external"
                ng-class="{'cursor-override': !hasPermission('mfrsupplier','demote')}"
                ng-click="promoteSupplier()">
            <i class="fa fa-toggle-right" ng-class="{'disabled': !hasPermission('mfrsupplier','promote')}"
               style=""></i>
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

        <div class="pull-right" ng-class="{'pull-right-300':supplierDetailsVm.tabs.files.active == true}">
            <button class="btn btn-sm btn-default" title="{{subscribeButtonTitle}}"
                    ng-click="supplierDetailsVm.subscribeSupplier()" ng-if="!loginPersonDetails.external">
                <i ng-if="supplierDetailsVm.subscribe == null || (supplierDetailsVm.subscribe != null && !supplierDetailsVm.subscribe.subscribe)"
                   class="la la-bell"></i>
                <i ng-if="supplierDetailsVm.subscribe != null && supplierDetailsVm.subscribe.subscribe"
                   class="la la-bell-slash"
                   title="{{'UN_SUBSCRIBE_TITLE' | translate }}"></i></button>
            <button class="btn btn-sm btn-default" title="Share" ng-if="!loginPersonDetails.external"
                    ng-click="supplierDetailsVm.shareSupplier()"
                    title="{{supplierDetailsVm.detailsShareTitle}}">
                <i class="las la-share"></i></button>
            <button class="btn btn-sm btn-default" title="Page Refresh"
                    ng-click="supplierDetailsVm.refreshDetails()"
                    style="margin-right: 10px;"
                    title="{{supplierDetailsVm.refreshTitle}}">
                <i class="fa fa-refresh"></i></button>
        </div>
        <%--<comments-btn ng-if="!supplierDetailsVm.tabs.files.active" id="details-comment"
                      object-type="mainVm.comments.objectType"
                      object-id="mainVm.comments.objectId"
                      comment-count="mainVm.comments.commentCount"></comments-btn>--%>
        <free-text-search ng-if="supplierDetailsVm.tabs.files.active" on-clear="supplierDetailsVm.onClear"
                          on-search="supplierDetailsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="supplierDetailsVm.active">
                        <uib-tab heading="{{supplierDetailsVm.tabs.basic.heading}}"
                                 active="supplierDetailsVm.tabs.basic.active"
                                 select="supplierDetailsVm.tabActivated(supplierDetailsVm.tabs.basic.id)">
                            <div ng-include="supplierDetailsVm.tabs.basic.template"
                                 ng-controller="SupplierBasicInfoController as supplierBasicVm"></div>
                        </uib-tab>
                        <%--<uib-tab heading="{{supplierDetailsVm.tabs.attributes.heading}}"
                                 active="supplierDetailsVm.tabs.attributes.active"
                                 select="supplierDetailsVm.tabActivated(supplierDetailsVm.tabs.attributes.id)">
                            <div ng-include="supplierDetailsVm.tabs.attributes.template"
                                 ng-controller="SupplierAttributesController as supplierAttributesVm"></div>
                        </uib-tab>--%>
                        <uib-tab id="contacts" heading="{{supplierDetailsVm.tabs.contacts.heading}}"
                                 active="supplierDetailsVm.tabs.contacts.active"
                                 select="supplierDetailsVm.tabActivated(supplierDetailsVm.tabs.contacts.id)">
                            <div ng-include="supplierDetailsVm.tabs.contacts.template"
                                 ng-controller="SupplierContactsController as supplierContactsVm"></div>
                        </uib-tab>

                        <uib-tab id="partsCount" heading="{{supplierDetailsVm.tabs.parts.heading}}"
                                 active="supplierDetailsVm.tabs.parts.active"
                                 select="supplierDetailsVm.tabActivated(supplierDetailsVm.tabs.parts.id)">
                            <div ng-include="supplierDetailsVm.tabs.parts.template"
                                 ng-controller="SupplierMfrPartsController as supplierMfrVm"></div>
                        </uib-tab>

                        <uib-tab id="files" heading="{{supplierDetailsVm.tabs.files.heading}}"
                                 active="supplierDetailsVm.tabs.files.active"
                                 select="supplierDetailsVm.tabActivated(supplierDetailsVm.tabs.files.id)">
                            <div ng-include="supplierDetailsVm.tabs.files.template"
                                 ng-controller="SupplierFilesController as supplierFilesVm"></div>
                        </uib-tab>
                        <uib-tab id="ppaps" heading="{{supplierDetailsVm.tabs.ppap.heading}}"
                                 active="supplierDetailsVm.tabs.ppap.active"
                                 select="supplierDetailsVm.tabActivated(supplierDetailsVm.tabs.ppap.id)">
                            <div ng-include="supplierDetailsVm.tabs.ppap.template"
                                 ng-controller="SupplierPPAPController as supplierppapVm"></div>
                        </uib-tab>
                        <uib-tab id="supplierAudit1" heading="{{supplierDetailsVm.tabs.supplierAudit.heading}}"
                                 active="supplierDetailsVm.tabs.supplierAudit.active"
                                 select="supplierDetailsVm.tabActivated(supplierDetailsVm.tabs.supplierAudit.id)">
                            <div ng-include="supplierDetailsVm.tabs.supplierAudit.template"
                                 ng-controller="SupplierAuditController as supplierAuditVm"></div>
                        </uib-tab>
                        <uib-tab id="supplierPerformanceRatings"
                                 heading="{{supplierDetailsVm.tabs.spr.heading}}"
                                 active="supplierDetailsVm.tabs.spr.active"
                                 select="supplierDetailsVm.tabActivated(supplierDetailsVm.tabs.spr.id)">
                            <div ng-include="supplierDetailsVm.tabs.spr.template"
                                 ng-controller="SupplierSPRController as supplierAuditVm"></div>
                        </uib-tab>
                        <uib-tab id="cpiForm"
                                heading="{{supplierDetailsVm.tabs.cpi.heading}}"
                                active="supplierDetailsVm.tabs.cpi.active"
                                select="supplierDetailsVm.tabActivated(supplierDetailsVm.tabs.cpi.id)">
                            <div ng-include="supplierDetailsVm.tabs.cpi.template"
                                    ng-controller="SupplierCPIController as supplierCpiVm"></div>
                        </uib-tab>
                        <uib-tab id="4mChangeSupplier"
                                 heading="{{supplierDetailsVm.tabs.fmChangeSupplier.heading}}"
                                 active="supplierDetailsVm.tabs.fmChangeSupplier.active"
                                 select="supplierDetailsVm.tabActivated(supplierDetailsVm.tabs.fmChangeSupplier.id)">
                            <div ng-include="supplierDetailsVm.tabs.fmChangeSupplier.template"
                                 ng-controller="Supplier4mChangeController as supplier4mChangeVm"></div>
                        </uib-tab>
                        <plugin-tabs tabs="supplierDetailsVm.tabs" custom-tabs="supplierDetailsVm.customTabs"
                                     object-value="supplierDetailsVm.supplier" tab-id="supplierDetailsVm.tabId"
                                     active="supplierDetailsVm.active"></plugin-tabs>
                        <uib-tab id="" heading="{{supplierDetailsVm.tabs.timelineHistory.heading}}"
                                 active="supplierDetailsVm.tabs.timelineHistory.active"
                                 select="supplierDetailsVm.tabActivated(supplierDetailsVm.tabs.timelineHistory.id)">
                            <div ng-include="supplierDetailsVm.tabs.timelineHistory.template"
                                 ng-controller="SupplierTimeLineController as supplierTimeLineVm"></div>
                        </uib-tab>

                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>

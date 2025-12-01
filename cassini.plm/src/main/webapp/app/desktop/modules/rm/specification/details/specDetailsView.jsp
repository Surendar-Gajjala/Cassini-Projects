<div>
    <style scoped>
        .item-number {
            display: inline-block;
        }

        .item-rev {
            font-size: 16px;
            font-weight: normal;
        }

        /*.nav-tabs {
            border: 0;
            border-bottom: 1px solid #d8dbde !important;
        }

        .tab-content {
            padding-top: 20px;
            border-bottom: 0;
            border-right: 0;
            border-left: 0;
        }*/

        .panels-container {
            margin-top: -4px;
            float: right;
            margin-right: 30px;
        }

        .panels-container span:first-child {
            border-radius: 3px 0 0 3px;
        }

        .panel-summary {
            height: 34px;
            margin: 3px -20px 0px 8px;
            display: inline-block;
            width: 200px;
            border-radius: 0 3px 3px 0;
            padding-left: 10px;
            line-height: 34px;
        }

        .panel-summary span:first-child {
            width: 150px;
            height: 34px;
        }

        .panel-summary span:first-child h2 {
            margin: 0;
            line-height: 34px;
            color: #fff;
            font-size: 17px;
            display: inline-block;
            padding-right: 10px;
            border-right: 1px solid #fff;
            width: 120px;
        }

        .panel-summary span:nth-child(2) h1 {
            margin: 0;
            line-height: 34px;
            color: #fff;
            font-size: 18px;
            display: inline-block;
            width: 60px;
        }

        .panel-summary h2,
        .panel-summary h1 {
            text-align: center;
        }

        .panel-summary h1 {
            font-size: 16px;
        }

        .panel-total {
            background: #005C97; /* fallback for old browsers */
            background: -webkit-linear-gradient(to left, #363795, #005C97); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to left, #363795, #005C97); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .panel-finish {
            background: #159957; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #159957, #155799); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #159957, #155799); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .panel-inprogress {
            background: #fdc830; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #fdc830, #f37335); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #fdc830, #f37335); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
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
            top: 7px !important;
        }
    </style>

    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <div class="btn-group">
                <button class="btn btn-sm btn-default" ng-click="showAll('app.rm.specifications.all')"
                        ng-if="loginPersonDetails.external == false" title="{{'SHOW_ALL' | translate}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>

                <!--
                <button class="btn btn-sm btn-default min-width" ng-if="specDetailsVm.tabs.items.active"
                        ng-click="createSection(null)" translate>
                    ADD_SECTION
                </button>
                -->


                <button class="btn btn-sm btn-maroon"
                        ng-if="selectedSpecification.totalReqs > 0 && specDetailsVm.tabs.items.active"
                        ng-click="showReqTypeAttributes()"
                        title="{{specDetailsVm.reqAttributesTitle}}">
                    <i class="fa fa-newspaper-o"></i>
                </button>


                <button class="btn btn-sm btn-success" ng-click="specDetailsVm.showRevisionHistory()"
                        title="{{specDetailsVm.specRevisionHistory}}">
                    <i class="fa fa-history" aria-hidden="true" style=""></i>
                </button>

                <button ng-hide='selectedSpecification.latest == false'
                        ng-if="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' && (hasPermission('admin','all') || hasPermission('pgcspecification','edit') || specPermission.statusChangePermission == true)"
                        class="btn btn-sm btn-success" ng-click="reviseSpecification()"
                        title="{{specDetailsVm.reviseItem}}">
                    <i class="fa fa-repeat"></i>
                </button>

                <button class="btn btn-sm btn-success"
                        ng-if="hasPermission('admin','all') || specPermission.statusChangePermission == true"
                        ng-hide='selectedSpecification.lifecyclePhase.phaseType == "PRELIMINARY" || selectedSpecification.status == "FINISHED" || selectedSpecification.lifecyclePhase.phaseType == "RELEASED" '
                        ng-click="demoteSpecification()"
                        title="{{specDetailsVm.demoteSpec}}">
                    <i class="fa fa-toggle-left" style=""></i>
                </button>

                <button class="btn btn-sm btn-success"
                        ng-if="hasPermission('admin','all') || specPermission.statusChangePermission == true"
                        ng-hide="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' || selectedSpecification.status == 'FINISHED' || selectedSpecification.totalReqs <= 0"
                        ng-click="promoteSpecification()"
                        title="{{specDetailsVm.promoteSpec}}">
                    <i class="fa fa-toggle-right" style=""></i>
                </button>

                <div class="btn-group" ng-if="specDetailsVm.tabs.items.active">
                    <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                            ng-if="hasPermission('admin','all') || specPermission.exportPermission == true"
                            aria-haspopup="true" aria-expanded="false" title="{{'EXPORT' | translate}}">
                        <i class="fa fa-upload" style=""></i>
                        <%--<span class="mr5" translate>ALL_VIEW_EXPORT</span>--%><span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-click="specDetailsVm.exportSpecification('excel')"><a href="">Excel</a></li>
                        <li ng-click="specDetailsVm.exportSpecification('reqif')"><a href="">ReqIF</a></li>
                    </ul>
                </div>

                <div class="btn-group" ng-if="specDetailsVm.tabs.items.active">

                    <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                            ng-if="hasPermission('admin','all') || specPermission.importPermission == true"
                            aria-haspopup="true" aria-expanded="false" title="{{'IMPORT' | translate}}">
                        <i class="fa fa-download" style=""></i>
                        <%--<span class="mr5" translate>IMPORT</span>--%><span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li onclick="$('#fileExcel').click()"><a href="">Excel</a></li>
                        <li onclick="$('#fileReqIf').click();"><a href="">ReqIF</a></li>
                    </ul>


                    <input type="file" id="fileExcel" value="file"
                           onchange="angular.element(this).scope().importExcel()"
                           style="display: none">

                    <input type="file" id="fileReqIf" value="file"
                           onchange="angular.element(this).scope().importReqIF()"
                           style="display: none">


                </div>


                <button class="btn btn-default btn-sm" title="{{subscribeButtonTitle}}"
                        ng-click="specDetailsVm.subscribeSpecification(selectedSpecification)">
                    <i ng-if="specDetailsVm.subscribe == null || (specDetailsVm.subscribe != null && !specDetailsVm.subscribe.subscribe)"
                       class="la la-bell"></i>
                    <i ng-if="specDetailsVm.subscribe != null && specDetailsVm.subscribe.subscribe"
                       class="la la-bell-slash"></i>
                </button>

                <button class="btn btn-sm btn-success"
                        title="{{specDetailsVm.addWorkflowTitle}}"
                        ng-show="specDetailsVm.tabs.workflow.active && selectedSpecification.workflow == null && selectedSpecification.lifecyclePhase.phaseType != 'RELEASED'"
                        ng-click="specDetailsVm.addWorkflow()">
                    <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
                </button>
                <button ng-if="specDetailsVm.tabs.workflow.active"
                        ng-show="selectedSpecification.workflow != null && selectedSpecification.startWorkflow != true && selectedSpecification.lifecyclePhase.phaseType != 'RELEASED'"
                        title="{{specDetailsVm.changeWorkflowTitle}}"
                        class="btn btn-sm btn-success" ng-click="specDetailsVm.changeWorkflow()">
                    <i class="fa fa-indent" aria-hidden="true" style=""></i>
                </button>

                <%--   <button class="btn btn-sm btn-success"
                           ng-if="specDetailsVm.tabs.files.active"
                           title="{{addFolder}}"
                           ng-hide='selectedSpecification.status == "FINISHED" || selectedSpecification.lifecyclePhase.phaseType == "RELEASED" '
                           ng-click="addSpecFolder()">
                       <i class="fa fa-folder"></i>
                   </button>--%>
                <button ng-if="specDetailsVm.tabs.files.active && hasPermission('item','edit') && hasFiles == true"
                        title="{{downloadTitle}}"
                        class="btn btn-sm btn-success" ng-click="specDetailsVm.downloadFilesAsZip()">
                    <i class="fa fa-download"></i>
                </button>
                <button class="btn btn-default btn-sm"
                        ng-show="showCopySpecFilesToClipBoard && clipBoardSpecFiles.length == 0"
                        ng-click="copySpecFilesToClipBoard()" title="{{copyFileToClipboard}}">
                    <i class="fa fa-copy" style=""></i>
                </button>
                <div class="btn-group" ng-show="showCopySpecFilesToClipBoard && clipBoardSpecFiles.length > 0">
                    <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        <span class="fa fa-copy" style=""></span><span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-click="clearAndCopySpecFilesToClipBoard()"><a href="">Clear and Add Files</a></li>
                        <li ng-click="copySpecFilesToClipBoard()"><a href="">Add to Existing Files</a></li>
                    </ul>
                </div>

                <button class="btn btn-default btn-sm"
                        ng-if="showRequirementsCopyToClipBoard && clipBoardRequirements.length == 0"
                        ng-click="copyRequirementsToClipBoard()" title="Copy Requirement to Clipboard">
                    <i class="fa fa-copy" style="font-size: 20px;"></i>
                </button>
                <div class="btn-group"
                     ng-if="showRequirementsCopyToClipBoard && clipBoardRequirements.length > 0">
                    <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        <span class="fa fa-copy" style="font-size: 20px;"></span><span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-click="clearAndCopyRequirementsToClipBoard()"><a href="">Clear and Add
                            Requirements</a></li>
                        <li ng-click="copyRequirementsToClipBoard()"><a href="">Add to Existing Requirements</a>
                        </li>
                    </ul>
                </div>
            </div>
            <%--<free-text-search ng-if="specDetailsVm.tabs.files.active" on-clear="specDetailsVm.onClear"
                              on-search="specDetailsVm.freeTextSearch"></free-text-search>--%>
        <span class="panels-container">
            <span class="panel-summary panel-total">
                <span><h2 translate>TOTALREQUIREMENTS</h2></span>
                <span><h1>{{selectedSpecification.totalReqs}}</h1></span>
            </span>

            <span class="panel-summary panel-inprogress">
                <span><h2 translate>PENDING</h2></span>
                <span><h1>{{selectedSpecification.pending}}</h1></span>
            </span>

            <span class="panel-summary panel-finish">
                <span><h2 translate>FINISHED</h2></span>
                <span><h1>{{selectedSpecification.finished}}</h1></span>
            </span>
        </span>
        </div>
        <div class="view-content no-padding" style="overflow-y: hidden;padding: 10px;">
            <div class="row row-eq-height" style="margin: 0;">
                <div class="col-sm-12" style="padding: 10px;">
                    <div class="item-details-tabs">
                        <uib-tabset active="specDetailsVm.active">
                            <uib-tab heading="{{specDetailsVm.tabs.basic.heading}}"
                                     active="specDetailsVm.tabs.basic.active"
                                     select="specDetailsVm.specDetailsTabActivated(specDetailsVm.tabs.basic.id)">
                                <div ng-include="specDetailsVm.tabs.basic.template"
                                     ng-controller="SpecificationBasicInfoController as specBasicVm"></div>
                            </uib-tab>
                            <uib-tab heading="{{'DETAILS_TAB_ATTRIBUTES' | translate}}"
                                     active="specDetailsVm.tabs.attributes.active"
                                     select="specDetailsVm.specDetailsTabActivated(specDetailsVm.tabs.attributes.id)">
                                <div ng-include="specDetailsVm.tabs.attributes.template"
                                     ng-controller="SpecAttributesController as specAttributesVm"></div>
                            </uib-tab>

                            <uib-tab id="requirements-tab"
                                     heading="{{specDetailsVm.tabs.items.heading}}"
                                     active="specDetailsVm.tabs.items.active"
                                     select="specDetailsVm.specDetailsTabActivated(specDetailsVm.tabs.items.id)">
                                <div ng-include="specDetailsVm.tabs.items.template"
                                     ng-controller="SpecificationItemsController as specItemsVm"></div>
                            </uib-tab>


                            <%--  <uib-tab heading="{{specDetailsVm.tabs.items.heading}}" active="specDetailsVm.tabs.items.active"
                                       select="specDetailsVm.specDetailsTabActivated(specDetailsVm.tabs.items.id)">
                                  <div ng-include="specDetailsVm.tabs.items.template"
                                       ng-controller="SpecificationElementsController as specElementsVm"></div>
                              </uib-tab>--%>


                            <uib-tab id="files" heading="{{specDetailsVm.tabs.files.heading}}"
                                     active="specDetailsVm.tabs.files.active"
                                     select="specDetailsVm.specDetailsTabActivated(specDetailsVm.tabs.files.id)">
                                <div ng-include="specDetailsVm.tabs.files.template"
                                     ng-controller="SpecificationFilesController as specFilesVm"></div>
                            </uib-tab>
                            <uib-tab id="workflow"
                                     heading="{{specDetailsVm.tabs.workflow.heading}}"
                                     active="specDetailsVm.tabs.workflow.active"
                                     select="specDetailsVm.specDetailsTabActivated(specDetailsVm.tabs.workflow.id)">
                                <div ng-include="specDetailsVm.tabs.workflow.template"
                                     ng-controller="SpecWorkflowController as specWfVm"></div>
                            </uib-tab>
                            <uib-tab heading="{{specDetailsVm.tabs.permissions.heading}}"
                                     active="specDetailsVm.tabs.permissions.active"
                                     select="specDetailsVm.specDetailsTabActivated(specDetailsVm.tabs.permissions.id)">
                                <div ng-include="specDetailsVm.tabs.permissions.template"
                                     ng-controller="PermissionsController as permissionsVm"></div>
                            </uib-tab>
                        </uib-tabset>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
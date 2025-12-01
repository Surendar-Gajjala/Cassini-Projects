<div ng-if="prBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_PR_DETAILS</span>
    </span>
    <br/>
</div>
<div ng-if="prBasicVm.loading == false">
    <div class="row row-eq-height">
        <div class="col-sm-12">
            <div class="item-details" style="padding: 30px">
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>PROBLEM_REPORT_TYPE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{prBasicVm.problemReport.prType.name}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>PR_NUMBER</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{prBasicVm.problemReport.prNumber}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>PRODUCT</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a ui-sref="app.items.details({itemId: prBasicVm.problemReport.product})"
                           title="{{clickToShowDetails}}">{{prBasicVm.problemReport.productObject.itemName}}</a>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>INSPECTION</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="" title="{{clickToShowDetails}}"
                           ui-sref="app.pqm.inspection.details({inspectionId: prBasicVm.problemReport.inspection})">{{prBasicVm.problemReport.inspectionObject.inspectionNumber}}</a>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>PROBLEM</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href=""
                           ng-if="hasPermission('problemreport','edit') && !problemReport.released && problemReport.statusType != 'REJECTED'"
                           onaftersave="prBasicVm.updateProblemReport()"
                           editable-textarea="prBasicVm.problemReport.problem">
                            <span ng-bind-html="(prBasicVm.problemReport.problem ) || 'ADD_DESCRIPTION' | translate"
                                  title="{{prBasicVm.problemReport.problem}}"></span>
                        </a>
                        <span ng-if="!hasPermission('problemreport','edit') || problemReport.released || problemReport.statusType == 'REJECTED'">
                            {{prBasicVm.problemReport.problemHtml}}
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>DESCRIPTION</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href=""
                           ng-if="hasPermission('problemreport','edit') && !problemReport.released && problemReport.statusType != 'REJECTED'"
                           onaftersave="prBasicVm.updateProblemReport()"
                           editable-textarea="prBasicVm.problemReport.description">
                            <span ng-bind-html="(prBasicVm.problemReport.description ) || 'ADD_DESCRIPTION' | translate"
                                  title="{{prBasicVm.problemReport.description}}"></span>
                        </a>
                        <span ng-if="!hasPermission('problemreport','edit') || problemReport.released || problemReport.statusType == 'REJECTED'">
                            {{prBasicVm.problemReport.descriptionHtml}}
                        </span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>STEPS_TO_REPRODUCE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href=""
                           ng-if="hasPermission('problemreport','edit') && !problemReport.released && problemReport.statusType != 'REJECTED'"
                           onaftersave="prBasicVm.updateProblemReport()"
                           editable-textarea="prBasicVm.problemReport.stepsToReproduce">
                            <span ng-bind-html=" (prBasicVm.problemReport.stepsToReproduce ) || 'ADD_STEPS_TO_REPRODUCE' | translate  "
                                  title="{{prBasicVm.problemReport.stepsToReproduce}}"></span>
                        </a>

                        <span ng-if="!hasPermission('problemreport','edit') || problemReport.released || problemReport.statusType == 'REJECTED'">
                            {{prBasicVm.problemReport.stepsToReproduceHtml}}
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>REPORTER_TYPE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <reporter-type reporter-type="prBasicVm.problemReport.reporterType"></reporter-type>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>REPORTED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span ng-if="prBasicVm.problemReport.reporterType == 'INTERNAL'">{{prBasicVm.problemReport.reportedByObject.fullName || prBasicVm.problemReport.otherReported}}</span>
                        <span ng-if="prBasicVm.problemReport.reporterType == 'CUSTOMER' || prBasicVm.problemReport.reporterType == 'SUPPLIER'">
                            {{prBasicVm.problemReport.reportedByObject.name || prBasicVm.problemReport.otherReported}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>QUALITY_ANALYST</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="#" e-style="width: 250px"
                           ng-if="hasPermission('admin','all') && !problemReport.released && problemReport.statusType != 'REJECTED'"
                           onaftersave="prBasicVm.updateProblemReport()"
                           editable-select="prBasicVm.problemReport.qualityAnalystObject"
                           title="{{prBasicVm.clickToUpdatePerson}}"
                           e-ng-options="person as person.fullName for person in prBasicVm.qualityAnalysts | orderBy:'fullName' track by person.id">
                            {{prBasicVm.problemReport.qualityAnalystObject.fullName}}
                        </a>
                        <span ng-if="!hasPermission('admin','all') || problemReport.released || problemReport.statusType == 'REJECTED'">
                            {{prBasicVm.problemReport.qualityAnalystObject.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>WORKFLOW_STATUS</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <workflow-status-settings workflow="prBasicVm.problemReport"></workflow-status-settings>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>DEFECT_TYPE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="#" e-style="width: 250px"
                           ng-if="hasPermission('admin','all') && !problemReport.released && problemReport.statusType != 'REJECTED'"
                           onaftersave="prBasicVm.updateProblemReport()"
                           editable-select="prBasicVm.problemReport.failureType"
                           e-ng-options="value for value in prBasicVm.problemReport.prType.failureTypes.values">
                            {{prBasicVm.problemReport.failureType}}
                        </a>
                        <span ng-if="!hasPermission('admin','all') || problemReport.released || problemReport.statusType == 'REJECTED'">
                            {{prBasicVm.problemReport.failureType}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>SEVERITY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="#" e-style="width: 250px"
                           ng-if="hasPermission('admin','all') && !problemReport.released && problemReport.statusType != 'REJECTED'"
                           onaftersave="prBasicVm.updateProblemReport()"
                           editable-select="prBasicVm.problemReport.severity"
                           e-ng-options="value for value in prBasicVm.problemReport.prType.severities.values">
                            {{prBasicVm.problemReport.severity}}
                        </a>
                        <span ng-if="!hasPermission('admin','all') || problemReport.released || problemReport.statusType == 'REJECTED'">
                            {{prBasicVm.problemReport.severity}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>DISPOSITION</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="#" e-style="width: 250px"
                           ng-if="hasPermission('admin','all') && !problemReport.released && problemReport.statusType != 'REJECTED'"
                           onaftersave="prBasicVm.updateProblemReport()"
                           editable-select="prBasicVm.problemReport.disposition"
                           e-ng-options="value for value in prBasicVm.problemReport.prType.dispositions.values">
                            {{prBasicVm.problemReport.disposition}}
                        </a>
                        <span ng-if="!hasPermission('admin','all') || problemReport.released || problemReport.statusType == 'REJECTED'">
                            {{prBasicVm.problemReport.disposition}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{prBasicVm.problemReport.createdByPerson.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{prBasicVm.problemReport.createdDate}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{prBasicVm.problemReport.modifiedByPerson.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{prBasicVm.problemReport.modifiedDate}}</span>
                    </div>
                </div>

                <basic-attribute-details-view object-type="QUALITY"
                                              quality-type="PROBLEMREPORT"
                                              has-permission="hasPermission('problemreport','edit') && !prBasicVm.problemReport.released && prBasicVm.problemReport.statusType != 'REJECTED'"
                                              object-id="problemReport.id"></basic-attribute-details-view>

                <object-attribute-details-view object-type="PRTYPE" show-attributes="true"
                                               has-permission="hasPermission('problemreport','edit') && !problemReport.released && problemReport.statusType != 'REJECTED'"
                                               object-type-id="problemReport.prType.id"
                                               object-id="problemReport.id"></object-attribute-details-view>
            </div>
        </div>
    </div>
</div>
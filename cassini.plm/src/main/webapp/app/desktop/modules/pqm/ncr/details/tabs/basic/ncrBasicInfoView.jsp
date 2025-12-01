<div ng-if="ncrBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_NCR_DETAILS</span>
    </span>
    <br/>
</div>
<div ng-if="ncrBasicVm.loading == false">
    <div class="row row-eq-height">
        <div class="col-sm-12">
            <div class="item-details" style="padding: 30px">
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>NCR_TYPE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{ncrBasicVm.ncr.ncrType.name}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>NCR_NUMBER</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{ncrBasicVm.ncr.ncrNumber}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>TITLE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href=""
                           ng-if="ncr.statusType != 'REJECTED' && !ncr.released && hasPermission('ncr','edit')"
                           onaftersave="ncrBasicVm.updateNCR()"
                           editable-textarea="ncrBasicVm.ncr.title">
                            <span ng-bind-html="ncrBasicVm.ncr.titleHtml"></span>
                        </a>
                        <span ng-if="ncr.released || ncr.statusType == 'REJECTED' || !hasPermission('ncr','edit')">{{ncrBasicVm.ncr.titleHtml}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>INSPECTION</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="" title="{{clickToShowDetails}}"
                           ui-sref="app.pqm.inspection.details({inspectionId: ncrBasicVm.ncr.inspection})">{{ncrBasicVm.ncr.inspectionObject.inspectionNumber}}</a>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>DESCRIPTION</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href=""
                           ng-if="ncr.statusType != 'REJECTED' && !ncr.released && hasPermission('ncr','edit')"
                           onaftersave="ncrBasicVm.updateNCR()"
                           editable-textarea="ncrBasicVm.ncr.description">
                            <span ng-bind-html="(ncrBasicVm.ncr.description ) || 'ADD_DESCRIPTION' | translate"
                                  title="{{ncrBasicVm.ncr.description}}"></span>
                        </a>
                        <span ng-if="ncr.released || ncr.statusType == 'REJECTED' || !hasPermission('ncr','edit')">{{ncrBasicVm.ncr.descriptionHtml}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>REPORTED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{ncrBasicVm.ncr.reportedByObject.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>REPORTED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{ncrBasicVm.ncr.reportedDate}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>QUALITY_ANALYST</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="#" e-style="width: 250px"
                           ng-if="ncr.statusType != 'REJECTED' && !ncr.released && hasPermission('admin','all')"
                           onaftersave="ncrBasicVm.updateNCR()"
                           editable-select="ncrBasicVm.ncr.qualityAnalystObject"
                           title="{{ncrBasicVm.clickToUpdatePerson}}"
                           e-ng-options="person as person.fullName for person in ncrBasicVm.qualityAnalysts | orderBy:'fullName' track by person.id">
                            {{ncrBasicVm.ncr.qualityAnalystObject.fullName}}
                        </a>
                        <span ng-if="ncr.statusType == 'REJECTED' || ncr.released || !hasPermission('admin','all')">
                            {{ncrBasicVm.ncr.qualityAnalystObject.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>WORKFLOW_STATUS</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <workflow-status-settings workflow="ncrBasicVm.ncr"></workflow-status-settings>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>DEFECT_TYPE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{ncrBasicVm.ncr.failureType}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>SEVERITY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{ncrBasicVm.ncr.severity}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>DISPOSITION</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{ncrBasicVm.ncr.disposition}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{ncrBasicVm.ncr.createdByPerson.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{ncrBasicVm.ncr.createdDate}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{ncrBasicVm.ncr.modifiedByPerson.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{ncrBasicVm.ncr.modifiedDate}}</span>
                    </div>
                </div>

                <basic-attribute-details-view object-type="QUALITY"
                                              quality-type="NCR"
                                              has-permission="hasPermission('ncr','edit') && !ncrBasicVm.ncr.released && ncrBasicVm.ncr.statusType != 'REJECTED'"
                                              object-id="ncr.id"></basic-attribute-details-view>

                <object-attribute-details-view object-type="NCRTYPE" show-attributes="true"
                                               has-permission="hasPermission('ncr','edit') && !ncr.released && ncr.statusType != 'REJECTED'"
                                               object-type-id="ncr.ncrType.id"
                                               object-id="ncr.id"></object-attribute-details-view>
            </div>
        </div>
    </div>
</div>
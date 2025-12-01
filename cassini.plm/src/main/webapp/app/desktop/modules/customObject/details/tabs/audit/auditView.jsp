<div>
    <style scoped>
        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -10px !important;
        }

        .ui-select-bootstrap .ui-select-match-text span {
            vertical-align: bottom;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }

        .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
            position: absolute !important;
        }

        a.disabled {
            cursor: not-allowed !important;
            color: lightgrey;
        }

        .planDateContainer {
            margin-bottom: 0 !important;
        }

        .planDateContainer [type=text] {
            display: block;
            filter: alpha(opacity=0);
            opacity: 0;
            position: absolute;
            margin-top: -20px;
            margin-left: -25px;
            width: 65px;
            cursor: pointer;
        }

        #audit-plan {
            height: calc(100% - 40px);
            /*position: absolute;*/
            /*overflow: auto !important;*/
            padding: 5px;
            padding-top: 40px;
        }

        #audit-plan table tbody tr:last-child {
            border-bottom: 1px solid #ddd;
        }

        .action-buttons {
            height: 40px;
            padding: 5px;
            background: #f9fbfe;
            width: calc(100% - 23px);
            position: fixed;
        }
    </style>
    <div class="action-buttons">
        <button class="btn btn-xs btn-success" ng-click="auditVm.saveAudit()">Save</button>
        <button class="btn btn-xs btn-primary" ng-disabled="checkAllAreaApproved()">Print</button>
    </div>
    <div id="audit-plan">
        <table class='table'>
            <thead>
            <tr>
                <th rowspan="2" style="width: 1% !important;white-space: nowrap;" translate>Kind/Area</th>
                <th ng-repeat="month in auditVm.months" style="text-align: center;">{{month.label}}<br>

                </th>
                <th rowspan="2" class="col-width-100 sticky-col sticky-actions-col"
                    style="text-align: center;" translate>ACTIONS
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="auditVm.loading == true">
                <td colspan="100"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                       class="mr5"><span translate>Loading audits...</span>
                </td>
            </tr>
            <tr ng-if="auditVm.loading == false && auditVm.internalAudit.audits.length == 0">
                <td colspan="100"><span translate>No audits</span></td>
            </tr>
            <tr ng-repeat="audit in auditVm.internalAudit.audits">
                <td style="width: 1% !important;white-space: nowrap;">{{audit.name}}</td>
                <td ng-repeat="month in auditVm.months" class="col-width-200" style="border-left: 1px solid #ddd;">
                    <div ng-if="audit.editMode && audit.name != 'System Audit External' && audit.name != 'System Audit Internal'">
                        <ui-select multiple ng-model="auditVm.getProducts(audit,month).selected" theme="bootstrap"
                                   on-select="auditVm.selectProduct($item,audit,month)" style="width: 300px;"
                                   on-remove="auditVm.removeProduct($item,audit,month)">
                            <ui-select-match allow-clear=true placeholder="Select product">{{$item}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="product in auditVm.products track by $index">
                                <div ng-bind="product"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                    <div style="padding: 10px 0;"
                         ng-repeat="product in auditVm.getProducts(audit,month).products">
                        <div ng-if="audit.name != 'System Audit External' && audit.name != 'System Audit Internal'">
                            <span style="font-weight: bold">Product</span> : {{product.name}}
                        </div>
                        <div style="margin-top: 5px"
                             ng-if="audit.name == 'System Audit External' || audit.name == 'System Audit Internal'">
                            <span ng-if="!audit.editMode"><span style="font-weight: bold"></span>No. : {{product.number}}</span>
                            <input type="text" class="form-control" ng-model="product.number" ng-if="audit.editMode"
                                   placeholder="Number" style="width: 100%;"/>
                        </div>
                        <div style="margin-top: 5px">
                            <span ng-if="!audit.editMode"><span style="font-weight: bold">Auditor</span> : {{product.auditor}}</span>
                            <input type="text" class="form-control" ng-model="product.auditor" ng-if="audit.editMode"
                                   placeholder="Auditor" style="width: 100%;"/>
                        </div>
                        <div style="margin-top: 5px"
                             ng-hide="audit.name == 'System Audit External' || audit.name == 'System Audit Internal'">
                            <ui-select ng-model="product.status" theme="bootstrap"
                                       style="width:100%" ng-if="audit.editMode">
                                <ui-select-match placeholder="Select Status">{{$select.selected}}</ui-select-match>
                                <ui-select-choices
                                        repeat="status in auditVm.statuses | filter: $select.search">
                                    <div>{{status}}</div>
                                </ui-select-choices>
                            </ui-select>
                            <span ng-if="!audit.editMode"><span style="font-weight: bold">Status</span> :
                                <span class="label label-primary">{{product.status}}</span>
                            </span>
                        </div>
                    </div>
                </td>
                <td class="text-center sticky-col sticky-actions-col" style="width: 100px;">
                    <span class="btn-group" ng-if="audit.editMode == true" style="margin: 0">
                        <i title="{{'SAVE' | translate}}" style="cursor: pointer"
                           ng-click="auditVm.saveAudit(audit)"
                           class="la la-check">
                        </i>
                        <i title="{{'CANCEL' | translate}}" style="cursor: pointer"
                           ng-click="auditVm.cancelChanges(audit)"
                           class="la la-times">
                        </i>
                    </span>
                    <span class="row-menu" ng-if="audit.editMode == false || audit.editMode == undefined" uib-dropdown
                          style="min-width: 50px">
                        <span dropdown-append-to-body></span>
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;right: 42px;top: 15px;">
                                <li style="cursor: pointer" ng-click="auditVm.editAudit(audit)">
                                    <a translate>EDIT</a>
                                </li>
                            </ul>
                    </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div class="col-sm-12" style="padding-bottom: 100px;">
        <h5>Created</h5>

        <div style="display: flex">
            <div class="col-sm-3 form-group">
                <label class="col-sm-4 control-label" style="text-align: right;">
                    <span translate>Date</span> : </label>

                <div class="col-sm-7">

                </div>
            </div>
            <div class="col-sm-3 form-group">
                <label class="col-sm-4 control-label" style="text-align: right;">
                    <span translate>Name</span> : </label>

                <div class="col-sm-7">

                </div>
            </div>
            <div class="col-sm-3 form-group">
                <label class="col-sm-4 control-label" style="text-align: right;">
                    <span translate>Signature</span> : </label>

                <div class="col-sm-7">
                    <img ng-src="{{auditVm.internalAudit.createdBySignature}}"
                         ng-if="auditVm.internalAudit.createdBySignature !=null && auditVm.internalAudit.createdBySignature !='' "
                         style="height:80px !important;width:180px !important;"/>
                    <input type="file" name="file" ng-disabled="checkAllAreaApproved()"
                           ng-model="auditVm.internalAudit.createdBySignature"
                           accept="image/*" app-filereader/>
                </div>
            </div>
        </div>

        <h5>Released</h5>

        <div>
            <div class="col-sm-3 form-group">
                <label class="col-sm-4 control-label" style="text-align: right;">
                    <span translate>Date</span> : </label>

                <div class="col-sm-7">
                    <input type="text" class="form-control" name="date"
                           placeholder="Select Date" date-picker
                           ng-model="auditVm.internalAudit.releasedDate"/>
                </div>
            </div>
            <div class="col-sm-3 form-group">
                <label class="col-sm-4 control-label" style="text-align: right;">
                    <span translate>Name</span> : </label>

                <div class="col-sm-7">
                    <input type="text" class="form-control" name="name"
                           placeholder="Enter Name"
                           ng-model="auditVm.internalAudit.name"/>
                </div>
            </div>
            <div class="col-sm-3 form-group">
                <label class="col-sm-4 control-label" style="text-align: right;">
                    <span translate>Signature</span> : </label>

                <div class="col-sm-7">
                    <img ng-src="{{auditVm.internalAudit.releasedBySignature}}"
                         ng-if="auditVm.internalAudit.releasedBySignature !=null && auditVm.internalAudit.releasedBySignature !='' "
                         style="height:80px !important;width:180px !important;"/>
                    <input type="file" name="file" ng-disabled="checkAllAreaApproved()"
                           ng-model="auditVm.internalAudit.releasedBySignature"
                           accept="image/*" app-filereader/>

                </div>
            </div>
        </div>
    </div>
</div>
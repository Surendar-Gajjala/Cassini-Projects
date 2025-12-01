<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 16-11-2018
  Time: 12:07
  To change this template use File | Settings | File Templates.
--%>
<style>
    .success {
        color: #3ccbbf;
    }

    .new {
        color: white;
        background: /*#0f12f3cc !important;*/ #3ccbbf !important;
    }

    .start {
        color: white;
        background: /*#0f12f3cc !important;*/ #3ccbbf !important;
    }

    .inprogress {
        color: white;
        background: #6be1f2 !important;
    }

    .completed {
        color: white;
        background: #68d3ab !important;
    }
</style>
<div class="row" style="margin: 0;padding: 20px;">
    <form class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-4 control-label">
                <span><b>Complaint Number:</b></span>
            </label>

            <div class="col-sm-6">
                <div class="input-group mb15">
                    <input type="text" class="form-control" name="title"
                           ng-model="detailsVm.complaint.complaintNumber" readOnly>
                </div>
            </div>
        </div>
        <div ng-if="detailsVm.complaint != null">
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <b>Complainant :</b>
                </label>

                <div class="col-sm-6" style="margin-top: 7px;">
                    <span <%--ng-bind="detailsVm.personTypeMap[detailsVm.complaint.personObject.personType].name"--%>>

                        {{detailsVm.personTypeMap[detailsVm.complaint.personObject.personType].name}}
                        </span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <b>Status :</b>
                </label>

                <div class="col-sm-6" style="margin-top: 7px;">
                    <div class="col-sm-4">
                                <span class="flag"
                                      ng-class="{'new':detailsVm.complaint.status == 'NEW',
                                         'inprogress':detailsVm.complaint.status == 'INPROGRESS',
                                         'completed':detailsVm.complaint.status == 'COMPLETED'}">{{detailsVm.complaint.status}}</span>
                    </div>
                    <div class="col-sm-2" style="margin-left: 50px;margin-top: -8px;">
                        <button class="btn btn-sm btn-success"
                                title="Start Complaint"
                                ng-if="detailsVm.complaint.status == 'NEW'"
                                ng-click="detailsVm.statusUpdate('start')">
                            <i <%--class="btn btn-sm btn-primary" style="font-size: 10px;  color: white; background: #6be1f2 !important; "--%>
                                     style="  font-style: normal">
                                   <%-- style="font-family: 'Ubuntu' !important;"--%>Start</i>
                        </button>
                        <button class="btn btn-sm btn-success" title="Resolved Complaint"
                                style="margin-left: 50px;margin-top: 0px;"
                                ng-if="detailsVm.complaint.status != 'NEW' && detailsVm.complaint.status != 'COMPLETED'"
                                ng-disabled="(detailsVm.complaint.status == 'FACILITATED' && personType.name == 'Facilitator') ||
                                             (detailsVm.complaint.status == 'ASSISTED' && personType.name == 'Assistor')"
                                ng-click="detailsVm.statusUpdate('resolved')">
                             <i <%--class="fa fa-check-square" style="font-size: 20px;"--%> style="  font-style: normal">Resolve</i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="form-group"
                 ng-if="(personType.name == 'Responder' || personType.name == 'Administrator') &&
                 (detailsVm.complaint.status == 'INPROGRESS' || detailsVm.complaint.status == 'AT_ASSISTOR')">
                <label class="col-sm-4 control-label">
                    <b>Request An Assistor:</b>
                </label>

                <div class="col-sm-6" style="margin-top: 7px;">
                    <ui-select class="required-field" ng-model="detailsVm.complaint.assistor"
                               ng-model-options="{debounce:1000}" ng-change="detailsVm.assignToAssistor()"
                               theme="bootstrap">
                        <ui-select-match placeholder="Select Assistor">{{$select.selected.fullName}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="assistor.id as assistor in detailsVm.assistors | filter: $select.search">
                            <div ng-bind="assistor.fullName | highlight: $select.fullName.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
            <div class="form-group" ng-if="(personType.name == 'Assistor'  || personType.name == 'Administrator') &&
            (detailsVm.complaint.status == 'AT_ASSISTOR' || detailsVm.complaint.status == 'AT_FACILITATOR')">
                <label class="col-sm-4 control-label">
                    <b>Escalate To Facilitator:</b>
                </label>

                <div class="col-sm-6" style="margin-top: 7px;">
                    <ui-select class="required-field" ng-model="detailsVm.complaint.facilitator"
                               ng-model-options="{debounce:1000}" ng-change="detailsVm.assignToFacilitator()"
                               theme="bootstrap">
                        <ui-select-match placeholder="Select Facilitator">{{$select.selected.fullName}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="facilitator.id as facilitator in detailsVm.facilitators | filter: $select.search">
                            <div ng-bind="facilitator.fullName | highlight: $select.fullName.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <b>Utility :</b>
                </label>

                <div class="col-sm-6" style="margin-top: 7px;">
                    <span>{{detailsVm.complaint.utility}}</span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <b>Location :</b>
                </label>

                <div class="col-sm-6" style="margin-top: 7px;">
                    <span>{{detailsVm.complaint.location}}</span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <b> Created Date :</b>
                </label>

                <div class="col-sm-6" style="margin-top: 7px;">
                    <span>{{detailsVm.complaint.createdDate}}</span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <b> Details :</b>
                </label>

                <div class="col-sm-6" style="margin-top: 7px;">
                    <span>{{detailsVm.complaint.details}}</span>
                </div>
            </div>
        </div>
    </form>
</div>
<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 20-11-2018
  Time: 17:46
  To change this template use File | Settings | File Templates.
--%>
<style>
    #td {
        display: run-in;
        word-wrap: break-word;
        width: 300px;
        white-space: normal;
    }

    .table thead > tr > th {
        background-color: #fff;
        color: #000000;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .panel.panel-default .panel-heading {
        background-color: #fff !important;
        padding: 0;
        height: 50px;
        border: 1px solid #dddddd;
    }

    #freeTextSearchDirective {
        top: 8px !important;
    }

    .new {
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

    .flag {
        position: relative;

        display: inline;

        background: rgb(173, 223, 229);

        padding: 6px 10px;

        border-radius: 5px;

        font-weight: 600;

        text-align: left;
    }
    .gatePass-notification {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }
    .gatePass-notification .badge1 {
        position: absolute;
        top: 0px;
        right: -38px;
        padding: 0px 6px;
        border-radius: 75%;
        background-color: #22a3e6;
        color: black;
        font-size: 17px;
    }
</style>
<div class="view-container" applicationfitcontent>
    <div class="view-toolbar">
        <button ng-if="loginPersonDetails.person.id == 1"
                class="btn btn-sm btn-success" ng-click="allCompsVm.newComplaint()">New Complaint
        </button>
        <button ng-if="loginPersonDetails.person.id == 1"
                class="btn btn-sm btn-success" ng-click="allCompsVm.exportReport()">Reports
        </button>
        <%-- <free-text-search  on-clear="allCompsVm.resetPage"
                           on-search="allCompsVm.freeTextSearch"></free-text-search>--%>
        <p class="blink" ng-if="allCompsVm.showSearchMode == true"
           style="color: blue;text-align: center;margin-top: -25px;font-size: 16px;">View is in search mode</p>
        <free-text-search on-clear="allCompsVm.resetPage"  search-term="allCompsVm.searchText"
                          on-search="allCompsVm.freeTextSearch"></free-text-search>



        <div class="search-element1 search-input-container inner-addon right-addon">
           <%-- <input type="search" style="border: 1px solid lightgrey;"
                   class="form-control input-sm search-form"
                   placeholder="Search"
                   onfocus="this.setSelectionRange(0, this.value.length)"
                   ng-model="allCompsVm.filters.searchQuery"
                   ng-model-options="{ debounce: 500 }"
                   ng-change="allCompsVm.loadProjects()">
            <a><i class="fa fa-search" ng-click="allCompsVm.searchReport()"></i></a>--%>
        </div>
        <div id="search-results-container" class="search-element1 search-results-container"
             ng-mouseover="selectedRow = null"
             style="overflow-y: auto;">
            <div ng-if="allCompsVm.complaints.length == 0" style="padding: 20px;">
                <h5>No results found</h5>
            </div>
            <div class="result-item" ng-repeat="complaint in allCompsVm.complaint.groupUtility"
                 ng-click="allCompsVm.openComplaintDetails(groupUtility)">
                <div class="result-item-row" ng-class="{'selected':$index == selectedRow}"
                     style="padding: 5px 10px;">
                    <span>{{groupUtility}}</span>
                </div>
            </div>
        </div>

    </div>


    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">
        <div class="panel panel-default panel-alt widget-messaging">
            <div class="panel-heading" style="">

                <div style="text-align: left;margin-top: 15px;margin-left: 15px;color: #002451;font-weight: bold;font-size: 20px;">

                    <a class="gatePass-notification">
                        <span class="badge1">{{allCompsVm.complaints.content.length}}</span>Complaints
                    </a>
                </div>

            </div>
                <div class="pull-right text-center" style="padding: 10px;margin-top: -35px;">
                    <div>
                            <span ng-if="allCompsVm.loading == false">
                                <medium>
                                    <span style="margin-right: 10px;">
                                        Displaying {{allCompsVm.complaints.numberOfElements}} of
                                            {{allCompsVm.complaints.totalElements}}
                                    </span>
                                </medium>
                            </span>
                        <span class="mr10">Page {{allCompsVm.complaints.totalElements != 0 ? allCompsVm.complaints.number+1:0}} of {{allCompsVm.complaints.totalPages}}</span>
                        <a href="" ng-click="allCompsVm.previousPage()"
                           ng-class="{'disabled': allCompsVm.complaints.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="allCompsVm.nextPage()"
                           ng-class="{'disabled': allCompsVm.complaints.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
            <div class="widget-panel" style="overflow-y: auto;min-height: 350px;width: 100%;">
                <div class="responsive-table" style="padding: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th>Complaint Number</th>
                            <th>Complainant</th>
                            <th>Status</th>
                            <th>Location</th>
                            <th>Group Utility</th>
                            <th>Utility</th>
                            <th>Created Date</th>
                            <th>Details</th>
                            <th style="width: 100px;text-align: center">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="allCompsVm.loading == true">
                            <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">Loading Complaints...
                            </span>
                            </td>
                        </tr>
                        <tr ng-if="allCompsVm.loading == false && allCompsVm.complaints.content.length == 0">
                            <td colspan="10" style="padding-left: 11px;">No Complaints....</td>
                        </tr>
                        <tr ng-repeat="complaint in allCompsVm.complaints.content">
                            <td><a href="" ng-click="allCompsVm.showComplaintDetails(complaint)">
                                    <span
                                            ng-bind-html="complaint.complaintNumber | highlightText: freeTextQuery"></span><%--{{complaint.complaintNumber}}--%>
                            </a>
                            </td>
                            <td>
                                    <span
                                            ng-bind-html="allCompsVm.personTypeMap[complaint.personObject.personType].name<%--+' - '+ complaint.personObject.traineeId || complaint.personObject.designation--%> | highlightText: freeTextQuery"></span></a>
                                <%--{{allCompsVm.personTypeMap[complaint.personObject.personType].name}} -
                                {{complaint.personObject.traineeId}}{{complaint.personObject.designation}}--%>
                            </td>
                            <td><%--<a href="" ng-click="allCompsVm.showComplaintDetails(complaint)">--%>
                                    <span class="flag"
                                          ng-class="{'new':complaint.status == 'NEW',
                                         'inprogress':complaint.status == 'INPROGRESS',
                                         'completed':complaint.status == 'COMPLETED'}"
                                          ng-bind-html="complaint.status | highlightText: freeTextQuery"></span><%--{{complaint.status}}--%>
                            </td>
                            <td><%--<a href="" ng-click="allCompsVm.showComplaintDetails(complaint)">--%>
                                    <span
                                            ng-bind-html="complaint.location | highlightText: freeTextQuery"></span><%--{{complaint.location}}--%>
                            </td>
                            <td> <span
                                    ng-bind-html="complaint.groupUtility | highlightText: freeTextQuery"></span><%--{{complaint.utility}}--%>
                            </td>

                            <td> <span
                                    ng-bind-html="complaint.utility | highlightText: freeTextQuery"></span><%--{{complaint.utility}}--%>
                            </td>
                            <td><span
                                    ng-bind-html="complaint.createdDate | highlightText: freeTextQuery"></span><%--{{complaint.createdDate}}--%>
                            </td>
                            <td> <span
                                    ng-bind-html="complaint.details.length > 30 ? complaint.details.trunc(30, true) : complaint.details | highlightText: freeTextQuery"
                                    title="{{complaint.details}}">
                                <%--{{complaint.details | limitTo: 30 }}{{complaint.details.length > 30 ? '...' : ''}}--%></span>
                            </td>
                            <td class="actions-col" style="text-align: center;">
                                <div class="btn-group">
                                    <button class="btn btn-xs btn-primary" title="Start Complaint"
                                            ng-if="complaint.status == 'NEW' && (personType.name == 'Responder' || loginPersonDetails.person.id == 1)"
                                            ng-click="allCompsVm.startComplaint(complaint)">
                                        <i class="fa fa-hourglass-start"></i>
                                    </button>
                                    <button class="btn btn-xs btn-success" title="Resolved Complaint"
                                            ng-if="complaint.status != 'NEW' && complaint.status != 'COMPLETED'"
                                            ng-disabled="(complaint.status == 'FACILITATED' && personType.name == 'Facilitator') ||
                                             (complaint.status == 'ASSISTED' && personType.name == 'Assistor')"
                                            ng-click="allCompsVm.resolvedComplaint(complaint)">
                                        <i class="fa fa-check-square"></i>
                                    </button>
                                    <button class="btn btn-xs btn-info" title="Show History"
                                            ng-click="allCompsVm.showComplaintHistory(complaint)">
                                        <i class="fa fa-history"></i>
                                    </button>
                                    <button class="btn btn-xs btn-danger" title="Delete Complaint"
                                            ng-if="loginPersonDetails.person.id == 1"
                                            ng-click="allCompsVm.deleteComplaint(complaint)">
                                        <i class="fa fa-trash"></i>
                                    </button>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


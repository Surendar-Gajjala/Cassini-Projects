<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 16-11-2018
  Time: 12:07
  To change this template use File | Settings | File Templates.
--%>
<div style="padding: 10px">
    <style scoped>
        .timeline {
            width: 250px;
        }

        .timeline::before {
            left: -80px;
        }

        .timeline .flag {
            cursor: pointer;
        }

        .timeline .time-wrapper {
            margin-top: -20px;
            margin-left: 85px;
            width: 330px;
        }

        .timeline .time {
            font-size: 12px;
        }

        .new {
            color: white;
            background:#3ccbbf !important;
        }

        .new::after {
            border: solid transparent;
            border-right-color:#3ccbbf !important;
            border-width: 6px;
        }

        .inprogress {
            color: white;
            background: #6be1f2 !important;
        }

        .inprogress::after {
            border: solid transparent;
            border-right-color: #6be1f2 !important;
            border-width: 6px;
        }

        .completed {
            color: white;
            background: #68d3ab !important;
        }

        .completed::after {
            border: solid transparent;
            border-right-color:#68d3ab !important;
            border-width: 6px;
        }


    </style>

    <div>
        <ul class="timeline">
            <li ng-repeat="history in compHistVm.complaintHistory">
                <div class="direction-r">
                    <div class="flag-wrapper" style="text-align: left !important;">
                        <span class="flag"
                              ng-class="{'new':history.newStatus == 'NEW',
                                         'inprogress':history.newStatus == 'INPROGRESS',
                                         'completed':history.newStatus == 'COMPLETED'}">{{history.newStatus}}</span>
                    </div>
                    <div class="desc" style="width: 450px;">
                        <span class="time"
                              style="font-size: 14px;">
                            <span style="font-style: normal">Timestamp</span> : {{history.modifiedDate}}</span><br>
                        <span class="time" style="font-size: 14px;">
                            <span style="font-style: normal;">Submitted By:
                             <span>
                                {{history.submittedByObject.fullName}}
                             </span>
                             </span>
                            <br>
                            <span style="font-style: normal;">Assigned To:
                             <span>
                                {{history.assignedToObject.fullName}}
                             </span>
                                </span>
                        </span>
                    </div>
                </div>
            </li>
            <%--<li ng-if="loginPersonDetails.person.id == 1">
                <div class="direction-r" ng-if="compHistVm.complaintHistory.length < 3">
                    <div class="flag-wrapper" style="text-align: left !important;">
                        <button class="btn btn-sm btn-primary" title="Start Complaint"
                                ng-if="compHistVm.complaintHistory.length == 1"
                                ng-click="compHistVm.statusUpdate()">
                            <i class="fa fa-hourglass-start" style="font-size: 20px;"></i>
                        </button>
                        <button class="btn btn-sm btn-success" title="Resolved Complaint"
                                ng-if="compHistVm.complaintHistory.length == 2"
                                ng-click="compHistVm.statusUpdate()">
                            <i class="fa fa-check-square" style="font-size: 20px;"></i>
                        </button>
                    </div>

                </div>
            </li>--%>
        </ul>
    </div>
</div>
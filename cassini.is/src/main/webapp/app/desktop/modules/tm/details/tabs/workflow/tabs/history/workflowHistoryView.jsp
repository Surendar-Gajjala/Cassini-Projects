<div>
    <div ng-if="workflowHistoryVm.workflowHistory.length == 0" style="padding: 20px;font-style: italic">
        <span>No History</span>
    </div>

    <ul class="timeline" style="margin-left: 100px;" ng-if="workflowHistoryVm.workflowHistory.length > 0">
        <li ng-repeat="history in workflowHistoryVm.workflowHistory">
            <div class="direction-r">
                <div style="position: relative;display: inline-block;text-align: left;">
                    <span class="flag" style="">{{history.statusObject.name}}</span>
                        <span class="time-wrapper">
                            <%--<span class="time">{{history.timestamp}}</span>--%>
                        </span>
                </div>
                <div class="desc">
                    <span style="font-size: 16px;font-style: normal;">
                        <span style="font-size: 14px;">{{history.timestamp}}</span>
                        <span style="color: #000;font-style: italic;font-size: 14px;">
                            ( {{history.statusObject.createdByObject.firstName}} )
                        </span>
                    </span>
                </div>
            </div>
        </li>
    </ul>
</div>
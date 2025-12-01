<div>
    <style scoped>
        .view-content .table-footer {
            padding: 0 10px 0 10px;
            position: absolute;
            bottom: 0px !important;
            height: 40px;
            width: 100%;
            border-top: 1px solid #D3D7DB;
            display: table;
        }

        .view-content .table-footer > div {
            display: table-row;
            line-height: 30px;
        }

        .view-content .table-footer > div h5 {
            margin: 0;
        }

        .view-content .table-footer > div > div {
            display: table-cell;
            vertical-align: middle;
        }

        .view-content .table-footer > div > div > i {
            font-size: 16px;
        }

        .view-content .responsive-table {
            padding: 10px;
            position: absolute;
            bottom: 40px;
            top: 52px;
            overflow: auto;
        }

        .view-content .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
            background-color: #fff;
        }
    </style>
    <div class="view-container" fitcontent>
        <div class="view-toolbar">

            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{feedbackVm.support}}</span>
            <button class="btn btn-sm btn-success" ng-click="feedbackVm.showNewFeedback()">
                <i class="fa fa-plus" style="" aria-hidden="true"></i>
            </button>
            <div style="position: absolute; top: 15px; left: 200px;">
                <input type="radio" name="ticketType"  ng-value="'open'" ng-model="feedbackVm.ticketType" ng-checked=true
                       ng-click="feedbackVm.getAllOpenTickets()"> <span>{{feedbackVm.open}}</span>
                <input type="radio" name="ticketType"  ng-value="'close'" ng-model="feedbackVm.ticketType"
                       ng-click="feedbackVm.getAllClosedTickets()"> <span>{{feedbackVm.closed}}</span>
            </div>
        </div>
        <div class="view-content no-padding" style="padding: 10px;">
            <div class="responsive-table" style="">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr>
                        <th>{{feedbackVm.number}}</th>
                        <th>{{feedbackVm.summary}}</th>
                        <th>{{feedbackVm.status}}</th>
                        <th>{{feedbackVm.createdDate}}</th>
                        <th>{{feedbackVm.updatedDate}}</th>
                        <th>{{feedbackVm.completedDate}}</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="feedbackVm.tickets.length == 0">
                        <td colspan="25" translate>NO_ENTRIES</td>
                    </tr>
                    <tr  ng-repeat="ticket in feedbackVm.tickets">
                        <td><span style="width: 50px;">{{ticket.number}}</span></td>
                        <td><span>{{ticket.summary}}</span></td>
                        <td><span style="width: 100px;">{{ticket.status}}</span></td>
                        <td><span style="width: 150px;">{{ticket.createdDate}}</span></td>
                        <td><span style="width: 150px;">{{ticket.updatedDate}}</span></td>
                        <td><span style="width: 150px;">{{ticket.completedDate}}</span></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="table-footer">
                <div>
                    <div class="text-right">
                        <a href="" ng-click="feedbackVm.previousPage()"
                           ng-class="{'disabled': feedbackVm.previous}"><i class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="feedbackVm.nextPage()"
                           ng-class="{'disabled': feedbackVm.next}"><i class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
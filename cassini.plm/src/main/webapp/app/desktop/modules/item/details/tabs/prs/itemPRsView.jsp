<div class='responsive-table'>
    <table class='table table-striped'>
        <thead>
        <tr>
            <th style=" width: 150px;">Number</th>
            <th style=" width: 150px;">Reported By</th>
            <th style=" width: 150px;text-align: center;">Status</th>
            <th>Title</th>
            <th>Description</th>
            <th style=" width: 150px;">Assigned To</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="itemPrsVm.loading == true">
            <td colspan="6"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading problem reports...
            </td>
        </tr>
        <tr ng-if="itemPrsVm.loading == false && itemPrsVm.prs.length == 0">
            <td colspan="6">No problem reports</td>
        </tr>
        <tr ng-repeat="pr in itemPrsVm.prs">
            <td style=" width: 150px;">
                <a href="" ng-click="itemPrsVm.showPrDetails(pr)">{{pr.number}}</a>
            </td>
            <td style=" width: 150px;">{{pr.reportedBy}}</td>
            <td style=" width: 150px;text-align: center;">{{pr.status}}</td>
            <td>{{pr.title.length > 50 ? pr.title.trunc(50,true) : pr.title}}</td>
            <td>{{pr.description.length > 50 ? pr.description.trunc(50,true) : pr.description}}</td>
            <td style=" width: 150px;">{{pr.assignedToObject.firstName}}</td>
        </tr>
        </tbody>
    </table>
</div>
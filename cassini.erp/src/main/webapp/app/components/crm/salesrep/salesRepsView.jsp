<script type="text/ng-template" id="salesreps-view-tb">
    <div>
        <button class="btn btn-sm btn-primary" ng-click="addSalesRep()" style="width: 80px">Add</button>
    </div>
</script>

<div class="row">
    <div class="col-md-12">
        <div style="text-align: right; margin-bottom: 20px;">
            <pagination total-items="salesReps.totalElements"
                        items-per-page="pageable.size"
                        max-size="5"
                        boundary-links="true"
                        ng-model="pageable.page"
                        ng-change="pageChanged()">
            </pagination>

            <div style="margin-top: -25px;">
                <small>Total {{salesReps.totalElements}} sales reps</small>
            </div>
        </div>

        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Name</th>
                    <th style="width: 150px">Date of Hire</th>
                    <th style="width: 150px">Status</th>
                    <th style="width: 150px">Office Phone</th>
                    <th style="width: 150px">Mobile Phone</th>
                    <th style="width: 150px">Email</th>
                </tr>
            </thead>
            <tbody>
                <tr ng-if="salesReps.totalElements == 0">
                    <td colspan="6">No sales reps</td>
                </tr>
                <tr ng-repeat="salesRep in salesReps.content">
                    <td><a href="" ng-click="showDetails(salesRep)">{{salesRep.firstName}}</a></td>
                    <td style="width: 150px">{{salesRep.dateOfHire}}</td>
                    <td style="width: 150px">{{salesRep.status}}</td>
                    <td style="width: 150px">{{salesRep.phoneOffice}}</td>
                    <td style="width: 150px">{{salesRep.phoneMobile}}</td>
                    <td style="width: 150px">{{salesRep.email}}</td>
                </tr>
            </tbody>
        </table>
    </div>
</div>
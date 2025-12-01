<script type="text/ng-template" id="salesregions-view-tb">
    <div>
        <button class="btn btn-sm btn-primary" ng-click="addSalesRegion()" style="width: 80px">Add</button>
    </div>
</script>
<div class="row">
    <div class="col-md-4" style="margin-top: 20px;">

    </div>
    <div class="col-md-8"  style="text-align: right" ng-show="pagedResults.numberOfElements > 0">
        <div style="text-align: right;">
            <pagination total-items="pagedResults.totalElements"
                        items-per-page="pageable.size"
                        max-size="5"
                        boundary-links="true"
                        ng-model="pageable.page"
                        ng-change="pageChanged()">
            </pagination>
        </div>

        <div style="margin-top: -25px;">
            <small>Total {{pagedResults.totalElements}} sales regions</small>
        </div>
    </div>
</div>
<table class="table table-striped table-responsive">
    <thead>
        <tr>
            <th style="width: 100px; text-align: center">Actions</th>
            <th>Name</th>
            <th>District</th>
            <!--<th>Country</th>-->
            <th>State</th>
        </tr>
    </thead>
    <tbody>
        <tr ng-show="showFilters" style="background-color: #D9EDF7;">
            <td style="width:100px; text-align: center; vertical-align: middle;">
                <div class="btn-group" style="margin-bottom: 0px;">
                    <table-filters-actions apply-filters="applyCriteria()" reset-filters="resetCriteria()"/>
                </div>
            </td>


            <td style="vertical-align: middle;">
                <table-filter placeholder="Name" filters="criteria" property="name" apply-filters="applyCriteria()" />
            </td>

            <td style="vertical-align: middle;">
                <table-filter placeholder="Name" filters="criteria" property="district" apply-filters="applyCriteria()" />
            </td>


            <td style="vertical-align: middle;"
                ng-class="{ hasFilter: (criteria.state != null && criteria.state != '') }">
                <ui-select ng-model="criteria.state" on-select="applyCriteria()" theme="bootstrap" style="width:200px">
                    <ui-select-match allow-clear="true"  placeholder="Select state">{{$select.selected.name}}</ui-select-match>
                    <ui-select-choices repeat="state in states | filter: $select.search">
                        <div ng-bind-html="state.name | highlight: $select.search"></div>
                    </ui-select-choices>
                </ui-select>
            </td>

        </tr>


        <tr ng-if="pagedResults.numberOfElements == 0">
            <td colspan="5">
                <span ng-hide="loading">No sales regions</span>
                <span ng-show="loading">
                    <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading sales regions...
                </span>
            </td>
        </tr>


        <tr ng-repeat="salesRegion in pagedResults.content">
            <td style="width:100px; text-align: center; vertical-align: middle;">
                <div class="btn-group" dropdown style="margin-bottom: 0px;" ng-hide="salesRegion.editMode">
                    <button type="button" class="btn btn-sm btn-default dropdown-toggle" dropdown-toggle>
                        <i class="fa fa-cog fa-fw"></i></span>
                    </button>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="" ng-click="showEditMode(salesRegion)">Edit</a></li>
                        <li><a href="" ng-click="removeItem($index);">Delete</a></li>
                    </ul>
                </div>


                <div class="btn-group" style="margin-bottom: 0px;" ng-show="salesRegion.editMode">
                    <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(salesRegion)"><i class="fa fa-check"></i></button>
                    <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(salesRegion);"><i class="fa fa-times"></i></button>
                </div>
            </td>

            <td style="vertical-align: middle;">
                <span ng-show="salesRegion.showValues">{{ salesRegion.name }}</span>
                <input placeholder="Enter name" class="form-control" type="text"
                       ng-show="salesRegion.editMode" ng-model="salesRegion.name">
            </td>

            <td style="vertical-align: middle;">
                <span ng-show="salesRegion.showValues">{{ salesRegion.district }}</span>
                <input placeholder="Enter district" class="form-control" type="text"
                       ng-show="salesRegion.editMode" ng-model="salesRegion.district">
            </td>

            <td style="vertical-align: middle;">
                <span ng-show="salesRegion.showValues">{{ salesRegion.state.name }}</span>
                <ui-select ng-show="salesRegion.editMode" ng-model="salesRegion.state" theme="bootstrap" style="width:200px">
                    <ui-select-match placeholder="Select state">{{$select.selected.name}}</ui-select-match>
                    <ui-select-choices repeat="state in salesRegion.countryAndStates.states | filter: $select.search">
                        <div ng-bind-html="state.name | highlight: $select.search"></div>
                    </ui-select-choices>
                </ui-select>
            </td>
        </tr>
    </tbody>
</table>
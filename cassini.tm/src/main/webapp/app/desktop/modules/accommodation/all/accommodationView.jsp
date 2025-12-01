<div class="view-container">
    <div class="view-toolbar">
        <div class="col-md-2" ng-if="hasRole('Administrator') == true || isAdmin() == true">
            <button class="btn btn-sm btn-success" ng-click="accommodationVm.createAccommodation();">Add Accommodation
            </button>
        </div>
        <h3 style="display: inline-flex; margin-top: inherit">Total: &nbsp<span class="label label-success"
                                                                                style="padding-top: 6px;"> Accommodations {{accommodationVm.accommodations.length}} </span>&nbsp
            <span class="label label-info" style="padding-top: 6px;">Suites {{accommodationVm.totalSuites}}  </span>&nbsp
            <span class="label label-warning" style="padding-top: 6px;">Beds {{accommodationVm.totalBeds}}</span></h3>
        <free-text-search on-clear="accommodationVm.resetPage"
                          on-search="accommodationVm.freeTextSearch"></free-text-search>
    </div>

    <div class="view-content">
        <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
            <div ng-repeat="accommodation in accommodationVm.accommodations">
                <div class="panel panel-default panel-alt col-xs-4" style="margin-top: 20px;">
                    <div class="panel-heading"
                         style="background-color: #E4E7EA; height: 6px; border: 1px solid #dddddd;">
                        <div class="row">
                            <button class="btn btn-xs btn-info" ng-click="accommodationVm.createSuit(accommodation);"
                                    style="margin-top: -22px;">Add Suite
                            </button>
                            <div class="panel-title" style="text-align: center; margin-top: -28px;"><a href=""
                                                                                                       ng-click="accommodationVm.showAccommodationDetails(accommodation)">{{accommodation.name}}</a>
                            </div>
                            <li class="pull-right fa fa-plus-circle" ng-show="!accommodation.showSuites"
                                style="cursor: pointer; margin-top: -16px;" ;
                                ng-click="accommodationVm.showAccSuits(accommodation)"></li>
                            <li class=" pull-right fa fa-minus-circle" ng-show="accommodation.showSuites"
                                style="cursor: pointer; margin-top: -16px;" ;
                                ng-click="accommodationVm.hideAccSuits(accommodation)"></li>
                        </div>
                    </div>
                    <div class="panel-body" style="overflow-y: auto;" ng-show="accommodation.showSuites">
                        <div class="row">
                            <div ng-repeat="suit in accommodation.suites">
                                <div class="panel panel-default panel-alt col-sm-12" style="margin-top: 20px;">
                                    <div class="panel-heading"
                                         style="background-color: #E4E7EA; height: 6px; border: 1px solid #dddddd;">
                                        <div class="row">
                                            <div class="panel-title" style="text-align: center; margin-top: -18px;">
                                                {{suit.name}}
                                            </div>
                                            <span style="margin-left: 3px;">Available : <a href=""
                                                                                           ng-click="accommodationVm.assignBed(suit, suit.available)">{{suit.available}}</a></span>
                                            <span style="margin-left: 3px;">Occupied : <a href=""
                                                                                            ng-click="accommodationVm.showBedAssignments(suit, suit.occupied)">{{suit.occupied}}</a></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>









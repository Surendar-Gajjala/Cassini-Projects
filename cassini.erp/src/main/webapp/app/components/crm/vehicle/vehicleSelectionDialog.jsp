<div class="modal-header"
     style="background: #E4E7EA none repeat scroll 0% 0%; border-bottom: 1px solid #CCC;">
    <h3 style="margin-top:0px; margin-bottom:0px;">Select Vehicle</h3>
</div>
<div class="modal-body" style="max-height:500px; overflow: auto;">
    <div class="row">
        <div class="col-md-12" style="padding:0px">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="width: 80px; text-align: center">
                        <input id="vehicleColumn" name="vehicleSelected" type="radio" value="" style="display:none">
                        <label for="vehicleColumn"></label>
                    </th>
                    <th style="">Vehicle Number</th>
                    <th style="">Description</th>
                </tr>
                </thead>
                <tbody>

                <tr ng-if="vehicles.length == 0">
                    <td colspan="4">
                        <span ng-hide="loading">No vehicles</span>
                            <span ng-show="loading">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading vehicles...
                            </span>
                    </td>
                </tr>


                <tr ng-repeat="vehicle in vehicles">
                    <td style="width:50px; text-align:center; vertical-align: middle;" align="center">
                        <div class="rdio rdio-default" style="margin-left: 25px;">
                            <input id="vehicle{{$index}}" name="vehicleSelected" ng-value="true" type="radio"
                                   ng-model="vehicle.selected" ng-click="selectVehicle(vehicle)">
                            <label for="vehicle{{$index}}"></label>
                        </div>
                    </td>

                    <td style="vertical-align: middle;">
                        {{ vehicle.number }}
                    </td>

                    <td style="vertical-align: middle;">
                        {{ vehicle.description }}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br/>
</div>
<div class="modal-footer" style="background-color: #F9F9F9;text-align: left;">
    <div class="row">
        <div class="col-md-6" style="text-align: left">
            <h4>Selected Vehicle: <span class="text-success">{{selectedVehicle.number}}</span></h4>
        </div>
        <div class="col-md-6" style="text-align: right">
            <button type="button" class="btn btn-default" style="min-width: 80px"
                    ng-click="cancel()">Cancel</button>
            <button class="btn btn-primary" style="min-width: 80px"
                    ng-click="ok()" ng-disabled="selectedVehicle == null">Select</button>
        </div>
    </div>
</div>
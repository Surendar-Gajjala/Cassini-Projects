<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 07-08-2018
  Time: 15:50
  To change this template use File | Settings | File Templates.
--%>
<div class="modal-header"
     style="background: #E4E7EA none repeat scroll 0% 0%; border-bottom: 1px solid #CCC;">
    <h3 style="margin-top:0px; margin-bottom:0px;">Select Materials</h3>
</div>
<div class="modal-body">
    <div class="row">
        <div class="col-md-12" style="text-align: center" ng-show="materials.numberOfElements > 0">
            <div>
                <pagination total-items="materials.totalElements"
                            items-per-page="20"
                            max-size="5"
                            boundary-links="true"
                            ng-model="pageable.page"
                            ng-change="loadMaterials()">
                </pagination>
            </div>

            <div style="margin-top: -25px;">
                <small>Total {{materials.totalElements}} materials</small>
            </div>
        </div>
    </div>

    <br/>

    <div class="row">
        <div class="col-md-12" style="padding:0px;max-height: 300px;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="width: 80px;">
                    </th>
                    <th style="">Name</th>
                    <th style="">Cost</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="materials.content.length == 0">
                    <td colspan="2">
                        <span ng-hide="loading">No materials</span>
                            <span ng-show="loading">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading materials...
                            </span>
                    </td>
                </tr>


                <tr ng-repeat="material in materials.content">
                    <td style="width:50px; text-align:center; vertical-align: middle;" align="center">
                        <div style="margin-left: 25px;">
                            <input id="material{{$index}}" ng-value="true" type="checkbox"
                                   ng-model="material.checked" ng-click="selectMaterial(material)">
                            <label for="material{{$index}}"></label>
                        </div>
                    </td>

                    <td style="vertical-align: middle;">
                        {{ material.name }}
                    </td>
                    <td style="vertical-align: middle;">
                        <input ng-show="material.checked" type="number" ng-model="material.cost" style="width: 80px"/>
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
        <div class="col-md-8" style="text-align: left">
            <h4>Selected Materials: <span class="text-success">{{selectedSupp.length}}</span></h4>
        </div>
        <div class="col-md-4" style="text-align: right">
            <button type="button" class="btn btn-default" style="min-width: 80px"
                    ng-click="cancel()">Cancel
            </button>
            <button class="btn btn-primary" style="min-width: 80px"
                    ng-click="ok()">Select
            </button>
        </div>
    </div>
</div>